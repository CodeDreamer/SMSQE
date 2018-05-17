; Function to get Stuffer Buffer   V1.00		 2005	 Marcel Kilgus

	section hotkey

	xdef	hot_getstuff$

	xref	hot_thact
	xref	hk_getbf
	xref	ut_gtint
	xref	ut_rtsst

	include 'dev8_keys_err'
	include 'dev8_ee_hot_bv'

;+++
; Set a string in the stuffer buffer
;
; s$ = HOT_GETSTUFF$ (0 | -1)
;---
hot_getstuff$
	jsr	ut_gtint
	moveq	#0,d0
	tst.w	d3
	beq.s	hg_parok

	move.w	(a6,a1.l),d0
	addq.l	#2,a1
	move.l	a1,bv_rip(a6)
	tst.w	d0
	beq.s	hg_parok
	cmp.w	#-1,d0
	bne.s	hg_ipar
hg_parok
	lea	hk_getbf,a2
	jsr	hot_thact
	move.l	a1,a4
	move.w	d2,d1
	jmp	ut_rtsst

hg_ipar
	moveq	#err.ipar,d0
	rts

	end
