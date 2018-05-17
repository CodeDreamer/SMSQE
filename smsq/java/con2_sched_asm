; CON scheduler for SMSQmulator

; 2004-03-18	1.01	cursor status is toggled before calling cn_curtg (wl)
; 2012-xx-xx	1.01	suspend java job when cursor toggle
; 2016		no longer used in SMSQmulator 2.17
	section con
;
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_java'
;
	xdef	cn_sched
;
	xref	pt_sched
	xref	cn_curtg
;
cn_sched
	move.l	sys_ckyq(a6),d0 	; keyboard queue
	beq.s	csh_exit		; ... none
	move.l	d0,a0
	move.b	sd_curf-sd_keyq(a0),d0	; cursor status
	beq.s	csh_exit		; ... none
	sub.w	d3,sys_cfst(a6) 	; count down
	bgt.s	csh_exit		; not ready yet
	move.w	#12,sys_cfst(a6)

	moveq	#jt5.slep,d0
	dc.w	jva.trp5		; call java possibly suspend job
	sub.w	#sd_keyq,a0		; base of channel block
	neg.b	sd_curf(a0)		; change state	- this is wished state
	jsr	cn_curtg		; toogle now

csh_exit
	jmp	pt_sched
	end
