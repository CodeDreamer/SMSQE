* Write messages and query for SMSQ    1994   Tony Tebby
*
	section utils
*
	xdef	ut_messg		send message
	xdef	ut_mesa1		... pointed to by (a1)
	xdef	ut_msexq		file exists, (a1 preserved) query Y/N
	xdef	ut_msw2q		wild card 2 file query (a3/a4)
	xdef	ut_mswq 		wild card query (a1)
	xdef	ut_mswoq		wild card overwrite query
	xdef	ut_msovq		overwrite file (a1 preserved) query
	xdef	ut_qyn			query Y/N
	xdef	ut_qynaq		query Y/N/A/Q
*
	xref	ut_chd4 		channel (d4)
*
;	 xref	 ut_wrtch		 write character
;	 xref	 ut_wrtnl		 write newline
;	 xref	 ut_wrta1		 write (a1) relative
;	 xref	 ut_trap3		 trap3, d3=-1
*
	include 'dev8_sbsext_ext_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_msg8'
	include 'dev8_mac_assert'
*
* overwrite file?
*
ut_msovq
	movem.l d4/a1,-(sp)		save channel pointer / file pointer
	moveq	#0,d4			channel #0
	bsr.l	ut_chd4
	moveq	#ms.overw,d0		write 'OK to overwrite'
	bsr.s	ut_messg
	bsr.l	ut_wrtspc		space
	move.l	4(sp),a1
	bsr.l	ut_wrta1		write filename
	bsr.s	ut_qyn			prompt for y/n
	movem.l (sp)+,d4/a1		restore filename pointer
	rts
*
* single file exists, overwrite?
*
ut_msexq
	movem.l d4/a1,-(sp)		save channel pointer / file pointer
	moveq	#0,d4			channel #0
	bsr.l	ut_chd4
	bsr.l	ut_wrta1		write filename
	moveq	#ms.exist,d0		write exists message
	bsr.s	ut_messg
	moveq	#ms.overw,d0		write 'OK to overwrite'
	bsr.s	ut_messg
	bsr.s	ut_qyn			prompt for y/n
	movem.l (sp)+,d4/a1		restore filename pointer
utm_rts
	rts
*
* write a message
*
ut_messg
	lsr.w	#1,d0			convert to message code
	assert	ms.yn,0
	move.w	#msg8.yn,a1
	sub.w	d0,a1
	moveq	#sms.mptr,d0
	trap	#do.sms2
ut_mesa1
	move.w	(a1)+,d2		set d2 length of string
	moveq	#io.sstrg,d0		send string
	bra.l	ut_trap3
	page
*
* two file query
*
ut_msw2q
	cmp.w	#2,d7			is it force all?
	beq.s	utm_rts
	move.l	a3,a1			first filename
	bsr.l	ut_wrta1		write it
	moveq	#ms.to,d0		write ' TO '
	bsr.s	ut_messg		   message
	move.l	a4,a1			second filename
ut_mswq
	cmp.w	#2,d7			is it force all?
	beq.s	utm_rts 		... yes
	bsr.l	ut_wrta1		write filename
	bra.s	ut_qynaq		and prompt
*
* wild card overwrite
*
ut_mswoq
	tst.w	d7			is it overwrite all?
	bgt.s	utq_all 		... yes, done
	moveq	#ms.overw,d0		write overwrite message
	bsr.s	ut_messg
*
* query action in d7		-1 quit, 0 query, +1 all, +2 force all
*	reply in status 	EQ yes or all, NE no or quit
*
ut_qynaq
	bsr.s	utq_ynaq		set pointer to replies
	move.b	(a1),d4 		assume 'Y' reply
	tst.w	d7			all?
	bgt.s	utq_nl			... yes, just put <NL>
	moveq	#ms.ynaq,d0		Y/N/A/Q message
	moveq	#2,d3			four replies (0,1,2,3)
	bra.s	ut_query
ut_qyn
	moveq	#ms.yn,d0		yes/no message
	moveq	#0,d3			two replies only (0,1)
ut_query
	bsr.s	ut_messg		send message
	move.w	d3,d2			get reply key safe
utq_skip
	moveq	#io.fbyte,d0		fetch byte
	moveq	#0,d3			with immediate return
	trap	#3			to clear buffer
	tst.l	d0			character read?
	beq.s	utq_skip		... yes, get another
*
utq_loop
	bsr.s	utq_ynaq		set pointer to replies
	move.b	1(a1,d2.w),d4		set default reply
	moveq	#sd.cure,d0		enable cursor
	bsr.s	ut_trap3
	moveq	#io.fbyte,d0		fetch byte
	bsr.s	ut_trap3
	bne.s	utq_curs		... oops
	cmp.b	#$1b,d1 		is it escape?
	beq.s	utq_curs		... yes, treat as quit
	moveq	#$ffffffdf,d4
	and.b	d1,d4			set return byte (upper cased)
utq_curs
	moveq	#sd.curs,d0		suppress cursor
	bsr.s	ut_trap3
	cmp.b	#$20,d4 		check if in printable range
	bls.s	utq_loop		... try again
	cmp.b	#$bf,d4
	bhi.s	utq_loop		... try again
	move.b	d4,d1
	bsr.s	ut_wrtch		write character
utq_nl
	bsr.s	ut_wrtnl		and newline
*
	tst.b	bv_brk(a6)		check for break
	bge.s	utq_quit		... yes, quit
	bsr.s	utq_ynaq		set pointer to replies
	cmp.b	(a1)+,d4		is it yes?
	beq.s	utq_rts 		... yes
	cmp.b	(a1)+,d4		is it no?
	beq.s	utq_no			... yes
	tst.w	d2			are all and quit permitted?
	beq.s	utq_pcol		... no, try again
	cmp.b	(a1)+,d4		is it all?
	beq.s	utq_all 		... yes
	cmp.b	(a1),d4 		is it quit?
	beq.s	utq_quit		... yes
utq_pcol
	moveq	#sd.pcol,d0		backspace a column
	bsr.s	ut_trap3
	bra.s	utq_loop
*
utq_all
	move.w	#1,d7			set 'all' flag
utq_ok
	moveq	#0,d0			set EQ return
	rts
utq_quit
	moveq	#-1,d7			set do no more
utq_no
	tst.l	(sp)			set NE return
utq_rts
	rts
utq_ynaq
	move.w	#msg8.ynch,a1		get YNAQ
	moveq	#sms.mptr,d0
	trap	#do.sms2
	addq.l	#2,a1			skip count
	rts

* Write out bits and pieces    Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_trap3
	xdef	ut_trp3r
	xdef	ut_wrta1
	xdef	ut_wrtst
	xdef	ut_wrtnl
	xdef	ut_wrtch
*
*
ut_wrtspc
	moveq	#' ',d1
	bra.s	ut_wrtch
ut_wrtnl
	moveq	#$a,d1			newline
ut_wrtch
	moveq	#io.sbyte,d0		send one character
	bra.s	ut_trap3
*
ut_wrta1
	move.w	(a6,a1.l),d2		get character count
	addq.l	#2,a1			move pointer on
ut_wrtst
	moveq	#io.sstrg,d0		send string
*
* trap #4 then trap #3
*
ut_trp3r
	trap	#4			relative a6
*
* trap #3 preserving d3
*
ut_trap3
	move.l	d3,-(sp)		save d3
	moveq	#-1,d3			set infinite timeout
	trap	#3			do trap
	move.l	(sp)+,d3		restore d3
	tst.l	d0			test error return
	rts
	end
