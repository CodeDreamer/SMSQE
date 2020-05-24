; base area SMSQ Q68 Drivers   0.00 (C) W. Lenerz 2016
; 2020-03-27	1.01	check whether kbd read via interrutps is possible

	section header

	xref	smsq_end

header_base
	dc.l	gl_most-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_most	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	17,'SMSQ Q68  Drivers '
	dc.l	'    '
	dc.w	$200a


	section base

	xref	iob_smsq		 ; SMSQ IOB bits

	xref	q68_int2
	xref	q68_int2h

	xref	kbd_initiq68
	xref	kbd_initi
	xref	kbd_intr
	xref	mse_init
	xref	dev_init
	xref	nul_init
	xref	pipe_init
	xref	rd_init
	xref	iob_init
	xref	history_init
	xref	ssss_init
	xref	q68_spp_init

	xref	cpy_mmod

	include 'dev8_keys_qlv'
	include 'dev8_keys_q68'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_psf'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_gold_keys'
	include 'dev8_mac_assert'
	include 'dev8_keys_68000'

gl_most
	bra.l	start

	xdef	iou_dirshr
iou_dirshr dc.w 0	; shared directories supported	(RAM disk)

	section init
start
	jsr	ssss_init
	jsr	dev_init
	jsr	nul_init
	jsr	pipe_init
	jsr	rd_init
	jsr	iob_init
	jsr	history_init

; now the interrupt handlers, try to order them so that the one that will
; probably called the most (mouse, as it generates several interrupts per
; move) is called first. This means that it should be linked in last.
	jsr	q68_spp_init		; serial port,	will be last interrupts called
	st	kbd_status		; try  to use interrupts for keyboard
	move.b	kbd_status,d0
	btst	#7,d0			; can I use interrupts for keyboard?
	beq.s	no_intr 		;   ... no
	jsr	kbd_initi		;   ... yes, so generic smsq kbd queue poll
	jsr	kbd_intr		;	and set up kbd read via interrupt
	bra.s	common
no_intr jsr	kbd_initiq68		; both kbd fetch and read queue via poll
common	jsr	mse_init

sys_init
; When we have linked in the interrupt servers, we can enable the interrupts

	moveq	#sms.xtop,d0
	trap	#do.sms2
	clr.w	psf_sr(a5)		; we can clear interrupts on return

mmreg	reg	d1/a1/a2
new	movem.l mmreg,-(a7)
	lea	q68_int2h,a0
	jsr	cpy_mmod
	lea	exv_i2,a5
	move.l	4(a0),(a5)		; set interrupt level 2 routine address
	movem.l (a7)+,mmreg
poll
	lea	hw_poll,a0		; set hardware polling routine
	lea	sms.hpoll,a5		; and tidy up
	move.l	a0,(a5)
	rts

;+++
; Q68 SMSQ polling interrupt server operating off the frame interrupt.
; Clears the frame interupt (and, implicitly, the interface interrupt).
; See SHD_POLL.
;---
hw_poll
	moveq	#pc.intrf,d7
	or.b	sys_qlir(a6),d7 	 ; Im' not sure this makes sense for Q68
	move.b	d7,pc_intr		 ; clear offending interrupt
	rts

	end
