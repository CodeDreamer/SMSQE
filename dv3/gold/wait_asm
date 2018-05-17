; Gold Card Wait for Ticks    1994	  Tony Tebby

	section fd

	xdef	fd_wait

	include 'dev8_keys_sys'
	include 'dev8_keys_qlhw'

;+++ 
; Wait for ticks
;
;	d0  c  r   number if ticks / 0
;
;---
fd_wait
	move.l	d1,-(sp)
	move.w	sr,d1			 ; are interrupts disabled?
	and.w	#$0600,d1
	beq.s	fdw_mpoll		 ; ... no, count missing polls

fdw_wframe
	moveq	#pc.intrf,d1
	and.b	pc_intr,d1		 ; frame interrupt?
	beq.s	fdw_wframe		 ; ... no
	or.b	sys_qlir(a6),d1 	 ; clear interrupt flag value
	move.b	d1,pc_intr		 ; clear interrupt bit
	subq.w	#1,d0			 ; one gone
	bpl.s	fdw_wframe
	bra.s	fdw_exit

fdw_mpoll
	add.w	sys_pict(a6),d0
	bvc.s	fdw_mploop		  ; ok
	sub.w	sys_pict(a6),d0 	 ; ... bad
	sub.w	d0,sys_pict(a6) 	 ; ... backspace the counter a bit
	bra.s	fdw_mpoll

fdw_mploop
	cmp.w	sys_pict(a6),d0
	bhi.s	fdw_mploop
fdw_exit
	move.l	(sp)+,d1
	moveq	#0,d0
	rts

	end
