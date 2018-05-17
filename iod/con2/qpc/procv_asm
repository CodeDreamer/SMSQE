; QPC QL/Hi RES Mode Procs	    2000  Tony Tebby
;				    2000  Marcel Kilgus
	section init

	xdef	cn_procv

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_smsq_qpc_keys'
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
	lea	qpc_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	qpc_xprocs,a1
	jsr	gu_tpadd

	lea	qpc_procs,a1
	jmp	ut_procdef

qpc_defs dc.l	th_name+2+4
	dc.l	qpc_thing-*
	dc.l	'2.00'
qpc_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; Here is the code to re-initialise the qpc display
;
;******************************************************************************

qpc_xprocs
	prc_stt qpc_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

qpc_procs
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

qpc_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
qpc_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
qpc_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
qpc_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
qpc_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

qpc_rword dc.w thp.ret+thp.swrd
	 dc.w	0

qpc_thing
;+++
; invert display
;     (a1)     pointer to 0/1
;---
qpc_invr thg_extn INVR,qpc_type,qpc_uwrd
	bra.s	disp_do

;+++
; find display type = 0, 16 or 32
;     4(a1)	pointer to value
;---
qpc_type thg_extn TYPE,qpc_colr,qpc_rword
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
; QPC colour depth change
;	(a1)	colour depth code
;	then size
;	then rate
;	then blank
;---
qpc_colr thg_extn COLR,qpc_size,qpc_pclr
qpc.reg reg	d1/d2/d3/d4/d6/a0/a1/a2/a3/a4
	movem.l qpc.reg,-(sp)

	move.l	(a1)+,d3		; colour depth
	move.w	d3,d0
	beq.s	qpcs_size
	cmp.w	#ptd.08,d0		; for Aurora compatible mode
	beq.s	qpcs_size
	cmp.w	#ptd.16,d0
	beq.s	qpcs_size
	bra.s	qpcs_scolr

;+++
; QPC size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
qpc_size thg_extn SIZE,qpc_frrt,qpc_psiz
	movem.l qpc.reg,-(sp)

qpcs_scolr
	moveq	#-1,d3			; no colour depth change

qpcs_size
	movem.l (a1),d1/d2

	bsr.s	qpcs_do

	movem.l (sp)+,qpc.reg
	moveq	#0,d0
qpcs_rts
	rts

qpcs_do
	moveq	#sms.xtop,d0
	trap	#1
	move.l	sys_clnk(a6),a3

	move.l	d3,d7			 ; colour requested
	bpl.s	qpcs_chkxy		 ; no colour given
	moveq	#0,d7
	move.b	pt_cdpth(a3),d7 	 ; current colour

qpcs_chkxy
	move.l	d1,d0			 ; either x or y given
	or.l	d2,d0
	bne.s	qpcs_xysize		 ; ... yes
	move.w	pt_xscrs(a3),d1 	 ; ... current x
	move.w	pt_yscrs(a3),d2 	 ; ... current y
qpcs_xysize
	move.w	d1,d6
	swap	d6
	move.w	d2,d6

qpcs_change
	cmp.b	pt_cdpth(a3),d7 	 ; is current colour requested?
	bne.s	qpcs_dochange		 ; no, change the colour

	cmp.l	pt_xscrs(a3),d6
	beq	qpcs_rts
qpcs_dochange
	move.l	pt_change(a3),a1
	jmp	(a1)			 ; change display mode / size

;+++
; qpc set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
qpc_frrt thg_extn RATE,qpc_blnk,qpc_prat
	bra	disp_do

;+++
; qpc set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
qpc_blnk thg_extn BLNK,,qpc_pblnk
	bra	disp_do

	end
