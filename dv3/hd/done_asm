; DV3 Hard Disk All Files Closed	  1993     Tony Tebby

	section dv3

	xdef	hd_done

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'
;+++
; This routine is called when all files are closed
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers preserved, status return arbitrary
;---
hd_done
	tst.b	ddf_lock(a4)		 ; locked?
	beq.s	hdd_rts
	tst.b	hdl_freq(a3)		 ; any flush pending?
	beq.s	hd_unlock		 ; ... no
	move.b	#ddf.unlock,ddf_lock(a4) ; unlock sometime
	st	hdl_freq(a3)		 ; request flush
	or.b	#1,hdl_actm(a3) 	 ; ensure at least a tick
hdd_rts
	rts

hd_unlock
	assert	ddf.open,0
	sf	ddf_lock(a4)		 ; ... no, unlocked now
	jmp	hdl_unlock(a3)
	end
