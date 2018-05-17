; Initialise SMSQ  for SMSQmulator V2.04 (c) W. Leerz 2012-2015

; 2003-01-22	2.03	use sms_dlan to copy default language (wl)
; 2015 Jul 27	2.04	removed debug code (blats etc)

; based on
; Initialise SMSQ  V2.02     1994  Tony Tebby

	section base

	xdef	smsq_base

	xref	init_vec		 ; initialise vector area
	xref	init_trp		 ; initialise traps
	xref	init_sys		 ; initialise system variables
	xref	init_exrv		 ; initialise exception redir vector
	xref	init_sbst		 ; initialise SBASIC stub
	xref	init_ext		 ; initialise extensions

	xref	mem_achp

	xref.l	smsq_vers
	xref	smsq_end

	include 'dev8_keys_sys'
	include 'dev8_keys_jcbq'
	include 'dev8_keys_chp'
	include 'dev8_keys_err'
	include 'dev8_keys_java'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_config_keys'

	include 'dev8_mac_assert'

smsq_base
	lea	ini_sstk,a7		 ; set ssp
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
	jsr	init_vec		 ; initialise vector area
	jsr	init_trp		 ; initialise TRAP vectors

	move.l	sms.sysb,a0
	move.l	jva_lkptr,d0		 ; java -> smsqe comm block
	beq.s	rnd_do			 ; ?? a very bad ooops here!
	add.l	#jva_rand,d0		 ; point to mouse area in comm block: mouse pos
	move.l	d0,a2
	move.w	(a2),sys_rand(a0)	 ; random number seed
rnd_do
	jsr	init_sys		 ; initialise system vars
	jsr	init_exrv		 ; initialise exception redirection vec
	jsr	init_sbst		 ; initialise SuperBASIC stub

; system initialisation complete

	move.l	#sb.jobsz+jcb_end,d1	 ; set up job

	jsr	mem_achp		 ; starting here, if it ever does

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
	move.w	#$0700,sr		 ; go, but no interrupts yet!!

smsi_job
	jsr	init_ext		 ; initialise (SBASIC and) extensions
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

	end
