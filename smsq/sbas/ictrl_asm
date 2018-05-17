; SBAS_ICTRL - General Control Structure Interpretation    1994 Tony Tebby
;
; 2006-03-25  1.01  Fixed crashes in SELect statements with several ranges (MK)

	section sbas

	xdef	bo_mistake
	xdef	bo_stop     ; stop

	xdef	bo_skip6
	xdef	bo_rts
	xdef	bo_rts2
	xdef	bo_rts4
	xdef	bo_scrub8
	xdef	bo_gorel
	xdef	bo_ambs

	xdef	bo_goadd	; GOTOs
	xdef	bo_ogoadd
	xdef	bo_goline
	xdef	bo_ogoline
	xdef	bo_golinec
	xdef	bo_ogolinec
	xdef	bo_gsadd
	xdef	bo_ogsadd
	xdef	bo_gsline
	xdef	bo_ogsline
	xdef	bo_gslinec
	xdef	bo_ogslinec

	xdef	bo_goz
	xdef	bo_gonz

	xdef	bo_selfp	 ; SELect
	xdef	bo_selint
	xdef	bo_selend
	xdef	bo_on
	xdef	bo_onxpr
	xdef	bo_ofpr
	xdef	bo_ointr

	xdef	bo_wherr
	xdef	bo_endwh
	xdef	bo_nowherr

	xdef	sb_fint

	xref	bo_pcall

	xref	sb_ibreak
	xref	sb_iloop
	xref	sb_istop
	xref	sb_ierror
	xref	sb_ierset
	xref	sb_istopok
	xref	sb_rrets
	xref	sb_stint
	xref	sb_anyfp2
	xref	sb_sttadd

	xref	qa_nint

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_err4'
	include 'dev8_smsq_sbas_inter_keys'
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

;--------------------------------------------- end of program
bo_stop
	tst.b	sb_cont(a6)		 ; already stoppped?
	beq.s	bostop_ok
	clr.b	sb_cont(a6)		 ; do not continue
	move.w	#sb.stop,sb_actn(a6)	 ; stop
	st	sb_nline(a6)		 ; really stop
bostop_ok
	jmp	sb_istopok

;---------------------
bo_mistake
	moveq	#ern4.mist,d0
	jmp	sb_ierset

;--------------------------------------------- odd control
bo_skip6
	addq.l	#6,a4			 ; skip six bytes
	jmp	(a5)
bo_rts4
bo_rts2
bo_rts
	rts				 ; RTS!!!!!

bo_scrub8
	addq.l	#8,a4			 ; skip eight bytes
	jmp	(a5)
bo_gorel
	add.w	(a4),a4 		 ; relative jump
	jmp	(a5)


bo_ambs
	move.w	(a4)+,d3		 ; name index
	moveq	#-nt.sbprc,d0
	add.b	nt_nvalp(a3,d3.w),d0	 ; sb proc?
	beq.s	bam_proc		 ; ... yes
	subq.b	#nt.mcprc-nt.sbprc,d0	 ; mc proc?
	bne.s	bo_gorel		 ; ... no, skip call
bam_proc
	addq.l	#4,a4
	jmp	bo_pcall		 ; do generic procedure call



;--------------------------------------------- GOTOs
bo_gsadd
	lea	4(a4),a0		 ; return address
	bsr.s	bgs_setup
bo_goadd
	move.l	(a4),a4 		 ; absolute jump
	jmp	sb_ibreak		 ; and check break

bo_ogsadd
	move.l	(a4)+,a0		 ; return address
	bsr.s	bgs_setup
bo_ogoadd
	bsr.s	bog_int 		 ; get integer
	ble.s	bog_or
	cmp.w	(a4)+,d0
	bgt.s	bog_or			 ; too large
	lsl.w	#3,d0
	lea	-4(a4,d0.w),a0
	move.l	(a0)+,a4
	jmp	sb_ibreak		 ; and check break

bo_gsline
	move.l	a4,a0			 ; return address
	bsr.s	bgs_setup
