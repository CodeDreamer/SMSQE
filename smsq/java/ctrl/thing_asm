; SMSQmulator Control Procedures   V1.02    2017 W. Lenerz
; contains the extensions
; MBAR, VER$, WTIT,NNAM,PPUP, TMRS,TMRG and dsome more
; 1.02 LOG added
; 1.01 JVA_MINIMIZE added
	section exten

	xdef	ctrl_thing
	xdef	ctrl_tname

	xref	thp_ostr
	xref	thp_nul
	xref	wl_od
	xref	thp_wd
	xref	thp_str
	xref	thp_rstr
	xref	jt_qlfp 		; next extension

	include 'dev8_keys_thg'
	include 'dev8_keys_qlv'
	include 'dev8_keys_java'
	include 'dev8_keys_psf'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_base_keys'


hd_byte dc.w	thp.ubyt,0

hd_wdstr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.call+thp.str ; string
	 dc.w	0

hd_bochr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.char+thp.opt ; with optional character
	 dc.w	0


hd_noptm dc.w	thp.ubyt	 ; drive
	 dc.w	thp.ubyt+thp.opt+thp.nnul ; default is set
	 dc.w	0

jth_rwrd dc.w	thp.ret+thp.uwrd
	 dc.w	0

jth_rlng dc.w	thp.ret+thp.ulng
	 dc.w	0
jth_log  dc.w	 thp.ubyt+thp.opt
	 dc.w	 0

;+++
; Thing NAME
;---
ctrl_tname dc.w   tn2-*-2
	dc.b	'JAVA Control'
tn2
	dc.w	0			: do NOT remove this line!
;+++
;
; This is the Thing with the "control" extensions
;
;---
ctrl_thing

;+++
; JVAMBAR
; no parameter, turns menu bar back on
;---
jt_mbar thg_extn {MBAR},jt_vers,thp_nul
	moveq	#jt5.mbar,d0
	dc.w	jva.trp5		; this sets D0 & cc
	rts


;+++
; JVA_VER$
; One string return parameter.	There must be space for 4 chars + legth word
; returns the SMSQmulator version
;---
jt_vers thg_extn {VER$},jt_wtit,thp_rstr
	move.w	2(a1),d1
	ble.s	badpar
	subq.w	 #4,d1
	ble.s	badpar
	moveq	#jt5.vers,d0
	dc.w	jva.trp5		; get SMSQmulator version into d1
	move.l	4(a1),a1
	move.l	d1,2(a1)		;
	move.w	#4,(a1)
	rts				; D0 & cc set by java
badpar
	moveq	#err.ipar,d0
	rts


;+++
; JVA_WINDOWTITLE
; ONE cumpulsory string call parameter.
; Sets the SMSQmulator window title.
;---
jt_wtit thg_extn {WTIT},jt_nnam,thp_str
	move.l	4(a1),a1
	moveq	#jt5.wtit,d0
	dc.w	jva.trp5		; D0 & cc set by java
wdw_rts rts

 
;+++
; JVA_NETNAME$
; One string return parameter.
; Tries to get the SMSQmulator net name.
;---
jt_nnam thg_extn {NNAM},jt_hadd,thp_rstr
	moveq	#jt9.name,d0
	dc.w	jva.trp9		; get name, ; D0 & cc set by java
	rts

 
;+++
; JVA_NETADDR$
; One string return parameter.
; Tries to get the SMSQmulator net address.
;---
jt_hadd thg_extn {HADD},jt_ppup,thp_rstr
	moveq	#jt9.addr,d0
	dc.w	jva.trp9		; get addr, ; D0 & cc set by java
	rts

;+++
; JVA_POPUP
; No parameter.
; Pops up/blinks SMSQmulator wdw.
;---
jt_ppup thg_extn {PPUP},jt_minimize,thp_nul
	clr.l	d0
	dc.w	jva.trpb
	rts				; D0 & cc set by java

     
