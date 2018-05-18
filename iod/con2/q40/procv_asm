; Q40 QL/Hi RES Mode Procs	 2000	Tony Tebby
; Added DISP_MODE	1.01	 W. Lenerz 2017 Nov 21

	section init

	xdef	cn_procv

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef

	include 'dev8_keys_q40'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_mac_proc'
	include 'dev8_mac_thg'
	include 'dev8_mac_basic'
	include 'dev8_mac_assert'

;+++
; initialise QVME Thing and procs
;---
cn_procv
	lea	q40_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	q40_xprocs,a1
	jsr	gu_tpadd

	lea	q40_procs,a1
	jmp	ut_procdef

q40_defs dc.l	th_name+2+4
	dc.l	q40_thing-*
	dc.l	'2.00'
q40_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; Here is the code to re-initialise the q40 display
;
;******************************************************************************

q40_xprocs
	prc_stt q40_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

q40_procs
	proc_stt
	proc_ref DISP_COLOUR
	proc_ref DISP_SIZE
	proc_ref DISP_RATE
	proc_ref DISP_MODE
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
disp_mode proc	MODE
disp_type fun40 TYPE

disp_rate
disp_blank
disp_inverse
disp_do
	moveq	#0,d0
	rts

q40_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
q40_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
q40_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
q40_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
q40_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

q40_rword dc.w thp.ret+thp.swrd
	 dc.w	0

q40_word dc.w thp.swrd
	 dc.w	0

q40_thing
;+++
; invert display
;     (a1)     pointer to 0/1
;---
q40_invr thg_extn INVR,q40_type,q40_uwrd
	bra.s	disp_do

;+++
; find display type = 0 or 33
;     4(a1)	pointer to value
;---
q40_type thg_extn TYPE,q40_mode,q40_rword
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
; Qx0 set display mode
;	(a1)	display mode
;---
q40_mode thg_extn MODE,q40_colr,q40_word
	moveq	#sms.xtop,d0
	trap	#1
	move.l	sys_clnk(a6),a3
	moveq	#3,d4			; max mode
	move.l	(a1),d6 		; mode wished for
	and.l	d4,d6
	move.b	q40_dmode,d0		; current mode
	and.b	d0,d4			;
	cmp.l	d6,d4			; same mode we're already in?
	beq.s	done			; yes, done
	tst.b	d0			; are we right now in mode 8?
	bne.s	cont			; no
	moveq	#4,d1			; yes, get back to mode 4
	moveq	#$10,d0
	trap	#1
cont	lea	tab,a1
	clr.l	d7
	move.b	(a1,d6.w),d7

	move.l	pt_change(a3),a1
	tst.l	d6			; mode 0 (ql mode 8) wished?
	beq.s	mode8			; yes
	jmp	(a1)

mode8	moveq	#1,d6
	jsr	(a1)			; first change into QL mode 4
	moveq	#8,d1
	moveq	#$10,d0
	trap	#1			; then set mode 8
done	clr.l	d0
	rts

;	 jmp	 (a1)
;	 lea	 pt_modex,a1

moderr
	moveq	#err.ipar,d0
	rts

tab	dc.b	ptd.ql,ptd.ql,ptd.16,ptd.16
	;	   0	1	2	3

       dc.w	0			; even out
;+++
; Q40 colour depth change
;	(a1)	colour depth code
;	then size
;	then rate
;	then blank
;---
q40_colr thg_extn COLR,q40_size,q40_pclr
q40.reg reg	d1/d2/d3/d4/d6/a0/a1/a2/a3/a4
	movem.l q40.reg,-(sp)
colr
	move.l	(a1)+,d3		; colour depth
	move.w	d3,d0
	beq.s	q40s_size
	subq.w	#ptd.16,d0
	beq.s	q40s_size
	bra.s	q40s_scolr

;+++
; Q40 size change
;	(a1)	x
;      4(a1)	y
;	then rate
;	then blank
;---
q40_size thg_extn SIZE,q40_frrt,q40_psiz
	movem.l q40.reg,-(sp)

q40s_scolr
	moveq	#-1,d3			; no colour depth change

q40s_size
	movem.l (a1),d1/d2

	bsr.s	q40s_do

	movem.l (sp)+,q40.reg
	moveq	#0,d0
q40s_rts
	rts



q40s_do
	moveq	#sms.xtop,d0
	trap	#1

	move.l	sys_clnk(a6),a3

	moveq	#3,d4
	and.b	q40_dmode,d4		 ; current mode

	move.l	d3,d7			 ; colour requested
	bpl.s	q40s_chkxy		 ; no colour given
	moveq	#0,d7
	move.b	pt_cdpth(a3),d7 	 ; current colour

q40s_chkxy
	move.l	d1,d0			 ; either x or y given
	or.l	d2,d0
	bne.s	q40s_xysize		 ; ... yes
	move.w	pt_xscrs(a3),d1 	 ; ... current x
	move.w	pt_yscrs(a3),d2 	 ; ... current y
q40s_xysize

	cmp.w	#$200,d1
	sgt	d1
	cmp.w	#$100,d2
	sgt	d2
	or.b	d1,d2
	beq.s	q40s_small		 ; small
	moveq	#ptd.16,d7		 ; large must be 16 bit colour
	assert	ptd.16,q40.dl
	move.b	d7,d6			 ; required mode
	bra.s	q40s_changeq

q40s_small
	assert	ptd.16-1,q40.ds 	 ; 3 xor 1 = 2 (small high colour)
	assert	ptd.ql+1,q40.d4 	 ; 0 xor 1 = 1 (ql mode 4)
	assert	0,q40.d8
	assert	ptm.ql8,8
	moveq	#-1-ptm.ql8,d6
	or.b	sys_qlmr(a6),d6 	 ; ff for mode 8, f7 for mode 4
	asr.b	#3,d6			 ; ff		  fe
	not.b	d6
	eor.b	d7,d6			 ; required mode

q40s_changeq
	cmp.b	d6,d4			 ; change mode?
	beq.s	q40s_rts
	move.l	pt_change(a3),a1
	jmp	(a1)			 ; ... yes, change it

;+++
; Q40 set frame rate, line rate
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
q40_frrt thg_extn RATE,q40_blnk,q40_prat
	bra	disp_do

;+++
; Q40 set blank
;
;	(a1)	x blank
;      4(a1)	y blank
;---
q40_blnk thg_extn BLNK,,q40_pblnk
	bra	disp_do

	end
