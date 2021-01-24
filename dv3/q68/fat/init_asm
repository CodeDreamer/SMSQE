; DV3 Q68 SDHC Driver initialisation 1.01  W. Lenerz 2016-2020
;
; based on
;
; DV3 Q40 IDE Initialisation  V3.00    1992  Tony Tebby
; 1.01	rearranged config items

	section dv3

	xdef	fat_init		; inits the driver

	xref	dv3_link
	xref	dv3_acdef

	xref	gu_fopen
	xref	gu_fclos
	xref	fat_vers

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_mac_assert'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'
	include 'dev8_keys_q68'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_mac_MultiConfig02'

;+++
; DV3 Q68 SDHC FAT driver initialisation
;
;	a3  r	device driver linkage
;---
fat.rev equ	'1'
fat_init
	lea	qcf_fatl,a1
	tst.b	(a1)			; load driver at all?
	beq.s	fat_out 		; no

	lea	fat_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			; link in procedures

	lea	fat_table,a3
	jsr	dv3_link		; link in driver

; now a little fudge : if SMSQE is to boot from FAT 1, I set the USE name
; of the device to "flp" - this is so that I can use the facility offered in
;"sbas_main_asm" to boot either from win or from flp
	move.b	sms.conf+sms_bflp,d0	; boot from "floppy" = fat?
	beq.s	fat_set 		; no, done
	move.l	#'FLP0',ddl_dnuse+2(a3) ; yes pretend I'm flp
fat_set lea	qcf_fatu,a2
	lea	hdl_unit(a3),a1
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+		; set configured units (cards)
	addq.w	#8,a1
	move.l	(a2)+,(a1)+		; set configured partitions
	move.l	(a2)+,(a1)+
fat_out rts

fat_table
	link_table	FAT, fat.rev, hdl_end, ddf_dtop
	buffered			 ; commenting out results in a not working drive
	sectl		512
	mtype		ddl.hd
	msect		1		; this will use slave blocks for simple reads
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

	thing		fat_tname,fat_thing

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

	proc_thg	{FAT Control}
	fun40_thg	{FAT Control}

FAT_use   proc	{USE }
FAT_drive proc	{DRIV}
FAT_wp	  proc	{WPRT}
FAT_drive$ fun40 {DRV$}

fat_proctab
	proc_stt
	proc_ref FAT_USE
	proc_ref FAT_DRIVE
	proc_ref FAT_WP
	proc_end
	proc_stt
	proc_ref FAT_DRIVE$
	proc_end

   
; fat config items
	dc.w	0
qcf_fatu	dc.b	0,q68.coff,-1,-1,-1,-1,-1,-1 ; the cards for each fat drive
qcf_fatp	dc.b	1,1,1,1,1,1,1,1 ; the partition (1-4)
qcf_fatl	dc.b	0

	mkcfstart
	mkcfhead {FAT device settings},{fat_vers}

	  mkcfitem 'q68i',code,'L',qcf_fatl,,,\
	  {Link in FAT driver?} \
	  0,N,{No},1,Y,{Yes}

	  mkcfitem 'Q68a',code,0,qcf_fatu,,,\
	  {Fat1_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68b',code,0,qcf_fatu+1,,,\
	  {Fat2_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68c',code,0,qcf_fatu+2,,,\
	  {Fat3_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68d',code,0,qcf_fatu+3,,,\
	  {Fat4_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68e',code,0,qcf_fatu+4,,,\
	  {Fat5_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68f',code,0,qcf_fatu+5,,,\
	  {Fat6_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68g',code,0,qcf_fatu+6,,,\
	  {Fat7_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}
	  mkcfitem 'Q68h',code,0,qcf_fatu+7,,,\
	  {Fat8_ is on card},0,1,{1},q68.coff,2,{2},$ff,N,{None}

	  mkcfitem 'Q68R',byte,'P',qcf_fatp,,,\
	  {Fat1_ partition number (1-4)},1,4
	  mkcfitem 'Q68S',byte,'P',qcf_fatp+1,,,\
	  {Fat2_ partition number (1-4)},1,4
	  mkcfitem 'Q68T',byte,'P',qcf_fatp+2,,,\
	  {Fat3_ partition number (1-4)},1,4
	  mkcfitem 'Q68U',byte,'P',qcf_fatp+3,,,\
	  {Fat4_ partition number (1-4)},1,4
	  mkcfitem 'Q68V',byte,'P',qcf_fatp+4,,,\
	  {Fat5_ partition number (1-4)},1,4
	  mkcfitem 'Q68W',byte,'P',qcf_fatp+5,,,\
	  {Fat6_ partition number (1-4)},1,4
	  mkcfitem 'Q68X',byte,'P',qcf_fatp+6,,,\
	  {Fat7_ partition number (1-4)},1,4
	  mkcfitem 'Q68Y',byte,'P',qcf_fatp+7,,,\
	  {Fat8_ partition number (1-4)},1,4

	mkcfblend

	mkcfend

	end
