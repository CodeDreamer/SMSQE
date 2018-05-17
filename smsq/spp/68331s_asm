; MC68331 Serial port driver	V2.10	  1999  Tony Tebby

	section spp

	xdef	s33_drdef

	xref	iob_gbps
	xref	iob_eof
	xref	iob_pbyt
	xref	iob_room

	xref.s	s33.intl
	xref.s	s33.imsk
	xref.s	s33.base	; base port number = 1 for port = SER1

	include 'dev8_keys_k'
	include 'dev8_keys_6833x'
	include 'dev8_keys_buf'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_hwt'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;
; Table defining the phyisical layer of the s33 driver
;
s33_drdef
	dc.w	s33_check-*
	dc.w	s33_vector-*
	dc.w	s33_preset-*
	dc.w	s33_init-*
	dc.w	s33_istable-*

;+++
; Vector table defining hardware specific IO routines
;---
s33_vector
	dc.w	spd_open,s33_open-*-2
	dc.w	spd_close,s33_close-*-2
	dc.w	spd_int,s33_istable-*-2
	dc.w	spd_baud,s33_baud-*-2
	dc.w	spd_iopr,s33_iopr-*-2
	dc.w	spd_oopr,s33_oopr-*-2
	dc.w	0

;+++
; Preset table defining hardware specific values
;---
s33_preset
	assert	spd_pdir,spd_hand-1
	dc.w	spd_pdir,2,$ff00      ; any parity, no handshake
	dc.w	spd_room+2,2,spd.room ; input buffer spare room
	dc.w	spd_ilen+2,2,spd.ilen ; input buffer length
	dc.w	0

;+++
; Interrupt server table (conventional priorities)
;---
s33_istable
	dc.w	s33_iserve-*,s33.intl,hwt.ser<<8  ; just one server
	dc.w	0

;+++
; Routine to check whether particular ports exist
;
;	d0 cr	port to check / 0 it does not exist
;	d0 cr	0 / highest port number = 1
;	d7    s if d0 = 0 on entry otherwise preserved
;	a4  r	hardware base
;
;	status returned as d0
;---
s33_check
	tst.w	d0
	bne.s	s33c_portn		 ; check port n
s33_portx
	moveq	#s33.base,d0		 ; port always present
	rts

s33c_portn
	sub.w	#s33.base,d0		 ; this port?
	beq.s	s33_portx		 ; ... yes
	moveq	#0,d0
	rts

;+++
; s33 port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
s33_init
	move.w	#(sci.baud+38400)/38400,sci_sccr0
	move.w	#sci.rxint,sci_sccr1	 ; set up serial port
	moveq	#0,d0
	rts

;+++
; s33 port set baud
;
;	d0  r	0
;	d1 c  p baud rate
;	d2 c  s baud rate / 300
;	d7    s
;	a3 c  p s33 port linkage block
;
;	all other registers preserved
;---
s33_baud
	move.l	#sci.baud/150,d0       ; 2xbaud const / 300
	divu	d2,d0		       ; 2xbaud counter
	addq.w	#1,d0
	lsr.w	#1,d0
	move.w	d0,sci_sccr0

	moveq	#0,d0
	rts

;+++
; s33 port receive input operation
;
;	a3 c  p s33 port linkage block
;	all other registers preserved
;	status returned according to D0
;---
s33_iopr
	tst.b	spd_iact(a3)		 ; already ready?
	bpl.s	s33_rxen		 ; ... no, must be xon / xoff
	tst.l	d0
	rts


;+++
; s33 open port
;
;	a3 c  p s33 port linkage block
;	all other registers preserved
;	status returned according to D0
;---
s33_open
	move.w	#sci.rxint,sci_sccr1	 ; enable receive interrupt
	moveq	#0,d0			 ; OK return

;+++
; s33 port receive enable. If not already enabled, it checks for room before
; enabling receive.
;
;	a3 c  p s33 port linkage block
;	all other registers preserved
;	status returned according to D0
;---
s33_rxen
	move.l	d0,-(sp)
	tst.b	spd_iact(a3)		 ; already ready?
	bne.s	crxe_exit		 ; ... yes
	move.l	spd_ibuf(a3),d0 	 ; any buffer?
	ble.s	crxe_exit		 ; ... no
	move.l	a2,-(sp)
	move.l	d0,a2
	jsr	iob_room		 ; enough room

	cmp.l	spd_room(a3),d0
	ble.s	crxe_inact		 ; ... no

	move.w	sr,-(sp)		 ; save status
	or.w	#s33.imsk,sr		 ; no interrupts

	tst.b	spd_hand(a3)		 ; xon/xoff?
	ble.s	crxe_hs 		 ; ... no

	move.b	#k.xon,spd_xonf(a3)	 ; set xon char to be sent

	bsr.s	s33_oopr		 ; activate output
;	bra.s	crxe_iact
crxe_hs
crxe_iact
	st	spd_iact(a3)		 ; receive active now

	move.w	(sp)+,sr		 ; restore status
crxe_exa2
	move.l	(sp)+,a2
crxe_exit
	move.l	(sp)+,d0
	rts

crxe_inact
	move.b	#1,spd_iact(a3) 	 ; xoffed
	bra.s	crxe_exa2

;+++
; s33 port close. If there is no input buffer, it is a nop.
;
;	d0 c  p pointer to input buffer
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
s33_close
       tst.l	d0			; input queue?
       beq.s	crxd_rts		; ... no


