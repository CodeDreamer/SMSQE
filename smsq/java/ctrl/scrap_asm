; SMSQmulator control extension thing (c) W. Lenerz 2017

*************** *********************************************************
*	Copying to/from the Scrap to/from the clipboard
*	v. 1.05  2017 Jul 21 added syncscrap
*	v. 1.04  2017 Apr 07 made into extn thing
*
*	copyright (C) W. Lenerz 2016-2017
*
************************************************************************


	section exten

	include dev8_keys_thg
	include dev8_keys_sbasic
	include dev8_keys_menu
	include dev8_keys_scrap
	include dev8_keys_java
	include dev8_keys_qdos_sms
	include dev8_mac_proc2
	include 'dev8_mac_thg'
	include DEV8_smsq_java_ctrl_scrap_keys

	xdef	jt_sget

	xref	gu_achp0
	xref	ut_usscp
	xref	gu_rchp
	xref	ut_frscp
	xref	thp_nul

stack	equ	32060

mregs	reg	a0/a1/a4/d2

;********
; JVA_SGET
; no parameter
; GET content of system clipboard and put it into the menu extensions' scrap
;********

jt_sget thg_extn {SGET},jt_sput,thp_nul
	moveq	#-1,d1
jss_sgt movem.l mregs,-(a7)
	move.l	#'PUT ',d2		; we're PUTting things into the scrap
	jsr	ut_usscp		; use extension
	bne.s	exit			; ooops
	move.l	a1,a4
	move.l	d1,d0			; amount of mem to get
	bpl.s	get_mem
	move.l	#stack,d0		; none set, so use this
get_mem jsr	gu_achp0		; get mem
	bne.s	exit1			; ooops

	move.b	#0,scp_dtyp(a0) 	; what to put = string
	move.l	#0,scp_styp(a0) 	; source type
	move.l	#0,scp_flln(a0) 	; flag : overwrite existing string
	move.l	#0,scp_uccd(a0) 	; no additional copy routine
	lea	52(a0),a1		; point to place for string
	move.l	a1,scp_strg(a0) 	; set in parameters
	moveq	#jtd.xc2s,d0
	dc.w	jva.trpD		; get data into (a1) from java

	move.l	a0,a1			; point to params now
	jsr	thh_code(a4)		; call the scrap extension routine
	move.l	d0,-(a7)
exit3	jsr	gu_rchp 		; release space
	bra.s	exit2
exit1	move.l	d0,-(a7)
exit2	bsr	ut_frscp		; free	scrap
	move.l	(a7)+,d0
exit	movem.l (a7)+,mregs
	tst.l	d0
	rts

 
;********
; JVA_SPUT
; no parameter
; PUT content of the menu extensions' scrap into the system clipboard
;********

jt_sput thg_extn {SPUT},jt_sync,thp_nul
jss_spt movem.l mregs,-(a7)
	move.l	#'GET ',d2		; we're GETting things from the scrap
	jsr	ut_usscp		; use extension
	bne.s	exit			; ooops
	move.l	a1,a4

	move.l	#stack,d0
	jsr	gu_achp0		; get mem
	bne.s	exit1			; ooops

	move.b	#0,scg_dtyp(a0) 	; what to get = string
	move.l	#32000,scg_bufl(a0)	; size of buffer
	move.l	#0,scg_uccd(a0) 	; no additional copy routine
	lea	52(a0),a1		; point to place for string
	move.l	a1,scg_dest(a0) 	; where to copy to

	move.l	a0,a1			; point to params now
	jsr	thh_code(a4)		; call the scrap extension routine
	move.l	d0,-(a7)
	bne.s	exit3
	lea	52(a0),a1

	moveq	#jtd.xs2cp,d0
	dc.w	jva.trpD		; get data at (a1) to java
	bra.s	exit3


;+++
; JVA_SYNCSCRAP
; sync the scrap and the clopboard
;
; this is a blatant copy of MK's corresponding QPC code!
;---
jt_sync thg_extn {SYNC},jt_stop,thp_nul
	movem.l mregs,-(sp)
	moveq	#jva_sjbid,d0
	move.l	jva_lkptr,d1		; get linkage
	beq	exit			; none?????
	move.l	d1,a0			; ptr to syncscrap job ID ...
	add.l	d0,a0			; ... now
	move.l	(a0),d1 		; job ever existed?
	beq.s	jva_snojb		; no, create it
	moveq	#sms.injb,d0
	moveq	#0,d2
	trap	#1
	tst.l	d0			; job still exists?
	beq.s	jva_sscpf		; yes, job already exists & is executing

