; DEV initialisation  V2.00   1985  Tony Tebby   QJUMP

	section dev

	xdef	dev_init
	xdef	dev_name

	xref.l	dev_vers

	xref	gu_achpp
	xref	dev_open
	xref	ut_procdef

	xref	dev_xinit

	include 'dev8_mac_proc'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_dd_dev_data'

;+++
; Initialise DEV_USE and DEV device.
;
;	status return standard
;---
dev_init
	lea	proctab,a1
	jsr	ut_procdef

	move.l	#dvd_end,d0		 ; allocate linkage
	jsr	gu_achpp

	move.l	a0,a3

	lea	dev_open,a1
	move.l	a1,iod_open(a3) 	 ; just open
	lea	dev_nimp,a1
	move.l	a1,iod_frmt(a3) 	 ; and format

	lea	iod_plen(a3),a1
	move.l	#dvd.plen,(a1)+ 	 ; physical linkage

	move.l	a1,a0			 ; drive name
	move.w	#3,(a0)+
	move.l	dev_name,(a0)+
	move.w	(a1)+,(a0)+
	move.l	(a1),(a0)

	lea	iod_iolk(a3),a0 	 ; link in
	moveq	#sms.lfsd,d0
	trap	#do.sms2

	jmp	dev_xinit		 ; extra init

dev_nimp
	moveq	#err.nimp,d0
	rts

dev_name dc.l	'DEV0'

	section procs
proctab
	proc_stt
	proc_def DEV_USE
	proc_def DEV_USEN
	proc_def DEV_LIST
	proc_end
	proc_stt
	proc_def DEV_USE$
	proc_def DEV_NEXT
	proc_end
	end
