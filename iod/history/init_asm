; Prototpe HISTORY driver

	section history

	xdef	history_init

	xref.l	history_vers

	xref	gu_achpp
	xref	iou_idset
	xref	iou_idlk

	include 'dev8_keys_iod'
	include 'dev8_iod_history_data'
	include 'dev8_mac_vec'

;+++
; HISTORY initialisation
;---
history_init
	lea	his_link,a3
	jsr	iou_idset		; setup history linkage
	move.l	#hil.defl,hil_defl(a3)
	jmp	iou_idlk		; and link in

his_link
	dc.l	hil_end+iod.sqhd
	dc.l	1<<iod..ssr+1<<iod..sdl+1<<iod..scn serial, delete and name

	novec		       ; no servers
	novec
	novec
	vec	history_io     ; but a full set of opens
	vec	history_open
	vec	history_close

	vec	history_cnam   ; name

	end
