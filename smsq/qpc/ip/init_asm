; IP initialisation  V1.00				 2004	Marcel Kilgus

	section ip

	xdef	ip_init

	xref.l	ip_vers

	xref	ip_io
	xref	ip_open
	xref	ip_close
	xref	ip_cnam
	xref	iou_idset
	xref	iou_idlk

	include 'dev8_mac_vec'
	include 'dev8_keys_iod'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_qpc_ip_data'

;+++
; Initialise IP devices.
;
;	status return standard
;---
ip_init
	dc.w	qpc.ipcla		 ; close all previous sockets

	lea	ip_link,a3
	jsr	iou_idset		 ; setup IP linkage
	jmp	iou_idlk		 ; and link in

ip_link
	dc.l	ipd_end+iod.sqhd
	dc.l	1<<iod..ssr+1<<iod..scn  ; serial and name

	novec			 ; no servers
	novec
	novec
	vec	ip_io		 ; but a full set of opens
	vec	ip_open
	vec	ip_close

	vec	ip_cnam 	 ; name

	end
