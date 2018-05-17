; DV3 SCSI specific routines    1990	  Tony Tebby  QJUMP

	section dv3

	xdef	sc_ckrdy		 ; check drive ready
	xdef	sc_lock 		 ; lock door
	xdef	sc_unlock		 ; unlock door
	xdef	sc_ststp		 ; start / stop
	xdef	sc_cmdn 		 ; command, no parameters
	xdef	sc_cmdr 		 ; command to read sector
	xdef	sc_cmdw 		 ; command to write sector
	xdef	sc_cmdi 		 ; command to read information
	xdef	sc_statr		 ; read status of SCSI device
	xdef	sc_nmode		 ; reset normal mode
	xdef	sc_reset		 ; reset after timeout

	xref	sc_wait80
	xref	sc_p20m

	xref	sc_statrd

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_tt'
	include 'dev8_keys_err'
	include 'dev8_keys_scsi'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

;+++
; Lock the door
;
;	d0 c  p
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return arbitrary
;---
sc_lock
scl.reg reg	d0/d3/a2
	movem.l scl.reg,-(sp)
	lea	hdl_remd(a3),a2 	 ; offset tables
	moveq	#0,d0
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d0 ; target number
	move.b	hdl_nlok-hdl_remd(a2,d0.l),d3 ; one more partition locked
	addq.b	#1,hdl_nlok-hdl_remd(a2,d0.l)
	tst.b	d3			 ; first one?
	bne.s	scl_exit		 ; ... no
	moveq	#scc.lock,d3		 ; lock the door
	bra.s	scl_check
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
sc_unlock
	movem.l scl.reg,-(sp)
	lea	hdl_remd(a3),a2 	 ; offset tables
	moveq	#0,d0
	move.b	hdl_targ-hdl_remd-1(a2,d7.w),d0 ; target number
	subq.b	#1,hdl_nlok-hdl_remd(a2,d0.l) ; one fewer partitions locked
	bgt.s	scl_exit		 ; ... still some locked
	clr.b	hdl_nlok-hdl_remd(a2,d0.l) ; safety?
	moveq	#scc.unlk,d3		 ; unlock the door
scl_check
	add.w	d0,a2
	moveq	#scc.lkun,d0		 ; lock or unlock

	tst.b	(a2)			 ; removable?
	beq.s	scl_exit		 ; ... no
;;; the following code is not required if ckwp can determine if removable
;;;	   bmi.s   scl_do		    ; ... yes
;;;	   bsr.s   sc_cmdh		    ; don't know
;;;	   seq	   (a2) 		    ; set flag so I do know
;;;	   bra.s   scl_exit
;;;scl_do
	bsr.s	sc_cmdh

scl_exit
	movem.l (sp)+,scl.reg
	rts

;+++
; Start / Stop the drive
;
;	d3 c  p 4 start/stop command bytes / zero
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return zero
;---
sc_ststp
	moveq	#scc.stst,d0		 ; start / stop
	bsr.s	sc_cmdh
	beq.s	ass_rts
	moveq	#scc.stst,d0
	bsr.s	sc_cmdh 		 ; try again (Syquest)
	moveq	#0,d0
ass_rts
	rts

;+++
; Check Ready Status
;
;	d0  r	0 ready, +1 changed, -ve bad
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
sc_ckrdy
sck.reg reg	d2/d3
	movem.l sck.reg,-(sp)

	add.w	d7,a3
	moveq	#0,d0
	move.b	hdl_targ-1(a3),d0	 ; target number
	sub.w	d7,a3
	add.l	d0,a3
	tst.b	hdl_remd(a3)		 ; removeable?
	sub.l	d0,a3
	beq.s	sck_ok			 ; not removeable, do not check

sck.ntry equ	3-1
	moveq	#sck.ntry,d2		 ; try three times

sck_loop
	moveq	#0,d3
	moveq	#scc.test,d0		 ; test drive
	bsr.s	sc_cmdn
	dble	d2,sck_loop
	bne.s	sck_bad

sck_done
	subq.w	#sck.ntry,d2		 ; any error?
	bne.s	sck_chnge

sck_ok
	moveq	#0,d0
sck_exit
	movem.l (sp)+,sck.reg
	rts

sck_bad
	moveq	#err.mchk,d0
	bra.s	sck_exit

