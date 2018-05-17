; Open console for current job

	section utility

	include dev8_keys_err
	include dev8_keys_qdos_io
	include dev8_mac_xref

	xdef	ut_opconhi	; open console and set highest display mode
	xdef	ut_opcon	; open console under current display mode

ut_opconhi
	xjsr	ut_himod	; check and set mode first
;+++
; Open console for current job with minimum size.
; Ensure that pointer interface and window manager are present.
;
;		Entry				Exit
;	a0					channel ID
;	a2					window manager vector
;
;	Error returns:	All usual open errors
;			err.nimp	ptr_gen or wman not present
;	Condition codes set on return
;---
ut_opcon
ut_opreg reg	d1-d3/a1
	movem.l ut_opreg,-(sp)
	lea	con_def,a0
	moveq	#0,d3
	xjsr	gu_fopen
	bne.s	op_ret
	moveq	#iop.pinf,d0
	xjsr	gu_iow
	bne.s	op_noptr
	move.l	a1,a2
	move.l	a1,d0
	beq.s	op_noman
	moveq	#0,d0
op_ret
	movem.l (sp)+,ut_opreg
	rts

op_noptr
op_noman
	moveq	#err.nimp,d0
	bra.s	op_ret

con_def dc.w	11
	dc.b	'con_2x1a0x0 '

	end
