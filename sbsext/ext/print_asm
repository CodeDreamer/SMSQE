; PRINT / INPUT / WIDTH       V2.01      1990	Tony Tebby   QJUMP
;
; 2006-04-02  2.01  INPUT returns buffer overlow for compiled programs (MK)
; 2017-01-30  2.02  INPUT buffer is limited to max string length (32K -2) (wl)

	section exten

	xdef	print
	xdef	input
	xdef	width

	xref	ut_chan1
	xref	ut_gtst1
	xref	ut_gtin1
	xref	ut_gxin1
	xref	ut_remst
	xref	ut_wrtch
	xref	ut_wrtnl
	xref	ut_wrtst
	xref	ut_trp3r
	xref	ut_trap3
	xref	ut_chkri
	xref	ut_chkbf

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_io'

width
	jsr	ut_chan1		 ; channel
	bne.s	pr_rts
	jsr	ut_gxin1
	bne.s	pr_rts
	move.w	(a6,a1.l),ch_width(a6,d4.l) ; set width
	rts

pr_ipar
	moveq	#err.ipar,d0
pr_rts
	rts

print
	moveq	#0,d7		 ; output only
	bra.s	pr_do
input
	moveq	#-1,d7		 ; input only?
pr_do
	jsr	ut_chan1	 ; get channel default #1
	bne.s	pr_rts

	move.l	sb_buffb(a6),a1
	moveq	#iow.chrq,d0
	jsr	ut_trp3r
	bne.s	pr_chpos
	move.w	(a6,a1.l),ch_width(a6,d4.l) ; set width
	move.w	4(a6,a1.l),ch_colmn(a6,d4.l) ; and position
	moveq	#1,d0
	and.l	d0,d7		 ; input and output (+1) or output (0) only

pr_chpos
	move.w	ch_colmn(a6,d4.l),d6 ; current character pos

	moveq	#2,d5		 ; fake up ';' to start with
	swap	d5
	cmp.l	a3,a5		 ; any parameters
	beq.l	pr_final	 ; ... no, new line at end of nothing

; major loop through params

pr_loop
	moveq	#0,d2		 ; pretend zero length
	move.b	1(a6,a3.l),d5	 ; save separator
	lsr.b	#4,d5
	and.w	#$7,d5
	swap	d5		 ; get previous separator
	cmp.b	#5,d5		 ; TO?
	bne.s	pr_type

	move.l	#$20002,d5	 ; reset to semi;semi

	jsr	ut_gtin1	 ; get TO
	bne.s	pr_rts
	move.w	(a6,a1.l),d3	 ; position to skip to
	addq.l	#2,a1
	tst.b	d7		 ; print permitted?
	bmi.l	pr_eloop	 ; ... no
	bsr.l	pr_skip
	bra.l	pr_eloop

pr_type
	move.w	#$0f0f,d3
	and.w	(a6,a3.l),d3
	beq.s	pr_sep		 ; nothing, do previous separator
	tst.b	d7		 ; input permitted?
	beq.s	pr_getp 	 ; ... no
	tst.w	nt_name(a6,a3.l) ; name?
	blt.s	pr_getp 	 ; ... no

	cmp.b	#nt.arr,(a6,a3.l) ; array?
	bne.s	pr_geti 	 ; ... no

	cmp.b	#nt.st,d3	 ; string?
	bgt	pr_ipar 	 ; ... no
	move.l	nt_value(a6,a3.l),d0
	add.l	sb_datab(a6),d0
	cmp.w	#1,4(a6,d0.l)	 ; one dimensional?
	bne	pr_ipar 	 ; ... no

pr_geti
	bset	#31,d2		 ; flag input
	bra.s	pr_sep

pr_getp
	move.l	nt_value(a6,a3.l),d0 ; a value?
	bmi.s	pr_star

	cmp.b	#nt.arr,nt_nvalp(a6,a3.l) ; array?
	beq.l	pr_array	  ; ... yes


	jsr	ut_gtst1	 ; get one string onto stack
	bne	pr_rts
	moveq	#0,d2
	move.w	(a6,a1.l),d2	 ; length of item
	lea	2(a1),a4	 ; pointer
	jsr	ut_remst	 ; clean up
	bra.s	pr_sep

