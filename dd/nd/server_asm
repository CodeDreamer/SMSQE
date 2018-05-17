* Network server   V0.1    1985  Tony Tebby   QJUMP
*
*	FSERVE			sets up file server
*
	section nd
*
	xdef	fserve
*
	xref	ut_gtnm1	get name
	xref	ut_gxli1	get buffer size
	xref	ut_fjob 	find job (name in a1)
	xref	ut_fdev 	find device linkage block
	xref	ut_crjob	create resident job
	xref	ut_ajob 	activate job
	xref	ut_rjbme	remove me
*
	include dev8_dd_nd_keys
*
* Network server definition
*
ns_jdef dc.w	64		64 bytes stack (excessive)
	dc.w	net_server-*
ns_name dc.w	6,'Server'
*
nsv_nam dc.w	3,'NSV' 	network server device name
*
* Check if server is running
*
ns_chjob
	move.l	bv_bfbas(a6),a1 	put name in BASIC buffer
	move.l	ns_name(pc),(a6,a1.l)
	move.l	ns_name+4(pc),4(a6,a1.l)
	bra.l	ut_fjob 		find job
*
* Start network server
*
fserve
	bsr.s	ns_chjob		check if already running
	beq.s	nss_rts
	lea	ns_jdef(pc),a4		start up job
	bsr.l	ut_crjob
	bne.s	nss_rts
	moveq	#8,d2			priority 8
	bra.l	ut_ajob
nss_rts
	rts
	page
*
* Network server job
*
net_server
	moveq	#io.open,d0		open my net channel
	moveq	#myself,d1
	moveq	#io.old,d3		just for me
	lea	nsv_nam(pc),a0
	trap	#2
	tst.l	d0
ns_suicide
	bne.l	ut_rjbme		suicide
	move.l	a0,a5			save the net channel
*
ns_loop
	moveq	#0,d3			return immediately
ns_loopw
	moveq	#io.fstrg,d0		fetch a packet
	move.l	a5,a0
	trap	#3
	tst.l	d0			ok?
	beq.s	ns_action		... yes
	addq.l	#-err.nc,d0		was it nc?
	bne.s	ns_suicide
*
	moveq	#mt.susjb,d0		suspend
	moveq	#myself,d1		... me
	moveq	#5,d3			for 5 ticks
	sub.l	a1,a1
	trap	#1
	bra.s	ns_loop
*
* packet read, action it
*
ns_action
	bsr.s	ns_do			do action
	moveq	#io.sstrg,d0		and send reply
	move.b	d6,d2			set action
	move.w	#1000,d3		timeout after 20 seconds
	move.l	a5,a0
	trap	#3 
	moveq	#50,d3			wait up to one second
	bra.s	ns_loopw		next action (and wait)
*
* do action
*
ns_do
	move.l	a1,a2			set block address
	move.l	(a1)+,a0		get id (or d3!!)
	move.b	d1,d6			and save action key
	move.l	(a1)+,d1		get d1
	move.l	(a1)+,d2		and d2
	tst.b	d6			check action
	bgt.s	ns_trap 		... it is a trap
*
* read or write complete block
*
nsb_do
	move.l	a1,a3			save block pointer
	moveq	#fs.posab,d0		absolute position
	moveq	#0,d3
	trap	#3
	move.l	a3,a1			reset block pointer
	moveq	#io.sstrg,d0		assume send string
	bchg	#0,d6			was it send or get?
	beq.l	ns_do_tr3		... send
*
	moveq	#sd.cure,d0		... fetch, try enabling the cursor
	trap	#3
	move.l	a3,a1			restore string pointer
	tst.l	d0			did it enable?
	bne.s	nsb_get 		... no, get block
	bsr.s	nsb_get 		... yes, get what we can
	moveq	#io.sstrg,d0		and echo it
	move.w	d1,d2
	move.l	a3,a1
	trap	#3
	rts
*
nsb_get
	moveq	#io.fstrg,d0		fetch bytes
	bra.s	ns_ftrap		and set return length
*
* packet is a trap
*
ns_trap
	moveq	#io.close,d0		assume close
	cmp.b	#io.close,d6		trap #2  or  trap #3
	bgt.s	ns_trap3		... trap #3
	beq.s	ns_trap2		... close trap #2
*
	moveq	#io.delet,d0		assume delete
	move.w	a0,d3			long word is actually d3 key, not ID
	subq.l	#8,a1			there are no additional params!!
	move.l	a1,a0			pointer to name in a0
	tst.b	d3
	blt.s	ns_trap2		... key negative
	moveq	#io.open,d0		... key positive, open file
	bsr.s	ns_trap2
	bne.s	nst2_rts
	move.l	a0,(a2)+		set id
*
	moveq	#fs.posab,d0		position absolute
	moveq	#0,d1			... to start position
	moveq	#0,d3
	trap	#3
	moveq	#err.bp,d1		check if BP
	cmp.l	d1,d0
	seq	(a2)+			set serial only flag
	rts
*
ns_trap2
	moveq	#myself,d1
	move.l	a2,-(sp)		save a2 (for delete)
	trap	#2			do trap #2
	move.l	(sp)+,a2
	move.l	d0,(a2)+		set error return
nst2_rts
	rts
*
* Trap #3 actions
*
ns_trap3
	move.b	d6,d0			set action
	moveq	#-sd.pxenq,d5		is it fetch window info?
	add.b	d6,d5
	subq.b	#sd.chenq-sd.pxenq,d5
	bls.s	ns_fwind		... yes, fetch type trap
	sub.b	#fs.mdinf-sd.chenq,d5	is it fetch medium info?
	beq.s	ns_ftrap		... yes
	sub.b	#$4f-fs.mdinf,d5	is it extended info?
	beq.s	ns_xinf

	sub.b	#fs.headr-$4f,d5       is it fetch headr?
	bne.s	ns_strap		... no, send type trap
*
ns_ftrap
	bsr.s	ns_do_tr3		fetch information
	add.l	a1,a2			set length of return
	rts

ns_xinf
	bsr.s	ns_do_tr3		fetch information
	add.w	#$40,a2 		return $40 bytes
	rts
*
ns_fwind
	bsr.s	ns_do_tr3		fetch window information
	addq.l	#8,a2			return 8 bytes
	rts
*
ns_strap
	moveq	#-sd.point,d5		is it graphics
	add.b	d6,d5
	subq.b	#sd.gcur-sd.point,d5	in range point to gcur?
	bhi.s	ns_do_tr3		... no
*
	lea	$1c0(a1),a3		copy
	moveq	#7,d5			eight long words
ns_copy
	move.l	(a1)+,(a3)+
	dbra	d5,ns_copy
*
	lea	$1c0-$20(a1),a1 	and move a1 to leave room for graphics RI
*
* do a trap #3 and set d0, d1, da1 return
*
ns_do_tr3
	moveq	#5,d3			wait for it (a bit)
	move.l	a1,-(sp)
	trap	#3
	move.l	d0,(a2)+		set error return
	move.l	d1,(a2)+		set d1
	sub.l	(sp)+,a1		set a1 difference
	move.l	a1,(a2)+
	rts
	end
