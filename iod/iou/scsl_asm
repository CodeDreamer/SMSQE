; IO Utilities Scan Slave Blocks  V1.00    1988   Tony Tebby QJUMP

	section iou

	xdef	iou_scsl

	include 'dev8_keys_sys'
	include 'dev8_keys_sbt'
;+++
; IO Utilities scan slave blocks between bottom and top of free mamory
;
;	d0 cr	drive ID / status byte (less ID + file bit) passed to action
;	a1    p passed as pointer to slave block table
;	a2 c  p pointer to action routine / passed as pointer to slave block
;	all other registers except d7/a0 passed
;
;	error status as returned from action routine
;---
iou_scsl
ios.reg  reg	d7/a0/a1/a2
ios_act  equ	$0c
ios.wrk  reg	a0/a1/a2
ios.wrkl equ	$0c

	movem.l ios.reg,-(sp)
	move.b	d0,d7
	lsl.b	#4,d7			 ; drive id in msnibble of lsbyte
	bset	#sbt..fsb,d7		 ; filing system

	move.l	sys_sbtb(a6),a1 	 ; base of slave block table
	move.l	sys_fsbb(a6),a2 	 ; first fssb
	move.l	a2,d0
	sub.l	a6,d0
	lsr.l	#sbt.shft,d0
	add.l	d0,a1			 ; first vacant sbt entry

	move.l	sys_sbab(a6),a0 	 ; top of slave block area



ios_loop
	moveq	#sbt.driv,d0		 ; mask of drive bits
	and.b	sbt_stat(a1),d0
	cmp.b	d0,d7			 ; right drive?
	bne.s	ios_next		 ; ... no
	moveq	#sbt.inus,d0
	and.b	sbt_stat(a1),d0 	 ; set status
	beq.s	ios_next
	movem.l ios.wrk,-(sp)		 ; save work regs 
	pea	ios_rest
	move.l	ios_act+ios.wrkl+4(sp),-(sp) ; call action routine
	rts
ios_rest
	movem.l (sp)+,ios.wrk		 ; restore work regs
	bne.s	ios_exit
ios_next
	addq.l	#8,a1			 ; next slave block
	add.w	#512,a2
	cmp.l	a0,a2			 ; at top?
	blo.s	ios_loop		 ; ... not yet

	moveq	#0,d0

ios_exit
	movem.l (sp)+,ios.reg
	rts
	end
