; DV3 QLSD &  FAT16 Find Partition     V1.00    2017 W. Lenerz
;
	section dv3

	xdef	hd_fpart
		       
	xref	hfp_sdhc
	xref	hfp_fat

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;-------------------------------------------
; Find the "partition" a drives is on.
; This is split into separate files as the different drivers require
; different code.
;-------------------------------------------

hd_fpart
	cmp.l	#'FAT0',ddl_dname+2(a3) ; FAT device?
	bne	hfp_sdhc		; no, so it's win or qub
	bra	hfp_fat 		; FAT
	end
