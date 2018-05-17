; Find a job in my linked list for HOME thing  (c) 2005 W. Lenerz, Marcel Kilgus

; 2005-10-23  0.00  initial release version (wl)
; 2005-11-16  0.01  list operations now atomic (possible concurrent access) (mk)

	section util

	xdef	h_fndjb

	include dev8_keys_err
	include dev8_keys_chp
	include dev8_smsq_home_data

;---------------------------------------------------------------------
; try to find our job in the list
;	A0 cr u pointer to first link/pointer to link if found
;	A2 c  p pointer to thing header
;	D0    r on exit error
;	D1 c  p job ID of job to be found
;
; returns	Z if if found in list, D0 undetermined
;		NZ (D0 = err.itnf) if not found in list
;
;---------------------------------------------------------------------

h_fndjb
	move.l	d7,-(sp)
	move	sr,d7
	trap	#0			; atomic operation!

	lea	hmt_frst(a2),a0 	; point to first
h_srch
	move.l	hl_next(a0),d0		; try to point to first/next link
	beq.s	h_nf			; there is none, job not found
	move.l	d0,a0			; point to this next one
	cmp.l	chp_ownr-chp.len(a0),d1 ; same ID ?
	bne.s	h_srch			;  ... no, try again
	clr.l	d0			; no error
h_exit
	move	d7,sr
	move.l	(sp)+,d7
	tst.l	d0
	rts

h_nf	moveq	#err.itnf,d0
	bra.s	h_exit

	end
