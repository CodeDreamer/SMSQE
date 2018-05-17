; Read/scan pixel colour		v0.00   Sept 1987  J.R.Oakley	QJUMP
;
;	Registers:
;		Entry				Exit
;	D1	position (x|y)			new position | colour
;	D2	key | colour			(preserved)
;	A0	(cdb)
;
;	key bit 	meaning
;	31		set => scan required
;	19		set => scan until same colour: else scan to different
;	18/17		0=scan up, 1=scan down, 2=scan left, 3=scan right
;	16		set => compare with given colour, else with start colour
;
;	If we're comparing with the start pixel then the start pixel
;	is not itself scanned.
;
	section driver
;
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_con'
;
	include 'dev8_mac_assert'
;
	xdef	pt_rpixl
;
pt_rpixl
	bsr	ptr_setu		; do initial setup
	bne	ptr_exit		; whoops
	tst.l	d2			; are we scanning?
	bpl	ptr_crpx		; no, just check and read pixel
;
	btst	#iop..gcl,d2		; are we given a colour?
	bne.s	ptr_fgcl		; yes, just find it
	move.w	d7,d0
	and.w	(a1),d0
	bsr.s	ptr_mtoc		; no, find current
	move.w	d0,d2			; and set it as "parameter"
ptr_fgcl
	and.w	#$7,d2			; only eight colours	 $$$$ MAGIC !!!!
	add.w	d2,d2
	move.w	0(a2,d2.w),d2		; get appropriate pattern mask
	btst	#iop..slr,d2		; east/west scan?
	beq.s	ptr_snss		; no, set up for north/south
	move.w	10*2(a2),d3		; yes, get shift distance
	swap	d1			; start co-ordinate is this
	move.w	d1,d5			; assume we're going west...
	move.w	9*2(a2),d4		; ...so re-start at rh pixel
	btst	#iop..sdr,d2		; are we?
	beq.s	ptr_jmps		; yes, jump into scan routine
	sub.w	sd_xsize(a0),d5 	; amount left to scan...
	neg.w	d5			; ...is...
	subq.w	#1,d5			; ...this much
	move.w	8*2(a2),d4		; this is re-start mask
	bra.s	ptr_jmps		; do scan
;
;	Come here if scanning north or south
;
ptr_snss
	and.w	d7,d2			; mask the interesting pixel
	moveq	#-1,d3			; assume we scan south
	move.w	pt_bytel(a3),d4
	neg.w	d4			; bytes per row
	move.w	d1,d5			; there's this many pixels left
	btst	#iop..sdr,d2		; are we scanning north?
	beq.s	ptr_jmps		; yes, do scan
	neg.w	d3
	neg.w	d4			; opposite scan direction
	sub.w	sd_ysize(a0),d5 	; this many...
	neg.w	d5
	subq.w	#1,d5			; ...pixels to go
;
;	We now have
;	D1	start co-ordinate
;	D2	colour pattern
;	D3	shift/scan distance (1 or 2 for mode 4 or 8)
;	D4	re-start mask
;	D5	pixels left to window edge
;	D7	start mask
;
;	or
;
;	D1	start co-ordinate
;	D2	colour pattern (masked)
;	D3	scan distance (+/- 1)
;	D4	scan row increment
;	D5	pixels left to window edge
;	D7	mask
;
;
ptr_jmps
	move.w	d7,d6			; get start mask...
	and.w	d2,d6			; ...thus start pattern
	moveq	#$e,d0
	swap	d2			; get compare/direction data
	and.w	d2,d0
	swap	d2
	move.w	jmptab(pc,d0.w),d0	; and call one of the scan routines
	jmp	jmptab(pc,d0.w)
;
	assert	iop..sdr-17,iop..slr-18,iop..ssc-19
jmptab
	dc.w	ptr_nsne-jmptab
	dc.w	ptr_nsne-jmptab
	dc.w	ptr_wene-jmptab
	dc.w	ptr_eane-jmptab
	dc.w	ptr_nseq-jmptab
	dc.w	ptr_nseq-jmptab
	dc.w	ptr_weeq-jmptab
	dc.w	ptr_eaeq-jmptab
;
;	Convert colour mask to number
;
;	Registers:
;		Entry				Exit
;	D0	masked colour			colour number
;	D1-D3					smashed
;	D7	mask used			preserved
;	A2	colour table
;
mtocreg reg	d1-d3
;
ptr_mtoc
	movem.l mtocreg,-(sp)
	move.w	d0,d1			; keep colour safe
	moveq	#7,d0			; there are 8 possibilities
	moveq	#$7*2,d2
