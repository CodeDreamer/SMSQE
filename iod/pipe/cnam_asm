; Setup name of PIPE Channel   V2.00	 1996	Tony Tebby

	section pipe

	xdef	pipe_cnam

	xref	pipe_name

	xref	iou_chid
	xref	cv_iwdec

	include 'dev8_keys_err'
	include 'dev8_iod_pipe_data'
	include 'dev8_mac_assert'

;+++
; Set up pipe channel name in (a1)
;---
pipe_cnam
	move.w	d2,d4
	subq.w	#5,d4			 ; space for name
	ble.l	pcn_ipar		 ; none

	clr.w	(a1)+			 ; no name
	move.l	a1,a2
	move.l	pipe_name,(a2)+

	move.l	pic_qin(a0),d0		 ; input pipe
	sgt	d7
	bne.s	pcn_loc 		 ; it is
	move.l	pic_qout(a0),d0 	 ; output pipe

pcn_loc
	move.l	d0,a4
	tst.b	(a4)			 ; closed?
	bmi.s	pcn_inout		 ; ... yes
	assert	pin_nin+4,pin_nout+2,pin_qu
	tst.l	-(a4)			 ; named queue?
	beq.s	pcn_nname		 ; ... no
	blt.s	pcn_dir 		 ; directory

	lea	pin_name-pin_nin(a4),a4  ; ... the name
	move.w	(a4)+,d0		 ; the length
	beq.s	pcn_inout

	move.b	#'_',(a2)+		 ; the separator

	sub.w	d0,d4			 ; length remaining
	bge.s	pcn_copy
	add.w	d4,d0
	moveq	#0,d4
pcn_copy
	move.b	(a4)+,(a2)+
	subq.w	#1,d0
	bgt.s	pcn_copy
	bra.s	pcn_inout

pcn_nname
	move.l	a0,a4
	move.l	pic_qin(a0),d0		 ; get queue
	beq.s	pcn_chid		 ; it is us anyway
	move.l	d0,a4
	lea	-pic_qu(a4),a4
pcn_chid
	jsr	iou_chid		 ; get channel ID
	bne.s	pcn_inout
	move.b	#'<',(a2)+
	exg	a2,a0
	move.w	d0,-(a1)
	jsr	cv_iwdec		 ; convert to decimal
	addq.w	#2,a1
	exg	a2,a0
	move.b	#'>',(a2)+
	bra.s	pcn_inout

pcn_dir
	moveq	#1,d7			 ; flag directory

pcn_inout
	subq.w	#6,d4			 ; space required
	blt.s	pcn_done
	move.b	#' ',(a2)+
	move.b	#'(',(a2)+

	lea	pcn_in,a4
	tst.b	d7			 ; assume in
	blt.s	pcn_sdir		 ; it is
	addq.l	#pcn_out-pcn_in,a4
	beq.s	pcn_sdir3
	addq.l	#pcn_dirn-pcn_out,a4
pcn_sdir3
	move.b	(a4)+,(a2)+
pcn_sdir
	move.b	(a4)+,(a2)+		 ; characters
	move.b	(a4)+,(a2)+
	move.b	#')',(a2)+


pcn_done
	sub.l	a1,a2			 ; set length of name
	move.w	a2,-(a1)
	moveq	#0,d0
	rts

pcn_ipar
	moveq	#err.ipar,d0
	rts

pcn_in	dc.b	'in'
pcn_out dc.b	'out'
pcn_dirn dc.b	 'dir'


	end
