; ACSI Specific Routines        1990	  Tony Tebby  QJUMP

	section dv3

	xdef	ac_ckrdy		 ; check drive ready
	xdef	ac_lock 		 ; lock door
	xdef	ac_unlock		 ; unlock door
	xdef	ac_ststp		 ; start / stop
	xdef	ac_cmdn 		 ; command, no parameters
	xdef	ac_cmdr 		 ; command to read sector
	xdef	ac_cmdw 		 ; command to write sector
	xdef	ac_cmdi 		 ; command to read information
	xdef	ac_statr		 ; status read
	xdef	ac_nmode		 ; reset normal mode
	xdef	ac_reset		 ; reset after timeout

	xdef	acp_paus

	xref	ac_stat
	xref	at_seta
	xref	at_setra
	xref	at_setwa
	xref	at_p10u
	xref	at_wait80

	include 'dev8_keys_atari'
	include 'dev8_keys_err'
	include 'dev8_keys_scsi'
	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_smsq_base_keys'

	include 'dev8_mac_assert'

;+++
; ACSI SPECIFIC ROUTINES
;---
ac_acsi
;+++
; Default ASCI slugging
;---
acp_paus dc.b	30
	ds.w	0

;+++
; Lock the door
;
;	d0 c  p
;	d7 c  p lsw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return arbitrary
;---
ac_lock
acl.reg reg	d0/d3/a2
	movem.l acl.reg,-(sp)
	lea	hdl_remd(a3),a2 	 ; offset tables
	moveq	#0,d3
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d3 ; target number
	move.b	hdl_nlok-hdl_remd(a2,d3.l),d0 ; one more partition locked
	addq.b	#1,hdl_nlok-hdl_remd(a2,d3.l)
	tst.b	d0			 ; first one?
	bne.s	acl_exit		 ; ... no
	moveq	#scc.lock,d0		 ; lock the door
	bra.s	acl_do
;+++
; Unlock the door
;
;	d0 c  p
;	d7 c  p lsw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return arbitrary
;---
ac_unlock
	movem.l acl.reg,-(sp)
	lea	hdl_remd(a3),a2 	 ; offset tables
	moveq	#0,d3
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d3 ; target number
	subq.b	#1,hdl_nlok-hdl_remd(a2,d3.l) ; one fewer partitions locked
	bgt.s	acl_exit		 ; ... still some locked
	clr.b	hdl_nlok-hdl_remd(a2,d3.l) ; to be sure
	moveq	#scc.unlk,d0		 ; unlock the door
acl_do
	tst.b	hdl_remd-hdl_remd(a2,d3.w) ; removable?
	bge.s	acl_exit		 ; ... no

	move.l	d0,d3
	moveq	#scc.lkun,d0		 ; lock or unlock
	bsr.s	ac_cmdh

acl_exit
	movem.l (sp)+,acl.reg
	rts

;+++
; Start / Stop the drive
;
;	d3 c  p 4 start/stop command bytes / zero
;	d7 c  p lsw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return zero
;---
ac_ststp
	add.w	d7,a3
	moveq	#0,d0
	move.b	hdl_targ-1(a3),d0	 ; target number
	sub.w	d7,a3
	add.l	d0,a3
	cmp.b	#hdl.vrtx,hdl_remd(a3)	 ; vortex?
	sub.l	d0,a3
	beq.s	ass_vrtx		 ; ... yes

	moveq	#scc.stst,d0		 ; start / stop
	bra.s	ass_cmd
ass_vrtx
	moveq	#scc.stsv,d0		 ; Vortex command!!!
ass_cmd
	bsr.s	ac_cmdh

ass_ok
	moveq	#0,d0
	rts

;+++
; Check Ready Status
;
;	d0  r	0 ready, +1 changed, -ve bad
;	d7 c  p lsw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard or positive
;---
ac_ckrdy
ack.reg reg	d2/d3/a2
	movem.l ack.reg,-(sp)
	lea	hdl_remd(a3),a2 	 ; offset tables
	moveq	#0,d3
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d3 ; target number
	tst.b	hdl_remd-hdl_remd(a2,d3.l) ; removable disk?
	beq.s	ack_ok			 ; ... no

ack.ntry equ	3-1

	moveq	#ack.ntry,d2		 ; try three times once runup

ack_loop
	moveq	#0,d3
	moveq	#scc.test,d0		 ; test drive
	bsr.s	ac_cmdn
	blt.s	ack_bad 		 ; bad bad bad
	dbeq	d2,ack_loop

