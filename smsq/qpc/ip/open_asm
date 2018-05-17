; Open IP	   V1.01				 2004	Marcel Kilgus

	section ip

	xdef	ip_open

	xref	io_ckchn

	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_qpc_ip_data'
	include 'dev8_smsq_qpc_keys'

stk.reg reg	d3/a0
stk.a0	equ	4

ip_open
	movem.l stk.reg,-(sp)
	move.l	#$dfdfdfdf,d0
	move.w	(a0)+,d2
	and.l	(a0)+,d0
	cmp.l	tcp_name,d0
	beq.s	ipo_found
	cmp.l	udp_name,d0
	beq.s	ipo_found
	cmp.l	sck_name,d0
	beq.s	ipo_sck
	moveq	#err.fdnf,d0
	bra.s	ipo_exit

tcp_name dc.b	'TCP_'
udp_name dc.b	'UDP_'
sck_name dc.b	'SCK_'

ipo_sck
	cmp.l	#4,d3			; is a channel id in d3?
	bls.s	ipo_found		; ... probably not

	movem.l d7/a0/a3,-(sp)
	moveq	#0,d0
	move.l	d3,a0			; check channel it
	jsr	io_ckchn
	tst.l	d0
	bmi.s	ipo_ipar
	cmp.l	#chn.id,chn_id(a0)	; really IP channel?
	bne.s	ipo_ipar		; ... no
	move.l	chn_data(a0),d3 	; data address for that channel
	movem.l (sp)+,d7/a0/a3

ipo_found
	moveq	#chn_end,d1
	move.w	mem.achp,a2
	jsr	(a2)
	bne.s	ipo_exit

	move.l	#chn.id,chn_id(a0)
	move.l	a0,a2			; remember chan-block
	move.l	stk.a0(sp),a1		; device name
	dc.w	qpc.ipopn		; returns internal ptr in a0
	bne.s	ipo_error

	move.l	a0,chn_data(a2) 	; remember internal ptr in chan block
	move.l	a2,stk.a0(sp)		; return allocated chan block in a0

ipo_exit
	movem.l (sp)+,stk.reg
	rts

ipo_ipar
	movem.l (sp)+,d7/a0/a3
	moveq	#err.ipar,d0
	bra.s	ipo_exit

ipo_error
	move.l	d0,-(sp)
	move.l	a2,a0
	move.w	mem.rchp,a2		; error, release CDB
	jsr	(a2)
	move.l	(sp)+,d0
	bra.s	ipo_exit

	end
