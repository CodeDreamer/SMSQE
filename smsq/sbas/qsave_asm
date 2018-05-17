; SMSQ_SBAS_QSAVE   QLOAD (Merge) V2.00

	section sbas

	xdef	sb_qsave

	xref	gu_achp0
	xref	gu_rchp

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_parser_keys'

;+++
; SBASIC QSAVE
;
;	a0 cp	channel ID
;
;	Status return 0 or -ve
;---
sb_qsave
	move.l	a0,-(sp)		 ; save channel
	move.l	a0,a2
	move.l	sb_nmtbp(a6),d0
	sub.l	sb_nmtbb(a6),d0 	 ; size of name table
	move.l	d0,d1
	addq.l	#8,d0			 ; spare entry for top
	jsr	gu_achp0
	bne.l	sqs_exit0
	st	(a0,d1.l)		 ; flag top of table
	move.l	a0,-(sp)		 ; save base of table

	move.l	sb_cmdlt(a6),d0 	 ; command line top
	sub.l	sb_cmdlb(a6),d0 	 ; base
	jsr	gu_achp0		 ; allocate a copy
	bne.l	sqs_rchp4
	move.l	a0,-(sp)

	move.l	a2,a0			 ; channel

; first we fill our dummy name table
;
;	bytes	0-1		as NT ORed with $1000 for formal params and pf
;				      ORed with $2000 for FOR loop vars
;				      ORed with $4000 for WHEN vars
;	bytes	2-3		reduced name table number
;	bytes	4-7		not used

sqs..def equ	12
sqs..for equ	13
sqs..when equ	14

; to do this we scan the program looking for names

	move.l	4(sp),a2		 ; the table

	move.l	sb_srcep(a6),d4 	 ; go up to here
	move.l	sb_srceb(a6),a4 	 ; starting here
	move.l	sb_nmtbb(a6),a3
	add.l	a6,a4
	add.l	a6,d4
	add.l	a6,a3

	moveq	#0,d5			 ; count lines
sqs_lloop
	cmp.l	d4,a4			 ; at end yet?
	bge.l	sqs_wtable
	addq.w	#1,d5			 ; one more line
	addq.l	#6,a4			 ; skip link and line number

sqs_stloop
	moveq	#0,d7			 ; nothing special with names
sqs_stspce
	moveq	#$4f,d1
	and.b	(a4),d1
	move.w	(a4)+,d0		 ; start of statement token
	tst.b	d1			 ; spaces?
	beq.s	sqs_stspce
	cmp.w	#tkw.for,d0		 ; FOR?
	beq.s	sqs_for
	cmp.w	#tkw.when,d0		 ; WHEN?
	beq.s	sqs_when
	cmp.w	#tkw.def,d0		 ; DEF?
	bne.s	sqs_jmp
	bra.s	sqs_def

sqs_for
	move.b	(a4),d1
	move.w	(a4)+,d0		 ; next token
	cmp.b	#tkb.spce,d1		 ; spaces?
	beq.s	sqs_for
	move.w	(a4)+,d1		 ; name number
	lsl.w	#nt.ishft,d1		 ; shifted
	move.w	#1<<sqs..for,d0 	 ; must be name token for index
	or.w	(a3,d1.w),d0		 ; usage / type combined with flags
	cmp.w	#1<<sqs..for+nt.var<<8+$ff,d0 ; var?
	bgt.s	sqs_fors
	or.w	#nt.for<<8,d0		 ; make it for
sqs_fors
	or.w	d0,(a2,d1.w)		 ; marked in table
	bra	sqs_ntok

sqs_when
	move.b	(a4),d1
	move.w	(a4)+,d0		 ; next token
	cmp.b	#tkb.spce,d1		 ; spaces?
	beq.s	sqs_when
	cmp.b	#tkb.name,d1		 ; name?
	bne.s	sqs_ntok		 ; ... no, must be err
	move.w	#1<<sqs..when,d0	 ;
	bra.s	sqs_nflag

sqs_def
	move.w	#1<<sqs..def,d7 	 ; mark all names
					 ; and get next token
sqs_ntok
	moveq	#$4f,d1
	and.b	(a4),d1 		 ; masked byte token
	move.w	(a4)+,d0		 ; word token
