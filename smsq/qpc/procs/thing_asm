; QPC Extension thing

	section thing

	xdef	qpc_defs

	xref	thp_2wd
	xref	thp_str
	xref	thp_rstr
	xref	thp_nrstr
	xref	thp_slwd
	xref	ut_usscp
	xref	ut_frscp

	include 'dev8_keys_thg'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_qpc_procs_scrap_keys'

qpc_rword dc.w thp.ret+thp.uwrd
qpc_null  dc.w 0

;qpc_glgw  dc.w thp.ulng
;	   dc.w thp.uwrd
;	   dc.w 0

;qpc_glrw  dc.w thp.ulng
;	   dc.w thp.ret+thp.uwrd
;	   dc.w 0

qpc_gwrw  dc.w thp.uwrd
	  dc.w thp.ret+thp.uwrd
	  dc.w 0

qpc_byte  dc.w thp.ubyt
	  dc.w 0

qpc_2str  dc.w thp.call+thp.str
	  dc.w thp.call+thp.opt+thp.str
	  dc.w 0

qpc_pstr  dc.w thp.uwrd 	; port
	  dc.w thp.call+thp.str ; string
	  dc.w 0

qpc_defs  dc.l th_name+2+3
	  dc.l qpc_thing-*
	  dc.l '1.10'

;+++
; ASCI Thing NAME
;---
qpc_tname dc.b 0,3,'QPC',0

;+++
; This is the Thing with the QPC extensions
;---
qpc_thing

qpct.reg reg	 d1/d2/a0/a1

;+++
; QPC_EXIT
;---
qpc_exit thg_extn {EXIT},qpc_mspeed,qpc_null
	dc.w	qpc.exit
	dc.w	$4afb

;+++
; QPC_MSPEED hor,ver
;---
qpc_mspeed thg_extn {MSPD},qpc_ver$,thp_2wd
	moveq	#0,d0
	rts

;+++
; QPC_VER$
;---
qpc_ver$ thg_extn {VER$},qpc_netname$,thp_rstr
	movem.l qpct.reg,-(sp)
	move.l	4(a1),a0
	dc.w	qpc.ver+1
	move.l	d1,2(a0)
	move.w	#4,(a0)
	movem.l (sp)+,qpct.reg
	moveq	#0,d0
	rts

;+++
; QPC_NETNAME$
;---
qpc_netname$ thg_extn {NAM$},qpc_cmdline$,thp_rstr
	movem.l qpct.reg,-(sp)
	move.l	4(a1),a0
	dc.w	qpc.nnam		; get network name
	moveq	#0,d0
	movem.l (sp)+,qpct.reg
	rts

;+++
; QPC_CMDLINE$
;---
qpc_cmdline$ thg_extn {CMD$},qpc_hostos,thp_rstr
	movem.l qpct.reg,-(sp)
	move.w	2(a1),d1
	move.l	4(a1),a0
	dc.w	qpc.cmdl
	movem.l (sp)+,qpct.reg
	rts

;+++
; QPC_HOSTOS
;---
qpc_hostos thg_extn {HOST},qpc_maximize,qpc_rword
	dc.w	qpc.os
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	d0,(a1)
	move.l	(sp)+,a1
	moveq	#0,d0
	rts

;+++
; QPC_MAXIMIZE
;---
qpc_maximize thg_extn {MAXW},qpc_minimize,qpc_null
	dc.w	qpc.maxwn
	moveq	#0,d0
	rts

;+++
; QPC_MINIMIZE
;---
qpc_minimize thg_extn {MINW},qpc_restore,qpc_null
	dc.w	qpc.minwn
	moveq	#0,d0
	rts

;+++
; QPC_RESTORE
;---
qpc_restore thg_extn {RESW},qpc_windowsize,qpc_null
	dc.w	qpc.reswn
	moveq	#0,d0
	rts

;+++
; QPC_WINDOWSIZE x,y
;---
qpc_windowsize thg_extn {WSIZ},qpc_qlscremu,thp_2wd
	move.l	(a1)+,d1
	move.l	(a1)+,d2
	dc.w	qpc.sszwn
	moveq	#0,d0
	rts

;+++
; QPC_QLSCREMU x
;---
qpc_qlscremu thg_extn {SEMU},qpc_exec,thp_slwd
	move.l	(a1),d1
	beq.s	qse_set
	cmp.b	#-1,d1
	beq.s	qse_set
	cmp.b	#4,d1
	beq.s	qse_set
	cmp.b	#8,d1
	bne.s	qpc_ipar
qse_set
	dc.w	qpc.qlemu+1
	moveq	#0,d0
	rts

qpc_ipar
	moveq	#err.ipar,d0
	rts

