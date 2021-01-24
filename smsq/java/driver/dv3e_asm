; SMSQ/E for built in DV3 drivers (c) T. Tebby / W. Lenerz 2012
; v. 1.01  check for swin

	section header

	xref	smsq_end
	xref.l	dv3_vers
	include dev8_keys_java

header_base
	dc.l	dv3_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-dv3_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	nm_end-*-2
	dc.b	'JAVA DV3'
nm_end
	dc.l	dv3_vers
	dc.w	$200a

	section base

	xref	dv3_init
	xref	fd_init
	xref	hd_init
	xref	dv3_addfd
	xref	qlf_table

dv3_base
	bra.l	init

	section init
init
	jsr	dv3_init	 ; initialise
	jsr	fd_init
	moveq	#jte.swin,d0
	dc.w	jva.trpE
	bne.s	qlf
	jsr	hd_init
qlf	lea	qlf_table,a1
	jmp	dv3_addfd
	end

	end
