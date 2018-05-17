; Use and Free Menu Extension		 1990 Jochen Merz  V0.01

	section utility

	include dev8_keys_wman
	include dev8_keys_wstatus
	include dev8_keys_qdos_sms
	include dev8_keys_err
	include dev8_keys_thg
	include dev8_mac_xref

	xdef	ut_usmen		; use MENUS
	xdef	ut_frmen		; free MENUS
	xdef	ut_usscp		; use SCRAP
	xdef	ut_frscp		; free SCRAP

;+++
; Use Menu Extension / use Scrap Extension
;
;		Entry				Exit
;	D2.l	Extension ID
;	D3.l					Version
;	A1					Address of Thing
;
;	Error returns:	ERR.NI		THING not implemented
;			ERR.NF		Menus not found
;			any returns from Menu
;---
ut_usscp
	movem.l a0/a2,-(sp)
	lea	scrp_thg,a0
	bra.s	use_all
ut_usmen
	movem.l a0/a2,-(sp)
	lea	menu_thg,a0
use_all
	moveq	#-1,d1
	moveq	#-1,d3
	moveq	#sms.uthg,d0		; use Menus Thing
	xjsr	gu_thjmp
	movem.l (sp)+,a0/a2
	rts

;+++
; Free Menu Extension / free Scrap Extension
;
; Error codes are preserved
;---
ut_frscp
	movem.l d0/a0/a2,-(sp)
	lea	scrp_thg,a0
	bra.s	free_all
ut_frmen
	movem.l d0/a0/a2,-(sp)
	lea	menu_thg,a0
free_all
	moveq	#-1,d1
	moveq	#sms.fthg,d0		; free Menus Thing
	xjsr	gu_thjmp
	movem.l (sp)+,d0/a0/a2
	rts

menu_thg dc.w	5,'Menus '
scrp_thg dc.w  16,'Scrap Extensions'

	end
