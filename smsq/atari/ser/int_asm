; SER interrupt routines  V2.01     1989  Tony Tebby	QJUMP

	section ser

	xdef	mfp_intr
	xdef	mfp_intt
	xdef	mfp_intc
	xdef	mfp2_intr
	xdef	mfp2_intt

	xdef	scca_intr
	xdef	scca_intt
	xdef	scca_intc
	xdef	scca_ints
	xdef	sccb_intr
	xdef	sccb_intt
	xdef	sccb_intc
	xdef	sccb_ints

	xdef	mfp_oact
	xdef	mfp2_oact
	xdef	sccb_oact
	xdef	scca_oact

	xdef	ser_oact

	xref	ser_rxdi

	xref	iob_gbps
	xref	iob_eof
	xref	iob_pbyt
	xref	iob_room

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_scc'
	include 'dev8_keys_k'
	include 'dev8_keys_68000'
	include 'dev8_keys_buf'
	include 'dev8_keys_par'
	include 'dev8_mac_assert'

reg.int reg	d0/d1/d7	       ; a2/a3 already saved
reglist reg	d0/d1/d7/a2/a3

;+++
; SCC channel A Special condition servers
;---
scca_ints
;	 move.b  #sccc.rhiu,scc_ctra	 ; clear in service
;	 rte
;+++
; SCC channel A receive byte.
; If receiving the byte reduces the room in the queue below the minimum,
; the handshakes are negated.
;---
scca_intr
	movem.l reg.int,-(sp)
	move.b	#sccc.rhiu,scc_ctra
; it seems that the receive status bit is not set on at least some 8531s
;	 btst	 #sccs..char,scc_ctra	  ; ready?
;	 beq.s	 scca_nop		  ; ... no
	move.b	scc_data,d1
	bra.s	ser_intr

scca_nop
	movem.l (sp)+,reglist		 ; restore
	rte

;+++
; SCC channel B Special condition servers
;---
sccb_ints
;+++
; SCC channel B receive byte.
; If receiving the byte reduces the room in the queue below the minimum,
; the handshakes are negated.
;---
sccb_intr
	movem.l reg.int,-(sp)
	move.b	#sccc.rhiu,scc_ctrb	 ;
	btst	#sccs..char,scc_ctrb	 ; ready?
	beq.s	sccb_nop		 ; ... no
	move.b	scc_datb,d1
	bra.s	ser_intr

sccb_nop
	movem.l (sp)+,reglist		 ; restore
	rts

;+++
; MFP2 receive byte.
; If receiving the byte reduces the room in the queue below the minimum,
; the handshakes are negated.
;---
mfp2_intr
	movem.l reg.int,-(sp)
	bclr	#mfp..rxi,mfp_isa+at_mfp2  ; clear in service
	btst	#mfp..rxr,mfp_rstt+at_mfp2 ; ready?
	beq.l	sir_exit		 ; ... no
	move.b	mfp_sdat+at_mfp2,d1	 ; data byte
	bra.s	ser_intr

;+++
; MFP receive byte.
; If receiving the byte reduces the room in the queue below the minimum,
; the handshakes are negated.
;---
mfp_intr
	movem.l reg.int,-(sp)
	bclr	#mfp..rxi,mfp_isa+at_mfp  ; clear in service
	btst	#mfp..rxr,mfp_rstt+at_mfp ; ready?
	beq.l	sir_exit		 ; ... no
	move.b	mfp_sdat+at_mfp,d1	 ; data byte
ser_intr
	tst.b	prd_hand(a3)		 ; xoff?
	ble.s	sir_ibuf		 ; ... no
	assert	k.xon-$11,k.xoff-$13,0
	moveq	#$7d,d0
	and.b	d1,d0
	cmp.b	#k.xon,d0		 ; xon or xoff?
	bne.s	sir_ibuf		 ; ... no
	btst	#1,d1			 ; on or off
	sne	prd_xoff(a3)		 ; ... yes
	bne.s	sir_exit		 ; ... off, done
	bsr.l	mfp_oact		 ; ... on, ensure transmit active
	bra.s	sir_exit

sir_ibuf
	move.l	prd_ibuf(a3),d0 	 ; input queue block
	ble.s	sir_exit		 ; ... none!!!!
	move.l	d0,a2
	move.b	prb_fz(a2),d0		 ; <FF> or CTRL Z
	subq.b	#1,d0
	ble.s	sir_ckcr		 ; ... not CTRL Z
	move.b	d1,d0
	tst.b	prb_prty(a2)		 ; parity?
	beq.s	sir_ckcz		 ; ... no
	bclr	#7,d0			 ; ... ignore it
sir_ckcz
	cmp.b	#26,d0			 ; is it CTRL Z?
	bne.s	sir_ckcr
	jsr	iob_eof 		 ; mark end of file
	jsr	ser_rxdi		 ; disable receive
	st	prd_ibuf(a3)		 ; no input buffer pointer now
	bra.s	sir_exit