;+++
; JVA_MINIMIZE
; No parameter.
; Pops up/blinks SMSQmulator wdw.
;---
jt_minimize thg_extn {MMIZ},jt_host,thp_nul
	moveq	#jtb.mini,d0
	dc.w	jva.trpb
	rts				; D0 & cc set by java

     
;+++
; JVA_HOSTOS$
; One string return parameter.
; Tries to get the name and version of the host os smsqmulator is running on
;---
jt_host thg_extn {HOST},jt_sndo,thp_rstr
	moveq	#jt5.host,d0
	bra.s	do_tp5		      ; get name, ; D0 & cc set by java
	rts


*************************************************************************
*
*	Setting/getting a timer with a millisecond precision.
*
*	JTMRSET sets (starts) the timer
*
*	a=JTMRGET() gets elapsed milliseconds since timer was started
*
************************************************************************

	    
;+++
; JVA_SOUNDING
; One word return parameter.
; Gets the status of sond plaing   (0 =hidden, 1 =showing)
;---
jt_sndo thg_extn {SNDO},jt_tmrs,jth_rwrd
	moveq	#jt5.sndon,d0
	bra.s	do_tp5
		      
;+++
; JVA_TMRSET
; No parameter.
; Reset the timer to "0".
;---
jt_tmrs thg_extn {TMRS},jt_tmrg,thp_nul
	moveq	#jt5.sttmr,d0
do_tp5	dc.w	jva.trp5
out	rts


;+++
; JVA_TMRGET
; One longword return parameter.
; Get current value of timer.
;---
jt_tmrg thg_extn {TMRG},jt_scru,jth_rlng
	moveq	#jt5.gttmr,d0
	bra.s	do_tp5


;+++
; JVA_SCRUPDT
; One word parameter, the screen update rate.
; Sets the screen update rate.
;---
jt_scru thg_extn {SCRU},jt_mbst,thp_wd
; set the screen update rate
	moveq	#jt5.scrup,d0
	bra.s	do_tp5

;+++
; JVA_MBAR_STATUS
; One word return parameter.
; Gets the status of the menu bar   (0 =hidden, 1 =showing)
;---
jt_mbst thg_extn {MBST},jt_exit,jth_rwrd
	moveq	#jt5.jbget,d0
	bra.s	do_tp5

;+++
; JVA_EXIT
; No parameter.
; Quits the emulation.
;---
jt_exit thg_extn {EXIT},jt_lgon,thp_nul
	moveq	#jt5.exit,d0
	bra.s	do_tp5


;+++
; JVA_LOGON/LOGOFF
; No parameter.
; switches logging on/off
;---

jt_lgon thg_extn {LON },jt_loff,jth_log
	bra.s	lg_exit
	move.l	jva_lkptr,d0
	beq.s	lg_exit
	move.l	d0,a3
	move.l	jva_kbrd(a3),d0
	beq.s	lg_exit
	lea	int3,a3
	move.l	a3,$6c
	moveq	#jte.lon,d0
do_trpE dc.w	jva.trpE
lg_exit moveq	#0,d0
	rts

jt_loff thg_extn {LOFF},jt_qlfp,thp_nul
	bra.s	lg_exit
	lea	endrt,a1
	move.l	a1,$6c
	moveq	#jte.loff,d0
	bra.s	do_trpE

int3
	movem.l psf.reg,-(sp)		 ; save main working registers
	movem.l psf.oreg,-(a7)
	move.l	sms.sysb,a6		 ; system variable base
shd_poll

	move.l	jva_lkptr,d0
	beq.s	int3out
	move.l	d0,a5
	move.l	jva_kbrd(a5),d0
	beq.s	int3out
	move.l	d0,a5
	jsr	(a5)
int3out
	movem.l (a7)+,psf.oreg
	movem.l (sp)+,psf.reg		restore registers
endrt	rte

	end
