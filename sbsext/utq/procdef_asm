; link in extensions	        1988  Tony Tebby  Qjump

	section procs

	xdef	ut_procdef

	xref	ut_reassert

	include 'dev8_keys_qlv'

;+++
; Links and re-asserts the procedures pointd to by a1.
;
;	d2   s
;	a1 c s	pointer to procedure table
;---
ut_procdef
	movem.l a1/a2,-(sp)
	move.w	sb.inipr,a2
	jsr	(a2)
	movem.l (sp)+,a1/a2
; ut_reassert here !!!
	end
