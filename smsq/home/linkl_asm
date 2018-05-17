; Link new entry into HOME thing linked list (c) Marcel Kilgus, W. Lenerz 2005

; 2005-10-23  1.00  initial release version
; 2005-11-16  1.01  list ops now atomic (possible concurrent acccess) (MK)
;		    fixed bug in unlink routine (MK)


	section util

	xdef	h_link
	xdef	h_unlnk

	include dev8_keys_err
	include dev8_keys_chp
	include dev8_smsq_home_data

;---------------------------------------------------------------------
; try to link in a new job entry
;	A0 c  p pointer to link to add
;	A2 c  p pointer to thing header
;	D0  r	0 on exit
; this always succeeds
;
;---------------------------------------------------------------------
lnkreg	reg	a0/a1/d1/d7
h_link
	movem.l lnkreg,-(sp)
	move	sr,d7
	trap	#0			; atomic operation!
	move.l	hmt_frst(a2),d0 	; point to first link
	beq.s	set_first		; there is none yet!

	move.l	d0,a1			; points to former first element
	move.l	a1,hl_next(a0)		; this is the next one along the list
	move.l	a0,hl_prev(a1)		; previous element
set_first
	move.l	a0,hmt_frst(a2) 	; this is first element now
set_out
	move	d7,sr
	movem.l (sp)+,lnkreg
	moveq	#0,d0
	rts

;---------------------------------------------------------------------
; try to unlink a job entry
;	A2 c  p pointer to thing header
;	D1 c  p job ID
;---------------------------------------------------------------------

h_unlnk
	movem.l lnkreg,-(sp)
	move	sr,d7
	trap	#0			; atomic operation!
	move.l	hmt_frst(a2),d0 	; point to first entry in link list
hu_loop
	beq.s	set_out 		; (!!!!) there is none ????
	move.l	d0,a0			; first/next entry
	cmp.l	chp_ownr-chp.len(a0),d1 ; remove this entry ?
	beq.s	del_link		;  ... yes
	move.l	hl_next(a0),d0		;  ... no, try next one
	bra.s	hu_loop

; a0 points to entry to remove
; if previous = 0 , this is first one
; there is an additional difficulty since the first link is not a link in
; this sense, but a pointer to the first list element
del_link
	move.l	hl_prev(a0),d0		; pointer to previous link
	beq.s	is_frst 		; there is none (this is first entry)
	move.l	d0,a1			; point to previous element
	move.l	hl_next(a0),d1		; pointer to next link
	move.l	d1,hl_next(a1)		; in previous entry
	beq.s	set_out 		; and there is no next
	move.l	d1,a0			; a0 points to next
	move.l	a1,hl_prev(a0)		; set previous in it
	bra.s	set_out

; special case, this is the first element in the list
is_frst move.l	hl_next(a0),d1		; point to next (or none)
	move.l	d1,hmt_frst(a2) 	; set it
	beq.s	set_out
	move.l	d1,a0
	clr.l	hl_prev(a0)		; show it is first element
	bra.s	set_out

	end
