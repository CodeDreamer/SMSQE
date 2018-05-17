; SBAS_ALWRK - allocate work space   V2.00	 1993	 Tony Tebby

	section sbas

	xdef	sb_alwrk
	xdef	sb_xpwrk
	xdef	sb_rewrk
	xdef	sb_shwrk

	xref	sb_insmem
	xref	gu_achp0
	xref	gu_rchp

	include 'dev8_keys_qdos_sms'

;+++
; SBASIC shrink work area
;
; This routine shrinks a work area.
;
;	a0  r	pointer to shrunk area
;	a1 c  p pointer to pointer to base of work area
;
;	All other registers preserved
;	Status return 0
;---
sb_shwrk
	move.l	d1,-(sp)
	move.l	(a1),a0 		 ; base
	move.l	a0,d0
	beq.s	sbs_exit
	move.l	4(a1),d1		 ; running pointer
	sub.l	a0,d1			 ; all we need
	moveq	#sms.schp,d0
	trap	#do.smsq
	add.l	a0,d1			 ; new top
	move.l	d1,8(a1)
sbs_exit
	movem.l (sp)+,d1		 ; restore registers
	moveq	#0,d0
	rts

;+++
; SBASIC expand work area
;
; This routine expands a work area.
; In this version it allocates a new, copies the old to the new and throws the
; old away
; It ususe and sets three consecutive long words, the base pointer, the running
; pointer (=base) and the top pointer.
;
; If there is not enough memory, it panics.
;
;	d0 cr	new space to allocate
;	a0  r	space allocated
;	a1 c  p pointer to pointer to base of work area
;
;	All other registers preserved
;	Status return 0
;---
sb_xpwrk
	move.l	d0,8(a1)		 ; size in top!
	jsr	gu_achp0		 ; allocate new
	bne.l	sb_insmem

	move.l	(a1),d0 		 ; old base
	movem.l d0/a0/a1/a2,-(sp)	 ; keep old base useful registers
	move.l	a0,(a1)+		 ; set base
	sub.l	a0,d0			 ; old base - new base

	move.l	(a1),a2 		 ; old pointer
	sub.l	d0,(a1)+		 ; ... new pointer
	move.l	a0,d0
	add.l	d0,(a1) 		 ; top

	movem.l (sp),a0/a1		 ; old and new bases
sbx_copy
	move.l	(a0)+,(a1)+		 ; copy
	cmp.l	a2,a0			 ; up to old pointer?
	blt.s	sbx_copy

	move.l	(sp)+,a0		 ; old base
	jsr	gu_rchp

	movem.l (sp)+,a0/a1/a2		 ; restore registers
	moveq	#0,d0
	rts

;+++
; SBASIC allocate work area
;
; This routine allocates a new work area, returning the old work area if
; necessary. It sets three consecutive long words, the base pointer, the running
; pointer (=base) and the top pointer.
;
; If there is not enough memory, it panics.
;
;	d0 cr	space to allocate
;	a0  r	space allocated
;	a1 c  p pointer to pointer to base of work area
;
;	All other registers preserved
;	Status return 0
;---
sb_alwrk
	move.l	d0,8(a1)		 ; size in top!
	bsr.s	sb_rewrk		 ; release old work area
	jsr	gu_achp0		 ; allocate new
	bne.l	sb_insmem
	move.l	a0,(a1)+		 ; set base
	move.l	a0,(a1)+		 ; ... pointer
	move.l	a0,d0
	add.l	d0,(a1) 		 ; top
	subq.l	#8,a1
	moveq	#0,d0
	rts

;+++
; SBASIC return work area
;
; This routine returns a work area, if allocated, clearing the base pointer
;
;	a1 c  p pointer to pointer to base of work area
;
;	All registers preserved
;	Status according to D0
;---
sb_rewrk
	tst.l	(a1)			 ; any old area?
	beq.s	sbr_exit
	move.l	a0,-(sp)
	move.l	(a1),a0
	clr.l	(a1)
	jsr	gu_rchp 		 ; return old
	move.l	(sp)+,a0
sbr_exit
	tst.l	d0
	rts
	end
