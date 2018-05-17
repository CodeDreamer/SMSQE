; Simple choice box			 1991 Jochen Merz   V001
;		    ... with default item


	section utility


	include dev8_keys_thg
	include  dev8_mac_xref
	include  dev8_keys_menu

	xdef	mu_selct   ; the old name
	xdef	mu_itsl    ;  and the new

	xdef	mu_ditsl   ; ... with default item
	
;+++
; Select one out of two or three choices.
;		Entry			Exit
;	D0.l				error, 0 or +1 for ESC
;	D1.l	origin or -1		preserved
;	D2.b	colourways		preserved
;	D3.l	number of default item	option selected
;	D5.l	ptr to 1st choice	smashed
;	D6.l	ptr to 2nd choice	smashed
;	D7.l	ptr to 3rd choice or 0	smashed
;	A0	window title		preserved
;	A2	window message		preserved
;---
stk.frm equ	$50
slc_reg reg	d4/d6/a1-a5
mu_selct
mu_itsl
	moveq	#1,d3			; item number 1
mu_ditsl
	movem.l slc_reg,-(sp)
	sub.l	#stk.frm,sp
	move.l	sp,a5			; prepare workspace

	subq.w	#1,d3			; item 0 to
	bmi	slc_error		; error

	cmp.w	#2,d3			; too much
	bhi	slc_error		; error

	lea	$44(a5),a1		; my buffer
	move.l	d5,(a1)+		; fill in first item
	move.l	d6,(a1)+		; second
	move.l	d7,(a1) 		; ...and third item

	moveq	#2,d4			; default 2 items

	tst.l	d7			; three items
	beq.s	no_three		; no

	moveq	#3,d4			; three items

no_three
	movem.l d1-d3,-(sp)
	move.l	#'ITSL',d2
	xbsr	ut_usmen
	movem.l (sp)+,d1-d3
	bne	slc_rts 		; cannot use menu

	move.l	a1,a4			; that's the Thing

	move.l	#(thp.call+thp.str)<<16,d5

	move.l	d5,is_mentp(a5)
	move.l	a0,is_mennm(a5) 	; window title

	move.l	d5,is_prmtp(a5)
	move.l	a2,is_prmpt(a5) 	; request string

	cmp.b	#2,d4			; two items
	bhi.s	three_items		; no

	lea	table2_bas,a2		; base of two items
	bra.s	all_cont

three_items
	lea	table3_bas,a2		; base of three items
all_cont
	add.w	d3,d3
	add.w	d3,d3			; get into offset
	add.l	(a2,d3.w),a2		; a2 table

	move.w	(a2),d6 		; first offset
	lea	$44(a5),a3		; base of messages

	move.l	d5,is_item1-4(a5)
	move.l	(a3,d6.w),is_item1(a5)	; option 1

	move.l	d5,is_item2-4(a5)
	move.w	$4(a2),d6		; second offset
	move.l	(a3,d6.w),is_item2(a5)	; option 2

	move.l	d5,is_item3-4(a5)
	move.w	$8(a2),d6		; third offset
	move.l	(a3,d6.w),is_item3(a5)	; option 3

	swap	d1
	move.l	d1,is_xpos(a5)		; x-origin

	swap	d1
	move.l	d1,is_ypos(a5)		; y-origin

	and.l	#$ff,d2 		; only low byte
	move.l	d2,is_mainc(a5) 	; main colourway

	move.l	#(thp.ret+thp.ulng)<<16,is_itnum-4(a5)
	lea	$40(a5),a3
	move.l	a3,is_itnum(a5) 	; return parameter

	move.l	a5,a1			; the parameter table
	jsr	thh_code(a4)		; do request via menu
	bne.s	err_rts 		; failed, do not read return parameter

	move.l	$40(a5),d3		; return parameter
	subq.w	#1,d3			; because offset

	add.w	d3,d3
	add.w	d3,d3			; get into offset
	move.w	$2(a2,d3.w),d3		; change into real item

err_rts
	xbsr	ut_frmen		; free menu
	bra.s	slc_rts

slc_error
	moveq	#-1,d0			; error

slc_rts
	add.l	#stk.frm,sp		; adjust stack
	movem.l (sp)+,slc_reg
	tst.l	d0
	rts


table3_bas
	dc.l   table3_one-table3_bas
	dc.l   table3_two-table3_bas
	dc.l   table3_three-table3_bas
table3_one
	dc.w	0,1
	dc.w	4,2
	dc.w	8,3

table3_two
	dc.w	4,2
	dc.w	0,1
	dc.w	8,3

table3_three
	dc.w	8,3
	dc.w	0,1
	dc.w	4,2

table2_bas
	dc.l	table2_one-table2_bas
	dc.l	table2_two-table2_bas
table2_one
	dc.w	0,1
	dc.w	4,2
	dc.w	8,0
table2_two
	dc.w	4,2
	dc.w	0,1
	dc.w	8,0


	end
