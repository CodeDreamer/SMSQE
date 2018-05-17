; Sprite utilities  V1.00				2005  Marcel Kilgus
;
; 2005-03-06  1.00  Initial revision (MK)

	section wman

	include dev8_keys_qdos_pt

	xdef	wm_cpspr

ca.entries equ	16			; entries in object address cache
pto.extblk equ	5			; size of extended sprite block

;+++
; Copy complete sprite definition (including WMAN extension)
;
;	d0  r	negative if error (illegal definition found)
;	d1  r	size of copy
;	a1 c  p ptr to definition
;	a3 c  p ptr to free memory area (0 if only size calculation needed)
;
; About $100 bytes of stack space needed
;---
wm_cpspr
	move.l	a3,d0
	bne	copy_sprite

;+++
; Calculate size of a complete sprite definition (including WMAN extension)
;
;	d0  r	negative if error (illegal definition found)
;	d1  r	size calculated
;	a1 c  p ptr to definition
;
; About $100 bytes of stack space needed
;---
calc_sprite_copy
	movem.l d2/a0-a1/a5,-(sp)
	suba.w	#ca.entries*8,sp	; reserve space for sprite addr cache
	move.l	sp,a5
	moveq	#ca.entries-1,d1
csc_clearcache
	clr.l	(a5)+
	clr.l	(a5)+
	dbf	d1,csc_clearcache
	move.l	sp,a5

	move.w	#$ff00,d0
	and.w	(a1),d0
	beq.s	csc_sysspr		; system sprites reference is easy

	btst	#pto..blk,pto_ctrl(a1)	; extended block?
	beq.s	csc_noextended		; ...no

	moveq	#pto.xshl,d1
	bsr.s	calc_chain_size
	bne.s	csc_exit

; Extended sprite definition
	lea	pto_blk(a1),a0
	move.l	(a0),d0
	beq.s	csc_exit		; no extended sprite block

	add.l	d0,a0
	add.l	#pto.extblk*4,d1	; sprite block there, 5 additional ptrs
	moveq	#pto.extblk-1,d2
csc_loop
	move.l	a0,a1
	move.l	(a0)+,d0		; extended sprite pointer
	beq.s	csc_skip
	add.l	d0,a1
	bsr.s	calc_chain_size
	bne.s	csc_exit
csc_skip
	dbf	d2,csc_loop
	bra.s	csc_exit

csc_sysspr
	moveq	#2,d1			; sysspr refs are 2 bytes
	bra.s	csc_exit
csc_noextended
	moveq	#pto.hdrl,d1
	bsr.s	calc_chain_size
csc_exit
	adda.w	#ca.entries*8,sp	; give back cache
	movem.l (sp)+,d2/a0-a1/a5
	rts

;+++
; Calculate size of sprite chain
;
;	d0  r	negative if error (illegal definition found)
;	d1 c  u new size will be ADDED to d1
;	a1 c  p ptr to definition
;---
calc_chain_size
	movem.l a1/a3,-(sp)
ccs_loop
	bsr	object_cache		; might set a3!
	beq.s	ccs_exit		; already counted this chain
	move.l	#$ff00,d0
	and.w	(a1),d0
	beq.s	ccs_syssprite		; system sprite is special

	bsr.s	calc_single_size
	bne.s	ccs_exit
	lea	pto_nobj(a1),a1 	; next sprite?
	move.l	(a1),d0
	beq.s	ccs_exit		; no
	add.l	d0,a1
	bra.s	ccs_loop
ccs_syssprite
	addq.l	#2,d1			; system sprite is 2 bytes
	moveq	#0,d0
ccs_exit
	movem.l (sp)+,a1/a3
	rts

;+++
; Calculate size of single sprite definition
;
;	d0  r	negative if error (illegal definition found)
;	d1 c  u new size will be ADDED to d1
;	a1 c  p ptr to definition
;---
calc_single_size
	movem.l d2-d4/a0-a1/a3,-(sp)
	lea	sprite_table,a0
	move.w	(a1),d0
