; Buffering Initialisation

	section iou

	xdef	iob_init

	xref	iob_schd
	xref	iou_idset
	xref	iou_idlk

	include 'dev8_keys_iod'

;+++
; This routine sets up the scheduler linkage for the IO buffering routines
;
;	a3  r	pointer to IOB linkage
;---
iob_init
	lea	iob_link,a3		 ; IO linkage definition
	jsr	iou_idset		 ; set it up
	jmp	iou_idlk		 ; and link in

iob_link
	dc.l	iod_iend+iod.sqhd
	dc.l	0			 ; no io
	dc.w	0			 ; no external
	dc.w	0			 ; no polling
	dc.w	iob_schd-*		 ; scheduler
	end
