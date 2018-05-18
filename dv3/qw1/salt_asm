; DV3 QLW1 (Qubide) Sector Allocate, Locate and Truncate  V3.01  2017 W. Lenerz
; based on
; DV3 QL Sector Allocate, Locate and Truncate  V3.00	       1992 Tony Tebby


	section dv3

	xdef	qw1_sawa
	xdef	qw1_slwa
	xdef	qw1_stwa
			      
	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlw1'


; search for the first free cluster
;	d0    s
;	d3    s
;	d4  r	fat entry for first free cluster, or 0 if no free cluster found
;	a2 cr	pointer to root sector+stat of fat / free fat entry
;	a4 c  p pointer to drive definition
;
; !!!!!!      on return, CC = Z if drive full (!), else NZ     !!!!!!!!

qwsa_ffc
	clr.l	d4			; preset drive full
	move.l	qwf_mtop(a4),d3 	; end of fat
	lea	qwf_fat(a4),a2		; point to start of fat
	moveq	#-1,d0			; $ff means free cluster
ffc_lp	cmp.l	d3,a2			; end of fat reached?
	beq.s	ffc_exit		; yes->
	cmp.b	(a2),d0 		; is cluster free?
	beq.s	ffc_free		; ... yes
	addq.l	#4,a2			; ... no, next fat entry
	bra.s	ffc_lp
ffc_free
	move.l	a2,d4			; fat entry for free cluster
ffc_exit
	tst.l	d4
	rts


;+++
; DV3 QLW1 allocate new cluster of sectors
;
;	d0 cr	known logical drive group / logical drive group
;	d1 c  p file group of known drive group
;	d2 c  p next file group
;	d6 c  p file ID (updated if first)
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;
;---
qw1_sawa
qwsa.reg reg	d2/d3/d4/a2

	movem.l qwsa.reg,-(sp)
	lea	qwf_root(a4),a2 	; root sector
	move.w	qw1_fgrp(a2),d3 	; number of free clusters
	beq.s	qwsa_drfl		; none, drive is full
	bsr.s	qwsa_ffc		; find a free cluster (in d4 & a2)
	beq.s	qwsa_drfl		; couldn't, drive is full (!!!!)

	sub.l	a4,d4			; relative to drive defn
	move.l	d4,d0			; will be cluster number
	sub.l	#qwf_fat,d0		; fat entry, now relative to start of fat
	lsr.l	#2,d0			; each cluster = 4 bytes in fat
	      
	tst.w	d2			; is this a new file?
	bne.s	qswa_old		; ... no
	move.w	d0,d6			; ... yes fileID = cluster nbr
qswa_old
	move.w	d6,(a2)+		; file ID
	move.w	d2,(a2) 		; block nbr

	lea	qwf_root(a4),a2 	; root sector + 1st part of fat

	subq.w	#1,qw1_fgrp(a2) 	; one free cluster less
	subq.w	#1,ddf_afree+2(a4)	; fewer clusters now

	bsr.s	qws_smd4		; update byte map (d4)

qwsa_exit
	movem.l (sp)+,qwsa.reg
	rts

qwsa_drfl
	moveq	#err.drfl,d0
	bra.s	qwsa_exit		 ; drive full

;+++
; DV3 QLW1 locate cluster of sectors
;
;	d0 cr	known logical cluster / logical cluster
;	d1 c  p file group of known drive group
;	d2 c  p file group required starting at 0  (??????)
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return positive (OK) or standard error
;---
qw1_slwa
qwsl.reg reg	d2/d4/a2
	movem.l qwsl.reg,-(sp)
	bsr.s	qwsl_find		 ; find sector
	movem.l (sp)+,qwsl.reg
	rts

qwsl_find
	lea	qwf_fat(a4),a2		; point to start of fat
	move.l	qwf_mtop(a4),d0 	; end of fat
qwsl_loop
	cmp.l	a2,d0			; end reached?
	beq.s	qwsl_mchk		; yes, not found
	cmp.w	(a2),d6 		; for this file?
	bne.s	qwsl_next		; no
	cmp.w	2(a2),d2		; this block?
	beq.s	qwsl_ok 		; yes
