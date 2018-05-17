* CALL command	(smsq)	V1.0    1985	 Tony Tebby   QJUMP
*
	section exten
*
*	CALL address, parameters
*
	xdef	callsq
	xdef	call_code
*
	xref	ut_gtlin

	xref	ut_reassert
	xref	sb_reprocs
*
	include dev8_keys_sbasic
	include dev8_keys_sys
	include dev8_keys_qdos_sms
	include dev8_keys_err
	include dev8_smsq_smsq_base_keys
*
callsq
	bsr.l	ut_gtlin		get some long integers
	bne.s	call_rts		... oops
	ext.l	d3
	lsl.w	#2,d3			convert count to bytes
	beq.s	call_bp 		... oops
	add.l	d3,sb_arthp(a6) 	and remove parameters from RI stack
	move.l	(a6,a1.l),d0		put first parameter as return address!
	movem.l 4(a6,a1.l),d1-d7/a0-a5	set parameters

call_code
	move.b	sb_edt(a6),-(sp)	save edit flag
	sf	sb_edt(a6)
	move.l	sb_qlibr(a6),-(sp)	save qlib bit
	pea	call_fix
	move.l	d0,-(sp)		call address
	move.l	d1,-(sp)
	moveq	#sms.cach,d0		temporarily suppress cache
	moveq	#2,d1
	trap	#1
	move.l	(sp)+,d1
call_bp
	moveq	#err.ipar,d0		 preset err.ipar
call_rts
	rts


call_fix
	move.l	d0,-(sp)
	move.l	a1,-(sp)
	move.l	sb_qlibr(a6),a1
	cmp.l	8(sp),a1		 any QLIB change?
	beq.s	call_reproc		 ... no
	bsr.s	call_qlib
call_reproc
	move.b	sb_edt(a6),d0		 any name table change?
	beq.s	call_done

	lea	sb_reprocs,a1
	jsr	ut_reassert

call_done
	move.b	12(sp),d0
	or.b	d0,sb_edt(a6)		 update edit

	move.l	(sp)+,a1
	move.l	(sp)+,d0
	addq.l	#6,sp
	rts

call_qlib
	moveq	#sms.xtop,d0
	trap	#do.sms2
	move.l	sys_sbab(a6),a5
	move.l	a1,sb_offs+sb_qlibr(a5)  set liberator
	rts

	end