pr_star
	move.l	sb_buffb(a6),a4
	moveq	#1,d2
	move.b	#'*',(a6,a4.l)	 ; unset variable

pr_sep
	bsr.l	pr_dosep

	tst.l	d2		 ; input or output?
	bmi.s	pr_inp		 ; input
	beq.l	pr_eloop	 ; ... neither

	tst.b	d7		 ; print permitted?
	bmi.l	pr_eloop	 ; ... no
	move.l	a4,a1
	jsr	ut_wrtst	 ; write string
	bne.s	pr_rts2
	add.w	d2,d6		 ; position
	bra.l	pr_eloop

pr_inp
	move.l	sb_buffb(a6),sb_buffp(a6)
pr_inpl
	move.l	sb_buffp(a6),a1
	move.l	sb_buffb+8(a6),d2
	sub.l	a1,d2		 ; buffer length
	move.w	d2,a2
	cmp.l	d2,a2		 ; word overflowed?
	beq.s	pr_inpdo
	move.l	#$7ffe,d2	 ; max length  *** 2.02
pr_inpdo
	moveq	#iob.flin,d0
	jsr	ut_trp3r
	beq.s	pr_inpok
	moveq	#err.bffl,d1
	cmp.l	d1,d0		 ; buffer full?
	bne.s	pr_curd
	tst.b	d7		 ; console (print permitted)?
	bpl.s	pr_rts2 	 ; ... no, give up
	move.l	a1,sb_buffp(a6)  ; set pointer
	cmpi.l	#sb.flag,sb_flag(a6) ; interpreted BASIC?		     MK
	bne.s	pr_bffl 	 ; ... no, don't try to resize the buffer!   MK

**** 2.02
	move.l	#$7ffe,d1	 ; max length (a1 points AFTER the bytes got)
	add.l	sb_buffb(a6),d1  ; + buffer base
	sub.l	sb_buffp(a6),d1  ; - buffer pointer = 7ffe - number in buffer
	ble.s	pr_bffl 	 ; we're at max capacity
	move.l	d1,d2		 ; nbr of bytes to get
			
	jsr	ut_chkbf	 ; allocate, puts sb_buffp(a6) into A1
	bra.s	pr_inpdo	 ; DON'T check d2 any more
			  
**** 2.02
			
pr_curd
	move.l	d0,-(sp)
	moveq	#iow.dcur,d0
	jsr	ut_trap3
	move.l	(sp)+,d0
pr_rts2
	rts

pr_bffl
	bsr.s	pr_inpok	 ; do return what we've got so far           MK
	moveq	#err.bffl,d0	 ; and re-issue error condition 	     MK
	rts								     MK

pr_inpok
	sub.l	sb_buffb(a6),a1  ; total length of item
	subq.l	#1,a1		 ; length of item
	move.w	a1,d3		 ; keep it
	add.w	d3,d6		 ; current posn

	tst.b	d7		 ; print permitted?
	bmi.s	pr_xfer 	 ; ... no
	moveq	#iow.pcol,d0
	jsr	ut_trap3	 ; previous
	bne.s	pr_xfer
	moveq	#iow.ncol,d0	 ; ... next to remove pending NL
	jsr	ut_trap3

pr_xfer
	move.l	a0,a4		 ; keep channel ID

	move.l	sb_arthp(a6),a1  ; result here please
	move.l	sb_buffb(a6),a0  ; coming from here

	moveq	#$f,d0
	and.b	1(a6,a3.l),d0	 ; type of parameter required
	subq.b	#2,d0
	blt.s	pr_instr	 ; ... string
	bgt.s	pr_inint

	jsr	cv.decfp*3+qlv.off	; ... floating point
	bra.s	pr_cv
pr_inint
	jsr	cv.deciw*3+qlv.off	; ... integer
pr_cv
	bne.s	pr_rts0 	 ; ... oops
	bra.s	pr_putp

