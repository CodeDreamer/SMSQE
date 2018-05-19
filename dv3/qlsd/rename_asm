; DV3 rename file	     V3.00	     1992 Tony Tebby
;
; QDOS compatible version

	section dv3

	xdef	dv3_rename

	xref	dv3_ckro
	xref	dv3_qdit

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_mac_assert'

;+++
; DV3 rename file
; Cheat version: assumes drive name is three characters long
;---
dv3_rename
	tst.b	d3c_ro(a0)		 ; read only?
	bne.s	drn_ro
	move.w	(a1)+,d4		 ; length of filename
	move.l	#$dfdfdfff,d0
	and.l	(a1)+,d0		 ; upper cased name
	sub.w	d7,d0			 ; less drive number
	sub.l	ddl_dnuse+2(a3),d0	 ; should be drive name
	bne.s	drn_iname
	cmp.b	#'_',(a1)+		 ; underscore?
	bne.s	drn_iname
	subq.w	#5,d4			 ; remaining length of name
	ble.s	drn_iname
	cmp.w	#d3c.qnml,d4		 ; too long?
	bls.s	drn_alloc		 ; ... ok

drn_iname
	moveq	#err.inam,d0
	rts

drn_ro
	moveq	#err.rdo,d0
	rts

drn_alloc
	move.l	a0,-(sp)		 ; save old channel block
	moveq	#(d3c_end+d3c.pthl)/4,d1
	lsl.l	#2,d1
	moveq	#0,d2
	movem.l a1/a3/a6,-(sp)		 ; for QDOS compatibility
	move.w	mem.achp,a2
	jsr	(a2)			 ; allocate new channel block
	movem.l (sp)+,a1/a3/a6
	beq.s	drn_setc		 ; ... ok, setup channel
	move.l	(sp)+,a0		 ; ... oops
	rts

drn_setc
	move.l	a3,-(sp)
	lea	d3c_qname(a0),a5
	move.w	d4,(a5)+		 ; length of copied name
	lea	d3c_name+2(a0),a3
	move.l	a3,d2
	lea	dv3_qdit,a2		 ; QDOS to internal translate table
	moveq	#0,d1
	bra.s	drn_cne
drn_cname
	move.b	(a1)+,d1
	move.b	d1,(a5)+		 ; name given is copied
	move.b	(a2,d1.w),(a3)+ 	 ; internal character
	assert	d3c.pname,0
;; the following is only applies to DOS format and if required should be
;; put into ddf_drname
;;	  bne.s   drn_cnt
;;	  cmp.b   #d3c.pext,-2(a3)	   ; name sep, was previous ext sep?
;;	  bne.s   drn_cnt		   ; ... no
;;	  subq.l  #2,a3 		   ; ... yes, blat out ext
;;	  sf	  (a3)+
;;drn_cnt
drn_cne
	dbra	d4,drn_cname
	sf	(a3)			 ; null at end

	exg	a3,d2
	sub.l	a3,d2			 ; length of name
	move.w	d2,-2(a3)

	move.l	ddf_itopck(a4),a2	 ; internal to format dep check
	lea	d3c_end(a0),a5		 ; where to put it
	move.l	a5,a1			 ; (and use it)
	move.w	d2,(a5)+
	moveq	#0,d1
	bra.s	drn_cchke

drn_cchk
	move.b	(a3)+,d1
	move.b	(a2,d1.w),(a5)+ 	 ; translated character
drn_cchke
	dbra	d2,drn_cchk
	sf	(a5)			 ; null at end

	move.l	(sp)+,a3		 ; restore linkage
	move.l	(sp),a2 		 ; old channel block

	assert	d3c_sdsb,d3c_sdid-4,d3c_sdlen-8,d3c_sdent-$c
	lea	d3c_sdsb(a0),a5
	move.l	ddf_rdsb(a4),(a5)+	 ; this is a good start for slave block

	move.l	ddf_rdid(a4),(a5)+	 ; preset directory info
	move.l	ddf_rdlen(a4),(a5)+
	moveq	#-1,d0
	move.l	d0,(a5)

	st	d3c_denum+1(a0) 	 ; invalid directory entry number

	moveq	#0,d2			 ; set no info
	moveq	#ddf.rename,d3		 ; rename

	jsr	ddf_drname(a4)		 ; do rename action
	bne.s	drn_exrt

	move.w	(a1)+,d2		 ; length of name
	lea	d3c_name(a2),a2
	move.w	d2,(a2)+

	move.l	ddf_ptoi(a4),d5 	 ; translate table
	beq.s	drn_rsnt		 ; ... no translate

	moveq	#0,d1
drn_rsname
	move.b	(a1)+,d1
	move.b	(a3,d1.w),(a2)+ 	 ; translate
	subq.w	#1,d2
	bgt.s	drn_rsname

	bra.s	drn_setqn

drn_rsnt
	move.b	(a1)+,(a2)+		 ; copy
	subq.w	#1,d2
	bgt.s	drn_rsnt

drn_setqn
	move.l	(sp),a2
	lea	d3c_qname(a2),a5
	lea	d3c_qname(a0),a1

	move.w	(a1)+,d2		 ; length of given name
	move.w	d2,(a5)+
drn_setqloop
	move.b	(a1)+,(a5)+		 ; copy qdos name
	subq.w	#1,d2
	bgt.s	drn_setqloop

	move.l	d6,d3c_flid(a2) 	 ; file ID

	assert	d3c_sdsb,d3c_sdid-4,d3c_sdlen-8,d3c_sdent-$c

	lea	d3c_sdsb(a2),a2
	lea	d3c_sdsb(a0),a1

	move.l	(a1)+,(a2)+		 ; copy information from new drive
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+
	move.l	(a1)+,(a2)+

drn_exrt
	move.l	d0,d4
	movem.l a3/a6,-(sp)		 ; for QDOS compatibility
	move.w	mem.rchp,a1
	jsr	(a1)			 ; return dummy block
	movem.l (sp)+,a3/a6

	move.l	(sp)+,a0		 ; restore channel
	move.l	d4,d0
drn_exit
	rts

	end
