; Move SuperBASIC area	    V2.00    1992  Tony Tebby	QJUMP

	section mem

	xdef	sb_mup
	xdef	sb_mdown

	include 'dev8_keys_sbasic'
	include 'dev8_keys_sys'
	include 'dev8_keys_jcbq'
	include 'dev8_keys_psf'
;+++
; Move SuperBASIC up
;
;	d1 c  p distance to move (+ve)
;	a0 c  p pointer to new TPA
;	a6 c  p pointer to system variables area (already adjusted)
;---
sb_mup
sbm.reg reg	d1/d2/a0/a1/a2/a3
	movem.l sbm.reg,-(sp)
	neg.l	d1			 ; moving the other way

	move.l	a0,d0
	sub.l	sys_sbab(a6),d0 	 ; length of new area
	lea	(a0,d1.l),a1		 ; old top
	move.l	a1,a3			 ; ... saved old top
	lsr.l	#5,d0
	bra.s	sbmu_mend

sbmu_move
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
sbmu_mend
	dbra	d0,sbmu_move

	move.l	a1,a2			 ; ... saved old bottom
	bra.s	sbm_adj
;+++
; Move SuperBASIC down
;
;	d0 cr	amount to move
;	d1 c  p distance to move +ve
;	a0 c  p new base of TPA
;	a6 c  p pointer to system variables area (already adjusted)
;---
sb_mdown
	movem.l sbm.reg,-(sp)
	move.l	a0,d0			 ; top of new area
	move.l	sys_sbab(a6),a0 	 ; bottom of new area
	sub.l	a0,d0			 ; amount to move
	lea	(a0,d1.l),a1		 ; bottom of old area
	move.l	a1,a2

	ror.l	#5,d0
	bra.s	sbmd_e32

sbmd_m32
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
sbmd_e32
	dbra	d0,sbmd_m32

	clr.w	d0
	rol.l	#3,d0
	bra.s	sbmd_mend
sbmd_ml
	move.l	(a1)+,(a0)+
sbmd_mend
	dbra	d0,sbmd_ml

	move.l	a1,a3			 ; ... saved top of ald area
	move.l	sys_sbab(a6),a0 	 ; restore bottom pointer

sbm_adj
	lea	sb_offs(a0),a0		 ; SBASIC
	moveq	#(sb_nmlsp+4)/4,d0	 ; SBASIC
smb_aadr				 ; SBASIC
	add.l	d1,(a0)+		 ; SBASIC
	dbra	d0,smb_aadr		 ; SBASIC

; not SBASIC	    move.l  sys_jbtb(a6),a1	     ; base of job table is job 0
; ..........	    sub.l   d1,(a1)		     ; adjust job pointer
; ..........	    cmp.l   (a1),a0		     ; correct now?
; ..........	    bne.s   sbm_bad
; ..........
; ..........	    sub.l   d1,jcb_a6(a0)	     ; adjust base pointers
; ..........	    move.l  jcb_a7(a0),a1	     ; stack pointer
; ..........	    cmp.l   a2,a1
; ..........	    blt.s   sbm_psf		     ; ... not moved
; ..........	    cmp.l   a3,a1
; ..........	    bge.s   sbm_psf		     ; ... no
; ..........	    sub.l   d1,a1
; ..........	    move.l  a1,jcb_a7(a0)	     ; adjust saved a7
; ..........
; ..........sbm_psf
; ..........	    move.l  sys_psf(a6),a0	     ; primary stack frame
; ..........	    move.l  psf_a6(a0),a1	     ; has a6 moved
; ..........	    cmp.l   a2,a1
; ..........	    blt.s   sbm_exit		     ; ... no
; ..........	    cmp.l   a3,a1
; ..........	    bge.s   sbm_exit		     ; ... no
; ..........	    sub.l   d1,a1
; ..........	    move.l  a1,psf_a6(a0)	     ; adjust saved a6
; ..........
; ..........	    move.l  usp,a1		     ; SP moved?
; ..........	    cmp.l   a2,a1
; ..........	    blt.s   sbm_exit		     ; ... no
; ..........	    cmp.l   a3,a1
; ..........	    bge.s   sbm_exit		     ; ... no
; ..........	    sub.l   d1,a1
; ..........	    move.l  a1,usp		     ; move SP
; ..........
sbm_exit
	movem.l (sp)+,sbm.reg
	moveq	#0,d0
	rts

; ..........sbm_bad
; ..........	    trap    #15
; ..........	    bra.s   sbm_bad
; ..........	    dc.w    'Allocate BASIC error'
	end
