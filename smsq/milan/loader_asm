; SMSQ Milan Loader V3

; The config information is copied
; The RAM is setup.
; The hardware is initialised.
; The base area write routine is copied to routine address
; For STs
;    The first few modules are copied to base of memory.
;    All modules which would end above $17d00 are then copied to high memory.
; For TTs (with fast RAM)
;    The first (selected) module is copied to base of memory
;    This and all subsequent modules are copied to high memory
; The headers are thrown away and replaced by a link pointer which points to
;    the link pointer of the next module.
; Sets useable RAM limits
; Calls sms.base with (slow) RAMTOP in a4

	section loader

;	 xref	 init_hw
	xref	loader_end
;	 xref	 gu_exvt
;	 xref	 mk_mmutab

	include 'dev8_keys_68000'
	include 'dev8_keys_tos_sys'
	include 'dev8_keys_stella_bl'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_mac_config02'
	include 'dev8_mac_assert'

header_base
	dc.l	loader_base-header_base  ; length of header
	dc.l	0			 ; module length unknown
	dc.l	loader_end-loader_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	loader_name-*

loader_name
	dc.w	22,'Milan Loader for SMSQ '
	dc.l	'    '

;asq_fram dc.b	 $ff
;	  dc.b	 0
;asq_cx
;asq_adpt dc.b	 0
;asq_par  dc.b	 0
;asq_ser1 dc.b	 0
;asq_ser2 dc.b	 0
;asq_bflp dc.b	 1
;asq_bwin dc.b	 1
;asq_lang dc.w	 44
;asq_targ dc.b	 0
;asq_part dc.b	 0
;asq_scsh dc.w	 512
;asq_scsv dc.w	 256
;asq_scrv dc.w	 50
;asq_scrh dc.w	 15625
;asq_scbh dc.w	 128
;asq_scbv dc.w	 0
;asq_ascr dc.b	 -1
	 dc.b	0
	 dc.w	0

;	 xref.l  smsq_vers
;	 mkcfhead {SMSQ},{smsq_vers}
;
;	 mkcfitem 'OSPU',byte,'U',asq_targ,,,\
;	{WIN1 target number (ACSI 0 to 7, SCSI 8 to 15)},0,15
;
;	 mkcfitem 'OSPP',byte,'P',asq_part,,,\
;	{WIN1 partition number},0,11
;
;	 mkcfitem 'OSPL',word,'L',asq_lang,,,\
;	{Default Language Code 33=F, 44=GB, 49=D},0,$7fff
;
;	 mkcfitem 'OSPF',code,'F',asq_fram,,,\
;	{Use Fast RAM if exists},$ff,Y,{Yes},$00,N,{No}
;
;	 mkcfitem 'OSPD',code,'D',asq_adpt,,,\
;	{Display adapter}\
;	 $00,A,{Automatic Detection}\
;	 $01,F,{Futura},$41,X,{Extended},$81,V,{QVME},$21,M,{Mono}
;
;	 mkcfitem 'OSPX',word,'X',asq_scsh,,,\
;	{Initial display width},512,2048
;
;	 mkcfitem 'OSPY',word,'Y',asq_scsv,,,\
;	{Initial display height},256,2048
;
;	 mkcfitem 'OSPR',word,'R',asq_scrv,,,\
;	{Initial vertical rate},50,120
;
;	 mkcfitem 'OSPS',word,'S',asq_scrh,,,\
;	{Initial horizontal rate or 0 if vertical blank > 0},0,65000
;
;	 mkcfitem 'OSPH',word,'H',asq_scbh,,,\
;	{Initial horizontal blank},32,512
;
;	 mkcfitem 'OSPV',word,'V',asq_scbv,,,\
;	{Initial vertical blank or 0 if horizontal rate > 0},0,128
;
;	 mkcfitem 'OSPA',code,'F',asq_ascr,,,\
;	{Auto detect removable medium on ACSI bus},$ff,Y,{Yes},$00,N,{No}
;
;	 mkcfend
;	 ds.w	 0
;
load_wbase
	move.w	d0,(a5)+
	rts

loader_base
	move.w	#$2700,sr		 ; no interrupts


  lea	  loader_end,a0        ; OS1 header
