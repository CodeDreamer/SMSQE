; Pointer Physical Routines  V1.06    1985   Tony Tebby   QJUMP
; Internal Mouse Interface / Sandy SQB / CST Thor / Atari QL
; / Standard keyboard  pointer driver
;
; 2020-08-17  1.06  Added QEmulator support

	section driver

	xdef	pt_start
	xdef	pt.xrate

	xref	pt_init
	xref	pt_pbyte

	include dev8_keys_sys
	include dev8_keys_iod
	include dev8_keys_chn
	include dev8_keys_qdos_io
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_sms
	include dev8_keys_con

;	a5 c s	pointer to config block

pt_nqimi equ	0	  ; offset of no qimi flag from (a5)


pt.xrate equ	2	  ; correction to mouse speed for frame rate

rmh_flag equ	$00	  ; rom flag
rmh.flag equ	$4afb0001 ; expected value
rmh_name equ	$08	  ; rom name

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

;	SuperQBoard hardware

ms_stat  equ	$3fcc
ms_clear equ	$3fd0

ms.intxy equ	$ffffff00+%11000000
ms.buttn equ		  %00001110
ms.intbt equ	$ffffff00+%11001110
ms.check equ		  %00001110

;	Thor hardware

mt_data  equ	$ffffff40
mt_ctrl  equ	$ffffff44

mt.eni	 equ	%00011111	enable interrupt, select data register

mt.intxy equ	%11000000+$ffffff00
mt..intx equ	7
mt..dirx equ	6
mt..inty equ	6
mt..diry equ	7

mt.buttn equ	%00111000
mt..buts equ	3

; atari linkage block

ma_id	 equ	$00
ma.id	 equ	'ASTK'
ma_mint  equ	  $80	word	    count of mouse interrupts
ma_diffx equ	  $82	word	    x movement
ma_diffy equ	  $84	word	    y movement
ma_hit	 equ	  $86	byte	    hit button
ma_do	 equ	  $87	byte	    do button

wmon_def
	dc.b	0,0		#0
	dc.w	512,50,0,206
	dc.b	0,4
	dc.b	255,1		#1
	dc.w	256,202,256,0
	dc.b	2,7
	dc.b	255,1		#2
	dc.w	256,202,0,0
	dc.b	7,2

con_def dc.w	3,'CON'


pt_start
	moveq	#iop.pinf,d0		 ; check pointer information
	moveq	#-1,d3
	sub.l	a0,a0
	trap	#do.io
	tst.l	d0
	beq.l	ptin_rts		 ; it is already there

;	Now check if this is a Thor!!

	moveq	#-1,d6			; ... assume Thor
	moveq	#sms.info,d0
	trap	#do.sms2		; find system info

	move.l	sys_chtt(a0),a1 	; top of channel table
	cmp.l	#$FF000000!' THR',-(a1) ; check for THOR fixed flag
	beq.l	ptin_init		; ... yes, no need to patch

	st	sys_tsdd(a0)		; disable Thor screen driver
	move.l	sys_chtb(a0),a1 	; channel table
	move.l	(a1),a1 		; channel 0
	cmp.l	#$100,(a1)		; standard driver?
	beq.l	ptin_nthr		; ... yes, not a THOR

	move.w	sr,d7
reg.thor reg	a3/a4/a5
	movem.l reg.thor,-(sp)
	trap	#0			 ; we are going to get dirty
	
	move.l	a0,a5			 ; sysvar base
	move.l	sys_chtb(a5),a4 	 ; channel table base
	move.l	(a4),a0 		 ; channel 0
	move.l	chn_drvr(a0),a3 	 ; driver

	moveq	#sms.riod,d0
	move.l	a3,a0
	trap	#1			 ; get rid of offending bit

	lea	iod_xilk-iod_iolk(a3),a0
	tst.l	(a0)			 ; any ext int?
	beq.s	ptit_rpl
	moveq	#sms.rexi,d0
	trap	#1
ptit_rpl
	lea	iod_pllk-iod_iolk(a3),a0
	tst.l	(a0)
	beq.s	ptit_rsd
	moveq	#sms.rpol,d0
	trap	#1
