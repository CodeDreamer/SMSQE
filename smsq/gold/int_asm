; QL Interrupt server

	section qd

	xdef	qd_int2

	xref	ser_tx

	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_iod'
	include 'dev8_smsq_smsq_base_keys'

;+++
; QL Interrupt Server
;---
qd_int2

; set primary stack frame and locate system variable base

	movem.l psf.reg,-(sp)		 ; save main working registers
	move.l	sms.sysb,a6		 ; system variable base

	moveq	#%11111,d7		 ; look at interrupt bits only
	and.b	pc_intr,d7		 ; interrupt register
	ror.w	#1,d7			 ; gap in carry
	bcs.s	qdi_gdum
	ror.w	#2,d7			 ; transmit in carry
	bcs.s	qdi_stx
	ror.w	#1,d7			 ; frame in carry
	bcs.s	shd_poll
	tst.b	d7
	bne.s	qdi_ext
	rol.w	#4,d7
qdi_clri
	or.b	sys_qlir(a6),d7
	move.b	d7,pc_intr		 ; clear offending interrupt
qdi_exit
	movem.l (sp)+,psf.reg
	rte

shd_poll
	addq.w	#1,sys_pict(a6) 	 ; one more poll

	move.l	sms.spoll,a5
	jmp	(a5)			 ; polling interrupt

qdi_ext
	movem.l d0-d6/a0-a5,-(sp)
	lea	sys_exil(a6),a0 	 ; list of ext int actions

qdie_sloop
	move.l	(a0),d0
	beq.s	qdie_done		 ; ... done
	move.l	d0,a0
	lea	-iod_xilk(a0),a3	 ; base of linkage
	move.l	iod_xiad(a3),a4 	 ; address of polling routine
	move.l	a0,-(sp)
	jsr	(a4)			 ; do routine
	move.l	(sp)+,a0		 ; restore
	bra.s	qdie_sloop

qdie_done
	movem.l (sp)+,d0-d6/a0-a5
	moveq	#pc.intre,d7
	bra.s	qdi_clri


qdi_gdum
	moveq	#pc.intrg,d7		 ; clear this interrupt
	bra.s	qdi_clri
qdi_stx
	movem.l d0/d1/d3/d4/a2/a3,-(sp)
	jsr	ser_tx
	movem.l (sp)+,d0/d1/d3/d4/a2/a3
	moveq	#pc.intrt,d7		 ; clear this interrupt
	bra.s	qdi_clri

	end
