; DV3 MSDOS Find Directory Length from FAT  V3.00	    1993 Tony Tebby

	section dv3

	xdef	msd_drlen

	include 'dev8_keys_dos'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_mac_xword'
	include 'dev8_mac_assert'
;+++
; DV3 MSDOS Find Directory Length
;
;	d0 cr	ID / length of directory (bytes)
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return arbitrary
;
;---
msd_drlen
mdl.reg reg	d1/d2/a2
	movem.l mdl.reg,-(sp)

	move.l	mdf_fat(a4),a2		 ; base of FAT

	moveq	#0,d2			 ; count zero
	tst.w	d0			 ; any directory at all?
	beq.s	mdl_exit

	assert	ddf.msd3,0
	tst.b	ddf_stype(a4)		 ; format subtype
	bne.s	mdl4_set		 ; 4 nibble, the easy case

mdl3_loop
	addq.w	#1,d2
	move.w	d0,d1			 ; cluster in word
	add.w	d0,d1			 ; index table
	add.w	d0,d1
	lsr.w	#1,d1
	bcs.s	mdl3_ck2		 ; odd group
	moveq	#$f,d0
	and.b	1(a2,d1.w),d0		 ; ms nibble
	lsl.w	#8,d0
	move.b	(a2,d1.w),d0		 ; ls 2 nibbles
	tst.w	d0
	beq.s	mdl_exit		 ; oops
	cmp.w	#dos.eof3,d0		 ; end of file
	blo.s	mdl3_loop
	bra.s	mdl_exit

mdl3_ck2
	move.b	1(a2,d1.w),d0		 ; ms 2 nibbles
	lsl.w	#8,d0
	move.b	(a2,d1.w),d0		 ; ls nibble
	lsr.w	#4,d0
	beq.s	mdl_exit		 ; oops
	cmp.w	#dos.eof3,d0		 ; end of file
	blo.s	mdl3_loop
	bra.s	mdl_exit

mdl4_set
mdl4_loop
	addq.w	#1,d2
	move.l	d0,d1
	add.l	d1,d1
	move.w	(a2,d1.l),d0		 ; next group
	beq.s	mdl_exit		 ; ... oops
	xword	d0
	cmp.w	#dos.eof4,d0
	blo.s	mdl4_loop


mdl_exit
	move.w	ddf_asize(a4),d0
	mulu	d2,d0			 ; return length
	moveq	#2*dos.drel,d2
	sub.l	d2,d0			 ; less the two spurious entries
	movem.l (sp)+,mdl.reg
	rts
	end
