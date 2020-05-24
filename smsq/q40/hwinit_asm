; This version allows assembly by GWASS and Qmac
; DEV7 must point to either the GWAS macros or the Qmac macros as appropriate
; The changes to make this work for Q40 as well as v3.10 are marked **** NEW
; and start at hwi_ram_over. There two imstructions and a label.

; You will also note a slight change at hwinit


; SMSQ Q40 Hardware Initialisation V3
; Called with A7 a valid, safe stack
; The caches are invalidated
; The hardware is initialised.
; If d7 = 0	the RAM is setup.
;		$0003,d4 is written to address 0: 'ROM' or 'RES'
;		It returns  pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    ROM or RES in d4
;			    zero in d5
;			    zero in d6
;			    top of RAM in d7
* 2003-09-27	v. 2.00 (?)	added OSPS config item
* 2005-01-10	V. 2.01 	use non cached modes for startup again (WL)
* 2005-11-12	V. 2.02 	better cache handling (TG)
* 2018-03-30	V. 2.03 	new config tem (boot from floppy?)
*				don't use trap#0 at 2.02


	section hwinit

	xdef	sms_wbase

	xref	q40_prtype
	xref	smsq_end

	include 'dev8_keys_q40'
	include 'dev8_keys_q40_multiIO'
	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_mac_config02'
	include 'dev8_mac_assert'
	include 'dev8_mac_creg'
	include 'dev8_mac_switch'

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
	dc.w	36,'Q40 Hardware Initialisation for SMSQ'
	dc.l	'    '



