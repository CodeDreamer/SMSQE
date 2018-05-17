; SER receive enable / disable	 V2.00	   1994  Tony Tebby

	section ser

	xdef	ser_rxen
	xdef	ser_iopr
	xdef	ser_rxdi

	xref	iob_room
	xref	ql_hcmdn

	include 'dev8_keys_k'
	include 'dev8_mac_assert'
	include 'dev8_keys_par'
	include 'dev8_keys_sys'
	include 'dev8_keys_buf'

;+++
; SER input operation
;
;	a3 c  p SER linkage block
;	all other egisters preserved
;	status returned according to D0
;---
ser_iopr
	tst.b	prd_iact(a3)		 ; already ready?
	beq.s	sio_room		 ; ... no, must be xon / xoff
	tst.l	d0
	rts

sio_room
	move.l	d0,-(sp)
	move.l	prd_ibuf(a3),d0 	 ; any buffer?
	ble.s	srxe_exd0		 ; ... no, unusual this
	move.l	a2,-(sp)
	move.l	d0,a2
	jsr	iob_room		 ; enough room
	cmp.l	prd_room(a3),d0
	move.l	(sp)+,a2
	ble.s	srxe_exd0		 ; ... no
srxe_xon
	move.b	#k.xon,prd_xonf(a3)	 ; set xon char to be sent
srxe_exd0
	move.l	(sp)+,d0
	rts


;+++
; SER receive enable.
;
;	a3 c  p SER linkage block
;	all other egisters preserved
;	status returned according to D0
;---
ser_rxen
	move.l	d0,-(sp)
	move.l	a2,-(sp)
	tst.b	prd_hand(a3)
	ble.s	srxe_ena
	move.b	#k.xon,prd_xonf(a3)	 ; set xon char to be sent

srxe_ena
	move.l	prc_ibuf(a0),a2
	add.w	#buf_eoff,a2		 ; 'queue'

	st	prd_iact(a3)		 ; ready now
	moveq	#1,d0			 ; rx enable -1

ser_cmd
	movem.l d1/d2/a4/a5,-(sp)
	moveq	#0,d1
	move.b	prd_serx(a3),d1
	add.b	d1,d0			 ; command

	lsl.w	#2,d1
	lea	-4(a6,d1.w),a5
	move.l	a2,sys_qls1r(a5)	 ; set 'queue' pointer

	move.w	sr,-(sp)
	or.w	#$0700,sr
	jsr	ql_hcmdn		 ; send nibble
	move.w	(sp)+,sr
	movem.l (sp)+,d1/d2/a4/a5
	move.l	(sp)+,a2
	move.l	(sp)+,d0
	rts

;+++
; SER receive disable.
;
;	a3 c  p SER linkage block
;	all other registers preserved
;	status returned according to D0
;---
ser_rxdi
	move.l	d0,-(sp)
	move.l	a2,-(sp)
	sub.l	a2,a2			 ; no queue
	moveq	#3,d0			 ; rx disable -1
	sf	prd_iact(a3)		 ; not ready now
	bra.s	ser_cmd
	end
