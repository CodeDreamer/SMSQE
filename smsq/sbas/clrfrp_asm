; SBAS_CLRFRP - Clear FOR and REPeat loops in Command Line    1994 Tony Tebby

	section sbas

	xdef	sb_clrfrp

	xref	sb_clrval
	xref	sb_setvar

	include 'dev8_keys_sbasic'
	include 'dev8_mac_assert'

;+++
; SBASIC - Clear FOR and REPeat loops in command line
;
;	d0/d1/d2/d3/a0/a1/a3/a4 smashed
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return arbitrary
;---
sb_clrfrp
	move.l	sb_nmtbb(a6),a3 	 ; name table
	move.l	sb_nmtbp(a6),a4
	moveq	#-1,d2			 ; NOT program line is -1
;;	  moveq   #0,d2 		   ; assume no program
;;	  moveq   #0,d3
;;;; for the moment, try without clearing FOR / REP in program
;;;;	    tst.b   sb_edt(a6)		     ; edited?
;;;;	    bne.s   scf_ntlend		     ; ... yes, program is not valid
;;	  move.l  sb_progb(a6),d2	   ; program base
;;	  beq.s   scf_ntlend		   ; no program
;;	  move.l  sb_progp(a6),d3	   ; program top
	bra.s	scf_ntlend

scf_ntloop
	assert	6,nt.rep,nt.for-1
	moveq	#$e,d1
	and.b	nt_nvalp(a6,a3.l),d1	 ; usage
	subq.b	#nt.rep,d1		 ; FOR/REP ?
	bne.s	scf_ntnext

	move.l	nt_value(a6,a3.l),a0	 ; value
	cmp.w	dt_start(a0),d1 	 ; in program?
	bne.s	scf_ntnext		 ; ... yes
scf_clear
	jsr	sb_clrval		 ; clear the value
	jsr	sb_setvar		 ; and reset to variable
scf_ntnext
	addq.l	#nt.len,a3
scf_ntlend
	cmp.l	a4,a3			 ; all done?
	blt.s	scf_ntloop
	rts
	end
