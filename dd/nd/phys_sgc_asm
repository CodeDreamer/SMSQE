* Network physical layer - Sinclair std+  V2.0 for gold card	 Tony Tebby
*
       section nd
*
	xdef	nd_send 		send a packet
	xdef	nd_send0		send a packet (server protocol)
	xdef	nd_read 		read a packet
	xdef	nd_read0		read a packet (server protocol)
	xdef	nd_bcast		read a broadcast

	xdef	nds_bytes		patch
	xdef	ndr_1byte		patch
	xdef	nds_sscout		patch
*
	xref	nd_break		read break keys
*
	include dev8_dd_nd_keys
	include dev8_mac_assert
*
*	d6    p copy of tmode for pc
*	d7    p direct input bit number
*	a0 c  p channel definition / $20(a0) header / $28(a0) data block
*	a3 c  p linkage block
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
* check there's a gap at least 200us long
*
	moveq	#ndt_wgap,d0	     test 26*7.7; 50*4; 66*3.06us = 200us
	bsr.l	ndt_wact
	bne.s	nd_e2_nc
*
* Gap found, look for scout for 20ms (50us active)
*
	moveq	#ndt_lsct,d0	     wait 2600*7.7; 5000*4; 6560*3.06us = 20ms
	bsr.l	ndt_wact
	beq.s	nd_e3_nc		... no
*
	moveq	#ndt_csct,d0	     test 4*7.7=30us; 10*4; 13*3.06 = 40us
	bsr.l	ndt_wina
nd_e3_nc
	beq.s	nd_e1_nc		not a scout
*
	move.w	ndt_esct(a3),d0 	198*2.4; 582*.83; 758*.64 = 475;485us
	dbra	d0,*		18    ; 10    = 2.4;0.64us until end of scout
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
	lsl.b	#3,d3			11111111.11111111.000ddddd.sssss000
	addq.b	#3,d3			11111111.11111111.000ddddd.sssss011
	lsl.w	#2,d3			11111111.11111111.0dddddss.sss01100
	lsl.l	#6,d3			11111111.110ddddd.sssss011.00000000
	moveq	#14,d1			send 15 bits 110xxxxxxxxxxxx01
*
* Wait at least 3 ms for any other scout
*
	assert	err.nc,-1      ; (dbra sets d0.w to -1)
	move.w	ndt_wsct(a3),d0 	390*7.7; 750*4; 980*3.06 = 3ms
	bsr.l	ndt_walp		... wait for active
	bne.s	nd_unset		... net active return +ve
*
	move.w	ndt_tsct(a3),d2 	timer constant

nds_sscout
	moveq	#2,d0		08    ; 04     wait loop counter
	move.b	d6,d3		08    ; 04     get tmode (shifted)
	lsr.l	#1,d3		14    ; 10     get next bit into tmode
	move.b	d3,(a4) 	13    ; 15     send the bit
	blt.s	nds_sact	18 12 ; 10 08  ... net is inactive

nds_tact
;	 ror.b	 d2,d7				 3*18; 3*82; 3*124 rot 4;38;59(2)
	move.w	d2,d7		;;;;;
nds_paus1
	subq.w	#1,d7		;;;;;
	bne.s	nds_paus1	;;;;;

	btst	d7,(a5) 			3*12;	 3*15  ) loop 48;108;150
	dbne	d0,nds_tact	  152 ;   454 3*18+8;	3*10+4 ) 6.4;9.0;9.4 us
	bra.s	nds_nxts	   18 ;    10

nds_sact
;	 ror.b	 d2,d7				 3*18; 3*82; 3*124 rot 4;38;59(2)
	move.w	d2,d7		;;;;;
nds_paus2
	subq.w	#1,d7		;;;;;
	bne.s	nds_paus2	;;;;;

	btst	d7,(a5) 			3*12;	 3*15  ) loop 48;108;150
	dbra	d0,nds_sact    152    ;454    3*18+8;	3*10+4 ) 6.4;9.0;9.4 us
	moveq	#0,d0		08    ; 04
*
nds_nxts
	dbne	d1,nds_sscout	18    ; 10
	bne.s	nd_ex_nc
*					239/243 (+21) cycles = 32 to 35us loop
*					gives scout 478 to 528us (QL)
*
*					385/389 cycles = 32us loop
*					gives scout 480 us + waits (Gold12)
*
*					511/515 cycles = 32us loop
*					gives scout 480 us + waits (Gold16)

	tst.b	nd_dest(a0)		was it broadcast?
	bne.s	nds_header		... no, send header
	move.w	ndt_xsct(a3),d0 	... yes, wait 2083*2.4 7812*.64 = 5ms
	dbra	d0,*		18    ; 10   with net active
*
nds_header
	moveq	#8,d2			send eight bytes from header
	bsr.l	nds_half		(half a packets worth)
	bne.s	nd_ex_nc		... oops
*
	move.w	d5,d2			length of data block
	bsr.l	nds_half		(the other half of the packet)
	bne.s	nd_ex_nc		... oops
