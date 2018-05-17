; Check PAR/SER name   V2.00	 1989	Tony Tebby

	section par

	xdef	par_cknm
	xdef	par_pprty
	xdef	par_phand
	xdef	par_ptrn
	xdef	par_pcr
	xdef	par_pff

	xref	iou_dnam

	include 'dev8_keys_err'
	include 'dev8_keys_par'
;+++
; PAR / SER check name
;
;	d4  r	-1 PRT, 0 (PAR/PRT) or prd_ser1 (SER) (for prt_use)
;	d5  r	-1 output only, 0 input and output, 1 input only
;	a0 c  p pointer to name
;	a2 c  p pointer to space for parameters
;	a3 c  p pointer to linkage block (uses prd_ pare, sere, prt, prtd)
;
;---
par_cknm
pckn.reg reg	 d6/d7/a0/a3
frame	 equ	 12
	movem.l pckn.reg,-(sp)
	sub.w	#frame,sp

	move.w	(a0)+,d7		 ; number of characters
	cmp.w	#3,d7			 ; enough?
	blo.l	pckn_fdnf
	cmp.w	#9,d7			 ; too many
	bhi.l	pckn_fdnf

	move.l	#$dfdfdfff,d6
	and.l	(a0)+,d6		 ; first four
	move.b	#' ',d6 		 ; and the first three

	move.w	prd_prt(a3),d0		 ; PRT possible?
	beq.s	pckn_par
	cmp.l	#'PRT ',d6		 ; PRT?
	bne.s	pckn_par		 ; ... no, try PAR

	cmp.w	#3,d7			 ; ... just PRT?
	bne.l	pckn_fdnf
	moveq	#-1,d4
	moveq	#-1,d5			 ; output only
	lea	prd_prt(a3,d0.w),a0	 ; actual name
	move.w	(a0)+,d7
	move.l	(a0)+,d6		 ; PRT device
	bra.s	pckn_snm		 ; check the rest of the name

pckn_par
	cmp.l	#'PAR ',d6		 ; is it PAR?
	bne.s	pckn_ser		 ; ... no
	moveq	#0,d4			 ; PAR device
	moveq	#-1,d5			 ; output only
	tst.b	prd_pare(a3)		 ; is PAR enabled?
	bne.s	pckn_snm		 ; ... yes

pckn_ser
	tst.b	prd_sere(a3)		 ; is SER enabled?
	beq.s	pckn_fdnf		 ; .. no
	moveq	#prd_ser1/2,d4
	add.w	d4,d4
	moveq	#0,d5			 ; assume bidirectional
	cmp.l	#'SER ',d6
	beq.s	pckn_snm		 ; ... it is
	moveq	#-1,d5			 ; output only?
	cmp.l	#'STX ',d6
	beq.s	pckn_snm
	moveq	#1,d5			 ; input only?
	cmp.l	#'SRX ',d6
	bne.s	pckn_fdnf

pckn_snm
	move.l	sp,a3
	move.w	d7,(a3)+
	move.l	#'SER ',(a3)+		 ; put parameters
	move.b	-1(a0),-1(a3)
	move.l	(a0)+,(a3)+
	move.w	(a0)+,(a3)+
	move.l	sp,a0
	move.l	a2,a3

	jsr	iou_dnam		 ; decode name
	bra.s	pckn_exit
	bra.s	pckn_exit
	bra.s	pckn_exok
	dc.w	3,'SER'
	dc.w	6
	dc.w	-1,1			 ; number
par_pprty
	dc.w	4,'OEMS'		 ; parity
par_phand
	dc.w	3,'HIX' 		 ; handshake
par_ptrn
	dc.w	2,'DT'			 ; translate
par_pcr
	dc.w	3,'RCA' 		 ; <CR>
par_pff
	dc.w	2,'FZ'			 ; <FF> or CTRL Z

pckn_fdnf
	moveq	#err.fdnf,d0
	bra.s	pckn_exit
pckn_exok
	move.w	(a3),d0 		 ; port number
	beq.s	pckn_inam		 ; bad
	subq.w	#4,d0
	bhi.s	pckn_inam
	moveq	#0,d0
pckn_exit
	add.w	#frame,sp
	movem.l (sp)+,pckn.reg
	rts
pckn_inam
	moveq	#err.inam,d0
	bra.s	pckn_exit

	end
