; DOS initialisation  V1.01				 1997	Marcel Kilgus

	section dos

	xdef	dos_init

	xref.l	dos_vers

	xref	gu_achpp
	xref	gu_thini
	xref	dos_io
	xref	dos_open
	xref	dos_close
	xref	dos_defs

	include 'dev8_mac_proc'
	include 'dev8_mac_basic'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_qpc_dos_data'

;+++
; Initialise DOS_USE and DOS device.
;
;	status return standard
;---
dos_init
	dc.w	qpc.dclal		 ; first close all previous DOS files

	move.l	#dsd_end,d0		 ; allocate linkage
	jsr	gu_achpp

	move.l	a0,a3

	lea	dos_io,a1
	move.l	a1,iod_ioad(a3) 	 ; io
	lea	dos_open,a1
	move.l	a1,iod_open(a3) 	 ; open
	lea	dos_close,a1
	move.l	a1,iod_clos(a3) 	 ; close
	lea	dos_nimp,a1
	move.l	a1,iod_frmt(a3) 	 ; format

	lea	iod_plen(a3),a1
	move.l	#dsd.plen,(a1)+ 	 ; physical linkage

	move.l	a1,a0			 ; drive name
	move.w	#3,(a0)+
	move.l	dos_name,(a0)+
	move.w	(a1)+,(a0)+
	move.l	(a1),(a0)

	lea	iod_iolk(a3),a0 	 ; link in
	moveq	#sms.lfsd,d0
	trap	#do.sms2

	lea	dos_defs,a1		 ; Thing definition
	jsr	gu_thini

	lea	dos_proctab,a1		 ; SBASIC procedures
	move.w	sb.inipr,a2
	jmp	(a2)

dos_nimp
	moveq	#err.nimp,d0
	rts

dos_name dc.b	'DOS0'

	proc_thg {DOS Control}
	fun_thg  {DOS Control}

dos_use 	proc	{USE }
dos_drive	proc	{DRIV}
;dos_drive$	 fun	 {DRV$},204     ; Macro does MOVEQ, i.e. val gets -ve!
dos_drive$	move.l	#204,d7
		bsr.s	fun_thg
		dc.l	'DRV$'

dos_proctab
	proc_stt
	proc_ref DOS_USE
	proc_ref DOS_DRIVE
	proc_end
	proc_stt
	proc_ref DOS_DRIVE$
	proc_end
	end
