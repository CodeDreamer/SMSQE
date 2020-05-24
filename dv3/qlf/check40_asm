; DV3 QL Check Format		   V3.01	   1992 Tony Tebby
;
; 2020-03-07  3.01  Better handling of medium name (MK+wl)

	section dv3

	xdef	qlf_check

	xref	qlf_tbwa

	xref	dv3_psector
	xref	dv3_change
	xref	dv3_setfd
	xref	dv3_redef

	include 'dev8_keys_ql5b'
	include 'dev8_keys_qlwa'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_hdr'
	include 'dev8_mac_assert'
;+++
; DV3 QL5B / QLWA Check Format
;
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to root sector
;	a2    p
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	this is a clean routine
;	status return standard or positive (not recognised)
;
;---
qlf_check
qck.regc reg	d1/d2/a2/a5
qck.regx reg	d3-d4/d6/a0/a1
	movem.l qck.regc,-(sp)

	tst.b	ddf_mstat(a4)		 ; valid medium?
	beq.s	qck_changed		 ; ... no

	lea	qdf_map(a4),a5
	move.l	a1,a2
	moveq	#qwa_uchk,d1
	cmp.l	#qwa.id,qwa_id(a5)	 ; QLWA?
	beq.s	qck_scheck		 ; ... yes
	moveq	#q5a_mupd,d1
qck_scheck
	move.w	d1,d2			 ; check up to and including count
	lsr.w	#2,d2
	move.w	d2,d0
qck_loop
	cmpm.l	(a5)+,(a2)+
	dbne	d0,qck_loop
	bne.s	qck_updated
qck_ckok
	st	ddf_mstat(a4)

	moveq	#0,d0
qckc_exit
	movem.l (sp)+,qck.regc
	rts

qck_updated
	lea	qdf_map(a4),a5		 ; retry check
	move.l	a1,a2
	addq.l	#1,(a1,d1.l)		 ; update check
	move.w	d2,d0			 ; check up to and including count
qck_uloop
	cmpm.l	(a5)+,(a2)+
	dbne	d0,qck_uloop
	sne	d0

	subq.l	#1,(a1,d1.l)		 ; undo update check
	tst.b	d0
	beq.s	qck_ckok		 ; it was OK

qck_changed
	jsr	dv3_change		 ; change OK
	bne.s	qckc_exit		 ; ... no

	movem.l qck.regx,-(sp)
	tst.b	ddl_buff(a3)		 ; buffered?
	beq.l	qck_bad 		 ; ... no

	assert	ddf.ql5a,0
	sf	ddf_stype(a4)		 ; clear subtype (ql5a)
	move.l	q5a_id(a1),d0		 ; ID long word
	sub.l	#q5a.id,d0		 ; QL5A?
	beq.l	qck_5set		 ; ... yes
	subq.l	#1,d0			 ; QL5B?
	beq.l	qck_5b
	cmp.l	#qwa.id,qwa_id(a1)	 ; QLWA?
	bne.l	qck_bad 		 ; ... no

	lea	qlf_tbwa,a2		 ; reset table
	jsr	dv3_setfd

	addq.b	#ddf.qlwa,ddf_stype(a4)  ; subtype 2

	move.w	ddf_slen(a4),d0 	 ; sector length
	move.w	qwa_sctm(a1),d2 	 ; sectors in map
	mulu	d2,d0			 ; size of map
	add.l	#qdf_map,d0
	move.l	d0,d1			 ; keep size
	jsr	dv3_redef		 ; re-allocate drive definition
	bne.l	qck_exit		 ; ... oops

	lea	(a4,d1.l),a2
	move.l	a2,qdf_mtop(a4) 	 ; map top
	move.w	d2,qdf_msect(a4)	 ; number of map sectors

	lea	qwa_name(a1),a2 	 ; for QLWA
	lea	ddf_mname(a4),a0
	moveq	#ddf.mnlen,d6		 ; max length
	move.w	(a2)+,d0
	cmp.w	d0,d6
	bge.s	qck_sz
	move.w	d6,d0			 ; limit length
