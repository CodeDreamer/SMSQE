;	Set sub-area descriptor 	V2.00	 1990 Tony Tebby
;
;	Generate a descriptor for a sub-area, above/below/left/right
;	or including cursor line/column.
;
;	Registers:
;		Entry				Exit
;	D1					size of sub-area
;	D2					origin of sub-area
;	D3	required sub-area
;	A0	pointer to cdb
;
;	The required sub-area is described by setting bits in D3 thus:
;		Bit		Area
;		0		0	      < X < cursor top
;		1		cursor top    < X < cursor bottom
;		2		cursor bottom < X < area bottom
;		4		0	      < Y < cursor left
;		5		cursor left   < Y < cursor right
;		6		cursor right  < Y < area right
;
	section con

	include 'dev8_keys_con'

	xdef	cn_ssuba

cn_ssuba
	move.b	d3,d0			; get X bits
	lsr.b	#4,d0
	bsr.s	bms_setc		; set the co-ordinate

	addq.l	#2,a0			; offset to Y
	move.b	d3,d0			; and use Y bits
	bsr.s	bms_setc
	subq.l	#2,a0

	rts

;	Set a co-ordinate

bms_setc
	swap	d1
	swap	d2			; keep any existing co-ordinates
	and.w	#%111,d0		; bits are in 0..7
	add.w	d0,d0
	move.w	cset_tab(pc,d0.w),d0	; get setup routine
	jmp	cset_tab(pc,d0.w)	; and jump to it

cset_tab
	dc.w	set0-cset_tab
	dc.w	set1-cset_tab
	dc.w	set2-cset_tab
	dc.w	set3-cset_tab
	dc.w	set4-cset_tab
	dc.w	set7-cset_tab
	dc.w	set6-cset_tab
	dc.w	set7-cset_tab

set0
	move.w	sd_xmin(a0),d2		; origin is left
	clr.w	d1			; but no size
	rts

set1
	move.w	sd_xmin(a0),d2		; origin is left
	move.w	sd_xpos(a0),d1		; size is...up to cursor
	rts

set2
	move.w	sd_xpos(a0),d2		; origin is cursor left
	add.w	sd_xmin(a0),d2
	move.w	sd_xinc(a0),d1		; size is cursor X size
	rts

set3
	move.w	sd_xmin(a0),d2		; origin at left
	move.w	sd_xpos(a0),d1		; size is...
	add.w	sd_xinc(a0),d1		; ...to right hand end
	rts

set4
	move.w	sd_xpos(a0),d2		; origin is...
	add.w	sd_xinc(a0),d2		; ...right of cursor
	move.w	sd_xsize(a0),d1 	; size is all area...
	sub.w	d2,d1			; ...from right of cursor
	add.w	sd_xmin(a0),d2
	rts

set6
	move.w	sd_xpos(a0),d2		; origin is left of cursor
	move.w	sd_xsize(a0),d1 	; size is...
	sub.w	d2,d1			; ...from left of cursor
	add.w	sd_xmin(a0),d2
	rts

set7
	move.w	sd_xmin(a0),d2		; origin is origin
	move.w	sd_xsize(a0),d1 	; size is the lot
	rts

	end
