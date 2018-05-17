; Atari QL Mode set display size  V2.01    2000  Tony Tebby

	section con

	xdef	cn_disp_size

	xref	cn_disp_clear

	include 'dev8_keys_atari'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'

;+++
; Set the display size
;
;	d6 c  p pointer to size definition (SIZE proc definition)
;	d7 c  p colour depth = 0
;	a3 c  p console linkage
;	a6 c  p system variables
;---
cn_disp_size
cds.reg reg	d0-d7/a0-a5
	movem.l cds.reg,-(sp)
	clr.l	(sp)			 ; say no size change
	move.l	d6,a1			 ; screen size

; Check which emulator

	lea	pt_scren(a3),a0 	 ; screen def
	lea	cds_initbs,a4		 ; assume standard size

	moveq	#$ffffffc0,d2
	and.b	sys_mtyp(a6),d2 	 ; check display type
	beq.l	cds_futura		 ; Futura
	bmi.l	cds_qvme		 ; QVME

; Edwin shifter pickup

	move.b	#vdr.syme,vdr_symd	 ; set sync mode
	move.b	#vdr.rese,vdr_res	 ; and resolution

	cmp.w	#512,2(a1)		 ; Edwin standard or extended
	bgt.s	cds_ehigh

	lea	vde_slct,a2		 ; read this to select Edwin

	move.b	(a2),d1
	move.b	(a2),d1
	move.b	(a2),d1

	move.b	#vde.left,vde_left	 ; left edge
	move.b	#vde.in0,vde_init
	move.b	#vde.in1,vde_init
	move.b	#vde.in2,vde_init
	move.b	#vde.top,vde_top	 ; top

	move.b	#vdel.wdl,vde_wrdl	 ; width
	move.b	#vdel.lnl,vde_lins	 ; lines
	move.b	#vdel.lnh,vde_lins
	clr.b	vdel_fsl		 ; frequency select

	clr.b	vde_desl		 ; done

	move.b	#vdf.sb1,vdr_sb1	 ; set standard screen base
	move.b	#vdf.sb2,vdr_sb2

	bra.s	cds_set

cds_ehigh
	lea	cds_initbe,a4		 ; Edwin's extended

	lea	vde_slct,a2		 ; read this to select Edwin

	move.b	(a2),d1
	move.b	(a2),d1
	move.b	(a2),d1

	move.b	#vde.left,vde_left	 ; left edge
	move.b	#vde.in0,vde_init
	move.b	#vde.in1,vde_init
	move.b	#vde.in2,vde_init
	move.b	#vde.top,vde_top	 ; top

	move.b	#vde.wrdl,vde_wrdl	 ; width
	move.b	#vde.linl,vde_lins	 ; lines
	move.b	#vde.linh,vde_lins
	clr.b	vde_frsl		 ; frequency select

	clr.b	vde_desl		 ; done

	move.b	#vde.sb1,vdr_sb1	 ; set Edwin extended screen base
	move.b	#vde.sb2,vdr_sb2

	bra.s	cds_set

; Futura datasenter emulator

cds_futura
	move.w	#10000,d0
cds_fset
	move.w	vdr_palt,vdr_palt	 ; set palette over and over
	dbra	d0,cds_fset

	move.b	#vdr.symf,vdr_symd	 ; set sync mode
	move.b	#vdr.resf,vdr_res	 ; and resolution

	move.b	#vdf.sb1,vdr_sb1	 ; set futura screen base
	move.b	#vdf.sb2,vdr_sb2

; Set standard or extended screen size

cds_set
	move.l	(a4)+,(a0)+		 ; screen base
	move.l	(a4)+,(a0)+		 ; screen size (bytes)
	move.w	(a4)+,(a0)+		 ; bytes per line
	move.l	(a4)+,(a0)+		 ; screen size (pixels)

	st	(sp)			 ; say size changed
	bra.l	cds_clear

;------------------------------------------------------------------------------
;
; QVME card
;
cds_qvme
	tst.l	(a0)			 ; screen def already set?
	bne.s	cds_cparm
	lea	cds_deft,a4		 ; dummy table
	moveq	#cds_deftt-cds_deft,d0
cds_qset
	move.w	(a4)+,(a0)+
	subq.w	#2,d0
	bgt.s	cds_qset

