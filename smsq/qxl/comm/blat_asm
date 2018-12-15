; SMS (QXL) Supervisor mode BLAT routine
; 2006-10-20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)
	section blat

	xdef	blatt
	xdef	blattl

	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_qxl_keys'


*blat macro blv
* move.b [blv],-(sp)
* jsr	 blatt
* addq.l #2,sp
* endm
*
*blatl macro blv
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm



blattl
  movem.l d0/d3,-(sp)
  move.l $c(sp),d3
  rol.l  #8,d3
  bsr.s  blats
  rol.l  #8,d3
  bsr.s  blats
  rol.l  #8,d3
  bsr.s  blats
  rol.l  #8,d3
  bsr.s  blats
  movem.l (sp)+,d0/d3
  rts


blatt
  movem.l d0/d3,-(sp)
  move.b $c(sp),d3
  bsr.s  blats
  movem.l (sp)+,d0/d3
  rts

blats
  movem.l d1/d2/d3/a0,-(sp)
  move.l #500,d2

bl
  lea	 $100000,a0
  move.l $8(sp),d0
  move.w sr,-(sp)
  or.w	 #$0700,sr

  moveq  #7,d1
bl1
  bsr.s ns
  st	qxl_neth
  bsr.s ns
  st	qxl_netl
  dbra	d1,bl1

  moveq  #7,d1
bl2
  bsr.s ns
  st	qxl_neth
  bsr.s ns
  lsl.b #1,d0
  bcs.s bl2e
  st	qxl_netl
bl2e
  bsr.s nl
  st	qxl_netl
  dbra	d1,bl2

  bsr.s ns
  st	qxl_neth
  bsr.s ns
  st	qxl_netl

  moveq #50,d1
bl3
  bsr.s nl
  dbra d1,bl3

  move.w  (sp)+,sr
  dbra d2,bl

  movem.l (sp)+,d1/d2/d3/a0
  moveq #0,d0
  rts

ns
  moveq #8,d3
nsl
  tst.l (a0)
  tst.l $10(a0)
  add.w #$20,a0
  dbra d3,nsl
  rts

nl
  moveq #100,d3
nll
  tst.l (a0)
  tst.l $10(a0)
  add.w #$20,a0
  dbra d3,nll
  rts

	end
