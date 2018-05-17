; DV3 Check Hard Disk for Direct Sector Open   V3.00    1993 Tony Tebby

	section dv3

	xdef	hd_direct

	xref	hd_fpart

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; DV3 Check hard disk for direct sector open
;
;	d1 c  p sector length
;	d2 c  p density
;	d3-d6	scratch
;	d7 c  p drive ID / number
;	a0 c  p channel block
;	a1/a2	scratch
;	a3 c  p pointer to linkage block
;	a4 c  u pointer to physical definition
;
;	error return 0 or error
;---
hd_direct
hdd.reg reg	d1/d2
	movem.l hdd.reg,-(sp)		 ; used by fpart
	jsr	hdl_ckrdy(a3)		 ; check ready
	blt.s	hdd_exit

	jsr	hd_fpart		 ; find partition
	bne.s	hdd_exit

	jsr	hdl_ckwp(a3)		 ; check write protect

	moveq	#0,d0
hdd_exit
	movem.l (sp)+,hdd.reg
	rts
	end
