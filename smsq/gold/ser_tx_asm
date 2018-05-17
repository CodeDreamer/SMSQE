; Gold Card SER transmit   V2.03    1994    Tony Tebby

	section ser

	xdef	ser_tx
	xdef	ser_sched
	xdef	ser_oopr
	xdef	ser_oact

	xref	iob_room
	xref	iob_gbps

	include 'dev8_keys_k'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_sys'
	include 'dev8_keys_par'
	include 'dev8_mac_assert'
	include 'dev8_smsq_smsq_base_keys'

ser_oact
ser_oopr
	btst	#pc..txfl,pc_ipcrd	 ; buffer full?
	bne.s	sto_exd0		 ; ... yes
	movem.l d0/d1/d3/d4/d7/a2/a3,-(sp)
	bsr.s	ser_tx
	movem.l (sp)+,d0/d1/d3/d4/d7/a2/a3
sto_exd0
	tst.l	d0
	rts
;+++
; SER scheduler transmit
;
;	d0    s
;	d1    s
;	d3 c  s number of missing polls
;	d4    s
;	d7    s
;	a2    s
;	a3    s
;
;---
ser_sched
	btst	#pc..serb,sys_tmod(a6)
	bne.s	ssh_rts 		 ; not in serial mode
	btst	#pc..txfl,pc_ipcrd	 ; buffer full?
	bne.s	ssh_rts 		 ; ... yes
	move.w	sr,-(sp)
	or.w	#$0700,sr
	bsr.s	ser_tx			 ; try xmit
	bpl.s	ssh_swap		 ; swap ports
ssh_exit
	move.w	(sp)+,sr
ssh_rts
	rts

ssh_swap
	add.b	d3,d3
	sub.b	d3,sys_stmo(a6) 	 ; decrement timer
	bge.s	ssh_exit		 ; not timed out yet
	st	sys_stmo(a6)		 ; always timed out
	move.l	sms.qlser,a3
	moveq	#pc..dtr1,d4
	moveq	#$fffffff8,d1		 ; get rid of old baud rate
	and.b	sys_tmod(a6),d1
	bchg	#pc..sern,d1		 ; which serial port is it now
	bne.s	ssh_buff		 ; ... 1
	add.w	#prd_ser2-prd_ser1,a3	 ; ... 2
	moveq	#pc..cts2,d4
ssh_buff
	move.l	prd_obuf(a3),d0 	 ; output buffer
	ble.s	ssh_exit		 ; ... none
	move.l	d0,a2

	tst.b	prd_hand(a3)		 ; hardware handshake?
	bge.s	ssh_setmd		 ; ... no
	btst	d4,pc_ipcrd		 ; handshake
	bne.s	ssh_exit		 ; ... wait

ssh_setmd
	or.b	prd_txbd(a3),d1 	 ; set baud rate for this one
	move.b	d1,sys_tmod(a6)
	move.b	d1,pc_tctrl		 ; swap ports
	bsr.s	stx_wbyte		 ; send byte
	bra.s	ssh_exit		 ; done

;+++
; SER interrupt transmit
;
;	d0    s
;	d1    s
;	d4    s
;	d7    s
;	a2    s
;	a3    s
;
;---
ser_tx
	btst	#pc..serb,sys_tmod(a6)
	bne.l	stx_rtok		 ; not in serial mode
	btst	#pc..txfl,pc_ipcrd	 ; buffer full?
	bne.l	stx_rtok		 ; ... yes
	move.l	sms.qlser,a3
	moveq	#pc..dtr1,d4
	btst	#pc..sern,sys_tmod(a6)	 ; which serial port currently xmit
	beq.s	stx_buff		 ; ... 1
	add.w	#prd_ser2-prd_ser1,a3	 ; ... 2
	moveq	#pc..cts2,d4
stx_buff
	move.l	prd_obuf(a3),d0 	 ; output buffer
	ble.l	stx_swap		 ; ... none
	move.l	d0,a2

	tst.b	prd_hand(a3)		 ; hardware handshake?
	bge.s	stx_wbyte		 ; ... no
	btst	d4,pc_ipcrd		 ; handshake
	bne.s	stx_eofck		 ; ... wait

stx_wbyte
	move.l	a2,d0
	move.w	prd_stxp(a3),d1 	 ; pause for extra stop bits
	lea	$20000,a2
	move.b	(a2),d7
stx_wait
	move.b	d7,(a2) 		 ; predictable QL wait
	dbra	d1,stx_wait
	move.l	d0,a2

	tst.b	prd_hand(a3)		 ; hardware handshake?
	bge.s	stx_nbyte		 ; ... no
	btst	d4,pc_ipcrd		 ; handshake
	bne.s	stx_eofck		 ; ... wait

stx_nbyte
	move.b	prd_xonf(a3),d1 	 ; xon/xoff to be sent?
	beq.s	stx_xoff
	btst	#1,d1			 ; on or off
	seq	prd_iact(a3)		 ; ... yes
	bra.s	stx_sbcx		 ; ... send it
stx_xoff
	tst.b	prd_xoff(a3)		 ; transmit xoffed?
	bne.s	stx_swap		 ; .. yes
stx_gbyte
	moveq	#0,d7
	move.b	prb_prty(a2),d7
	jsr	iob_gbps		 ; get byte with parity set
	beq.s	stx_sbyte		 ; ... something
	blt.s	stx_swap		 ; ... nothing
					 ; ... end of file
	subq.b	#1,d1			 ; is ff required?
	bge.s	stx_eof
	move.l	prd_obuf(a3),d0 	 ; ... no, try next buffer
	move.l	d0,a2
	bgt.s	stx_gbyte		 ; OK
	bra.s	stx_swap		 ; try other port

stx_eof
	bgt.s	stx_cz			 ; ... but really it is CTRL Z
	moveq	#k.ff,d1	  
	bra.s	stx_sbyte		 ; send ff

stx_cz
	moveq	#26,d1			 ; send CTRL Z

stx_sbcx
	clr.b	prd_xonf(a3)		 ; no xon/xoff to send now
stx_sbyte
	move.b	d1,pc_tdata		 ; send byte
	move.b	prd_timo(a3),sys_stmo(a6) ; and timeout
stx_rtok
	moveq	#0,d0
	rts

stx_eofck
 ;;; could put end of file check here
stx_swap
	moveq	#1,d0
	rts
	end
