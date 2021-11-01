; DV3 Qubide (QLW1) Check Format   V3.03	   2017 W. Lenerz
;
; based on
;
; DV3 QL Check Format		   V3.00	   1992 Tony Tebby
;
; 2020-03-07  3.02  Better handling of medium name (MK+wl)
; 2021-08-01  3.03  Last version trashed sectors per cluster info, fixed (MK)

	section dv3

	xdef	qw1_check
	xdef	qw1_dlen

	xref	qw1_table

	xref	dv3_psector
	xref	dv3_change
	xref	dv3_setfd
	xref	dv3_redef
			
	include 'dev8_dv3_keys'
	include 'dev8_keys_qlw1'
	include 'dev8_keys_hdr'
	include 'dev8_mac_assert'

;+++
; QLW1 Check Format
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
qw1_check
qck.regc reg	d1/d2/a2/a5
qck.regx reg	d3-d4/d6/a0/a1
	movem.l qck.regc,-(sp)
	tst.b	ddf_mstat(a4)		; valid medium?
	beq.s	qck_changed		; ... no

	lea	qwf_root(a4),a5
	move.l	a1,a2			; root sector
	moveq	#$3f,d0 		; test $100 bytes
qck_loop
	cmpm.l	(a5)+,(a2)+
	dbne	d0,qck_loop
	bne.s	qck_changed
qck_ckok
	st	ddf_mstat(a4)
	moveq	#0,d0
qckc_exit
	movem.l (sp)+,qck.regc
	rts

qck_changed
	jsr	dv3_change	       ; change OK
	bne.s	qckc_exit	       ; ... no

	movem.l qck.regx,-(sp)
	tst.b	ddl_buff(a3)	       ; buffered?
	beq.l	qck_bad 	       ; ... no

	assert	ddf.qw1,0
	sf	ddf_stype(a4)	       ; clear subtype

	cmp.l	#qw1.id,qw1_iden(a1)   ; QLW1?
	bne.l	qck_bad 	       ; ... no
	clr.l	d0
	move.w	qw1_sctg(a1),d0        ; sectors per cluster (1 sector=512 bytes)
	move.l	d0,d6		       ; keep
	move.w	qw1_sctm(a1),d2        ; nbr of clusters in fat
	mulu	d0,d2		       ; d2 = sectors per fat
	move.l	d2,d0
	moveq	#9,d1
	lsl.l	d1,d0		       ; size of fat in bytes (if it occupied all clusters)
	move.l	#qwf_root,d1
	add.l	d1,d0		       ; + fixed drv defn block size
	jsr	dv3_redef	       ; re-allocate drive definition
	bne.l	qck_exit	       ; ... oops

; now calculate the real end of the FAT and set it
	clr.l	d0
	move.w	qw1_ngrp(a1),d0 	; total nbr of clusters
	lsl.l	#2,d0			: I need 4 bytes per cluster
	add.l	d1,d0			; root sect/fat start in defn block
	add.l	#qw1_gmap,d0		; offset to start of fat

	lea	(a4,d0.l),a2
	move.l	a2,qwf_mtop(a4) 	; fat top
	move.w	d2,qwf_msect(a4)	; number of fat sectors

	lea	qw1_name(a1),a2 	; name has no length word
	lea	ddf_mname+2(a4),a0	; skip len word
	move.l	(a2)+,(a0)+
	move.l	(a2)+,(a0)+
	move.w	(a2)+,(a0)+		; max name is 10 long

	moveq	#9,d0			; max nbrs of chars -1 (dbf)
	moveq	#' ',d4 		; string is space filled
cmp_lp	cmp.b	-(a2),d4
	dbne	d0,cmp_lp
	addq.w	#1,d0
	move.w	d0,ddf_mname(a4)

	assert	ddf_strk,ddf_sintlv-4,ddf_sskew-6,ddf_heads-8
	lea	ddf_strk(a4),a2
	move.w	qw1_sctt(a1),(a2)+	; sectors per track
	addq.l	#2,a2			; jump over spare
	clr.l	d4
	move.l	d4,(a2)+		; 0 interleave and skew
	move.b	qw1_numh(a1),d4
	move.w	d4,(a2)+		; 0 number of heads

	assert ddf_heads+2,ddf_scyl
	assert ddf_scyl,ddf_asect-2,ddf_asize-4,ddf_atotal-6,ddf_agood-$a

	move.w	qw1_sctc(a1),(a2)+	; sectors per cylinder
	move.w	d6,(a2)+		; cluster size (sectors)
	move.w	ddf_slen(a4),d0
	mulu	d6,d0
	move.w	d0,(a2)+		; cluster size (bytes)
	addq.l	#2,a2			; word into long word
	move.w	qw1_ngrp(a1),(a2)+	; total number of clusters
	addq.l	#2,a2			; word into long word
	move.w	qw1_ggrp(a1),(a2)+	; number of good clusters
	addq.l	#2,a2			; word into long word
	move.w	qw1_fgrp(a1),(a2)+	; number of free clusters

	move.l	ddf_psoff(a4),ddf_lsoff(a4) ; first sector
	lea	qwf_root(a4),a2 	; ... here
	moveq	#512/4-1,d3		; copy one sector
	exg	a1,a2			; a2 root sect, a1 in drv defn blck
