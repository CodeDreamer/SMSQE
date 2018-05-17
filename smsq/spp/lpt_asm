; IBM Standard LPT physical routines  V2.10     1999  Tony Tebby
; NB. These routines assume that the LPT port is in fact an ECP port

	section spp

	xdef	lpt_drdef

	xref	iob_gbyt

	xref	lpt_address
	xref.s	lpto.dtr
	xref.s	lpto.str
	xref.s	lpto.ctr
	xref.s	ecpo.data
	xref.s	ecpo.cnfga
	xref.s	ecpo.cnfgb
	xref.s	ecpo.ecr

	xref.s	lpt.ie
	xref.s	ecp.ie

	xref.s	lpt.intl
	xref.s	lpt.imsk
	xref.s	lpt.cycles	; SPP max cycles
	xref.s	lpt.burst	; ECP burst

	xref.s	lpt.base	; base port number = 1 for LPT1 = PAR1
	xref.s	lpt.mxport	; max physical port number (-ve if none)
	xref.s	lpt.mxecp	; max ecp (-ve if none)

	include 'dev8_keys_k'
mio_s	equ	0		; dummy bus width
	include 'dev8_keys_multiio'
	include 'dev8_keys_buf'
	include 'dev8_keys_iod'
	include 'dev8_keys_serparprt'
	include 'dev8_keys_hwt'
	include 'dev8_mac_assert'

;
; Table defining the phyisical layer of the LPT driver
;
lpt_drdef
	dc.w	lpt_check-*
	dc.w	lpt_vector-*
	dc.w	lpt_preset-*
	dc.w	lpt_init-*
	dc.w	lpt_istable-*

;+++
; Vector table defining hardware specific IO routines
;---
lpt_vector
	dc.w	spd_open,lpt_open-*-2
	dc.w	spd_close,lpt_close-*-2
	dc.w	spd_int,lpt_istable-*-2
;	 dc.w	 spd_iopr,lpt_iopr-*-2
	dc.w	spd_oopr,lpt_oopr-*-2
	dc.w	0

;+++
; Preset table defining hardware specific values
;---
lpt_preset
	assert	spd_pdir,spd_hand-1
	dc.w	spd_pdir,2,$0100      ; output only
	dc.w	0

;+++
; Interrupt server table (conventional priorities)
;---
lpt_istable
	dc.w	lpt_iserve-*,lpt.intl,hwt.par<<8  ; just one server
	dc.w	0

;+++
; Routine to check whether particular ports exist
;
;	d0 cr	port to check / 0 is it does not exist
;	d0 cr	0 / highest port number
;	d7    s if d0 = 0 on entry, otherwise preserved
;	a4  r	hardware base
;
;	status returned as d0
;---
lpt_check
	tst.w	d0
	bne.s	lptc_portn		 ; check port n
	moveq	#lpt.mxport,d7		 ; start at top port (physical)
	blt.s	lptc_none

lptc_loop
	move.l	d7,d0
	bsr.s	lptc_portp		 ; check physical port number
	bne.s	lptc_rts
	subq.b	#1,d7
	bge.s	lptc_loop

lptc_none
	moveq	#0,d0
lptc_rts
	rts


lptc_portn
	sub.w	#lpt.base,d0		 ; convert to physical port 0-3
	cmp.w	#lpt.mxport,d0
	bhi.s	lptc_noport

lptc_portp
	move.l	d7,-(sp)
	move.w	d0,d7
	lsl.w	#2,d7
	lea	lpt_address,a4
	move.l	(a4,d7.w),d7		 ; hardware base address
	beq.s	lptc_noport

	move.l	d7,a4

	cmp.b	#lpt.mxecp,d0		 ; ECP port?
	bgt.s	lptc_ckspp		 ; ... no
	move.b	#ecpm.spp,ecpo.ecr(a4)	 ; ... possibly, set to spp

lptc_ckspp
	assert	lptc.clear,lptc.ecp
	move.b	#lptc.clear,lpto.ctr(a4) ; clear control reg
	move.b	#$7f,lpto.dtr(a4)	 ; delete character
	move.b	#lptc.clear,lpto.ctr(a4) ; change bus value
	cmp.b	#$7f,lpto.dtr(a4)	 ; is port still set?
	bne.s	lptc_noport

	cmp.b	#lpt.mxecp,d0		 ; ECP port?
	bgt.s	lptc_spp		 ; ... no

	move.b	#ecpm.conf,ecpo.ecr(a4)  ; set to config mode
	clr.b	ecpo.cnfga(a4)		 ; clear config
	moveq	#$ffffff00+ecp.mode,d7
	and.b	ecpo.ecr(a4),d7 	 ; is it config mode?
	cmp.b	#ecpm.conf,d7
	bne.s	lptc_spp		 ; ... no, must be SPP
	moveq	#ecp.width,d7
	and.b	ecpo.cnfga(a4),d7	 ; is it 8 bit port?
	cmp.b	#ecp.8bit,d7
	bne.s	lptc_spp		 ; ... no
	move.b	#ecpm.fifo+ecp.ie,ecpo.ecr(a4)	; set to PS2 (bidirectional) port mode
	tst.b	ecpo.ecr(a4)		 ; 68040

