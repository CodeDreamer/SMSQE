; SBAS_INIPR - Initialise SBASIC Procedures - QL compatible    1992  Tony Tebby

	section sbas

	xdef	sb_inipr
	xdef	sb_anam2

	xref	sb_resnt
	xref	sb_resnl
	xref	sb_lname
	xref	sb_pturbo

	xref	sb_aldat8
	xref	sb_clrstk

	xref	gu_achpp
	xref	gu_rchp
	xref	cv_lctab

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'

;+++
; SuperBASIC routine - initialise procedures and function
;
;	a1 c  p pointer to PROCTAB
;	a6 c  p pointer to SuperBASIC vars
;---
sb_inipr
sbi.reg reg	d1/d2/d3/d4/d5/d6/d7/a0/a2/a3/a4/a5
	movem.l sbi.reg,-(sp)
	move.w	sr,d7
	trap	#0
	move.w	d7,-(sp)
	move.l	a6,-(sp)
	move.l	a1,-(sp)
stk_a6	equ	4
stk_a1	equ	0

	moveq	#sms.info,d0
	trap	#do.sms2
	tst.l	d1			 ; is it Job 0?
	sne	d7			 ; set flag
	bne.s	sbi_start		 ; ... no
	move.l	sys_sbab(a0),a6 	 ; ... yes, add to SuperBASIC stub
	add.w	#sb_offs,a6
	move.l	sys_jbtb(a0),a0
	move.l	(a0),a5 		 ; and stub in job 0
	add.w	#sb_offs,a5

sbi_start
	move.w	#$0800,d5		 ; set type proc

sbi_pfloop
	tst.b	d7			 ; SBASIC or SuperBASIC?
	bne.l	sbi_sballc
	moveq	#4,d1
	add.w	(a1)+,d1		 ; number of procs/fns + spare
	lsl.l	#3,d1			 ; eight bytes each
	move.l	d1,d2			 ; ... in each table

	add.l	sb_nmtbp(a6),d1
	sub.l	sb_nmtbp+4(a6),d1	 ; extra room required
	bge.s	sbi_nlroom
	moveq	#0,d1			 ; do not allocate negative area!
sbi_nlroom
	add.l	sb_nmlsp(a6),d2
	sub.l	sb_nmlsp+4(a6),d2	 ; extra room required
	bge.s	sbi_alroom
	moveq	#0,d2			 ; do not allocate negative area!
sbi_alroom
	add.l	d1,d2			 ; total room to allocate
	beq.s	sbi_scan
	moveq	#15,d0
	add.l	d0,d2
	not.l	d0
	and.l	d0,d2			 ; round up
	move.l	d2,d0
	add.l	sb_nmlsp+4(a6),d0	 ; + old size
	sub.l	(a6),d0
	jsr	gu_achpp		 ; allocate new area
	bne.l	sbi_exit
	move.l	a0,a3			 ; new base
	move.l	a0,d3
	move.l	a6,a2			 ; old base
	add.l	(a6),a2
	sub.l	a2,d3			 ; amount moved

	move.l	sb_nmtbp+4(a6),d0	 ; copy up to and including name table
	sub.l	(a6),d0
	beq.s	sbi_mpnt
sbi_cpnt
	move.l	(a2)+,(a3)+
	subq.l	#4,d0
	bgt.s	sbi_cpnt


	add.l	d1,a3			 ; extra room required

	move.l	sb_nmlsp+4(a6),d0
	sub.l	sb_nmlsb(a6),d0 	 ; copy all of name list
	beq.s	sbi_mpnt
sbi_cpnl
	move.l	(a2)+,(a3)+
	subq.l	#4,d0
	bgt.s	sbi_cpnl

	move.l	a6,a0
	add.l	(a6),a0 		 ; old base
	jsr	gu_rchp

sbi_mpnt
	move.l	a6,a0
	moveq	#sb_nmtbp/4,d0		 ; amount to adjust
sbi_mloop
	add.l	d3,(a0)+		 ; adjust relative adresses
	dbra	d0,sbi_mloop

	add.l	d3,d1
	add.l	d1,(a0)+		 ; ... and name list
	add.l	d1,(a0)+
	add.l	d3,d2
	add.l	d2,(a0)+		 ; ... and top

	bra.s	sbi_scan

