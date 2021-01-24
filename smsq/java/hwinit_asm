; SMSQmulator "Hardware" Initialisation V2.01  (c) W. Lenerz

; based on T. Tebby's code
;
* 2003-09-27		v.2.00 (?)	added OSPS config item (wl)
* 2015 May 24		  2.01		added config item for ieee floats  (wl)


; Called with a7 a valid, safe stack
;	      d4 the host (QDOS) version number
; The hardware is initialised.
; If d7 = 0	The caches are invalidated
;		The RAM is setup.
;		It returns  pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    zero in d5
;			    zero in d6
;			    top of RAM in d7
;
; at the end of this routine, we must have :
;			    pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    stack pointer in a7 (usually just below sysvar)
;			    d4 may be 'ROM ' or 'RES ' (if set on call)
;			    if d7 HI RAM, d5 is top of base RAM, otherwise 0
;			    if d7 HI RAM, d6 is base of HI RAM, otherwise 0
;			    top of RAM in d7


	section hwinit

	xref	smsq_end
	xref.l	smsq_vers
	xref	gl_disptype

	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_smsq_gold_keys'
	include 'dev8_smsq_gold_kbd_abc_keys'
	include 'dev8_mac_config02'
	include 'dev8_mac_assert'
	include 'dev8_mac_creg'
	include 'dev8_keys_java'
	include 'dev8_keys_con'

header_base
	dc.l	hwinit_base-header_base  ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-hwinit_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	hwinit_name-*

hwinit_name
	dc.w	34,'Java Emul Initialisation for SMSQ '
	dc.l	'    '
	dc.l	jva_cfgf1
	dc.l	jva_cfgf2
	dc.l	jva_cfgf3
	dc.l	jva_cfgf4
	dc.l	'SMSQ'
	dc.l	'XqXq'
	dc.l	'3.37'
	dc.l	'0000'
glc_cx	 dc.b	-1
glcxcpar dc.b	-1
glc_sqr  dc.b	0
glcxser2 dc.b	0
glcxbflp dc.b	0
glcxbwin dc.b	1
glc_lang dc.w	44
glcxbtarg dc.b	1
glcxbpart dc.b	0
glc_scr  dc.b	0
glc_mode dc.b	ptd.16
glcxidisp dc.b	0,0,0,0,0,0,0,0,0,0
glc_nqimi dc.b	1			; use less cpu when idle? (used for qimi on gold card)
glc_clock dc.b	0
glcxxxx   dc.b	0,0			; reset for small mem
qcf_mlan  dc.w	44			; default message language
qcf_kstuf dc.b	250			; 1c = stuffer buffer key
qcf_curs  dc.b	0			; sprite as cursor?
qcf_bgio  dc.b	1			; background IO?
qcf_ctlc  dc.b	0			; new ctrl c bahaviuor?
glc.conf  equ	*-glc_cx

	mkcfhead {SMSQ},{smsq_vers}


	mkcfitem 'OSPM',word,'L',qcf_mlan,,,\
       {Default Messages Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff
			
;*	  mkcfitem 'OSPK',code,0,glc_mode,,,\
;*	 {Initial colour depth},0,0,{QL},2,0,{16-bit}

	mkcfitem 'OSPS',byte,'S',qcf_kstuf,,,\
       {Stuffer buffer key for edit line calls},0,$ff

noyes	mkcfitem 'OSPC',code,'C',qcf_curs,,,\
       {Use sprite for cursor?} \
	0,N,{No},1,Y,{Yes}

	mkcfitem 'OSPB',code,'B',qcf_bgio,,,\
       {Enable CON background I/O},.noyes


	mkcfitem 'OSPD',code,'S',qcf_ctlc,,,\
       {Use new CTRL+C switch behaviour},.noyes

	mkcfitem 'OSPF',code,'F',glcxbtarg,,,\
       {Use ieee floating point routines},.noyes
	
	mkcfend
	dc.l	0

; copy module at (a0) to 4(a1) up to (a2), linking to (a3) and updating a3 to a1

jav_mdinst
	move.l	a1,(a3)
	move.l	a1,a3
	clr.l	(a1)+
jav_wbmloop
	move.w	(a0)+,(a1)+	       ; set
	cmp.l	a2,a1
	blt.s	jav_wbmloop
	rts

; this is called from soft & hard reset.
; hard reset : d7 = 0
; soft reset: d7<>0
hwinit_base				; this is called from soft & hard reset
	bra.s	hwinit
	dc.w	0

hwinit	tst.l	d7			; soft or hard reset?
	beq.s	hwinit2 		; hwinit2 : hard reset
	moveq	#2,d0			; trap index = reset cpu (do hard reset)
	dc.w	jva.trp5		; reset CPU, (do hard reset)
hwinit2 move.l	a7,d7			; top of ram
	sub.l	#jva.ssp,d7		; minus space for ssp = new ramptop

	move.l	 #sys.java+sys.mqlc,d6	; 68000 / java + disp type
	move.w	#$2700,sr		; stop interrupts
hwi_comms
	move.l	(sp)+,a0		; return address

	clr.l	-(sp)			; facilities
	clr.l	-(sp)			; language
	move.l	d6,-(sp)		; processor, mmu/fpu, std disp
	move.l	#'JAVA',-(sp)		; this is a java machine emulator

	clr.l	d6			; no special RAM
	clr.l	d5

	move.l	sp,a5			; loader communications block
	lea	glc_cx,a2		; config block
	lea	sms_wbase,a3		; java emul write routine
	lea	mod_table,a4		; module table
					; a5 is already set
	lea	jav_mdinst,a6		; java emul module install

	jmp	(a0)			; go back (to loader_asm  after label loader base


sms_wbase
	move.w	d0,(a5)+		; write in "difficult" areas
	rts



; loader tables

mod_table

	dc.l	sms.base-4,sms.base
	dc.l	0			; load everything in one slice

;	 dc.l	 sms.base-4,$4000	  ; first slice
;	 dc.l	 $4200-4,$c000		  ; second slice
;	 dc.l	 $10020-4,$17d00	  ; third slice (avoid 'Gold')
;	 dc.l	 0

	end
