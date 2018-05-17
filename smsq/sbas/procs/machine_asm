; SBAS_PROCS_MACHINE - SBASIC Info on Machine  V2.00    1994  Tony Tebby

	section exten

	xdef	machine
	xdef	processor

	xref	ut_rtint
	xref	err_bp

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'

;+++
; MACHINE
;---
machine
	moveq	#sys_mtyp-$70,d1
	bsr.s	mc_fetch
	and.w	#sys.mtyp+sys.blit,d1	 ; machine type and blitter bits only
	jmp	ut_rtint

;+++
; PROCESSOR
;---
processor
	moveq	#sys_ptyp-$70,d1
	bsr.s	mc_fetch
	lsr.b	#4,d1			 ; hex 0x0 / 4
	ext.w	d1
	mulu	#10,d1			 ; return decimal
	jmp	ut_rtint

mc_fetch
	cmp.l	a3,a5
	bne.l	err_bp
	moveq	#sms.xtop,d0		 ; xtop
	trap	#do.smsq
	move.b	$70(a6,d1.w),d1
	moveq	#0,d0
	rts

	end
