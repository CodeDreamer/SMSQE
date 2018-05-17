; Atari procedures for QL Mode Displays        1992  Tony Tebby

	section init

	xdef	cn_procv

	xref	cn_disp_size
	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_keys_sys'
	include 'dev8_keys_con'
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
	lea	atv_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	atv_xprocs,a1
	jsr	gu_tpadd

	lea	atv_procs,a1
	jmp	ut_procdef

atv_defs dc.l	th_name+2+4
	dc.l	atv_thing-*
	dc.l	'2.00'
atv_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; Here is the code to re-initialise the q40 display
;
;******************************************************************************

atv_xprocs
	prc_stt atv_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

atv_procs
	proc_stt
	proc_ref DISP_COLOUR
	proc_ref DISP_SIZE
	proc_ref DISP_RATE
	proc_ref DISP_BLANK
	proc_ref DISP_INVERSE
	proc_end
	proc_stt
	proc_ref DISP_TYPE
	proc_ref A_EMULATOR,disp_type
	proc_end

	proc_thg QVME
	fun40_thg QVME

disp_colour proc COLR
disp_size proc	SIZE
disp_rate proc	RATE
disp_blank proc BLNK
disp_type fun40 TYPE

disp_inverse
disp_do
	moveq	#0,d0
	rts

atv_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
atv_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
atv_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
atv_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
atv_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

atv_rword dc.w thp.ret+thp.swrd
	 dc.w	0

atv_thing
;+++
; invert display
;     (a1)     pointer to 0/1
;---
atv_invr thg_extn INVR,atv_type,atv_uwrd
	bra.s	disp_do

;+++
; find display type
;     4(a1)	pointer to value
;---
atv_type thg_extn TYPE,atv_colr,atv_rword
	moveq	#sms.xtop,d0
	trap	#do.smsq
	moveq	#0,d0
	move.b	sys_mtyp(a6),d0 ; display type in most sig two bits
	lsr.b	#6,d0
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	d0,(a1) 	; return it
	move.l	(sp)+,a1
	moveq	#0,d0
	rts


atv.reg reg	d5/d6/a1/a3/a4/a5

;+++
; AT colour depth change
;	(a1)	colour depth code
;	then size
;	then rate
;	then blank
;---
atv_colr thg_extn COLR,atv_size,atv_pclr
	movem.l atv.reg,-(sp)

	addq.l	#4,a1			 ; ignore colour depth
	bra.s	atvs_size

;+++
; AT VME bus / extended mode 4 size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
atv_size thg_extn SIZE,atv_frrt,atv_psiz
	movem.l atv.reg,-(sp)
atvs_size
	bsr.l	atv_setparm
	move.l	(a1)+,d5
	move.l	d5,(sp) 		 ; new x size
	swap	d5
	move.l	(a1)+,d0
	move.l	d0,4(sp)
	move.w	d0,d5
	bra.s	atv_frx 		 ; set frame rate

;+++
; AT VME set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
atv_frrt thg_extn RATE,atv_blnk,atv_prat
	movem.l atv.reg,-(sp)
	bsr.s	atv_setparm
	moveq	#0,d5			 ; no size change
atv_frx
	move.l	(a1)+,8(sp)		 ; new rates
	move.l	(a1)+,$c(sp)
	bra.s	atv_blx

;+++
; AT VME set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
atv_blnk thg_extn BLNK,,atv_pblnk
	movem.l atv.reg,-(sp)
	bsr.s	atv_setparm
	moveq	#0,d5			 ; no size change
atv_blx
	move.l	(a1)+,$10(sp)		 ; new blank
	move.l	(a1)+,$14(sp)

	move.l	sp,d6
	bsr.s	atv_change

	move.l	a5,sp
	movem.l (sp)+,atv.reg
	moveq	#0,d0
	rts

atv_change
	moveq	#sms.xtop,d0
	trap	#do.sms2

	move.l	sys_clnk(a6),a3 	 ; pointer context
	moveq	#0,d7			 ; ql colours
	move.l	d5,d0			 ; size change?
	beq.s	atv_disp_size		 ; ... no, call disp size directly
	cmp.l	pt_xscrs(a3),d5 	 ; same size?
	beq.s	atv_disp_size
	move.l	pt_change(a3),a4
	jmp	(a4)

atv_disp_size
	bra.l	cn_disp_size

atv_setparm
	move.l	(sp)+,a4
	move.l	sp,a5			 ; stack clean up
	moveq	#5,d0
atv_clrparm
	clr.l	-(sp)
	dbra	d0,atv_clrparm
	jmp	(a4)
	end
