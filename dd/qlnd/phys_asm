* Network physical layer - Sinclair standard +	V2.02	 1985	Tony Tebby  QJUMP
*
* 2017-03-28  2.01  Removed reference to nd_break (MK)
* 2018-11-25  2.02  Fixed crash in nd_setup when network is not available (MK)

	section nd
*
	xdef	nd_send 		send a packet
	xdef	nd_send0		send a packet (server protocol)
	xdef	nd_read 		read a packet
	xdef	nd_read0		read a packet (server protocol)
*
	xdef	nd_sbuf 		set buffer pointers
	xdef	nd_srpnt		set buffer running pointer
	xdef	nd_nextb		set next block pointers
*
	xdef	nd_setup		setup pointers
	xdef	nd_ex_ok		normal exit
*
	xdef	ndr_headr		read header
	xdef	ndr_bytes		read bytes
*
*	xref	nd_break		read break keys
*
	include dev8_dd_qlnd_keys
*
*	d6    p copy of tmode for pc
*	d7    p direct input bit number
*	a0 c  p channel definition / $20(a0) header / $28(a0) data block
*
nd_sbuf
	bsr.s	nd_srpnt		get pointer to data block
	move.l	a1,nd_epnt(a0)		set end pointer
	rts
nd_srpnt
	lea	nd_data(a0),a1		get pointer to data block
	move.l	a1,nd_rpnt(a0)		set running pointer
	rts
	page
*
* Network read packet
*
nd_read0
	clr.w	nd_blkl(a0)		clear block number
nd_read
	bsr.l	nd_setup		setup registers etc.
	bsr.s	nd_sbuf 		set buffer pointers
*
* look for a gap at least 200us long
*
	moveq	#49,d0			gap length 50*4us = 200us
ndr_wgap
	btst	d7,(a5) 	12	check for activity
	dbeq	d1,ndr_wgap	18    
	bne.s	nd_e2_nc	12
*
* Gap found, look for scout for 20ms (50us active)
*
	move.w	#4999,d0		5000*30 = 150000 cycles 20ms
ndr_scout
	btst	d7,(a5) 	12	scout yet?
	dbne	d0,ndr_scout	18
	beq.s	nd_e3_nc		... no
*
	moveq	#9,d0			10*30= 300 cycles 40us
ndr_sctst
	btst	d7,(a5) 	12	still scout?
	dbeq	d0,ndr_sctst	18	
nd_e3_nc
	beq.s	nd_e1_nc		not a scout
*
	move.w	#197,d0 		198*18=3564 cycles  475us (+50us=525)
	dbra	d0,*		18	wait until end of scout
*
* Scout found and skipped, read header
*
	bsr.l	ndr_headr		read header
nd_e2_nc
	bne.s	nd_e1_nc		... oops
	move.b	(a1)+,d0		get destination from packet header
	cmp.b	nd_self(a0),d0		is it for me?
	bne.s	nd_e1_nc		... no
	move.b	(a1)+,d1		get source from packet header
	cmp.b	nd_dest(a0),d1		is from my preferred source?
	beq.s	ndr_for_me		... yes
	cmp.b	nd_dest(a0),d0		will I accept an offer from anybody?
	bne.s	nd_e1_nc		... no
	move.b	d1,nd_dest(a0)		... yes, from now on I talk to you only!!
*
ndr_for_me
	move.w	(a1)+,d5		get block/byte (or checksum)
	sub.w	nd_blkl(a0),d5		and compare against required
*
	move.l	(a1),nd_type(a0)	set type, number of bytes and checksum
	moveq	#0,d4			set number of bytes to read
	move.b	nd_nbyt(a0),d4
	addq.b	#-nf.sblk,(a1)		is it send large block?
	bne.s	ndr_ackh		... no
	lsl.w	#2,d4			... yes, get 4* as many
