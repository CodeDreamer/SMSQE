; link in extensions	        1988  Tony Tebby  Qjump

	section procs

	xdef	ut_procdef

	xref	ut_reassert

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'

;+++
; Links and (re-asserts) the procedures pointed to by a1.
;
;	d2   s
;	a1 c s	pointer to procedure table
;---
ut_procdef
	jmp	sb.inipr*3+qlv.off
	end