css_idsearch
	move.w	(a0)+,d2
	beq.s	css_error		; ID not found in list, bad definition
	cmp.w	d0,d2
	beq.s	css_idfound
	addq.w	#2,a0
	bra.s	css_idsearch

css_idfound
	add.l	#pto.hdrl,d1		; that's for sure now
	move.w	#$0004,d2		; 4 byte alignment
	swap	d2
	move.w	(a0)+,d2		; bpp
	lea	pto_cpat(a1),a0
	move.l	(a0),d0 		; pattern?
	beq.s	css_checkmask		; ...no

	add.l	d0,a0
	moveq	#pto..pcmp,d0
	bsr.s	calc_pattern_size

css_checkmask
	lea	pto_mask(a1),a0
	move.l	(a0),d0 		; mask?
	beq.s	css_exit		; ...no

	add.l	d0,a0
	btst	#pto..alph,pto_ctrl(a1) ; alpha blended?
	beq.s	css_noalpha

	move.l	#$00010008,d2		; 1 byte aligned, 8bpp
css_noalpha
	moveq	#pto..mcmp,d0
	bsr.s	calc_pattern_size
	moveq	#0,d0			; no error
css_exit
	movem.l (sp)+,d2-d4/a0-a1/a3
	rts
css_error
	moveq	#-1,d0
	bra.s	css_exit

;+++
; Calculate size of pattern or mask
;
;	d1 c  u updated size
;	d2 c  s alignment (high word), bpp (low word)
;	d3    s
;	d4    s
;	a0 c  s ptr to pattern or mask
;	a1 c  p ptr to sprite definition
;	a3    s
;---
calc_pattern_size
	move.l	a1,-(sp)
	move.l	a0,a1
	bsr	object_cache		; also cache this address
	move.l	(sp)+,a1
	beq	cps_exit		; already cached

	btst	d0,pto_ctrl(a1) 	; compressed?
	bne.s	cps_compressed

	move.l	d2,d3
	move.w	pto_xsiz(a1),d0 	; horizontal size
	mulu	d3,d0			; bits per line
	addq.l	#7,d0			; round up
	lsr.l	#3,d0			; bytes per line
	swap	d3			; alignment in bytes
	subq.w	#1,d3
	add.w	d3,d0			; round up
	not.w	d3
	and.w	d3,d0			; and cut to alignment
	mulu	pto_ysiz(a1),d0 	; complete size
	add.l	d0,d1
	bra.s	cps_exit

; Unfortunatelly there is no easy way to know how long a compressed segment is
cps_compressed
	move.l	(a0)+,d0
	moveq	#0,d3
	cmp.l	#'RLE1',d0
	beq.s	cpsc_go
	moveq	#1,d3
	cmp.l	#'RLE2',d0
	beq.s	cpsc_go
	cmp.l	#'RLE4',d0
	bne.s	cps_exit
	moveq	#2,d3
cpsc_go
	addq.l	#8,d1			; base size of compressed data
	move.l	(a0)+,d2		; size of uncompressed data
	moveq	#0,d0
	bra.s	cpsc_loopend
cpsc_loop
	addq.l	#1,d1			; add control byte
	moveq	#0,d0
	move.b	(a0)+,d0
	bmi.s	cpsc_rle

	addq.b	#1,d0			; uncompressed run is following
	lsl.l	d3,d0			; take item size into account
	add.l	d0,a0			; skip in data
	add.l	d0,d1			; add to overall size
	bra.s	cpsc_loopend
cpsc_rle
	move.l	#257,d4
	sub.l	d0,d4			; compressed run
	lsl.l	d3,d4			; item size
	move.l	d4,d0

	moveq	#1,d4			; single item in compressed data
	lsl.l	d3,d4
	add.l	d4,a0			; skip in data
	add.l	d4,d1			; add to overall size

cpsc_loopend
	sub.l	d0,d2			; subtract from uncompresse size
	bhi.s	cpsc_loop		; ... still more to do
	addq.l	#1,d1			; pad to word boundary
	andi.l	#$fffffffe,d1
