; Simple Serial IO handling  QL Compatible    1993  Tony Tebby
;
; 2011-04-29  1.01  Fixed uq_ssq (GG)

	section uq

	xdef	uq_ssio
	xdef	uq_ssq

	xref	ioq_tstg
	xref	ioq_gbyt
	xref	ioq_pbyt

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_k'


uq_ssq
	move.l	d1,d3
	lea	$1c(a0),a2		 ; standard queue
	cmp.b	#iob.fmul,d0		 ; fetch
	bhi.s	usq_check
	subq.l	#4,a2
usq_check
	move.l	(a2),d3 		 ; any queue?
	beq.l	usio_ipar
	move.l	d3,a2			 ; the queue

	pea	ioq_pbyt
	pea	ioq_gbyt
	pea	ioq_tstg
	move.l	sp,a4
	bsr.s	usio_do
	lea	12(sp),sp
	rts

uq_ssio
	move.l	(sp)+,a4		 ; pointer to routines
	pea	$c(a4)			 ; return address
usio_do
	move.l	d1,d3
	moveq	#0,d1
	ext.w	d0
	subq.w	#iob.smul+1,d0		; simple byte io?
	blo.s	usio_jmp		; ... yes
	sub.w	#iof.shdr-(iob.smul+1),d0 ; File io?
	blt.s	usio_ipar		; ... no
	cmp.w	#iof.save-iof.shdr,d0
	bhi.s	usio_ipar

usio_jmp
	move.b	usio_btab(pc,d0.w),d1
	jmp	usio_btab(pc,d1.w)

	dc.b	usio_tbyt-usio_btab
	dc.b	usio_fbyt-usio_btab
	dc.b	usio_flin-usio_btab
	dc.b	usio_fmul-usio_btab
	dc.b	usio_elin-usio_btab
	dc.b	usio_sbyt-usio_btab
	dc.b	usio_ipar-usio_btab
	dc.b	usio_smul-usio_btab
usio_btab
	dc.b	usio_shdr-usio_btab
	dc.b	usio_rhdr-usio_btab
	dc.b	usio_load-usio_btab
	dc.b	usio_save-usio_btab

usio.sreg  reg	d2/d3/a1/a4
usio_rhdr		; read header
	moveq	#15,d2
	tst.w	d3			 ; any read?
	bgt.l	usio_floop		 ; yes
	movem.l usio.sreg,-(sp)
	move.l	(a4),a4 		 ; test
	jsr	(a4)
	movem.l (sp)+,usio.sreg
	tst.l	d0
	bne.s	usio_exd1
	addq.b	#1,d1			 ; $ff?
	bne.s	usio_ipar

	movem.l usio.sreg,-(sp)
	move.l	4(a4),a4		 ; fetch
	jsr	(a4)
	movem.l (sp)+,usio.sreg

	moveq	#1,d3			 ; one byte fetched
	bra.s	usio_floop

usio_shdr		; set header
	moveq	#15,d2
	tst.w	d3			 ; any sent?
	bgt.s	usio_sloop		 ; yes
	st	d1			 ; ... no, send flag
	movem.l usio.sreg,-(sp)
	move.l	8(a4),a4		 ; send
	jsr	(a4)
	movem.l (sp)+,usio.sreg
	tst.l	d0
	bne.s	usio_exd1
	moveq	#1,d3			 ; one byte sent
	bra.s	usio_sloop

usio_elin
usio_ipar
	moveq	#err.ipar,d0
usio_exd1
	move.l	d3,d1
	tst.l	d0
	rts

usio_tbyt		; test byte
	move.l	(a4),a4
	jmp	(a4)

usio_fbyt		; fetch byte
	move.l	4(a4),a4
	jmp	(a4)

usio_flin		; fetch line
	cmp.w	d2,d3
	bge.s	usio_bffl		 ; buffer full
	movem.l usio.sreg,-(sp)
	move.l	4(a4),a4		 ; fetch
	jsr	(a4)
	movem.l (sp)+,usio.sreg
	tst.l	d0
	bne.s	usio_exd1

	move.b	d1,(a1)+
	addq.l	#1,d3
	cmp.b	#k.nl,d1		 ; end of line?
	bne.s	usio_flin		 ; ... no
	bra.s	usio_exd1

usio_bffl
	moveq	#err.bffl,d0
	bra.s	usio_exd1

usio_fmul		; fetch multiple / load
	ext.l	d2
usio_load
	moveq	#0,d0
usio_floop
	cmp.l	d2,d3
	bge.s	usio_exd1		 ; buffer full
	movem.l usio.sreg,-(sp)
	move.l	4(a4),a4		 ; fetch
	jsr	(a4)
	movem.l (sp)+,usio.sreg
	tst.l	d0
	bne.s	usio_exd1

	move.b	d1,(a1)+
	addq.l	#1,d3
	bra.s	usio_floop

usio_sbyt		; send byte
	move.b	d3,d1			 ; byte to send
	move.l	8(a4),a4
	jmp	(a4)

usio_smul	       ; send multiple / save
	ext.l	d2
usio_save
	moveq	#0,d0
usio_sloop
	cmp.l	d2,d3
	bge.s	usio_exd1		 ; all gone
	move.b	(a1),d1

	movem.l usio.sreg,-(sp)
	move.l	8(a4),a4		 ; send
	jsr	(a4)
	movem.l (sp)+,usio.sreg
	tst.l	d0
	bne.s	usio_exd1
	addq.l	#1,a1
	addq.l	#1,d3
	bra.s	usio_sloop

	end
