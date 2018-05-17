* ED - Find BASIC line		 1985	Tony Tebby   QJUMP
*
	section ed
*
	xdef	ed_bscan
	xdef	ed_stlin
	xdef	ed_fline
	xdef	ed_flins
	xdef	ed_nline
	xdef	ed_pline
*
	xref	ed_setps
	xref	ed_xplin
*
	include dev8_sbsext_ed_keys
	include dev8_sbsext_ed_data
*
* Scan backwards
*
*	d1 c s	number of lines to go back
*	d2   r	line length
*	d3   r	row within line
*	d4   r	line number
*	a4   r	address of line
*
ed_bscan
	move.w	d1,d4
	bsr.l	ed_setps	set current cursor position before call pr.list
	move.w	d4,d1
	move.w	edr_rno(a5),d3	get current top row
	move.w	edr_lnr(a5),d4
	move.w	d1,-(sp)	save number of rows to scan back
	bsr.s	ed_fline	find line
	bne.s	edbs_exit	... no file
edbs_loop
	sub.w	d3,(sp) 	move back by one line
	ble.s	edbs_done	... we've gone back far enough
	bsr.l	ed_pline	find previous line
	bne.s	edbs_top	... at start
	move.w	d1,d4
	bsr.l	ed_xplin	expand it
	move.l	bv_bfp(a6),d3	find length in chars
	sub.l	bv_bfbas(a6),d3
	subq.l	#ed.xoff+1,d3	less indent - 1 to round up!
	divs	ed_ncolx(a5),d3 (for once DIVS is OK - truncates towards zero)
	addq.w	#1,d3		actual number of rows
	bra.s	edbs_loop
*
edbs_top
	clr.w	(sp)		at top, start at row zero of line
edbs_done
	move.w	(sp),d3 	set number of row in line to start at
	neg.w	d3
	bsr.s	ed_stlin	set top line address
edbs_exit
	addq.l	#2,sp		remove back scan count from stack
	rts
*
*	d1  r	line number found (0 is not found, else >= d4)
*	d2 cr	line length
*	d4 c p	line number to be found (fline only)
*	a4 cr	address of line wrt base of BASIC vars
*
* Set top line addresss and length
*
ed_stlin
	move.l	a4,a1		save top line address
	sub.l	bv_pfbas(a6),a1
	move.l	a1,ed_topad(a5)
	move.w	d2,ed_topln(a5) and length of line
	bra.s	edf_rtst
*
* Find line from scratch
*
ed_fline
	move.l	ed_topad(a5),a4 set pointer to program file
	add.l	bv_pfbas(a6),a4
	move.w	ed_topln(a5),d2 set length of top line
	bne.s	ed_flins	... set
	move.w	(a6,a4.l),d2	set it
*
* Find line with d2, a4 already set
*
ed_flins
	moveq	#0,d0			clear error ret
	moveq	#0,d1			set no line
	cmp.l	bv_pfp(a6),a4		pointer off end?
	bge.s	edf_or			... yes, out of range
	move.w	pf_lno(a6,a4.l),d1	check line number
	cmp.w	d4,d1
	bgt.s	edf_back		... we need to go back
	beq.s	edf_rts 		... we are there 
edf_fwd
	bsr.s	ed_nline		move to next line
	bne.s	edf_rtst		... off end
	cmp.w	d4,d1			are we there yet?
	blt.s	edf_fwd
edf_rtst
	tst.l	d0			set status
edf_rts
	rts
*
edf_back
	bsr.s	ed_pline		previous line
	bne.s	exit_ok 		(off start, found line)
	bsr.s	ed_pline		move back
	cmp.w	d4,d1			there yet?
	beq.s	edf_rts 		... exactly
	bgt.s	edf_back		not yet
*
ed_nline
	addq.l	#2,a4			skip link
	add.w	d2,a4			move to next line
	cmp.l	bv_pfp(a6),a4		end of file?
	bge.s	edn_eof
	add.w	pf_link(a6,a4.l),d2	and set new line length
slin_ok
	move.w	pf_lno(a6,a4.l),d1
exit_ok
	moveq	#0,d0			OK
	rts
*
ed_pline
	cmp.l	bv_pfbas(a6),a4 	beginning of file?
	ble.s	edp_bof
	sub.w	pf_link(a6,a4.l),d2	set length of previous line
	sub.w	d2,a4			and move back
	subq.l	#2,a4			to link
	bra.s	slin_ok 		set new line length
*
* at end or beginning of file
*
edn_eof
	sub.w	d2,a4			reset to this line
	subq.w	#2,a4
edp_bof
	bsr.s	slin_ok 		set line number
edf_or
	moveq	#err.or,d0		and say out of range
	rts
	end
