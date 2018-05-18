; Get parameters - QL Compatible    1990  Tony Tebby
;
; 2017-05-12  1.01  Fixed arithmetic-stack overflow on long strings (MK)

	section uq

	xdef	sb_gtlin
	xdef	sb_gtstr
	xdef	sb_gtfp
	xdef	sb_gtint

	xref	sb_resar

	xref	qr_nint
	xref	qr_nlint
	xref	qr_float
	xref	cr_deciw
	xref	cr_decil
	xref	cr_decfp
	xref	cr_fpdec
	xref	cr_iwdec

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

arith.spare equ $10

;+++
; Get Long Integer Parameters
;
;	a3 c  p pointer to base of parameters
;	a6 c  p pointer to top of parameters
;
;	status return standard
;---
sb_gtlin
	moveq	#nt.li,d0
	bra.s	sb_getp

;+++
; Get String Parameters
;
;	a3 c  p pointer to base of parameters
;	a6 c  p pointer to top of parameters
;
;	status return standard
;---
sb_gtstr
	moveq	#nt.st,d0
	bra.s	sb_getp

;+++
; Get Floating Point Parameters
;
;	a3 c  p pointer to base of parameters
;	a6 c  p pointer to top of parameters
;
;	status return standard
;---
sb_gtfp
	moveq	#nt.fp,d0
	bra.s	sb_getp

;+++
; Get Integer Parameters
;
;	a3 c  p pointer to base of parameters
;	a6 c  p pointer to top of parameters
;
;	status return standard
;---
sb_gtint
	moveq	#nt.in,d0

sb_getp
sgp.reg reg	d4/d6/d7/a0/a4/a5
	movem.l sgp.reg,-(sp)
	move.l	d0,d4
	move.l	a5,d3
	sub.l	a3,d3
	lsr.w	#3,d3			 ; number of parameters
	beq.l	sgp_dma1		 ; set dummy a1
	cmp.b	#1,-8(a6,a5.l)		 ; expression can only occur in Turbo TK
	bne.s	sgp_setup

	moveq	#0,d6			 ; once only through loop
	moveq	#$0f,d0
	and.b	-7(a6,a5.l),d0		 ; ignore sep
	beq.l	sgp_ipar
	move.b	d0,-7(a6,a5.l)
	cmp.b	#nt.fp,d0		 ; type of parameter
	bgt.l	sgp_xint
	bra.s	sgp_xfp

	cmp.b	d0,d4			 ; expected type?
	beq.l	sgp_eloop		 ; string is string
	bne.l	sgp_ipar		 ; trap converted string for the moment

sgp_setup
	move.w	d3,d6
	move.l	sb_arthp(a6),a1
	move.w	#arith.spare,a4 	 ; enough room for anything but string
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.l	sgp_eloop
	add.l	sb_arthl(a6),a4 	 ; arithmetic limit
	bra.l	sgp_eloop

sgp_loop
	subq.l	#8,a5			 ; next param
	move.l	sb_datab(a6),a0
	add.l	4(a6,a5.l),a0		 ; pointer to value

	move.w	#$0f0f,d0
	and.w	(a6,a5.l),d0		 ; ignore sep
	beq.l	sgp_ipar
	move.b	d0,1(a6,a5.l)		 ; clear sep

	cmp.w	#nt.arrst,d0		 ; string array?
	blt.s	sgp_var 		 ; ... no, var (or substring array)
	beq.l	sgp_stra		 ; ... yes

	cmp.w	#nt.rep<<8,d0
	blt.l	sgp_ipar		 ; must be array, sb proc or fun
	cmp.w	#nt.for<<8+nt.in,d0
	bgt.l	sgp_ipar		 ; or mc proc

sgp_var
	cmp.b	#nt.fp,d0		 ; type of parameter
	blt.l	sgp_str
	bgt.s	sgp_int

sgp_fp
	cmp.l	a4,a1			 ; enough room?
	bhs.s	sgp_pfp 		 ; ... yes
	bsr.l	sgp_alloc		 ; ... no
sgp_pfp
	subq.l	#6,a1			 ; push float
	move.w	(a6,a0.l),(a6,a1.l)
	move.l	2(a6,a0.l),2(a6,a1.l)
sgp_xfp
	cmp.b	d0,d4			 ; required type?
	beq.l	sgp_eloop		 ; ... yes
	bgt.s	sgp_nint		 ; ... no, nint it

	move.l	sb_buffb(a6),a0 	 ; convert into buffer
	jsr	cr_fpdec
	bra.s	sgp_mchr		 ; and move the characters to the stack

sgp_nint
	cmp.b	#nt.li,d4		 ; lint?
	beq.s	sgp_nlint
	jsr	qr_nint 		 ; nint it
	beq.l	sgp_eloop
	bra.l	sgp_exit

sgp_nlint
	jsr	qr_nlint		 ; nlint it
	beq.l	sgp_eloop
	bra.l	sgp_exit

sgp_int
	cmp.l	a4,a1			 ; enough room?
	bhs.s	sgp_pint		 ; ... yes
	bsr.l	sgp_alloc		 ; ... no
sgp_pint
	subq.l	#2,a1
	move.w	(a6,a0.l),(a6,a1.l)
