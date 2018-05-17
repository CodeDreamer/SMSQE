; SMS (QXL) Screen Copy Routine
; 2006.10.01	1.01	bugfix for correct copy loop (BC)


	section comm

	xdef	qxl_scrcopy

	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_keys_sys'

qsc.scinc equ	7     ; scan increment
qsc.sci2  equ	2     ; scan start increment

;+++
; This routine sets up interrupt server messages for copying the screen
;
;	d0/d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5 smashed
;
;---
qxl_scrcopy
	move.l	qxl_scr_work,a5
	tst.b	qxl_vhold(a5)	; mode change in progress?
	bne.l	qsc_exit	; ... yes

	move.b	qxl_vcopy(a5),d4 ; max lines to copy

qsc_retry
	move.l	qxl_cpyb(a5),d3 ; base of copy area
	beq.l	qsc_exit
	moveq	#0,d2
	move.w	qxl_scrl(a5),d2 ; length of line
	move.w	qxl_scrp(a5),d1 ; previous line
;	 bmi.l	 qsc_fcopy	 ; force copy

	move.b	qxl_vchek(a5),d5 ; try up to n lines
	bge.l	qsc_ckle	; OK
	moveq	#0,d5
	addq.b	#1,qxl_vchek(a5)

qsc_lnloop
	tst.w	qxl_vfine(a5)	; fine search?
	beq.s	qsc_lnskip	; ... no
	addq.w	#1,d1		; next
	cmp.w	qxl_scry(a5),d1 ; off end?
	blo.s	qsc_setup	; ... no
	clr.w	qxl_vfine(a5)	; ... yes, reset
	bra.s	qsc_lnlast
qsc_lnskip
	addq.w	#qsc.scinc,d1	; next line (coarse)
	cmp.w	qxl_scry(a5),d1 ; off end?
	blo.s	qsc_setup
qsc_lnlast
	sub.w	qxl_scry(a5),d1 ; start again here

qsc_setup
	move.w	d2,d0
	mulu	d1,d0		; offset from base
	move.l	qxl_scrb(a5),a0
	add.l	d0,a0
	move.l	qxl_cpyb(a5),a1 ; base of copy area
	add.l	d0,a1

	moveq	#0,d6
	move.w	d2,d0
	subq.w	#8,d0
	lsr.w	#4,d0		; blocks to compare - half a block
	bcc.s	qsc_ckl2	; half block extra
qsc_ckl
	move.l	(a0)+,d6
	sub.l	(a1)+,d6	; difference
	move.l	(a0)+,d7
	sub.l	(a1)+,d7	; difference
	or.l	d7,d6		; accumulated
qsc_ckl2
	move.l	(a0)+,d7
	sub.l	(a1)+,d7	; difference
	or.l	d7,d6		; accumulated
	move.l	(a0)+,d7
	sub.l	(a1)+,d7	; difference
	or.l	d7,d6		; accumulated
	dbne	d0,qsc_ckl
	beq.s	qsc_cknc	; no change

	tst.w	qxl_vfine(a5)	; changed, was it fine?
	bne.s	qsc_uline	; ... yes, update the line
	move.w	#qsc.scinc*3/2,qxl_vfine(a5) ; scinc+ misses before reverting
	move.w	d1,d0		; changed line
	move.w	qxl_scrp(a5),d1 ; previous line
	cmp.w	d0,d1		; is previous above changed?
	blo.s	qsc_lnloop
	moveq	#0,d1		; start at top
	bra.s	qsc_setup

qsc_cknc
	tst.w	qxl_vfine(a5)	; not changed, was it fine?
	beq.s	qsc_ckle	; ... no
	subq.w	#1,qxl_vfine(a5) ; ... yes, count down

qsc_ckle
	move.w	d1,qxl_scrp(a5) ; save current line as previous
	subq.b	#1,d5
	bcc	qsc_lnloop	 ; unchanged, try again

qsc_exit
	rts

