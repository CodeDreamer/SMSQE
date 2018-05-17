; Base area SMSQ system sprites

	section header

	xref	smsq_end

	include 'dev8_keys_con'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sys'

header_base
	dc.l	sys_sprt-header_base	; length of header
	dc.l	0			; module length unknown
	dc.l	smsq_end-sys_sprt	; loaded length
	dc.l	0			; checksum
	dc.l	0			; select
	dc.b	1			; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	20
	dc.w	'SMSQ System sprites '
	dc.l	'    '
	dc.w	$200a

	section base

	xref	sp_table

; Just add all sprites we have to the system sprite table.

sys_sprt
	moveq	#sms.xtop,d0
	trap	#do.smsq
	move.l	sys_clnk(a6),d0
	beq.s	ss_exit
	move.l	d0,a3
	move.l	pt_vecs(a3),a2

	lea	sp_table,a4
	move.l	a4,a5
	move.w	(a4)+,d5		; that many sprites
	moveq	#0,d1
ss_loop
	move.l	(a4)+,a1		; get pointer to sprite
	add.l	a5,a1
	jsr	pv_sspr(a2)
	addq.w	#1,d1
	cmp.w	d5,d1
	blt.s	ss_loop
ss_exit
	rts

	end