;+++
; s33 port receive disable.
;---
s33_rxdi
	sf	spd_iact(a3)		 ; not active now
crxd_rts
	rts

;+++
; s33 port output operation
;---
s33_oopr
	st	spd_oact(a3)		 ; activate output
	move.w	#sci.rtint,sci_sccr1	 ; enable xmit interrupt
	tst.l	d0			 ; output starts automatically
	rts

;+++
; s33 port interrupt server
;---
s33_iserve
	move.w	#1<<sci..rdrf+1<<sci..tdre,d7 ; look at ready bits
	and.w	sci_scsr,d7
	beq.l	s33i_exit		 ; ... no

	tst.b	spd_iact(a3)		 ; input active?
	beq.l	s33i_xmit
	assert	sci..rdrf,6
	tst.b	d7
	beq.l	s33i_xmit		 ; rx bit not set

; Receive byte

	st	d6
	move.b	sci_scdr,d1

	tst.b	spd_hand(a3)		 ; xoff?
	ble.s	s33i_ibuf		 ; ... no
	assert	k.xon-$11,k.xoff-$13,0
	moveq	#$7d,d0
	and.b	d1,d0
	cmp.b	#k.xon,d0		 ; xon or xoff?
	bne.s	s33i_ibuf		 ; ... no
	btst	#1,d1			 ; on or off
	sne	spd_xoff(a3)		 ; ... yes
	bne.s	s33i_xmit		 ; ... off, done
	bsr.s	s33_oopr		 ; ... on, ensure transmit active
	bra.s	s33i_xmit

s33i_ibuf
	move.l	spd_ibuf(a3),d0 	 ; input queue block
	ble.s	s33i_xmit		 ; ... none!!!!
	move.l	d0,a2
	move.b	spb_fz(a2),d0		 ; <FF> or CTRL Z
	subq.b	#1,d0
	ble.s	s33i_ckcr		 ; ... not CTRL Z
	move.b	d1,d0
	tst.b	spb_prty(a2)		 ; parity?
	beq.s	s33i_ckcz		 ; ... no
	bclr	#7,d0			 ; ... ignore it
s33i_ckcz
	cmp.b	#26,d0			 ; is it CTRL Z?
	bne.s	s33i_ckcr
	jsr	iob_eof 		 ; mark end of file
	jsr	s33_rxdi		 ; disable receive
	st	spd_ibuf(a3)		 ; no input buffer pointer now
	bra.s	s33i_xmit

s33i_ckcr
	move.b	spb_cr(a2),d0		 ; cr to lf?
	beq.s	s33i_pbyt		 ; ... no
	cmp.b	d0,d1			 ; cr?
	bne.s	s33i_pbyt		 ; ... no
	move.b	spb_lf(a2),d1		 ; ... yes
	beq.s	s33i_xmit		 ; cr ignored

s33i_pbyt
	jsr	iob_pbyt		 ; put byte in buffer
	jsr	iob_room
	cmp.l	spd_room(a3),d0 	 ; enough?
	bgt.s	s33i_xmit		 ; ... plenty
	tst.b	spd_hand(a3)		 ; handshake?
	bgt.s	s33i_xoff		 ; XON/XOFF
	move.b	#1,spd_iact(a3) 	 ; marked xoffed
	bra.s	s33i_xmit
s33i_xoff
	move.b	#k.xoff,spd_xonf(a3)	 ; xoff to be sent
	bsr.l	s33_oopr
	sf	spd_iact(a3)		 ; input not active

; transmit byte

s33i_xmit
	tst.b	spd_oact(a3)		 ; output active
	beq.s	s33i_exit		 ; ... no
	btst	#sci..tdre,d7		 ; transmit ready?
	beq.s	s33i_exit
	st	d6

s33i_gbyte
	move.b	spd_xonf(a3),d1 	 ; xon/xoff to be sent?
	bne.s	s33i_sbcx		 ; ... yes
	move.l	spd_obuf(a3),d0 	 ; is there a queue?
	ble.s	s33i_nxmit		 ; ... no
	tst.b	spd_xoff(a3)		 ; transmit xoffed?
	bne.s	s33i_nxmit		 ; .. yes

	move.l	d0,a2			 ; set queue address

	moveq	#0,d7
	move.b	spb_prty(a2),d7
	jsr	iob_gbps		 ; get byte with parity set
	beq.s	s33i_txok		 ; ... something interrupt on xmit
	blt.s	s33i_nmore		 ; ... nothing

	subq.b	#1,d1			 ; is ff required?
	blt.s	s33i_gbyte		 ; ... no, try next queue
	bgt.s	s33i_cz 		 ; ... but really it is CTRL Z
	moveq	#k.ff,d1	  
	bra.s	s33i_txok		 ; send ff
s33i_cz
	moveq	#26,d1			 ; send CTRL Z

s33i_sbcx
	clr.b	spd_xonf(a3)		 ; no xon/xoff to send now / CC=Z
s33i_txok
	move.b	d1,sci_scdr		 ; send data
	bra.s	s33i_exit

s33i_nmore
s33i_nxmit
	move.w	#sci.rxint,sci_sccr1	 ; disable xmit interrupt
	sf	spd_oact(a3)		 ; deactivate output

s33i_exit
	move.l	(a5)+,a4		 ; standard exit
	move.l	(a5)+,a3
	jmp	(a4)

	end