;qsc_fcopy
;	 addq.w  #1,qxl_scrp(a5) ; next line, when zero, reverts to normal
;	 move.w  d2,d0		; qxl line length
;	 add.w	 qxl_scry(a5),d1 ; our line to copy
;	 mulu	 d1,d2		; offset in screen
;	 mulu	 qxl_vgal(a5),d1 ; ... in vga
;	 add.w	 qxl_vga0(a5),d1 ; +base in vga (zero for all large screens)
;	 move.l  qxl_scrb(a5),a3 ; screen
;	 add.l	 d2,a3
;	 move.l  d3,a4
;	 add.l	 d2,a4		; copy
;	 moveq	 #0,d2		; no vga offset
;	 bra.s	 qsc_copycnv	; copy converting

qsc_uline
	move.w	d1,qxl_scrp(a5) ; save current line
	mulu	qxl_vgal(a5),d1 ; offset
	add.w	qxl_vga0(a5),d1 ; + base of vga memory (zero for large screens)
	lsl.w	#4,d0		; nr 16 byte checks left to bytes
	sub.w	#16,d2		; start of last 16 byte group
	sub.w	d0,d2		; offset to start of run
	lea	-16(a0),a3	; save start of run on screen
	lea	-16(a1),a4	; save start of run in copy

	add.w	d0,a0		; end of line on screen
	add.w	d0,a1		; end of line in copy

	tst.w	d2		; start below start of line?
	bpl.s	qsc_fel 	; ... no
	addq.l	#8,a3		; yes, it must have been start of line 800x600
	addq.l	#8,a4
	moveq	#0,d2		; start of run on vga

qsc_fel
	move.l	-(a0),d6	; compare 4 bytes
	sub.l	-(a1),d6
	move.l	-(a0),d7	; and another 4
	sub.l	-(a1),d7
	or.l	d6,d7
	beq.s	qsc_fel 	; look backwards for first different

	sub.l	a3,a0		; length of different bit - 8
	move.w	a0,d0
	addq.w	#8,d0

;	d0	QXL bytes in run
;	d1	VGA address of start of line
;	d2	QXL offset of start of run in line
;	a3	QXL screen
;	a4	Copy screen

qsc_copycnv
	move.w	qxl_vconv(a5),d7
	beq.l	qsc_copy
	lsr.w	#1,d0		; ... run in words in QL, bytes VGA
	lsr.w	#1,d2		; ... offset in words in QL, bytes VGA
	add.l	d2,d1		; start of run
	move.w	d1,d2
	add.w	d0,d2		; run crosses 64k boundary?
	bcc.s	qsc_copy48	; ... no
	sub.w	d2,d0		; ... yes, shorten run
	subq.w	#1,qxl_scrp(a5) ; redo this line
qsc_copy48
	cmp.w	#qxl.vc8vga,d7	; mode 8?
	beq.s	qsc_copy8	; ... yes

	moveq	#qxm_vdat,d7	; headers
	add.w	d0,d7
	move.l	d7,d6
	add.l	d7,d6
	move.l	qxl_qxpc_mess,a2
	move.l	qxl_qxpc_eom(a2),a0 ; start of next message
	add.l	a0,d6		; end of message
	cmp.l	qxl_qxpc_eob(a2),d6
	bgt.l	qsc_no_copy

	move.l	d6,qxl_qxpc_eom(a2)
	lea	(a0,d7.l),a1	; start of second plane

	move.w	#qxm.pplu<<8+1,(a0)+; first plane flags
	move.w	#qxm.pplu<<8+2,(a1)+; second plane flags
	move.l	d1,(a0)+	; window and start address
	move.l	d1,(a1)+	; window and start address
	lsr.w	#1,d0		; vga words
	move.w	d0,(a0)+	; length
	move.w	d0,(a1)+

	subq.w	#1,d0		; copy loop length -1
qsc_loop4
	move.l	(a3),(a4)+	; update copy screen
	move.b	(a3)+,(a1)+	; green
	move.b	(a3)+,(a0)+	; red
	move.b	(a3)+,(a1)+
	move.b	(a3)+,(a0)+

	dbra	d0,qsc_loop4

	bra.l	qsc_ecopy

