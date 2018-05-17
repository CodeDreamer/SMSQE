* Network physical layer - QXL (20 MHz)       1984 & 1993 Tony Tebby
*
       section nd
*
	xdef	nd_qxlc 		network access code
	xdef	nd_qxle

	xdef	nd_send 		send a packet
	xdef	nd_send0		send a packet (server protocol)
	xdef	nd_read 		read a packet
	xdef	nd_read0		read a packet (server protocol)
	xdef	nd_bcast		read a broadcast
*
	xref	nd_break		read break keys
*
	include dev8_dd_nd_keys
	include dev8_mac_assert
	include dev8_smsq_qxl_keys
	include dev8_smsq_smsq_base_keys
	include dev8_keys_sys

; The lines ;----------- in this code represent line (16 byte) boundaries
; The code is copied to address qxl_netc ($1f000) to ensure line alignment
; The first two enties are to enable the scout and send byte timing to be
; adjusted.
; The timer constants are in the linkage block (a3)

nd_qxlc
	bra.s	ndi_sscout     ; remember to set d1,d3,a3,a4,a5
	bra.s	ndi_sbytes     ; remember to set d2,a1,a3,a4,a5
	nop
	nop
	nop
	nop

***********************************
* wait for active / inactive timers - 28.2 cycles / 1.41 us
*
ndt_wact equ	*-nd_qxlc+qxl_netc
	move.w	(a3,d0.w),d0		   ; set up timer
;-------------------------------------------
ndt_walp
	tst.b	(a2)			   ; wait for active
	dbne	d0,ndt_walp
	rts

	dc.w	0,0

ndt_wina equ	*-nd_qxlc+qxl_netc
	move.w	(a3,d0.w),d0
;-------------------------------------------
ndt_wilp
	tst.b	(a2)			   ; wait for inactive
	dbeq	d0,ndt_wilp
	rts

	dc.w	0,0

ndt_wait equ	*-nd_qxlc+qxl_netc
	move.w	(a3,d0.w),d0
;-------------------------------------------
ndt_wtlp
	tst.b	(a2)			   ; wait, ignoring active/inactive
	dbra	d0,ndt_wtlp
	rts

******************************************
* Send Scout: d1 bits in d3 msbytes
*
nd_sscout equ	 *-nd_qxlc+qxl_netc

ndi_sscout
	move.w	ndt_tsct(a3),d2 	timer constant
	lsl.l	#8,d3			next bit is lsb
	not.l	d3
nds_sscbit
;-----------------------------------------
	move.w	d2,d0
	clr.w	d3
	lsr.l	#1,d3
	dc.w	$50f5,$3200   ; st (a5,d3.w*2)
	nop
	tst.w	d3
	bpl.s	nds_sact		  ... net is active
;-----------------------------------------
nds_tact
	tst.b	(a2)
	dbne	d0,nds_tact		 31us in loop
	bra.s	nds_nxts

	dc.w	0,0,0,0
;-----------------------------------------
nds_sact
	tst.b	(a2)
	dbra	d0,nds_sact		 31us in loop
	moveq	#0,d0
*
nds_nxts
	dbne	d1,nds_sscbit		 32us loop gives scout 480 us +
	rts

	dc.w	0
;----------------------------------------
	dc.w	0,0,0
********************************
* Send d2 bytes (a1)
*
nd_sbytes equ	 *-nd_qxlc+qxl_netc
ndi_sbytes
	moveq	#ndt_paus,d0		pause 140 us
	jsr	ndt_wait

	moveq	#0,d3			preset send byte to 1s

;----------------------------------------
nds_byloop
	move.b	(a1)+,d3		get byte
	not.b	d3
	add.w	d3,d3			shift left
	addq.b	#1,d3
	lsl.w	#2,d3			preceded by two stop bits
	swap	d3			in msword
	moveq	#12,d1			send 13 bits altogether
*
nds_bit_loop
	asr.l	#1,d3			get next bit
;----------------------------------------
	dc.w	$50f5,$3200   ; st (a5,d3.w*2)
	nop

	move.w	ndt_send(a3),d0
	subq.w	#4,d0	      ; go down in counts of 4
	bge.s	*-2
	roxr.w	#2,d0	      ; v clear   ls bits in carry and sign
