; Q68 extended colour mode	1.00 based on TT's Q40 code

	section init

	xdef	pt_xmodec		 ; check if mode can be set
	xdef	pt_xmode		 ; set mode

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_q68'
	include 'dev8_mac_assert'

;+++
; Check and set mode 4 or 8
;
;	d1 c  r mode if can be set
;	a3 c  p pointer to console linkage
;	a6 c  p pointer to system variables
;---
pt_xmode
	move.b	d1,d0			; QL mode 8
	cmp.b	#ptm.ql8,d1		; ... is it?
	beq.s	ptxm_set
	move.b	#4,d0			; QL mode 4
	clr.l	d1
ptxm_set
	move.b	d1,sys_qlmr(a6)
	assert	q68.d4,1
	assert	q68.d8,0
	seq	d0
	neg	d0
	move.b	d0,q68_dmode		; set display
ptxm_ok
	moveq	#0,d0
	rts

pt_xmodec				; this is called from the MODE command
	assert	ptm.ql4,0
	assert	ptm.ql8,8
	move.b	q68_dmode,d0
	subq.b	#1,d0
	ble.s	ptxm_ok

;	 moveq	 #$ffffffff-ptm.ql8,d0
;	 and.b	 pt_dmode(a3),d0	 ; is it ql mode?
;	 beq.s	 ptxm_ok		 ; ... yes can do mode

;	clr.b	sys_qlmr(a6)
	moveq	#err.ipar,d0
	rts

	end
