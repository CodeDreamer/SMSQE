; Prototpe HISTORY driver  V2.02     Tony Tebby
;
; 2005-12-06  2.02  Now keeps track of number of messages in history (MK)
;		    Returns number of messages as file length (MK)
;		    File pointer is limited to the number of messages (MK)
;		    Disallows empty strings (MK)

	section history

	xdef	history_io

	xref	gu_achpp
	xref	gu_rchp
	xref	history_name

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_k'
	include 'dev8_iod_history_data'
	include 'dev8_mac_assert'

;+++
; HISTORY IO routine
;
; In IO mode this deals with
;	IOB.FLIN	standard
;	IOB.FMUL	2 bytes (n) followed by n bytes
;	IOB.ELIN	d1=0
;	IOB.SBYT	end of message
;	IOB.SMUL	n bytes to add to buffer
;			(MSW D2=ST) n+2 bytes n followed by n bytes message
;
;	IOF.POSA	set position in file
;	IOF.POSR	move position in file
;	IOF.RHDR	read header
;
;	d0 cr	IO key / status
;	d1 cr	IO parameter
;	d2 c	IO parameter
;	d3   s
;	a1 cr	IO parameter
;	a3/a4	scratch
;	a6 c  p pointer to sysvar
;
;	status returns standard
;
;---
history_io
	move.l	hic_hist(a0),d3 	; directory?
	beq.l	hio_dir 		; ... yes
	move.l	d3,a2			; save history pointer

	cmp.b	#iob.smul,d0		; simple byte io?
	bhi.s	hio_posn		; ... no, try position
	add.w	d0,d0
	move.w	hio_btab(pc,d0.w),d0
	jmp	hio_btab(pc,d0.w)
hio_btab
	dc.w	hio_ipar-hio_btab
	dc.w	hio_ipar-hio_btab
	dc.w	hio_flin-hio_btab
	dc.w	hio_fmul-hio_btab
	dc.w	hio_elin-hio_btab
	dc.w	hio_sbyt-hio_btab
	dc.w	hio_ipar-hio_btab
	dc.w	hio_smul-hio_btab

hio_posn
	sub.b	#iof.posa,d0		 ; position?
	beq.s	hio_posa
	subq.b	#iof.posr-iof.posa,d0
	beq.s	hio_posr
	subq.b	#iof.rhdr-iof.posr,d0
	bne.s	hio_ipar

	moveq	#0,d0
	move.w	hid_cnt(a2),d0		 ; set msgs in history as file length
	move.l	d0,(a1)+
	bra	hdfm_sethd		 ; fill rest of header entry

hio_posr
	moveq	#0,d0			 ; default to "no error"
	neg.l	d1			 ; just enquire?
	beq.s	hps_ret
	tst.b	hic_msnv(a0)		 ; message number valid?
	beq.s	hps_posa		 ; it was at start
	add.l	hic_msnr(a0),d1 	 ; get this message next
	bra.s	hps_retcl		 ; clear message pointer and return

hio_posa
	moveq	#0,d0			 ; default to "no error"
	neg.l	d1			 ; to start?
	beq.s	hps_reset		 ; ... yes
hps_posa
	tas	hic_msnv(a0)		 ; message number valid

	move.l	hid_msnr(a2),d3
	move.l	d3,hic_msrf(a0) 	 ; new reference
	add.l	d3,d1			 ; position relative to most recent

hps_retcl
	clr.l	hic_mspt(a0)		 ; kill the message pointer
	move.l	d1,hic_msnr(a0)
hps_ret 				 ; restrict "file ptr" to valid range
	move.l	hic_msnr(a0),d1
	move.l	hid_msnr(a2),d2
	cmp.l	d2,d1			 ; must be lower or equal to reference!
	ble.s	hps_checklow
	move.l	d2,hic_msnr(a0) 	 ; upper bond!
	moveq	#err.eof,d0
	bra.s	hps_bondok
hps_checklow
	moveq	#0,d3
	move.w	hid_cnt(a2),d3		 ; messages in history
	sub.l	d3,d2			 ; now lower bond
	cmp.l	d2,d1
	bgt.s	hps_bondok
	move.l	d2,hic_msnr(a0) 	 ; restrict to lower bond!
	moveq	#err.eof,d0
hps_bondok
	moveq	#0,d1
	tst.b	hic_msnv(a0)		 ; at start?
	beq.s	hps_exit
	move.l	hid_msnr(a2),d1
	sub.l	hic_msnr(a0),d1
hps_exit
	tst.l	d0
	rts

hps_reset
	sf	hic_msnv(a0)		 ; reset to start
	bra.s	hps_exit

hio_ipar
	moveq	#err.ipar,d0
hio_rts
	rts

; ****** DIRECTORY entries

