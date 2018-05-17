; SBAS_EXECUTE - execution control	   1994   Tony Tebby

	section sbas

	xdef	sb_execute

	xref	sb_compile
	xref	sb_inter
	xref	sb_chan0
	xref	gu_fclos

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

;+++
; SuperBASIC execution control for program
;
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_execute
	jsr	sb_compile		 ; compile
	bne.s	sbx_rts 		 ; program error

	tst.b	sb_clc0(a6)		 ; close 0 on run?
	beq.s	sbx_run 		 ; ... no
	jsr	sb_chan0		 ; find channel 0
	clr.b	sb_clc0(a6)
	jsr	gu_fclos		 ; close it

	moveq	#-1,d0
	move.l	sb_chanb(a6),a0
	move.l	d0,ch_chid(a6,a0.l)	 ; mark closed

sbx_run
	jsr	sb_inter		 ; interpret program
	beq.s	sbx_rts 		 ; OK

	sf	sb_cmdst(a6)		 ; give up command line
	move.l	d0,sb_erno(a6)
	move.w	sb_line(a6),d6
	move.w	d6,d1
	swap	d6
	clr.w	d6
	move.b	sb_stmt(a6),d6		 ; stopped here

	tst.b	sb_wheiu(a6)		 ; when error in use?
	bne.s	sbx_rtd0		 ; ... yes, give up

	move.w	d1,sb_cline(a6) 	 ; set continue statement
	move.b	d6,sb_cstmt(a6)
	move.w	d1,sb_eline(a6) 	 ; set error statement
	move.b	d6,sb_estmt(a6)

	tst.b	sb_break(a6)		 ; break?
	bpl.s	sbx_rtd0		 ; ... yes, give up

	move.w	sb_wherr(a6),sb_nline(a6) ; when?
	ble.s	sbx_rtd0		  ; ... no

	move.b	#1,sb_nstmt(a6)
	st	sb_wheiu(a6)		 ; when now in use
	bra	sb_execute		 ; ... use it

sbx_rtd0
	tst.l	d0
sbx_rts
	rts
	end
