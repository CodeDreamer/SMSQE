; SBAS_MAIN - Main SuperBASIC control routine v1.04
;
; 2005-11-15  1.01  Sets the boot-filename as the current file name (MK)
; 2005-12-08  1.02  Also registers boot in HOME directory if possible (MK)
;		    New command line history (up/down arrows) (MK)
; 2017-04-05  1.03  Fixed LRESPR-within-PROCedure crash (MK)
; 2020-05-05  1.04  Fixed "buffer full" error in command line history (wl)

	section sbas

	xdef	sb_main
	xdef	sb_start
	xdef	sb_xcmd

	xdef	sb_error
	xdef	sb_insmem
	xdef	sb_panic
	xdef	sb_wermess
	xdef	sb_chan0
	xdef	gu_shome

	xref	sb_vers
	xref	sb_qload
	xref	sb_palin
	xref	sb_paerr
	xref	sb_ledit
	xref	sb_lista
	xref	sb_compile
	xref	sb_inter
	xref	sb_execute
	xref	sb_istop

	xref	sb_resbf

	xref	sb_clrstk
	xref	sb_clrcmp
	xref	sb_clrdat
	xref	sb_clrall
	xref	sb_clrfrp

	xref	sb_ret2pos

	xref	sb_inchan

	xref	sb_litem
	xref	sb_die
	xref	sb_fatal

	xref	gu_smul
	xref	gu_sstrg
	xref	gu_sbyte
	xref	gu_clra
	xref	gu_fopen
	xref	gu_fclos
	xref	gu_achp0
	xref	gu_iow

	xref	uq_opcon

	xref	sb_ermess

	xref	gu_thjmp

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_keys_k'
	include 'dev8_keys_thg'
	include 'dev8_keys_con'
	include 'dev8_mac_assert'

sb_bf	dc.w	9,'flp0_boot'
sb_bw	dc.w	9,'win0_boot'

;+++
; SBASIC main control routine - set up initial command channel
;---
sb_main
;*******
;	 move.l  d7,-(sp)		  ; save config flag
;*******
;	 move.l  #16384,d0
;	 move.l  d0,d2
;	 jsr	 gu_achp0
;	 move.l  a0,a1
;	 move.l  a0,a2
;	 lea	 qmon,a0
;	 moveq	 #ioa.kshr,d3
;	 jsr	 gu_fopen
;	 bne.s	 sbm_oboot		  ; no qmon!
;	 moveq	 #iof.load,d0
;	 jsr	 gu_iow 		  ; load
;	 jsr	 gu_fclos		  ; close
;	 bne.s	 sbm_oboot
;	 jsr	 (a2)			  ; initialise it
;*******
;	 lea	 qmon_n+1,a1
;	 jsr	 sb_litem		  ; find QMON
;	 bmi.s	 sbm_oboot
;	 sub.l	 a3,a3
;	 move.l  a3,a5			  ; no parameters
;	 jsr	 (a1)			  ; call it
;	 bra.s	 sbm_oboot
;
;qmon	 dc.w	 14,'flp1_rext_qmon'
;qmon_n  dc.w	 4,'QMON'
;*******
;	 lea	 sbm_test,a1
;	 move.w  (a1)+,d2
;	 sub.l	 a0,a0
;	 moveq	 #-1,d3
;	 moveq	 #iob.smul,d0
;	 trap	 #do.io
;	 moveq	 #50,d3
;	 trap	 #do.io
;	 bra.s	 sbm_oboot
;sbm_test dc.w	 14,'TEST VERSION',$0a0a
;
;*******
;sbm_oboot
;	 move.l  (sp)+,d7
;*******
	tst.b	d7			 ; check the config byte
	beq.s	sbm_owin		 ; ... not flp
	lea	sb_bf+12,a1
	bsr.s	sbm_obfl
	beq.s	sbm_bcmd
sbm_owin
	swap	d7
	lea	sb_bw+12,a1
	bsr.s	sbm_obfl
	bne.l	sbm_rcmd		 ; ... not open

sbm_bcmd
	move.l	a0,sb_cmdch(a6) 	 ; ... opened, set command channel
	bra.l	sbm_start

