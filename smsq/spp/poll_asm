; SER polling routine  V2.10	    1999 Tony Tebby
; 2005-10-10	2.11	direct suppression if not still counting (MK)


	section spp

	xdef	spp_poll

	include 'dev8_keys_buf'
	include 'dev8_keys_serparprt'

;+++
; SER polling routine
; This checks all the ser ports for a carrier detect timout.
; If there is one, it checks it.
;---
spp_poll
	move.w	spd_nser(a3),d7 	 ; number of ser ports
	move.l	spd_pser(a3),a3 	 ; ser linkages
	bra.s	sppp_eloop
sppp_loop
	move.w	spd_cdef(a3),d1 	 ; timeout?
	beq.s	sppp_next
	move.l	spd_ibuf(a3),d0 	 ; buffer?
	beq.s	sppp_next		 ; ... no
	move.l	d0,a2

	move.l	spd_cdchk(a3),a4
	jsr	(a4)			 ; check carrier detect
	bne.s	sppp_restart		 ; ... carrier

	subq.w	#1,spd_cdct(a3) 	 ; ... no carrier, countdown
	bgt.s	sppp_next		 ; still counting
	blt.s	sppp_supp1		 ; ... suppress it
	tas	buf_eoff(a2)		 ; ... timed out, set eof

sppp_supp1
	moveq	#0,d1			 ; no timeout now

sppp_restart
	move.w	d1,spd_cdct(a3) 	 ; carrier - reset timer

sppp_next
	add.w	#spd.len,a3
sppp_eloop
	dbra	d7,sppp_loop

	rts
	end