sqs_jmp
	add.b	d1,d1			 ; byte doubled
	bvs.s	sqs_float		 ; floating point
	move.w	sqs_tab(pc,d1.w),d1
	jmp	sqs_tab(pc,d1.w)

sqs_tab
	dc.w	sqs_ntok-sqs_tab	 ; spaces
	dc.w	sqs_ntok-sqs_tab	 ; keys
	dc.w	sqs_ntok-sqs_tab
	dc.w	sqs_ntok-sqs_tab

	dc.w	sqs_sym-sqs_tab 	 ; symbols
	dc.w	sqs_ntok-sqs_tab	 ; ops
	dc.w	sqs_ntok-sqs_tab	 ; monops
	dc.w	sqs_ntok-sqs_tab

	dc.w	sqs_name-sqs_tab	 ; name
	dc.w	sqs_ntok-sqs_tab
	dc.w	sqs_ntok-sqs_tab
	dc.w	sqs_strg-sqs_tab	 ; string

	dc.w	sqs_text-sqs_tab	 ; text
	dc.w	sqs_ntok-sqs_tab	 ; line number
	dc.w	sqs_ntok-sqs_tab	 ; separator
	dc.w	sqs_ntok-sqs_tab

sqs_float
	addq.l	#4,a4
	bra	sqs_ntok

sqs_sym
	cmp.w	#tkw.eol,d0		 ; end of line?
	beq	sqs_lloop		 ; ... yes
	cmp.w	#tkw.coln,d0		 ; end of statement?
	bne.s	sqs_ntok		 ; ... no
	bra	sqs_stloop

sqs_name
	move.w	d7,d0			 ; flag to set
sqs_nflag
	move.w	(a4)+,d1		 ; name number
	lsl.w	#nt.ishft,d1		 ; shifted
	or.w	(a3,d1.w),d0		 ; usage / type combined with flags
	or.w	d0,(a2,d1.w)		 ; marked in table
	bra	sqs_ntok

sqs_strg
sqs_text
sqs_ctxt
	move.w	(a4)+,d1		 ; length
	moveq	#1,d0
	and.w	d1,d0
	add.w	d0,d1
	add.w	d1,a4
	bra	sqs_ntok

; now we write the table out, but first the header

sqs_wtable
	move.w	d5,-(sp)		 ; number of lines
	clr.l	-(sp)			 ; table size unknown
	moveq	#-$80,d0		 ; why -$80???
	add.l	sb_buffb+8(a6),d0
	sub.l	sb_buffb(a6),d0
	move.w	d0,-(sp)		 ; buffer size
	move.w	#'Q1',-(sp)
	moveq	#10,d2
	moveq	#-1,d3
	move.l	sp,a1
	moveq	#iob.smul,d0
	trap	#3
	add.w	#10,sp

	moveq	#0,d6			 ; count the entries
	moveq	#0,d7			 ; and the total size
	move.l	sb_nmlsb(a6),a5 	 ; name list base
	add.l	a6,a5
	sub.l	a2,a3			 ; offset between name tables
	move.l	a3,d4

	lea	-8(a2),a4		 ; base of table
sqs_ntloop
	addq.l	#8,a4			 ; ... next
	move.w	(a4),d0 		 ; name used?
	beq.s	sqs_ntloop		 ; ... no
	blt.s	sqs_nsbas		 ; end of table

	and.w	#$0e00,d0		 ; check for sbasic pf
	cmp.w	#$0400,d0
	beq.s	sqs_ntloop		 ; second pass for these
	bsr.l	sqs_wentry
	clr.w	(a4)			 ; written now
	bra.s	sqs_ntloop

sqs_nsbas
	lea	-8(a2),a4		 ; base of table
sqs_nsloop
	addq.l	#8,a4			 ; ... next
	move.w	(a4),d0 		 ; name used?
	beq.s	sqs_nsloop		 ; ... no
	blt.s	sqs_prog		 ; end of table
	bsr.l	sqs_wentry
	clr.w	(a4)			 ; written now
	bra.s	sqs_nsloop

; d6,d7 now have the magic numbers for the table
; before putting these into the file, we write all of the program, changing
; the name indices.

sqs_prog
	move.l	sb_srceb(a6),a4 	 ; starting here
	add.l	a6,a4
	bra.s	sqp_eloop

