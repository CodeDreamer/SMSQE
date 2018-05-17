; QXL QL/Hi RES Mode Procs	    2000  Tony Tebby
; 2005.01.10	1.01	useless weeded out (BC)
	section init

	xdef	cn_procv

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_smsq_qxl_keys'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'
	include 'dev8_mac_proc'
	include 'dev8_mac_thg'
	include 'dev8_mac_basic'
	include 'dev8_mac_assert'


;+++
; initialise QVME Thing and procs
;---
cn_procv
	lea	qxl_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	qxl_xprocs,a1
	jsr	gu_tpadd

	lea	qxl_procs,a1
	jmp	ut_procdef

qxl_defs dc.l	th_name+2+4
	dc.l	qxl_thing-*
	dc.l	'2.00'
qxl_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; Here is the code to re-initialise the qxl display
;
;******************************************************************************

qxl_xprocs
	prc_stt qxl_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

qxl_procs
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
	moveq	#0,d0
	rts

qxl_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
qxl_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
qxl_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
qxl_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
qxl_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

qxl_rword dc.w thp.ret+thp.swrd
	 dc.w	0

qxl_thing
;+++
; invert display
;     (a1)     pointer to 0/1
;---
qxl_invr thg_extn INVR,qxl_type,qxl_uwrd
	bra.s	disp_do

;+++
; find display type = 0 or 32
;     4(a1)	pointer to value
;---
qxl_type thg_extn TYPE,qxl_colr,qxl_rword
	moveq	#sms.dmod,d0	; get mode
	moveq	#-1,d1
	moveq	#-1,d2
	trap	#do.smsq
	and.w	#$00ff,d1
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	d1,(a1) 	; return it
	move.l	(sp)+,a1
	moveq	#0,d0
	rts

;+++
; QXL colour depth change
;	(a1)	colour depth code
;	then size
;	then rate
;	then blank
;---
qxl_colr thg_extn COLR,qxl_size,qxl_pclr
qxl.reg reg	d1/d2/d3/d4/d6/a0/a1/a2/a3/a4
	movem.l qxl.reg,-(sp)

	move.l	(a1)+,d3		; colour depth
	move.w	d3,d0
	beq.s	qxls_size
	subq.w	#ptd.16,d0
	beq.s	qxls_size
	bra.s	qxls_scolr

;+++
; QXL size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
qxl_size thg_extn SIZE,qxl_frrt,qxl_psiz
	movem.l qxl.reg,-(sp)

qxls_scolr
	moveq	#-1,d3			; no colour depth change

qxls_size
	movem.l (a1),d1/d2

	bsr.s	qxls_do

	movem.l (sp)+,qxl.reg
	moveq	#0,d0
qxls_rts
	rts



qxls_do
	moveq	#sms.xtop,d0
	trap	#1
	move.l	qxl_scr_work,a2 	 ; screen work area
	move.l	sys_clnk(a6),a3

	move.l	d3,d7			 ; colour requested
	bpl.s	qxls_chkxy		 ; no colour given
	moveq	#0,d7
	move.b	pt_cdpth(a3),d7 	 ; current colour

qxls_chkxy
	move.l	d2,d0			 ; either x or y given
	or.l	d1,d0
	bne.s	qxls_xysize		 ; ... yes
	move.w	pt_xscrs(a3),d1 	 ; ... current x
	move.w	pt_yscrs(a3),d2 	 ; ... current y
qxls_xysize
	divu	#3,d2
	mulu	#4,d2
	cmp.l	d1,d2
	bge.s	qxls_check
	move.l	d1,d2			; v. 1.01

qxls_check
	moveq	#2,d6			 ; assume giant
	cmp.l	#800,d2
	bgt.s	qxls_change		 ; it is
	moveq	#1,d6
	cmp.l	#640,d2
	bgt.s	qxls_change		 ; 800x600
	moveq	#0,d6
	cmp.l	#512,d2
	bgt.s	qxls_change		 ; 640x480
	tst.b	d7			 ; QL mode?
	seq	d6			 ; ... size -1 only for QL mode

qxls_change
	cmp.b	pt_cdpth(a3),d7 	 ; is current colour requested?
	bne.s	qxls_dochange		 ; no, change the colour

	cmp.b	qxl_vsize(a2),d6
	beq	qxls_rts
qxls_dochange
	move.l	pt_change(a3),a1
	jmp	(a1)			 ; change display mode / size


;+++
; qxl set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
qxl_frrt thg_extn RATE,qxl_blnk,qxl_prat
	bra	disp_do

;+++
; qxl set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
qxl_blnk thg_extn BLNK,,qxl_pblnk
	bra	disp_do

	end
