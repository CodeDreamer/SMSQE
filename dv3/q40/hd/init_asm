; DV3 Q40 IDE Initialisation  V3.03    1992  Tony Tebby
;
; v3.03 2020-05-06 delay whilst waiting for drive to come online (wl)
; v3.02 WIN_CHECK,CARD_DIR$ added    @ w. lenerz 2020
; V3.01 Additions for LBA access      W. Lenerz 2017 Nov 21
;
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
	xref	hd_1sec
	xref	dv3_link
	xref	dv3_acdef

	xref	norm_nm

	xref	gu_fopen
	xref	gu_fclos

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_keys_q40_multiIO'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
	include 'dev8_mac_Multiconfig02'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_q40'
	include 'dev8_smsq_smsq_base_keys'

ide.0	  equ	ide0
ide.1	  equ	ide1
drv_led   equ	hdl_freq+1

	assert	IDE0A-IDE0,IDE1A-IDE1
ideo.alt  equ	IDE0A-IDE0

ideo.data equ	Q40IOW-Q40IO+ideo_data
ideo.errr equ	ideo_errr
ideo.scnt equ	ideo_scnt
ideo.sect equ	ideo_sect
ideo.cyll equ	ideo_cyll
ideo.cylh equ	ideo_cylh
ideo.head equ	ideo_head
ideo.cmd  equ	ideo_cmd
ideo.stat equ	ideo_stat


;+++
; DV3 Q40 disk initialisation
;
;	d6   s
;	d7   s
;	a3	smashed
;---
hd_init
	lea	hd_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			; link in procedures

	lea	hd_table,a3
	jsr	dv3_link		; link in ide driver

	jsr	hd_1sec
	move.l	d0,hdl_1sec(a3)
	move.l	d0,d5

	lea	q40_wn1+2,a0		; configured name
	lea	hdl_end(a3),a1		; names lie after device defn (linkage) block
	bsr	norm_nm 		; copy & normalise name

	moveq	#0,d3
	move.b	q40_dkdl,d3		; wait for drive?
	beq.s	hdi_ckdrive		; no
	mulu	#25,d3			; the check takes a long time
	moveq	#1,d2			; read 1 sector
	moveq	#0,d7			; of disk (and target) 1
	lea	hdl_buff(a3),a1 	; root sector buffer

hdi_wait
	jsr	hd_1sec
	moveq	#0,d0
	jsr	hdl_rsint(a3)		; check disk : try to read sector 0
	beq.s	hdi_ckdrive		; success, done
	dbf	d3,hdi_wait
hdi_ckdrive
	moveq	#0,d1			; try partitions 0 to 7
	moveq	#1,d7			; for WIN1
	st	hdl_npart(a3)		; assume no partition found at all
hdi_loop
	move.b	d1,hdl_part(a3)
	lea	hdi_boot,a0
	moveq	#ioa.kshr,d3
	jsr	gu_fopen		; try to open
	beq.s	hdi_found		; OK
	tst.b	hdl_npart(a3)		; any partition found at all?
	bne.s	hdi_npart		; ... no

	lea	hdi_blat,a0		; blat the definition
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
	link_table	WIN, hd.rev, hdl_end+8*12, ddf_dtop
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
	fun_thg {WIN Control}

win_use   proc	{USE }
win_drive proc	{DRIV}
win_start proc	{STRT}
win_stop  proc	{STOP}
win_remv  proc	{REMV}
win_wp	  proc	{WPRT}
win_format proc {FRMT}
win_slug  proc	{SLUG}
win_check proc	{CHEK}
card_crush proc {CRSH}
win_drive$ fun40 {DRV$}
card_dir$	move.l	#18*14,d7
		bsr	fun_thg
		dc.l	'CDIR'

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
	proc_ref WIN_CHECK
	proc_ref CARD_CRUSH
	proc_end
	proc_stt
	proc_ref WIN_DRIVE$
	proc_ref CARD_DIR$
	proc_end


	mkcfstart

	mkcfhead {WIN},1

	  mkcfitem 'Q481',string,0,q40_wn1,,ppr,{Filename for WIN1 container file},0

	  mkcfitem 'Q482',byte,'D',q40_dkdl,,,\
	  {Disk startup delay: 0 - 20},0,20

	mkcfblend

	mkcfend


q40_wn1 dc.w   13,7
	dc.w   'QXL.WIN',0,0,0

drv_ind dc.b	$ff,0

q40_dkdl dc.b	0

; post processing routine to make sure that a name is a valid 8.3 name
ppr	tst.b	d1
	beq.s	p_ok
	addq.l	#2,a0			; point to item name
	move.w	(a0)+,d2		; length
	beq.s	p_ok			; 0 length is fine
	cmp.w	#12,d2			; max chars allowed (8+3+extn)
	bgt.s	ipar			; too many
	subq.w	#1,d2			; prepare for dbf
	moveq	#'.',d5 		; compare with this
	moveq	#0,d6			; nbr of chars compared till now
p_lp1	cmp.b	(a0)+,d5		; try to find extension marker
	beq.s	extn			; found it, check name
	addq.l	#1,d6			; new number of chars checked
	dbf	d2,p_lp1		; check all chars
; if we get here, there is no extension
	subq.w	#8,d6			; max nbr of chars w/o extn allowed
	ble.s	p_ok			; correct name w/ extn
	bra.s	ipar			; wrong name
; d2 nbr of chars remaining
; d6 nbr of chars before extension
extn
	subq.w	#8,d6			; nbr of chars got before extn
	bgt.s	ipar			; too many chars before the extension
	cmp.w	#3,d2			; how many chars remaining for the extn?
	bgt.s	ipar			; too many!
p_ok	moveq	#0,d0
	rts
ipar	moveq	#err.inam,d0
	rts

	end