qck_rlp move.l	(a2)+,(a1)+		; copy root sector
	dbf	d3,qck_rlp
	move.w	d2,d1			; nbr of sects to load for fat/root sect
	subq.w	#1,d1			; minus the one just copied
	ble	qck_bad 		; huh?
	moveq	#1,d3			; start at 2nd sector of file
	moveq	#0,d2
	move.b	ddl_msect(a3),d2	; multple sector reads supported?
	bne.s	qck_ldmul		; ... yes
	moveq	#1,d2			; ... no, load the map sector by sector
qck_ldmul
	cmp.w	d2,d1			; load max sectors?
	bhs.s	qck_lmdo		; ... yes
	move.w	d1,d2
qck_lmdo
	move.l	d3,d0
	jsr	dv3_psector		; physical sector
	jsr	ddl_rsect(a3)		; read all of map
	bne.s	qck_done		; bad
	add.l	d2,d3
	move.w	d2,d0
	mulu	ddf_slen(a4),d0
	add.l	d0,a1			; move pointer on
	sub.w	d2,d1
	bhi.s	qck_ldmul
	moveq	#0,d0
qck_done
	clr.l	d1
	move.w	d1,ddf_rdid(a4) 	; root directory ID id always 0
	bsr.s	qw1_dlen		; get length of root dir
	moveq	#hdr.len,d6
	sub.l	d6,d0
	move.l	d0,ddf_rdlen(a4)	; set it
		
	move.l	d6,ddf_fhlen(a4)	; set header length

; now find highest current file number
;	 move.l  #qw1_maxf,d0		 ; highest allowed file number
;	 moveq	 #2,d1			 ; current highest (0 = root dir, 1=trash)
;	 clr.l	 d2
;	 lea	 qwf_fat(a4),a1 	 ; start of fat
;	 move.l  qwf_mtop(a4),a2	 ; end of fat
;qck_fxlp
;	 cmp.l	 a1,a2			 ; reached end of fat?
;	 beq.s	 qck_fxok		 ; yes, done
;	 move.w  (a1),d2		 ; file number in this fat entry
;	 cmp.l	 d0,d2			 ; higher than max allowed file?
;	 bgt.s	 qck_dolp		 ; yes, don't use
;	 cmp.l	 d2,d1			 ; higher than current highest?
;	 bge.s	 qck_dolp		 ; yes, don't use
;	 move.l  d2,d1			 ; new highest file number
;qck_dolp
;	 addq.l  #4,a1
;	 bra.s	 qck_fxlp
;qck_fxok
;	 move.l  d1,qwf_hfil(a4)	 ; keep highest file number
	st	ddf_mstat(a4)		; ... medium OK now

	moveq	#ddf.change,d0
	bra.s	qck_exit

qck_ok
	moveq	#0,d0
qck_exit
	movem.l (sp)+,qck.regx
	movem.l (sp)+,qck.regc
	rts

qck_bad
	moveq	#ddf.unrec,d0		; unrecognised format
	bra.s	qck_exit


**************************
;
; find length of a directory
;
;	find all clusters for this file, *nbr sects per cluster *512
;
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;	d0 r	length of file in bytes INCLUDING the header
;	d1 c  s file ID
;
*******************************

dlenreg reg	a1/a2
qw1_dlen
	movem.l dlenreg,-(a7)
	lea	qwf_fat(a4),a1		; root sector (incl. fat) starts here
	move.l	qwf_mtop(a4),a2 	; and stops here
	clr.l	d0			; number of clusters found, none yet
qck_dllp
	cmp.l	a1,a2
	beq.s	qck_dlend		; done
	cmp.w	(a1),d1 		; this file?
	bne.s	qck_dlnxt		; no
	addq.l	#1,d0			; one more cluster
qck_dlnxt
	addq.l	#4,a1			; point next fat entry
	bra.s	qck_dllp

qck_dlend
	lea	qwf_root(a4),a2
	move.w	qw1_sctg(a2),d1 	; nbr of sectors per cluster
	mulu	d1,d0			; d0 = nbr of sectors for dir
	moveq	#9,d1
	lsl.l	d1,d0			; d0 = length of file

	tst.l	d0
	movem.l (a7)+,dlenreg
	rts

	end
