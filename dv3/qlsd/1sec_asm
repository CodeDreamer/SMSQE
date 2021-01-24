; QLSD 1 second loop timer			  1999       Tony Tebby

	section dv3

	xdef	qlsd_1sec

	include 'dev8_keys_sys'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_qdos_sms'
	include 'dev8_dv3_qlsd_keys'

;+++
; Set up 1 second loop timer
;
;	d0  r	1 second timer
;
;---
qlsd.1sec reg	d5/d6/d7/a0/a5
qlsd_1sec
	movem.l qlsd.1sec,-(sp)
	move.w	sr,d7
	moveq	#sms.info,d0
	trap	#1

	trap	#0
	ori.w	#$0700,sr		 ; disable interrupts for this

	lea	if_base+spi_read,a5	 ; address of QLSD read register
	moveq	#2,d6

sd1s_wait
	moveq	#pc.intrf,d0
	and.b	pc_intr,d0		 ; frame interrupt?
	beq.s	sd1s_wait		 ; ... no
	or.b	sys_qlir(a0),d0 	 ; clear interrupt flag value
	move.b	d0,pc_intr		 ; clear interrupt bit

	subq.w	#1,d6			 ; make sure it is genuine
	bgt.s	sd1s_wait

sd1s_count
	move.l	#4096/50,d5		 ; dummy (large) counter
	moveq	#0,d0
sd1s_loop
	and.b	(a5),d0
	bne.s	sd1s_loop		 ; never true
	subq.l	#1,d5
	bgt.s	sd1s_loop		 ; time-out loop

	addq.l	#1,d6

	moveq	#pc.intrf,d0
	and.b	pc_intr,d0		 ; frame interrupt?
	beq.s	sd1s_count		 ; ... no
	or.b	sys_qlir(a0),d0 	 ; clear interrupt flag value
	move.b	d0,pc_intr		 ; clear interrupt bit

	lsl.l	#8,d6			 ; multiply loop timer by 4096
	lsl.l	#4,d6

	move.l	d6,d0
	move.w	d7,sr
	movem.l (sp)+,qlsd.1sec
	rts

	end