ptit_rsd
	lea	iod_shlk-iod_iolk(a3),a0
	tst.l	(a0)
	beq.s	ptit_cls
	moveq	#sms.rshd,d0
	trap	#1

ptit_cls
	moveq	#-1,d5
ptit_clp
	addq.l	#1,d5
	cmp.w	sys_chtp(a5),d5 	 ; end of channel table?
	bgt.s	ptit_open
	move.l	(a4)+,d0
	ble.s	ptit_clp
	move.l	d0,a0
	move.l	chn_tag(a0),d0
	move.w	d5,d0
	move.l	d0,a0			 ; complete channel ID
	moveq	#ioa.clos,d0
	trap	#do.ioa
	bra.s	ptit_clp

ptit_open
	clr.w	sys_chtg(a5)		 ; reset tag
	lea	wmon_def,a4		 ; set pointer to definitions

	moveq	#2,d5			 ; and set the first three channels
ptit_olp
	lea	con_def,a0		 ; console
	moveq	#ioa.open,d0
	moveq	#0,d1
	moveq	#0,d3
	trap	#do.ioa

	moveq	#iow.defw,d0		 ; define window
	move.b	(a4)+,d1		 ; border colour
	move.b	d1,d4			 ; save it
	move.b	(a4)+,d2		 ; and width
	moveq	#-1,d3			 ; no timeout
	move.l	a4,a1			 ; and pointer to definition
	trap	#3			 ; do trap (ignoring errors)
	moveq	#iow.defb,d0		 ; reset border cos of little bug
	move.b	d4,d1
	trap	#3
	addq.l	#8,a4			 ; ink/paper definition
	moveq	#iow.spap,d0
	move.b	(a4),d1
	trap	#3
	moveq	#iow.sstr,d0
	move.b	(a4)+,d1
	trap	#3
	moveq	#iow.sink,d0
	move.b	(a4)+,d1
	trap	#3
	moveq	#iow.clra,d0
	trap	#3

	dbra	d5,ptit_olp

	move.w	d7,sr
	movem.l (sp)+,reg.thor
	bra.s	ptin_init

ptin_nthr
	moveq	#0,d6			 ; not THOR

	andi.l	#$ff00ffff,d2		 ; ensure OS is JS or later
	cmpi.l	#$31003130,d2		 ; "1.10"
	bcc.s	ptin_init		 ; all right

	lea	qdos_too_old,a1
	move.l	a1,d0
	bset	#31,d0
	bra.l	ptin_rts

qdos_too_old
	dc.w	26
	dc.b	'PE needs at least JS ROM!',10

ptin_init
	bsr.l	pt_init
	bne.l	ptin_rts

	lea	ptq_kyrw(pc),a0 	 ; assume this is not a Thor
	move.l	a0,pt_kyrwr(a3) 	 ; use simple keyrow code

	moveq	#sms.info,d0		 ; find sys var
	trap	#do.sms2
	lea	sys_poll(a0),a0 	 ; scan linked list
pti_sca
	move.l	(a0),d0 		 ; end?
	beq.s	pti_kbd
	move.l	d0,a0
	cmp.l	#ma.id,ma_id-iod_pllk(a0); Atari keyboard linkage?
	bne.s	pti_sca 		 ; ... no

	subq.l	#iod_pllk,a0
	move.l	a0,pt_aext(a3)		 ; keep linkage address in exti address

	lea	pta_poll,a1
	move.l	a1,pt_apoll(a3) 	 ; set Atari mouse polling routine
	lea	pt_lpoll(a3),a0
	moveq	#sms.lpol,d0		 ; and link in
	trap	#do.sms2

	lea	pta_kyrw,a1
	move.l	a1,pt_kyrwr(a3) 	 ; atari ST keyrow for dual enter keys

	bra.l	ptin_rts

pti_kbd
	st	pt_aext(a3)		 ; assume keyboard
	tst.b	pt_nqimi(a5)		 ; can it be qimi?
	bne.s	pti_cthr		 ; ... no, try thor

	move.w	sr,d7
	trap	#0			 ; supervisor mode, poll enabled
	moveq	#sms.info,d0		 ; find sys var
	trap	#do.sms2
	move.l	a0,a1
	moveq	#sms.rrtc,d0		 ; wait for clock to change
	trap	#1
	move.l	d1,d4
