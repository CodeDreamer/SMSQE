; DV3 Atari Floppy Disk Initialisation	V3.00	 1992	Tony Tebby

	section dv3

	xdef	fd_init

	xref.l	fd_vers
	xref.s	fd.rev
	xref	dv3_link
	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_mac_assert'
	include 'dev8_keys_qlv'

;+++
; DV3 Atari floppy disk initialisation
;
;	a3	smashed
;---
fd_init
	lea	fd_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			 ; link in procedures

	lea	fd_table,a3
	jmp	dv3_link		 ; link in fd driver

fd_table
	link_table	FLP, fd.rev, fdl_end4, ddf_dtop
	buffered
	track
	sectl		128,256,512,1024,2048
	mtype		ddl.flp
	density 	ddf.sd,ddf.dd,ddf.hd

	poll		fd_poll

	check		fd_check
	direct		fd_direct
	rsect		fd_rdirect16
	wsect		fd_wdirect16
	slbfill 	fd_slbfill
	slbupd		fd_slbupd
	dflush		fd_dflush
	fflush		fd_fflush
	mformat 	fd_mformat
	status		fd_fstatus
	done		fd_done

	thing		fd_tname,fd_thing

	assert		fdl_drvs,fdl_actm-1,fdl_rnup-2,fdl_apnd-3
	preset_b	fdl_drvs, $ff,0,fdl.rnup,fdl.apam


	assert		fdl_maxd,fdl_offd-1,fdl_maxt-2,fdl_defd-4
	preset_b	fdl_maxd, 2, 0, 0, fdl.maxt, $ff, 0

	preset_b	fdl_step,3,3,3,3

	preset_b	fdl_mxdns,ddf.hd     ; no higher than HD

	link_end	fdl_buff

	section exten

	proc_thg {FLP Control}

flp_use   proc	{USE }
flp_sec   proc	{SEC }
flp_start proc	{STRT}
flp_track proc	{TRAK}
flp_density proc {DENS}
flp_step  proc	{STEP}

fd_proctab
	proc_stt
	proc_ref FLP_USE
	proc_ref FLP_SEC
	proc_ref FLP_START
	proc_ref FLP_TRACK
	proc_ref FLP_DENSITY
	proc_ref FLP_STEP
	proc_end
	proc_stt
	proc_end

	end
