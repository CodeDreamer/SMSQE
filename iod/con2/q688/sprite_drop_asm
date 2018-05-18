; Sprite dropping code	  V1.12 			 1985 Tony Tebby
;							  2003 Marcel Kilgus
;
; 2003-02-11  1.10  Added alpha blending code (MK)
; 2005-03-27  1.11  Extension for empty (solid) mask (MK)
; 2013-04-28  1.12  xdef'd the aur2rgb_tab table (wl)
;
;	d0   s
;	d1   s
;	d2   s
;	d3   s
;	d4   s
;	d5 c s	sprite width / sprite word counter
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

	xref	rgb2aur_tab

	xdef	aur2rgb_tab
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
	move.l	a3,d0
	btst	#0,d0
	bne.s	odd
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

next_line
	add.w	a6,a3		; move address to next line
	dbra	d6,sp_line	; next line
	rts


odd
	move.l	(a5)+,d0
	not.l	d0
	move.b	(a3),d1
	lsl.l	#8,d1
	lsl.l	#8,d1
	move.w	1(a3),d1
	lsl.l	#8,d1
	move.b	3(a3),d1
	and.l	d1,d0
	move.l	(a4)+,d1
	eor.l	d1,d0
	move.b	d0,3(a3)
	lsr.l	#8,d0
	move.w	d0,1(a3)
	lsr.l	#8,d0
	lsr.l	#8,d0
	move.b	d0,(a3)
	addq.l	#4,a3
;	 move.l  d0,(a3)+
	subq.w	#1,d4		next long word
	bgt.s	odd	 ... there is another one
	bra.s	next_line





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
	move.l	a1,-(sp)
	lea	aur2rgb_tab(pc),a0
	lea	rgb2aur_tab(pc),a1
	swap	d5		; get real sprite width
	moveq	#4,d0		; calculata pattern padding
	sub.w	d5,d0
	and.w	#3,d0
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
	clr.w	d0
	move.b	(a4)+,d0	; source
	move.b	(a5)+,d6	; alpha channel
	beq.s	alpha_loope	; completely transparent
	cmp.b	#255,d6
	beq.s	alpha_write	; completely opaque

	clr.w	d3
	move.b	(a3),d3 	; destination
	add.w	d0,d0
	add.w	d3,d3
	move.w	(a0,d0.w),d0	; convert to R3G3B3
	move.w	(a0,d3.w),d3
	move.w	d0,d1
	move.w	d0,d2
	move.w	d3,d4
	move.w	d3,d7
	and.w	#%000000111,d0	; split into components
	and.w	#%000111000,d1
	and.w	#%111000000,d2
	and.w	#%000000111,d3	; split into components
	and.w	#%000111000,d4
	and.w	#%111000000,d7

	sub.w	d3,d0		; source - destination
	sub.w	d4,d1
	sub.w	d7,d2
	muls	d6,d0		; (source - destination) * alpha
	muls	d6,d1
	muls	d6,d2
	asr.l	#8,d0		; normalize
	asr.l	#8,d1
	asr.l	#8,d2
	and.w	#$FFF8,d1	; and cut
	and.w	#$FFC0,d2
	add.w	d3,d0
	add.w	d4,d1
	add.w	d7,d2
	or.w	d1,d0
	or.w	d2,d0
	move.b	(a1,d0.w),d0

alpha_write
	move.b	d0,(a3)
alpha_loope
	addq.w	#1,a3
	subq.w	#1,d5		; next byte
	bgt.s	alpha_loop	; ... there is another one

	swap	d5
	add.w	d5,a3		; add padding
	add.w	d5,a4
	swap	d6
	add.w	a6,a3		; move address to next line
	dbra	d6,alpha_line	; next line
	move.l	(sp)+,d5
	move.l	(sp)+,a1
	rts

aur2rgb_tab
	dc.w	$000,$040,$008,$048,$002,$003,$00A,$00B
	dc.w	$080,$0C0,$088,$0C8,$082,$0C3,$08A,$0CB
	dc.w	$010,$050,$018,$058,$012,$013,$01A,$01B
	dc.w	$090,$0D0,$098,$0D8,$092,$0D3,$09A,$0DB
	dc.w	$004,$005,$00C,$00D,$006,$007,$00E,$00F
	dc.w	$084,$0C5,$08C,$0CD,$086,$0C7,$08E,$0CF
	dc.w	$014,$015,$01C,$01D,$016,$017,$01E,$01F
	dc.w	$094,$0D5,$09C,$0DD,$096,$0D7,$09E,$0DF
	dc.w	$100,$140,$108,$148,$102,$143,$10A,$14B
	dc.w	$180,$1C0,$188,$1C8,$182,$1C3,$18A,$1CB
	dc.w	$110,$150,$118,$158,$112,$153,$11A,$15B
	dc.w	$190,$1D0,$198,$1D8,$192,$1D3,$19A,$1DB
	dc.w	$104,$145,$10C,$14D,$106,$147,$10E,$14F
	dc.w	$184,$1C5,$18C,$1CD,$186,$1C7,$18E,$1CF
	dc.w	$114,$155,$11C,$15D,$116,$157,$11E,$15F
	dc.w	$194,$1D5,$19C,$1DD,$196,$1D7,$19E,$1DF
	dc.w	$020,$060,$028,$068,$022,$023,$02A,$02B
	dc.w	$0A0,$0E0,$0A8,$0E8,$0A2,$0E3,$0AA,$0EB
	dc.w	$030,$070,$038,$078,$032,$033,$03A,$03B
	dc.w	$0B0,$0F0,$0B8,$0F8,$0B2,$0F3,$0BA,$0FB
	dc.w	$024,$025,$02C,$02D,$026,$027,$02E,$02F
	dc.w	$0A4,$0E5,$0AC,$0ED,$0A6,$0E7,$0AE,$0EF
	dc.w	$034,$035,$03C,$03D,$036,$037,$03E,$03F
	dc.w	$0B4,$0F5,$0BC,$0FD,$0B6,$0F7,$0BE,$0FF
	dc.w	$120,$160,$128,$168,$122,$163,$12A,$16B
	dc.w	$1A0,$1E0,$1A8,$1E8,$1A2,$1E3,$1AA,$1EB
	dc.w	$130,$170,$138,$178,$132,$173,$13A,$17B
	dc.w	$1B0,$1F0,$1B8,$1F8,$1B2,$1F3,$1BA,$1FB
	dc.w	$124,$165,$12C,$16D,$126,$167,$12E,$16F
	dc.w	$1A4,$1E5,$1AC,$1ED,$1A6,$1E7,$1AE,$1EF
	dc.w	$134,$175,$13C,$17D,$136,$177,$13E,$17F
	dc.w	$1B4,$1F5,$1BC,$1FD,$1B6,$1F7,$1BE,$1FF

	end