cps_exit
	rts

;+++
; Copy complete sprite definition (including WMAN extension)
;
;	d0  r	negative if error (illegal definition found)
;	d1  r	size copied
;	a1 c  p ptr to definition
;	a3 c  p ptr to free memory area
;
; About $100 bytes of stack space needed
;---
copy_sprite
	movem.l d2/d5/a0-a6,-(sp)
	move.l	a3,a2			; fn was previously designed for a2
	suba.w	#ca.entries*8,sp	; reserve space for sprite addr cache
	move.l	sp,a5
	moveq	#ca.entries-1,d1
cs_clearcache
	clr.l	(a5)+
	clr.l	(a5)+
	dbf	d1,cs_clearcache
	move.l	sp,a5

	move.l	a2,a4			; remember base of heap
	move.w	#$ff00,d0
	and.w	(a1),d0
	beq.s	cs_sysspr		; system sprite refs are ease
	btst	#pto..blk,pto_ctrl(a1)	; extended block?
	beq.s	cs_noextended		; ...no

	move.l	a2,a6			; remember base of heap
	moveq	#pto.xshl,d5
	bsr.s	copy_chain
	bne.s	cs_exit

; Extended sprite definition
	move.l	pto_opts(a1),pto_opts(a6) ; copy options verbatim
	lea	pto_blk(a1),a0
	move.l	(a0),d0
	bne.s	cs_copyblk		; copy extended sprite block
	clr.l	pto_blk(a6)		; no extended sprite block
	bra.s	cs_exit

cs_copyblk
	add.l	d0,a0

	lea	pto_blk(a6),a6		; insert ptr to extended block
	move.l	a2,d0
	sub.l	a6,d0			; relative to running ptr
	move.l	d0,(a6)

	move.l	a2,a6			; remember base of new extended block
	add.w	#pto.extblk*4,a2	; sprite block there, 5 additional ptrs
	moveq	#pto.extblk-1,d2
cs_loop
	move.l	a0,a1
	move.l	(a0)+,d0		; extended sprite pointer
	beq.s	cs_skip
	add.l	d0,a1

	moveq	#pto.hdrl,d5
	bsr.s	copy_chain
	bne.s	cs_exit
	move.l	a3,d0
	sub.l	a6,d0
	move.l	d0,(a6)+		; fill in new block
	bra.s	cs_continue
cs_skip
	move.l	d0,(a6)+		; fill in new block
cs_continue
	dbf	d2,cs_loop
	bra.s	cs_exit

cs_sysspr
	move.w	(a1)+,(a2)+		; system sprite is a single word
	bra.s	cs_exit

cs_noextended
	moveq	#pto.hdrl,d5
	bsr.s	copy_chain
cs_exit
	move.l	a2,d1
	sub.l	a4,d1			; size of copied area
	adda.w	#ca.entries*8,sp	; give back stack space
	movem.l (sp)+,d2/d5/a0-a6
	rts

;+++
; Copy sprite chain
;
;	d0  r	negative if error (illegal definition found)
;	d5 c  p size to reserve for header of new sprite
;	a1 c  p ptr to definition
;	a2 c  u ptr to free memory area
;	a3  r	ptr to new sprite
;---
copy_chain
	movem.l a1/a4/a6,-(sp)
	bsr	object_cache
	beq.s	cc_exitcached		; already copied this chain
	move.l	a2,a6			; not cached, a6 is base of new chain
cc_loop
	move.l	#$ff00,d0
	and.w	(a1),d0
	beq.s	cc_syssprite		; system sprite always breaks chain

	move.l	a2,a4			; remember base of current sprite
	bsr.s	copy_single
	bne.s	cc_exit
	lea	pto_nobj(a1),a1 	; next sprite?
	lea	pto_nobj(a4),a4
	move.l	(a1),d0
	beq.s	cc_nonext		; none

	add.l	d0,a1			; next source sprite
	bsr	object_cache		; this in cache?
	beq.s	cc_cached		; already copied this chain
	move.l	a2,d0
	sub.l	a4,d0
	move.l	d0,(a4)
	bra.s	cc_loop

