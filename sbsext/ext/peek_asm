; PEEK / POKE		 V2.02	    1990  Tony Tebby	QJUMP
;
; 2005-12-10  2.01  Added PEEK_F, PEEKS_F, POKE_F, POKES_F (MK)
; 2005-12-29  2.02  Ensures that PEEK_F only returns valid floats (MK)
;
;	PEEK$ (addr,length)
;	PEEK (addr)
;	PEEK_W (addr)
;	PEEK_L (addr)
;	PEEK_F (addr)
;
;	POKE$ addr,values
;	POKE addr,values
;	POKE_W addr,values
;	POKE_L addr,values
;	POKE_F addr,values
;
;	PEEKS$ (addr,length)
;	PEEKS (addr)
;	PEEKS_W (addr)
;	PEEKS_L (addr)
;	PEEKS_F (addr)
;
;	POKES$ addr,values
;	POKES addr,values
;	POKES_W addr,values
;	POKES_L addr,values
;	POKES_F addr,values
;
	section exten

	xdef	peek$
	xdef	peek
	xdef	peek_w
	xdef	peek_l
	xdef	peek_f
	xdef	peeks$
	xdef	peeks
	xdef	peeks_w
	xdef	peeks_l
	xdef	peeks_f
	xdef	poke$
	xdef	poke
	xdef	poke_w
	xdef	poke_l
	xdef	poke_f
	xdef	pokes$
	xdef	pokes
	xdef	pokes_w
	xdef	pokes_l
	xdef	pokes_f

	xref	ut_gtst1
	xref	ut_gxin1
	xref	ut_gtli1
	xref	ut_gtlin
	xref	ut_rtsst
	xref	ut_retin
	xref	ut_rtfd1
	xref	ut_retfp
	xref	ut_ckri6
	xref	ut_gtfp

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'

*****************************************************
peeks$
	trap	#0
	pea	user
peek$
	moveq	#0,d7			 ; anything goes
	bsr.l	peek_add
	move.l	a1,sb_arthp(a6) 	 ; restore stack
	jsr	ut_gxin1		 ; string length
	bne.l	peek_rts
	move.w	(a6,a1.l),d1
	addq.l	#2,sb_arthp(a6)
	move.l	a0,a4
	jmp	ut_rtsst

****************************************************
peeks
	trap	#0
	pea	user
peek
	moveq	#0,d7			 ; anything goes
	bsr.l	peek_add
	moveq	#0,d1
	move.b	(a0),d1
	bra.s	peek_rtin
	bra.s	peek_rtin
	bra.s	peek_rtin
*****************************************************
peeks_w
	trap	#0
	pea	user
peek_w
	moveq	#1,d7			 ; odd address is bad
	bsr.l	peek_add
	move.w	(a0),d1
	bra.s	peek_rtin
	nop
peek_rtin
	subq.w	#2,a1
	move.w	d1,(a6,a1.l)
	jmp	ut_retin		 ; return integer

******************************************************
peeks_l
	trap	#0
	pea	user
peek_l
	moveq	#1,d7			 ; odd address is bad
	bsr.l	peek_add
	move.l	(a0),d1
	bra.s	peek_rtli
	nop
peek_rtli
	move.l	a1,sb_arthp(a6)
	jmp	ut_rtfd1		 ; return float

*********************************************
peeks_f
	trap	#0
	pea	user
peek_f
	moveq	#1,d7			; odd address is pad
	bsr.l	peek_add
	move.l	a1,sb_arthp(a6)
	bsr.l	ut_ckri6		; check room for 6 bytes
	subq.l	#6,a1
	move.w	(a0),d0 		; ensure value is valid
	bne.s	peek_chkf
	tst.l	2(a0)			; 0 exponent needs 0 mantissa
	bne.s	peek_orng
	bra.s	peek_okf
peek_chkf
	andi.w	#$f000,d0
	bne.s	peek_orng
peek_okf
	move.w	(a0)+,(a6,a1.l)
	move.l	(a0)+,2(a6,a1.l)
	bra.s	peek_rtf
	nop
peek_rtf
	jmp	ut_retfp

peek_orng
	moveq	#err.orng,d0
	rts

*********************************************
pokes$
pokes
	trap	#0
	pea	user
poke$
poke
	moveq	#0,d7			 ; anything goes
	bsr.l	peek_add