pti_wait
	moveq	#sms.rrtc,d0		 ; try again
	trap	#1
	cmp.l	d1,d4			 ; the same?
	bne.s	pti_ckbut
	cmp.w	#60,sys_pict(a1)	 ; ... have we waited long enough?
	blt.s	pti_wait
pti_ckbut
	move.l	d1,d4			 ; save time
	moveq	#0,d1			 ; set zero time
	moveq	#sms.srtc,d0
	trap	#1

	move.w	mi_butt,d6		 ; are there buttons?

	move.l	d4,d1			 ; reset time
	moveq	#sms.srtc,d0
	trap	#1

	move.w	d7,sr			 ; back to user mode

	tst.w	d6			 ; board present?
	beq.s	pti_cthr		 ; ... no, try Thor

	lea	pti_poll(pc),a1 	 ; set polling routine address
	move.l	a1,pt_apoll(a3)
	lea	pt_lpoll(a3),a0 	 ; link in polling routine
	moveq	#sms.lpol,d0
	trap	#1

	lea	pti_ext(pc),a1		 ; set external interrupt address
	move.l	a1,pt_aext(a3)
	lea	pt_lext(a3),a0		 ; link in external interrupt
	moveq	#sms.lexi,d0
	trap	#1
	tst.b	mi_clear		 ; enable interrupts
	bra.l	ptin_rts
	page

;	Come here to see if this is a Thor: this has its own mouse, and
;	also requires a patched version of keyrow.

pti_cthr
	tst.l	d6
	bge.s	pti_csqb		; ... no, try sandy SQB

	lea	ptt_kyrw(pc),a0 	; use keyrow patch
	move.l	a0,pt_kyrwr(a3) 	; fill it in for SCHED
	lea	ptt_poll(pc),a1 	; set polling routine address
	move.l	a1,pt_apoll(a3)
	lea	pt_lpoll(a3),a0 	; link in polling routine
	moveq	#sms.lpol,d0
	trap	#do.sms2

	lea	ptt_ext(pc),a1		 ; set external interrupt address
	move.l	a1,pt_aext(a3)
	lea	pt_lext(a3),a0		 ; link in external interrupt
	moveq	#sms.lexi,d0
	trap	#do.sms2
	move.b	#mt.eni,mt_ctrl 	 ; enable interrupts
	bra.l	ptin_rts
	page
		  ; check for SQB
pti_csqb
	move.w	sr,d7			 ; status reg
	trap	#0
	or.w	#$0700,sr		 ; no interrupts
	moveq	#sms.info,d0
	trap	#1
	lea	sys_fsdl(a0),a0 	 ; fsd link list
pti_lflp
	move.l	(a0),d0 		 ; next
	beq.l	ptin_rte
	move.l	d0,a0
	cmp.w	#3,iod_dnus-iod_iolk(a0) ; flp?
	bne.s	pti_lflp		 ; ... no
	move.l	#$dfdfdf00,d0
	and.l	iod_dnus-iod_iolk+2(a0),d0 ; upper case
	cmp.l	#'FLP'<<8,d0		 ; the right chars?
	bne.s	pti_lflp		 

	move.l	4(a0),d0		 ; set base address of space of IO
	and.l	#$ffffc000,d0
	move.l	d0,a0
	move.l	a0,pt_hbase(a3) 	 ; save hardware base

	moveq	#0,d0			 ; no error
	cmp.l	#'Sand',rmh_name+2(a0)	 ; Sandy interface?
	bne.s	ptin_qemu		 ; ... no

	moveq	#ms.intbt,d1		 ; get interrupt bits and buttons
	tst.b	ms_clear(a0)		 ; ... after clearing 
	and.b	ms_stat(a0),d1
	cmp.b	#ms.check,d1		 ; correct?
	bne.s	ptin_rte		 ; ... no

	lea	pts_poll(pc),a1 	 ; set polling routine address
	move.l	a1,pt_apoll(a3)
	lea	pt_lpoll(a3),a0 	 ; link in polling routine
	moveq	#sms.lpol,d0
	trap	#1

	lea	pts_ext(pc),a1		 ; set external interrupt address
	move.l	a1,pt_aext(a3)
	lea	pt_lext(a3),a0		 ; link in external interrupt
	moveq	#sms.lexi,d0
	trap	#1
	bra.s	ptin_rte