bo_goline
	bsr.s	bog_int 		 ; get integer
	swap	d0
	clr.w	d0
	jsr	sb_sttadd		 ; statement to address
	move.l	d0,a4
	jmp	sb_ibreak		 ; and check break

bgs_setup
	moveq	#rt.gssize,d0
	move.l	sb_retsp(a6),a2 	 ; return stack
	add.l	a2,d0
	cmp.l	sb_retst(a6),d0 	 ; full?
	blt.s	bgs_srets		 ; no
	bsr.l	sb_rrets		 ; ... yes
bgs_srets
	add.l	a6,a2
	assert	rt.gosub,0
	moveq	#0,d0
	move.l	d0,(a2)+		 ; key, line and statement
	move.l	a0,(a2)+		 ; return address
	sub.l	a6,a2
	move.l	a2,sb_retsp(a6) 	 ; save stack pointer
	rts

bog_or
	moveq	#err.orng,d0
	jmp	sb_istop		 ; bad!!


bog_int
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bog_pint
	bsr.s	sb_fint
	bne.s	bogi_erxp
bog_pint
	move.w	(a1)+,d0
	rts

bogi_erxp
	moveq	#err.iexp,d0
	jmp	sb_istop

;+++
; This routine is entered after a   CMP.W #AR.INT,(A1)+ (cc NZ)
; to get an integer on the stack. It returns status standard.
;---
sb_fint
	blt.s	bogi_nint		 ; not a long int
	move.l	(a1)+,d0
	move.w	d0,a0
	cmp.l	d0,a0			 ; sign extendable?
	bne.s	bogi_erxp
	rts


bogi_nint
	cmp.w	#ar.strng,-2(a1)	 ; was it a string
	ble.l	sb_stint		 ; ... yes
	jsr	qa_nint 		 ; ... no, nearest integer
	bne.s	bogi_erxp
	rts

bo_ogsline
bo_ogslinec
	move.l	(a4)+,a0		 ; return address
	bsr.s	bgs_setup
bo_ogoline
bo_ogolinec
	bsr.s	bog_int 		 ; get integer
	ble.s	bog_or
	cmp.w	(a4)+,d0
	bgt.s	bog_or			 ; too large

	subq.w	#1,d0
	bra.s	bog_slend
bog_sloop
	addq.w	#2,a4
	add.w	(a4),a4 		 ; skip inmtermediate gotos
bog_slend
	dbra	d0,bog_sloop

	addq.l	#4,a4			 ; skip last skip
	jmp	(a5)

bo_gslinec
	move.w	#-1,a0			 ; return line (-ve)
	bsr.s	bgs_setup
bo_golinec
	bsr.s	bog_int 		 ; get integer
	blt.s	bog_or
	move.w	d0,sb_cline(a6)
	clr.b	sb_cstmt(a6)
	sf	sb_cont(a6)
	move.w	#sb.cont,sb_actn(a6)	 ; continue from here
	jmp	sb_istopok

;--------------------------------------- conditionals
bo_gonz
	move.l	(a4)+,d4		 ; where to go
	lea	sb_ibreak,a5		 ; if true, test break
	bra.s	bogc_do
bo_goz
	move.l	(a4)+,d4		 ; where to go
	exg	a4,d4			 ; the other way round
bogc_do
	move.w	(a1)+,d0		 ; type of expression
	subq.w	#ar.float,d0		 ; most common expression
	beq.s	bogc_fp
	bgt.s	bogc_in

	jsr	sb_anyfp2
bogc_fp
	addq.l	#2,a1
	tst.l	(a1)+			 ; zero mantissa?
	beq.l	sb_iloop		 ; ... yes, carry on
	move.l	d4,a4
	jmp	(a5)

bogc_in
	tst.w	(a1)+			 ; zero integer?
	beq.l	sb_iloop		 ; ... yes, carry on
	move.l	d4,a4
	jmp	(a5)

