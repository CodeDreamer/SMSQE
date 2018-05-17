; DV3 QPC Floppy Disk All Files Closed	        1993	  Tony Tebby

	section dv3

	xdef	fd_done

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'
	include 'dev8_smsq_qpc_keys'

;+++
; This routine is called when all files are closed
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers preserved, status return arbitrary
;---
fd_done
	move.b	#ddf.unlock,ddf_lock(a4) ; unlock sometime
	tst.b	fdl_freq(a3)		 ; any flush pending?
	bne.s	fd_rts			 ; ... yes
	assert	ddf.open,0
	sf	ddf_lock(a4)		 ; ... no, unlocked now
	dc.w	qpc.frlse		 ; release drive
fd_rts
	rts
	end
