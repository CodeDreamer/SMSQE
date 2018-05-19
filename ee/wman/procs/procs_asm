; WMAN colours basic procedures  V1.04		       2002  Marcel Kilgus
;
; 2003-05-27  1.01  Changed routines to always use sys_clnk to get WMAN vec (MK)
;		    Added WM_MOVEMODE (WL/MK)
; 2003-09-05  1.02  Fixed WM_BLOCK (GG/MK)
; 2013-04-28  1.03  Added WM_MOVEALPHA (wl)
; 2018-04-08  1.04  Mini-optimisations (mk)

	section wm_ext

	xdef	wm_paper
	xdef	wm_ink
	xdef	wm_strip
	xdef	wm_block
	xdef	wm_border
	xdef	wm_movemode
	xdef	wm_movealpha
	xdef	sp_reset
	xdef	sp_set
	xdef	sp_get
	xdef	sp_getcount
	xdef	sp_jobpal
	xdef	sp_jobownpal

	xref	ut_chan1
	xref	ut_rtint
	xref	ut_gtint
	xref	ut_gtin1
	xref	ut_gtlin
	xref	ut_gxli1
	xref	ut_gxli2
	xref	ut_gtnm1
	xref	ut_fjob

	include 'dev8_keys_wman_data'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_con'
	include 'dev8_keys_wman'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