;---------------------------------------- select bits
bo_selfp
	cmp.w	#ar.float,(a1)+ 	 ; ensure float
	beq.s	bsf_on			 ; and do on
	jsr	sb_anyfp2		 ; convert to float
bsf_on
	tst.w	2(a1)			 ; positive or negative value
	bpl.s	bsp_on			 ; positive
	bra.l	bsn_on

bsp_onnxt
	move.l	a2,a4			 ; pointer to next on
bsp_on
	cmp.w	#bo.on,(a4)+		 ; is next ON constant, expr or end?
	beq.s	bsp_onc 		 ; ON constant ranges
	bgt.s	bsp_onxpr		 ; ON expression ranges
	addq.l	#6,a1			 ; clean arith stack
bo_selend
	jmp	(a5)


bsp_onc
	move.l	(a4)+,a2		 ; next ON
	move.l	a4,a0			 ; pointer to ranges
	add.w	(a0)+,a4		 ; this selection
	move.w	(a0),d4 		 ; number of ranges
	beq.s	bsf_end 		 ; ... none, it is remainder, continue
	move.w	(a1),d2 		 ; comparison value
	move.l	2(a1),d1
	addq.l	#4,a0
bsp_ckc
	move.w	(a0)+,d0		 ; exponent to check
	move.l	(a0)+,d3		 ; and mantissa
	blt.s	bsp_ckch		 ; negative, must be in range
	cmp.w	d0,d2			 ; exponent below range?
	blt.s	bsp_ckc8		 ; ... yes
	bgt.s	bsp_ckch		 ; ... in range
	cmp.l	d3,d1			 ; mantissa below range?
	blt.s	bsp_ckc8		 ; ... yes
bsp_ckch
	move.w	(a0)+,d0		 ; exponent to check
	move.l	(a0)+,d3		 ; and mantissa
	blt.s	bsp_ckc2		 ; negative, must be out of range
	cmp.w	d0,d2			 ; exponent above range?
	bgt.s	bsp_ckc2		 ; ... yes
	blt.s	bsf_end 		 ; ... in range
	cmp.l	d3,d1			 ; mantissa in range?
	ble.s	bsf_end 		 ; ... yes, we've got it
bsp_ckc2
	addq.l	#2,a0
	subq.w	#1,d4			 ; another range?
	bgt.s	bsp_ckc 		 ; ... yes
	bra.s	bsp_onnxt		 ; ... no, next on
bsp_ckc8
	addq.l	#8,a0
	subq.w	#1,d4			 ; another range?
	bgt.s	bsp_ckc 		 ; ... yes
	bra.s	bsp_onnxt		 ; ... no, next on

bsf_xend
	addq.l	#2,sp			 ; skip count
	move.l	(sp)+,a4		 ; where to go
	addq.l	#4,sp			 ; skip next ON address
bsf_end
	addq.l	#6,a1			 ; clean up stack
	jmp	(a5)

bsp_onxpr
	move.l	(a4)+,-(sp)		 ; next ON
	move.l	a4,a0			 ; pointer to ranges
	add.w	(a4)+,a0		 ; this selection
	move.l	a0,-(sp)						    ü
	move.w	(a4)+,-(sp)		 ; number of ranges
bsp_ckx
	jsr	(a5)			 ; evaluate expression
	cmp.w	#ar.float,(a1)+ 	 ; is it a float already?
	beq.s	bsp_ckl 		 ; ... yes
	jsr	sb_anyfp2
bsp_ckl
	move.w	(a4)+,d0		 ; offset to next range
	bne.s	bsp_range		 ; ... there is one
	move.w	(a1)+,d2		 ; exponent to check
	move.l	(a1)+,d1		 ; and mantissa
	blt.s	bsp_ckxe		 ; negative, no match
	move.l	2(a1),d3
	sub.w	(a1),d2 		 ; exponent difference
	blt.s	bsp_vxn 		 ; negative
	beq.s	bsp_vxeq
	subq.w	#1,d2			 ; only one different?
	bne.s	bsp_ckxe		 ; ... no
	asr.l	#1,d3			 ; ... yes, adjust
	bra.s	bsp_vxeq
