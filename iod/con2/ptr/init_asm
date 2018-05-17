* Pointer version of console initialisation code  V2.05    1991   Tony Tebby
*
* 2003-03-01  2.01  Added call to system sprite initialisation (MK)
* 2003-03-04  2.02  Initialises pixel aspect ratio (MK)
* 2004-03-29  2.03  Call sprite cursor job table initialisation (wl)
* 2005-11-16  2.04  Call background I/O initialisation (MK)
* 2006-05-15  2.05  Enable background I/O according to SMSQ/E configuration (MK)
*		    Enable new or old CTRL+C behaviour according to config (MK)

	xdef	pt_init
*
	xref	pt_bginit
	xref	pt_bgprocs
	xref	pt_bgctl
	xref	pt_installv
	xref	pt_isspr
	xref	curs_init
*
	xref	cn_dfnt1
	xref	cn_dfnt2
*
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_con'
	include 'dev8_mac_proc'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'

	section init

proc_tab
	proc_stt
	proc_def CKEYON
	proc_def CKEYOFF
	proc_def MOUSE_SPEED
	proc_def MOUSE_STUFF
	proc_end
	proc_stt
	proc_end

pt.dqln equ	232
pt.dcon equ	sd_keyq+16+pt.dqln
pt.dq2	equ	pt.dqln-sd.dq2

*	d0  r	>0 if not first pointer driver def
*	d2 c  p colour depth 0-4
*	d3 c  p mode
*	a3  r	base of linkage block
*
pt_init
reglist reg	d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a4/a5

	bsr.s	pti_nasty
	tst.l	d0
	bgt.s	pti_rts
	lea	proc_tab,a1		 new procedures
	move.w	sb.inipr,a2
	jsr	(a2)
	lea	pt_bgprocs,a1
	move.w	sb.inipr,a2
	jsr	(a2)
pt_ok
	moveq	#0,d0
pti_rts
	rts


pti_nasty
	moveq	#sms.xtop,d0
	trap	#do.sms2

	or.w	#$0700,sr

	movem.l reglist,-(sp)
	move.b	d3,d5
	moveq	#0,d7
	move.b	d2,d7

	move.l	sys_clnk(a6),d6 	; console linkage already set?
	move.l	d6,a3
	bne.l	pti_setinstall		; ... yes, just set the install ptr

pti_alloc
	moveq	#sms.achp,d0		; first allocate the linkage block
	move.l	#pt_end+pt.dcon,d1
	moveq	#0,d2			; owned by 0
	trap	#1

	move.l	a0,a3			; linkage block address
	move.l	#pt.ident,pt_ident(a3)

	move.l	a3,sys_clnk(a6) 	; save console linkage

	lea	cn_dfnt1(pc),a4
	move.l	a4,pt_sfnt1(a3) 	; set up standard founts
	lea	cn_dfnt2(pc),a4
	move.l	a4,pt_sfnt2(a3)
*
	move.b	#pt.supmd,pt_pstat(a3)	; set pointer suppressed
*
	move.w	#pt.kaccl,pt_kaccl(a3)	; set default accelerators
	move.w	#pt.xaccl,pt_xaccl(a3)
	move.w	#pt.yaccl,pt_yaccl(a3)
	move.b	#pt.wake,pt_wake(a3)
	move.b	#pt.relax,pt_relax(a3)
*
	st	pt_dmode(a3)		; mode not set
	st	pt_cdpth(a3)		; colour depth not set
*
	lea	pt_end+sd_keyq(a3),a2
	move.l	a2,pt_dumq1(a3) 	; set dummy keyboard queue address
	move.l	#sd_keyq,pt_kqoff(a3)	; ... offset

	moveq	#12,d1			; very short dummy queue 1
	move.w	ioq.setq,a5		; reset it
	jsr	(a5)
	move.l	a2,(a2) 		; point to myself!!!

	move.l	a2,sys_ckyq(a6) 	; current queue
*
	move.l	a2,a4
	lea	sd.dq2(a2),a2		; dummy keyboard queue 2
	moveq	#pt.dq2,d1		; reasonable length
	move.w	ioq.setq,a5
	jsr	(a5)			; set up queue
	move.l	a4,(a2) 		; and link to dummy 1

	move.b	sms.conf+sms_ctrlc,d1	; 0 = old, 1 = new behaviour
	sne	d1			; 0/$FF
	ext.w	d1			; 0/$FFFF
	not.w	d1			; $FFFF/0
	move.w	d1,pt_swwin(a3) 	; $FFFF = old behaviour

	jsr	pt_bginit		; init background I/O data
	moveq	#0,d1
	moveq	#0,d2
	move.b	sms.conf+sms_bgio,d1
	jsr	pt_bgctl		; enable bg I/O if configured to do so

	jsr	pt_isspr		; set up system sprite table

	jsr	curs_init		; init sprite cursor job table

	move.l	#$80156B9,pt_asprt(a3)	; 2/( (4/3) * (575/512) * (51.2/51.95) )
	clr.w	pt_asprt+4(a3)
pti_setinstall
	lea	pt_cdtab(a3),a4
	move.b	d5,(a4,d7.w)		; set mode for this colour depth

	lea	pt_installv,a1
	lsl.w	#2,d7			; index driver install table
	move.l	a1,pt_drtab-pt_cdtab(a4,d7.w) ; set pointer

	move.l	d6,d0			; return >0 if already existed
	movem.l (sp)+,reglist
	rts

	end
