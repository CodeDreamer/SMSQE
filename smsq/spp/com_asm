; IBM COM physical routines  V2.10     1999  Tony Tebby

	section spp

	xdef	com_drdef

	xref	iob_gbps
	xref	iob_eof
	xref	iob_pbyt
	xref	iob_room

	xref	com_wtick

	xref	com_address
	xref.s	com.intl
	xref.s	como.data
	xref.s	como.ier
	xref.s	como.fifo
	xref.s	como.iir
	xref.s	como.lcr
	xref.s	como.mcr
	xref.s	como.lsr
	xref.s	como.msr
	xref.s	como.dll
	xref.s	como.dlh

	xref.s	com.imsk

	xref.s	com.base	; base port number = 1 for COM1 = SER1
	xref.s	com.mxport	; max physical port

	include 'dev8_keys_k'
mio_s	equ	0		; dummy bus width
	include 'dev8_keys_multiio'
	include 'dev8_keys_buf'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_hwt'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;
; Table defining the phyisical layer of the COM driver
;
com_drdef
	dc.w	com_check-*
	dc.w	com_vector-*
	dc.w	com_preset-*
	dc.w	com_init-*
	dc.w	com_istable-*

;+++
; Vector table defining hardware specific IO routines
;---
com_vector
	dc.w	spd_open,com_open-*-2
	dc.w	spd_close,com_close-*-2
	dc.w	spd_int,com_istable-*-2
	dc.w	spd_baud,com_baud-*-2
	dc.w	spd_mouse,com_mouse-*-2
	dc.w	spd_iopr,com_iopr-*-2
	dc.w	spd_oopr,com_oopr-*-2
	dc.w	spd_cdchk,com_cdchk-*-2
	dc.w	0

;+++
; Preset table defining hardware specific values
;---
com_preset
	assert	spd_pdir,spd_hand-1
	dc.w	spd_pdir,2,$ffff    ; any parity, hardware handshake
	dc.w	spd_room+2,2,spd.room ; input buffer spare room
	dc.w	spd_ilen+2,2,spd.ilen ; input buffer length
	dc.w	0

;+++
; Interrupt server table (conventional priorities)
;---
com_istable
	dc.w	com_iserve-*,com.intl,hwt.ser<<8  ; just one server
	dc.w	0

;+++
; Routine to check whether particular ports exist
;
;	d0 cr	port to check / 0 is it does not exist
;	d0 cr	0 / highest port number
;	d7    s if d0 = 0 on entry otherwise preserved
;	a4  r	hardware base
;
;	status returned as d0
;---
com_check
	tst.w	d0
	bne.s	comc_portn		 ; check port n
	moveq	#com.mxport,d7		 ; start at top port (physical)
comc_loop
	move.l	d7,d0
	bsr.s	comc_portp		 ; check physical port number
	bne.s	comc_rts
	subq.b	#1,d7
	bge.s	comc_loop
comc_noport
	moveq	#0,d0
	rts

comc_portn
	sub.w	#com.base,d0		 ; convert to physical port 0-3
	cmp.w	#com.mxport,d0
	bhi.s	comc_noport

comc_portp
	lsl.w	#2,d0
	lea	com_address,a4
	move.l	(a4,d0.w),a4		 ; hardware base address
	lsr.w	#2,d0
	cmp.l	#0,a4
	beq.s	comc_noport		 ; no port

	assert	como_iir,como_fifo
	lea	como.iir(a4),a4
	assert	com.fdis,0
	move.b	#com.fdis,(a4)		 ; disble fifo
	tst.b	(a4)
	bmi.s	comc_noport		 ; msb should be 0
	move.b	#com.fmin,(a4)		 ; enable fifo
	tst.b	(a4)
	bpl.s	comc_noport		 ; msb should be set
	lea	-como.iir(a4),a4
	add.w	#com.base,d0		 ; set logical port number
comc_rts
	rts

;+++
; Routine to check whether particular port is connected to a mouse
;
;	d1-d3 s
;	d7    s
;	a3 c  p linkage
;	a4    s hardware base
;
;	status returned 0 if mouse connected or err.fdnf
;---
com_mouse
	tst.w	spd_port(a3)		 ; does this port exist?
	ble.s	comm_fdnf		 ; ... no

	move.w	#1200/300,d2
	bsr.s	com_baud		 ; set baud rate and hardware base

	move.b	#com.7bit+com.2stop,como.lcr(a4) ; set data byte frame

