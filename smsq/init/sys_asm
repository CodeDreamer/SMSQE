; Initialise system variables	V2.02	 1994 Tony Tebby

	section init

	xdef	init_sys

	xref	init_wbase
	xref	mem_init

	xref	shd_poll
	xref	sms_rte

	xref.l	th_vers
	xref	th_entry
	xref	th_exec

	xref	gu_achpp

	xref.l	smsq_vers

	include 'dev8_keys_sys'
	include 'dev8_keys_sbt'
	include 'dev8_keys_thg'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
;+++
;	d7 c	size of memory
;	a6  r	base of system vars
;---
init_sys

	clr.b	$18063			 ; MAGIC!!!

	lea	shd_poll,a0
	lea	sms.spoll,a5		 ; set scheduler polling link
	jsr	init_wbase

	lea	sms_rte,a0
	lea	sms.rte,a5		 ; and return
	jsr	init_wbase

	move.l	sms.sysb,a6
  
	jsr	mem_init
 
	move.l	#sysid.sq,(a6)		 ; set sysvar id
	addq.w	#7,sys_rand(a6) 	 ; set random number
	addq.b	#1,sys_nnnr(a6) 	 ; network node number
	addq.w	#3,sys_swtc(a6) 	 ; switch character
	move.w	#sys.polf,sys_polf(a6)	 ; standard polling rate

	move.w	#sys.rdel,sys_rdel(a6)	 ; keyboard timing
	addq.w	#sys.rtim,sys_rtim(a6)

	move.l	sms.machine,d0		 ; machine type
	move.b	d0,sys_mtyp(a6) 	 ; let the drivers sort out the display
	swap	d0
	move.b	d0,sys_ptyp(a6) 	 ; set MMU / FPU in processor type
	lsr.w	#8,d0
	or.b	d0,sys_ptyp(a6)

	move.w	#$7fff,sys_iopr(a6)	 ; io priority
  
	st	sys_castt(a6)		 ; caches off
     
	move.l	#smsq_vers,sys_vers(a6)  ; version

; add thing

th.entry equ	th.len+6
	moveq	#th.entry+$18,d0	 ; allocate thing itself
	jsr	gu_achpp
	move.l	a0,sys_thgl(a6) 	 ; point to it
	lea	th.entry(a0),a2
	move.l	a2,th_thing(a0) 	 ; point to vector
	lea	th_verid(a0),a0
	move.l	#th_vers,(a0)+		 ; set version
	move.w	#5,(a0)+		 ; and name
	move.l	#'THIN',(a0)+
	move.b	#'G',(a0)+

	move.l	#'THG%',(a2)+		 ; ID
	subq.l	#1,(a2)+		 ; type
	lea	th_entry,a0
	move.l	a0,(a2)+		 ; th_entry
	lea	th_exec,a0
	move.l	a0,(a2)+		 ; th_exec
 
; add language dependent list and message table

	moveq	#$40,d0 		 ; initial allocation
	jsr	gu_achpp		 ; allocate
	lea	$3c(a0),a1		 ; end of allocation (one spare)
	move.l	a1,(a0)+
	addq.l	#4,a0
	move.l	a0,sys_ldmlst(a6)
	move.l	a0,-(a0)		 ; empty list

	move.l	#4*256,d0
	jsr	gu_achpp
	move.l	a0,sys_mstab(a6)	 ; empty table

; add defaults

	move.w	sms_defl+sms.conf,sys_lang(a6) ; set language

	moveq	#108,d0 		3*36
	jsr	gu_achpp

	lea	sys_prgd(a6),a2 	and set the pointers to the defaults
	lea	prog_ddd(pc),a1
	bsr.s	set_def 		program default
	lea	data_ddd(pc),a1
	bsr.s	set_def 		data default
	lea	dest_ddd(pc),a1
	bsr.s	set_def 		destination default
 
	moveq	#0,d0
	rts

set_def
	move.l	a0,(a2)+		default address
	moveq	#36/4-1,d0		copy 36 bytes
set_dloop
	move.l	(a1)+,(a0)+
	dbra	d0,set_dloop
	rts

prog_ddd dc.w	5,'flp1_'
data_ddd dc.w	5,'flp1_'
dest_ddd dc.w	3,'par'

	end