sck_chnge
	moveq	#1,d0			 ; medium changed?
	bra.s	sck_exit

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
sc_cmdh
	assert	hdl.hold,-1
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll
	bsr.s	sc_cmdn
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	tst.l	d0
	rts

;+++
; Command to Send SCSI Command no Data, and read status.
;
;	d0 cr	command / status
;	d3 c  p four bytes of parameters
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard or >0 status error
;---
sc_cmdn
scn.reg reg	d2/d3
	movem.l scn.reg,-(sp)
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll

	move.l	d0,d2			 ; action

	bsr.l	sc_cmds 		 ; select
	bne.s	scn_exit

	move.l	d2,d0			 ; action

	bsr.l	sc_cmdp 		 ; send parameters
	bne.s	scn_exit

	bsr.l	sc_statr

scn_exit
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll

	movem.l (sp)+,scn.reg
	tst.l	d0
	rts

;+++ 
; Command to Read SCSI Info - does not read status
;
;	d0 cr	command / status
;	d3 c  p next four bytes
;	d7 c  p drive ID / number
;	a1  r	pointer to buffer
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return standard
;---
sc_cmdi
	lea	hdl_buff(a3),a1 	 ; load into buffer
sci_do
sci.reg reg	d2/d3/a2
	movem.l sci.reg,-(sp)
	move.l	d0,d2
	bsr.l	sc_cmds 		 ; ... select and start
	bne.s	sci_exit

	move.b	#scd.read,scd_ctrl	 ; set dma direction

	lea	scd_addr,a2
	move.l	a1,d0
	movep.l d0,scd_addr-scd_addr(a2)
	moveq	#$70,d0 		 ; arbitrary length
	movep.l d0,scd_cnt-scd_addr(a2)

	move.l	d2,d0
	bsr.l	sc_cmdp 		 ; send parameters
	bne.s	sci_exit

	bsr.l	sc_dmar

;&& jsr sc_p20m

sci_exit
	movem.l (sp)+,sci.reg
	tst.l	d0
	rts

;+++
; Command to read sectors from an SCSI device
;
;	d2 c  p number of sectors (word, 1-255 sectors)
;	d3 c  p block position (21 bits zero extended / 32 bits)
;	d7 c  p drive ID / number
;	a1 c  p dma address
;	a3 c  p winchester linkage block
;
;	status return 0 or ERR.NC
;
;---
sc_cmdr
scr.reg reg	d2/d3/a2
	movem.l scr.reg,-(sp)
	cmp.l	#$001fffff,d3		 ; group 1 read?
	bhi.s	scr_group1

	lsl.l	#8,d3
	move.b	d2,d3			 ; four bytes of params
	moveq	#scc.read,d0 ; for debug
	bsr.l	sc_cmds 		 ; select
	bne.s	scr_exit

	move.b	#scd.read,scd_ctrl	 ; set dma direction

	lea	scd_addr,a2
	move.l	a1,d0
	movep.l d0,scd_addr-scd_addr(a2)
	moveq	#0,d0
	move.b	d2,d0
	lsl.w	#8,d0
	add.l	d0,d0			 ; number of bytes
	movep.l d0,scd_cnt-scd_addr(a2)

	moveq	#scc.read,d0		 ; read sector command
	bsr.l	sc_cmdp 		 ; send parameters
	beq.s	scr_dmar
	bne.s	scr_exit

scr_group1
	and.l	#$000000ff,d2		 ; clear out top end
	exg	d2,d3			 ; for 10 byte cmd, unit with count

	moveq	#scc.read,d0 ; for debug
	bsr.l	sc_cmds 		 ; select
	bne.s	scr_exit

	move.b	#scd.read,scd_ctrl	 ; set dma direction

	lea	scd_addr,a2
	move.l	a1,d0
	movep.l d0,scd_addr-scd_addr(a2)
	moveq	#0,d0
	move.b	d3,d0
	lsl.w	#8,d0
	add.l	d0,d0			 ; number of bytes
	movep.l d0,scd_cnt-scd_addr(a2)

	moveq	#scc.read1,d0		 ; group 1 read sector command
	bsr.l	sc_cmdp10		 ; send parameters
	bne.s	scr_exit

