; QLSD init code 1.03				 W. Lenerz + M. Kilgus 2018
;
; 2018-29-05  1.01  Output the recognised hardware revison (MK)
; 2020-04-05  1.02  Removed CARD_INIT (wl)
; 2020-05-08  1.03  Added network driver call (MK)

	section init

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_mac_proc'
	include 'dev8_mac_text'
	include 'dev8_mac_assert'

	xdef	init

	xref	win_init
	xref	dv3_addfd
	xref	qlf_table
	xref	inicrd
	xref	nd_init

inirgs	reg	d7/a0/a3
ini.d7	equ	0
ini.a0	equ	4

; d7: 0 = ROM start, 1 = RESPR start
init	movem.l inirgs,-(a7)
	lea	qlf_table,a1
	jsr	dv3_addfd		; return d7 = "thing"
	jsr	win_init

	moveq	#sms.info,d0
	trap	#1
	move.l	a0,qlsd_sysvars(a3)	; save so we don't need sms.info again

	assert	qlsd.smsq,0		; so we can reuse d0=0
	cmp.l	#sysid.sq,(a0)		; SMSQ/E
	beq.s	set_os

	moveq	#qlsd.minerva,d0
	move.l	sys_chtt(a0),a1 	; Minerva extension block lives here
	cmp.l	#'JSL1',$46(a1) 	; Magic
	beq.s	set_os			; Yes, Minerva

	moveq	#qlsd.qdos,d0		; Otherwise we assume QDOS
set_os	move.b	d0,qlsd_os(a3)

	moveq	#1,d5			; init drive 1
	jsr	inicrd
	move.l	d0,d7			; remember error for later

; Output hardware version information
	move.l	ini.a0(sp),a0		; channel ID
	move.w	ut.wtext,a2
	lea	txt.hw(pc),a1		; "QLSD"
	jsr	(a2)

	lea	if_base,a1
	move.b	if_enable(a1),d1	; Reference ROM byte
	move.b	if_version(a1),d0	; Read hardware version register
	lea	txt.v1(pc),a1		; "v1"
	cmp.b	d0,d1			; v1 has ROM at if_version...
	beq.s	write_hw		; ... so it should match if_enable
	andi.b	#if.ver_mask+if.io_mode,d0

	lea	txt.sw(pc),a1		; "v2 (switch)"
	cmp.b	#if.ver_v2+if.io_mode,d0
	beq.s	write_hw

	lea	txt.sp(pc),a1		; "v2 (SPI)"
	cmp.b	#if.ver_v2,d0
	beq.s	write_hw

	lea	txt.un(pc),a1		; "unknown"
write_hw
	jsr	(a2)			; HW revision

	lea	txt.c1(pc),a1		; "card 1"
	jsr	(a2)

	lea	txt.nf(pc),a1		; "not found"
	tst.l	d7
	bne.s	write_msg		; no card, don't mess with driver name

	lea	txt.in(pc),a1		; "initialised"
	tst.l	ini.d7(sp)		; ROM start?
	bne.s	write_msg		; ... no, don't fake MDV name

	assert	qlsd.smsq,0
	tst.b	qlsd_os(a3)		; SMSQ?
	beq.s	write_msg		; ... yes, don't fake MDV name

	move.l	#'MDV0',ddl_dnuse+2(a3) ; Fake MDV name so we can auto-boot
	st	qlsd_fake(a3)		; Remember that we faked it
write_msg
	jsr	(a2)

	move.l	qlsd_sysvars(a3),a4	; nd_init needs sysVar in a4
	bsr	nd_init 		; RESPR version will have empty stub

	movem.l (a7)+,inirgs
	lea	procs,a1
	move.w	sb.inipr,a2
	jmp	(a2)
procs
	proc_stt
	proc_def WIN_DRIVE
	proc_def WIN_USE
	proc_def WIN_CHECK
	proc_end
	proc_stt
	proc_end

txt.hw	mkstr	{H/W }
txt.v1	mkstr	{v1}
txt.sw	mkstr	{v2 (Sw)}
txt.sp	mkstr	{v2 (SPI)}
txt.un	mkstr	{unknown}
txt.c1	mkstr	{: Card 1 }
txt.nf	mkstr	{not found\}
txt.in	mkstr	{initialised\}

	end
