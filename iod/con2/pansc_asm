;	Pan and scroll	       V2.00   1998 Tony Tebby
;
;	Registers:
;		Entry				Exit
;	D1	distance to move
;	A0	pointer to cdb

	section con

	include 'dev8_keys_con'
	include 'dev8_keys_sys'

	xref	cn_ssuba
	xref	cn_mblock
	xref	cn_fblock

	xdef	cn_scral
	xdef	cn_scrtp
	xdef	cn_scrbt
	xdef	cn_panal
	xdef	cn_pancl
	xdef	cn_pancr

;	Entry points for scroll all and part area

cn_scrtp
	moveq	#cn.bptop,d3
	bra.s	cns_ssub
cn_scrbt
	moveq	#cn.bpbot,d3
	bra.s	cns_ssub
cn_scral
	moveq	#cn.bpall,d3
cns_ssub
	move.w	d1,d4
	jsr	cn_ssuba(pc)		; set area
	movem.l d1/d2,-(sp)		; save it on stack
	move.l	sp,a1			; point to it
	move.w	d4,d1			; restore movement
	bsr.s	cns_sets		; do it
	addq.l	#8,sp			; pop the descriptor
	rts

cns_sets
	swap	d1
	clr.w	d1			; don't move sideways
	swap	d1
	bra.s	cnp_move		; move it

;	Entry points for pan all and part area

cn_panal
	moveq	#cn.bpall,d3
	bra.s	cns_psub
cn_pancl
	moveq	#cn.bpcln,d3
	bra.s	cns_psub
cn_pancr
	moveq	#cn.bpcrt,d3
cns_psub
	cmp.b	#ptm.ql8,pt_dmode(a3)	; which mode are we in?
	bne.s	cns_psdo		; not 8 colour
	bclr	#0,d1			; even pan only
cns_psdo
	move.w	d1,d4
	jsr	cn_ssuba(pc)		; set area
	movem.l d1/d2,-(sp)		; save it on stack
	move.l	sp,a1			; point to it
	move.w	d4,d1			; restore movement
	bsr.s	cns_setp		; do it
	addq.l	#8,sp			; pop the descriptor
	rts

cns_setp
	swap	d1
	clr.w	d1			; don't move vertically

pnsreg	reg	d1/d3/d6/d7/a1-a5

cnp_move
	movem.l pnsreg,-(sp)
	move.w	sd_linel(a0),a2 	; get row increment
	move.l	sd_scrb(a0),a3		; and base address

	move.l	4(a1),d2		; get area origin
	move.l	d2,d3			; two copies of it
	bsr.l	cnp_sorg		; set an origin
	bsr.l	cnp_sorg		; and another

	move.l	(a1),d4 		; get size
	sub.w	d1,d4			; smaller 'cos of move
	bls.l	cnp_clral		; so small we can just clear!
	swap	d1
	swap	d4
	sub.w	d1,d4			; smaller in X too
	bls.s	cnp_clral
	swap	d4
	move.l	d4,d1			; set area to move

	move.l	a3,a4
	move.l	a3,a5			; same start address for areas
	move.l	a2,a3			; and row increment

	jsr	cn_mblock		; move the appropriate bit

;	Having done the move, clear the edges

	movem.l (sp),pnsreg		; recover the parameters
	move.w	d1,d4			; scroll distance
	beq.s	cnp_clsx		; none, must have been a pan
	movem.l (a1),d1/d2		; get area scrolled
	bpl.s	cnp_stys		; scrolled down, just set y size
	neg.w	d4			; absolute scroll distance
	add.w	d1,d2			; clear origin is...
	sub.w	d4,d2			; ...almost at bottom
cnp_stys
	move.w	d4,d1			; clear height is scroll amount
	bra.s	cnp_fillc

cnp_clsx
	swap	d1			; get pan distance
	move.l	d1,d4			; keep it, and scroll
	tst.w	d4			; was there a pan?
	beq.s	cnp_exok		; no, so exit
	movem.l (a1),d1/d2		; get area
	swap	d4			; get scroll distance
	tst.w	d4			; which way was it?
	bmi.s	cnp_chsz		; up, so just shorten area
	add.w	d4,d2			; down, move area down
	neg.w	d4
cnp_chsz
	add.w	d4,d1			; shorten area

	swap	d1
	swap	d2			; modify in X axis
	swap	d4
	tst.w	d4
	bpl.s	cnp_stxs		; panned right, just set x size
	neg.w	d4			; absolute pan distance
	add.w	d1,d2			; clear origin is...
	sub.w	d4,d2			; ...almost at right
cnp_stxs
	move.w	d4,d1			; clear width is pan amount
	swap	d1
	swap	d2			; back to normal co-ord order

cnp_fillc
	moveq	#-1,d6			; assume no pattern
	move.l	sd_pmask(a0),d7 	; colour mask
	move.l	#%11111000,d0
	and.b	sd_pcolr(a0),d0 	; pattern bits
	beq.s	cnp_fill		; ... no patterm
	move.l	d0,d6
	lsr.b	#6,d6			; just the pattern bits

cnp_fill
	move.w	sd_linel(a0),a2 	; get row increment
	move.l	sd_scrb(a0),a1		; and base address
	jsr	cn_fblock		; and fill the block

cnp_exok
	moveq	#0,d0
	movem.l (sp)+,pnsreg	 
	rts

;	Come here if the pan/scroll has cleared the whole area

cnp_clral
	movem.l (a1),d1/d2		; get area 
	bra.s	cnp_fillc		; and clear it

cnp_sorg
	swap	d1
	swap	d2			; get other co-ordinate
	swap	d3
	tst.w	d1			; which way to move?
	bpl.s	cnp_adit		; positive, add to destination
	neg.w	d1			; negative...
	add.w	d1,d2			; ...add to source
	bra.s	cnp_exso
cnp_adit
	add.w	d1,d3
cnp_exso
	rts


	end
