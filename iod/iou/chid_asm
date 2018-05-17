; IO Utilities Find Channel ID	 V2.00	 1995	 Tony Tebby

	section iou

	xdef	iou_chid

	include 'dev8_keys_sys'
	include 'dev8_keys_chn'

;+++
; IO Utilities Find Channel ID
;
;	d0  r	channel ID or -1
;	a4 c  p base of channel block
;	all other registers preserved
;	status negative if channel id not found
;---
iou_chid
ici.reg  reg	a1
	move.l	a1,-(sp)

	move.l	chn_tag(a4),d0		 ; set tag in top bit

	move.l	sys_chtb(a6),a1
	move.w	sys_chtp(a6),d0
ici_loop
	cmp.l	(a1)+,a4		 ; there yet?
	dbeq	d0,ici_loop
	bne.s	ici_nf			 ; not found

	neg.w	d0
	add.w	sys_chtp(a6),d0

ici_exit
	move.l	(sp)+,a1
	rts

ici_nf
	moveq	#-1,d0
	bra.s	ici_exit
	end