qwsl_next
	addq.l	#4,a2			; point next fat entry
	bra.s	qwsl_loop
qwsl_ok
	move.l	a2,d0			; entry we're looking for
	sub.l	a4,d0			; relative
	sub.l	#qwf_fat,d0		; relative to start of fat
	lsr.l	#2,d0			; each cluster = 4 bytes in fat
qwsl_done
	tst.l	d0
	rts

qwsl_mchk
	moveq	#err.mchk,d0		 ; oops, not found
	rts



; I need to calculate in what sector of the fat the updated fat entry is
; and update the FAT byte map accordingly.
; Remember, part of the fat is also stared in the root sector itself, the fat
; starts at offset qw1_gmap in the root sector, so the first sector of the fat
; is actually the root sector
;
;	d4  c  s entry in fat (within the drive definition block)
;	a4  c  p drive definition block

qws_smd4
	sub.l	#qwf_fat+qw1_gmap,d4	; d4 now relative to fat 2nd sector
	ble.s	rootsect		; this entry is still in root sector

	divu	ddf_slen(a4),d4 	; sector
	addq.w	#1,d4			; we have already ruled out sector 0
	add.w	#qwf_mupd,d4		; point to byte map
	st	(a4,d4.w)		; this sector is updated
rootsect
	st	qwf_mupd(a4)		; root sector updated, too
	st	ddf_mupd(a4)		; map updated
	rts


;+++
; DV3 QLW1 truncate cluster allocation : truncate all clusters (not sectors!)
; from D2 (inclusive) upwards.
;
;	d0 cr	known logical drive cluster / error status
;	d1 c  p file cluster of known drive cluster
;	d2 c  p first file cluster to free (first block to free)
;	d6 c  p file ID
;	d7 c  p drive ID / number
;	a0 c  p pointer to channel block
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return standard
;---
qw1_stwa
qwst.reg reg	d1/d2/d3/d4/d5/d6/d7/a1/a2/a3
	movem.l qwst.reg,-(sp)
	lea	qwf_fat(a4),a2		; point to start of fat
	move.l	qwf_mtop(a4),a3 	; end of fat
	moveq	#-1,d1			; $ff is free cluster marker
	clr.l	d3			; will be nbr of deleted clusters
	move.l	#qwf_fat+qw1_gmap,d0	; make relative to fat 2nd sector...
	add.l	a4,d0			; ...now
	move.w	ddf_slen(a4),d5 	; sector length
	lea	qwf_mupd(a4),a1 	; ptr to byte map
	move.w	#qwf_mupd,d7
qwst_loop
	cmp.l	a3,a2			; end of fat reached?
	beq.s	qwst_exok		; yes, done
	cmp.w	(a2),d6 		; this file?
	bne.s	qwst_dolp		; ... no
	cmp.w	2(a2),d2		; block must be deleted?
	bgt.s	qwst_dolp		; ... no
	move.b	d1,(a2) 		; mark cluster as free
	addq.w	#1,d3			; show one more cluster freed
	move.l	a2,d4

; now update FAT byte map		(see keys_qlw1)
	sub.l	d0,d4			; d4 now relative to fat 2nd sector
	ble.s	qwst_dolp		; this entry is still in root sector

	divu	d5,d4			; fat sector
	addq.w	#1,d4			; we have already ruled out sector 0
	add.w	d7,d4			; point to byte map
	st	(a4,d4.w)		; this fat sector is updated

qwst_dolp
	addq.l	#4,a2			; point next fat entry
	bra.s	qwst_loop
				
qwst_exok
	lea	qwf_root(a4),a2 	 ; point to start of fat
	add.w	d3,qw1_fgrp(a2) 	; I freed this many clusters
	add.w	d3,ddf_afree+2(a4)
	st	qwf_mupd(a4)		; root sector updated
	st	ddf_mupd(a4)		; map updated

	moveq	#0,d0
qwst_exit
	movem.l (sp)+,qwst.reg
	rts

qwst_mchk
	moveq	#err.mchk,d0		 ; oops, not found
	bra.s	qwst_exit



	end
