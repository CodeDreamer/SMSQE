; WMAN colours basic procedures base  V1.02	       2002 Marcel Kilgus
;
; 2002-05-27  1.01  Added WM_MOVEMODE (WL)
; 2013-04-28  1.02  Added WM_MOVEALPHA (wl)

	section wm_ext

	xdef	wm_initp

	include 'dev8_keys_qlv'
	include 'dev8_mac_proc'

;+++
; Initialise SBASIC procedures
;---
wm_initp
	lea	wm_procs,a1		  ; WMAN procedures
	move.w	sb.inipr,a2
	jmp	(a2)

wm_procs
	proc_stt
	proc_def WM_PAPER
	proc_def WM_INK
	proc_def WM_STRIP
	proc_def WM_BLOCK
	proc_def WM_BORDER
	proc_def WM_MOVEMODE
	proc_def WM_MOVEALPHA
	proc_def SP_RESET
	proc_def SP_GET
	proc_def SP_SET
	proc_def SP_JOBPAL
	proc_def SP_JOBOWNPAL
	proc_end
	proc_stt
	proc_def SP_GETCOUNT
	proc_end

	end
