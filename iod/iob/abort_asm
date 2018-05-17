; Buffering (abort/clear) utility  V2.00    1989  Tony Tebby	QJUMP

	section iou

	xdef	iob_abort
	xdef	iob_clear

	xref	iob_pmul0
	xref	iob_eof
	xref	iob_allc
	xref	gu_rchp
	xref	iob_ptab

	include 'dev8_keys_buf'
	include 'dev8_keys_k'

iba.pre  equ	4
iba.post equ	2
iba_pre  dc.b	k.cr,k.nl,k.nl,k.nl
iba_post dc.b	k.cr,k.ff

;+++
; This routine sets up an ABORTED message in a new buffer, sets the
; get pointer to point to it and then throws away all the existing for
; files which are 'end of filed'.
;
; This routine must not be called from an interrupt server.
;
;	a2 c  p pointer to pointer to get buffer
;	all other registers including D0 preserved
;--
iob_abort
ibab.reg reg	d0-d3/d7/a0-a5
	move.l	(a2),d1 		 ; anything to do?
	beq.l	ibab_rts		 ; ... no
	movem.l ibab.reg,-(sp)
	jsr	iob_ptab		 ; printer aborted message in a1
	moveq	#8,d0			 ; enough for pre and post-ambles
	add.w	(a1),d0
	jsr	iob_allc
	blt.s	ibab_clr		 ; can't do, just clear buffers
	move.l	a2,a3			 ; save pointer to pointer
	move.l	a0,a2			 ; set pointer to buffer
	move.l	d1,a0			 ; old buffer
	move.l	buf_attr(a0),buf_attr(a2) ; set attributes
	clr.b	buf_eoff(a2)		 ; but not end of file attribute
	move.l	a3,buf_ptrg(a2) 	 ; and get pointer

	move.l	a1,a5
	lea	iba_pre,a1
	moveq	#iba.pre,d2
	jsr	iob_pmul0		 ; fill buffer
	exg	a1,a5
	move.w	(a1)+,d2
	jsr	iob_pmul0		 ; fill buffer
	exg	a1,a5
	moveq	#iba.post,d2
	jsr	iob_pmul0		 ; fill buffer


	jsr	iob_eof 		 ; and close it
	exg	a2,a3
	moveq	#-1,d3			 ; put first open buffer at start
	bra.s	ibab_do
;+++
; This routine throws away all the existing buffers for
; files which are 'end of filed'.
;
; This routine must not be called from an interrupt server.
;
;	a2 c  p pointer to pointer to get buffer
;	all other registers including D0 preserved
;--
iob_clear
	tst.l	(a2)			 ; anything to do?
	beq.s	ibab_rts
	movem.l ibab.reg,-(sp)

ibab_clr
	sub.l	a3,a3			 ; pointer to message
ibab_do
	tas	(a2)			 ; stop interrupt server
	move.l	(a2),d2
	bclr	#31,d2
ibab_next
	move.l	d2,d1			 ; next list of buffers
	beq.s	ibab_set		 ; ... no more
	move.l	d1,a4			 ; keep pointer to this list
	move.l	buf_nxtl(a4),d2 	 ; ... and the next

ibab_bloop
	move.l	d1,a1
	move.l	buf_nxtb(a1),d1 	 ; last buffer?
	bgt.s	ibab_bloop		 ; ... no
	tst.b	buf_eoff(a1)		 ; end of filed?
	bge.s	ibab_keep		 ; ... no

	move.l	a4,d1
ibab_cloop
	move.l	d1,a0			 ; next buffer
	move.l	buf_nxtb(a0),a1 	 ; ... and the next
	jsr	gu_rchp 		 ; return it
	move.l	a1,d1			 ; is there a next?
	bgt.s	ibab_cloop		 ; ... yes
	moveq	#0,d0
	bra.s	ibab_next		 ; ... no, look at next list

ibab_keep
	clr.l	buf_nxtl(a4)		 ; no next now

	move.l	a3,d0			 ; any to keep yet?
	beq.s	ibab_fbuf		 ; ... no, this must be the first buffer
	tst.b	d3			 ; keep this one at the beginning?
	bne.s	ibab_fbuf		 ; ... yes

ibab_lloop
	move.l	d0,a1
	move.l	buf_nxtl(a1),d0 	 ; last buffer list?
	bne.s	ibab_lloop		 ; ... no
	move.l	a4,buf_nxtl(a1) 	 ; ... yes, link in
	bra.s	ibab_next

ibab_fbuf
	move.l	a3,buf_nxtl(a4) 	 ; next to keep
	move.l	a4,a3			 ; first buffer in list to keep
	moveq	#0,d3			 ; normal now
	bra.s	ibab_next

ibab_set
	move.l	a3,(a2) 		 ; set get pointer
ibab_exit
	movem.l (sp)+,ibab.reg

ibab_rts
	tst.l	d0
	rts
	end