hio_dir
	cmp.b	#iob.fmul,d0		 ; simple fetch multiple bytes io?
	bne.s	hid_fs			 ; ... no, try filing system

	cmp.w	#$40,d2 		 ; 40 byte entries only
	bne.s	hio_ipar

	move.l	hic_pths(a0),a2 	 ; pointer
	move.l	(a2)+,d4
	beq.s	hdfm_eof		 ; end of list
	move.l	a2,hic_pths(a0) 	 ; save it

	lea	hil_list(a3),a2

hdfm_look
	assert	hid_link,0
	move.l	(a2),d0 		 ; next history
	beq.s	hdfm_empty
	move.l	d0,a2
	cmp.l	d0,d4
	bne.s	hdfm_look		 ; ... not there yet

	moveq	#$40-hid_hbuf,d0	 ; 'file length' adjust
	add.l	hid_htop(a2),d0
	sub.l	a2,d0
	move.l	d0,(a1)+
hdfm_sethd
	clr.w	(a1)+
	clr.l	(a1)+
	clr.l	(a1)+
	lea	hid_name(a2),a4
	moveq	#(hid.name+2-1)/4,d3
hdfm_cname
	move.l	(a4)+,(a1)+
	dbra	d3,hdfm_cname

	moveq	#($40-hid.name-2-14)/2,d3

hdfm_ret
	moveq	#$40,d1 		 ; return
	moveq	#0,d0

hdfm_clear
	move.w	d0,(a1)+		 ; clear the rest
	dbra	d3,hdfm_clear
	rts

hdfm_empty
	moveq	#($40-2)/2,d3		 ; empty return
	bra.s	hdfm_ret

hdfm_eof
	moveq	#err.eof,d0
	rts

; DIR fs calls

hid_fs
	sub.b	#iof.rhdr,d0		 ; read header
	beq.s	hid_rhdr
	sub.b	#iof.minf-iof.rhdr,d0	 ; medium info
	bne	hio_ipar

; DIR medium info

	move.l	hic_nrhs+2(a0),d1
	move.w	hic_nrhs+2(a0),d1

	lea	history_name,a4
	move.l	(a4)+,(a1)+
	move.l	(a4)+,(a1)+
	move.w	(a4)+,(a1)+

	moveq	#0,d0
	rts

; DIR read header

hid_rhdr
	move.l	hic_nrhs(a0),d1 	 ; number of entries
	lsl.l	#6,d1			 ; length of list
	move.l	d1,(a1)+		 ; file length
	move.w	#$00ff,(a1)+		 ; directory
	clr.l	(a1)+
	clr.l	(a1)+
	moveq	#14,d1			 ; return
	moveq	#0,d0
	rts

hio_elin
	tst.l	d0			 ; ELIN with empty buffer?
	bne	hio_ipar		 ; ... no
hio_flin
	bsr.l	hio_pick		 ; pick a message out of the history
	cmp.w	d2,d1
	bge.l	hio_bffl0
	move.l	d1,d0
	bra.s	hfl_cend
hfl_copy
	move.b	(a4)+,(a1)+		 ; copy message
hfl_cend
	dbra	d0,hfl_copy

	move.b	#k.nl,(a1)+		 ; set NL at end
	addq.w	#1,d1
hfl_ok
	moveq	#0,d0

hio_clear
	assert	hic_msln,hic_stat-2
	assert	hic.idle,0
	clr.l	hic_msln(a0)
	rts

hio_fmul
	move.w	hic_stat(a0),d0 	 ; what are we expecting
	beq.s	hfm_check		 ; nothing special
	bmi.s	hfm_oops		 ; waiting for smul
	cmp.w	hic_msln(a0),d2 	 ; waiting for fmul, length correct?
	bne.s	hfm_oops
	move.w	d2,d1			 ; length
	move.l	hic_buff(a0),a4
	bra.s	hfm_cend
hfm_copy
	move.b	(a4)+,(a1)+		 ; copy message
hfm_cend
	dbra	d2,hfm_copy
	bra.s	hfl_ok

hfm_check
	tst.w	d2			 ; nothing is OK
	beq.s	hfm_ok
	subq.w	#2,d2			 ; fetch length?
	bne.s	hfm_oops
	bsr.l	hio_pick		 ; find message
	move.l	d1,d0
	cmp.l	hic_bufl(a0),d0 	 ; buffer long enough?
	ble.s	hfm_cbuff
	bsr.l	hio_abuf		 ; re-allocate buffer
	bne.s	hfm_rts
hfm_cbuff
	move.l	hic_buff(a0),a2
	move.w	d1,d0
	bra.s	hfm_cblend
hfm_cbloop
	move.b	(a4)+,(a2)+
