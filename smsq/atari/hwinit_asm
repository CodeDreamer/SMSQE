; SMSQ Atari Hardware Initialisation V3
; 2003-05-18			3.01	added message language coinfig item (wl)
; Called with A7 a valid, safe stack
; The hardware is initialised.
; If d7 = 0	the RAM is setup.
;		It returns  pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    top of slow RAM in d5 or 0 if no fast RAM
;			    base fast RAM in d6 or 0 if no fast RAM
;			    top of fast / slow RAM in d7

	section hwinit

	xdef	sms_wbase

	xref	at_prtype
	xref	at_disptype

	xref	gu_wbuse

	xref	smsq_end

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_scc'
	include 'dev8_keys_68000'
	include 'dev8_keys_tos_sys'
	include 'dev8_keys_sys'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
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
	dc.w	38,'Atari Hardware Initialisation for SMSQ'
	dc.l	'    '

asq_fram dc.b	$ff
	 dc.b	0
asq_cx
asq_adpt dc.b	0
asq_par  dc.b	0
asq_ser1 dc.b	0		; 2
asq_ser2 dc.b	0
asq_bflp dc.b	1		; 4
asq_bwin dc.b	1
asq_lang dc.w	44		; 6
asq_targ dc.b	0		; 8
asq_part dc.b	0
asq_scsh dc.w	512		; a
asq_scsv dc.w	256		; c
asq_scrv dc.w	50		; e
asq_scrh dc.w	15625		; 10
asq_scbh dc.w	128		; 12
asq_scbv dc.w	0		; 14
asq_ascr dc.b	-1		; 16
	 dc.b	0
	 dc.w	0		; 18
