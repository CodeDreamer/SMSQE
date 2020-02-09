; QXL_MAIN.ASM The main loop
; 2006.10.01	1.01	update led
qxl_main:
        ASSUME ds:DGROUP
        ASSUME es:DGROUP

qxm_loop:
	call	qxl_rtc_check			; time changed?

	test	phd_rlen,0ffffh			; read physical request?
	jns	qxm_no_rphys			; S set for action
	call	qxl_do_rphys
qxm_no_rphys:

	test	phd_wlen,0ffffh			; write physical request?
	jns	qxm_no_wphys			; S set for action
	call	qxl_do_wphys
qxm_no_wphys:

	test  lpt_txdata,0ffh			; data for printers?
	jz	qxm_no_lpt
	call	qxl_do_lptp				; yes, send if possible
qxm_no_lpt:

	test  com_txdata,0ffh			; data for com ports?
	jz	qxm_no_com
	call	qxl_do_comp				; yes, send if possible
qxm_no_com:

	test  vmod_set,0ffffh			; set video mode?
	js	qxm_no_vmod
	call	qxl_do_vmod				; yes, do it
qxm_no_vmod:

        call    qxl_do_led                      ; update led if needed

qxm_no_op:
	test	kbd_stop,0ffh
	jz	qxm_loop
        call    qld_from_bios
	retn