sqp_lloop
	move.l	(sp),a1 		 ; buffer
	move.w	(a4)+,(a1)+		 ; link

sqp_ntok
	moveq	#$4f,d1
	and.b	(a4),d1 		 ; masked byte token

	move.w	(a4)+,d0
	move.w	d0,(a1)+		 ; copy token

	add.b	d1,d1			 ; byte doubled
	bvs.s	sqp_float		 ; floating point
	move.w	sqp_tab(pc,d1.w),d1
	jmp	sqp_tab(pc,d1.w)

sqp_tab
	dc.w	sqp_ntok-sqp_tab	 ; spaces
	dc.w	sqp_ntok-sqp_tab	 ; keys
	dc.w	sqp_ntok-sqp_tab
	dc.w	sqp_ntok-sqp_tab

	dc.w	sqp_sym-sqp_tab 	 ; symbols
	dc.w	sqp_ntok-sqp_tab	 ; ops
	dc.w	sqp_ntok-sqp_tab	 ; monops
	dc.w	sqp_ntok-sqp_tab

	dc.w	sqp_name-sqp_tab	 ; name
	dc.w	sqp_ntok-sqp_tab
	dc.w	sqp_ntok-sqp_tab
	dc.w	sqp_strg-sqp_tab	 ; string

	dc.w	sqp_text-sqp_tab	 ; text
	dc.w	sqp_lno-sqp_tab 	 ; line number
	dc.w	sqp_ntok-sqp_tab	 ; separator
	dc.w	sqp_ntok-sqp_tab

sqp_float
	move.l	(a4)+,(a1)+		 ; copy the rest
	bra	sqp_ntok

sqp_name
	move.w	(a4)+,d1		 ; name number
	lsl.w	#nt.ishft,d1		 ; index
	move.w	2(a2,d1.w),(a1)+	 ; and translated
	bra	sqp_ntok

sqp_strg
sqp_text
sqp_ctxt
	move.w	(a4)+,d1		 ; length
	move.w	d1,(a1)+
	beq	sqp_ntok
sqp_txlp
	move.w	(a4)+,(a1)+
	subq.w	#2,d1
	bgt.s	sqp_txlp

	bra	sqp_ntok

sqp_lno
	move.w	(a4)+,(a1)+
	bra	sqp_ntok

sqp_sym
	cmp.w	#tkw.eol,d0		 ; end of line?
	bne.s	sqp_ntok		 ; ... no

	move.l	a1,d2			 ; end of line
	move.l	(sp),a1
	sub.l	a1,d2
	moveq	#iob.smul,d0
	trap	#3
	tst.l	d0
	bne.s	sqs_rchp8
sqp_eloop
	dbra	d5,sqp_lloop

	moveq	#4,d1
	moveq	#iof.posa,d0		 ; position to start
	trap	#3
	move.w	d7,-(sp)
	move.w	d6,-(sp)
	moveq	#4,d2			 ; write 4 bytes table size
	move.l	sp,a1
	moveq	#iob.smul,d0
	trap	#3
	addq.l	#4,sp			 ; clean up stack

sqs_rchp8
	move.l	(sp)+,a0		 ; release buffer
	jsr	gu_rchp

sqs_rchp4
	move.l	(sp)+,a0		 ; release table
	jsr	gu_rchp

sqs_exit0
	move.l	(sp)+,a0
	rts

sqs_wentry
	move.l	(a6),a1 		 ; fill the buffer
	add.l	a6,a1
	move.l	a1,d2
	move.w	(a4),(a1)+		 ; type
	move.w	d6,2(a4)		 ; new number
	addq.w	#1,d6

	move.l	a5,a3			 ; name list
	add.w	nt_name(a4,d4.l),a3	 ; name pointer
	moveq	#0,d1
	move.b	(a3)+,d1		 ; length of name
	move.l	d1,(a1)+		 ; long word !!

	moveq	#1,d0
	and.w	d1,d0
	bra.s	sqs_wnle
sqs_wnloop
	move.b	(a3)+,(a1)+
sqs_wnle
	dbra	d1,sqs_wnloop

	exg	a1,d2
	sub.l	a1,d2			 ; length
	add.w	d0,d2			 ; made even
	add.w	d2,d7			 ; total size

	moveq	#iob.smul,d0
	trap	#3
	rts

	end