qcf_mlan dc.w	44		; 1a
qcf_kstuf dc.b	250
qcf_curs  dc.b	0

	xref.l	smsq_vers
	mkcfhead {SMSQ},{smsq_vers}

	mkcfitem 'OSPU',byte,'U',asq_targ,,,\
       {WIN1 target number (ACSI 0 to 7, SCSI 8 to 15)},0,15

	mkcfitem 'OSPP',byte,'P',asq_part,,,\
       {WIN1 partition number},0,11

	mkcfitem 'OSPK',word,'L',asq_lang,,,\
       {Default Keyboard Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff

	mkcfitem 'OSPL',word,'L',qcf_mlan,,,\
       {Default Messages Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff

	mkcfitem 'OSPF',code,'F',asq_fram,,,\
       {Use Fast RAM if exists},$ff,Y,{Yes},$00,N,{No}

	mkcfitem 'OSPD',code,'D',asq_adpt,,,\
       {Display adapter}\
	$00,A,{Automatic Detection}\
	$01,F,{Futura},$41,X,{Extended},$81,V,{QVME},$21,M,{Mono}

	mkcfitem 'OSPX',word,'X',asq_scsh,,,\
       {Initial display width},512,2048

	mkcfitem 'OSPY',word,'Y',asq_scsv,,,\
       {Initial display height},256,2048

	mkcfitem 'OSPR',word,'R',asq_scrv,,,\
       {Initial vertical rate},50,120

	mkcfitem 'OSPS',word,'S',asq_scrh,,,\
       {Initial horizontal rate or 0 if vertical blank > 0},0,65000

	mkcfitem 'OSPH',word,'H',asq_scbh,,,\
       {Initial horizontal blank},32,512

	mkcfitem 'OSPV',word,'V',asq_scbv,,,\
       {Initial vertical blank or 0 if horizontal rate > 0},0,128

	mkcfitem 'OSPA',code,'F',asq_ascr,,,\
       {Auto detect removable medium on ACSI bus},$ff,Y,{Yes},$00,N,{No}


	mkcfitem 'OSPT',byte,'T',qcf_kstuf,,,\
       {Stuffer buffer key for edit line calls},0,$ff

	mkcfitem 'OSPC',code,'C',qcf_curs,,,\
       {Use sprite for cursor?} \
	0,N,{No},1,Y,{Yes}

	mkcfend
	ds.w	0

; copy module at (a0) to 4(a1)-(a2) linking to (a3) and updating a3 to a1

at_mdinst
	move.l	a1,(a3)
	move.l	a1,a3
	clr.l	(a1)+
at_mdloop
	move.w	(a0)+,(a1)+		 ; copy module
	cmp.l	a2,a1
	blt.s	at_mdloop
	rts


hwinit_base
	bra.s	hwinit
	dc.w	0

hwinit_hreset	       ; special hard reset entry
	move.w	#$2700,sr
	reset
	movem.l 0,a0/a1
	move.l	a0,a7
	jmp	(a1)   ; hard reset = standard rom resetsoft reset

hwinit
  move.l  d7,-(sp)
  bne.s   qmon_done
  lea	  smsq_end,a0	       ; OS1 header
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
  move.l  (sp)+,d7

	jsr	at_prtype		 ; determine processor / machine type (d6)
	move.b	asq_adpt,d5		 ; preset type?
	bclr	#0,d5
	bne.s	hwi_peri		 ; ... no
	jsr	at_disptype		 ; get display type (d5)

hwi_peri
	or.b	d5,d6
	move.b	#sys.mtyp,d5
	and.b	d6,d5
	cmp.b	#sys.mmste,d5		 ; mega ste?
	blt.s	hwi_sst 		 ; ... no, ST
	beq.s	hwi_sste		 ; ... yes

	lea	mfp2_init,a0		 ; set up mfp2 registers
	lea	at_mfp2,a1
	moveq	#mfp2_iend-mfp2_init-1,d0
hwi_mfp2
	move.b	(a0)+,(a1)
	addq.l	#2,a1
	dbra	d0,hwi_mfp2

hwi_sste
	move.b	#scc_wmictl,scc_ctra
	move.b	#scci.rest,scc_ctra
	move.b	#scc_wvec,scc_ctra
	move.b	#scc_vbas>>2,scc_ctra

	clr.b	scu_smsk

hwi_sst
	lea	mfp_init,a0		 ; set up mfp registers
	lea	at_mfp,a1
	moveq	#mfp_iend-mfp_init-1,d0
hwi_mfp
	move.b	(a0)+,(a1)
	addq.l	#2,a1
	dbra	d0,hwi_mfp

	move.b	#gen.ctls,gen_ctls
	move.b	#gen.init,gen_ctlw	 ; reset control port

	move.b	#atm.nint,at_midic	 ; disable MIDI interrupts

	moveq	#7,d0			 ; clear all of palette reg
	lea	vdr_palt,a0
hwi_palt
	clr.l	(a0)+
	dbra	d0,hwi_palt

	move.l	d6,d0
	asl.l	#8,d0			 ; check processor type
	bvs.s	hwi_hset		 ; not 000

	lea	at_stesp,a2		 ; 68000 - try to set STE speed register
	moveq	#at.stefs,d1
	jsr	gu_wbuse
	beq.s	hwi_hset		 ; ... OK
	lea	at_adse,a2		 ; try to set adspeed
	jsr	gu_wbuse

hwi_hset
	tst.l	d7			 ; RAM set?
	beq.s	hwi_mem
	rts

hwi_mem
	move.l	(sp)+,a1		 ; return address

	clr.l	-(sp)			 ; facilities
	clr.l	-(sp)			 ; language
	move.l	d6,-(sp)		 ; processor, mmu/fpu, std disp
	move.l	#'ATST',-(sp)		 ; Atari ST series

	move.l	phystop,d7		 ; top of ST RAM
	moveq	#0,d6			 ; no fast RAM
	moveq	#0,d5

	cmp.l	#ramtop.val,ramvalid	 ; FAST RAM?
	bne.s	hwi_mmu 		 ; ... no

	cmp.w	#$0100,ramtop		 ; empty?
	ble.s	hwi_mmu 		 ; ... yes
	move.l	d7,d5
	move.l	#$1000000,d6
	move.l	ramtop,d7		 ; ... no, set up dual RAM

	move.b	asq_fram,d0		 ; fast ram to be used as heap?
	beq.s	hwi_mmu 		 ; ... no
	bset	#sbl.fh,sbl_facility(sp) ; set fast ram facility

hwi_mmu
	cmp.b	#$30,sbl_ptype(sp)
	bne.s	hwi_smash		 ; ... not 030

	moveq	#0,d0
	dc.l	$4e7b0002		 ; disable caches 68030

	moveq	#sys.mtyp,d0
	and.l	sbl_mtype(sp),d0
	cmp.b	#sys.mtt,d0		 ; TT?
	bne.s	hwi_smash		 ; ... no

	lea	sms.mtable,a0		 ; create standard memory table
	bsr.l	hwi_setmmu

	clr.l	-(sp)
	dc.l	$f0174000		 ; set MMU TC to zero
	addq.l	#4,sp
	lea	hwi_cpuroot,a0
	dc.l	$f0104c00		 ; set CPU root pointer
	lea	hwi_tc,a0
	dc.l	$f0104000		 ; set TC
	lea	hwi_tt0,a0
	dc.l	$f0100800		 ; set tt0
	lea	hwi_tt1,a0
	dc.l	$f0100c00		 ; set tt1

hwi_smash
	clr.l	memvalid		 ; clear reset flags
	clr.l	ramvalid
	clr.l	resvalid

	lea	at_mdinst,a6		 ; Atari version of module installation
	move.l	sp,a5			 ; loader communications block
	lea	mod_table,a4		 ; module table
	tst.l	d6			 ; fast ram?
	beq.s	hwi_swbase		 ; ... no
	lea	mod_tableTT,a4		 ; ... yes, load all in fast RAM
hwi_swbase
	lea	sms_wbase,a3		 ; write to base
	lea	asq_cx,a2		 ; config block

	jmp	(a1)

; smsq write to base area routine

sms_wbase
	move.w	d0,(a5)+
	rts

; loader tables

mod_table
	dc.l	sms.base-4,$4000	 ; first slice
	dc.l	$4200-4,$c000		 ; second slice
	dc.l	$10000-4,$17d00 	 ; third slice
	dc.l	0

mod_tableTT
	dc.l	sms.base-4,sms.base	 ; first slice (just the reset routine)
	dc.l	0  ; put all modules in fast memory


hwi_cpuroot
	dc.l	$80000002
	dc.l	sms.mtable
hwi_tc
	dc.l	$80f04445
hwi_tt0
	dc.l	$00000000  ; was $017e8107 Fast RAM transparent
hwi_tt1
	dc.l	$f00f8107

mmu.tab  equ	%10	flag for table descriptor, short format
mmu.page equ	%01	flag for page descriptor, short format
mmu.wp	 equ	 $4	write protect
mmu.icch equ	$40	inhibit cache

mmu.tabl equ	$40	MMU table length

;+++
; Create MMU table
;
;		Entry			Exit
;	A0	mmu table base		preserved
;---
hwi_setmmu
	movem.l a0-a2,-(sp)
	lea	mmu.tabl(a0),a2
	addq.l	#mmu.tab,a2
	move.l	a2,(a0)+			  ; $00000000
	move.l	a2,(a0)+			  ; $10000000
	move.l	a2,(a0)+			  ; $20000000
	move.l	a2,(a0)+			  ; $30000000
	move.l	a2,(a0)+			  ; $40000000
	move.l	a2,(a0)+			  ; $50000000
	move.l	a2,(a0)+			  ; $60000000
	move.l	a2,(a0)+			  ; $70000000
	move.l	a2,(a0)+			  ; $80000000
	move.l	a2,(a0)+			  ; $90000000
	move.l	a2,(a0)+			  ; $a0000000
	move.l	a2,(a0)+			  ; $b0000000
	move.l	a2,(a0)+			  ; $c0000000
	move.l	a2,(a0)+			  ; $d0000000
	move.l	a2,(a0)+			  ; $e0000000
	subq.l	#mmu.tab,a2
	lea	mmu.tabl(a2),a2
	move.l	a2,(a0) 			  ; sub-table for hardware
	addq.l	#mmu.tab,(a0)+			  ; $f0000000

tab_2ndd
	lea	mmu.tabl(a2),a2
	move.l	a2,(a0) 			  ; sub-table for RAM
	addq.l	#mmu.tab,(a0)+			  ; $x0000000
	move.l	#$01000000+mmu.page,(a0)+	  ; $x1000000
	move.l	#$02000000+mmu.page,(a0)+	  ; $x2000000
	move.l	#$03000000+mmu.page,(a0)+	  ; $x3000000
	move.l	#$04000000+mmu.page,(a0)+	  ; $x4000000
	move.l	#$05000000+mmu.page,(a0)+	  ; $x5000000
	move.l	#$06000000+mmu.page,(a0)+	  ; $x6000000
	move.l	#$07000000+mmu.page,(a0)+	  ; $x7000000
	move.l	#$08000000+mmu.page,(a0)+	  ; $x8000000
	move.l	#$09000000+mmu.page,(a0)+	  ; $x9000000
	move.l	#$0a000000+mmu.page,(a0)+	  ; $xa000000
	move.l	#$0b000000+mmu.page,(a0)+	  ; $xb000000
	move.l	#$0c000000+mmu.page,(a0)+	  ; $xc000000
	move.l	#$0d000000+mmu.page,(a0)+	  ; $xd000000
	move.l	#$0e000000+mmu.page,(a0)+	  ; $xe000000
	move.l	#$0f000000+mmu.page,(a0)+	  ; $xf000000

mmu_hard
	move.l	#$00000000+mmu.page,(a0)+	  ; $x0000000
	move.l	#$01000000+mmu.page,(a0)+	  ; $x1000000
	move.l	#$02000000+mmu.page,(a0)+	  ; $x2000000
	move.l	#$03000000+mmu.page,(a0)+	  ; $x3000000
	move.l	#$04000000+mmu.page,(a0)+	  ; $x4000000
	move.l	#$05000000+mmu.page,(a0)+	  ; $x5000000
	move.l	#$06000000+mmu.page,(a0)+	  ; $x6000000
	move.l	#$07000000+mmu.page,(a0)+	  ; $x7000000
	move.l	#$08000000+mmu.page,(a0)+	  ; $x8000000
	move.l	#$09000000+mmu.page,(a0)+	  ; $x9000000
	move.l	#$0a000000+mmu.page,(a0)+	  ; $xa000000
	move.l	#$0b000000+mmu.page,(a0)+	  ; $xb000000
	move.l	#$0c000000+mmu.page,(a0)+	  ; $xc000000
	move.l	#$0d000000+mmu.page,(a0)+	  ; $xd000000
	move.l	#$fe000000+mmu.icch+mmu.page,(a0)+; $xe000000	 VME !
	move.l	a2,(a0) 			  ; sub-table for hardware
	addq.l	#mmu.tab,(a0)+			  ; $xf000000

tab_3rdd
	move.l	#$ff000000+mmu.page,(a0)+	  ; $xx000000
	move.l	#$ff100000+mmu.page,(a0)+	  ; $xx100000
	move.l	#$ff200000+mmu.page,(a0)+	  ; $xx200000
	move.l	#$ff300000+mmu.page,(a0)+	  ; $xx300000
	move.l	#$ff400000+mmu.page,(a0)+	  ; $xx400000
	move.l	#$ff500000+mmu.page,(a0)+	  ; $xx500000
	move.l	#$ff600000+mmu.page,(a0)+	  ; $xx600000
	move.l	#$ff700000+mmu.page,(a0)+	  ; $xx700000
	move.l	#$ff800000+mmu.page,(a0)+	  ; $xx800000
	move.l	#$ff900000+mmu.page,(a0)+	  ; $xx900000
	move.l	#$ffa00000+mmu.page,(a0)+	  ; $xxa00000
	move.l	#$ffb00000+mmu.page,(a0)+	  ; $xxb00000
	move.l	#$ffc00000+mmu.page,(a0)+	  ; $xxc00000
	move.l	#$ffd00000+mmu.page,(a0)+	  ; $xxd00000
	move.l	#$ffe00000+mmu.page,(a0)+	  ; $xxe00000
	move.l	#$fff00000+mmu.icch+mmu.page,(a0)+; $xxf00000
	movem.l (sp)+,a0-a2
	rts

mfp_init
	dc.b	0		 ; no output data
	dc.b	mfp.acte	 ; active edge
	dc.b	mfp.ddir	 ; data direction
	dc.b	1<<mfp..tai	 ; ADMA timer
	dc.b	1<<mfp..hdi+1<<mfp..tci ; disk int and 200 Hz enabled
	dc.b	0,0,0,0,0,0
	dc.b	mfp.vect ; V180, software end of interrupt (should be automatic)
	dc.b	0,0,$51  ; no timers AB, C is 200 Hz and serial port D 9600
	dc.b	0,0,192,2 ; no timers AB, C is 200 Hz and serial port D 9600

	dc.b	0,mfp.npty+mfp.lstp+mfp.8bit+mfp.adiv; set usart
				 ; no parity, 1.5 stop bits, 8 bit async
	dc.b	mfp.rxen	 ; receiver enabled
	dc.b	mfp.txen	 ; transmitter enabled

mfp_iend
	ds.w	0
mfp2_init
	dc.b	0		 ; no output data
	dc.b	mfp.acte	 ; active edge
	dc.b	mfp.ddir	 ; data direction
	dc.b	1<<mfp..tai	 ; SCSI timer
	dc.b	0,0,0,0,0,0,0
	dc.b	mfp2.vect ; V1C0, software end of interrupt (should be automatic)
	dc.b	0,0,$01  ; no timers ABC and serial port D 9600
	dc.b	0,0,0,2  ; no timers ABC and serial port D 9600

	dc.b	0,mfp.npty+mfp.lstp+mfp.8bit+mfp.adiv; set usart
				 ; no parity, 1.5 stop bits, 8 bit async
	dc.b	mfp.rxen	 ; receiver enabled
	dc.b	mfp.txen	 ; transmitter enabled

mfp2_iend
	ds.w	0

	end