sbm_obfl
	move.l	-(a1),-(sp)
	move.l	-(a1),-(sp)
	move.l	-(a1),-(sp)
	move.l	sp,a0
	add.b	d7,5(a0)		 ; ... xxx1 or 2
	moveq	#ioa.kshr,d3
	jsr	gu_fopen
	bne.s	sbm_obfl_rts
	lea	sb_fname(a6),a1 	 ; copy filename to "current name"
	move.l	(sp),(a1)+
	move.l	4(sp),(a1)+
	move.l	8(sp),(a1)+
	sf	sb_fnbas(a6)
	lea	sb_fname(a6),a1 	 ; now use it for setting the home dir
	moveq	#-1,d1
	bsr.s	gu_shome
	moveq	#0,d0
sbm_obfl_rts
	add.w	#12,sp
	rts

homereg reg	a0-a4/d1-d3
d1stak	equ	0			; where D1 is on stack
d2stak	equ	4			; same for D2

;+++
; Set home directory for the given job in the home thing.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	A1	Pointer to file name	preserved
;---
gu_shome
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'SETH',d2		; extension in thing to use
	move.l	a1,a4			; save filename
	lea	home_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	sh_exit 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	move.l	d1stak(sp),d1		; get job ID back
	sub.l	#12,sp			; get some space
	move.l	sp,a1			; and point to it
	move.l	d1,(a1) 		; insert ID of job
	move.l	#$c1000000,4(a1)	; thp.call+thp.str
	move.l	a4,8(a1)		; set pointer to this string
	jsr	thh_code(a0)		; call extn thing
	add.l	#12,sp			; reset stack
	move.l	d0,-(sp)		; remember error
	lea	home_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	(sp)+,d0		; restore error
sh_exit
	movem.l (sp)+,homereg
	rts

home_name
	dc.w  4,'HOME'

; Channel handling

;+++
; Set channel for commands, if none, it dies
;
;	a0  r	channel ID
;
;---
sbm_comch
	move.l	sb_cmdch(a6),d0 	 ; command channel?
	bne.s	sbm_comset		 ; ... ok
	bsr.s	sbm_chan0		 ; try channel 0
	bne.l	sb_die			 ; ... no luck
	rts

sbm_comset
	move.l	d0,a0			 ; command channel
	rts

;+++
; Set channel 0
;
;	d4  r	pointer to channel table entry rel a6
;	a0  r	channel ID
;
;	Status return 0 or -ve (err.ichn)
;---
sb_chan0
sbm_chan0
	move.l	sb_chanb(a6),a0 	 ; pointer to table
	cmp.l	sb_chanp(a6),a0 	 ; off top?
	bhs.s	sbm_nch0		 ; ... yes

	move.l	ch_chid(a6,a0.l),a0	 ; channel open?
	move.w	a0,d0
	bmi.s	sbm_nch0		 ; ... no

	moveq	#0,d0			 ; and OK
	rts

sbm_nch0
	moveq	#err.ichn,d0
	rts

;+++
; Set error channel
;---
sbm_errch
	movem.l d0/d6,-(sp)		 ; save error code and line

	moveq	#-1,d0			 ; clear trap #4
	move.l	d0,a0
	trap	#3

	bsr.s	sbm_chan0		 ; try channel 0
	beq.s	sbme_exit

	st	sb_clc0(a6)		 ; close channel 0 on run

	lea	con_def,a1		 ; open channel
	jsr	uq_opcon
	bne.l	sb_fatal

	move.l	sb_chanb(a6),a2
	jsr	sb_inchan		 ; set open channel
	cmp.l	sb_chanp(a6),a2 	 ; one more?
	ble.s	sbme_exit
	move.l	a2,sb_chanp(a6)

sbme_exit
	movem.l (sp)+,d0/d6
	rts

con_def dc.b	$ff,$1,$0,$7
	dc.w	$100,$3e,$80,$60

;+++
; Write error message to channel 0
;---
sb_wermess
sbm_ech0
	bsr	sbm_errch		; channel for error messages
sbm_werms
	moveq	#0,d2
	move.l	d0,d4
	jsr	gu_smul 		; wait for channel
	move.l	d4,d0
	move.w	ut.werms,a2
	jmp	(a2)

sbm_wint
	move.w	ut.wint,a2
	jmp	(a2)

