; QPC Standard Floppy Disk Polling Routine Flush        1993	  Tony Tebby
;
; 2017-12-11  1.00  Added QPC driver flush to standard code (MK)

	section dv3

	xdef	fd_pflush

	xref	fd_wsint
	xref	fd_rid
	xref	dv3_fdef
	xref	dv3_sbrange
	xref	dv3_sbclr

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'
	include 'dev8_smsq_qpc_keys'

;+++
; This routine is called from poll to flush all buffers / maps for all drives
;
;	a3 c  p linkage block
;
;	all registers except d0 preserved
;---
fd_pflush
fdpa.reg reg	d1/d2/d7/a0/a1/a4
	movem.l fdpa.reg,-(sp)
	move.b	fdl_maxd(a3),d7 	 ; start at highest drive
fdp_fdef
	jsr	dv3_fdef		 ; find definition
	blt.s	fdp_ndef		 ; ... none
	tst.b	ddf_mstat(a4)		 ; ok?
	bge.s	fdp_bdrive		 ; ... no, scrub the slave blocks

	movem.l ddf_slbl(a4),a0/a1	 ; slave block range
	move.l	a1,d0			 ; any range
	beq.s	fdp_mcheck		 ; ... no

	st	ddf_mupd(a4)		 ; ensure mapp updated (updates root)
	pea	fdp_krange
	pea	fdp_fssb		 ; action routine
	jmp	dv3_sbrange		 ; do all slave blocks in range

fdp_bdrive
	clr.b	ddf_mupd(a4)		 ; do not update map
	tst.l	ddf_slbl(a4)		 ; slave block range set?
	beq.s	fdp_mcheck		 ; ... no
	jsr	dv3_sbclr		 ; clear slave blocks (panic)

fdp_krange
	st	ddf_slbl(a4)		 ; enormous number at bottom of range
	clr.l	ddf_slbh(a4)		 ; no top end of range

fdp_mcheck
	tst.b	ddf_mupd(a4)		 ; map updated?
	beq.s	fdp_ddone		 ; ... no, drive done

fdp_mflush
	moveq	#0,d1
fdp_mloop
	jsr	ddf_umap(a4)		 ; next updated map sector
	beq.s	fdp_ddone		 ; ... drive done
	bsr.s	fdp_wsec		 ; write sector
	btst	d7,fdl_stpb(a3) 	 ; is the drive still OK
	bne.s	fdp_mloop		 ; ... yes

fdp_ddone
	jsr	fd_rid			 ; read ID
	clr.b	ddf_mupd(a4)
	tst.b	ddf_lock(a4)		 ; unlock?
	blt.s	fdp_ndef
; ***** QPC
	sf	ddf_lock(a4)
	dc.w	qpc.frlse		 ; release drive, might trigger cached write
	beq.s	fdp_ndef		 ; all ok
	bsr.s	fdp_chkerr		 ; something bad, do error stuff (beep)
; ***** QPC
fdp_ndef
	subq.b	#1,d7			 ; next drive
	bgt.s	fdp_fdef

fdp_exit
	movem.l (sp)+,fdpa.reg
	rts

fdp_fssb
	move.l	(a2),d1 		 ; check
	moveq	#-1,d0
	add.w	ddf_xslv(a4),d0 	 ; any more sectors?
	blt.s	fdpf_do
	beq.s	fdpf_c2
	cmp.l	3*sbt.len(a2),d1
	bne.s	fdp_ok			 ; not a complete group
	cmp.l	2*sbt.len(a2),d1
	bne.s	fdp_ok			 ; not a complete group
fdpf_c2
	cmp.l	1*sbt.len(a2),d1
	bne.s	fdp_ok			 ; not a complete group

fdpf_do
	bclr	#sbt..wrq,sbt_stat(a2)	 ; not needing write any more
	beq.s	fdp_ok			 ; not that it ever did

	tst.w	d0			 ; any more sectors?
	blt.s	fdpf_set
	beq.s	fdpf_2
	bclr	#sbt..wrq,sbt_stat+3*sbt.len(a2) ; clear write in other sectors
	bclr	#sbt..wrq,sbt_stat+2*sbt.len(a2)
fdpf_2
	bclr	#sbt..wrq,sbt_stat+1*sbt.len(a2)

fdpf_set
	moveq	#0,d0
	move.w	sbt_dgrp(a2),d0
	moveq	#0,d1
	move.b	sbt_dsct(a2),d1 	 ; physical group / sector

	jsr	ddf_logphys(a4) 	 ; physical sector

fdp_wsec
	moveq	#1,d2
	jsr	fd_wsint		 ; write sector
	beq.s	fdp_rts
	assert	ddf.mok-$ff,ddf.mnew,0
	tst.b	ddf_mstat(a4)		 ; status OK or new?
	bgt.s	fdp_sbclr		 ; ... no
fdp_chkerr
	cmp.l	#err.rdo,d0		 ; read only?
	bne.s	fdp_blab		 ; ... no
fdp_sbclr
	jsr	dv3_sbclr		 ; ... yes, clear slave blocks

fdp_blab
	movem.l d1/d5/d7/a3,-(sp)
	lea	fdp_beep,a3
	moveq	#sms.hdop,d0
	trap	#do.sms2
	movem.l (sp)+,d1/d5/d7/a3
	bclr	d7,fdl_stpb(a3) 	 ; check again Sam
fdp_ok
	moveq	#0,d0			 ; throw away error sectors
fdp_rts
	rts

fdp_beep dc.b	$a,8,0,0,$aa,$aa,$01,$14,$c8,$00,$28,$23,$3f,0,1,0

	end
