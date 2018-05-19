; Q68 normalise names : make SMSQE name into 8.3 name
;
; v. 1.01 removed reference to keys_q68
; v. 1.00 copyright (C) W. Lenerz 2016
;
	section dv3

	xdef	 norm_nm

	xref	 cv_upcas

	include 'dev8_keys_err'


;--------------
; Normalize a name to FAT32 8.3 format : copy and normalize an smsqe name from
; (a0) to (a1). Normalizing a name means that all unused parts of the name are
; filled with spaces. If a name can't be normalized (too long, or wrong format),
; set entire 11 bytes +1 additional byte at the end of 8.3 name to 0.
;
; d0  r    0 or error
; d1	s
; d3	s
; d4	s
; a0 c	s  pointer to smsqe name
; a1 c	s  pointer to space for 8.3 name
;
; status return (0=OK)
;	err.itnf	0 length smsqe name passed
;	err.ipar	smsqe name was too long, either entire name or any name part
;
norm_nm move.l	a1,d4		; keep if error
	move.w	(a0)+,d0	; name length
	beq.s	notfnd		; none
	cmp.w	#12,d0		; max name length
	bgt.s	ipar		; ooops
	subq.w	#1,d0		; prepare for dfb
	moveq	#8,d3		; max length of name part
cpynm	move.b	(a0)+,d1	; char to copy
	cmp.b	#'.',d1 	; is it extension marker?
	beq.s	extn		; yes, deal with it
noextn	subq.w	#1,d3		; one less char to be copied
	bmi.s	ipar		; got more than all allowed chars
put_in
	bsr	cv_upcas	; upper case
	move.b	d1,(a1)+	; fill in char
docp1	dbf	d0,cpynm	; go around as long as there are chars or extension reached
	bsr.s	do_fill 	; possible fill remainder with spaces

	tst.w	d0		; anything left?
	bge.s	nmfill		; yes
	moveq	#3,d3		; if no extension present
	bra.s	dfp

nmfill	moveq	#3,d3
cpyext	move.b	(a0)+,d1	; char to copy
	subq.w	#1,d3		; one less char to be copied
	bmi.s	ipar		; got more than all allowed chars
	bsr	cv_upcas	; upper case
	move.b	d1,(a1)+	; fill in char
docp2	dbf	d0,cpyext	; go around as long as there are chars
dfp	bsr.s	do_fill 	; possibly fill chars
	clr.b	(a1)		; last byte is 0
	moveq	#0,d0
	rts
	
; here we hit the extension marker
extn
	bsr.s	do_fill 	; fill any remaining name part with spaces
	subq.w	#1,d0		; extensions means one character less to copy
	bge.s	nmfill		; deal with extension
; special case if name ends on '.'
	moveq	#3,d3
	bra.s	dfp

; fill with d3+1 spaces
fill_lp move.b	#' ',(a1)+
do_fill dbf	d3,fill_lp
	rts


; error returns
; in case of error (a1) is zeroed over 3 long words

notfnd	moveq	#err.itnf,d0
	bra.s	empty
; name was wrong
ipar	moveq	#err.ipar,d0
empty	move.l	d4,a1
	clr.l	(a1)+
	clr.l	(a1)+
	clr.l	(a1)+
	tst.l	d0
	rts

	end
