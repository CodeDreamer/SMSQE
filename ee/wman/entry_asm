; Window manager entry point	V1.02	  1986  Tony Tebby  QJUMP
;
; 2002-11-13  1.01  Added wm_setpal, wm_getpal and wm_trap3 entries (MK)
; 2003-01-28  1.02  Added wm_opw and wm_ssclr
; 2005-10-10  1.03  Added wm_cpspr (MK)
; 2020-08-16  1.04  Added wm_drndx (AH)

	section wman

	xdef	wm_entry

	xref.l	wm_vers

	xref	wm_setup
	xref	wm_smenu
	xref	wm_prpos
	xref	wm_pulld
	xref	wm_unset
	xref	wm_wrset
	xref	wm_wdraw
	xref	wm_mdraw
	xref	wm_index
	xref	wm_swdef
	xref	wm_ldraw
	xref	wm_rptr
	xref	wm_mhit
	xref	wm_pansc
	xref	wm_idraw
	xref	wm_chwin
	xref	wm_drbdr
	xref	wm_msect
	xref	wm_stlob
	xref	wm_stiob
	xref	wm_fsize
	xref	wm_swinf
	xref	wm_swlit
	xref	wm_swapp
	xref	wm_swsec
	xref	wm_rname
	xref	wm_ename
	xref	wm_upbar
	xref	wm_erstr
	xref	wm_rptrt
	xref	wm_setsp
	xref	wm_getsp
	xref	wmc_trap3
	xref	wm_opw
	xref	wm_ssclr
	xref	wm_jbpal
	xref	wm_cpspr
	xref	wm_drndx			;(AH)

	section version
	dc.w	20,'Window Manager V'
	dc.l	wm_vers
	dc.b	$20,$0a
	section wman
wm_entry
	dc.l	wm_vers 		$00
	bra.l	wm_setup		$04
	bra.l	wm_smenu		$08
	bra.l	wm_prpos		$0c
	bra.l	wm_pulld		$10
	bra.l	wm_unset		$14
	bra.l	wm_wrset		$18
	bra.l	wm_wdraw		$1c
	bra.l	wm_mdraw		$20
	bra.l	wm_index		$24
	bra.l	wm_swdef		$28
	bra.l	wm_ldraw		$2c
	bra.l	wm_rptr 		$30
	bra.l	wm_mhit 		$34
	bra.l	wm_pansc		$38
	bra.l	wm_idraw		$3c
	bra.l	wm_chwin		$40
	bra.l	wm_drbdr		$44
	bra.l	wm_msect		$48
	bra.l	wm_stlob		$4c
	bra.l	wm_stiob		$50
	bra.l	wm_fsize		$54
	bra.l	wm_swinf		$58
	bra.l	wm_swlit		$5c
	bra.l	wm_swapp		$60
	bra.l	wm_swsec		$64
	bra.l	wm_rname		$68
	bra.l	wm_ename		$6c
	bra.l	wm_upbar		$70
	bra.l	wm_erstr		$74
	bra.l	wm_rptrt		$78
	bra.l	wm_setsp		$7c
	bra.l	wm_getsp		$80
	bra.l	wmc_trap3		$84
	bra.l	wm_opw			$88
	bra.l	wm_ssclr		$8c
	bra.l	wm_jbpal		$90
	bra.l	wm_cpspr		$94
	bra.l	wm_drndx		$98	;(AH)

we_rts
	rts


	end