;+++
; WM_PAPER (#ch,) colour
;---
wm_paper
	bsr	wm_getwman
	jsr	ut_chan1		; get a channel id
	bne.s	wm_rts
	jsr	ut_gxli1
	bne.s	wm_rts

	moveq	#iow.spap,d0
	move.l	(a6,a1.l),d1
	move.l	d1,d7
	moveq	#-1,d3
	jsr	wm.trap3(a2)
	bne.s	wm_rts
	moveq	#iow.sstr,d0
	move.l	d7,d1
	jmp	wm.trap3(a2)

;+++
; WM_INK (#ch,) colour
;---
wm_ink
	moveq	#iow.sink,d7
	bra.s	wm_setcolour

;+++
; WM_STRIP (#ch,) colour
;---
wm_strip
	moveq	#iow.sstr,d7
wm_setcolour
	bsr	wm_getwman
	jsr	ut_chan1		; get a channel id
	bne.s	wm_rts
	jsr	ut_gxli1
	bne.s	wm_rts

	move.l	d7,d0
	move.l	(a6,a1.l),d1
	moveq	#-1,d3
	jsr	wm.trap3(a2)
wm_rts
	rts

;+++
; WM_BLOCK (#ch,) xs, ys, xo, yo, col
;---
wm_block
	bsr	wm_getwman
	jsr	ut_chan1		; get a channel id
	bne.s	wm_rts
	subq.l	#8,a5
	cmpa.l	a3,a5
	ble	wm_ipar
	jsr	ut_gtint
	cmp.w	#4,d3
	bne	wm_ipar
	lea	4*8(a3),a3
	addq.l	#8,a5
	jsr	ut_gxli1
	bne.s	wm_rts

	moveq	#iow.blok,d0
	move.l	(a6,a1.l),d1
	moveq	#-1,d3
	lea	4(a6,a1.l),a1
	jmp	wm.trap3(a2)

;+++
; WM_BORDER (#ch,) width, colour
;---
wm_border
	bsr	wm_getwman
	jsr	ut_chan1		; get a channel id
	bne.s	wm_rts
	jsr	ut_gxli2
	bne.s	wm_rts

	moveq	#iow.defb,d0
	move.l	4(a6,a1.l),d1
	move.l	(a6,a1.l),d2
	moveq	#-1,d3
	jmp	wm.trap3(a2)

;+++
; WM_MOVEMODE move_type% (0..3)
;---
wm_movemode
	bsr	wm_getwdata
	jsr	ut_gtin1		; get one int
	bne.s	wm_rts
	move.w	(a6,a1.l),d0
	blt.s	wm_ipar 		; and it must be between 0...
	cmp.w	#3,d0			; ... and 2
	bgt.s	wm_ipar
	move.b	d0,wd_movemd(a2)
	clr.l	d0
	rts

;+++
; WM_MOVEALPHA move_alpha% (0..255)
;---
wm_movealpha
	bsr	wm_getwdata
	jsr	ut_gtin1		; get one int
	bne.s	alp_rts
	move.w	(a6,a1.l),d0
	move.b	d0,wd_alpha(a2)
	clr.l	d0
alp_rts rts
		
;+++
; SP_RESET (no)
;---
sp_reset
	bsr	wm_getwman		; get WMAN vector
	moveq	#0,d4			; default to palette 0
	jsr	ut_gtint
	cmp.w	#1,d3			; max 1 parameter
	bhi.s	wm_ipar
	tst.w	d3
	beq.s	spr_nonum
	move.w	(a6,a1.l),d4		; palette number
spr_nonum
	move.l	d4,d3
	moveq	#0,d1
	moveq	#-1,d2
	suba.l	a1,a1
	jsr	wm.getsp(a2)
	jmp	wm.setsp(a2)

wm_ipar
	moveq	#err.ipar,d0
sp_rts
	rts

;+++
; SP_GETCOUNT
;---
sp_getcount
	bsr	wm_getwman
	cmpa.l	a3,a5
	bne.s	wm_ipar

	moveq	#-1,d2
	jsr	wm.getsp(a2)
	move.w	d2,d1
	jmp	ut_rtint

;+++
; SP_GET (no,) adr, first, count
;---
sp_get
	bsr.s	wm_getwman
	moveq	#0,d4			; default to palette 0
	jsr	ut_gtlin
	cmp.w	#3,d3
	beq.s	spg_nonum
	cmp.w	#4,d3
	bne.s	wm_ipar
	move.l	(a6,a1.l),d4		; given palette number
	addq.l	#4,a1
spg_nonum
	move.l	d4,d3
	move.l	4(a6,a1.l),d1
	move.l	8(a6,a1.l),d2
	move.l	(a6,a1.l),a1
	jmp	wm.getsp(a2)

;+++
; SP_SET (no,) adr, first, count
;---
sp_set
	bsr.s	wm_getwman
	moveq	#0,d4			; default to palette 0
	jsr	ut_gtlin
	cmp.w	#3,d3
	beq.s	sps_nonum
	cmp.w	#4,d3
	bne.s	wm_ipar
	move.l	(a6,a1.l),d4		; given palette number
	addq.l	#4,a1
sps_nonum
	move.l	d4,d3
	move.l	4(a6,a1.l),d1
	move.l	8(a6,a1.l),d2
	move.l	(a6,a1.l),a1
	jmp	wm.setsp(a2)

;+++
; SP_JOBPAL job id / job name, no
;---
sp_jobpal
	bsr.s	wm_getwman
	bsr.s	job_proc
	bne.s	wm_rts2

	move.l	d2,d3
	suba.l	a1,a1
	jmp	wm.jbpal(a2)

;+++
; SP_JOBOWNPAL job id / job name, ptr to palette
;---
sp_jobownpal
	bsr.s	wm_getwman
	bsr.s	job_proc
	bne.s	wm_rts2

	move.l	d2,a1
	moveq	#-1,d3
	jmp	wm.jbpal(a2)

;+++
; Get WMAN vector in a2
;---
wm_getwman
	movem.l d1-d3/a0,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_clnk(a0),a2
	move.l	pt_wman(a2),a2
	movem.l (sp)+,d1-d3/a0
wm_rts2
	rts

;+++
; Get WMAN data ptr in a2
;---
wm_getwdata
	movem.l d1-d3/a0,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_clnk(a0),a2
	move.l	pt_wdata(a2),a2
	movem.l (sp)+,d1-d3/a0
	rts

;+++
; Get job ID from supplied parameter(s)
;---
job_proc
	moveq	#8,d5			get 2 parameters (8 bytes)
*
	cmp.l	a3,a5			must be some parameters
	beq.s	job_bp
	tst.b	(a6,a3.l)		is it unused name?
	beq.s	job_gtnm		... yes, get name
	moveq	#$f,d0
	and.b	1(a6,a3.l),d0		get variable type
	subq.b	#1,d0			is it string?
	beq.s	job_gtnm		... yes
	tst.w	2(a6,a3.l)		any name?
	bmi.s	job_gtln		... no, get long integers
	tst.w	4(a6,a3.l)		any value?
	bpl.s	job_gtln		... yes, get it
job_gtnm
	move.l	d5,d6			save number of parms
	bsr.l	ut_gtnm1		get name
	bne.s	job_rts 		... oops
	addq.l	#8,a3			move parameter pointer on
*
	bsr.l	ut_fjob 		find job
	bne.s	job_rts 		... oops
*
	subq.l	#8,a0			backspace a0 to base of job
	move.l	d4,d1			set Job ID
	moveq	#0,d2			no additional information
	cmp.l	a3,a5			any more params?
	beq.s	job_rts 		... no
	subq.l	#4,d6			was another param needed?
	beq.s	job_bp			... no, bad
	bsr.l	ut_gxli1		... yes, one long integer
	bne.s	job_rts
	addq.l	#4,sb_arthp(a6) 	restore RI stack
	move.l	d4,d1			reset job ID
	subq.l	#4,a1			move RI stack pointer to ...
	bra.s	job_parm		... set parameter
*
job_gtln
	bsr.l	ut_gtlin		get long integers
	bne.s	job_rts
	ext.l	d3
	lsl.w	#2,d3			make d3 number of bytes
	add.l	d3,sb_arthp(a6) 	and restore RI stack pointer
	sub.w	d5,d3			did we get the right number?
	blt.s	job_bp			too few
	beq.s	job_id1 		combined id
	subq.w	#4,d3			was it number/tag?
job_bp
	bne.l	wm_ipar 		... no
	move.l	6(a6,a1.l),d1		get tag in msw of d1
	move.w	2(a6,a1.l),d1		and number in lsw
	addq.l	#4,a1			move up a bit
	bra.s	job_parm
job_id1
	move.l	(a6,a1.l),d1		get job id
job_parm
	move.l	4(a6,a1.l),d2		set parameter
	tst.l	d0
job_rts
	rts

	end
