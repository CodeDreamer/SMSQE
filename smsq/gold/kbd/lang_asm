; base area SMSQ GOLD Keyboard Tables

	section header

	xref	kbde_tab
	xref	kbdd_tab
	xref	kbdf_tab
;	 xref	 kbdn_tab
	xref	kbdk_tab
	xref	kbdes_tab

	xref	kbd_nsid
	xref	kbdf_nsid
	xref	kbdes_nsid

	xref	smsq_end

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_ldm'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_gold_kbd_abc_keys'

header_base
	dc.l	kbd_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-kbd_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	26,'SMSQ GOLD Keyboard Tables '
	dc.l	'    '
	dc.w	$200a

select
	cmp.b	#kb.abc>>8,sbl_mtype+2(a5)  ; ABC keybard?
	beq.s	sel_noload		    ; ... yes
	moveq	#sbl.load,d0
	rts
sel_noload
	moveq	#sbl.noload,d0
	rts

	section base

kbd_base
	lea	kbd_def,a1		 ; link in keyboard tables
	moveq	#sms.lldm,d0
	trap	#do.sms2
	rts

kbd_def
	dc.w	ldm.kbdt,0,44		 ; English
	dc.w	6
	dc.l	kbd_eng-*

	dc.w	ldm.kbdt,0,49		 ; German
	dc.w	6
	dc.l	kbd_deu-*

	dc.w	ldm.kbdt,0,33		 ; French
	dc.w	6
	dc.l	kbd_fra-*

;	 dc.w	 ldm.kbdt,0,47		  ; Norwegian
;	 dc.w	 6
;	 dc.l	 kbd_nor-*

	dc.w	ldm.kbdt,0,45		 ; Denmark
	dc.w	6
	dc.l	kbd_dk-*

	dc.w	ldm.kbdt,0,34		 ; Spanish
	dc.w	0
	dc.l	kbd_esp-*

kbd_eng dc.w	44
	dc.w	kbde_tab-*,kbd_nsid-*-2
kbd_deu dc.w	49
	dc.w	kbdd_tab-*,kbd_nsid-*-2
kbd_fra dc.w	33
	dc.w	kbdf_tab-*,kbdf_nsid-*-2
;kbd_nor dc.w	 47
;	 dc.w	 kbdn_tab-*,kbd_nsid-*-2
kbd_dk	dc.w	45
	dc.w	kbdk_tab-*,kbd_nsid-*-2
kbd_esp dc.w	34
	dc.w	kbdes_tab-*,kbdes_nsid-*-2

	end
