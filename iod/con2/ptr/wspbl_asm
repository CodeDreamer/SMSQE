; Write sprite or blob				V2.01	 1991 Tony Tebby
;
; 2003-02-17  2.01  Moved check for system sprite to generic routine (MK)
;		    Bugfix, corrected preserved trap registers (wl)
;
;	This routine deals with vectored sprites, and finds
;	objects of the appropriate mode.  If it doesn't recognise
;	the data, or it is of the wrong mode, then nothing is done.
;
	section driver
;
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_con'
;
	xref	sp_drop
	xref	bl_drop
	xref	pt_drop
	xref	pt_chkbp
	xref	pt_chksp
;
	xdef	pt_wspr1
	xdef	pt_wblb1
	xdef	pt_lblob
	xdef	pt_doblb
;
; Write a line of blobs
;
;	Not very efficient for now - just writes a complete
;	blob at each point on the line, except the last.
;
;	Registers:
;		Entry				Exit
;	D1	start point x,y 		end point
;	D2	end point (not plotted) 	preserved
;	A0	channel block			preserved
;	A1	pointer to blob 		preserved
;	A2	pointer to pattern		preserved
;
trapreg reg	a1/a2
pt_lblob
	movem.l trapreg,-(sp)		; make sure we keep these regs
	jsr	pt_chkbp		; find valid version of objects
	blt.s	ptl_exit		; ...oops
;
	moveq	#1,d5			; assume Y will increment
	move.w	d2,d3			; end Y
	sub.w	d1,d3			; less start is distance to move
	bpl.s	ptl_xdif		; positive, do X
	moveq	#-1,d5			; negative
	neg.w	d3			; absolute distance
ptl_xdif
	swap	d1
	swap	d2			; get X
	moveq	#1,d6			; assume X will increment
	cmp.b	#8,sd_wmode(a0) 	; is this mode 8?
	bne.s	ptl_stxi		; no
	lsr.w	d6,d1			; yes, scale X by two
	lsr.w	d6,d2
ptl_stxi
	move.w	d2,d4			; end X
	sub.w	d1,d4			; less start is distance to move
	bpl.s	ptl_sdir		; positive, find bigger distance
	moveq	#-1,d6			; X goes down
	neg.w	d4
ptl_sdir
	cmp.b	#8,sd_wmode(a0) 	; mode 8?
	bne.s	ptl_swpx		; no, swap X back
	add.w	d6,d6			; yes, X step...
	add.w	d1,d1			; ...and start...
	add.w	d2,d2			; ...and end are multiples of 2
ptl_swpx
	swap	d6			; put X step...
	clr.w	d6			; ...in MSW
	swap	d1
	swap	d2			; swap back ends
;
	cmp.w	d3,d4			; do we move further in X or Y?
	bge.s	ptl_scnt		; X, set initial count
	exg	d3,d4			; swap increments
	exg	d5,d6			; and which gets done more often
ptl_scnt
	move.w	d4,d7			; initial count is...
	lsr.w	#1,d7			; ...half max count
	bra.s	ptl_lpe 		; plot all but last point
;
drreg	reg	d2-d7/a2
ptl_loop
	movem.l drreg,-(sp)
	bsr.s	pt_doblb		; write blob at current point
	movem.l (sp)+,drreg
	add.l	d6,d1			; next point is here
	sub.w	d3,d7			; does it move the other way too?
	bpl.s	ptl_lpe 		; nope
	add.l	d5,d1			; yes, move it
	add.w	d4,d7			; and reset counter
ptl_lpe
	cmp.l	d1,d2			; at the end?
	bne.s	ptl_loop		; not yet
	moveq	#0,d0			; yes, no problems
ptl_exit
	movem.l (sp)+,trapreg
	rts
	page
;
; write sprite into window
;
pt_wspr1
	movem.l trapreg,-(sp)
	jsr	pt_chksp		; check pointer, get correct mode
	blt.s	ptd_exit		; whoops
	lea	sp_drop(pc),a5		; drop in sprite
	bra.s	drop
;
;	Write blob into window
;
pt_wblb1
	movem.l trapreg,-(sp)
	jsr	pt_chkbp		; check blob and pattern
	blt.s	ptd_exit		; wrong
	lea	bl_drop(pc),a5		; drop in blob of pattern
drop
	jsr	pt_drop(pc)		; drop in object
ptd_exit
	movem.l (sp)+,trapreg
	rts
;
pt_doblb
	lea	bl_drop(pc),a5		; drop in blob of pattern
	jmp	pt_drop(pc)		; drop in object

	end
