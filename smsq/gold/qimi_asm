; Gold Card Qimi Driver        V2.00    1998  Tony Tebby

	section header

	xref	smsq_end

	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'

header_base
	dc.l	gl_qimi-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-gl_qimi	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	qimi_select-header_base  ; select in auto test
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	16,'SMSQ QIMI Driver'
	dc.l	'    '
	dc.w	$200a


qimi_select
	tst.b	sms_nqimi+sms.conf	 ; can it be qimi?
	bne.s	qimi_noload
	moveq	#sbl.load,d0
	rts
qimi_noload
	moveq	#sbl.noload,d0
	rts

	section base

	xref	pt_button3

	include dev8_keys_sys
	include dev8_keys_iod
	include dev8_keys_qdos_sms
	include dev8_keys_con

;	QIMI hardware

mi_quad  equ	$1bfbc	0001 10xx xxx1 x01x xx0x
mi_butt  equ	$1bf9c	0001 10xx xxx1 x00x xx0x == 18000 if board absent
mi_clear equ	$1bfbe	0001 10xx xxx1 x01x xx1x

mi.intxy equ	%00100100
mi..intx equ	2
mi..dirx equ	4
mi..inty equ	5
mi..diry equ	0

mi.buttn equ	%00110000
mi..buts equ	4


gl_qimi
	moveq	#sms.xtop,d0
	trap	#do.sms2
	moveq	#sms.rrtc,d0		 ; wait for clock to change
	trap	#1
	move.l	d1,d4
qin_wait
	moveq	#sms.rrtc,d0		 ; try again
	trap	#1
	cmp.l	d1,d4			 ; the same?
	bne.s	qin_ckbut
	cmp.w	#60,sys_pict(a6)	 ; ... have we waited long enough?
	blt.s	qin_wait
qin_ckbut
	move.l	d1,d4			 ; save time
	moveq	#0,d1			 ; set zero time
	moveq	#sms.srtc,d0
	trap	#1

	move.w	mi_butt,d6		 ; are there buttons?

	move.l	d4,d1			 ; reset time
	moveq	#sms.srtc,d0
	trap	#1

	tst.w	d6			 ; QIMI board present?
	beq.s	ptin_rts		 ; ... no

	move.l	sys_clnk(a6),a3

	lea	qimi_poll(pc),a1	 ; set polling routine address
	move.l	a1,iod_plad(a3)
	lea	iod_pllk(a3),a0 	 ; link in polling routine
	moveq	#sms.lpol,d0
	trap	#1

	lea	qimi_ext(pc),a1 	 ; set external interrupt address
	move.l	a1,iod_xiad(a3)
	lea	iod_xilk(a3),a0 	 ; link in external interrupt
	moveq	#sms.lexi,d0
	trap	#1

	tst.b	mi_clear		 ; enable interrupts

ptin_rts
	rts

	page

; external interrupt server for Internal Mouse Interface

qimi_ext
	move.b	mi_quad,d1		get port status register
	move.b	mi_clear,d0		(clear interrupts)
	moveq	#mi.intxy,d0		check for interrupts
	and.b	d1,d0
	beq.s	qext_rts		 ... none
	addq.w	#1,pt_xicnt(a3) 	... yes, count it

	btst	#mi..intx,d1		get x interrupt bit
	beq.s	qext_y_test		 ... not set
	moveq	#1,d0			assume add
	btst	#mi..dirx,d1		is it up?
	bne.s	qext_x_move		 ... no
	moveq	#-1,d0
qext_x_move
	add.w	d0,pt_xinc(a3)
qext_y_test
	btst	#mi..inty,d1		get y interrupt bit
	beq.s	qext_rts		 ... not set
	moveq	#1,d0
	btst	#mi..diry,d1		is it left?
	beq.s	qext_y_move		 ... no
	moveq	#-1,d0
qext_y_move
	add.w	d0,pt_yinc(a3)
qext_rts
	rts
	page

; look at the buttons on the QIMI

qimi_poll
	moveq	#mi.buttn,d1		get button presses
	and.b	mi_butt,d1
	lsr.b	#mi..buts,d1		look up table
	move.b	qimi_btab(pc,d1.w),d2	get new key
	bmi.l	pt_button3		stuff special keys

qpol_snk
	move.b	d2,pt_bpoll(a3) 	this is new key
	clr.b	pt_lstuf(a3)		and it isn't a stuff
qpol_rts
	rts

qimi_btab dc.b	 $ff,1,2,0		 button pressed

	end
