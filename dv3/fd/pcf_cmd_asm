; DV3 PC Compatble FDC send command    V2.00	 1991	Tony Tebby

	section fd

	xdef	fd_cmd			; send arbitrary
	xdef	fd_cmd_spec		; send specify command
	xdef	fd_cmd_sense		; send sense command
	xdef	fd_cmd_recal		; send re-calibrate command
	xdef	fd_cmd_seek		; send seek command
	xdef	fd_cmd_stin		; send step in by 5 tracks command
	xdef	fd_cmd_sint		; send sense interrupt command
	xdef	fd_cmd_rid		; send read id command
	xdef	fd_cmd_rw		; send command to read or write
	xdef	fd_cmd_fmtt		; send format track command

	xref	fd_reset
	xref	fd_fint

	xref.l	fdc_stat
	xref.l	fdc_data

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

fdc.reg reg	d5/d6/a0

*+++
; This routine sends an arbitrary command to the floppy disk controller.
;
;	d2 c  u number of bytes to send
;	a1 c  u pointer to command
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd
	movem.l fdc.reg,-(sp)
	move.b	(a1)+,d1
	bsr.l	fdc_cmds		 ; start the command
fdc_loop
	subq.w	#1,d2
	ble.l	fdc_exit		 ; ... done
	move.b	(a1)+,d1
	bsr.l	fdc_cmdb		 ; command parameter byte
	bra.s	fdc_loop

*+++
; This routine sends a sense command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_sense
	moveq	#fdc.senc,d0		 ; sense
	bra.s	fdc_2byte

*+++
; This routine sends a sense interrupt command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_sint
	moveq	#fdc.sinc,d0
	movem.l fdc.reg,-(sp)
	bsr.l	fdc_cmds
	bra.l	fdc_ok

*+++
; This routine sends a recalibrate command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_recal
	moveq	#fdc.recc,d0
fdc_2byte
	movem.l fdc.reg,-(sp)
	bsr.l	fdc_cmds		 ; start the command
	moveq	#0,d0
	bsr.l	fdc_cmd1		 ; standard first byte - no side
	bra.s	fdc_ok

*+++
; This routine sends a seek command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_seek
	moveq	#fdc.seec,d0
	movem.l fdc.reg,-(sp)
	bsr.l	fdc_cmds		 ; start the command
	moveq	#0,d0
	bsr.l	fdc_cmd1		 ; nearly standard first byte
	move.b	fdl_trak(a3),d0 	 ; byte 2 track
	bra.s	fdc_last

*+++
; This routine sends a step in command to the floppy disk controller.
; Currently it steps to track 5.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_stin
	moveq	#fdc.seec,d0
	movem.l fdc.reg,-(sp)
	bsr.s	fdc_cmds		 ; start the command
	moveq	#0,d0
	bsr.l	fdc_cmd1		 ; nearly standard first byte
	moveq	#5,d0			 ; byte 2 track
	bra.s	fdc_last

*+++
; This routine sends a read sector ID command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_rid
	moveq	#fdc.ridc,d0		 ; read ID
	movem.l fdc.reg,-(sp)
	bsr.s	fdc_cmdsd		 ; start the command
	bsr.l	fdc_cm1s		 ; standard first byte
	bra.s	fdc_ok

*+++
; This routine sends a read or write command to the floppy disk controller.
;
;	d0 cr	command number / status
;	d2 c  p number of sectors
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
*---
fd_cmd_rw
	movem.l fdc.reg,-(sp)
	bsr.s	fdc_cmdsd		 ; start the command
	bsr.l	fdc_cm1s		 ; standard first byte

	move.b	fdl_trak(a3),d0 	 ; byte 2 track
	bsr.l	fdc_cmdb
	move.b	fdl_side(a3),d0 	 ; byte 3 side
	bsr.l	fdc_cmdb
	move.b	fdl_sect(a3),d0 	 ; byte 4 sector
	bsr.s	fdc_cmdb
	move.b	ddf_slflag(a4),d0	 ; byte 5 sector length
	bsr.s	fdc_cmdb
	move.b	fdl_sect(a3),d0 	 ; byte 6 max sector
	add.b	d2,d0
	subq.b	#1,d0
	bsr.s	fdc_cmdb
	moveq	#0,d0			 ; byte 7 gap
	move.b	ddf_slflag(a4),d0
	move.b	fdc_rwgap(pc,d0.w),d0
	bsr.s	fdc_cmdb
	moveq	#$ffffffff,d0		 ; byte 8 $ff
