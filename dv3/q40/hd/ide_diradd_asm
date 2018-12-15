; DV3 IDE Direct Sector Access Address	V3.00	  1998     Tony Tebby
; use per drive check for lba not per device  V3.01     W. Lenerz 2017 Nov 18

	section dv3

	xdef	id_diradd		; direct sector address

	xref	id_ident
	xref	dv3_dsector

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'

;+++
; This routine converts the address on drive to cyl/head/sector format if req
;
;	d0 cr	sector number (may return error code)
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0 or ERR.MCHK
;
;---
id_diradd
	tst.b	ddf_lba(a4)		 ; cylinder head side required?
	beq.s	idd_rts 		 ; no

	assert	ddf_heads+2,ddf_scyl
	tst.l	ddf_heads(a4)		 ; sectors / sides set?
	bne.s	idd_convert		 ; ... yes, convert
	move.l	d0,-(sp)
	jsr	id_ident		 ; identify
	bne.s	idd_oops		 ; ... oops
	move.l	(sp)+,d0

idd_convert
	jsr	dv3_dsector		 ; convert sector number
	cmp.b	d0,d0
idd_rts
	rts


idd_oops
	addq.l	#4,sp
	rts

	end
