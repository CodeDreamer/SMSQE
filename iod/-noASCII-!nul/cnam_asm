; Setup name of NUL Channel   V2.00     1996  Tony Tebby

	section nul

	xdef	nul_cnam

	xref	nul_name
	xref	nul_parm

	include 'dev8_keys_err'
	include 'dev8_iod_nul_data'
	include 'dev8_mac_assert'

;+++
; Set up nul channel name in (a1)
;---
nul_cnam
	move.w	d2,d4
	subq.w	#5,d4			 ; space for name
	ble.s	ncn_ipar		 ; none

	clr.w	(a1)+			 ; no name
	move.l	a1,a2
	move.l	nul_name,(a2)+
	subq.l	#1,a2

	move.w	nlc_parm(a0),d0 	 ; parameter
	beq.s	ncn_done		 ; ... none
	move.b	nul_parm-1(pc,d0.w),(a2)+ ; set it

ncn_done
	sub.l	a1,a2			 ; set length of name
	move.w	a2,-(a1)
	moveq	#0,d0
	rts

ncn_ipar
	moveq	#err.ipar,d0
	rts

	end
