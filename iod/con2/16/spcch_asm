; 16 bit sprite cache routines	  V2.15 		 1999	Tony Tebby
;							  2002	Marcel Kilgus
;
; 2001-06-29  2.01  Fixed severe bug in cache setup (MK)
; 2002-11-12  2.10  Added 24bit sprite routines (MK)
; 2002-12-15  2.11  Increase the support of sprite mode (JG)
; 2003-01-12  2.12  Added support for bigger sprites (MK)
;		    Added mode 33 support for QPC/QXL (MK)
; 2003-02-17  2.13  Moved sprite list scan to pt_fsprd (MK)
; 2003-02-21  2.14  Added RLE compression and Alpha channel support (MK)
;		    Moved generic code to ptr_spcch_asm (MK)
; 2003-10-26  2.15  Correct QL mode 8 routine (JG)

	section driver

	xdef	ptc_convert
	xdef	ptc_pattern
	xdef	ptc_mask
	xdef	pt.spmax
	xdef	pt.blmax
	xdef	pt.spxsw
	xdef	pt.rpxsw
	xdef	pt.spsln
	xdef	pt.spcln
pt.sppref equ	14
	xdef	pt.sppref

	xref	pt_palsprite  ; native 'fixed' pal for 256 colours (grbgrbgx)
	xref	pt_pal16sprite; native 'fixed' pal for 16 colours (irgb)
	xref	pt_pal4sprite ; native 'fixed' pal for 4 colours (gr)
	xref	pt_pal2sprite ; native 'fixed' pal for 2 colours (w)

	xref	cn_24b_ccolr

	include 'dev8_keys_con'
	include 'dev8_keys_qdos_pt'

pt.spxsw equ	1	  ; shift pixels to sprite word
pt.rpxsw equ	1	  ; round up pixels to sprite word
pt.spsln equ	(pt.spspx*2+4)*pt.spspy ; length of sprite save area
pt.spmax equ	(pt.spspx*2+4)*pt.spspy*2+pto.hdrl+8
pt.blmax equ	(pt.spspx*2+4)*pt.spspy+pto.hdrl
pt.spcln equ	16*4+pt.spmax*2+pt.blmax*2  ; length of sprite cache

ptc_convert
	dc.w	ptcp_ql8-ptc_convert
	dc.w	ptcp_1pal-ptc_convert
	dc.w	ptcp_1bt-ptc_convert
	dc.w	ptcp_ql4-ptc_convert
	dc.w	ptcp_2pal-ptc_convert
	dc.w	ptcp_2bt-ptc_convert
	dc.w	ptcp_4pal-ptc_convert
	dc.w	ptcp_4bt-ptc_convert
	dc.w	ptcp_pal-ptc_convert
	dc.w	ptcp_8bt-ptc_convert
	dc.w	ptcp_m32bt-ptc_convert
	dc.w	ptcp_m33bt-ptc_convert
	dc.w	ptcp_24-ptc_convert
ptc_pattern
	dc.w	ptcm_ql8-ptc_convert
	dc.w	ptcm_1pal-ptc_convert
	dc.w	ptcm_1bt-ptc_convert
	dc.w	ptcm_ql4-ptc_convert
	dc.w	ptcm_2pal-ptc_convert
	dc.w	ptcm_2bt-ptc_convert
	dc.w	ptcm_4pal-ptc_convert
	dc.w	ptcm_4bt-ptc_convert
	dc.w	ptcm_pal-ptc_convert
	dc.w	ptcm_8bt-ptc_convert
	dc.w	ptcm_m32bt-ptc_convert
	dc.w	ptcm_m33bt-ptc_convert
	dc.w	ptcm_24-ptc_convert
ptc_mask

;+++
; Conversion routines
;
;	d1 c  p  x size (at least pattern routines must preserve this)
;	d2 c  p  y size (at least pattern routines must preserve this)
;	d7    p  at least lower word must be preserved
;	a0    p
;	a1    p
;	a2 c  s  pointer to original mode buffer
;	a4 cr u  pointer to native mode buffer / updated
;	a5    p
;	a6    p
;
;	all other registers might be smashed
;---

