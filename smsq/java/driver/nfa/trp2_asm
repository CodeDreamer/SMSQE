; Trap2 routines for win/sfa/nfa drivers	 v. 1.00 (C) W Lenerz 2012-2020.

; They just call the JVM.
; v. 1.00 2020 Feb 21 check when closing file tp see whether drive should be unlocked
; v. 0.01 10.07.2012 close file decreases nbr of open files, deletes chan defn blk

	section nfa

	xdef	nfa_opn
	xdef	nfa_clo
	xdef	nfa_fmt
	xdef	nfa_dlt

	xref	iou_close

	include 'dev8_keys_java'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
		
reg.opfl reg	d6

nfa_opn
	movem.l reg.opfl,-(sp)
	move.b	chn_drid(a0),iod_drid(a1) ; keep drive ID

	moveq	#0,d6			 ; get drive number
	move.b	iod_dnum(a4),d6
	swap	d6			 ; in msw
	move.l	a1,chn_ddef(a0) 	 ; set definition pointer
	move.l	d6,chn_drnr(a0) 	 ; drive / file ID 
	move.l	iod_rdln(a1),chn_feof(a0) ; and length

	movem.l (sp)+,reg.opfl
opold	moveq	#1,d0			; type of trap = open
	dc.w	jva.trp2		; do open in java
	tst.l	d0
nfa_rts rts


nfa_clo moveq	#2,d0			; close channel
	dc.w	jva.trp2		; do close in java
	jmp	iou_close

	tst.l	d0
	bmi.s	nfa_rts
	bpl.s	special_close		; special close (win, removable, last file closed)
	move.b	chn_drid(a0),d0
	lsl.b	#2,d0
	lea	sys_fsdd(a6),a2
	move.l	(a2,d0.w),a2
	subq.b	#1,iod_nrfl(a2) 	; one less channel open

rel_blk lea	chn_link(a0),a0
	lea	sys_fsdt(a6),a1
	move.w	mem.rlst,a4
	jsr	(a4)
	lea	-chn_link(a0),a0
	move.w	mem.rchp,a4
	jmp	(a4)


nfa_fmt moveq	#3,d0			; format medium
	dc.w	jva.trp2		; do in java
	tst.l	d0
	rts

nfa_dlt moveq	#4,d0			; delete file
	dc.w	jva.trp2		; do in java
	tst.l	d0
	rts

; a special close may happen when the last file of a removable win drive is closed
; in that case, delete the drive definition block
special_close
	cmp.l	#jva_xtra,d0		; is it really a special close?
	bne.s	err_rts 		; no, so really strange
	move.l	a0,-(a7)		; keep channel ID
	move.b	chn_drid(a0),d0
	lsl.b	#2,d0
	lea	sys_fsdd(a6),a2
	add.w	d0,a2
	move.l	(a2),d0 		; ptr to mem occupied by block
	beq.s	no_rel			; huh????????
	move.l	d0,a0
	clr.l	(a2)			; there is no pointer anymore
	move.w	mem.rchp,a2		; release this block
	jsr	(a2)
no_rel	move.l	(a7)+,a0
	moveq	#0,d0
	bra.s	rel_blk

err_rts moveq	#err.itnf,d0
	rts
	end
