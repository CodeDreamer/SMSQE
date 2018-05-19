; Upper case character table/conversion       V2.00    1986  Tony Tebby   QJUMP
;
; Reduced code size version (MK)

	section cv

	xdef	cv_upcas

;+++
; Convert character to upper case
;
;	Registers:
;		Entry				Exit
;	D1.w	character (MSB ignored) 	upper-case equivalent
;---
cv_upcas
	andi.w	#$ff,d1
	cmpi.b	#'a',d1
	blt.s	cvu_rts
	cmpi.b	#'z',d1
	bgt.s	cvu_more
	subi.b	#32,d1
cvu_rts rts
cvu_more
	cmp.b	#$80,d1 	; Also from umlaut a...
	blt.s	cvu_rts
	cmp.b	#$8b,d1 	; ...till ae
	bgt.s	cvu_rts
	addi.b	#32,d1
	rts

	end
