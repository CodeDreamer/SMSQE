; HOTKEY Extensions  V2.03	 1988	Tony Tebby   QJUMP
; Added function FEP V2.04	pjwitte 2oo3
; 2005.01.12	2.05	added hot_getstuff$ (mk)


	section hotkey

	xdef	hot_init

	xref	ut_procdef

	include dev8_mac_proc
	include dev8_ee_hk_data

	section hotkey

hot_init
	lea	proc_tab,a1		 ; BASIC extensions
	jmp	ut_procdef

	section procs
proc_tab
	proc_stt
	proc_def HOT_STUFF
	proc_def HOT_GO
	proc_def HOT_STOP
	proc_def HOT_LIST
	proc_def HOT_DO
	proc_def EXEP
	proc_def ERT
	proc_def TH_FIX
	proc_def ALTKEY
	proc_end

	proc_stt
	proc_def FEP			; Function FEP pjw
	proc_def HOT_RES
	proc_def HOT_RES1
	proc_def HOT_CHP
	proc_def HOT_CHP1
	proc_def HOT_LOAD
	proc_def HOT_LOAD1,HOT_LOD1
	proc_def HOT_THING
	proc_def HOT_THING1,HOT_THG1

	proc_def HOT_PICK
	proc_def HOT_WAKE
	proc_def HOT_KEY
	proc_def HOT_CMD
	proc_def HOT_NAME$
	proc_def HOT_TYPE
	proc_def HOT_OFF
	proc_def HOT_SET
	proc_def HOT_REMV
	proc_def HOT_GETSTUFF$
	proc_end

	end
