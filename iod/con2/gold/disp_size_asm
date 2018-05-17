; Gold Card Set Display Size			v1.10  2000  Tony Tebby
;							2003  Marcel Kilgus
;
; 2003-03-05  1.10  Completely revamped version for mode 16 (MK)

	section init

	xdef	cn_disp_size
	xdef	au_modes

	xref	cn_disp_clear

	include 'dev8_keys_aurora'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_mac_proc'
	include 'dev8_mac_thg'
	include 'dev8_mac_basic'
	include 'dev8_mac_assert'


	section driver


*
*	Table of data to be filled in - offset in dddb/word to set
*
cds_initbs
	dc.l	$20000,$8000
	dc.w	$80
	dc.w	$200
	dc.w	$100

cds_initlc
	dc.l	vram_base,640*480/4
	dc.w	$100
	dc.w	640
	dc.w	480

; Modes must be in descending sizes! dwmode macro in keys_aurora
au_modes
	dc.w	au_modes_ql-*
	dc.w	au_modes_8-*

au_modes_ql
	dwmode	11,1024,768,%10000011,0,0,vram_llen
	dwmode	10,1024,512,%00000011,0,0,vram_llen
	dwmode	09,1024,480,%00000011,0,16,vram_llen
	dwmode	08,0768,576,%10000010,0,0,vram_llen
	dwmode	07,0768,480,%10000010,0,48,vram_llen
	dwmode	06,0768,384,%00000010,0,0,vram_llen
	dwmode	05,0640,480,%10000001,0,0,vram_llen
	dwmode	04,0640,320,%00000001,0,0,vram_llen
	dwmode	03,0512,480,%10000001,16,0,vram_llen
	dwmode	02,0512,384,%10000000,0,0,vram_llen
	dwmode	01,0512,320,%00000001,16,0,vram_llen
	dwmode	00,0512,256,%00000000,0,0,vram_llen
	dc.l	-1			; end of list

au_modes_8
	dwmode	03,512,480,%10011001,0,0,vramhc_llen
	dwmode	02,512,384,%10011000,0,0,vramhc_llen
	dwmode	01,512,320,%00011001,0,0,vramhc_llen
	dwmode	00,512,256,%00011000,0,0,vramhc_llen
	dc.l	-1

;+++
; Set size
;	d6 c  p internal mode key
;	d7 c  p ptd.ql or ptd.08
;---
cn_disp_size
cds.reg reg	d1/d2/a4
	movem.l cds.reg,-(sp)

	lea	cds_initbs,a4		  ; standard display

; Check which display

	moveq	#$ffffffe0,d0
	and.b	sys_mtyp(a6),d0 	 ; check display type
	beq.s	cds_set 		 ; QL

	cmp.b	#sys.mqlc,d0		 ; LCD
	bne.s	cds_aurora		 ; ... no, aurora

	lea	cds_initlc,a4
	moveq	#$ffffff00+em.lcd,d0	 ; mode is fixed
	move.b	d0,emcr
	move.b	d0,sys_hstt(a6) 	 ; set hardware status

cds_set
	move.l	(a4)+,pt_scren(a3)
	move.l	(a4)+,pt_scrsz(a3)
	move.w	(a4)+,pt_scinc(a3)
	move.l	(a4)+,pt_xscrs(a3)

	jsr	cn_disp_clear		; clear display
	bra	cds_rts


cds_aurora
	assert	0,ptd.ql,ptd.08-2

	lea	au_modes,a4
	adda.w	d7,a4
	adda.w	(a4),a4 		 ; pointer to mode table for colour
	suba.l	#aum.size,a4

cds_lookup
	adda.l	#aum.size,a4
	cmp.l	#-1,aum.size(a4)	 ; last entry?
	beq.s	cds_eol 		 ; yes, just take it
	cmp.b	aum.nr(a4),d6
	bne.s	cds_lookup

cds_eol 				 ; mode table is now in a4
	move.b	aum.hw(a4),d0		 ; hardware bits
	cmp.b	#ptd.ql,d7
	bne.s	cds_setmode

	moveq	#1<<sysqm..8,d1 	 ; ql 4/8 mode bit
	assert	sysqm..8,em..m0
	and.b	sys_qlmr(a6),d1 	 ; in the same place !!!!!
	assert	em..ar,7		 ; aspect ratio bit 7
	or.b	d1,d0			 ; resolution + mode + square pixel
cds_setmode
	move.b	d0,emcr 		 ; set display mode
	move.b	d0,sys_hstt(a6) 	 ; set hardware status

	movem.w aum.xres(a4),d1/d2	 ; resolution

	moveq	#%10101,d0		 ; jumper bits
	and.b	emcr,d0
	roxr.w	#2,d0
	ror.w	#1,d0
	lsr.b	#1,d0
	rol.w	#2,d0
	add.w	d0,d0
	move.w	au_hmax(pc,d0.w),d0	 ; max y size for monitor
	cmp.w	d0,d2
	bls.s	cds_setc
	move.l	d0,d2

cds_setc
	move.l	#vram_base,pt_scren(a3)
	move.l	#vram_size,pt_scrsz(a3)
	move.w	aum.llen(a4),pt_scinc(a3)
	movem.w d1/d2,pt_xscrs(a3)
	jsr	cn_disp_clear		; first clear whole VRAM

	move.l	aum.base(a4),pt_scren(a3)
	move.l	#vram_base+vram_size,d0
	sub.l	aum.base(a4),d0 	; rest of VRAM size
	move.l	d0,pt_scrsz(a3)

cds_rts
	movem.l (sp)+,cds.reg
	moveq	#0,d0
	rts

au_hmax
	dc.w	288	   max ql height, non-interlaced
	dc.w	576	   max ql height, interlaced
	dc.w	576	   max vga
	dc.w	768	   max vga, intl
	dc.w	576	   max svga
	dc.w	768	   max svga, intl
	dc.w	768	   max multisynch
	dc.w	768	   max multisynch (diagnostic = 960 displayed)

	end
