* Network broadcast
*
       section nd
*
	xdef	nd_bcast		read a broadcast
*
	xref	nd_srpnt
	xref	nd_ex_ok
	xref	nd_sbuf
	xref	nd_break		read break keys
	xref	ndr_headr
	xref	ndr_bytes
	xref	nd_setup
	xref	nd_nextb

*
	include dev8_dd_qlnd_keys
	include dev8_mac_assert

*	d6 c  p size of buffer extension / copy of tmode for pc
*	d7    p direct input bit number
*	a0 c  p channel definition / $20(a0) header / $28(a0) data block
*
* Network Broadcast
*
nd_bcast
	bsr.l	nd_sbuf 		set buffer pointers
	add.l	d6,nd_epnt(a0)		and extend the buffer
	bsr.l	nd_setup		setup registers etc.
ndb_check	  
	move.l	nd_rpnt(a0),a1		get running pointer
	cmp.l	nd_epnt(a0),a1		check for end of buffer
	bgt.s	ndb_abort
*
* Wait for continuous activity for minimum 500 us
*
ndb_start
	bsr.s	nd_break		check for break
	beq.s	ndb_wscout		not pressed, look for scout
*
ndb_abort
	move.b	#1,nd_type(a0)		... set eof
	bra.s	ndb_eof
*
ndb_wscout
	moveq	#88,d0
ndb_tsct
	btst	d7,(a5) 		gap?
	beq.s	ndb_start		... yes, restart
	dbra	d0,ndb_tsct
*
	bsr.l	ndr_headr		read bytes
	bne.s	ndb_nack		... oops
	tst.b	(a1)			was it broadcast?
	bne.s	ndb_nack		... it must have been!!!
*
	addq.l	#2,a1			look at block/byte
	move.b	(a1)+,d5
	lsl.w	#8,d5
	move.b	(a1)+,d5
	sub.w	nd_blkl(a0),d5		and compare against required
*
	move.b	(a1)+,nd_type(a0)	set type
	move.b	(a1)+,d2		number of bytes
	move.b	(a1)+,nd_dchk(a0)	... and checksum
*
	bsr.l	ndr_bytes
	bne.s	ndb_nack		... oops
	cmp.b	nd_dchk(a0),d4		was checksum OK?
	bne.s	ndb_nack
*
	moveq	#10,d0
	bsr.s	ndb_active		send acknowledgement
*
	tst.w	d5			was it the correct block?
	bne.s	ndb_eof
	bsr.l	nd_nextb		move to next block
	move.l	a1,nd_rpnt(a0)
ndb_eof
	tst.b	nd_type(a0)		was it eof?
	beq.s	ndb_check		... no, check if buffer full and continue
*
	move.l	a1,nd_epnt(a0)		... yes, set end pointer
	bsr.s	nd_srpnt		... and reset running pointer
	bra.l	nd_ex_ok
*
ndb_nack
	moveq	#35,d0
ndb_natst
	btst	d7,(a5)
	bne.s	ndb_nact
	dbra	d0,ndb_natst
ndb_stt1
	bra.s	ndb_start		network is idle - restart
*
ndb_nact
	moveq	#0,d0
ndb_nttst
	addq.l	#1,d0
	btst	d7,(a5)
	bne.s	ndb_nttst
	cmp.l	#94,d0
	blt.s	ndb_nack		not BACK, try again

	moveq	#82,d0
	dbra	d0,*
*
	moveq	#1,d0
	bsr.s	ndb_active
	bra.s	ndb_stt1		and restart
*
ndb_active
	lsl.w  #7,d0
	moveq	#-1,d3			active bit
	move.b	d6,d3			mode
	lsr.w	#1,d3			shifted
ndb_sactiv
	move.b	d3,(a4) 		set active
	dbra	d0,ndb_sactiv		2083*2.4; 6000*.83; 7812*.64 = 5ms
*
	bclr	#7,d3			set inactive
	move.b	d3,(a4) 
	rts
	end
