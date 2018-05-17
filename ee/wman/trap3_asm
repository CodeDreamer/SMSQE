; New colour trap dispatcher   V1.02	2002 Marcel Kilgus
;
; 2003-01-30  1.01  Fixed condition code returns (MK)
; 2002-06-05  1.02  Fixed bug in setborder (MK + WL)
; 2010-12-17  1.03  Fixed 3d border with zero width

	section wman

	xdef	wmc_trap3
	xdef	wmc_colour
	xdef	wmc_error

	xref	wm_getspentry
	xref	wm_3db
	xref	wm_rmbrdr

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_wman'

wmc.reg reg	d1-d2
wmc.d1	equ	0
wmc.d2	equ	4

; Could do a jump table but it's only 6 different codes anyway
wmc_trap3
	movem.l wmc.reg,-(sp)
	cmp.w	#iow.defb,d0
	beq.s	wmc_defb
	cmp.w	#iow.defw,d0
	beq.s	wmc_defw
	cmp.w	#iow.spap,d0
	beq	wmc_spap
	cmp.w	#iow.sstr,d0
	beq	wmc_sstr
	cmp.w	#iow.sink,d0
	beq	wmc_sink
	cmp.w	#iow.blok,d0
	beq	wmc_blok
	movem.l (sp)+,wmc.reg
	trap	#3
	tst.l	d0
	rts

trap3
	move.l	wmc.d2(sp),d2
	trap	#3
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Define window
wmc_defw
	tst.w	d2		; no border? -> no special code
	beq.s	trap3

	movem.l d1-d3,-(sp)
	moveq	#0,d2		; no border at first
	trap	#3
	tst.l	d0
	bne.s	wmc_err12
	movem.l (sp)+,d1-d3

	moveq	#iow.defb,d0
	bra.s	wmc_setborder

wmc_err16
	adda.w	#4,sp
wmc_err12
	adda.w	#12,sp		; skip d1-d3
	movem.l (sp)+,wmc.reg	; restore original d1/d2
	rts

; Define window border
wmc_defb
	jsr	wm_rmbrdr	; first remove existing border
wmc_setborder
	bsr	wmc_colour
	bne.s	wmsb_ordinary	; high colour border

	move.w	d1,d2		; either "oldstyle" or 3d special
	andi.w	#$ff00,d2
	beq.s	wmsb_draw

	move.w	wmc.d2+2(sp),d2 ; border width
	beq.s	wmsb_draw
	jsr	wm_3db		; draw 3d border
	bra.s	wmsb_exit

wmsb_ordinary
	addq.l	#iow.borp-iow.papp,d0
	tst.l	d2		; stipple requested?
	bmi.s	wmsb_draw	; no

	movem.l d0-d3,-(sp)
	moveq	#-1,d2		; define main colour
	trap	#3
	tst.l	d0
	bne.s	wmc_err16
	movem.l (sp)+,d0-d3
	moveq	#0,d1
	move.w	d2,d1		; stipple colour in d1

wmsb_draw
	move.w	wmc.d2+2(sp),d2 ; border width. MSW of d2 is stipple code or -1
	trap	#3
wmsb_exit
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Set paper colour
wmc_spap
	bsr	wmc_colour
	beq	trap3
wmc_new3
	tst.l	d2		; stipple requested?
	bmi.s	wm3_solid

	movem.l d0-d2,-(sp)
	moveq	#-1,d2
	trap	#3		; first do solid colour
	tst.l	d0
	bne.s	wmc_err12
	movem.l (sp)+,d0-d2
	moveq	#0,d1
	move.w	d2,d1		; stipple colour
	swap	d2		; stipple key

wm3_solid
	trap	#3
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Set strip colour
wmc_sstr
	bsr.s	wmc_colour
	beq	trap3
	addq.l	#iow.strp-iow.papp,d0
	bra.s	wmc_new3

; Set ink colour
wmc_sink
	bsr.s	wmc_colour
	beq	trap3
	addq.l	#iow.inkp-iow.papp,d0
	bra.s	wmc_new3

; Draw block
wmc_blok
	bsr.s	wmc_colour
	beq	trap3

	move.l	a2,-(sp)
	suba.l	#3*4,sp 	; build colour block on stack
	move.l	d1,(sp) 	; main colour
	tst.l	d2
	bmi.s	wmb_solid	; no stipple

	clr.w	4(sp)
	move.w	d2,6(sp)	; stipple colour
	swap	d2
	clr.w	8(sp)
	move.w	d2,10(sp)	; stipple code
	bra.s	wmb_draw

wmb_solid
	move.l	d1,4(sp)
	move.l	d2,8(sp)	; no stipple