bsp_vxn
	addq.w	#1,d2			 ; only one difference
	bne.s	bsp_ckxe		 ; ... no
	asr.l	#1,d1			 ; ... yes, adjust
bsp_vxeq
	sub.l	d3,d1			 ; difference
	bpl.s	bsp_vmchk		 ; positive
	neg.l	d1
bsp_vmchk
	cmp.l	#$000000c0,d1		 ; 2^-23 difference?
	blt.s	bsf_xend
	bra.s	bsp_ckxe

bsp_range
	move.w	(a1)+,d2		 ; exponent to check
	move.l	(a1)+,d1		 ; and mantissa
	blt.s	bsp_ckxh		 ; negative, must be in range
	cmp.w	(a1),d2 		 ; exponent below range?
	bgt.s	bsp_ckxo		 ; ... yes
	blt.s	bsp_ckxh		 ; ... in range
	cmp.l	2(a1),d1		 ; mantissa below range?
	bgt.s	bsp_ckxo		 ; ... yes

bsp_ckxh
	jsr	(a5)			 ; evaluate expression
	cmp.w	#ar.float,(a1)+ 	 ; is it a float already?
	beq.s	bsp_ckh 		 ; ... yes
	jsr	sb_anyfp2
bsp_ckh
	move.w	(a1)+,d2		 ; exponent to check
	move.l	(a1)+,d1		 ; and mantissa
	blt.s	bsp_ckxe		 ; negative, must be out of range ** 1.01
	cmp.w	(a1),d2 		 ; exponent above range?
	blt.s	bsp_ckxe		 ; ... yes  ** 1.01
	bgt.s	bsf_xend		 ; ... in range
	cmp.l	2(a1),d1		 ; mantissa in range?
	blt.s	bsp_ckxe		 ; ... no
	bra.l	bsf_xend		 ; ... yes, we've got it

bsp_ckxo
	add.w	d0,a4			 ; skip TO check
bsp_ckxe
	subq.w	#1,(sp) 		 ; another range?
	bgt.s	bsp_ckx 		 ; ... yes

	addq.l	#6,sp			 ; ... no, forget this selection
	move.l	(sp)+,a4		 ; and try next
	bra.l	bsp_on			 ; next on

bsn_onnxt
	move.l	a2,a4			 ; pointer to next on
bsn_on
	cmp.w	#bo.on,(a4)+		 ; is next ON constant, expr or end?
	beq.s	bsn_onc 		 ; ON constant ranges
	bgt.s	bsn_onxpr		 ; ON expression ranges
	addq.l	#6,a1			 ; clean stack
	jmp	(a5)


bsn_onc
	move.l	(a4)+,a2		 ; next ON
	move.l	a4,a0			 ; pointer to ranges
	add.w	(a0)+,a4		 ; this selection
	move.w	(a0),d4 		 ; number of ranges
	beq.l	bsf_end 		 ; ... none, it is remainder, continue
	move.w	(a1),d2 		 ; comparison value
	move.l	2(a1),d1
	addq.l	#4,a0
bsn_ckc
	move.w	(a0)+,d0		 ; exponent to check
	move.l	(a0)+,d3		 ; and mantissa
	bge.s	bsn_ckc8		 ; positive, must be out of range
	cmp.w	d0,d2			 ; exponent below range?
	bgt.s	bsn_ckc8		 ; ... yes
	blt.s	bsn_ckch		 ; ... in range
	cmp.l	d3,d1			 ; mantissa below range?
	blt.s	bsn_ckc8		 ; ... yes
