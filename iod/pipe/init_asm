; Prototpe PIPE driver

	section pipe

	xdef	pipe_init

	xref.l	pipe_vers

	xref	iou_idset
	xref	iou_idlk

	include 'dev8_keys_iod'
	include 'dev8_iod_pipe_data'
	include 'dev8_mac_vec'

;+++
; PIPE initialisation
;---
pipe_init
	lea	pip_link,a3
	jsr	iou_idset		 ; setup pipe linkage
	move.l	#pil.defl,pil_defl(a3)
	jmp	iou_idlk		 ; and link in

pip_link
	dc.l	pil_end+iod.sqhd
	dc.l	1<<iod..ssr+1<<iod..sdl+1<<iod..scn serial, delete and name

	novec			 ; no servers
	novec
	novec
	vec	pipe_io 	 ; but a full set of opens
	vec	pipe_open
	vec	pipe_close

	vec	pipe_cnam	 ; name


	end
