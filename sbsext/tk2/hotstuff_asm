; Stuff HOTKEY buffer			 1991 Jochen Merz   V0.01
;
; 2018-01-16  1.00  Compact(er) version for TK2 ROM use (MK)

	section utility

	include dev8_keys_hk_vector
	include dev8_keys_qdos_sms
	include dev8_keys_thg
	include dev8_keys_err
	include dev8_keys_sys

	xdef	ut_stufc

;+++
; Stuff some characters into the HOTKEY buffer.
;
;		Entry			Exit
;	D0				error
;	D2	number of characters	smashed
;	A1	ptr to characters
;---
ut_stufc
	movem.l d1-d2/d4/d7/a0-a5,-(sp)
	move.l	a1,a5		    ; save chars
	move.w	d2,d4		    ; save number of chars

	bsr.s	ut_thvec	    ; get THING vector
	bne.s	stf_err

	lea	hk_thing,a0	    ; Hotkey Extension
	moveq	#127,d3 	    ; timeout
	moveq	#sms.myjb,d1	    ; that's the current job
	moveq	#sms.uthg,d0	    ; use Thing
	jsr	(a4)		    ; do it

	move.l	a1,a3		    ; the Hotkey linkage must be in A3
	move.l	a5,a1		    ; restore chars
	bne.s	stf_err

	move.w	d4,d2		    ; restore number of chars
	move.l	hk.stbuf(a3),a2
	jsr	(a2)

	moveq	#sms.myjb,d1	    ; that's the current job
	moveq	#sms.fthg,d0	    ; free Thing
	jsr	(a4)		    ; do it
stf_err
	movem.l (sp)+,d1-d2/d4/d7/a0-a5
	rts

hk_thing dc.w	6,'Hotkey'

;+++
; Find Thing utilitiy vector of HOTKEY System II.
; Note this only works if a HOTKEY System version 2.03 or later is present.
;
;		Entry				Exit
;	a4					Thing Utility Vector
;
;	Error returns:	err.nimp		THING does not exist
;	Condition codes set
;---
ut_thvec
	moveq	#sms.info,d0		; get system variables
	trap	#do.sms2
	move.w	sr,d7			; save current sr
	trap	#0			; into supervisor mode
	move.l	sys_thgl(a0),d1 	; this is the Thing list
	beq.s	thvec_nf		; empty list, very bad!
	move.l	d1,a0
thvec_lp
	move.l	(a0),d1 		; get next list entry
	beq.s	th_found		; end of list? Here should be THING!
	move.l	d1,a0			; next link
	bra	thvec_lp
thvec_nf
	moveq	#err.nimp,d0		; THING does not exist
	bra.s	thvec_rt
th_found
	move.l	th_thing(a0),a0 	; get start of Thing
	cmp.l	#-1,thh_type(a0)	; is it our special THING?
	bne.s	thvec_nf		; sorry, it isn't
	move.l	8(a0),a4		; this is the vector we look for
thvec_rt
	move.w	d7,sr
	tst.l	d0
	rts

	end