ack_done
	subq.w	#ack.ntry,d2		 ; any error?
	bne.s	ack_chnge

ack_ok
	moveq	#0,d0
ack_exit
	movem.l (sp)+,ack.reg
	rts

ack_bad
	moveq	#err.mchk,d0		 ; bad
	bra.s	ack_exit

ack_chnge
	moveq	#1,d0			 ; medium changed?
	bra.s	ack_exit

;+++
; Command to Send SCSI Command no Data and read status, holding the poll task
;
;	d0 cr	command / status
;	d3 c  p four bytes of parameters
;	d7 c  p lsw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard or >0 status error
;---
ac_cmdh
	assert	hdl.hold,-1
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll
	bsr.s	ac_cmdn
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	tst.l	d0
	rts

;+++
; Command to Send SCSI Command no Data and read status
;
;	d0 cr	command / status
;	d3 c  p four bytes of parameters
;	d7 c  p lsw drive number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard or >0 status error
;---
ac_cmdn
acn.reg reg	d2/d3
	movem.l acn.reg,-(sp)
	tas	sys_dmiu(a6)		 ; dragon

;;;;;	     move.w  sr,-(sp)  ; this used to be necessary

	bsr.l	ac_cmds 		 ; ... select and start
	bne.s	acn_oops

	bsr.l	ac_cmdp 		 ; send parameters
	bne.s	acn_oops

	move.l	#dma.adat<<16+dma.adat,d0 ; no DMA
	bsr.l	ac_cmdl
	bne.s	acn_oops

;;;;;	     move.w  (sp)+,sr
	jsr	at_p10u 		 ; pause 10 us

	bsr.l	ac_stat
;;;;;	     bra.s   acn_exit
acn_oops
;;;;;	     move.w  (sp)+,sr
acn_exit
	bclr	#7,sys_dmiu(a6)
	movem.l (sp)+,acn.reg
	tst.l	d0
	rts

;+++ 
; Command to Read SCSI Info
;
;	d0 cr	command / status
;	d3 c  p four bytes of parameters
;	d7 c  p lsw drive number
;	a1 c  p pointer to buffer
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
ac_cmdi
aci_do
aci.reg reg	d2/d3
	movem.l aci.reg,-(sp)
;;;;;	     move.w  sr,-(sp)  ; this used to be necessary

	bsr.l	ac_cmds 		 ; ... select and start
	bne.s	aci_exit

	jsr	at_seta

	bsr.l	ac_cmdp 		 ; send parameters
	bne.s	aci_exit

	moveq	#1,d2			 ; one sector max
	jsr	at_setra		 ; set dma read and count

	move.l	#dma.aprd<<16+dma.adrd,d0 ; read sector(s)
	bsr.l	ac_cmdl

	jsr	at_p10u 		 ; pause 10 us

aci_exit
;;;;;	     move.w  (sp)+,sr
	movem.l (sp)+,aci.reg
	tst.l	d0
	rts

;+++
; Command to read sectors from an ACSI device
;
;	d2 c  p number of sectors (word, 0=256)
;	d3 c  p block position (21 bits zero extended)
;	d7 c  p lsw drive number (1 to 8)
;	a1 c  p dma address
;	a3 c  p winchester linkage block
;
;	status return 0 or ERR.NC
;
;---
ac_cmdr
 clr.b hdl_err(a3)
	move.l	d3,-(sp)
;;;;;	     move.w  sr,-(sp)  ; this used to be necessary

	lsl.l	#8,d3			 ; in two regs
	move.b	d2,d3

	moveq	#scc.read,d0		 ; read sector command
	bsr.s	ac_cmds 		 ; ... select and start
	bne.s	acr_exit

	jsr	at_seta 		 ; set dma address

	bsr.l	ac_cmdp 		 ; send parameters
	bne.s	acr_exit

	jsr	at_setra		 ; set dma read and count

	move.l	#dma.aprd<<16+dma.adrd,d0 ; read sector(s)
	bsr.l	ac_cmdl

	jsr	at_p10u 		 ; pause 10 microseconds

acr_exit
;;;;;	     move.w  (sp)+,sr
	move.l	(sp)+,d3
	tst.l	d0
	rts

;+++
; Command to write sectors to an ACSI device
;
;	d2 c  p number of sectors (word, 0=256)
;	d3 c  p block position
;	d7 c  p lsw drive number (1 to 8)
;	a1 c  p dma address
;	a3 c  p winchester linkage block
;
;	status return 0 or ERR.NC
;
;---
ac_cmdw
	move.l	d3,-(sp)
