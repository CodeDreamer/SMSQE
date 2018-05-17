; Accelerator routine

	section utility

	include dev8_keys_thg
	include dev8_keys_qdos_sms
	include dev8_keys_menu
	include dev8_mac_xref

	xdef	ut_a_on
	xdef	ut_a_off
	xdef	ut_astat

;+++
;		Entry			Exit
;
;	Error returns:	ERR.NI	Thing or THING not implemented
;---
stk.frm  equ	$20
accel_reg   reg d2-d3/a0-a5

ut_a_on
	movem.l accel_reg,-(sp)
	move.l	#'ON  ',d2		; we need switch on
	bra.s	acce_all
ut_a_off
	movem.l accel_reg,-(sp)
	move.l	#'OFF ',d2		; we need switch off
	bra.s	acce_all
ut_astat
	movem.l accel_reg,-(sp)
	move.l	#'STAT',d2		; we need state of Accelerator
acce_all
	sub.l	#stk.frm,sp
	move.l	sp,a5			; prepare workspace
	move.l	a0,-(sp)		; because A2 => additional result
	lea	acce_thg,a0		; Accelerator Thing
	moveq	#127,d3 		; timeout
	moveq	#sms.myjb,d1		; that's the current job
	moveq	#sms.uthg,d0		; use Thing
	xjsr	gu_thjmp
	move.l	(sp)+,a0

	tst.l	d0
	bne.s	no_accelerator		; failed

	move.l	a1,a4			; address of thing
	move.l	#(thp.ret+thp.ulng)<<16,$0(a5) ; ret param is long
	lea	$8(a5),a1
	move.l	a1,$4(a5)		; pointer to return parameter
	move.l	a5,a1

	jsr	thh_code(a4)		; do it
	bne.s	err_rts

	move.l	$8(a5),d1		; return parameter
err_rts
	move.b	d1,-(sp)
	lea	acce_thg,a1		; Accelerator Thing
	xjsr	ut_thfre		; and free Accelerator Extension
	move.b	(sp)+,d1
no_accelerator
	add.l	#stk.frm,sp
	movem.l (sp)+,accel_reg
	tst.l	d0
	rts

ut_usacc
	move.l	a0,-(sp)		; because A2 => additional result
	lea	acce_thg,a0		; Accelerator Thing
	moveq	#127,d3 		; timeout
	moveq	#sms.myjb,d1		; that's the current job
	moveq	#sms.uthg,d0		; use Thing
	xjsr	gu_thjmp
	move.l	(sp)+,a0
	rts


acce_thg dc.w  11,'Accelerator '


	end
