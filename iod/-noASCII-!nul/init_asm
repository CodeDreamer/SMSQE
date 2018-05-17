; NUL device initialisation  V2.01     1989  Tony Tebby   QJUMP

	section nul

	xdef	nul_init

	xref.l	nul_vers

	xref	iou_idset
	xref	iou_idlk

	include 'dev8_keys_iod'
	include 'dev8_iod_nul_data'
	include 'dev8_mac_vec'

;+++
; NUL driver initialisation
;---
nul_init
	lea	nul_link,a3
	jsr	iou_idset		 ; setup pipe linkage
	jmp	iou_idlk		 ; and link in

nul_link
	dc.l	nld_end+iod.sqhd
	dc.l	1<<iod..ssr+1<<iod..scn serial and name

	novec			 ; no servers
	novec
	novec
	vec	nul_io		 ; but a full set of opens
	vec	nul_open
	vec	nul_close

	vec	nul_cnam	; name



	end
