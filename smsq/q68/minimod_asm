; Copy a minimodule to fast memory static maemory  1.00 (c) W. Lenerz 2017

	section copy

	include 'dev8_keys_q68'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'

	xdef	cpy_mmod


********************************************************************
; This tries to copy a minimodule to fast static memory and creates a linkage
; block there.
; If it can't (because there wouldn't be enough space), it reserves some memory
; on the common heay, just for the linkage block.
;
; A minimodule is just a small piece of code that would greatly benefit the
; system if it were executed from fast memory.
; Typically, this would be an interrupt service routine.
; Fast memory is scarce (12 K between $19000 and 1BFFF), so don't squander it.
;
; ***********************
;
; WARNING minimodules must be entirely selfcontined, position independent and
;	NOT contain  PC relative code such as:
;	jsr  <routine>
;	This will have been compiled to PC relative code, but the position of
;	<routine> relative to the PC will change when copying the module,
;	unless <routine> is also copied!!!
;
; ***********************
;
;	 a minimodule looks like this:
;
; header dc.w	 start-*	; start of the routine's executable code (!)
;	 dc.w	 end-*		; end of the routine, incl any data it might need
;
; start
; ...
; end
;
; Only the section between start and end is copied to fast memory.
; If there is none, only a linkage block is created
*********************************************************************

; d0   r  0 if no error, <0 if error, >0 if routine was put into common heap
; d1	s
; A0 c r  minimodule header / linkage area with ptr to routine alreay set
; a1	s
; a2	s

cpy_mmod
	clr.l	d1
	move.w	(a0),d0 	; rel ptr to start of code
	move.w	2(a0),d1	; rel ptr to end...
;	 subq.l  #8,d1		 ; ...now
	add.w	d0,a0		; start of code
	move.l	q68_sramb,a1	; pointer to where I can store the code
	move.l	a1,a2		; keep
	sub.w	d0,d1		; nbr of bytes to copy
	blt.s	error		; negative????
	addq.l	#3,d1
	lsr.l	#2,d1		; nbr of longwords to copy
	move.l	d1,d0
	addq.l	#2,d0		; 2 more long words needed for linkage block
	lsl.l	#2,d0		; final nbr of bytes needed
	add.l	a1,d0		; this is where we would end up
	sub.l	#q68_sramt,d0	; would we overshoot?
	bge.s	over		; yes, don't copy, just get linkage block
	bra.s	do_cpy
cpy_lp	move.l	(a0)+,(a1)+
do_cpy	dbf	d1,cpy_lp
	move.l	a1,a0		; start of link
	clr.l	(a1)+		; space for next link pointer
	move.l	a2,(a1)+	; my routine
	move.l	a1,q68_sramb	; keep for next time
	clr.l	d0
	rts

error	moveq	#err.ipar,d0
	rts


; we would overshoot, just get some mem for the linkage blk
over	move.l	a0,-(a7)	; keep start of code
	moveq	#8,d1		; reserve 8 bytes
	clr.l	d2		; owner is the system
	moveq	#sms.achp,d0
	trap	#1
	move.l	(a7)+,4(a0)	; set start of code
	moveq	#1,d0		; show overshoot
	rts

	end
   
