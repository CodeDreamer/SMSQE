; PAR/SER device initialisation  V2.01	   1989  Tony Tebby   QJUMP

; Interrupt version for Atari

	section par

	xdef	par_init

	xref.l	par_vers
	xref	par_tnam
	xref	par_thing
	xref	par_procs
	xref	ut_procdef
	xref	ser_init
	xref	par_int
	xref	par_actv
	xref	iou_idset
	xref	iou_idlk
	xref	iou_lkvm
	xref	gu_thzlk

	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_keys_atari'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_par'
	include 'dev8_mac_assert'
	include 'dev8_mac_vec'
;+++
; PAR driver initialisation.
;---
par_init
	lea	par_procs,a1		 ; set up procedures
	jsr	ut_procdef

	move.w	sr,d7			 ; save status
	trap	#0

	lea	par_link,a3
	jsr	iou_idset		 ; setup par linkage
	lea	par_actv,a0
	move.l	a0,prd_oopr(a3) 	 ; output operation
	st	prd_pare(a3)		 ; par enabled
	jsr	iou_idlk		 ; and link in

	addq.b	#1,prd_port(a3) 	 ; one port only

	moveq	#prd_veco,d1		 ; funny vectored interrupt - offset
	lea	par_int,a1		 ;   - address of server
	jsr	iou_lkvm		 ; link minimum vectored server
	move.l	a1,mfp_vbas+vio_prdy	 ; set address

	lea	par_thing,a1		 ; our Thing
	move.l	a1,th_thing+prd_thgl(a3) ; ... set pointer
	lea	th_verid+prd_thgl(a3),a0
	move.l	#par_vers,(a0)+ 	 ; ... set version
	lea	par_tnam,a1		 ; thing name
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.b	(a1)+,(a0)+

	assert	prd.parx,-1
	subq.b	#1,prd_parx(a3) 	 ; PAR1 exists

	assert	prd_sere,prd_pare-1,prd_prtp-2
	move.l	#$ffffff00,prd_prtl+prd_sere(a3) ; par/ser enabled, par
	move.w	#prd_prtd-prd_prt-prd_parl,prd_prt+prd_parl(a3) ; par is prt
	move.w	#3,prd_prtd(a3)
	move.l	#'PAR ',prd_prtd+2(a3)

	lea	prd_thgl(a3),a0 	 ; link in thing
	jsr	gu_thzlk

	or.w	#$0700,sr		 ; no interrupts
	bset	#mfp..pri,mfp_pre	 ; enable interrupt
	bset	#mfp..pri,mfp_prm	 ; and unmask

	move.b	#cen.ctls,cen_ctls	 ; select control
	moveq	#1<<cen..stb,d1
	or.b	cen_ctlr,d1
	move.b	d1,cen_ctlw		 ; unstrobe

	move.w	d7,sr
	jmp	ser_init

par_link
	dc.l	prd_end+iod.sqhd
	dc.l	1<<iod..ssr+1<<iod..scn serial and name

	novec			 ; no servers
	novec
	novec
	vec	par_io		 ; but a full set of opens
	vec	par_open
	vec	par_close

	vec	par_cnam	 ; name

	end