sbm_werrl
	move.l	d0,-(sp)
	bsr	sbm_errch		 ; channel for error messages
	tst.l	d6
	ble.s	sbm_werr		 ; no line

	move.l	#err4.atln,d0
	bsr	sbm_werms		 ; at line
	move.l	d6,d1
	swap	d1
	bsr.s	sbm_wint		 ; line number
	tst.w	d6			 ; any statement?
	beq.s	sbm_perrs		 ; ... no
	moveq	#':',d1
	jsr	gu_sbyte		 ; :
	move.w	d6,d1
	bsr.s	sbm_wint		 ; statement number
sbm_perrs
	moveq	#' ',d1
	jsr	gu_sbyte

sbm_werr
	move.l	(sp)+,d0
	bra.s	sbm_werms		 ; write message

sbm_break
	moveq	#err.nc,d0
sbm_perr
	moveq	#0,d6			 ;
sbm_perrl
	bsr	sbm_werrl		 ; write error line

	tst.b	sb_wheiu(a6)		 ; error in when error processing?
	beq.s	sbm_rcmd
	move.l	#err4.wher,d0
	bsr	sbm_werms		 ; write message string

sbm_rcmd
	sf	sb_wheiu(a6)		 ; clear when error in use
	st	sb_nline(a6)		 ; stop executing
	sf	sb_cmdst(a6)		 ; and do not continue

sbm_done
;******************************************
;+++
; Main SBASIC Start (initial command channel set up)
;---
sb_start
sbm_start
	tst.l	sb_hichn(a6)		 ; history already set up?
	bne.s	sbm_hisok		 ; ... yes
	moveq	#ioa.open,d0
	moveq	#-1,d1
	moveq	#ioa.kexc,d3
	lea	history_name,a0
	trap	#2			 ; open private history
	move.l	a0,sb_hichn(a6)
sbm_hisok
	move.l	sb_cmdch(a6),d0 	 ; command channel
	beq.s	sbm_loop		 ; ... none
	move.l	d0,a0
	moveq	#-1,d3
	bsr.s	sbm_spos0		 ; set position
	bne.s	sbm_loop		 ; ... really not a file

	moveq	#iob.fmul,d0
	moveq	#2,d2			 ; get first two characters of file
	move.l	sb_buffb(a6),a1
	trap	#4
	trap	#3
	move.w	-2(a6,a1.l),d4		 ; QLOAD format flag
	bsr.s	sbm_spos0
	cmp.w	#'Q1',d4		 ; QLOAD
	bne.s	sbm_loop		 ; ... no

	sf	sb_cmdst(a6)		 ; command line in buffer will be destroyed
	jsr	sb_qload		 ; do QLOAD
	beq.l	sbm_close
	st	sb_nline(a6)		 ; do not continue
	moveq	#0,d6			 ; no error line
	bsr	sbm_werrl
	bra.l	sbm_close

sbm_spos0
	moveq	#0,d1
	moveq	#iof.posa,d0
	trap	#3
	tst.l	d0
	rts

history_name
	dc.w	12
	dc.b	'HISTORY_2048'

; Main SBASIC Loop

sbm_loop
	tst.l	sb_cmdch(a6)		 ; reading program?
	bne.s	sbm_nextl		 ; ... yes
	tst.w	sb_nline(a6)		 ; execute?
	bpl.l	sbm_execute		 ; ... yes
	tst.b	sb_cmdst(a6)		 ; in the middle of exec command line?
	bne.l	sbm_xcmd		 ; ... yes

sbm_nextl
	move.l	sb_buffb(a6),sb_buffp(a6)
	clr.l	sb_pcerp(a6)		 ; no error position

sbm_retry
;****	     move.l  sb_ccptr(a6),d0	      ; cammand code pointer
;****	     bne.s   sbm_flcc		      ; there is one

	bsr	sbm_comch		 ; channel for commands
	moveq	#iow.sova,d0		 ; is it window
	moveq	#0,d1
	moveq	#-1,d3
	trap	#3
	move.l	d0,d6			 ; save flag

	moveq	#0,d1
	move.l	sb_pcerp(a6),d2 	 ; write out ok bit of line
	beq.s	sbm_eline		 ; ... none
	tst.l	d6			 ; console?
	bne.l	sbm_close		 ; ... no, close file