; Get current QVME size, rate and blank
;
;	d5	total / reference x
;	d6	total / reference y
;	d7	frame rate
cds_cparm
	move.w	pt_xscrs(a3),d1 	 ; x size
	move.w	pt_yscrs(a3),d2 	 ; y size
	move.l	pt_xtotl(a3),d5 	 ; x total / ref
	move.l	pt_ytotl(a3),d6 	 ; y total / ref
	move.w	pt_frate(a3),d7 	 ; frame rate

	move.l	(a1)+,d0		 ; x size
	ble.s	cds_gety		 ; ... use old value
	cmp.w	d0,d1			 ; changed?
	beq.s	cds_gety		 ; ... no
	move.w	d0,d1
	st	(sp)			 ; flag change

cds_gety
	move.l	(a1)+,d0		 ; y size
	ble.s	cds_chkx		 ; ... use old value
	cmp.w	d0,d2			 ; changed?
	beq.s	cds_chkx		 ; ... no
	move.w	d0,d2
	st	(sp)			 ; flag change

cds_chkx
	tst.b	(sp)			 ; size change?
	beq.s	cds_frate		 ; ... no, do frame rate

	and.w	#$ffe0,d1		 ; x32 pixels
	cmp.w	#vde.stex,d1		 ; too wide?
	bls.s	cds_x_min		 ; ... no
	move.w	#vde.stex,d1
cds_x_min
	cmp.w	#512,d1 		 ; too narrow?
	bge.s	cds_cky
	move.w	#512,d1

cds_cky
	and.w	#$fff8,d2		 ; x8 lines
	cmp.w	#vde.stey,d2		 ; too high?
	bls.s	cds_y_min		 ; ... no
	move.w	#vde.stey,d2
cds_y_min
	cmp.w	#240,d2 		 ; too low?
	bge.s	cds_scheck
	move.w	#240,d2

cds_scheck
	move.w	d1,d0			 ; total
	mulu	d2,d0
	cmp.l	#vde.step,d0		 ; too large?
	bls.s	cds_rcheck		 ; ... no
cds_reduce
	move.w	d2,d0
	add.w	d2,d0
	cmp.w	d1,d0			 ; reduce x or y?
	bgt.s	cds_redy		 ; ... y (back towards 2:1 ratio)
	sub.w	#32,d1			 ; ... x
	bra.s	cds_scheck
cds_redy
	subq.w	#8,d2
	bra.s	cds_scheck

cds_rcheck
	and.w	#512*4-1,d0		 ; find remainder of 512 bytes
	cmp.w	#490*4,d0
	bls.s	cds_frate
	cmp.l	#vde.step/2,d0		 ; more than 1/2 size?
	bge.s	cds_reduce		 ; ... yes, reduce the size
	add.w	#32,d1			 ; increase x
	addq.w	#8,d2			 ; and y
	bra	cds_chkx		 ; redo checks


cds_frate
	move.l	(a1)+,d3		 ; frame rate?
	beq.s	cds_lrate
	move.w	d3,d7			 ; set new frame rate

cds_lrate
	move.l	(a1)+,d3		 ; line rate?
	beq.s	cds_xblank		 ; ... no

	divu	d7,d3			 ; total number of lines (line / frame)
	move.w	d2,d0			 ; number of visible lines
	lsr.w	#5,d0			 ; visible / 32
	add.w	d2,d0			 ; minimum total
	cmp.w	d0,d3			 ; number of lines large enough?
	bhs.s	cds_lrset
	move.w	d0,d3			 ; ... no, use minimum
cds_lrset
	move.w	d3,d6
	swap	d6
	move.w	d2,d6			 ; total y / visible y

cds_xblank
	move.l	(a1)+,d3		 ; x blank
	beq.s	cds_yblank		 ; not specified

	add.w	d1,d3
	move.w	d3,d5			 ; x total
	swap	d5
	move.w	d1,d5			 ; x total / x visible

cds_yblank
	move.l	(a1)+,d3		 ; y blank
	beq.s	cds_setvp		 ; not specified

	add.w	d2,d3
	move.w	d3,d6
	swap	d6
	move.w	d2,d6			 ; y total / y visible

cds_setvp
	move.w	d1,pt_xscrs(a3) 	 ; x size
	move.w	d2,pt_yscrs(a3) 	 ; y size
	move.l	d5,pt_xtotl(a3) 	 ; x total / ref visible
	move.l	d6,pt_ytotl(a3) 	 ; y total / ref visible
	move.w	d7,pt_frate(a3) 	 ; frame rate

	move.w	d1,d0
	lsr.w	#2,d0
	move.w	d0,pt_scinc(a3) 	 ; x increment

	mulu	d2,d0
	move.l	d0,pt_scrsz(a3) 	 ; screen size

