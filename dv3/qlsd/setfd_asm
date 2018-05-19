; DV3 Set Format Dependent Vector     V3.00	      1992 Tony Tebby

	section dv3

	xdef	dv3_setfd

	xref	dv3_svec
	xref	dv3_fdvec
	xref	dv3_logphys

	xref	cinvi

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_err'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Set the Format Dependent Vector
;
;	d1-d7 p
;	a0-a1 p
;	a2 c  p pointer to format dependent vector
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to definition block
;	a5    p
;	a6 c  p pointer to system variables
;
;	Status return standard
;---
dv3_setfd
dsf.reg reg	d1/d2/a0/a1/a2/a5
	movem.l dsf.reg,-(sp)

	move.b	fdv_ftype(a2),ddf_ftype(a4) ; set constants
	move.b	fdv_dosd(a2),ddf_dosd(a4)
	move.b	fdv_zalloc(a2),ddf_zalloc(a4)

	lea	fdv_itop(a2),a5 	 ; set translate tables
	lea	ddf_itop(a4),a2
	bsr.s	dsf_vec
	move.l	a0,(a2)+
	bsr.s	dsf_vec
	move.l	a0,(a2)+
	bsr.s	dsf_vec
	move.l	a0,(a2)+

	lea	ddf_mcheck(a4),a0	 ; fill in vectors here
	lea	dv3_fdvec,a2		 ; default vector
	moveq	#(fdv_lvec-fdv_mcheck)/2-2,d2 ; include last vector but allow
					       ; for extra logphys and dbra
	moveq	#(fdv_logphys-fdv_mcheck)/2,d1 ; first slice includes logphys
	tst.b	ddl_cylhds(a3)		 ; cylinder / head / side?
	beq.s	dsf_svec		 ; ... no
	subq.w	#1,d1			 ; ... yes, put logphys in second slice
dsf_svec
	sub.w	d1,d2			 ; the remainder
	jsr	dv3_svec		 ; setup first slice of vectors
	addq.w	#2,a5			 ; skip one logphys
	move.w	d2,d1
	jsr	dv3_svec		 ; setup second slice of vectors

	jsr	cinvi			 ; clear instruction cache on SGC
	moveq	#0,d0
	movem.l (sp)+,dsf.reg
	rts

dsf_vec
	move.l	a5,a0			 ; address
	move.w	(a5)+,d0
	add.w	d0,a0			 ; rel address
	bne.s	dsf_rts 		 ; ... ok
	sub.l	a0,a0			 ; ... was zero
dsf_rts
	rts

	end
