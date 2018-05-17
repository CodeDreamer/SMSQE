; Java Set Display Size    (C) W. Lenerz 2010

	section init

	xdef	cn_disp_size

;	 xref	 cn_disp_clear

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_java'


	section driver

;+++
; Set size
;	d6 c  p internal mode key
;	d7 c  p ptd.ql or ptd.16	; ignored
;---
cn_disp_size
cds.reg reg	d1/d2/a4
	movem.l cds.reg,-(sp)
	move.l	4,a4			; get ptr to linkage block
	add.l	#jva_scrn,a4
	move.l	(a4)+,pt_scren(a3)	; screen address
	move.l	(a4)+,pt_scrsz(a3)	; screen size
	move.w	(a4)+,pt_scinc(a3)	; screen line increase in bytes
	move.l	(a4)+,pt_xscrs(a3)	;
	movem.l (sp)+,cds.reg
	moveq	#0,d0
	rts
	end