hfm_cblend
	dbra	d0,hfm_cbloop

	move.w	d1,hic_msln(a0) 	 ; save length
	move.w	d1,(a1)+
	beq.s	hfm_d1			 ; not expecting anything
	move.w	#hic.wtfm,hic_stat(a0)
hfm_d1
	moveq	#2,d1			 ; return parameters
hfm_ok
	moveq	#0,d0
hfm_rts
	rts


hfm_oops
	bsr.s	hio_clear		 ; we were expecting an smul, scrub it
	bra	hio_ipar

hio_sbyt
	move.l	hic_buff(a0),a1 	 ; send message from here
	move.w	hic_msln(a0),d2
	bsr.l	hio_stuff		 ; stuff it
	bra.s	hio_clear		 ; and clear it

hio_smul
	move.l	a1,-(sp)		 ; save for return
	move.w	hic_stat(a0),d0 	 ; what are we expecting?
	assert	0,hic.idle,hic.wtfm-1,hic.wtnl+1
	beq.s	hsm_check		 ; nothing special
	bmi.s	hsm_add 		 ; waiting for newline, add to buffer
	bsr.s	hio_clear		 ; we were expecting a fmul! scrub it
hsm_check
	move.l	a1,d0			 ; odd address?
	btst	#0,d0
	bne.s	hsm_print		 ; ... yes
	move.l	d2,d0
	subq.w	#2,d0			 ; check for put
	cmp.w	(a1),d0 		 ; length matches?
	bne.s	hsm_print		 ; ... no, treat as PRINT
	swap	d0
	cmp.w	#'ST',d0		 ; put a string $$$$$$$$$ MAGIC
	bne.s	hsm_add
	addq.w	#2,a1
	subq.w	#2,d2
	bsr.l	hio_stuff		 ; stuff string
	bra.s	hsm_rd1

hsm_print
	cmp.b	#k.nl,-1(a1,d2.w)	 ; terminated with NL
	bne.s	hsm_add 		 ; ... no
	subq.w	#1,d2			 ; strip newline
	bsr.l	hio_stuff		 ; stuff it
	addq.l	#1,a1			 ; allow for NL
	bra.s	hsm_rd1

hsm_add
	moveq	#0,d0
	move.w	hic_msln(a0),d0 	 ; current message length
	add.w	d2,d0			 ; additional room required
	cmp.l	hic_bufl(a0),d0 	 ; enough?
	ble.s	hsm_acopy		 ; ... yes
	bsr.s	hio_abuf
	bne.s	hsm_rd1

hsm_acopy
	move.l	hic_buff(a0),a2
	move.l	a2,d1
	add.w	hic_msln(a0),a2 	 ; start of next lump of characters

	bra.s	hsm_alend
hsm_aloop
	move.b	(a1)+,(a2)+
hsm_alend
	dbra	d2,hsm_aloop

	sub.l	d1,a2
	move.w	a2,hic_msln(a0) 	 ; add to message length
	move.w	#hic.wtnl,hic_stat(a0)
	moveq	#0,d0

hsm_rd1
	move.l	a1,d1
	sub.l	(sp)+,d1
	rts

hio_abuf
	moveq	#$1f,d3
	add.l	d0,d3
	and.w	#$ffe0,d3
	move.l	d3,d0			 ; allocation rounded up
	move.l	a0,a2			 ; save channel block
	jsr	gu_achpp
	exg	a0,a2
	bne.s	hab_rts
	move.l	a2,a3			 ; running pointer
	move.l	hic_buff(a0),a5
	move.w	hic_msln(a0),d0
hab_buffl
	move.l	(a5)+,(a3)+
	subq.w	#4,d0
	bcc.s	hab_buffl

	move.l	a0,a3
	move.l	hic_buff(a3),a0
	jsr	gu_rchp 		 ; return old buffer
	move.l	a3,a0
	move.l	a2,hic_buff(a0) 	 ; set new buffer
	move.l	d3,hic_bufl(a0)
	moveq	#0,d0
hab_rts
	rts

hio_bffl0
	moveq	#0,d1
hio_bffl
	moveq	#err.bffl,d0
	rts

; STUFF D2.L CHARS (A1)
;
; MK special: now keeps track of the number of messages in buffer. This is
; a bit messy, because the original routine just overwrote as many messages
; as necessary to insert the new one. The new routine checks /while/ overwriting
; the messages for a new message start (empty space -> character flank) and
; decrements the overall message counter in this case. This is solved by
; having every copy loop 2 times and building some sort of state machine between
; those loops
hio_stuff
	clr.b	hic_msnv(a0)		 ; next time get first message
	and.l	#$0000ffff,d2
	beq.s	his_end 		 ; empty strings confuse us! Don't allow
	move.l	hid_hptr(a2),a4 	 ; pointer (to 0)
	addq.l	#1,a4			 ; start of next message
	move.l	hid_htop(a2),d0 	 ; top
	move.l	d0,d3
	sub.l	d2,d0			 ; highest for message (+1)
	cmp.l	d0,a4
	blt.s	his_stuff		 ; ... OK

	lea	hid_hbuf(a2),a5 	 ; buffer is here
	cmp.l	d0,a5
	bgt.s	hio_bffl		 ; ... oops

	moveq	#0,d0
	sub.l	a4,d3			 ; amount to clear at end
	blt.s	his_start