scr_dmar
	bsr.l	sc_dmar

;&& jsr sc_p20m

scr_exit
	movem.l (sp)+,scr.reg
	tst.l	d0
	rts

;+++
; Command to write sectors to an SCSI device
;
;	d2 c  p number of sectors (word, 0=256)
;	d3 c  p block number
;	d7 c  p drive ID / number
;	a1 c  p dma address
;	a3 c  p winchester linkage block
;
;	status return 0 or ERR.NC
;
;---
sc_cmdw
scw.reg reg	d2/d3/a2
	movem.l scw.reg,-(sp)

	cmp.l	#$001fffff,d3		 ; group 1 read?
	bhi.s	scw_group1

	lsl.l	#8,d3			 ; set parameters
	move.b	d2,d3
	moveq	#scc.writ,d0 ; for debug
	bsr.s	sc_cmds 		 ; select
	bne.s	scr_exit

	move.b	#scd.writ,scd_ctrl	 ; set dma direction

	lea	scd_addr,a2
	move.l	a1,d0
	movep.l d0,scd_addr-scd_addr(a2)
	moveq	#0,d0
	move.b	d2,d0
	lsl.w	#8,d0
	add.l	d0,d0			 ; number of bytes
	movep.l d0,scd_cnt-scd_addr(a2)

	moveq	#scc.writ,d0		 ; write sector command
	bsr.l	sc_cmdp 		 ; send parameters
	beq.s	scw_dmaw
	bne.s	scr_exit

scw_group1
	and.l	#$000000ff,d2		 ; clear out top end
	exg	d2,d3			 ; for 10 byte cmd, unit with count

	moveq	#scc.writ,d0 ; for debug
	bsr.s	sc_cmds 		 ; select
	bne.s	scr_exit

	move.b	#scd.writ,scd_ctrl	 ; set dma direction

	lea	scd_addr,a2
	move.l	a1,d0
	movep.l d0,scd_addr-scd_addr(a2)
	moveq	#0,d0
	move.b	d3,d0
	lsl.w	#8,d0
	add.l	d0,d0			 ; number of bytes
	movep.l d0,scd_cnt-scd_addr(a2)

	moveq	#scc.writ1,d0		 ; write sector command
	bsr.l	sc_cmdp10		 ; send parameters
	bne.s	scr_exit

scw_dmaw
	bsr.l	sc_dmaw

scw_exit
	movem.l (sp)+,scw.reg
	tst.l	d0
	rts
	page

; Physical access routines

;+++
; This routine selects the SCSI device
;
;	d0 cr	command (lsb) / error code
;	d3 c  p four bytes of parameters
;	d7 c  p drive ID / number
;	a3 c  p winchester linkage block
;
;	status return 0 OK, or ERR.NC
;---
sc_cmds
; move.b d0,hdl_info+1(a3) ; save command
; move.l d3,hdl_info+2(a3)
; sf	 hdl_info+6(a3)
; btst #0,hdl_paus-hdl_thge+1(a3)
; beq.s cmds
; pea	cm
; bsr.s sc_fail
;cmds

	moveq	#$fffffff0,d0		 ; 240*83ms == 20 ms
	jsr	sc_wait80

scc_fwait
	moveq	#scs.bsy,d0
	and.b	scs_bstt,d0		 ; selected still?
	beq.s	scc_sel 		 ; ... no
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	scc_fwait
	bsr.l	sc_resbus		 ; ... yes, reset bus

	moveq	#$fffffff0,d0		 ; 240*83ms == 20 ms
	jsr	sc_wait80

scc_fwait2
	moveq	#scs.bsy,d0
	and.b	scs_bstt,d0		 ; selected still?
	beq.s	scc_sel 		 ; ... no
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	scc_fwait2
; move.b #1,hdl_err(a3)
; move.b scs_bstt,hdl_info+6(a3)
; pea bb
; bsr.s sc_fail
	bra.l	sc_reset		 ; ... yes, reset
;cm dc.b 0,10,' command',$d,$a
;bb dc.b 0,12,' bus busy ',$d,$a
;
; xdef sc_fail
; xref sc_debstr
; xref sc_debhex
;sc_fail
; pea hdl_info+1(a3)
; move.w #6,-(sp)
; jsr sc_debhex
; jmp sc_debstr

