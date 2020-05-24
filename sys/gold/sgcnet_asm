; SuperGoldCard network routines

	section sgc

	xdef	sgc_nds_bytes
	xdef	sgc_ndr_1byte
	xdef	sgc_nds_sscout

	include 'dev8_sys_gold_keys'
	include 'dev8_dd_nd_keys'
	include 'dev8_mac_assert'

; SGC network send bytes
sgc_nds_bytes
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

; SGC network receive one byte
sgc_ndr_1byte
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

; SGC network send scout
sgc_nds_sscout
	addi.l	#$1c,(sp)
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
	bra.s	nds_nxts

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
	rts

	end