ptr_mtol
	move.w	0(a2,d2.w),d3		; get full colour mask
	and.w	d7,d3			; add same mask as current pixel
	subq.w	#2,d2
	cmp.w	d3,d1			; is it the same?
	dbeq	d0,ptr_mtol		; keep going until it is
	cmp.w	#7,d0			; colour seven?
	beq.s	ptr_mtoe		; yes, stays the same
	move.b	sd_wmode(a0),d1 	; if it's mode 4...
	bclr	d1,d0			; it's even (cunning,eh?)
ptr_mtoe
	movem.l (sp)+,mtocreg
	rts
;
;	Scan exit points
;
ptr_done
	bsr	ptr_mtoc		; convert colour found to number
	swap	d1			; put new position in msw
	move.w	d0,d1			; colour in lsw
	bra.s	ptr_exok
ptr_dnnf
	moveq	#-1,d1			; no position, no colour
ptr_exok
	moveq	#0,d0
ptr_exit
	rts
;
;	Scan north/south for same colour
;
ptr_nseq
	btst	#iop..gcl,d2		; were we given a colour?
	beq.s	ptr_snee		; no, skip first pixel
ptr_snel
	move.w	d7,d0			; get...
	and.w	(a1),d0 		; ...current pixel colour bits
	cmp.w	d0,d2			; same as required pattern?
;
	beq	ptr_done		; yes, finished found
ptr_snee
	subq.w	#1,d5			; can we do more?
	bmi.s	ptr_dnnf		; no, not found
	add.w	d3,d1			; next pixel is this one
	add.w	d4,a1			; go one line up or down
	bra.s	ptr_snel
;
;	Scan north/south for different colour
;
ptr_nsne
	btst	#iop..gcl,d2
	beq.s	ptr_snne
ptr_snnl
	move.w	d7,d0			; get...
	and.w	(a1),d0 		; ...current pixel colour bits
	cmp.w	d0,d2			; same as required pattern?
;
	bne	ptr_done		; no, finished found
ptr_snne
	subq.w	#1,d5			; can we do more?
	bmi.s	ptr_dnnf		; no, not found
	add.w	d3,d1			; next pixel is this one
	add.w	d4,a1			; go one line up or down
	bra.s	ptr_snnl
;
;	Scan east for same colour
;
ptr_eaeq
	btst	#iop..gcl,d2
	beq.s	ptr_seee
ptr_seew
	move.w	d7,d0			; get...
	and.w	(a1),d0 		; ...current pixel colour bits
	cmp.w	d0,d6			; same as required pattern?
;
	beq	ptr_done		; yes, finished found
ptr_seee
	sub.w	d3,d5			; can we do more?
	bmi.s	ptr_dnnf		; no, not found
	add.w	d3,d1			; next pixel is this one
	lsr.w	d3,d6			; shift search pattern
	lsr.w	d3,d7			; and mask
	bcc	ptr_seew		; mask still in word, keep shifting
	addq.l	#2,a1			; next word
	move.w	d4,d7			; re-start with new mask...
	move.w	d7,d6			; ...and
	and.w	d2,d6			; ...pattern
	bra.s	ptr_seew
;
;	Scan east for different colour
;
ptr_eane
	btst	#iop..gcl,d2
	beq.s	ptr_sene
ptr_senw
	move.w	d7,d0			; get...
	and.w	(a1),d0 		; ...current pixel colour bits
	cmp.w	d0,d6			; same as required pattern?
;
	bne	ptr_done		; no, finished found
ptr_sene
	sub.w	d3,d5			; can we do more?
	bmi.s	ptr_dnnf		; no, not found
	add.w	d3,d1			; next pixel is this one
	lsr.w	d3,d6			; shift search pattern
	lsr.w	d3,d7			; and mask
	bcc	ptr_senw		; mask still in word, keep shifting
	addq.l	#2,a1
	move.w	d4,d7			; re-start with new mask...
	move.w	d7,d6			; ...and
	and.w	d2,d6			; ...pattern
	bra.s	ptr_senw
;
;	Scan west for same colour
;
ptr_weeq
	btst	#iop..gcl,d2
	beq.s	ptr_swee
ptr_swew
	move.w	d7,d0			; get...
	and.w	(a1),d0 		; ...current pixel colour bits
	cmp.w	d0,d6			; same as required pattern?
