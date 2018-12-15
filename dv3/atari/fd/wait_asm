; DV3 Atari Wait for Ticks    1994	  Tony Tebby

	section fd

	xdef	fd_wait

	xref	at_pd0u

	include 'dev8_keys_sys'
	include 'dev8_keys_q40'

;+++ 
; Wait for ticks
;
;	d0  c  r   number of ticks / 0
;
;---
fd_wait
	lsl.w	#5,d0			 ; 20000/32 us units
fdw_loop
	swap	d0
	move.w	#20000/32,d0
	jsr	at_pd0u 		 ; pause
	swap	d0
	subq.w	#1,d0
	bgt.s	fdw_loop
	moveq	#0,d0
	rts
	end
