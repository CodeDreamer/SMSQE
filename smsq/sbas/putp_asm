; Put parameters - QL / SBASIC Compatible   V2.00    1994  Tony Tebby

	section uq

	xdef	sb_putp

	xref	sb_aldat
	xref	sb_redat
	xref	sb_aldatl
	xref	sb_redatl

;	 xref	 mem_alhp
;	 xref	 mem_rehp

	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_keys_sbasic'
	include 'dev8_mac_assert'

;+++
; Put Parameter
;
;	d1/d2/d3/a0/a1/a2 smashed
;	a1  r	arith stack pointer after item removed
;	a3 c  p pointer to parameter
;
;	status return standard
;---
sb_putp
	move.l	sb_arthp(a6),a1
	add.l	a6,a1
	move.l	nt_value(a6,a3.l),a0	 ; existing allocation
	move.l	sb_datab(a6),a2
	add.l	a6,a2			 ; offsets for addresses
	add.l	a2,a0

	move.b	nt_nvalp(a6,a3.l),d1	 ; usage
	move.w	#1<<nt.var+1<<nt.rep+1<<nt.for,d0
	btst	d1,d0
	bne.s	spp_type		 ; ... ok, it's a variable
	subq.b	#nt.arr,d1		 ; array
	beq.s	spp_array		 ; ... yes
	addq.b	#nt.arr,d1		 ; unset?
	beq.s	spp_var 		 ; ... yes, set to var
	bra.s	spp_basg		 ; ... no

spp_array
	add.l	(a0)+,a2		 ; pointer to value
	cmp.w	#1,(a0)+		 ; one dim?
	bne.s	spp_basg		 ; ... no

	moveq	#$f,d0
	and.b	nt_vtype(a6,a3.l),d0
	subq.b	#nt.st,d0		 ; string?
	beq.l	spp_stra		 ; ... yes, assign to it
	blt.l	spp_sstra		 ; ... substring is even easier
spp_basg
	move.l	#err4.basg,d0
	rts

spp_var
	tst.w	nt_usetp(a6,a3.l)	 ; is it null?
	beq.s	spp_done		 ; ... yes, do not assign
	move.b	#nt.var,nt_nvalp(a6,a3.l) ; it is a variable now
spp_type
	moveq	#$f,d0
	and.b	nt_vtype(a6,a3.l),d0	 ; variable type
	subq.w	#nt.fp,d0		 ; most common type of variable
	blt.s	spp_str
	bgt.s	spp_in
spp_fp
	move.l	(a1)+,(a0)+
spp_in
	move.w	(a1)+,(a0)
spp_done
	sub.l	a6,a1
	move.l	a1,sb_arthp(a6) 	 ; reset arith stack
	moveq	#0,d0
	rts

spps_newall
	move.l	a2,-(sp)		 ; save offset on allocate
	bra.s	spps_alloc

spp_str
	move.w	(a1)+,d2		 ; length of string
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	beq.s	spps_cksb		  ; ... yes

; OLD style SuperBASIC for QLIBERATOR

	moveq	#2+7,d1 		 ; ... no, allocation is just string
	add.w	d2,d1
	and.w	#$fff8,d1		 ; new string allocation
	move.l	a0,d0			 ; any old allocation?
	bmi.s	spps_newall		 ; ... no

	moveq	#2+7,d0
	add.w	(a0),d0
	and.w	#$fff8,d0		 ; old string allocation
	cmp.w	d0,d1
	beq.s	spps_set		 ; ... it's OK

	move.l	a2,-(sp)
	move.w	d1,-(sp)
	move.l	d0,d1
	jsr	sb_redatl		 ; release allocation
	move.w	(sp)+,d1

spps_alloc
	jsr	sb_aldatl		 ; allocate

	move.l	a0,d0			 ; new allocation
	sub.l	(sp)+,d0
	move.l	d0,nt_value(a6,a3.l)
	bra.s	spps_set

;	 moveq	 #2+7,d1		  ; ... no, allocation is just string
;	 add.w	 d2,d1
;	 and.w	 #$fff8,d1		  ; new string allocation
;	 move.l  a0,d0			  ; any old allocation?
;	 bmi.s	 spps_alloc		  ; ... no

;	 moveq	 #2+7,d0
;	 add.w	 (a0),d0
;	 and.w	 #$fff8,d0		  ; old string allocation
;	 cmp.w	 d0,d1
;	 beq.s	 spps_set		  ; ... it's OK

