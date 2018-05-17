; Gold / Supergold card determine display hardware type  1997	Tony Tebby

	section init

	xdef	gl_disptype

	include 'dev8_keys_aurora'
	include 'dev8_keys_sys'

;+++
; Determines the display hardware type
;
;	d6 c  u $x0000x00+machine  +  $00   QL
;				      $C0   LCD
;				      $A0   Aurora
;	status returned 0
;---
gl_disptype
	cmp.b	#sys.msgld,d6		; Super gold card?
	bne.s	gld_done		; ... no
	move.l	#vram_base,a0		; Check for Nastas hardware. It just
	move.l	(a0),d0
	move.w	#vram.test,(a0) 	; tests for ram at the place we
	clr.w	$20004			; expect the vram to be. If anyone
	cmpi.w	#vram.test,(a0) 	; else builds a card with ram here,
	bne.s	gld_rest		; we have a problem. Ho hum....
	add.b	#sys.maur,d6		; "Aurora" flag
	move.l	d0,(a0)
	add.l	#vram_lcd,a0		; check beyond end of LCD card ram
	move.l	(a0),d0
	move.w	#vram.test,(a0)
	clr.w	$20004
	cmpi.w	#vram.test,(a0)
	beq.s	gld_rest
	add.b	#sys.mqlc-sys.maur,d6	; set QL LCD
gld_rest
	move.l	d0,(a0)
gld_done
	moveq	#0,d0
	rts

	end
