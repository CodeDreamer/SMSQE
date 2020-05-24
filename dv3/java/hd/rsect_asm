; DV3 java Hard Disk Read Sector  1.00	  2014 W. Lenerz

; DV3 QXL Hard Disk Read Sector      1993     Tony Tebby


	section dv3

	xdef	hd_rdirect		; direct read sector
	xdef	hd_rsint		; internal read sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface


	include 'dev8_keys_java'


;+++
; This routine reads a sector from a hard disk at an even address
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors = 1
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
hd_rdirect
hd_rsint
	jsr	hd_hold 		; hold
	bne.s	hdr_rts

	move.l	d0,-(a7)		; will be popped up in java
	moveq	#jta.rsec,d0
	dc.w	jva.trpa		; java side handles the rest, including setting the regs on return
	addq.l	#4,a7
	jmp	hd_release		; release

hdr_rts rts

	end
