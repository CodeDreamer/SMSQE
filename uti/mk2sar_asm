; Make a 2-dimensional string array		1992 Jochen Merz

	section utility

	include dev8_keys_err

	xdef	ut_mk2sa

	xref	ut_alchp
	xref	ut_rechp

;+++
; Creating a 2-dimensional string array is a two-stage process. The first run
; counts the number of items and finds out the longest string. In case both
; values are known, the first run may be left out.
; Space for the array is allocated, the descriptor block is generated and the
; second run is started which should fill in the string in the relevant space.
;
;		Entry				Exit
;	d1.w	number of entries-1 (or -1)	 number of entries-1
;	d2.w	maximum entry length (if d1<>0) ???
;	d3	value passed to routines	value returned from routine
;	d4+	preserved			preserved
;	a0					ptr to array descr. or 0
;	a1	pointer to count/copy routine	???
;	a2	value passed to routine 	value returned from routine
;	a3	value passed to routine 	value returned from routine
;	a4+	preserved			preserved
;
; The first run should count the number of array elements.
; On success, d0 should return 0. If no other item is found, err.eof should
; be returned and this run is left. Any other error aborts array creation.
;
;	d0.l	-1				0, err.eof or other error
;	d1.w	entry number			preserved
;	d2.w					length of this entry
;	d3	value passed to routine 	value returned from routine
;	a2	value passed to routine 	value returned from routine
;	a3	value passed to routine 	value returned from routine
;
; The second run should fill in the strings. d3/a2/a3 have the same initial
; values as in the first run. Error returns as above.
;
;	d0.l	0				0, err.eof or other error
;	d1.w	entry number			preserved
;	d2.w	max space for this entry	preserved
;	d3	value passed to routine 	value returned from routine
;	a0	space for this entry		preserved
;	a2	value passed to routine 	value returned from routine
;	a3	value passed to routine 	value returned from routine
;
;---
ut_mk2sa
mk2reg	reg	d4-d5/a4
	movem.l mk2reg,-(sp)
	tst.w	d1			; first pass necessary?
	bpl.s	mk2_achp		; no, so allocate memory
	moveq	#0,d1			; first entry is 0
	moveq	#0,d4			; reset maximum length count
	movem.l d3/a2-a3,-(sp)		; keep supplied registers for 2nd pass
mk2_count
	moveq	#-1,d0			; signal 'counting'
	jsr	(a1)
	tst.l	d0			; success?
	beq.s	mk2_cntok
	cmp.l	#err.eof,d0		; no further item?
	bne.s	mk2_cnter		; serious error, abort
	movem.l (sp)+,d3/a2-a3		; get back regs for 2nd pass
	subq.w	#1,d1			; this item gave EOF
	move.w	d4,d2			; maximum entry space
	bra.s	mk2_achp		; now allocate memory
mk2_cntok
	addq.w	#1,d1			; next entry
	cmp.w	d2,d4			; result higher than current max?
	bhi.s	mk2_count		; no, next one
	move.w	d2,d4			; set new max
	bra.s	mk2_count

mk2_achp
	moveq	#0,d0
	tst.w	d1			; do we have minimum of 1 item?
	bmi.s	mk2_err 		; no, so error
	moveq	#3,d0			; string count plus odd byte
	add.w	d2,d0			; plus max length
	bclr	#0,d0			; make even
	move.w	d0,d5
	move.w	d1,d4
	addq.w	#1,d1			; absolute number of entries
	mulu	d1,d0			; table size
	moveq	#14,d1			; descriptor size
	add.l	d0,d1			; plus table size
	bsr	ut_alchp		; request memory
	bne.s	mk2_err 		; failed, return error

	move.l	a0,a4			; keep pointer to descriptor
	moveq	#14,d0
	add.l	a0,d0			; get pointer to table
	move.l	d0,(a0)+		; here are the contents
	move.w	#2,(a0)+		; 2 dimensions
	move.w	d4,(a0)+		; that many items
	move.w	d5,(a0)+		; .. of this size
	subq.w	#2,d5
	move.w	d5,(a0)+		; string length for 2nd dim
	move.w	#1,(a0)+		; string is 1 byte per char

	moveq	#0,d1			; reset entry number
	move.w	d5,d2			; max space per entry
	addq.w	#2,d5			; entry multiplier
mk2_copy
	moveq	#0,d0			; signal 'copy now'
	jsr	(a1)			; call routine
	tst.l	d0
	bne.s	mk2_rchp		; fatal error, return memory
	add.w	d5,a0			; point to next entry
	addq.w	#1,d1			; update entry counter
	dbra	d4,mk2_copy		; loop until done
	move.l	a4,a0			; that's the final array
	bra.s	mk2_ret 		; return without error

mk2_rchp
	move.l	a4,a0
	bsr	ut_rechp		; release memory
	bra.s	mk2_err 		; and return error

mk2_cnter
	add.w	#$0c,sp 		; remove regs for 2nd pass

mk2_err
	sub.l	a0,a0			; no array!
mk2_ret
	movem.l (sp)+,mk2reg
	tst.l	d0			; lead through error
	rts

	end