bsn_ckch
	move.w	(a0)+,d0		 ; exponent to check
	move.l	(a0)+,d3		 ; and mantissa
	bge.l	bsf_end 		 ; positive, must be in range
	cmp.w	d0,d2			 ; exponent above range?
	blt.s	bsn_ckc2		 ; ... yes
	bgt.l	bsf_end 		 ; ... in range
	cmp.l	d3,d1			 ; mantissa in range?
	ble.l	bsf_end 		 ; ... yes, we've got it
bsn_ckc2
	addq.l	#2,a0
	subq.w	#1,d4			 ; another range?
	bgt.s	bsn_ckc 		 ; ... yes
	bra.s	bsn_onnxt		 ; ... no, next on
bsn_ckc8
	addq.l	#8,a0
	subq.w	#1,d4			 ; another range?
	bgt.s	bsn_ckc 		 ; ... yes
	bra.s	bsn_onnxt		 ; ... no, next on

bsn_onxpr
	move.l	(a4)+,-(sp)		 ; next ON
	move.l	a4,a0			 ; pointer to ranges
	add.w	(a4)+,a0		 ; this selection
	move.l	a0,-(sp)
	move.w	(a4)+,-(sp)		 ; number of ranges
bsn_ckx
	jsr	(a5)			 ; evaluate expression
	cmp.w	#ar.float,(a1)+ 	 ; is it a float already?
	beq.s	bsn_ckl 		 ; ... yes
	jsr	sb_anyfp2
bsn_ckl
	move.w	(a4)+,d0		 ; offset to next range
	bne.s	bsn_range		 ; ... there is one
	move.w	(a1)+,d2		 ; exponent to check
	move.l	(a1)+,d1		 ; and mantissa
	bge.s	bsn_ckxe		 ; positive, no match
	move.l	2(a1),d3
	sub.w	(a1),d2 		 ; exponent difference
	blt.s	bsn_vxn 		 ; negative
	beq.s	bsn_vxeq
	subq.w	#1,d2			 ; only one different?
	bne.s	bsn_ckxe		 ; ... no
	asr.l	#1,d3			 ; ... yes, adjust
	bra.s	bsn_vxeq
bsn_vxn
	addq.w	#1,d2			 ; only one difference
	bne.s	bsn_ckxe		 ; ... no
	asr.l	#1,d1			 ; ... yes, adjust
bsn_vxeq
	sub.l	d3,d1			 ; difference
	bpl.s	bsn_vmchk		 ; positive
	neg.l	d1
bsn_vmchk
	cmp.l	#$000000c0,d1		 ; 2^-23 difference?
	bgt.s	bsn_ckxe
	bra.l	bsf_xend

bsn_range
	move.w	(a1)+,d2		 ; exponent to check
	move.l	(a1)+,d1		 ; and mantissa
	bge.s	bsn_ckxo		 ; positive, must be out of range
	cmp.w	(a1),d2 		 ; exponent below range?
	blt.s	bsn_ckxo		 ; ... yes
	bgt.s	bsn_ckxh		 ; ... in range
	cmp.l	2(a1),d1		 ; mantissa below range?
	bgt.s	bsn_ckxo		 ; ... yes

bsn_ckxh
	jsr	(a5)			 ; evaluate expression
	cmp.w	#ar.float,(a1)+ 	 ; is it a float already?
	beq.s	bsn_ckh 		 ; ... yes
	jsr	sb_anyfp2
bsn_ckh
	move.w	(a1)+,d2		 ; exponent to check
	move.l	(a1)+,d1		 ; and mantissa
	bge.l	bsf_xend		 ; positive, must be in range
	cmp.w	(a1),d2 		 ; exponent above range?
	bgt.s	bsn_ckxe		 ; ... yes **1.01
	blt.l	bsf_xend		 ; ... in range
	cmp.l	2(a1),d1		 ; mantissa in range?
	blt.s	bsn_ckxe		 ; ... no
	bra.l	bsf_xend		 ; ... yes, we've got it

bsn_ckxo
	add.w	d0,a4			 ; skip TO check
