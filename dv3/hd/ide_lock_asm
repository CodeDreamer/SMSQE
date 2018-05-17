; DV3 IDE Lock/Unlock Drive   V3.00     1998	  Tony Tebby

	section dv3

	xdef	id_lock 		; lock drive
	xdef	id_unlock		; unlock drive

	xref	id_cmdw

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'

;+++
; This routine locks an IDE drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_lock
  ; save a5!!
	moveq	#0,d0
	rts
;+++
; This routine unlocks an IDE drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_unlock
  ; save a5!!
	moveq	#0,d0
	rts

	end