qsc_copy8
	movem.l a5/a6,-(sp)
	lea	qsc_grtab,a5
	lea	qsc_bftab,a6

	moveq	#qxm_vdat,d7	; headers
	add.w	d0,d7
	move.l	d7,d6
	add.l	d7,d6
	add.l	d7,d6
	move.l	qxl_qxpc_mess,a2
	move.l	qxl_qxpc_eom(a2),a0 ; start of next message
	add.l	a0,d6		; end of message
	cmp.l	qxl_qxpc_eob(a2),d6
	bgt.l	qsc_no_copy

	move.l	d6,qxl_qxpc_eom(a2)
	lea	(a0,d7.l),a1	; start of second plane
	lea	(a1,d7.l),a2	; third plane

	move.w	#qxm.pplu<<8+1,(a0)+; first plane flags
	move.w	#qxm.pplu<<8+2,(a1)+; second plane flags
	move.w	#qxm.pplu<<8+4,(a2)+; third plane flags
	move.l	d1,(a0)+	; window and start address
	move.l	d1,(a1)+
	move.l	d1,(a2)+
	lsr.w	#1,d0		; vga words
	move.w	d0,(a0)+	; length
	move.w	d0,(a1)+
	move.w	d0,(a2)+

	subq.w	#1,d0		; copy loop length -1
	moveq	#0,d1
qsc_loop8
	move.b	(a3)+,d1	; get green
	move.b	d1,(a4)+	; update copy screen
	move.b	(a5,d1.w),(a1)+
	move.b	(a3)+,d1	; get red/blue
	move.b	d1,(a4)+	; update copy screen
	move.b	(a5,d1.w),(a0)+ ; red
	move.b	(a6,d1.w),(a2)+ ; blue

	move.b	(a3)+,d1	; get green
	move.b	d1,(a4)+	; update copy screen
	move.b	(a5,d1.w),(a1)+
	move.b	(a3)+,d1	; get red/blue
	move.b	d1,(a4)+	; update copy screen
	move.b	(a5,d1.w),(a0)+ ; red
	move.b	(a6,d1.w),(a2)+ ; blue

	dbra	d0,qsc_loop8

	movem.l (sp)+,a5/a6
	bra.s	qsc_ecopy


;
; simple copy data
;
qsc_copy
	add.l	d2,d1		; start of run
	move.w	d1,d2
	add.w	d0,d2		; run crosses 64k boundary?
	bcc.s	qsc_copys	; ... no
	sub.w	d2,d0		; ... yes, shorten run
	subq.w	#1,qxl_scrp(a5) ; redo this line
qsc_copys
	moveq	#qxm_vdat,d6	; header
	add.w	d0,d6
	move.l	qxl_qxpc_mess,a2
	move.l	qxl_qxpc_eom(a2),a0 ; start of next message
	add.l	a0,d6		; end of message
	cmp.l	qxl_qxpc_eob(a2),d6
	bgt.s	qsc_no_copy

	move.l	d6,qxl_qxpc_eom(a2)

	move.w	#qxm.pkpu<<8,(a0)+; packed pixel flags
	move.l	d1,(a0)+	; window and start address
	lsr.w	#1,d0		; vga words
	move.w	d0,(a0)+	; length

	lsr.w	#2,d0		; v. 1.01 bugfix
	subq.w	#1,d0		; copy loop length -1
qsc_loops
	move.l	(a3),(a4)+	; update copy screen
	move.l	(a3)+,(a0)+	; copy 8 bytes at a time
	move.l	(a3),(a4)+
	move.l	(a3)+,(a0)+
	dbra	d0,qsc_loops

qsc_ecopy
	subq.b	#1,d4		; another to copy?
	bne	qsc_retry
qsc_no_copy
	rts

