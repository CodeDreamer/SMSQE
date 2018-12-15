; DV3 PC Compatible Pause 2 ms	    2000     Tony Tebby

	section dv3

	xdef	fd_p2ms 	       ; pause 2 ms

	xref.l	fdc_stat

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; Pause 2 ms
;
;	all registers preserved
;
;---
fd_p2ms
fp2.reg reg	d0/a0
	movem.l fp2.reg,-(sp)

	lea	fdc_stat,a0		 ; status register address
	move.l	fdl_1sec(a3),d0 	 ; 1 second timer ish
fp2_wait
	tst.b	(a0)			 ; status
	sub.l	#500,d0 		 ; count down
	bgt.s	fp2_wait

	movem.l (sp)+,fp2.reg
	rts

	end
