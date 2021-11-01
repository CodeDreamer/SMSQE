; QPC_PROCS_BASE - QPC Procedures Base	V2.04	 2021	Marcel Kilgus
;
; 2021-08-02  2.04  Added QPC_FLASHBUTTON and QPC_HASFOCUS (MK)

	section header

	xref	smsq_end
	xref	qpc_defs
	xref	gu_thini

header_base
	dc.l	sb_initp-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-sb_initp	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; one level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	22,'QPC SBASIC Procedures '
	dc.l	'    '
	dc.w	$200a

	section init

	include 'dev8_keys_qlv'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'

;+++
; Initialise SBASIC procedures
;---
sb_initp
	lea	qpc_defs,a1		; Thing definition
	jsr	gu_thini

	lea	qpc_procs,a1		; QPC procedures
	move.w	sb.inipr,a2
	jmp	(a2)

	proc_thg	{QPC}

qpc_exit	  proc	  {EXIT}
qpc_mspeed	  proc	  {MSPD}
qpc_maximize	  proc	  {MAXW}
qpc_minimize	  proc	  {MINW}
qpc_restore	  proc	  {RESW}
qpc_windowsize	  proc	  {WSIZ}
qpc_qlscremu	  proc	  {SEMU}
qpc_exec	  proc	  {EXEC}
qpc_syncscrap	  proc	  {SYNC}
qpc_windowtitle   proc	  {WTIT}
qpc_flashbutton   proc	  {FLSH}
ser_setport	  proc	  {SSET}
par_setprinter	  proc	  {PSET}
par_setfilter	  proc	  {PFST}

	fun_thg       {QPC}

qpc_ver$	  fun	  {VER$},16
qpc_netname$	  fun	  {NAM$},64
qpc_cmdline$	  fun	  {CMD$},127
qpc_hostos	  fun	  {HOST},16
qpc_hasfocus	  fun	  {FOCS},16
ser_getport$	  fun	  {SGET},16
par_printercount  fun	  {PCNT},16
par_printername$  fun	  {PNAM},127
par_getprinter$   fun	  {PGET},127
par_defaultprinter$ fun   {PDEF},127
par_getfilter	  fun	  {PFGT},16

qpc_procs
	proc_stt
	proc_ref QPC_EXIT
	proc_ref QPC_MSPEED
	proc_ref QPC_MAXIMIZE
	proc_ref QPC_MINIMIZE
	proc_ref QPC_WINDOWSIZE
	proc_ref QPC_RESTORE
	proc_ref QPC_QLSCREMU
	proc_ref QPC_EXEC
	proc_ref QPC_SYNCSCRAP
	proc_ref QPC_WINDOWTITLE
	proc_ref QPC_FLASHBUTTON
	proc_ref SER_SETPORT
	proc_ref PAR_SETPRINTER
	proc_ref PAR_SETFILTER
	proc_end
	proc_stt
	proc_ref QPC_VER$
	proc_ref QPC_NETNAME$
	proc_ref QPC_CMDLINE$
	proc_ref QPC_HOSTOS
	proc_ref QPC_HASFOCUS
	proc_ref SER_GETPORT$
	proc_ref PAR_PRINTERCOUNT
	proc_ref PAR_PRINTERNAME$
	proc_ref PAR_DEFAULTPRINTER$
	proc_ref PAR_GETPRINTER$
	proc_ref PAR_GETFILTER
	proc_end

	end