*
ndr_ackh
	bsr.l	nds_ack 		acknowledge header
	move.w	d4,d2			and set number of bytes to read
	bsr.l	ndr_bytes
	bne.s	nd_bad_block		... oops
	cmp.b	nd_dchk(a0),d4		was checksum ok?
	bne.s	nd_bad_block
	tst.b	nd_dest(a0)		was it file server transaction?
	blt.s	ndr_wchk		... yes
	tst.b	nd_self(a0)
	bge.s	ndr_ack 		... no, acknowledge
ndr_wchk
	cmp.w	d5,d4			... yes, check whole checksum
	bne.s	nd_bad_block		... ... oops
	clr.w	d5
ndr_ack
	bsr.l	nds_ack 		send acknowledge
*
	tst.w	d5			was this the right block?
	bne.s	nd_e1_nc		... no, leave eof flag
	move.l	a1,nd_epnt(a0)		... yes, set end pointer
	bsr.s	nd_nextb		move to next block
	bra.l	nd_ex_ok		done
*
nd_bad_block
	sf	nd_type(a0)		clear type
nd_e1_nc
	bra.l	nd_ex_nc		not complete
*
* move to next block
*
nd_nextb
	addq.b	#1,nd_blkl(a0)		move block low on one
	bcc.s	nd_nx_rts
	addq.b	#1,nd_blkh(a0)		carry, so move block high on
nd_nx_rts
	rts
	page
*
* Network send packet
*
* Create checksum
*
nd_csum
	moveq	#0,d4			clear checksum
	moveq	#0,d2
nd_csloop
	move.b	(a1)+,d2	12	get byte
	add.w	d2,d4		08	add byte
	subq.w	#1,d1		08	next byte
	bne.s	nd_csloop	18	46 cycles 6.13us  * (255+7) max = 1.6ms
	rts
*
* Send a packet - if net busy, give up
*
nd_send0
nd_send
	bsr.l	nd_setup		set net mode and registers
*
* Now do all the checksum rubbish so we can go straight on from the scout
*
	moveq	#0,d5			length of data block
	move.b	nd_nbyt(a0),d5
	cmp.b	#nf.sblk,nd_type(a0)	is it remote file block?
	bne.s	nds_cdata		... no, create data checksum
	lsl.w	#2,d5			... yes, 4* as many bytes
nds_cdata
	move.w	d5,d1			create checksum
	lea	nd_data(a0),a1		of data
	bsr.s	nd_csum
	move.b	d4,nd_dchk(a0)		in data checksum loc
	tst.b	nd_self(a0)		is it server
	blt.s	nds_wchk		... yes
	tst.b	nd_dest(a0)
	bge.s	nds_chead		... no, just create header checksum
nds_wchk
	move.w	d4,nd_blkl(a0)		... yes, set word checksum in block number
nds_chead
	moveq	#7,d1			create checksum
	lea	nd_hedr(a0),a1		of header
	bsr.s	nd_csum
	move.b	d4,(a1) 		in header checksum loc at end of header
*
	subq.l	#7,a1			backspace to start of header
*
* Now send a scout, checking if anyone else is
*
	moveq	#-1,d3			preset 1s
	move.w	nd_dest(a0),d3		get destination and self
	lsl.w	#5,d3			11 significant bits
	asr.l	#3,d3			two zeros
	swap	d3			preceded by two ones
	asr.l	#6,d3			11111111.1dddd00ss.ssss0011.11111111
	moveq	#15,d1			send 16 bits
*
* Wait at least 3 ms for any other scout
*
	moveq	#err.nc,d0		preset not complete
	move.w	#749,d2 		750 * 4us = 3ms
nds_wscout
	btst	d7,(a5) 	12	check if net active
	dbne	d2,nds_wscout	18	30 cycles 4us
	bne.s	nd_unset		... net active return +ve
*
nds_sscout
	move.b	d6,d3		08	get tmode (shifted)
	lsr.l	#1,d3		14	get next bit into tmode
	move.b	d3,(a4) 	12	send the bit
	blt.s	nds_sact	18 12	... net is active
*
	moveq	#3,d0
