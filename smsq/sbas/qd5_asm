; SMSQ_SBAS_QD5      QD5 Interface  v1.01
;
; 2006-01-10  1.01  Sets file name from QD as home file name (MK)

	section sbas

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_k'
	include 'dev8_keys_qdthg'
	include 'dev8_smsq_sbas_parser_keys'
	include 'dev8_mac_assert'

	xdef	sb_qd5
	xdef	sb_qdthing

	xref	sb_name
	xref	gu_thexn
	xref	gu_shome

	xref	sb_palin
	xref	sb_ledit
	xref	sb_execute
	xref	sb_clrdat
	xref	sb_die
	xref	sb_fatal
	xref.l	sb.vers
	xref	gu_thini
	xref	ca_idec
	xref	mu_rperr

qdr_line equ	0	; QD's line number
qdr_clmn equ	2
qdr_sbln equ	4	; SBASIC's line number
qdr_stmt equ	6
qdr_errtx equ	8

stk.lcnt equ	0
stk.sba6 equ	2
stk.qda4 equ	6
stk.qda5 equ	10
stk.qda6 equ	14


;+++
; Initialise Thing
;---
sb_qdthing
	lea	sbqd_thtab,a1
	jmp	gu_thini

sbqd_thtab
	dc.l	th_name+10		 ; size of linkage
	dc.l	sq_thing-*		 ; thing
	dc.l	sb.vers 		 ; version
sbqd_name
	dc.w	7,'SBAS/QD'


sq_thing
	dc.l	thh.flag	    ; flag
	dc.l	0
	dc.l	'QD5T'
	dc.l	0
	dc.l	0
	dc.l	0
	dc.l	sb_init-sq_thing
	dc.l	sb_tidy-sq_thing
	dc.l	sb_f10-sq_thing
	dc.l	sb_nimp-sq_thing
	dc.l	sb_nimp-sq_thing
	dc.l	sb_nimp-sq_thing
	dc.l	sb_nimp-sq_thing
	dc.l	sb_nimp-sq_thing

sb_init
	moveq	#0,d0
	moveq	#0,d1
	moveq	#0,d2
	move.l	d0,a3
	rts

sb_nimp
	moveq	#err.nimp,d0
sb_tidy
sb_rts
	rts

sb_f10
	clr.l	(a4)
	clr.l	4(a4)		 ; init return parameters

	clr.w	-(sp)		 ; no string
	movem.l a4/a5/a6,-(sp)
	move.l	#'QD5S',-(sp)
	move.l	#$00140004,-(sp)
	move.l	sp,a1		 ; parameter
	lea	sb_name,a0
	lea	sbqd_name,a2
	moveq	#-1,d1
	move.l	#$0008ffff,d2
	jsr	gu_thexn

	addq.l	#8,sp
	movem.l (sp)+,a4/a5/a6	 ; clean up
	addq.w	#2,sp

	move.l	d0,d7		; keep error code
	beq	sb_okay

	move.w	qdr_line(a4),d1 ; line nr. given?
	ble	sb_retokay

	moveq	#qd_info,d0
	bsr	call_qd5
	move.w	qv_color(a1),d6 ; fetch colour in case we need it
	move.b	(a6,d6.w),d6	; the colour!
	move.w	qv_1stvl(a1),d2
	move.w	d1,(a6,d2.w)	; start listing from here
	movem.l d6-d7/a0-a4,-(sp)
	moveq	#qd_prttxt,d0
	bsr	call_qd5	; print text
	movem.l (sp)+,d6-d7/a0-a4
	move.w	qv_cscry(a1),d2
	clr.w	(a6,d2.w)
	move.w	qv_cscrx(a1),d2
	moveq	#-2,d1
	add.w	qdr_clmn(a4),d1
	bge.s	sb_errpos_ok
	moveq	#0,d1
sb_errpos_ok
	move.w	d1,(a6,d2.w)	; column
	moveq	#qd_curpos,d0
	bsr	call_qd5	; position the cursor

	lea	qdr_errtx+2(a4),a0 ; buffer for error text construction
	move.w	#err4.atln,a1	; we want 'At line'
	moveq	#sms.mptr,d0
	trap	#do.sms2
	move.w	(a1)+,d0
	subq.w	#1,d0
sb_cpyatln
	move.b	(a1)+,(a0)+	; copy into buffer
	dbra	d0,sb_cpyatln
	moveq	#0,d1
	move.w	qdr_sbln(a4),d1 ; offending line number
	jsr	ca_idec 	; put line number into buffer
	move.w	qdr_stmt(a4),d1 ; statement number given?
	beq.s	sb_nostmt	; no
	move.b	#':',(a0)+	; add colon
	jsr	ca_idec 	; add statement number to text buffer
sb_nostmt
	move.b	#' ',(a0)+	; follow it by a space
	move.l	d7,a1		; the SBASIC error to report
	moveq	#sms.mptr,d0
	trap	#do.sms2	; convert SBASIC error
	move.w	(a1)+,d0	; message length
	subq.w	#1,d0
