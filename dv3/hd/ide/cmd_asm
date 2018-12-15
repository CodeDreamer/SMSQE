; DV3 IDE Commands	       V3.00    1998	Tony Tebby

	section dv3

	xdef	id_cmd
	xdef	id_cmdw

	xref	id_statw

	xref.l	ide.0
	xref.l	ide.1

	xref.s	ideo.scnt
	xref.s	ideo.sect
	xref.s	ideo.cyll
	xref.s	ideo.cylh
	xref.s	ideo.head
	xref.s	ideo.cmd
	xref.s	ideo.stat
	xref.s	ideo.alt
	
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_mac_assert'

;+++
; This sends a command to an IDE drive and waits for completion
;
;	d0 cr	command / error code
;	d1 c  p sector address
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;	a5  r	base of register block
;
;	status return +ve error
;		      -ve no status (time-out)
;			0 status ok
;---
id_cmdw
	bsr.s	id_cmd		 ; do command
	beq.l	id_statw	 ; ... ok, wait for reply
	rts
;+++
; This sends a command to an IDE drive and waits for completion
;
;	d0 cr	command / error code
;	d1 c  p sector address
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;	a5  r	base of register block
;
;	status return 0 or -ve
;---
id_cmd
idc.reg reg	d1/d3/d6
	movem.l idc.reg,-(sp)
	lea	ide.0,a5		 ; assume primary
	move.w	#hdl_targ-1,d6
	add.w	d7,d6
	move.b	(a3,d6.w),d6		 ; target number 0-3
	roxr.b	#2,d6			 ; primary / secondary in C
	bcc.s	idc_stslave
	lea	ide.1,a5		 ; it is secondary
idc_stslave
	lsr.b	#7-ide..slv,d6		 ; set prmary / slave bit
	or.b	#ide.head,d6		 ; set basic head reg value
	tst.b	ddl_cylhds(a3)		 ; cylinder / head / side format?
	bne.s	idc_sttimer		 ; ... yes, set timer
	or.b	#ide.lba,d6		 ; set basic LBA msb reg value

idc_sttimer
	move.l	hdl_1sec(a3),d3

idc_wait
	assert	ide..busy,7
	tst.b	ideo.stat(a5)		 ; busy?
	bpl.s	idc_cmd 		 ; ... no, look for command

	subq.l	#1,d3			 ; wait
	bgt.s	idc_wait
	bra.s	idc_timeout

idc_cmd
	move.b	d6,ideo.head(a5)

	tst.b	ideo.stat(a5)		 ; this drive busy?
	bpl.s	idc_cmdw		 ; ... no, look for command

	subq.l	#1,d3			 ; wait
	bgt.s	idc_cmd
	bra.s	idc_timeout

idc_cmdw
	btst	#ide..drdy,ideo.stat(a5) ; ready for command?
	bne.s	idc_setadd		 ; ... yes, setup address
	subq.l	#1,d3			 ; wait
	bgt.s	idc_cmdw
	bra.s	idc_timeout

idc_setadd
	move.b	#ide.nint,ideo.alt(a5)	 ; kill the interrupts

	move.b	d2,ideo.scnt(a5)	 ; set sector count

	btst	#ide..lba,d6		 ; lba format?
	bne.s	idc_lba 		 ; ... yes

	move.b	d1,ideo.sect(a5)
	lsr.w	#8,d1
	or.b	d6,d1			 ; set head reg value
	move.b	d1,ideo.head(a5)

	swap	d1
	move.b	d1,ideo.cyll(a5)	 ; lsb of cylinder
	lsr.w	#8,d1
	move.b	d1,ideo.cylh(a5)	 ; msb of cylinder
	bra.s	idc_scmd

idc_lba
	move.b	d1,ideo.sect(a5)	 ; bits 7..0

	lsr.w	#8,d1
	move.b	d1,ideo.cyll(a5)	 ; bits 15..8
	swap	d1
	move.b	d1,ideo.cylh(a5)	 ; bits 23..16
	lsr.w	#8,d1
	or.b	d6,d1			 ; set LBA msb reg value
	move.b	d1,ideo.head(a5)

idc_scmd
	move.w	hdl_1sec(a3),d3 	 ; 1 second timer / 65536 = 15us

	move.b	d0,ideo.cmd(a5) 	 ; do command

idc_wbusy
	assert	ide..busy,7
	tst.b	ideo.stat(a5)		 ; busy?
	dbmi	d3,idc_wbusy		 ; ATA3 spec says 400ns max

	moveq	#0,d0
idc_exit
	movem.l (sp)+,idc.reg
	rts
idc_timeout
	moveq	#err.mchk,d0
	bra.s	idc_exit


	end
