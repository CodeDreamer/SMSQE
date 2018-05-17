; Add a file to a recent list	V1.00   2015 W. Lenerz


	section things

	xdef	add2hp

	xref	cklp2

	include dev8_keys_recent_thing


; Add a file to a heap item pointed to by A3
; d3	s
; a1 c	s	parameter stack pointer  (name is at 4(a1))
; a2 c p	thing
; a3 c p	heap
; a4	s
; when we get here, the name is already capitalized and at rcnt_ucfn(a2)

hpreg	reg	d2/a2-a3
add2hp	movem.l hpreg,-(a7)
				
chk_ext move.l	4(a1),a4
	move.w	(a4),d2 		; length of name
	beq.s	out			; none, done
	move.l	rcnt_hend(a3),a4	; end of space for list

; now jump into the check file subroutine in things_asm
chk.reg reg	d1/d3/d7/a0/a3-a5	; **** keep this in sync with things_asm

	pea	ck_res			; we will return to this label
	movem.l chk.reg,-(sp)
	moveq	#hd_entryl-2,d3
	lea	rcnt_ucfn+2(a2),a5	; point to uncased filename, after length
	move.l	a5,d7
	lea	rcnt_hdr(a3),a3 	; point to start of list in heap
					; now check file exists in heap
	bra	cklp2			; this will return to next instr

ck_res	beq.s	out			; file was aleady in there
	move.l	a3,a2			; A2 = heap, space for list
	move.l	rcnt_1st(a2),a3 	; point to first entry in list
	move.l	a3,d3			; point to first entry in list

addlp1	tst.w	(a3)			; next entry - if it's empty, nothing there
	beq.s	add_fil 		; so add file here
	add.l	#hd_entryl,a3		; point to next space
	cmp.l	a4,a3			; shoot over end of space for list?
	blt.s	no_over 		; no
	lea	rcnt_hdr(a2),a3 	; point to start of space for list
no_over cmp.l	a3,d3			; did we come round to first entry?
	bne.s	addlp1			; no, so continue
					; here we looped around: list is full
	add.l	#hd_entryl,a3		; point to next space = last entry
	cmp.l	a4,a3			; would we overshoot end?
	blt.s	add_fil 		; no, fill in name
	lea	rcnt_hdr(a2),a3 	; point to start of space for list

add_fil move.l	a3,d3
	move.l	4(a1),a1		; name
	move.w	(a1)+,d0
	beq.s	out			; huh, 0 length file? change nothing, then
	cmp.w	#rc_maxlen,d0		; max allowed file name size
	ble.s	ad1
	move.w	#rc_maxlen,d0
ad1	move.w	d0,(a3)+		; file length
	subq.w	#1,d0
addlp2	move.b	(a1)+,(a3)+
	dbf	d0,addlp2
	move.l	d3,rcnt_1st(a2) 	; new first
out	movem.l (sp)+,hpreg		; done adding file
	clr.l	d0
	rts

	end
