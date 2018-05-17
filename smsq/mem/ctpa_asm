; Contract transient program area  V2.02    1994  Tony Tebby

	section mem

	xdef	mem_ctpa	       ; contract tpa
	xdef	mem_ctpas

	xref	mem_rlsb	       ; release slave blocks (a0 by d1)
	xref	sb_mup

	include 'dev8_keys_sys'
	include 'dev8_keys_chp'
	include 'dev8_keys_sbt'

;+++
; Contract TPA
;
;	d0  r	error return 0
;	d1 c	length to contract
;	a6 c  p system variables area base
;
;	all other registers preserved
;---
mem_ctpa
mrt.reg reg	d1/a0/a1
	movem.l mrt.reg,-(sp)		 ; save volatiles
	move.l	#-sbt.size,d0
	and.l	d0,d1

; entry with d1/a0/a1 on stack

mem_ctpas
	move.l	sys_sbab(a6),a0 	 ; start of area to release
	add.l	d1,sys_tpab(a6) 	 ; move up tpa
	add.l	d1,sys_sbab(a6) 	 ; ... and SuperBASIC compatibility
	bsr.l	mem_rlsb		 ; release slave blocks (a0 by d1)

	move.l	sys_tpab(a6),a0
	cmp.l	sys_sbab(a6),a0 	 ; SuperBASIC table area to move
	beq.s	mrt_exok		 ; ... none
	jsr	sb_mup			 ; ... up
mrt_exok
	moveq	#0,d0
mrt_exit
	movem.l (sp)+,mrt.reg
	rts

	end