sbi_sballc			 ; allocate for SBASIC
	moveq	#4,d1
	add.w	(a1)+,d1		 ; number of procs/fns + spare
	lsl.l	#3,d1			 ; eight bytes each
	move.l	d1,-(sp)		 ; ... in each table
	jsr	sb_resnt
	move.l	(sp)+,d1
	jsr	sb_resnl


; go through proc_tab entries

sbi_scan
	move.l	sb_nmlsb(a6),a3
	add.l	a6,a3			 ; absolute name list base

sbi_ptloop
	move.l	a1,d3
	move.w	(a1)+,d0		 ; get next offset
	beq.l	sbi_pfend
	ext.l	d0
	add.l	d0,d3			 ; set next address
	moveq	#0,d1			 ; length in a word !!
	move.b	(a1)+,d1		 ; set length of name
	beq.l	sbi_ptnxt		 ; ... none

	move.l	sb_nmtbb(a6),a4 	 ; get name table address
	move.l	a1,d4			 ; save proctab address
	lea	cv_lctab(pc),a2 	 ; check table
	moveq	#0,d6			 ; check reg
	bra.s	sbi_ntend

sbi_ntloop
	tst.w	(a6,a4.l)		 ; is it empty
	beq.s	sbi_ntnxt		 ; ... yes
	move.w	nt_name(a6,a4.l),d0	 ; any name?
	bmi.s	sbi_ntnxt		 ; ... no
	lea	(a3,d0.w),a0		 ; address of name
	cmp.b	(a0)+,d1		 ; name the same length?
	bne.s	sbi_ntnxt
	moveq	#0,d2
	move.b	d1,d2			 ; do not smash length
	subq.w	#1,d2
sbi_cmploop
	move.b	(a0)+,d6
	move.b	(a2,d6.w),d0		 ; first lc char
	move.b	(a1)+,d6
	sub.b	(a2,d6.w),d0		 ; second lc char
	dbne	d2,sbi_cmploop
	bne.s	sbi_ptrest

	move.w	d5,(a6,a4.l)		 ; set name type
	move.l	d3,4(a6,a4.l)		 ; set procedure address

	move.b	d1,d0
sbi_onam
	move.b	-(a1),-(a0)		 ; overwrite old name
	subq.b	#1,d0
	bgt.s	sbi_onam
	bra.s	sbi_ptnxt		 ; and look at next proctab entry

sbi_ptrest
	move.l	d4,a1			 ; restore pointer to name
sbi_ntnxt
	addq.l	#8,a4			 ; next name table entry
sbi_ntend
	cmp.l	sb_nmtbp(a6),a4 	 ; last?
	blt.s	sbi_ntloop		 ; ... no take the next


	move.w	d5,(a6,a4.l)		 ; name type
	move.l	sb_nmlsp(a6),a0
	move.l	a0,d0
	sub.l	sb_nmlsb(a6),d0
	move.w	d0,2(a6,a4.l)		 ; name pointer
	move.l	d3,4(a6,a4.l)		 ; code address

	add.l	a6,a0
	move.b	d1,(a0)+		 ; length of name
	move.l	a1,a2
	move.b	d1,d0
sbi_cnloop
	move.b	(a2)+,(a0)+
	subq.b	#1,d0
	bgt.s	sbi_cnloop

	sub.l	a6,a0
	move.l	a0,sb_nmlsp(a6) 	 ; end of name
	addq.l	#8,sb_nmtbp(a6) 	 ; one more procedure / function

sbi_ptnxt
	bset	#0,d1			 ; make name length odd byte
	add.w	d1,a1			 ; and move on to next word
	bra	sbi_ptloop

sbi_pfend
	bset	#8,d5			 ; now for functions $0800 -> $0900
	beq	sbi_pfloop		 ; ... not done

sbi_ok
	moveq	#0,d0