scc_sel
	move.l	d6,-(sp)
	lsl.l	#3,d3
	add.w	d7,a3
	moveq	#7,d0
	and.b	hdl_unit-1(a3),d0	 ; unit number
	or.b	d0,d3
	ror.l	#3,d3
	moveq	#7,d0
	and.b	hdl_targ-1(a3),d0	 ; target number
	sub.w	d7,a3
	moveq	#0,d6
	bset	d0,d6

	moveq	#0,d0
	move.b	d0,scs_tcmd		 ; tcmd (Profibuch clears all)

	move.b	d6,scs_data		 ; select device
	move.l	(sp)+,d6

	move.b	#scs.wena,scs_icmd	 ; output the select bit
	move.b	#scs.sel,scs_icmd	 ; select

	moveq	#50,d0			 ; 50*20=1s
scc_stimer
	swap	d0
	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80
	swap	d0

scc_swait
	btst	#scs..bsy,scs_bstt	 ; busy yet?
	bne.s	scc_sok 		 ; ... yes
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timed out?
	beq.s	scc_swait

	dbra	d0,scc_stimer		 ; try again
; move.b #2,hdl_err(a3)
; move.b scs_bstt,hdl_info+6(a3)
; pea sf
; bsr.s sc_fail
	bra.l	sc_timeout		 ; oops, back to normal mode
;sf dc.b 0,16,' select failed',$d,$a

scc_sok
	move.b	#scs.wena,scs_icmd	 ; write data enabled

	moveq	#0,d0			 ; selected now
	rts

;+++
; This routine sends the command and 5 parameter bytes to an SCSI device
;
;	d0 cr	command / error code
;	d3 c  p 4 bytes of parameters
;	d7 c  p drive ID / number
;	a3 c  p winchester linkage block
;
;	status return 0 OK, or ERR.NC
;
;---
sc_cmdp
scc.reg reg	d3/d4
	movem.l scc.reg,-(sp)

	moveq	#5,d4

	move.b	#scs.cmd,scs_tcmd	 ; command phase

scc_ploop
	move.b	d0,scs_data		 ; data byte

	moveq	#100,d0 		 ; 100*20ms = 2 s
scc_ptimer
	swap	d0
	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80
	swap	d0

scc_pwait
	btst	#scs..req,scs_bstt	 ; request?
	bne.s	scc_pack		 ; ... yes
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	scc_pwait

	dbra	d0,scc_ptimer
scc_perr
; move.b #$15,hdl_err(a3)
; sub.b  d4,hdl_err(a3)
; move.b scs_bstt,hdl_info+6(a3)
; pea pf
; bsr.l sc_fail
	bsr.l	sc_reset
	bra.s	scc_exit
;pf dc.b 0,17,' command failed',$d,$a,0

scc_pack
	move.b	#scs.wack,scs_icmd	 ; write acknowledge

	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80

scc_pawait
	btst	#scs..req,scs_bstt	 ; request?
	beq.s	scc_pnack		 ; ... no
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	scc_pawait
	bra.s	scc_perr

scc_pnack
	move.b	#scs.wena,scs_icmd	 ; ... end

	rol.l	#8,d3			 ; next parameter
	move.b	d3,d0			 ; set data
	clr.b	d3			 ; gone now
	dbra	d4,scc_ploop

	move.b	d0,scs_icmd		 ; d0.b is 0

scc_ok
	moveq	#0,d0
scc_exit
	movem.l (sp)+,scc.reg
	tst.l	d0
	rts

;+++
; This routine sends a group 1 command and 9 parameter bytes to an SCSI device
;
;	d0 cr	command / error code
;	d2 c  s address (p2-p5)
;	d3 c  s ms3 bits unit (p1), lsbyte = length (p8)
;	d7 c  p drive ID / number
;	a3 c  p winchester linkage block
;
;	status return 0 OK, or ERR.NC
;
;---
sc_cmdp10
sc10.reg reg	 a1/d4
sc10.frame equ	 10
	movem.l sc10.reg,-(sp)
	move.l	sp,a1
	sub.w	#sc10.frame,sp
	clr.b	-(a1)			 ; p9 = control
	move.b	d3,-(a1)		 ; p8 = 1 to 255 sectors
	clr.w	-(a1)			 ; p7 = 0 ,p6 = 0
	move.l	d2,-(a1)		 ; p5 - p2 = block number
	move.b	d0,d3
	rol.l	#8,d3
	move.w	d3,-(a1)		 ; p1 = LUN, p0 = command

	moveq	#9,d4

	move.b	#scs.cmd,scs_tcmd	 ; command phase

