; Combine 2 blocks with alpha blending and write result to screen
; (8 bit aurora mode)
; v.1.01  make generalized (wl)
; v.1.00  first specialized version (destination was always screen)
;
; based on
; MK's sprite dropping code and on
; Move area (generalised PAN/SCROLL)
;
	section con

	xdef	pt_cmbblk

	xref	rgb2aur_tab
	xref	aur2rgb_tab		; conversion tables
	include dev8_keys_java

;	d0    s ? / some arbitray value
;	d1 c  s size of section to combine / scratch
;	d2 c  s origin in source area1 / scratch
;	d3 c  s origin in destination area / scratch
;	d4 c  s origin in source area 2/ scratch
;	d5    s / (address of conversion table) scratch
;	d6 c	alpha / preserved
;	d7 c  s row increment of source area 2 / scratch
;	a0    s / scratch (address of conversion table)
;	a1 c  s base address of source area2  / scratch
;	a2 c  s row increment of source area1 / scratch
;	a3 c  s row increment of destination area / scratch
;	a4 c  s base address of source area1 / scratch
;	a5 c  s base address of destination area / scratch
;	a6/a7	preserved
;


alregs	reg	d1-d3/d7



pt_cmbblk
	moveq	#jt5.comb,d0
	dc.w	jva.trp5
	tst.l	d0
	bne.s	cont
	rts
cont

; set source 1 address
	move.l	a2,d0			 ; row increment
	mulu	d2,d0
	add.l	d0,a4			 ; start of row
	swap	d2
	add.w	d2,a4			 ; first pixel
		    
; set source 2 address
	move.l	d7,d0			 ; row increment
	mulu	d4,d0
	add.l	d0,a1			 ; start of row
	swap	d4
	add.w	d4,a1			 ; first pixel

; set destination address
	move.l	a3,d0			 ; row increment
	mulu	d3,d0
	add.l	d0,a5			 ; start of row
	swap	d3
	add.w	d3,a5			 ; first pixel


	move.w	d1,d3			 ; row count
	ble	cmbu_rts		 ; what, no or negative rows?
	swap	d1
	move.w	d1,d0			 ;
	ble	cmbu_rts		 ; no or negative xsize?
	sub.w	d0,a2
	sub.w	d0,a3			 ; corrected increment
	sub.w	d0,d7

	move.l	a1,d5			 ; D5 =  source2
	lea	aur2rgb_tab(pc),a0
	lea	rgb2aur_tab(pc),a1	 ; a1 = table
	subq.w	#1,d1			 ; take one byte off width
	subq.w	#1,d3			 ; d3 was > 0
	clr.l	d2
;	 clr.l	 d4			  ; prepare for loop
cmbu_loop
	move.l	d1,d2
cmb_lp
	clr.l	d0
	move.b	(a4)+,d0		 ; source1
	movem.l alregs,-(a7)
	clr.l	d3
	clr.l	d7
	exg	d5,a1			 ; a1 = source2
	move.b	(a1)+,d3		 ; "destination" = source2
	add.w	d0,d0
	add.w	d3,d3
	move.w	(a0,d0.w),d0		 ; convert to R3G3B3
	move.w	(a0,d3.w),d3
	move.w	d0,d1
	move.w	d0,d2
	move.w	d3,d4
	move.w	d3,d7
	and.w	#%000000111,d0		 ; split into components
	and.w	#%000111000,d1
	and.w	#%111000000,d2
	and.w	#%000000111,d3		 ; split into components
	and.w	#%000111000,d4
	and.w	#%111000000,d7

	sub.w	d3,d0			 ; source - src2
	sub.w	d4,d1
	sub.w	d7,d2
	muls	d6,d0			 ; (source - src2) * alpha
	muls	d6,d1
	muls	d6,d2
	asr.l	#8,d0			 ; normalize
	asr.l	#8,d1
	asr.l	#8,d2
	and.w	#$FFF8,d1		 ; and cut
	and.w	#$FFC0,d2
	add.w	d3,d0
	add.w	d4,d1
	add.w	d7,d2
	or.w	d1,d0
	or.w	d2,d0
	exg	d5,a1			 ; a1 = conversion table
	move.b	(a1,d0.w),d0
alpha_write
	movem.l (a7)+,alregs
	move.b	d0,(a5)+		 ; copy byte into screen
cmbu_lle
	dbra	d2,cmb_lp

cmbu_lpe
	add.l	a2,a4			 ; start of next line
	add.l	a3,a5
	add.l	d7,d5			 ; d5 = source2
cmbu_blke
	dbra	d3,cmbu_loop

cmbu_rts
	rts

	end
