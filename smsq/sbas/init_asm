; SBAS_INIT - Initialise SBASIC vars	 V2.02	  1992  Tony Tebby  QJUMP

	section sbas

	xdef	sb_initv
	xdef	sb_initc

	xref	sb_resch
	xref	sb_inallc
	xref	sb_inchan
	xref	gu_achp0
	xref	uq_opcon

	xref	sb_job
	xref	sb_chk$heap

	include 'dev8_keys_sys'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'
	include 'dev8_mac_assert'

;+++
; Initialise SBASIC variables area
; The SuperBASIC stub area is already set
;
;	d0-d3	scratch
;	a0/a1	scratch
;	a6 cr	pointer to base of Job / pointer to SBASIC variables
;---
sb_initv
sbi.skip equ	$20
	lea	sbi.skip(a6),a1 		   ; clear data area
	moveq	#0,d0
	move.w	#(sb.jobsz-sbi.skip-sb.alstk)/4,d1 ; but do not bother with stack
sbi_clmem
	move.l	d0,(a1)+
	dbra	d1,sbi_clmem

	lea	sb_vars-sb_offs(a6),a6	 ; from now on, this is the only a6
	move.l	#sb.flag,sb_flag(a6)	 ; flag it as genuine

	moveq	#4,d3			 ; amount on stack
	add.l	a7,d3			 ; base of stack

	move.l	#sb.prstl,sb_prstl(a6)	 ; limit of stack
	sub.l	a6,d3
	move.l	d3,sb_prstp(a6)
	move.l	d3,sb_prstb(a6) 	 ; processor stack pointers!!!!

	moveq	#sms.info,d0
	trap	#do.smsq
	move.l	sys_sbab(a0),a0
	move.l	sb_offs+sb_qlibr(a0),sb_qlibr(a6) ; set QLIB pointer

	lea	sb_job,a0		 ; base of job
	move.l	a0,sb_sbjob(a6) 	 ; set it
	lea	sb_chk$heap,a0		 ; heap checking patch
	move.l	a0,sb_chkhp(a6) 	 ; set it
	assert	sb.edt,$ff
	st	sb_edt(a6)		 ; program not up to date
	jmp	sb_inallc		 ; initial allocation

;+++
; Initialise SBASIC Job 0 channels
;---
sb_initc
	lea	con0,a1
	bsr.s	sbi_open
	lea	con1,a1
	bsr.s	sbi_open
	lea	con2,a1

sbi_open		       ; $$redundant?
	jsr	uq_opcon		 ; open console
	moveq	#ch.len,d1
	jsr	sb_resch		 ; reserve channel space
	move.l	sb_chanp(a6),a2
	jsr	sb_inchan
	move.l	a2,sb_chanp(a6)
	moveq	#0,d0
	rts

con0	dc.b	255,1,0,4
	dc.w	512,52,0,204
con1	dc.b	255,1,2,7
	dc.w	256,202,256,0
con2	dc.b	255,1,7,2
	dc.w	256,202,0,0

	end
