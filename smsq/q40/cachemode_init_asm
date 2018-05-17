; Q60 cachemodes	1.00  (wl)
; 2005-01-21	1.01	pre-configure to serialized  & use config info really (wl)

	section init

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_mac_config02'
	include 'dev8_mac_proc'

	xref copyback
	xref serialized
	xref writethrough
	xref smsq_end

	section header

qx0c_vers	equ	'1.01'

header_base
	dc.l	qx0c_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-qx0c_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*
smsq_name
	dc.w	smsq_name2-*-2,'Initialise Q40/Q60 Cache modes '
smsq_name2
	dc.l	qx0c_vers
	dc.w	$200a
	  

	xref.l	smsq_vers
	mkcfhead {Cache Mode},{smsq_vers}

	mkcfitem 'OS60',code,'I',qx0c_cf,,,\
	{Initial Cache mode}\
	1,S,{Serialized = Cache OFF},2,W,{Writethrough},3,C,{Copyback}

	mkcfend
	ds.w	0


	section base

qx0c_base
	lea	procs,a1		; procedures
	move.w	sb.inipr,a2
	jsr    (a2)		       ; link them in

	lea	qx0c_cf(pc),a2
	move.b	(a2),d0 		; preconfigured cache mode
	subq.b	#1,d0			; is it serialized?
	bne.s	qx0c_ns 		;    no ->...
	lea	serialized(pc),a2
	bra.s	qx0c_end
qx0c_ns subq.b	#1,d0			; is it writethrough?
	bne.s	qx0c_nw 		;    no->...
	lea	writethrough(pc),a2
	bra.s	qx0c_end
qx0c_nw lea	copyback(pc),a2 	; copyback is default
qx0c_end
	movem.l a3/a5,-(a7)
	move.l	a3,a5
	jsr	(a2)			; set cache mode now
	movem.l (a7)+,a3/a5
	moveq	#0,d0			; always return w/o error
	rts


qx0c_cf dc.b	1,1
	  
procs
	proc_stt
	proc_def	COPYBACK
	proc_def	WRITETHROUGH
	proc_def	SERIALIZED
	proc_end
	proc_stt
	proc_end

	end
