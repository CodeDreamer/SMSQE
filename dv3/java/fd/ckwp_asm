; DV3 Java Floppy Disk Check Wrte Protect  V3.00   1993   Tony Tebby
; based on
; DV3 QPC Floppy Disk Check Write Protect  V3.00   1993   Tony Tebby



	section dv3

	xdef	fd_ckwp

	include 'dev8_dv3_keys'
	include 'dev8_keys_java'

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
	move.b	ddf_dnum(a4),d7 	; drive nbr (use same reg as for other traps)
	moveq	#jt8.ckwp,d0		; check write protect now (sets D0)
	dc.w	jva.trp8
	blt.s	fcwp_rts
	sne	ddf_wprot(a4)		; set write protect
fcwp_rts
	moveq	#0,d0
	movem.l (sp)+,d7
	rts

	end