;;;;;	     move.w  sr,-(sp)  ; this used to be necessary

	lsl.l	#8,d3			 ; in two regs
	move.b	d2,d3

	jsr	at_seta 		 ; set DMA address

	moveq	#scc.writ,d0		 ; select and write sector command
	bsr.s	ac_cmds
	bne.s	acw_exit

	bsr.l	ac_cmdp 		 ; the rest of the parameters
	bne.s	acw_exit

	jsr	at_setwa		 ; set DMA write and count

	move.l	#dma.apwr<<16+dma.adwr,d0 ; write sector(s)
	bsr.l	ac_cmdl

acw_exit
;;;;;	     move.w  (sp)+,sr
	move.l	(sp)+,d3
	tst.l	d0
	rts
	page


; Physical access routines

;+++
; This routine sends first byte of command to an ACSI device
;
;	d0 cr	command (lsb) / error code
;	d3 c  u four bytes of parameters
;	d7 c  p lsw drive number (1 to 8)
;	a3 c  p winchester linkage block
;
;	status return 0 OK, or ERR.NC
;
;---
ac_cmds
	move.l	d7,-(sp)

	tst.w	hdl_paus(a3)		 ; pause?
	bge.s	acs_sdo

acs_pause
	btst	#mfp..tai,mfp_tapi	 ; wait for timeout
	beq.s	acs_pause
	move.b	#mfp.hdpc,mfp_hdpi	 ; interrupt not pending now
	sf	hdl_paus(a3)		 ; ... no pause now

acs_sdo
	move.l	(sp),d7
	lsl.l	#3,d3
	add.w	d7,a3
	or.b	hdl_unit-1(a3),d3	 ; unit number
	ror.l	#3,d3
	lsl.b	#3,d0
	or.b	hdl_targ-1(a3),d0	 ; target number
	ror.b	#3,d0			 ; drive number
	sub.w	d7,a3

	move.l	d0,d7

	btst	#mfp..dmi,mfp_dmi	 ; go straight there if we can
	bne.s	acs_wgo

	move.w	#200,d0 		 ; 20ms * 100 = 4s
acs_ws500
	swap	d0
	move.b	#$f0,d0 		 ; 240*83us == 20 ms
	jsr	at_wait80
	swap	d0

acs_wstart
	btst	#mfp..dmi,mfp_dmi
	bne.s	acs_wgo
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	acs_wstart
	dbra	d0,acs_ws500		 ; and again

 move.b #$f0,hdl_err(a3)
	move.l	(sp)+,d7
	bra.s	acs_timo		 ; timed out

acs_wgo
	move.l	d7,d0
	move.l	(sp)+,d7

	move.b	#mfp.hdpc,mfp_hdpi	 ; interrupt not pending now

;;;;;	     or.w    #$2700,sr		      ; no interrupts now, used to be nec

	move.w	#dma.acmd,dma_mode	 ; first byte of command
	swap	d0			 ; ####
	move.w	#dma.adat,d0		 ; ####
	move.l	d0,dma_data		 ; set data and next mode ####
	moveq	#0,d0
	rts

acs_timo
	moveq	#err.nc,d0
	rts

;+++
; This routine sends the 4 parameter bytes of command to an ACSI device
;
;	d0  r	error code
;	d3 c  p four bytes of parameters
;	d7 c  p lsw drive number (1 to 8)
;	a3 c  p winchester linkage block
;
;	status return 0 OK, or ERR.NC
;
;---
ac_cmdp
acc.reg reg	d2/d3/d4
	movem.l acc.reg,-(sp)

	moveq	#3,d2

	move.w	#100,d4 		 ; 20ms * 100 = 2 s

acc_ploop
	btst	#mfp..hdi,mfp_hdpi	 ; wait for interrupt
	bne.s	acc_pnext

acc_pw50
	moveq	#$fffffff0,d0		 ; 240*83us == 20 ms
	jsr	at_wait80

acc_pwait
	btst	#mfp..hdi,mfp_hdpi	 ; wait for interrupt
	bne.s	acc_pnext
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	acc_pwait
	dbra	d4,acc_pw50		 ; and again

 move.b #$f4,hdl_err(a3)
 sub.b	d2,hdl_err(a3)
	bra.s	acc_timo		 ; timed out