bsn_ckxe
	subq.w	#1,(sp) 		 ; another range?
	bgt.s	bsn_ckx 		 ; ... yes

	addq.l	#6,sp			 ; ... no, forget this selection
	move.l	(sp)+,a4		 ; and try next
	bra.l	bsn_on			 ; next on

;-------------------------------------------------------
bo_selint
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bsi_on			 ; ... yes
	bsr	sb_fint
	bra.s	bsi_on

bsi_onnxt
	move.l	a2,a4			 ; pointer to next on
bsi_on
	cmp.w	#bo.on,(a4)+		 ; is next ON constant, expr or end?
	beq.s	bsi_onc 		 ; ON constant ranges
	bgt.s	bsi_onxpr		 ; ON expression ranges
	addq.l	#2,a1			 ; clean stack
	jmp	(a5)			 ; ... done

bsi_onc
	move.l	(a4)+,a2		 ; next ON
	move.l	a4,a0			 ; pointer to ranges
	add.w	(a0)+,a4		 ; this selection
	move.w	(a0),d4 		 ; number of ranges
	beq.s	bsi_end 		 ; ... none, it is remainder, continue
	move.w	(a1),d1 		 ; comparison value
bsi_ckc
	addq.l	#4,a0
	cmp.w	(a0)+,d1		 ; below range?
	blt.s	bsi_ckce		 ; ... yes
	cmp.w	(a0),d1 		 ; in range?
	ble.s	bsi_end 		 ; ... yes, we've got it
bsi_ckce
	subq.w	#1,d4			 ; another range?
	bgt.s	bsi_ckc 		 ; ... yes

	bra.s	bsi_onnxt		 ; ... no, next on

bsi_xend
	addq.l	#2,sp			 ; skip count
	move.l	(sp)+,a4		 ; where to go
	addq.l	#4,sp			 ; skip next ON address
bsi_end
	addq.l	#2,a1			 ; clean up stack
	jmp	(a5)

bsi_onxpr
	move.l	(a4)+,-(sp)		 ; next ON
	move.l	a4,a0			 ; pointer to ranges
	add.w	(a4)+,a0		 ; this selection
	move.l	a0,-(sp)
	move.w	(a4)+,-(sp)		 ; number of ranges
bsi_ckx
	jsr	(a5)			 ; evaluate expression
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bsi_ckl 		 ; ... yes
	bsr	sb_fint
bsi_ckl
	move.w	(a1)+,d1
	move.w	(a4)+,d0		 ; offset to next range
	bne.s	bsi_range		 ; ... there is one
	cmp.w	(a1),d1 		 ; value matches?
	beq.s	bsi_xend
	bra.s	bsi_ckxe

bsi_range
	cmp.w	(a1),d1 		 ; below range?
	bgt.s	bsi_ckxo		 ; ... yes
	jsr	(a5)			 ; evaluate expression
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bsi_ckh 		 ; ... yes
	bsr	sb_fint
bsi_ckh
	move.w	(a1)+,d1
	cmp.w	(a1),d1 		 ; in range?
	blt.s	bsi_ckxe		 ; ...no
	bra.s	bsi_xend		 ; ... yes, we've got it

bsi_ckxo
	add.w	d0,a4			 ; skip TO check
bsi_ckxe
	subq.w	#1,(sp) 		 ; another range?
	bgt.s	bsi_ckx 		 ; ... yes

	addq.l	#6,sp			 ; ... no, forget this selection
	move.l	(sp)+,a4		 ; and try next
	bra.s	bsi_on			 ; next on

bo_on
bo_onxpr
bo_ofpr
bo_ointr
	jmp	sb_ierror



; WHEN

bo_wherr
	move.w	(a4)+,sb_wherr(a6)	 ; set when error
	jmp	(a5)

bo_endwh
	clr.b	sb_cont(a6)		 ; do not continue
	move.w	#sb.cont,sb_actn(a6)	 ; do continue!!
	jmp	sb_istopok

bo_nowherr
	clr.w	sb_wherr(a6)		 ; no when error now
	jmp	(a5)
	end
