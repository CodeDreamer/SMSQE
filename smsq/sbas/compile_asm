; SBAS_COMPILE - compiler control	  1994   Tony Tebby

	section sbas

	xdef	sb_compile

	xref	sb_cmptk
	xref	sb_cmpnset
	xref	sb_cmpnuse
	xref	sb_cmpop
	xref	sb_cmpstt
	xref	sb_cmpadd
	xref	sb_cmpdst

	xref	sb_shwrk

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

;+++
; SuperBASIC compiler control for program
;
;	a4 r	pointer to program
;	a5 c  p base of program token block
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_compile
	tst.b	sb_edt(a6)		 ; pre-compile program required?
	bge.s	sbc_cmdl		 ; ... no

	sf	sb_cmdl(a6)
	jsr	sb_cmpnset		 ; set up name usage table

	clr.w	sb_line(a6)		 ; line number counter
	move.l	sb_srceb(a6),a2 	 ; base
	addq.l	#2,a2			 ; skip link word
	move.l	sb_srcep(a6),d7 	 ; and top of program
	lea	sb_ptokb(a6),a5 	 ; where to put tokens
	jsr	sb_cmptk		 ; convert source to compiler tokens
	move.l	d0,d4
	jsr	sb_cmpnuse		 ; sort out usage
	exg	d4,d0
	tst.l	d0			 ; error in precompile?
	bne.s	sbc_cmder		 ; ... yes
	move.l	d4,d0			 ; ... in usage?
	beq.s	sbc_pred		 ; ... no, precompile done

sbc_cmder
	tst.w	sb_nline(a6)		 ; is is actually command line
	bmi.s	sbc_cmdl		 ; ... yes
	bra.l	sbc_seter		 ; ... no, report error

sbc_pred
	move.b	#1,sb_edt(a6)		 ; mark pre-compiled

sbc_cmdl
	tst.w	sb_nline(a6)		 ; command line required?
	bge.s	sbc_compprg		 ; ... no

	st	sb_cmdl(a6)		 ; processing command line
	move.w	#-1,sb_line(a6) 	 ; line zero is first line
	move.l	sb_cmdlb(a6),a2 	 ; base
	move.l	sb_cmdlp(a6),d7 	 ; and top of program

	lea	sb_ctokb(a6),a5 	 ; where to put tokens
	jsr	sb_cmptk		 ; convert command to compiler tokens
	bne.s	sbc_seter

	jsr	sb_cmpop		 ; convert to program
	bne.s	sbc_rts
	jsr	sb_cmpstt		 ; create statement table
	jsr	sb_cmpadd		 ; fill in addresses

sbc_compprg
	tst.b	sb_edt(a6)		 ; compilation required?
	beq.s	sbc_ok			 ; ... no, never
	tst.b	sb_cmppg(a6)		 ; requested?
	bne.s	sbc_doprg		 ; ... yes
	tst.w	sb_nline(a6)		 ; actually doing command line?
	bmi.s	sbc_ok			 ; ... yes, do not bother with prog

sbc_doprg
	sf	sb_cmdl(a6)
	lea	sb_ptokb(a6),a5 	 ; where to put tokens
	jsr	sb_cmpop		 ; convert to program
	bne.s	sbc_rts
	jsr	sb_cmpstt		 ; create program statement table
	jsr	sb_cmpadd		 ; fill in addresses
	jsr	sb_cmpdst		 ; create data statement table

	lea	sb_dtstb(a6),a1
	jsr	sb_shwrk		 ; shrink data statements area
	lea	sb_progb(a6),a1
	jsr	sb_shwrk		 ; shrink program area

	clr.b	sb_edt(a6)		 ; not edited now
	clr.b	sb_cmppg(a6)		 ; no request

sbc_ok
	moveq	#0,d0
sbc_rts
	sf	sb_cmppg(a6)		 ; no request for compilation now
	rts

sbc_seter
	move.l	sb_pcerp(a6),d6 	 ; error line in d6
	clr.w	d6
	tst.l	d0
	rts
	end
