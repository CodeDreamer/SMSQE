; Function to set Thing HOTKEY	 V2.00	  1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hot_thing
	xdef	hot_thg1
	xdef	hot_wake

	xref	hot_gtky
	xref	hot_itnm
	xref	hot_sets

	include 'dev8_keys_err'
	include 'dev8_ee_hk_data'

;+++
; Set up a THING HOTKEY
;
; error = HOT_THING (key,name)
;---
hot_thing
	moveq	#hki.xthg,d6		 ; set execute thing
	bra.s	ht_parm

;+++
; Set up a WAKE HOTKEY
;
; error = HOT_WAKE (key,name)
;---
hot_thg1
hot_wake
	moveq	#hki.wake,d6		 ; set pick

ht_parm
	jsr	hot_gtky		 ; first the key
	bne.s	ht_rts

	jsr	hot_itnm		 ; get item name
	beq.l	hot_sets		 ; set using utility
	blt.s	ht_rts

	moveq	#err.ipar,d0
ht_rts
	rts
	end