sbm_weln
	move.l	sb_buffb(a6),a1
	sub.l	a1,d2
	subq.w	#1,d2			 ; back a bit
	bpl.s	sbm_wline
	moveq	#0,d2
sbm_wline
	moveq	#iob.smul,d0
	trap	#4
	trap	#3
sbm_eline
	tst.l	d6			 ; console?
	bne	sbm_fline

	swap	d1			 ; cursor in top
	move.l	sb_buffp(a6),a1
	move.w	a1,d1			 ; current line length
	sub.w	sb_buffb+2(a6),d1
	move.l	sb_buffb+8(a6),d2	 ; top of buffer
	sub.l	sb_buffb(a6),d2
	moveq	#iob.elin,d0
	trap	#4
	trap	#3

	moveq	#0,d6			 ; no line number
	lea	sbm_perrl,a2		 ; print error if not complete
	tst.l	d0
	bne	sbm_ercmd
	cmp.b	#k.esc,-1(a6,a1.l)	 ; ends with esc?
	beq	sbm_scrub		 ; ... yes

; +++ code for command line history
hst_reg reg	d0-d4/a0-a2
	movem.l hst_reg,-(sp)
	cmp.b	#k.up,-1(a6,a1.l)	 ; ends with up?
	beq	sbm_histup
	cmp.b	#k.down,-1(a6,a1.l)	 ; down maybe?
	beq.s	sbm_histdown

; None of those, so enter string into our history
	move.w	d1,d2			 ; length of string
	beq.s	sbm_no_add
	move.l	sb_buffb(a6),a1
	cmp.b	#k.nl,(a6,a1.l) 	 ; only new line?
	beq.s	sbm_no_add
	moveq	#iob.smul,d0
	moveq	#-1,d3
	move.l	sb_hichn(a6),a0
	trap	#4			 ; enter new line for history
	trap	#3
sbm_no_add
	movem.l (sp)+,hst_reg

	move.b	#k.nl,-1(a6,a1.l)	 ; set end of line
	bra	sbm_pline

; Clear old string from screen and set cur pos to start of empty space
; d1 = string length as given from iob.elin
; a0 = CON channel ID
sbm_clearstr
	move.l	d1,-(sp)
	moveq	#iow.xtop,d0
	moveq	#-1,d3
	lea	sbm_getcurpos,a2	 ; re-construct cursor pos of line start
	trap	#3
	move.l	d1,d4

	moveq	#iow.spix,d0
	move.w	d1,d2
	swap	d1
	moveq	#-1,d3
	trap	#3			 ; set cursor pos to string position
	move.l	(sp)+,d1

	move.w	d1,d2			 ; length of string
	moveq	#' ',d0
	move.l	sb_buffb(a6),a1 	 ; buffer base
	bra.s	sbm_hcloops
sbm_hcloop
	move.b	d0,(a6,a1.l)		 ; clear string
	addq.l	#1,a1
sbm_hcloops
	dbf	d1,sbm_hcloop
	moveq	#iob.smul,d0
	move.l	sb_buffb(a6),a1 	 ; buffer base
	trap	#4
	trap	#3			 ; overwrite old string with blanks
	moveq	#iow.spix,d0
	move.l	d4,d1
	swap	d1
	move.w	d4,d2
	moveq	#-1,d3
	trap	#3			 ; set cursor pos to string position
	rts

; Down key was pressed
sbm_histdown
	bsr.s	sbm_clearstr		 ; clear old string from screen
	moveq	#iof.posr,d0
	moveq	#-2,d1			 ; back down by 2
	moveq	#-1,d3
	move.l	sb_hichn(a6),a0 	 ; history channel ID
	trap	#3
	moveq	#err.eof,d1
	cmp.l	d1,d0			 ; out of bonds?
	bne.s	sbm_gethist		 ; no, go ahead
	move.l	sb_buffb(a6),sb_buffp(a6) ; yes, return with empty string
	bra.s	sbm_endhist

; Up key was pressed
sbm_histup
	bsr.s	sbm_clearstr		 ; clear old string from screen
	moveq	#-1,d1
	moveq	#0,d2			 ; just read FPOS for now
