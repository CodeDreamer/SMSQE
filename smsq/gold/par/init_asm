; Gold Card PAR/SER device initialisation  V2.03     1994  Tony Tebby

	section par

	xdef	par_init

	xref.l	par_vers
	xref	par_tnam
	xref	par_thing
	xref	par_procs
	xref	ut_procdef
	xref	ser_init
	xref	par_io
	xref	par_open
	xref	par_close
	xref	par_sched
	xref	par_cnam
	xref	par_oopr
	xref	iou_idset
	xref	iou_idlk
	xref	gu_thzlk

	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_thg'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_par'
	include 'dev8_smsq_gold_keys'
	include 'dev8_mac_assert'
;+++
; PAR driver initialisation.
;---
par_init
	lea	par_procs,a1		 ; set up procedures
	jsr	ut_procdef
	bsr.s	pari_setup
	jmp	ser_init

pari_setup
	moveq	#sms.xtop,d0
	trap	#do.smsq

	lea	par_link,a3
	jsr	iou_idset		 ; setup par linkage

	lea	par_thing,a1		 ; our Thing
	move.l	a1,th_thing+prd_thgl(a3) ; ... set pointer
	lea	th_verid+prd_thgl(a3),a0
	move.l	#par_vers,(a0)+ 	 ; ... set version
	lea	par_tnam,a1		 ; thing name
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.b	(a1)+,(a0)+
	lea	prd_thgl(a3),a0 	 ; link in thing
	jsr	gu_thzlk

	tst.b	sys_ptyp(a6)		 ; 68000?
	beq.s	pari_rts		 ; ... yes, done

	lea	par_oopr,a1
	move.l	a1,prd_oopr(a3) 	 ; out op is the same as scheduler
	st	prd_pare(a3)		 ; par enabled
	assert	prd.parx,-1
	subq.b	#1,prd_parx(a3) 	 ; PAR exists
	addq.b	#1,prd_port(a3) 	 ; one port only

	assert	prd_puls,prd_wait-2
	move.l	#$00010060,prd_puls(a3)  ; 2 us ish pulse, 100 us wait

	clr.b	glo_pdat+glc_base	 ; clear data
	tst.b	glo_pstb+glc_base	 ; unstrobe

	jmp	iou_idlk		 ; link in

pari_rts
	rts

par_link
	dc.l	prd_end+iod.sqhd
	dc.l	1<<iod..ssr+1<<iod..scn serial and name
	dc.w	0
	dc.w	0
	dc.w	par_sched-*  ; only a scheduler server
	dc.w	par_io-*     ; but a full set of opens
	dc.w	par_open-*
	dc.w	par_close-*

	dc.w	par_cnam-*

	end
