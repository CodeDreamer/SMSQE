; DV3 Standard Floppy Mark Slave Block Updated	        1993	  Tony Tebby

	section dv3

	xdef	fd_slbupd

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine is called when a slave block (or map information) is updated
;
;	d7 c  p drive ID / number
;	a1 c  p pointer to slave block table (or 0)
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers preserved, status return according to d0
;---
fd_slbupd
	st	ddf_slld(a4)		 ; mark slaved loading preferred
	st	fdl_freq(a3)		 ; flush
	move.b	fdl_apnd(a3),fdl_actm(a3) ; count
	tst.l	d0
	rts
	end
