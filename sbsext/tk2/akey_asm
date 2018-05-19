* ALT key processing  V0.02    1985  Tony Tebby   QJUMP
*
*	ALTKEY character,strings	setup strings for ALT character
*	ALTKEY character		clear strings for ALT character
*	ALTKEY				clear all altkey strings
*
	section ak
*
	xdef	ak_init 	initialise ALT key processing
	xdef	altkey
*
	xref	ut_fdev 	find device
	xref	ut_gtstr	get strings
	xref	err_bp
*
	include dev8_sbsext_ext_keys
	include dev8_keys_k
*
akd_pllk equ	$08		polling int linkage from A3
akd_plad equ	$0c		polling int routine address from A3
akd_list equ	$10		pointer to list of key definitions
akd_addr equ	$14		pointer to current definition being expanded
akd.len  equ	$40		actual length of block
*
ak_alchp
	moveq	#mt.alchp,d0		allocate space in common heap
	moveq	#0,d2			permanent
	trap	#1
	tst.l	d0
	rts
*
ak_init
	moveq	#akd.len,d1		allocate link block
	bsr.s	ak_alchp		in common heap
	bne.s	ak_rts
*
	move.l	a0,a4			save base
	moveq	#mt.inf,d0
	trap	#1			get base of sysvar
	lea	sv_pllst(a0),a0 	address of first polling link
aki_loop
	move.l	a0,a1			save link
	move.l	(a0),d0
	beq.s	ak_link 		end of list, link in us
	move.l	d0,a0			next address
	swap	d0			in ROM
	tst.w	d0
	bne.s	aki_loop
*
	move.l	(a0),(a4)		; move links
	move.l	4(a0),4(a4)
	move.l	a4,(a1) 		reset link pointer
	addq.l	#8,a4
	move.l	a1,a0
	bra.s	aki_loop		carry on
*
ak_link
	lea	ak_poll(pc),a1
	move.l	a1,4(a4)
	move.l	a4,(a0) 		set link
ak_rts
	rts
	page
*
* Set up altkey strings
*
altkey
	moveq	#sv_pllst,d3		look through the polling list
	lea	ak_poll(pc),a2		... for our routine
	bsr.l	ut_fdev
	bne.s	ak_rts1 		... not found!!!
	lea	akd_list-akd_pllk(a0),a4 set address of link list pointer
*
	move.l	bv_ribas(a6),d5 	find size of RI stack
	sub.l	bv_rip(a6),d5
*
	bsr.l	ut_gtstr		get lots of strings
	bne.s	ak_rts1 		... oops
*
	move.w	d3,d6			save number of strings
	bne.s	ak_rkey 		... at least one, remove this key def
*
	moveq	#0,d7			remove all, starting at zero
ak_rloop
	bsr.s	ak_rdef 		remove this definition
	subq.b	#1,d7			next
	bne.s	ak_rloop		... all done
ak_rts1
	rts
*
* before finding new definition, remove the old
*
ak_rkey
	move.l	a1,a5			keep RIP safe
	subq.w	#1,(a6,a5.l)		just one character?
	bne.l	err_bp			... no
	move.b	2(a6,a5.l),d7		get character
	addq.l	#4,a5			... and move to next string
	bsr.s	ak_rdef 		remove this definition
*
	subq.w	#1,d6			any new definition strings?
	ble.s	ak_rts2 		... no
*
	move.l	bv_ribas(a6),d1 	get total length RI stack
	sub.l	a5,d1
	sub.l	d5,d1			less length it started as
	addq.l	#5,d1			plus space for header
	bsr.l	ak_alchp		allocate it in heap
	bne.s	ak_rts2 		... oops
*
	move.l	a4,a1			link pointer
	move.w	ut..link,a2
	jsr	(a2)			and link in
*
	addq.l	#4,a0			put character in
	move.b	d7,(a0)+
*
* now copy all the strings
*
ak_ssloop
	move.w	(a6,a5.l),d1		length of string
	bra.s	ak_sclend
ak_scloop
	move.b	2(a6,a5.l),(a0)+	set character
	addq.l	#1,a5
ak_sclend
	dbra	d1,ak_scloop
*
	subq.w	#1,d6			another string?
	ble.s	ak_rts2 		... no
*
	move.b	#k.nl,(a0)+		put in a newline
*
	moveq	#3,d1			and fiddle around to get to next string
	add.l	a5,d1
	bclr	#0,d1
	move.l	d1,a5
	bra.s	ak_ssloop		copy next string
*
* remove definition for d7
*
ak_rdef
	move.l	a4,a1			link pointer
	move.l	a1,a0
ak_rdlook
	move.l	(a0),d0 		next pointer
	beq.s	ak_rts2 		... end
	move.l	d0,a0
	cmp.b	4(a0),d7		the right one
	bne.s	ak_rdlook		... no
*
	move.w	ut..unlnk,a2
	jsr	(a2)			... yes, unlink it
*
	moveq	#mt.rechp,d0		and return the space to the heap
	trap	#1
ak_rts2
	rts
	page
*
* ALT key polling routine
*
ak_poll
	move.l	sv_keyq(a6),a2		set keyboard queue pointer
	move.l	a3,a4			keep linkage block address safe
*
	move.l	akd_addr(a4),d0 	are we already expanding a queue?
	bgt.s	ak_stuff		... yes, stuff the queue
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
	bne.s	ak_sstrg		... no, set string
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
*
* expand an ALT key
*
ak_sstrg
	lea	akd_list(a4),a0 	set start of list
ak_slook
	move.l	(a0),d0 		end of list?
	beq.s	ak_rts5 		... yes
	move.l	d0,a0
	cmp.b	4(a0),d1		the right character?
	bne.s	ak_slook		... no, try again
*
	addq.l	#5,d0			move to start of string
	move.l	q_nxtout(a2),q_nextin(a2) remove all pending
*
* stuff the queue
*
ak_stuff
	move.l	d0,a1			set address
ak_stloop
	move.l	a1,akd_addr(a4)
	move.b	(a1)+,d1		get next character
	beq.s	ak_stend		... end of string
	move.w	io..qin,a3		put character in
	jsr	(a3)
	beq.s	ak_stloop		... ok, next
ak_rts5
	rts
ak_stend
	clr.l	akd_addr(a4)		clear string address
	rts
	end
