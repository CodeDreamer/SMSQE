; DV3 Atari Find Partition	V3.00	 1992	Tony Tebby

	section dv3

	xdef	hd_fpart

	include 'dev8_keys_err'
	include 'dev8_keys_qlwa'
	include 'dev8_keys_atari'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
;+++
; DV3 Atari find partition.
; It starts by loading and examining the root sector.
;
;	d1  r	as d3 but in ths format if required
;	d2  r	size of partition, -1 no partition table
;	d3  r	sector number with partition root
;	d4  r	partition ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return 0 or error
;---
hd_fpart
hfp.reg reg	d5/d6/a1/a2
	movem.l hfp.reg,-(sp)
	lea	hdl_part-1(a3),a1
	moveq	#0,d4
	move.b	(a1,d7.w),d4		 ; partition reqired
	bmi.s	hfp_mchk

	moveq	#0,d3			 ; start at real root
	moveq	#0,d6			 ; offset starts at 0

hfp_rootloop
	lea	hdl_buff(a3),a1 	 ; root sector buffer
	move.l	d3,d0			 ; physical root sector
	moveq	#1,d2
	jsr	hdl_rsint(a3)		 ; read sector
	bne.s	hfp_npart		 ; no partition

	cmp.l	#qwa.id,qwa_id(a1)	 ; qdos map?
	beq.s	hfp_root		 ; ... yes

	lea	art_p0(a1),a1		 ; look from partition 0
	move.l	#art.pchk,d1
	move.l	#art.xflg,d5
	moveq	#4,d2			 ; check four in first section

hfp_partloop
	move.l	d1,d0

	and.l	art_pflg(a1),d0 	 ; partition flag
	cmp.l	d5,d0			 ; extended?
	beq.s	hfp_xlink
	subq.b	#1,d4			 ; partition found
	blt.s	hfp_found
	add.w	#art.plen,a1
	subq.w	#1,d2			 ; extended group yet?
	bne.s	hfp_partloop
	tst.l	d2			 ; all tried?
	beq.s	hfp_mchk
	moveq	#-8,d2
	neg.w	d2			 ; try eight more
	lea	art_pext-4*art.plen(a1),a1
	bra.s	hfp_partloop

hfp_xlink
	move.l	d6,d3
	add.l	art_pstt(a1),d3 	 ; new root
	tst.l	d6			 ; first partition yet?
	bne	hfp_rootloop
	move.l	d3,d6			 ; ... yes, save base of first
	bra	hfp_rootloop

hfp_found
	btst	#art.pval,art_pflg(a1)	 ; partition valid?
	beq.s	hfp_mchk

	move.l	art_psiz(a1),d2 	 ; size of partition
	add.l	art_pstt(a1),d3 	 ; root sector for partition
	move.l	d3,d1
	move.l	d0,d4			 ; partition ID
hfp_ok
	move.l	d3,d1
	moveq	#0,d0
	move.b	d0,hdl_npart(a3)	 ; partition found
hfp_exit
	movem.l (sp)+,hfp.reg
	rts
hfp_mchk
	moveq	#err.mchk,d0
hfp_npart
	st	hdl_npart(a3)		 ; set no partition found
	bra.s	hfp_exit

hfp_root
	tst.b	d4			 ; partition must be 0
	bne.s	hfp_mchk
	moveq	#-1,d2			 ; size unknown
	move.l	#art.qflg,d4		 ; for QDOS disk
	bra.s	hfp_ok

	end
