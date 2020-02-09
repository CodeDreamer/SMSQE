; QLSD Initilaise an SHDC card (via bit-banging)      1.00 (C) W. Lenerz 2017
;
; 2018-06-11  1.01  Take the QLSD hardware lock before initialising card (MK)

	section procs

	xdef	inicrd

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'

; algo :
; put into spi mode
; cmd0
; cmd8
; cmd58
; cmd55
; cmd58

inireg	reg	d1/d2/d3/d5/a2/a6

;+++
; This routine initialises an sdhc card
; *** read/write via bit bang
;
;	d5 c  p card number (1...3)
;	a3 c  p driver linkage block
;
;	status return 0, ERR.MCHK or ERR.FDIU
;---
inicrd	andi.w	#3,d5
	beq.s	err_ipar
	movem.l inireg,-(a7)
	move.l	qlsd_sysvars(a3),a6	; so caller doesn't have to provide it
	move.l	hdl_1sec(a3),d0 	; something like one second timeout
lock_wait
	bset	#7,sys_qlsd(a6) 	; prevent poll from accessing device
	beq.s	lock_ok 		; got the lock, proceed
	subq.l	#3,d0
	bgt.s	lock_wait
	movem.l (a7)+,inireg
	moveq	#err.fdiu,d0		; still in use, give up
	rts

err_ipar
	moveq	#err.ipar,d0
	rts

lock_ok
	move.w	#SPI_SELECT0,d3
	add.w	d5,d3
	move.l	#IF_BASE,a2		; point to card physical addresses

; set interface to a known state
	tst.b	IF_RESET(a2)		; reset the IF
	tst.b	IF_ENABLE(a2)		; enable it
	tst.b	SPI_SELECT0(a2) 	; deselect all
	tst.b	SPI_XFER_OFF(a2)	; use bitbang protocol

	move.w	sr,d0
	trap	#0
	move.w	d0,-(a7)

; now put I/F into SPI mode	       ; pretend to send many bytes, toggles clock
	moveq	#10,d2
inilp1	moveq	#-1,d0
	bsr	sendbyte
	dbf	d2,inilp1

	tst.b	(a2,d3.w)		; select the card

; send cmd0 101 times or until correct response
	moveq	#100,d2 		; nbr of loop iterations +1
inilp2	move.l	#$40000000,d0
	move.w	#$95,d1
	bsr	snd_cmd 		; send command 0 - put to idle
	bne.s	lp2dbf			; command sending went wrong ->
	subq.b	#1,d1			; sent OK, but correct response?
	beq.s	reply1			; ... yes ->
lp2dbf	dbf	d2,inilp2		; ... no, try again
	bra	ini_err 		; if we get here, no correct answer from card

; here I got a valid reply , so cmd0 was successful
; now try to send command8
reply1
	move.l	#$48000001,d0		; prepare cmd8
	move.w	#$aa87,d1
	bsr	snd_cmd 		; send command 8, return 1 + 4 more bytes
	bne	ini_err 		; ooos
	cmp.b	#1,d1			; correct response?
	bne	ini_err 		; no
	bsr	readbyte		; 2
	bsr	readbyte		; 3
	bsr	readbyte		; 4
	move.b	d0,d2			; keep 4th response byte
	bsr	readbyte		; 5
	cmp.b	#1,d2			; 4th response byte must be 1
	bne	ini_err 		; but it isn't
	cmp.b	#$aa,d0 		; 5th response byte must be $aa
	bne.s	ini_err 		; but is isn't

; here I got a correct reply from command8, now do command58
	move.l	#$7a000000,d0
	move.w	#$87,d1

	bsr	snd_cmd 		; send command 58, 1 + 4 more bytes
	bne.s	ini_err 		; oops on send command

	bsr.s	readbyte		; 2
	bsr.s	readbyte		; 3
	bsr.s	readbyte		; 4
	bsr.s	readbyte		; 5

; now do cmd55 followed by acmd41
	move.w	#$9fff,d2		; should be enough on a slow machine

inilp3	move.l	#$77000000,d0		; send command55 (special command to follow)
	move.w	#$87,d1
	bsr.s	snd_cmd
	bne.s	ini_err 		; pb on send command
	move.l	#$69400000,d0		; send acmd41 (sd_send_op_cond)
	move.w	#$87,d1
	bsr.s	snd_cmd 		; reply will be 0 if success
	bne.s	ini_err
	tst.b	d1			; check for correct reply from card
	beq.s	reply2			; got correct reply
	dbf	d2,inilp3		; didn't, so try again
	bra.s	ini_err 		; if we get here, reply never was 0
