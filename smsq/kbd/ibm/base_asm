; IBM SMSQ Keyboard Tables base area
;
; 2020-04-14  1.01  Added Spanish tables (MK)

	section base

	xdef	kbd_base

	xref	kbdu_tab
	xref	kbde_tab
	xref	kbdd_tab
	xref	kbdf_tab
	xref	kbdi_tab
	xref	kbdes_tab

	xref	kbd_nsid
	xref	kbdf_nsid
	xref	kbdes_nsid

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_ldm'

kbd_base
	lea	kbd_def,a1		 ; link in keyboard tables
	moveq	#sms.lldm,d0
	trap	#do.sms2
	rts

kbd_def
	dc.w	ldm.kbdt,0,1		 ; USA keyboard
	dc.w	6
	dc.l	kbd_usa-*

	dc.w	ldm.kbdt,0,44		 ; English
	dc.w	6
	dc.l	kbd_eng-*

	dc.w	ldm.kbdt,0,49		 ; German
	dc.w	6
	dc.l	kbd_deu-*

	dc.w	ldm.kbdt,0,39		 ; Italian
	dc.w	6
	dc.l	kbd_ita-*

	dc.w	ldm.kbdt,0,33		 ; French
	dc.w	6
	dc.l	kbd_fra-*

	dc.w	ldm.kbdt,0,34		 ; Spanish
	dc.w	0
	dc.l	kbd_esp-*

kbd_usa dc.w	1
	dc.w	kbdu_tab-*,kbd_nsid-*-2
kbd_eng dc.w	44
	dc.w	kbde_tab-*,kbd_nsid-*-2
kbd_deu dc.w	49
	dc.w	kbdd_tab-*,kbd_nsid-*-2
kbd_ita dc.w	39
	dc.w	kbdi_tab-*,kbd_nsid-*-2
kbd_fra dc.w	33
	dc.w	kbdf_tab-*,kbdf_nsid-*-2
kbd_esp dc.w	34
	dc.w	kbdes_tab-*,kbdes_nsid-*-2

	end
