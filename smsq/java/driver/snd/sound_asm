; SOUND device driver open,close and io/routines  v. 1.00 (c) W.Lenerz 2014

; v. 1.00   2014 Jan 16


	section sound

	include 'dev8_keys_java'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_sms'

	xdef	snd_open
	xdef	snd_close
	xdef	snd_io

				
; open routine, check whether it's this device
snd_open
	moveq	#1,d2		; default drive
	move.w	(a0),D0 	; length name
	subq.w	#5,d0		; 'sound'
	beq.s	possible
	subq.w	#1,d0		;'soundx'
	bne.s	notme		; can't be me
	moveq	#0,d2
possible
	move.l	2(a0),d1	; part of name
	and.l	#$dfdfdfdf,d1	; upper case
	cmp.l	#'SOUN',d1	;
	bne.s	notme		; it's not me
	move.b	6(a0),d1	; last byte of name
	and.l	#$dfdfdfdf,d1
	cmp.b	#'D',d1
	bne.s	notme		; not me
	tst.w	d2		; use default device?
	bne.s	default 	; yes
	move.b	7(a0),d2	; no, the number
	sub.b	#'0',d2 	; must be 1...9
	ble.s	errbp
	cmp.b	#9,d2
	bhi.s	errbp
default
	moveq	#jt5.osnd,d0
	dc.w	jva.trp5	; try to really open channel in java
	tst.l	d0		; failed
	bne.s	out
	moveq	#$34,d1
	move.w	mem.achp,a2	; allocate chan defn block in heap
	jsr	(a2)
	bne.s	out
	move.b	d2,$20(a0)	; keep device nbr
	tst.l	d0
out	rts


notme	moveq	#err.fdnf,d0
	rts

errbp	moveq	#err.inam,d0
	rts


; close routine, just return chan def blk mem
snd_close
	moveq	#jt5.csnd,d0
	dc.w	jva.trp5	 ; call java to show close
	move.w	mem.rchp,a2
	jmp	(a2)
				

; the io routine  , just call java
snd_io
	move.l	d0,d4		; type of trap
	moveq	#jt5.asnd,d0	: do it in java
	dc.w	jva.trp5
	tst.l	d0
	rts

	end
