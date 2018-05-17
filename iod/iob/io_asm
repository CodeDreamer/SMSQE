; Buffering IO		 V2.00	   1989  Tony Tebby   QJUMP

	section iou

	xdef	iob_io
	xdef	iob_iohdr

	xref	iob_tbyt
	xref	iob_gbyt
	xref	iob_glin
	xref	iob_gmul
	xref	iob_pbyt
	xref	iob_pmul

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_buf'

;+++
; This routine does standard serial IO.
;
;	d0 cr	IO key / status
;	d1 cr	IO parameter
;	d2 c	IO parameter (could be extended or set to 15)
;	a1 cr	IO parameter
;	a2 c  p pointer to pointers to IO queues
;	a6 c  p pointer to sysvar
;
;	status returns standard, except end of file is +1
;
;---
iob_io
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
	dc.b	ibio_smul-ibio_btab
	dc.b	ibio_smul-ibio_btab
;+++
; This is the entry point to header IO
;
;	d0 cr	IO key / status
;	d1 cr	IO parameter
;	d2 c	IO parameter (could be extended or set to 15)
;	a1 cr	IO parameter
;	a2 c  p pointer to pointers to IO queues
;	a6 c  p pointer to sysvar
;
;	status returns standard, except end of file is +1
;
;---
iob_iohdr
ibio_fs
	sub.b	#iof.shdr,d0		 ; set header
	beq.s	ibio_shdr
	subq.b	#iof.rhdr-iof.shdr,d0	 ; read header
	beq.s	ibio_rhdr
	subq.b	#iof.load-iof.rhdr,d0	 ; load file
	beq.s	ibio_load
	subq.b	#iof.save-iof.load,d0	 ; save file
	beq.s	ibio_save

ibio_ipar
	moveq	#err.ipar,d0
ibio_rts
	rts

ibio_rhdr		; read header
	moveq	#15,d2			 ; read fifteen bytes
	tst.w	d1			 ; any read yet
	bne.s	ibio_load		 ; ... yes, fetch multiple
	bsr.s	ibio_tbyt		 ; test for $ff
	bne.s	ibio_rts
	addq.b	#1,d1			 ; was it $ff
	bne.s	ibio_ipar		 ; ... no, not header
	bsr.s	ibio_gbyt		 ; ... yes, fetch byte (and ignore it)
	moveq	#1,d1			 ; one read
	jmp	iob_gmul		 ; get the rest

ibio_shdr		; set header
	moveq	#15,d2			 ; send 15 bytes
	tst.w	d1			 ; any sent yet?
	bne.s	ibio_save		 ; ... yes, carry on
	st	d1
	bsr.s	ibio_sbyt		 ; send flag byte
	sf	d1
	bne.s	ibio_rts
	moveq	#1,d1			 ; one byte gone now
	jmp	iob_pmul		 ; send the rest

ibio_tbyt		; test byte
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jmp	iob_tbyt		 ; test for byte

ibio_fbyt		; fetch byte
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
ibio_gbyt
	jmp	iob_gbyt		 ; get byte

ibio_flin		; fetch line
	ext.l	d2
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jmp	iob_glin

ibio_fmul		; fetch multiple / load
	ext.l	d2
ibio_load
	move.l	bio_ibuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jmp	iob_gmul

ibio_sbyt		; send byte
	move.l	bio_obuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jmp	iob_pbyt

ibio_smul	       ; send multiple / save
	ext.l	d2
ibio_save
	move.l	bio_obuf(a2),d0 	 ; get buffer/queue pointer
	beq.s	ibio_ipar		 ; ... none
	move.l	d0,a2
	jmp	iob_pmul

	end