;	 move.l  a1,-(sp)
;	 move.l  d1,-(sp)
;	 move.l  d0,d1
;	 lea	 sb_frdat(a6),a1
;	 jsr	 mem_rehp		  ; return heap
;	 move.l  (sp)+,d1
;	 move.l  (sp)+,a1
;
;spps_alloc
;	 lea	 sb_frdat(a6),a0	  ; free space link
;	 jsr	 mem_alhp		  ; allocate
;
;	 move.l  a0,d0			  ; new allocation
;	 sub.l	 a2,d0
;	 move.l  d0,nt_value(a6,a3.l)
;	 bra.s	 spps_set

spps_cksb
	moveq	#dt_stchr-dt_stalc+7,d1  ; add 6 bytes and round up
	add.w	d2,d1
	and.w	#$fff8,d1		 ; to multiple of 8
	cmp.w	dt_stalc(a0),d1 	 ; variable allocation, enough?
	bls.s	spps_set

	move.l	a0,d3			 ; save old base of allocation

	jsr	sb_aldat		 ; allocate hole

	assert	dt_stalc+4,dt_flstr+1,dt_stlen
	move.w	d1,(a0)+		 ; allocation
	move.w	#$00ff,(a0)+		 ; flags
	move.l	a0,nt_value(a6,a3.l)	 ; where the item is

	exg	a0,d3
	subq.l	#-dt_stalc,a0
	move.w	(a0),d1
	jsr	sb_redat		 ; release old allocation
	move.l	d3,a0

spps_set
	move.w	d2,(a0)+		 ; length
	beq.s	spss_done
spps_copy
	move.w	(a1)+,(a0)+		 ; copy bytes
	subq.w	#2,d2			 ; two at a time
	bgt.s	spps_copy
spss_done
	bra	spp_done

spp_stra
	exg	a0,a2			 ; a0 is destination , a2 descriptor
	moveq	#0,d2
	move.w	(a1)+,d2		 ; length of string
	move.w	(a2)+,d1		 ; the last dimension
	cmp.w	d1,d2			 ; truncate?
	bls.s	spsa_set		 ; ... no

	addq.w	#1,d2			 ; round up
	bclr	#0,d2
	add.l	a1,d2			 ; for reset stack

	move.w	d1,(a0)+		 ; length
	beq.s	spsa_done
spsa_tcopy
	move.w	(a1)+,(a0)+		 ; copy bytes
	subq.w	#2,d1			 ; two at a time
	bgt.s	spsa_tcopy
spsa_done
	move.l	d2,a1			 ; and set stack
	bra	spp_done

spsa_set
	sub.w	d2,d1			 ; trailing spaces
	move.w	#'  ',d0		 ; space chars
	move.w	d2,(a0)+		 ; length
	addq.w	#2,d2
	bra.s	spsa_ecopy
spsa_copy
	move.w	(a1)+,(a0)+		 ; copy bytes
spsa_ecopy
	subq.w	#2,d2			 ; two at a time
	bgt.s	spsa_copy
	beq.s	spsa_efill		 ; fill
	move.b	d0,-1(a0)		 ; blat the last char
	bra.s	spsa_efill
spsa_fill
	move.w	d0,(a0)+		 ; and fill with spaces
spsa_efill
	subq.w	#2,d1
	bge.s	spsa_fill
	bra	spp_done



spp_sstra
	moveq	#0,d2
	move.w	(a1)+,d2		 ; length of string to assign
	move.w	(a0),d1 		 ; length of substring
	move.l	a1,a0			 ; copy from here
	moveq	#1,d0
	and.w	d2,d0
	add.w	d2,d0			 ; rounded up
	add.l	d0,a1			 ; stack pointer

	sub.w	d1,d2			 ; long enough?
	bge.s	spss_cple		 ; ... yes
	add.w	d2,d1			 ; bytes of ss to copy
	bra.s	spss_cple

spss_copy
	move.b	(a0)+,(a2)+		 ; copy bytes
spss_cple
	dbra	d1,spss_copy

	neg.w	d2			 ; any fill in?
	ble	spp_done		 ; ... no

spss_fill
	move.b	#' ',(a2)+		 ; pad with spaces at end
	subq.w	#1,d2
	bgt.s	spss_fill
	bra	spp_done


	end
