; DV3 Atari ACSI Disk Initialisation  V3.00    1992  Tony Tebby

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
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'

;+++
; DV3 Atari ACSI disk initialisation
;	d5 c  s ACSI auto detect removable
;	d6 c  s boot target and partition
;	d7   s
;	a3	smashed
;---
hd_init
	lea	ac_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			 ; link in procedures

	lea	ac_table,a3
	jsr	dv3_link		 ; link in acsi driver
	move.l	a3,a5			 ; save ACSI linkage
	move.l	a3,a4			 ; no scsi yet, asci is favourite

	tst.b	d5			 ; auto detect removable?
	bne.s	hdi_mtype		 ; ... yes
	clr.l	hdl_remd(a3)
	clr.l	hdl_remd+4(a3)		 ; ... no

hdi_mtype
	moveq	#sms.info,d0
	trap	#1
	moveq	#sys.mtyp,d1
	and.b	sys_mtyp(a0),d1 	 ; machine type

	cmp.b	#sys.mtt,d1		 ; TT
	bne.s	hdi_drive		 ; ... no

	lea	sc_table,a3
	jsr	dv3_link
	move.l	a3,a4			 ; save SCSI linkage

hdi_drive
	tst.w	d6			 ; anything other than 0,0
	beq.s	hdi_ckdrive		 ; ... no

	bclr	#3+8,d6 		 ; 0..7 or 8..15?
	beq.s	hdi_pracsi
	cmp.l	a4,a5			 ; any scsi?
	beq.s	hdi_ckdrive		 ; ... no
	move.l	a4,a5			 ; preset scsi


hdi_pracsi
	move.b	d6,hdl_part(a5) 	 ; set partition
	lsr.w	#8,d6
	move.b	d6,hdl_targ(a5)
	moveq	#0,d0
	rts

hdi_ckdrive
	moveq	#0,d1			 ; try partitions 0 to 7
	moveq	#1,d7			 ; for WIN1
	st	hdl_npart(a3)		 ; assume no partition found at all

hdi_loop
	move.b	d1,hdl_part(a3)
	lea	hdi_boot,a0
	moveq	#ioa.kshr,d3
	jsr	gu_fopen		 ; try to open
	beq.s	hdi_found		 ; OK
	tst.b	hdl_npart(a3)		 ; any partition found at all?
	bne.s	hdi_npart		 ; ... no

	lea	hdi_blat,a0		 ; blat the definition
	jsr	dv3_acdef

	addq.b	#1,d1
	cmp.b	#7,d1
	ble.s	hdi_loop		 ; try first 8 partitions

hdi_npart
	st	hdl_part(a3)		 ; no partition
	cmp.l	a5,a3			 ; was it ASCI we have just tried?
	move.l	a5,a3
	bne.s	hdi_drive

	moveq	#0,d0
	move.b	d0,hdl_part(a4) 	 ; reset favourite drive to partition 0
	rts

hdi_blat
	sf	ddf_mstat(a4)
	rts

hdi_found
	jsr	gu_fclos
	moveq	#sms.xtop,d0
	trap	#do.smsq
	lea	sys_datd(a6),a0 	 ; data default
	bsr.s	hdi_dset
	lea	sys_prgd(a6),a0
hdi_dset
	move.l	(a0),d0 		 ; set?
	beq.s	hdi_rts
	move.l	d0,a0
	move.l	hdi_boot+2,2(a0)	 ; set boot device as default
	moveq	#0,d0
hdi_rts
	rts



hdi_boot dc.w	 9,'win1_boot'

ac_table
	link_table	WIN, hd.rev, hdl_end, ddf_dtop
	buffered
	sectl		512
	mtype		ddl.hd
	msect		255
	density 	ddf.dd

	poll		ahd_poll

	check		hd_check
	direct		hd_direct
	rsect		ac_rsect
	wsect		ac_wsect
	slbfill 	hd_slbfill
	slbupd		hd_slbupd
	dflush		hd_dflush
	fflush		hd_fflush
	fslave		ahd_fslave
	mformat 	ahd_mformat
	status		hd_fstatus
	done		hd_done

	thing		hd_tname,hd_thing

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

	preset_b	hdl_part,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	preset_b	hdl_remd,1,1,1,1,1,1,1,1

	preset_v	hdl_rsint,ac_rsint
	preset_v	hdl_wsint,ac_wsint
	preset_v	hdl_ckrdy,ac_ckrdy
	preset_v	hdl_ckwp,ac_ckwp
	preset_v	hdl_lock,ac_lock
	preset_v	hdl_unlock,ac_unlock
	preset_v	hdl_ststp,ac_ststp

	link_end	hdl_buff

sc_table
	link_table	WIN, hd.rev, hdl_end, ddf_dtop
	buffered
	sectl		512
	mtype		ddl.hd
	msect		255

	poll		ahd_poll

	check		hd_check
	direct		hd_direct
	rsect		sc_rsect
	wsect		sc_wsect
	slbfill 	hd_slbfill
	slbupd		hd_slbupd
	dflush		hd_dflush
	fflush		hd_fflush

	mformat 	ahd_mformat
	status		hd_fstatus
	done		hd_done

	master		WIN

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

	preset_b	hdl_part,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	preset_b	hdl_remd,1,1,1,1,1,1,1,1

	preset_v	hdl_rsint,sc_rsint
	preset_v	hdl_wsint,sc_wsint
	preset_v	hdl_ckrdy,sc_ckrdy
	preset_v	hdl_ckwp,sc_ckwp
	preset_v	hdl_lock,sc_lock
	preset_v	hdl_unlock,sc_unlock
	preset_v	hdl_ststp,sc_ststp

	link_end	hdl_buff


	section exten

	proc_thg {WIN Control}
	fun40_thg {WIN Control}

win_use   proc	{USE }
win_drive proc	{DRIV}
win_start proc	{STRT}
win_stop  proc	{STOP}
win_remv  proc	{REMV}
win_wp	  proc	{WPRT}
win_format proc {FRMT}
win_slug  proc	{SLUG}

win_drive$ fun40 {DRV$}

ac_proctab
	proc_stt
	proc_ref WIN_USE
	proc_ref WIN_DRIVE
	proc_ref WIN_START
	proc_ref WIN_STOP
	proc_ref WIN_REMV
	proc_ref WIN_WP
	proc_ref WIN_FORMAT
	proc_ref WIN_SLUG
	proc_end
	proc_stt
	proc_ref WIN_DRIVE$
	proc_end

	end