sc10_ploop
	move.b	(a1)+,scs_data		 ; data byte

	moveq	#100,d0 		 ; 100*20ms = 2 s
sc10_ptimer
	swap	d0
	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80
	swap	d0

sc10_pwait
	btst	#scs..req,scs_bstt	 ; request?
	bne.s	sc10_pack		  ; ... yes
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	sc10_pwait

	dbra	d0,sc10_ptimer
sc10_perr
; move.b #$15,hdl_err(a3)
; sub.b  d4,hdl_err(a3)
; move.b scs_bstt,hdl_info+6(a3)
; pea pf
; bsr.l sc_fail
	bsr.l	sc_reset
	bra.s	sc10_exit
;pf dc.b 0,17,' command failed',$d,$a,0

sc10_pack
	move.b	#scs.wack,scs_icmd	 ; write acknowledge

	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80

sc10_pawait
	btst	#scs..req,scs_bstt	 ; request?
	beq.s	sc10_pnack		  ; ... no
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	sc10_pawait
	bra.s	sc10_perr

sc10_pnack
	move.b	#scs.wena,scs_icmd	 ; ... end

	dbra	d4,sc10_ploop

	moveq	#0,d0
	move.b	d0,scs_icmd		 ; d0.b is 0

sc10_exit
	add.w	#sc10.frame,sp
	movem.l (sp)+,sc10.reg
	tst.l	d0
	rts

;+++
; This routine sets dma read
;
;	d0 r	error code
;	d7 c  p drive ID / number
;	a3 c  p winchester linkage block
;
;	status return OK
;
;---
sc_dmar
	jsr	sms.cinvd		 ; invalidate data cache
	assert	scs.rena,0
	moveq	#0,d0

	move.b	d0,scs_icmd		 ; set bus direction

	move.b	#scs.din,scs_tcmd	 ; data phase
	tst.b	scs_rsti		 ; clear errors
	move.b	#scs.dmod,scs_mode	 ; dma mode
	move.b	#scd.dmar,scd_ctrl	 ; enable dma
	tst.b	scd_ctrl		 ; enable dma !!!!
	move.b	d0,scs_dmai		 ; dma input

	rts

;+++
; This routine sets dma write
;
;	d0 r	error code
;	d7 c  p drive ID / number
;	a3 c  p winchester linkage block
;
;	status return OK
;
;---
sc_dmaw
	assert	scs.dout,0
	moveq	#0,d0

	move.b	#scs.wena,scs_icmd	 ; set bus direction

	move.b	d0,scs_tcmd		 ; data phase
	tst.b	scs_rsti		 ; clear errors
	move.b	#scs.dmod,scs_mode	 ; dma mode
	move.b	#scd.dmaw,scd_ctrl	 ; enable dma
	tst.b	scd_ctrl		 ; enable dma !!!!
	move.b	d0,scs_dmas		 ; dma send

	rts

; read status / message byte

scs_rstt
	moveq	#100,d0 		 ; 2 s

scs_rsxwait
	swap	d0
	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80
	swap	d0

scs_rswait
	btst	#scs..req,scs_bstt	 ; request?
	bne.s	scs_rsack		 ; ... yes
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	scs_rswait
	dbra	d0,scs_rsxwait		 ; try again

; move.b scs_bstt,hdl_info+6(a3)
; pea sb
; bsr.l sc_fail
	bra.l	sc_timeout
;sb dc.b 0,17,' no status byte',$d,$a,0



scs_rsack
	move.b	scs_data,d0		 ; data byte
	move.b	#scs.rack,scs_icmd	 ; read acknowledge

	swap	d0
	move.b	#$f0,d0 		 ; 240*83ms == 20 ms
	jsr	sc_wait80
	swap	d0

scs_rsawait
	btst	#scs..req,scs_bstt	 ; request?
	beq.s	scs_rsnack		 ; ... no
	btst	#mfp..tai,at_mfp2+mfp_pnda	 ; timeout?
	beq.s	scs_rsawait

