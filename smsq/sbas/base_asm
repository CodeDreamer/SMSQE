; SBAS_BASE - SBASIC Base  V2.02    1992  Tony Tebby  QJUMP

vec	macro	routine
	xref	[routine]
	dc.w	[routine]-*
	endm

	section header

	xref	smsq_end

header_base
	dc.l	sb_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-sb_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	 6,'SBASIC'
	dc.l	'    '
	dc.w	$200a

	xref	sb_main 		 ; to get it all loaded
	xref	sb_initv
	xref	sb_revect
	xref	sb_thing
	xref	sb_fileinfo
	xref	sb_qdthing
	xref	sb_job

	include 'dev8_keys_qlv'
	include 'dev8_keys_68000'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'

;+++
; The base routine for SBASIC initialisation
;
;	a6 cr	base of job / pointer to SBASIC variables
;---
sb_base

; first the nasty bit, the QL vectors

	bsr.s	sbi_nasty

	jsr	sb_initv		 ; initialise SBASIC variables
	jsr	sb_thing		 ; and SBASIC Thing
	jsr	sb_fileinfo		 ; and fileinfo thing
	jmp	sb_qdthing		 ; and QD thing

sbi_nasty
	moveq	#sms.xtop,d0
	trap	#do.sms2

	lea	sb_job,a0
	lea	sms.sbjob,a5
	bsr.s	sb_wba0 		 ; set address of SBASIC job

	lea	sb_vector,a2

sb_qlva
	move.w	(a2)+,d2		 ; next group
	beq.s	sb_vspecial		 ; done
	move.w	(a2)+,d1		 ; vector offset
	move.w	d2,a1			 ; ql vector
	add.w	d2,d2
	lea	qlv.off(a1),a5		 ; staging post for this vector
	add.w	d2,a5			 ; = offset + 4* QLV
sb_qlv
	move.l	a2,a0
	move.w	(a2)+,d2
	beq.s	sb_qlva

	add.w	d2,a0			 ; address of routine

	move.w	d1,d0			 ; vector offset
	add.w	a5,d0			 ; offset vector
	bsr.s	sb_wba1 		 ; set it

	bsr.s	sb_veca0		 ; set vector
	bra.s	sb_qlv

sb_veca0
	move.w	#jmp.l,d0		 ; jmp
	bsr.s	sb_wbase
sb_wba0
	move.l	a0,d0			 ; to a0
sb_wbd0
	swap	d0
	bsr.s	sb_wbase
	swap	d0
sb_wbase
	jmp	sms.wbase

sb_wba1
	exg	a5,a1
	bsr.s	sb_wbase
	exg	a5,a1
	rts

sb_vspecial
	lea	ut.cstr,a1		 ; the old cstr vector
	move.w	(a1),a2 		 ; points to here
	addq.l	#2,a2			 ; the JMP address
	move.l	(a2),a0
	subq.l	#6,a0
	bsr.s	sb_veca0		 ; naughty QLIB CSTR entry
	move.w	a5,d0
	exg	a5,a1
	bsr.s	sb_wbase		 ; new CSTR vector
	exg	a5,a1
	move.l	(a2),a0 		 ; real CSTR entry
	bsr.s	sb_veca0

	lea	sb_revect,a2		 ; reserve space vector
	move.w	(a2)+,d0		 ; offset from start of stage post
	move.w	(a2)+,d2		 ; stagepost code to copy
	move.w	(a2)+,d3		 ; amount of vectors

	add.w	a5,d0			 ; entry to stagepost
	lea	qa.resri,a1
	bsr.s	sb_wba1 		 ; set it

sb_cpyres
	move.w	(a2)+,d0		 ; copy stage post code
	bsr.s	sb_wbase
	subq.w	#2,d2
	bgt.s	sb_cpyres

sb_cpyrev
	move.w	(a2)+,d0		 ; jmp
	bsr.s	sb_wbase
	move.l	a2,d0
	add.l	(a2)+,d0		 ; absolute address
	bsr.s	sb_wbd0
	subq.w	#6,d3
	bgt.s	sb_cpyrev

	moveq	#0,d0
sb_rts
	rts

sb_vector

; Open Window utilities

	dc.w	opw.wind,0

 vec uq_opwin	 $00c4		; Open WINDow using name
 vec uq_opcon	 $00c6		; Open CONsole
 vec uq_opscr	 $00c8		; Open SCReen

; Error handling utilities

 vec uq_wersy	 $00ca		; Write an ERror to SYstem window
 vec uq_werms	 $00cc		; Write an ERror MeSsage
 vec uq_wint	 $00ce		; Write an INTeger
 vec uq_wtext	 $00d0		; Write TEXT

	dc.w	0

; date

	dc.w	cv.datil,0

 vec uq_datel	 $00d6		; date (6 word) to long

	dc.w	0

; string utilities

	dc.w	ut.cstr,0

 vec uq_cstr	 $00e6		; Compare STRings

	dc.w	0

; Conversions

	dc.w	cv.ildat,0

 vec uq_ldate	 $00ec		; Integer (Long) to DAte and Time string
 vec uq_ldowk	 $00ee		; Integer (Long) to DAY string
 vec cr_fpdec	 $00f0		; Floating Point to ascii DECimal
 vec cr_iwdec	 $00f2		; integer (word) to ascii decimal
 vec cr_ibbin	 $00f4		; integer (byte) to ascii binary
 vec cr_iwbin	 $00f6		; integer (word) to ascii binary
 vec cr_ilbin	 $00f8		; integer (long) to ascii binary
 vec cr_ibhex	 $00fa		; integer (byte) to ascii hexadecimal
 vec cr_iwhex	 $00fc		; integer (word) to ascii hexadecimal
 vec cr_ilhex	 $00fe		; integer (long) to ascii hexadecimal
 vec cr_decfp	 $0100		; decimal to floating point
 vec cr_deciw	 $0102		; decimal to integer word
 vec cr_binib	 $0104		; binary ascii to integer (byte)
 vec cr_biniw	 $0106		; binary ascii to integer (word)
 vec cr_binil	 $0108		; binary ascii to integer (long)
 vec cr_hexib	 $010a		; hexadecimal ascii to integer (byte)
 vec cr_hexiw	 $010c		; hexadecimal ascii to integer (word)
 vec cr_hexil	 $010e		; hexadecimal ascii to integer (long)

; SuperBASIC interpreter

 vec sb_inipr	 $0110		; INITialise PRocedure table
 vec sb_gtint	 $0112		; GeT INTeger
 vec sb_gtfp	 $0114		; GeT Floating Point
 vec sb_gtstr	 $0116		; GeT STRing
 vec sb_gtlin	 $0118		; GeT Long INteger
 vec sb_resar	 $011a		; REServe arithmetic (RI) stack
 vec qr_op	 $011c		; QL Arithmetic OPeration
 vec qr_mop	 $011e		; QL Arithmetic Multiple OPeration
 vec sb_putp	 $0120		; PUT Parameter

	dc.w	0

; Parser

	dc.w	sb.parse,-$4000

 vec sb_parse	 $012c		; parse
 vec sb_graph	 $012e		; main graph
 vec sb_expgr	 $0130		; expression graph
 vec sb_strip	 $0132		; strip spare spaces from line
 vec sb_paerr	 $0134		; re-process error line to MISTake
 vec sb_ledit	 $0136		; line edit
 vec sb_expnd	 $0138		; expand / print lines
 vec sb_paini	 $013a		; initialise parser

	 dc.w	0
	 dc.w	0

	end