;+++
; QPC_EXEC name,parameters
;---
qpc_exec thg_extn {EXEC},qpc_windowtitle,qpc_2str
	movem.l a2-a3,-(sp)
	move.l	4(a1),a2
	suba.l	a3,a3
	cmp.w	#0,8(a1)		; optional parameter missing?
	beq.s	qe_nopar
	move.l	12(a1),a3
qe_nopar
	dc.w	qpc.exec+2
	movem.l (sp)+,a2-a3
	rts

;+++
; QPC_WINDOWTITLE name
;---
qpc_windowtitle thg_extn {WTIT},ser_getport,thp_str
	move.l	a0,-(sp)
	move.l	4(a1),a0
	dc.w	qpc.wtit
	movem.l (sp)+,a0
	rts

;+++
; s$ = SER_GETPORT(port%)
;---
ser_getport thg_extn {SGET},ser_setport,thp_nrstr
	move.l	a0,-(sp)
	move.l	(a1)+,d0		; port
	move.w	2(a1),d1		; buffer size
	move.l	4(a1),a0		; buffer
	dc.w	qpc.sgetp
	movem.l (sp)+,a0
	rts

;+++
; SER_SETPORT port, name$
;---
ser_setport thg_extn {SSET},par_printercount,qpc_pstr
	move.l	a0,-(sp)
	move.l	(a1)+,d0
	move.l	4(a1),a0
	dc.w	qpc.ssetp
	movem.l (sp)+,a0
	rts

;+++
; count% = PAR_PRINTERCOUNT
;---
par_printercount thg_extn {PCNT},par_printername$,qpc_rword
	moveq	#-1,d0
	dc.w	qpc.pinfo
	bne.s	ppc_exit
	move.l	a1,-(sp)
	move.l	4(a1),a1
	move.w	d1,(a1)
	move.l	(sp)+,a1
ppc_exit
	rts

;+++
; s$ = PAR_PRINTERNAME$(printer%)
;---
par_printername$ thg_extn {PNAM},par_defaultprinter$,thp_nrstr
	move.l	a0,-(sp)
	move.l	(a1)+,d0		; number
	move.w	2(a1),d1		; buffer size
	move.l	4(a1),a0		; buffer
	dc.w	qpc.pinfo
	movem.l (sp)+,a0
	rts

;+++
; s$ = PAR_DEFAULTPRINTER$
;---
par_defaultprinter$ thg_extn {PDEF},par_getprinter$,thp_rstr
	move.l	a0,-(sp)
	move.w	2(a1),d1		; buffer size
	move.l	4(a1),a0		; buffer
	moveq	#-2,d0
	dc.w	qpc.pinfo
	movem.l (sp)+,a0
	rts

;+++
; s$ = PAR_GETPRINTER$(port%)
;---
par_getprinter$ thg_extn {PGET},par_setprinter,thp_nrstr
	move.l	a0,-(sp)
	move.l	(a1)+,d0		; port
	move.w	2(a1),d1		; buffer size
	move.l	4(a1),a0		; buffer
	dc.w	qpc.pgetp
	movem.l (sp)+,a0
	rts

;+++
; PAR_SETPRINTER port%, name$
;---
par_setprinter thg_extn {PSET},par_getfilter,qpc_pstr
	move.l	a0,-(sp)
	move.l	(a1)+,d0
	move.l	4(a1),a0
	dc.w	qpc.psetp
	movem.l (sp)+,a0
	rts

;+++
; status% = PAR_GETFILTER(port%)
;---
par_getfilter thg_extn {PFGT},par_setfilter,qpc_gwrw
	move.l	a1,-(sp)
	move.l	(a1)+,d0		; port
	dc.w	qpc.pgetf+1
	move.l	4(a1),a1
	move.w	d1,(a1)
	move.l	(sp)+,a1
	tst.l	d0
	rts

;+++
; PAR_SETFILTER port%, status%
;---
par_setfilter thg_extn {PFST},qpc_syncscrap,thp_2wd
	move.l	(a1)+,d0		; port
	move.l	(a1)+,d1
	dc.w	qpc.psetf+1
	tst.l	d0
	rts

;+++
; QPC_SYNCSCRAP
;---
qpc_syncscrap thg_extn {SYNC},,qpc_null
	movem.l d1-d3/a0-a3,-(sp)
	moveq	#0,d0
	dc.w	qpc.os
	tst.w	d0
	beq.s	qpc_sscpf		; We're running under DOS -> quit

	lea	qpc_sjbid,a0
	move.l	(a0),d1
	tst.l	d1
	beq.s	qpc_snojb
	moveq	#sms.injb,d0
	moveq	#0,d2
	trap	#1
	tst.l	d0
	beq.s	qpc_sscpf		; Job already exists

