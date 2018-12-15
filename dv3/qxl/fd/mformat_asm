; DV3 QXL Floppy Disk Format		      1993	Tony Tebby

	section dv3

	xdef	fd_mformat

	xref	dv3_slen
	xref	fd_hold
	xref	fd_release

	xref	qxl_mess_add

	xref	gu_achp0
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
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

	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	move.l	qxl_message,a1
	lea	qxl_ms_phys(a1),a1	 ; key

	move.b	#qxm.fdriv,(a1)+	 ; ... message key
	move.b	d7,(a1)+		 ; flp drive
	move.w	#1,(a1)+		 ; only one message at a time
	clr.l	(a1)+			 ; no sectors
	clr.l	(a1)			 ; no ID
	subq.l	#8,a1
	move.w	#$c,-(a1)		 ; message length

	jsr	qxl_mess_add		 ; add the message
fmf_wait_done
; blat #$aa
	tst.w	(a1)			 ; wait for reply (flagged by -ve len)
	bpl.s	fmf_wait_done
; blat #$55

	tst.b	qxm_err-qxl_ms_len(a1)	 ; error?
	bne.s	fmf_fmtf

	movem.w qxm_ngood-qxl_ms_len(a1),d2/d3 ; nr tracks / sectors per track

	tst.w	d2			 ; any tracks
	beq.s	fmf_fmtf		 ; ... no

	move.b	#ddf.dd,ddf_density(a4)  ; set density (assumed)
	cmp.b	#9,d3			 ; more than 9?
	ble.s	fmf_shdc		 ; ... no
	move.b	#ddf.hd,ddf_density(a4)  ; reset density
fmf_shdc
	lea	dff_size+dff.size*6(a0),a0
	clr.l	-(a0)			 ; no 4096 byte
	clr.l	-(a0)			 ; no 2048 byte
	clr.l	-(a0)			 ; no 1024 byte
	move.l	d3,-(a0)		 ; track size in 512 byte
	clr.l	-(a0)			 ; no 256 byte
	clr.l	-(a0)			 ; no 128 byte
	move.w	d2,-(a0)		 ; cylinders
	move.w	#2,-(a0)		 ; heads

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

	end
