; DV3 QLSD Check for Open					 2018 M. Kilgus

	section dv3

	xdef	qlsd_check

	xref	hd_check

	include 'dev8_dv3_qlsd_keys'

;+++
; DV3 QLSD check for open
;
;	d1-d6	scratch
;	d7 c  p drive ID / number
;	a0 c  p channel block
;	a1/a2	scratch
;	a3 c  p pointer to linkage block
;	a4 c  u pointer to physical definition
;
;	error return 0 or error
;---
qlsd_check
	tst.b	qlsd_fake(a3)		; did we fake the name for auto-boot?
	beq.s	qlsd_do_check		; ... no
	move.l	ddl_dname+2(a3),ddl_dnuse+2(a3) ;... yes, restore real name
	clr.b	qlsd_fake(a3)		; we only do this once after boot
qlsd_do_check
	bra	hd_check

	end