poke_loop
	moveq	#$0f,d0
	and.w	nt_usetp(a6,a3.l),d0
	subq.b	#nt.st,d0		 ; string?
	bgt.s	poke_byte		 ; ... no
	jsr	ut_gtst1		 ; get a string
	bne.s	poke_rts
	move.w	(a6,a1.l),d1		 ; length
	bra.s	poke_stle
poke_stloop
	move.b	2(a6,a1.l),(a0)+
	bra.s	poke_stnxt
	nop
poke_stnxt
	addq.l	#1,a1
poke_stle
	dbra	d1,poke_stloop
	bra.s	poke_le

poke_byte
	jsr	ut_gtli1		 ; get value
	bne.s	poke_rts
	move.b	3(a6,a1.l),(a0)+
	bra.s	poke_le
	nop
poke_le
	addq.l	#8,a3
	cmp.l	a5,a3			 ; all done?
	blt.s	poke_loop
poke_rts
	rts

*********************************************
pokes_w
	trap	#0
	pea	user
poke_w
	moveq	#1,d7			 ; odd address is bad
	bsr.s	peek_add

	jsr	ut_gtlin		 ; get values
	bne.s	poke_rts
	bra.s	poke_wend
poke_wloop
	move.w	2(a6,a1.l),(a0)+
	bra.s	poke_wnxt
	nop
poke_wnxt
	addq.l	#4,a1
poke_wend
	dbra	d3,poke_wloop
	rts

************************************************
pokes_l
	trap	#0
	pea	user
poke_l
	moveq	#1,d7			 ; odd address is bad
	bsr.s	peek_add

	jsr	ut_gtlin		 ; get values
	bne.s	poke_rts
	bra.s	poke_lend
poke_lloop
	move.l	(a6,a1.l),(a0)+
	bra.s	poke_lnxt
	nop
poke_lnxt
	addq.l	#4,a1
poke_lend
	dbra	d3,poke_lloop
	rts

************************************************
pokes_f
	trap	#0
	pea	user
poke_f
	moveq	#1,d7			 ; odd address is bad
	bsr.s	peek_add

	jsr	ut_gtfp
	bne.s	poke_rts
	bra.s	poke_fend
poke_floop
	move.w	(a6,a1.l),(a0)+
	move.l	2(a6,a1.l),(a0)+
	bra.s	poke_fnxt
	nop
poke_fnxt
	addq.l	#6,a1
poke_fend
	dbra	d3,poke_floop
	rts

;--------------------------------------------------
peek_add
	sub.l	a0,a0			 ; ... no offset
	tst.b	nt_nvalp(a6,a3.l)	 ; usage?
	bne.s	peek_direct		 ; ... it is direct

	moveq	#$18,d0
	add.l	a3,d0
	sub.l	a5,d0
	bgt.s	peek_ipar4		 ; there must be at least 3 params

	move.l	a6,a4			 ; assume SBASIC
	move.l	a6,a0
	cmp.w	#nt.bslsh<<nt.seps,nt_usetp(a6,a3.l) ; SB?
	beq.s	peek_indir
	cmp.w	#nt.exclm<<nt.seps,nt_usetp(a6,a3.l) ; sysvar?
	bne.s	peek_rt4
	sub.l	a4,a4			 ; nothing to add
	moveq	#sms.info,d0
	trap	#do.smsq		 ; base of sysvar
peek_indir
	addq.l	#8,a3
	tst.b	(a6,a3.l)		 ; null vector?
	beq.s	peek_offset
	jsr	ut_gtli1		 ; get vector
	bne.s	peek_rt4
	add.l	(a6,a1.l),a0
	addq.l	#4,a1
	move.l	a1,sb_arthp(a6)
	move.l	(a0),a0 		 ; vector
	add.l	a4,a0			 ; base

peek_offset
	addq.l	#8,a3			 ; ... next param
peek_direct
	jsr	ut_gtli1		 ; get address
	bne.s	peek_rt4
	addq.l	#8,a3			 ; and skip it
	add.l	(a6,a1.l),a0		 ; address
	addq.l	#4,a1
	move.l	a0,d1
	and.l	d1,d7			 ; error?
	beq.s	peek_rts		 ; ... no
peek_ipar4
	moveq	#err.ipar,d0
peek_rt4
	addq.l	#4,sp
peek_rts
	rts


user
	and.w	#$dfff,sr		 ; back to user mode
	rts

	end