sbm_huagain
	moveq	#-1,d3			 ; set here so it's set in sbm_gethist
	tst.l	d1
	beq.s	sbm_gethist		 ; at end of history, exit loop
	moveq	#iof.posr,d0
	move.l	d2,d1
	move.l	sb_hichn(a6),a0 	 ; history channel ID
	trap	#3
	moveq	#-1,d2			 ; Backtrack in case of EOF
	cmp.l	#err.eof,d0
	beq.s	sbm_huagain		 ; if EOF, try to hit last line

sbm_gethist
	move.l	#$7fff,d0		 ; max allowed size for iob.flin
	move.l	sb_buffb(a6),a1 	 ; buffer base
	move.l	sb_buffb+8(a6),d2	 ; top of buffer
	sub.l	a1,d2			 ; buffer size
	cmp.l	d0,d2			 ; mustn't exceed max value
	ble.s	sbm_histget
	move.l	d0,d2
sbm_histget
	moveq	#iob.flin,d0
	trap	#4
	trap	#3			 ; get line out of history
	subq.l	#1,a1
	move.l	a1,sb_buffp(a6) 	 ; set cursor pos to end of line
sbm_endhist
	movem.l (sp)+,hst_reg
	moveq	#0,d1
	bra	sbm_eline
;--- code for command line history

sbm_scrub
	move.l	sb_buffb(a6),sb_buffp(a6) ; ... no, scrub it
	moveq	#0,d1
	bra	sbm_eline		 ; and try again

sbm_fline
	move.l	sb_buffb(a6),sb_buffp(a6); base of buffer
sbm_flloop
	move.l	sb_buffp(a6),a1 	 ; base of input line
	move.l	sb_buffb+8(a6),d2	 ; top of buffer
	sub.l	a1,d2
	move.w	d2,a2
	cmp.l	d2,a2			 ; word overflowed?
	beq.s	sbm_fldo
	move.l	#$7fff,d2		 ; max length
sbm_fldo
	moveq	#iob.flin,d0
	trap	#4
	trap	#3
	tst.l	d0
	beq.s	sbm_pline

	lea	sbm_nextl,a2		 ; do next line if not complete

	moveq	#err.bffl,d1
	cmp.l	d1,d0			 ; buffer full?
	bne.s	sbm_ercmd
	move.l	a1,sb_buffp(a6) 	 ; set pointer
	sub.l	sb_buffb(a6),a1 	 ; total length so far
	move.l	a1,d1			 ; space required
	jsr	sb_resbf		 ; allocate
	bra.s	sbm_flloop

sbm_ercmd
	moveq	#err.nc,d1
	cmp.l	d1,d0			 ; not complete?
	beq.s	sbm_errec		 ; ... yes, try again
	tst.l	sb_cmdch(a6)		 ; was it read from #0?
	bne.s	sbm_close		 ; ... no, do that now
	moveq	#err.eof,d1
	cmp.l	d1,d0			 ; end of file?
	beq	sb_die			 ; ... yes, exit OK
	moveq	#err.bffl,d1
	cmp.l	d1,d0			 ; buffer full?
	bne	sb_fatal		 ; ... no, not so good
sbm_errec
	jmp	(a2)			 ; do not complete action

sbm_close
	bsr.s	sbm_clcmd		 ; close command channel
	tst.w	sb_nline(a6)		 ; run?
	bpl.l	sbm_execute
	bra	sbm_nextl


sbm_pline
	subq.w	#1,d1			 ; any line at all
	beq.s	sbm_scrub		 ; ... no
	move.l	a1,sb_buffp(a6)

;+++
; SBASIC entry with command in buffer
;---
sb_xcmd
	clr.w	sb_line(a6)
	jsr	sb_palin		 ; parse line
	beq.s	sbm_ledit
	move.l	sb_line(a6),d6		 ; error line
	clr.w	d6
	bsr.l	sbm_werrl
	tst.l	sb_cmdch(a6)		 ; reading from file?
	beq.s	sbm_errline		 ; ... no, it is an error line
	jsr	sb_paerr
	beq.s	sbm_ledit		 ; from file, edit it in

sbm_errline
	subq.l	#1,sb_buffp(a6) 	 ; remove newline
	bra.l	sbm_retry