qmon_loop
  move.l  sbl_mbase(a0),d0     ; header length
  beq.s   loader_start	       ; no more
  cmp.l   #'QMON',4(a0,d0.l)   ; qmon?
  beq.s   qmon_call
  add.l   sbl_mlength(a0),d0
  add.l   d0,a0 	       ; next header
  bra.s   qmon_loop
qmon_call
  jsr	  8(a0,d0.l)	       ; call qmon

loader_start
;	 lea	 sms.conf,a1
;	 lea	 asq_cx,a0
;	 moveq	 #7,d0
;load_conf
;	 move.l  (a0)+,(a1)+		  ; copy config info
;	 dbra	 d0,load_conf

;	 lea	 exv_top-$200,sp	  ; stack in exv area (loader above it)!
;
;	 jsr	 init_hw		  ; initialise the hardware
;	 or.l	 d1,d2

	lea	exv_top-$40,a5		 ; loader communications block
	move.l	a5,a0
	move.l	#'ATST',(a0)+		 ; Atari ST series
	move.l	d2,(a0)+		 ; machine type
	clr.l	(a0)+			 ; language
	clr.l	(a0)			 ; facilities

;	 move.l  phystop,d7		  ; top of ST RAM
;	 moveq	 #0,d6			  ; no fast RAM
;
;	 cmp.l	 #ramtop.val,ramvalid	  ; FAST RAM?
;	 bne.s	 load_proc		  ; ... no
;
;	 cmp.w	 #$0100,ramtop		  ; empty?
;	 ble.s	 load_proc		  ; ... yes
;	 move.l  ramtop,d6		  ; ... no
;
;	 move.b  asq_fram,d0		  ; fast ram to be used as heap?
;	 beq.s	 load_proc		  ; ... no
;	 bset	 #sbl.fh,(a0)		  ; set fast ram facility
;
;load_proc
;	 rol.l	 #8,d2			  ; look at processor
;	 cmp.b	 #$30,d2
;	 bne.s	 init_smash		  ; ... not 030
;
;	 moveq	 #0,d0
;	 dc.l	 $4e7b0002		  ; disable caches 68030
;
;	 cmp.b	 #sys.mtt,d1		  ; TT?
;	 bne.s	 init_smash		  ; ... no
;
;	 lea	 sms.mtable,a0		  ; create standard memory table
;	 jsr	 mk_mmutab
;
;	 clr.l	 -(sp)
;	 dc.l	 $f0174000		  ; set MMU TC to zero
;	 addq.l  #4,sp
;	 lea	 init_cpuroot,a0
;	 dc.l	 $f0104c00		  ; set CPU root pointer
;	 lea	 init_tc,a0
;	 dc.l	 $f0104000		  ; set TC
;	 lea	 init_tt0,a0
;	 dc.l	 $f0100800		  ; set tt0
;	 lea	 init_tt1,a0
;	 dc.l	 $f0100c00		  ; set tt1
;
;init_smash
;	 clr.l	 memvalid		  ; clear reset flags
;	 clr.l	 ramvalid
;	 clr.l	 resvalid
;
;	 move.l  load_wbase,sms.wbase	  ; base write routine
;
; All necessary bits are initialised, time to copy the OS into position.

; First though, we need to copy the loader to a safe place.

	lea	mod_base,a0
	move.l	#mod_end-mod_base,d0
	move.l	sp,a1

load_seg
	move.w	(a0)+,(a1)+
	subq.l	#2,d0
	bne.s	load_seg

	lea	loader_end,a0		 ; where the first bit of code is
	jmp	(sp)			 ; jump to loader code


init_cpuroot
	dc.l	$80000002
	dc.l	sms.mtable
init_tc
	dc.l	$80f04445
init_tt0
	dc.l	$00000000  ; was $017e8107 Fast RAM transparent
init_tt1
	dc.l	$f00f8107

; This is the loader itself

	xdef	mod_base     ; so we can see how large it is
	xdef	mod_end

mod_base
	clr.l	-(sp)
	move.l	sp,a3			 ; dummy first link here
	moveq	#0,d5			 ; count modules
	move.l	d7,a4			 ; provisional RAMTOP

