; Copy a string 			 1992 Jochen Merz

	section utility

	xdef	ut_cpyst

;+++
; Copy a string. If the destination address is zero, nothing is copied.
; If source address is zero, destination string length is set to zero.
;
;		Entry			Exit
;	a0	ptr to destination	preserved
;	a1	ptr to source		preserved
;---
cpy.reg reg	  a0/a1/d0
ut_cpyst
	movem.l cpy.reg,-(sp)
	move.l	a0,d0			; check for zero destination
	beq.s	cpy_exit		; do nothing at all
	move.l	a1,d0			; source pointer zero?
	beq.s	cpy_szer		; yes, set destination to zero
	move.w	(a1)+,d0		; otherwise get string length
cpy_szer
	move.w	d0,(a0)+		; copy length word
	bra.s	cpy_end
cpy_lp
	move.b	(a1)+,(a0)+		; copy character by character
cpy_end
	dbra	d0,cpy_lp		; do it for the whole string
cpy_exit
	movem.l (sp)+,cpy.reg
	rts

	end
