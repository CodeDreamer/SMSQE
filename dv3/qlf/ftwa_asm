; DV3 QLWA Format	      V3.01	      1992 Tony Tebby
;
; 2020-04-07  3.01  Changed for new ddf_mname format with size word (MK)

	section dv3

	xdef	qlf_ftwa

	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_qlwa'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_hdr'
	include 'dev8_mac_assert'

;+++
; DV3 QLWA Format
;
; This sets up the blank map and header and flushes it.
; It also sets the good and free allocation units into the drive definition
; (as well as the inbuild file header length) and sets the medium to "good"
; before the flush.
;
;	d0 cr	format type / error code
;	d7 c  p drive ID / number
;	a0 c  p pointer to bad sector map or 0
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
qlf_ftwa
qfm.reg reg	d1/d2/d3/d4/d5/d6/a0/a1/a2
	movem.l qfm.reg,-(sp)

	move.l	ddf_atotal(a4),d2	 ; total allocation
	move.l	d2,d0
	subq.l	#1,d0
	lsr.l	#1,d0
	moveq	#0,d1			 ; good sector
	lea	qdf_map+qwa_gmap(a4),a1

qfm_smgood
	move.l	d1,(a1)+		 ; mark all sectors good
	dbra	d0,qfm_smgood

	move.w	ddf_asect(a4),d3	 ; allocation size
	move.l	d2,d1			 ; total allocation
	move.l	a0,d0			 ; bad sector map
	beq.s	qfm_link		 ; ... none
	tst.b	(a0)			 ; bad sector list?
	bmi.l	qfm_fmtf		 ; ... yes, cannot handle this

	move.l	d2,d4
	mulu	d3,d4			 ; sectors to check
	moveq	#0,d0			 ; sector being checked

qfm_ckloop
	move.b	(a0)+,d5		 ; next eight sectors all alright?
	beq.s	qfm_cklend		 ; ... yes

qfm.regb reg	d0/d1/d7
	movem.l qfm.regb,-(sp)

	move.w	#7,d1			 ; check all seven bits
qfm_ckbits
	add.b	d5,d5
	bcc.s	qfm_ckbend		 ; this bit is ok

	move.l	d0,d6
	divu	d3,d6			 ; logical group
	moveq	#0,d7
	move.w	d6,d7
	add.l	d7,d7			 ; position in map

	lea	qdf_map+qwa_gmap(a4),a2
	tst.w	(a2,d7.l)		 ; already a bad sector in this one?
	beq.s	qfm_ckbend		 ; ... yes
	neg.w	(a2,d7.l)		 ; it is bad now
	subq.w	#1,d2			 ; one fewer groups

qfm_ckbend
	addq.l	#1,d0			 ; next sector of bad byte
	dbra	d1,qfm_ckbits

	movem.l (sp)+,qfm.regb

qfm_cklend
	addq.l	#8,d0			 ; next byte
	cmp.l	d4,d0
	blt.s	qfm_ckloop

; bad groups are marked, d1 is total, d2 good, link up the map list

qfm_link
	lea	qdf_map+qwa_gmap(a4),a0  ; first map group
	moveq	#1,d0
	move.w	qdf_msect(a4),d6	 ; number of map sectors
	moveq	#0,d4
	move.w	d6,d4
	subq.w	#1,d4
	divu	ddf_asect(a4),d4
	addq.w	#1,d4			 ; number of map groups
qfm_smap
	tst.w	(a0)			 ; must be OK
	bne.l	qfm_fmtf
	move.w	d0,(a0)+		 ; link to next
	addq.w	#1,d0
	cmp.w	d4,d0
	ble.s	qfm_smap
	clr.w	-2(a0)			 ; last is zero
	tst.w	(a0)+
	bne.l	qfm_fmtf		 ; directory sector is next

	tst.w	(a0)+			 ; for simplicity, this must be good
	bne.l	qfm_fmtf

	move.l	a0,a1
	addq.w	#1,d0			 ; next

qfm_lklp
	tst.w	(a1)+			 ; next ok?
	bne.s	qfm_lkle		 ; ... no
	move.w	d0,-(a0)		 ; ... yes, set number (back a bit!!)
	move.l	a1,a0			 ; and set next to fill in
