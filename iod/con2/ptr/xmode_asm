; Standard extended colour mode

	section init

	xdef	pt_xmodec		 ; check if mode can be set
	xdef	pt_xmode		 ; set mode

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_mac_assert'

;+++
; Check and set mode
;
;	d1 c  r mode if can be set
;	a3 c  p pointer to console linkage
;	a6 c  p pointer to system variables
;---
pt_xmode
	cmp.b	#ptm.ql8,d1
	beq.s	ptxm_set
	clr.l	d1
ptxm_set
	move.b	d1,sys_qlmr(a6)
ptxm_ok
	moveq	#0,d0
	rts

pt_xmodec
	assert	ptm.ql4,0
	assert	ptm.ql8,8
	moveq	#$ffffffff-ptm.ql8,d0
	and.b	pt_dmode(a3),d0 	; is it ql mode?
	beq.s	ptxm_ok 		; ... yes can do mode

	clr.b	sys_qlmr(a6)
	moveq	#err.ipar,d0
	rts

	end
