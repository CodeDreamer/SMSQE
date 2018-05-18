; Nasty Initialisation for QPC SMSQ/E  v1.03		     2006 Marcel Kilgus
;
; 2006-05-18  1.01  Set low (but hopefully still save) execution delay
; 2006-12-18  1.02  Fixed FP patch offsets
; 2017-12-09  1.03  Fixed SQRT offset (wl)

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
	dc.w	24,'SMSQ QPC Initialisation '
	dc.l	'    '
	dc.w	$200a


	section init

	xref	qpc_isrv
	xref	qpc_event_list

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
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'
	include 'dev8_mac_creg'

nasty_base
; The nasty initialisation requires a return to system state

	moveq	#sms.xtop,d0
	trap	#do.sms2		 ; do code until rts as extop

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

	lea	qpc_event_list,a1
	dc.w	qpc.inie		 ; initialise event vectors
	lea	qpc_isrv,a1
	move.l	a1,qpc_ivec		 ; and interrupt vector

	cmp.l	#$60000,sys_ramt(a6)
	sle	sms.conf+sms_128k	 ; set small machine <= 256k

	move.b	#5,sys_xdly(a6) 	 ; set hopefully safe execution delay

	dc.w	qpc.kbeep		 ; kill beep sound (if any)

	lea	qpc_scr_work,a0
	clr.l	qpc_scra(a0)		 ; no allocation yet
	clr.l	qpc_scrb(a0)		 ; ... so no base address
	move.b	#4,qpc_vql(a0)		 ; default to 4 colour for QL

	clr.l	qpc_kbd_link
	clr.l	qpc_spp_link

	bsr.s	qpc_patchfpu

	movea.l #qpc_schdp,a0		 ; link in own scheduler routine
	lea	qpc_sched(pc),a1
	move.l	a1,(a0)
	moveq	#sms.lshd,d0
	movea.l #qpc_schdl,a0
	trap	#$1

	and.w	#$f8ff,sr		 ; now we can enable interrupts
	rts

; DIRTY! This routine patches our own FP routines using a table
; Must be adapted when FP routines change
qpc_patchfpu
	movea.w $11c,a0 		 ; FP vector (points to a jump)
	movea.l 2(a0),a0		 ; Get address from jmp and use as base
	lea	qpc_patchtable(pc),a1
qpc_patchloop
	move.w	(a1)+,d0
	beq.s	qpc_endpatch
	move.w	(a1)+,d1
	cmp.w	(a0,d0.w),d1		 ; Original code ok?
	bne.s	qpc_endpatch		 ; No, just quit patching
	move.w	(a1)+,(a0,d0.w) 	 ; Patch new code
	bra.s	qpc_patchloop

qpc_endpatch
	rts

ib	equ	$ab00

qpc_patchtable
;		adr  ,origi,patch
	dc.w	$0a2c,$48e7,ib+0	 ; ADDD
	dc.w	$0a62,$48e7,ib+1	 ; ADD
	dc.w	$0a3c,$48e7,ib+2	 ; SUBD
	dc.w	$0a5e,$4eba,ib+3	 ; SUB
	dc.w	$0ad8,$3011,ib+4	 ; DOUBLE
	dc.w	$0aec,$5351,ib+5	 ; HALVE
	dc.w	$08ae,$48e7,ib+6	 ; DIVD
	dc.w	$08be,$48e7,ib+7	 ; DIV
	dc.w	$08b6,$4eba,ib+8	 ; 1/x
	dc.w	$0934,$48e7,ib+9	 ; MULD
	dc.w	$0948,$48e7,ib+10	 ; MUL
	dc.w	$093c,$48e7,ib+11	 ; x^2
;	 dc.w	 $09fc,$2341,ib+12	  ; SQRT
	dc.w	$0afe,$2029,ib+12	 ; SQRT
	dc.w	$06b0,$48e7,ib+13	 ; COS
	dc.w	$06bc,$48e7,ib+14	 ; SIN
	dc.w	$0702,$4eba,ib+15	 ; COT
	dc.w	$0714,$4eba,ib+16	 ; TAN
	dc.w	0

;+++
; QPC SMS2 polling interrupt server operating off the poll interrupt.
; See SHD_POLL.
;---
hw_poll
	or.w	#$0700,sr		 ; disble interrupts until sched or RTE
	clr.b	sys_plrq(a6)		 ; request serviced
	rts

;+++
; QPC scheduler loop.
;---
qpc_sched
	dc.w	qpc.idle		 ; now's a good time to idle if possible
	rts

	end
