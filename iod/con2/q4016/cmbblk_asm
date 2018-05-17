; Combine 2 blocks with alpha blending and write result to screen
; (16 bit mode 32)
; v.1.01  make generalized (wl)
; v.1.00  first specialized version (destination was always screen)
;
; based on
; MK's sprite dropping code and on
; Move area (generalised PAN/SCROLL)
;
    


	section con

	xdef	pt_cmbblk

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


alregs	reg	d1-d3/d7



pt_cmbblk

; set source1 address
	move.l	a2,d0			; row increment
	mulu	d2,d0
	add.l	d0,a4			; start of row
	swap	d2
	add.w	d2,a4
	add.w	d2,a4			; first pixel

; set source2 address
	move.l	d7,d0			; row increment
	mulu	d4,d0			; * nbr of rows
	add.l	d0,a1			; start of row
	swap	d4
	add.w	d4,a1
	add.w	d4,a1			; first pixel to treat in source2

; set destination address
	move.l	a3,d0			; row increment
	mulu	d3,d0
	add.l	d0,a5			; start of row
	swap	d3
	add.w	d3,a5
	add.w	d3,a5			; first pixel

	move.w	d1,d3			; row count (block y size)
	ble	cmbu_rts		; 0 or neg y size  ?
	swap	d1			; x size = width in pixels
	move.w	d1,d0			; 1 pix = 1 word in mode 32
	ble	cmbu_rts		; 0 or neg x size ?
	add.w	d0,d0			; xsize in words of block
	sub.w	d0,a2			; row increment - xsize of block (incrementation in loop)
	sub.w	d0,a3			; corrected increment
	sub.w	d0,d7

	subq.w	#1,d1			; take one word off width
	subq.w	#1,d3			; prepare for dbf (d3 was >0)
cmbu_loop
	move.w	d1,d2			; nbr of words to move adjusted for dbf
cmbu_llp
	move.w	(a4)+,d0		; get from source 1
	movem.l  alregs,-(a7)		;
	move.w	(a1)+,d3		; second source

	move.w	d0,d1			; gggggrrrrrbbbbbw
	move.w	d0,d2
	move.w	d3,d4
	move.w	d3,d7
	and.w	#%0000000000111110,d0	; split into components
	and.w	#%0000011111000000,d1
	and.l	#%1111100000000000,d2
	and.w	#%0000000000111110,d3	; split into components
	and.w	#%0000011111000000,d4
	and.l	#%1111100000000000,d7

	sub.w	d3,d0			; source1 - src2
	sub.w	d4,d1
	sub.l	d7,d2
	asr.l	#8,d2			; lower it, we only have a 16 bit mul
	muls	d6,d0			; (source1 - src2) * alpha
	muls	d6,d1
	muls	d6,d2
	asr.l	#8,d0			; normalize
	asr.l	#8,d1
	and.w	#%1111111111111110,d0	; and cut
	and.w	#%1111111111000000,d1
	and.w	#%1111100000000000,d2
	add.w	d3,d0			; dest + (source1 - src2) * alpha
	add.w	d4,d1
	add.w	d7,d2
	or.w	d1,d0
	or.w	d2,d0
	movem.l (a7)+,alregs
	move.w	d0,(a5)+
	dbra	d2,cmbu_llp

cmbu_lpe
	add.l	a2,a4			 ; start of next line
	add.l	d7,a1
	add.l	a3,a5
cmbu_blke
	dbra	d3,cmbu_loop

cmbu_rts
	rts

	end
