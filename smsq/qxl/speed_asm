; QXL Processor speed check	 1995	     Tony Tebby   QJUMP

	section init

	xdef	qxl_speed

	include 'dev8_smsq_qxl_keys'

screen equ	$20000

;+++
; determine processor speed
;
;---
qxl_speed
	lea	qxs_code,a0
	lea	screen,a1		 ; copy to screen for consistant timing
	moveq	#(qxs_rts-qxs_code)/4,d0

qxs_copy
	move.l	(a0)+,(a1)+
	dbra	d0,qxs_copy

	jmp	screen

qxs_code
 
;+++++++		     ; line
	lea	qxl_clock,a0
	move.w	(a0),d2 		 ; clock rate, interrupt counter
;-------		     ; half line
	bgt.s	qxs_rts 		 ; already set
	subq.w	#1,(a0) 		 ; start counting down
	move.w	#-100,d2		 ; and wait a bit for things to settle
	moveq	#0,d1			 ; count
	moveq	#0,d1			 ; count
;+++++++
qxs_wait_start
	clr.w	qxl_mtick_count 	 ; prevent poll
	cmp.w	(a0),d2
	beq.s	qxs_wait_start		 ; wait for start
	sub.w	#100,d2 		 ; count for 100 interrupts
;------
qxs_wait_1
	addq.l	#1,d1
	clr.w	qxl_mtick_count 	 ; prevent poll
	cmp.w	(a0),d2
	blo.s	qxs_wait_1

 move.l d1,$300
	add.l	#3204/2,d1		 ; round
	divu	#3204,d1		 ; clock speed
	move.w	d1,qxl_clock

qxs_rts
	rts
	end
