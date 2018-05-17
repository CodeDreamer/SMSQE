; Use and Free all kind	of standard things	 1993 Jochen Merz  V0.03

	section	utility

	include	dev8_keys_wman
	include	dev8_keys_wstatus
	include	dev8_keys_qdos_sms
	include	dev8_keys_thg

	xdef	ut_thuse		; use a	thing
	xdef	ut_thfre		; free a thing
	xdef	ut_ushok		; use Hotkey
	xdef	ut_frhok		; free Hotkey

	xref	ut_thjmp

;+++
; General Thing	Use routine.
;		Entry				Exit
;	D2.l	Extension ID
;	D3.l					Version
;	A1	Thing Name			Address	of Thing
;
;	Error returns:	ERR.NI		THING not implemented
;			ERR.NF		Thing not found
;			any returns
;---
usereg	 reg	  a0/a2
ut_thuse
	movem.l	usereg,-(sp)
	move.l	a1,a0
	moveq	#-1,d3		    ; timeout forever
	bra.s	use_all

ut_ushok
	movem.l	usereg,-(sp)
	lea	hk_thing,a0	    ; Hotkey Extension
	moveq	#127,d3		    ; timeout
use_all
	moveq	#sms.myjb,d1	    ; that's the current job
	moveq	#sms.uthg,d0	    ; use Thing
	jsr	ut_thjmp
	movem.l	(sp)+,usereg
	rts

;+++
; General Thing	free.
;
;		Entry			Exit
;	A1	Thing name		???
;
;	Error codes are	preserved
;---
freereg	reg  d0/a0/a2
ut_thfre
	movem.l	freereg,-(sp)
	move.l	a1,a0
	bra.s	free_all
ut_frhok
	movem.l	freereg,-(sp)
	lea	hk_thing,a0	    ; hotkey
free_all
	moveq	#sms.myjb,d1	    ; that's the current job
	moveq	#sms.fthg,d0	    ; free Thing
	jsr	ut_thjmp
	movem.l	(sp)+,freereg
	rts

hk_thing dc.w	6,'Hotkey'



	end
