* QDOS Trap #1 Emulation   V2.01   1986  Tony Tebby   QJUMP
*
	section qd
*
	xdef	qd_trap1
	xdef	qd_t1tab
*
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_smsq_smsq_base_keys'

	dc.l	0,0,0,0 		  sixteen bytes for cache patch

*
* set primary stack frame and locate system variable base
*
qd_trap1
	movem.l psf.reg,-(sp)		save main working registers
trap1_entry
	move.l	sms.sysb,a6		system variable base

	lea	sms.t1max,a5
	cmp.w	(a5)+,d0		valid operation?
	bhi.l	sms_ikey
	move.w	d0,d7
	lsl.w	#2,d7
	move.l	(a5,d7.w),a5
	jmp	(a5)
*
	page
*
	xref	sms_cjid
	xref	sms_ikey
	xref	sms_rtok
	xref	sms_rte
*
	xref	sms_crjb
	xref	sms_injb
	xref	sms_rmjb
	xref	sms_frjb
	xref	mem_frtp
	xref	qd_exv
	xref	shd_ssjb
	xref	shd_usjb
	xref	shd_acjb
	xref	shd_spjb
	xref	sms_alhp
	xref	sms_rehp
	xref	sms_arpa
	xref	sms_ampa
	xref	sms_rmpa
	xref	sms_achp
	xref	sms_rchp
	xref	sms_lexi
	xref	sms_rexi
	xref	sms_lpol
	xref	sms_rpol
	xref	sms_lshd
	xref	sms_rshd
	xref	sms_liod
	xref	sms_riod
	xref	sms_lfsd
	xref	sms_rfsd
	xref	sms_iopr
	xref	sms_cach
	xref	sms_trns
	xref	sms_lldm
	xref	sms_lenq
	xref	sms_lset
	xref	sms_pset
	xref	sms_mptr
	xref	sms_fprm
	xref	sms_schp
	xref	shd_sevt
	xref	shd_wevt
*
trp1_inf
	bsr.l	sms_cjid			get current job id in d1
	move.l	sys_vers(a6),d2 		set version
	move.l	a6,a0				sysvar base
qd_ok
	bra.l	sms_rtok			... done

qd_nop2
	moveq	#0,d2				no d2
qd_nop1
	moveq	#0,d1				no d1
qd_nop
	bra.l	sms_rtok

qd_spjb
	and.w	#$00ff,d2			increment only
	jmp	shd_spjb

qd_acjb
	and.w	#$00ff,d2			increment only
	jmp	shd_acjb

qd_cbas
	move.l	sys_jbpt(a6),sys_cbas(a6)
	bra.l	sms_rtok

qd_xtop
	move.l	psf_pc(sp),d7			operation address
	move.l	usp,a5
	move.l	(a5)+,psf_pc(sp)		return address
	move.l	a5,usp
	move.l	sp,a5				psf
	pea	sms_rte 			return
	move.l	d7,-(sp)			routine
	rts


*

qd_t1tab
	dc.w	$3e				max table size

	dc.w	trp1_inf-*		 $00
	dc.w	sms_crjb-*
	dc.w	sms_injb-*
	dc.w	qd_cbas-*
	dc.w	sms_rmjb-*		 $04
	dc.w	sms_frjb-*
	dc.w	mem_frtp-*
	dc.w	qd_exv-*
	dc.w	shd_ssjb-*		 $08
	dc.w	shd_usjb-*
	dc.w	qd_acjb-*
	dc.w	qd_spjb-*
	dc.w	sms_alhp-*		 $0c
	dc.w	sms_rehp-*
	dc.w	sms_arpa-*
	dc.w	sms_ikey-*
	dc.w	qd_nop2-*		 $10  (no mode)
	dc.w	qd_nop-*		      (no hdop)
	dc.w	qd_nop-*		      (no baud)
	dc.w	qd_nop1-*		      (no rtc)
	dc.w	qd_nop1-*		 $14  (no rtc)
	dc.w	qd_nop1-*		      (no rtc)
	dc.w	sms_ampa-*
	dc.w	sms_rmpa-*
	dc.w	sms_achp-*		 $18
	dc.w	sms_rchp-*
	dc.w	sms_lexi-*
	dc.w	sms_rexi-*
	dc.w	sms_lpol-*		 $1c
	dc.w	sms_rpol-*
	dc.w	sms_lshd-*
	dc.w	sms_rshd-*
	dc.w	sms_liod-*		 $20
	dc.w	sms_riod-*
	dc.w	sms_lfsd-*
	dc.w	sms_rfsd-*
	dc.w	sms_trns-*		 $24
	dc.w	qd_xtop-*
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*		 $28
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*		 $2c
	dc.w	sms_ikey-*
	dc.w	sms_iopr-*
	dc.w	sms_cach-*
	dc.w	sms_lldm-*		 $30
	dc.w	sms_lenq-*
	dc.w	sms_lset-*
	dc.w	sms_pset-*
	dc.w	sms_mptr-*		 $34
	dc.w	sms_fprm-*
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*
	dc.w	sms_schp-*		 $38
	dc.w	sms_ikey-*
	dc.w	shd_sevt-*
	dc.w	shd_wevt-*
	dc.w	sms_ikey-*		 $3C
	dc.w	sms_ikey-*
	dc.w	sms_ikey-*

*
	end