ptin_qemu
	moveq	#$e6,d0 		 ; Check for QemuLator
	moveq	#1,d1
	trap	#1
	tst.l	d0
	bne.s	ptin_rte0		 ; Nope
	subq.b	#1,d1
	bne.s	ptin_rte0		 ; D1 changed -> also nope
	dc.w	$ADCA			 ; Start emulator side of mouse driver
	dc.b	'MMou'
	bne.s	ptin_rte0

	lea	ptq_poll(pc),a1 	 ; set polling routine address
	move.l	a1,pt_apoll(a3)
	lea	pt_lpoll(a3),a0 	 ; link in polling routine
	moveq	#sms.lpol,d0
	trap	#1
ptin_rte0
	moveq	#0,d0
ptin_rte
	move.w	d7,sr			 ; back to user mode, interrupts enabled
ptin_rts
	rts

; polling routine for QEmulator
ptq_poll
	moveq	#0,d0
	dc.w	$ADCB			 ; This updates the CON variables
	dc.b	'MMou'
	rts

	page

; external interrupt server for Internal Mouse Interface

pti_ext
	move.b	mi_quad,d1		get port status register
	move.b	mi_clear,d0		(clear interrupts)
	moveq	#mi.intxy,d0		check for interrupts
	and.b	d1,d0
	beq.s	ptie_rts		 ... none
	addq.w	#1,pt_xicnt(a3) 	... yes, count it

;$$$	tst.b	pt_pstat(a3)		is pointer visible?
;$$$	bne.s	pte_rts 		... no

	btst	#mi..intx,d1		get x interrupt bit
	beq.s	ptie_y_test		 ... not set
	moveq	#1,d0			assume add
	btst	#mi..dirx,d1		is it up?
	bne.s	ptie_x_move		 ... no
	moveq	#-1,d0
ptie_x_move
	add.w	d0,pt_xinc(a3)
ptie_y_test
	btst	#mi..inty,d1		get y interrupt bit
	beq.s	ptie_rts		 ... not set
	moveq	#1,d0
	btst	#mi..diry,d1		is it left?
	beq.s	ptie_y_move		 ... no
	moveq	#-1,d0
ptie_y_move
	add.w	d0,pt_yinc(a3)
ptie_rts
	rts
	page

; look at the buttons on the QIMI
pti_poll
	clr.b	pt_kypol(a3)		ensure new key is clear
	move.b	pt_aext(a3),d2		keyboard?
	not.b	d2
	beq.s	ptip_rts		yes
	moveq	#mi.buttn,d1		get button presses
	and.b	mi_butt,d1
	lsr.b	#mi..buts,d1		look up table
	move.b	pti_btab(pc,d1.w),d2	get new key
	bpl.s	ptip_snk		not stuffer, set new key

	tas	pt_lstuf(a3)		was last press a stuff? (this one is)
	blt.s	ptip_rts		yes, don't do another
	move.l	sys_ckyq(a6),a2 	no, stuff something in here
	move.b	pt_stuf1(a3),d1 	get first character
	beq.s	ptip_rts		isn't one
	jsr	pt_pbyte(pc)		there is one, stuff it
	move.b	pt_stuf2(a3),d1 	is there another?
	beq.s	ptip_rts		no
	jmp	pt_pbyte(pc)		yes, stuff that and return

ptip_snk
	move.b	d2,pt_kypol(a3) 	this is new key
	clr.b	pt_lstuf(a3)		and it isn't a stuff
ptip_rts
	rts

pti_btab dc.b	$ff,1,2,0		button pressed
	page

; external interrupt server for Sandy

pts_ext
	move.l	pt_hbase(a3),a0 	 ; hardware base
	move.b	ms_stat(a0),d1		 ; get port status register
	move.b	ms_clear(a0),d0 	(clear interrupts)
	moveq	#ms.intxy,d0		check for interrupts
	and.b	d1,d0
	beq.s	ptse_rts		 ... none
	addq.w	#1,pt_xicnt(a3) 	... yes, count it

