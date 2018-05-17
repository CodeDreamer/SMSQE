; Procedure to set Stuffer Buffer   V2.00     1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hot_stuff

	xref	hot_thact
	xref	hk_sstbf
	xref	gu_achp0
	xref	gu_rchp
	xref	ut_gtnm1
	xref	ut_gxnm1

	include 'dev8_keys_err'
	include 'dev8_ee_hot_bv'

;+++
; Set a string in the stuffer buffer
;
; HOT_STUFF string |,string|
;---
hot_stuff
	moveq	#8,d0			 ; one parameter only?
	add.l	a3,d0
	cmp.l	d0,a5
	beq.s	hs_one			 ; ... yes
	addq.l	#8,a3
	jsr	ut_gxnm1		 ; get one string
	bne.s	hs_rts
	move.l	a1,bv_rip(a6)		 ; prepare for another
	subq.l	#8,a3			 ; other parameter
	jsr	ut_gtnm1
	bne.s	hs_rts
	move.w	(a6,a1.l),d1		 ; first string length
	lea	2(a1,d1.w),a2		 ; end of it
	move.l	a2,d2
	addq.w	#1,d2
	bclr	#0,d2			 ; start of next
	move.w	(a6,d2.l),d1		 ; length of second string
	add.w	d1,(a6,a1.l)		 ; total length
	bmi.s	hs_ipar
hs_cat
	move.b	2(a6,d2.l),(a6,a2.l)	 ; copy char
	addq.l	#1,d2
	addq.l	#1,a2
	subq.w	#1,d1
	bgt.s	hs_cat
	bra.s	hs_set

hs_one
	jsr	ut_gtnm1		 ; just one string
	bne.s	hs_rts
hs_set
	moveq	#8,d0			 ; spare
	add.w	(a6,a1.l),d0
	jsr	gu_achp0		 ; allocate heap for it
	bne.s	hs_rts

	move.w	(a6,a1.l),d0
	move.w	d0,d2			 ; length
	move.l	a0,a2
hs_copy
	move.w	2(a6,a1.l),(a2)+	 ; copy string
	addq.l	#2,a1
	subq.w	#2,d0
	bgt.s	hs_copy

	move.l	a0,a1			 ; point to string
	lea	hk_sstbf,a2		 ; set stuffer buffer
	jsr	hot_thact		 ; do it
	jmp	gu_rchp 		 ; and return heap

hs_ipar
	moveq	#err.ipar,d0
hs_rts
	rts
	end
