; Initialise exception vectors	 V2.01	   1992 Tony Tebby
; 2013 Jan 25  2.02  correct stack compression (GG)
	section init

	xdef	init_exv
	xdef	init_exrv

	xref	init_wbase
	xref	sms_privv
	xref	sms_buse

	xref	gu_achpp

	include 'dev8_keys_sys'
	include 'dev8_keys_68000'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'


ix_wbase
	jmp	init_wbase

;+++
; Initialise Exception Vector Table To NOPs
; User mode execution errors are initialised a stop process routine.
; Non vectored interrupts are set to point to an RTE.
;
; The stop process routine, checks that the error occurred in USER mode, and
; If so it moves the execution address to the USER stack and sets the execution
; address to a BRA *, RTS.
;
;	d0/a0/a1/a2/a5 smashed
;
;---
init_exv
	lea	berr,a0 		 ; bus error
	lea	exv_accf,a5
	bsr.s	ix_wbase
	addq.l	#aerr-berr,a0
	bsr.s	ix_wbase

	add.w	#stop-aerr,a0
	moveq	#(exv_ispu-exv_ilin)/4-1,d1 ; illegal ins up to to spurious int
ixv_exv
	bsr.s	ix_wbase
	dbra	d1,ixv_exv

	lea	rte,a0
	moveq	#8+16-1,d1		 ; interrupt 0 to 7, trap 0 to 15
ixv_itrap
	bsr.s	ix_wbase
	dbra	d1,ixv_itrap
	rts

;+++
; Initialise Exception redirection table
;
;	d1/a0/a1/a2/a3/a5 smashed
;	a6 c  p pointer to sys vars
;
;	status return standard
;
;---
init_exrv
; rts *************
	moveq	#ixr.len,d0		 ; our vector
	jsr	gu_achpp
	bne.s	ixr_rts
	move.l	a0,a3

	lea	ixr_tab,a2
	lea	ixr_redir-2,a0		 ; bus error is off top of table
	lea	exv_accf,a5		 ; ... so set it
	bsr.s	ix_wbase

	moveq	#0,d1
	moveq	#ixr.len/4-1,d2

ixr_loop
	move.b	(a2)+,d1		 ; next exception
	move.l	d1,a5
	move.l	(a5),(a3)+		 ; save vector
	addq.w	#2,a0
	bsr.s	ix_wbase		 ; set new vactor
	dbra	d2,ixr_loop

	sub.w	#ixr_offs+ixr.len,a3
	move.l	a3,sys_ertb(a6)

	moveq	#0,d0
ixr_rts
	rts

ixr_buse				 ; bus error handler code
	jsr	sms_buse
	bra.s	ixr_redir		 ; if no recovery, treat as address err
	bra.s	ixr_buse		 ; bus error could be QL vector access
ixr_redir
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_privv		 ; try recovery
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
	bsr.s	ixr_rdo 		 ; get vector number on stack
ixr_privv
	jsr	sms_privv		 ; piviledge violation handler
ixr_rdo
; dc.w $4afb ***********************
	movem.l a5/a6,-(sp)		 ; save special reg set
	move.l	sms.sysb,a6		 ; system variable base
	move.l	sys_ertb(a6),a6 	 ; get ertab
	lea	ixr_redir-ixr_offs/2+2,a5
	sub.l	$8(sp),a5		 ; relative branch
	add.l	a5,a5			 ; for first vector a5 is now -ixr_offs
	sub.l	a5,a6
	move.l	(a6),$8(sp)		 ; so set return address
	movem.l (sp)+,a5/a6
	rts				 ; to junp to handler


ixr_tab dc.b	exv_aerr		 ; address error
	dc.b	exv_ilin
	dc.b	exv_div0
	dc.b	exv_chk
	dc.b	exv_trpv
	dc.b	exv_prvv
	dc.b	exv_trac
	dc.b	exv_i7
	dc.b	exv_trp5
	dc.b	exv_trp6
	dc.b	exv_trp7
	dc.b	exv_trp8
	dc.b	exv_trp9
	dc.b	exv_trpa
	dc.b	exv_trpb
	dc.b	exv_trpc
	dc.b	exv_trpd
	dc.b	exv_trpe
	dc.b	exv_trpf
ixr_offs equ	$54
ixr.len equ	$4c


	section base

berr
	nop
aerr
	move.l	a6,-(sp)		 ; save reg
	move.l	sms.sysb,a6
	tst.b	sys_ptyp(a6)		 ; what type of processor
	move.l	(sp)+,a6
	beq.s	stopa00 		 ; 00 08
	move.l	d0,-(sp)
	move.w	4+6(sp),d0
	lsr.w	#8,d0
	lsr.w	#4,d0			 ; index stack frame table
	move.b	exf_size(pc,d0.w),d0
	move.l	6(sp),6(sp,d0.w)	 ; compress stack  v. 3.14
	move.w	4(sp),4(sp,d0.w)
	move.l	(sp),(sp,d0.w)		 ; compress stack 3.14
	clr.w	$a(sp,d0.w)
	add.w	d0,sp
	move.l	(sp)+,d0
	bra.s	stop

exf_size dc.b	0,0,4,4,8,0,0,$34,$32,$0c,$18,$54,$10,0,0,0

stopa00
	addq.l	#8,sp			 ; skip junk
stop
	btst	#sr..s,(sp)		 ; supervisor mode?
	bne.s	rte			 ; !!!!!
	move.l	a0,-(sp)
	move	usp,a0
	move.l	6(sp),-(a0)		 ; instruction address
	move	a0,usp
	lea	bras,a0
	move.l	a0,6(sp)		 ; next instruction
	move.l	(sp)+,a0
rte
	rte

bras
	bra.s	*
	rts


	end
