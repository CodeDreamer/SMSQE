; Buffering IO with XLATE	V2.02	  1989  Tony Tebby   QJUMP

	section iou

	xdef	iob_iox

	xref	iob_iohdr

	xref	iob_tbpr
	xref	iob_gbpr
	xref	iob_glpr
	xref	iob_gmpr
	xref	iob_pbyt
	xref	iob_pmul
	xref	iob_pmba

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_k'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_buf'
;+++
; This routine does standard serial IO with translate and parity.
; Additionally, after an input or output operation, it calls the driver's
; own input or output routine.
;
; A2 points to a block of 4 long words:
;				Pointer to input buffer
;				Pointer to output buffer
;				Pointer to input routine
;				Pointer to output routine
;
;	d0 cr	IO key / status
;	d1 cr	IO parameter
;	d2 c	IO parameter (could be extended or set to 15)
;	d7 c  p MSB set for XLATE (output)
;		MSB of LSW <CR> (2 <CR> to <LF>, 4 <CR> to <CR><LF>)
;		LSB parity (2=odd, 4=even, 6=mark 8=space)
;	a1 cr	IO parameter
;	a2 cs	pointer to pointers to IO queues
;	a6 c  p pointer to sysvar
;
;	status returns standard, except end of file is +1
;
;---
iob_iox
	move.l	a2,-(sp)
	cmp.b	#iob.smul,d0		; simple byte io?
	bhi.s	ibio_fs 		; ... no, try filing system
	move.b	ibio_btab(pc,d0.w),d0
	jmp	ibio_btab(pc,d0.w)
ibio_btab
	dc.b	ibio_tbyt-ibio_btab
	dc.b	ibio_fbyt-ibio_btab
	dc.b	ibio_flin-ibio_btab
	dc.b	ibio_fmul-ibio_btab
	dc.b	ibio_ipar-ibio_btab
	dc.b	ibio_sbyt-ibio_btab
	dc.b	ibio_suml-ibio_btab
	dc.b	ibio_smul-ibio_btab

ibio_fs
	sub.b	#iof.load,d0		 ; load file
	beq.s	ibio_load
	subq.b	#iof.save-iof.load,d0	 ; save file
	beq.s	ibio_save
	tst.l	d7			 ; any parity or translate?
	bne.s	ibio_ipar		 ; ... yes
	move.l	(sp)+,a2		 ; ... no, try header
	add.b	#iof.save,d0		 ; restore d0
	jmp	iob_iohdr

ibio_ipar
	moveq	#err.ipar,d0
ibio_ex
	move.l	(sp)+,a2
ibio_rts
	tst.l	d0			 ; set condition codes
	rts

ibio_tbyt		; test byte
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jsr	iob_tbpr		 ; test for byte with parity
	bra.s	ibio_exin

ibio_fbyt		; fetch byte
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jsr	iob_gbpr		 ; get byte with parity
	bra.s	ibio_exin

ibio_flin		; fetch line
	ext.l	d2
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jsr	iob_glpr		 ; get line with parity
	bra.s	ibio_exin

ibio_fmul		; fetch multiple / load
	ext.l	d2
ibio_load
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jsr	iob_gmpr		 ; get multiple with parity
ibio_exin
	move.l	(sp)+,a2		 ; restore buffer pointers
	tst.l	bio_iopr(a2)		 ; any input routine?
	beq	ibio_rts		 ; ... no
	move.l	bio_iopr(a2),a2 	 ; ... yes, jump to it
	jmp	(a2)

ibio_sbyt		; send byte
	move.l	bio_obuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	move.l	d7,d0			 ; translate?
	bmi.s	ibio_tout		 ; ... yes
	lsr.w	#8,d0			 ; <CR>
	bne.s	ibio_cout		 ; ... yes
	jsr	iob_pbyt		 ; ... no
	bra.s	ibio_exout

ibio_suml	       ; send multiple untranslated
	ext.l	d2
	move.l	bio_obuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jsr	iob_pmul		 ; ... no
	bra.s	ibio_exout