nds_tact
	btst	d7,(a5) 		test for inactive
	dbne	d0,nds_tact	  120	30 * 4 = 120 cycles
	bne.s	nd_ex_nc	   12	(with contention = 144 cycles)
	bra.s	nds_nxts	   18
*
nds_sact
	moveq	#7,d0
	dbra	d0,*	       144	18 * 8 = 144 cycles
*
nds_nxts
	dbra	d1,nds_sscout	18	214 cycles = 28.5us loop
*					contention will give up to 240 <= 32us
*					there are 15 bits before net steady active
*					15 bits is 428us (good QLs)
*						up to 480us (bad QLs)
*
	tst.b	nd_dest(a0)		was it broadcast?
	bne.s	nds_header		... no, send header
	move.w	#2082,d0		... yes, wait 2083*18=37494 cycles  5ms
	dbra	d0,*			    with net active
*
nds_header
	moveq	#8,d2			send eight bytes from header
	bsr.l	nds_half		(half a packets worth)
	bne.s	nd_ex_nc		... oops
*
	move.w	d5,d2			length of data block
	bsr.s	nds_half		(the other half of the packet)
	bne.s	nd_ex_nc		... oops
*
	tst.b	nd_dest(a0)		was it broadcast?
	bne.s	nds_done		... no, done
*
	moveq	#124,d0 		wait 125*4us = 500us for broadcast ack
nds_wback
	btst	d7,(a5) 	12	test if network active
	dbne	d0,nds_wback	18
	beq.s	nd_ex_nc		... no acknowledge
*
nds_wbend
	btst	d7,(a5) 	12	test if network inactive
	bne.s	nds_wbend	18	... and carry on until it is
*
	moveq	#124,d0 		wait 125*4us = 500us for broadcast nack
nds_wbnack
	btst	d7,(a5) 	12	test if network active
	dbne	d0,nds_wbnack	18
	bne.s	nd_ex_nc		... no nack
nds_done
	bsr.l	nd_nextb
	page
*
* Exits
*
nd_ex_ok
	moveq	#0,d0			OK
	bra.s	nd_unset
nd_ex_nc
	moveq	#err.nc,d0		not complete - quite normal
nd_unset
	move.b	sv_tmode(a6),(a4)	reset transmit mode
nd_exit
	movem.l (sp)+,d1-d7/a1/a4/a5	pop regs
	and.w	#$f8ff,sr		reenable interrupts
	tst.l	d0
	rts
nd_ex_mdv
	moveq	#err.nc,d0
	bra.s	nd_exit

*
* setup d6 shifted transmit mode
*	d7 netin bit (0)
*	a4 transmit mode register address
*	a5 network read address
*
nd_setup
	move.l	(sp)+,d0		pop return address
	movem.l d1-d7/a1/a4/a5,-(sp)	save regs
	move.l	d0,a1
*
	moveq	#pc.mdvmd,d0		check the microdrive mode bit
	move.b	sv_tmode(a6),d6
	and.b	d6,d0
	bne.s	nd_ex_mdv		can't do ought about mdv
*
	lea	pc_tctrl,a4		set transmit control reg
	lea	pc_netrd-pc_tctrl(a4),a5 ... and network read reg
*
	or.w	#$0700,sr		disable interrupts
	move.w	sv_timo(a6),d0		get RS232 timeout
	ble.s	nd_netset		... none
	mulu	#5357,d0		5357*3.73333 = 20ms / timeout
wait_loop	
	subq.l	#1,d0		10
	bne.s	wait_loop	18	28 cycles  3.73333us
*
nd_netset
	clr.w	sv_timo(a6)		clear timeout
	or.b	#pc.netmd,d6		set the network mode bits
	move.b	d6,(a4) 		and set the pc (do not bother about sv)
*
	lsl.b	#1,d6			get rid of direct output bit
	moveq	#pc..diri,d7		set direct input bit (direct out is msb)
	jmp	(a1)
	page