*
	tst.b	nd_dest(a0)		was it broadcast?
	bne.s	nds_done		... no, done
*
	moveq	#ndt_back,d0		wait 65*7.7; 125*4; 164*3.06us = 500us
	bsr.s	ndt_wact		look for active
	beq.s	nd_ex_nc		... no acknowledge

	moveq	#ndt_bace,d0		2600*7.7; 5000*4; 6560*3.06us = 20ms
	bsr.s	ndt_wina		look for inactive
*
	moveq	#ndt_back,d0		wait 65*7.7; 125*4; 164*3.06us = 500us
	bsr.s	ndt_wact		look for active
	bne.s	nd_ex_nc		... nack
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
* Timers (D0) 62/64 cycles = 7.7/8us	  Overhead 92/170 cycles = 12/23us (QL)
* Timers (D0) 47/49 cycles = 3.92/4.08us  Overhead  48 cycles = 4us (Gold 12)
* Timers (D0) 49/51 cycles = 3.06/3.2us   Overhead  48 cycles = 3us (Gold 16)
*
ndt_wact
	move.w	(a3,d0.w),d0	26 ; 14 ; 14
ndt_walp
	ror.l	#8,d7		28 ; 24 ; 24
	btst	d7,(a5) 	12 ; 13 ; 15
	dbne	d0,ndt_walp	18 ; 10 ; 10
	rts

ndt_wina
	move.w	(a3,d0.w),d0	26 ; 14 ; 14
ndt_wilp
	ror.l	#8,d7		28 ; 24 ; 24
	btst	d7,(a5) 	12 ; 13 ; 15
	dbeq	d0,ndt_wilp	18 ; 10 ; 10
	rts
*
* setup d6 shifted transmit mode
*	d7 netin bit (0)
*	a4 transmit mode register address
*	a5 network read address
*
nd_setup
	move.l	(sp)+,d0		pop return address
	movem.l d1-d7/a1/a4/a5,-(sp)	save regs
	move.l	d0,a1			... return is jmp (a1)
*
	moveq	#pc.mdvmd,d0		check the microdrive mode bit
	move.b	sv_tmode(a6),d6
	and.b	d6,d0
	bne.s	nd_ex_mdv		 can't do ought about mdv
*
	lea	pc_tctrl,a4		set transmit control reg
	lea	pc_netrd-pc_tctrl(a4),a5 ... and network read reg
*
	or.w	#$0700,sr		disable interrupts

	cmp.l	#'SMSQ',(a6)		SMSQ?
	beq.s	timo_smsq
	move.w	sv_timo(a6),d0		get RS232 timeout
	ble.s	nd_netset		... none
	clr.w	sv_timo(a6)
	bra.s	timo
timo_smsq
	move.b	sv_timo(a6),d0
	ble.s	nd_netset		... none
	clr.b	sv_timo(a6)

timo
timo_loop
	moveq	#pc.intrf,d1
	and.b	pc_intr,d1		 ; frame interrupt?
	beq.s	timo_loop		 ; ... no
	or.b	sv_pcint(a6),d1 	 ; clear interrupt flag value
	move.b	d1,pc_intr		 ; clear interrupt bit
	subq.b	#1,d0			 ; one gone
	bge.s	timo_loop

*
nd_netset
	or.b	#pc.netmd,d6		set the network mode bits
	move.b	d6,(a4) 		and set the pc (do not bother about sv)
*
	lsl.b	#1,d6			get rid of direct output bit
	assert	pc..diri,0     ; **** code assumes d7=0
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
nds_rts
	rts
*
* Send acknowledge
*
nds_ack
	moveq	#1,d2			send one byte
	move.l	a1,-(sp)
	move.b	d2,-(sp)
	move.l	sp,a1
	bsr.s	nds_bytes
	addq.w	#2,sp
	move.l	(sp)+,a1
	rts
*
* Send bytes
*
nds_bytes
	move.w	ndt_paus(a3),d0 	pause (QL overhead = 20/40us to here)
	dbra	d0,*	      50*2.4 = 120us; 162*0.83 = 135us; 219*.64 = 140us
*
	move.w	ndt_send(a3),d0 	send loop timer
	moveq	#-1,d3			preset send byte to 1s
*
nds_byloop
	move.b	(a1)+,d3		get byte
nds_1byte
	lsl.w	#1,d3			set zero start bit
	rol.l	#2,d3			preceded by two stop bits
	lsl.l	#8,d3			in msbyte (and beyond) of word
	moveq	#12,d1			send 13 bits altogether
*
nds_bit_loop
	move.b	d6,d3		08; 04; 04  get shifted tmode
	asr.l	#1,d3		14; 10; 10  get next bit
	move.b	d3,(a4) 	13; 13; 15  ... set tmode
	move.w	d0,d7	    ;;;
	move.w	d0,d7	    ;;;
	move.w	d0,d7	    ;;;
	move.w	d0,d7	    ;;;
	move.w	d0,d7	    ;;;
nds_pause
	subq.w	#1,d7	    ;;;
	bne.s	nds_pause   ;;;