;+++
; QL mode 4 (2 bit original QL mode)
; Pattern
;---
ptcpql_pal dc.b   0,2*2,2*4,2*7

ptcp_ql4
	swap	d7
	move.l	pt_palql(a3),a3  ; palette
	move.w	d2,d7
	bra.s	ptcpql_emake

ptcpql_make
	move.w	d1,d3
ptcpql_word
	moveq	#32,d6	   ; 32 bits at a time
	move.b	(a2)+,d4
	move.b	(a2)+,d5
	lsl.w	#8,d4
	lsl.w	#8,d5
	move.b	(a2)+,d4
	move.b	(a2)+,d5
ptcpql_bit
	moveq	#0,d0
	add.w	d4,d4
	addx.b	d0,d0
	add.w	d5,d5
	addx.b	d0,d0
	move.b	ptcpql_pal(pc,d0.l),d0
	move.w	(a3,d0.l),(a4)+

	moveq	#0,d0
	add.w	d4,d4
	addx.b	d0,d0
	add.w	d5,d5
	addx.b	d0,d0
	move.b	ptcpql_pal(pc,d0.l),d0
	move.w	(a3,d0.l),(a4)+

	subq.w	#2,d3
	ble.s	ptcpql_emake
	subq.w	#4,d6
	bgt.s	ptcpql_bit
	bra.s	ptcpql_word
ptcpql_emake
	dbra	d7,ptcpql_make
	swap	d7
	rts

;+++
; QL mode 4 (2 bit original QL mode)
; Mask
;---
ptcm_ql4
	bra.s	ptcmql_emake
ptcmql_make
	move.w	d1,d3
ptcmql_word
	moveq	#32,d6	   ; 32 bits at a time
	move.b	(a2)+,d5
	or.b	(a2)+,d5
	lsl.w	#8,d5
	move.b	(a2)+,d5
	or.b	(a2)+,d5
ptcmql_bit
	moveq	#0,d0
	add.w	d5,d5
	bcc.s	ptcmql_bit2
	moveq	#-1,d0
	clr.w	d0

ptcmql_bit2
	add.w	d5,d5
	bcc.s	ptcmql_set
	not.w	d0
ptcmql_set
	move.l	d0,(a4)+

	subq.w	#2,d3
	ble.s	ptcmql_emake
	subq.w	#4,d6
	bgt.s	ptcmql_bit
	bra.s	ptcmql_word
ptcmql_emake
	dbra	d2,ptcmql_make
	rts

;+++
; QL mode 8 (4 bit original QL mode)
; Pattern
;
; The flash bit is lost... it's really too much hardware depended so far
; there is a limit to backward compatibility!
;---

ptcpql8_pal dc.b  2*0,2*1,2*2,2*3,2*0,2*1,2*2,2*3,2*4,2*5,2*6,2*7,2*4,2*5,2*6,2*7

ptcp_ql8
	swap	d7
	move.l	pt_palql(a3),a3
	move.w	d2,d7
	bra.s	ptcpql8_emake
ptcpql8_make
	move.w	d1,d3
ptcpql8_word
	moveq	#8,d6  ; 32 bits at a time, only 8 pixels (v. 2.15)!
	move.b	(a2)+,d4
	move.b	(a2)+,d5
	lsl.w	#8,d4
	lsl.w	#8,d5
	move.b	(a2)+,d4	; d4: GFGFGFGF GFGFGFGF
	move.b	(a2)+,d5	; d5: RBRBRBRB RBRBRBRB
