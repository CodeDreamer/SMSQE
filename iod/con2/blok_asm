;	Write a block			V2.01   1998 Tony Tebby
;
; 2016-04-16  2.01  Added alpha blending support (MK)
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D1	colour (QL)
;	A0	^ cdb
;	A1	^ block descriptor
;	A2	^ primary colour, secondary colour, stipple
;
	section con

	include 'dev8_keys_con'
	include 'dev8_keys_err'

	xdef	cn_blok
	xdef	cn_blkp
	xdef	cn_blkt
	xdef	cn_blkn

	xref	cn_ql_colr
	xref	cn_pal_mcolr
	xref	cn_pal_ccolr
	xref	cn_24b_mcolr
	xref	cn_24b_ccolr
	xref	cn_nat_mcolr
	xref	cn_nat_ccolr

	xref	cn_chken
	xref	cn_xblock
	xref	cn_fblock
	xref	cn_ablock

cn_blok
	move.w	d1,d3			; colour
	jsr	cn_ql_colr		; set QL colour
	bra.s	cnb_draw

cn_blkp
	move.l	(a2)+,d3		; main colour
	jsr	cn_pal_mcolr
	move.l	(a2)+,d3		; contrast colour
	move.l	(a2)+,d6
	bmi.s	cnb_draw		; ... none
	jsr	cn_pal_ccolr
	bra.s	cnb_draw

cn_blkt
	move.l	(a2)+,d3		; main colour
	jsr	cn_24b_mcolr
	move.l	(a2)+,d3		; contrast colour
	move.l	(a2)+,d6
	bmi.s	cnb_draw		; ... none
	jsr	cn_24b_ccolr
	bra.s	cnb_draw

cn_blkn
	move.l	(a2)+,d3		; main colour
	jsr	cn_nat_mcolr
	move.l	(a2)+,d3		; contrast colour
	move.l	(a2)+,d6
	bmi.s	cnb_draw		; ... none
	jsr	cn_nat_ccolr

cnb_draw
	addq.w	#1,d6
	cmp.w	#4,d6			; valid stipple?
	bhi.s	cnb_ipar
	subq.w	#1,d6
	move.l	sd_xsize(a0),d1 	; get size of area
	moveq	#0,d2			; allowed origin is 0
	jsr	cn_chken(pc)		; OK?
	bne.s	cnb_exit		; no
	movem.l (a1),d1/d2		; size od block
	add.l	sd_xmin(a0),d2		; set origin offset
	move.l	sd_scrb(a0),a1		; ...base...
	move.w	sd_linel(a0),a2 	; ...and row increment
	btst	#sd..xor,sd_cattr(a0)	; xor?
	bne.l	cn_xblock
	move.b	sd_alpha(a0),d5
	cmp.b	#$ff,d5
	bne.l	cn_ablock
	bra.l	cn_fblock

cnb_exit
	rts

cnb_ipar
	moveq	#err.ipar,d0
	bra.s	cnb_exit

	end
