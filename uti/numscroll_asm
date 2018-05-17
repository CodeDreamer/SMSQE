; Standard action routine for numerical scroll-arrows	 1998 Jochen Merz

	section utility

	xdef	mea_numscroll

	include dev8_keys_qdos_pt
	include dev8_keys_wstatus
	include dev8_keys_wwork
	include dev8_keys_wman
	include dev8_keys_k
	include dev8_mac_xref

;+++
; This routine handles numerical scroll-arrows next to a loose menu item.
; The routine deals with the increment/decrement of bytes, words, longwords,
; updates the value (if within given ranges) and prints the new value.
; It modifies the numerical value, not the string representation (which is
; updated from the value).
;
;		Entry				Exit
;	d1	0 add / 1 sub			smashed
;	d2.w	step				smashed
;	d3.b	0 for b, + for w, - for l	smashed
;	d4	lower limit (or 0)		smashed
;	d5	upper limit (or -1)		smashed
;	d7.w	item number of value string item smashed
;	a0/a1/a2/a4 from loose menu action routine preserved
;	a3	ptr to item value (b, w, l)	smashed
;	a5	ptr to value string		smashed
;---
mea_numscroll
	tst.b	d3			; value size
	beq.s	numscr_byte
	bgt.s	numscr_word
	bmi.s	numscr_long
numscr_byte
	move.b	(a3),d6 		; get current value
	tst.b	d1			; add or subtract?
	bne.s	numbyte_sub
	add.b	d2,d6			; add step
	cmp.b	d5,d6			; limit reached?
	ble.s	numbyte_conv
	move.b	d5,d6			; use upper limit
	bra.s	numbyte_conv
numbyte_sub
	sub.b	d2,d6			; subtract step
	cmp.b	d4,d6
	bge.s	numbyte_conv
	move.b	d4,d6			; use lower limit
numbyte_conv
	move.b	d6,(a3) 		; new value
	moveq	#0,d1
	move.b	d6,d1			; put into right register
	bra.s	num_conv
numscr_long
	bra.s	draw_it
numscr_word
	move.w	(a3),d6 		; get current value
	tst.b	d1			; add or subtract?
	bne.s	numword_sub
	add.w	d2,d6			; add step
	cmp.w	d5,d6			; limit reached?
	ble.s	numword_conv
	move.w	d5,d6			; use upper limit
	bra.s	numword_conv		; convert and print
numword_sub
	sub.w	d2,d6			; subtract step
	cmp.w	d4,d6
	bge.s	numword_conv
	move.w	d4,d6			; use lower limit
numword_conv
	move.w	d6,(a3) 		; new value
	moveq	#0,d1
	move.w	d6,d1			; put into right register
num_conv
	move.l	a0,-(sp)		; save channel ID
	lea	2(a5),a0		; buffer
	xjsr	cv_d1asc		; convert
	move.l	a0,d0
	sub.l	a5,d0			; calc length of string
	subq.l	#2,d0			; minus length word
	move.w	d0,(a5) 		; insert length
	move.l	(sp)+,a0		; restore channel ID
draw_it
	move.b	#wsi.mkav,ws_litem(a1,d7.w)
	moveq	#-1,d3
	jmp	wm.ldraw(a2)

	end
