; Procedure to stuff a string into the hotkey buffer	 1990	 Tony Tebby

	section hotkey

	xdef	hxt_dost

	xref	hxt_list

	xref	thp_str

	xref	hk_sstbf

	xref	gu_hkuse
	xref	gu_hkfre

	include 'dev8_ee_hk_data'
	include 'dev8_keys_thg'
	include 'dev8_mac_thg'

;+++ 
; Stuff the stuffer buffer
;
; STUFF string
;---
hxt_dost thg_extn DOST,hxt_list,thp_str
hds.reg reg	a1/d2/a3
	movem.l hds.reg,-(sp)

	move.l	4(a1),a1		 ; real pointer to string
	move.w	(a1)+,d2

	jsr	gu_hkuse		 ; use hotkey
	bne.s	hds_exit

	jsr	hk_sstbf		 ; set stuffer buffer

	jsr	gu_hkfre		 ; free the hotkey system

hds_exit
	movem.l (sp)+,hds.reg
	rts
	end
