; q68 QL/Hi RES Mode Procs	  2000  Tony Tebby

	section init

	xdef	cn_procv

	xref	gu_tpadd
	xref	gu_thini
	xref	ut_procdef
	xref	pt_modex

	include 'dev8_keys_qlhw'
	include 'dev8_keys_q68'
	include 'dev8_keys_con'
	include 'dev8_keys_err'
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
	lea	q68_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	q68_xprocs,a1
	jsr	gu_tpadd

	lea	q68_procs,a1
	jmp	ut_procdef

q68_defs dc.l	th_name+2+4
	dc.l	q68_thing-*
	dc.l	'2.00'
q68_tnam dc.w	4,'QVME'

	section con

;******************************************************************************
;
; Here is the code to re-initialise the q68 display
;
;******************************************************************************


q68_xprocs
	prc_stt q68_tnam
	prc_def SIZE,Disp_size
	prc_def RATE,Disp_rate
	prc_def BLNK,Disp_blank
	prc_end

q68_procs
	proc_stt
	proc_ref DISP_MODE
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

disp_mode proc MODE
disp_type fun40 TYPE

	



q68_pclr
	 dc.w  thp.opt+thp.swrd+thp.nnul ; optional unsigned word
q68_psiz
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
q68_prat
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
q68_pblnk
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
q68_uwrd
	 dc.w  thp.opt+thp.uwrd  ; optional unsigned word
	 dc.w	0

q68_rword dc.w thp.ret+thp.swrd
	 dc.w	0

q68_thing

;+++
; Q68 find display type = 0 or 33
;     4(a1)	pointer to value
;---
q68_type thg_extn TYPE,q68_mode,q68_rword
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
; Q68 set display mode
;	(a1)	display mode
;---
q68_mode thg_extn MODE,q68_invr,q68_pclr
	moveq	#sms.xtop,d0
	trap	#1
	move.l	sys_clnk(a6),a3
	moveq	#7,d4
	and.b	q68_dmode,d4		; current mode
	move.l	(a1),d6 		; mode wished for
	blt.s	moderr
	cmp.l	d6,d4			; same mode we're already in?
	beq.s	done			; yes, done
	cmp.b	#q68.dmax,d6
	bgt.s	moderr			; this mode doesn't exist
	move.b	q68_dmode,d0		; are we right now in mode 8?
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

tab	dc.b	ptd.ql,ptd.ql,ptd.16,ptd.16,ptd.ql,ptd.08,ptd.16,ptd.16
	;	   0	1	2	3      4     5	   6	  7	  8



;+++
; invert display			**** does nothing
;     (a1)     pointer to 0/1
;---
q68_invr thg_extn INVR,q68_frrt,q68_uwrd
	bra.s	disp_do

;+++
;  set frame rate, line rate	     **** does nothing
;
;	(a1)	frame rate
;      4(a1)	line rate
;---
q68_frrt thg_extn RATE,q68_blnk,q68_prat
	bra.s	  disp_do

;+++
;  set blank			     **** does nothing
;
;	(a1)	x blank
;      4(a1)	y blank
;---
q68_blnk thg_extn BLNK,q68_colr,q68_pblnk
	bra.s	 disp_do

;+++
;  set colours			 **** does nothing
;
;	(a1)	x blank
;      4(a1)	y blank
;---
q68_colr thg_extn COLR,q68_size,q68_pblnk
	bra.s	 disp_do
 
;+++
;  set colours			 **** does nothing
;
;	(a1)	x blank
;      4(a1)	y blank
;---
q68_size thg_extn SIZE,,q68_pblnk

disp_inverse
disp_rate
disp_blank
disp_colour
disp_size
disp_do
	moveq	#0,d0
	rts

	end