ptcpql8_bit
	moveq	#0,d0
	add.w	d4,d4
	addx.b	d0,d0
	add.w	d4,d4
	addx.b	d0,d0
	add.w	d5,d5
	addx.b	d0,d0
	add.w	d5,d5
	addx.b	d0,d0
	move.b	ptcpql8_pal(pc,d0.l),d0
	move.w	(a3,d0.l),(a4)+
	move.w	(a3,d0.l),(a4)+ ; SIC: two GD2 pixels for one mode 8 pixel
	subq.w	#2,d3
	ble.s	ptcpql8_emake
	subq.w	#1,d6
	bgt.s	ptcpql8_bit
	bra.s	ptcpql8_word
ptcpql8_emake
	dbra	d7,ptcpql8_make
	swap	d7
	rts

;+++
; QL mode 8 (4 bit original QL mode)
; Pattern
;
; JG comment: I do not like the way the mask is handled
; It is done so in order to have the same behaviour as QL4 emulation
; as provided with 2.99, but I might want to change it some days!
; (QL native are not really 'palette' mapped... IMNSHO)
; Last note: given that wrong behavior, only R/G bits are taken into
; account so far, F/B are ignored!
;---
ptcm_ql8
	bra.s	ptcmql8_emake
ptcmql8_make
	move.w	d1,d3
ptcmql8_word
	moveq	#8,d6  ; 32 bits at a time
	move.b	(a2)+,d5
	or.b	(a2)+,d5
	lsl.w	#8,d5
	move.b	(a2)+,d5
	or.b	(a2)+,d5
ptcmql8_bit
	moveq	#0,d0
	add.w	d5,d5
	bcc.s	ptcmql8_set
	moveq	#-1,d0
ptcmql8_set
	lsl.w	#1,d5	; again, ignore the Flash/Blue mask part
	move.l	d0,(a4)+
	subq.w	#2,d3
	ble.s	ptcmql8_emake
	subq.w	#1,d6
	bgt.s	ptcmql8_bit
	bra.s	ptcmql8_word
ptcmql8_emake
	dbra	d2,ptcmql8_make
	rts

;+++
; Mode 16 (8 bit fixed palette mapped)
; Pattern & Mask
;---
ptcp_8bt
ptcm_8bt
	lea	pt_palsprite,a3 	 ; grbgrbgm palette (Aurora compatible)
ptcp_8
	move.l	d2,d6
	move.w	d1,d5
	addq.w	#1,d5
	and.w	#2,d5			 ; flag extra word
	bra.s	ptcppl_emake

ptcppl_make
	move.w	d1,d3
ptcppl_word
	moveq	#0,d4
	move.b	(a2)+,d4
	add.w	d4,d4
	move.w	(a3,d4.w),(a4)+
	moveq	#0,d4
	move.b	(a2)+,d4
	add.w	d4,d4
	move.w	(a3,d4.w),(a4)+
	subq.w	#2,d3
	bgt.s	ptcppl_word
	add.w	d5,a2			 ; skip extra words
ptcppl_emake
	dbra	d6,ptcppl_make
	rts

;+++
; Mode 31 (8 bit palette mapped)
; Pattern
;---
ptcp_pal
	move.l	pt_pal256(a3),a3	 ; palette
	bra.s	ptcp_8

;+++
; Mode 31 (8 bit palette mapped)
; Mask
;---
ptcm_pal
	bra.s	ptcmpl_emake
ptcmpl_make
	move.w	d1,d3
ptcmpl_word
	tst.b	(a2)+
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	tst.b	(a2)+
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	subq.w	#2,d3
	bgt.s	ptcmpl_word
	add.w	d5,a2			 ; skip extra words
ptcmpl_emake
	dbra	d2,ptcmpl_make
	rts

