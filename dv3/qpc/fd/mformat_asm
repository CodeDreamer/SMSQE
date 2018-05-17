; DV3 QPC Floppy Disk Format v1.01	      1993	Tony Tebby
;					      2000	Marcel Kilgus
;
; 2006-06-18  1.01  Use density from FLP_DENSITY if no density given (MK)

	section dv3

	xdef	fd_mformat

	xref	dv3_slen
	xref	fd_hold
	xref	fd_release
	xref	fd_ckwp

	xref	gu_achp0
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'

;+++
; This routine formats a medium
;
;	d0 cr	format type / error code
;	d1 cr	format dependent flag or zero / good sectors
;	d2  r	total sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return standard
;---
fd_mformat
fmf.reg reg	d3/d4/d5/a0/a1/a2/a5
	movem.l fmf.reg,-(sp)

fmf_wstart
	jsr	fd_hold
	bne.s	fmf_wstart

	move.l	#(36*2*80+7)/8,d0	 ; space 512 byte sector map
	jsr	gu_achp0		 ; on 80 track DS ED drive
	bne.l	fmf_exit

	cmp.b	#2,d7
	bhi.l	fmf_fmtf

	jsr	fd_ckwp
	blt.l	fmf_exrt		 ; ... oops

	tst.b	ddf_wprot(a4)		 ; write protected?
	bne.l	fmf_ro

	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	move.b	ddf_density(a4),d2	 ; specified density
	bge.s	fmf_sssct
	move.b	fdl_defd(a3),d2 	 ; default density
	bge.s	fmf_sssct
	moveq	#-1,d2			 ; auto detect
	bra.s	fmf_sdensity
fmf_sssct
	assert	0,ddf.dd-1,ddf.hd-2
	mulu	#9,d2			 ; now number of sectors
	cmp.w	#18,d2
	bgt.l	fmf_fmtf
fmf_sdensity
	move.w	fdl_maxt(a3),d3 	 ; number of tracks
	moveq	#2,d4			 ; number of sides

	dc.w	qpc.fdfmt
	dc.w	$4AFB			 ; for security

	tst.l	d2			 ; any disk?
	beq.s	fmf_fmtf		 ; ... no

	move.b	#ddf.dd,ddf_density(a4)  ; set density (assumed)
	cmp.b	#9,d2			 ; more than 9?
	ble.s	fmf_shdc		 ; ... no
	move.b	#ddf.hd,ddf_density(a4)  ; reset density

fmf_shdc
	lea	dff_size+dff.size*6(a0),a0
	clr.l	-(a0)			 ; no 4096 byte
	clr.l	-(a0)			 ; no 2048 byte
	clr.l	-(a0)			 ; no 1024 byte
	move.l	d2,-(a0)		 ; track size in 512 byte
	clr.l	-(a0)			 ; no 256 byte
	clr.l	-(a0)			 ; no 128 byte
	move.w	d3,-(a0)		 ; cylinders
	move.w	d4,-(a0)		 ; heads

					 ; d1 is format dependent flag!!
	jsr	ddf_fselect(a4) 	 ; select format
	bne.s	fmf_exrt		 ; exit and return sector table

	clr.l	dff_size+dff.size*ddf.512(a0) ; all OK!
	clr.l	(a0)

	bset	d7,fdl_stpb(a3) 	 ; this drive will not have stopped
	moveq	#ddf.full,d0		 ; ... the only type of format we can do
	jsr	ddf_format(a4)		 ; so do it!
	st	ddf_slbl(a4)		 ; set slave block range

fmf_exrt
	jsr	gu_rchp 		 ; return bit of heap

fmf_exit
	jsr	fd_release
	movem.l (sp)+,fmf.reg
	tst.l	d0
	rts

fmf_fmtf
	moveq	#err.fmtf,d0
	bra.s	fmf_exrt

fmf_ro
	moveq	#err.rdo,d0
	bra.s	fmf_exrt

	end
