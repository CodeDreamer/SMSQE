; (Super)GoldCard initialisation. Deciphered by Marcel Kilgus

	section sgc

	xdef	gl_init
	xdef	gl_rtc_status
	xdef	gl_rtc_fail

	xref	gl_proc

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sys'
	include 'dev8_sys_gold_keys'

; Output text on boot screen
smul_0
	suba.l	a0,a0
	moveq	#iob.smul,d0
	moveq	#-1,d3
	trap	#3
	rts

; (Super)GoldCard initialisation. Called by QDOS extension ROM code
gl_init
	move.w	glx_ptch+glk.card,d7	; card type
	bne.s	ini_banner		; ... Gold Card
	lea	super,a1		; "Super"
	moveq	#6,d2
	bsr.s	smul_0
ini_banner
	lea	$1000a,a1		; "Gold Card  Vx.xx",$0A
	moveq	#18,d2
	bsr.s	smul_0			; output ROM banner

	lea	sgx_work+sgo_rtcs,a0
	bsr.l	gl_rtc_status		; RTC status ok?
	bne.s	ini_rtc_fail		; ... no

	sf	sgx_work+sgo_prtc	; make sure we write to RTC (unprotected)
	moveq	#sms.rrtc,d0
	trap	#1
	moveq	#sms.srtc,d0		; set RTC again
	trap	#1

	lea	sgx_work+sgo_rtcs,a0
	bsr.l	gl_rtc_status		; RTC still ok?
	beq.s	ini_rtc_ok		; ... yes, get config out of it

ini_rtc_fail
	lea	rtc_fail,a1		; output RTC problem
	move.w	(a1)+,d2
	bsr.s	smul_0
	trap	#0
	moveq	#0,d0			; default is all config bits off
	bra.s	ini_config

; RTC is ok, read configuration out of it
ini_rtc_ok
	move.l	glx_ptch+glk.base,a2	; get hardware base address
	trap	#0
	ori.w	#$700,sr
	tst.w	d7			; card type
	bne.s	ini_rtc_gc

; Super Gold Card read RTC interrupt register. We use it for config storage!
	sf	glo_rtcc(a2)		; SGC RTC write sequence
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	#$30,glo_rtcc(a2)	; enable interrupt register
	move.b	glo_rtcs(a2),d0 	; read interrupt register
	sf	glo_rtcc(a2)		; SGC RTC write sequence
	sf	glo_dow(a2)
	sf	glo_d01(a2)
	move.b	#$10,glo_rtcc(a2)	; disable interrupt register
	bra.s	ini_config

; Gold Card read RTC interrupt register. We use it for config storage!
ini_rtc_gc
	move.b	#$30,glo_rtcc(a2)	; enable interrupt register
	move.b	glo_rtcs(a2),d0 	; read interrupt register
	move.b	#$10,(a2)		; disable interrupt register
ini_config
	lea	sgx_work+sgo_confg,a0	; configuration bits
	moveq	#3,d1			; 4 bits of config
ini_sloop
	lsl.b	#1,d0			; translate the bits into config bytes
	scs	(a0)+
	dbf	d1,ini_sloop

	tst.w	d7
	bne.s	ini_xdone		; for GC we're done here

	subq.l	#2,a0			; sgo_cscr2
	tst.b	(a0)
	beq.s	ini_scr2
	sf	sgo_scr2(a2)		; disable SCR2
	bra.s	ini_cache
ini_scr2
	tst.b	sgo_scr2(a2)		; enable SCR2

ini_cache
	sf	sgx_work+sgo_cache	; disable cache
ini_xdone
	andi.w	#$d8ff,sr
	moveq	#sms.info,d0
	trap	#1
	move.b	gl_proc,sys_ptyp(a0)
	cmpi.l	#$40000,sys_ramt(a0)	; 128KB RAM ("QL mode")?
	bne.s	ini_nslug		; ... no
	st	sgx_work+sgo_prtc	; ... yes, protect RTC in this case
	rts

ini_nslug
	move.l	#sgo.slugd,sgx_work+sgo_slug ; disable SLUG
	rts

gl_rtc_status
	move.l	d0,-(a7)
	move.b	(a0),d0
	not.b	d0
	cmp.b	1(a0),d0
	movem.l (a7)+,d0
	bne.s	gl_rtc_ok		; invalid value found, default to "ok"?
	tst.b	1(a0)			; $ff in case of failure
	rts

gl_rtc_ok
	move.w	#rtc.ok,(a0)
	cmp.b	d0,d0
	rts

gl_rtc_fail
	move.w	#rtc.fail,(a0)
	rts

; Prefix for ROM banner text in case of the SGC
super
	dc.w	'Super '

; RTC failed
rtc_fail
	dc.w	24
	dc.b	'Real time clock failure',$a

	end
