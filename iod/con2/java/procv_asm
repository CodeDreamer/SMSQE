; Java initialise displays.
;
; based on
;
; Gold Card initialise QL Mode Displays 	1.01   1992  Tony Tebby  QJUMP
;

	section init

	xdef	cn_procv


	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_keys_java'
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
	lea	jva_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	jva_xprocs,a1		; thing extensions
	jsr	gu_tpadd

	lea	jva_procs,a1		; sbasic extens
	jmp	ut_procdef

jva_defs dc.l	th_name+2+4
	dc.l	jva_thing-*
	dc.l	'2.00'
jva_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; MOST OF THESE ROUTINES DON'T DO ANYTHING, EXCEPTO FOR DISP_TYPE
;
;******************************************************************************

jva_xprocs
	prc_stt jva_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate		; this doesn't do  anything, keep for compatibility
	prc_def BLNK,Disp_blank 	; same
	prc_end

jva_procs
	proc_stt
	proc_ref DISP_COLOUR
	proc_ref DISP_SIZE
	proc_ref DISP_RATE		; this doesn't do  anything, keep for compatibility
	proc_ref DISP_BLANK		; this doesn't do  anything, keep for compatibility
	proc_ref DISP_INVERSE		; this doesn't do  anything, keep for compatibility
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

jva_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
jva_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
jva_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
jva_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
jva_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

jva_rword dc.w thp.ret+thp.swrd
	 dc.w	0

jva_thing
;+++
; invert display			**** does nothing
;     (a1)     pointer to 0/1
;---
jva_invr thg_extn INVR,jva_type,jva_uwrd
	bra.s	disp_do



;+++
; jva set frame rate, line rate 	**** does nothing
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
jva_frrt thg_extn RATE,jva_blnk,jva_prat
	bra.s	  disp_do

;+++
; jva set blank 			**** does nothing
;
;	(a1)	x blank
;      4(a1)	y blank
;---
jva_blnk thg_extn BLNK,,jva_pblnk
	bra.s	 disp_do



;+++
; find display type = 0 (QL) or 32  (16 bit colour)
;     4(a1)	pointer to value
;---
jva_type thg_extn TYPE,jva_colr,jva_rword
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
; jva colour depth change		**** does nothing
;	(a1)	colour depth code
;	then size
;	then rate    (ignored)
;	then blank   (ignored)
;---
jva_colr thg_extn COLR,jva_size,jva_pclr
jva.reg reg	d1/d2/d3/d4/d6/a0/a1/a2/a3/a4

	bra    disp_do		       ; NIY

	movem.l jva.reg,-(sp)

	move.l	(a1)+,d3		; colour depth (0 or 32)
	move.w	d3,d0
	beq.s	jvas_size
	subq.w	#ptd.16,d0
	beq.s	jvas_size
	bra.s	jvas_scolr

;+++
; jva size change			**** does nothing
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
jva_size thg_extn SIZE,jva_frrt,jva_psiz

	bra    disp_do		       ; NIY

	movem.l jva.reg,-(sp)

jvas_scolr
	moveq	#-1,d3			; no colour depth change

jvas_size
	movem.l (a1),d1/d2

	bsr.s	jvas_do

	movem.l (sp)+,jva.reg
	moveq	#0,d0
jvas_rts
	rts



jvas_do
	moveq	#sms.xtop,d0
	trap	#1

	move.l	sys_clnk(a6),a3

	moveq	#3,d4
 ;;	  and.b   jva_dmode,d4		   ; current mode

	move.l	d3,d7			 ; colour requested
	bpl.s	jvas_chkxy		 ; no colour given
	moveq	#0,d7
	move.b	pt_cdpth(a3),d7 	 ; current colour

jvas_chkxy
	move.l	d1,d0			 ; either x or y given
	or.l	d2,d0
	bne.s	jvas_xysize		 ; ... yes
	move.w	pt_xscrs(a3),d1 	 ; ... current x
	move.w	pt_yscrs(a3),d2 	 ; ... current y
jvas_xysize

	cmp.w	#$200,d1
	sgt	d1
	cmp.w	#$100,d2
	sgt	d2
	or.b	d1,d2
	beq.s	jvas_small		 ; small
	moveq	#ptd.16,d7		 ; large must be 16 bit colour
    ;;	  assert  ptd.16,jva.dl
	move.b	d7,d6			 ; required mode
	bra.s	jvas_changeq

jvas_small
   ;	 assert  ptd.16-1,jva.ds	  ; 3 xor 1 = 2 (small high colour)
   ;	 assert  ptd.ql+1,jva.d4	  ; 0 xor 1 = 1 (ql mode 4)
   ;	 assert  0,jva.d8
   ;	 assert  ptm.ql8,8
	moveq	#-1-ptm.ql8,d6
	or.b	sys_qlmr(a6),d6 	 ; ff for mode 8, f7 for mode 4
	asr.b	#3,d6			 ; ff		  fe
	not.b	d6
	eor.b	d7,d6			 ; required mode

jvas_changeq
	cmp.b	d6,d4			 ; change mode?
	beq.s	jvas_rts
	move.l	pt_change(a3),a1
	jmp	(a1)			 ; ... yes, change it
	    


	end
