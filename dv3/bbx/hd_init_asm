; DV3 Bytebox IDE Initialisation  V3.00    1992  Tony Tebby

	section dv3

	xdef	hd_init

	xdef	ide.0
	xdef	ide.1
	xdef	ideo.data
	xdef	ideo.errr
	xdef	ideo.scnt
	xdef	ideo.sect
	xdef	ideo.cyll
	xdef	ideo.cylh
	xdef	ideo.head
	xdef	ideo.cmd
	xdef	ideo.stat
	xdef	ideo.alt

	xref.l	hd_vers
	xref.s	hd.rev
	xref	dv3_link
	xref	dv3_acdef

	xref	gu_fopen
	xref	gu_fclos

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_keys_bytebox'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'
	include 'dev8_mac_assert'


ide.0	  equ	ide0
ide.1	  equ	ide1

	assert	IDE0A-IDE0,IDE1A-IDE1
ideo.alt  equ	IDE0A-IDE0

ideo.data equ	 IDEIOW-IDEIO+ideo_data
ideo.errr equ	 ideo_errr
ideo.scnt equ	 ideo_scnt
ideo.sect equ	 ideo_sect
ideo.cyll equ	 ideo_cyll
ideo.cylh equ	 ideo_cylh
ideo.head equ	 ideo_head
ideo.cmd  equ	 ideo_cmd
ideo.stat equ	 ideo_stat

ide.1sec equ	1000000

;+++
; DV3 ByteBox disk initialisation
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
	jsr	dv3_link		 ; link in ide driver

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
	clr.b	hdl_part(a3)		 ; no partition, assume 0

	moveq	#0,d0
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


hd_table
	link_table	{WIN}, hd.rev, hdl_end, ddf_dtop
	buffered
	track
	sectl		512
	mtype		ddl.hd
	msect		255
	density 	ddf.dd

	poll		hd_poll

	check		hd_check
	direct		hd_direct
	rsect		id_rdirect
	wsect		id_wdirect
	slbfill 	hd_slbfill
	slbupd		hd_slbupd
	dflush		hd_dflush
	fflush		hd_fflush
;;;	fslave		hd_fslave
	mformat 	id_mformat
	status		hd_fstatus
	done		hd_done

	thing		hd_tname,hd_thing

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

	preset_b	hdl_part,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	preset_b	hdl_remd,1,1,1,1,1,1,1,1

	preset_l	hdl_1sec,ide.1sec

	preset_v	hdl_rsint,id_rsint
	preset_v	hdl_wsint,id_wsint
	preset_v	hdl_ckrdy,id_ckrdy
	preset_v	hdl_ckwp,id_ckwp
	preset_v	hdl_lock,id_lock
	preset_v	hdl_unlock,id_unlock
	preset_v	hdl_ststp,id_ststp

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

hd_proctab
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
