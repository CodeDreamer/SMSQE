; Config section for recent thing	V1.00 (c) W. Lenerz 2016

	section config

	include 'dev8_mac_config02'

	xdef	rcfg_nbr	; number of files per list
	xdef	rcfg_file	; file to save/load
	xdef	rcfg_use	; use thing at all?

	xref	rcnt_vers

rcfg_nbr dc.b	20		; number of files
rcfg_use dc.b	0		; use thing at all? (preset = no)

filenm	dc.w	41		; max size
rcfg_file
	dc.w	fl2-*-2
	dc.b	'win1_recent_list'   ; preset
fl2
	ds.w	14		; file name


fl_desc dc.w	fd2-*-2
	dc.b	'File to save lists to/load lists from'
fd2


	mkcfhead {Recent Thing},{rcnt_vers}

	
	mkcfitem 'RCT1',code,'U',rcfg_use,,,\
	{Use Recent thing?},1,Y,{Yes},0,N,{No}
			
	mkcfitem 'RCT2',byte,'M',rcfg_nbr,,,\
	{Max nbr of items in list},1,$ff

	mkcfitem 'RCT3',string,'F',filenm,,,\
	{File to save lists to/load lists from},cfs.file

	mkcfend

	end
