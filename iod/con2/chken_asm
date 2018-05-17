;	Check for block enclosed	V2.00   1998 Tony Tebby
;
;	Registers:
;		Entry				Exit
;	D0					0 if OK, err.orng if not
;	D1	allowed size			preserved
;	D2	allowed origin			preserved
;	A1	^ block descriptor		preserved

	section con

	include 'dev8_keys_err'

	xdef	cn_chken

cn_chken
	addq.l	#2,a1
	bsr.s	cmc_chks
	subq.l	#2,a1
	bne.s	cmc_exit
*
	swap	d1
	swap	d2
	bsr.s	cmc_chks
	swap	d2
	swap	d1
*
cmc_exit
	tst.l	d0
	rts
*
cmc_chks
	move.w	d2,d0			; origin of area
	sub.w	4(a1),d0		; less origin of block
	bgt.s	cmc_exor		; ...oops
	add.w	d1,d0			; plus size
	blt.s	cmc_exor		; is available size

	cmp.w	(a1),d0 		; compare
	blo.s	cmc_exor		; ...oops
	moveq	#0,d0
	rts
cmc_exor
	moveq	#err.orng,d0
	rts
*
	end
