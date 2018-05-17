; Use and Free DDe Extension	        1990 Jochen Merz  V0.01

	section utility

	include dev8_keys_wman
	include dev8_keys_wstatus
	include dev8_keys_qdos_sms
	include dev8_keys_err
	include dev8_keys_thg
	include dev8_mac_xref

	xdef	ut_usdde		; use DATAdesign Engine
	xdef	ut_frdde		; free DATAdesign Engine

;+++
; Use DATAdesign Engine
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
ut_usdde
	movem.l a0/a2,-(sp)
	lea	dde_thg,a0
	moveq	#-1,d1
	moveq	#-1,d3
	moveq	#sms.uthg,d0		; use DDe Thing
	xjsr	gu_thjmp
	movem.l (sp)+,a0/a2
	rts

;+++
; Free DATAdesign Engine
;
; Error codes are preserved
;---
ut_frdde
	movem.l d0/a0/a2,-(sp)
	lea	dde_thg,a0
	moveq	#-1,d1
	moveq	#sms.fthg,d0		; free DDe Thing
	xjsr	gu_thjmp
	movem.l (sp)+,d0/a0/a2
	rts

dde_thg dc.w   17,'DATAdesign.engine '


	end
