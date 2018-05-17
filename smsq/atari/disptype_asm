; ATARI determine display hardware type        1993  Tony Tebby

	section init

	xdef	at_disptype

	xref	gu_exvt

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_tt'
	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_smsq_base_keys'

;+++
; Determines the display hardware type (monochrome preferred if not TT)
;
;	d5  r	byte			$00   unknown or none
;					$20   mono (not TT)
;					$40   extended mode 4
;					$80   QVME
;	d6 c  p hardware type
;	status returned 0
;---
at_disptype
	cmp.b	#sys.mtt,d6		 ; TT
	blt.s	atd_ckmono
	bsr.s	atd_colour		  ; check for colour adapter
	tst.b	d5			 ; any adapter?
	bne.s	atd_rts_ok		  ; ... yes
	move.w	#ttd.sthi,ttd_res	 ; ... no, set ST HI RES mode
	bra.s	atd_mono

atd_ckmono
	tst.b	mfp_mon 		 ; is it monochrome?
	bpl.s	atd_mono		 ; ... yes
	bsr.s	atd_colour		 ; get type of colour adapter
	bra.s	atd_rts_ok

atd_mono
	moveq	#sys.mmon,d5		 ; ... yes, monochrome
atd_rts_ok
	moveq	#0,d0
	rts

atd_colour
atd.reg  reg	 a0/a1/a2/a3
	movem.l atd.reg,-(sp)

	lea	atd_wbyte,a0		 ; write byte to address select reg
	bsr.s	atd_buset
	beq.s	atd_ext 		 ; no error, try extended emulator

	lea	atd_waccs,a0		 ; write byte with access control
	bsr.s	atd_buset
	bne.s	atd_ext 		 ; ... it is not QVME card
	move.b	#sys.mvme,d5		 ; ... it is QVME card
	bra.s	atd_done

atd_ext
	lea	vde_slct,a0		 ; read this to select Edwin Ext disp
	move.w	#$0222,vdr_palt+2	 ; set palette
	move.b	(a0),d5 		 ; ... try to select
	move.b	(a0),d5
	move.b	(a0),d5

	move.w	#$0555,vdr_palt+2	 ; change palette
	move.b	#$f,vde_desl		 ; deselect
	move.w	#$0777,d5
	and.w	vdr_palt+2,d5		 ; check palette
	clr.w	vdr_palt+2
	sub.w	#$0222,d5
	bne.s	atd_none

	moveq	#sys.mext,d5
	bra.s	atd_done

atd_none
	moveq	#0,d5
atd_done
	movem.l (sp)+,atd.reg
	rts


atd_buset
	moveq	#exv_accf,d0		 ; bus error has been renamed access f
	jmp	gu_exvt

atd_waccs
	move.b	#0,vde_accs		 ; try with access control
atd_wbyte
	move.b	#0,vde_radr		 ; simple test for bus error
	rts

	end
