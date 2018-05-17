; DV3 Gold Card sets up 1 second loop timer     1999	    Tony Tebby

	section fd

	xdef	fd_1sec

	xref.l	fdc_stat

	include 'dev8_keys_sys'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Set up 1 second loop timer
;
;	d0  r	1 second timer
;
;---
fd_1sec
hd_1sec
	moveq	#sms.xtop,d0
	trap	#1

	movem.l d5/d6,-(sp)

	move	#$2700,sr		 ; disable interrupts for this

	jsr	sms.cenab		 ; enable both caches

	lea	fdc_stat,a5		 ; address of status register
	moveq	#2,d6

fd1s_wait
	moveq	#pc.intrf,d0
	and.b	pc_intr,d0		 ; frame interrupt?
	beq.s	fd1s_wait		 ; ... no
	or.b	sys_qlir(a6),d0 	 ; clear interrupt flag value
	move.b	d0,pc_intr		 ; clear interrupt bit

	subq.w	#1,d6			 ; make sure it is genuine
	bgt.s	fd1s_wait

fd1s_count
	move.l	#4096/50,d5		 ; dummy (large) counter
	moveq	#0,d0
fd1s_loop
	and.b	(a5),d0
	bne.s	fd1s_loop		 ; never true
	subq.l	#1,d5
	bgt.s	fd1s_loop		 ; time-out loop

	addq.l	#1,d6

	moveq	#pc.intrf,d0
	and.b	pc_intr,d0		 ; frame interrupt?
	beq.s	fd1s_count		 ; ... no
	or.b	sys_qlir(a6),d0 	 ; clear interrupt flag value
	move.b	d0,pc_intr		 ; clear interrupt bit

	lsl.l	#8,d6			 ; multiply loop timer by 4096
	lsl.l	#4,d6

	jsr	sms.cdisb		 ; disable caches again

	move.l	d6,d0
	movem.l (sp)+,d5/d6
	rts

	end
