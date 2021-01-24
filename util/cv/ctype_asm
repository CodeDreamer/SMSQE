* Character Type conversion	   V2.01    1986  Tony Tebby  QJUMP
*					 Apr 1988  J.R.Oakley  QJUMP
*
* 2020-08-24  2.01  Moved table to own file to save space when code not needed (MK)

	section cv
*
	xdef	cv_ctype
	xref	cv_cttab
*
	include dev8_keys_k
*+++
* Convert a character into its type. The type returned will be one of:
*
*	Type		code
*	cursor		-2
*	non-printing	 0
*	digit (0..9)	 2
*	lowercase	 4
*	uppercase	 6
*	other		 8
*
*	Registers:
*		Entry				Exit
*	D1.w	character (MSB ignored) 	type
*---
cv_ctype
	and.w	#$00ff,d1
	move.b	cv_cttab(pc,d1.w),d1
	ext.w	d1			; might be negative
	rts
*
	end
