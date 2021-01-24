; DOS Control Procedure V1.01				 1997	Marcel Kilgus
;
; 2017-04-20  1.01  Fixed DOS_USE without any parameter (MK)

	section dos

	xdef	dos_defs

	xref	iou_flnk
	xref	thp_ostr
	xref	thp_nrstr

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_iod'
	include 'dev8_mac_thg'
	include 'dev8_smsq_qpc_keys'

dos_dstr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.call+thp.str ; string
	 dc.w	0

dos_defs
	dc.l	th_name+2+16
	dc.l	dos_thing-*
	dc.l	'1.00'

;+++
; DOS Thing NAME
;---
dos_tname dc.b	0,11,'DOS Control',$a

;+++
; This is the Thing with the DOS extensions
;---
dos_thing

dos.reg reg	d1-d2/d6-d7/a0-a1

;+++
; DOS_USE xxx
;---
dos_use thg_extn {USE },dos_drive,thp_ostr
	movem.l dos.reg,-(sp)
	move.l	#'DOS0',d7		 ; standard name
	move.l	d7,d6			 ; assume reset
	move.l	4(a1),d0		 ; any parameter?
	beq.s	du_look
	move.l	d0,a1

	subq.w	#3,(a1) 		 ; 3 characters long
	bne.s	dos_err 		 ; ... oops
	move.l	2(a1),d6		 ; get new name
	and.l	#$5f5f5f00,d6		 ; in upper case
	add.b	#'0',d6 		 ; ending with '0'

du_look
	bsr	iou_flnk		 ; find linkage
	bne.s	du_rts
du_set
	move.l	d6,iod_dnus-iod_iolk+2(a0) ; set new name
	moveq	#0,d0
du_rts
	movem.l (sp)+,dos.reg
	rts

;+++
; DOS_DRIVE n,f$
;---
dos_drive thg_extn {DRIV},dos_drive$,dos_dstr
	movem.l dos.reg,-(sp)
	move.l	(a1)+,d1
	ble.s	dos_err
	cmp.l	#8,d1
	bhi.s	dos_err

	move.l	4(a1),a1
	dc.w	qpc.sdbse+1
dos_exit
	movem.l (sp)+,dos.reg
	rts

dos_err
	moveq	#err.ipar,d0
	bra.s	dos_exit

;+++
; DOS_DRIVE$ n
;---
dos_drive$ thg_extn {DRV$},,thp_nrstr
	movem.l dos.reg,-(sp)
	move.l	(a1)+,d1
	ble.s	dos_err
	cmp.l	#8,d1
	bhi.s	dos_err

	move.w	2(a1),d0		 ; buffer size
	move.l	4(a1),a1

	dc.w	qpc.gdbse+1
	movem.l (sp)+,dos.reg
	rts

	end
