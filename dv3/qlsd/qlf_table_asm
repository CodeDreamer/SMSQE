; DV3 QL Format Table		     V3.00	     1992 Tony Tebby

	section dv3

	xdef	qlf_table
	xdef	qlf_tbwa

	include 'dev8_mac_vec'
	include 'dev8_dv3_keys'

;+++
; DV3 QL5A/B Format Table
;
;---
qlf_table

;+++
; DV3 QLWA Format Table
;
;---
qlf_tbwa
	novec			 ; next
	dc.b	ddf.qdos	 ; QL5A / QL5B / QLWA format
	dc.b	0		 ; real date
	dc.b	0,4,'QDOS',0,0,0,0

	dc.b	$ff		 ; zero length file has first sector allocated
	dc.b	0		 ; spare

	vec	dv3_iqdt	 ; internal to pathname translate
	vec	dv3_qdit	 ; pathname to internal translate
	vec	dv3_iqct	 ; internal to checking translate

	vec	qlf_check	 ; check format
	vec	qlf_drname	 ; search for name in directory
	vec	qlf_drmake	 ; make directory
	vec	qlf_drent	 ; fetch directory entry
	vec	qlf_drefile	 ; ... for file
	novec			 ; ... selected
	vec	qlf_drsfile	 ; set directory entry for file
	vec	qlf_sawa	 ; sector allocate
	vec	qlf_slwa	 ; sector locate
	vec	qlf_stwa	 ; sector truncate
	vec	qlf_ldwa	 ; scatter load
	novec			 ; scatter save
	vec	qlf_fsel	 ; format select
	vec	qlf_ftwa	 ; format
	novec			 ; logical group to physical
	vec	dv3_logp	 ; logical group to physical
	novec			 ; reset medium information
	novec			 ; set medium name
	vec	qlf_umwa	 ; update map

	end