sb_cpyerr
	move.b	(a1)+,(a0)+	; copy into buffer
	dbra	d0,sb_cpyerr
	lea	qdr_errtx+2(a4),a1
	sub.l	a1,a0
	move.w	a0,-(a1)	; insert text length
	move.l	a1,d0
	bset	#31,d0		; turn into error

	move.l	#$10000028,d1	; position here
	moveq	#0,d2		; (bug in menu)
	move.b	d6,d2		; colour
	jsr	mu_rperr	; report the error

sb_retokay
	moveq	#0,d0
sb_okay
	rts


; called with QD5 Interface bits (QD5S, a4,a5,a6) on stack

sb_qd5
	move.l	a6,(sp) 	; save SBASIC's A6
	clr.w	-(sp)		; initialise line count
	movem.l stk.qda4(sp),a4-a6

	moveq	#qd_info,d0
	bsr.s	call_qd5	; get table in a1
	move.w	qv_filnm(a1),d0 ; offset of file name in QD variables
	lea	(a6,d0.w),a1	; file name
	tst.w	(a1)
	beq.s	sb_firstline

	moveq	#-1,d1
	jsr	gu_shome	; set file name for home thing

sb_firstline
	moveq	#qd_1stln,d0
	bsr.s	call_qd5	; get ptr to first line

sb_nextline
	movem.l stk.qda5(sp),a5-a6
	moveq	#qd_nxtln,d0
	bsr.s	call_qd5	; get next line
	move.l	stk.sba6(sp),a6
	bne.s	sb_exec

	addq.w	#1,(sp) 	; next line
	move.l	sb_buffb(a6),a0
	add.l	a6,a0		; copy to here
	lea	$8(a2),a1
	move.w	(a1)+,d0	; line length
	beq.s	sb_add_nl	; empty, so just add NEWLINE character
	subq.w	#1,d0		; prepare for DBRA
sb_cpy_loop
	move.b	(a1)+,(a0)+
	dbra	d0,sb_cpy_loop	; copy into SBASIC's input buffer
sb_add_nl
	move.b	#k.nl,(a0)	; add a NEWLINE

	addq.w	#1,sb_line(a6)	; another line
	move.l	a2,-(sp)
	jsr	sb_palin	; parse it!
	move.l	(sp)+,a2
	bne	pars_error

	moveq	#6,d0
	add.l	sb_cmdlb(a6),d0 ; is it blank?
	cmp.l	sb_cmdlp(a6),d0
	bne.s	sb_putln
	move.l	#tkw.coln<<16+tkw.eol,-2(a6,d0.l) ; add colon to end of line
	addq.l	#2,sb_cmdlp(a6)
sb_putln
	jsr	sb_ledit	; add to program
	nop
	bra.s	sb_nextline

call_qd5
	move.l	a5,a0
	add.l	(a5,d0.w),a0
	jmp	(a0)


sb_exec
	sf	sb_wheiu(a6)		 ; when error not in use
	jsr	sb_execute	; interpret the program
	blt.s	exec_error
	tst.b	sb_cont(a6)	; continue?
	bne.s	sb_done 	; ... yes, but how can we???

	move.w	sb_actn(a6),d0		 ; action required
	beq.s	sb_clear		 ; clear

	sub.w	#sb.cont,d0		 ; continue?
	blt.s	sb_done 		 ; no, nor no action
	bgt.s	sb_nact 		 ; no action

	move.w	sb_cline(a6),d0 		 ; continue line
	ble.s	sb_done 			 ; ... not set
	move.w	d0,sb_nline(a6)
	move.b	sb_cstmt(a6),sb_nstmt(a6)
	clr.w	sb_cline(a6)			 ; clear continue line
	sf	sb_wheiu(a6)			 ; when error not in use
	bra	sb_exec 			 ; and continue

sb_clear
	jsr	sb_clrdat			 ; clear data and return stacks
	clr.l	sb_dline(a6)			 ; and data pointer
	assert	0,sb.edt-$ff,sb.edtn-$80
	tas	sb_edt(a6)			 ; edited! to redo name types

sb_nact
	move.w	sb_line(a6),sb_nline(a6)	 ; current line
	move.b	sb_stmt(a6),sb_nstmt(a6)	 ; and statement
	bra	sb_exec 			 ; ... recompile and continue


sb_done
	bra.l	sb_die


exec_error
	move.l	stk.qda4(sp),a5
	move.l	d6,qdr_sbln(a5) 	 ; error line
	swap	d6

	move.l	sb_srceb(a6),a4 	 ; base of program
	moveq	#2,d1
	moveq	#1,d2			 ; line number
	bra.s	xerror_check
xerror_lline
	addq.w	#1,d2
	add.w	(a6,a4.l),d1		 ; new line length
	add.w	d1,a4			 ; next line
xerror_check
	cmp.w	4(a6,a4.l),d6		 ; the right line?
	bgt.s	xerror_lline

	move.w	d2,qdr_line(a5) 	 ; set qd line number

	jmp	sb_fatal

pars_error
	move.l	stk.qda4(sp),a4
	move.w	stk.lcnt(sp),(a4)+ ; line to list from
	move.l	sb_pcerp(a6),d1
	sub.l	sb_buffb(a6),d1
	addq.l	#1,d1
	move.w	d1,(a4)+	  ; offending character
	move.w	sb_line(a6),(a4)+ ; SBASIC's line number
	jmp	sb_fatal

	end
