* Network Driver initialisation (Fixed clock)  V0.2    1985  Tony Tebby  QJUMP
*
	section nd
*
	xdef	nd_initb
	xdef	nf_name
*
	xref	nd_io			network io
	xref	nd_open 			open
	xref	nd_close			close
	xref	nd_rechp			release heap
	xref	nd_test 			test input
	xref	nd_fbyte			fetch byte
	xref	nd_sbyte			send byte
*
	xref	nf_io			network file io
	xref	nf_open 			     open
	xref	nf_close			     close
	xref	nf_formt			     format
	xref	nf_test 			     test byte
	xref	nf_fbyte			     fetch byte
	xref	nf_sbyte			     send byte
*
	xref	nu_io			network file io with use
	xref	nu_open 			     open with use
	xref	nu_close			     close with use
*
	xref	ns_io			network server io
	xref	ns_open 			       open
*						       (close is rechp)

	xref	gu_achpp

	include dev8_dd_nd_keys
*
* net server device definitions
*
ns_defs
	dc.b	%11100000,3	3 items all addresses
	dc.w	ns_io-*
	dc.w	ns_open-*
	dc.w	nd_rechp-*
*
* net io device definitions
*
nd_defs dc.b	%11111100,7	7 items 6 addresses 1 rts
	dc.w	nd_io-*
	dc.w	nd_open-*
	dc.w	nd_close-*
	dc.w	nd_test-*
	dc.w	nd_fbyte-*
	dc.w	nd_sbyte-*
	rts
*
* net file io directory definitions
*
nf_def1 dc.b	%11100000,3	3 items all addresses
	dc.w	nf_io-*
	dc.w	nf_open-*
	dc.w	nf_close-*
nf_def2 dc.b	%10000111,10  10 items 1 address 4 constants 3 addresses 1 rts
	dc.w	nf_formt-*
	dc.l	nf_plend
nf_name dc.w	1,'N '
	dc.w	nf_test-*
	dc.w	nf_fbyte-*
	dc.w	nf_sbyte-*
	rts
	dc.w	0	      ... and one spare
*
* net file io use definitions
*
nu_def1 dc.b	%11100000,3	3 items all addresses
	dc.w	nu_io-*
	dc.w	nu_open-*
	dc.w	nu_close-*
nu_def2 dc.b	%10000000,3	3 items 1 address 2 constants
	dc.w	nf_formt-*
	dc.l	nf_plend
*
nd_initb
	move.l	#ndd.lens+ndd.leni+ndd.lenf+ndd.lenu,d0   enough room for all
	jsr	gu_achpp
	bne.s	ndi_rts
*
	move.l	a0,a3			set start address
*
	move.l	a1,d3
	lea	ns_defs,a1
	bsr.s	ndi_sdef		... nsv
	bsr.s	ndi_sdef		... net
	bsr.s	ndi_sdef		... start of file linkage
	bsr.s	ndi_sdff		... second bit of file linkage
	moveq	#(ndd_endf-ndt_wgap)/2,d2
	exg	d3,a1
	bsr.s	ndi_sdloop		... timing constants
	exg	d3,a1
	bsr.s	ndi_sdef		... start of use
	bsr.s	ndi_sdff		... second bit of use linkage
*
	move.l	a0,a3
*
	moveq	#mt.liod,d0		link in IO driver for nsv
	trap	#1
	lea	ndd.lens(a3),a0 	link in IO driver for net
	moveq	#mt.liod,d0
	trap	#1
	lea	ndd.lens+ndd.leni+ndd.lenf(a3),a0 link in
	moveq	#mt.ldd,d0		directory driver for file 'use'
	trap	#1
	lea	ndd.lens+ndd.leni(a3),a0 link in 
	moveq	#mt.ldd,d0		directory driver for file io
	trap	#1
ndi_rts
	rts
*
ndi_sdff
	addq.l	#ndd_frmt-(ndd_cllk+4)-4,a3  leave gap between close and format
ndi_sdef
	addq.l	#4,a3			leave space for linkage
	move.b	(a1)+,d1		set mask
	move.b	(a1)+,d2		and number to fill
ndi_sdloop   
	move.l	a1,a2			set address
	move.w	(a1)+,d0		and offset / value
	lsl.b	#1,d1			get next type
	bcc.s	ndi_sval		... a value
	add.w	d0,a2			add offset to address
	move.l	a2,(a3)+		and set absolute address
	bra.s	ndi_sdend
ndi_sval
	move.w	d0,(a3)+		set word
ndi_sdend
	subq.b	#1,d2			next set
	bgt.s	ndi_sdloop
	rts
	end
