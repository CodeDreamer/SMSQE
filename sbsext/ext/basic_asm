* BASIC execution    V0.02     1985	Tony Tebby   QJUMP
*
* 2005-11-01  0.02  added call to re-set home directory on load (wl)
* 2005-11-17  0.03  if A1 absoute, use Supervisor mode, only set home dir if basic name set (mk)

	section exten
*
*	CLEAR			clear variables, returns, when error and heap
*	NEW			clear everything
*	STOP			stop execution
*	RUN [line number]	start execution
*	DO filename		execute commands from file
*      qLRUN filename		load file and RUN
*      qLOAD filename		load file and get command
*      qMRUN filename		merge file and continue
*      qMERGE filename		merge file and get command or continue
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
	xdef	qlrun
	xdef	qload
	xdef	qmrun
	xdef	qmerge
	xdef	continue
	xdef	retry
*
	xdef	list
	xdef	save
	xdef	qsave
	xdef	save_o
	xdef	qsave_o
*
	xref	sb_qsave

	xref	clchp
	xref	ut_gxin1		; get exactly one integer
	xref	ut_gtin1		; get one integer
	xref	ut_fopen		; open file using parameter
	xref	ut_opdefx		; open file using name
	xref	ut_fclos		; close file
	xref	ut_chan 		; get default channel d6
	xref	ut_ckprc		; check not in procedure
	xref	ut_msovq

	xref	ut_getext
	xref	uxt_bopt
	xref	uxt_breq
	xref	uxt_sreq
	xref	uxt.bas
	xref	uxt.sav

	xref	gu_shomeover		; overwrite home dir
*
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_io'
	include 'dev8_sbsext_ut_opdefx_keys'
*
*****
retry
	subq.b	#1,sb_cstmt(a6) 	; backspace continue statement
	bge.s	continue		; ... ok
	clr.b	sb_cstmt(a6)		; ... oops multiple retries
*****
continue
	moveq	#sb.cont,d5		; set stop action
	cmp.l	a3,a5			; any parameters?
	beq.s	stop_cm1		; ... no, done
	bsr.l	ut_gxin1		; get exactly one integer
	bne.s	bas_rts1		; ... oops
	move.w	(a6,a1.l),sb_cline(a6)	; set continue line
	clr.b	sb_cstmt(a6)		; ... statement
stop_cm1
	bra.l	stop_cmd		; and stop (ish)
*****
qmerge
	moveq	#1,d7			; _sav required
*****
merge
	moveq	#sb.merge,d5		; merge
	moveq	#-1,d6			; set command line number
	tst.b	sb_cmdl(a6)		; is it a command line?
	bne.s	merge_do		; ... yes, real merge
	bra.s	mrun			; ... no, mrun
*****
qmrun
	moveq	#1,d7			; _sav required
*****
mrun
	moveq	#sb.mrun,d5		; mrun
	moveq	#0,d6			; set start of program
merge_do
	bsr.l	ut_ckprc		; check not in procedure
	bne.s	bas_rts1		; ... oops
	tst.b	sb_cmdl(a6)		; is it a single line?
	bne.s	load_do 		; ... yes, do load
	move.w	sb_line(a6),d6		; ... no, set line number to continue from
	bra.s	load_merge
*****
do
	moveq	#sb.stop,d5		; set stop action
	moveq	#-1,d6			; set command line number
	bra.s	load_merge		; and continue as load and merge
*****
qload
	moveq	#1,d7			; _sav required
*****
load
	moveq	#sb.load,d5		; set stop action
	moveq	#-1,d6			; set command line number
	bra.s	load_set
*****
qlrun
	moveq	#1,d7			; _sav required
*****
lrun
	moveq	#sb.lrun,d5		; set stop action
	moveq	#0,d6			; set command line number
load_set
	bset	#31,d5			; flag save name
	clr.l	sb_fnbas(a6)
load_do
	move.l	a3,-(sp)		; save parm pointer
	bsr.l	clchp			; clear the common heap
	move.l	(sp)+,a3
load_merge
	move.l	sb_cmdch(a6),d0 	; is command channel already open?
	beq.s	lm_open 		; ... no
	move.l	d0,a0
	moveq	#ioa.clos,d0		; ... yes, close it
	trap	#2
lm_open
	lea	uxt_bopt,a2		; set optional extensions
	tst.l	d7
	beq.s	lm_opdo 		; ... none
	lea	uxt_sreq,a2		; ... compulsory _sav
lm_opdo
	moveq	#uod.extn+uod.prgd+uod.datd+uod.typ0,d2 ; retry options
	moveq	#ioa.kshr,d3
	bsr.l	ut_fopen		; open basic program file
bas_rts1
	bne	bas_rts 		; ... oops
	move.l	a0,sb_cmdch(a6) 	; set command channel

	tst.l	d5			; save name?
	bpl.s	clr_welin		; ... no
