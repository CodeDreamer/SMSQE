; SOUNDFILE keyword	   V.1.03 (c) W. Lenerz 2004

****************************************************
* SOUNDFILE "file_name_ub"
*
* play the sampled file via the ssss
*
* v. 1.03	2004 Dec 11 16:58:55  introduced additional parameter
* v. 1.02	2004 Dec 11 10:31:55
*
* (c) W. Lenerz 2004 - see the licence in the documentation
*
*****************************************************
	section sound

	xdef	soundfile		; play file
	xdef	sf1
	xdef	withbuff2
	xdef	sf2
	xdef	sf3
	include 	dev8_keys_qdos_io
	include 	dev8_keys_qdos_ioa
	include 	dev8_smsq_q40_hdop_data
	include 	dev8_keys_68000
	include 	dev8_keys_err
	include 	dev8_keys_sss
	include 	dev8_keys_sbasic
	include 	dev8_keys_qdos_sms
	include 	dev8_keys_qlv



sf1	clr.l	d5
	move.l	a5,d0
	sub.l	a3,d0
	subq.l	#8,d0			; only one param?
	beq.s	sf1a			; yes
	lea	-8(a5),a3
	move.w	sb.gtint,a2
	jsr	(a2)			; get one int
	move.w	(a6,a1.l),d5		;
	addq.l	#2,sb_arthp(a6)
	move.l	a3,a5
	subq.l	#8,a3
sf1a	move.w	sb.gtstr,a2
	jsr	(a2)			; try to get one string
	move.l	d0,d6			;
	bne	sf_out			; ... couldn't!
	move.l	a1,a0			; string rel to a6
	add.l	a6,a0			; string absolute now
	move.l	a1,sb_arthp(a6)
	clr.l	d0
	rts

sf2	moveq	#ioa.open,d0
	moveq	#-1,d1			; channel belongs to me
	moveq	#ioa.kshr,d3		; open key: old; non exclusve, read only
	trap	#2			; try to open file
	move.l	d0,d6			; OK?
	bne.s	sf_out			; no!
sf3	move.l	exv_i4,a3		; point to Interrupt level 4
	move.l	-(a3),a2		; this could be SSSS vector
	cmp.l	#sss.flag,-(a3) 	; is it really?
	bne.s	sf_nosss		; no!
	moveq	#-1,d3			; file IO timeout
	moveq	#0,d0
	rts

soundfile
	bsr.s	sf1			; get some data
	bne.s	sf_exit 		; ooops
	bsr.s	sf2			; open channels
	bne.s	sf_exit 		; ooops

withbuff2
	movem.l  a2/d5,-(a7)		; keep
	jsr	sss_setm(a2)		; set up multiple queue (A1 & A2 return)
	move.l	a2,d2			; end of queue
	movem.l  (a7)+,a2/d5
	sub.l	a1,d2			; that much space in sound queue
	beq.s	withbuff2		; ooops, none!
;	 subq.l  #1,d2
;	 bclr	 #0,d2			 ; make sure it's even
	cmp.l	#$8000,d2
	blt.s	load
	move.l	#$7ffe,d2		; load bytes size is in 1 word!
load	moveq	#iob.fmul,d0
	trap	#3			; load bytes into here
	move.l	d0,d6			; keep error
	beq.s	play			; there was no error, so continue
	move.l	a1,d1			; ignore error for now
	addq.l	#1,d1
	bclr	#0,d1
	move.l	d1,a1			; I want this always even
play	jsr	sss_addm(a2)		; add these bytes
	tst.l	d6			; load ok?
	beq.s	withbuff2		; yes, do more
	move.l	d6,d0			; keep (!)
sf_err	clr.l	d6			; preset no error
	cmp.l	#err.eof,d0		; is "error" end of file?
	beq.s	sf_out2 		; yes so no error, actually
	move.l	d0,d6			; keep true error
	moveq	#0,d5			; no more repetition

sf_out2 tst.w	d5			; do we repeat?
	beq.s	close			; no
	subq.w	#1,d5
	moveq	#iof.posa,d0		; position file pointer
	clr.l	d1			; ... to beginning of file
	trap	#3			; ...  now
	bra.s	withbuff2		; and restart

close	moveq	#ioa.clos,d0
	trap	#2			; close the file

sf_out	move.l	d6,d0			; recover any errors
sf_exit rts

; error if no ssss exists - return not implemented
sf_nosss
	moveq	#err.nimp,d6
	bra.s	sf_out2

	end
