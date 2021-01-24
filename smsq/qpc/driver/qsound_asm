; Base area SMSQ QSOUND drivers  v1.00	 2021 by Marcel Kilgus

	section header

	xref	smsq_end
	xref.l	qsound.vers

header_base
	dc.l	qpc_qsound-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-qpc_qsound	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	22,'SMSQ QPC QSOUND Driver'
	dc.l	qsound.vers
	dc.w	$200a

	section base

	xref	qsound_base

qpc_qsound
	jmp	qsound_base

	end
