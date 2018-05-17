; Gold Card PAR scheduler routine  V2.03     1994  Tony Tebby

	section par

	xdef	par_sched
	xdef	par_oopr
	xdef	par_actv

	xref	iob_gbyt

	include 'dev8_keys_err'
	include 'dev8_keys_68000'
	include 'dev8_keys_k'
	include 'dev8_keys_par'
	include 'dev8_smsq_gold_keys'

reglist  reg	d0/d1/d2/a0/a2/a3
;+++
; PAR activate output
; This will do nothing if the printer is busy. But if the printer is not busy,
; then it will put a byte directly, and wait for a a ready
;
;	Status and DO as called
;---
par_actv
;+++
; PAR output operation.
; This will do nothing if the printer is busy. But if the printer is not busy,
; then it will put a byte directly, and wait for a a ready
;
;	Status and DO as called
;---
par_oopr
;+++
; PAR scheduler.
; This will do nothing if the printer is busy. But if the printer is not busy,
; then it will put a byte directly, and wait for a a ready
;
;	Status and DO as called
;---
par_sched
	tst.b	glo_prdy+glc_base	 ; ready? !!!!
	bpl.s	pars_exit		 ; ... no, done

	movem.l reglist,-(sp)
	move.l	prd_obuf(a3),a2 	 ; is there a buffer?
pint_nbf
	move.l	a2,d0
	ble.s	pint_nop		 ; ... no

	move.w	#$1000,d2		 ; total delay up to 1000 us

pint_loop
	jsr	iob_gbyt		 ; get a byte
	blt.s	pint_nop		 ; ... nothing there
	beq.s	pint_send		 ; ... something
					 ; ... end of file
	subq.b	#1,d1			 ; is <FF> or CTRL Z required?
	blt.s	pint_nbf		 ; ... no
	bgt.s	pint_cz
	moveq	#k.ff,d1	  
	bra.s	pint_send		 ; send <FF>

pint_cz
	moveq	#26,d1			 ; send CTRL Z

pint_send
	lea	glo_pstb+glc_base,a0
	move.b	d1,glo_pdat-glo_pstb(a0) ; set data

	st	(a0)			 ; strobe
	move.w	prd_puls(a3),d0
	sub.w	d0,d2			 ; take off from delay

pint_puls
	st	(a0)
	dbra	d0,pint_puls		 ; wait a bit

	tst.b	(a0)			 ; unstrobe
	tst.b	(a0)			 ; and wait
	tst.b	(a0)

	move.w	prd_wait(a3),d0 	 ; wait for ready
	beq.s	pint_done
	sub.w	d0,d2			 ; take off total time
	bmi.s	pint_done
pint_wait
	tst.b	glo_prdy-glo_pstb(a0)	 ; ready?
	dbmi	d0,pint_wait
	bpl.s	pint_done		 ; ... no

	add.w	d0,d2			 ; correct wait
	bra.s	pint_loop

pint_nop
pint_done
	movem.l (sp)+,reglist
pars_exit
	tst.l	d0
	rts

	end
