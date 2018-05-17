* Draw border round object hit area  V1.05     1986  Tony Tebby   QJUMP
*
* 2002-13-11  1.05  Adapted for high colour use (MK)
*
	section wman
*
	xdef	wm_drbdr
	xdef	wm_drins
	xdef	wm_clrci
*
	xref	wmc_trap3
*
	include dev8_keys_qdos_io
	include dev8_keys_wstatus
*
*	d0  r	error return
*	a0 c  p channel ID of window
*	a1 c  p pointer to window status area
*
*		all other registers preserved
*
xsize	equ	$00
ysize	equ	$02
xorg	equ	$04
yorg	equ	$06
*
reglist  reg	d1-d4/a1
stk_wstt equ	$18			 ; pointer to status on stack
wdef.blk equ	$08			 ; 8 bytes for define block
*
* clear current item
*
wm_clrci
	tst.w	ws_citem(a1)		 ; is there a current loose item?
	bmi.s	wci_rts 		 ; ... no, can't get rid of it
	st	ws_citem(a1)		 , ... there is not now
	move.l	ws_ciact(a1),d0 	 ; current item action routine
	beq.s	wmc_drbdr		 ; ... none
	clr.l	ws_ciact(a1)		 ; ... none now
	move.l	d0,-(sp)		 ; ... go for it!!
wci_rts
	rts
*

wm_drins
	moveq	#0,d0			 ; no border offset
	bra.s	wdr_do
wm_drbdr
wmc_drbdr
	moveq	#-1,d0			 ; add border to outside
wdr_do
	movem.l reglist,-(sp)		 ; save registers
	lea	ws_cibrw(a1),a1
	move.w	(a1)+,d2		 ; border width
	move.w	(a1),d1 		 ; border colour
	add.w	#ws_cihit-ws_cipap+wdef.blk,a1 ; end of definition block
*
	move.w	d2,d4			 ; border width
	and.w	d0,d4			 ; (or not)
	move.w	-(a1),d0		 ; y origin
	sub.w	d4,d0			 ; less border
	move.w	d0,-(sp)
	add.w	d4,d4
	move.w	-(a1),d0		 ; x origin
	sub.w	d4,d0			 ; less border
	move.w	d0,-(sp)
	move.w	-(a1),d0		 ; height
	add.w	d4,d0			 ; + twice border
	move.w	d0,-(sp)
	add.w	d4,d4
	add.w	-(a1),d4		 ; width + four times border
	move.w	d4,-(sp)
	move.l	sp,a1
	bsr.s	wdb_wdef		 ; redefine window and border
	bne.s	wdb_exit
*
	move.l	stk_wstt(sp),a1 	 ; reset pointer to window status
	lea	ws_subdf(a1),a1 	 ; genuine window area
	moveq	#0,d2			 ; no border
	bsr.s	wdb_wdef
*
wdb_exit
	addq.l	#wdef.blk,sp		 ; restore stack
	movem.l (sp)+,reglist		 ; and registers
wdb_rts
	rts
*
* redefine window
*
wdb_wdef
	moveq	#iow.defw,d0
	moveq	#forever,d3
	jsr	wmc_trap3
	tst.l	d0
	rts

	end
