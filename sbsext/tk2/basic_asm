* BASIC execution    V1.00     1985	Tony Tebby   QJUMP
*
* 2017-03-08  1.00  Backported from SMSQ/E to QDOS			    MK

	section exten
*
*	CLEAR			clear variables, returns, when error and heap
*	NEW			clear everything
*	STOP			stop execution
*	RUN [line number]	start execution
*	DO filename		execute commands from file
*	LRUN filename		load file and RUN
*	LOAD filename		load file and get command
*	MRUN filename		merge file and continue
*	MERGE filename		merge file and get command or continue
*	CONTINUE [line number]	continue execution
*	RETRY [line number]	retry last statement
*
*	LIST [#channel,] ranges list BASIC program
*	SAVE [filename,] ranges   save BASIC program
*	SAVE_O [filename,] ranges save (overwrite) BASIC program
*
	xdef	clear
	xdef	new
	xdef	stop
	xdef	run
	xdef	do
	xdef	lrun
	xdef	load
	xdef	mrun
	xdef	merge
	xdef	continue
	xdef	retry
*
	xdef	save
	xdef	save_o
*

	xref	clchp
	xref	ut_gxin1		; get exactly one integer
	xref	ut_gtin1		; get one integer
	xref	ut_fopen		; open file using parameter
	xref	ut_opdefx		; open file using name
	xref	ut_fclos		; close file
	xref	ut_chan 		; get default channel d6
	xref	ut_ckprc		; check not in procedure

	xref	uxt_bopt

*
	include 'dev8_keys_bv'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_io'
	include 'dev8_sbsext_ut_opdefx_keys'
*
*****
retry
	subq.b	#1,bv_cnstm(a6) 	; backspace continue statement
	bge.s	continue		; ... ok
	clr.b	bv_cnstm(a6)		; ... oops multiple retries
*****
continue
	moveq	#sb.cont,d5		; set stop action
	cmp.l	a3,a5			; any parameters?
	beq.s	stop_cm1		; ... no, done
	bsr.l	ut_gxin1		; get exactly one integer
	bne.s	bas_rts1		; ... oops
	move.w	(a6,a1.l),sb_cline(a6)	; set continue line
	clr.b	bv_cnstm(a6)		; ... statement
	clr.w	bv_cnind(a6)		; ... in-line loop index
	clr.b	bv_cninl(a6)		; ... in-line loop flag
stop_cm1
	bra.s	stop_cmd		; and stop (ish)
*****
merge
	moveq	#sb.merge,d5		; merge
	moveq	#-1,d6			; set command line number
	tst.b	sb_cmdl(a6)		; is it a command line?
	bne.s	merge_do		; ... yes, real merge
*****
mrun
	moveq	#sb.mrun,d5		; mrun
	moveq	#0,d6			; set start of program
merge_do
	bsr.l	ut_ckprc		; check not in procedure
	bne.s	bas_rts1		; ... oops
	tst.b	sb_cmdl(a6)		; is it a single line?
	bne.s	load_do 		; ... yes, do load
	move.w	bv_linum(a6),d6 	; ... no, set line number to continue from
	bra.s	load_merge
*****
do
	moveq	#sb.stop,d5		; set stop action
	moveq	#-1,d6			; set command line number
	bra.s	load_merge		; and continue as load and merge
*****
load
	moveq	#sb.load,d5		; set stop action
	moveq	#-1,d6			; set command line number
	bra.s	load_do
*****
lrun
	moveq	#sb.lrun,d5		; set stop action
	moveq	#0,d6			; set command line number
load_do
	move.l	a3,-(sp)		; save parm pointer
	bsr.l	clchp			; clear the common heap
	move.l	(sp)+,a3
load_merge
	move.l	bv_comch(a6),d0 	; is command channel already open?
	beq.s	lm_open 		; ... no
	move.l	d0,a0
	moveq	#ioa.clos,d0		; ... yes, close it
	trap	#2
lm_open
	lea	uxt_bopt,a2		; set optional extensions
	moveq	#uod.extn+uod.prgd+uod.datd+uod.typ0,d2 ; retry options
	moveq	#ioa.kshr,d3
	bsr.l	ut_fopen		; open basic program file
bas_rts1
	bne.s	bas_rts 		; ... oops
	move.l	a0,sb_cmdch(a6) 	; set command channel
	bra.s	clr_welin

*****
run
	moveq	#sb.run,d5		; set stop action
	moveq	#0,d6			; set start of program
	cmp.l	a3,a5			; any parameters?
	beq.s	clr_welin		; ... no
	bsr.l	ut_gxin1		; get exactly one integer
	bne.s	bas_rts
	move.w	(a6,a1.l),d6		; set start line number
	clr.b	sb_stmt(a6)		; and statement number
	bra.s	clr_welin		; clear when error line
*****
stop
	moveq	#sb.stop,d5		; set stop action
stop_cmd
	moveq	#-1,d6			; set command line number
	bra.s	clr_when		; and clear when flag
*****
new
	moveq	#sb.new,d5		; set stop action
	bra.s	clear_cmd
*****
clear
	moveq	#sb.clear,d5		; set stop action
	move.w	sb_line(a6),d6		; assume continue
	tst.b	sb_cmdl(a6)		; is it single line?
	beq.s	clr_heap		; ... no
clear_cmd
	moveq	#-1,d6			; set command line number
*
clr_heap
	bsr.l	clchp			; clear common heap
clr_welin
	clr.w	sb_wherr(a6)		; clear when error line number
clr_when
	clr.b	sb_wheiu(a6)		; clear when error processing
set_nxln
	move.w	d6,sb_nline(a6) 	; set next line
	move.b	sb_stmt(a6),sb_nstmt(a6) ; and statement
	move.w	d5,sb_actn(a6)		; set stop action
	sf	sb_cont(a6)		; and stop
bas_ok
	moveq	#0,d0			; and no error
bas_rts
	rts

save_inam
	moveq	#err.inam,d0
	rts
save_ipar
	moveq	#err.ipar,d0
	rts

*********************
save
	moveq	#ioa.knew,d3		; open new file
	bra.s	save_chk
*********************
save_o
	moveq	#ioa.kovr,d3		; open overwrite
save_chk
save_open
	moveq	#uod.datd+uod.prmt,d2	; save without extension
	bsr.l	ut_fopen		; open file
	bne.s	list_rts		; ... oops, output channel not OK
	addq.l	#8,a3			; and skip
	bsr.s	list_save
	bra.l	ut_fclos

*
list_save
	st	sb_pline(a6)		; print line
	clr.w	sb_lsfil(a6)		; and clear list fill
list_loop
	moveq	#0,d4			; preset start
	move.w	#$7fff,d6		; and end of range
*
	cmp.l	a3,a5			; is there a parameter at all?
	beq.s	list_do 		;  ... no
	move.w	(a6,a3.l),d5		; get variable type and usage
	cmp.w	#$0050,d5		; is it null TO
	beq.s	list_top		; ... yes, just get the top end of range
	bsr.l	ut_gtin1		; ... no, get one integer
	bne.s	list_rts
	move.w	(a6,a1.l),d4		; and set low end of range
*
	lsr.b	#4,d5			; get old separator
	subq.b	#5,d5			; was it TO?
	beq.s	list_top		; ... yes, get top end of range
	move.w	d4,d6			; ... no, list single line
	bra.s	list_next
list_top
	addq.l	#8,a3			; next parameter is top of range
	cmp.w	a3,a5			; is there one?
	beq.s	list_do 		;  ... no
	move.w	#$ff0f,d5		; check for null
	and.w	(a6,a3.l),d5
	beq.s	list_next		; ... it is
	bsr.l	ut_gtin1		; ... no, get one integer
	bne.s	list_rts
	move.w	(a6,a1.l),d6		; set top end
list_next
	addq.l	#8,a3
*
list_do
	move.w	sb.expnd,a2
	move.l	a5,-(sp)		; save top pointer
	jsr	qlv_jump(a2)
	move.l	(sp)+,a5
	cmp.l	a3,a5
	bgt.s	list_loop
list_rts
	rts

	end
