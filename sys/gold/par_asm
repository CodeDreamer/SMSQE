; SuperGoldCard PAR driver. Deciphered by Marcel Kilgus

	section sgc

	xdef	par_init

	xref	ut_gtnam

	include 'dev8_sys_gold_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_iod'
	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'

ddb_ssio  equ	$28			; iou.ssio function block
ddb_ssi   equ	$2c			; byte fetch
ddb_sso   equ	$30			; byte send
ddb_rts   equ	$34			; RTS
ddb_open  equ	$36			; set if already open
ddb_clch  equ	$37			; character on close
ddb_magic equ	$38			; PAR% marker
ddb_buff  equ	$3d			; 3 byte buffer
ddb_ptr   equ	$40			; buffer pointer
ddb_code  equ	$44			; PAR open code
ddb.cname equ	$14			; po_name - par_open
ddb_name  equ	ddb_code+ddb.cname
ddb.csize equ	$4c			; par_opdo - par_open
ddb_end   equ	ddb_code+ddb.csize

chn_crlf  equ	$18			; LF -> CRLF translation flags
chn_clch  equ	$19			; character on close
chn_tra   equ	$1a

par_init
	tst.w	glx_ptch+glk.card
	bne.s	par_rts 		; skip for GC

	move.l	#ddb_end,d1
	moveq	#0,d2
	moveq	#sms.achp,d0
	trap	#1
	tst.l	d0
	bne.s	par_rts

	move.l	a0,a4
	lea	iod_ioad(a4),a3
	lea	par_io,a2
	move.l	a2,(a3)+		; I/O
	move.l	a3,a1
	addq.l	#4,a3			; skip open
	lea	par_close,a2
	move.l	a2,(a3)+		; close
	lea	par_bp,a2
	move.l	a2,(a3)+		; ddb_ssio - byte test not implemented
	move.l	a2,(a3)+		; ddb_ssi - byte fetch not implemented
	lea	par_pbyt,a2
	move.l	a2,(a3)+		; ddb_sso - byte send
	move.w	par_rts,(a3)+		; ddb_rts
	clr.w	(a3)+			; ddb_open/ddb_clch
	move.l	#'PAR%',(a3)+		; ddb_magic
	addq.l	#4,a3			; ddb_buff
	move.l	a3,(a3)+		; ddb_ptr. Point to selve -> empty
	move.l	a3,(a1) 		; open

	lea	par_open,a2		; copy code so we can alter the name
	lea	par_opdo,a1
par_copy
	move.w	(a2)+,(a3)+
	cmpa.l	a1,a2
	blt.s	par_copy
	move.l	a2,-(a3)		; patch par_opdo into jmp

	lea	par_sched,a2
	move.l	a2,iod_shad(a4)
	lea	iod_shlk(a4),a0
	moveq	#sms.lshd,d0
	trap	#1

	lea	iod_iolk(a4),a0
	moveq	#sms.liod,d0
	trap	#1

	lea	par_procs,a1
	move.w	sb.inipr,a2
	jsr	(a2)
	moveq	#0,d0
par_rts
	rts

par_procs
	 dc.w	1
	 dc.w	par_use-*
	 dc.b	7,'PAR_USE'
	 dc.w	0,0,0

; PAR_USE
par_use
	move.l	#'PAR0',d4
	moveq	#8,d0
	add.l	a3,d0
	sub.l	a5,d0
	bge.s	par_use_param
par_bp
	moveq	#-15,d0
	rts

par_use_param
	bgt.s	par_use_find
	bsr.l	ut_gtnam
	bne.s	par_bp
	cmpi.w	#3,(a6,a1.l)		; must be 3 letters long
	bne.s	par_bp
	move.l	2(a6,a1.l),d4
	andi.l	#$dfdfdfdf,d4		; uppercase

par_use_find
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_iodl(a0),a0 	; list of I/O drivers
	move.l	#'PAR%',d2
par_use_next
	cmp.l	ddb_magic-iod_iolk(a0),d2
	beq.s	par_use_set
	move.l	(a0),d0
	move.l	d0,a0
	bne.s	par_use_next

	moveq	#-7,d0
	rts

par_use_set
	move.l	d4,ddb_name-iod_iolk+2(a0)
	moveq	#0,d0
	rts

; The following code will be copied to the DDB
par_open
	move.l	a3,a4			; 0000
	lea	-8*2(sp),sp		; 0002
	move.l	a7,a3			; 0006
	move.w	iou.dnam,a2		; 0008
	jsr	(a2)			; 000c
	bra.s	po_exit 		; 000e
	bra.s	po_exit 		; 0010
	bra.s	po_do			; 0012
po_name
	dc.w	3,'PAR' 		; 0014 ddb.cname
	dc.w	8			; 001a
	dc.w	-1,1			; 001c
	dc.w	4,'OEMS'		; 0020
	dc.w	2,'IH'			; 0026
	dc.w	3,'RCA' 		; 002a
	dc.w	2,'TD'			; 0030
	dc.w	2,'FZ'			; 0034
	dc.w	' _',0			; 0038
	dc.w	1,'K'			; 003c
