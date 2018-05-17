; Pick my channel to the top		1995 Jochen Merz
; Pick me to the top

	section utility

	include dev8_keys_qdos_io
	include dev8_keys_qdos_sms

	xdef	ut_pickchn	; pick this channel (owned by me) to the top
	xdef	ut_pickme	; pick me to the top

;+++
; Pick me to the top. - Cannot fail!
;---
pick.reg reg d1-d3/a0-a2
ut_pickme
	movem.l pick.reg,-(sp)
	bsr.s	get_my_jobid
	sub.l	a0,a0		; use the only ever existing channel
pick_cont
	moveq	#-1,d3
	moveq	#iop.pick,d0
	trap	#do.io
	movem.l (sp)+,pick.reg
	tst.l	d0
	rts

;+++
; Pick this channel (owned by me) to the top.
;
;		Entry			Exit
;	A0	channel ID		preserved
;---
ut_pickchn
	movem.l pick.reg,-(sp)
	bsr.s	get_my_jobid
	move.l	12(sp),a0
	bra.s	pick_cont

get_my_jobid
	moveq	#sms.info,d0
	trap	#do.smsq
	rts

	end
