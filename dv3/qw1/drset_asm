; DV3 QLW1 (Qubide) Format Set Directory Entry	  V3.00    W. Lenerz 2017
; based on
; DV3 QL Format Set Directory Entry    V3.00	       1992 Tony Tebby

	section dv3

	xdef	qw1_drsfile
	xdef	qw1_drloc

	xref	dv3_drloc
	xref	dv3_drupd

	include 'dev8_keys_hdr'
	include 'dev8_dv3_keys'

;+++
; DV3 QL Format Set Directory Entry For File
;
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a2    p
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard (not recognised)
;
;---
qw1_drsfile
qds.reg reg	d2/d5/a2
	movem.l qds.reg,-(sp)
	bsr.s	qw1_drloc
	bne.s	qds_exit

	move.l	d3c_updd(a0),hdr_date(a2); copy dates and version numbers
	move.w	d3c_vers(a0),hdr_vers(a2)
	move.l	d3c_arcd(a0),hdr_bkup(a2)
	move.l	d3c_feof(a0),(a2)+	 ; length
	move.w	d3c_dsattr(a0),(a2)+	 ; attributes
	move.l	d3c_data(a0),(a2)+	 ; data
	move.l	d3c_xtra(a0),(a2)	 ; extra

	jsr	dv3_drupd
	moveq	#0,d0

qds_exit
	movem.l (sp)+,qds.reg
	rts

;+++
; DV3 QL Format locate directory entry
;
;	d2    s
;	d5    s
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a2  r	pointer to entry
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard (not recognised)
;
;---
qw1_drloc
	move.l	d3c_sdent(a0),d2	 ; our own entry

	moveq	#1,d5			 ; entry 0 is at position 1
	add.l	d2,d5
	moveq	#7-hdr.sft,d0		 ; header to sector
	add.b	ddf_slflag(a4),d0	 ; + sector length flag
	lsr.l	d0,d5			 ; header sector

	jsr	dv3_drloc		 ; locate sector
	bne.s	qds_rts

	addq.l	#1,d2
	lsl.w	#hdr.sft,d2
	and.w	ddf_smask+2(a4),d2	 ; byte within sector
	add.w	d2,a2			 ; start of data
	moveq	#0,d0
qds_rts
	rts

	end
