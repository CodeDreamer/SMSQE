; Cache Handling  V2.00    1986  Tony Tebby

	section sms

	xdef	sms_cach

	xref	sms_rtok

	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_keys_68000'
	include 'dev8_keys_sys'

;+++
; Cache handling OS entry
;---
sms_cach
	pea	sms_rtok	 ; always return OK
	tst.l	d1		 ; cache on?
	beq.s	sch_off 	 ; ... no
	bmi.s	sch_ret 	 ; ... query
	subq.b	#2,d1		 ; temporary suppress?
	beq.s	sch_temp

	move.w	sys_castt(a6),d0 ; already on or suppressed?
	bge.s	sch_ret 	 ; ... yes
	lea	sms.cenab,a5	 ; cache routine
	cmp.w	#rts,(a5)	 ; is it just rts?
	beq.s	sch_ret 	 ; ... yes
	clr.b	sys_castt(a6)	 ; set on
	tst.b	d0		 ; ... suppressed?
	bne.s	sch_ret 	 ; ... yes
	jsr	(a5)		 ; enable both caches
	bra.s	sch_ret

sch_temp
	move.b	#sys.casup,sys_castt+1(a6) ; suppress (even if disabled)
	bra.s	sch_dis
sch_off
	st	sys_castt(a6)
sch_dis
	jsr	sms.cdisb	 ; disable both caches

sch_ret
	moveq	#0,d1
	move.b	sys_castt(a6),d1 ; return cache status
	addq.b	#1,d1
	moveq	#0,d0
	rts
	end
