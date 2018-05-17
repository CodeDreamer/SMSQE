; DV3 QXL Lock/Unlock Drive   V3.00     1998	  Tony Tebby

	section dv3

	xdef	hd_lock 		; lock drive
	xdef	hd_unlock		; unlock drive

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'

;+++
; This routine locks a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_lock
  ; save a5!!
	moveq	#0,d0
	rts
;+++
; This routine unlocks a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_unlock
  ; save a5!!
	moveq	#0,d0
	rts

	end
