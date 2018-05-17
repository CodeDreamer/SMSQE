; QDOS Trap #0 Emulation   V2.01    1993  Tony Tebby	QJUMP

	section qd

	xdef	qd_trap0

	include 'dev8_keys_68000'

	dc.l	0,0,0,0 		   ; 16 bytes for cache patch

;+++
; Goto supervisor mode
;---
qd_trap0
	bset	#sr..s,(sp)		   ; ... set supervisor mode
	rte				   ; and return
	end
