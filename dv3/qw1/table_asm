; DV3 QUBIDE Format Table		V3.01	        2017 W. Lenerz
; based on
; DV3 QLWA Format Table 		V3.00	        1993 Tony Tebby

	section dv3

	xdef	qw1_table

	include 'dev8_mac_vec'
	include 'dev8_dv3_keys'

;+++
; DV3 QWA1  Format Table
;
;---
qw1_table
	vec	qlf_table	 ; next
	dc.b	ddf.qubi	 ; QWA1 format
	dc.b	$0		 ; real date
	dc.b	0,4,'QUBI',0,0,0,0

	dc.b	$ff		 ; zero length file has first sector allocated
	dc.b	0		 ; spare

	vec	dv3_iqdt	 ; internal to pathname translate
	vec	dv3_qdit	 ; pathname to internal translate
	vec	dv3_iqct	 ; internal to pathname translate

	vec	qw1_check	 ; check format
	vec	qw1_drname	 ; search for name in directory
	vec	qw1_drmake	 ; make directory
	vec	qw1_drent	 ; fetch directory entry
	vec	qw1_drefile	 ; ... for file
	novec			 ; ... selected
	vec	qw1_drsfile	 ; set directory entry for file
	vec	qw1_sawa	 ; sector allocate
	vec	qw1_slwa	 ; sector locate
	vec	qw1_stwa	 ; sector truncate
	novec			 ; scatter load
	novec			 ; scatter save
	vec	qw1_fsel	 ; format select
	vec	qw1_frmt	 ; format
	novec			 ; logical group to physical
	vec	dv3_logp	 ; logical group to physical
	novec			 ; reset medium information
	novec			 ; set medium name
	vec	qw1_umap	 ; update map

	end