;----------------------------------------
	bcs.s	nds_eb23
	bpl.s	nds_eb0
	dc.w	$59fc	 ; trapvs
	bra.s	nds_eb1

nds_eb23
	bpl.s	nds_eb2
	bra.s	nds_eb3
	moveq	#0,d0
	moveq	#0,d0
;----------------------------------------
nds_eb3
	bra.s	nds_ebit
	dc.w	$59fc	; trapvs
nds_eb0
	dc.w	$59fc	; trapvs
nds_eb1
nds_eb2
	dc.w	$59fc	; trapvs
nds_ebit
	clr.w	d3
	dbra	d1,nds_bit_loop 	hopefully about 224 cycles

	subq.w	#1,d2			next byte
;----------------------------------------
	bne.s	nds_byloop
*
	st	(a4)			inactive
	rts

******************************************************
* Read bytes (byte count d2, checksum d4, address a1)
*
nd_rbytes equ	 *-nd_qxlc+qxl_netc
	move.l	nd_rpnt(a0),a1		read multiple bytes into data buffer
nd_r1byte equ	 *-nd_qxlc+qxl_netc
	moveq	#0,d4			checksum
	moveq	#ndt_rtmo,d0		initial timeout

ndr_byloop
	moveq	#8,d1			eight bits per byte (plus start bit)
;----------------------------------------
	jsr	ndt_wact		wait for net to go active
	beq.s	ndr_nc			... oops, loss of comms

*
ndr_start
	tst.b	(a2)			wait for start bit
	dbeq	d0,ndr_start
	bne.s	ndr_nc			... oops, loss of comms
;----------------------------------------
	move.w	ndt_rdly(a3),d0 	set up start bit timer
	bra.s	ndr_bwait

ndr_bit_loop
	move.w	ndt_rbit(a3),d0
ndr_bwait
	subq.w	#4,d0	      ; go down in counts of 4
	bge.s	*-2
	roxr.w	#2,d0	      ; v clear   ls bits in carry and sign
;----------------------------------------
	bcs.s	ndr_eb23
	bpl.s	ndr_eb0
	dc.w	$59fc	 ; trapvs
	bra.s	ndr_eb1

ndr_eb23
	bpl.s	ndr_eb2
	bra.s	ndr_eb3
	moveq	#0,d0
	moveq	#0,d0
;----------------------------------------
ndr_eb3
	bra.s	ndr_rbit
	dc.w	$59fc	; trapvs
ndr_eb0
	dc.w	$59fc	; trapvs
ndr_eb1
ndr_eb2
	dc.w	$59fc	; trapvs
;----------------------------------------
ndr_rbit
	move.b	(a2),d3 		next data bit
	ror.w	#1,d3
	dbra	d1,ndr_bit_loop

	lsr.w	#8,d3			get byte into low end
	move.b	d3,(a1)+		put into buffer
	add.w	d3,d4			and add to checksum
	moveq	#ndt_rbto,d0		subsequent byte timeout = about 7 bits
	subq.w	#1,d2			next byte
	bne.s	ndr_byloop
	rts
ndr_nc
	moveq	#err.nc,d0
	rts

nd_qxle
*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

*******************************************
* Set Buffer Pointers
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
*******************************************
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
	moveq	#ndt_wgap,d0		test 200us
	jsr	ndt_wact
	bne.s	nd_e2_nc
*
* Gap found, look for scout for 20ms (50us active),
*
	and.w	#$f8ff,sr		reenable interrupts

	moveq	#ndt_lsct,d0		wait 20ms
	jsr	ndt_wact
	beq.s	nd_e3_nc		... no

	or.w	#$0700,sr		disable interrupts

	moveq	#ndt_csct,d0		test 40us
	jsr	ndt_wina
nd_e3_nc
	beq.s	nd_e1_nc		not a scout
*
	moveq	#ndt_esct,d0
	jsr	ndt_wait		wait 490us until end of scout
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
	jsr	nd_rbytes
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
******************************************************
* Network send packet
*
* Create checksum
*
nd_csum
	moveq	#0,d4			clear checksum
	moveq	#0,d2
