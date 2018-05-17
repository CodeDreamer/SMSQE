; Sprite dropping code	  V1.11 			 1985 Tony Tebby
;							  2003 Marcel Kilgus
;
; 2003-02-11  1.10  Added alpha blending code (MK)
; 2005-03-27  1.11  Extension for empty (solid) mask (MK)
;
;	d0   s
;	d1   s
;	d2   s
;	d3   s
;	d4   s
;	d5 c s	sprite width or -1 (=no alpha) / sprite word counter
;	d6 c s	row counter
;	d7   s
;
;	a0   s
;	a3 c s	new pointer to screen
;	a4   s	pointer to sprite pattern
;	a5 c s	pointer to sprite pointers / pointer to sprite mask
;	a6 c p	address increment of screen
;
	section driver
;
	xdef	sp_drop
	xdef	sp_dropi
;
sp_drop
	move.l	a5,a4		; set pointer to sprite pattern
	add.l	(a5)+,a4
	move.l	a5,d3
	move.l	(a5),a5
	move.l	a5,d4
	beq.s	spd_nomask	; ... sprite mask ptr is 0
	add.l	d3,a5		; absolute sprite mask ptr
spd_nomask
;	 add.w	 d0,a4		 ; offset to start (not needed anymore? MK)
;	 add.w	 d0,a5
sp_dropi
	move.l	a5,d0		; any mask?
	beq.s	sp_nomask

	tst.l	d5		; alpha blending requested (MSW positive)?
	bpl.s	alpha_dropi

sp_line
	move.l	d5,d4		; set counter
;
long_word
	move.l	(a5)+,d0
	not.l	d0
	and.l	(a3),d0
	move.l	(a4)+,d1
	eor.l	d1,d0
	move.l	d0,(a3)+
	subq.w	#1,d4		; next long word
	bgt.s	long_word	; ... there is another one

	add.w	a6,a3		; move address to next line
	dbra	d6,sp_line	; next line
	rts
;
sp_nomask
	move.l	d5,d4		; set counter
;
snm_long_word
	move.l	(a4)+,(a3)+
	subq.w	#1,d4		; next long word
	bgt.s	snm_long_word	; ... there is another one

	add.w	a6,a3		; move address to next line
	dbra	d6,sp_nomask	; next line
	rts

;
alpha_dropi
	swap	d5		; get real sprite width
	move.w	d5,d0		; calculata pattern padding
	and.w	#1,d0
	add.w	d0,d0
	swap	d5
	move.w	d0,d5		; and save it
	swap	d5
	move.l	d5,-(sp)
;
alpha_line
	move.l	(sp),d5 	; restore counter
	swap	d6
	clr.w	d6
;
alpha_loop
	move.w	(a4)+,d0	; source
	move.b	(a5)+,d6	; alpha channel
	beq.s	alpha_loope	; completely transparent
	cmp.b	#255,d6
	beq.s	alpha_write	; completely opaque

	move.w	(a3),d3 	; destination
	ror.w	#8,d0
	ror.w	#8,d3
	move.w	d0,d1		; rrrrrggggggbbbbb
	move.w	d0,d2
	move.w	d3,d4
	move.w	d3,d7
	and.w	#%0000000000011111,d0	; split into components
	and.w	#%0000011111100000,d1
	and.l	#%1111100000000000,d2
	and.w	#%0000000000011111,d3	; split into components
	and.w	#%0000011111100000,d4
	and.l	#%1111100000000000,d7

	sub.w	d3,d0		; source - dest
	sub.w	d4,d1
	sub.l	d7,d2
	asr.l	#8,d2		; lower it, we only have a 16 bit mul
	muls	d6,d0		; (source - dest) * alpha
	muls	d6,d1
	muls	d6,d2
	asr.l	#8,d0		; normalize
	asr.l	#8,d1
	and.w	#%1111111111100000,d1	; and cut
	and.w	#%1111100000000000,d2
	add.w	d3,d0		; dest + (source - dest) * alpha
	add.w	d4,d1
	add.w	d7,d2
	or.w	d1,d0
	or.w	d2,d0

	ror.w	#8,d0
alpha_write
	move.w	d0,(a3)
alpha_loope
	addq.w	#2,a3
	subq.w	#1,d5		; next pixel
	bgt.s	alpha_loop	; ... there is another one

	swap	d5
	add.w	d5,a3		; add padding
	add.w	d5,a4
	swap	d6
	add.w	a6,a3		; move address to next line
	dbra	d6,alpha_line	; next line
	move.l	(sp)+,d5
	rts

	end
