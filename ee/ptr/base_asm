; PTR Base

	include 'dev8_mac_text'
	include 'dev8_mac_config02'

	section base

	xref	pt_start

	lea	pt_conf,a5			 ; point to config info
	bra.s	pt_start

	section config

	xref.l	pt_vers
	mkcfhead {QPTR},{pt_vers}

	mkcfitem 'PT_Q',code,0,pt_nqimi,,,\
       {Do you wish to ignore the QIMI interface},0,N,{No},$ff,Y,{Yes}

	mkcfend

pt_conf
pt_nqimi dc.b	0
	 dc.b 0,0,0

	end