reply2
	move.l	#$7a000000,d0		; send command58 -read ocr
	move.w	#$87,d1
	bsr.s	snd_cmd 		;
	bne.s	ini_err
	bsr.s	readbyte		; 1 get 4 more response bytes
	move.b	d0,d2			; keep response 1
	bsr.s	readbyte		; 2
	bsr.s	readbyte		; 3
	bsr.s	readbyte		; 4
	andi.b	#$40,d2 		; is this an SDHC card?
	beq.s	ini_err 		; no! -->
	tst.b	SPI_XFER_FAST(a2)	; yes, all good, set fast commo mode
	moveq	#0,d0
ini_out move.w	(a7)+,sr
	tst.b	SPI_SELECT0(a2) 	; deselect all
ini_rts bclr	#7,sys_qlsd(a6) 	; release device
	movem.l (a7)+,inireg
	tst.l	d0
	rts
		
ini_err moveq	#err.mchk,d0
	bra.s	ini_out
	

;+++
; This routine reads a byte from an sdhc card through bit-banging.
; A byte is read bit by bit, the MSB comes first.
;
;      d0  r	byte read
;      d1    s
;      a2 c  p	pointer to interface base for the selected card
;
;      no error return
;+++

readbyte
	tst.b	SPI_SET_MOSI(a2)	; output high
	moveq	#7,d1			; dbf for 8 bits
	moveq	#0,d0			; d0 will contain result

rblp	tst.b	SPI_CLR_SCLK(a2)	; set clock low
	nop				; wait
	tst.b	SPI_READ(a2)		; got a bit  ?
	beq.s	nextbit 		; no
	bset	d1,d0			; ... yes, show it
nextbit
	tst.b	SPI_SET_SCLK(a2)	; set clock high
	dbf	d1,rblp
	rts

;+++
; This routine sends a command to an sdhc card. The command is 6 bytes long.
; Bytes are sent via bitbang.
; On entry d0 contains the first 4 bytes and d1.w the last two.
;
;	d0 cr	first 4 command bytes / error code
;	d1 cr	.W : 2 last command bytes / return byte
;	d3    s
;	a2 c  p pointer to interface base
;
;	status return 0 or ERR.MCHK (card didn't reply)
;---
; I could loop this, but it's just a few bytes anyway

snd_cmd
	move.w	d1,-(a7)		; sendbyte destroys d1
	move.l	d0,d3
	moveq	#-1,d0
	bsr.s	sendbyte		; (pretend to) send a byte (sends 8 clock pulses)
	move.l	d3,d0
	rol.l	#8,d0
	bsr.s	sendbyte		; send first byte
	rol.l	#8,d0
	bsr.s	sendbyte		; send next byte
	rol.l	#8,d0
	bsr.s	sendbyte		; send 3rd byte
	rol.l	#8,d0
	bsr.s	sendbyte		; send 4th byte
	move.w	(a7)+,d3
	move.w	d3,d0
	ror.w	#8,d0
	bsr.s	sendbyte		; penultimate byte
	move.b	d3,d0
	bsr.s	sendbyte		; last byte
; now wait for response
	move.w	#99,d3
waitlp	bsr.s	readbyte		; get response byte, myst be <>-1 if success
	cmp.b	#-1,d0			; was reply -1 (=bad)?
	bne.s	got_rply		; ...  no, so we're done ok
	dbf	d3,waitlp		; ...  yes, command not finished yet
	moveq	#err.mchk,d0		; if we get here, card didn't reply in time
	rts
got_rply
	move.b	d0,d1			; return byte
	moveq	#0,d0			; all OK
	rts


;+++++++++++++++++++++++
; This routine sends a byte to an sdhc card through bit-banging.
; A byte is sent bit by bit, the MSb comes first.
;
;      d0 c  p byte to send
;      d1    s
;      a2 c  p	pointer to interface base
;
;      no error return
;+++++++++++++++++++++++
sendbyte
	moveq	#7,d1			; dbf for 8 bits
sb_lp
	btst	d1,d0			; is this bit set?
	beq.s	not_set 		; ... no

bit_set tst.b	SPI_SET_MOSI(a2)	; output high
	bra.s	common
not_set tst.b	SPI_CLR_MOSI(a2)	; output low
common	tst.b	SPI_CLR_SCLK(a2)	; clock low
	nop				; wait
	tst.b	SPI_SET_SCLK(a2)	; clock high
	dbf	d1,sb_lp		; do for all bits
	rts

	end