;
	beq	ptr_done		; yes, finished found
ptr_swee
	sub.w	d3,d5			; can we do more?
	bmi	ptr_dnnf		; no, not found
	sub.w	d3,d1			; next pixel is this one
	lsl.w	d3,d6			; shift search pattern
	lsl.w	d3,d7			; and mask
	bcc	ptr_swew		; mask still in word, keep shifting
	subq.l	#2,a1			; back a word
	move.w	d4,d7			; re-start with new mask...
	move.w	d7,d6			; ...and
	and.w	d2,d6			; ...pattern
	bra.s	ptr_swew
;
;	Scan west for different colour
;
ptr_wene
	btst	#iop..gcl,d2
	beq.s	ptr_swne
ptr_swnw
	move.w	d7,d0			; get...
	and.w	(a1),d0 		; ...current pixel colour bits
	cmp.w	d0,d6			; same as required pattern?
;
	bne	ptr_done		; no, finished found
ptr_swne
	sub.w	d3,d5			; can we do more?
	bmi	ptr_dnnf		; no, not found
	sub.w	d3,d1			; next pixel is this one
	lsl.w	d3,d6			; shift search pattern
	lsl.w	d3,d7			; and mask
	bcc	ptr_swnw		; mask still in word, keep shifting
	subq.l	#2,a1
	move.w	d4,d7			; re-start with new mask...
	move.w	d7,d6			; ...and
	and.w	d2,d6			; ...pattern
	bra.s	ptr_swnw
;
;	Come here to read pixel colour
;
ptr_crpx
	move.w	d7,d0			; get mask...
	and.w	(a1),d0 		; ...and screen contents at that point
	bsr	ptr_mtoc		; convert mask to colour
	move.w	d0,d1			; return it
	moveq	#0,d0			; no problems
	rts
;
;	Set up to read pixels
;
;	Registers:
;		Entry				Exit
;	D0					error if out of range
;	D1	start co-ordinate		preserved
;	D4					amount mask is shifted
;	D7					first mask to use
;	A0	cdb				preserved
;	A1					first word to read
;	A2					colour pattern table
;
ptr_setu
	swap	d1
	cmp.w	sd_xsize(a0),d1 	; in range?
	bcc.s	ptr_exor		; nope
	swap	d1
	cmp.w	sd_ysize(a0),d1 	; how about y?
	bcc.s	ptr_exor		; that's wrong 
;
	move.w	d1,d3			; we start...
	add.w	sd_ymin(a0),d3		; ...absolutely this far...
	mulu	pt_bytel(a3),d3 	; ...down the screen
	add.l	sd_scrb(a0),d3		; here
	move.l	d3,a1			; now we can use an address register
	move.l	d1,d3
	swap	d3			; this is how far across
	add.w	sd_xmin(a0),d3		; absolutely
	move.w	d3,d4			; keep it for now
	and.w	#$fff8,d3		; make address...	 $$$$ MAGIC !!!!
	lsr.w	#2,d3			; ...offset in line	 $$$$ MAGIC !!!!
	add.w	d3,a1			; first (only) word to read 
;
	lea	m4tab(pc),a2		; start mask... 	 $$$$ MAGIC !!!!
	tst.b	sd_wmode(a0)		; ...might be here 
	beq.s	ptr_shim		; ...it is		 $$$$ MAGIC !!!!
	lea	m8tab(pc),a2		; ...no, it's here       $$$$ MAGIC !!!!
	and.w	#$fffe,d4		; shift should be even	 $$$$ MAGIC !!!!
ptr_shim
	move.w	8*2(a2),d7		; get mask from table
	and.w	#7,d4			; this is a shift	 $$$$ MAGIC !!!!
	lsr.w	d4,d7			; mask first pixel to read
;
	moveq	#0,d0			; no errors so far
	rts
;
ptr_exor
	moveq	#err.orng,d0
	rts
;
;	Colour mask tables: bit patterns for solid colours, leftmost
;	and rightmost pixel masks, and shift distances
;
m4tab
	dc.w	$0000,$0000,$00ff,$00ff,$ff00,$ff00,$ffff,$ffff
	dc.w	$8080,$0101
	dc.w	1
m8tab
	dc.w	$0000,$0055,$00aa,$00ff,$aa00,$aa55,$aaaa,$aaff
	dc.w	$c0c0,$0303
	dc.w	2
;
	end