lptc_exit
	move.l	(sp)+,d7
	add.w	#lpt.base,d0		 ; set logical port number
	rts

lptc_spp
	move.b	#lptc.output+lpt.ie,lpto.ctr(a4) ; set output mode (SPP)
	tst.b	lpto.ctr(a4)		 ; 68040
	bra.s	lptc_exit

lptc_noport
	moveq	#-lpt.base,d0
	bra.s	lptc_exit

;+++
; LPT port linkage / hardware initialisation = none, check leaves port OK
;
;	a3 c  p linkage for this port
;	a4    s hardware base
;
;	status returned zero
;---
lpt_init
	pea	lpt_sched		 ; install scheduler for any port
	move.l	spd_ppar(a3),a4 	 ; base definition
	move.l	(sp)+,iod_shad(a4)

	move.l	spd_hbase(a3),a4
	btst	#lpt..slin,lpto.ctr(a4)  ; is port set for spp?
	beq.s	lptin_ok		 ; ... no
	move.w	#1,spd_puls(a3) 	 ; set 1 cycle pulse
lptin_ok
	moveq	#0,d0
	rts

;+++
; LPT port open operation
;
;	d0  r	0
;	a3 c  p LPT port linkage block
;	all other registers preserved
;	status returned 0
;---
lpt_open
	tst.b	spd_oact(a3)		 ; output active
	bne.s	lpto_d0 		 ; ... yes
	move.l	a4,-(sp)
	move.l	spd_hbase(a3),a4

	tst.w	spd_puls(a3)		 ; spp mode?
	bne.s	lpto_spp

	moveq	#$ffffff00+ecp.mode,d0
	and.b	ecpo.ecr(a4),d0 	 ; current mode
	cmp.b	#ecpm.fifo,d0		 ; already in fifo mode?
	beq.s	lpto_exit
	move.b	#lptc.ecp,lpto.ctr(a4)	 ; set to fifo mode
	move.b	#ecpm.fifo+ecp.ie,ecpo.ecr(a4)
	tst.b	ecpo.ecr(a4)		 ; 68040
	bra.s	lpto_exit

lpto_spp
	moveq	#lptc.ocmask,d0
	and.b	lpto.ctr(a4),d0
	cmp.b	#lptc.ocheck,d0 	 ; is port already set?
	beq.s	lpto_exit
	move.b	#ecpm.spp+ecp.ie,ecpo.ecr(a4)
	move.b	#lptc.output+lpt.ie,lpto.ctr(a4) ; set to spp output mode
	tst.b	lpto.ctr(a4)		 ; 68040

lpto_exit
	move.l	(sp)+,a4
lpto_d0
	moveq	#0,d0
	rts
;+++
; LPT port close operation
;
;	a3 c  p LPT port linkage block
;	all other registers preserved
;	status returned according to D0
;---
lpt_close
	tst.l	d0
	rts

;+++
; LPT port receive input operation
;
;	a3 c  p LPT port linkage block
;	all other registers preserved
;	status returned according to D0
;---
lpt_iopr
	tst.l	d0
	rts

;+++
; LPT port sheduler routine
;
;	a3 c  p LPT 1 port linkage block
;---
lpt_sched
	move.w	spd_npar(a3),d5 	 ; number of ports to do
	beq.s	lpts_rts		 ; ... none

lpts_loop
	tst.b	spd_oact(a3)		 ; server active?
	beq.s	lpts_eloop		 ; ... no
	move.l	spd_hbase(a3),a4	 ; set hardware base
	bgt.s	lpts_spp		 ; spp mode

	btst	#ecp..fifof,ecpo.ecr(a4) ; fifo full?
	bne.s	lpts_eloop		 ; ... yes, next
	move.w	sr,-(sp)
	or.w	#lpt.imsk,sr
	bsr.s	lpti_xdo		 ; do fifo mode interrupt server
	move.w	(sp)+,sr
	bra.s	lpts_eloop

lpts_spp
	assert	lpt..nbusy,7
	tst.b	lpto.str(a4)		 ; busy?
	bpl.s	lpts_eloop		 ; ... yes
	move.w	sr,-(sp)
	or.w	#lpt.imsk,sr
	bsr.l	sppi_xdo		 ; do spp mode interrupt server
	move.w	(sp)+,sr

lpts_eloop
	add.w	#spd.len,a3		 ; next linkage
	subq.w	#1,d5
	bgt.s	lpts_loop
lpts_rts
	rts

