; SMSQ_SBAS_QLOAD   QLOAD (Merge) V2.00

	section sbas

	xdef	sb_qload

	xref	gu_achp0
	xref	gu_rchp
	xref	sb_lnam2
	xref	sb_gnam2
	xref	sb_anam2
	xref	sb_ledit
	xref	sb_resbf
	xref	sb_rescl
	xref	sb_ressr

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_parser_keys'

;+++
; SBASIC QLOAD
;
;	a0 cp	channel ID
;
;	Status return 0 or -ve
;---
sb_qload
	move.l	a0,-(sp)
	move.l	sb_buffb(a6),a1 	 ; read header
	move.l	a1,a3
	moveq	#$40,d2
	moveq	#-1,d3
	moveq	#iof.rhdr,d0
	trap	#4
	trap	#3
	tst.l	d0
	bne.l	sql_exit0
	move.l	a0,a1
	move.l	(a6,a3.l),d2		 ; size of file
	move.l	d2,d0
	jsr	gu_achp0
	bne.l	sql_exit0
	move.l	a0,a2
	move.l	a0,-(sp)		 ; save base
	exg	a0,a1
	moveq	#iof.load,d0
	trap	#3
	tst.l	d0
	bne.l	sql_rchp4
	move.l	(a2)+,d1		 ; size of buffer
	ext.l	d1
	moveq	#-$80,d0
	sub.l	d0,d1			 ; real size
	add.l	sb_buffb(a6),d1
	sub.l	sb_buffb+8(a6),d1	 ; extra space required
	ble.s	sql_ntab		 ; ... none
	move.l	sb_buffb+8(a6),sb_buffp(a6) ; pretend it is full
	jsr	sb_resbf

sql_ntab
	moveq	#0,d0
	move.w	(a2)+,d0		 ; temporary name table
	move.w	d0,d6			 ; number of entries to process
	add.w	d0,d0			 ; size
	beq.l	sql_rchp4		 ; ... none, we've done
	jsr	gu_achp0
	bne.l	sql_rchp4
	move.l	a0,-(sp)		 ; save base of table (why?)

	move.w	(a2)+,a5		 ; length of names
	move.w	(a2)+,d7		 ; number of lines
	beq.l	sql_rchp8		 ; ... none
	move.l	(a6,a3.l),d1		 ; file length
	sub.l	a5,d1			 ; approximate length of program
	jsr	sb_ressr		 ; reserve it

	add.l	a2,a5			 ; pointer to program
	move.l	a2,a1
	bra.s	sql_nlpe
sql_nloop
	moveq	#$f,d4			 ; just the usage
	and.b	(a1)+,d4
	move.b	(a1)+,d5		 ; type
	move.l	(a1)+,d2		 ; length of name
	sub.l	a6,a1			 ; make rel a6
	jsr	sb_lnam2		 ; already defined?
	beq.s	sql_nset		 ; ... yes
	cmp.b	#nt.mcprc,d4		 ; mc proc or fun?
	blt.s	sql_aname		 ; ... no, add the name
	jsr	sb_gnam2		 ; get global name
	beq.s	sql_nset
sql_aname
	jsr	sb_anam2		 ; add name to table
	sub.w	d2,a1			 ; and backspace
sql_nset
	move.l	a3,d0
	sub.l	sb_nmtbb(a6),d0
	lsr.l	#nt.ishft,d0
	move.w	d0,(a0)+		 ; name table for this one

	add.l	a6,a1
	moveq	#1,d0
	and.w	d2,d0
	add.w	d0,d2
	add.w	d2,a1			 ; next nt entry
sql_nlpe
	dbra	d6,sql_nloop

; the name table is now set up, all we have to do is copy the parsed lines
; into the command line, changing the name table entries as we go.

; first make sure we have room

	moveq	#0,d0			 ; line length
	moveq	#0,d1			 ; longest line
	move.w	d7,d2
	move.l	a5,a1
	bra.s	sql_lllend
sql_llloop
	add.w	(a1)+,d0
	add.w	d0,a1
	cmp.w	d1,d0
	ble.s	sql_lllend
	move.w	d0,d1
sql_lllend
	dbra	d2,sql_llloop

	move.l	sb_cmdlb(a6),sb_cmdlp(a6) ; clear line
	jsr	sb_rescl		 ; and reserve the space we need

sql_lloop
	move.l	(sp),a0 		 ; table
	addq.l	#2,a5			 ; skip link

	move.l	sb_cmdlb(a6),a1

sql_ntok
	moveq	#$4f,d1
	and.b	(a5),d1 		 ; masked byte token

	move.w	(a5)+,d0
	move.w	d0,(a6,a1.l)
	addq.l	#2,a1			 ; copy token
	add.b	d1,d1			 ; byte doubled
	bvs.s	sql_float		 ; floating point
	move.w	sql_tab(pc,d1.w),d1
	jmp	sql_tab(pc,d1.w)

sql_tab
	dc.w	sql_ntok-sql_tab	 ; spaces
	dc.w	sql_ntok-sql_tab	 ; keys
	dc.w	sql_ntok-sql_tab
	dc.w	sql_ntok-sql_tab

	dc.w	sql_sym-sql_tab 	 ; symbols
	dc.w	sql_ntok-sql_tab	 ; ops
	dc.w	sql_ntok-sql_tab	 ; monops
	dc.w	sql_ntok-sql_tab

	dc.w	sql_name-sql_tab	 ; name
	dc.w	sql_ntok-sql_tab
	dc.w	sql_ntok-sql_tab
	dc.w	sql_strg-sql_tab	 ; string

	dc.w	sql_text-sql_tab	 ; text
	dc.w	sql_lno-sql_tab 	 ; line number
	dc.w	sql_ntok-sql_tab	 ; separator
	dc.w	sql_ntok-sql_tab

sql_float
	move.l	(a5)+,(a6,a1.l)
	addq.l	#4,a1			 ; copy the rest
	bra	sql_ntok

sql_name
	move.w	(a5)+,d1		 ; name number
	add.w	d1,d1			 ; doubled
	move.w	(a0,d1.w),(a6,a1.l)	 ; and translated
	addq.l	#2,a1
	bra	sql_ntok

sql_strg
sql_text
sql_ctxt
	move.w	(a5)+,d1		 ; length
	move.w	d1,(a6,a1.l)
	addq.l	#2,a1
	beq	sql_ntok
sql_txlp
	move.w	(a5)+,(a6,a1.l)
	addq.l	#2,a1
	subq.w	#2,d1
	bgt.s	sql_txlp

	bra	sql_ntok

sql_lno
	move.w	(a5)+,(a6,a1.l)
	addq.w	#2,a1			 ; copy the line number
	bra	sql_ntok

sql_sym
	cmp.w	#tkw.eol,d0		 ; end of line?
	bne.s	sql_ntok		 ; ... no

	move.l	a1,sb_cmdlp(a6) 	 ; set command line pointer
	jsr	sb_ledit
	nop
	subq.w	#1,d7
	bgt	sql_lloop

	moveq	#0,d0

sql_rchp8
	move.l	(sp)+,a0		 ; return table
	jsr	gu_rchp
sql_rchp4
	move.l	(sp)+,a0		 ; and file image
	jsr	gu_rchp

sql_exit0
	move.l	(sp)+,a0
	rts
	end
