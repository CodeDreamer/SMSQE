; DV3 Hard Disk Polling Routine Flush	     1993     Tony Tebby

	section dv3

	xdef	hd_pflush

	xref	dv3_fdef
	xref	dv3_sbrange
	xref	dv3_sbclr

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'

;+++
; This routine is called from poll to flush all buffers / maps for all drives
;
;	a3 c  p linkage block
;
;	all registers except d0 preserved
;---
hd_pflush
hdpa.reg reg	d1/d2/d7/a0/a1/a4
	movem.l hdpa.reg,-(sp)
	move.b	hdl_maxd(a3),d7 	 ; start at highest drive
hdp_hdef
	jsr	dv3_fdef		 ; find definition
	blt.s	hdp_ndef		 ; ... none
	tst.b	ddf_mstat(a4)		 ; ok?
	bge.s	hdp_bdrive		 ; ... no, scrub the slave blocks

	bsr.s	hdp_dflush		 ; flush

	tst.b	ddf_lock(a4)		 ; unlock?
	ble.s	hdp_ndef
	sf	ddf_lock(a4)		 ; requested
	jsr	hdl_unlock(a3)
	bra.s	hdp_ndef

hdp_bdrive
	jsr	dv3_sbclr		 ; clear slave blocks (panic)

hdp_ndef
	subq.b	#1,d7			 ; next drive
	bgt.s	hdp_hdef

hdp_exit
	movem.l (sp)+,hdpa.reg
	rts

hdp_dflush
	movem.l ddf_slbl(a4),a0/a1	 ; slave block range
	move.l	a1,d0			 ; any range
	beq.s	hdp_mcheck		 ; ... no
	bsr.s	hdp_fflush		 ; ... yes, flush file buffers

	st	ddf_slbl(a4)		 ; enormous number at bottom of range
	clr.l	ddf_slbh(a4)		 ; no top end of range
	bra.s	hdp_mflush		 ; always flush map

hdp_mcheck
	tst.b	ddf_mupd(a4)		 ; map updated?
	beq.s	hdp_rts

hdp_mflush
	moveq	#0,d1
hdp_mloop
	jsr	ddf_umap(a4)		 ; next updated map sector
	beq.s	hdp_rts
	bsr.s	hdp_wsec		 ; write sector
	bra.s	hdp_mloop

hdp_fflush
	pea	hdp_fssb		 ; action routine
	jmp	dv3_sbrange

hdp_fssb
	bclr	#sbt..wrq,sbt_stat(a2)	 ; not needing write any more
	beq.s	hdp_ok			 ; not that it ever did

	moveq	#0,d0
	move.w	sbt_dgrp(a2),d0
	moveq	#0,d1
	move.b	sbt_dsct(a2),d1 	 ; physical group / sector

	jsr	ddf_logphys(a4) 	 ; physical sector

hdp_wsec
	moveq	#1,d2			 ; one sector - could do better!
	jsr	hdl_wsint(a3)		 ; write sector
	beq.s	hdp_rts
	cmp.l	#err.rdo,d0		 ; read only?
	bne.s	hdp_blab		 ; ... no
	jsr	dv3_sbclr		 ; ... yes, clear slave blocks

hdp_blab
	movem.l d1/d5/d7/a3,-(sp)
	lea	hdp_beep,a3
	moveq	#sms.hdop,d0
	trap	#do.sms2
	movem.l (sp)+,d1/d5/d7/a3
hdp_ok
	moveq	#0,d0			 ; throw away error sectors
hdp_rts
	rts

hdp_beep dc.b	$a,8,0,0,$aa,$aa,$01,$14,$c8,$00,$28,$23,$3f,0,1,0

	end
