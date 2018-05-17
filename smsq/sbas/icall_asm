; SBAS_ICALL - PROC/FN Call and Unravel      1994 Tony Tebby

	section sbas

	xdef	sb_icall	    ; procedure, function call
	xdef	sb_icall_pas	    ; ditto, params (a3-a5) and addr (a0) set

	xref	sb_clrprm

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; Within this routine
;	D7 is returned as the called size of the arithmetic stack
;	A1 was pointer to arith stack
;	A3 was pointer to name table on entry
;	A6 is pointer to system variables
;---

sb_icall
	move.l	sb_retsp(a6),a2
	move.l	rt_def(a6,a2.l),a0	 ; routine address
	sub.l	a6,a3
	move.l	rt_tparm(a6,a2.l),a5	 ; top parameter
	add.l	a3,a5
	add.l	rt_parm(a6,a2.l),a3	 ; bottom parameter
	sub.w	#rt.pfsize,a2		 ; remove return frame
	move.l	a2,d2
	cmp.l	sb_retsb(a6),a2
	ble.s	sic_setr
	tst.b	rt_def(a6,a2.l) 	 ; is this frame set up?
	bmi.s	sic_setr		 ; ... yes
	tst.b	rt_type(a6,a2.l)	 ; proc fn?
	ble.s	sic_setr
	sub.w	#rt.pfsize,a2		 ; ... yes

sic_setr
	move.l	a2,sb_retsp(a6)

sb_icall_pas
	moveq	#0,d7
	move.l	sb_nmtbb(a6),d1

	sub.l	a6,a1			 ; set arithmetic stack
	move.l	a1,sb_arthp(a6)
	sub.l	sb_arthb(a6),a1 	 ; size of
;sbtrns is only required if sb vars are above data areas
;sbtrns        bclr    #7,sb_grphb(a6)		; and set to zero for Turbo
	movem.l d1/d2/d5/d6/a1/a3/a4/a5,-(sp)
	jsr	(a0)
	movem.l (sp)+,d1/d2/d5/d6/d7/a3/a4/a5
	move.l	d0,-(sp)		 ; save error code
	move.l	d2,sb_retsp(a6) 	 ; restore return frame
;sbtrns        move.l  sb_grphp(a6),sb_grphb(a6) ; reset graph base for Turbo

	sub.l	sb_nmtbb(a6),d1 	 ; name table moved
	sub.l	d1,a3			 ; adjust pointers
	sub.l	d1,a5
	cmp.l	a3,a5			 ; any parameter frame to clear?
	beq.s	sic_exit

	jsr	sb_clrprm		 ; clear frame (a3-a5)

sic_exit
	move.l	(sp)+,d0		 ; error?
	rts
	end
