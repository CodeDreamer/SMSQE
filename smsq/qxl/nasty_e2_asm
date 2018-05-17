; Nasty Initialisation for QXL SMSQ/E
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)


	section header

	xref	smsq_end

header_base
	dc.l	nasty_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-nasty_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	24,'SMSQ QXL Initialisation '
	dc.l	'    '
	dc.w	$200a


	section init

	xref	qxl_speed
	xref	qxl_isrv
	xref	qxl_queue_set
	xref	qxl_mess_add

	xref	sms_hdop
	xref	sms_rrtc
	xref	sms_srtc
	xref	sms_artc

	xref	gu_achpp

	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'
	include 'dev8_mac_creg'

*blat macro blv
* xref blatt
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm
*
*blatl macro blv
* xref blattl
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

nasty_base
; bsr xx
; bra.s *
xx

; The nasty initialisation requires a return to system state

	moveq	#sms.xtop,d0
	trap	#do.sms2		 ; do code until rts as extop

; blat #$55

	clr.w	psf_sr(a5)		 ; enable interrupts on return

	st	sys_castt(a6)		 ; keep caches disabled

	lea	hw_poll,a0
	move.l	a0,sms.hpoll		 ; poll tidy up

	lea	sms_hdop,a0
	move.l	a0,sms.t1tab+sms.hdop*4  ; set hdop

	assert	sms.rrtc,sms.srtc-1,sms.artc-2
	lea	sms.t1tab+sms.rrtc*4,a1  ; set rtc
	lea	sms_rrtc,a0
	move.l	a0,(a1)+
	lea	sms_srtc,a0
	move.l	a0,(a1)+
	lea	sms_artc,a0
	move.l	a0,(a1)+

	assert	qxl_ninl,0		 ; set ninl to 0
	move.l	#$00030000,0		 ; and address 0 to = QL address 0
	assert	qxl_ninh,4
	st	qxl_ninh

	move.b	#qxl.intn,qxl_intn	 ; set vectored interrupt number
	lea	qxl_isrv,a1
	move.l	a1,qxl_ivec		 ; and interrupt vector

	moveq	#0,d0
	move.b	sms.conf+sms_clock-8,d0
	move.w	d0,qxl_clock

	cmp.l	#$60000,sys_ramt(a6)
	sle	sms.conf+sms_128k	 ; set small machine <= 256k

	clr.w	qxl_junk		 ; clear junk word
	clr.w	qxl_mtick_count

	move.l	#qxl_qxpc.alc+qxl_qxpc.ext+qxl_qxpc.qu+qxl_pcqx.alc+qxl_scr.work+qxl.message,d1
	move.l	sms.usetop,d2
	move.l	d2,d0
	sub.l	sys_ramt(a6),d0 	 ; enough room above RAMTOP?
	cmp.l	d0,d1
	bgt.s	qns_respr
	sub.l	d1,d2			 ; put all the buffers etc below usetop
	and.w	#$fff0,d2
	move.l	d2,a0
	bra.s	qns_setbuff

qns_respr
	moveq	#sms.arpa,d0		 ; put all the buffers etc in the RP
	trap	#do.sms2

qns_setbuff
	move.l	a0,a3			 ; base of buffer area
	add.w	#qxl_qxpc.ext,a0

;; tst.b $2817f
;; beq.s xx2
;; blatl  a0
;;xx2

	move.l	a0,qxl_qxpc_mess	 ; qxl-pc buffer
	move.l	a0,qxl_qxpc_eom(a0)	 ; empty
	lea	qxl_qxpc.alc-$8(a0),a1
	move.l	a1,qxl_qxpc_eob(a0)	 ; set limit
	add.w	#qxl_qxpc.alc,a0

	move.l	a0,qxl_qxpc_qu
	lea	qxl_qxpc.qu(a0),a2
	jsr	qxl_queue_set		 ; set up queue
	move.l	a2,a0

	move.l	a0,qxl_pcqx_mess	 ; pc-qxl buffer
	add.w	#qxl_pcqx.alc,a0

	move.l	a0,qxl_scr_work 	 ; screen work area (zero size screen)
	move.l	sys_ramt(a6),a1
	cmp.l	a1,a3			 ; buffer area above RAMTOP?
	ble.s	qns_noscrn
	move.l	a1,qxl_scrb(a0) 	 ; set screen base to RAMTOP
	sub.l	a1,a3
	move.l	a3,qxl_scra(a0)
	bra.s	qns_ssbits
qns_noscrn
	clr.l	qxl_scra(a0)		 ; no allocation yet
	clr.l	qxl_scrb(a0)		 ; ... so no base address
qns_ssbits
	move.b	#4,qxl_vql(a0)		 ; default to 4 colour for QL
	move.w	#qxl.vchek<<8+qxl.vcopy,qxl_vchek(a0) ; initial scan bits

	add.w	#qxl_scr.work,a0


	move.l	a0,qxl_message
	clr.w	qxl_ms_pcset(a0)	 ; no PC setup message yet
	clr.l	qxl_ms_len+qxl_ms_vmode(a0) ; no vga mode message
	clr.l	qxl_ms_len+qxl_ms_date(a0)  ; no date message
	clr.l	qxl_ms_len+qxl_ms_beep(a0)  ; no beep message
	clr.l	qxl_ms_len+qxl_ms_mouse(a0) ; clear mouse command
	clr.l	qxl_ms_mouse+qxm_dx(a0)
	clr.l	qxl_ms_len+qxl_ms_port(a0)  ; no port message
	clr.l	qxl_ms_len+qxl_ms_flow(a0)  ; no flow control message
	clr.l	qxl_ms_len+qxl_ms_phys(a0)  ; no physical transaction
	clr.l	qxl_ms_len+qxl_ms_vgap(a0)  ; no VGA palette message
	move.w	#1,qxl_ms_beep+qxl_ms_spare(a0) ; kill beep
	clr.l	qxl_kbd_link
	clr.l	qxl_spp_link

; blat #$81
; blatl qxl_qxpc_mess
; blat #$82
; blatl qxl_pcqx_mess
; blat #$83
; blatl qxl_qxpc_qu
; move.l qxl_qxpc_qu,a1
; blat #$84
; blatl (a1)
; blatl 4(a1)
; blatl 8(a1)
; blatl $c(a1)
; blat #$85
; blatl qxl_scr_work
; blat #$86
; blatl qxl_message

	st	qxl_junk		 ; junk incoming messages

	and.w	#$f8ff,sr		 ; now we can enable interrupts

	jsr	qxl_speed		 ; check speed

	move.l	qxl_message,a0	  ;;; strictly unnecessary

	lea	qxl_ms_pcset+6(a0),a1
	clr.w	(a1)
	move.l	#4<<16+qxm.rsetup<<8,-(a1)

	jsr	qxl_mess_add		 ; send request for PC setup message
qns_wait
	tst.w	qxl_ms_pcset(a0)	 ; and wait for PC setup message
	beq.s	qns_wait

	sf	qxl_junk		 ; accept incoming messages

; blat (a0)
; blat 7(a0)

	rts

;+++
; QXL SMS2 polling interrupt server operating off the poll interrupt.
; See SHD_POLL.
;---
hw_poll
;  subq.w  #1,$20004
;  bgt.s   xx
;  move.w  #50,$20004
;  not.w   $20006
;xx
	or.w	#$0700,sr		 ; disble interrupts until sched or RTE
	clr.b	sys_plrq(a6)		 ; request serviced
	rts

	end
