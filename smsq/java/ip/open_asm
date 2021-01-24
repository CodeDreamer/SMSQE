; Open IP	   V1.03				 2004	Marcel Kilgus
; 1.03 check for UDP (wl)				  2020	W. Lenerz
; 1.02 adapted for smsqmulator (wl)

	section ip

	xdef	ip_open
	xdef	udp_name
	xdef	udd_name
	xref	io_ckchn

	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_java_ip_data'
	include 'dev8_keys_java'

tregs	reg	d3/d7/a0    ; if you change this also change the code at
			    ; ipo_set and in java, both rely on a0 being at 8(a7)

tcp_name dc.b	'TCP_'
udp_name dc.b	'UDP_'
sck_name dc.b	'SCK_'
udd_name dc.b	'UDD_'
     
ip_open
	movem.l tregs,-(sp)
	move.l	#$dfdfdfdf,d6
	and.l	2(a0),d6
	cmp.l	sck_name,d6		; is it a socket?
	bne.s	ipo_open		; no, try to open
ipo_sck
	cmp.l	#4,d3			; is a channel id in d3?
	bls.s	ipo_open		; ... probably not

; here this is an "accept" call, d3 is not an open key but contains
; the channel ID of a previously opened channel ("open" for SCK channels only)
	movem.l d7/a0/a3,-(sp)
	moveq	#0,d0
	move.l	d3,a0			; check channel ID
	jsr	io_ckchn		; on return A0 = channel address
	tst.l	d0
	bmi.s	ipo_ipar
	cmp.l	#chn.id,chn_id(a0)	; really IP channel?
	bne.s	ipo_ipar		; ... no
	move.l	chn_data(a0),d3 	; (negative) key in java map for that socket
	movem.l (sp)+,d7/a0/a3

ipo_open
	moveq	#jt9.opn,d0
	cmp.l	udp_name,d6
	beq.s	neg_it
	cmp.l	udd_name,d6
	bne.s	t1
neg_it	neg.l	d0
t1	dc.w	jva.trp9		; open channel now, D7 = key returned

	bne.s	ipo_exit		; oops, couldn't open

	moveq	#chn_end,d1
	move.w	mem.achp,a0		; get memory for channel block
	jsr	(a0)
	bne.s	ipo_exit		; oops	(leaves useless socket in map)
					; but this isn't going to happen often!
	move.l	#chn.id,chn_id(a0)	; say that this is one of my devices
	move.l	d6,chn_typ(a0)		; what kind of device
	move.l	d7,chn_data(a0) 	; put negative key into channel block
ipo_set move.l	a0,8(sp)		; replace A0...

ipo_exit
	movem.l (sp)+,tregs		; ... now
	rts

ipo_ipar				; bad channel ID on "accept" call.
	movem.l (sp)+,d7/a0/a3
	moveq	#err.ipar,d0
	bra.s	ipo_exit

	end
