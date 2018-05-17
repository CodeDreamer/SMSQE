; PAR interrupt routine  V2.02	   1989  Tony Tebby   QJUMP

	section par

	xdef	par_int
	xdef	par_actv

	xref	iob_gbyt

	include 'dev8_keys_err'
	include 'dev8_keys_atari'
	include 'dev8_keys_68000'
	include 'dev8_keys_k'
	include 'dev8_keys_par'

reg.int  reg	d0/d1		     ; a2/a3 already saved
reglist  reg	d0/d1/a2/a3
;+++
; PAR activate output.
; This will do nothing if the printer is busy. But if the printer is not busy,
; then it will put a byte directly, and there will be an interrupt when the
; byte is accepted.
;
;	Status and DO as called
;---
par_actv
	btst	#mfp..prd,mfp_prd	 ; ready? !!!!
	bne.s	para_exit		 ; ... no, done

	movem.l reglist,-(sp)
	move.w	sr,-(sp)
	or.w	#sr.i7,sr		 ; no interrupts
	bsr.s	par_intr
	move.w	(sp)+,sr
	movem.l (sp)+,reglist

para_exit
	tst.l	d0
	rts

;+++
; PAR interrupt server
;---
par_int
	movem.l reg.int,-(sp)
	bsr.s	par_intr		 ; interrupt routine
	movem.l (sp)+,reglist
	rte

par_intr
	bclr	#mfp..pri,mfp_prs	 ; clear in service
	btst	#mfp..prd,mfp_prd	 ; ready? !!!!
	bne.s	pint_done		 ; ... done
	move.l	prd_obuf(a3),a2 	 ; is there a buffer?
pint_nbf
	move.l	a2,d0
	ble.s	pint_nop		 ; ... no

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
	move.w	sr,-(sp)		 ; save status
	or.w	#$0700,sr		 ; no interrupts
	move.b	#cen.dats,cen_dats	 ; select data register
	move.b	d1,cen_datw		 ; set data
	move.b	#cen.ctls,cen_ctls	 ; select control
	moveq	#$ffffffff-(1<<cen..stb),d1
	and.b	cen_ctlr,d1
	move.b	d1,cen_ctlw		 ; strobe
	move.w	prd_puls(a3),d0
pint_puls
	subq.w	#1,d0			 ; wait a bit
	bge.s	pint_puls

	bset	#cen..stb,d1
	move.b	d1,cen_ctlw		 ; unstrobe
	move.w	(sp)+,sr		 ; restore status
; ****	bra.s	pint_done

pint_nop
; ****	sf	prd_oact(a3)		 ; nop on this interrupt, not active now

pint_done
	rts
	end