cc_cached
	move.l	a3,d0
	sub.l	a4,d0
cc_nonext
	move.l	d0,(a4)
	bra.s	cc_exitok

cc_syssprite
	move.w	(a1)+,(a2)+		; system sprite is a single word
cc_exitok
	moveq	#0,d0			; no error
cc_exit
	move.l	a6,a3			; return base of this chain
cc_exitcached
	movem.l (sp)+,a1/a4/a6
	rts

;+++
; Copy single sprite definition
;
;	d0  r	negative if error (illegal definition found)
;	d5 c  p size to reserve for header of new sprite
;	a1 c  p ptr to definition
;	a2 c  u ptr to free memory area
;---
copy_single
	movem.l d2-d4/a0-a1/a3-a4,-(sp)
	lea	sprite_table,a0
	move.w	(a1),d0
csi_idsearch
	move.w	(a0)+,d2
	beq.s	csi_error		; ID not found in list, bad definition
	cmp.w	d0,d2
	beq.s	csi_idfound
	addq.w	#2,a0
	bra.s	csi_idsearch

csi_idfound
	move.l	a2,a4			; remember header bae
	move.l	pto_form(a1),(a4)+
	move.l	pto_size(a1),(a4)+
	move.l	pto_org(a1),(a4)+
	add.l	d5,a2			; skip to space for data

	move.w	#$0004,d2		; 4 byte alignment
	swap	d2
	move.w	(a0)+,d2		; bpp
	lea	pto_cpat(a1),a0
	move.l	(a0),d0 		; pattern?
	beq.s	csi_nopattern		; ...no

	add.l	d0,a0
	moveq	#pto..pcmp,d0
	bsr.s	copy_pattern
	move.l	a3,d0
	sub.l	a4,d0
	move.l	d0,(a4)+		; insert rel ptr to pattern
	bra.s	csi_checkmask

csi_nopattern
	clr.l	(a4)+			; no pattern
csi_checkmask
	lea	pto_mask(a1),a0
	move.l	(a0),d0 		; mask?
	beq.s	csi_nomask		; ...no

	add.l	d0,a0
	btst	#pto..alph,pto_ctrl(a1) ; alpha blended?
	beq.s	csi_noalpha

	move.l	#$00010008,d2		; 1 byte aligned, 8bpp
csi_noalpha
	moveq	#pto..mcmp,d0
	bsr.s	copy_pattern
	move.l	a3,d0
	sub.l	a4,d0
	move.l	d0,(a4)+		; insert rel ptr to mask
	moveq	#0,d0
	bra.s	csi_exit

csi_nomask
	clr.l	(a4)+			; no mask
csi_exit
	movem.l (sp)+,d2-d4/a0-a1/a3-a4
	rts
csi_error
	moveq	#-1,d0
	bra.s	csi_exit

;+++
; Copy pattern or mask
;
;	d2 c  s alignment (high word), bpp (low word)
;	d3    s
;	d4    s
;	a0 c  s ptr to source pattern or mask
;	a1 c  p ptr to sprite definition
;	a2 c  u ptr to free memory area
;	a3  r	ptr to pattern/mask
;---
copy_pattern
	move.l	a1,-(sp)
	move.l	a0,a1
	bsr	object_cache		; also cache this address
	move.l	(sp)+,a1
	beq	cp_exit 		; already cached
	move.l	a2,a3			; object will be put here

	btst	d0,pto_ctrl(a1) 	; compressed?
	bne.s	cp_compressed

	move.l	d2,d3
	move.w	pto_xsiz(a1),d0 	; horizontal size
	mulu	d3,d0			; bits per line
	addq.l	#7,d0			; round up
	lsr.l	#3,d0			; bytes per line
	swap	d3			; alignment in bytes
	subq.w	#1,d3
	add.w	d3,d0			; round up
	not.w	d3
	and.w	d3,d0			; and cut to alignment
	mulu	pto_ysiz(a1),d0 	; complete size

	bsr.s	copy_area
	bra.s	cp_exit

