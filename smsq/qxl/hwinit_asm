; SMSQ QXL Hardware Initialisation V3
; 2003-05-18	v.3.01		added config item for meaage language (wl)
; The caches are invalidated
; (The hardware is initialised)
; If d7 = 0	the RAM is setup.
;		$0003,d4 is written to address 0 'ROM' or 'RES'
;		It returns  pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    ROM or RES in d4
;			    zero in d5
;			    zero in d6
;			    top of RAM in d7

	xref	smsq_end

	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_mac_config02'
	include 'dev8_mac_assert'
	include 'dev8_mac_creg'

	section hwinit

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
	dc.w	36,'QXL Hardware Initialisation for SMSQ'
	dc.l	'    '

qxl_cx
qxl_xxx  dc.b	0
qxl_par  dc.b	7
qxl_ser1 dc.b	4	     ; $08
qxl_ser2 dc.b	0
qxl_bflp dc.b	1
qxl_bwin dc.b	1
qxl_lang dc.w	44	     ; $0c		6
qxl_btp  dc.b	0,0				8
qxl_size dc.b	0  ; 640x480 ; $10		a
qxl_colr dc.b	0  ; 0 = QL, 2 = 8 bit, 3 = 16 bit
	 dc.w	0,0,0	     ;		     c-10
	 dc.w	0,0	     ;		     12
	 dc.b	0	     ;		     16
qxl_clck dc.b	0
	 dc.w	0	     ;		     18
qcf_mlan dc.w	44	     ;		     1a

qcf_kstuf	dc.b	250	 ; 1c
qcf_curs	dc.b	1	 ; 1d

	 dc.w	0

	xref.l	smsq_vers
	mkcfhead {SMSQ},{smsq_vers}

	mkcfitem 'OSPD',code,'D',qxl_size,,,\
       {Display size}\
	-1,Q,{QL 512x256},0,V,{VGA 640x480},1,S,{SVGA 800x600},2,X,{XVGA 1024x768}

	mkcfitem 'OSPO',code,'M',qxl_colr,,,\
       {Display colours}\
	0,Q,{QL},3,H,{High colour 16 bit}

	mkcfitem 'OSPF',code,'F',qxl_bflp,,,\
       {If floppy disk inserted, boot from}\
	 0,N,{Neither},1,A,{A:(FLP1)},2,B,{B:(FLP2)}

	mkcfitem 'OSPW',code,'W',qxl_bwin,,,\
       {Otherwise boot from}\
   1,1,{WIN1},2,2,{WIN2},3,3,{WIN3},4,4,{WIN4},5,5,{WIN5},6,6,{WIN6},7,7,{WIN7}

	mkcfitem 'OSPK',word,'L',qxl_lang,,,\
       {Default Keyboard Language Code (33=F, 44=GB, 49=D, 39=IT etc.)},0,$7fff

	mkcfitem 'OSPM',word,'L',qcf_mlan,,,\
       {Default Messages Language Code 33=F, 44=GB, 49=D, 39=IT},0,$7fff

	mkcfitem 'OSPC',byte,'C',qxl_clck,,,\
       {Processor clock speed (or 0 for auto detect)},0,$7f

	mkcfitem 'OSPS',byte,'S',qcf_kstuf,,,\
       {Stuffer buffer key for edit line calls},0,$ff

	mkcfitem 'OSPC',code,'C',qcf_curs,,,\
       {Use sprite for cursor?} \
	0,N,{No},1,Y,{Yes}

	mkcfend

; copy module at (a0) to 4(a1)-(a2) linking to (a3) and updating a3 to a1

qxl_mdinst
; moveq #2,d0
; jsr x_flash
	move.l	a1,(a3) 		 ; link
	move.l	a1,a3
	clr.l	(a1)+
qxl_mdloop
	move.w	(a0)+,(a1)+		 ; copy module
	cmp.l	a2,a1
	blt.s	qxl_mdloop
; moveq #1,d0
; jsr x_flash
	rts


hwinit_base
	bra.s	hwinit
	dc.w	0

