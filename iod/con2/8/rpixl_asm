;	Read/scan pixel colour	v0.00   Sept 1987  J.R.Oakley	QJUMP
;	2017-03-20  1.01  adapted for 8 bit colour (dgw)
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
ptr_exor
	moveq	#err.orng,d0
	rts
;
pt_rpixl
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
	add.w	d3,a1
;
	tst.l	d2			; are we scanning?
	bpl	ptr_crpx		; no, just read pixel
;
	btst	#iop..gcl,d2		; were we given a colour?
	bne.s	ptr_fgcl		; yes, just find it
	move.b	(a1),d2 		; else set current as "parameter"
ptr_fgcl
	moveq	#-1,d3			; assume scan is north or west
	btst	#iop..slr,d2		; east/west scan?
	beq.s	ptr_snss		; no, set up for north/south
;
;	Scanning east or west
;
	moveq	#-1,d4			; west pixel displacement
	swap	d1			; start co-ordinate is this
	move.w	d1,d5			; assume we're going west...
	btst	#iop..sdr,d2		; are we?
	beq.s	ptr_jmps		; yes, jump into scan routine
	neg.w	d3			; else east  
	neg.w	d4			; opposite scan direction
	sub.w	sd_xsize(a0),d5 	; amount left to scan...
	neg.w	d5			; ...is...
	subq.w	#1,d5			; ...this much
	bra.s	ptr_jmps		; do scan
;
;	Scanning north or south
;
ptr_snss
	move.w	pt_bytel(a3),d4
	neg.w	d4			; bytes per row
	move.w	d1,d5			; there's this many pixels left
	btst	#iop..sdr,d2		; are we scanning north?
	beq.s	ptr_jmps		; yes, do scan
	neg.w	d3			; else south
	neg.w	d4			; opposite scan direction
	sub.w	sd_ysize(a0),d5 	; this many...
	neg.w	d5
	subq.w	#1,d5			; ...pixels to go
;
;	We now have
;
;	D1	start co-ordinate
;	D2	colour
;	D3	coord displacement (+/- 1)
;	D4	pixel scan increment
;	D5	pixels left to window edge
;
;
ptr_jmps
	btst	#iop..ssc,d2		; search same colour?
	beq.s	ptr_diff		; no
;
;	Scan for same colour
;
	btst	#iop..gcl,d2		; were we given a colour?
	beq.s	ptr_sskp		; no, skip first pixel
ptr_snxt
	cmp.b	(a1),d2 		; same as required?
	beq.s	ptr_done		; yes, finished found
ptr_sskp
	subq.w	#1,d5			; can we do more?
	bmi.s	ptr_dnnf		; no, not found
	add.w	d3,d1			; update coord
	add.w	d4,a1			; next pixel
	bra.s	ptr_snxt
;
;	Scan for different colour
;
ptr_diff
	btst	#iop..gcl,d2		; were we given a colour?
	beq.s	ptr_dskp		; no, skip first pixel
ptr_dnxt
	cmp.b	(a1),d2 		; same as required?
	bne.s	ptr_done		; no, finished found
ptr_dskp
	subq.w	#1,d5			; can we do more?
	bmi.s	ptr_dnnf		; no, not found
	add.w	d3,d1			; update coord
	add.w	d4,a1			; next pixel
	bra.s	ptr_dnxt
;
;	Scan exit points
;
ptr_done
	swap	d1			; put new position in msw
	clr.w	d1
	move.b	(a1),d1 		; colour in lsw
	bra.s	ptr_exok
ptr_dnnf
	moveq	#-1,d1			; no position, no colour
	bra.s	ptr_exok
;
;	Read pixel colour
;
ptr_crpx
	clr.w	d1
	move.b	(a1),d1 		; return pixel colour
ptr_exok
	moveq	#0,d0			; no problems
	rts

	end