qck_sz	move.w	d0,(a0)+
	bra.s	qck_lp1
qck_cp1 move.b	(a2)+,(a0)+
qck_lp1 dbf	d0,qck_cp1

	clr.w	ddf_rdid(a4)
	move.w	qwa_root(a1),ddf_rdid+2(a4) ; root directory ID
	moveq	#-hdr.len,d0
	add.l	qwa_rlen(a1),d0
	move.l	d0,ddf_rdlen(a4)	  ; and length

	assert	qwa_intl,qwa_sctg-2,qwa_sctt-4,qwa_trkc-6
	movem.w qwa_intl(a1),d0/d1/d3/d4

	assert	ddf_strk,ddf_sintlv-4,ddf_sskew-6

	lea	ddf_strk(a4),a5 	 ; start filling in standard info
	move.w	d3,(a5)+		 ; sectors per track
	addq.l	#2,a5
	move.w	d0,(a5)+		 ; physical interleave
	clr.w	(a5)+			 ; skew

	assert	ddf_sintlv+4,ddf_heads,ddf_scyl-2,ddf_asect-4
	move.w	d4,(a5)+		 ; heads
	mulu	d3,d4
	move.w	d4,(a5)+		 ; sectors per cylinder
	move.w	d1,(a5)+		 ; allocation size

	assert	ddf_asect,ddf_asize-2,ddf_atotal-4,ddf_agood-8,ddf_afree-$c
	mulu	ddf_slen(a4),d1
	move.w	d1,(a5)+

	move.w	qwa_ngrp(a1),d1
	move.l	d1,(a5)+		 ; total
	move.l	d1,(a5)+		 ; and good
	move.w	qwa_fgrp(a1),d1
	move.l	d1,(a5)+

	move.l	ddf_psoff(a4),ddf_lsoff(a4) ; first sector
	move.w	d2,d1			 ; number of sectors to load
	lea	qdf_map(a4),a1		 ; ... here

	moveq	#0,d3
	moveq	#0,d2
	move.b	ddl_msect(a3),d2	 ; multple sector reads supported?
	bne.s	qck_ldmul		 ; ... yes
	moveq	#1,d2			 ; ... no, load the map sector by sector
qck_ldmul
	cmp.w	d2,d1			 ; load max sectors?
	bhs.s	qck_lmdo		 ; ... yes
	move.w	d1,d2
qck_lmdo
	move.l	d3,d0
	jsr	dv3_psector		 ; physical sector
	jsr	ddl_rsect(a3)		 ; read all of map
	bne.s	qck_lexit		 ; bad
	add.l	d2,d3
	move.w	d2,d0
	mulu	ddf_slen(a4),d0
	add.l	d0,a1			 ; move pointer on
	sub.w	d2,d1
	bhi.s	qck_ldmul

	moveq	#0,d0
qck_lexit
	bra.l	qck_done		 ; ... ok


qck_5b
	addq.b	#ddf.ql5b,ddf_stype(a4)  ; subtype 1

qck_5set
	tst.b	ddf_lba(a4)		 ; cylinder head side
	beq.l	qck_bad 		 ; ... no

	moveq	#q5a_gmap-1,d1
	moveq	#0,d0
	move.w	q5a_totl(a1),d0 	 ; total sectors
	divu	q5a_allc(a1),d0 	 ; total groups
	add.w	d0,d1			 ; map space required
	add.w	d0,d1
	add.w	d0,d1			 ; 3 bytes / group
	move.l	d1,d0			 ; size of map - 1
	divu	ddf_slen(a4),d0
	addq.w	#1,d0			 ; room for complete sectors
	move.w	d0,d2
	mulu	ddf_slen(a4),d0
	add.l	#qdf_map,d0
	jsr	dv3_redef		 ; re-allocate drive definition
	bne.l	qck_exit		 ; ... oops

	lea	qdf_map+1(a4),a2
	add.l	d1,a2
	move.l	a2,qdf_mtop(a4) 	 ; map top
	move.w	d2,qdf_msect(a4)	 ; number of map sectors

	lea	q5a_mnam(a1),a2
	lea	ddf_mname+2(a4),a0	; point after length word
	move.l	(a2)+,(a0)+		; copy all of name
	move.l	(a2)+,(a0)+
	move.w	(a2)+,(a0)+		; max name is 10 long + 2 bytes rnd

	moveq	#9,d0			; max nbrs of chars -1 (dbf)
	moveq	#' ',d6 		; string is space filled