sgp_xint
	cmp.b	d0,d4			 ; required type?
	beq.l	sgp_eloop		 ; ... yes
	bgt.s	sgp_extl		 ; long int
	cmp.b	#nt.fp,d4		 ; what type of thing?
	beq.s	sgp_float		 ; float

	move.l	sb_buffb(a6),a0 	 ; convert into buffer
	jsr	cr_iwdec

sgp_mchr
	move.l	a0,d0
	sub.l	sb_buffb(a6),d0 	 ; number of characters
	bra.l	sgp_sets

sgp_extl
	move.w	(a6,a1.l),d0		 ; sign extend integer to long
	ext.l	d0
	subq.l	#2,a1
	move.l	d0,(a6,a1.l)
	bra.l	sgp_eloop

sgp_float
	jsr	qr_float		 ; nint it
	bra.l	sgp_eloop

sgp_str
	tst.b	d0			 ; substring array?
	bne.s	sgp_ststr		 ; ... no
	cmp.w	#1,4(a6,a0.l)		 ; one dim?
	bne.l	sgp_ipar		 ; ... no
	move.w	6(a6,a0.l),d0		 ; length
	move.l	(a6,a0.l),a0		 ; pointer to value
	add.l	sb_datab(a6),a0
	add.w	d0,a0			 ; and end

	cmp.b	#1,d4			 ; required type?
	bne.s	sgp_cvstr		 ; ... no, convert string

	moveq	#0,d1
	move.w	d0,d1
	move.l	a1,d7
	sub.l	d1,d7			 ; appx arith stack pointer after op
	cmp.l	a4,d7			 ; enough room?
	bhs.s	sgp_scpys		 ; ... yes
	bsr.l	sgp_ald1		 ; ... no
sgp_scpys
	btst	#0,d0			 ; odd?
	beq.s	sgp_scpye		 ; ... no
	subq.l	#1,a1			 ; ... yes, odd byte on stack
	bra.s	sgp_scpye

sgp_scpyc
	subq.l	#1,a0
	subq.l	#1,a1
	move.b	(a6,a0.l),(a6,a1.l)	 ; copy characters
sgp_scpye
	subq.w	#1,d1
	bge.s	sgp_scpyc

	subq.l	#2,a1
	move.w	d0,(a6,a1.l)		 ; and string length
	bra.s	sgp_eloop

sgp_stra
	cmp.w	#1,4(a6,a0.l)		 ; one dim?
	bne.s	sgp_ipar		 ; ... no
	move.l	(a6,a0.l),a0		 ; pointer to value
	add.l	sb_datab(a6),a0

sgp_ststr
	move.w	(a6,a0.l),d0		 ; string length
	lea	2(a0,d0.w),a0		 ; and end

	cmp.b	#1,d4			 ; required type?
	bne.s	sgp_cvstr		 ; ... no, convert string

sgp_sets
	moveq	#1,d1			 ; odd?
	and.b	d0,d1
	add.w	d1,a0			 ; ... and end of chars
	add.w	d0,d1

	move.l	a1,d7
	sub.l	d1,d7			 ; appx arith stack pointer after op
	cmp.l	a4,d7			 ; enough room?
	bge.s	sgp_cpye		 ; ... yes		       *** 1.01
	bsr.s	sgp_ald1		 ; ... no
	bra.s	sgp_cpye

sgp_cpyc
	subq.l	#2,a0
	subq.l	#2,a1
	move.w	(a6,a0.l),(a6,a1.l)	 ; copy characters
sgp_cpye
	subq.w	#2,d1
	bge.s	sgp_cpyc

	subq.l	#2,a1
	move.w	d0,(a6,a1.l)		 ; and string length
	bra.s	sgp_eloop

sgp_cvstr
	move.l	a0,d7
	sub.w	d0,a0			 ; set pointers for conversion routines
	cmp.b	#3,d4			 ; which conversion
	blt.s	sgp_dfp 		 ; ... fp
	beq.s	sgp_diw 		 ; ... int

	jsr	cr_decil		 ; string to long int
	beq.s	sgp_eloop
	bra.s	sgp_exit

sgp_diw
	jsr	cr_deciw		 ; string to int
	beq.s	sgp_eloop
	bra.s	sgp_exit

sgp_dfp
	jsr	cr_decfp		 ; string to fp
	bne.s	sgp_exit

sgp_eloop
	dbra	d6,sgp_loop

	move.l	a1,sb_arthp(a6) 	 ; arithmetic stack pointer

sgp_ok
	moveq	#0,d0
	moveq	#0,d1
sgp_exit
	movem.l (sp)+,sgp.reg
	rts

sgp_dma1
	move.w	#sb_zero,a1		 ; (a6,a1.l) points to zero (nearly)
	bra.s	sgp_ok

sgp_ipar
	moveq	#err.ipar,d0
	bra.s	sgp_exit

sgp_alloc
	moveq	#arith.spare*2,d1	 ; allocate
sgp_ald1
	movem.l d0/d1/d2/d3,-(sp)
	move.l	a1,sb_arthp(a6)
	jsr	sb_resar		 ; some more space
	movem.l (sp)+,d0/d1/d2/d3
	move.l	sb_arthp(a6),a1
	move.w	#arith.spare,a4 	 ; enough room for anything but string
	add.l	sb_arthl(a6),a4 	 ; arithmetic limit
	rts

	end