;$$$	tst.b	pt_pstat(a3)		is pointer visible?
;$$$	bne.s	ptse_rts		 ... no

	lsl.b	#1,d1			get x interrupt bit
	bcc.s	ptse_y_test		 ... not set
	moveq	#1,d0			assume add
	bclr	#6,d1			is it up?
	beq.s	ptse_x_move		 ... no
	moveq	#-1,d0
ptse_x_move
	add.w	d0,pt_xinc(a3)
ptse_y_test
	lsl.b	#1,d1			get y interrupt bit
	bcc.s	ptse_rts		 ... not set
	moveq	#1,d0
	bclr	#6,d1			is it left?
	beq.s	ptse_y_move		 ... no
	moveq	#-1,d0
ptse_y_move
	add.w	d0,pt_yinc(a3)
ptse_rts
	rts
	page

; look at the buttons on the SuperQBoard

pts_poll
	clr.b	pt_kypol(a3)		 ; newkey is clear
	moveq	#%00001110,d1		 ; get button presses
	move.l	pt_hbase(a3),a0
	and.b	ms_stat(a0),d1
	lsr.b	#1,d1			 ; look up table
	move.b	pts_btab(pc,d1.w),d2
	bpl.s	ptsp_snk		 ; not stuffer, set new key

	tas	pt_lstuf(a3)		 ; was last press a stuff? (this one is)
	blt.s	ptsp_rts		 ; yes, don't do another
	move.l	sys_ckyq(a6),a2 	 ; no, stuff something in here
	move.b	pt_stuf1(a3),d1 	 ; get first character
	beq.s	ptsp_rts		 ; isn't one
	jsr	pt_pbyte(pc)		 ; there is one, stuff it
	move.b	pt_stuf2(a3),d1 	 ; is there another?
	beq.s	ptsp_rts		 ; no
	jmp	pt_pbyte(pc)		 ; yes, stuff that and return

ptsp_snk
	move.b	d2,pt_kypol(a3) 	 ; this is new key
	clr.b	pt_lstuf(a3)		 ; and it isn't a stuff
ptsp_rts
	rts

pts_btab dc.b	1,$ff,1,1,2,2,3,0	 ; button pressed (one is high priority)

; external interrupt server for Thor Mouse Interface

ptt_ext
	moveq	#mt.intxy,d1		interrupt mask
	and.b	mt_ctrl,d1		get port status register
	beq.s	ptte_rts		 ... no interrupts
	addq.w	#1,pt_xicnt(a3) 	... interrupt, count it

	move.b	mt_data,d2		get direction
	btst	#mt..intx,d1		get x interrupt bit
	beq.s	ptte_y_test		 ... not set
	moveq	#1,d0			assume add
	btst	#mt..dirx,d2		is it left?
	beq.s	ptte_x_move		 ... no
	moveq	#-1,d0
ptte_x_move
	add.w	d0,pt_xinc(a3)
ptte_y_test
	btst	#mt..inty,d1		get y interrupt bit
	beq.s	ptte_rts		 ... not set
	moveq	#1,d0
	btst	#mt..diry,d2		is it up?
	bne.s	ptte_y_move		 ... no
	moveq	#-1,d0
ptte_y_move
	add.w	d0,pt_yinc(a3)
ptte_rts
	rts
	page

; look at the buttons on the Thor

ptt_poll
	clr.b	pt_kypol(a3)		ensure new key is clear
	move.b	pt_aext(a3),d2		keyboard?
	not.b	d2
	beq.s	ptit_rts		yes
	moveq	#mt.buttn,d1		get button presses
	and.b	mt_data,d1
	lsr.b	#mt..buts,d1		look up table
	move.b	ptt_btab(pc,d1.w),d2	get new key
	bpl.s	ptit_snk		not stuffer, set new key

	tas	pt_lstuf(a3)		was last press a stuff? (this one is)
	blt.s	ptit_rts		yes, don't do another
	move.l	sys_ckyq(a6),a2 	no, stuff something in here
	move.b	pt_stuf1(a3),d1 	get first character
	beq.s	ptit_rts		isn't one
	jsr	pt_pbyte(pc)		there is one, stuff it
	move.b	pt_stuf2(a3),d1 	is there another?
	beq.s	ptit_rts		no
	jmp	pt_pbyte(pc)		yes, stuff that and return

