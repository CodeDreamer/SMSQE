; HOTKEY Expand Executable Item Name  V2.00     1988	Tony Tebby   QJUMP

	section hotkey

	xdef	hk_xname
	xdef	hk_wname

	xref	hk_getbf

	include 'dev8_ee_hk_data'

;+++
; This routine finds a wake name
;
;	d0	scratch
;	a1 cr	pointer to item / pointer to wake name
;	a3 c  p Hotkey linkage
;
;	status return arbitrary
;---
hk_wname
	movem.l a0/a2,-(sp)
	moveq	#hki.wsep,d0		 ; look for wsep
	bsr.s	hk_xndo 		 ; expand the name

	move.l	a0,a1			 ; wake name?
	move.l	a2,d0			 ; ... or this one?
	beq.s	hwn_exit		 ; ... no
	move.l	a2,a1			 ; ... yes

hwn_exit
	movem.l (sp)+,a0/a2
	rts

;+++
; This routine expands an executable item name
;
;	d0	scratch
;	a0  r	pointer to thing / filename
;	a1 cr	pointer to item / pointer to parameter string
;	a2  r	pointer to Job name
;	a3 c  p Hotkey linkage
;
;	status return arbitrary
;---
hk_xname
	moveq	#hki.jsep,d0		 ; we want Job name
hk_xndo
hxn.reg reg	d1/d2/d3/d4/a0/a4/a5
stk_a0	equ	$10
	movem.l hxn.reg,-(sp)
	move.b	d0,d3				 ; separator for last bit
	lea	hkd_buf1(a3),a0 		 ; buffer to fill
	lea	hki_name(a1),a4 		 ; running pointer to name
	sub.l	a1,a1				 ; no parameter
	sub.l	a2,a2				 ; no Job name
	move.w	(a4)+,d1			 ; length of item name
	move.w	#hkd.bufl-4,d4			 ; max length acceptable
	sub.w	d1,d4				 ; spare
	bhs.s	hxn_name
	add.w	d4,d1				 ; max length
	moveq	#0,d4				 ; ... no spare

hxn_name
	move.l	a0,stk_a0(sp)			 ; return this
	addq.l	#2,a0
	move.l	a0,a5
	bra.s	hxn_nlend
hxn_nloop
	move.b	(a4)+,d0
	cmp.b	#hki.psep,d0			 ; parameter?
	beq.s	hxn_parm
	cmp.b	#hki.jsep,d0			 ; Job name?
	beq.s	hxn_jobs
	cmp.b	#hki.wsep,d0			 ; Wake name?
	beq.s	hxn_jobs
	move.b	d0,(a0)+
hxn_nlend
	dbra	d1,hxn_nloop
	bra.s	hxn_done

hxn_parm
	move.l	a0,d0
	sub.l	a5,d0
	move.w	d0,-(a5)			 ; set length of previous

	and.w	#1,d0				 ; length odd?
	add.w	d0,a0				 ; next string starts here
	addq.l	#4,a0				 ; ... but parameter string!!!
	move.l	a0,a1				 ; ... it's the parameter
	addq.l	#2,a0
	move.l	a0,a5

	cmp.b	#hki.popn,(a4)+ 		 ; open bracket?
	bne.s	hxn_nparm			 ; ... no, I do not know what to do
	subq.w	#2,d1

hxn_ploop
	move.b	(a4)+,d0
	cmp.b	#hki.pcls,d0			 ; close?
	beq.s	hxn_jobn			 ; ... yes, look for Job name
	cmp.b	#hki.pstf,d0			 ; stuffer character?
	bne.s	hxn_pput

	moveq	#0,d0
	jsr	hk_getbf

	cmp.w	d4,d2				 ; enough room?
	ble.s	hxn_expe
	move.w	d4,d2				 ; ... no
	bra.s	hxn_expe
hxn_expl
	move.b	(a1)+,(a0)+
hxn_expe
	dbra	d2,hxn_expl

	lea	-2(a5),a1		 ; restore parameter pointer
	bra.s	hxn_plend

hxn_pput
	move.b	d0,(a0)+
hxn_plend
	subq.w	#1,d1
	bge.s	hxn_ploop
	bra.s	hxn_done

hxn_jobs
	subq.l	#1,a4				 ; back a bit
	addq.w	#1,d1
hxn_jobn
	move.b	(a4)+,d0			 ; next char
	cmp.b	d3,d0				 ; Job or Wake name?
	beq.s	hxn_fjbn			 ; ... yes
	cmp.b	#hki.jsep,d0			 ; Job name?
	bne.s	hxn_done			 ; ... no
hxn_fjbn
	subq.w	#2,d1				 ; any name?
	blt.s	hxn_done			 ; ... no
	move.l	a0,d0
	sub.l	a5,d0
	move.w	d0,-(a5)			 ; set length of previous

	and.w	#1,d0				 ; length odd?
	add.w	d0,a0				 ; next string starts here

	move.l	a0,a2				 ; ... it's the Job name
	addq.l	#2,a0
	move.l	a0,a5
hxn_jloop
	move.b	(a4)+,(a0)+
hxn_jlend
	dbra	d1,hxn_jloop

hxn_done
	move.l	a0,d0
	sub.l	a5,d0
	move.w	d0,-(a5)			 ; set length of previous

	move.l	a1,d0				 ; parameter set?
	beq.s	hxn_exit
	move.w	(a1),d0 			 ; ... length of string
	clr.w	-(a1)				 ; ... no channels
	addq.w	#4,d0				 ; four more bytes
	move.w	d0,-(a1)

hxn_exit
	movem.l (sp)+,hxn.reg
	rts

hxn_nparm
	sub.l	a1,a1				 ; no parameter
	bra.s	hxn_exit
	end