;+++
; Mode 32 (16 bit RGB in rrrrrggggggbbbbb little endian format - QPC/QXL)
; Pattern & Mask
;
; This is for Q40 only, it's dead code for QPC/QXL. But this way both
; versions can be kept within this generic file. It's only a few bytes anyway.
;
; QXL/QPC format is "R5G6B5", but in Intel ordering whereas Q40 format is
; "G5R5B5W1", but in Motorola ordering
; Mapping method: Red is Red, Blue is Blue, Most of Green is Green
; whereas Least Green is White
; It allow to be conservative and a reverse mapping is possible without loss
; of information
;
; In C: new = ((old & 0xF800)>>5) | ((old & 0x07C0)<<5)
;	    | ((old & 0x001F)<<1) | ((old & 0x0020)>>5);
;---
ptcp_m32bt
ptcm_m32bt
	movem.l d2/d7,-(sp)
	bra.s	ptcm32_emake

ptcm32_make
	move.w	d1,d6		; x size
ptcm32_word
	move.w	(a2)+,d3	; d3: old --> d3/d4/d5 == (white/red) /green /blue
	rol.w	#3,d3		; bbbbbrrr rrgggggg
	move.w	d3,d4
	move.w	d3,d5
	andi.w	#%0000011111000001,d3	; -----rrr rr-----w
	andi.w	#%0000000000111110,d4	; -------- --ggggg-
	andi.w	#%1111100000000000,d5	; bbbbb--- --------
	ror.w	#6,d4			; ggggg--- --------
	rol.w	#6,d5			; -------- --bbbbb-
	or.w	d4,d3			; gggggrrr rr-----w
	or.w	d5,d3			; gggggrrr rrbbbbbw
	move.w	d3,(a4)+
	subq.w	#1,d6
	bgt.s	ptcm32_word
	move.l	d1,d3
	andi.l	#1,d3
	beq.s	ptcm32_emake
	move.w	(a2)+,(a4)+		; do not bother to convert padding!
ptcm32_emake
	dbra	d2,ptcm32_make
	movem.l (sp)+,d2/d7
	rts

;+++
; Mode 33 (16 bit RGB in gggggrrrrrbbbbbw big endian format - Q40)
; Pattern & Mask
;
; This is for QPC/QXL only, it's dead code for the Q40. But this way both
; versions can be kept within this generic file. It's only a few bytes anyway.
;---
ptcp_m33bt
ptcm_m33bt
	movem.l d2/d7,-(sp)
	bra.s	ptcm33_emake

ptcm33_make
	move.w	d1,d6			; x size
ptcm33_word
	move.w	(a2)+,d3		; gggggrrr rrbbbbbw
	ror.w	#3,d3			; bbwggggg rrrrrbbb
	move.w	d3,d4
	move.w	d4,d5
	andi.w	#%0001111100000000,d3	; 000ggggg 00000000
	andi.w	#%0010000011111000,d4	; 00w00000 rrrrr000
	andi.w	#%1100000000000111,d5	; bb000000 00000bbb
	ror.w	#6,d5			; 000bbbbb 00000000
	rol.w	#6,d3			; gg000000 00000ggg
	or.w	d4,d3
	or.w	d5,d3
	move.w	d3,(a4)+
	subq.w	#1,d6
	bgt.s	ptcm33_word
	move.l	d1,d3
	andi.l	#1,d3
	beq.s	ptcm33_emake
	move.w	(a2)+,(a4)+		; do not bother to convert padding!
ptcm33_emake
	dbra	d2,ptcm33_make
	movem.l (sp)+,d2/d7
	rts

;+++
; Mode 64 (24 bit RGB in $RRRGGGBBB000 format)
; Pattern & Mask
;---
ptcm_24
ptcp_24
	movem.l d2/d7,-(sp)
	move.w	d1,d5
	andi.w	#1,d5			 ; flag extra word
	bra.s	ptc24_emake
ptc24_make
	move.w	d1,d6			 ; x size
ptc24_long
	move.l	(a2)+,d3
	jsr	cn_24b_ccolr		 ; trashes d0 and d7
	move.w	d3,(a4)+
	subq.w	#1,d6
	bgt.s	ptc24_long
	tst.w	d5
	beq.s	ptc24_emake
	clr.w	(a4)+
ptc24_emake
	dbra	d2,ptc24_make
	movem.l (sp)+,d2/d7
	rts