; Loop for the case "currently in empty space"
his_cltop_e
	tst.b	(a4)
	bne.s	his_clmsg		 ; start of a message
his_cl_e
	move.b	d0,(a4)+
	dbra	d3,his_cltop_e
	bra.s	his_start

his_clmsg
	subq.w	#1,hid_cnt(a2)		 ; start of a message, subtract counter
; Loop for the case "currently in message"
his_cltop
	tst.b	(a4)
	beq.s	his_cl_e		 ; in empty space again
	move.b	d0,(a4)+
	dbra	d3,his_cltop		 ; includes sentinal

his_start
	move.l	a5,a4			 ; stuff it here
	bra.s	his_stuff

; Loop for "within empty space"
his_stufl_e
	tst.b	(a4)
	bne.s	his_msg 		 ; start of new message
his_copy_e
	move.b	(a1)+,(a4)+
his_stuff
	dbra	d2,his_stufl_e
	moveq	#0,d2			 ; flag "was in empty space"
	bra.s	his_endstuff

his_msg
	subq.w	#1,hid_cnt(a2)		 ; start of a message, subtract counter
; Loop for "within message"
his_stufl
	tst.b	(a4)
	beq.s	his_copy_e
	move.b	(a1)+,(a4)+
	dbra	d2,his_stufl		 ; leave d2 as $FFFF!

his_endstuff
	move.l	a4,hid_hptr(a2)
	addq.l	#1,hid_msnr(a2) 	 ; one more message
	addq.w	#1,hid_cnt(a2)		 ; one more message in history

	moveq	#0,d0
	tst.b	(a4)			 ; marker conveniently there?
	beq.s	his_end 		 ; yes, exit

	tst.w	d2			 ; was in empty space?
	bne.s	his_clnxt		 ; no, just delete rest of string
	subq.w	#1,hid_cnt(a2)		 ; start of a message, subtract counter
his_clnxt
	move.b	d0,(a4)+		 ; set marker and delete rest of
	tst.b	(a4)			 ;   string until next marker
	bne.s	his_clnxt
his_end
	moveq	#0,d0
	rts

; PICK OUT MESSAGE - (A4) length D1

hio_pick
	lea	hid_hbuf(a2),a5 	 ; base of it
	move.l	hid_msnr(a2),d3 	 ; current message
	tas	hic_msnv(a0)		 ; message number valid?
	bne.s	hip_lmsg
	move.l	d3,hic_msnr(a0) 	 ; use current
	bra.s	hip_smsg

hip_lmsg
	cmp.l	hic_msrf(a0),d3 	 ; has history changed?
	bne.s	hip_smsg		 ; ... yes
	move.l	hic_mspt(a0),d0 	 ; ... no, we can use the message pointer
	beq.s	hip_smsg		 ; but it is not valid
	move.l	d0,a4
	moveq	#0,d3			 ; take the next message
	bra.s	hip_next		 ; check next

hip_smsg
	move.l	d3,hic_msrf(a0) 	 ; keep current as reference
	sub.l	hic_msnr(a0),d3 	 ; less required message number
	move.l	hid_hptr(a2),a4 	 ; pointer (to 0 at end of last message)

hip_scanm
	move.l	a4,d1			 ; save start
hip_lz
	tst.b	-(a4)			 ; at start of message?
	bne.s	hip_lz			 ; ... no

	subq.l	#1,d3			 ; decrement count
	bcs.s	hip_set 		 ; found it
hip_next
	cmp.l	a5,a4			 ; off start?
	bgt.s	hip_chk
	move.l	hid_htop(a2),a4 	 ; top
hip_lnz
	tst.b	-(a4)			 ; at end of message?
	beq.s	hip_lnz 		 ; ... no

	addq.l	#1,a4
hip_chk
	cmp.l	hid_hptr(a2),a4 	 ; wrapped around?
	bne.s	hip_scanm
	move.l	a4,d1			 ; set zero message (one byte) return
	addq.l	#1,d1
hip_set
	subq.l	#1,hic_msnr(a0) 	 ; next time take previous message
	move.l	a4,hic_mspt(a0) 	 ; and save pointer in case
	addq.l	#1,a4			 ; first real character
	sub.l	a4,d1
	rts

	end
