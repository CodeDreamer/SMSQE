; Set up pointers for sprites  V0.02    1985  Tony Tebby	QJUMP
;
; 2005-11-16  0.02  Sets up pointers for CDB if a CDB is given (MK)
;		    Untested!
;
;	d0 cr	x address / column offset
;	d1 cr	y address / row offset
;	d2  r	column
;	d3  r	row
;	d5 c p	word counter
;	d6 c p	row counter
;	d7  r	right shift (low word) / 16-shift (high word)
;
;	a2 c p	old pointer to screen
;	a3 cr	linkage / new pointer to screen
;	a6  r	address increment of screen: line \ line-sprite
;
	section driver
;
	xdef	sp_ptr
	xdef	spm_ptr
spm_ptr
	include 'dev8_keys_con'
;
sp_ptr
	move.l	a3,a6
	move.l	pt_sbase(a6),a3 ; set base address of screen
	move.l	a0,d2		; any CDB given?
	beq.s	sp_ptrscr	; no, target must be screen
	cmp.l	sd_scrb(a0),a3	; is it really?
	bne.s	sp_ptrwin	; no! Use data from CDB
sp_ptrscr
	ext.l	d0
	ror.l	#4,d0		; set address in 16 pixel units
	lsl.w	#1,d0		$$$$
	add.w	d0,a3		; and add to screen base
*
	clr.w	d0		; recover 16 pixel offset
	rol.l	#4,d0
	move.w	d0,d7		; into d7
	swap d7
	neg.w	d0		; ... and 16-offset in top end
	add.w	#$10,d0
	move.w	d0,d7
	swap	d7
*
	mulu	pt_bytel(a6),d1 ; now add y position
	add.l	d1,a3
*
	move.l	a2,d0		; set d0,d1 to column,row of old address
	sub.l	pt_sbase(a6),d0
	divu	pt_bytel(a6),d0
	move.w	d0,d1
	swap	d0
	lsr.w	#1,d0		$$$$$
	move.l	a3,d2		; take away column,row of new address
	sub.l	pt_sbase(a6),d2
	divu	pt_bytel(a6),d2
	sub.w	d2,d1
	move.w	d2,d3
	swap	d2
	lsr.w	#1,d2		$$$$$
	sub.w	d2,d0
	move.w	d0,-(sp)

	move.w	d5,d0		; nr of words wide
	addq.w	#1,d0		: +1
	lsl.w	#1,d0		; *2 (bytes)	$$$$$
	move.w	d0,-(sp)
	move.l	pt_bytel(a6),d0
	move.w	pt_bytel(a6),d0
	move.l	d0,a6
	sub.w	(sp)+,a6	; screen increments

	move.w	(sp)+,d0
	rts

sp_ptrwin
	move.l	sd_scrb(a0),a3	; "screen" base in CDB
	ext.l	d0
	ror.l	#4,d0		; set address in 16 pixel units
	lsl.w	#1,d0		$$$$
	add.w	d0,a3		; and add to screen base
;
	clr.w	d0		; recover 16 pixel offset
	rol.l	#4,d0
	move.w	d0,d7		; into d7
	swap d7
	neg.w	d0		; ... and 16-offset in top end
	add.w	#$10,d0
	move.w	d0,d7
	swap	d7
;
	mulu	sd_linel(a0),d1 ; now add y position
	add.l	d1,a3
;
	move.w	d5,d0		; nr of long words wide
	addq.w	#1,d0		; +1
	lsl.w	#1,d0		; *2 (bytes)	$$$$$
	move.w	d0,-(sp)
	move.l	sd_linel(a0),d0
	move.w	sd_linel(a0),d0
	move.l	d0,a6
	sub.w	(sp)+,a6	; screen increments

	move.w	#$7fff,d0	; return ridiculously large offset
	move.w	#$7fff,d1	; return ridiculously large offset
	rts

	end
