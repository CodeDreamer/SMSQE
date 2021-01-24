; Close DOS	    V1.01     1997   Marcel Kilgus
;
; 2018-12-31  1.01  Adapted for new QPC2v5 DOS driver

	section dos

	xdef	dos_close

	xref	iou_achb
	xref	iou_rchb
	xref	ioa_cknm

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_qpc_dos_data'
	include 'dev8_smsq_qpc_keys'

dos_close
reglist reg	a3/a4
	movem.l reglist,-(sp)
	move.l	chn_hand(a0),d1
	dc.w	qpc.dclse
	cmp.b	#4,chn_accs(a0)
	bne.s	dos_smsclose

	dc.w	qpc.ddelt		; Delete temporary directory file
dos_smsclose
	move.l	chn_ddef(a0),a4 	; Physical definition
	subq.b	#1,iod_nrfl(a4) 	; decrement file count

	lea	chn_link(a0),a0
	lea	sys_fsch(a6),a1
	move.w	mem.rlst,a2
	jsr	(a2)			; unlink file from list
	lea	-chn_link(a0),a0
	move.w	mem.rchp,a2
	jsr	(a2)
	movem.l (sp)+,reglist
	rts

	end
