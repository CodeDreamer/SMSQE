; SBAS_PROCS_OUTLN - SBASIC OUTLN  V2.00  Stolen from PTR Toolkit

	section exten

	xdef	outln

	xref	ut_chan1
	xref	ut_chlook
	xref	ut_gtint

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'

;+++
; OUTLN xs,ys,xo,yo,xsh,ysh,mv
;---
outln
	cmp.l	a5,a3			 ; any parameters?
	bne.s	beo_parm
	moveq	#0,d6			 ; outline channel 0
	jsr	ut_chlook
	move.l	sb_buffb(a6),a1 	 ; -1 size
	moveq	#-1,d0
	move.l	d0,(a6,a1.l)
	moveq	#0,d1
	moveq	#0,d2
	jmp	beo_doit

beo_parm
	jsr	ut_chan1		 ; get channel ID to outline
	bne.s	beo_exit
	jsr	ut_gtint
	bne.s	beo_exit
*
*	See which options are in use
*
	moveq	#0,d1			; assume no shadow
	moveq	#0,d2			; and set outline

	lea	8(a1),a2		; point to extra parameters
	subq.w	#4,d3			; must have at least four parameters
	bmi.s	beo_exbp		; ...oops
	beq.s	beo_doit		; exactly, set outline
	subq.w	#2,d3			; up to three more
	bmi.s	beo_smov		; only one, it's move flag
	move.l	0(a6,a2.l),d1		; two or three include shadow
	addq.l	#4,a2			; may be a flag afterwards
	tst.w	d3			; is there?
	beq.s	beo_doit		; no
beo_smov
	move.w	0(a6,a2.l),d2		; set move flag
beo_doit
	moveq	#-1,d3			; sometime
	moveq	#iop.outl,d0		; set outline to definition on RIstk
	trap	#4			; that's A6-relative
	trap	#3
beo_exit
	tst.l	d0
	rts
beo_exbp
	moveq	#err.ipar,d0
	bra.s	beo_exit
	end
