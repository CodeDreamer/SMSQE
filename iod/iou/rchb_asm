; IO Utilities Release Channel Block   V2.00   1989   Tony Tebby QJUMP

	section iou

	xdef	iou_rchb

	include 'dev8_keys_qlv'
;+++
; IO Utilities Release Channel Block
;
;	d0    p
;	a0 c  p base of block
;	all other registers preserved
;	status returned according to d0
;---
iou_rchb
ior.reg  reg	d0-d3/a0-a3
	movem.l ior.reg,-(sp)
	move.w	mem.rchp,a2
	jsr	(a2)
ioa_exit
	movem.l (sp)+,ior.reg
	tst.l	d0
	rts
	end
