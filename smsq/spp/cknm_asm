; Check SER/PAR/PRT  name   V2.10     1999  Tony Tebby

	section spp

	xdef	spp_cknm
	xdef	spp_pprty
	xdef	spp_phand
	xdef	spp_ptrn
	xdef	spp_pcr
	xdef	spp_pff

	xref	iou_dnam

	include 'dev8_keys_err'
	include 'dev8_keys_serparprt'
	include 'dev8_mac_assert'

;+++
; PAR / SER PRT check name
;
;	d4  r	msb -ve if PRT, lsb 0 (PAR) or $ff (SER) (for prt_use)
;	d5  r	byte -1 output only, 0 input and output, 1 input only
;	a0 c  p pointer to name
;	a2 c  p pointer to space for parameters
;	a3 cr	pointer to block of linkages / particular linkage block
;
;---
spp_cknm
spck.reg reg	 d6/d7/a0/a4
frame	 equ	 12
	movem.l spck.reg,-(sp)
	sub.w	#frame,sp

	moveq	#0,d4			 ; NOT PRT

spck_prt
	move.w	(a0)+,d7		 ; number of characters
	cmp.w	#3,d7			 ; enough?
	blo.l	spck_fdnf
	cmp.w	#9,d7			 ; too many
	bhi.l	spck_fdnf

	move.l	#$dfdfdfff,d6
	and.l	(a0)+,d6		 ; first four
	move.b	#' ',d6 		 ; and the first three

	cmp.l	#'PRT ',d6		 ; PRT?
	bne.s	spck_par		 ; ... no, try PAR

	cmp.w	#3,d7			 ; ... just PRT?
	bne.l	spck_fdnf

	moveq	#-1,d4			 ; flag PRT
	move.l	spd_pprt(a3),a0 	 ; prt definition
	assert	spd_pname,0
	bra.s	spck_prt

spck_par
	cmp.l	#'PAR ',d6		 ; is it PAR?
	bne.s	spck_ser		 ; ... no

	tst.l	d4			 ; PRT?
	blt.s	spck_parp		 ; ... yes
	tst.b	spd_parp(a3)		 ; is PAR enabled for PAR?
	bne.s	spck_parp

	moveq	#-1,d5			 ; output only
	tst.b	spd_pars(a3)		 ; is PAR enabled for SER?
	bne.s	spck_serp		 ; ... yes
	bra.l	spck_fdnf

spck_ser
	tst.l	d4			 ; PRT?
	blt.s	spck_stx		 ; ... yes, SER / STX only

	tst.b	spd_sers(a3)		 ; is SER for SER?
	bne.s	spck_schk		 ; ... it is
	tst.b	spd_serp(a3)		 ; is SER for PAR?
	beq.l	spck_fdnf		 ; ... no

	cmp.l	#'SER ',d6		 ; could be emulate ser on par
	beq.s	spck_parp		 ; ... it is
	bra.l	spck_fdnf

spck_schk
	moveq	#0,d5			 ; assume bidirectional
	cmp.l	#'SER ',d6
	beq.s	spck_serp		 ; ... it is
	moveq	#1,d5			 ; input only?
	cmp.l	#'SRX ',d6
	beq.s	spck_serp
spck_stx
	moveq	#-1,d5			 ; output only?
	cmp.l	#'STX ',d6
	beq.s	spck_serp		 ; ... yes
	cmp.l	#'SER ',d6		 ; ... no, could be PRT
	bne.l	spck_fdnf

spck_serp
	st	d4			 ; serial port
	move.w	spd_nser(a3),d6 	 ; number of SER ports
	beq.l	spck_fdnf
	move.l	spd_pser(a3),a4 	 ; set SER linkage
	bra.s	spck_parm

spck_parp
	sf	d4			 ; parallel port
	; note that d5 is not yet set if d4.b=0
	move.w	spd_npar(a3),d6 	 ; number of PAR ports
	beq.l	spck_fdnf
	move.l	spd_ppar(a3),a4 	 ; set PAR linkage

spck_parm
	move.l	sp,a3
	move.w	d7,(a3)+
	move.l	#'SER ',(a3)+		 ; put parameters
	move.b	-1(a0),-1(a3)
	move.l	(a0)+,(a3)+
	move.w	(a0)+,(a3)+
	move.l	sp,a0
	move.l	a2,a3

	jsr	iou_dnam		 ; decode name
	bra.s	spck_exit
	bra.s	spck_exit
	bra.s	spck_chkparm
	dc.w	3,'SER'
	dc.w	6
	dc.w	-1,1			 ; number
spp_pprty
	dc.w	4,'OEMS'		 ; parity
spp_phand
	dc.w	3,'HIX' 		 ; handshake
spp_ptrn
	dc.w	2,'DT'			 ; translate
spp_pcr
	dc.w	3,'RCA' 		 ; <CR>
spp_pff
	dc.w	2,'FZ'			 ; <FF> or CTRL Z


spck_chkparm
	move.w	(a3),d7 		 ; port number
	beq.s	spck_inam		 ; bad
	cmp.w	#1,d6			 ; more than one port?
	beq.s	spck_p1 		 ; ... no
	cmp.w	d6,d7
	bhi.s	spck_nimp
	move.w	d7,d0
	subq.w	#1,d0
	mulu	#spd.len,d0		 ; linkage offset
	add.w	d0,a4
	cmp.w	spd_port(a4),d7 	 ; the right port????
	beq.s	spck_pdir
	bra.s	spck_nimp

spck_p1
	move.w	#1,(a3) 		 ; always port 1 if only one port
spck_pdir
	move.l	a4,a3			 ; linkage block
	move.w	2(a3),d0		 ; parity / direction
	btst	d0,spd_pdir(a3) 	 ; allowed parity / dirction?
	beq.s	spck_inam		 ; ... no
	tst.b	d4			 ; PAR port?
	bne.s	spck_ok

	move.b	spck_dtab(pc,d0.w),d5	 ; bi_directional
	bpl.s	spck_inam		 ; input only not allowed

spck_ok
	moveq	#0,d0
spck_exit
	add.w	#frame,sp
	movem.l (sp)+,spck.reg
	rts
spck_inam
	moveq	#err.inam,d0
	bra.s	spck_exit
spck_fdnf
	moveq	#err.fdnf,d0
	bra.s	spck_exit
spck_nimp
	moveq	#err.nimp,d0
	bra.s	spck_exit

spck_dtab dc.b	-1,-1,1,0,0,1		; .OeMS?  ... E is not allowed

	end
