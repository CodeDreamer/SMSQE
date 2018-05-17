; Make a simple hash value from a string	V1.00   2015 W. Lenerz

	section hash

	include dev8_keys_recent_thing

	xdef	hash
	xdef	hash2
	xdef	rcnt_hash$

	xref	ut_gxst1
	xref	ut_rtfd1

; make a simple hash value from a string
; the only requirements are speed and not too many collisions

; d0	s
; d1  r 	hash
; d2	s
; d3	s
; a3 c	s	points to string

hash	move.l	#rc_inihash,d1		; 5381
hash2	move.w	(a3),d0 		; length of string
	beq.s	out			; none?!
	add.w	#1,d0			; +length word - dbf
	clr.l	d2
iloop	move.b	(a3)+,d2
	andi.b	#$df,d2 		; make case insensitive
	move.l	d1,d3			; next char
	lsl.l	#5,d1
	add.l	d3,d1			; hash * 33
	add.l	d2,d1			; + char
	dbf	d0,iloop		; the length word itself is part of the hash
out	rts


; sbasic function
rcnt_hash$
	jsr	ut_gxst1		; get single string
	bne.s	out			; karang
	lea	(a1,a6.l),a3		; string to set
	bsr.s	hash
inf_out jmp	ut_rtfd1


	end
