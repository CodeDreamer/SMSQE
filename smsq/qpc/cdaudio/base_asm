; QPC_CDAUDIO_PROCS_BASE - QPC CD-Audio Base  V1.00   1996  Marcel Kilgus

	section header

	xref	smsq_end
	xref	cd_defs
	xref	gu_thini

header_base
	dc.l	cd_initp-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-cd_initp	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; one level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	16,'CD-Audio driver '
	dc.l	'    '
	dc.w	$200a

	section init

	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_mac_basic'
	include 'dev8_mac_proc'

;+++
; Initialise SBASIC procedures
;---
cd_initp
	lea	cd_defs,a1		  ; Thing definition
	jsr	gu_thini

	lea	cd_procs,a1		  ; QL procedures
	move.w	sb.inipr,a2
	jmp	(a2)

cd_init 	proc	{INIT}
cd_play 	proc	{PLAY}
cd_stop 	proc	{STOP}
cd_resume	proc	{RSME}
cd_eject	proc	{EJCT}
cd_close	proc	{CLSE}

	proc_thg {CD-Audio Control}     ; In the middle because otherwise
	fun40_thg {CD-Audio Control}    ; the short branch would get OOR

cd_isplaying	fun40	{IPLA}
cd_isclosed	fun40	{ICLO}
cd_isinserted	fun40	{IINS}
cd_ispaused	fun40	{IPAU}
cd_track	fun40	{TRCK}
cd_tracktime	fun40	{TTIM}
cd_alltime	fun40	{ATIM}
cd_hsg2red	fun40	{H2R }
cd_red2hsg	fun40	{R2H }
cd_trackstart	fun40	{TSTA}
cd_tracklength	fun40	{TLEN}
cd_firsttrack	fun40	{FTRK}
cd_lasttrack	fun40	{LTRK}
cd_length	fun40	{LGTH}
cd_hour 	fun40	{HOUR}
cd_minute	fun40	{MIN }
cd_second	fun40	{SEC }

cd_procs
	proc_stt
	proc_ref CD_INIT
	proc_ref CD_PLAY
	proc_ref CD_STOP
	proc_ref CD_RESUME
	proc_ref CD_EJECT
	proc_ref CD_CLOSE
	proc_end
	proc_stt
	proc_ref CD_ISPLAYING
	proc_ref CD_ISCLOSED
	proc_ref CD_ISINSERTED
	proc_ref CD_ISPAUSED
	proc_ref CD_TRACK
	proc_ref CD_TRACKTIME
	proc_ref CD_ALLTIME
	proc_ref CD_HSG2RED
	proc_ref CD_RED2HSG
	proc_ref CD_TRACKSTART
	proc_ref CD_TRACKLENGTH
	proc_ref CD_FIRSTTRACK
	proc_ref CD_LASTTRACK
	proc_ref CD_LENGTH
	proc_ref CD_HOUR
	proc_ref CD_MINUTE
	proc_ref CD_SECOND
	proc_end

	end
