; reassert extensions		 1988	Tony Tebby  Qjump

	section procs

	xdef	ut_reassert

	xref	ut_cnmar

bv_ntbas equ	$18
bv_ntp	 equ	$1c
bv_nlbas equ	$20
;+++
; Reasserts the definitions in the procedure table pointed to by a1.
; On return, the last routine to be replaced is pointed to by a1, this
; enables routines to be intercepted.
;
;	a1 cr	pointer to procedure table / pointer to routine replaced
;---
ut_reassert
reglist reg	d1/d2/d5/d6/a2/a3/a4/a5
	movem.l reglist,-(sp)
	sub.l	a4,a4			 ; no substitution
	move.l	a1,a0			 ; set pointer to proc tab
	move.w	#$0800,d5		 ; set type proc

utr_pfloop
	addq.l	#2,a0			 ; skip number of procs/fns

; go through proc_tab entries

pt_loop
	move.l	a0,a5
	move.w	(a0)+,d0		 ; get next offset
	beq.s	utr_end
	sub.l	a4,a4			 ; no substitution
	add.w	d0,a5			 ; set next address
	move.b	(a0),d6 		 ; set length of name
	subq.l	#1,a0			 ; pretend count is a word
	move.l	bv_ntbas(a6),a3 	 ; get name table address
nt_entry
	move.l	(a6,a3.l),d0		 ; is it empty
	beq.s	nt_next 		 ; ... yes
	move.l	bv_nlbas(a6),a1
	add.w	d0,a1			 ; address of name
	subq.l	#1,a1			 ;(pretent there is a word at the start)
	bsr.l	ut_cnmar
	bne.s	nt_next

	cmp.w	(a6,a3.l),d5		 ; is it the same type?
	bne.s	nt_set
	cmp.l	4(a6,a3.l),a5		 ; is it the same address?
	beq.s	nt_set
	move.l	4(a6,a3.l),a4		 ; keep old address
nt_set
	move.w	d5,(a6,a3.l)		 ; set name type
	move.l	a5,4(a6,a3.l)		 ; set procedure address
	move.l	a0,a2			 ; name address
	addq.l	#2,a2			 ; start of name characters
	move.b	d6,d0			 ; number of characters
nm_loop
	move.b	(a2)+,2(a6,a1.l)	 ; move character
	addq.l	#1,a1
	subq.b	#1,d0
	bgt.s	nm_loop
	bra.s	pt_next 		 ; and look at next proctab entry

nt_next
	addq.l	#8,a3			 ; next name table entry
	cmp.l	bv_ntp(a6),a3		 ; last?
	blt.s	nt_entry		 ; ... no take the next

pt_next
	and.w	#$00fe,d6		 ; make name length even byte
	add.w	d6,a0
	addq.w	#3,a0			 ; and move on to next word
	bra.s	pt_loop

utr_end
	bset	#8,d5			 ; now for functions
	beq.s	utr_pfloop		 ; ... done
	moveq	#0,d0
	move.l	a4,a1			 ; set substitution address
	movem.l (sp)+,reglist
	rts
	end