sbi_exit
	tst.b	d7			 ; SBASIC?
	bne.s	sbi_rte 		 ; ... yes

	jsr	sb_pturbo		 ; patch turbo toolkit

	move.l	a5,d1
	sub.l	a6,d1			 ; the difference

	lea	sb_nmtbb(a6),a6
	lea	sb_nmtbb(a5),a5 	 ; the name table base pointer
	bsr.s	sbi_cptr		 ; name table base updated
	bsr.s	sbi_cptr		 ; name table pointer
	bsr.s	sbi_cptr		 ; name list base
	bsr.s	sbi_cptr		 ; name list pointer

	move.l	stk_a1(sp),a1		 ; reset name pointer @@@@@
	move.l	stk_a6(sp),a6		 ; and a6	      @@@@@
	not.b	d7			 ; clear global       @@@@@
	bra	sbi_start		 ; to redo proc funs for job 0 @@@@@

sbi_rte
	move.l	(sp)+,a1
	move.l	(sp)+,a6
	move.w	(sp)+,sr
	movem.l (sp)+,sbi.reg
	jmp	sb_change		 ; program changed

sbi_cptr
	move.l	(a6)+,d2
	sub.l	d1,d2
	move.l	d2,(a5)+		 ; update pointer
	rts

;+++
; Add a name to the table - fills in the type and dummy value as well.
; If there is an anything on the return stack, it clears it
;
;	d2 c  p length of name
;	a1 c  u pointer to chars of name (rel A6)
;	a3  r	pointer to new entry (rel A6)
;---
sb_anam2
sa2.reg reg	d1/d2/d3/a0/a2
stk_d2	equ	$04
	movem.l sa2.reg,-(sp)

	move.l	sb_retsp(a6),d0
	cmp.l	sb_retsb(a6),d0 	 ; anything on stack?
	beq.s	sa2_do			 ; ... no, get on with it
	jsr	sb_clrstk		 ; clear stack

sa2_do
	moveq	#1,d1
	add.w	d2,d1
	jsr	sb_resnl		 ; space for name
	moveq	#8,d1
	jsr	sb_resnt		 ; ... and table entry

	move.l	stk_d2(sp),d2
	move.l	sb_nmlsp(a6),a3
	move.l	a3,d3
	sub.l	sb_nmlsb(a6),d3 	 ; pointer to name list rel base

	move.b	d2,(a6,a3.l)		 ; length
	addq.l	#1,a3
sa2_cloop
	move.b	(a6,a1.l),(a6,a3.l)	 ; copy name
	addq.l	#1,a3
	addq.l	#1,a1
	subq.w	#1,d2
	bgt.s	sa2_cloop

	move.l	a3,sb_nmlsp(a6) 	 ; end of name list

	jsr	sb_aldat8		 ; initial allocation

	move.b	-1(a6,a3.l),d2		 ; final character
	sub.b	#'$',d2 		 ; string?
	beq.s	sa2_str
	subq.b	#'%'-'$',d2		 ; integer?
	beq.s	sa2_int

	move.l	a0,a3
	clr.l	(a3)+			 ; zero float
	move.l	#dt.flfp,(a3)+		 ; and flags
	moveq	#nt.fp,d0		 ; FP
	bra.s	sa2_set

sa2_int
	move.l	a0,a3
	clr.w	(a3)+			 ; zero int
	move.l	#dt.flint,(a3)+ 	 ; and flags
	moveq	#nt.in,d0
	bra.s	sa2_set

sa2_str
	move.l	#$000800ff,(a0)+	 ; null string flags
	clr.l	(a0)
	moveq	#nt.st,d0

sa2_set
	assert	0,nt.unset	    ; assume unset - this may be overwritten
	move.l	sb_nmtbp(a6),a3
	move.w	d0,nt_usetp(a6,a3.l)
	move.w	d3,nt_name(a6,a3.l)
	move.l	a0,nt_value(a6,a3.l)
	addq.l	#8,sb_nmtbp(a6)

	moveq	#0,d0
	movem.l (sp)+,sa2.reg

;+++
; Mark program changed
;---
sb_change
	assert	0,sb.edt-$ff,sb.edtn-$80
	tas	sb_edt(a6)		 ; edited! to redo name types
	sf	sb_cont(a6)		 ; do not continue
	move.w	#sb.nact,sb_actn(a6)	 ; but no action
	tst.l	d0
	rts
	end