; now calculate the true total lines and pixels from the stored total
; and the reference visible and the required visible = total * (required/ref)

	move.l	d6,d4			 ; y total / ref
	swap	d4
	mulu	d2,d4			 ; stored total * required
	add.l	d4,d4			 ; stored total * required * 2
	divu	d6,d4			 ; / reference
	addq.w	#1,d4			 ; rounding
	lsr.w	#1,d4			 ; d4 is required total lines
	move.w	d4,d6			 ; keep it safe
	mulu	d4,d7			 ; d7 is target line rate

	move.l	d5,d3			 ; x total / ref
	swap	d3
	mulu	d1,d3			 ; stored total * required
	add.l	d3,d3			 ; stored total * required * 2
	divu	d5,d3			 ; / reference
	addq.w	#1,d3			 ; rounding
	lsr.w	#1,d3			 ; d3 is required total pixels
	move.w	d3,d5			 ; keep it safe
	mulu	d3,d7			 ; d7 is target pixel rate

	asl.l	#4,d7			 ; should now be a word in msw
	bvc.s	cds_fndr		 ; ... yes, find pixel rate
	moveq	#-1,d7			 ; highest rate

cds_fndr
	swap	d7			 ; target pixel rate / 4096 in D7
	lea	cds_rate-2,a0		 ; 1 word before rate table
	moveq	#cds.rate-1,d0

cds_rloop
	move.l	(a0)+,d4		 ; next rate
	cmp.w	d7,d4			 ; high enough?
	bhs.s	cds_chkpxr
	swap	d0
	move.w	d4,d0
	swap	d0
	dbra	d0,cds_rloop

cds_chkpxr
	sub.w	d7,d4			 ; target to current
	swap	d0
	sub.w	d0,d7			 ; last to target
	cmp.w	d4,d7
	bhs.s	cds_setpxr		 ; current is nearer
	subq.l	#4,a0			 ; previous is nearer
cds_setpxr
	lea	vde_accs,a2		 ; set screen base for STE

	move.b	#0,(a2) 		 ; access
	move.b	(a0),vde_stsf-vde_accs(a2) ; frequency
	move.b	(a0)+,vde_stef-vde_accs(a2)
	move.b	#0,(a2) 		 ; access
	move.b	(a0),vde_stem-vde_accs(a2) ; QL mode + select
	move.b	(a0)+,vde_stsm-vde_accs(a2)

	moveq	#3,d4			 ; assume shift by three
	move.b	-(a0),d0
	lsl.b	#2,d0
	clr.w	d0
	addx.b	d0,d4			 ; d3 = 3 or 4 for 8 or 16 pixel
	move.w	d5,d3
	add.w	d3,d3			 ; 2 * nr pixels
	lsr.w	d4,d3			 ; 2 * nr chars
	addq.w	#1,d3			 ; rounded up
	lsr.w	#1,d3			 ; x total chars


	lea	cds_rord,a4		 ; order for setting registers

	swap	d1
	clr.w	d1
	bsr.s	cds_sreg		 ; set register 10
	bsr.s	cds_sreg		 ; set register 11
	bsr.s	cds_sreg		 ; set register 12
	bsr.s	cds_sreg		 ; set register 13
	bsr.s	cds_sreg		 ; set register 14
	bsr.s	cds_sreg		 ; set register 15

	swap	d1
	lsr.w	d4,d1			 ; multiples of 8 / 16 visible pixels
	bsr.s	cds_stotal		 ; set scan registers d3 / d1
	bsr.s	cds_svisible

	move.w	d5,d0
	lsr.w	d4,d0
	lsr.w	#5,d0			 ; x sync width
	addq.b	#1,d0
	move.w	d6,d1			 ; y total lines
	lsr.w	#3+3,d1 		 ; 1/8 character line
	lsl.b	#4,d1			 ; y sync in upper nibble
	or.b	d0,d1
	bsr.s	cds_sreg		 ; set sync widths

	moveq	#7,d1
	and.w	d6,d1			 ; extra lines (total mod 8)
	bsr.s	cds_sreg

	move.w	d2,d1			 ; visible lines
	lsr.w	#3,d1			 ; height was a multiple of 8
	move.w	d6,d3			 ; ... total lines
	lsr.w	#3,d3			 ; /8
	bsr.s	cds_stotal		 ; set scan registers d3 first
	moveq	#$10,d1 		 ; set mode
	bsr.s	cds_sreg
	moveq	#7,d1			 ; set 8 lines a square
	bsr.s	cds_sreg
	bsr.s	cds_svisible


;----------------------------------------------------------------------------
; ALL SET UP

cds_clear
	tst.b	(sp)			 ; display size changed?
	beq.s	cds_exit
	jsr	cn_disp_clear		 ; ... yes, clear screen
