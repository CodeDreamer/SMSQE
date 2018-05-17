; DV3 MSDOS Format Table		V3.00	        1993 Tony Tebby

	section dv3

	xdef	msd_table
	xdef	msd_tab4

	include 'dev8_mac_vec'
	include 'dev8_dv3_keys'

;+++
; DV3 MSDOS  Format Table 3 nibble FAT
;
;---
msd_table
	novec			 ; next
	dc.b	ddf.msdos	 ; MSDOS format
	dc.b	$ff		 ; MSDOS date
	dc.b	0,5,'MSDOS',0,0,0

	dc.b	0		 ; zero length file has no sector allocated
	dc.b	0		 ; spare

	vec	dv3_imdt	 ; internal to pathname translate
	vec	dv3_mdit	 ; pathname to internal translate
	vec	dv3_imdt	 ; internal to pathname translate

	vec	msd_check	 ; check format
	vec	msd_drname	 ; search for name in directory
	vec	msd_drmake	 ; make directory
	vec	msd_drent	 ; fetch directory entry
	vec	msd_drefile	 ; ... for file
	novec			 ; ... selected
	vec	msd_drsfile	 ; set directory entry for file
	vec	msd_sal3	 ; sector allocate
	vec	msd_slc3	 ; sector locate
	vec	msd_str3	 ; sector truncate
	novec			 ; scatter load
	novec			 ; scatter save
	vec	msd_fsel	 ; format select
	vec	msd_frmt	 ; format
	novec			 ; logical group to physical
	vec	dv3_logp	 ; logical group to physical
	novec			 ; reset medium information
	novec			 ; set medium name
	vec	msd_umap	 ; update map

msd_tab4
	novec			 ; next
	dc.b	ddf.msdos	 ; MSDOS format
	dc.b	$ff		 ; MSDOS date
	dc.b	0,5,'MSDOS',0,0,0
	dc.b	0		 ; zero length file has no sector allocated
	dc.b	0		 ; spare

	vec	dv3_imdt	 ; internal to pathname translate
	vec	dv3_mdit	 ; pathname to internal translate
	vec	dv3_imdt	 ; internal to pathname translate

	vec	msd_check	 ; check format
	vec	msd_drname	 ; search for name in directory
	vec	msd_drmake	 ; make directory
	vec	msd_drent	 ; fetch directory entry
	vec	msd_drefile	 ; ... for file
	novec			 ; ... selected
	vec	msd_drsfile	 ; set directory entry for file
	vec	msd_sal4	 ; sector allocate
	vec	msd_slc4	 ; sector locate
	vec	msd_str4	 ; sector truncate
	novec			 ; scatter load
	novec			 ; scatter save
	vec	msd_fsel	 ; format select
	vec	msd_frmt	 ; format
	novec			 ; logical group to physical
	vec	dv3_logp	 ; logical group to physical
	novec			 ; reset medium information
	novec			 ; set medium name
	vec	msd_umap	 ; update map
	end
