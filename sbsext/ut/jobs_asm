*  Create and remove Jobs  V0.0    Tony Tebby	 QJUMP
*
	section utils
*
	xdef	ut_crjob		create a resident Job
	xdef	ut_crjd1
	xdef	ut_ajob 		activate job (d5)
	xdef	ut_jober		suicide with error message
	xdef	ut_rjbme		remove me
	xdef	ut_rjob 		remove job (d5)
*
	include dev8_sbsext_ext_keys
*
*	d5 cr	job ID
*	a0  r	$c on from call value of a4
*	a4 cr	pointer to Job definition / pointer to 7 char of name of Job
*		Job definition
*				word	data space
*				word	pointer to start address
*				string	job name (assumed 6 chacacters)
*
ut_crjob
	moveq	#0,d1			independent
ut_crjd1
	moveq	#mt.cjob,d0		create job
	moveq	#$10,d2 		program size (6 jmp.l, 2 4AFB, 8 name)
	moveq	#0,d3
	move.w	(a4)+,d3		data space
	sub.l	a1,a1			start address
	bsr.s	ut_trap1
	bne.s	utj_rts 		... no
*
	move.w	#$4ef9,(a0)+		JMP.L
	move.l	a4,a1
	add.w	(a4)+,a1		start address
	move.l	a1,(a0)+
*
	move.w	#$4afb,(a0)+		standard job header
	move.l	(a4)+,(a0)+		name length and two characters
	move.l	(a4)+,(a0)+		... and another two characters
*
	exg	a0,a4			set return values
	move.l	d1,d5
	bra.s	utj_test
*
ut_ajob
	moveq	#mt.activ,d0		activate
	move.l	d5,d1			Job (d5)
	moveq	#0,d3			independent
ut_trap1
	trap	#1			priority set in D2
utj_test
	tst.l	d0
utj_rts
	rts
*
ut_jober
	jsr	ut..err0*3+qlv.off	write error message
ut_rjbme
	moveq	#myself,d5		suicide
ut_rjob
	move.l	d5,d1			set job to kill
	move.l	d0,d3			... error return
	move.l	d0,d4			(saved)
	moveq	#mt.frjob,d0
	trap	#1
	move.l	d4,d0			restore error return
	rts
	end
