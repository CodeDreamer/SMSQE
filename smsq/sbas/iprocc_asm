; SBAS_IPROCC - PROC	 Code Call    1994 Tony Tebby

	section sbas

	xdef	bo_docpr	    ; routine calls
	xdef	bo_dopr

	xref	bo_dospr
	xref	bo_dosprc

	xref	sb_icall
	xref	sb_clrstk

	xref	sb_istop
	xref	sb_ireset
	xref	sb_ierset

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_mac_assert'

;+++
; Within this routine
;
;	D6 is limit of arithmetic stack (with some bytes spare)
;	A1 is pointer to arithmetic stack
;	A2 is entry address
;	A3 is pointer to name table
;	A4 is pointer to program
;	A5 address of next token loop
;	A6 is pointer to system variables
;---

;--------------------------------- routine calls
bo_docpr
	sf	sb_redo(a6)
	jsr	sb_icall		 ; call procedure / function

	add.l	sb_arthb(a6),d7
	move.l	d7,sb_arthp(a6) 	 ; reset RI stack
	lea	(a6,d7.l),a1

	tst.b	sb_redo(a6)		 ; redo?
	bne.s	bip_redo		 ; ... yes
	tst.l	d0			 ; error?
	bne.s	bip_istop		 ; ... yes
	tst.b	sb_cont(a6)		 ; continue?
	bne.l	sb_ireset		 ; ... yes
bip_istop
	jmp	sb_istop

bip_redo
	move.l	sb_retsp(a6),a2
	move.l	rt.pfsize+rt_setup(a6,a2.l),a4 ; restore address of setup
	subq.l	#2,a4			 ; true start
	pea	sb_ireset
	jmp	sb_clrstk		 ; unravel the stack

;--------------------------------------
bo_dopr
	move.w	(a4)+,d3		 ; name index
	moveq	#-nt.mcprc,d0
	add.b	nt_nvalp(a3,d3.w),d0	 ; mc proc?
	beq.s	bo_docpr		 ; ... yes
	addq.b	#nt.mcprc-nt.sbprc,d0	 ; sb proc?
	bne.s	bdp_bprc

	tst.b	sb_cmdl(a6)		 ; command line?
	beq.l	bo_dospr		 ; ... no, normal SBASIC
	bra.l	bo_dosprc		 ; or SBASIC from command line

bdp_bprc
	moveq	#ern4.bprc,d0
	jmp	sb_ierset
	end