mod_loop
	move.l	sbl_mbase(a0),d3	 ; base of module
	ble.l	mod_done		 ; ... no more
	move.l	sbl_select(a0),d0	 ; select?
	beq.s	mod_load		 ; ... no

mod.sreg reg	d3/d5/d6/d7/a0/a3/a4
	movem.l mod.sreg,-(sp)
	jsr	(a0,d0.l)		 ; do select routine
	movem.l (sp)+,mod.sreg
	tst.b	d0
	bgt.s	mod_load

mod_skip
	add.l	sbl_mlength(a0),a0	 ; skip module
	add.l	d3,a0
	bra.s	mod_loop

mod_load
	move.l	sbl_rlength(a0),d2	 ; real length
	beq.s	mod_skip		 ; ... none, ignore it
	tst.l	d5			 ; high memory only?
	bmi.s	mod_top 		 ; ... yes
	lea	mod_table,a6		 ; module base table
mod_blook
	move.l	(a6)+,d0		 ; another module area?
	beq.s	mod_top
	move.l	d0,a1			 ; base of module in this area
	lea	4(a1,d2.l),a2		 ; the end of the copy
	cmp.l	(a6)+,a2		 ; is this within the area?
	bge.s	mod_blook		 ; ... no

	moveq	#$f+4,d0		 ; round up and allow for link
	add.l	a2,d0
	and.w	#$fff0,d0		 ; round up to 4 long word line
	subq.l	#4,d0			 ; link goes here
	move.l	d0,-8(a6)		 ; next module in this area
	bra.s	mod_snext

mod_top
	move.l	d7,a2			 ; the end in top area
	sub.l	d2,d7			 ; the start
	and.w	#$fff0,d7		 ; rounded down to line of 4 long words
	subq.l	#4,d7			 ; the link
	move.l	d7,a1

mod_snext
	move.l	a0,a6			 ; save base

	move.l	sbl_mlength(a0),d1	 ; module length
	add.l	d3,a0			 ; the start of original
	add.l	a0,d1			 ; the header of the next

	move.l	a1,(a3) 		 ; set link
	move.l	a1,a3			 ; next link
	clr.l	(a1)+

mod_copy
	move.w	(a0)+,(a1)+
	cmp.l	a2,a1
	blt.s	mod_copy

	tst.l	d5			 ; first module?
	bne.s	mod_cnext		 ; ... no
	tst.l	d6			 ; any high memory
	beq.s	mod_cnext		 ; ... no
	move.l	sp,a3			 ; ... yes, copy to high memory
	move.l	d6,d7			 ; high memory pointer
	bset	#31,d5			 ; ... and flag
	move.l	a6,a0			 ; starting with this module
	bra.s	mod_top

mod_cnext
	addq.l	#1,d5
	move.l	d1,a0			 ; header of next module
	bra	mod_loop

mod_done
	tst.l	d6			 ; FAST RAM
	beq.s	mod_st			 ; ... none
	btst	#sbl.fh,sbl_facility(a5) ; fast ram facility?
	beq.s	mod_nofast		 ; ... no
	bra.s	mod_setram

mod_st
	move.l	d7,a4			 ; RAMTOP
mod_nofast
	moveq	#0,d7			 ; no fast ram

mod_setram
	move.l	(sp),a0 		 ; start address
	addq.l	#4,a0
	move.l	a4,sms.usetop		 ; RAMTOP saved for reference
	move.l	#$1000000,sms.framb	 ; MAGIC$$$$
	move.l	d7,sms.framt		 ; set fast RAMTOP or 0
	beq.s	mod_go

	move.w	#jmp.l,sms.base 	 ; patch in jump to real base module
	move.l	a0,sms.base+2

mod_go
	clr.b	ini_ouch		 ; no startup debug
	clr.l	sms.qrom
	jmp	(a0)			 ; first module

mod_table
	dc.l	sms.base-4,$4000	 ; first slice
	dc.l	$4200-4,$c000		 ; second slice
	dc.l	$10000-4,$17d00 	 ; third slice
	dc.l	0

mod_end
	end
