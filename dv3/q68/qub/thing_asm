; DV3 Standard Hard Disk Control Procedures   V1.03    1999   Tony Tebby
; adapted for Q68 qubdide driver	       1.03	2017   W. Lenerz
; 1.01 removed reference to cv_locas (mk)

	section exten

	xdef	qub_thing
	xdef	qub_tname

	xref	win_drv
	xref	win_wpr

	xref	thp_ostr
	xref	thp_nrstr
	xref	thp_wd
	xref	dv3_usep
	xref	dv3_acdef

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_q68'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; ASCI Thing NAME
;---
qub_tname dc.b	 0,11,'QUB Control',$a
	dc.w 0
;+++
; This is the Thing with the QUB extensions
;---
qub_thing

;+++
; QUB_USE xxx
;---
qub_use thg_extn {USE },win_wpr,thp_ostr
	jmp	dv3_usep


	end