qfm_lkle
	addq.w	#1,d0			 ; next
	cmp.w	d1,d0			 ; all done?
	blo.s	qfm_lklp

; group map is set up, set the header
	lea	qdf_map(a4),a2		 ; fill in header
	assert	0,qwa_id,qwa_name-4,qwa_spr0-$1a,qwa_uchk-$1c
	move.l	#qwa.id,(a2)+		 ; ID
	lea	ddf_mname(a4),a1
	move.w	(a1)+,d5
	move.w	d5,(a2)+		 ; name length
	bra.s	qfm_cpy_start
qfm_cpy_name
	move.b	(a1)+,(a2)+
qfm_cpy_start
	dbf	d5,qfm_cpy_name

	lea	qdf_map+qwa_spr0(a4),a1  ; fill up to here with spaces
qfm_name_fill
	cmp.l	a2,a1
	beq.s	qfm_name_done
	move.b	#' ',(a2)+
	bra.s	qfm_name_fill

qfm_name_done
	clr.w	(a2)+			 ; 2 spare bytes
	move.w	sys_rand(a6),(a2)+	 ; msw update
	clr.w	(a2)+			 ; clear lsw update

	moveq	#0,d0
	assert	qwa_uchk+4,qwa_intl,qwa_sctg-2,qwa_sctt-4,qwa_trkc-6,qwa_cyld-8
	clr.w	(a2)+			 ; interleave
	move.w	d3,(a2)+		 ; sectors per group
	move.w	ddf_strk(a4),(a2)+	 ; sectors per track
	move.w	ddf_heads(a4),(a2)+	 ; tracks per cyl
	beq.s	qfm_cyld
	move.w	ddf_atotal(a4),d0
	mulu	ddf_asect(a4),d0
	swap	d0
	move.l	d0,-(sp)
	move.w	ddf_atotal+2(a4),d0
	mulu	ddf_asect(a4),d0
	add.l	(sp)+,d0		 ; total sectors
	subq.l	#1,d0			 ; round up cylinders
	divu	ddf_scyl(a4),d0
	addq.w	#1,d0

qfm_cyld
	move.w	d0,(a2)+		 ; cylinders per drive

	move.w	d4,d5			 ; number of map groups
	addq.w	#1,d5			 ; first free

	assert	qwa_cyld+2,qwa_ngrp,qwa_fgrp-2,qwa_sctm-4,qwa_nmap-6
	move.w	d2,(a2)+		 ; number of groups
	move.l	d2,d0			 ; good
	sub.w	d5,d0			 ; ... less map and dir

	move.w	d0,(a2)+		 ; free groups
	move.w	d6,(a2)+		 ; sectors per map
	move.w	#1,(a2)+		 ; number of maps

	assert	qwa_nmap+2,qwa_free,qwa_root-2,qwa_rlen-4,qwa_fsct-8
	move.w	d5,(a2)+		 ; first free
	move.w	d4,(a2)+		 ; root dir first group = ID
	moveq	#hdr.len,d5
	move.l	d5,(a2)+		 ; end of directory
	move.l	ddf_psoff(a4),(a2)+	 ; start sector
	clr.w	(a2)			 ; park cylinder

	lea	ddf_agood(a4),a2	 ; set remaining bits of drive def
	assert	ddf_agood,ddf_afree-4,ddf_fhlen-8
	move.l	d2,(a2)+
	move.l	d0,(a2)+
	move.l	d5,(a2)+

	move.w	d4,ddf_rdid+2(a4)	 ; including root dir ID

; all set up, flush it

	lea	qdf_mupd(a4),a2
qfm_mulp
	st	(a2)+			 ; mark all map sectors updated
	subq.w	#1,d6
	bgt.s	qfm_mulp

	assert	ddf.mok,$ff
	st	ddf_mstat(a4)
	st	ddf_mupd(a4)		 ; map updated

	jsr	ddl_dflush(a3)		 ; flush

qfm_ok
	moveq	#0,d0
qfm_exit
	movem.l (sp)+,qfm.reg
	rts

qfm_fmtf
	moveq	#err.fmtf,d0
	bra.s	qfm_exit

	end