sir_ckcr
	move.b	prb_cr(a2),d0		 ; cr to lf?
	beq.s	sir_pbyt		 ; ... no
	cmp.b	d0,d1			 ; cr?
	bne.s	sir_pbyt		 ; ... no
	move.b	prb_lf(a2),d1		 ; ... yes
	beq.s	sir_exit		 ; cr ignored

sir_pbyt
	jsr	iob_pbyt		 ; put byte in buffer
	jsr	iob_room
	cmp.l	prd_room(a3),d0 	 ; enough?
	bgt.s	sir_exit		 ; ... plenty
	tst.b	prd_hand(a3)		 ; handshake?
	bgt.s	sir_xoff		 ; XON/XOFF
	jsr	ser_rxdi		 ; negate handshakes
	bra.s	sir_exit
sir_xoff
	move.b	#k.xoff,prd_xonf(a3)	 ; xoff to be sent
	bsr.s	mfp_oact
	sf	prd_iact(a3)		 ; input not active

sir_exit
	movem.l (sp)+,reglist		 ; restore
	rte				 ; and return

;+++
; SER output activate.
; This will do nothing if the xmit buffer is full. But if the xmit buffer is
; empty and the CTS is asserted (or the handshake flag is not set)
; empty then it will put a byte directly, and there will be an interrupt when
; byte is transmitted.
;
;	Status and D0 as called
;---
ser_oact
	tst.b	prd_hwt(a3)		 ; hardware type
	beq.s	mfp_oact
	bgt.s	mfp2_oact
	cmp.w	#scc_ctra,prd_hwb(a3)	 ; channel A?
	bne.s	sccb_oact		 ; ... no

scca_oact
	btst	#sccs..temp,scc_ctra	 ; buffer empty?
	beq.s	soa_exit		 ; ... no, done
	movem.l reglist,-(sp)
	move.w	sr,-(sp)
	or.w	#sr.i7,sr
	bsr.l	scca_ittr
	move.w	(sp)+,sr
	movem.l (sp)+,reglist		 ; restore exit regs
	tst.l	d0
	rts

sccb_oact
	btst	#sccs..temp,scc_ctrb	 ; buffer empty?
	beq.s	soa_exit		 ; ... no, done
	movem.l reglist,-(sp)
	move.w	sr,-(sp)
	or.w	#sr.i7,sr
	bsr.l	sccb_ittr
	move.w	(sp)+,sr
	movem.l (sp)+,reglist		 ; restore exit regs
	tst.l	d0
	rts

mfp2_oact
	btst	#mfp..txr,mfp_tstt+at_mfp2 ; ready?
	beq.s	soa_exit		   ; ... no, done
	movem.l reglist,-(sp)
	move.w	sr,-(sp)
	or.w	#sr.i7,sr
	bsr.l	mfp2_ittr
	move.w	(sp)+,sr
	movem.l (sp)+,reglist		 ; restore exit regs
	tst.l	d0
	rts

mfp_oact
	btst	#mfp..txr,mfp_tstt+at_mfp ; ready?
	beq.s	soa_exit		  ; ... no, done

	movem.l reglist,-(sp)
	move.w	sr,-(sp)
	or.w	#sr.i7,sr
	bsr.l	mfp_ittr
	move.w	(sp)+,sr
	movem.l (sp)+,reglist		 ; restore exit regs

soa_exit
	tst.l	d0
	rts

;+++
; SCC channel A interrupt server (CTS)
;---
scca_intc
;+++
; SCCA interrupt server (transmit)
;---
scca_intt
	move.b	#sccc.rint,scc_ctra	 ; clear cts pending
	move.b	#sccc.rtxi,scc_ctra
	move.b	#sccc.rhiu,scc_ctra	 ; and highest in service
	movem.l reg.int,-(sp)
	bsr.s	scca_ittr
	movem.l (sp)+,reglist		 ; restore exit regs
	rte

scca_ittr
   ; xxx_oact enters here because buffer empty must be checked int disabled
	btst	#sccs..temp,scc_ctra	 ; buffer empty?
	beq.s	scca_ncts		 ; ... no

	tst.b	prd_hand(a3)		 ; hardware handshaking?
	bge.s	scca_gbyte		 ; ... no
	btst	#sccs..cts,scc_ctra	 ; clear to send?
	beq.s	scca_scts		 ; ... no, retry on CTS rising
scca_gbyte
	bsr.l	stx_gbyte
	bmi.s	scca_ncts		  ; nothing to do on cts
	move.b	d1,scc_data		  ; send data

scca_ncts
;**	   move.b  #scc_ictl,scc_ctra
;**	   move.b  #0,scc_ctra		     ; mask cts interrupt
	rts

scca_scts
;**	   move.b  #scc_ictl,scc_ctra
;**	   move.b  #scci.icts,scc_ctra	     ; set cts interrupt
	rts

