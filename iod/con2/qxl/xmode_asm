; QXL extended colour mode

	section init

	xdef	pt_xmodec		 ; check if mode can be set
	xdef	pt_xmode		 ; set mode
	xdef	qxl_xmode		 ; set QXL on mode change

	xref	qxl_mess_add

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'

;+++
; Check and set mode
;
;	d1 c  r mode if can be set
;	a3 c  p pointer to console linkage
;	a6 c  p pointer to system variables
;---
pt_xmode
	move.b	d1,d0		 QXL mode 8
	cmp.b	#ptm.ql8,d1	 ... is it?
	beq.s	ptxm_set
	move.b	#4,d0		 QXL mode 4
	clr.l	d1
ptxm_set
	move.b	d1,sys_qlmr(a6)

	movem.l a1/a2/a3,-(sp)
	move.l	qxl_scr_work,a2 	 ; screen work area
	move.l	qxl_message,a3		 ; message area
	move.b	d0,qxl_vql(a2)		 ; set video conversion mode
	bsr.s	qxl_xmode
	movem.l (sp)+,a1/a2/a3
ptxm_ok
	moveq	#0,d0
	rts

pt_xmodec
	assert	ptm.ql4,0
	assert	ptm.ql8,8
	moveq	#$ffffffff-ptm.ql8,d0
	and.b	pt_dmode(a3),d0 	; is it ql mode?
	beq.s	ptxm_ok 		; ... yes can do mode

	clr.b	sys_qlmr(a6)
	moveq	#err.ipar,d0
	rts

;---
; QXL set mode 4/8
;
;	a1   s
;	a2 c  p pointer to screen work area
;	a3 c  p pointer to message area
;+++
qxl_xmode
	assert	0,qxl.vc4vga-2,qxl.vc8vga-6
	moveq	#0,d0
	move.b	qxl_vql(a2),d0		 ; QL mode
	subq.w	#2,d0			 ; set conversion to VGA
	move.w	d0,qxl_vconv(a2)

	lea	qxl_ms_vgap+qxl_ms_key(a3),a1 ; vga palette message
	move.b	#qxm.spal,(a1)+ 	 ; set palette
	move.b	qxl_vql(a2),(a1)+	 ; ql mode
	clr.w	(a1)+			 ; standard palette

	subq.w	#6,a1
	move.w	#4,(a1)
	jmp	qxl_mess_add		 ; add message to queue



	end
