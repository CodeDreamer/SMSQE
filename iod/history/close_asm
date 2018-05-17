; Close HISTORY Channel   V2.01     1991  Tony Tebby

	section history

	xdef	history_close

	xref	gu_rchp
	xref	iou_rchb

	include 'dev8_iod_history_data'
;+++
; HISTORY channel close operations.
;---
history_close
	move.l	a0,a4			 ; save channel block pointer
	move.l	hic_hist(a4),d0 	 ; history
	beq.s	hicl_rchn		 ; ... none, it must have been DIR

	move.l	d0,a0
	subq.w	#1,hid_nuse(a0) 	 ; one fewer users
	tst.w	hid_name(a0)		 ; named?
	bne.s	hicl_rbuf
	jsr	gu_rchp 		 ; ... no, throw it away

hicl_rbuf
	move.l	hic_buff(a4),a0
	jsr	gu_rchp 		 ; return buffer

hicl_rchn
	move.l	a4,a0
	jsr	iou_rchb		 ; return channel block
	moveq	#0,d0
	rts

	end
