; Base area SMSQ Q40 Drivers / 16 bit display
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'


*blatu macro blv
* xref blattu
* move.b [blv],-(sp)
* jsr	 blattu
* add.w  #2,sp
* endm
*
*blatul macro blv
* xref blattul
* move.l [blv],-(sp)
* jsr	 blattul
* addq.l #4,sp
* endm

header_base
	dc.l	qxl_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-qxl_con	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	26,'SMSQ QXL 16 bit CON Driver'
	dc.l	'    '
	dc.w	$200a

	section base

	xref	pt_setup

QXL_con
	tst.b	sms.conf+sms_128k	 ; small machine?
	bne.s	qcn_ok			 ; ... yes

	move.b	sms.conf+sms_issize,d1	 ; config screen size
	moveq	#ptd.16,d2		 ; 16 bit colour depth
	moveq	#ptm.pc16,d3		 ; i6 bit VESA (PC) driver
	move.b	sms.conf+sms_ismode,d4	 ; config screen colour depth
	jmp	pt_setup

qcn_ok
	moveq	#0,d0
	rts

	end
