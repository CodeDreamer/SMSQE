; DV3 Q68 SDHC Card Read Sector  1.05	 2016-2020 W. Lenerz

; 1.05	2020-05-06 added 16 bit read (wl)
; 1.04		   add hd_rdsect label (wl)
; 1.03	2018-05-09 use sysvars for card type
; 1.02	try longer at readloop and multiblock read (50 times, for slower cards)
; 1.01	multiblock read introduced (wl)


	section dv3

	xdef	hd_rdsect	       ; direct read sector
	xdef	hd_rdirect		; direct read sector
	xdef	hd_rsint		; internal read sector
	xdef	q68_dvstrt		; for copy2mem

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface
	xref	snd_cmd 		; send a command to sdhc
	xref	q68_dvend		; for copy2mem

	include 'dev8_keys_java'
	include 'DEV8_dv3_keys'
	include 'DEV8_keys_err'
	include 'DEV8_keys_sys'
	include 'DEV8_dv3_hd_keys'
	include 'dev8_keys_q68'

q68_dvstrt				; needed for copy2mem
	dc.w	hd_rdirect-*
	dc.w	q68_dvend-*+2

readreg reg	d1-d7/a1-a3

;+++
; This routine reads a sector from an sdhc card, normally for direct sector I/O.
;+++
hd_rdirect
	tst.w	ddf_ftype(a4)		; really direct sector I/O?
	bge.s	hd_rsint		; ... no ->...
hd_rdsect
	add.l	ddf_psoff(a4),d0	; ... yes, add "partition start"
	bra.s	hd_rsint

;+++
; This routine reads a sector from an sdhc card.  The sector number is that of
; the absolute sector on the SDHC card.
; All normal read accesses to the card go through here.
; Read is done via "background transfer".
;
;	d0 cr	absolute sector (=block) number  / error code
;	d1    p
;	d2 c  p number of sectors to read (1 = 1 sector, 2 = 2 sects etc)
;	d7 c  p drive ID / number  (from 1 to 8)
;	a1 c  p address to read into , MUST be large enough to hold the total nbr of sectors requested
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all other regs preserved
;
;	status return 0 or ERR.MCHK
;	The sector number is for sectors 512 bytes long. The interface needs
;	a sector (block) position starting from the very first sector.
;---
hdr_inus
	rts
hd_rsint
	jsr	hd_hold 		; hold the drive for me
	bne.s	hdr_inus		; can't
	movem.l readreg,-(a7)
	lea	hdl_unit-1(a3),a2	; point to "unit" = card for this drive
	clr.l	d4
	clr.l	d1
	move.b	(a2,d7.w),d1
	move.l	#mmc1_cs,a2		; point to card physical addresses
	add.w	d1,a2			; a2 points to the correct addresses for this drive

	divu	#q68.coff,d1
	add.w	#sys_q8ct,d1
	move.b	(a6,d1.w),d4		; get card type (and shift amount)
	blt	hdr_err 		; card isn't initialized

no_l	st	drv_cs(a2)		; enable the card
		  
;;;;;  debug code ;;;;; -------------------------------
	genif debug = 1
	cmp.l	#'gold',q68_jflg	; SMSQ68mulator?
	bne.s	nojava			; no...->
	move.l	d4,d1
	move.l	ddf_psoff(a4),d4
	move.l	d0,-(a7)		; will be popped up in java
	tst.w	d7
	bne.s	ok
	moveq	#1,d7
ok	moveq	#jd3.rsec,d0
	dc.w	jva.trpa		; call SMSQ68mulator
	bra   hdr_rts			; and continue ok
nojava
	endgen
;;;;; end debug code ;;;;;; ------------------------------

	move.l	d0,d7			; sector to get
	moveq	#17,d6			; single sector read
	subq.l	#1,d2			; go around d2 times
	beq.s	send_cmd
	moveq	#18,d6			: multiple sector read

; first send command17 ($51) or 18($52) to the card, to determine block to be read
; ($51, 4 bytes with the address to read from, $ff)

send_cmd
	moveq	#50,d5			; try this 51 times, for slower cards
rlp1	move.l	d7,d1			; sector nbr to read (0a0b0c0d)
	tst.w	d4			; is it sd card (need to shift)?
	beq.s	blk_add 		; ... no
	lsl.l	d4,d1			; ... yes, block * 512 =address
blk_add move.l	d6,d0			; command
	bsr	snd_cmd 		; send command to card
	bne	hdr_rts 		; ooops
	tst.b	d1			; return must be 0
	beq.s	drv_rdy 		; ... it is
	dbf	d5,rlp1 		; ... it is not, try again
	bne	hdr_err 		; finally give up