comm_strip
	move.b	como.data(a4),d1	 ; strip fifo / buffer
	btst	#com..dr,como.lsr(a4)	 ; any rx data?
	bne.s	comm_strip		 ; ... yes

	move.b	#com.dtr+com.rts+com.ien,como.mcr(a4) ; enable dtr/rts

	jsr	com_wtick		 ; wait for at least one tick

comm_check
	btst	#com..dr,como.lsr(a4)	 ; any rx data?
	beq.s	comm_reset		 ; ... no
	cmp.b	#'M',como.data(a4)	 ; mouse?
	bne.s	comm_check		 ; ... no
	move.b	#com.fzero,como.fifo(a4) ; fifo enabled with zero threshold
	tst.b	como.msr(a4)		 ; 68040
	moveq	#0,d0			 ; the port is in the right mode now
	rts

comm_reset
	bsr.s	com_init		 ; reset port
comm_fdnf
	moveq	#err.fdnf,d0
	rts

;+++
; COM port linkage / hardware initialisation
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
com_init
cini.reg reg	d1/d2/d7
	movem.l cini.reg,-(sp)
	move.w	#38400/300,d2
	bsr.s	com_baud		 ; set baud rate and hardware base

	move.b	#com.8bit+com.2stop,como.lcr(a4) ; set data byte frame
	move.b	#$ffffff00+com.fmax+com.fclr,como.fifo(a4) ; set fifo operational
	move.b	#com.ien,como.mcr(a4)		 ; general interrupt enable
	move.b	#com.rxbi+com.txbi+com.rxsi+com.msi,como.ier(a4) ; enable all interrupts
	tst.b	como.msr(a4)		 ; 68040

	movem.l (sp)+,cini.reg
	moveq	#0,d0
	rts

;+++
; COM port set baud
;
;	d1 c  p baud rate
;	d2 c  s baud rate / 300
;	d7    s
;	a3 c  p COM port linkage block
;	a4  r	hardware address
;
;	all other registers preserved
;---
com_baud
	move.l	spd_hbase(a3),a4
	move.l	#1536,d0		 ; 4*clock / 300
	divu	d2,d0			 ; 4*rate divider
	cmp.w	#2,d0			 ; high rate?
	bls.s	qbd_high
	lsr.w	#2,d0			 ; low rate
	bra.s	qbd_set
qbd_high
	bset	#15,d0			 ; high rate
qbd_set
	move.w	sr,d7
	or.w	#com.imsk,sr		  ; no interrupts
	or.b	#com.dlab,como.lcr(a4)	  ; set baud rate
	move.b	d0,como.dll(a4)
	lsr.w	#8,d0
	move.b	d0,como.dlh(a4)
	and.b	#$ff-com.dlab,como.lcr(a4)
	tst.b	como.msr(a4)		 ; 68040

	move.w	d7,sr
	moveq	#0,d0
	rts

;+++
; COM port receive input operation
;
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
com_iopr
	tst.b	spd_iact(a3)		 ; already ready?
	beq.s	com_rxen		 ; ... no, must be xon / xoff
	tst.l	d0
	rts

;+++
; COM port open. Enables receive.
;
;	d0  r	0
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned 0
;---
com_open
	moveq	#0,d0

;+++
; COM port receive enable. If not already enabled, it checks for room before
; enabling receive.
;
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
com_rxen
	move.l	d0,-(sp)
	tst.b	spd_iact(a3)		 ; already ready?
	bne.s	crxe_exit		 ; ... yes
	move.l	spd_ibuf(a3),d0 	 ; any buffer?
	ble.s	crxe_exit		 ; ... no
	move.l	a2,-(sp)
	move.l	d0,a2
	jsr	iob_room		 ; enough room

	cmp.l	spd_room(a3),d0
	ble.s	crxe_exa2		 ; ... no

	move.w	sr,-(sp)		 ; save status
	or.w	#com.imsk,sr		 ; no interrupts

	tst.b	spd_hand(a3)		 ; xon/xoff?
	ble.s	crxe_hs 		 ; ... no

	move.b	#k.xon,spd_xonf(a3)	 ; set xon char to be sent

	bsr.l	com_oopr		 ; activate output
	bra.s	crxe_iact