sbm_ledit
	jsr	sb_ledit
	bra.s	sbm_ccmd		 ; command line

	assert	sb.edt,$ff
	st	sb_edt(a6)		 ; program edited

	bra.l	sbm_loop

sbm_ccmd
	jsr	sb_clrfrp		 ; clear FOR / REP loops defined in cmd
sbm_xcmd
	st	sb_nline(a6)		 ; command line
	move.b	sb_cmdst(a6),sb_nstmt(a6); continue from here
	st	sb_cmdst(a6)		 ; allow it to be reset

	jsr	sb_compile		 ; compile command line
	bne.s	sbm_xerr		 ; compilation error

	jsr	sb_inter		 ; interpret
	move.b	sb_stmt(a6),d1
	and.b	d1,sb_cmdst(a6) 	 ; set command stmt if poss

	tst.l	d0			 ; error?
	beq.s	sbm_ncont

sbm_xerr
	sf	sb_cmdst(a6)		 ; give up command line
	pea	sbm_perr
sbm_clcmd
	move.l	sb_cmdch(a6),d1 	 ; reading from file?
	beq.s	sbm_rts
	clr.l	sb_cmdch(a6)		 ; no file now
	move.l	d1,a0			 ; close command channel
	jmp	gu_fclos
sbm_rts
	rts

sbm_execute
	jsr	sb_execute		 ; execute program
	bne.l	sbm_perrl

sbm_ncont
	tas	sb_break(a6)		 ; just in case
	beq.l	sbm_break

	tst.b	sb_cont(a6)		 ; continue?
	bne.l	sbm_loop

	move.w	sb_actn(a6),d0		 ; action required

	move.w	sbm_actn(pc,d0.w),d0
	jmp	sbm_actn(pc,d0.w)

sbm_actn
	dc.w	sbm_clear-sbm_actn
	dc.w	sbm_new-sbm_actn
	dc.w	sbm_stop-sbm_actn
	dc.w	sbm_run-sbm_actn
	dc.w	sbm_lrun-sbm_actn
	dc.w	sbm_load-sbm_actn
	dc.w	sbm_mrun-sbm_actn
	dc.w	sbm_merge-sbm_actn
	dc.w	sbm_cont-sbm_actn
	dc.w	sbm_nact-sbm_actn

sbm_clear
	jsr	sb_clrdat			 ; clear data and return stacks
	clr.l	sb_dline(a6)			 ; and data pointer
	assert	0,sb.edt-$ff,sb.edtn-$80
	tas	sb_edt(a6)			 ; edited! to redo name types
sbm_nact
	jsr	sb_ret2pos			 ; ret stack to line no ** 1.03
	move.w	sb_line(a6),d0			 ; current line
	beq.s	sbm_nactc
	move.w	d0,sb_nline(a6) 		 ; next program line
	move.b	sb_stmt(a6),sb_nstmt(a6)	 ; and statement
	bra	sbm_execute			 ; ... program
sbm_nactc
	st	sb_nline(a6)			 ; command line
	tst.b	sb_cmdst(a6)			 ; continue command line?
	bne	sbm_xcmd			 ; ... yes, command line
	bra	sbm_loop

sbm_new
sbm_lrun
sbm_load
	jsr	sb_clrall			 ; clear all
	sf	sb_cmdst(a6)			 ; clear command line stmt nr
	clr.l	sb_dline(a6)			 ; and data pointer
	clr.l	sb_edlin(a6)			 ; and edit line
sbm_lmrun
	assert	sb.edt,$ff
	st	sb_edt(a6)			 ; set edited, just in case!
	clr.w	sb_cline(a6)			 ; clear continue line
	sf	sb_wheiu(a6)			 ; when error not in use
	clr.l	sb_flags(a6)			 ; and flags
	bra.l	sbm_start

sbm_mrun
sbm_merge
	tst.b	sb_cmdl(a6)			 ; command line?
	blt.s	sbm_lmrun			 ; ... yes
	move.w	sb_line(a6),sb_nline(a6)	 ; continue line
	move.b	sb_stmt(a6),sb_nstmt(a6)	 ; and statement
	bra.s	sbm_lmrun

