* QDOS Trap #2 Emulation   V2.00    1986  Tony Tebby	QJUMP
*
	section qd
*
	xdef	qd_trap2
*
	xref	ioa_open
	xref	ioa_clos
	xref	ioa_frmt
	xref	ioa_delf
	xref	ioa_sown
	xref	ioa_cnam
	xref	sms_ikey
*
	include dev8_keys_sys
	include dev8_keys_jcbq
	include dev8_keys_psf
	include dev8_keys_qdos_ioa
	include 'dev8_smsq_smsq_base_keys'

	dc.l	0,0,0,0 		  16 bytes for cache patch

*
* set primary stack frame and locate system variable base
*
qd_trap2
	movem.l psf.reg,-(sp)		save main working registers
trap2_entry
	move.l	sms.sysb,a6		system variable base
*
	cmp.w	#ioa.maxk,d0		out of range?
	bhi.l	sms_ikey		... yes
*
* check for relative addressing
*
	move.l	sys_jbpt(a6),d7 	pointer to job table
	ble.s	trp2_jmp
	move.l	d7,a5
	move.l	(a5),a5 		pointer to jcb
	bclr	#7,jcb_rela(a5) 	clear relative addressing flag
	beq.s	trp2_jmp		... already clear
	add.l	psf_a6(sp),a0		make pointer relative
trp2_jmp
	move.w	d0,d7			index table of keys
	add.w	d7,d7
*
	move.w	trp2_tab(pc,d7.w),d7
	jmp	trp2_tab(pc,d7.w)	jump to it
*
trp2_tab
	dc.w	sms_ikey-trp2_tab
	dc.w	ioa_open-trp2_tab
	dc.w	ioa_clos-trp2_tab
	dc.w	ioa_frmt-trp2_tab
	dc.w	ioa_delf-trp2_tab
	dc.w	ioa_sown-trp2_tab
	dc.w	ioa_cnam-trp2_tab
	end