crxe_hs
	move.l	spd_hbase(a3),a2	 ; hardware base
	or.b	#com.dtr+com.rts,como.mcr(a2)
	tst.b	como.msr(a4)		 ; 68040
crxe_iact
	st	spd_iact(a3)		 ; receive active now

	move.w	(sp)+,sr		 ; restore status
crxe_exa2
	move.l	(sp)+,a2
crxe_exit
	move.l	(sp)+,d0
	rts

;+++
; COM port close. If there is no input buffer, it is a nop.
;
;	d0 c  p pointer to input buffer
;	a3 c  p COM port linkage block
;	all other registers preserved
;	status returned according to D0
;---
com_close
       tst.l	d0			; input queue?
       beq.s	crxd_rts		; ... no

;+++
; COM port receive disable.
;---
com_rxdi
	move.w	sr,-(sp)		 ; save status
	move.l	a2,-(sp)
	or.w	#com.imsk,sr		 ; no interrupts

	move.l	spd_hbase(a3),a2	 ; hardware base
	and.b	#$ff-com.dtr-com.rts,como.mcr(a2)

	sf	spd_iact(a3)		 ; not active now
	move.l	(sp)+,a2
	move.w	(sp)+,sr		 ; restore status
crxd_rts
	rts

;+++
; COM port check carrier detect
;---
com_cdchk
	move.l	spd_hbase(a3),a4	 ; hardware base
	btst	#com..dcd,como.msr(a4)	 ; carrier detect?
	rts

;+++
; COM port interrupt server
;---
com_iserve
	move.l	spd_hbase(a3),a4	 ; hardware base

	moveq	#com.intbit,d0		 ; look at interrupts
	and.b	como.iir(a4),d0
	btst	#0,d0			 ; any?
	bne.l	comi_exit		 ; ... no

comi_jump
	st	d6			 ; interrupt serviced
	add.w	comi_tab(pc,d0.w),d0
	jmp	comi_tab(pc,d0.w)
comi_tab
	dc.w	comi_modem-*
	dc.w	comi_xmit-*
	dc.w	comi_rcv-*
	dc.w	comi_error-*

comi_error
	move.b	como.lsr(a4),d0 	 ; get (clear) error
	bra.l	comi_exit		 ; do nothing with it

; Receive byte

comi_rcv
	btst	#com..dr,como.lsr(a4)	 ; any rx data?
	beq.l	comi_exit		 ; ... no

	move.b	como.data(a4),d1

	tst.b	spd_hand(a3)		 ; xoff?
	ble.s	comi_ibuf		 ; ... no
	assert	k.xon-$11,k.xoff-$13,0
	moveq	#$7d,d0
	and.b	d1,d0
	cmp.b	#k.xon,d0		 ; xon or xoff?
	bne.s	comi_ibuf		 ; ... no
	btst	#1,d1			 ; on or off
	sne	spd_xoff(a3)		 ; ... yes
	bne.s	comi_rcv		 ; ... off, done
	bsr.s	com_oopr		 ; ... on, ensure transmit active
	bra.s	comi_rcv

comi_ibuf
	move.l	spd_ibuf(a3),d0 	 ; input queue block
	ble.s	comi_rcv		 ; ... none!!!!
	move.l	d0,a2
	move.b	spb_fz(a2),d0		 ; <FF> or CTRL Z
	subq.b	#1,d0
	ble.s	comi_ckcr		 ; ... not CTRL Z
	move.b	d1,d0
	tst.b	spb_prty(a2)		 ; parity?
	beq.s	comi_ckcz		 ; ... no
	bclr	#7,d0			 ; ... ignore it
comi_ckcz
	cmp.b	#26,d0			 ; is it CTRL Z?
	bne.s	comi_ckcr
	jsr	iob_eof 		 ; mark end of file
	jsr	com_rxdi		 ; disable receive
	st	spd_ibuf(a3)		 ; no input buffer pointer now
	bra.s	comi_rcv

comi_ckcr
	move.b	spb_cr(a2),d0		 ; cr to lf?
	beq.s	comi_pbyt		 ; ... no
	cmp.b	d0,d1			 ; cr?
	bne.s	comi_pbyt		 ; ... no
	move.b	spb_lf(a2),d1		 ; ... yes
	beq.s	comi_rcv		 ; cr ignored

