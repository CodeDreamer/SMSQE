; base area SMSQ Q68 Drivers / QL display  0.00 W. Lenerz 2016

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_keys_q68'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_mac_assert'

header_base
	dc.l	gl_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_con 	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	20,'SMSQ Q68  CON Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

; d2  r  colour depth	(ptd.xxxx)
; d3  r  colour mode	(ptm.xxxx)
; d4  r  =d2 : install this driver , <> d2 : do not install this driver
gl_con
	move.b	sms.conf+sms_ismode,d1	; config screen size / mode
	moveq	#ptd.ql,d2		; ql colour depth
	moveq	#ptm.ql4,d3		; ql mode 4(8) driver
	move.w	d1,d0
	subq.w	#1,d0			; normal ql mode( or 8)?
	ble.s	inst			; yes, install this
	cmp.b	#4,d1			; big ql mode 4screen?
	beq.s	inst			; yes
	moveq	#-1,d4			; no, so don't install
	jmp	pt_setup

inst	move.l	d2,d4			; install this
	jmp	pt_setup


    

	move.b	sms.conf+sms_ismode,d1	 ; config screen size / mode
	moveq	#ptd.ql,d2		 ; ql colour depth
	moveq	#ptm.ql4,d3		 ; ql mode 4(8) driver
	move.l	d2,d4			 ; presume we want to install this mode
	move.b	d1,d0
	subq.b	#1,d0			 ; install?
	ble.s	inst1			  ; yes if small mode 8/4 colours
	subq.b	#q68.dl4-1,d2
	beq.s	inst1			  ; also if large mode 4
	moveq	#-1,d4			 ; do not install
inst1	 jmp	 pt_setup

	end
