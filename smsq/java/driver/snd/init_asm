; SOUND device initialisation	V1.00	 2014	 W. lenerz

; 1.00 2014 Jan 16 0
; BASED ON tt'S NUL device  V2.01     1989  Tony Tebby   QJUMP

	section sound

	xdef	snd_init

	xref	iou_idset
	xref	iou_idlk

	include 'dev8_keys_iod'
	include 'dev8_mac_vec'

;+++
; SOUND driver initialisation
;---
snd_init
	lea	snd_link,a3
	jsr	iou_idset		 ; setup sound linkage
	jmp	iou_idlk		 ; and link in

snd_link
	dc.l	$34+iod.sqhd

	dc.l	1<<iod..ssr+1<<iod..scn serial and name

	novec			; no servers
	novec
	novec
	vec	snd_io
	vec	snd_open
	vec	snd_close
	vec	snd_cnam
	end
