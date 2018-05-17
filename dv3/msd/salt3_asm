; DV3 MSDOS Sector Allocate, Locate and Truncate  V3.00        1993 Tony Tebby

	section dv3

	xdef	msd_sal3
	xdef	msd_slc3
	xdef	msd_str3

	xref	msd_setmu

	include 'dev8_keys_DOS'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; DV3 MSDOS allocate new group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p next file group
;	d6 cr	file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
msd_sal3
ms3a.reg reg	d1/d2/d3/a2
	movem.l ms3a.reg,-(sp)

	subq.w	#1,d2
	blt.s	ms3a_new		 ; new file

	bsr.l	ms3l_find		 ; find previous group
	blt.s	ms3a_exit

	move.l	d0,d2			 ; keep previus group
	bsr.s	ms3a_vg 		 ; looking from d0
	ble.s	ms3a_drfl		 ; ... not found

	move.w	d2,d1
	add.w	d1,d2
	add.w	d1,d2
	ror.l	#1,d2			 ; index the FAT for previous group
	add.w	d2,a2

	move.w	d0,d1
	tst.l	d2			 ; odd cluster
	bpl.s	ms3a_sevn		 ; ... no

	lsl.w	#4,d1
	or.b	#$f,d1
	and.b	d1,(a2)+		 ; lower nibble
	lsr.w	#8,d1
	move.b	d1,(a2) 		 ; upper byte
	bra.s	ms3a_upd

ms3a_sevn
	move.b	d1,(a2)+		 ; lower byte
	lsr.w	#8,d1
	or.b	#$f0,d1
	and.b	d1,(a2) 		 ; upper nibble

ms3a_upd
	bsr.l	ms3_setmu

ms3a_exit
	movem.l (sp)+,ms3a.reg
	rts

ms3a_drfl
ms3n_drfl
	moveq	#err.drfl,d0
	bra.s	ms3a_exit		 ; drive full

;+++
; DV3 MSDOS allocate first group of new file
;
;	d0  r	logical drive group
;	d6  r	file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
;;;msd_snw3
;;;	   movem.l ms3a.reg,-(sp)
ms3a_new
	bsr.s	ms3a_vg0		 ; look for vacant sector from start
	ble.s	ms3n_drfl		 ; ... not found
	move.w	d0,d6			 ; use first group number as file ID
	move.l	d0,d3c_flid(a0) 	 ; and set ID in channel

	movem.l (sp)+,ms3a.reg
	rts


; look for vacant group

ms3a_vg
	bsr.s	ms3a_vgx		 ; look for vacant group from d0
	bgt.s	ms3a_rts		 ; .. ok

ms3a_vg0
	moveq	#1,d0			 ; try from start
ms3a_vgx
	move.l	mdf_fat(a4),a2		 ; base of FAT
	addq.w	#1,d0			 ; + 1 entry

	move.w	mdf_fent(a4),d1 	 ; number to search
	sub.w	d0,d1
	ble.s	ms3a_nv 		 ; ... none

	move.w	d0,d3
	add.w	d3,d0
	add.w	d3,d0
	lsr.w	#1,d0
	add.w	d0,a2			 ; initial map position

	bcs.s	ms3a_vl2		 ; ... odd start

	subq.w	#1,d1			 ; we've done one more this route
	assert	dos.free,0
ms3a_vloop
	tst.b	(a2)+			 ; could this be empty?
	bne.s	ms3a_vl2		 ; ... no
	moveq	#$f,d0
	and.b	(a2),d0 		 ; is it?
	beq.s	ms3a_vs1		 ; ... yes
ms3a_vl2
	moveq	#$fffffff0,d0		 ; the other nibble
	and.b	(a2)+,d0
	or.b	(a2)+,d0		 ; with the next two nibbles
	beq.s	ms3a_vs2
	subq.w	#2,d1
	bgt.s	ms3a_vloop		 ; carry on
	blt.s	ms3a_nv 		 ; no vacancy

	tst.b	(a2)+			 ; could last one be empty?
	bne.s	ms3a_nv 		 ; ... no
	moveq	#$f,d0
	and.b	(a2),d0 		 ; is it?
	bne.s	ms3a_nv 		 ; ... no

ms3a_vs1
	assert	dos.eff3,$fff
	or.b	#$f,(a2)		 ; set end of file
	st	-(a2)
	bra.s	ms3a_vset
ms3a_vs2
	assert	dos.eff3,$fff
	st	-(a2)
	or.b	#$f0,-(a2)		 ; set end of file

ms3a_vset
	subq.w	#1,ddf_afree+2(a4)	 ; one fewer alloc
	addq.l	#1,a2
	bsr.s	ms3_setmu

	move.l	a2,d0
	move.l	mdf_fat(a4),a2		 ; base of FAT
	sub.l	a2,d0
	add.w	d0,d0
	divu	#3,d0			 ; new group number
	ext.l	d0
ms3a_rts
	rts

ms3a_nv
	moveq	#0,d0			 ; no vacant groups
	rts

;+++
; DV3 MSDOS locate group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p file group required
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
msd_slc3
ms3l.reg reg	d1/d2/d3/a2
	movem.l ms3l.reg,-(sp)
	bsr.s	ms3l_find
	movem.l (sp)+,ms3l.reg
	rts

; routine to find a group in the map

ms3l_find
	tst.w	d0			 ; any known group?
	beq.s	ms3l_start
	cmp.w	d1,d2			 ; req sector before or after known?
	bhi.s	ms3l_look		 ; here or after
	beq.s	ms3l_d0