;;;
	move	sr,d3
	trap	#0			; enter supervisor mode for absolute a1
	add.l	a6,a1			; make a1 absolute
	moveq	#-1,d1			; for myself!
	jsr	gu_shomeover		; overwrite home directory
	sub.l	a6,a1
	move	d3,sr
;;;
	bsr.s	ls_setname

	lea	sb_fvers(a6),a2 	; set version
	clr.w	(a2)
	moveq	#iof.vers,d0
	moveq	#-1,d1
	trap	#3
	tst.l	d0
	bne.s	clr_welin
	move.w	d1,(a2)
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
	clr.l	sb_fnbas(a6)		; clear filename
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

ls_setname
	move.w	(a6,a1.l),d2		; length of name
	jsr	ut_getext		; get extension
	cmp.l	uxt.bas,d1		; was it _bas?
	seq	sb_fnbas(a6)		; ... yes, flag it
	beq.s	ls_nm4			; yes, set length 4 shorter
	cmp.l	uxt.sav,d1		; was it _sav?
	seq	sb_fnbas(a6)		; ... yes, flag it
	bne.s	ls_ncopy
ls_nm4
	subq.w	#4,d2
ls_ncopy
	lea	sb_fname(a6),a2 	; *** immoveable
	lea	2(a6,a1.l),a1
	move.w	d2,(a2)+
ls_ncloop
	move.w	(a1)+,(a2)+		; copy name without extension
	subq.w	#2,d2
	bgt.s	ls_ncloop
	rts
	page

save_inam
	moveq	#err.inam,d0
	rts
save_ipar
	moveq	#err.ipar,d0
	rts

*********************
qsave
	moveq	#ioa.knew,d3		; open new file
	bra.s	qsave_chk
*********************
qsave_o
	moveq	#ioa.kovr,d3		; open overwrite
qsave_chk
	lea	uxt_sreq,a2		; extension required
	move.l	a5,d0
	sub.l	a3,d0			; any parameters
	bne.s	qsave_open		;  ... yes
	moveq	#uod.extn,d2		;  ... no, save without def or prompt
	bsr.s	save_ofile
	beq.s	qsave_do
qsave_rts
	rts

qsave_open
	subq.l	#8,d0			; ... just one parameter?
	bne.s	save_ipar		;  ... no
	moveq	#uod.extn+uod.datd+uod.prmt,d2
	bsr.l	ut_fopen		; open file (with extension)
	bne.s	qsave_rts		; ... oops, output channel not OK
	clr.w	sb_fvers(a6)
	bsr.s	ls_setname

qsave_do
	pea	save_vers
	jmp	sb_qsave		; quick save

save_vers
	move.l	d0,-(sp)
	moveq	#iof.vers,d0		; set version
	moveq	#1,d1
	add.w	sb_fvers(a6),d1
	trap	#3
	move.l	(sp)+,d0		; ignore any error from version
	jmp	ut_fclos

*********************
list
	moveq	#2,d6			; default channnel 2
	jsr	ut_chan
	beq.s	list_save
	rts
*********************
save
	moveq	#ioa.knew,d3		; open new file
	bra.s	save_chk
*********************
save_o
	moveq	#ioa.kovr,d3		; open overwrite
save_chk
	cmp.l	a3,a5			; any parameters
	bne.s	save_open		;  ... yes
	moveq	#0,d2			;  ... no, save without def or prompt
	tst.b	sb_fnbas(a6)		; _bas to be added?
	beq.s	save_oname		;  ... no
	moveq	#uod.extn,d2		;  ... yes, save with extension
	lea	uxt_breq,a2

save_oname
	bsr.s	save_ofile
	beq.s	save_do
save_rts
	rts

save_ofile
	lea	sb_fname,a1		; file name
	tst.w	(a6,a1.l)		; any name?
	beq.s	save_inam
	movem.l d2/a1/a2,-(sp)
	jsr	ut_opdefx
	beq.s	save_rt12
	moveq	#err.fex,d1
	cmp.l	d1,d0			; exists?
	bne.s	save_rt12		; ... no
	jsr	ut_msovq		; overwrite?
	movem.l (sp)+,d2/a1/a2
	bne.s	save_rts		; ... no
	moveq	#ioa.kovr,d3
	jmp	ut_opdefx		; open again
save_rt12
	add.w	#12,sp
	rts

save_open
	moveq	#uod.datd+uod.prmt,d2	; save without extension
	bsr.l	ut_fopen		; open file
	bne.s	list_rts		; ... oops, output channel not OK
	clr.w	sb_fvers(a6)
	bsr	ls_setname
	addq.l	#8,a3			; and skip
save_do
	pea	save_vers		; set version and close
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
	move.l	a5,-(sp)		; save top pointer
	jsr	sb.expnd*3+qlv.off	; and list (from start)
	move.l	(sp)+,a5
	cmp.l	a3,a5
	bgt.s	list_loop
list_rts
	rts
	end