;+++
; SCC channel B interrupt server (CTS)
;---
sccb_intc
;+++
; SCCB interrupt server (transmit)
;---
sccb_intt
	move.b	#sccc.rint,scc_ctrb	 ; clear cts pending
	move.b	#sccc.rtxi,scc_ctrb
	move.b	#sccc.rhiu,scc_ctrb	 ; and highest in service
	movem.l reg.int,-(sp)
	bsr.s	sccb_ittr
	movem.l (sp)+,reglist		 ; restore exit regs
	rte

sccb_ittr
   ; xxx_oact enters here because buffer empty must be checked int disabled
	btst	#sccs..temp,scc_ctrb	 ; buffer empty?
	beq.s	sccb_ncts		 ; ... no

	tst.b	prd_hand(a3)		 ; hardware handshaking?
	bge.s	sccb_gbyte		 ; ... no
	btst	#sccs..cts,scc_ctrb	 ; clear to send?
	beq.s	sccb_scts		 ; ... no, retry on CTS rising
sccb_gbyte
	bsr.l	stx_gbyte
	bmi.s	sccb_ncts		  ; nothing to do on cts
	move.b	d1,scc_datb		  ; send data

sccb_ncts
;**	   move.b  #scc_ictl,scc_ctrb
;**	   move.b  #0,scc_ctrb		     ; mask cts interrupt
	rts

sccb_scts
;**	   move.b  #scc_ictl,scc_ctrb
;**	   move.b  #scci.icts,scc_ctrb	     ; set cts interrupt
	rts

;+++
; MFP2 interrupt server (transmit)
;---
mfp2_intt
	movem.l reg.int,-(sp)
	bclr	#mfp..txi,mfp_txs	 ; clear in service
	bsr.s	mfp2_ittr
	movem.l (sp)+,reglist		 ; restore exit regs
	rte

mfp2_ittr
   ; xxx_oact enters here because buffer empty must be checked int disabled
	btst	#mfp..txr,mfp_tstt+at_mfp2 ; ready?
	beq.s	mfp2_ncts		  ; ... no, cts not required now

	bsr.s	stx_gbyte
	bmi.s	mfp2_ncts		 ; nothing to do on cts
	move.b	d1,mfp_sdat+at_mfp2	 ; send data

mfp2_ncts
	rts

;+++
; MFP interrupt server (CTS)
;---
mfp_intc
	movem.l reg.int,-(sp)
	bclr	#mfp..csi,mfp_css	 ; clear in service
	bsr.s	mfp_ittr
	movem.l (sp)+,reglist		 ; restore exit regs
	rte
;+++
; MFP interrupt server (transmit)
;---
mfp_intt
	movem.l reg.int,-(sp)
	bclr	#mfp..txi,mfp_txs	 ; clear in service
	bsr.s	mfp_ittr
	movem.l (sp)+,reglist		 ; restore exit regs
	rte

mfp_ittr
   ; xxx_oact enters here because buffer empty must be checked int disabled
	btst	#mfp..txr,mfp_tstt+at_mfp ; ready?
	beq.s	mfp_ncts		 ; ... no, cts not required now

	tst.b	prd_hand(a3)		 ; hardware handshaking?
	bge.s	mfp_gbyte		 ; ... no
	btst	#mfp..cts,mfp_port+at_mfp ; clear to send?
	bne.s	mfp_scts		 ; ... no, retry on CTS falling
mfp_gbyte
	bsr.s	stx_gbyte
	bmi.s	mfp_ncts		 ; nothing to do on cts
	move.b	d1,mfp_sdat+at_mfp	 ; send data

mfp_ncts
	bclr	#mfp..csi,mfp_mskb+at_mfp ; mask cts interrupt
	rts

mfp_scts
	bset	#mfp..csi,mfp_mskb+at_mfp ; set cts interrupt
	rts


stx_gbyte
	move.b	prd_xonf(a3),d1 	 ; xon/xoff to be sent?
	bne.s	stxg_sbcx		 ; ... yes
	move.l	prd_obuf(a3),d0 	 ; is there a queue?
	ble.s	stxg_nop		 ; ... no
	tst.b	prd_xoff(a3)		 ; transmit xoffed?
	bne.s	stxg_nop		 ; .. yes

	move.l	d0,a2			 ; set queue address

	moveq	#0,d7
	move.b	prb_prty(a2),d7
	jsr	iob_gbps		 ; get byte with parity set
	ble.s	stxg_rts		 ; ... nothing or something
					 ; ... end of file
	subq.b	#1,d1			 ; is ff required?
	blt.s	stx_gbyte		 ; ... no, try next queue
	bgt.s	stxg_cz 		 ; ... but really it is CTRL Z
	moveq	#k.ff,d1	  
	bra.s	stxg_sbyte		 ; send ff

stxg_cz
	moveq	#26,d1			 ; send CTRL Z

stxg_sbcx
stxg_sbyte
	clr.b	prd_xonf(a3)		 ; no xon/xoff to send now / CC=Z
	rts
stxg_nop
	moveq	#-1,d0			 ; set CC=N
stxg_rts
	rts

	end