wmb_draw
	subi.l	#iow.papp,d0	; d0 now 0, 4 or 8
	lsr.l	#2,d0		; 0, 1 or 2
	addi.l	#iow.blkp,d0	; iow.blkp, iow.blkt or iow.blkn
	movea.l sp,a2		; colour block
	trap	#3

	adda.l	#3*4,sp 	; release stack frame
	movem.l (sp)+,a2
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

;+++
; Interpret colour code given as a word and convert it to a code
; that can be given to one of the traps. Also return which trap to
; use for the code.
;
;	d0  r	either untouched (Z set) or a new paper colour trap # (Z clear)
;	d1 cr	old word colour code / new colour code
;	d2  r	-1 if no stipple, 000s00cc for stipple code/stipple colour
;	all other registers preserved
;--
wmc_colour
	move.l	d3,-(sp)
	moveq	#6,d3		; allow 5 recursive sys pal entries
wmc_loop
	btst	#wmc..rgb,d1	; 15 bit RGB definition?
	bne	wmc_15bit
	btst	#wmc..stip,d1
	bne.s	wmc_stipple	; 2*6 bit stipple definition?

	move.l	d1,-(sp)
	lsr.w	#8,d1
	beq.s	wmc_keep	; just keep colour code

	cmp.b	#wmc.pal,d1	; palette mode?
	beq.s	wmc_palette

	cmp.b	#wmc.syspal,d1	; system palette?
	beq.s	wmc_syspal

	cmp.b	#wmc.gray,d1	; gray colour?
	beq.s	wmc_gray

	cmp.b	#wmc.3db,d1	; special border code?
	bne.s	wmc_error8	; unknown colour code

; Old style or special code - just pass back original colour code
wmc_keep
	movem.l (sp)+,d1/d3	; Keep Z flag state!
	rts

; Return error colour
wmc_error8
	move.l	(sp)+,d1
wmc_error4
	move.l	(sp)+,d3
wmc_error
	move.w	#238,d1 	; error colour
	cmp.b	d1,d1		; set Z flag
	rts

; Palette entry
wmc_palette
	movem.l (sp)+,d1/d3
	andi.l	#$ff,d1
	moveq	#-1,d2		; no stipple
	moveq	#iow.papp,d0	; use palette functions
	rts

; System palette entry. Can be recursive
wmc_syspal
	move.l	(sp)+,d1
	andi.w	#$ff,d1
	bsr	wm_getspentry	; get entry out of system palette
	subq.w	#1,d3		; max recursion level reached?
	bne.s	wmc_loop	; no, evaluate colour
	bra.s	wmc_error4

; Stipple colour
wmc_stipple
	move.w	d1,d2		; %000000000000000001wwxxxxxxyyyyyy
	andi.l	#$3f,d1 	; %00000000000000000000000000yyyyyy
	asl.l	#4,d2		; %00000000000001wwxxxxxxyyyyyy0000
	rol.w	#6,d2		; %00000000000001wwyyyyyy0000xxxxxx
	andi.l	#$3003f,d2	; %00000000000000ww0000000000xxxxxx
	move.l	(sp)+,d3
	moveq	#iow.papp,d0	; use palette functions
	rts

; Gray colour
wmc_gray
	moveq	#0,d1
	move.b	3(sp),d1
	asl.l	#8,d1
	move.b	3(sp),d1	; repeat byte
	asl.l	#8,d1
	move.b	3(sp),d1	; and again
	asl.l	#8,d1
	adda.l	#4,sp
	move.l	(sp)+,d3
	moveq	#-1,d2		; no stipple
	moveq	#iow.papt,d0	; true colour routine
	rts

; 15 bit RGB colour
wmc_15bit
	move.w	d1,d0
	swap	d0
	move.w	d1,d0
	rol.w	#6,d1
	lsr.w	#5,d0
	andi.w	#$1f,d1 	; r
	andi.w	#$1f,d0 	; g
	move.b	wmc_5to8(pc,d1.w),d1	; R
	asl.l	#8,d1
	move.b	wmc_5to8(pc,d0.w),d1	; RG
	asl.l	#8,d1
	swap	d0
	andi.w	#$1f,d0 	; b
	move.b	wmc_5to8(pc,d0.w),d1	; RGB
	asl.l	#8,d1			; RGB0
	move.l	(sp)+,d3
	moveq	#-1,d2		; no stipple
	moveq	#iow.papt,d0	; use true colour functions
	rts

wmc_5to8
	dc.b	$00,$08,$10,$18,$21,$29,$31,$39
	dc.b	$42,$4a,$52,$5a,$63,$6b,$73,$7b
	dc.b	$84,$8c,$94,$9c,$a5,$ad,$b5,$bd
	dc.b	$c6,$ce,$d6,$de,$e7,$ef,$f7,$ff

	end