; move.b scs_bstt,hdl_info+6(a3)
; pea ss
; bsr.l sc_fail
	bra.l	sc_timeout
;ss dc.b 0,20,' status byte stuck',$d,$a

scs_rsnack
	move.b	#scs.rena,scs_icmd	 ; ... end
	cmp.b	d0,d0			 ; status byte received
	rts

;+++
; This routine reads the SCSI command status.
;
;	d0  r	error code
;	a3 c  p winchester linkage
;
;	status return 0 OK, ERR.NC or ERR.MCHK or >0 scsi error
;
;---
sc_statr
scs_sttr
scs.reg reg	d1
	movem.l scs.reg,-(sp)
	move.b	#scs.cmod,scs_mode	 ; normal (command mode)

	move.b	#scs.rena,scs_icmd	 ; read

	move.b	#scs.stt,scs_tcmd	 ; status phase
	bsr	scs_rstt
	bne.l	scs_mchk
	move.b	d0,d1

	move.b	#scs.msg,scs_tcmd	 ; message phase
	bsr	scs_rstt
	bne.s	scs_mchk
	moveq	#0,d0
	move.b	d1,d0			 ; check the status
	beq.s	scs_pchk

	and.b	#%00011110,d1		 ; standard bits
	subq.b	#scc.cond,d1		 ; check condition?
	bne.s	scs_done		 ; ... no

; move.b d0,hdl_info+6(a3)
; pea es
; bsr.l sc_fail

; WOW here we go recursive!!

srs.reg reg	d3/a1
	movem.l srs.reg,-(sp)
	moveq	#scc.reqs,d0
	moveq	#hdl.errs,d3
	lea	hdl_errs(a3),a1

	bsr.l	sci_do			 ; command to read inf
	bne.s	srs_mchk

	jsr	sc_statrd		 ; wait for status at end of command
	bne.s	srs_mchk

	move.b	scs_key(a1),d0		 ; the real error

; move.b d0,hdl_info+6(a3)
; pea ek
; bsr.l sc_fail
; tst.b   d0

	beq.s	srs_done		 ; ... none!!
	subq.w	#sck.recv,d0
	beq.s	srs_done		 ; ... recovered
	addq.w	#sck.recv,d0
	bra.s	srs_done

;es dc.b 0,15,' error status',$d,$a,0
;ek dc.b 0,12,' error key',$d,$a

srs_mchk
	moveq	#err.mchk,d0
srs_done
	movem.l (sp)+,srs.reg
	bne.s	scs_done

scs_pchk
	moveq	#0,d0
;	 moveq	 #scs.sper,d0		  ; parity error?
;	 and.b	 scs_stat,d0
;	 beq.s	 scs_done
;	 moveq	 #sck.abrt,d0		  ; return aborted command
	bra.s	scs_done

scs_mchk
; move.b #$81,hdl_err(a3)
	moveq	#err.mchk,d0

scs_done
	movem.l (sp)+,scs.reg


;+++
; Reset normal mode at end of operation (automatic at end of sc_STAT)
; This code is duplicated in sc_stat
; status according to D0
;---
sc_nmode
	move.l	d0,-(sp)
	bclr	#scd..ena,scd_ctrl	 ; disable dma
	moveq	#0,d0
	move.b	d0,scd_ctrl		 ; clear dma
	assert	scs.nop,scs.idle,0
	move.b	d0,scs_icmd		 ; and icmd and mode
	move.b	d0,scs_mode
	tst.b	scs_rsti		 ; clear interrupts
	move.l	(sp)+,d0
	rts

sc_timeout
	moveq	#-1,d0
	bra.s	sc_nmode

;+++
; Reset bus and normal mode after timout
;
;	status and D0 ERR.NC
;---
sc_reset
	pea	sc_timeout		 ; return to nmode

sc_resbus
	move.b	#scs.rst,scs_icmd	 ; reset

	bsr.s	sc_waitr		 ; wait

	move.b	#scs.nop,scs_icmd	 ; unreset

sc_waitr
	moveq	#24,d0
sc_waitrl
	jsr	sc_p20m 		 ; 20 ms pause
	dbra	d0,sc_waitrl
	rts

	end
