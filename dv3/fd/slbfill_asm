; DV3 Standard Floppy Disk Fill Slave Block     1993	  Tony Tebby

	section dv3

	xdef	fd_slbfill

	xref	fd_rsint

	include 'dev8_dv3_keys'

;+++
; This routine fills a slave block with a sector from a floppy disk
;
;	d7 c  p drive ID / number
;	a1 c  p pointer to slave block table
;	a2 c  p pointer to slave block
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_slbfill
fds.reg reg	d1/d2
	movem.l fds.reg,-(sp)
	exg	a1,a2			 ; internal operations other way round

	moveq	#0,d0
	moveq	#0,d1
	move.w	sbt_dgrp(a2),d0
	move.b	sbt_dsct(a2),d1
	jsr	ddf_logphys(a4)

	moveq	#1,d2
	jsr	fd_rsint		 ; read sector
	exg	a1,a2
	beq.s	fd_ok
	subq.b	#sbt.read-sbt.mpty,sbt_stat(a1) ; no longer awaiting read
	bra.s	fd_exit
fd_ok
	subq.b	#sbt.read-sbt.true,sbt_stat(a1) ; no longer awaiting read
fd_exit
	movem.l (sp)+,fds.reg
	tst.l	d0
	rts

	end
