; SBAS_PROCS_INSTR - SBASIC INSTR control  V2.00    1994  Tony Tebby

	section exten

	xdef	instr_case

	xref	ut_gxin1

	include 'dev8_keys_sbasic'

;+++
; INSTR_CASE 0/1
;---
instr_case
	jsr	ut_gxin1
	tst.w	(a6,a1.l)
	sne	sb_cinst(a6)		  ; set instr flag
	rts
	end
