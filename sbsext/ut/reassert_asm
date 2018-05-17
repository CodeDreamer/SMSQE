; reassert extensions		 1988	Tony Tebby  Qjump

	section procs

	xdef	ut_reassert

	xref	ut_cnmar

	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_sms'

bv_ntbas equ	$18
bv_ntp	 equ	$1c
bv_nlbas equ	$20
;+++
; Reasserts the definitions in the procedure table pointed to by a1.
;
;	a1 c  s pointer to procedure table
;---
ut_reassert
reglist reg	d5/d6/a2/a3/a4/a5
	moveq	#sms.info,d0
	trap	#do.smsq
	tst.l	d1
	bne.s	utr_do
	jmp	sb.inipr*3+qlv.off

utr_do
	movem.l reglist,-(sp)
	move.l	a1,a0			 ; set pointer to proc tab
	move.w	#$0800,d5		 ; set type proc

utr_pfloop
	addq.l	#2,a0			 ; skip number of procs/fns

; go through proc_tab entries

pt_loop
	move.l	a0,a5
	move.w	(a0)+,d0		 ; get next offset
	beq.s	utr_end
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
	movem.l (sp)+,reglist
	rts
	end
