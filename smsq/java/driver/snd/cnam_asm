; Setup name of SOUND Channel  V1.00 (c) w. lenerz

; v. 1.00  2014 Jan 16	copyright
; based on Setup name of NUL Channel   V2.00	 1996	Tony Tebby


	section sound

	xdef	snd_cnam


	include 'dev8_keys_err'

;+++
; Set up sound channel name in (a1)
;---
snd_cnam
	move.w	d2,d4
	subq.w	#8,d4			; space for name
	ble.s	ncn_ipar		; none

	move.w	#6,(a1)
	move.l	#'SOUN',2(a1)		; put in name
	move.b	#'D',6(a1)
	move.b	$20(a0),d1		; and device type
	add.b	#'0',d1
	move.b	d1,7(a1)
	moveq	#0,d0
	rts

ncn_ipar
	moveq	#err.ipar,d0
	rts

	end
