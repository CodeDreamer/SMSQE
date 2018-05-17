; Base area SMSQ Window Manager V1.02				 Tony Tebby
;							   2002  Marcel Kilgus
;
; 2002-11-13  1.01  Initialises system palette (MK)
; 2003-01-25  1.02  Moved SMSQ/E specific code here (MK)
; 2003-05-27  1.03  Now uses new WMAN data handling (MK)

	section header

	xref.l	wm_vers
	xref	wm_entry
	xref	wm_initdata
	xref	wm_initp
	xref	smsq_end

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'

header_base
	dc.l	smsq_wman-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-smsq_wman	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; no select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	20,'SMSQ Window Manager '
	dc.l	wm_vers
	dc.w	$200a

	section base

smsq_wman
	bsr	wm_initdata		 ; initialise WMAN data area
	bsr	wm_initp		 ; initialise basic keywords

	moveq	#sms.xtop,d0
	trap	#do.sms2
	move.l	sys_clnk(a6),a5 	 ; console linkage
	pea	wm_entry		 ; window manager entry vector
	move.l	(sp)+,pt_wman(a5)	 ; in pointer location
	moveq	#0,d0
	rts

	end
