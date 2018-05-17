; SETRATE10/ SETRATE20 keywords   v.1.00  (c) 2004   W. Lenerz


; set the sound resolutions - but only for Qx0, ignore on all other machines
; copyright (c) W. Lenerz  - see the licence in the documentation
; v. 1.00	2004 Nov 11 08:35:37


	section sound

	xdef		setrate10
	xdef		setrate20
	include 	dev8_keys_q40
	include 	dev8_keys_qdos_sms
	include 	dev8_keys_sys


setrate20
	move.b	#q40.50us,d7
	bra.s	setcomn
setrate10
	move.b	#q40.100us,d7
setcomn
common	moveq	#sms.info,d0
	trap	#1			; get ptr to system variables in a0
	cmp.b	#sys.mq40,sys_mtyp(a0)	; is this a Q40?
	bne.s	out			; no, just ignore
	move.b	d7,q40_srate
out	moveq	#0,d0			: this shouldn't strictly be necessary
	rts
	end
