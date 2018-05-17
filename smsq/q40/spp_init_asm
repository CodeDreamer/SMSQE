; Q40 SER PAR PRT device initialisation  V2.10	  1999  Tony Tebby

	section spp

	xdef	q40_spp_init

	xref	spp_init
	xref	spp_mouse

	xdef	com_wtick

	xdef	com_address
	xdef	lpt_address

	xdef	como.data
	xdef	como.ier
	xdef	como.fifo
	xdef	como.iir
	xdef	como.lcr
	xdef	como.mcr
	xdef	como.lsr
	xdef	como.msr
	xdef	como.dll
	xdef	como.dlh
	xdef	lpto.dtr
	xdef	lpto.str
	xdef	lpto.ctr
	xdef	ecpo.data
	xdef	ecpo.cnfga
	xdef	ecpo.cnfgb
	xdef	ecpo.ecr

	xdef	lpt.ie
	xdef	ecp.ie

	xdef	com.intl
	xdef	lpt.intl

	xdef	com.imsk
	xdef	lpt.imsk
	xdef	lpt.cycles
	xdef	lpt.burst

	xdef	com.base
	xdef	com.mxport
	xdef	lpt.base
	xdef	lpt.mxport
	xdef	lpt.mxecp

	include 'dev8_keys_q40'
	include 'dev8_keys_q40_multiio'
	include 'dev8_mac_vecx'

com.intl   equ	$020d	; interrupt level 2 / 8 kHz response
lpt.intl   equ	$0210	; interrupt level 2 / 64 kHz response
lpt.cycles equ	120	; max 120 cycles per go for spp
lpt.burst  equ	31	; max 32 byte bursts for ECP

como.data  equ	como_rxdr
como.ier   equ	como_ier
como.fifo  equ	como_fifo
como.iir   equ	como_iir
como.lcr   equ	como_lcr
como.mcr   equ	como_mcr
como.lsr   equ	como_lsr
como.msr   equ	como_msr
como.dll   equ	como_dll
como.dlh   equ	como_dlh

lpto.dtr   equ	lpto_dtr
lpto.str   equ	lpto_str
lpto.ctr   equ	lpto_ctr
ecpo.data  equ	ecpo_data
ecpo.cnfga equ	ecpo_cnfga
ecpo.cnfgb equ	ecpo_cnfgb
ecpo.ecr   equ	ecpo_ecr

lpt.ie	   equ	lptc.inte*0		; do not enable SPP interrupts
ecp.ie	   equ	ecp.ecps*1		; disable ECP interrupts

com.imsk   equ	$0200 ; mask interrupts to level 2 for critical ops
lpt.imsk   equ	$0200 ; mask interrupts to level 2 for critical ops

com.base   equ	1    ; COM1 = SER1
com.mxport equ	3    ; physical ports 0,1,2,3
com_address dc.l COM1,COM2,COM3,COM4

lpt.base   equ	1    ; LPT1 = PAR1
lpt.mxport equ	2    ; physical ports 0,1,2
lpt.mxecp  equ	1    ; ECP on 0,1 only
lpt_address dc.l LPT1,LPT2,LPT3


;+++
; SPP driver initialisation.
;
;	a1-a3  scratch
;	status return standard
;---
q40_spp_init
	lea	q40_par,a1
	lea	q40_ser,a2
	jsr	spp_init		 ; set up serial and parallel ports
	jmp	spp_mouse		 ; find mouse

q40_par
	vecx	lpt_drdef		 ; standard IBM LPT driver
	novec
q40_ser
	vecx	com_drdef		 ; standard IBM COM driver
	novec

; com wtick is called with interrupts disabled (during init) to wait
; for at least two ticks

com_wtick
	moveq	#2,d0			 ; wait for two+1 ticks

comw_clear
	st	q40_fack		 ; clear frame interrupt

	btst	#q40..frame,q40_ir	 ; frame interrupt?
	bne.s	comw_clear		 ; ... yes, it is still there

comw_1
	btst	#q40..frame,q40_ir	 ; frame interrupt?
	beq.s	comw_1			 ; ... no

	dbra	d0,comw_clear

	rts












	end