drv_rdy tst.b	q68_v2			; hardware v2?
	beq	eight_bit		; no->
	moveq	#2,d5

; wait for reply from card, should be datablock start token ($fe)
readloop
	moveq	#-2,d0			; datablock start token ($fe)
	moveq	#100,d3 		; outer loop counter
delay	move.w	#$fff8,d1		; inner loop counter
waitlp1 st	drv_xfer(a2)
	cmp.b	drv_read(a2),d0 	; get byte ; was it $fe ?
	dbeq	d1,waitlp1		; ... no, go round again
	beq.s	go_on			; ... yes, got correct reply->
	dbf	d3,delay		; no correct reply after inner loop done
	bra.s	hdr_err 		; if we get here, no correct reply, give up
				
;-----------------------------------
; got a correct token back from the card, it's ready for transfer
; now read (copy) 512 bytes
; the old way of copying this was : read / write in one instruction
; e.g.
;
;	 st	 (a3)			 ; send byte please
;	 move.b  (a2),(a1)+		 ; got it
; but curiously, this is slower than this:
; The new way of copying this : attempt to interleave read byte from card
; with write byte to mem: After signaling to the card that a new byte should be
; read ( => st (a3) ), it will take some time to actually get the byte from the
; card. The read attempt will then be blocked by the card until the byte is
; ready and during that time the CPU is "suspended", ie. nothing takes place.
; Since writing the byte gotten from the card to memory also takes some time,
; the idea is to interleave both operations.
; So the scheme is :
;  signal read readiness		: st (a3)
;  put previous byte got into memory	: move.b d0,(a1)+
;  get next byte from card		: move.b (a2),d0
;
;-----------------------------------


; "new" hardware, use 16 bit access

cpy16	macro
	move.w	(a2),d0 		; get  word
	move.b	d5,(a3) 		; prepare for next read already
	move.w	d0,(a1)+		; write word I just got
	endm

go_on	lea	drv_xfer(a2),a3 	; address to signal readiness to
	move.b	d5,(a3) 		; send word please
	add.w	#drv_read,a2		; where to read byte from
	move.w	#31,d3			; read loop is unrolled 8 times
getlp					; attempt to interleave read & writes
	cpy16
	cpy16
	cpy16
	cpy16
	cpy16
	cpy16
	cpy16
	cpy16
	dbf	d3,getlp		; get 32 * 16 bytes
	move.w	(a2),d0 		; forget the 2 CRC bytes
	sub.w	#drv_read,a2
	dbf	d2,readloop		;

hdr_cpe cmp.b	#18,d6			; was it multi-block?
	bne.s	hdr_ok			; ... no

	moveq	#12,d0			; ... yes, send cmd12 = stop reading
	moveq	#0,d1			; no parameter
	bsr	snd_cmd 		; send command to card

hdr_ok	moveq	#0,d0
hdr_rts clr.b	drv_cs(a2)
	tst.l	d0
	movem.l (a7)+,readreg
	jmp	hd_release		; will jump to hd_release & test d0
hdr_err
	moveq	#err.mchk,d0		; signal drive wasn't found
	bra.s	hdr_rts


; "old" hardware, use 8 bit access
; this is much the same as the 16 bits

cpy8	macro
	move.b	(a2),d0 		; get byte
	st	(a3)			; prepare for next read already
	move.b	d0,(a1)+
	endm

eight_bit
readlp8 				; wait for reply from card, should be datablock start token ($fe)
	moveq	#-2,d0			; datablock start token ($fe)
	moveq	#100,d3 		; outer loop counter
delay8	move.w	#$fff8,d1		; inner loop counter
waitlp2 st	drv_xfer(a2)
	cmp.b	drv_read(a2),d0 	; get byte ; was it $fe ?
	dbeq	d1,waitlp2		; ... no, go round again
	beq.s	go_on8			; ... yes, got correct reply->
	dbf	d3,delay8		; no correct reply after inner loop done
	bra.s	hdr_err 		; if we get here, no correct reply, give up

go_on8	lea	drv_xfer(a2),a3 	; address whither to signal readiness
	st	(a3)			; send byte please
	add.w	#drv_read,a2		; where to read byte from
	move.w	#63,d3			; read loop is unrolled 8 times
get8lp8 				; label can't be on same line as macro
	cpy8
	cpy8
	cpy8
	cpy8
	cpy8
	cpy8
	cpy8
	cpy8

	dbf	d3,get8lp8		  ; get 64 * 8 bytes

	move.b	(a2),d0
	st	(a3)
	move.b	(a2),d0 		; forget the 2 CRC bytes
	sub.w	#drv_read,a2
	dbf	d2,readlp8		; (still used if SD card not sdhc)

	bra	hdr_cpe 		; done

	end
