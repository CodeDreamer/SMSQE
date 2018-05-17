; Open NUL Channel   V2.00     1989  Tony Tebby

	section nul

	xdef	nul_open
	xdef	nul_name
	xdef	nul_parm

	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_iod_nul_data'
	include 'dev8_mac_assert'
;+++
; NUL channel open operations. (Using IOU.DNAM)
;---
nul_open
frame	equ	$4
	subq.l	#frame,sp		 ; make room for the parameters
	move.l	a3,a4			 ; save pointer to def block
	move.l	sp,a3			 ; set pointer to parameters
	move.w	iou.dnam,a2		 ; read name
	jsr	(a2)
	bra.s	nulo_exit
	bra.s	nulo_exit
	bra.s	nulo_setup
	dc.w	3
nul_name
	dc.w	'NUL'
	dc.w	1
	dc.w	4
nul_parm
	dc.w	'FZLP'

nulo_setup
	moveq	#nlc_end,d1
	move.w	mem.achp,a2		 ; allocate in heap
	jsr	(a2)
	bne.s	nulo_exit		 ; ... oops
	move.w	(sp),d1 		 ; parameter
	move.w	d1,nlc_parm(a0) 	 ; save it
	add.w	d1,d1
	move.w	nul_key(pc,d1.w),nlc_key(a0) ; set key


nulo_exit
	addq.l	#frame,sp
	rts

nul_key dc.w	nlc.bp,nlc.file,nlc.zero,nlc.line,nlc.paus
	end
