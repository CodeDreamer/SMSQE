; SBAS_LITEM - Locate Item		   V2.01    1992  Tony Tebby  QJUMP

; This routine currently assumes the fixed memory model

	section sbas

	xdef	sb_litem
	xdef	sb_lname
	xdef	sb_lnam2
	xdef	sb_gnam2

	xref	sb_anam2

	xref	cv_lctab

	include 'dev8_keys_sbasic'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'

;+++
; Locate Global SBASIC Name (a6,a1) length d2, and copy to local table
; If the global name is found, then the name entry and characters are
; copied to the local table. The copy includes the pointer to the value, this
; should be OK as the only names in the global name table are procedures and
; functions.
; This routine assumes that the name is not in the local table.
;
;	d2 c  p length of name
;	a1 c  p pointer to characters of name (rel A6)
;	a3  r	pointer to name table
;
;	status return standard
;---
sb_gnam2
sg2.reg reg	d1/d3/d4/d7/a0/a1/a2/a4
	movem.l sg2.reg,-(sp)
	move.w	sr,d7
	trap	#0			 ; supervisor mode to fiddle
	move.l	a6,-(sp)
	moveq	#0,d4
	move.b	d2,d4			 ; length in a word
	add.l	a6,a1			 ; absolute name address
	moveq	#sms.info,d0
	trap	#do.sms2		 ; find system vars
	move.l	sys_sbab(a0),a6
	add.w	#sb_offs,a6		 ; base of old SuperBASIC area
	move.w	d4,d2
	bsr.s	sbl_locate		 ; locate item in old area
	add.l	a6,a3			 ; absolute nt entry
	move.l	(sp)+,a6		 ; restore a6
	bne.s	sg2_exit		 ; name not found

	move.l	a3,a2			 ; old name table entry
	sub.w	d2,a4			 ; name characters
	move.l	a4,a1
	sub.l	a6,a1

	jsr	sb_anam2		 ; add name to table
	move.w	nt_usetp(a2),nt_usetp(a6,a3.l)
	move.l	nt_value(a2),nt_value(a6,a3.l) ; set table entry

sg2_exit
	move.w	d7,sr
	tst.l	d0
	movem.l (sp)+,sg2.reg
	rts

;+++
;
; Locate SBASIC name (a6,a1) length d2
;
;	d2 c  p length of name
;	a1 c  p pointer to characters of name (rel A6)
;	a3  r	pointer to name table
;
;	status return standard
;---
sb_lnam2
sl2.reg reg	d1/d3/d4/d7/a0/a2/a4
	movem.l sl2.reg,-(sp)
;*	  move.w  sr,d7
;*	  trap	  #0			   ; supervisor mode to fiddle
	add.l	a6,a1			 ; locate uses absolute a1
	bsr.s	sbl_locate
	sub.l	a6,a1
;*	  move.w  d7,sr
;*	  tst.l   d0
	movem.l (sp)+,sl2.reg
	rts

; subroutine to locate an item
;	d2 c  p length of name
;	a1 c  p pointer to characters of name (abs)
;	a3  r	pointer to table entry (rel a6)
;	a4  r	points beyond end of name list characters (abs)
;
;	smashes d1,d3,d4,a0,a2
;	status return standard

sbl_locate
	move.l	sb_nmtbp(a6),a3
	move.l	sb_nmtbb(a6),d4
	moveq	#0,d0
	lea	cv_lctab(pc),a2
	bra.s	sbl_eloop

sbl_loop
	move.w	nt_usetp(a6,a3.l),d0	 ; is it genuine?
	beq.s	sbl_next		 ; ... no
	move.w	nt_name(a6,a3.l),d0	 ; is there a name
	blt.s	sbl_next		 ; ... no
	move.l	sb_nmlsb(a6),a4
	add.l	a6,a4
	add.w	d0,a4
	move.l	a1,a0
	move.b	(a4)+,d3		 ; name length
	cmp.b	d2,d3			 ; the same?
	bne.s	sbl_next		 ; ... no
	moveq	#0,d0
sbl_nloop
	move.b	(a4)+,d0
	move.b	(a2,d0.w),d1
	move.b	(a0)+,d0		 ; the difference
	sub.b	(a2,d0.w),d1
	bne.s	sbl_next		 ; jump as soon as possible
	subq.b	#1,d3
	bne.s	sbl_nloop
	rts				 ; return OK

sbl_next
sbl_eloop
	subq.l	#8,a3
	cmp.l	d4,a3			 ; last?
	bge.s	sbl_loop		 ; ... no take the next

	moveq	#err.itnf,d0
	rts

;+++
; Locate SuperBASIC item (already in SuperBASIC context)
;
;	d0  r	item type/usage (lsword) or err.nf
;	a1 cr	pointer to name / pointer to item
;	a6 c... pointer to SuperBASIC
;
;	Status returned according to d0
;
;---
sb_litem
sbl.reg reg	d1/d2/d3/d4/a0/a2/a3/a4
	movem.l sbl.reg,-(sp)
	bsr.s	sb_lname
	move.l	4(a6,a3.l),a1		 ; address
	movem.l (sp)+,sbl.reg
	rts

;+++
; Locate SuperBASIC name (internal to SuperBASIC)
;
;	d0  r	item type/usage (lsword) or err.nf
;	d1-d4	scratch
;	a0	scratch
;	a1 c  p pointer to name (byte length string)
;	a2	scratch
;	a3  r	pointer to name table (=ntp if not found)
;	a4	scratch
;	a6 c... pointer to SuperBASIC
;
;	Status returned according to d0
;
;---
sb_lname
;*	  move.w  sr,d0
;*	  trap	  #0			   ; supervisor mode to fiddle
;*	  move.w  d0,-(sp)

	move.b	(a1)+,d2
	bsr.s	sbl_locate
	subq.l	#1,a1
	bne.s	sbl_end
	move.w	nt_usetp(a6,a3.l),d0
sbl_exit
;*	  move.w  (sp)+,sr
;*	  tst.l   d0
	rts
sbl_end
	move.l	sb_nmtbp(a6),a3
;*	  move.w  (sp)+,sr
;*	  tst.l   d0
	rts
	end