ptit_snk
	move.b	d2,pt_kypol(a3) 	this is new key
	clr.b	pt_lstuf(a3)		and it isn't a stuff
ptit_rts
	rts

ptt_btab dc.b	$ff,1,$ff,1,2,$ff,2,0
	page

;	General purpose keyrow routine: PTT_KYRW is for the Thor, PTQ_
;	is for real QL compatibles...

;	Registers:
;		Entry				Exit
;	D1					keyrow value
;	A3	IPC command

pta_kyrw
	cmp.b	#9,(a3) 	      ; keyrow?
	bne.s	ptq_kyrw	      ; ... no
	cmp.b	#1,6(a3)	      ; row 1?
	bne.s	ptq_kyrw	      ; ... no
	move.l	a3,-(sp)	      ; ... yes, save a3
	lea	pta_keypad,a3	      ; ... and get extra enter key
	bsr.s	ptq_kyrw
	move.l	(sp)+,a3
	lsr.b	#1,d1		      ; bit 1->0
	and.b	#1,d1
	move.b	d1,-(sp)
	bsr.s	ptq_kyrw	      ; now the real row
	or.b	(sp)+,d1	      ; both enter keys
	rts

pta_keypad dc.b 9,1,0,0,0,0,14,%10    ; read row 14
	ds.w   0

ptq_kyrw
	moveq	#sms.hdop,d0
	trap	#do.sms2
	rts

thorreg reg	d2/d3/a0-a3
ptt_kyrw
	  MOVEM.L   thorreg,-(A7)
	  MOVEQ     #sms.hdop,D0	ip.com call
	  TRAP	    #do.sms2		tests joystick port...
	  move.b    6(a3),D3		pick up keyrow number
	  MOVEQ     #$0,D7
	  MOVEQ     #sms.info,D0	system info call
	  TRAP	    #do.sms2
	  CMPI.B    #$7,D3		keyrow 7 has alt/shft/ctrl keys
	  BNE.S     L3
	  TST.W     $80(A0)		Thor shift key hit?
	  BEQ.S     L1
	  ADDQ.W    #1,D7		set bit 1 of d7
L1
	  TST.B     $82(A0)		Thor ctrl key hit?
	  BEQ.S     L2
	  ADDQ.W    #2,D7		set bit 2 of d7
L2
	  TST.B     $83(A0)		Thor alt key hit?
	  BEQ.S     L3
	  ADDQ.W    #4,D7		set bit 3 of d7
L3
	  MOVEM.L   sys_caps(A0),D0/D1/D2   get capslock, auotrepeat etc.
	  TST.L     D2
	  BMI.S     L9
	  CMPI.B    #$FF,D0
	  BNE.S     L4
	  LSR.W     #8,D0
L4
	  MOVE.B    L10(PC,D0.W),D1
	  CMPI.B    #$BF,D0
	  BLS.S     L8
	  CMPI.B    #$FC,D0
	  BCC.S     L8
	  CMPI.B    #$DF,D0
	  BLS.S     L6
	  SUBQ.B    #7,D3
	  BNE.S     L5
	  TST.B     D1
	  BPL.S     L9
	  ORI.B     #$1,D7
	  BRA.S     L9
L5
	  ADDQ.B    #7,D3
	  BNE.S     L9
	  ANDI.B    #$7F,D1
	  OR.B	    D1,D7
	  BRA.S     L9
L6
	  SUBQ.B    #7,D3
	  BNE.S     L7
	  LSR.B     #1,D1
	  BCC.S     L9
	  ORI.B     #$2,D7
	  BRA.S     L9
L7
	  ADDQ.B    #6,D3
	  BNE.S     L9
	  ANDI.B    #$FE,D1
	  OR.B	    D1,D7
	  BRA.S     L9
