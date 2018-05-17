; IO Utilities Find Slave Block   V1.00    1988   Tony Tebby QJUMP

	section iou

	xdef	iou_fisl

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_sbt'
	include 'dev8_keys_chn'
	include 'dev8_mac_assert'
;+++
; IO Utilities find slave block
;
; If the block is not found, d0 is returned as a valid physical block (msw)
; and logical block found which is before the block required. If all else
; fails, it returns the file ID / 0.
;
;	d0  r	0 if found, otherwise some valid physical block / logical block
;	d4 c  p block / byte
;	d6 c  p drive / file
;	a0 c  p channel block
;	a2  r	pointer to slave block 
;
;	error status -ve for not found
;---
iou_fisl
ifi.reg  reg	d1/d2/d4/d7/a1
ifi.xreg reg	d0/d1/d2/d4/d7/a1
stk_phys equ	$00
stk_blok equ	$02 
	movem.l ifi.reg,-(sp)
	clr.w	-(sp)
	move.w	d6,-(sp)		 ; preset return
	move.w	d6,d4			 ; file number needed
	swap	d4			 ; file / block number required
	move.b	chn_drid(a0),d7
	lsl.w	#4,d7			 ; drive id in msnibble of lsbyte
	bset	#sbt..fsb,d7
	move.l	chn_csb(a0),a1		 ; current slave block
	move.l	a1,d0
	bne.s	ifi_start		 ; ... ok
	move.l	sys_sbrp(a6),a1 	 ; ... not OK, start at current
ifi_start
	moveq	#sbt.driv,d0
	and.b	sbt_stat(a1),d0
	cmp.b	d0,d7			 ; the right drive?
	bne.s	ifi_look		 ; ... no
	cmp.b	#sbt.mpty,sbt_stat(a1)	 ; empty?
	beq.s	ifi_look
	assert	sbt_file,sbt_blok-2
	cmp.w	sbt_file(a1),d6 	 ; the right file?
	bne.s	ifi_look
	cmp.w	sbt_blok(a1),d4 	 ; the right block?
	beq.s	ifi_done		 ; ... yes
	blo.s	ifi_look		 ; ... too far on
	move.w	sbt_blok(a1),stk_blok(sp) ; set block found
	move.w	sbt_phyg(a1),stk_phys(sp)

ifi_look
	move.l	sys_sbtb(a6),d0 	 ; base of slave block table
	move.l	sys_sbab(a6),d2 	 ; top of slave block area
	sub.l	a6,d2
	lsr.l	#sbt.shft,d2
	add.l	d0,d2			 ; top of area to look

	move.l	sys_fsbb(a6),d1 	 ; first fssb
	sub.l	a6,d1
	lsr.l	#sbt.shft,d1
	add.l	d0,d1			 ; first vacant sbt entry

	move.l	a1,a2			 ; start both up and down here

ifi_loop
	addq.l	#8,a1			 ; next one up
	cmp.l	d2,a1			 ; at top?
	blt.s	ifi_ckup
	move.l	d1,a1			 ; start again at the bottom
ifi_ckup
	cmp.l	a1,a2			 ; all the way round?
	beq.s	ifi_exit		 ; ... yes

	moveq	#sbt.driv,d0		 ; mask of drive bits
	and.b	sbt_stat(a1),d0
	cmp.b	d0,d7			 ; right drive?
	bne.s	ifi_down		 ; ... no
	cmp.b	#sbt.mpty,sbt_stat(a1)	 ; empty?
	beq.s	ifi_down
	assert	sbt_file,sbt_blok-2
	cmp.l	sbt_file(a1),d4 	 ; right file / block?
	beq.s	ifi_done

ifi_down
	cmp.l	d1,a2			 ; at bottom?
	bgt.s	ifi_ckdn		 ; ... no
	move.l	d2,a2
ifi_ckdn
	subq.l	#8,a2
	cmp.l	a1,a2			 ; all the way round?
	beq.s	ifi_exit		 ; ... yes

	moveq	#sbt.driv,d0		 ; mask of drive bits
	and.b	sbt_stat(a2),d0
	cmp.b	d0,d7			 ; right drive?
	bne.s	ifi_loop
	cmp.b	#sbt.mpty,sbt_stat(a2)	 ; empty?
	beq.s	ifi_loop
	assert	sbt_file,sbt_blok-2
	cmp.l	sbt_file(a2),d4 	 ; right file / block?
	bne.s	ifi_loop

	move.l	a2,a1			 ; it's this one

ifi_done
	addq.l	#4,sp			 ; zero return
	move.l	a1,chn_csb(a0)		 ; current slave block
	move.l	a1,d0
	sub.l	sys_sbtb(a6),d0
	lsl.l	#sbt.shft,d0
	move.l	d0,a2
	add.l	a6,a2			 ; actual slave block pointer
	moveq	#0,d0
	movem.l (sp)+,ifi.reg
	rts


ifi_exit
	moveq	#-1,d0			 ; set return -ve (d0 will be reset
	movem.l (sp)+,ifi.xreg
	rts
	end