comi_pbyt
	jsr	iob_pbyt		 ; put byte in buffer
	jsr	iob_room
	cmp.l	spd_room(a3),d0 	 ; enough?
	bgt.s	comi_rcv		 ; ... plenty
	tst.b	spd_hand(a3)		 ; handshake?
	bgt.s	comi_xoff		 ; XON/XOFF
	bsr	com_rxdi		 ; negate handshakes
	bra	comi_rcv
comi_xoff
	move.b	#k.xoff,spd_xonf(a3)	 ; xoff to be sent
	bsr.s	com_oopr
	sf	spd_iact(a3)		 ; input not active

comi_exit
	move.l	(a5)+,a4		 ; standard exit
	move.l	(a5)+,a3
	jmp	(a4)


comi.xreg reg	d0/d1/d2/d7/a2/a4
comi.xr1  reg	d0/d1/d2/d7/a2
com_oopr
	move.l	a4,-(sp)
	move.l	spd_hbase(a3),a4	 ; hardware base
	btst	#com..thre,como.lsr(a4)  ; ready?
	beq.s	coa_ex1 		 ; ... no, done
	movem.l comi.xr1,-(sp)
	move.w	sr,-(sp)
	or.w	#com.imsk,sr
	bsr.s	comi_xdo
	move.w	(sp)+,sr
	movem.l (sp)+,comi.xreg 	 ; restore exit regs
	tst.l	d0
comi_rts
	rts

coa_ex1
	move.l	(sp)+,a4
	tst.l	d0
	rts

; cts (or other msr) interrupt

comi_modem
	tst.b	como.msr(a4)		 ; clear interrupt

; transmit byte

comi_xmit
	pea	comi_exit
comi_xdo
	moveq	#15,d2			 ; fifo fill

	btst	#com..thre,como.lsr(a4)  ; ready?
	beq.s	comi_ncts		 ; ... no, cts not required now

	tst.b	spd_hand(a3)		 ; hardware handshaking?
	bge.s	comi_gbyte		 ; ... no
	btst	#com..cts,como.msr(a4)	 ; clear to send?
	beq.s	comi_scts		 ; ... no, retry on CTS falling
comi_gbyte
	move.b	spd_xonf(a3),d1 	 ; xon/xoff to be sent?
	bne.s	comi_sbcx		 ; ... yes
	move.l	spd_obuf(a3),d0 	 ; is there a queue?
	ble.s	comi_nxmit		 ; ... no
	tst.b	spd_xoff(a3)		 ; transmit xoffed?
	bne.s	comi_nxmit		 ; .. yes

	move.l	d0,a2			 ; set queue address

	moveq	#0,d7
	move.b	spb_prty(a2),d7
	jsr	iob_gbps		 ; get byte with parity set
	beq.s	comi_txok		 ; ... something interrupt on xmit
	blt.s	comi_nmore		 ; ... nothing

	subq.b	#1,d1			 ; is ff required?
	blt.s	comi_gbyte		 ; ... no, try next queue
	bgt.s	comi_cz 		 ; ... but really it is CTRL Z
	moveq	#k.ff,d1	  
	bra.s	comi_txok		 ; send ff
comi_cz
	moveq	#26,d1			 ; send CTRL Z

comi_sbcx
	clr.b	spd_xonf(a3)		 ; no xon/xoff to send now / CC=Z
comi_txok
	move.b	d1,como.data(a4)	 ; send data
	tst.b	como.msr(a4)		 ; 68040
	dbra	d2,comi_gbyte		 ; ... try again

comi_ncts
	move.b	#com.rxbi+com.txbi+com.rxsi,como.ier(a4) ; mask cts interrupt
	tst.b	como.msr(a4)		 ; 68040
	rts

comi_scts
	move.b	#com.rxbi+com.rxsi+com.msi,como.ier(a4) ; set cts interrupt
	tst.b	como.msr(a4)		 ; 68040
	rts

comi_nmore
	btst	#com..thre,como.lsr(a4)  ; ready?
	beq.s	comi_ncts		 ; ... no, set xmit interrupt

comi_nxmit
	move.b	#com.rxbi+com.rxsi,como.ier(a4) ; mask xmit/cts interrupts
	tst.b	como.msr(a4)		 ; 68040
	rts

	end