qcf_cx
qcf_mmu 	dc.b	 -1	; 00
qcf_par 	dc.b	 0	;
qcf_ser1	dc.b	 0	; 02
qcf_ser2	dc.b	 0	;
qcf_bflp	dc.b	 0	; 04
qcf_bwin	dc.b	 1	;
qcf_lang	dc.w	 44	; 06
qcf_targ	dc.b	 0	; 08
qcf_part	dc.b	 0	;
qcf_issize	dc.b	 0	; 0a
qcf_ismode	dc.b	 1	;
qcf_fill	ds.b	 14	; 0c ... 19
qcf_mlan	dc.w	 44	; 1a
qcf_kstuf	dc.b	250	; 1c
qcf_curs	dc.b	1	; 1d
qcf_ctrc	dc.b	0	; 1e
qcf_bgio	dc.b	0	; 1f


	xref.l	smsq_vers
	mkcfhead {SMSQ},{smsq_vers}

	mkcfitem 'OSPL',word,'L',qcf_lang,,,\
       {Default Keyboard Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff

	mkcfitem 'OSPD',code,'D',qcf_ismode,,,\
       {Initial display mode}\
	1,4,{QL Mode 4},2,S,{Small 16 bit},3,L,{Large 16 Bit}

	mkcfitem 'OSPM',code,'M',qcf_mmu,,,\
       {Set up MMU for QLiberator}\
	0,N,{No},$ff,Y,{Yes}

	mkcfitem 'OSPF',code,'M',qcf_bflp,,,\
       {Try to boot from floppy}\
	0,N,{No},$1,Y,{Yes}
      
	mkcfitem 'OSPK',word,'L',qcf_mlan,,,\
       {Default Messages Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff

	mkcfitem 'OSPS',byte,'S',qcf_kstuf,,,\
       {Stuffer buffer key for edit line calls},0,$ff

noyes  mkcfitem 'OSPC',code,'C',qcf_curs,,,\
       {Use sprite for cursor?} \
	0,N,{No},1,Y,{Yes}


	mkcfitem 'OSPB',code,'B',qcf_bgio,,,\
       {Enable CON background I/O},.noyes


	mkcfitem 'OSPD',code,'S',qcf_ctrc,,,\
       {Use new CTRL+C switch behaviour},.noyes


	mkcfend
	ds.w	0

; copy module at (a0) to 4(a1)-(a2) linking to (a3) and updating a3 to a1

q40_mdinst
	move.l	d0,d0			 ; debugger precaution
	st	q40_lowrom		 ; write to RAM
	move.l	a1,(a3)
	move.l	a1,a3
	clr.l	(a1)+
q40_mdloop
	move.w	(a0)+,(a1)+		 ; copy module
	cmp.l	a2,a1
	blt.s	q40_mdloop
	st	q40_lowram		 ; read from RAM
	rts

hwinit_base
	bra.s	hwinit
	dc.w	0

hwinit_hreset				; special hard reset entry
	move.w	#$2700,sr
	reset
	st	q40_lowrom		; read from ROM ***** this code must be in high memory
	movem.l 0,a0/a1
	move.l	a0,a7
	jmp	(a1)			; hard reset = standard rom reset

hwinit					; soft reset
	moveq	#0,d0			; disable caches
	pcreg	vbr			; a better way than the "official"!!
	pcreg	cacr

	genif	.ass_type = 0
	cpusha				; then push and invalidate
;
;	 move.l  d0,a0
;	 dc.w	 $4E7B,$8801		 ; movec a0,vbr : re-init VBR to 0
;
	tst.l	d7			; RAM set?
	bne.s	hwi_peri		; ... yes, do not do TTRs

	itt0	#$00ffe000		; set ITT0 for all cached write through
	itt1	#$00000000		; disable ITT1
	dtt0	#$ff00e040		; set DTT0 for top 16M serialised
	dtt1	#$00ffe020		; set DTT1 for all cached copyback
;	dtt1	#$00ffe000		; set DTT1 for all cached write through

; NB the tranparent translation is changed if the MMU is set

	endgen

	genif	.ass_type = 1
	cpusha	bc			; then push and invalidate

	tst.l	d7
	bne	hwi_peri

 screg	      itt0,#$00ffe000		; set ITT0 for all cached write through
 screg	      itt1,#$00000000		; disable ITT1
 screg	      dtt0,#$ff00e040		; set DTT0 for top 16M serialised
 screg	      dtt1,#$00ffe020		; set DTT1 for all cached copyback

	endgen


hwi_peri
	st	q40_ebr 		; reset extension bus
	st	q40_kie 		; enable / disable interrupts
	st	q40_sie
	clr.b	q40_eie
	clr.b	q40_50uie
	move.b	qcf_ismode,q40_dmode	; initial mode
	clr.b	q40_kack		; acknowledge junk
	clr.b	q40_fack
	clr.b	q40_50uack
	st	q40_led 		; LED on
	
	moveq	#-1,d0
	dbra	d0,*			; pause
	clr.b	q40_ebr 		; unreset extension bus

	nop
	nop

	clr.b	FDC+fdco_dctl		; turn floppy drives off

	tst.l	d7			; RAM set?
	beq.s	hwi_mem
	rts

hwi_mem

	move.l	d4,-(sp)
	lea	smsq_end,a0		; OS1 header
qmon_loop
	move.l	sbl_mbase(a0),d0	; header length
	beq.s	qmon_done		; no more
	cmp.l	#'QMON',4(a0,d0.l)	; qmon?
	beq.s	qmon_call
	add.l	sbl_mlength(a0),d0
	add.l	d0,a0			; next header
	bra.s	qmon_loop
qmon_call
	lea	8(a0,d0.l),a0
	moveq	#-1,d0
	jsr	(a0)			; call qmon
qmon_done
	move.l	(sp)+,d4

; NB the host module has ensured that the code is either in ROM
; or starts at $28000 - this test, therefore, cannot smash it

	move.l	#$400000,a2		; 4 Meg search increment
	sub.l	a4,a4
	move.l	#$1357fdb9,d2		; flag
	moveq	#0,d3

hwi_ram_flag
	add.l	a2,a4			; next block
	add.l	d2,d3
	move.l	d3,(a4) 		; flag it
	cmp.l	(a4),d3 		; correct?
	bne.s	hwi_ram_over		; ... no, over the top

	sub.l	a1,a1
	moveq	#0,d1
hwi_ram_check
	add.l	a2,a1
	add.l	d2,d1
	cmp.l	(a1),d1 		; flag correct?
	bne.s	hwi_ram_over		; ... no, wrapped around
	cmp.l	a4,a1			; up to end yet?
	blt.s	hwi_ram_check		; ... no
	bra.s	hwi_ram_flag

hwi_ram_over
	jsr	q40_prtype		; determine processor type (d6)
	btst	#25,d6			; Q40? . .  **** NEW
	beq.s	hwi_q40 		; . . yes   **** NEW

; 2.02
	move.w	sr,d2			; save current state (supervisor/user mode)
;	 trap	 #0			 ; go to supervisor mode if not already in it.

	gcreg	pcr,d0			; get the configuration register value.
	move.w	d0,d1
	lsr.w	#8,d1			; the revision number is in the 2nd lsb.
	subq.b	#6,d1			; anything below 6 is buggy.
	bpl.s	not_buggy		;
	bset	#5,d0			; set bit 5 to disable the buggy "store/load
					; bypass" optimization (cures errata i14 & i15)
not_buggy
	bset	#0,d0			; make sure the superscalar mode is enabled.
	pcreg	pcr,d0			    ; set the pcr.
	move.w	d2,sr			; return to previous state.
	moveq	#0,d0

; end 2.02
hwi_q40 				; **** NEW
	move.b	qcf_mmu,d0		; set up MMU?
	beq.s	hwi_setRAM
	assert	sys.immu,1
	btst	#16,d6			; MMU?
	beq.s	hwi_setRAM
	bsr.s	hwi_mmuset		; set up MMU table below (a4)

hwi_setRAM
	st	q40_lowrom		; write to RAM
	move.w	#3,0
	move.l	d4,2
	st	q40_lowram		; read from RAM

	move.l	(sp)+,a0		; return address

	clr.l	-(sp)			; facilities
	clr.l	-(sp)			; language
	move.l	d6,-(sp)		; processor, mmu/fpu, std disp
	move.l	#'Q40 ',-(sp)		; Q40

	move.l	a4,d7			; ram top
	moveq	#0,d6			; no special RAM
	moveq	#0,d5

	lea	q40_mdinst,a6		; q40 version of module installation
	move.l	sp,a5			; loader communications block
	lea	mod_table,a4		; module table
	lea	sms_wbase,a3		; write to base
	lea	qcf_cx,a2		; config block
	jmp	(a0)

; smsq write to base area routine

sms_wbase
	st	q40_lowrom		 ; write to RAM
	move.w	d0,(a5)+
	st	q40_lowram		 ; read from RAM
	rts

; loader tables

mod_table
	dc.l	sms.base-4,sms.base  ; first slice (just the reset routine)
	dc.l	0  ; we cannot have code that can write to low memory in low
		   ; memory - this is a problem for QMON and hard reset (above)

mmu.ptr  equ	%11+%01000		; resident + used
mmu.cpmode equ	%11+%11000+%0100000	; resident + used and modified + copyback

hwi_mmuset ;  bra.s	 *
	lea	mmu.ptr(a4),a1
	move.l	#$04000000+mmu.cpmode,a0 ; 64 Mbyte limit
	move.w	#$04000000/$2000-1,d0	; 8k pages (8k entries)
hwim_pgloop
	sub.w	#$2000,a0
	move.l	a0,-(a4)		; page entries
	dbra	d0,hwim_pgloop

	move.w	#$2000/32-1,d0
hwim_ptloop
	sub.w	#32*4,a1		; 32 pages per pointer (256 pointers)
	move.l	a1,-(a4)
	dbra	d0,hwim_ptloop

	lea	mmu.ptr(a4),a0		; lower pointer table
	lea	128*4(a0),a1		; upper pointer table
	moveq	#128/2-1,d0		; 128 entries in root
hwim_rtloop
	move.l	a1,-(a4)
	move.l	a0,-(a4)
	dbra	d0,hwim_rtloop

	genif	.ass_type = 0

	dc.w	$F518			; pflusha ; invalid ATC
	screg	urp,a4
	screg	srp,a4
	screg	tc,#$c000		; enable MMU

	itt0   #$fe00e000		; set ITT0 for ROM / Screen cached writethrough
	itt1   #$00000000		; set ITT1 for none
	dtt0   #$ff00e040		; set DTT0 for top 16M serialised
	dtt1   #$fe00e020		; set DTT0 for screen cached copyback

	endgen

	genif	.ass_type = 1

	pflusha
	screg	urp,a4
	screg	srp,a4
	screg	tc,#$c000		; enable MMU

	screg  itt0,#$fe00e000		; set ITT0
	screg  itt1,#$00000000		; set ITT1
	screg  dtt0,#$ff00e040		; set DTT0
	screg  dtt1,#$fe00e020		; set DTT0

	endgen

	rts

	end