hwinit_hreset	       ; special hard reset entry
	lea	-1,a4		; max memory
	jmp	sms.base	; hard reset = soft reset

hwinit
	move.l	(sp)+,a0	; return address

	cdisa40 		; disable caches
	cinva			; invalidate caches

; moveq #1,d0
; jsr x_flash

	tst.b	qxl_int_ack	; clear interrupt
	nop

	tst.l	d7		; memory already set?
	beq.s	hwi_mem 	; ... no
	jmp	(a0)

hwi_mem
;	 iacr0	 #$007fe020	 ; set IACR0 for bottom end cached copyback
	iacr0	#$007fe000	; set IACR0 for bottom end cached write through
	iacr1	#$007fe060	; set IACR1 for top non cacheable
;	 dacr0	 #$007fe020	 ; set DACR0 for bottom end cached copyback
	dacr0	#$007fe000	; set DACR0 for bottom end cached write through
	dacr1	#$807fe040	; set DACR1 for top end serialised

	move.l	#'Tony',d0
	move.l	d0,0
	move.l	#$100000,a2		 ; search increment = 1M
	move.l	#$100000,a3		 ; base of search
	move.l	a3,a4

hwi_ram_look
	cmp.l	(a4),d0 		 ; wrapped around to 0?
	beq.s	hwi_ram_done		    ; ... yes
	move.l	a4,(a4) 		 ; flag this address

	move.l	a3,a1
hwi_ram_check
	cmp.l	(a1),a1 		 ; flag correct?
	bne.s	hwi_ram_done		    ; ... no, wrapped around
	add.l	a2,a1
	cmp.l	a4,a1			 ; up to end yet?
	ble.s	hwi_ram_check		    ; ... no

	add.l	a2,a4			 ; 1M pages
	bra.s	hwi_ram_look

hwi_ram_done
	clr.l	0			 ; clear reset vector
	clr.l	4

	moveq	#0,d5
	moveq	#0,d6
	move.l	a4,d7
; move.l d7,d0
; clr.w  d0
; swap	 d0
; lsr.w  #4,d0	; memory in Mb
; jsr x_flash

	lea	sms.conf,a1
	lea	qxl_cx,a2		 ; config info
	lea	sms_wbase,a3		 ; write to base
	lea	mod_table,a4		 ; module table

	clr.l	-(sp)			 ; facilities
	clr.l	-(sp)			 ; language
	move.l	#$40<<24+0<<16+0<<8+sys.mqxl,-(sp) ; 68040, no mmu/fpu, std disp
	move.l	#'QXL ',-(sp)		 ; QXL
	move.l	sp,a5			 ; loader communications block
	lea	qxl_mdinst,a6

	jmp	(a0)

; SMSQ write to base routine

sms_wbase
	move.w	d0,(a5)+
	rts

; loader tables

mod_table
	dc.l	sms.base-4,sms.base	 ; first slice
	dc.l	0
	dc.l	sms.base-4,$4000	 ; first slice
	dc.l	$4200-4,$c000		 ; second slice
	dc.l	$10000-4,$17d00 	 ; third slice
	dc.l	0

; This flash routine sends the net low for one period, then high for one period.
; This is repeated d0 times and then there are two periods low.
; The sequence is repeated once again and followed by a double pause.

x_flash
; rts
	movem.l d0/d1,-(sp)
	bsr.s	flash1			; flash once
	move.l	(sp),d0
	bsr.s	flash1			; and again
	bsr.s	flashidle		; extra idel
	movem.l (sp)+,d0/d1
	rts

flashlp
	tst.b	qxl_netl		 ; net low
	bsr.s	flashp
	tst.b	qxl_neth		 ; net high
	bsr.s	flashp
flash1
	dbra	d0,flashlp		 ; ... and repeat

	tst.b	qxl_netl		 ; idle low
flashidle
	bsr.s	flashp
	nop
flashp
	move.l	#1000000,d1		 ; 1,000,000 cycles for about .2 sec
	move.l	#300000,d1		; 1,000,000 cycles for about .2 sec
	subq.l	#1,d1
	bne.s	*-2
	rts

	end
