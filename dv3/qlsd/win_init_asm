; DV3 QLSD SDHC (WIN) Driver initialisation 1.02  2018 W. Lenerz + M. Kilgus
;
; based on
;
; DV3 Q40 IDE Initialisation  V3.00    1992  Tony Tebby
;
; !!! non standard linkage block size
;
; 2018-06-07  1.01  Added new qlsd_* vectors (MK)
; 2018-07-18  1.02  New default filenames for WIN1..8 (MK)

	section dv3

	xdef	win_init		; inits the driver
	xdef	qsd_cf1
	xdef	qsd_cf2
	xdef	qsd_cf3
	xdef	qsd_cf4
	xdef	qsd_cf5
	xdef	qsd_cf6
	xdef	qsd_cf7
	xdef	qsd_cf8
	xdef	qsd_crd

	xref	dv3_link
	xref	qlsd_1sec
	xref.l	qlsd_vers
	xref	norm_nm

	xref	gu_fopen
	xref	gu_fclos

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_mac_assert'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_smsq_base_keys'

;++++++++++++++++++++
; DV3 QLSD SDHC (WIN) driver initialisation
;
;	a3  r	device driver linkage
;--------------------
win_reg reg	d4-d7
win_init
	movem.l win_reg,-(sp)
	lea	hd_table,a3
	jsr	dv3_link		; link in driver
	tst.l	d0
	bne.s	out			; ooops!

	jsr	qlsd_1sec		; calibrate timing
	move.l	d0,hdl_1sec(a3) 	; and remember it

; Copy the drive->card mappings
	lea	hdl_unit(a3),a1
	lea	qsd_crd,a0
	move.l	(a0)+,(a1)+
	move.l	(a0)+,(a1)+

; Normalize the configured names into our working copy
	lea	qsd_cf1+2,a0		; point first drive name
	move.l	a0,d5
	lea	qlsd_names(a3),a1	; point space for normalized names
	move.l	a1,d6
	moveq	#16,d2			; offset from one name to the other
	moveq	#7,d7
norm_lp bsr	norm_nm 		; normalize name at (a0) to (a1)
	add.l	d2,d5
	move.l	d5,a0
	add.l	d2,d6
	move.l	d6,a1
	dbf	d7,norm_lp
	moveq	#0,d0			; done
out	movem.l (sp)+,win_reg
	rts

hd_table
	link_table	WIN, '1', qlsd_end, ddf_dtop
	buffered			; commenting out results in a not working drive
	sectl		512
	mtype		ddl.hd
	msect		255		; this will still use slave blocks for simple reads
	density 	ddf.dd

	poll		hd_poll_check

	check		qlsd_check
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

;;	  thing 	  hd_tname,hd_thing

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

; each preset costs 12 bytes
;	 preset_b	 hdl_remd,1,1,1,1,1,1,1,1
;	 preset_b	 hdl_unit,1,2,1,2,1,2,1,2
;	 preset_b	 hdl_part,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff

	preset_v	hdl_rsint,hd_rsint
	preset_v	hdl_wsint,hd_wsint
	preset_v	hdl_ckrdy,hd_ckrdy
	preset_v	hdl_ckwp,hd_ckwp
	preset_v	hdl_lock,hd_lock
	preset_v	hdl_unlock,hd_unlock
	preset_v	hdl_ststp,hd_ststp

	preset_l	qlsd_magic,qlsd.magic
	preset_l	qlsd_version,qlsd_vers
	preset_v	qlsd_rscard,hd_rscard_api
	preset_v	qlsd_wscard,hd_wscard_api
	preset_v	qlsd_inicard,inicrd

	link_end	hdl_buff

; Card numbers
qsd_crd dc.b	1,2,1,1,1,1,1,1

; Configured names. Spacing must be 16 bytes
qsd_cf1 dc.w	13,7,'QXL.WIN',0,0
qsd_cf2 dc.w	13,7,'QXL.WIN',0,0
qsd_cf3 dc.w	13,8,'QXL3.WIN',0,0
qsd_cf4 dc.w	13,8,'QXL4.WIN',0,0
qsd_cf5 dc.w	13,8,'QXL5.WIN',0,0
qsd_cf6 dc.w	13,8,'QXL6.WIN',0,0
qsd_cf7 dc.w	13,8,'QXL7.WIN',0,0
qsd_cf8 dc.w	13,8,'QXL8.WIN',0,0

	end
