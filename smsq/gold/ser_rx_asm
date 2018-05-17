; Gold Card SER receive  V2.03	  1994    Tony Tebby

	section ser

	xdef	ser_rx

	xref	iob_room
	xref	iob_eof
	xref	iob_pbyt

	xref	ql_hcmdn
	xref	ql_hcmdr

	include 'dev8_keys_par'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_base_keys'
;+++
; SER receive
;
;	d0 c  s 0 = port 1,  1 = port 2
;	d1    s
;	d2    s
;	d3    s
;	d4 c  s IPC command nibble
;	a2    s
;	a3    s
;	a4    s
;	a5    s
;
;---
ser_rx
	move.l	sms.qlser,a3
	tst.w	d0			 ; 1 or 2?
	beq.s	srx_ckbuff		 ; ... 1
	add.w	#prd_ser2-prd_ser1,a3	 ; ... 2
srx_ckbuff
	move.l	prd_ibuf(a3),a2 	 ; any input buffer?
	move.l	a2,d0
	ble.s	srx_do
	jsr	iob_room
	move.l	d0,d3
	moveq	#$32,d0 		 ; allow up to 32 byte transfer
	cmp.l	d0,d3
	blt.l	srx_nroom		 ; ... no room

srx_do
	move.b	d4,d0
	jsr	ql_hcmdn		 ; 1 nibble command
	moveq	#8,d2
	jsr	ql_hcmdr		 ; read 1 byte
	moveq	#$3f,d4
	and.b	d1,d4			 ; number of bytes to read
	sub.l	d4,d3			 ; space left
	bra.s	srx_eloop

srx_loop
	moveq	#8,d2
	jsr	ql_hcmdr		 ; read 1 byte
	move.l	a2,d0			 ; any buffer?
	ble.s	srx_eloop		 ; ... no

	tst.b	prd_hand(a3)		 ; xoff?
	ble.s	srx_ibuf		 ; ... no
	assert	k.xon-$11,k.xoff-$13,0
	moveq	#$7d,d0
	and.b	d1,d0
	cmp.b	#k.xon,d0		 ; xon or xoff?
	bne.s	srx_ibuf		 ; ... no
	btst	#1,d1			 ; on or off
	sne	prd_xoff(a3)		 ; ... yes
	bra.s	srx_eloop

srx_ibuf
	move.b	prb_fz(a2),d0		 ; <FF> or CTRL Z
	subq.b	#1,d0
	ble.s	srx_ckcr		 ; ... not CTRL Z
	move.b	d1,d0
	tst.b	prb_prty(a2)		 ; parity?
	beq.s	srx_ckcz		 ; ... no
	bclr	#7,d0			 ; ... ignore it
srx_ckcz
	cmp.b	#26,d0			 ; is it CTRL Z?
	bne.s	srx_ckcr
	jsr	iob_eof 		 ; mark end of file
	st	prd_ibuf(a3)		 ; no input buffer pointer now
	bra.s	srx_eloop

srx_ckcr
	move.b	prb_cr(a2),d0		 ; cr to lf?
	beq.s	srx_pbyt		 ; ... no
	cmp.b	d0,d1			 ; cr?
	bne.s	srx_pbyt		 ; ... no
	move.b	prb_lf(a2),d1		 ; ... yes
	beq.s	srx_eloop		 ; cr ignored

srx_pbyt
	jsr	iob_pbyt		 ; put byte in buffer

srx_eloop
	dbra	d4,srx_loop

	cmp.l	prd_room(a3),d3 	 ; enough room left?
	bgt.s	srx_exit		 ; ... plenty
	tst.b	prd_hand(a3)		 ; handshake?
	ble.s	srx_exit		 ; not XON/XOFF

	move.b	#k.xoff,prd_xonf(a3)	 ; xoff to be sent

srx_exit
	rts

srx_nroom
	tst.b	prd_hand(a3)		 ; no room - is it XON XOFF?
	ble.s	srx_exit		 ; ... no
	tst.b	prd_iact(a3)		 ; alredy inactive?
	beq.s	srx_exit
	move.b	#k.xoff,prd_xonf(a3)	 ; send xoff (this is called from poll)
	bra.s	srx_exit		 ; xoff will be sent on next sched
	end
