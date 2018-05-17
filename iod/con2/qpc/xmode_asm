; QPC extended colour mode

	section init

	xdef	pt_xmodec		 ; check if mode can be set
	xdef	pt_xmode		 ; set mode
	xdef	qpc_xmode		 ; set QPC on mode change

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'

;+++
; Check and set mode
;
;	d1 c  r mode if can be set
;	a3 c  p pointer to console linkage
;	a6 c  p pointer to system variables
;---
pt_xmode
	move.b	d1,d0		 QPC mode 8
	cmp.b	#ptm.ql8,d1	 ... is it?
	beq.s	ptxm_set
	move.b	#4,d0		 QPC mode 4
	clr.l	d1
ptxm_set
	move.b	d1,sys_qlmr(a6)

	movem.l a1/a2/a3,-(sp)
	move.l	#qpc_scr_work,a2	 ; screen work area
	move.b	d0,qpc_vql(a2)		 ; set video conversion mode
	bsr.s	qpc_xmode
	movem.l (sp)+,a1/a2/a3
ptxm_ok
	moveq	#0,d0
	rts

pt_xmodec
	assert	ptm.ql4,0
	assert	ptm.ql8,8
	move.b	d1,d0
	asr.b	#3,d0
	addq.b	#1,d0			; addjust to 1 (=4 col) or 2 (=8 col)
	dc.w	qpc.qlemu		; try to set emulation mode
	moveq	#$ffffffff-ptm.ql8,d0
	and.b	pt_dmode(a3),d0 	; is it ql mode?
	beq.s	ptxm_ok 		; ... yes can do mode

	clr.b	sys_qlmr(a6)
	moveq	#err.ipar,d0
	rts

;+++
; QPC set mode 4/8
;
;	a1   s
;	a2 c  p pointer to screen work area
;---
qpc_xmode
	moveq	#0,d0
	move.b	qpc_vql(a2),d0		 ; QL mode

	dc.w	qpc.sqlmd		 ; tell it QPC
	rts

	end
