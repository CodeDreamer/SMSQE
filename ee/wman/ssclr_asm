* Set Single Colour Patterns   V1.01    1986  Tony Tebby   QJUMP
*					 2003  Marcel Kilgus
*
* 2002-11-13  1.00  Added support for new colour definitions
* 2003-04-30  1.01  Uses new "sprite cache version" byte definition
*
	section wman
*
	xdef	wm_ssclr
*
	xref	wmc_colour
*
	include dev8_keys_wstatus
	include dev8_keys_qdos_io
	include dev8_keys_qdos_pt
*
*	d1 c  p (word) colour number
*	a1 c  p pointer to window status area
*	a2 c  p pointer to pattern ($60 bytes long)
*
reglist reg	d1-d3/a2
reg.d1	equ	0
reg.a2	equ	12

wm_ssclr
	movem.l reglist,-(sp)
	moveq	#0,d3			default is ql pattern only
	bsr	wmc_colour
	beq.s	qms_oldstyle		yes, it's just original colour code

; D0 is now either iow.papp (palette) or iow.papt (true colour)
	adda.l	#$20,a2 		leave space for ql pattern
	move.w	#$021f,d2		palette mapped
	moveq	#4,d3			pattern is 4 bytes long
	cmp.l	#iow.papp,d0
	beq.s	qms_header
	move.w	#$0240,d2		use true colour
	moveq	#4*4,d3 		pattern is 16 bytes long
qms_header
	move.w	d2,(a2)+		mode of pattern
	clr.b	(a2)+
	move.b	ws_scach(a1),d0
	addq.b	#1,d0
	andi.w	#pto.cver,d0
	move.b	d0,ws_scach(a1)
	move.b	d0,(a2)+		sprite cache version
	move.l	#$00040001,(a2)+	4x1
	clr.l	(a2)+			origin zero
	moveq	#$c,d0
	move.l	d0,(a2)+		set relative pointer to pattern
	add.l	d3,d0			add pattern size
	subq.l	#4,d0			adjust relative address
	move.l	d0,(a2)+		set relative pointer to mask
	clr.l	(a2)+			no further pattern
*
	cmp.l	#4,d3			8bpp?
	beq.s	qms_8bpp

	move.l	d1,(a2)+		insert 24bpp pattern 4 times
	move.l	d1,(a2)+
	move.l	d1,(a2)+
	move.l	d1,(a2)+
	moveq	#-1,d1			solid mask
	move.l	d1,(a2)+
	move.l	d1,(a2)+
	move.l	d1,(a2)+
	move.l	d1,(a2)+
	bra.s	qms_addold

qms_8bpp
	move.b	d1,(a2)+		4 pixel a 8bit
	move.b	d1,(a2)+
	move.b	d1,(a2)+
	move.b	d1,(a2)+
	moveq	#-1,d1			32 bit solid mask
	move.l	d1,(a2)+

; Also add an old ql mode pattern for compatibility
qms_addold
	moveq	#$c,d3			relative ptr to high colour pattern
	move.l	reg.d1(sp),d1		original colour code
	move.l	reg.a2(sp),a2		start of pattern buffer

qms_oldstyle
	move.w	ws_wmode(a1),(a2)+	set mode of pattern
	clr.b	(a2)+
	move.b	ws_scach(a1),d0
	addq.b	#1,d0
	andi.w	#pto.cver,d0
	move.b	d0,ws_scach(a1)
	move.b	d0,(a2)+		sprite cache version
	move.l	#$00100001,(a2)+	16x1
	clr.l	(a2)+			origin zero
	moveq	#$c,d0
	move.l	d0,(a2)+		set relative pointer to pattern
	move.l	d0,(a2)+		... and mask
	move.l	d3,(a2)+		probably pointer to other pattern
*
	and.w	#$07,d1 		0 to 7 only
	add.w	d1,d1			all patterns are a word
	tst.b	ws_wmode+1(a1)		4 colour mode?
	beq.s	wmp_pset		... yes
	add.w	#wmp_8c-wmp_4c,d1
wmp_pset
	move.w	wmp_4c(pc,d1.w),d0	get pattern
	move.w	d0,(a2)+		set one word
	move.w	d0,(a2)+		and next
	moveq	#-1,d0
	move.l	d0,(a2)+		set mask
*
	movem.l (sp)+,reglist
	moveq	#0,d0			done
	rts
*
wmp_4c	dc.w	$0000,$0000,$00ff,$00ff,$ff00,$ff00,$ffff,$ffff
wmp_8c	dc.w	$0000,$0055,$00aa,$00ff,$aa00,$aa55,$aaaa,$aaff
	end
