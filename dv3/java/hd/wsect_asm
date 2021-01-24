; DV3 java Hard Disk Write Sector  1.00     W. Lenerz 2020
; based on
; DV3 QXL Hard Disk Read Sector      1993     Tony Tebby


	section dv3

	xdef	hd_wdirect		; direct write sector
	xdef	hd_wsint		; internal write sector

	xref	hd_hold 		; reserve the interface
	xref	hd_release		; release the interface


	include 'dev8_keys_java'


;+++
; This routine writes a sector to a hard disk from an even address
;
;	d0 cr	cylinder + side + sector / error code
;	d2 c  p number of sectors = 1
;	d7 c  p drive ID / number
;	a1 c  p address to read from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
hd_wdirect
hd_wsint
	jsr	hd_hold 		; hold
	bne.s	hdr_rts 		; ooops

	move.l	d0,-(a7)
	moveq	#jta.wsec,d0
	dc.w	jva.trpa		; java side handles the rest, including setting the regs on return
	addq.l	#4,a7
	jmp	hd_release		; release (and set cc accoding to d0)

hdr_rts rts

	end
