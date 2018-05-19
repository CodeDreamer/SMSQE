; SBSEXT_TK2_NET_INIT - TK2 network part initialisation    2018 Marcel Kilgus

	section tk2

	xdef	init

	xref	nd_init
	xref.l	tk2_vers

	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_proc'

init
	suba.l	a0,a0
	lea	banner(pc),a1
	move.w	ut.wtext,a2
	jsr	(a2)

	moveq	#sms.info,d0
	trap	#1
	movea.l a0,a4			; SysVar

	jsr	nd_init
	lea	net_procs,a1
	move.w	sb.inipr,a2
	jmp	(a2)

net_procs
	proc_stt
	proc_def FSERVE 		; file server
	proc_def NFS_USE
	proc_end

	proc_stt
	proc_end

banner	dc.w	39
	dc.b	'Toolkit II v'
	dc.l	tk2_vers
	dc.b	'network QJUMP/M.Kilgus',10,0

	end
