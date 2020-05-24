; DV3 QL5B Format	      V3.00	      1992 Tony Tebby
;
; 2020-04-07  3.01  Changed for new ddf_mname format with size word (MK)

	section dv3

	xdef	qlf_ft5b

	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_sys'
	include 'dev8_keys_ql5b'
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_mac_assert'

;+++
; DV3 QL5B Format
;
; This sets up the blank map and header and flushes it.
; It also sets the good and free allocation units into the drive definition
; (as well as the inbuild file header length) and sets the medium to "good"
; before the flush.
;
;	d0 cr	format type / error code
;	d7 c  p drive ID / number
;	a0 c  p pointer to sector map
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;
;---
qlf_ft5b
qfm.reg reg	d1/d2/d3/d4/d5/a0/a1/a2/a5
	movem.l qfm.reg,-(sp)

	cmp.b	#ddf.2048,ddf_slflag(a4) ; ED?
	beq.s	qfm_ed			 ; ... yes
	cmp.w	#18,ddf_scyl(a4)	 ; SS, DD or HD?
	bhi.s	qfm_hd
	beq.s	qfm_dd
	lea	qfm_sstab,a5		 ; SS
	bra.s	qfm_str
qfm_dd
	lea	qfm_ddtab,a5		 ; DD
	bra.s	qfm_str
qfm_hd
	lea	qfm_hdtab,a5		 ; HD
	bra.s	qfm_str
qfm_ed
	lea	qfm_edtab,a5		 ; ED

qfm_str
	move.w	(a5)+,d0		 ; amount to preset
	lea	qdf_map+q5a_soff(a4),a1
qfm_strl
	move.w	(a5)+,(a1)+
	subq.w	#2,d0
	bgt.s	qfm_strl

	move.l	ddf_atotal(a4),d2	 ; total allocation
	move.w	d2,d0
	lea	qdf_map+q5a_gmap(a4),a1

	moveq	#$fffffffd,d3		 ; good sector flag
qfm_smgood
	move.b	d3,(a1) 		 ; mark all sectors good
	addq.l	#3,a1
	subq.w	#1,d0
	bne.s	qfm_smgood


	move.w	ddf_asect(a4),d3	 ; allocation size
	mulu	d3,d2			 ; sectors to check
	move.l	d2,d1			 ; saved
	move.w	d2,d0
	moveq	#0,d4			 ; sector being checked
	lea	qdf_map+q5a_lgph(a4),a1  ; sector mapping

qfm_ckloop
	addq.w	#8,d4
	move.b	(a0)+,d5		 ; next eight sectors all alright?
	beq.s	qfm_cklend		 ; ... yes

	subq.w	#8,d4			 ; backspace
	swap	d0
	move.w	#7,d0			 ; check all seven bits
qfm_ckbits
	add.b	d5,d5
	bcc.s	qfm_ckbend		 ; this bit is ok

qfm.regb reg	d0/d1/d4/d5/d6
	movem.l qfm.regb,-(sp)
	move.w	ddf_scyl(a4),d6
	divu	d6,d4			 ; sector on cylinder / cylinder number
	move.l	d4,d5
	mulu	d6,d4
	move.w	d5,d1
	mulu	qdf_map+q5a_soff(a4),d1  ; total offset
	move.w	ddf_strk(a4),d0
	divu	d0,d1
	swap	d1			 ; offset on track
	clr.w	d5
	swap	d5
	divu	d0,d5			 ; sector / side
	swap	d5			 ; side / sector
	sub.w	d1,d5			 ; unoffset
	bpl.s	qfm_bllog		 ; look for logical sector
	add.w	d0,d5			 ; 0 to strk-1
qfm_bllog
	ror.w	#7,d5
	lsl.l	#7,d5
	swap	d5			 ; side (msb) and sector together

	subq.w	#1,d6

qfm_blcheck
	cmp.b	(a1,d6.w),d5		 ; the right sector?
	dbeq	d6,qfm_blcheck

	add.w	d6,d4			 ; logical sector
	divu	d3,d4			 ; logical group
	mulu	#3,d4			 ; position in map

	lea	qdf_map+q5a_gmap(a4),a2
	moveq	#$fffffffe,d0
	cmp.b	(a2,d4.l),d0		 ; already a bad sector in this one?
	beq.s	qfm_bdone		 ; ... yes
	move.b	d0,(a2,d4.l)		 ; it is bad now
	sub.w	d3,d2			 ; one fewer groups, n fewer sectors
qfm_bdone
	movem.l  (sp)+,qfm.regb

qfm_ckbend
	addq.w	#1,d4			 ; next sector of bad byte
	dbra	d0,qfm_ckbits

	swap	d0			 ; restore outer loop count

qfm_cklend
	subq.w	#8,d0			 ; next byte
	bgt.s	qfm_ckloop