nds_ebit
	dbra	d1,nds_bit_loop 18; 10; 10  83; 131/133; 173/175 (84; 134; 179)
*					      ; 55/57 cycles with zero rot
	subq.w	#1,d2			next byte
	bne.s	nds_byloop
*
	move.b	d6,d3			get mode
	lsr.b	#1,d3			inactive
	move.b	d3,(a4) 		and set it
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
	move.w	ndt_rtmo(a3),d3     timeout 625*4; 1315*1.9; 1560*1.6 = 2.5ms
	moveq	#0,d4			checksum

ndr_byloop
	assert	ndt_rdly+2,ndt_rbit
	move.l	ndt_rdly(a3),d0 	set up timers
	swap	d0
	moveq	#7,d1			eight bits per byte
*
ndr_active
	btst	d7,(a5) 		wait for net to go active
	dbne	d3,ndr_active
	beq.s	ndr_nc			... oops, loss of comms
*
ndr_start
	btst	d7,(a5) 	   12 ;    13 ;    15  wait for start bit
	dbeq	d3,ndr_start	26 18 ; 14 10 ; 14 10
	bne.s	ndr_nc		12    ; 08    ; 08     ... oops, loss of comms
*
;	 ror.l	 d0,d7		 24    ; 96    ; 134  rot 6;44;63(0)
*				       pause to center bit reads at
*				       84+42 cycles nom less 8 for read/write
*				       cycle difference of 2 per bit; 118 cycles
*				       required (104 to 133) (issue 6)
*				       84+42 cycles nom less 4 for read/write
*				       cycle difference of 1 per bit; 122 cycles
*				       required (104 to 139) (issue 5)
*				       134+77+8 (loop can be 4 cycles ave too
*				       quick) cycles nom 219 cycles Gold12
*				       178+89 cycles nom 267 cycles Gold16
*				       46+23 cycles nom 69 4xgold
*
	move.w	d0,d7	   ;;;;
ndr_paus1		   ;;;;
	subq.w	#1,d7	   ;;;;
	bne.s	ndr_paus1  ;;;;

	swap	d0		08; 04; 04
ndr_bit_loop
;	 ror.l	 d0,d7		 22; 50; 72  rot 5;21;32(0)
	move.w	d0,d7	   ;;;;
	move.w	d0,d7	   ;;;;
	move.w	d0,d7	   ;;;;
ndr_paus2		   ;;;;
	subq.w	#1,d7	   ;;;;
	bne.s	ndr_paus2  ;;;;

	move.b	(a5),d3 	12; 13; 15  (104/139; 199/225; 247/273 + con)
	ror.w	#1,d3		12; 08; 08  into msbyte of word
;	 ror.w	 d0,d7		 20; 48; 70  rot 5;21;32
	move.w	d0,d7	   ;;;;
	move.w	d0,d7	   ;;;;
ndr_paus3		   ;;;;
	subq.w	#1,d7	   ;;;;
	bne.s	ndr_paus3  ;;;;
	dbra	d1,ndr_bit_loop 18; 10; 10  84; 129/131; 175/177 cycle loop

	lsr.w	#8,d3			get byte into low end
	move.b	d3,(a1)+		put into buffer
	add.w	d3,d4			and add to checksum
	move.w	ndt_rbto(a3),d3 	20*4; 42*1.9; 50*1.6 = 80us = 7 bits
	subq.w	#1,d2			next byte
	bne.s	ndr_byloop
	rts
ndr_nc
	moveq	#err.nc,d0
	rts

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
	bsr.l	nd_break		check for break
	beq.s	ndb_wscout		not pressed, look for scout
*
ndb_abort
	move.b	#1,nd_type(a0)		... set eof
	bra.s	ndb_eof
*
ndb_wscout
	moveq	#ndt_bsct,d0		500 us of continuous activity
	bsr.l	ndt_wina		wait for inactive
	beq.s	ndb_start		... yes, restart
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
	bsr.l	nd_srpnt		... and reset running pointer
	bra.l	nd_ex_ok
*
ndb_nack
	moveq	#ndt_wgap,d0		wait for 200us inactive
	bsr.l	ndt_wact
	beq.s	ndb_start		network is idle - restart
*
ndb_nact
	moveq	#ndt_back,d0		check for 500us active
	bsr.l	ndt_wina
	beq.s	ndb_nack		not BACK, try again

	move.w	ndt_bnak(a3),d0 	wait 83*2.4; 240*.83; 312*.64 = 200 us
	dbra	d0,*
*
	bsr.s	ndb_active
	bra.s	ndb_start		and restart
*
ndb_active
	move.w	ndt_xack(a3),d0 	5000us active
	moveq	#-1,d3			active bit
	move.b	d6,d3			mode
	lsr.w	#1,d3			shifted
	move.b	d3,(a4) 		set active

	dbra	d0,*			2083*2.4; 6000*.83; 7812*.64 = 5ms
*
	bclr	#7,d3			set inactive
	move.b	d3,(a4) 
	rts
	end