cmp_lp	cmp.b	-(a2),d6
	bne.s	cont1
	dbf	d0,cmp_lp
cont1	addq.w	#1,d0
	move.w	d0,ddf_mname(a4)

	moveq	#hdr.len,d6

	move.l	#ddf.rdid,ddf_rdid(a4)	 ; root directory ID
	move.w	q5a_eodr(a1),d0  ; nr sectors
	mulu	ddf_slen(a4),d0
	add.w	q5a_eodr+2(a1),d0 : + sector pos
	sub.l	d6,d0
	move.l	d0,ddf_rdlen(a4)	 ; and length

	assert	q5a_strk,q5a_scyl-2,q5a_trak-4,q5a_allc-6
	movem.w q5a_strk(a1),d1/d2/d3/d4

	assert	ddf_strk,ddf_sintlv-4,ddf_sskew-6

	lea	ddf_strk(a4),a5 	 ; start filling in standard info
	move.w	d1,(a5)+		 ; sectors per track
	addq.l	#2,a5
	clr.l	(a5)+			 ; physical interleave

	assert	ddf_sintlv+4,ddf_heads,ddf_scyl-2,ddf_asect-4
	moveq	#0,d0
	move.w	d2,d0
	divu	d1,d0
	move.w	d0,(a5)+		 ; heads
	move.w	d2,(a5)+		 ; sectors per cylinder
	move.w	d4,(a5)+		 ; allocation size

	assert	ddf_asect,ddf_asize-2
	move.w	ddf_slen(a4),d0
	mulu	d4,d0
	move.w	d0,(a5)+

	moveq	#0,d0
	move.w	q5a_totl(a1),d0 	 ; total sectors
	divu	d4,d0
	move.l	d0,ddf_atotal(a4)

	moveq	#0,d0
	move.w	q5a_good(a1),d0 	 ; total good
	divu	d4,d0
	move.l	d0,ddf_agood(a4)

	moveq	#0,d0
	move.w	q5a_free(a1),d0 	 ; total free
	divu	d4,d0
	move.l	d0,ddf_afree(a4)

	move.l	ddf_psoff(a4),ddf_lsoff(a4) ; no (additional) offset

qck_ldmap
	move.l	a1,a2
	lea	qdf_map(a4),a1
	move.w	ddf_slen(a4),d0
	lsr.w	#2,d0
	subq.w	#1,d0
qck_cloop
	move.l	(a2)+,(a1)+		 ; copy root sector
	dbra	d0,qck_cloop

	move.l	qdf_mtop(a4),a2 	 ; top of map
	moveq	#0,d1
qck_mloop
	cmp.l	a2,a1
	bge.s	qck_done		 ; no more map to read
	addq.w	#1,d1			 ; next sector
	moveq	#0,d0			 ; of group zero!
	jsr	ddf_logphys(a4)
	moveq	#1,d2
	jsr	ddl_rsect(a3)		 ; read it
	bne.s	qck_exit
	add.w	ddf_slen(a4),a1
	bra.s	qck_mloop

qck_done
	moveq	#hdr.len,d6
	move.l	d6,ddf_fhlen(a4)	 ; common to both formats

	st	ddf_mstat(a4)		 ; ... medium OK now

	moveq	#ddf.change,d0
	bra.s	qck_exit

qck_ok
	moveq	#0,d0
qck_exit
	movem.l (sp)+,qck.regx
	movem.l (sp)+,qck.regc
	rts

qck_bad
	moveq	#ddf.unrec,d0		 ; unrecognised format
	bra.s	qck_exit

	end
