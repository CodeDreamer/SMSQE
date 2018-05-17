; Setup name of HISTORY Channel   V2.00     1996  Tony Tebby

	section history

	xdef	history_cnam

	xref	history_name

	include 'dev8_keys_err'
	include 'dev8_iod_history_data'

;+++
; Set up history channel name in (a1)
;---
history_cnam
	move.w	d2,d4
	subq.w	#8,d4			 ; space for name
	ble.s	hcn_ipar		 ; none

	clr.w	(a1)+			 ; no name
	move.l	a1,a2
	move.l	history_name,(a2)+
	move.l	history_name+4,(a2)+
	subq.l	#1,a2

	move.l	hic_hist(a0),a4 	 ; the history
	lea	hid_name(a4),a4 	 ; the name
	move.w	(a4)+,d0		 ; the length
	beq.s	hcn_done

	move.b	#'_',(a2)+		 ; the separator

	cmp.w	d4,d0			 ; max length
	ble.s	hcn_copy
	move.w	d4,d0
hcn_copy
	move.b	(a4)+,(a2)+
	subq.w	#1,d0
	bgt.s	hcn_copy


hcn_done
	sub.l	a1,a2			 ; set length of name
	move.w	a2,-(a1)
	moveq	#0,d0
	rts

hcn_ipar
	moveq	#err.ipar,d0
	rts

	end