pr_instr
	moveq	#1,d2		 ; round up
	and.w	d3,d2
	add.w	d3,d2
	add.w	d2,a0		 ; end of string
	moveq	#2,d1
	add.w	d2,d1
	jsr	ut_chkri	 ; check for room

pr_cploop
	subq.l	#2,a0
	subq.l	#2,a1
	move.w	(a6,a0.l),(a6,a1.l)
	subq.w	#2,d2
	bge.s	pr_cploop

	move.w	d3,(a6,a1.l)

pr_putp
	move.l	a1,sb_arthp(a6)  ; RIP up to date
	jsr	sb.putp*3+qlv.off     

	move.l	a4,a0		 ; restore channel ID

pr_eloop
	addq.l	#8,a3
	cmp.l	a3,a5		 ; any more params
	bgt.l	pr_loop

	swap	d5		 ; last separator
	moveq	#%00001011,d0
	btst	d5,d0		 ; 1 and 3 need action
	beq.s	pr_done

pr_final
	bsr.s	pr_dosep	 ; do final sep
pr_done
	move.w	d6,ch_colmn(a6,d4.l) ; set position

	moveq	#0,d0
pr_rts0
	rts

pr_dosep
	tst.b	d7		 ; print permitted?
	bmi.s	pr_rts1 	 ; ... no

	cmp.b	#1,d5		 ; tab?
	beq.s	pr_dotab
	blt.s	pr_nl		 ; 0 is newline like 3

	cmp.b	#3,d5		 ; semi colon?
	blt.s	pr_rts1 	 ; ... yes
	beq.s	pr_nl		 ; ... no, newline

	bsr.s	pr_sroom
	move.w	d6,d3		 ; !!!!!
	beq.s	pr_rts1 	 ; at start of line
	addq.w	#1,d3		 ; new position
	move.w	d2,d0
	add.w	d3,d0
	bra.s	pr_room 	 ; room on line?

pr_dotab
	bsr.s	pr_sroom
	move.w	d6,d3		 ; current posn
	addq.w	#8,d3
	lsr.w	#3,d3
	lsl.w	#3,d3
	move.w	d3,d0
	addq.w	#8,d0
pr_room
	cmp.w	ch_width(a6,d4.l),d0 ; enough room for this column
	bgt.s	pr_nl		 ; ... no

pr_skip
	moveq	#' ',d1 	 ; pad with spaces
	jsr	ut_wrtch
	addq.w	#1,d6
	cmp.w	d3,d6
	blt.s	pr_skip
pr_rts1
	rts

pr_nl
	jsr	ut_wrtnl	 ; new line
	moveq	#0,d6
	rts

pr_sroom
	move.w	ch_width(a6,d4.l),d0
	beq.s	pr_rts1 	 ; no specified width
	ext.l	d6
	divu	d0,d6
	swap	d6		 ; remainder gives position
	rts
; Here we do arrays
; We set up, (at $20 in the buffer) a block which looks a bit like the array
; descriptor: the count is omitted and there is an extra index which has a
; zero upper limit. There is both a count and a limit for each index and the
; multiplier is a long word (to simplify FP). The indexes are ls index first.
;
;	pointer 	long	current item pointer
;	  index limit	   word
;	  current index    word
;	  index multiplier long
;
pr_desc equ	$20

pr_array
	move.l	sb_buffb(a6),a2
	add.w	#pr_desc,a2
	move.l	d0,a0
	add.l	sb_datab(a6),a0
	move.l	(a6,a0.l),a1
	add.l	sb_datab(a6),a1
	move.l	a1,(a6,a2.l)
	addq.l	#4,a2		 ; initial pointer
	move.w	4(a6,a0.l),d0	 ; number of indices
	beq	pr_eloop

	move.w	d0,d1
	lsl.w	#2,d1
	lea	2(a0,d1.w),a0	 ; last index

	tst.b	d3		 ; substring?
	beq.s	pr_sstra	 ; ... yes
	subq.b	#nt.fp,d3	 ; something else?
	blt.s	pr_stra 	 ; string
	bgt.s	pr_inta 	 ; int

	moveq	#6,d2		 ; scale by 6 bytes
	lea	pr_fpel,a4	 ; and convert from fp
	bra.s	pr_arslend

