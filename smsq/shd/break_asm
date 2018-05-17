* scheduler break handling  1994  Tony Tebby  QJUMP
*
	section shd
*
	xdef	shd_break	find job to break
*
	include dev8_keys_sys
	include dev8_keys_jcbq
	include dev8_keys_iod
	include dev8_keys_chn
	include dev8_keys_con
	include dev8_keys_sbasic
	include dev8_mac_assert

shd_break
	sf	sys_dfrz(a6)		 ; unfreeze screen
	move.l	sys_jbtb(a6),a5 	 ; pointer to job table

	move.l	sys_chtb(a6),a1
	move.l	(a1),a0 		 ; channel #0
	moveq	#-iod_iolk,d0
	move.l	chn_drvr(a0),a3
	add.l	a3,d0
	cmp.l	#sd.stdln,(a0)		 ; standard ptr linkage?
	bne.s	shb_std 		 ; ... no standard QL

	bsr.s	shb_ckcon
	beq.s	shb_brchk		 ; check it is basic

	move.l	4(a1),a0		 ; channel #1
	move.l	chn_drvr(a0),a3
	bsr.s	shb_ckcon
	beq.s	shb_brchk		 ; check it is basic
	bra.s	shb_cbas		 ; bad

shb_ckcon
	move.l	pt_head-iod_iolk(a3),a0  ; top primary
	lea	-sd.extnl-sd_prwlt(a0),a0
	move.l	a0,d1
	bmi.s	shb_rts 		 ; bad address
	cmp.l	sys_ramt(a6),a0
	bhi.s	shb_rts
	cmp.l	chn_drvr(a0),a3 	 ; same linkage?
shb_rts
	rts



shb_std
	move.w	sys_chtp(a6),d2

	move.l	sys_ckyq(a6),a0
	lea	-sd_keyq(a0),a0 	 ; standard console
shb_ckloop
	cmp.l	(a1)+,a0		 ; look for current
	dbeq	d2,shb_ckloop
	bne.s	shb_job0

shb_brchk
	move.w	chn_ownr+2(a0),d0
	beq.s	shb_job0		 ; it's 0
	lsl.w	#2,d0

	move.l	(a5,d0.w),a1		 ; job jcb

shb_flchk
	lea	sb_vars(a1),a2
	cmp.l	jcb_a6(a1),a2		 ; a6 in the right place?
	bne.s	shb_cbas		 ; no, check current basic
	assert	sb_flag,-4
	cmp.l	#sb.flag,-(a2)		 ; SBASIC?
	beq.s	shb_dobrk		 ; ... yes

shb_cbas
	move.l	sys_cbas(a6),a3 	 ; current basic
	move.l	(a3),d0
	cmp.l	a1,d0			 ; tried this one?
	move.l	d0,a1
	bne.s	shb_flchk		 ; ... no

shb_job0
	move.l	(a5),a1 		 ; pointer to job 0
shb_dobrk
	clr.b	sb_break+sb_vars(a1)	 ; tell BASIC it's broken
	move.w	jcb_wait(a1),d0 	 ; wait counter
	beq.s	shb_exit		 ; ... not waiting
	addq.w	#1,d0			 ; if <-1 do not disturb
	blt.s	shb_exit
	clr.w	jcb_wait(a1)		 ; release

	move.l	jcb_rflg(a1),d0 	 ; ... any release flag address?
	ble.s	shb_exit		 ; ... no
	move.l	d0,a1
	sf	(a1)			 ; clear release flag
shb_exit
	rts				 ; return
	end
