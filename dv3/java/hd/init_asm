; DV3 java HDD inititilization	 1.00 @ W. Lenerz 2020
;
; based on
;
; DV3 Q40 IDE Initialisation  V3.00    1992  Tony Tebby

	section dv3

	xdef	hd_init

	xref.l	hd_vers
	xref.s	hd.rev
	xref	dv3_link
	xref	dv3_acdef

	xref	gu_fopen
	xref	gu_fclos

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'

;+++
; DV3 JAVA HD disk initialisation
;
;	d6   s
;	d7   s
;	a3	smashed
;---
hd_init
	lea	hd_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			 ; link in procedures

	lea	hd_table,a3
	jsr	dv3_link		 ; link in driver

hdi_rts
	rts

hd_table
	link_table	HDD, hd.rev, hdl_end, ddf_dtop
	buffered
	sectl		512
	mtype		ddl.hd
	msect		255
	density 	ddf.dd

	poll		hd_poll_check

	check		hd_check
	direct		hd_direct
	rsect		hd_rdirect
	wsect		hd_wdirect
	slbfill 	hd_slbfill
	slbupd		hd_slbupd
	dflush		hd_dflush
	fflush		hd_fflush
;;;	fslave		hd_fslave
	mformat 	hd_mformat
	status		hd_fstatus
	done		hd_done

	thing		hd_tname,hd_thing

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

	preset_b	hdl_part,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	preset_b	hdl_remd,0,0,0,0,0,0,0,0
;	 preset_b	 hdl_remd,1,1,1,1,1,1,1,1

	preset_v	hdl_rsint,hd_rsint
	preset_v	hdl_wsint,hd_wsint
	preset_v	hdl_ckrdy,hd_ckrdy
	preset_v	hdl_ckwp,hd_ckwp
	preset_v	hdl_lock,hd_lock
	preset_v	hdl_unlock,hd_unlock
	preset_v	hdl_ststp,hd_ststp

	link_end	hdl_buff

	section exten

	proc_thg {HDD Control}
	fun_thg {HDD Control}

HDD_use   proc	{USE }
HDD_drive proc	{DRIV}
;HDD_start proc  {STRT}
;HDD_stop  proc  {STOP}
HDD_remv  proc	{REMV}
HDD_wp	  proc	{WPRT}
HDD_format proc {FRMT}
;HDD_slug  proc  {SLUG}


HDD_drive$
	move.l	#260,d7
	bsr.s	fun_thg
	dc.l	'DRV$'


hd_proctab
	proc_stt
	proc_ref HDD_USE
	proc_ref HDD_DRIVE
	proc_ref HDD_REMV
	proc_ref HDD_WP
	proc_ref HDD_FORMAT
	proc_end
	proc_stt
	proc_ref HDD_DRIVE$
	proc_end

	end
