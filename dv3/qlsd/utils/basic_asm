; QLSD utils init code 1.00			   W. Lenerz + M. Kilgus 2020

	section init

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_proc'
	include 'dev8_mac_text'
	include 'dev8_mac_assert'

	xdef	init

	xref	get_linkage
init
	suba.l	a0,a0
	lea	banner(pc),a1
	move.w	ut.wtext,a2
	jsr	(a2)

	bsr	get_linkage
	bne.s	init_err

	lea	procs,a1
	move.w	sb.inipr,a2
	jmp	(a2)

init_err
	suba.l	a0,a0
	lea	txt_err(pc),a1
	move.w	ut.wtext,a2
	jsr	(a2)
	moveq	#err.nc,d0
	rts

procs
	proc_stt
	proc_def CARD_RENF
	proc_def CARD_CREATE
	proc_end
	proc_stt
	proc_def CARD_DIR$
	proc_end

banner	mkstr	{QLSD CARD utilities v1.00 WL + MK 2020\}
txt_err mkstr	{QLSD driver not found or too old\}

	end
