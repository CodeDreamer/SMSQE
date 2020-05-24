; DV3 format routine (QDOS) entry	    V3.00	    1992 Tony Tebby
; 2020-04-07   3.01  set correct name in drive defn block  (mk+wl)
	section dv3

	xdef	dv3_format

	xref	dv3_locfd
	xref	dv3_sbclr
	xref	dv3_density

	include 'dev8_dv3_keys'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; DV3 SMSQ format routine
;
;	d1 c  s drive number
;	a1 c  s pointer to name
;	a3 c  u pointer to linkage
;	a4 c  u pointer to drive definition
;---
dv3_format
	moveq	#0,d7
	move.b	ddf_drid(a4),d7 	 ; drive ID
	swap	d7
	move.w	d1,d7			 ; drive number

	tst.b	ddf_nropen(a4)		 ; any files open on this medium?
	bne.l	dqf_inus		 ; ... yes

dqf_format
	jsr	ddl_fslave(a3)		 ; find slave block
	bne.l	dqf_rts 		 ; ... oops
	move.l	a3,ddf_ptddl(a4)	 ; set driver linkage

	moveq	#ddf.qdos,d0		 ; format type required
	jsr	dv3_locfd		 ; locate and set format dependent vector
	bne.l	dqf_rts 		 ; none loaded

	jsr	dv3_sbclr		 ; clear all slave blocks
	bsr.l	dqf_flush

	moveq	#0,d1			 ; assume no flag
	moveq	#0,d4			 ; no heads specified
	st	ddf_density(a4) 	 ; and default density
	clr.w	ddf_heads(a4)		 ; unknown number of heads

	move.w	(a1)+,d3		 ; length of name
	subq.w	#5,d3
	addq.l	#5,a1			 ; the real name

	moveq	#'*',d0
	cmp.b	-1(a1,d3.w),d0		 ; ends in *?
	beq.s	dqf_ss			 ; ... yes, single sided
	cmp.b	-2(a1,d3.w),d0		 ; ends in *c?
	bne.s	dqf_sname		 ; ... no, set name

	subq.w	#2,d3			 ; name is shorter now
	move.b	1(a1,d3.w),d0
	jsr	dv3_density		 ; get density
	bne.s	dqf_iname
	move.b	d2,ddf_density(a4)	 ; set required density
	bra.s	dqf_sname

dqf_ss
	subq.w	#1,d3
	move.b	#ddf.dd,ddf_density(a4)  ; single sided double density
	moveq	#1,d4

dqf_sname
	lea	ddf_mname(a4),a0	 ; set medium name
	cmp.b	#ddf.mnlen,d3
	ble.s	dqf_nlen		 ; 20 or fewer characters
	moveq	#ddf.mnlen,d3
dqf_nlen
	move.w	d3,(a0)+
	bra.s	dqf_snlend
dqf_snloop
	move.b	(a1)+,(a0)+		 ; set the name
dqf_snlend
	dbra	d3,dqf_snloop

	assert	ddf.mnew,0
	sf	ddf_mstat(a4)		 ; status is new

	sf	ddf_wprot(a4)		 ; not write protected

	lea	ddf_fsave(a4),a0	 ; clear a bit
	moveq	#(ddf_fhlen+4-ddf_fsave)/2-1,d0
dqf_cloop
	clr.w	(a0)+
	dbra	d0,dqf_cloop

	move.w	d4,ddf_heads(a4)	 ; but save number of heads if = 1

	moveq	#ddf.full,d0
	jsr	ddl_mformat(a3) 	 ; FORMAT
	bne.s	dqf_rts
	bsr.s	dqf_flush
	move.l	ddf_agood(a4),d1
	move.l	ddf_atotal(a4),d2	 ; set returns
	cmp.b	#ddl.flp,ddl_mtype(a3)	 ; floppy disk?
	bne.s	dqf_ok			 ; ... no
	move.w	ddf_asect(a4),d0
	mulu	d0,d1			 ; yes, return sectors
	mulu	d0,d2
dqf_ok
	moveq	#0,d0
dqf_rts
	rts

dqf_iname
	moveq	#err.inam,d0
	rts
dqf_erriu
	moveq	#err.fdiu,d0
	rts

dqf_flush
	jsr	ddl_dflush(a3)		 ; flush map!
	addq.l	#-err.nc,d0
	beq.s	dqf_flush		 ; until complete
	rts

; This lot is here for Conqueror only

dqf_inus
	tst.b	ddf_ftype(a4)		 ; direct access?
	bpl.s	dqf_erriu		 ; ... no
	sf	ddf_ftype(a4)
	sf	ddf_nropen(a4)		 ; make it normal
	bsr	dqf_format		 ; format
	st	ddf_ftype(a4)		 ; mark direct access
	move.l	ddf_chnlst(a4),a0
	move.l	a4,d3c_ddef(a0) 	 ; set address in drive definition
	addq.b	#1,ddf_nropen(a4)
	clr.l	ddf_fsave(a4)		 ; no saved format
	rts

	end