; *** Following are the less than 8 bit modes ***

;+++
; Mode 0 (1 bit black/white)
; Mask & Pattern
;---
ptcp_1bt
ptcm_1bt
	lea	pt_pal2sprite,a3	  ; white palette

; handle 1 bit / 2 colours ( white/black or palette )
ptcp_1
	move.l	d2,d6
	move.w	d1,d5
	and.w	#31,d5
	eor.w	#31,d5
	addq.w	#1,d5
	and.w	#31,d5
	lsr.w	#3,d5	; flag extra bytes
	bra.s	ptcppl1_emake

ptcppl1_make
	move.w	d1,d3
ptcppl1_word
	moveq	#0,d4
	move.b	(a2)+,d4
	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	subq.w	#2,d3
	ble.s	ptcppl1_early

	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	subq.w	#2,d3
	ble.s	ptcppl1_early

	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	subq.w	#2,d3
	ble.s	ptcppl1_early

	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+
	moveq	#0,d0
	add.b	d4,d4
	addx.b	d0,d0
	add.b	d0,d0
	move.w	(a3,d0.w),(a4)+

	subq.w	#2,d3
	bgt.s	ptcppl1_word
ptcppl1_early
	add.w	d5,a2			 ; skip extra words
ptcppl1_emake
	dbra	d6,ptcppl1_make
	rts

;+++
; Mode 3 (1 bit palette mapped)
; Pattern
;---
ptcp_1pal
	move.l	pt_pal256(a3),a3	 ; palette
	bra	ptcp_1

;+++
; Mode 3 (1 bit palette mapped)
; Mask
;---
ptcm_1pal
	move.w	d1,d5
	and.w	#31,d5
	eor.w	#31,d5
	addq.w	#1,d5
	and.w	#31,d5
	lsr.w	#3,d5	; flag extra bytes
	bra.s	ptcmpl1_emake
ptcmpl1_make
	move.w	d1,d3
ptcmpl1_word
	move.b	(a2)+,d0
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+

	subq.w	#2,d3
	ble.s	ptcmpl1_early
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+

	subq.w	#2,d3
	ble.s	ptcmpl1_early
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+

	subq.w	#2,d3
	ble.s	ptcmpl1_early
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+
	lsl.b	#1,d0
	scs	d4
	ext.w	d4
	move.w	d4,(a4)+
	subq.w	#2,d3
	bgt.s	ptcmpl1_word
ptcmpl1_early
	add.w	d5,a2			 ; skip extra words
ptcmpl1_emake
	dbra	d2,ptcmpl1_make
	rts

;+++
; Mode 4 (2 bit fixed gr)
; Pattern & Mask
;---
ptcp_2bt
ptcm_2bt
	lea	pt_pal4sprite,a3	  ; gr palette

; handle 2 bit colours (palette or red/magenta + green)
ptcp_2
	move.l	d2,d6
	move.w	d1,d5
	and.w	#15,d5
	eor.w	#15,d5
	addq.w	#1,d5
	and.w	#15,d5			  ; flag extra bytes
	lsr.w	#2,d5
	bra.s	ptcppl2_emake

ptcppl2_make
	move.w	d1,d3
ptcppl2_word
	moveq	#0,d4
	move.b	(a2)+,d4
	move.b	d4,d0	; back it up
	lsr.b	#6,d4
	add.b	d4,d4
	move.w	(a3,d4.w),(a4)+
	move.b	d0,d4
	lsr.b	#4,d4
	and.b	#3,d4
	add.b	d4,d4
	move.w	(a3,d4.w),(a4)+
	subq.w	#2,d3
	ble.s	ptcppl2_early
	move.b	d0,d4
	lsr.b	#2,d4
	and.b	#3,d4
	add.b	d4,d4
	move.w	(a3,d4.w),(a4)+
	move.b	d0,d4
	and.b	#3,d4
	add.b	d4,d4
	move.w	(a3,d4.w),(a4)+
	subq.w	#2,d3
	bgt.s	ptcppl2_word
