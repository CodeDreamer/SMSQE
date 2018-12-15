; DV3 QXL Floppy Disk Format		      1993	Tony Tebby

	section dv3

	xdef	fd_mformat

	xref	dv3_slen
	xref	fd_hold
	xref	fd_release

	xref	gu_achp0
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'
	include 'dev8_keys_java'

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

fdr_read
	move.b	ddf_density(a4),d2	; specified density (only DD & HD will be supported
	bge.s	dens_set
	move.b	fdl_defd(a3),d2 	; default density
	bge.s	dens_set
	moveq	#1,d2			; if none, set to DD
dens_set
	moveq	#jt8.frmt,d0
	dc.w	jva.trp8		; java side handles the format, which
	tst.l	d0			; just means wrting a file filled with ASCII "0"
	bne.s	exit

	move.l	#(36*2*80+7)/8,d0	; space 512 byte sector map
	jsr	gu_achp0		; on 80 track DS ED drive
	bne.s	exit

	lea	dff_size+dff.size*6(a0),a0
	clr.l	-(a0)			 ; no 4096 byte
	clr.l	-(a0)			 ; no 2048 byte
	clr.l	-(a0)			 ; no 1024 byte
	move.l	d2,-(a0)		 ; nbr sectors per track
	clr.l	-(a0)			 ; no 256 byte
	clr.l	-(a0)			 ; no 128 byte
	move.w	#80,-(a0)		 ; cylinders
	move.w	#2,-(a0)		 ; heads
					 ; (dv3_qlf_fsel_asm)
	jsr	ddf_fselect(a4)
	bne.s	ret

	clr.l	dff_size+dff.size*ddf.512(a0) ; all OK!
	clr.l	(a0)

	bset	d7,fdl_stpb(a3) 	 ; this drive will not have stopped - I have no idea what this does, but it doesn't do any harm, apparently
	moveq	#ddf.full,d0		 ; ... the only type of format we can do
	jsr	ddf_format(a4)		 ; (write first sectors to disk)
	st	ddf_slbl(a4)		 ; set slave block range
ret
	jmp	gu_rchp
exit
	rts

	end