po_exit
	lea	8*2(sp),sp		; 0040
	rts				; 0044
po_do
	jmp	$12345678		; 0046

par_opdo				; 004c ddb.csize
	moveq	#-9,d0
	tas	ddb_open(a4)
	bne.s	po_rts

	moveq	#$1c,d1
	move.w	mem.achp,a2
	jsr	(a2)
	bne.s	po_rts

	movem.w (sp),d0/d1/d2/d3/d4/d5/d6/d7	; parameters
	tst.w	d5
	beq.s	po_cl_set		; no char on close

	subq.w	#1,d5
	beq.s	po_ff			; send FORM FEED on close

	moveq	#26,d5			; send CTRL+Z on close
	bra.s	po_cl_set
po_ff
	moveq	#12,d5			; send FORM FEED on close
po_cl_set
	move.b	d5,chn_clch(a0)

	subq.w	#2,d3			; <0 = no, 0 = CR, >0 = CRLF
	move.b	d3,chn_crlf(a0)

	subq.w	#2,d4
	slt	chn_tra(a0)		; >0 = TRA

	moveq	#0,d0
po_rts
	lea	8*2(sp),sp
	rts

; Close
par_close
	tst.b	ddb_clch(a3)
	bne.s	pc_busy
	move.b	chn_clch(a0),ddb_clch(a3)
pc_busy
	sf	ddb_open(a3)
	move.w	mem.rchp,a2
	jmp	(a2)

par_io
	tst.b	ddb_clch(a3)
	bne.s	pio_nc
	pea	ddb_ssio(a3)
	move.w	iou.ssio,a2
	jmp	(a2)

; Send PAR byte
par_pbyt
	lea	ddb_ptr(a3),a2
	cmpa.l	ddb_ptr(a3),a2
	bne.s	pio_nc

	move.l	glx_ptch+glk.base,a2	; hardware base
	tst.b	glo_pbusy(a2)
	bpl.s	pio_nc			; printer busy

	cmpi.b	#$a,d1			; LF?
	bne.s	pio_tra

	tst.b	chn_crlf(a0)
	bmi.s	pio_tra
	beq.s	pio_cr

	bchg	#$1,chn_crlf(a0)
	bne.s	pio_tra
	bsr.s	pio_cr			; send CR first
	moveq	#$a,d1			; try LF next time
pio_nc
	moveq	#-1,d0
	rts

pio_cr
	moveq	#$d,d1
pio_tra
	tst.b	chn_tra(a0)
	beq.s	pio_send
	tst.b	sys_xact(a6)		; TRA active?
	beq.s	pio_send		; ... no

	move.l	sys_xtab(a6),a4 	; TRA table
	adda.w	2(a4),a4		; try table 1
	andi.w	#$ff,d1
	beq.s	pio_send

	move.b	(a4,d1.w),d0		; get translates character
	beq.s	pio_tra2
	move.b	d0,d1
	bra.s	pio_send

pio_tra2
	move.l	sys_xtab(a6),a4
	adda.w	4(a4),a4		; try table 2
	move.b	(a4)+,d0		; entry count
pio_tra_loop
	beq.s	pio_ok			; end of table

	cmp.b	(a4)+,d1		; this it?
	beq.s	pio_tra_buffer
	addq.l	#3,a4			; next sequence
	subq.b	#1,d0
	bra.s	pio_tra_loop

pio_tra_buffer
	lea	ddb_buff(a3),a2
	move.b	(a4)+,(a2)+
	move.b	(a4)+,(a2)+
	move.b	(a4),(a2)
	subq.l	#2,a2
	move.l	a2,ddb_ptr(a3)		; buffer byte sequence
	bra.s	pio_ok

pio_send
	move.b	d1,glo_pbyt(a2)
	st	glo_pstb(a2)		; set data strobe
	tst.l	$18000			; wait
	tst.b	glo_pstb(a2)		; reset data strobe

	move.w	#100,d0
pio_busy1
	tst.b	glo_pbusy(a2)		; wait while BUSY
	dbpl	d0,pio_busy1
	bmi.s	pio_ok			; not BUSY anymore, remove data

	move.w	#10000,d0
pio_busy2
	tst.b	glo_pbusy(a2)		; wait as long as BUSY is not set
	dbmi	d0,pio_busy2		; (for devices that don't "do" busy?)
pio_ok
	moveq	#$0,d0
	move.b	d0,glo_pbyt(a2) 	; reset data line
pio_rts
	rts

; PAR scheduler loop task
par_sched
	move.l	glx_ptch+glk.base,a2	; hardware base
	tst.b	glo_pbusy(a2)
	bpl.s	pio_rts 		; busy, exit

	lea	ddb_ptr(a3),a0
	cmpa.l	ddb_ptr(a3),a0		; anything in TRA buffer?
	beq.s	psch_chk_clch

	move.l	(a0),a0
	move.b	(a0)+,d1
	move.l	a0,ddb_ptr(a3)
	bra.s	pio_send

psch_chk_clch
	move.b	ddb_clch(a3),d1
	beq.s	pio_rts
	sf	ddb_clch(a3)
	bra.l	pio_tra

	end