ptcppl2_early
	add.w	d5,a2			 ; skip extra bytes
ptcppl2_emake
	dbra	d6,ptcppl2_make
	rts

;+++
; Mode 7 (2 bit palette mapped)
; Pattern
;---
ptcp_2pal
	move.l	pt_pal256(a3),a3	 ; palette
	bra	ptcp_2

;+++
; Mode 7 (2 bit palette mapped)
; Mask
;---
ptcm_2pal
	move.w	d1,d5
	and.w	#15,d5
	eor.w	#15,d5
	addq.w	#1,d5
	and.w	#15,d5			  ; flag extra bytes
	lsr.w	#2,d5
	bra.s	ptcmpl2_emake
ptcmpl2_make
	move.w	d1,d3
ptcmpl2_word
	move.b	(a2)+,d0
	move.b	d0,d6	;backup
	and.b	#%11000000,d0
	tst.b	d0
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	move.b	d6,d0
	and.b	#%00110000,d0
	tst.b	d0
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	subq.w	#2,d3
	ble.s	ptcmpl2_early
	move.b	d6,d0
	and.b	#%00001100,d0
	tst.b	d0
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	move.b	d6,d0
	andi.b	 #%00000011,d0
	tst.b	d0
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	subq.w	#2,d3
	bgt.s	ptcmpl2_word
ptcmpl2_early
	add.w	d5,a2			 ; skip extra words
ptcmpl2_emake
	dbra	d2,ptcmpl2_make
	rts

;+++
; Mode 8 (4 bit fixed irgb)
; Pattern & Mask
;---
ptcp_4bt
ptcm_4bt
	lea	pt_pal16sprite,a3	   ; irgb palette

; Handle 4 bit colours palette/mode
ptcp_4
	move.l	d2,d6
	move.w	d1,d5
	andi.w	#7,d5
	eori.w	#7,d5
	addq.w	#1,d5
	andi.w	#7,d5
	lsr.w	#1,d5
	bra.s	ptcppl4_emake

ptcppl4_make
	move.w	d1,d3
ptcppl4_word
	moveq	#0,d4
	move.b	(a2)+,d4
	move.b	d4,d0	; back it up
	lsr.b	#4,d4	; get the most significant part first
	add.b	d4,d4	; the max initial value of d4 is 15, a byte is enough for doubling
	move.w	(a3,d4.w),(a4)+
	move.b	d0,d4	; restore it
	and.b	#$0F,d4 ; get now the least significant part
	add.b	d4,d4
	move.w	(a3,d4.w),(a4)+
	subq.w	#2,d3
	bgt.s	ptcppl4_word
	add.w	d5,a2			 ; skip extra bytes
ptcppl4_emake
	dbra	d6,ptcppl4_make
	rts

;+++
; Mode 15 (4 bit palette mapped)
; Pattern
;---
ptcp_4pal
	move.l	pt_pal256(a3),a3	 ; palette
	bra	ptcp_4

;+++
; Mode 15 (4 bit palette mapped)
; Mask
;---
ptcm_4pal
	move.w	d1,d5
	andi.w	#7,d5
	eori.w	#7,d5
	addq.w	#1,d5
	andi.w	#7,d5
	lsr.w	#1,d5
	bra.s	ptcmpl4_emake
ptcmpl4_make
	move.w	d1,d3
ptcmpl4_word
	move.b	(a2)+,d0
	move.b	d0,d6	; backup it
	and.b	#$F0,d0
	tst.b	d0
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	move.b	d6,d0
	and.b	#$0F,d0
	tst.b	d0
	sne	d4
	ext.w	d4
	move.w	d4,(a4)+
	subq.w	#2,d3
	bgt.s	ptcmpl4_word
	add.w	d5,a2			 ; skip extra words
ptcmpl4_emake
	dbra	d2,ptcmpl4_make
	rts

	end
