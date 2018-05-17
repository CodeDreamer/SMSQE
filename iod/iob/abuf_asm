; Buffering (allocate buffer) utility  V2.00    1989  Tony Tebby   QJUMP

	section iou

	xdef	iob_abuf
	xdef	iob_anew
	xdef	iob_allc
	xdef	iob_lnew

	xref	gu_achpp

	include 'dev8_keys_buf'
	include 'dev8_mac_assert'
;+++
; This routine allocates a new buffer for output and sets the buffer
; pointers.
;
; This must not be called from an interrupt server
;
; This is a clean routine.
;
;	d0  r	status: 0, or err.imem
;	a2 cr	pointer to previous buffer (or zero) / new buffer
;	a3  r	pointer to next in
;	all other registers preserved
;--
iob_abuf
ibab.reg reg	a0
	move.l	a0,-(sp)
	move.l	buf_nxtb(a2),a0 	 ; is there already one?
	move.l	a0,d0
	bne.s	ibab_set		 ; ... yes

	bsr.s	iob_allc		 ; allocate a new one (d0=0)
	blt.s	ibab_exit
	lea	buf_strt(a0),a3 	 ; set return reg value

ibab_set
	assert	0,buf_nxtb,buf_nxtl-4,buf_ptrp-8,buf_ptrg-$c,buf_attr-$10
	add.w	#buf_attr,a0
	add.w	#buf_attr,a2
	move.l	(a2),(a0)		 ; attributes
	move.l	-(a2),-(a0)		 ; get pointer
	move.l	-(a2),-(a0)		 ; put pointer
	subq.l	#buf_ptrp-buf_nxtb,a0
	subq.l	#buf_ptrp-buf_nxtb,a2
	move.l	a0,(a2) 		 ; link
	move.l	a0,a2			 ; ... now we use this one
	move.l	buf_ptrp(a2),a0
	move.l	a2,(a0) 		 ; set in pointer
	moveq	#0,d0
ibab_exit
	move.l	(sp)+,a0
	rts

;+++
; Allocate a new buffer. It is set up and linked in.
; To ensure that an interrupt server taking bytes out of the buffer does
; not make a total mess of the linking in code, the pointer to the get
; buffer list is temporarily set negative. The interrupt server should be
; able to handle this.
;
; The pointer to the put buffer should be zero.
;
;	d0 cr	0 for dynamic, otherwise queue length / status 0 or err.imem
;	a1 c  p pointer to pointer to buffer for putting
;	a2 c	pointer to pointer to buffer for getting
;	a2  r	buffer pointer
;	all other registers preserved
;---
iob_anew
iban.reg reg	a0/a1/a3
	movem.l iban.reg,-(sp)
	bsr.s	iob_allc		 ; allocate
	beq.s	iban_set
	bra.s	iban_exit		 ; ... bad
;+++
; Link in new buffer
; To ensure that an interrupt server taking bytes out of the buffer does
; not make a total mess of the linking in code, the pointer to the get
; buffer list is temporarily set negative. The interrupt server should be
; able to handle this.
;
; The pointer to the put buffer should be zero.
;
;	d0  r	0
;	a0 c  p pointer to buffer
;	a1 c  p pointer to pointer to buffer for putting
;	a2 c	pointer to pointer to buffer for getting
;	a2  r	pointer to buffer
;	all other registers preserved
;---
iob_lnew
	movem.l iban.reg,-(sp)
iban_set
	move.l	a2,buf_ptrg(a0) 	 ; put pointer to pointer
	move.l	a1,buf_ptrp(a0) 	 ; get pointer to pointer
	move.l	a0,(a1) 		 ; point to this put buffer

	tas	(a2)			 ; no pointer to get buffer list
	move.l	(a2),d0
	bclr	#31,d0
	tst.l	d0			 ; any get buffer list?
	bne.s	iban_look		 ; ... yes
	move.l	a0,(a2) 		 ; ... no, point to this buffer
	bra.s	iban_ok

iban_look
	move.l	d0,a1
	move.l	buf_nxtl(a1),d0 	 ; another in buffer list?
	bne.s	iban_look		 ; ... yes

	move.l	a0,buf_nxtl(a1) 	 ; add buffer to list
	bclr	#7,(a2) 		 ; and reset pointer to list

iban_ok
	move.l	a0,a2			 ; set buffer address
	moveq	#0,d0			 ; OK
iban_exit
	movem.l (sp)+,iban.reg		 ; restore registers
	rts

;+++
; Allocate a new buffer. It is set up but not linked in.
;
;	d0 cr	queue length / status 0 or err.imem
;	a0  r	buffer pointer
;	all other registers preserved
;---
iob_allc
	move.l	d0,-(sp)		 ; save buffer length (normal return)
	bgt.s	iba_slen
	move.l	#buf.dyna-buf_strt-1,d0 ; allocate dynamic buffer
iba_slen
	add.l	#buf_strt+1,d0		 ; header and one spare
	move.l	d0,-(sp)
	jsr	gu_achpp
	bne.s	ibab_exa		 ; ... no memory left

	move.l	a0,d0
	add.l	(sp)+,d0		 ; end of buffer
	add.w	#buf_endb,a0
	assert	buf_endb,buf_nxtp-4,buf_nxtg-8
	move.l	d0,(a0)+		 ; set end
	moveq	#buf_strt-buf_nxtp,d0
	add.l	a0,d0
	move.l	d0,(a0)+		 ; ... in
	move.l	d0,(a0) 		 ; ... and out
	lea	-buf_nxtg(a0),a0
	move.l	(sp)+,d0
	beq.s	ibab_rts		 ; ... dynamic
	tas	buf_nxtb(a0)		 ; mark it static queue
	moveq	#0,d0
ibab_rts
	rts

ibab_exa
	addq.l	#8,sp
	rts
	end
