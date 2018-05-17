; Initialise SMSQ  V2.02     1994  Tony Tebby
; 2003-01-22	2.03	use sms_dlan to copy default language (wl)

	section base

	xdef	smsq_base

	xref	init_vec		 ; initialise vector area
	xref	init_trp		 ; initialise traps
	xref	init_sys		 ; initialise system variables
	xref	init_exrv		 ; initialise exception redir vector
	xref	init_sbst		 ; initialise SBASIC stub
	xref	init_ext		 ; initialise extensions

;sbtrns       xref    mem_atpa
	xref	mem_achp

	xref.l	smsq_vers
	xref	smsq_end

	include 'dev8_keys_sys'
	include 'dev8_keys_jcbq'
	include 'dev8_keys_chp'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_config_keys'

	include 'dev8_mac_assert'

smsq_base
	lea	ini_sstk,a7		 ; set ssp
	move.l #$20400,-(sp) #####
	bsr.l blat	     #####
	addq.l #4,sp

	bra.l	sms_reset		 ; reset

	section init

sms_reset
	move.l	sms.usetop,d7		 ; top of usable memory
	tst.l	sms.framt		 ; fast RAM installed?
	bne.s	sms_init		 ; ... yes, ignore request
	cmp.l	a4,d7			 ; requested above useable top
	bls.s	sms_init		 ; ... yes, ignore request
	move.l	a4,d7			 ; set requested RAMTOP

sms_init
	jsr	smsi_qmon	***********
	moveq	#0,d0
	lea	$20000,a0
	move.b	ini_ouch,d1		 ; save ouch status

smsi_cll
	move.l	d0,(a0)+		 ; clear low memory
	cmp.l	d7,a0
	blt.s	smsi_cll

	move.b	d1,ini_ouch

	move.l #$20400,-(sp) #####
	bsr.l blat	     #####
	jsr	init_vec		 ; initialise vector area
	bsr.l blat	     #####
	jsr	smsi_qmon	***********
	jsr	init_trp		 ; initialise TRAP vectors
	bsr.l blat	     #####
; jsr	  smsi_qmon	  ***********
	jsr	init_sys		 ; initialise system vars
	bsr.l blat	     #####
; jsr	  smsi_qmon	  ***********
	jsr	init_exrv		 ; initialise exception redirection vec
	bsr.l blat	     #####
; jsr	  smsi_qmon	  ***********
	jsr	init_sbst		 ; initialise SuperBASIC stub
	bsr.l blat	     #####
; jsr	  smsi_qmon	  ***********

; system initialisation complete

	move.l	#sb.jobsz+jcb_end,d1	 ; set up job

;sbtrns      jsr     mem_atpa		      ; starting here, if it ever does
; if this is changed back, look for all ;sbtrns
	jsr	mem_achp		 ; starting here, if it ever does
	bsr.l blat	     #####

	lea	chp.len(a0),a2
	move.w	#(jcb_end-chp.len)/4-1,d0
smsi_jclear
	clr.l	(a2)+
	dbra	d0,smsi_jclear

	move.l	sys_jbtb(a6),a1 	 ; job table
	move.l	a0,(a1) 		 ; job 0
	move.l	a1,sys_jbpt(a6) 	 ; current job
	move.b	#32,jcb_pinc(a0)	 ; priority 32
	move.l	sys_ertb(a6),jcb_exv(a0) ; exception redirection table
	lea	jcb_end(a0),a6
	lea	sb.jobsz(a6),a1
	move.l	a1,usp

	bsr.l blat	     #####
; jsr	  smsi_qmon	  ***********
	move.l	(sp)+,d0     #####
	move.w	#$0700,sr		 ; go, but no interrupts yet!!
	move.l	d0,-(sp)     #####
smsi_job

	bsr.l blatu	     #####
	jsr	init_ext		 ; initialise (SBASIC and) extensions
	bsr.l blatu	     #####

	trap	#0
	move.w	sms_dlan+sms.conf,d1	 ; default language
	moveq	#0,d2
	moveq	#sms.lset,d0		 ; set it
	trap	#do.smsq
	moveq	#0,d1			 ; default tra
	moveq	#0,d2
	moveq	#sms.trns,d0
	trap	#do.smsq

	move.l	sms.sysb,a0
	clr.w	sys_chtg(a0)		 ; start channels at 0,0
	moveq	#0,d7
	move.b	sms_bwin+sms.conf,d7
	swap	d7
	move.b	sms_bflp+sms.conf,d7
	move.l	sms.sbjob,a0
	and.w	#$dfff,sr		 ; user mode

	jmp	(a0)			 ; start job with config in d7
					 ; and a0 <> 0

smsi_qmon
	tst.b	ini_ouch
	beq.s	smsi_rts
	lea	smsq_base-4,a0

smsi_next
	move.l	(a0),d0
	beq.s	smsi_rts
	move.l	d0,a0
	cmp.l	#'QMON',8(a0)		 ; is it QMON?
	bne.s	smsi_next		 ; ... no
	jmp	$c(a0)			 ; call qmon
smsi_rts
	rts
blatu
	tst.b	ini_ouch
	bgt.s	blatx
	rts

blat
	tst.b	ini_ouch
	beq.s	blat_rts
	blt.s	x_flash
blatx
	cmp.b	#1,ini_ouch ; only produce blats on ini_ouch = 1
	bne.s	blat_rts
	movem.l d0/a4,-(sp)
	move.l	$0c(sp),a4
	move.l	#$ffff0000,(a4)
	addq.l	#4,$0c(sp)
	move.l	#4000000,d0
	subq.l	#1,d0
	bgt.s	*-2
	movem.l (sp)+,d0/a4
blat_rts
	rts

; This flash routine sends the net low for one period, then high for one period.
; This is repeated a4 times and then there are two periods low.
; The sequence is repeated once again and followed by a double pause.

	include 'dev8_smsq_qxl_keys'

x_flash
; rts
	movem.l d0/d1,-(sp)
	addq.l	#1,$0c(sp)		; increment
	move.l	$0c(sp),d0
	bsr.s	flash1			; flash once
	move.l	$0c(sp),d0
	bsr.s	flash1			; and again
	bsr.s	flashidle		; extra idel
	movem.l (sp)+,d0/d1
	rts

flashlp
	tst.b	qxl_netl		 ; net low
	bsr.s	flashp
	tst.b	qxl_neth		 ; net high
	bsr.s	flashp
flash1
	subq.b	#1,d0
	bcc.s	flashlp 		 ; ... and repeat

	tst.b	qxl_netl		 ; idle low
flashidle
	bsr.s	flashp
	nop
flashp
	move.l	#1000000,d1		 ; 1,000,000 cycles for about .2 sec
	move.l	#300000,d1		; 1,000,000 cycles for about .2 sec
	subq.l	#1,d1
	bne.s	*-2
	rts




	end
