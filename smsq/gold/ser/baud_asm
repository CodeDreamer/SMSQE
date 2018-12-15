; Gold Card Baud Rate	    1994	Tony Tebby

	section sms

	xdef	ser_baud

	xref	ql_hcmdb
	xref	ql_hcmdr

	include 'dev8_keys_qlhw'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_par'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

;+++
; Set Baud Rate
;
;	d1 c p	baud rate (n*5 or 1275) + 0, 1, 2
;
;	status return 0 or err.ipar
;---
ser_baud
sbd.reg reg	d1/d2/d3/d4/d5/d6/d7/a3/a4/a5
	movem.l sbd.reg,-(sp)
	moveq	#0,d5
	move.w	d1,d5
	move.l	d5,d1
	divu	#5,d5			 ; get port
	swap	d5
	cmp.w	#2,d5			 ; more than 2?
	bhi.s	sbd_ipar

	sub.w	d5,d1			 ; baud rate not including port
	cmp.w	#75,d1			 ; 75/1200
	beq.s	sbd_75_1200		 ; ... yes
	cmp.w	#1275,d1		 ; 1200/75
	beq.s	sbd_1200_75
	move.l	#1200,d4
	divu	d1,d4			 ; timeout
	addq.b	#2,d4

	divu	#150,d1 		 ; 300 is rate 6
					 ; 600 is rate 5
					 ; 1200 is rate 4
					 ; 2400 is rate 3
					 ; 4800 is rate 2
					 ; 9600 is rate 1
					 ; 19200 is rate 0

	moveq	#6,d7			 ; start at 300

sbd_look
	ror.w	#1,d1			 ; divide by 2, but keep rubbish
	cmp.w	#1,d1
	dbeq	d7,sbd_look
	bne.s	sbd_ipar		 ; ... tough

	swap	d1			 ; xmit baud in d7
	tst.w	d1
	bne.s	sbd_ipar
	move.w	d7,d6
	bra.s	sbd_set

sbd_75_1200
	moveq	#7,d6			 ; rx 75
	moveq	#4,d7			 ; tx 1200
	moveq	#2,d4
	bra.s	sbd_split

sbd_1200_75
	moveq	#4,d6			 ; rx 1200
	moveq	#7,d7			 ; tx 75
	moveq	#17,d4

sbd_split
	assert	sys.herm,1
	btst	#0,sys_mtyp(a6) 	 ; Hermes?
	beq.s	sbd_ipar		 ; ... no

sbd_set
	move.l	sms.qlser,a3
	cmp.b	#2,d5			 ; port 2 only?
	beq.s	sbd_set2
	moveq	#0,d1
	moveq	#$40,d3 		 ; Hermes set SER1 baud parameter
	bsr.s	sbd_setp

sbd_set2
	cmp.b	#1,d5			 ; port 1 only?
	beq.s	sbd_ok
	add.w	#prd_ser2-prd_ser1,a3
	moveq	#1<<pc..sern,d1
	moveq	#$ffffffc0,d3		 ; Hermes set SER2 baud parameter
	bsr.s	sbd_setp

	tst.b	d5			 ; set both?
	bne.s	sbd_ok			 ; ... no

	bsr.s	sbd_sboth		 ; ... yes

sbd_ok
	moveq	#0,d0
sbd_exit
	movem.l (sp)+,sbd.reg
	rts
sbd_ipar
	moveq	#err.ipar,d0
	bra.s	sbd_exit

sbd_setp
	move.w	sr,-(sp)
	or.w	#$0700,sr

	move.b	d7,prd_txbd(a3) 	 ; set baud rates in linkage
	move.b	d6,prd_rxbd(a3)
	move.b	d4,prd_timo(a3) 	 ; and serial timeout

	moveq	#1<<pc..serb+1<<pc..sern,d0
	and.b	sys_tmod(a6),d0
	bclr	#pc..serb,d0		 ; ser mode?
	beq.s	sbd_sxmit		 ; ... yes, set noramlly
	moveq	#1<<pc..sern,d0
	and.b	sys_tmsv(a6),d0 	 ; set in saved transmit mode byte
	cmp.b	d1,d0			 ; this port is save xmit?
	bne.s	sbd_srx 		 ; ... no, set RX
	moveq	#$fffffff8,d0
	and.b	sys_tmsv(a6),d0
	or.b	d7,d0
	move.b	d0,sys_tmsv(a6)
	bra.s	sbd_srx
sbd_sxmit
	cmp.b	d1,d0			 ; this port active?
	bne.s	sbd_srx 		 ; ... no, set RX
	moveq	#$fffffff8,d0
	and.b	sys_tmod(a6),d0
	or.b	d7,d0
	move.b	d0,sys_tmod(a6)
	move.b	d0,pc_tctrl

sbd_srx
	tst.b	d5			 ; both ports?
	beq.s	sbd_ie			 ; ... yes
	assert	sys.herm,1
	btst	#0,sys_mtyp(a6) 	 ; Hermes?
	beq.s	sbd_ipc
	moveq	#$ffffffcd,d0		 ; Hermes set baud
	jsr	ql_hcmdb
	move.b	d3,d0			 ; port (msb) and protect
	or.b	d6,d0			 ; baud rate
	jsr	ql_hcmdb
	moveq	#8,d2
	jsr	ql_hcmdr
	bra.s	sbd_ie

sbd_sboth
	move.w	sr,-(sp)
	or.w	#$0700,sr

sbd_ipc
	moveq	#$ffffffd0,d0		 ; baud is command D
	or.b	d6,d0			 ; include baud rate
	jsr	ql_hcmdb		 ; do command

sbd_ie
	move.w	(sp)+,sr
	rts

	end
