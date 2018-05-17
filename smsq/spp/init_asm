; SMSQ SER/PAR/PRT driver initialisation  V2.10     1999  Tony Tebby

	section spp

	xdef	spp_init

	xref	spp_thing
	xref	spp_tnam
	xref	spp_procs
	xref	spp_baud

	xref.l	spp_vers

	xref	hwt_vector		 ; set the vectors
	xref	hwt_preset		 ; preset values
	xref	hwt_init		 ; call init routine
	xref	hwt_iserve		 ; install the interrupt servers

	xref	iou_idset
	xref	iou_idlk
	xref	gu_achpp
	xref	gu_thzlk
	xref	ut_procdef

	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_thg'
	include 'dev8_keys_hwt'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_serparprt'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'
	include 'dev8_mac_vecx'
;+++
; SER/PAR/PRT driver initialisation.
;
;	a1 c  p pointer to table of PAR driver tables
;	a2 c  p pointer to table of SER driver tables
;	a3  r	SER/PAR/PRT linkage block
;	status return 0
;---
spp_init
sppi.reg reg	d1/d2/d3/d4/d5/d6/a0/a1/a2/a4
	movem.l sppi.reg,-(sp)
	bsr.s	spp_install		 ; install driver

	lea	spp_procs,a1		 ; set up procedures
	jsr	ut_procdef

	movem.l (sp)+,sppi.reg
	rts

spp_install
	moveq	#sms.xtop,d0
	trap	#do.sms2

	move.l	a1,a5
	bsr.l	sppi_pmax		 ; check the PAR parts
	move.l	d1,d2			 ; highest par
	move.l	a2,a5
	bsr.l	sppi_pmax		 ; check the SER parts
	move.l	d1,d3			 ; highest ser
	add.w	d2,d1			 ; total

	mulu	#spd.len,d1		 ; definition block length
	move.l	#spdp.len+spdt.len+iod.sqhd,d0
	add.l	d1,d0			 ; total space required

	jsr	gu_achpp
	bne.l	sppi_exit

	lea	spp_defs,a3		 ; IO routine definitions
	jsr	iou_idset		 ; set up

	move.l	a3,d4			 ; save par base
	move.w	d2,d5			 ; number of par ports
	mulu	#spd.len,d5
	add.l	d4,d5			 ; save ser base
	move.l	d1,d6
	add.l	d4,d6			 ; save prt base
	moveq	#spdp.len,d7
	add.l	d6,d7			 ; save thing base

	move.l	d7,a0
	move.l	a3,spth_spd(a0) 	 ; point from thing to base of block

	lea	spth_link(a0),a0	 ; set up thing linkage
	lea	spp_thing,a5		 ; our Thing
	move.l	a5,th_thing(a0) 	 ; ... set pointer
	lea	th_verid(a0),a4
	move.l	#spp_vers,(a4)+ 	 ; ... set version
	lea	spp_tnam,a5		 ; thing name
	move.l	(a5)+,(a4)+
	move.l	(a5)+,(a4)+
	move.l	(a5)+,(a4)+
	move.b	(a5)+,(a4)+

	jsr	gu_thzlk		 ; link in thing

	assert	spd_npar,spd_nser-2
	movem.w d2/d3,spd_npar(a3)	 ; set number of ports

	movem.l d4-d7,spd_ppar(a3)	 ; set base area pointers

	assert	spd_parp,spd_serp-1,spd_pars-2,spd_sers-3
	move.l	#$ff0000ff,spd_parp(a3)  ; and usage
	move.l	a3,-(sp)

	bsr.s	sppi_setb		 ; set all parallel port blocks
	move.w	d3,d2
	move.l	a2,a1
	bsr.s	sppi_setb		 ; set all serial port blocks

	lea	spp_baud,a0		 ; comms baud routine
	lea	sms.t1tab+sms.comm*4,a5  ; set here
	move.l	a0,d0
	swap	d0
	jsr	sms.wbase
	swap	d0
	jsr	sms.wbase

	move.l	(sp)+,a3		 ; recover linkage
	jsr	iou_idlk		 ; now link in (safe, poll does nothing
					 ; if the critical bits are 0)
sppi_exit
	rts

;+++
; Internal routine to check for the highest port number
;---
sppi_pmax
	moveq	#0,d1

sppi_pckloop
	move.l	a5,a0
	move.w	(a5)+,d0		 ; next table
	beq.s	sppi_rts

	add.w	d0,a0
	assert	drd_check,0
	add.w	(a0),a0 		 ; check routine for this driver
	moveq	#0,d0			 ; find max
	jsr	(a0)
	cmp.w	d1,d0			 ; new top?
	ble.s	sppi_pckloop
	move.w	d0,d1			 ; yes
	bra.s	sppi_pckloop

;+++
; Internal routine to setup the linkage for one port
;---
sppi_setb
	moveq	#0,d1			 ; highest port set
	bra.s	sppi_porteloop

sppi_portloop
	move.l	a1,a0

sppi_drvloop
	move.l	a0,a5			 ; try each driver in turn
	move.w	(a0)+,d0
	beq.s	sppi_portnext
	add.w	d0,a5			 ; table for this driver

	assert	drd_check,0
	move.l	a5,a4
	add.w	(a5)+,a4		 ; check routine for this driver
	move.l	d1,d0
	jsr	(a4)			 ; does this driver exists
	beq.s	sppi_drvloop		 ; ... no

	movem.l d4-d7,spd_ppar(a3)	 ; set base area pointers

	move.l	a4,spd_hbase(a3)	 ; hardware base
	move.w	d1,spd_port(a3) 	 ; port number

	jsr	hwt_vector		 ; set the vectors
	jsr	hwt_preset		 ; preset values
	jsr	hwt_init		 ; call init routine
	jsr	hwt_iserve		 ; install the interrupt servers

	move.w	d0,spd_hwto(a3) 	 ; offset in hardware table

; we do not loop back for the next driver as we install only one per port

sppi_portnext
	add.w	#spd.len,a3		 ; next block
sppi_porteloop
	addq.w	#1,d1			 ; next port
	cmp.w	d2,d1			 ; higher than max?
	bls	sppi_portloop		 ; try all drivers for the next port
sppi_rts
	rts

spp_defs
	dc.l	0	   ; pre-allocated
	dc.l	1<<iod..ssr+1<<iod..scn serial and name

	novec		   ; no xint
	vecx	spp_poll
	novec		   ; no sched
	vecx	spp_io	   ; a full set of opens
	vecx	spp_open
	vecx	spp_close

	vecx	spp_cnam

	end