cds_exit
	clr.l	(sp)
	movem.l (sp)+,cds.reg
	rts


; set scan d1 = visible, d3 = total

cds_stotal
	exg	d3,d1			 ; first do total
	subq.w	#1,d1			 ; total count is -1
	bsr.s	cds_sreg		 ; total width / height

	ext.l	d1			 ; now sync position
	add.w	d3,d1
	add.w	d3,d1			 ; 2*disp+total-1
	divu	#3,d1			 ; sync is 1/3 2/3
	bra.s	cds_sreg

cds_svisible
	move.w	d3,d1			 ; finally, visible

cds_sreg
	moveq	#0,d0
	move.b	(a4)+,d0
cds_srd0
	move.b	#0,(a2) 		 ; access
	move.b	d0,vde_radr-vde_accs(a2) ; register

	move.b	#0,(a2) 		 ; access
	move.b	d1,vde_rval-vde_accs(a2) ; value

	move.b	d1,vde_stsr-vde_accs(a2,d0.w) ; saved register value
	rts

cds_rord
	dc.b	10,11,12,13,14,15  ; junk registers
	dc.b	0,2,1,3,5,4,7,8,9,6    ; this order is critical!
	ds.w	0


rate	macro	FKhz,ICS,Kreg
	dc.w	[FKHz]000>>12		; f/4096
	ifnum	[FKhz] > 32000 goto highrate  ; 32 MHz / 8 = 4 MHZ = max 6845
	dc.b	$[ICS],$[Kreg]+vde.stql
	goto endrate
highrate maclab
	dc.b	$[ICS],$[Kreg]+vde.stqh
endrate  maclab
	endm

cds_rate
	rate	10000,0f,02
	rate	10070,06,1b
	rate	10286,10,1b
	rate	10438,0b,23
	rate	10625,1a,02
	rate	10833,07,13
	rate	11022,0c,1b
	rate	11225,15,02
	rate	11429,14,0b
	rate	11667,18,13
;***	rate	11875,1c,02
	rate	12000,13,13
	rate	12360,0d,13
	rate	12588,00,34
	rate	12829,15,0b
	rate	13000,07,1b
	rate	13333,04,2b
;***	rate	13500,17,02
	rate	13778,0c,23
	rate	13917,0b,2b
	rate	14167,01,34
	rate	14400,13,1b
	rate	14832,0d,1b
	rate	15000,16,1b
	rate	15450,0e,1b
	rate	15740,0a,34
	rate	16000,0f,1b
	rate	16257,02,34
	rate	16700,08,34
	rate	17000,1a,1b		 ; ?????
	rate	17500,18,23
	rate	18000,13,23
	rate	18370,0c,2b
	rate	18540,0d,23
	rate	18888,11,2b
	rate	19313,0e,23
	rate	20000,04,34
	rate	20875,0b,34
	rate	21250,1a,23		 ; ?????
	rate	21676,12,2b
	rate	22450,05,34
	rate	23333,18,2b
	rate	24000,13,2b
	rate	25000,16,2b
	rate	26667,0f,2b
	rate	27555,0c,34
	rate	28332,01,06
	rate	29933,15,2b
	rate	31480,0a,06
	rate	32514,02,06
	rate	35000,18,34
	rate	36000,03,06
	rate	37080,0d,34
; ****	      rate    37575,09,06	     V dangerous
	rate	38625,0e,34
	rate	40000,04,06
	rate	41750,0b,06
	rate	42500,1a,34
	rate	44900,05,06
;***	rate	47500,1c,34
	rate	50350,06,06
;***	rate	54000,17,34
	rate	56664,11,06
;***	rate	57500,1e,34
;***	rate	60000,1f,34
	rate	65000,07,06
	rate	70000,18,06
	rate	74160,0d,06
	rate	77250,0e,06
	rate	80000,0f,06
	rate	85000,1a,06
	rate	89800,15,06
cds_rend
cds.rate equ	(cds_rend-cds_rate)/4








cds_deft ; dummy linkage
	dc.l	vde_steb
	dc.l	512*256/4
	dc.w	512/4
	dc.w	0; 512 (size will be reset)
	dc.w	0; 256
	dc.w	640
	dc.w	512
	dc.w	312
	dc.w	256
	dc.w	50
cds_deftt

*
*	Screen sizes
*
cds_initbs
	dc.l	$00020000
	dc.l	$00008000
	dc.w	$0080,$0200,$0100

cds_initbe
	dc.l	vde_scrn
	dc.l	vde.ssiz
	dc.w	vde.bytl,vde.xhi,vde.yhi
	end
