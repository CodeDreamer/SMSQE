; DV3 IDE Read drive identification    V3.00    1998

	section dv3

	xdef	id_ident

	xref	id_rsecid		 ; special read ID sector

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_keys_err'

;+++
; Read Drive identification into buffer and set physical characteristics
;
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
id_ident
hdi.reg reg	d1/d2/a1
	movem.l hdi.reg,-(sp)
	lea	hdl_buff(a3),a1

	jsr	id_rsecid
	bne.s	hdi_mchk

	move.w	idei_lsect+hdl_buff(a3),d0
	move.w	d0,ddf_strk(a4) 	 ; number of sectors per track
	move.w	idei_lhead+hdl_buff(a3),d1
	move.w	d1,ddf_heads(a4)	 ; number of heads
	mulu	d1,d0			 ; number of sectors per cylinder
	move.w	d0,ddf_scyl(a4)
	move.w	d0,ddf_asect(a4)	 ; set also
	move.w	idei_lcyl+hdl_buff(a3),d0
	move.l	d0,ddf_atotal(a4)	 ; number of cyls is stored in atotal

	moveq	#0,d0

hdi_exit
	movem.l (sp)+,hdi.reg
	rts

hdi_mchk
	moveq	#err.mchk,d0
	bra.s	hdi_exit

	end
