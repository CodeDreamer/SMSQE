* QDOS Trap #3 Emulation   V2.00    1986  Tony Tebby	QJUMP
*
	section qd
*
	xdef	qd_trap3
	xdef	trp3_cinvi

	xref	io_ckchn
	xref	sms_cjid
	xref	sms_rte
	xref	shd_schd
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_iod
	include dev8_keys_jcbq
	include dev8_keys_revbin
	include dev8_keys_chn
	include dev8_keys_psf
	include dev8_keys_68000
	include dev8_keys_qdos_io
	include 'dev8_smsq_smsq_base_keys'

*
reglist reg	d2-d6/a0/a2-a4
stk_d3w equ	$06
regfrme equ	$24
	dc.l	0,0,0,0 		sixteen bytes for patch

*
* set primary stack frame and locate system variable base
*
qd_trap3
	movem.l psf.reg,-(sp)		save main working registers
trap3_entry
	move.l	sms.sysb,a6		system variable base

	movem.l reglist,-(sp)		save volatiles
*
	bsr.l	io_ckchn		check channel id
	bne.s	trp3_nrel		... oops, clear rela6
	bset	#0,chn_stat(a0) 	channel in use
	bne.l	trp3_retry		... already in use
*
	clr.l	-(sp)			set not relative

	move.l	sys_jbpt(a6),d7 	find job
	ble.s	trp3_d1
	move.l	d7,a4

	move.l	(a4),a4
	bclr	#7,jcb_rela(a4) 	relative?
	beq.s	trp3_d1

	move.l	psf_a6+regfrme+4(sp),(sp) set rel a6
	add.l	(sp),a1 		and adjust
	st	chn_stat(a0)		mark status
*
trp3_d1
	moveq	#0,d7
	move.b	d0,d7			clean up key
	move.l	d7,d0
	lsr.w	#3,d7			and check for clear d1
	dc.b	$01,$3b 		btst	d0,trp3_d1t(pc,d7.w)
	dc.b	$70,trp3_d1t-*
	beq.s	trp3_set
	moveq	#0,d1
*
trp3_set
	move.b	d0,chn_actn(a0) 	save action
*
	move.l	iod_ioad(a3),a4 	io routine
	moveq	#0,d3			first entry
	move.w	stk_d3w+4(sp),d7	  real wait in d7
	jsr	(a4)
	sub.l	(sp)+,a1		restore a1
*
	moveq	#-err.nc,d7		check for nc
	add.l	d0,d7
	beq.s	trp3_redo
trp3_done
	sf	chn_stat(a0)		clear status
	cmp.b	#iof.load,chn_actn(a0)	 ; load?
	bne.s	trp3_exit		; ... no
trp3_cinvi
	move.w	d0,d0			; ... may be patched to inval cache
trp3_exit
	movem.l (sp)+,reglist		restore registers
	jmp	sms_rte


trp3_rnc
	moveq	#err.nc,d0		not complete

trp3_nrel
	move.l	sys_jbpt(a6),d7 	find job
	ble.s	trp3_exit
	move.l	d7,a4
	move.l	(a4),a4
	bclr	#7,jcb_rela(a4) 	clear relative
	bra.s	trp3_exit
*
* table of entries clearing d1
*
trp3_d1t
	dc.b	..xx..xx,........,........,........
	dc.b	........,........,........,........
	dc.b	......xx,xx......,........,........
	dc.b	........,........,........,........
	dc.b	........,........,........,........
	dc.b	........,........,........,........
	dc.b	........,........,........,........
	dc.b	........,........,........,........

*
trp3_redo
	move.l	a0,a5			save channel address
	move.l	d1,d2			and d1!!
	move.w	stk_d3w(sp),d3		wait?
	beq.s	trp3_done		 ; ... no
	blt.s	trp3_gjb		 ; ... forever
	mulu	sys_polf(a6),d3 	 ; ... adjust timer
	divu	#sys.polf,d3
	tst.w	d3
	bpl.s	trp3_gjb		 ; ... ok wait
	move.w	#$7fff,d3		 ; greatest positive
 
trp3_gjb
;;	  btst	  #sr..s,psf_sr+regfrme(a7)
;;	  bne.s   trp3_done		   ; no wait in supervisor mode
	bsr.l	sms_cjid		get current job ID
	move.l	d1,chn_jbwt(a5) 	save waiting job id
	lea	chn_stat(a5),a4
	move.l	a4,jcb_rflg(a0) 	set flag address
	move.w	d3,jcb_wait(a0) 	... and wait
	moveq	#err.nc,d0		reset d0
	move.l	d2,d1			d1
	move.l	a5,a0			and a0
	bra.s	trp3_rshd
*
trp3_retry
	tst.w	d3			wait?
	beq.s	trp3_rnc		... no, not complete
	subq.l	#2,psf_pc+regfrme(sp)	backspace program counter
trp3_rshd
	st	sys_rshd(a6)		reschedule
	movem.l (sp)+,reglist		restore registers
	bra.l	shd_schd

	end
