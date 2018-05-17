; Locate String Within String   1990  Tony Tebby

	section uq

	xdef	uq_instr
	xdef	uq_cinstr

	xref	uq_otabn

;+++
; Locates string (a1) within string (a0) (case independent)
;
;	d0  r	returns 0 (no match) or position + 1
;	d1-d4	scratch
;	a0 c s	pointer to string
;	a1 c s	pointer to string
;	status return Z found, NZ not found
;---
uq_instr
uis.reg reg	d5/d6/d7/a2
	movem.l uis.reg,-(sp)
	lea	uq_otabn,a2		 ; ... no case
	move.w	(a1)+,d3		 ; length of a1 string
	move.w	(a0)+,d4		 ; length of a0 string
	moveq	#0,d5			 ; word register
	move.l	a1,d1
	move.l	a0,d0
	move.l	d0,d7			 ; start value of registers

	subq.w	#1,d3			 ; number of additional chars to check
	blt.s	uis_1			 ; none at all!
	move.w	d3,d2			 ; saved

	sub.w	d3,d4			 ; difference = nr of checks
	ble.s	uis_nmatch		 ; no match found

	subq.w	#1,d4
uis_loop
	move.l	d0,a0			 ; start of this search
	move.b	(a0)+,d5		 ; next character
	move.l	a0,d0			 ; ... and next
	move.b	(a2,d5.w),d6
	move.b	(a1),d5
	cmp.b	(a2,d5.w),d6		 ; matches?
uis_le
	dbeq	d4,uis_loop		 ; ... yes, or no
	addq.l	#1,a1
	beq.s	uis_cle 		 ; ... yes

uis_nmatch
	moveq	#0,d0			 ; no match, return zero
uis_exit
	movem.l (sp)+,uis.reg
	rts
uis_1
	moveq	#1,d0			 ; null string is always match at pos 1
	bra.s	uis_exit
uis_done
	sub.l	d7,d0			 ; position of match
	bra.s	uis_exit

uis_cloop
	move.b	(a0)+,d5		 ; next character
	move.b	(a2,d5.w),d6
	move.b	(a1)+,d5
	cmp.b	(a2,d5.w),d6		 ; matches?
uis_cle
	dbne	d2,uis_cloop
	beq.s	uis_done		 ; OK

	move.l	d1,a1			 ; reset a1 (compare string)
	move.w	d3,d2			 ; and d2 (nr remaining chars to comp)
	dbra	d4,uis_loop		 ;
	bra.s	uis_nmatch

;+++
; Locates string (a1) within string (a0) (case dependent)
;
;	d0  r	returns 0 (no match) or position + 1
;	d1-d4	scratch
;	a0 c s	pointer to string
;	a1 c s	pointer to string
;	status return Z found, NZ not found
;---
uq_cinstr
uci.reg reg	d5/d7
	movem.l uci.reg,-(sp)
	move.w	(a1)+,d3		 ; length of a1 string
	move.w	(a0)+,d4		 ; length of a0 string
	move.b	(a1)+,d5		 ; first char
	move.l	a1,d1
	move.l	a0,d0
	move.l	d0,d7			 ; start value of registers

	sub.w	d3,d4			 ; difference = nr of checks - 1
	blt.s	uci_nmatch		 ; no match found

	subq.w	#1,d3			 ; number of additional chars to check
	blt.s	uci_1			 ; none at all!
	subq.w	#1,d3

uci_set
	move.l	d0,a0			 ; start of this search
uci_loop
	cmp.b	(a0)+,d5		 ; next character
uci_le
	dbeq	d4,uci_loop		 ; ... yes, or no
	bne.s	uci_nmatch

	move.l	d1,a1			 ; next char to check
	move.l	a0,d0			 ; save start
	move.w	d3,d2			 ; set d2 (nr remaining chars to comp)
	blt.s	uci_done

uci_cloop
	cmpm.b	(a0)+,(a1)+		 ; next character
uci_cle
	dbne	d2,uci_cloop
	beq.s	uci_done		 ; OK

	dbra	d4,uci_set		 ; carry on if we can

uci_nmatch
	moveq	#0,d0			 ; no match, return zero
uci_exit
	movem.l (sp)+,uci.reg
	rts
uci_1
	moveq	#1,d0			 ; null string is always match at pos 1
	bra.s	uci_exit
uci_done
	sub.l	d7,d0			 ; position of match
	bra.s	uci_exit

	end
