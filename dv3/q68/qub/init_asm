; DV3 Q68 QUBIDE Driver initialisation 1.00  W. Lenerz 2016
;
; based on
;
; DV3 Q40 IDE Initialisation  V3.00    1992  Tony Tebby


	section dv3

	xdef	qub_init		; inits the driver
	xdef	ppr
	xref	dv3_link
	xref	dv3_acdef

	xref	gu_fopen
	xref	gu_fclos

	xref	norm_nm
	xref.l	qub_vers

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_mac_assert'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_q68'
	include 'dev8_mac_MultiConfig02'

;+++
; DV3 Q68 Qubide driver initialisation
;
;	a3  r	device driver linkage
;---
qub.rev equ	'1'

qub_init
	lea	qcf_qubl,a1
	tst.b	(a1)			; load driver at all?
	beq.s	qub_out 		; no

	lea	qub_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			; link in procedures

	lea	qub_table,a3
	jsr	dv3_link		; link in driver

qub_set lea	qcf_qubu,a2
	lea	hdl_unit(a3),a1
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+		; set configured units (cards)

	lea	q68_wn1+2,a0		; point first smsqe name
	move.l	a0,d5
	lea	qub_drvs,a1		; point space for normalized names
	move.l	a1,d6
	moveq	#7,d7
	move.l	a1,hdl_targ(a3) 	; point to where I can find it later
norm_lp bsr	norm_nm 		; normalize name at (a0) to (a1)
	moveq	#16,d3
	add.l	d3,d5
	move.l	d5,a0
	add.l	d3,d6
	move.l	d6,a1
	dbf	d7,norm_lp

;	 addq.w  #8,a1
;	 move.l  (a2)+,(a1)+		 ; set configured partitions
;	 move.l  (a2)+,(a1)+
qub_out rts

qub_table
	link_table	QUB, qub.rev, hdl_end, ddf_dtop
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
;;	  mformat	  hd_mformat
	status		hd_fstatus
	done		hd_done

	thing		qub_tname,qub_thing

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

	preset_v	hdl_rsint,hd_rsint
	preset_v	hdl_wsint,hd_wsint
	preset_v	hdl_ckrdy,hd_ckrdy
	preset_v	hdl_ckwp,hd_ckwp
	preset_v	hdl_lock,hd_lock
	preset_v	hdl_unlock,hd_unlock
	preset_v	hdl_ststp,hd_ststp

	link_end	hdl_buff

; the basic interface to the thing
	proc_thg {QUB Control}
	fun40_thg {QUB Control}

QUB_use   proc	{USE }
QUB_drive proc	{DRIV}
QUB_wp	  proc	{WPRT}
QUB_drive$ fun40 {DRV$}

qub_proctab
	proc_stt
	proc_ref QUB_USE
	proc_ref QUB_DRIVE
	proc_ref QUB_WP
	proc_end
	proc_stt
	proc_ref QUB_DRIVE$
	proc_end

; configuration info

	section config


; name of the qubide files on the SDHC cards. The names are spaced by 16 bytes

q68_wn1 dc.w   13,10
	dc.w   'QL_BDI.BIN',0

q68_wn2 dc.w   13,10
	dc.w   'QL_BDI.BIN',0

q68_wn3 dc.w   13,11
	dc.w   'QL_BDI3.BIN'

q68_wn4 dc.w   13,11
	dc.w   'QL_BDI4.BIN'

q68_wn5 dc.w   13,11
	dc.w   'QL_BDI5.BIN'

q68_wn6 dc.w   13,11
	dc.w   'QL_BDI6.BIN'

q68_wn7 dc.w   13,11
	dc.w   'QL_BDI7.BIN'

q68_wn8 dc.w   13,11
	dc.w   'QL_BDI8.BIN'
; these two data areas must be right one after the other.
; now follows a table of 8*16 bytes, for the normalized names of the drives
qub_drvs
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0

qcf_qubu	dc.b	 0,q68.coff,-1,-1,-1,-1,-1,-1 ; the card the file is on
;;qcf_qubp	  dc.b	   0,0,0,0,0,0,0,0	  ; qubide partition
qcf_qubl	dc.b	 1
	mkcfstart

	mkcfhead {QUB drives settings},{qub_vers}


noyes	  mkcfitem 'q689',code,'L',qcf_qubl,,,\
	  {Link in QUB driver?} \
	  0,N,{No},1,Y,{Yes}

	  mkcfitem 'q681',string,0,q68_wn1,,ppr,{Filename for QUB1},0
	  mkcfitem 'q682',string,0,q68_wn2,,ppr,{QUB2},0
	  mkcfitem 'q683',string,0,q68_wn3,,ppr,{QUB3},0
	  mkcfitem 'q684',string,0,q68_wn4,,ppr,{QUB4},0
	  mkcfitem 'q685',string,0,q68_wn5,,ppr,{QUB5},0
	  mkcfitem 'q686',string,0,q68_wn6,,ppr,{QUB6},0
	  mkcfitem 'q687',string,0,q68_wn7,,ppr,{QUB7},0
	  mkcfitem 'q688',string,0,q68_wn8,,ppr,{QUB8},0

	  mkcfitem 'q68J',code,0,qcf_qubu,,,\
	  {Qub1_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68K',code,0,qcf_qubu+1,,,\
	  {Qub2_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68L',code,0,qcf_qubu+2,,,\
	  {Qub3_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68M',code,0,qcf_qubu+3,,,\
	  {Qub4_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68N',code,0,qcf_qubu+4,,,\
	  {Qub5_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68O',code,0,qcf_qubu+5,,,\
	  {Qub6_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68P',code,0,qcf_qubu+6,,,\
	  {Qub7_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'q68Q',code,0,qcf_qubu+7,,,\
	  {Qub8_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
  
	mkcfblend

	mkcfend

; post processing routine to make sure that a name is a valid 8.3 name
ppr
	tst.b	d1
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
