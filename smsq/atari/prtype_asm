; ATARI determine processor / hardware type	   1993  Tony Tebby

	section init

	xdef	at_prtype

	xref	gu_exvt
	xref	gu_rbuse

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_rtc'
	include 'dev8_keys_atari_blit'
	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_mac_creg'

;+++
; Determines the variable bits of the hardware.
;
;	d6 r	four bytes:	msbyte	processor number 00, 10, 20, 30, 40 hex
;
;					co-processor	 01 Internal PMMU
;							 02 68851 PMMU
;							+04 Internal FPU
;							+08 68881/2 FPU
;
;					special procs	 00
;
;				lsbyte	machine 	 00 ST
;							 02 Mega ST (ST+RTC)
;							 04 Stacy
;							 08 STE
;							 0A Mega STE
;							 10 Falcon
;							 18 TT
;							 +1 for blitter
;---
at_prtype
atp.reg  reg	 d2/a0/a1/a2
	movem.l atp.reg,-(sp)

	moveq	#sys.88xf,d2		 ; if there is an FPU, assume 68881

	moveq	#0,d6			 ; base processor, base machine
	lea	atp_vbase,a0		  ; set vector base (68010+)
	moveq	#exv_ilin,d0		 ; trap illegal instructions
	bsr.s	atp_exv 		  ; illegal instruction on 68000
	bne.s	atp_pdone		  ; ... it is, processor done

	moveq	#$10,d6 		 ; 68010
	lea	atp_odda,a0		  ; word access at odd address
	moveq	#exv_aerr,d0		 ; trap address errors
	bsr.s	atp_exv 		  ; address error on 68010
	bne.s	atp_pdone		  ; ... it is, processor done

	lea	atp_itt0,a0		  ; read instruction transparent trans
	moveq	#exv_ilin,d0		 ; trap illegal instructions
	bsr.s	atp_exv 		  ; illegal instruction on 68020, 68030
	beq.s	atp_40			  ; ... it is 40, check EC, LC

	move.w	#sys.851m<<8+$20,d6	 ; assume 68020 with 68851
	lea	atp_pmove,a0		  ; pmove
	move.l	#exv_flin<<16+exv_ilin,d0  ; trap f-line + illegal instructions
	bsr.s	atp_exv
	beq.s	atp_pdone		  ; ... it is, processor done
	ext.w	d6			 ; assume 68020, no MMU
	subq.w	#1,d0			 ; f-line?
	beq.s	atp_pdone		  ; ... it is, processor done
	move.w	#sys.immu<<8+$30,d6	 ; 68030, internal PMMU
	bra.s	atp_pdone

atp_exv
	jmp	gu_exvt 		 ; do exception test

atp_40
	moveq	#$40,d6 		 ; 68040
	lea	atp_mmutc,a0		  ; read MMU Trans control register
	moveq	#exv_ilin,d0		 ; trap illegal instructions
	bsr.s	atp_exv 		  ; illegal?
	bne.s	atp_pdone		  ; ... it is, processor done
	move.w	#sys.immu<<8+$40,d6	 ; 680(LC)40

	moveq	#sys.ifpu,d2		 ; if 68040 assume internal FPU

atp_pdone
	lea	atp_fpu,a0		     ; check FPU
	moveq	#exv_flin,d0		 ; trap fline
	bsr.s	atp_exv
	bne.s	atp_cdone		  ; co-processors done

	lsl.w	#8,d2
	or.w	d2,d6			 ; set fp co-processor

atp_cdone

; now check machine

	moveq	#sys.mtt,d2		 ; assume TT (what about Falcon?)
	lea	rtc_seco,a2		 ; check for ST clock
	bsr.s	atp_buse		  ; if TT, bus error on byte access
	bne.s	atp_mdone		  ; ... machine done

	moveq	#sys.mmste,d2		 ; assume Mega STE
	lea	scu_sstt,a2		 ; check for SCU
	bsr.s	atp_buse		  ; if Mega STE, ok on on byte access
	beq.s	atp_mdone		  ; ... machine done

	moveq	#sys.mste,d2		 ; assume STE
	lea	snd_dmac,a2		 ; check for DMA sound
	bsr.s	atp_buse		  ; if STE, ok on on byte access
	beq.s	atp_mdone		  ; ... machine done

;	 moveq	 #sys.msta,d2		  ; assume Stacy
;	 lea	 at_stcnf,a2		  ; check for screen control
;	 bsr.s	 atp_buse		   ; if Stacy, ok on on byte access
;	 beq.s	 atp_mdone		   ; ... machine done

	moveq	#sys.mst,d2		 ; assume ST, no clock
	bset	#rtc..bk1,rtc_mode	 ; set alarm bank
	move.b	#7,rtc_mino		 ; to 7 minutes past
	moveq	#$f,d0
	and.b	rtc_mino,d0
	subq.b	#7,d0			 ; 7 accepted?
	bne.s	atp_ckdone		  ; ... no, it is ST
	bset	#rtc..ars,rtc_rest	 ; reset alarm
	and.b	rtc_mino,d0		 ; back to zero?
	bne.s	atp_ckdone		  ; ... no, it is ST
	moveq	#sys.mstr,d2		 ; it is ST with RTC

atp_ckdone
	bclr	#rtc..bk1,rtc_mode	 ; back to normal

atp_mdone

; now check for blitter

	cmp.b	#$30,d6 		 ; 68030 or better?
	bhs.s	atp_bdone		 ; ... yes, do not check for blitter
	cmp.b	#sys.mste,d2		 ; STE or better?
	bhs.s	atp_blit		 ; ... yes, blitter

	lea	at_blit,a2
	bsr.s	atp_buse		 ; bus error on word access?
	bne.s	atp_bdone		 ; ... yes, no blitter
atp_blit
	addq.w	 #1,d2			 ; blitter

atp_bdone
	ror.w	#8,d6
	swap	d6			 ; processor in MSB, copro in MSW
	move.w	d2,d6			 ; blitter, machine in LSW

	moveq	#0,d0
	movem.l (sp)+,atp.reg
	rts

atp_buse
	jmp	gu_rbuse

atp_vbase
	screg	vbr,#0			 ; set vector base to zero
	rts

atp_odda
	move.w	*-1(pc),d0		 ; access word at odd address
	rts

atp_itt0
	gcreg	itt0			 ; get inst transparent translate reg
	rts

atp_pmove
	dc.w	$f000,$5200		 ; PMOVE CAL,D0 (not possible on 68030)
	rts

atp_mmutc
	gcreg	tc			 ; get translate control
	rts

atp_fpu
	dc.w	$f200,$a800		 ; get FP status register
	rts

	end
