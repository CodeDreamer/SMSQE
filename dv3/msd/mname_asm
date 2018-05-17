; DV3 MSDOS Find Medium Name	  V3.00 	  1993 Tony Tebby

	section dv3

	xdef	msd_mname

	include 'dev8_keys_dos'
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_msd_keys'
;+++
; DV3 MSDOS Find Medium Name
;
;	a1  r	pointer to medium name
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return zero or err.itnf
;
;---
msd_mname
	move.l	d1,-(sp)
	move.l	mdf_rdir(a4),a1 	 ; base of root directory
	move.l	ddf_rdlen(a4),d0
	lsr.l	#dos.dres,d0		 ; entries to check
	subq.w	#1,d0
mmn_loop
	tst.b	(a1)			 ; empty?
	beq.s	mmn_nf			 ; ... yes
	cmp.b	#dos.dele,(a1)		 ; deleted?
	beq.s	mmn_next		 ; ... yes
	moveq	#$f,d1			 ; check least sig 4 bits only magic!!
	and.b	dos_attr(a1),d1
	subq.b	#dos.vol,d1		 ; volume name?
	beq.s	mmn_ok
mmn_next
	add.w	#dos.drel,a1
	dbra	d0,mmn_loop

mmn_nf
	move.l	(sp)+,d1
	moveq	#err.itnf,d0
	rts

mmn_ok
	move.l	(sp)+,d1
	moveq	#0,d0
	rts


	end
