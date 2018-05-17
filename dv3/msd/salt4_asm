; DV3 MSDOS Sector Allocate, Locate and Truncate  V3.00        1993 Tony Tebby

	section dv3

	xdef	msd_sal4
	xdef	msd_slc4
	xdef	msd_str4

	xref	msd_setmu

	include 'dev8_keys_DOS'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
	include 'dev8_mac_xword'

;+++
; DV3 MSDOS allocate new group of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p next file group
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
msd_sal4
ms4a.reg reg	d1/d2/a2
	movem.l ms4a.reg,-(sp)

	subq.l	#1,d2
	blt.s	ms4a_new		 ; new file

	bsr.s	ms4l_find		 ; find previous group
	blt.s	ms4a_exit

	move.l	d0,d2			 ; keep previus group
	bsr.s	ms4a_vg 		 ; looking from d0
	ble.s	ms4a_drfl		 ; ... not found

	add.l	d2,d2			 ; index the FAT for previous group
	add.l	d2,a2

	move.w	d0,d1
	xword	d1
	move.w	d1,(a2) 		 ; link new one into list
	jsr	msd_setmu

ms4a_exit
	movem.l (sp)+,ms4a.reg
	rts

ms4a_drfl
ms4n_drfl
	moveq	#err.drfl,d0
	bra.s	ms4a_exit		 ; drive full

;+++
; DV3 MSDOS allocate first group of new file
;
;	d0  r	logical drive group
;	d6 c  u file ID (0 if not allocated)
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
;;;msd_snw4
;;;	   movem.l ms4a.reg,-(sp)
ms4a_new
	bsr.s	ms4a_vg0		 ; look for vacant sector from start
	ble.s	ms4n_drfl		 ; ... not found
	move.w	d0,d6			 ; use first group number as file ID
	move.l	d0,d3c_flid(a0) 	 ; and set ID in channel

	movem.l (sp)+,ms4a.reg
	rts


; look for vacant group

ms4a_vg
	bsr.s	ms4a_vgx		 ; look for vacant group from d0
	bgt.s	ms4a_rts		 ; .. ok

ms4a_vg0
	moveq	#1,d0			 ; try from start
ms4a_vgx
	move.l	mdf_fat(a4),a2		 ; base of FAT
	addq.l	#1,d0			 ; + 1 entry
	add.l	d0,a2			 ; + group offset
	add.l	d0,a2

	moveq	#0,d1
	move.w	mdf_fent(a4),d1 	 ; number to search
	subq.l	#1,d1
	sub.l	d0,d1
	blt.s	ms4a_nv 		 ; ... none

	assert	dos.free,0
ms4a_vloop
	tst.w	(a2)+			 ; free?
	dbeq	d1,ms4a_vloop
	bne.s	ms4a_nv 		 ; none

	move.w	#dos.eff4,-(a2) 	 ; new end of file

	subq.w	#1,ddf_afree+2(a4)
	jsr	msd_setmu

	move.l	a2,d0
	move.l	mdf_fat(a4),a2		 ; base of FAT
	sub.l	a2,d0
	lsr.l	#1,d0			 ; new group number
ms4a_rts
	rts

ms4a_nv
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
msd_slc4
ms4l.reg reg	d1/d2/a2
	movem.l ms4l.reg,-(sp)
	bsr.s	ms4l_find
	movem.l (sp)+,ms4l.reg
	rts

; routine to find a group in the map

ms4l_find
	tst.w	d0			 ; any known group?
	beq.s	ms4l_start
	cmp.w	d1,d2			 ; req sector before or after known?
	bhi.s	ms4l_look		 ; here or after
	beq.s	ms4l_d0

ms4l_start
	move.l	d6,d0			 ; zeroth cluster
	moveq	#0,d1

ms4l_look
	move.l	mdf_fat(a4),a2
	sub.w	d1,d2			 ; go this far
	beq.s	ms4l_d0
	subq.w	#1,d2

ms4l_ckl
	move.l	d0,d1			 ; cluster in long
	add.l	d1,d1			 ; index table
	move.w	(a2,d1.l),d0		 ; next
	xword	d0
	dbeq	d2,ms4l_ckl
	beq.s	ms4l_mchk		 ; ... empty sector

	cmp.w	ddf_atotal+2(a4),d0	 ; valid sector?
	blo.s	ms4l_d0 		 ; ... yes

ms4l_mchk
	moveq	#err.mchk,d0		 ; oops, not found
ms4l_d0
	tst.l	d0
	rts

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
msd_str4
	tst.l	d6			 ; any sectors allocated at all?
	bne.s	ms4t_do 		 ; ... yes
	moveq	#0,d0
	rts

ms4t_do
ms4t.reg reg	d1/d2/d3/a1/a2
	movem.l ms4t.reg,-(sp)

	subq.l	#1,d2			 ; previous sector
	blt.s	ms4t_start		 ; ... none

	bsr.s	ms4l_find		 ; find previous
	blt.s	ms4t_exit

	move.l	d0,d1
	add.l	d1,d1			 ; index table
	move.w	(a2,d1.l),d0		 ; ... next group
	xword	d0
	move.w	#dos.eff4,(a2,d1.l)	 ; set new end of file
	move.l	d1,d2			 ; check for sector update
	bra.s	ms4t_sa1

ms4t_start
	move.l	d6,d0			 ; start at beginning of file
	moveq	#0,d6			 ; no ID now
	move.l	d6,d3c_flid(a0) 	 ; nor in channel block

	move.l	d0,d2
	add.l	d2,d2			 ; our check for update

ms4t_sa1
	move.l	mdf_fat(a4),a1
	move.l	ddf_smask(a4),d3
	not.l	d3
	and.l	d3,d2			 ; just the sector
	bra.s	ms4t_cknext

ms4t_loop
	move.l	d0,d1
	add.l	d1,d1
	move.w	(a1,d1.l),d0		 ; next group
	beq.s	ms4t_mchk		 ; ... oops
	xword	d0
	clr.w	(a1,d1.l)		 ; clear it
	addq.w	#1,ddf_afree+2(a4)	 ; ... one more sector free

	and.l	d3,d1			 ; sector the same as previous?
	cmp.l	d2,d1
	beq.s	ms4t_cknext

	bsr.s	ms4t_upd		 ; mark updated

	move.l	d1,d2			 ; our next check

ms4t_cknext
	cmp.w	ddf_atotal+2(a4),d0	 ; valid next sector?
	blo.s	ms4t_loop		 ; ... yes

	cmp.w	#dos.eof4,d0		 ; end of file?
	blo.s	ms4t_mchk		 ; ... no

	move.l	d1,d2
	bsr.s	ms4t_upd		 ; last sector updated

	moveq	#0,d0
ms4t_exit
	movem.l (sp)+,ms4t.reg
	rts

ms4t_mchk
	moveq	#err.mchk,d0
	bra.s	ms4t_exit
	rts

ms4t_upd
	lea	(a1,d2.l),a2		 ; this sector updated
	jmp	msd_setmu

	end
