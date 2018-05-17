; Check extension utility
	section utility

	xdef	fu_chkext

;+++
; Check if file ends in one of a list of given extensions
; The extension list should be right-justified, 3 upper-cased character
; longwords and ends with -1.
;
;		Entry			Exit
;	d1.l				0 (not in list) or position in list
;	d2.b				0 for QDOS (_xxx), 1 for DOS (.xxx)
;	a1	ptr to filename 	preserved
;	a3	ptr to list
;---
fu_chkext
	movem.l d3/a1,-(sp)

	move.w	(a1)+,d0		; filename length
	cmp.w	#4,d0			; extension possible at all?
	ble.s	err_ret
	moveq	#'_',d2 		; underscore before extn?
	sub.b	-4(a1,d0.w),d2		; yapp!
	beq.s	is_underscore
	sub.w	#48,d2			; difference to "."?
	cmp.w	#1,d2			; ... should leave 1
	bne.s	err_ret
is_underscore
	moveq	#0,d3			; prepare work register
	move.b	-3(a1,d0.w),d3
	rol.l	#8,d3
	move.b	-2(a1,d0.w),d3
	rol.l	#8,d3
	move.b	-1(a1,d0.w),d3		; this is 0xxx
	and.l	#$00dfdfdf,d3		; upper-case it

loop
	addq.l	#1,d1
	tst.l	(a3)			; end of list?
	bmi.s	err_ret
	cmp.l	(a3)+,d3		; same?
	beq.s	return
	bra.s	loop
err_ret
	moveq	#0,d1			; not in list
return
	movem.l (sp)+,d3/a1
	tst.b	d1			; set condition codes
	rts

	end
