; SMSQ Q68 Hardware Initialisation Copyright (c) W. Lenerz 2016-2020
; inspired by the q40 hwinit routine

; 1.03 remove card init config item, add card speed items, check for hardware revision; stop ethernet interrupts
;      reset ethernet interrupt blocker after reset
;
; 1.02 added kbd delay config item

; Called with A7 a valid, safe stack
; The hardware is initialised.
; If d7 = 0	the RAM is setup.
;		It returns  pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    ROM or RES in d4  NO
;			    zero in d5
;			    zero in d6
;			    top of RAM in d7

	section hwinit

	xdef	sms_wbase
	xref	smsq_end

	include 'dev8_keys_q68'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_mac_multiconfig02'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_config_keys'

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
	dc.w	36,'Q68 Hardware Initialisation for SMSQ'
	dc.l	'    '

       section config

qcf_cx
qcf_mmu 	dc.b	 -1	; 00
qcf_par 	dc.b	 0	;
qcf_ser1	dc.b	 0	; 02
qcf_ser2	dc.b	 0	;
qcf_bflp	dc.b	 0	; 04
qcf_bwin	dc.b	 1	;
qcf_lang	dc.w	 49	; 06
qcf_targ	dc.b	 0	; 08
qcf_part	dc.b	 0	;
qcf_issize	dc.b	 0	; 0a
qcf_ismode	dc.b	 1	;
q68_fst1	dc.b	 0	; 0c card 1 use fast speed?
q68_pmse	dc.b	 0	; 0d mouse per poll(2) or interrupt (0)
q68_led 	dc.b	 1	; 0e led state after boot
q68_fst2	dc.b	 0	; 0f card 2 use fast speed?
qcf_fill	ds.b	 10	; 10  ... 19
qcf_mlan	dc.w	 49	; 1a
qcf_kstuf	dc.b	 250	; 1c
qcf_curs	dc.b	 1	; 1d
qcf_bgio	dc.b	 0	; 1e
qcf_ctrc	dc.b	 0	; 1f
q68_kbdy	dc.w	 0	; 20
q68_fast	dc.b	 0

	xref.l	smsq_vers

	mkcfstart