ibio_smul	       ; send multiple / save
	ext.l	d2
ibio_save
	move.l	bio_obuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	move.l	d7,d0			 ; translate?
	bmi.s	ibio_tmul		 ; ... yes
	lsr.w	#8,d0			 ; <CR>
	bne.s	ibio_cmul		 ; ... yes
	jsr	iob_pmul		 ; ... no
	bra.s	ibio_exout

ibio_tmul
ibio_cmul
	moveq	#0,d0			 ; OK so far
	move.l	d2,-(sp)		 ; save count
	sub.l	d1,d2			 ; number to send
	bra.s	ibio_tle
ibio_tloop
	move.b	(a1),d1 		 ; next byte
	bsr.s	ibio_tra		 ; translate it
	bne.s	ibio_mret
	addq.l	#1,a1			 ; move on
	subq.l	#1,d2
ibio_tle
	bgt.s	ibio_tloop
ibio_mret
	moveq	#0,d1
	sub.l	d2,d1			 ; negative amount left
	move.l	(sp)+,d2
	add.l	d2,d1			 ; amount sent
	bra.s	ibio_exout

ibio_tout
ibio_cout
	bsr.s	ibio_tra		 ; translate out

ibio_exout
	move.l	(sp)+,a2		 ; restore buffer pointers
	tst.l	bio_oopr(a2)		 ; any output routine?
	beq	ibio_rts		 ; ... no
	move.l	bio_oopr(a2),a2 	 ; ... yes, jump to it
	jmp	(a2)

	page
; put byte with translate for JS and MG ROMs
ibio_tra
ix.reg	reg	d1/d2/a1
	movem.l ix.reg,-(sp)
	cmp.b	#k.ff,d1		 ; form feed?
	bhi.s	ix_trck
	beq.s	ix_ff
	cmp.b	#k.nl,d1		 ; newline?
	bne.s	ix_trck

	cmp.w	#$200,d7		 ; <CR>?
	blt.s	ix_trck 		 ; ... no

	moveq	#k.cr,d1
	cmp.w	#$400,d7		 ; <CR><LF>?
	blt.s	ix_sbyte		 ; ... no, <CR>

ix_crlf
	lea	ixb_crlf,a1
	moveq	#2,d2
	bra.s	ix_smul

ix_ff
	cmp.w	#$400,d7		 ; <CR><FF>
	blt.s	ix_trck 		 ; ... no
	lea	ixb_crff,a1
	moveq	#2,d2
	bra.s	ix_smul

ix_trck
	tst.l	d7			 ; translate?
	bpl.s	ix_sbyte		 ; ... no
ix_do
	move.l	sys_xtab(a6),a1 	 ; magic system variable
	add.w	2(a1),a1		 ; pointer to Xlate table
	moveq	#0,d2			 ; get word in d3
	move.b	d1,d2
	beq.s	ix_sbyte		 ; ... null
	move.b	(a1,d2.w),d1		 ; get xlated byte
	bne.s	ix_sbyte		 ; ... done

	move.l	sys_xtab(a6),a1 	 ; magic system variable
	add.w	4(a1),a1		 ; pointer to extension table
	move.b	(a1)+,d1		 ; number of entries
	bra.s	ix_elook
ix_look
	cmp.b	(a1)+,d2		 ; our character?
	beq.s	ix_sthree		 ; ... yes, send next three bytes
	addq.l	#3,a1			 ; ... next character
	subq.b	#1,d1
ix_elook
	bne.s	ix_look

	moveq	#' ',d1
	bra.s	ix_sbyte		 ; not found, send space

ix_sthree
	moveq	#3,d2
ix_smul
	jsr	iob_pmba		 ; put three bytes or none
	bra.s	ix_exit
ix_sbyte
	jsr	iob_pbyt		 ; put one byte
ix_exit
	movem.l (sp)+,ix.reg
	rts

ixb_crlf dc.b	k.cr,k.lf
ixb_crff dc.b	k.cr,k.ff

	end
