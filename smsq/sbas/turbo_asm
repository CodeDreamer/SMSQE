; Turbo	Patch(es)	      1993  Tony Tebby

	section	uq

	xdef	sb_pturbo

	xref	sb_litem

	include	'dev8_keys_qdos_sms'
	include	'dev8_keys_sys'
	include	'dev8_keys_sbasic'
	include	'dev8_keys_68000'

sbpt_name dc.w	  13,'BASIC_POINTER'
sbpt_code moveq	  #sms.info,d0	    ; get SB vars via SuperBASIC base
	  trap	  #do.sms2
sbpt_old
	  move.l  sys_sbab(a0),a0
	  lea	  sb_offs(a0),a0
	  rts

sbpt_repl moveq	  #sms.info,d0	    ; get SB vars via Job 0
	  trap	  #do.sms2
; @@@@@		 tst.l	 d1		   ; actually job 0?
; @@@@@		 beq.s	 sbpt_old	   ; ... yes, use old code
	  move.l  sys_jbtb(a0),a0
	  move.l  (a0),a0
	  lea	  sb_vars(a0),a0
	  rts

;+++
; Patch	Turbo TK
;
;	a6 c  p	pointer	to SBASIC area
;
;	status return standard
;---
sb_pturbo
	lea	sbpt_name+1,a1		 ; locate "BASIC_POINTER"
	jsr	sb_litem
	ble.s	sbpt_done

	lea	$200(a1),a0		 ; search range

	movem.l	sbpt_code,d1/d2/d3	 ; search pattern

sbpt_swap
	swap	d1
sbpt_check
	cmp.l	a0,a1			 ; end of search?
	bge.s	sbpt_done		  ; ...	yes

	cmp.w	(a1)+,d1		 ; match?
	bne.s	sbpt_check		 ; ... no

	swap	d1			 ; try other half
	cmp.w	(a1),d1			 ; match?
	bne.s	sbpt_swap		 ; ... no
	cmp.l	2(a1),d2		 ; 2nd long word?
	bne.s	sbpt_swap		 ; ... no match

	cmp.l	6(a1),d3		 ; 3nd long word?
	bne.s	sbpt_swap		 ; ... no match

	lea	sbpt_repl,a0		 ; replacement code address
	move.l	a0,(a1)
	move.w	#jmp.l,-(a1)		 ; and jump

sbpt_done
	moveq	#0,d0
	rts
	end