ms3l_start
	move.l	d6,d0			 ; zeroth cluster
	moveq	#0,d1

ms3l_look
	move.l	mdf_fat(a4),a2
	sub.w	d1,d2			 ; go this far
	beq.s	ms3l_d0
	subq.w	#1,d2

ms3l_ckl
	move.w	d0,d1			 ; cluster in word
	add.w	d0,d1			 ; index table
	add.w	d0,d1
	lsr.w	#1,d1
	bcs.s	ms3l_ck2		 ; odd group
	moveq	#$f,d0
	and.b	1(a2,d1.w),d0		 ; ms nibble
	lsl.w	#8,d0
	move.b	(a2,d1.w),d0		 ; ls 2 nibbles
	bne.s	ms3l_cke
	tst.w	d0
	bra.s	ms3l_cke

ms3l_ck2
	move.b	1(a2,d1.w),d0		 ; ms 2 nibbles
	lsl.w	#8,d0
	move.b	(a2,d1.w),d0		 ; ls nibble
	lsr.w	#4,d0

ms3l_cke
	dbeq	d2,ms3l_ckl
	beq.s	ms3l_mchk		 ; ... empty sector

	cmp.w	ddf_atotal+2(a4),d0	 ; valid sector?
	blo.s	ms3l_d0 		 ; ... yes

ms3l_mchk
	moveq	#err.mchk,d0		 ; oops, not found
ms3l_d0
	tst.l	d0
	rts

;--- map updated utility, a2 points to second byte    ???????????

ms3_setmu
	move.l	a2,d3
	sub.l	mdf_fat(a4),d3
	and.l	ddf_smask(a4),d3	 ; group spans sector boundary?
	bne.l	msd_setmu		 ; ... no

	subq.l	#1,a2
	jsr	msd_setmu		 ; yes, do previous
	addq.l	#1,a2
	jmp	msd_setmu		 ; then this

;+++
; DV3 MSDOS truncate sector allocation
;
;	d0 cr	known logical drive group / error status
;	d1 c  p file group of known drive group
;	d2 c  p first file group to free
;	d6 c  u file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;---
msd_str3
	tst.l	d6			 ; any groups allocated?
	bne.s	ms3t_do 		 ; ... yes
	moveq	#0,d0
	rts

ms3t_do
ms3t.reg reg	d1/d2/d3/a1/a2
	movem.l ms3t.reg,-(sp)

	subq.w	#1,d2			 ; previous sector
	blt.s	ms3t_start		 ; ... none

	bsr.s	ms3l_find		 ; find previous
	blt.l	ms3t_exit

	move.w	d0,d1			 ; end of file cluster in word
	add.w	d0,d1			 ; index table
	add.w	d0,d1
	lsr.w	#1,d1
	add.w	d1,a2
	bcs.s	ms3t_se2		 ; odd group

	moveq	#$f,d0
	and.b	1(a2),d0		 ; ms nibble
	lsl.w	#8,d0
	move.b	(a2),d0 		 ; + ls 2 nibbles is next group

	st	(a2)+			 ; mark end of file
	or.b	#$f,(a2)
	bsr.s	ms3_setmu
	bra.s	ms3t_sa1

ms3t_se2
	move.b	1(a2),d0		 ; ms 2 nibbles
	lsl.w	#8,d0
	move.b	(a2),d0 		 ; ls nibble of next
	lsr.w	#4,d0

	or.b	#$f0,(a2)+		 ; set end of file
	st	(a2)
	bsr.s	ms3_setmu
	bra.s	ms3t_sa1

ms3t_start
	move.l	d6,d0			 ; start at beginning of file
	moveq	#0,d6			 ; no file now
	move.l	d6,d3c_flid(a0) 	 ; at all!!

ms3t_sa1
	move.l	mdf_fat(a4),a1
	bra.s	ms3t_cknext

ms3t_loop
	move.w	d0,d1			 ; cluster in word
	add.w	d0,d1			 ; index table
	add.w	d0,d1
	lsr.w	#1,d1
	lea	1(a1,d1.w),a2
	bcs.s	ms3t_fr2		 ; odd group
	moveq	#$f,d0
	and.b	(a2),d0 		 ; ms nibble
	lsl.w	#8,d0
	move.b	-(a2),d0		 ; ls 2 nibbles

	sf	(a2)+			 ; free it
	and.b	#$f0,(a2)

	bra.s	ms3t_upd

ms3t_fr2
	move.b	(a2),d0 		 ; ms 2 nibbles
	lsl.w	#8,d0
	move.b	-(a2),d0		 ; ls nibble
	lsr.w	#4,d0

	and.b	#$0f,(a2)+		 ; set free
	sf	(a2)

ms3t_upd
	addq.w	#1,ddf_afree+2(a4)	 ; one more alloc
	bsr.l	ms3_setmu		 ; mark updated

ms3t_cknext
	cmp.w	ddf_atotal+2(a4),d0	 ; valid next sector?
	blo.s	ms3t_loop		 ; ... yes

	cmp.w	#dos.eof3,d0		 ; end of file?
	blo.s	ms3t_mchk		 ; ... no

ms3t_exok
	moveq	#0,d0
ms3t_exit
	movem.l (sp)+,ms3t.reg
	rts

ms3t_mchk
	moveq	#err.mchk,d0
	bra.s	ms3t_exit
	rts

	end
