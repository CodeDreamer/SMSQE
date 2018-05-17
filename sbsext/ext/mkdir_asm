; SuperBASIC Make Directory extension	 V2.00	  1989   Tony Tebby

	section exten

	xdef	make_dir
	xdef	fmake_dir

	xref	ut_gxnm1
	xref	ut_opdefx
	xref	ut_trap3
	xref	ut_fclos
	xref	ut_rtfd1

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_io'
	include 'dev8_sbsext_ut_opdefx_keys'

;+++
; Make directory procedure
;
;     MAKE_DIR filename
;
;---
make_dir
	moveq	#0,d7			 ; set procedure flag
	bra.s	mkd_do
;+++
; Make directory function
;
;     error = FMAKE_DIR (filename)
;
;---
fmake_dir
	moveq	#-1,d7			 ; set function flag
mkd_do
	jsr	ut_gxnm1		 ; get one name
	bne.s	mkd_exit		 ; ... oops
	moveq	#err.inam,d0		 ; assume bad name
	move.w	(a6,a1.l),d1		 ; length of name
	beq.s	mkd_err 		 ; oops
	lea	(a1,d1.w),a0
	cmp.b	#'_',1(a6,a0.l) 	 ; underscore at end?
	bne.s	mkd_open		 ; ... no
	subq.w	#1,(a6,a1.l)		 ; ... yes, name shorter
mkd_open
	moveq	#ioa.knew,d3		 ; new file
	moveq	#uod.datd,d2
	jsr	ut_opdefx		 ; open data default
	bne.s	mkd_err 		 ; ... oops
	moveq	#iof.mkdr,d0
	jsr	ut_trap3		 ; make directory
	jsr	ut_fclos		 ; close
mkd_err
	move.l	d0,d1			 ; error code
	tst.l	d7			 ; function?
	bne.l	ut_rtfd1		 ; ... yes, return error
mkd_exit
	rts
	end
