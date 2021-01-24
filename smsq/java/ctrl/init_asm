; SMSQMuator control thing	copyright (c) w. Lenerz 2017-2020

; v. 1.03 removed test & dead code
; v. 1.02 added logon, logoff
; v. 1.01 added jva_minimize


	include 'dev8_mac_basic2'
	include 'dev8_mac_proc'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include dev8_keys_thg
	include dev8_keys_err
	include dev8_keys_k
	include dev8_keys_qdos_sms


	section exten

	xdef	ctrl_init

	xdef	wl_od

	xref	gu_achpp
	xref	ctrl_thing
	xref	ctrl_tname
	xref	gu_thzlk
	xref	gu_thjmp


wl_od	dc.w	thp.ubyt+thp.opt+thp.call
	dc.w	0


ctrl_vers equ	'1.03'

ctrl_init
	lea	ctrl_tname,a0		; thing
	moveq	#sms.zthg,d0
	jsr	gu_thjmp		; zap previous thing of same name

	move.l	#$100,d0		; allocate linkage
	jsr	gu_achpp		; get mem

	move.l	a0,a1			; keep, will be ptr to thing

	lea	ctrl_thing,a0		; our Thing
	move.l	a0,th_thing(a1) 	; ... set pointer to it
	lea	th_verid(a1),a0
	move.l	#ctrl_vers,(a0)+	; and version
	lea	ctrl_tname,a3		; thing name
	move.w	(a3)+,(a0)+		; copy name length
	move.l	(a3)+,(a0)+		; copy name
	move.l	(a3)+,(a0)+
	move.l	(a3)+,(a0)+
	moveq	#sms.lthg,d0
	jsr	gu_thjmp		; link in thing now
	bne.s	ctrlout 		; oops

	lea	proctab,a1
	move.w	sb.inipr,a2
	jmp	(a2)			; link in keywords

ctrlout rts

; the macros xdef the thing name and the jumps to using it

	proc_thg    {JAVA Control}
	fun40_thg   {JAVA Control}
	fun_thg     {JAVA Control}

; these macros set up the basic procedures and functions that jump to using
; the extension thing
; when the extension thing routines are called, A1 points to the parameter list

jvasput proc	{SPUT}
jvambar proc	{MBAR}
jvaqlfp proc	{QLFP}
jvaiefp proc	{IEFP}
jvavers fun40	{VER$},8
jvawtit proc	{WTIT}
jvaexit proc	{EXIT}
jvappup proc	{PPUP}
jtmrset proc	{TMRS}
scrupdt proc	{SCRU}
jvasget proc	{SGET}
jvasync proc	{SYNC}
jvasstp proc	{SSTP}
jvamini proc	{MMIZ}
jvasndi fun	{SNDO},8
hostos	fun	{HOST},80
jtmrget fun	{TMRG},20
jbarget fun	{MBST},16

jvannam move.l	#256,d7
	bsr	fun_thg
	dc.l	'NNAM'
jvaaddr move.l	#256,d7
	bsr	fun_thg
	dc.l	'HADD'
jvalon	proc	{LON }
jvaloff proc	{LOFF}

; create the keywords
proctab
	proc_stt
	proc_ref JVASPUT
	proc_ref JVA_SPUT , jvasput
	proc_ref JVASGET
	proc_ref JVA_SGET ,jvasget
	proc_ref JVAMBAR
	proc_ref JVA_MBAR , jvambar
	proc_ref JVAQLFP
	proc_ref JVA_QLFP , jvaqlfp
	proc_ref JVAIEFP
	proc_ref JVA_IEFP , jvaiefp
	proc_ref JVA_WINDOWTITLE , jvawtit
	proc_ref EMU_WINDOWTITLE , jvawtit
	proc_ref JVA_POPUP , jvappup
	proc_ref JVA_MINIMIZE , jvamini
	proc_ref EMU_MINIMIZE , jvamini
	proc_ref JTMRSET
	proc_ref JVA_TMRSET , jtmrset
	proc_ref JVA_SCRUPDT , scrupdt
	proc_ref JVA_EXIT , jvaexit
	proc_ref EMU_EXIT , jvaexit
	proc_ref JVA_SYNCSCRAP , jvasync
	proc_ref EMU_SYNCSCRAP , jvasync
	proc_ref JVA_SYNCSCRAP_STOP , jvasstp
	proc_ref EMU_SYNCSCRAP_STOP , jvasstp
	proc_ref JVA_LOGON , jvalon
	proc_ref JVA_LOGOFF , jvaloff
	proc_end
	proc_stt
	proc_ref JVA_NETNAME$ , jvannam
	proc_ref JVA_VER$ , jvavers
	proc_ref EMU_NETNAME$ , jvannam
	proc_ref EMU_VER$ , jvavers
	proc_ref JTMRGET
	proc_ref JVA_TMRGET , jtmrget
	proc_ref JVA_MBAR_STATUS , jbarget
	proc_ref JVA_HOSTOS$ , hostos
	proc_ref EMU_HOSTOS$ , hostos
	proc_ref JVA_NETADDR$ , jvaaddr
	proc_ref EMU_NETADDR$ , jvaaddr
	proc_ref JVA_SOUNDING , jvasndi
	proc_end


	end
