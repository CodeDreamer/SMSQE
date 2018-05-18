; base area SMSQ Q68 Drivers / 8 bit  Aurora

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_keys_q68'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'


header_base
	dc.l	q68_con-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-q68_con	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
;	 dc.w	 nm2-*-2,'SMSQ Q68 8 bit (Aurora) Console Driver'
	dc.w	30,'SMSQ Q68 16 bit Console Driver'
	dc.l	'    '
	dc.w	$200a
nm2
	section base

	xref	pt_setup

q68_con clr.l  d0
	move.b	sms.conf+sms_ismode,d1	 ; config "size" for cn_disp_size
	moveq	#ptd.08,d2		 ; 8 bit colour depth
	moveq	#ptm.aur8,d3		 ; aurora 8 bit driver
	cmp.b	#q68.aur8,d1		 ; do we install this driver?
	bne.s	no_inst 		 ; no
	move.l	d2,d4			 ; yes so make d4=d2
	jmp	pt_setup
no_inst clr.l	d4			 ; d4<>d2 : do not install driver
	jmp	pt_setup


;	 move.b  sms.conf+sms_ismode,d1   ; config screen size / mode
;	 moveq	 #ptd.08,d2		  ; 16 bit colour depth
;	 moveq	 #ptm.aur8,d3		   ; Q68/Q40 16 bit driver
;	 move.b  d1,d4
;	 lsr.b	 #1,d4			  ; mode 0 or 1
;	 tst.b	 d4
;	 beq.s	 ok
;	 move.b  #1,d4			  ; make sure mode 5 is handled ok
;ok	 mulu	 d2,d4			  ; mode ql or 16 bit
;	 jmp	 pt_setup


	end
