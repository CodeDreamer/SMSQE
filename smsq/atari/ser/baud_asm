; Atari Baud Rate       1991	    Tony Tebby	 QJUMP

	section sms

	xdef	at_baud

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_scc'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
;+++
; Set Baud Rate
;
;	d1 c p	baud rate (n*10) + 0, 1, 2, 3, 4
;
;	status return 0 or err.ipar
;---
at_baud
qbd.reg reg	d1/d2/d7/a1
	movem.l qbd.reg,-(sp)
	move.l	d1,d0
	divu	#19200,d0		 ; check if n*19200
	swap	d0
	subq.w	#4,d0
	bls.s	qbd_port

	move.l	d1,d0			 ; check if 125000 or 83333
	sub.l	#83333,d0
	cmp.l	#4,d0
	bls.s	qbd_port_sel
	sub.l	#125000-83333,d0
	subq.l	#4,d0
	bls.s	qbd_port

	and.l	#$ffff,d1		 ; make word only

qbd_port
	move.l	d1,d0
	divu	#10,d0			 ; get port
	clr.w	d0
	swap	d0

qbd_port_sel
	sub.l	d0,d1			 ; baud rate not including port

	subq.w	#4,d0
	bhi.l	qbd_ipar
	beq.s	qbd_4			 ; port 4

	addq.w	#2,d0			 ; port 1, 2 or 3?
	bgt.s	qbd_3			 ; port 3
	beq.s	qbd_2			 ; port 2

; SER 1 MFP

	lea	at_mfp+mfp_ddat,a1	 ; timer D
	bra.s	qbd13_rate

; SER 3 MFP2

qbd_3
	moveq	#sys.mtyp,d0
	and.b	sys_mtyp(a6),d0 	 ; machine type
	cmp.b	#sys.mtt,d0		 ; tt
	bne.l	qbd_ipar		 ; ... no

	lea	at_mfp2+mfp_ddat,a1	  ; timer D

qbd13_rate
	moveq	#1,d0			 ; assume highest speed
	tst.w	d1			 ; any rate?
	beq.s	qbd1_set

	move.w	#19200*2,d0		 ; 19200 is rate 1
	divu	d1,d0
	addq.w	#1,d0			 ; round
	lsr.w	#1,d0
qbd1_set
	move.b	d0,(a1) 		 ; set rate
	bra.l	qbd_exok

; SER 2 SCC channel B

qbd_2
	move.w	sr,d7
	lea	scc_ctrb,a1
	moveq	#sccm.lstp+sccm.cl16,d2  ; divide by 16

	moveq	#sys.mtyp,d0
	and.b	sys_mtyp(a6),d0 	 ; machine type
	subq.b	#sys.mmste,d0		 ; mega ste
	blt.l	qbd_ipar		 ; ... no

	move.l	d1,d0
	beq.s	qbd_2max

	divu	#38400,d0		 ; for direct, check multiple of 38400
	swap	d0
	tst.w	d0			 ; remainder?
	beq.s	qbd_2dir		 ; ... no

qbd_2mste
	move.l	#8000000/32*2,d0	 ; 250000 is rate 1 (unobtainable)
	bra.s	qbd24_setr		 ; ... yes

qbd_2max
	moveq	#1,d0
	swap	d0			 ; rate 1
qbd_2dir
	moveq	#sccc.tctr+sccc.rctr,d1  ; clock direct from TRxCB
	swap	d0
	subq.w	#4,d0			 ; 38400*4?
	beq.s	qbd24_sets		 ; ... yes
	bgt.s	qbd_ipar2
	moveq	#$ffffff00+sccm.lstp+sccm.cl32,d2  ; divide by 32
	addq.w	#2,d0			 ; 38400*2?
	beq.s	qbd24_sets		 ; ... yes
qbd_ipar2
	bgt.s	qbd_ipar
	moveq	#$ffffff00+sccm.lstp+sccm.cl64,d2  ; divide by 64
	bra.s	qbd24_sets

; SER 4 SCC channel A

qbd_4
	moveq	#sccm.lstp+sccm.cl16,d2   ; divide by 16

	moveq	#sys.mtyp,d0
	and.b	sys_mtyp(a6),d0 	 ; machine type
	subq.b	#sys.mmste,d0		 ; mega ste
	blt.s	qbd_ipar		 ; ... no

	move.w	sr,d7
	lea	scc_ctra,a1
	move.l	#3672000/32*2,d0	 ; 114600 (app) is rate 1 (unobtainable)
	tst.w	d1			 ; any rate?
	bne.s	qbd24_setr		 ; ... yes

	moveq	#sccc.tcrt+sccc.rcrt,d1  ; clock direct from RTxCA
	bra.s	qbd24_sets		 ; set source

qbd24_setr
	add.l	d1,d0			 ; round
	lsr.l	#1,d0
qbd24_dchk
	cmp.l	#$ffff,d1		 ; to large for divu?
	bls.s	qbd24_div

	lsr.l	#3,d0
	lsr.l	#3,d1

qbd24_div
	divu	d1,d0
	subq.w	#2,d0			 ; -2 for counter!!
	bge.s	qbd24_setb

	moveq	#0,d0			 ; highest rate
qbd24_setb
	moveq	#sccc.tcbd+sccc.rcbd,d1  ; clock from baud rate gen
	move.w	#$2700,sr		 ; atomic accessing please
	move.b	#scc_divl,(a1)
	move.b	d0,(a1) 		 ; set rate lsb
	move.w	d7,sr
	lsr.w	#8,d0
	move.w	#$2700,sr		 ; atomic accessing please
	move.b	#scc_divh,(a1)
	move.b	d0,(a1) 		 ; set rate msb
	move.w	d7,sr
qbd24_sets
	move.w	#$2700,sr		 ; atomic accessing please
	move.b	#scc_wclk,(a1)
	move.b	d1,(a1) 		 ; set clock source
	move.w	d7,sr
	move.w	#$2700,sr		 ; atomic accessing please
	move.b	#scc_wmode,(a1)
	move.b	d2,(a1) 		 ; set divider
	move.w	d7,sr
qbd_exok
	moveq	#0,d0
qbd_exit
	movem.l (sp)+,qbd.reg
	rts

qbd_ipar
	moveq	#err.ipar,d0
	bra.s	qbd_exit



	end