acc_pnext
	move.b	#mfp.hdpc,mfp_hdpi	 ; interrupt not pending now
	rol.l	#8,d3			 ; next parameter
	move.w	d3,d0
	swap	d0
	move.w	#dma.adat,d0
	move.l	d0,dma_data		 ; set data / next mode

	move.w	#500,d4 		 ; 20ms * 500 = 10 s for next byte
	dbra	d2,acc_ploop

acc_ok
	moveq	#0,d0
acc_exit
	movem.l (sp)+,acc.reg
	tst.l	d0
	rts

acc_timo
	moveq	#err.nc,d0		 ; timed out
	bra.s	acc_exit

;+++
; This routine sends last byte of command to an ACSI device and sets dma mode
;
;	d0 cr	final mode / error code
;	d7 c  p lsw drive number (1 to 8)
;	a3 c  p winchester linkage block
;
;	status return 0 OK, or ERR.NC
;
;---
ac_cmdl
	movem.l acc.reg,-(sp)
	move.l	d0,d2			 ; keep mode safe     ####

	btst	#mfp..hdi,mfp_hdpi	 ; wait for interrupt
	bne.s	acc_last

	moveq	#100,d4 		 ; 20ms * 100 = 2 s
acc_lw50
	moveq	#$fffffff0,d0		 ; 240*83ms == 20 ms
	jsr	at_wait80
acc_lwait
	btst	#mfp..hdi,mfp_hdpi	 ; wait for interrupt
	bne.s	acc_last
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	acc_lwait
	dbra	d4,acc_lw50

 move.b #$15,hdl_err(a3)
	bra.s	acc_timo		 ; timed out

acc_last
	swap	d2			 ; set mode for last byte of command
	move.w	d2,dma_mode
	move.b	#mfp.hdpc,mfp_hdpi
	clr.w	d2			 ; ####
	swap	d2			 ; ####
	move.l	d2,dma_data		 ; set data (zero) and next mode
	bra	acc_ok

;+++
; This routine reads the ACSI command status
;
;	d0  r	error code
;	a3 c  p winchester linkage
;
;	status return 0 OK, ERR.MCHK or >0 status error
;
;---
ac_statr
acs.reg reg	d1
	movem.l acs.reg,-(sp)
	move.w	#dma.adat,dma_mode	 ; read data byte
	move.w	dma_data,d0
	and.l	#%00011110,d0		 ; standard bits
	beq.s	ars_exit
 move.b  d0,hdl_err(a3)
	cmp.b	#scc.cond,d0		 ; check condition?
	bne.s	ars_exit		 ; ... no

; WOW here we go recursive!!

ars.reg reg	d3/a1
	movem.l ars.reg,-(sp)
	move.w	hdl_paus(a3),d0
	or.b	#4,d0			 ; ensure a pause
	st	hdl_paus(a3)		 ; pause set
	jsr	at_wait80		 ; set up pause

	moveq	#scc.reqs,d0
	moveq	#hdl.errs,d3
	lea	hdl_errs(a3),a1
	cmp.l	#$1000000,a1		 ; in DMA range?
	blo.s	ars_pdo
	move.l	sms.128kb,a1		 ; ... no, use 128 k buffer
	sub.l	d3,a1			 ; in the underrun area!!!

ars_pdo
	bsr.l	aci_do			 ; command to read inf
	bne.s	ars_mchk

	bsr.l	ac_stat 		 ; wait for status at end of command
	bne.s	ars_mchk
	move.b	scs_key(a1),d0		 ; the real error
	beq.s	ars_done		 ; ... none!!
	subq.w	#sck.recv,d0
	beq.s	ars_done		 ; ... recovered
	addq.w	#sck.recv,d0
	bra.s	ars_done

ars_mchk
	moveq	#err.mchk,d0
ars_done
	movem.l (sp)+,ars.reg

ars_exit
	move.l	d0,-(sp)
	move.w	hdl_paus(a3),d0
	beq.s	acs_ex0
	st	hdl_paus(a3)		; pause set
	jsr	at_wait80		; set up pause

acs_ex0
	move.l	(sp)+,d0
	movem.l (sp)+,acs.reg
;+++
; Reset normal mode at end of operation (automatic at end of AC_STAT)
;
; status according to D0
;---
ac_nmode
	move.w	#dma.fstt,dma_mode	 ; fdc mode
	tst.l	d0
	rts

;+++
; Reset bus and normal mode after timeout
;
; status and D0: ERR.NC
;---
ac_reset
	moveq	#err.nc,d0
	bra.s	ac_nmode
	end
