; Gold Card initialise QL Mode Displays 	1.01   1992  Tony Tebby  QJUMP
;							2003  Marcel Kilgus
;
; 2003-03-05  1.01  Extended for mode 16 (MK)

	section init

	xdef	cn_procv

	xref	au_modes

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_keys_aurora'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_mac_proc'
	include 'dev8_mac_thg'
	include 'dev8_mac_basic'
	include 'dev8_mac_assert'


;+++
; initialise QVME Thing and procs
;---
cn_procv
	lea	glv_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	glv_xprocs,a1
	jsr	gu_tpadd

	lea	glv_procs,a1
	jmp	ut_procdef

glv_defs dc.l	th_name+2+4
	dc.l	glv_thing-*
	dc.l	'2.00'
glv_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; Here is the code to re-initialise the Aurora display
;
;******************************************************************************

glv_xprocs
	prc_stt glv_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

glv_procs
	proc_stt
	proc_ref DISP_COLOUR
	proc_ref DISP_SIZE
	proc_ref DISP_RATE
	proc_ref DISP_BLANK
	proc_ref DISP_INVERSE
	proc_end
	proc_stt
	proc_ref DISP_TYPE
	proc_end

	proc_thg QVME
	fun40_thg QVME

disp_colour proc COLR
disp_size proc	SIZE
disp_type fun40 TYPE

disp_rate
disp_blank
disp_inverse
disp_do
glv_rtsok
	moveq	#0,d0
	rts

glv_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
glv_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
glv_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
glv_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
glv_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

glv_rword dc.w thp.ret+thp.swrd
	 dc.w	0

glv_thing
;+++
; invert display
;     (a1)     pointer to 0/1
;---
glv_invr thg_extn INVR,glv_type,glv_uwrd
	bra.s	disp_do

;+++
; find display type = 0 (QL), 3 (LCD), 5 (Aurora QL) or 16 (Aurora 256)
;     4(a1)	pointer to value
;---
glv_type thg_extn TYPE,glv_colr,glv_rword
	moveq	#sms.dmod,d0	; get mode
	moveq	#-1,d1
	moveq	#-1,d2
	trap	#do.smsq
	and.w	#$00ff,d1
	move.w	d1,d0
	cmp.w	#ptm.aur8,d0	; is it GD2 mode?
	beq.s	glvt_ret	; yes, return it

	moveq	#sms.xtop,d0	; no, use traditional code
	trap	#do.smsq
	moveq	#0,d0
	move.b	sys_mtyp(a6),d0 ; display type in most sig 3 bits
	lsr.b	#5,d0
	move.b	glv_ttab(pc,d0.w),d0
glvt_ret
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	d0,(a1) 	; return it
	move.l	(sp)+,a1
	moveq	#0,d0
	rts

glv_ttab dc.b	0,4,1,6,2,5,3,7

;+++
; Gold card colour depth change
;	(a1)	colour depth code
;	then size
;	then rate
;	then blank
;---
glv_colr thg_extn COLR,glv_size,glv_pclr
glv.reg reg	d1/d2/d3/d4/d6/a0/a1/a2/a3/a4
	movem.l glv.reg,-(sp)

	move.l	(a1)+,d3		; colour depth
	move.w	d3,d0
	beq.s	glvs_size		; QL
	subq.w	#ptd.08,d0		; or 8 bit
	beq.s	glvs_size
	bra.s	glvs_scolr

;+++
; Gold card size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
glv_size thg_extn SIZE,glv_frrt,glv_psiz
	movem.l glv.reg,-(sp)

glvs_scolr
	moveq	#-1,d3			; no colour depth change

glvs_size
	movem.l (a1),d1/d2

	bsr.s	glvs_do

	movem.l (sp)+,glv.reg
	moveq	#0,d0
glvs_rts
	rts

glvs_do
	moveq	#sms.xtop,d0
	trap	#1
	move.l	sys_clnk(a6),a3

	moveq	#$ffffffe0,d0		 ; display type in most sig 3 bits
	and.b	sys_mtyp(a6),d0
	cmp.b	#sys.maur,d0		 ; Aurora?
	bne	glv_rtsok		 ; ... no

	move.l	d3,d7			 ; colour depth mode required
	bpl.s	glvs_chkxy
	moveq	#0,d7
	move.b	pt_cdpth(a3),d7 	 ; current colour depth
glvs_chkxy
	move.l	d1,d0			 ; either x or y given
	or.l	d2,d0
	bne.s	glvs_xysize		 ; ... yes
	move.w	pt_xscrs(a3),d1 	 ; ... current x
	move.w	pt_yscrs(a3),d2 	 ; ... current y
glvs_xysize

	lea	au_modes,a2		 ; Aurora modes
	assert	0,ptd.ql,ptd.08-2
	adda.w	d7,a2
	adda.w	(a2),a2 		 ; table for this mode
	move.l	a2,-(sp)

glvs_xloop
	adda.l	#aum.size,a2
	cmp.l	#-1,(a2)		 ; last in list?
	beq.s	glvs_xeol		 ; yes, exit
	cmp.w	aum.xres(a2),d1 	 ; mode still fits?
	bls.s	glvs_xloop		 ; yes, check next

glvs_xeol
	move.w	aum.xres-aum.size(a2),d1 ; best fitting X res
	move.l	(sp)+,a2
glvs_look4x
	cmp.w	aum.xres(a2),d1 	 ; look for start of list with this X
	beq.s	glvs_yloop
	adda.l	#aum.size,a2
	bra.s	glvs_look4x

glvs_yloop
	adda.l	#aum.size,a2
	cmp.l	#-1,(a2)
	beq.s	glvs_found
	cmp.w	aum.xres(a2),d1 	 ; still same X?
	bne.s	glvs_found		 ; no, exit
	cmp.w	aum.yres(a2),d2 	 ; mode still fits?
	bls.s	glvs_yloop		 ; yes, check next

glvs_found
	suba.l	#aum.size,a2
	move.b	aum.nr(a2),d6		 ; internal mode number
	move.l	aum.xres(a2),d0 	 ; resolution
	cmp.l	pt_xscrs(a3),d0 	 ; new size?
	bne.s	glv_dochange		 ; ... yes, change it

	cmp.b	pt_cdpth(a3),d7 	 ; new colour depth?
	beq	glv_rtsok		 ; no, no change
glv_dochange
	move.l	pt_change(a3),a1
	jmp	(a1)

;+++
; Gold Card Aurora set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
glv_frrt thg_extn RATE,glv_blnk,glv_prat
	bra	disp_do

;+++
; Gold Card Aurora set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
glv_blnk thg_extn BLNK,,glv_pblnk
	bra	disp_do

	end
