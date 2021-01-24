; QPC hardware specific AY sound part  2020 by Marcel Kilgus
; v. 1.00

	section qsound_hw

	xdef	ay_hw_setup
	xdef	ay_hw_wrreg
	xdef	ay_hw_wrall
	xdef	ay_hw_type
	xdef	ay_hw_freq
	xdef	ay_hw_stereo
	xdef	ay_hw_volume

	include 'dev8_keys_qsound'
	include 'dev8_iod_qsound_keys'
	include 'dev8_smsq_qpc_keys'

;+++
; AY hardware setup
;
;	a3 c p	QSound data block
;---
ay_hw_setup
	move.b	#2,qs_chip_count(a3)		; Two chips
	move.b	#6,qs_chan_count(a3)		; = 6 channels
	move.l	#1773400,qs_chip_freq(a3)	; AY frequency
	move.b	#ayct.ay,qs_chip_type(a3)	; AY chip
	move.b	#ayst.abc,qs_stereo(a3) 	; ABC
	move.b	#255,qs_volume(a3)
	rts

;+++
; AY write single register
;
;	d0.b cr   Chip number / Error return
;	d1.b c p  Register value
;	d2.b c p  Register number
;	a3   c p  QSound data block
;	a5   c	  Chip register cache
;---
ay_hw_wrreg
	move.w	d1,-(sp)
	move.b	d0,d1
	dc.w	qpc.ayreg+5	; chip in d1, registers in a5
	move.w	(sp)+,d1
	rts

;+++
; AY write all registers
;
;	d0    r   Error return
;	d1	s
;	d2.b c	s Chip number
;	a3   c p  QSound data block
;	a5   c	s Chip register cache
;---
ay_hw_wrall
	move.l	d2,d1
	dc.w	qpc.ayreg+5	; chip in d1, registers in a5
	rts

;+++
; AY set chip type
;
;	d0    r   Error return
;	d1.b cr   Chip type
;	d2     s
;---
ay_hw_type
	move.b	d1,d2
	dc.w	qpc.aychp	; type in d2
	rts

;+++
; AY set chip frequency
;
;	d0    r   Error return
;	d1.l cr   Chip frequency
;	d2     s
;---
ay_hw_freq
	move.l	d1,d2
	dc.w	qpc.ayfrq	; frequency in d2
	rts

;+++
; AY set stereo mode
;
;	d0    r   Error return
;	d1.b cr   Stereo mode
;	d2     s
;---
ay_hw_stereo
	move.l	d1,d2
	dc.w	qpc.ayste	; stereo mode in d2
	rts

;+++
; AY set stereo mode
;
;	d0    r   Error return
;	d1.b cr   Volume mode
;	d2     s
;---
ay_hw_volume
	move.l	d1,d2
	lsl.w	#8,d2
	move.b	d1,d2
	dc.w	qpc.ayste	; volume in d2
	rts

	end
