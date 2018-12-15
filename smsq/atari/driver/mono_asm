; base area SMSQ ATARI ST Drivers / mono monitor

	section header

	xref	smsq_end

	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_stella_bl'

header_base
	dc.l	at_smono-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-at_smono	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	22,'SMSQ Atari Mono Driver'
	dc.l	'    '
	dc.w	$200a

select
	moveq	#sys.mmon,d0
	and.l	sbl_mtype(a5),d0	 ; monochrome monitor
	beq.s	sel_rts
	moveq	#sbl.load,d0
sel_rts
	rts

	section base

	xref	pt_setup

at_smono
	moveq	#0,d1			 ; size in d1
	moveq	#ptd.ql,d2		 ; ql colour depth (emulation)
	moveq	#ptm.atm,d3		 ; Atari mono (mode 4 emulation) driver
	moveq	#ptd.ql,d4		 ; ql is required
	jmp	pt_setup

	end