qpc_snojb
	move.l	#'INFO',d2		; Test whether SCRAP's installed
	jsr	ut_usscp
	tst.l	d0
	bne.s	qpc_sscpf
	jsr	ut_frscp		; We don't need it now, release it

	moveq	#sms.crjb,d0
	moveq	#0,d1
	lea	qss_name,a3
	moveq	#$60,d2
	move.l	#$400,d3
	suba.l	a1,a1
	trap	#1
	tst.l	d0
	bne.s	qpc_sscpf

	lea	qss_job,a1
	move.w	#$4ef9,(a0)+		; JMP.L
	move.l	a1,(a0)+		; To start address
	move.w	#$4afb,(a0)+
	move.w	(a3)+,d0
	move.w	d0,(a0)+
qpc_cpyjbh
	move.b	(a3)+,(a0)+
	dbf	d0,qpc_cpyjbh

	moveq	#sms.acjb,d0
	moveq	#127,d2
	moveq	#0,d3
	trap	#1
	moveq	#0,d0
qpc_sscpf
	movem.l (sp)+,d1-d3/a0-a3
	rts

qss_name
	dc.w	17
	dc.b	'QPC SyncScrap job',0

qss_job
	lea	(a6,a4.l),a5

	moveq	#sms.info,d0
	trap	#1
	lea	qpc_sjbid,a0
	move.l	d1,(a0) 		; Save our job ID

	move.l	#'INFO',d2		; Use INFO thing
	jsr	ut_usscp
	tst.l	d0
	bne	qss_failed

	move.l	a1,a4
	lea	qss_inf(a5),a1
	jsr	thh_code(a4)
	move.w	qss_cnt(a5),qss_osm(a5)
qss_loop
	moveq	#sms.ssjb,d0
	moveq	#-1,d1
	moveq	#25,d3
	movea.l #0,a1
	trap	#1

	lea	qpc_clipc,a0
	move.b	(a0),d0
	cmp.b	qss_ocm(a5),d0
	beq.s	qss_scrap
	move.b	d0,qss_ocm(a5)

	dc.w	qpc.gcps+1		; Get size of clipboard contents
	tst.l	d0
	bmi.s	qss_loop		; Clipboard changed in the meantime

	add.l	#$10,d1 		; Can't harm
	moveq	#sms.achp,d0		; Allocate data area
	moveq	#-1,d2
	trap	#1
	tst.l	d0
	beq.s	qss_achp
	moveq	#0,d1			; Only release clipboard
qss_achp
	dc.w	qpc.gcpc		; Get clipboard contents
	tst.l	d1
	beq.s	qss_scrap

	lea	qss_put(a5),a1
	clr.l	(a1)+
	clr.l	(a1)+
	move.l	a0,(a1)+
	clr.l	(a1)+
	clr.l	(a1)+

	move.l	#'PUT ',d2		; Put data into buffer
	jsr	ut_usscp
	tst.l	d0
	bne.s	qss_scrap

	move.l	a1,a3
	lea	qss_put(a5),a1
	jsr	thh_code(a3)

	jsr	ut_frscp

	move.l	#'INFO',d2
	jsr	ut_usscp
	move.l	a1,a3
	lea	qss_inf(a5),a1
	jsr	thh_code(a3)
	jsr	ut_frscp
	move.w	qss_cnt(a5),qss_osm(a5) ; Prevent loopback

	move.l	qss_pad(a5),a0
	moveq	#sms.rchp,d0
	trap	#1

qss_scrap
	lea	qss_inf(a5),a1
	jsr	thh_code(a4)
	move.w	qss_cnt(a5),d0
	cmp.w	qss_osm(a5),d0
	beq	qss_loop
	move.w	d0,qss_osm(a5)

	cmp.b	#0,qss_typ(a5)		; Only text is accepted
	bne	qss_loop

	moveq	#sms.achp,d0		; Allocate data area
	move.l	qss_bfl(a5),d1
	moveq	#-1,d2
	trap	#1
	tst.l	d0
	bne	qss_loop
	cmp.l	qss_bfl(a5),d1
	blt	qss_loop

	lea	qss_get(a5),a1
	clr.l	(a1)+
	move.l	a0,(a1)+
	move.l	d1,(a1)+
	clr.l	(a1)+

	move.l	#'GET ',d2		; Get data buffer
	jsr	ut_usscp
	tst.l	d0
	bne	qss_loop

	move.l	a1,a3
	lea	qss_get(a5),a1
	jsr	thh_code(a3)

	jsr	ut_frscp

	move.l	qss_dst(a5),a0
	dc.w	qpc.pclp
	addq.b	#1,qss_ocm(a5)		; Prevent loopback

	moveq	#sms.rchp,d0
	trap	#1
	bra	qss_loop

qss_failed
	move.l	d0,d3
	moveq	#sms.frjb,d0
	moveq	#-1,d1
	trap	#1


	end
