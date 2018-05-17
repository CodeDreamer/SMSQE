; DV3 QPC Floppy Disk Check Write Protect  V3.00   1993   Tony Tebby

	section dv3

	xdef	fd_ckwp

	include 'dev8_dv3_keys'
	include 'dev8_smsq_qpc_keys'

;+++
; Check write protect
;
;
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to drive definition
;
;	all registers except d0 preserved
;
;	status standard
;
;---
fd_ckwp
	movem.l d7,-(sp)
	move.b	ddf_dnum(a4),d7
	dc.w	qpc.fckwp
	blt.s	fcwp_rts
	sne	ddf_wprot(a4)		; set write protected
	moveq	#0,d0
fcwp_rts
	movem.l (sp)+,d7
	rts

	end
