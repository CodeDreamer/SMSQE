; SMS (QXL) User mode BLAT routine

	section blat

	xdef	blattu
	xdef	blattul

	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_qxl_keys'


blattul
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

blattu
  movem.l d0/d3,-(sp)
  move.b $c(sp),d3
  bsr.s  blats
  movem.l (sp)+,d0/d3
  rts

blats
  movem.l d1/d2/d3/a0,-(sp)
  move.l #500,d2
bl
  move.l $8(sp),d3
  lea	 $100000,a0
  bsr.s bl_do
  dbra d2,bl

  movem.l (sp)+,d1/d2/d3/a0
  moveq #0,d0
  rts

bl_do
  moveq  #sms.xtop,d0
  trap	 #do.sms2
  or.w	 #$0700,sr
  jsr	 sms.cdisb
  move.l d3,d0

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
