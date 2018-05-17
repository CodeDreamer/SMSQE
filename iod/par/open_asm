; Open PAR, PRT or SER (or emulation) channel	V2.02	  1989  Tony Tebby

	section par

	xdef	par_open

	xref	par_cknm
	xref	iou_achb
	xref	iou_rchb
	xref	gu_rchp
	xref	iob_anew

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_buf'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_par'
	include 'dev8_mac_assert'
;+++
; PAR / SER channel open operations.
; This opens a PAR, SER PRT or emulation channel, and links the input/output
; queues/buffers into their respective linked lists.
;---
par_open
	bra.s	paro_name
	dc.w	3,'PAR'

paro_name
frame	equ	$0c			 ; six parameters
	sub.w	#frame,sp
	move.l	sp,a2			 ; parameter frame pointer
	jsr	par_cknm
	bne.l	paro_exit

	cmp.b	#ioa.kdir,d3		 ; directory?
	bhs.l	paro_dir		 ; ... apparently

	moveq	#1,d1			 ; assume port 1
	move.w	(a2)+,d2		 ; and get specified port

;**	   bra.s   paro_alloc
;**	   bra.s   paro_sern
;** dc.w '***NOP BRA before previous BRA for SERn'
;**paro_sern
	assert	prd.serx,1
	moveq	#-1,d0
	add.b	prd_port(a3),d0 	 ; how many ports?
	beq.s	paro_alloc
	blt.l	paro_nimp		 ; ... none!!!

	move.w	d2,d1			 ; port number
	subq.w	#1,d2
	cmp.w	d0,d2			 ; too many?
	bhi.l	paro_nimp		 ; ... yes
	mulu	#ser_inc,d2
	add.w	d2,a3			 ; adjust linkage
	tst.b	prd_serx(a3)		 ; this one available?
	assert	prd.serx,1
	ble.l	paro_nimp		 ; ... no

paro_alloc
	moveq	#prc_end,d0		 ; allocate channel block
	jsr	iou_achb
	bne.s	paro_exit

	lea	prc_parm(a0),a1
	move.w	d1,(a1)+		 ; port number
	move.w	(a2)+,(a1)
	lsl.w	(a1)+			 ; parity doubled
	move.w	(a2)+,d0		 ; handshake
	beq.s	paro_drct
	subq.b	#2,d0
	move.b	d0,prd_hand(a3) 	 ; in linkage

paro_drct
	move.w	(a2)+,d0		 ; direct / xlate
	subq.b	#1,d0
	bge.s	paro_sdrct		 ; it is set
	move.b	sys_xact(a6),d0 	 ; ... use default
paro_sdrct
	move.w	d0,(a1)+

	move.w	(a2)+,d0		 ; <CR>
	beq.s	paro_scr
	subq.w	#1,d0
	add.w	d0,d0
paro_scr
	move.w	d0,(a1)+
	move.w	(a2)+,(a1)+		 ; <FF> CTRL Z

	tst.b	d5			 ; input buffer required?
	blt.s	paro_obuf		 ; ... no
	assert	prd.serx,1
	tst.b	prd_serx(a3)		 ; serial?
	ble.s	paro_obuf		 ; ... no, check there is output
	lea	prc_ibuf(a0),a2
	lea	prd_ibuf(a3),a1
	tst.l	(a1)			 ; input buffer already there?
	bne.s	paro_fdiu		 ; ... yes
	move.l	prd_ilen(a3),d0 	 ; buffer size
	jsr	iob_anew		 ; allocate a new one
	bne.s	paro_rchb		 ; ... give up
	clr.l	buf_ptrg(a2)		 ; ... say permanent
	bsr.s	paro_att		 ; set attributes
paro_obuf
	tst.b	d5			 ; output?
	bgt.s	paro_sop		 ; ... no
	lea	prc_obuf(a0),a1 	 ; put in from here
	lea	prd_obuf(a3),a2 	 ; take out from here
	move.l	prd_olen(a3),d0 	 ; this length
	jsr	iob_anew		 ; and allocate
	bne.s	paro_rchb		 ; ... no can do
	bsr.s	paro_att		 ; set attributes
paro_sop
	move.l	prd_iopr(a3),prc_iopr(a0) ; set input
	move.l	prd_oopr(a3),prc_oopr(a0) ;    ... and output operations
	moveq	#0,d0
paro_exit
	add.w	#frame,sp
	tst.l	d0
	rts
paro_nimp
	moveq	#err.nimp,d0
	bra.s	paro_exit
paro_dir
paro_ipar
	moveq	#err.ipar,d0
	bra.s	paro_exit

paro_fdiu
	moveq	#err.fdiu,d0
paro_rchb
	move.l	prc_ibuf(a0),d1 	 ; any input buffer set up
	beq.s	paro_ra0
	clr.l	prd_ibuf(a3)		 ; clear put pointer
	exg	d1,a0
	jsr	gu_rchp 		 ; return buffer
	exg	a0,d1
paro_ra0
	jsr	iou_rchb		 ; now return the channel block
	bra.s	paro_exit

paro_att
	move.b	prc_fz+1(a0),prb_fz(a2)  ; set eof flag
	move.w	prc_prty(a0),d0 	 ; and parity
	move.b	d0,prb_prty(a2)
	tst.w	prc_lfcr(a0)		 ; <CR> conversion?
	ble.s	paro_rts		 ; ... no
	add.w	d0,d0
	add.w	prc_lfcr(a0),d0
	move.w	paro_lfcr-2(pc,d0.w),prb_lfcr(a2)
paro_rts
	rts
paro_lfcr dc.b	$0a,$0d,$00,$0d
	  dc.b	$8a,$0d,$80,$0d
	  dc.b	$0a,$8d,$00,$8d
	  dc.b	$8a,$8d,$80,$8d
	  dc.b	$0a,$0d,$00,$0d
	end
