; DV3 JAVA Floppy Disk Initialisation  0.00 @ 214 W. Lenerz
; based on
; DV3 QXL Floppy Disk Initialisation  V3.00    1992  Tony Tebby

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
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_mac_assert'
	include 'dev8_keys_qlv'
	include 'dev8_keys_java'


	dc.l	jva_cfgf1		; java config string
	dc.l	jva_cfgf2
	dc.l	jva_cfgf3
	dc.l	jva_cfgf4
	dc.l	'FLP0'

disable dc.b	0			; set by the java init routine at mc6000kcpu.setupsmsqe


;+++
; DV3 java floppy disk initialisation
;
;	a1/a2/a3      smashed
;---
fd_init lea	fd_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			; always link in procedures

	lea	disable(PC),a1		; is driver disabled?
	tst.b	(a1)
	bne.s	exit			; yes, don't link it in


	lea	fd_table,a3
	jsr	dv3_link		; link in fd driver , on return A3 = linkage block

	move.l	4,d0			; get config info
	beq.s	exit			; not there!
	move.l	d0,a1
	move.l	jva_flpu(a1),d0 	; usage name
	beq.s	exit
	move.l	d0,ddl_dnuse+2(a3)
exit	moveq	#0,d0
	rts


fd_table
	link_table	FLP, fd.rev, fdl_end2, ddf_dtop
	buffered
	track
	sectl		512
	mtype		ddl.flp
	density 	ddf.dd,ddf.hd	       ; no single density allowed
;	density 	ddf.sd,ddf.dd,ddf.hd

	poll		fd_poll

	check		fd_check
	direct		fd_direct
	rsect		fd_rdirect
	wsect		fd_wdirect
	slbfill 	fd_slbfill
	slbupd		fd_slbupd
	dflush		fd_dflush
	fflush		fd_fflush
	mformat 	fd_mformat
	status		fd_fstatus
	done		fd_done

	thing		fd_tname,fd_thing

	assert		fdl_drvs,fdl_actm-1,fdl_rnup-2,fdl_apnd-3
	preset_b	fdl_drvs, $ff,0,fdl.rnup,fdl.apmc

	assert		fdl_maxd,fdl_offd-1,fdl_maxt-2,fdl_defd-4
	preset_b	fdl_maxd, 8, 0, 0, fdl.maxt, $ff, 0

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
	proc_def FLP_DRIVE
	proc_end
	proc_stt
	proc_def FLP_DRIVE$
	proc_end

	end
