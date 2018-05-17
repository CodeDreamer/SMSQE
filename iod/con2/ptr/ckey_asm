; Cursor key control	        1988	Tony Tebby    QJUMP
; v .1.01 get channel ID (default to 0), do not presume main sbasic channel 0 (wl)
	section proc

	xdef	ckeyon 
	xdef	ckeyoff

	xref	ut_chan0

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_con'


null	dc.b	$00
setff	dc.b	$ff
;+++
; This routine sets the cursor key control flag to on.
;---
ckeyon
	lea	null,a4 		 ; set to zero
	bra.s	ck_do
;+++
; This routine sets the cursor key control flag to off.
;---
ckeyoff
	lea	setff,a4		 ; set to set

ck_do					 ; get channel ID, default to chan 0
	jsr	ut_chan0
	moveq	#iop.slnk,d0		 ; set linkage block
	move.w	#pt_ckey,d1
	moveq	#1,d2
	moveq	#-1,d3
;	 sub.l	 a0,a0
	move.l	a4,a1
	trap	#do.io
	rts
	end