fdc_last
	bsr.s	fdc_cmdb
fdc_ok
	moveq	#0,d0
fdc_exit
	movem.l (sp)+,fdc.reg
	rts

fdc_cmde
	jsr	fd_fint 		 ; kill the command
	addq.l	#4,sp			 ; remove return
	bra.s	fdc_exit

fdc_rwgap dc.b	6,13,27,55,111,223	 ; table of read write gap lengths

;
; start command with MFM flag
;
fdc_cmdsd
	tst.b	ddf_density(a4) 	 ; mfm?
	bne.s	fdc_cmds
	bclr	#fdc..mfm,d0		 ; ... no

; start command

fdc_cmds
	lea	fdc_stat,a0		 ; address of status register
	move.l	fdl_1sec(a3),d5
fdc_busy
	moveq	#fdcs.bsy,d6
	and.b	(a0),d6
	beq.s	fdc_cmdw
	subq.l	#1,d5
	bgt.s	fdc_busy		 ; wait until busy goes away
fdc_reset
	move.l	d0,-(sp)
	jsr	fd_fint 		 ; reset (force interrupt)
	move.l	(sp)+,d0
	bra.s	fdc_cmds

fdc_cmdw
	moveq	#fdcs.bit,d6
	and.b	(a0),d6 		 ; status
	assert	fdc..req,7
	bge.s	fdc_cmdw		 ; not ready
	cmp.b	#fdcs.stt,d6		 ; start of command
	beq.s	fdc_cmdp		 ; ... yes
	bra.s	fdc_reset

; first command byte

fdc_cm1s
	move.b	fdl_side(a3),d0 	 ; side number
	lsl.b	#2,d0			 ; in bit 2
fdc_cmd1
	add.b	fdl_selc(a3),d0 	 ; drive 1 to 4
	subq.b	#1,d0			 ; drive 0 to 3

; send command byte

fdc_cmdb
	moveq	#fdcs.bit,d6
	and.b	(a0),d6 		 ; status
	assert	fdc..req,7
	bge.s	fdc_cmdb		 ; not ready
	cmp.b	#fdcs.cmd,d6		 ; command byte required?
	bne.s	fdc_cmde		 ; ... oops
fdc_cmdp
	move.b	d0,fdc_data-fdc_stat(a0) ; command byte
	rts

;+++
; This routine sends a format track command to the floppy disk controller.
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
;---
fd_cmd_fmtt
	movem.l fdc.reg,-(sp)
	moveq	#fdc.fmtc,d0
	bsr.s	fdc_cmds		 ; start the command
	bsr.s	fdc_cm1s		 ; standard first byte

	move.b	fdf_fslen(a4),d0	 ; byte 2 sector length
	bsr.s	fdc_cmdb
	move.b	fdf_fstrk(a4),d0	 ; byte 3 sectors per track
	bsr.s	fdc_cmdb
	move.b	fdf_fgap(a4),d0 	 ; byte 4 gap
	bsr.s	fdc_cmdb
	moveq	#'0',d0 		 ; byte 5 fill byte
	bra.l	fdc_last

;+++
; This routine sends the specify command
;
;	d0 cr	step rate code (msnibble of ls byte)
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return standard
;
;---
fd_cmd_spec
	movem.l fdc.reg,-(sp)
	swap	d0
	move.b	#fdc.spcc,d0
	bsr	fdc_cmds		 ; start specify command

	swap	d0			 ; step rate
	or.b	#fdc.spc1,d0		 ; + constant bits (max head unload)
	bsr.s	fdc_cmdb

	moveq	#fdc.spc2,d0		 ; no DMA, min head load
	bra.l	fdc_last
	end
