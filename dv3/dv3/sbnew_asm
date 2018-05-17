; DV3 New Slave Block	  V3.00    1992   Tony Tebby

	section dv3

	xdef	dv3_sbnew

	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'
	include 'dev8_mac_assert'

;+++
; Allocate a new slave block. In this simple version it just takes the
; first slave block it comes to. If this is awaiting write, it flushes it.
; It fills in the current slave block table pointer in the channel block.
; It marks the block as valid to prevent odd actions occurring.
;
;	d0  r	error code = 0 in existing versions
;	d3 c  p physical sector / group
;	d5 c  p sector number of file
;	d6 c  p file number
;	d7 c  p drive ID / number
;	a1  r	pointer to slave block table
;	a2 cr	ptr to ptr to current sb / pointer to slave block contents
;
;	error status undefined
;---
dv3_sbnew
dsn.reg  reg	d0/d1/d2/d7
	movem.l dsn.reg,-(sp)

	move.w	ddf_xslv(a4),d2 	 ; extra slave blocks required
	assert	sbt.len,8
	lsl.w	#3,d2

	moveq	#sbt.len,d1
	add.l	sys_sbrp(a6),d1 	 ; next slave block
	sub.l	sys_sbtb(a6),d1 	 ; offset in table
	lsl.l	#sbt.shft,d1
	add.l	a6,d1			 ; pointer to slave block itself
	cmp.l	sys_fsbb(a6),d1 	 ; below base?
	blt.s	dsn_base
	move.l	d2,d0
	lsl.w	#sbt.shft,d0
	add.l	d1,d0
	cmp.l	sys_sbab(a6),d0 	 ; off top?
	blt.s	dsn_set
dsn_base
	move.l	sys_fsbb(a6),d1 	 ; start at beginning

dsn_set
	move.l	d1,d0			 ; block itself
	sub.l	a6,d1
	lsr.l	#sbt.shft,d1
	move.l	sys_sbtb(a6),a1
	add.l	d1,a1			 ; slave block pointer
	move.l	a1,(a2)
	add.w	d2,a1			 ; save pinter to last in current SB
	move.l	a1,sys_sbrp(a6)
	move.l	d0,a2			 ; slave block contents

	assert	0,sbt_stat,sbt_dsct-1,sbt_dgrp-2
	move.w	#sbt.true<<12,d7	 ; ............dddd ssss............
	swap	d7			 ; ssss............ ............dddd
	ror.l	#4,d7			 ; ddddssss........ ................
	or.l	d3,d7			 ; + phys sector / group

dsn_wait
	moveq	#sbt.actn,d0		 ; action bits
	and.b	sbt_stat(a1),d0
	beq.s	dsn_ours

	movem.l d1-d3/a0-a4,-(sp)
	moveq	#0,d0
	move.b	sbt_stat(a1),d0
	lsr.b	#4,d0			 ; drive number
	lsl.b	#2,d0			 ; ... offset in table
	lea	sys_fsdd(a6),a2
	move.l	(a2,d0.w),a2		 ; physical definition
	move.w	#-ddl_ddlk,a3
	add.l	ddf_ddlk(a2),a3
	move.l	ddl_fslv(a3),a4
	jsr	(a4)			 ; force slave this
	movem.l (sp)+,d1-d3/a0-a4
	bra.s	dsn_wait

dsn_ours
	move.l	d7,(a1)+		 ; set phys
	assert	0,sbt_file-4,sbt_sect-6
	move.w	d6,(a1)+		 ; set file
	move.w	d5,(a1) 		 ; and sector
	subq.l	#6,a1
	subq.w	#sbt.len,d2		 ; all set?
	blt.s	dsn_done		 ; ... yes
	subq.l	#sbt.len,a1		 ; ... no
	bra.s	dsn_wait

dsn_done
	moveq	#0,d0
	movem.l (sp)+,dsn.reg
	rts
	end
