; Extended colour mode for Java ; based on QL (Gold card)

; copyright (C) W lenerz 2012-2016

; 1.01 catch mode change for automatic mode setting if scr emu.


	section init

	xdef	pt_xmodec		 ; check if mode can be set
	xdef	pt_xmode		 ; set mode

	include 'dev8_keys_aurora'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
	include  'dev8_keys_java'
;+++
; Check and set mode
;
;	d1 c  r mode if can be set
;	a3 c  p pointer to console linkage
;	a6 c  p pointer to system variables
;---
pt_xmode
	move.b	d1,d0		 QL mode 8
	cmp.b	#ptm.ql8,d1	 ... is it?
	beq.s	ptxm_set
	move.b	#4,d0		 QL mode 4
	clr.l	d1
ptxm_set
	move.b	d1,sys_qlmr(a6)
	moveq	#1,d0
	dc.w	jva.trp5		; *** call back java with mode change

	moveq	#$ffffffe0,d0
	and.b	sys_mtyp(a6),d0 	 ; display bits
	cmp.b	#sys.maur,d0		 ; aurora ?
	bne.s	ptxm_ok

	moveq	#-1-(1<<em..m0),d0	 ; mask out mode bit
	and.b	sys_hstt(a6),d0 	 ; saved display status
	assert	sysqm..8,em..m0
	or.b	d1,d0
	move.b	d0,sys_hstt(a6)
	move.b	d0,emcr 		 ; set display status

ptxm_ok
	moveq	#0,d0
	rts

pt_xmodec
	assert	ptm.ql4,0
	assert	ptm.ql8,8
	moveq	#jt5.emumd,d0
	dc.w	jva.trp5		; try to set screen emulation mode
	moveq	#$ffffffff-ptm.ql8,d0
	and.b	pt_dmode(a3),d0 	; is it ql mode?
	beq.s	ptxm_ok 		; ... yes can do mode

	clr.b	sys_qlmr(a6)
	moveq	#err.ipar,d0
	rts

	end