sbm_stop
	tst.b	sb_cmdl(a6)			 ; command line?
	blt.s	sbm_stopc			 ; ... yes
	tst.b	sb_wheiu(a6)			 ; when error?
	bne.s	sbm_stopw			 ; ... yes
	move.w	sb_line(a6),sb_cline(a6)	 ; ... no, save context
	move.b	sb_stmt(a6),sb_cstmt(a6)
sbm_stopw
	sf	sb_wheiu(a6)			 ; when error not in use
	bra.l	sbm_done

sbm_stopc
	sf	sb_cmdst(a6)			 ; clear command line stmt nr
	bra.l	sbm_loop

sbm_run
	jsr	sb_clrstk			 ; clear return stack
	clr.w	sb_cline(a6)			 ; clear continue line
sbm_endw
	sf	sb_wheiu(a6)			 ; when error not in use
	clr.l	sb_flags(a6)			 ; and flags
	bra.l	sbm_loop

sbm_cont
	move.w	sb_cline(a6),d0 		 ; continue line
	ble.s	sbm_stop			 ; ... not set
	move.w	d0,sb_nline(a6)
	move.b	sb_cstmt(a6),sb_nstmt(a6)
	clr.w	sb_cline(a6)			 ; clear continue line
	sf	sb_wheiu(a6)			 ; when error not in use
	bra.l	sbm_execute			 ; and continue


; TEMPORARY error entries

sb_insmem
	bclr	#7,sb_inint(a6) 		 ; in interpreter?
	bne.l	sb_istop			 ; ... yes, stop it

	move.l	sb_prstb(a6),a7
	add.l	a6,a7				 ; reset stack
	jsr	sb_clrstk			 ; clear stacks
	jsr	sb_clrcmp			 ; clear compiled bits
	moveq	#err.imem,d0
	bra	sbm_perr

sb_error
	jsr	sb_ermess			 ; unknown error message

sb_panic
	sf	sb_inint(a6)			 ; not in interpreter now
	move.l	sb_prstb(a6),a7
	add.l	a6,a7				 ; reset stack
	move.l	#'FAIL',d1
	cmp.l	-(sp),d1			 ; already failed?
	beq.l	sb_fatal			 ; ... yes
	move.l	d1,(sp) 			 ; mark failed
	move.l	d0,-(sp)			 ; and save error
	bsr	sbm_clcmd			 ; close input channel
	jsr	sb_clrall			 ; try to clear
	move.l	(sp)+,d0
	clr.l	(sp)+				 ; mark not failed
	bra	sbm_perr

;+++
; Re-construct cursor position of start of string after an iob.elin call.
; Needs to be called through iow.xtop
;
;	entry				exit
; d1	curpos as given from iob.edlin	X/Y pixel cursor position in window
; a0	CDB				CDB
;---
sbm_getcurpos
	subq.w	#1,d1
	andi.l	#$ffff,d1
	moveq	#0,d2
	move.w	sd_xpos(a0),d2		; X cursor position
	divu	sd_xinc(a0),d2		; now in cursor increments
	swap	d2
	move.w	d2,d5			; remember offset
	swap	d5
	clr.w	d2
	swap	d2			; d2 = X cur pos in cur increment
	moveq	#0,d3
	move.w	sd_ypos(a0),d3		; Y cursor position
	divu	sd_yinc(a0),d3		; now in cursor increments
	swap	d3
	move.w	d3,d5			; remember offset
	clr.w	d3
	swap	d3			; d3 = Y cur pos in cur increment
	moveq	#0,d4
	move.w	sd_xsize(a0),d4
	divu	sd_xinc(a0),d4		; calculate window width in cursor inc
	mulu	d4,d3
	add.w	d2,d3			; now a stream position
	sub.l	d1,d3			; subtract string length
	bcc.s	sgc_1
	moveq	#0,d3
	bra.s	sgc_2
sgc_1	divu	d4,d3			; again X/Y position in cur increment
sgc_2	move.l	d3,d1
	swap	d1
	mulu	sd_xinc(a0),d1		; now in pixel coordinates
	mulu	sd_yinc(a0),d3
	swap	d1
	move.w	d3,d1
	add.l	d5,d1			; add remaining pixel offset
	rts

	end
