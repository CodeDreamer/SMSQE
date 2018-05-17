; DV3 PC Compatible FDC Read sector ID	   1998       Tony Tebby

	section fd

	xdef	fd_rid

	xref	fd_reset
	xref	fd_cmd_rid
	xref	fd_stat

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++ 
; Read sector ID (side 0).
; The PC compatible floppy disk controllers return the sector ID as part of the
; standard result bytes.
; If the read ID is successful, fdf_ctrk and fdf_slen are set.
;
;	d0  r	0 OK, -ve disaster, + conventional error
;
;	status return 0 ok, -ve timed out, +ve error 
;---
fd_rid
	bsr.s	fdr_do			 ; try once
	beq.s	fdr_setid		 ; ... OK
	blt.s	fdr_rts 		 ; ... no medium?
	cmp.w	#fdcs.derr,d0		 ; ... just a CRC error?
	bgt.s	fdr_exd0
	jsr	fd_reset		 ; reset the controller
	bsr.s	fdr_do			 ; ... try again
	bne.s	fdr_rts 		 ; ... give up

fdr_setid
	move.b	fdl_strk(a3),fdf_ctrk(a4) ; track
	move.b	fdl_sslf(a3),fdf_slen(a4) ; sector length flag
fdr_exd0
	tst.l	d0
fdr_rts
	rts

fdr_do
	move.b	fdl_side(a3),-(sp)	 ; save side
	clr.b	fdl_side(a3)		 ; side 0
	jsr	fd_cmd_rid		 ; read ID
	move.b	(sp)+,fdl_side(a3)
	jmp	fd_stat
	end
