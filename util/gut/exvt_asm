; gut_exvt   Check for one or two exceptions   Tony Tebby  1993
;	     Check for bus error
;	     Check for illegal instruction

	section gen_util

	xdef	gu_exvt
	xdef	gu_wbuse
	xdef	gu_rbuse
	xdef	gu_exii

	xref	sms_wbase

	include 'dev8_keys_68000'

gbt_rba2
	tst.b	(a2)			 ; simple test for bus error
	rts

gbt_wba2
	move.b	d1,(a2) 		 ; simple write
	rts
;+++
; This routine is called to check for a bus error on writing a byte to a
; given address
;
;	d0  r	0 OK, 1 bus error
;	d1 c  p byte to write
;	a0    s
;	a1    s
;	a2 c	address to read
;
;	status according to d0
;---
gu_wbuse
	lea	gbt_wba2,a0		 ; write byte to (a2)
	bra.s	gbt_do
;+++
; This routine is called to check for a bus error on reading a byte from a
; given address
;
;	d0  r	0 OK, 1 bus error
;	a0    s
;	a1    s
;	a2 c	address to read
;
;	status according to d0
;---
gu_rbuse
	lea	gbt_rba2,a0		 ; read byte from (a2)
gbt_do
	moveq	#exv_accf,d0		 ; bus error has been renamed access f
	bra.s	gu_exvt

;+++
; This routine is called to execute a subroutine with interception of
; the illegal isstruction trap. It should be called in supervisor mode.
;
; The routine returns d0=0 if illegal instruction is invoked.
;			=1 if illegal instruction invoked
;
;	d0  r	result
;	d1-d7	passed to routine, returned from routine
;	a0 c  s pointer to routine
;	a1    s must not be modified by routine
;	a3-a6	passed to routine, returned from routine
;
;	status returned according to d0
;
;---
gu_exii
	moveq	#exv_ilin,d0

;+++
; This routine is called to execute a subroutine with interception of one or
; two exception vectors. It should be called in supervisor mode.
;
; The two exception vector offsets should be in the upper and lower halves of
; d0. If there is only one, the upper half should be zero.
;
; The routine returns d0=0 if neither exception is invoked.
;			=1 if the exception in the lower word is invoked
;			=2 if the exception in the upper word is invoked
;
;	d0 cr	intercepts / result
;	d1-d6	passed to routine, returned from routine
;	d7    p
;	a0 c  s pointer to routine
;	a1    s must not be modified by routine
;	a3-a6	passed to routine, returned from routine
;
;	status returned according to d0
;
;---
gu_exvt
	move.l	d7,-(sp)
	move.w	d0,a1			 ; first exception to trap
	move.l	(a1),-(sp)		 ; save old vector
	move.w	a1,-(sp)		 ; and where it was
	pea	gxt_exv1
	move.l	d0,-(sp)
	move.l	4(sp),d0		 ; set new
	bsr.s	gxt_wbase
	move.l	(sp)+,d0

	swap	d0
	move.w	d0,a1			 ; second exception to trap
	move.l	(a1),(sp)		 ; save old vector
	move.w	a1,-(sp)		 ; and where it was
	beq.s	gxt_sstk
	pea	gxt_exv2
	move.l	(sp)+,d0		 ; set new
	bsr.s	gxt_wbase

gxt_sstk
	move.l	sp,a1			 ; our stack
	jsr	(a0)			 ; do routine
	moveq	#0,d7			 ; it was done

gxt_clean
	tst.w	(sp)			 ; any second vector?
	beq.s	gxt_cl2 		 ; ... no
	move.w	(sp),a1
	move.l	2(sp),d0		 ; restore second
	bsr.s	gxt_wbase
gxt_cl2
	addq.l	#6,sp
	move.w	(sp)+,a1
	move.l	(sp)+,d0		 ; restore first
	bsr.s	gxt_wbase

	move.l	d7,d0
	move.l	(sp)+,d7
	tst.l	d0
	rts

gxt_wbase
	swap	d0
	bsr.s	gxt_wb1 		 ; write msw
	swap	d0
gxt_wb1
	exg	a1,a5
	jsr	sms_wbase		 ; write word into base area
	exg	a5,a1
	rts

gxt_exv1
	moveq	#1,d7			 ; return 1
	bra.s	gxt_exv
gxt_exv2
	moveq	#2,d7			 ; return 2
gxt_exv
	move.l	a1,sp
	bra.s	gxt_clean		 ; and clean up

	end
