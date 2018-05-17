* Formatted print    V0.01    1985  Tony Tebby   QJUMP  
*
*	PRINT_USING #channel, format, items	formatted PRINT command
*
	section exten
*
	xdef	print_using
*
	xref	ut_chan1		get channel (default #1)
	xref	ut_chkri		check for room on RI stack
	xref	ut_gtst1		get one string on RI stack
	xref	ut_gtfp1		get one FP on stack
	xref	ut_wrtst		write string relative
	xref	idec_do 		do idec conversion
	xref	fdec_do 		do fdec conversion
	xref	fexp_do 		do fexp conversion
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
*	d2	length of format string (decremented)
*	d3	fill char
*	d4	dp type, sign  (or sign, dp type)
*	d5	msbit currency before sign, lsw ndp
*	d6	field width
*	d7	msbit scaled integer, msw comma count
*	a0	channel id
*	a1	pointer to buffer/rip
*	a2	pointer to buffer
*	a3	pointer to parameters
*	a4	pointer to format
*	a5	pointer to parameters
*
print_using
	bsr.l	ut_chan1		get output channel
	bne.s	pu_rts
*
	bsr.l	ut_gtst1		get a string (the format)
	bne.s	pu_rts
	move.l	a1,a4			save pointer
	move.w	(a6,a4.l),d2		and length
	addq.l	#2,a4
*
	move.l	bv_bfbas(a6),a1 	set pointer to buffer
*
pu_item
	moveq	#0,d7			not special
*
pu_loop
	bsr.s	pu_getf 		get next format character
	blt.s	pu_sbuf 		... end, send rest of buffer
	tst.b	d7			is it special string?
	beq.s	pu_esc			... no, check character
	cmp.b	d1,d7			is it end?
	bne.s	pu_putch		... no
	moveq	#0,d7			... yes, now it is not special
	bra.s	pu_loop
*
pu_esc
	cmp.b	#$7f,d1 		is it copyright?
	bne.s	pu_nl			... no, check newline
	bsr.s	pu_getf 		get next char
	bra.s	pu_putch		and transfer it
pu_nl
	cmp.b	#'\',d1 		is it newline?
	bne.s	pu_tspc 		... no, test if it is special
	moveq	#$a,d1
pu_tspc
	moveq	#-$22,d0
	add.b	d1,d0			is it " ($22)?
	beq.s	pu_special		... yes
	subq.b	#5,d0			is it ' ($27)?
	beq.s	pu_special		... yes 
	addq.b	#4,d0			is it # ($23)?
	beq.s	pu_field		... yes, start of field
	subq.b	#7,d0			is it * ($2a)?
	beq.s	pu_field		... yes, start of field
	subq.b	#1,d0			is it + ($2b)?
	beq.s	pu_splus		... yes, start field with plus or minus
	subq.b	#2,d0			is it - ($2d)?
	beq.s	pu_sminus		... yes, start field with minus
	addq.b	#5,d0			is it ( ($28)?
	beq.s	pu_sbrac		... yes, start field with bracket or space
	addq.b	#4,d0			is it $ ($24)?
	beq.s	pu_sfloat		... yes, start field with float currency
pu_putch
	bsr.s	pu_stuff		stuff character in
	beq.s	pu_loop 		... ok
	rts
*
pu_special
	move.b	d1,d7			start of special string
	bra.s	pu_loop
*
* Get next character from format
*
pu_getf
	subq.w	#1,d2			decrement count
	blt.s	pu_rts
	move.b	(a6,a4.l),d1		next character
	addq.l	#1,a4
pu_ok
	moveq	#0,d0
pu_rts
	rts
*
* Stuff one character into buffer
*
pu_stuff
	moveq	#1,d6			one character
	bsr.s	pu_cbuf 		check buffer
	bne.s	pu_rts			... oops
	move.b	d1,(a6,a1.l)		put character in
	addq.l	#1,a1			and move on
	bra.s	pu_ok
*
* Check the buffer
*
pu_cbuf
	add.w	d6,a1			set next pointer
	cmp.l	bv_bfbas+8(a6),a1	off end?
	sub.w	d6,a1
	ble.s	pu_ok			... no, done
*
* Send the buffer
*
pu_sbuf
	movem.w d1/d2,-(sp)		save our volatiles
	move.l	a1,d2			set number of characters
	move.l	bv_bfbas(a6),a1 	and buffer base
	sub.l	a1,d2
	bsr.l	ut_wrtst		write string from buffer
	movem.w (sp)+,d1/d2
	move.l	bv_bfbas(a6),a1 	reset pointer
	rts
	page
*
* Start decoding the field
*
pu_splus
	moveq	#1,d4			leading plus
	bra.s	pu_fset
pu_sminus
pu_sbrac
pu_sfloat
pu_field
	moveq	#0,d4			no leading sign
pu_fset
	sub.l	bv_ribas(a6),a4 	set format pointer relative
	move.l	a1,-(sp)
	moveq	#6*6,d1 		ensure there is room on RI stack
	bsr.l	ut_chkri		so nothing moves from now on
	move.l	(sp)+,a1
	add.l	bv_ribas(a6),a4 	reset format pointer
*
	moveq	#$7f,d7 		no commas
	moveq	#0,d6			no characters in field
	moveq	#0,d5			no dp
	swap	d4			no dp
	moveq	#' ',d3 		fill with spaces
	sub.l	a2,a2			no floating currency
*
	addq.l	#8,a3			next parameter
	cmp.l	a3,a5
pu_sbuf1
	ble.s	pu_sbuf 		... no more, done
*
	cmp.b	#'$',-1(a6,a4.l)	did it start with $
	beq.s	pu_flskp		... yes, skip floating currency
*
pu_flinc
	addq.w	#1,d6			one more character in field
pu_floop
	bsr.s	pu_getf 		get next field character
	blt.s	pu_fend1		... end of field
*	
	moveq	#-$21,d0
	add.b	d1,d0			is it ! ($21)?
	beq.l	pu_exp			... yes, exponent
	subq.b	#2,d0			is it # ($23)?
	beq.s	pu_fnext		... yes, just ordinary fill
	subq.b	#7,d0			is it * ($2a)?
	beq.s	pu_fstar		... yes, fill with star
	subq.b	#2,d0			is it , ($2c)?
	beq.s	pu_dp			... yes, decimal point
	subq.b	#2,d0			is it . ($2e)?
	beq.s	pu_dp			... yes, decimal point
	moveq	#4,d1			assume bracket
	addq.b	#5,d0			is it ) ($29)
	beq.s	pu_tsign		... yes, trailing sign
	moveq	#2,d1			assume minus
	subq.b	#4,d0			is it - ($2d)?
	beq.s	pu_tsign		... yes, trailing sign
	moveq	#3,d1			assume plus
	addq.b	#2,d0			is it + ($2b)?
	beq.s	pu_tsign		... yes, trailing sign
	addq.b	#7,d0			is it $ ($24)?
	beq.s	pu_flskp		yes skip floating currency
	sub.b	#$1a,d0 		is it >
	beq.s	pu_ishift		yes, set integer
*
	addq.w	#1,d2			backspace pointers
	subq.l	#1,a4
pu_fend1
	bra.s	pu_fend 		end of field
*
* Floating currency
*
pu_flskp
	clr.b	-1(a6,a4.l)		set start char to 0
pu_flloop
	move.l	a4,a2			set pointer to end
	bsr.l	pu_getf 		get character
	bne.s	pu_sbuf1		... end of format
	addq.w	#1,d6			one more character in field

	moveq	#-'#',d0
	add.b	d1,d0			start of numeric field
	beq.s	pu_flback		... yes
	subq.b	#'('-'#',d0
	beq.s	pu_csign		... yes
	subq.b	#'*'-'(',d0
	beq.s	pu_flback		... yes
	subq.b	#'-'-'*',d0
	beq.s	pu_csign		... yes
	addq.b	#'-'-'+',d0
	bne.s	pu_flloop
	moveq	#1,d4			leading plus
	swap	d4
pu_csign
	bset	#31,d5			currency before sign
pu_flback
	subq.l	#1,a2			... yes, backspace pointer to last curr
	bra.s	pu_floop		carry on with field
*
* DP (or thousands) found 
*
pu_dp
	tst.w	d4			any DP yet?
	beq.s	pu_sdp			... no
	moveq	#4,d7			... yes, set commas
pu_sdp
	move.b	d1,d4			set DP type
	clr.w	d5			reset NDP
	bra.s	pu_fone 		one more in field
*
* * found
*
pu_fstar
	moveq	#'*',d3 		set fill character
	bra.s	pu_fnext
*
* one more character in field
*
pu_ishift
	bset	#31,d7			flag integer shift
pu_fnext
	tst.w	d4			dp yet?
	beq.s	pu_fone 		... no
	addq.w	#1,d5			... yes
pu_fone
	addq.w	#1,d6			field is one greater
	bra.l	pu_floop		... next character
*
* print exponent format
*
pu_exp
	subq.w	#3,d2			there should be four !!!
	addq.l	#3,a4
	move.w	d5,d6			field is ndp+7
	ble.l	err_bp
	addq.w	#7,d6
	neg.w	d7			flag exponent format
	bra.s	pu_fend
*
* sign at end of field
*
pu_tsign
	addq.w	#1,d6			one more character
	swap	d4			put trailing sign in top
	move.w	d1,d4			of d4
	swap	d4
*
* end of field
*
pu_fend
	bsr.l	pu_cbuf 		check buffer for room
	movem.l d2/d3/a1/a2/a3/a4/a5,-(sp)
	moveq	#0,d0			assume no value
	tst.b	4(a6,a3.l)		any value?
	blt.s	pu_sstrg		... no
	move.w	#$0f0f,d0
	and.w	(a6,a3.l),d0
	beq.s	pu_sstrg		null
*
	tst.w	d4			any DP?
	bne.s	pu_dec			... yes, must be decimal
*
	subq.b	#1,d0			is field a string?
	bgt.s	pu_dec			... no, decimal
*
	move.l	4(a6,a3.l),a2		get string pointer
	add.l	bv_vvbas(a6),a2
	sub.w	#$300,d0		simple string?
	blt.s	pu_sslen		... yes
	movem.w 4(a6,a2.l),d0/d1	nr dimensions or substring base / top
	move.l	(a6,a2.l),a2
	add.l	bv_vvbas(a6),a2
	beq.s	pu_ssarr		... it was array
	subq.w	#1,d0			adjust substring start
	sub.w	d0,d1
	add.w	d0,a2
	move.w	d1,d0
	bra.s	pu_sstrg		and send string
pu_ssarr
pu_sslen
	move.w	(a6,a2.l),d0		set string length
	addq.l	#2,a2
pu_sstrg
	move.w	d6,d2			and field width
pu_strloop
	moveq	#' ',d1 		fill with ' '
	subq.w	#1,d0			all string written?
	blt.s	pu_strset		... yes
	move.b	(a6,a2.l),d1		... no, next character
	addq.l	#1,a2
pu_strset
	move.b	d1,(a6,a1.l)		set next string character
	addq.l	#1,a1
	subq.w	#1,d2			set hole in field
	bgt.s	pu_strloop
	bra.s	pu_fdone
*
pu_dec
	bsr.l	ut_gtfp1		get a floating point
	bne.s	pu_fexit		... oops
	movem.l (sp),d2/d3/a4/a5	restore fill character, and set addresses
	tst.w	d7			exponent form?
	bge.s	pu_fdec 		... no
	bsr.l	fexp_do 		do exponent conversion
	bra.s	pu_fdone
pu_fdec
	tst.l	d7			shifted integer>
	bmi.s	pu_idec 		... yes
	bsr.l	fdec_do 		do decimal conversion
	bra.s	pu_fdone
pu_idec
	bsr.l	idec_do
pu_fdone
	moveq	#0,d0			ok
pu_fexit
	movem.l (sp)+,d2/d3/a1/a2/a3/a4/a5
	add.w	d6,a1			move buffer pointer on
	beq.l	pu_item 		... next item
	rts
	end