pr_sstra
	moveq	#1,d1		 ; assume single characters
	cmp.w	#1,2(a6,a0.l)	 ; substrings to strings?
	bne.s	pr_sstras	 ; ... no

	subq.w	#1,d0		 ; one fewer indices for substring
	subq.l	#4,a0
	move.w	4(a6,a0.l),d1	 ; string length
pr_sstras
	move.w	d1,-6(a6,a2.l)	 ; length
	moveq	#1,d2		 ; scale by 1 byte
	lea	pr_sstel,a4	 ; write substrings
	bra.s	pr_arslend

pr_stra
	subq.w	#1,d0		 ; one fewer indices for string
	subq.l	#4,a0
	moveq	#1,d2		 ; scale by 1 byte
	lea	pr_stel,a4	 ; write strings
	bra.s	pr_arslend

pr_inta
	moveq	#2,d2		 ; scale by 2 bytes
	lea	pr_inel,a4	 ; write integers
	bra.s	pr_arslend

pr_arsloop
	move.w	(a6,a0.l),(a6,a2.l) ; max index
	clr.w	2(a6,a2.l)	 ; counter
	move.w	d2,d3
	mulu	2(a6,a0.l),d3	 ; multiplier for last index
	move.l	d3,4(a6,a2.l)
	addq.l	#8,a2		 ; next index
	subq.l	#4,a0
pr_arslend
	dbra	d0,pr_arsloop
	clr.l	(a6,a2.l)

pr_ardoloop
	jsr	(a4)		 ; type specific routine for item (a1)

	move.l	ch_chid(a6,d4.l),a0 ; channel
	bsr.l	pr_dosep	 ; do separator
	move.l	d5,d0
	swap	d0
	move.w	d0,d5		 ; all separators the same now

	move.l	a2,a1
	jsr	ut_wrtst	 ; write string
	bne.s	pr_rts3
	add.w	d2,d6		 ; position

	move.l	sb_buffb(a6),a1
	add.w	#pr_desc,a1
	move.l	(a6,a1.l),d3	 ; pointer
	lea	4(a1),a2
pr_arinc
	move.w	(a6,a2.l),d0	 ; next upper limit
	beq.l	pr_eloop	 ; none, done
	cmp.w	2(a6,a2.l),d0	 ; this index at limit
	bgt.s	pr_arsnext	 ; ... no, set next
	move.w	d0,d1
	mulu	6(a6,a2.l),d0	 ; lswords
	mulu	4(a6,a2.l),d1	 ; ms word
	swap	d1
	add.l	d1,d0		 ; total to backspace
	sub.l	d0,d3		 ; backspace pointer
	clr.w	2(a6,a2.l)	 ; and index
	addq.l	#8,a2		 ; next index
	bra.s	pr_arinc

pr_arsnext
	add.w	#1,2(a6,a2.l)	 ; move index on
	add.l	4(a6,a2.l),d3	 ; move pointer on
	move.l	d3,(a6,a1.l)	 ; save it
	move.l	d3,a1
	bra.s	pr_ardoloop


pr_sstel
	move.l	sb_buffb(a6),a2
	move.w	pr_desc-2(a6,a2.l),d2 ; string length
	move.l	a1,a2
pr_rts3
	rts

pr_stel
	move.w	(a6,a1.l),d2	 ; genuine string
	lea	2(a1),a2
	rts

pr_fpel
	move.l	a1,a2
	move.l	sb_cmdlb(a6),a1  ; put in  top end of buffer
	subq.l	#6,a1
	move.w	(a6,a2.l),(a6,a1.l)
	move.l	2(a6,a2.l),2(a6,a1.l)
	move.l	sb_buffb(a6),a0
	jsr	cv.fpdec*3+qlv.off	; convert in buffer

pr_prel
	move.l	a0,d2
	move.l	sb_buffb(a6),a2  ; characters
	sub.l	a2,d2		 ; length
	rts

pr_inel
	move.l	sb_buffb(a6),a0
	jsr	cv.iwdec*3+qlv.off	; convert in buffer
	bra.s	pr_prel

	end