; SMSQ generic config items

	mkcfhead {SMSQ},{smsq_vers}

	  mkcfitem 'OSPM',word,'M',qcf_mlan,,,\
	  {Default Messages Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff

	  mkcfitem 'OSPL',word,'L',qcf_lang,,,\
	  {Default Keyboard Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff
				      
	  mkcfitem 'OSPS',byte,'S',qcf_kstuf,,,\
	  {Stuffer buffer key for edit line calls},0,$ff

noyes	  mkcfitem 'OSPU',code,'U',qcf_curs,,,\
	  {Use sprite for cursor?} \
	  0,N,{No},1,Y,{Yes}

	  mkcfitem 'OSPB',code,'B',qcf_bgio,,,\
	  {Enable CON background I/O},.noyes

	  mkcfitem 'OSPN',code,'N',qcf_ctrc,,,\
	  {Use new CTRL+C switch behaviour},.noyes

	mkcfblend

	mkcfhead {Q68},{smsq_vers}
				
	  mkcfitem 'OSPD',code,'D',qcf_ismode,,,\
	  {Initial display mode}\
	  q68.d4,4,{Normal QL Mode 4},q68.dl4,Q,{Large QL Mode 4},\
	  q68.aur8,A,{8 bit Aurora},\
	  q68.ds,S,{Small 16 bit},q68.md,M,{Medium 16 bit},q68.dl,L,{Large 16 bit}
	
	  mkcfitem 'Q68A',code,0,qcf_bwin,,,\
	  {Boot from}\
	  1,1,{WIN1},2,2,{WIN2},3,3,{WIN3},4,4,{WIN4}\
	  5,5,{WIN5},6,6,{WIN6},7,7,{WIN7},8,8,{WIN8}\
	  9,F,{FAT1},0,N,{None}

	  mkcfitem 'Q689',code,0,q68_led,,,\
	  {Switch LED off when SMSQ/E is set up?},.noyes
	  
	  mkcfitem 'Q68L',code,0,q68_fst1,,,\
	  {Card 1 : Use faster (40 MHz) SD Card speed if available?},.noyes

	  mkcfitem 'Q68M',code,0,q68_fst2,,,\
	  {Card 2 : Use faster (40 MHz) SD Card speed if available?},.noyes
				    
	mkcfblend

	mkcfend

; CONFIG IDs already used

; free	Q680
;	Q681 - Q688	WIN
;	Q689		here
;	Q68A		here
;	Q68B - Q68K	WIN
;	Q68L - Q68M	here
; free	Q68N - Q68P
;	Q68R - Q68Z	FAT
;	Q68a - Q68i	FAT
; free	Q68j - Q68z
;	q681 - q689	QUB
;	q68J - q68Q	QUB

	section hwinit

; copy module at (a0) to 4(a1)-(a2) linking to (a3) and updating a3 to a1

q68_mdinst
	move.l	a1,(a3)
	move.l	a1,a3
	clr.l	(a1)+
q68_mdloop
	move.w	(a0)+,(a1)+		 ; copy module
	cmp.l	a2,a1
	blt.s	q68_mdloop
	rts

; start of code after a reset

hwinit_base				; this is the start after loading the OS from the card
					; in that case it is called from smsq_smsq_loader_asm
	bra.s	hwinit			; this is also what gets called after a "reset" command
					; in that case it's loaded at label ldm_softrset

	dc.w	0			; leave this or make the bra long !

hwinit_hreset				; special hard reset entry
	move.l	#-1,a4			; no mem
	move.l	sms.sysb,a6		; sysvars
	jmp	sms.base		; do warm reset
hwinit
	st	led			; LED on
	sf	q68_ethi		; block CP2200 interrupts v. 1.03

	clr.b	kbd_unlock		; no key may be got
	st	pc_intr 		; NOT CLEAR?????
	clr.b	pc_tctrl		; clear transmit control reg

	move.l	#q68_sramb+4,q68_sramb	; first free space in fast sram mem
	move.b	sms.conf+sms_ismode,d0	; initial screen mode
	tst.l	d7
	bne.s	hw_rev
	move.b	qcf_ismode,d0

; test for hardware revision
hw_rev	sf	mmc1_40hz		; if v1, this doesn't change value
	st	mmc1_40hz
	sf	mmc1_40hz
	st	mmc1_40hz		; fast toggle

	tst.b	mmc1_40hz		; should be NE
	beq.s	vers1			; but it isn't -> v1
	sf	mmc1_40hz
	tst.b	mmc1_40hz		; now should be 0
	bne.s	vers1			; didn't change status - v1
	st	q68_v2			; signal v2
	move.b	q68_fst1,mmc1_40Hz
	move.b	q68_fst2,mmc2_40Hz

vers1	move.b	d0,q68_dmode		; set initial screen mode
	subq.b	#1,d0			; small ql mode?
	beq.s	periok			; yes, no need to clear screen here
	moveq	#0,d0
	move.l	#q68_screen,a1
	move.l	a1,a0
	add.l	#q68.screen-4,a0

periok	clr.l	d0

	move.l	#$32000,0
	move.l	#$904,4
	tst.l	d7			; RAM set?
	beq.s	hwi_mem 		; no->
nol	rts
	    
hwi_mem move.l	(sp)+,a0		; return address

	clr.l	-(sp)			; facilities
	clr.l	-(sp)			; language
	move.l	#sys.mq68,-(sp) 	; processor, mmu/fpu, std disp
					; plain 68000,ql screen, Q68
	move.l	#'Q68 ',-(sp)		; Q68
	move.l	#28*1024*1024,d7	; provisional ramtop  - 28 MiB
	moveq	#0,d6			; no special RAM
	moveq	#0,d5
	lea	q68_mdinst,a6		; q68 version of module installation
	move.l	sp,a5			; loader communications block
	lea	mod_table,a4		; module table
	lea	sms_wbase,a3		; write to base

	lea	qcf_bwin,a2		; in config block
	cmp.b	#9,(a2) 		; boot from fat 1?
	bne.s	cont			; no
	move.b	#1,-1(a2)		; yes, use as FLP
cont	lea	qcf_cx,a2		; config block
nol2	jmp	(a0)

; smsq write to base area routine

sms_wbase
	move.w	d0,(a5)+
	rts

; loader tables

mod_table
	dc.l	sms.base-4,$4000	 ; first slice
	dc.l	$4200-4,$c000		 ; second slice
	dc.l	$10000,$17d00-4 	 ; third slice
	dc.l	0

	end
