; SMSQ GOLD card ND drivers

	section header

	xref	smsq_end
	xref.l	nd.card
	xref.s	nd.ptype

	include 'dev8_keys_stella_bl'

header_base
	dc.l	nd_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-nd_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select on processor
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	21,'SMSQ Net Driver - '
	dc.l	nd.card
	dc.l	'    '
	dc.w	$200a


select
	cmp.b	#nd.ptype,sbl_ptype(a5)  ; the right processor?
	bne.s	sel_noload
	moveq	#sbl.load,d0
	rts
sel_noload
	moveq	#sbl.noload,d0
	rts

	section base

	xref	nd_gold
nd_base
	jmp	nd_gold

	end