nd_csloop
	move.b	(a1)+,d2		get byte
	add.w	d2,d4			add byte
	subq.w	#1,d1			next byte
	bne.s	nd_csloop
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
	moveq	#ndt_wsct,d0		3ms
	jsr	ndt_wact		... wait for active
	bne.s	nd_unset		... net active return +ve
*
	jsr	nd_sscout
	bne.s	nd_ex_nc		; busy

	tst.b	nd_dest(a0)		was it broadcast?
	bne.s	nds_header		... no, send header
	moveq	#ndt_xsct,d0		... yes, wait = 5ms
	jsr	ndt_wait		with net active
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
	moveq	#ndt_back,d0		wait 500us
	jsr	ndt_wact		looking for active
	beq.s	nd_ex_nc		... no acknowledge

	moveq	#ndt_bace,d0		for 20ms
	jsr	ndt_wina		look for inactive
*
	moveq	#ndt_back,d0		wait 500us
	jsr	ndt_wact		looking for active
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
nd_exit
	move.l	d0,d1
	move.w	(sp)+,sys_castt(a6)
	bne.s	ndex_rest	       ; no cache or suppressed
	jsr	sms.cenab	       ; enable both
	bra.s	ndex_rest

ndex_ninst
	jsr	sms.cenab	       ; enable both
	jsr	sms.cdisi	       ; disable instruction

ndex_rest
	move.l	d1,d0
	movem.l (sp)+,d1-d7/a1/a2/a4/a5    pop regs
	and.w	#$f8ff,sr		reenable interrupts
	rts
*
* setup network, save regs, set cache etc
*
*	a2  r	neti
*	a4  r	netl
*	a5  r	neth
*
nd_setup
	move.l	(sp)+,d0		pop return address
	movem.l d1-d7/a1/a2/a4/a5,-(sp)    save regs

	move.l	d0,a1			... return is jmp (a1)
	lea	qxl_neti,a2
	lea	qxl_netl,a4
	lea	qxl_neth,a5		net low is a5 -$8000-$8000
	or.w	#$0700,sr		no interrupts
	move.w	sys_castt(a6),-(sp)	save cache status
	st	sys_castt(a6)		kill cache
	jsr	sms.cdisb		disable both caches
	jmp	(a1)
	page
*
* Send procedures
*
* Send half a packet (byte count d2, address a1)
*
nds_half
	jsr	nd_sbytes		send bytes
	tst.b	nd_dest(a0)		is it b/cast?
	beq.s	nds_rts 		... yes, done
	moveq	#1,d2			receive one byte reply
	lea	nd_data-1(a0),a1	put into header checksum
	jsr	nd_r1byte
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
	jsr	nd_sbytes
	addq.w	#2,sp
	move.l	(sp)+,a1
	rts
	page
*
* Read procedures
*
* Read header
*
ndr_headr
	moveq	#8,d2			read eight bytes
	jsr	nd_rbytes
	bne.s	ndrh_rts		... oops
	sub.b	-(a1),d4		take last byte away from checksum
	sub.b	(a1),d4 		is it ok?
	subq.l	#7,a1			and backspace pointer
ndrh_rts
	rts
*	d6 c  p size of buffer extension
*	d7    p direct input bit number
*	a0 c  p channel definition / $20(a0) header / $28(a0) data block
*
* Network Broadcast
*
nd_bcast
	bsr.l	nd_sbuf 		set buffer pointers
	add.l	d6,nd_epnt(a0)		and extend the buffer
	bsr.s	nd_setup		setup registers etc.
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
	jsr	ndt_wina		wait for inactive
	beq.s	ndb_start		... yes, restart
*
	bsr.s	ndr_headr		read bytes
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
	jsr	nd_rbytes
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
	jsr	ndt_wact
	beq.s	ndb_start		network is idle - restart
*
ndb_nact
	moveq	#ndt_back,d0		check for 500us active
	jsr	ndt_wina
	beq.s	ndb_nack		not BACK, try again

	move.w	ndt_bnak(a3),d0 	wait 200 us
	jsr	ndt_wait
*
	bsr.s	ndb_active
	bra.l	ndb_start		and restart
*
ndb_active
	st	(a5)			set active
	moveq	#ndt_xack,d0		5000us active
	jsr	ndt_wait

	st	(a4)			set inactive
	rts
	end
