; DV3 Standard Floppy Mark Slave Block Updated	        1993	  Tony Tebby

	section dv3

	xdef	hd_slbupd

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

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
hd_slbupd
	st	ddf_slld(a4)		 ; mark slaved loading preferred
	st	hdl_freq(a3)		 ; flush
	move.b	hdl_apnd(a3),hdl_actm(a3) ; count
	tst.l	d0
	rts
	end
