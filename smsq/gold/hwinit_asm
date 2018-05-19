; SMSQ Q40 Hardware Initialisation V3
;
; 2003-09-27  2.00  Added OSPS config item (wl)
; 2018-03-08  2.01  Disable ABC keyboard as it caused problems (MK)

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
	dc.w	34,'Gold Card Initialisation for SMSQ '
	dc.l	'    '

glc_cx	 dc.b	0
glcxcpar dc.b	0
glc_sqr  dc.b	0
glcxser2 dc.b	0
glcxbflp dc.b	1
glcxbwin dc.b	1
glc_lang dc.w	44
glcxbtarg dc.b	0
glcxbpart dc.b	0
glc_scr  dc.b	0
glc_mode dc.b	0
glcxidisp dc.b	0,0,0,0,0,0,0,0,0,0
glc_nqimi dc.b	0
glc_clock dc.b	0
glcxxxx   dc.b	0,0
qcf_mlan  dc.w	44
qcf_kstuf dc.b	250			; 1c
qcf_curs  dc.b	0
glc.conf  equ	*-glc_cx

	mkcfhead {SMSQ},{smsq_vers}


	mkcfitem 'OSPL',word,0,glc_lang,,,\
       {Default Keyboard Language (33=F, 44=GB, 49=D, 39=IT etc.)},0,$7fff

	mkcfitem 'OSPM',word,'L',qcf_mlan,,,\
       {Default Messages Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff
			
	mkcfitem 'OSPi',code,0,glc_nqimi,,,\
       {Do you wish to ignore the QIMI interface},0,N,{No},$ff,Y,{Yes}

	mkcfitem 'OSPb',code,0,glc_mode,,,\
       {Initial colour depth},0,0,{QL},2,0,{256}

	mkcfitem 'OSPt',code,0,glc_scr,,,\
       {Initial display resolution},0,0,{512x256},1,0,{512x320}\
	2,0,{512x384},3,0,{512x480},4,0,{640x320},5,0,{640x480}\
	6,0,{768x384},7,0,{768x480},8,0,{768x576},9,0,{1024x480}\
	10,0,{1024x512},11,0,{1024x768}

	mkcfitem 'OSPk',code,0,glc_abck,,,\
       {ABC keyboard},1,M,{Maybe},0,N,{No},$ff,Y,{Yes}
							    
	mkcfitem 'OSPS',byte,'S',qcf_kstuf,,,\
       {Stuffer buffer key for edit line calls},0,$ff

	mkcfitem 'OSPc',code,'C',qcf_curs,,,\
       {Use sprite for cursor?} \
	0,N,{No},1,Y,{Yes}

				
	mkcfend

glc_abck dc.b	0
	ds.w	0

glc_wbase
	sf	glc_base+glo_rdis	 ; disable read (enable write)
	move.w	d0,(a5)+		 ; set
	sf	glc_base+glo_rena
	rts				 ; $10 bytes

sgc_wbase
	add.l	#sgc_woff,a5
	move.w	d0,(a5)+		 ; write to upper memory
	sub.l	#sgc_woff,a5
	rts				 ; $10 bytes

; copy module at (a0) to 4(a1)-(a2) linking to (a3) and updating a3 to a1

glc_mdinst
	sf	glc_base+glo_rdis	 ; disable read (enable write)
	move.l	a1,(a3)
	move.l	a1,a3
	clr.l	(a1)+
	sf	glc_base+glo_rena
glc_wbmloop
	move.w	(a0)+,d0
	sf	glc_base+glo_rdis	 ; disable read (enable write)
	move.w	d0,(a1)+		 ; set
	sf	glc_base+glo_rena
	cmp.l	a2,a1
	blt.s	glc_wbmloop
	rts

sgc_mdinst
	move.l	a2,-(sp)
	move.l	a1,(a3) 		 ; set link
	cmp.l	#$20000,a1		 ; protected?
	bgt.s	sgc_wbmset
	sub.l	a1,a2
	add.l	#sgc_woff,a1		 ; write to high memory
	add.l	a1,a2
sgc_wbmset
	move.l	a1,a3			 ; save link pointer
	clr.l	(a1)+
sgc_wbmloop
	move.w	(a0)+,(a1)+		 ; copy
	cmp.l	a2,a1
	blt.s	sgc_wbmloop
	move.l	(sp)+,a2
	rts




hwinit_base
	bra.s	hwinit
	dc.w	0

hwinit_hreset				; special hard reset entry
	move.w	#$2700,sr
	movem.l 0,a0/a1
	move.l	a0,a7
	jmp	(a1)			; hard reset = standard rom reset

hwinit
	move.l	sms.machine,d6		 ; assumed machine type
	tst.l	d7			 ; memory set?
	bne.s	hwi_peri		 ; ... yes, just initialise the peri

	moveq	#sys.mgold,d6		 ; 68000 / gold card
	move.w	#$3700,sr
	move.w	sr,d0
	bclr	#12,d0			 ; master bit set
	beq.s	hwi_chk_abc
	move.l	#$20000000+sys.msgld,d6  ; $20000000
	move.w	d0,sr			 ; clear master bit

	moveq	#0,d0			 ; disable cache
	pcreg	cacr			 ; and set

hwi_chk_abc
	move.b	glc_abck,d0		 ; ABC keyboard
	beq.s	hwi_not_abc		     ; ... no
	bmi.s	hwi_set_abc		     ; ... yes

	cmp.w	#'13',d4		 ; Minerva?
	bgt.s	hwi_chk_abcm		 ; ... yes
	cmp.w	#'10',d4		 ; JS?
	bne.s	hwi_not_abc		 ; ... no
	cmp.w	#$60,$b950		 ; patched to ABC?
	bne.s	hwi_not_abc		 ; no
	bra.s	hwi_set_abc		 ; ... yes

hwi_chk_abcm
	cmp.b	#$ff,kb.data		 ; is keyboard data FF?
	beq.s	hwi_not_abc		 ; ... yes, it must be empty EPROM

hwi_set_abc
	add.w	#kb.abc,d6		 ; add ABC flag to machine type
hwi_not_abc

	jsr	gl_disptype		 ; find the display type


hwi_peri
	clr.b	mc_stat 		 ; clear master chip status

	clr.b	pc_tctrl
	move.b	#pc.read,pc_mctrl	 ; mdv read mode
	moveq	#%00011111,d0
	move.b	d0,pc_intr		 ; mask/clear all interrupts
	move.b	d0,pc_intr

	move.w	#kb.abc,d0		 ; ABC keyboard?
	and.w	d6,d0
	beq.s	hwi_peridone		 ; ... no, peripherals done

	move.b	kb.ff,d1		 ; do a keyboard-reset
	move.l	#$3ffff,d1		 ; wait a short while
hwi_abc_inlp
	subq.l	#1,d1
	bne.s	hwi_abc_inlp
	move.b	kb.ff,d1		 ; read data!!! to finish reset
; beware - we must not get an external interrupt until server linked in!!

hwi_peridone
	tst.l	d7			 ; RAM set?
	beq.s	hwi_ramtest		 ; ... no
	rts

hwi_ramtest
  lea	  smsq_end,a0	     ; OS1 header
qmon_loop
  move.l  sbl_mbase(a0),d0     ; header length
  beq.s   qmon_done	       ; no more
  cmp.l   #'QMON',4(a0,d0.l)   ; qmon?
  beq.s   qmon_call
  add.l   sbl_mlength(a0),d0
  add.l   d0,a0 	       ; next header
  bra.s   qmon_loop
qmon_call
  lea	  8(a0,d0.l),a0
  moveq   #-1,d0
  jsr	  (a0)		       ; call qmon
qmon_done


	move.l	#$40000,a2		 ; search increment

	lea	smsq_end,a5
hwi_ramt_lend
	move.l	sbl_mbase(a5),d0	 ; header length
	beq.s	hwi_ramt_look		 ; no more
	add.l	sbl_mlength(a5),d0
	add.l	d0,a5			 ; next header
	bra.s	hwi_ramt_lend

hwi_ramt_look
	add.l	a2,a5			 ; round up end of last module
	move.l	a5,d0
	subq.l	#1,d0
	and.l	#$fffc0000,d0
	move.l	d0,a5			 ; search starts above

	move.l	a5,a4
	sub.l	a2,a4

	move.l	0,d0			 ; contents of address 0

hwi_ramt_loop
	add.l	a2,a4			 ; 64K pages

	cmp.l	(a4),d0 		 ; wrapped around to 0?
	beq.s	hwi_ramt_done		 ; ... yes
	move.l	a4,(a4) 		 ; flag this address

	move.l	a5,a1
hwi_ramt_check
	cmp.l	(a1),a1 		 ; flag correct?
	bne.s	hwi_ramt_done		 ; ... no, wrapped around
	add.l	a2,a1
	cmp.l	a4,a1			 ; up to end yet?
	ble.s	hwi_ramt_check		 ; ... no
	bra.s	hwi_ramt_loop

hwi_ramt_done
	lea	glc_wbase,a3		 ; assumed write routine
	lea	glc_mdinst,a6		 ; assumed module install
	moveq	#sys.mtyp,d0
	and.b	d6,d0
	cmp.b	#sys.msgld,d0
	bne.s	hwi_comms		 ; not super gold
	lea	sgc_wbase,a3		 ; super gold card write routine
	lea	sgc_mdinst,a6		 ; super gold card module install

hwi_comms
	move.l	(sp)+,a0		 ; return address

	clr.l	-(sp)			 ; facilities
	clr.l	-(sp)			 ; language
	move.l	d6,-(sp)		 ; processor, mmu/fpu, std disp
	move.l	#'GOLD',-(sp)		 ; GOLD card

	move.l	a4,d7			 ; ram top
	moveq	#0,d6			 ; no special RAM
	moveq	#0,d5

	move.l	sp,a5			 ; loader communications block
	lea	mod_table,a4		 ; module table
	lea	glc_cx,a2		 ; config block
	jmp	(a0)

; loader tables

mod_table
	dc.l	sms.base-4,$4000	 ; first slice
	dc.l	$4200-4,$c000		 ; second slice
	dc.l	$10020-4,$17d00 	 ; third slice (avoid 'Gold')
	dc.l	0

	end
