; SER/PAR/PRT OPEN    V2.10	    1999   Tony Tebby

	section spp

	xdef	spp_open

	xref	spp_cknm
	xref	iou_achb
	xref	iou_rchb
	xref	iob_anew
	xref	iob_allc
	xref	iob_lnew
	xref	gu_rchp

	include 'dev8_keys_serparprt'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_sys'
	include 'dev8_keys_buf'
	include 'dev8_keys_err'

;+++
; PAR / SER channel open operations.
; This opens a PAR, SER PRT or emulation channel, and links the input/output
; queues/buffers into their respective linked lists.
;---
spp_open
	bra.s	sppo_name
	dc.w	3,'SPP'

sppo_name
frame	equ	$0c			 ; six parameters

	sub.w	#frame,sp
	move.l	sp,a2			 ; parameter frame pointer
	jsr	spp_cknm
	bne.l	sppo_exit

	cmp.b	#ioa.kdir,d3		 ; directory?
	bhs.l	sppo_dir		 ; ... apparently

	move.w	(a2)+,d1		 ; port number

sppo_alloc
	moveq	#spc_end,d0		 ; allocate channel block
	jsr	iou_achb
	bne.l	sppo_exit

	move.l	a3,spc_link(a0) 	 ; set linkage

	lea	spc_parm(a0),a1
	move.w	d1,(a1)+		 ; port number
	move.w	(a2)+,(a1)
	lsl.w	(a1)+			 ; parity doubled
	move.w	(a2)+,d0		 ; handshake
	beq.s	sppo_drct
	subq.b	#2,d0
	move.b	d0,spd_hand(a3) 	 ; in linkage

sppo_drct
	move.w	(a2)+,d0		 ; direct / xlate
	subq.b	#1,d0
	bge.s	sppo_sdrct		 ; it is set
	move.b	sys_xact(a6),d0 	 ; ... use default
sppo_sdrct
	move.w	d0,(a1)+

	move.w	(a2)+,d0		 ; <CR>
	beq.s	sppo_scr
	subq.w	#1,d0
	add.w	d0,d0
sppo_scr
	move.w	d0,(a1)+
	move.w	(a2)+,(a1)+		 ; <FF> CTRL Z

	tst.b	d5			 ; input buffer required?
	blt.s	sppo_obuf		 ; ... no
	clr.w	spd_cdct(a3)		 ; clear cd eof count
	lea	spc_ibuf(a0),a2
	lea	spd_ibuf(a3),a1
	tst.l	(a1)			 ; input buffer already there?
	bne.s	sppo_fdiu		 ; ... yes
	move.l	spd_ilen(a3),d0 	 ; buffer size
	jsr	iob_anew		 ; allocate a new one
	bne.s	sppo_rchb		 ; ... give up
	clr.l	buf_ptrg(a2)		 ; ... say permanent
	bsr.s	sppo_att		 ; set attributes
sppo_obuf
	tst.b	d5			 ; output?
	bgt.s	sppo_sop		 ; ... no
	move.l	spd_olen(a3),d0 	 ; this length
	move.l	a0,a5
	jsr	iob_allc		 ; and allocate
	exg	a0,a5
	bne.s	sppo_rchb		 ; ... no can do
sppo_sop
	move.l	spd_iopr(a3),spc_iopr(a0) ; set input
	move.l	spd_oopr(a3),spc_oopr(a0) ;    ... and output operations

	move.l	spd_open(a3),a4
	jsr	(a4)			 ; and setup / enable receiver
	bne.s	sppo_rchb		 ; ... oops, return channel block

	tst.b	d5			 ; output?
	bgt.s	sppo_exit		 ; ... no

	lea	spc_obuf(a0),a1 	 ; put in from here
	lea	spd_obuf(a3),a2 	 ; take out from here
	exg	a5,a0
	jsr	iob_lnew		 ; link buffer in
	move.l	a5,a0
	bsr.s	sppo_att		 ; set attributes
	moveq	#0,d0

sppo_exit
	add.w	#frame,sp
	tst.l	d0
	rts

sppo_dir
sppo_ipar
	moveq	#err.ipar,d0
	bra.s	sppo_exit

sppo_fdiu
	moveq	#err.fdiu,d0
sppo_rchb
	move.l	spc_obuf(a0),d1 	 ; remove output buffer
	beq.s	sppo_ribuf
	exg	d1,a0
	jsr	gu_rchp
	exg	a0,d1
sppo_ribuf
	move.l	spc_ibuf(a0),d1 	 ; any input buffer set up
	beq.s	sppo_ra0
	clr.l	spd_ibuf(a3)		 ; clear put pointer
	exg	d1,a0
	jsr	gu_rchp 		 ; return buffer
	exg	a0,d1
sppo_ra0
	jsr	iou_rchb		 ; now return the channel block
	bra.s	sppo_exit

sppo_att
	move.b	spc_fz+1(a0),spb_fz(a2)  ; set eof flag
	tst.b	d4			 ; par or ser
	beq.s	sppo_lfcr		 ; par, no parity
	move.w	spc_prty(a0),d0 	 ; and parity
	move.b	d0,spb_prty(a2)
sppo_lfcr
	tst.w	spc_lfcr(a0)		 ; <CR> conversion?
	ble.s	sppo_rts		 ; ... no
	add.w	d0,d0
	add.w	spc_lfcr(a0),d0
	move.w	sppo_lfcrtab-2(pc,d0.w),spb_lfcr(a2)
sppo_rts
	rts
sppo_lfcrtab
	dc.b  $0a,$0d,$00,$0d
	dc.b  $8a,$0d,$80,$0d
	dc.b  $0a,$8d,$00,$8d
	dc.b  $8a,$8d,$80,$8d
	dc.b  $0a,$0d,$00,$0d


	end
