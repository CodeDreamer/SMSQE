; SBAS_JOB - SBASIC Job     V2.00    1994  Tony Tebby	QJUMP

	section sbas

	xdef	sb_job
	xdef	sb_die
	xdef	sb_fatal
	xdef	sb_name

	xdef	sb_thing

	xref	sb_initv
	xref	sb_initc

	xref	sb_main
	xref	sb_start
	xref	sb_xcmd
	xref	sb_qd5

	xref	sb_anam2
	xref	sb_aldat
	xref	sb_resch
	xref	sb_inchan

	xref.l	sb.vers

	xref	uq_opcon

	xref	gu_thini
	xref	gu_mexec

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'
	include 'dev8_keys_k'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; Initialise Thing
;---
sb_thing
	lea	sb_thtab,a1
	jmp	gu_thini

sb_thtab
	dc.l	th_name+8		 ; size of linkage
	dc.l	sb_thdef-*		 ; thing
	dc.l	sb.vers 		 ; version
sb_name
	dc.w	6,'SBASIC'


sb_thdef
	dc.l	thh.flag	    ; flag
	dc.l	tht.exec	    ; executable
	dc.l	sb_job-sb_thdef     ; header
	dc.l	sbj_start-sb_job    ; ... length
	dc.l	sb.jobsz	    ; dataspace
	dc.l	sbj_start-sb_thdef  ; start address

;+++
; SBASIC Job
;---
sb_job
	bra.s	sbj_start
	dc.w	0,0,$4afb
	dc.w	6,'SBASIC'

sbj_job0
	jsr	sb_initc		 ; initialise job zero consoles
	jmp	sb_main

sbj_start
	move.l	a0,d0			 ; A0 is set if Job 0 entry
	bne.s	sbj_job0		 ; ... job 0 entry, vars already set

	jsr	sb_initv		 ; initial SBASIC allocation
	move.w	(sp)+,d4		 ; number of channels open
	beq.l	sbj_new 		 ; new SBASIC

	move.l	(sp),d0 		 ; first ID
	bmi.s	sbj_chn0		 ; definition for channel 0 (d4>0)
	cmp.l	#'QD5S',d0
	beq.l	sb_qd5

	move.l	(sp)+,sb_cmdch(a6)	 ; input file
	subq.w	#1,d4
	beq.s	sbj_ckstr		 ; no channels on stack

	moveq	#ch.len,d1
	mulu	d4,d1			 ; room required in channel table
	jsr	sb_resch

	move.l	sb_chanp(a6),a2
	subq.w	#1,d4
	bgt.s	sbj_chloop		 ; more than 2 channels
	tst.w	(sp)			 ; channel 0 open?
	bmi.s	sbj_chn0		 ; definition for channel 0 (d4=0)

sbj_chloop
	move.l	(sp)+,a0
	jsr	sb_inchan		 ; set up channel
	dbra	d4,sbj_chloop

	move.l	a2,sb_chanp(a6)

sbj_ckstr
	move.w	(sp)+,d3		 ; length of string
	beq.l	sbj_go			 ; ... none
	bra.s	sbj_cmd$		 ; set cmd$

; open console - size on stack, stack is cleaned

sbj_open0
	move.l	(sp)+,a3
	move.l	#$ff010004,-(sp)	 ; colour
	move.l	sp,a1
	jsr	uq_opcon
	bne.l	sb_fatal
	add.w	#$c,sp			 ; clear stack
	move.l	sb_chanp(a6),a2
	jsr	sb_inchan		 ; open channel
	move.l	a2,sb_chanp(a6)
	jmp	(a3)

sbj_chn0
	not.l	(sp)			 ; set origin right
	move.l	#$0100003e,-(sp)	 ; size
	bsr	sbj_open0		 ; open it

	move.w	(sp)+,d3		 ; length of string
	beq.s	sbj_go			 ; ... none
	tst.w	d4			 ; d4 is 0 for string = cmd$
	bne.s	sbj_cmdl		 ; 1 for string = command line

sbj_cmd$
	move.l	sb_buffb(a6),a1
	move.l	#'cmd$',(a6,a1.l)
	moveq	#4,d2			 ; set up cmd$
	jsr	sb_anam2
	move.b	#nt.var,(a6,a3.l)	 ; and usage

	moveq	#dt_stchr-dt_stalc+7,d1  ; add 6 bytes and round up
	add.w	d3,d1
	and.w	#$fff8,d1		 ; to multiple of 8
	jsr	sb_aldat		 ; allocate hole
	assert	dt_stalc+4,dt_flstr+1,dt_stlen
	move.w	d1,(a0)+		 ; allocation
	move.w	#$00ff,(a0)+		 ; flags
	move.l	a0,nt_value(a6,a3.l)

	move.w	d3,(a0)+		 ; length
sbj_csloop
	move.w	(sp)+,(a0)+		 ; copy characters
	subq.w	#2,d3
	bgt.s	sbj_csloop

	bra.s	sbj_go


sbj_new
	move.w	(sp)+,d3		 ; any string?
	beq.s	sbj_all 		 ; ... no, open all

sbj_cmdl
	move.l	sb_buffb(a6),a1
	add.l	a6,a1
sbl_clloop
	move.w	(sp)+,(a1)+
	subq.w	#2,d3
	bgt.s	sbl_clloop
	add.w	d3,a1			 ; correct the position
	move.b	#k.nl,(a1)+		 ; newline at end
	sub.l	a6,a1
	move.l	a1,sb_buffp(a6)
	jmp	sb_xcmd 		 ; execute first command


sbj_all
	jsr	sb_initc		 ; open all consoles
sbj_go
	jmp	sb_start

sb_die
	moveq	#0,d0
sb_fatal
	move.l	d0,d3
	moveq	#-1,d1
	moveq	#sms.frjb,d0
	trap	#do.sms2

	dc.l	$4afbedeb
	bra.s	*
	dc.l	'DIE '

	end