; Unfortunatelly there is no easy way to know how long a compressed segment is
cp_compressed
	move.l	(a0)+,d0
	moveq	#0,d3
	cmp.l	#'RLE1',d0
	beq.s	cpc_go
	moveq	#1,d3
	cmp.l	#'RLE2',d0
	beq.s	cpc_go
	cmp.l	#'RLE4',d0
	bne.s	cp_exit
	moveq	#2,d3
cpc_go
	move.l	d0,(a2)+		; copy ID
	move.l	(a0)+,d2		; size of uncompressed data
	move.l	d2,(a2)+		; copy size
	moveq	#0,d0
	bra.s	cpc_loopend
cpc_loop
	moveq	#0,d0
	move.b	(a0)+,d0
	move.b	d0,(a2)+
	bmi.s	cpc_rle

	addq.b	#1,d0			; uncompressed run is following
	lsl.l	d3,d0			; take item size into account
	bsr.s	copy_area
	bra.s	cpc_loopend
cpc_rle
	move.l	#257,d4
	sub.l	d0,d4			; compressed run
	lsl.l	d3,d4			; item size
	move.l	d4,d0

	moveq	#1,d4			; single item in compressed data
	lsl.l	d3,d4
	subq.w	#1,d4
cpcr_loop
	move.b	(a0)+,(a2)+
	dbf	d4,cpcr_loop
cpc_loopend
	sub.l	d0,d2			; subtract from uncompressed size
	bhi.s	cpc_loop		; ... still more to do
	move.l	a2,d0
	addq.w	#1,d0
	andi.l	#$fffffffe,d0		; pad to word boundary
	move.l	d0,a2
cp_exit
	rts

;+++
; Copy area of memory
;
;	d0 c	bytes to copy
;	a0 c  u source area
;	a2 c  u destination area
;---
copy_area
	move.l	d3,-(sp)
	move.l	d0,d3			; data to copy
	lsr.l	#2,d3			; long words
	beq.s	ca_bytes

cal_loop
	move.l	(a0)+,(a2)+
	subq.l	#1,d3
	bne.s	cal_loop
ca_bytes
	move.l	d0,d3
	andi.w	#3,d3
	bra.s	cab_start
cab_loop
	move.b	(a0)+,(a2)+
cab_start
	dbf	d3,cab_loop
	move.l	(sp)+,d3
	rts

;+++
;	Try to get address of already converted object out of cache. If not
;	found, add new entry
;
;	d0  r	cleared if address found in cache
;	a1 c  p source address to lookup
;	a2 c  p item that should be cached
;	a3  r	cached item if found
;	a5 c  p ptr to cache
;---
object_cache
	movem.l d1/a5,-(sp)
	moveq	#ca.entries-1,d0
oc_loop
	move.l	(a5),d1
	beq.s	oc_new			; at end of cache
	cmp.l	d1,a1
	beq.s	oc_found
	addq.l	#8,a5
	dbf	d0,oc_loop
	bra.s	oc_full 		; c. full, pretend that it'll be cached

oc_found
	move.l	4(a5),a3
	moveq	#0,d0
	bra.s	oc_exit

oc_new
	move.l	a1,(a5)+		; insert new entry
	move.l	a2,(a5) 		; and cached destination
oc_full
	moveq	#-1,d0
oc_exit
	movem.l (sp)+,d1/a5
	rts

sprite_table
;		ID    bpp
	dc.w	$0100,$02
	dc.w	$0101,$02
	dc.w	$0200,$01
	dc.w	$0203,$01
	dc.w	$0204,$02
	dc.w	$0207,$02
	dc.w	$0208,$04
	dc.w	$020F,$04
	dc.w	$0210,$08
	dc.w	$021F,$08
	dc.w	$0220,$10
	dc.w	$0221,$10
	dc.w	$0240,$20
	dc.w	0

	end
