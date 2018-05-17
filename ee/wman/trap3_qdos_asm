; New colour trap dispatcher   V1.02		2003 Marcel Kilgus
; QDOS Version!
;
; 2003-01-30  1.01  Fixed condition code returns
; 2010-12-17  1.02  Fixed 3d border with zero width

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
	bsr	wmc_colour
	trap	#3
	movem.l (sp)+,wmc.reg	; restore original d1/d2
	tst.l	d0
	rts

; Define window border
wmc_defb
	jsr	wm_rmbrdr	; first remove existing border
wmc_setborder
	bsr	wmc_colour

	move.w	d1,d2		; either "oldstyle" or 3d special
	andi.w	#$ff00,d2
	beq.s	wmsb_draw

	move.w	wmc.d2+2(sp),d2 ; border width
	beq.s	wmsb_draw
	jsr	wm_3db		; draw 3d border
	bra.s	wmsb_exit

wmsb_draw
	move.w	wmc.d2+2(sp),d2 ; border width
	trap	#3
wmsb_exit
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Set paper colour
wmc_spap
	bsr.s	wmc_colour
	trap	#3
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Set strip colour
wmc_sstr
	bsr.s	wmc_colour
	trap	#3
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Set ink colour
wmc_sink
	bsr.s	wmc_colour
	trap	#3
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

; Draw block
wmc_blok
	bsr.s	wmc_colour
	trap	#3
	movem.l (sp)+,wmc.reg
	tst.l	d0
	rts

;+++
; Adjust colour code for normal trap processing.
;
;	d0    p trap code
;	d1 cr	old word colour code / new colour code
;	all other registers preserved
;--
wmc_colour
	move.l	d3,-(sp)
	moveq	#6,d3		; allow 5 recursive sys pal entries
wmc_loop
	btst	#wmc..rgb,d1	; 15 bit RGB definition?
	bne	wmc_error4	; cannot do that
	btst	#wmc..stip,d1	; 2*6 bit stipple definition?
	bne.s	wmc_error4	; cannot do that

	move.l	d1,-(sp)
	lsr.w	#8,d1
	beq.s	wmc_keep	; just keep colour code

	cmp.b	#wmc.pal,d1	; palette mode?
	beq.s	wmc_error8	; cannot do that

	cmp.b	#wmc.syspal,d1	; system palette?
	beq.s	wmc_syspal	; only one we can (try)

	cmp.b	#wmc.gray,d1	; gray colour?
	beq.s	wmc_error8	; cannot do that

	cmp.b	#wmc.3db,d1	; special border code?
	bne.s	wmc_error8	; unknown colour code

; Old style or special code - just pass back original colour code
wmc_keep
	movem.l (sp)+,d1/d3	; keep Z flag!
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

; System palette entry. Can be recursive
wmc_syspal
	move.l	(sp)+,d1
	andi.w	#$ff,d1
	bsr	wm_getspentry	; get entry out of system palette
	subq.w	#1,d3		; max recursion level reached?
	bne.s	wmc_loop	; no, evaluate colour
	bra.s	wmc_error4

	end
