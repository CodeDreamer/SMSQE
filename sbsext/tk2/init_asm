; SBSEXT_TK2_INIT - TK2 initialisation			    2017 Marcel Kilgus

	section tk2

	xdef	init

	xref	tk2_ext_proc
	xref	tk2_procs
	xref	ut_procdef
	xref	gu_achpp
	xref	mdv_init
	xref	ak_init
	xref	nd_init

	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'

init
	moveq	#sms.info,d0
	trap	#1
	movea.l a0,a4			; SysVar

	bsr.s	set_defaults
	jsr	ak_init 		; Needs SysVars in a4
	jsr	nd_init 		; Needs SysVars in a4
	lea	tk2_ext_proc,a1
	jsr	ut_procdef
	jsr	mdv_init

	lea	tk2_procs,a1
	move.w	sb.inipr,a2
	jmp	(a2)

set_defaults
	moveq	#sms.achp,d0
	moveq	#36*3,d1
	moveq	#0,d2
	trap	#1

	lea	sys_prgd(a4),a2 	; And set the pointers to the defaults
	lea	prog_ddd(pc),a1
	bsr.s	set_def 		; Program default
	lea	data_ddd(pc),a1
	bsr.s	set_def 		; Data default
	lea	dest_ddd(pc),a1 	; Destination default
set_def
	move.l	a0,(a2)+		; Default address
	moveq	#36/4-1,d0		; Copy 36 bytes
set_dloop
	move.l	(a1)+,(a0)+
	dbra	d0,set_dloop
	rts

prog_ddd dc.w	5,'flp1_'
data_ddd dc.w	5,'flp1_'
dest_ddd dc.w	3,'par'

	end
