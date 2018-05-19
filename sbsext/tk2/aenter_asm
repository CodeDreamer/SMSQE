* ALT+ENTER line recall  V0.01	  1985-2017  Tony Tebby/Marcel Kilgus
	section ak
*
	xdef	ak_init 	initialise ALT+ENTER key processing
*
	include dev8_sbsext_ext_keys
	include dev8_keys_k
*
akd_pllk equ	$08		polling int linkage from A3
akd_plad equ	$0c		polling int routine address from A3
akd.len  equ	$40		actual length of block
*
* a4 = SysVars
*
ak_init
	moveq	#mt.alchp,d0		allocate space in common heap
	moveq	#akd.len,d1		allocate link block
	moveq	#0,d2			permanent
	trap	#1
	tst.l	d0
	bne.s	ak_rts
*
	lea	sv_pllst(a4),a2 	address of first polling link
aki_loop
	move.l	a2,a1			save link
	move.l	(a2),d0
	beq.s	ak_link 		end of list, link in us
	move.l	d0,a2			next address
	swap	d0			in ROM
	tst.w	d0
	bne.s	aki_loop
*
	move.l	(a2),(a0)		; move links
	move.l	4(a2),4(a0)
	move.l	a0,(a1) 		reset link pointer
	addq.l	#8,a0
	move.l	a1,a2
	bra.s	aki_loop		carry on
*
ak_link
	lea	ak_poll(pc),a1
	move.l	a1,4(a0)
	move.l	a0,(a2) 		set link
ak_rts
	rts

*
* ALT key polling routine
*
ak_poll
	move.l	sv_keyq(a6),a2		set keyboard queue pointer
	move.l	a3,a4			keep linkage block address safe
*
	move.w	io..qtest,a3		test the queue
	jsr	(a3)
	bne.s	ak_rts3 		... nothing there
	addq.b	#1,d1			... something, is it $FF
	bne.s	ak_rts3 		... ... no
*
* found ALT
*
	move.l	q_nxtout(a2),a1 	now check the next character
	addq.l	#1,a1
	cmp.l	q_end(a2),a1		off end?
	blt.s	ak_tempty		... no, test empty
	lea	q_queue(a2),a1		... yes, reset out pointer
ak_tempty
	move.l	q_nextin(a2),d2 	keep nextin pointer
	cmp.l	d2,a1			in and out the same?
	beq.s	ak_rts3 		... yes, can't read next character
	move.b	(a1),d1 		get next character
	cmp.b	#k.nl,d1		is it <NL>?
	bne.s	ak_rts4 		... no, we're done
*
	move.l	q_nxtout(a2),q_nextin(a2) eliminate alt <NL>
	moveq	#0,d0			now blat out all data
	bsr.s	ak_bline		back to beginning of line
	clr.b	(a1)			blat previous <NL>
	moveq	#-1,d0			do not blat
	bsr.s	ak_bline		back to the beginning of the previous li
	move.l	d1,q_nxtout(a2) 	nxtout after <NL>
ak_rts3
	rts
*
ak_bline
	lea	q_queue(a2),a3		set start of queue pointer
ak_bloop
	move.l	a1,d1			keep current pointer
	cmp.l	a3,a1			are we at start of queue buffer?
	bgt.s	ak_bnext		... no, back to next
	move.l	q_end(a2),a1		... yes, reset to end
ak_bnext
	cmp.b	#k.nl,-(a1)		... back one and check
	beq.s	ak_rts4 		... it is <NL>, done
	and.b	d0,(a1) 		blat if required
	cmp.l	d2,a1			have we gone all the way round?
	bne.s	ak_bloop		... no, carry on
ak_rts4
	rts

	end
