; DV3 HDD lock drive for SMSQmulator   1.00 @ W. Lenerz
; based on
; DV3 QXL Lock/Unlock Drive   V3.00     1998	  Tony Tebby

	section dv3

	xdef	hd_lock 		; lock drive
	xdef	hd_unlock		; unlock drive

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_java'

;+++
; This routine locks a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_lock
	moveq	#jta.lock,d0
	dc.w	jva.trpA
	moveq	#0,d0
	rts

;+++
; This routine unlocks a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_unlock
	move.l	a2,-(a7)
	andi.w	#$ff,d7
	lea	hdl_remd(a3),a2
	tst.b	-1(a2,d7.w)		; removeable?
	beq.s	hd_exit 		; ... no
	moveq	#jta.unlk,d0
	dc.w	jva.trpA
hd_exit
	move.l	(a7)+,a2
	moveq	#0,d0
	rts

	end