jva_snojb
	move.l	#'INFO',d2		; test whether SCRAP is installed
	jsr	ut_usscp		; by using it
	tst.l	d0
	bne.s	jva_sscpf		; scrap doesn't exist ->...
	jsr	ut_frscp		; we don't need it now, release it

	moveq	#sms.crjb,d0
	moveq	#0,d1
	moveq	#$60,d2
	move.l	#$400,d3
	suba.l	a1,a1
	trap	#1			; create the syncscrap job
	tst.l	d0
	bne.s	jva_sscpf		; ooops

	lea	jss_job,a1		; addresss of job code
	move.w	#$4ef9,(a0)+		; JMP.L
	move.l	a1,(a0)+		; to start address
	move.w	#$4afb,(a0)+		; SMSQE job special flag
	lea	jss_name,a3		; job name
	move.w	(a3)+,d0
	move.w	d0,(a0)+
jva_cpyjbh
	move.b	(a3)+,(a0)+
	dbf	d0,jva_cpyjbh		; set name

	moveq	#sms.acjb,d0		; activate job
	moveq	#127,d2 		; with high priority
	moveq	#0,d3
	trap	#1			; now
	moveq	#0,d0
jva_sscpf
	bra	exit			; done

jss_name
	dc.w	jsnm2-*-2
	dc.b	'JVA SyncScrap job'
jsnm2


; this is the actual job code
jss_job lea	(a6,a4.l),a5		; A5 = dataspace
	moveq	#sms.info,d0
	trap	#1
	move.l	jva_lkptr,a0		; get linkage
	add.w	#jva_sjbid,a0		; point to my job ID space
	move.l	d1,(a0) 		; save job ID

	move.l	#'INFO',d2		; Use INFO scrap thing
	jsr	ut_usscp
	tst.l	d0
	bne.s	jss_failed

	move.l	a1,a4			; keep ptr to thing
	lea	jss_inf(a5),a1
	jsr	thh_code(a4)		; get info
	move.w	jss_cnt(a5),jss_ssc(a5) ; old = current thing access counter

	moveq	#jtd.strt,d0
	dc.w	jva.trpD		; start java clipboard poll thread
jss_loop
	moveq	#sms.ssjb,d0
	moveq	#-1,d1
	moveq	#25,d3
	sub.l	a1,a1
	trap	#1			; snore half a sec

	; on the very first round, the jva clipboard change counter is 0
	move.l	jss_scc(a5),d1		; stored clipboard modify counter
	moveq	#jtd.qryc,d0		; query clipboard counter
	dc.w	jva.trpD		; call java :any change in clipboard?
	beq.s	jss_scrap		; no, test change in scrap
	move.l	d0,jss_scc(a5)		; set new counter
	add.l	#60,d1			; nbr of bytes necessary + buffer
	bsr	jss_sgt 		; get clipboard content into scrap

	move.l	#'INFO',d2
	jsr	ut_usscp
	move.l	a1,a3
	lea	jss_inf(a5),a1
	jsr	thh_code(a3)
	jsr	ut_frscp
	move.w	jss_cnt(a5),jss_ssc(a5) ; Prevent loopback

jss_scrap
	lea	jss_inf(a5),a1
	jsr	thh_code(a4)
	move.w	jss_cnt(a5),d0		; current scrap modify count
	cmp.w	jss_ssc(a5),d0		; same as my stored count?
	beq	jss_loop		; yes, done for this iteration
	move.w	d0,jss_ssc(a5)
	cmp.b	#0,jss_typ(a5)		; only text is accepted
	bne	jss_loop

	bsr	jss_spt 		; put scrap into java

	addq.l	#1,jss_scc(a5)		; prevent loopback
	bra	jss_loop


 
jss_failed
	moveq	#-1,d1
	moveq	#jva_sjbid,d3
	move.l	jva_lkptr,a0		; get linkage
	add.l	d3,a0			; point to my job ID
jss_stop
	clr.l	(a0)			; no more job there!
	moveq	#jtd.stop,d0		 ; stop java thread
	dc.w	jva.trpd
	move.l	d0,d3
	moveq	#sms.frjb,d0
	trap	#1

	rts				; leave this for jt_stop

;+++
; JVA_SYNCSCRAP_STOP stop the syncscrap job
;---
jt_stop thg_extn {SSTP},,thp_nul
	clr.l	d0
	moveq	#jva_sjbid,d3
	move.l	jva_lkptr,a0		; get linkage
	add.l	d3,a0			; point to my job ID
	move.l	(a0),d1 		; get it
	bne.s	jss_stop		; job exists

	moveq	#jtd.stop,d0		; stop java thread just in case
	dc.w	jva.trpd
	rts

	end
