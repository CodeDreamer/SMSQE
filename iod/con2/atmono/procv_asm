; Atari procedures for mono display	   1992  Tony Tebby

	section init

	xdef	cn_procv

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_keys_atari'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_mac_proc'
	include 'dev8_mac_thg'
	include 'dev8_mac_basic'
	include 'dev8_mac_assert'


	section exten

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

disp_inverse proc INVR
disp_colour proc COLR
disp_size proc	SIZE
disp_rate proc	RATE
disp_blank proc BLNK
disp_type fun40 TYPE

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
	move.l	(a1),d0
	trap	#0
	move.w	d0,vdr_palt
	and.w	#$dfff,sr
	moveq	#0,d0
	rts

;+++
; find display type
;     4(a1)	pointer to value
;---
atv_type thg_extn TYPE,atv_colr,atv_rword
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	#4,(a1) 	     ; return 4
	move.l	(sp)+,a1
	moveq	#0,d0
	rts

;+++
; AT colour depth change
;	(a1)	colour depth code
;	then size
;	then rate
;	then blank
;---
atv_colr thg_extn COLR,atv_size,atv_pclr
	moveq	#0,d0
	rts

;+++
; AT VME bus / extended mode 4 size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
atv_size thg_extn SIZE,atv_frrt,atv_psiz
	moveq	#0,d0
	rts

;+++
; AT VME set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
atv_frrt thg_extn RATE,atv_blnk,atv_prat
	moveq	#0,d0
	rts

;+++
; AT VME set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
atv_blnk thg_extn BLNK,,atv_pblnk
	moveq	#0,d0
	rts

	end