; group map is set up, set the header

	lea	qdf_map(a4),a2		 ; fill in header
	assert	0,q5a_id,q5a_mnam-4,q5a_rand-$e,q5a_mupd-$10
	move.w	#q5a.id>>16,(a2)+	 ; ID MSW
	move.w	(a5)+,(a2)+		 ; ... LSW
	lea	ddf_mname+2(a4),a1
	move.l	(a1)+,(a2)+		 ; copy name
	move.l	(a1)+,(a2)+
	move.w	(a1)+,(a2)+
	move.w	sys_rand(a6),(a2)+	 ; random number
	clr.l	(a2)+			 ; clear update

	assert	q5a_mupd,q5a_free-4,q5a_good-6,q5a_totl-8
	move.w	(a5)+,d4		 ; map groups
	move.w	d4,d0
	addq.w	#1,d0			 ; map + directory groups
	mulu	d3,d0			 ; ... sectors
	neg.w	d0
	add.w	d2,d0			 ; good sectors

	move.w	d0,(a2)+		 ; free
	move.w	d2,(a2)+		 ; good
	move.w	d1,(a2)+		 ; total

	assert	q5a_totl,q5a_strk-2,q5a_scyl-4,q5a_trak-6,q5a_allc-8,q5a_eodr-$a
	move.w	ddf_strk(a4),(a2)+	 ; sectors per track
	move.w	ddf_scyl(a4),d5
	move.w	d5,(a2)+		 ; per cylinder
	divu	d5,d1
	move.w	d1,(a2)+		 ; tracks
	move.w	d3,(a2)+		 ; sectors per group
	moveq	#hdr.len,d5
	move.l	d5,(a2)+		 ; end of directory

	divu	d3,d0			 ; free alloc
	divu	d3,d2			 ; good alloc

	lea	ddf_agood(a4),a2	 ; set remaining bits of drive def
	assert	ddf_agood,ddf_afree-4,ddf_fhlen-8
	move.l	d2,(a2)+
	move.l	d0,(a2)+
	move.l	d5,(a2)+

; now preallocate the map and directory sectors

	moveq	#$fffffff8,d1		 ; MAP ID
	lea	qdf_map+q5a_gmap(a4),a2
qfm_pmap
	moveq	#0,d0
qfm_ploop
	cmp.b	#$fd,(a2)		 ; good group?
	bne.s	qfm_fmtf		 ; ... no
	move.b	d1,(a2)+		 ; file ID
	clr.b	(a2)+
	move.b	d0,(a2)+		 ; and sector number
	addq.b	#1,d0
	cmp.b	d4,d0			 ; all map groups set?
	blt.s	qfm_ploop
	bgt.s	qfm_flush
	moveq	#0,d1			 ; now do directory
	moveq	#0,d4
	bra.s	qfm_pmap

qfm_flush
	move.w	qdf_msect(a4),d0
	lea	qdf_mupd(a4),a2
qfm_mulp
	st	(a2)+			 ; mark all map sectors updated
	subq.w	#1,d0
	bgt.s	qfm_mulp

	assert	ddf.mok,$ff
	st	ddf_mstat(a4)		 ; medium OK
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


qfm_sstab
	dc.w	2+18+18      ; soff + translations
	dc.w	5	     ; soff
	dc.b	$00,$03,$06,$01,$04,$07,$02,$05,$08
	dc.b	$80,$83,$86,$81,$84,$87,$82,$85,$88
	dc.b	$00,$03,$06,$01,$04,$07,$02,$05,$08
	dc.b	$09,$0c,$0f,$0a,$0d,$10,$0b,$0e,$11
	dc.w	'5A'
	dc.w	1	     ; number of map goups
qfm_ddtab
	dc.w	2+18+18
	dc.w	5
	dc.b	$00,$03,$06,$80,$83,$86,$01,$04,$07
	dc.b	$81,$84,$87,$02,$05,$08,$82,$85,$88
	dc.b	$00,$06,$0C,$01,$07,$0D,$02,$08,$0E
	dc.b	$03,$09,$0F,$04,$0A,$10,$05,$0B,$11
	dc.w	'5A'
	dc.w	1	     ; number of map groups
qfm_hdtab
	dc.w	2+36
	dc.w	2
	dc.b	$00,$02,$04,$06,$08,$0a,$0c,$0e,$10
	dc.b	$80,$82,$84,$86,$88,$8a,$8c,$8e,$90
	dc.b	$01,$03,$05,$07,$09,$0b,$0d,$0f,$11
	dc.b	$81,$83,$85,$87,$89,$8b,$8d,$8f,$91
	dc.w	'5B'
	dc.w	2	     ; number of map groups
qfm_edtab
	dc.w	2+20
	dc.w	2
	dc.b	$00,$02,$04,$06,$08,$80,$82,$84,$86,$88
	dc.b	$01,$03,$05,$07,$09,$81,$83,$85,$87,$89
	dc.w	'5B'
	dc.w	3	     ; number of map groups

	end