*
* Send procedures
*
* Send half a packet (byte count d2, address a1)
*
nds_half
	bsr.s	nds_bytes		send bytes
	tst.b	nd_dest(a0)		is it b/cast?
	beq.s	nds_rts 		... yes, done
	moveq	#1,d2			receive one byte reply
	lea	nd_data-1(a0),a1	put into header checksum
	bsr.s	ndr_1byte		
	subq.b	#1,d4			result should be 1
	rts
*
* Send acknowledge
*
nds_ack
	moveq	#1,d2			send one byte
	moveq	#$ffffff80,d3
	rol.l	#1,d3			value one ($ffffff01)
	bra.s	nds_1byte
*
* Send bytes
*
nds_bytes
	moveq	#49,d0			pause
	dbra	d0,*			18*50 = 900 cycles, 120us
*
	moveq	#-1,d3			preset send byte to 1s
*
nds_byloop
	move.b	(a1)+,d3		get byte
nds_1byte
	lsl.w	#1,d3			set zero start bit
	rol.l	#2,d3			preceded by two stop bits
	lsl.l	#8,d3			in msbyte (and beyond) of word
	moveq	#13,d1			send 13 bits (4 stop bits)
*
nds_bit_loop
	move.b	d6,d3		08	get shifted tmode
	asr.l	#1,d3		14	get next bit
	move.b	d3,(a4) 	13	... set tmode
	ror.w	#6,d0		22	wait
	subq.w	#1,d1		08	next bit
	bgt.s	nds_bit_loop	18	83 cycles (nearest to 84)
*
	subq.w	#1,d2			next byte
	bne.s	nds_byloop
*
	move.b	d6,d3			get mode
	lsr.b	#1,d3			inactive
	move.b	d3,(a4) 		and set it
nds_rts
	rts
	page
*
* Read procedures
*
* Read header
*
ndr_headr
	moveq	#8,d2			read eight bytes
	bsr.s	ndr_bytes
	bne.s	ndrh_rts		... oops
	sub.b	-(a1),d4		take last byte away from checksum
	sub.b	(a1),d4 		is it ok?
	subq.l	#7,a1			and backspace pointer
ndrh_rts
	rts
*
* Read bytes (byte count d2, checksum d4, address a1)
*
ndr_bytes
	move.l	nd_rpnt(a0),a1		read multiple bytes into data buffer
ndr_1byte
	move.w	#624,d0 		set up timeout
	moveq	#0,d4			and checksum
*
ndr_active
ndr_byloop
	btst	d7,(a5) 		wait for net to go active
	dbne	d0,ndr_active		30*625 = 18750 cycles 2.5ms
	beq.s	ndr_nc			... oops, loss of comms
*
	moveq	#-1,d0			set start bit timeout
*
ndr_start
	btst	d7,(a5) 	   12	wait for start bit
	dbeq	d0,ndr_start	26 18	up to 65536*4us 260ms
	bne.s	ndr_nc		12	... oops, loss of comms 
*
	moveq	#8,d1		08	eight bits per byte
	moveq	#14,d0		08	set pause to center bit reads at
*					84+42 cycles nom less 8 for read/write
*					cycle difference of 2 per bit; 118 cycles
*					required (104 to 133) (issue 6)
*					84+42 cycles nom less 4 for read/write
*					cycle difference of 1 per bit; 122 cycles
*					required (104 to 139) (issue 5)
*
ndr_bit_loop
	ror.w	d0,d7		38 26	pause
	move.b	(a5),d3 	   12	get bit  (104 to 139 av 122.5 from start)
	ror.w	#1,d3		   12	into msbyte of word
	moveq	#8,d0		   08	set pause
	subq.w	#1,d1		   08
	bgt.s	ndr_bit_loop	   18	84 cycle loop
*
	lsr.w	#8,d3			get byte into low end
	move.b	d3,(a1)+		put into buffer
	add.w	d3,d4			and add to checksum
	subq.w	#1,d2			next byte
	bne.s	ndr_byloop		(d0=8)
	rts
ndr_nc
	moveq	#err.nc,d0
	rts
	end