L8
	  MOVE.B    D1,D0
	  LSR.B     #4,D0
	  CMP.B     D0,D3
	  BNE.S     L9
	  ANDI.B    #$7,D1
	  MOVEQ     #$1,D0
	  LSL.B     D1,D0
	  OR.B	    D0,D7
L9
	  MOVE.W    D7,D1
	  MOVEM.L   (A7)+,thorreg
	  MOVEQ     #$0,D0
	  RTS
L10
	  DC.W	    $3144,$2423,$4664,$3436
	  DC.W	    $4253,$1032,$4026,$7665
	  DC.W	    $4563,$5433,$6667,$7451
	  DC.W	    $7356,$2113,$1520,$2513
	  DC.W	    $1643,$2741,$0602,$0727
	  DC.W	    $5065,$6035,$7755,$2275
	  DC.W	    $6543,$6141,$0602,$6207
	  DC.W	    $6050,$3737,$7735,$2275
	  DC.W	    $6144,$2423,$4664,$3436
	  DC.W	    $4252,$4732,$4026,$7665
	  DC.W	    $4563,$5433,$6667,$7451
	  DC.W	    $7356,$2130,$1520,$6255
	  DC.W	    $2544,$2423,$4664,$3436
	  DC.W	    $4252,$4732,$4026,$7657
	  DC.W	    $4563,$5433,$6667,$7451
	  DC.W	    $7356,$2130,$1520,$3413
	  DC.W	    $1343,$2741,$0602,$0727
	  DC.W	    $5065,$6035,$7755,$2275
	  DC.W	    $6543,$6141,$0602,$6207
	  DC.W	    $6050,$3737,$7735,$2275
	  DC.W	    $6144,$2423,$4664,$3436
	  DC.W	    $4252,$4732,$4026,$7665
	  DC.W	    $4563,$5433,$6667,$7451
	  DC.W	    $7356,$2130,$1520,$6255
	  DC.W	    $0206,$0307,$0206,$0307
	  DC.W	    $1082,$1183,$1082,$1183
	  DC.W	    $0414,$0515,$0414,$0515
	  DC.W	    $8090,$8191,$8191,$8191
	  DC.W	    $0000,$0000,$0000,$0000
	  DC.W	    $0202,$8282,$0808,$8888
	  DC.W	    $1010,$9090,$0101,$8181
	  DC.W	    $2020,$A0A0,$1653,$1000
	 page

; QL / Atari polling routine

pta_poll
	move.l	pt_aext(a3),a4
	move.w	ma_mint(a4),d2
	add.w	d2,pt_xicnt(a3) 	 ; count of ext ints
	move.w	ma_diffx(a4),d2
	add.w	d2,pt_xinc(a3)		 ; x movement
	move.w	ma_diffy(a4),d2
	add.w	d2,pt_yinc(a3)		 ; y movement

	clr.l	ma_diffx(a4)		 ; clear them
	clr.w	ma_mint(a4)

	moveq	#0,d2			 ; no buttons
	tst.b	ma_hit(a4)		 ; hit?
	beq.s	ptap_do 		 ; ... no
	moveq	#1,d2			 ; ... yes
	tst.b	ma_do(a4)		 ; both?
	beq.s	ptap_snk		 ; ... no
	bra.s	ptap_stf		 ; ... yes
ptap_do
	tst.b	ma_do(a4)		 ; do?
	beq.s	ptap_nky		 ; ... no
	moveq	#2,d2
ptap_snk
	clr.b	pt_lstuf(a3)		 ; not stuffing
ptap_nky
	move.b	d2,pt_kypol(a3) 	 ; set new key
ptap_rts
	rts

ptap_stf
	tas	pt_lstuf(a3)		was last press a stuff? (this one is)
	blt.s	ptap_rts		yes, don't do another
	move.l	sys_ckyq(a6),a2 	no, stuff something in here
	move.b	pt_stuf1(a3),d1 	get first character
	beq.s	ptap_rts		isn't one
	jsr	pt_pbyte(pc)		there is one, stuff it
	move.b	pt_stuf2(a3),d1 	is there another?
	beq.s	ptap_rts		no
	jmp	pt_pbyte(pc)		yes, stuff that and return

	end