;+++
; LPT port transmit output operation
; In this version, it just sets the output active, the scheduler will do
; the rest.
;
;	a3 c  p LPT port linkage block
;	all other registers preserved
;	status returned according to D0
;---
lpt_oopr
lpti.xreg reg	d0/d1/d2/d7/a2/a4
lpti.xr1  reg	d0/d1/d2/d7/a2
	tst.w	spd_puls(a3)		 ; spp mode?
	bne.s	lptoo_spp		 ; ... yes
	st	spd_oact(a3)		 ; activate output server
	tst.l	d0
	rts

lptoo_spp
	move.b	#1,spd_oact(a3) 	 ; flag old style server
	tst.l	d0
	rts

;+++
; LPT port interrupt server
;---
lpt_iserve
	tst.b	spd_oact(a3)		 ; server active?
	beq.s	lpti_exit		 ; ... no
	bgt.s	spp_iserve		 ; spp mode
	move.l	spd_hbase(a3),a4	 ; hardware base
	btst	#ecp..fifof,ecpo.ecr(a4) ; fifo full?
	bne.s	lpti_exit

	st	d6			 ; interrupt serviced
	pea	lpti_exit

lpti_xdo
	moveq	#lpt.burst,d2		 ; max burst

lpti_gbyte
	move.l	spd_obuf(a3),d0 	 ; is there a queue?
	ble.s	lpti_nomore		 ; ... no
	move.l	d0,a2			 ; set queue address

lpti_loop
	btst	#ecp..fifof,ecpo.ecr(a4) ; fifo full?
	bne.s	lpti_done		 ; ... yes

	jsr	iob_gbyt		 ; get byte
	beq.s	lpti_txok		 ; ... something, interrupt on xmit
	blt.s	lpti_nomore		 ; ... nothing

	subq.b	#1,d1			 ; is ff required?
	blt.s	lpti_gbyte		 ; ... no, try next queue
	bgt.s	lpti_cz 		 ; ... but really it is CTRL Z
	moveq	#k.ff,d1	  
	bra.s	lpti_txok		 ; send ff
lpti_cz
	moveq	#26,d1			 ; send CTRL Z

lpti_txok
	move.b	d1,ecpo.data(a4)	 ; send data
	tst.b	ecpo.ecr(a4)		 ; 68040
	dbra	d2,lpti_loop		 ; ... try again
lpti_done
	move.b	#ecpm.fifo+ecp.ecps,ecpo.ecr(a4)
	move.b	#ecpm.fifo+ecp.ie,ecpo.ecr(a4) ; re-enable interrupts (if poss)
	tst.b	ecpo.ecr(a4)		 ; 68040
lpti_rts
	rts

lpti_nomore
sppi_nomore
	clr.b	spd_oact(a3)		 ; server no longer active
	rts

lpti_exit

sppi_exit
	move.l	(a5)+,a4		 ; standard exit
	move.l	(a5)+,a3
	jmp	(a4)

;+++
; SPP port interrupt server
;---
spp_iserve
	move.l	spd_hbase(a3),a4	 ; hardware base
	assert	lpt..nbusy,7
	tst.b	lpto.str(a4)		 ; busy?
	bpl.s	sppi_exit		 ; ... yes

	st	d6			 ; interrupt serviced
	pea	sppi_exit

sppi_xdo
	move.l	spd_obuf(a3),d0 	 ; is there a queue?
	ble.s	sppi_nomore		 ; ... no
	move.l	d0,a2			 ; set queue address

	move.w	#lpt.cycles,d2		 ; max cycles (=approx max microsecs)

sppi_loop
	move.w	spd_wait(a3),d0 	 ; max wait between bytes
sppi_wait
	subq.w	#1,d2
	tst.b	lpto.str(a4)		 ; busy?
	bmi.s	sppi_gbyte		 ; ... no
	dbra	d0,sppi_wait		 ; keep on trying
	rts				 ; until we give up

sppi_gbyte
	jsr	iob_gbyt		 ; get byte
	beq.s	sppi_txok		 ; ... something, interrupt on xmit
	blt.s	sppi_nomore		 ; ... nothing

	subq.b	#1,d1			 ; is ff required?
	blt.s	sppi_gbyte		 ; ... no, try next queue
	bgt.s	sppi_cz 		 ; ... but really it is CTRL Z
	moveq	#k.ff,d1
	bra.s	sppi_txok		 ; send ff
sppi_cz
	moveq	#26,d1			 ; send CTRL Z

sppi_txok
	move.b	d1,lpto.dtr(a4) 	 ; send data
	move.w	spd_puls(a3),d0
	sub.w	d0,d2
sppi_ploop
	move.b	#lptc.ostrobe+lpt.ie,lpto.ctr(a4) ; strobe
	subq.b	#1,d0
	bgt.s	sppi_ploop
	move.b	#lptc.output+lpt.ie,lpto.ctr(a4)
	tst.b	lpto.ctr(a4)		 ; 68040

	subq.w	#2,d2
	bgt.s	sppi_loop		 ; ... try again

sppi_rts
	rts

	end
