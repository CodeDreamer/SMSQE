; Base area SMSQmulator Drivers / 16 bit display v1.00 (c) W. Lenerz 2012


; all the (screen) modules are called in turn. If the screen code
; for one corresponds to the one set up in the config block,
; that one will be chosen. Screen codes are ptd.16 etc

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'


header_base
	dc.l	java_con-header_base	  ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-java_con	  ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	27,'SMSQ JAVA 16 bit CON Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

java_con
	move.b	sms.conf+sms_issize,d1	; configured screen size
	moveq	#ptd.16,d2		; I can hendle 16 bit colour depth
	moveq	#ptm.pc16,d3		; i6 bit VESA (PC) driver
	move.b	sms.conf+sms_ismode,d4	; configured screen colour depth
	jmp	pt_setup		; if d2=d4, this is the screen that will be chosen

	end
