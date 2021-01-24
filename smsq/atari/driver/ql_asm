; base area SMSQ ATARI ST Drivers / QL display

	section header

	xref	smsq_end

	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'

header_base
	dc.l	at_sql-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-at_sql 	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	20,'SMSQ Atari QL Driver'
	dc.l	'    '
	dc.w	$200a

select
	moveq	#sbl.load,d0		 ; assume load
	moveq	#sys.mmon,d1
	and.l	sbl_mtype(a5),d1	 ; monochrome monitor
	beq.s	sel_rts
	moveq	#sbl.noload,d0		 ; ... yes
sel_rts
	rts

	section base

	xref	pt_setup

at_sql
	sub.w	#24,sp
	move.l	sp,d1			 ; put size in d1
	bsr.s	ats_sdisp

	moveq	#ptd.ql,d2		 ; ql colour depth
	moveq	#ptm.ql4,d3		 ; ql mode 4(8) driver
	moveq	#ptd.ql,d4		 ; ql driver is required
	jsr	pt_setup
	add.w	#24,sp
	rts

ats_sdisp
	moveq	#sms.xtop,d0		 ; confi area is supervisor access only
	trap	#do.sms2

	lea	sms.conf+sms_idisp,a0	 ; get config disp size
	moveq	#5,d0
	move.l	d1,a1
ats_sloop
	clr.w	(a1)+
	move.w	(a0)+,(a1)+
	dbra	d0,ats_sloop
	rts

	end