qsc_grtab
	dc.b $00,$00,$03,$03,$00,$00,$03,$03,$0C,$0C,$0F,$0F,$0C,$0C,$0F,$0F
	dc.b $00,$00,$03,$03,$00,$00,$03,$03,$0C,$0C,$0F,$0F,$0C,$0C,$0F,$0F
	dc.b $30,$30,$33,$33,$30,$30,$33,$33,$3C,$3C,$3F,$3F,$3C,$3C,$3F,$3F
	dc.b $30,$30,$33,$33,$30,$30,$33,$33,$3C,$3C,$3F,$3F,$3C,$3C,$3F,$3F
	dc.b $00,$00,$03,$03,$00,$00,$03,$03,$0C,$0C,$0F,$0F,$0C,$0C,$0F,$0F
	dc.b $00,$00,$03,$03,$00,$00,$03,$03,$0C,$0C,$0F,$0F,$0C,$0C,$0F,$0F
	dc.b $30,$30,$33,$33,$30,$30,$33,$33,$3C,$3C,$3F,$3F,$3C,$3C,$3F,$3F
	dc.b $30,$30,$33,$33,$30,$30,$33,$33,$3C,$3C,$3F,$3F,$3C,$3C,$3F,$3F
	dc.b $C0,$C0,$C3,$C3,$C0,$C0,$C3,$C3,$CC,$CC,$CF,$CF,$CC,$CC,$CF,$CF
	dc.b $C0,$C0,$C3,$C3,$C0,$C0,$C3,$C3,$CC,$CC,$CF,$CF,$CC,$CC,$CF,$CF
	dc.b $F0,$F0,$F3,$F3,$F0,$F0,$F3,$F3,$FC,$FC,$FF,$FF,$FC,$FC,$FF,$FF
	dc.b $F0,$F0,$F3,$F3,$F0,$F0,$F3,$F3,$FC,$FC,$FF,$FF,$FC,$FC,$FF,$FF
	dc.b $C0,$C0,$C3,$C3,$C0,$C0,$C3,$C3,$CC,$CC,$CF,$CF,$CC,$CC,$CF,$CF
	dc.b $C0,$C0,$C3,$C3,$C0,$C0,$C3,$C3,$CC,$CC,$CF,$CF,$CC,$CC,$CF,$CF
	dc.b $F0,$F0,$F3,$F3,$F0,$F0,$F3,$F3,$FC,$FC,$FF,$FF,$FC,$FC,$FF,$FF
	dc.b $F0,$F0,$F3,$F3,$F0,$F0,$F3,$F3,$FC,$FC,$FF,$FF,$FC,$FC,$FF,$FF

qsc_bftab
	dc.b $00,$03,$00,$03,$0C,$0F,$0C,$0F,$00,$03,$00,$03,$0C,$0F,$0C,$0F
	dc.b $30,$33,$30,$33,$3C,$3F,$3C,$3F,$30,$33,$30,$33,$3C,$3F,$3C,$3F
	dc.b $00,$03,$00,$03,$0C,$0F,$0C,$0F,$00,$03,$00,$03,$0C,$0F,$0C,$0F
	dc.b $30,$33,$30,$33,$3C,$3F,$3C,$3F,$30,$33,$30,$33,$3C,$3F,$3C,$3F
	dc.b $C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF,$C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF
	dc.b $F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF,$F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF
	dc.b $C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF,$C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF
	dc.b $F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF,$F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF
	dc.b $00,$03,$00,$03,$0C,$0F,$0C,$0F,$00,$03,$00,$03,$0C,$0F,$0C,$0F
	dc.b $30,$33,$30,$33,$3C,$3F,$3C,$3F,$30,$33,$30,$33,$3C,$3F,$3C,$3F
	dc.b $00,$03,$00,$03,$0C,$0F,$0C,$0F,$00,$03,$00,$03,$0C,$0F,$0C,$0F
	dc.b $30,$33,$30,$33,$3C,$3F,$3C,$3F,$30,$33,$30,$33,$3C,$3F,$3C,$3F
	dc.b $C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF,$C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF
	dc.b $F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF,$F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF
	dc.b $C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF,$C0,$C3,$C0,$C3,$CC,$CF,$CC,$CF
	dc.b $F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF,$F0,$F3,$F0,$F3,$FC,$FF,$FC,$FF


	end
