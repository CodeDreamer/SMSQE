; QXL_PROCS_BASE - QXL Procedures Base	V2.02	 1992	Tony Tebby

	section header

	xref	smsq_end

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
	dc.w	18,'SBASIC Procedures '
	dc.l	'    '
	dc.w	$200a


	xdef	tk2_ext

	xref	sb_procs
	xref	sb_xtprocs
	xref	qxl_prini		  ; our own QXL procs
	xref	sb_tk2procs

	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
;+++
; Initialise SBASIC procedures
;---
sb_initp
	lea	sb_procs,a1		  ; QL procedures
	bsr.s	sb_inipr
	lea	sb_xtprocs,a1		  ; SMSQ extra procedures
	bsr.s	sb_inipr
	jsr	qxl_prini

tk2_ext
	lea	sb_tk2procs,a1
sb_inipr
	move.w	sb.inipr,a2
	jmp	(a2)


	section exten

	xdef	dec_point
	xdef	zero_w
	xdef	prior
	xdef	pipe_len
	xdef	pipe_nol

dec_point dc.b	'.'
zero_w	 dc.w	0
prior	 dc.w	8
pipe_len dc.w	9,'pipe_1024'
pipe_nol dc.w	4,'pipe'

	end
