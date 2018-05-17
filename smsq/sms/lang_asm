; Language Handling Routines  V2.00    1986  Tony Tebby

	section sms

	xdef	sms_trns
	xdef	sms_lldm
	xdef	sms_lenq
	xdef	sms_lset
	xdef	sms_pset
	xdef	sms_mptr
	xdef	sms_fprm

	xref	sms_pref

	xref	sms_rte
	xref	sms_rtok
	xref	sms_ikey

	include 'dev8_keys_sys'
	include 'dev8_keys_ldm'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'

;-------------------------------------------------------------
sms_lldm
sll.reg reg	d1/d2/d3/a0/a1/a2/a3/a4
	movem.l sll.reg,-(sp)
	move.l	sys_ldmlst(a6),a5	 ; language dependent module list
	move.l	ldl_endl(a5),a4

	move.l	a1,a3			 ; a safe register

sll_loop
	bsr.s	sll_check		 ; check if room
	bne.s	sll_exit

	tst.w	ldm_type(a3)		 ; preference?
	bne.s	sll_adde		 ; ... no, add to end

	move.l	a4,a1
	addq.l	#4,a4			 ; new end
	move.l	a4,a0

	bra.s	sll_shifte
sll_shift
	move.l	-(a1),-(a0)		 ; copy up
sll_shifte
	cmp.l	a5,a1			 ; until all done
	bgt.s	sll_shift

	move.l	a3,(a1) 		 ; new module at beginning
	bra.s	sll_eloop		 ; and check

sll_adde
	move.l	a3,(a4)+

sll_eloop
	move.l	a4,ldl_endl(a5) 	 ; save new end
	addq.l	#ldm_next,a3
	moveq	#0,d0
	move.w	(a3),d0 		 ; next module to link
	add.l	d0,a3
	bne.s	sll_loop		 ; take next

sll_exit
	movem.l (sp)+,sll.reg
	bra.l	sms_rte


sll_check
	cmp.l	ldl_enda(a5),a4 	 ; end of allocation?
	blt.s	sll_ok
	moveq	#$40+ldl.hdr,d1 	 ; increase by this much (inc header)
	add.l	a4,d1
	sub.l	a5,d1
	moveq	#0,d2
	moveq	#sms.achp,d0		 ; allocate
	trap	#do.sms2
	tst.l	d0
	bne.s	sll_rts 		 ; ... oops
	lea	-$14(a0,d1.l),a1	 ; new end of allocation (one spare)
	move.l	a1,(a0)+
	sub.l	a5,a4			 ; size of list
	addq.l	#4,a4
	add.l	a0,a4			 ; new end of list
	move.l	a4,(a0)+

	move.l	a5,a1			 ; old list
	move.l	a5,d0
	move.l	a0,a5
	move.l	a5,sys_ldmlst(a6)	 ; new list

sll_copy
	move.l	(a1)+,(a0)+		 ; copy list
	cmp.l	a4,a0
	blo.s	sll_copy

	move.l	d0,a0
	subq.l	#ldl.hdr,a0		 ; base of allocation
	moveq	#sms.rchp,d0		 ; returned
	trap	#1
sll_ok
	moveq	#0,d0
sll_rts
	rts


;------------------------------------------------------------------------------
sms_mptr
	move.l	a1,d0
	bge.s	smp_null		 ; no message
smp_mset
	move.w	d0,a1			 ; sign extendable?
	cmp.l	a1,d0
	bne.s	smp_mess		 ; ... no, it must be a special
	neg.w	d0			 ; make positive
	add.w	d0,d0
	clr.b	d0			 ; message group in msb of lsw
	lsr.w	#6,d0			 ; message group
	move.l	sys_mstab(a6),a5	 ; message table
	add.w	d0,a5			 ; message table
	move.l	(a5),d0 		 ; any table?
	beq.s	smp_nomstb		 ; ... no

smp_fmsg
	move.l	d0,a5
	move.w	a1,d0
	neg.w	d0
	add.w	d0,d0
	ext.w	d0			 ; message number
	beq.s	smp_nomsg
	cmp.w	2(a5),d0		 ; number out of range
	bhs.s	smp_nomsg
	move.w	(a5,d0.w),d0
	lea	(a5,d0.w),a1		 ; the message
	jmp	sms_rtok

smp_mess
	bclr	#31,d0
	move.l	d0,a1			 ; set message
	jmp	sms_rtok

smp_null
	lea	null,a1
	jmp	sms_rtok
null	dc.w	0

smp_nomstb
	movem.l a5,-(sp)
	bsr.l	sln_prefx		 ; no message table, find the pref
	bsr.s	sls_setl		 ; try setting the table
	move.l	(sp)+,a5
	move.l	(a5),d0 		 ; now set?
	bne.s	smp_fmsg		 ; ... yes

smp_nomsg
	moveq	#err.noms,d0		 ; unknown error message
	cmp.w	a1,d0			 ; was it already?
	bne.s	smp_mset		 ; ... no
	lea	blat,a1
	jmp	sms_rtok
blat	dc.b	0,4,'*?!',$a

;------------------------------------------------------------------------------
sms_lset
	tst.l	d2			 ; d1 given?
	beq.s	sls_do			 ; ... no
	move.l	d2,d0
	rol.l	#8,d0
	move.b	d0,sys_vers+1(a6)	 ; replace the dot
sls_do
	bsr.s	slq_do			 ; find preference
	move.w	d1,sys_lang(a6) 	 ; set language
	pea	sms_rtok

; we now have (a5) as the preferred language

sls_setl
sls.reg reg	d1/d2/d3/a0/a1/a2/a3
	movem.l sls.reg,-(sp)
	move.l	sys_mstab(a6),a0	 ; the message table

	moveq	#0,d2			 ; we need msw d2 clear later!!
	move.l	a0,a1
sls_clear
	clr.l	(a1)+
	subq.b	#1,d2
	bne.s	sls_clear		 ; clear all of table

	move.l	sys_ldmlst(a6),a1	 ; the language dependent module list
sls_ldmloop
	move.l	(a1)+,d0		 ; the next module
	ble.s	sls_done
	move.l	d0,a2
	assert	0,ldm_type,ldm_group-2,ldm_lang-4
	cmp.w	#ldm.msgt,(a2)+ 	 ; message texts?
	bne.s	sls_ldmloop
	move.w	(a2)+,d2		 ; this group
	move.w	(a2),d1 		 ; this language
	move.l	(a0,d2.l),d0		 ; the previously accepted message
	beq.s	sls_ldmset		 ; none, accept this one

	move.l	d0,a3
	move.w	(a3),d3 		 ; previously accepted language

	move.l	a5,a3			 ; preferences
sls_pref
	move.w	(a3)+,d0		 ; another preference?
	beq.s	sls_ldmloop		 ; ... no
	cmp.w	d0,d3			 ; already preferred?
	beq.s	sls_ldmloop		 ; ... yes
	cmp.w	d0,d1			 ; new one preferred?
	bne.s	sls_ldmloop		 ; ... no

sls_ldmset
	addq.l	#ldm_module-ldm_lang,a2
	add.l	(a2),a2 		 ; pointer to new module
	move.l	a2,(a0,d2.l)		 ; set it
	bra.s	sls_ldmloop

sls_done
	move.l	(a0),sys_erms(a6)	 ; set QDOS message table
	movem.l (sp)+,sls.reg
	rts



slq_set
	assert	ldp_ireg,ldp_defs-4
	move.l	(a5)+,d2		 ; registration
	moveq	#0,d1
	move.w	(a5),d1 		 ; and code
	rts

;------------------------------------------------------------------------------
sms_lenq
	pea	sms_rtok		 ; return OK
slq_do
	pea	slq_set

;+++
; Find preference
;
;	d1 c  p language code or 0
;	d2 c  p car code if d2.w = 0
;	a5  r	pointer to preference table
;---
sln_pref
	tst.w	d1			 ; language given?
	bne.s	sln_prefl		 ; ... yes
	tst.l	d2			 ; car reg given?
	bne.s	sln_prefc		 ; ... yes
sln_prefx
	move.w	sys_lang(a6),d1 	 ; use current language
	bra.s	sln_prefl		 ; to find preference


;+++
; Find preference given a car registration
;
;	d1  r	language number if match otherwise 0
;	d2 c  p car code
;	a5  r	pointer to preference table
;---
sln_prefc
	move.l	sys_ldmlst(a6),d0	 ; language dependent list
slpc_loop
	move.l	d0,a5
	move.l	(a5)+,d0
	ble.s	slpc_none		  ; not found
	exg	d0,a5
	assert	0,ldm.pref,ldm_type
	tst.w	ldm_type(a5)		 ; preference?
	bne.s	slpc_none		  ; no, all checked
	move.w	ldm_lang(a5),d1 	 ; language
	addq.l	#ldm_module,a5
	add.l	(a5),a5
	cmp.l	(a5),d2
	bne.s	slpc_loop
	rts
slpc_none
	moveq	#0,d1
	bra.s	slp_none

;+++
; Find preference given a language code
;
;	d1 c  p language code
;	a5  r	pointer to preference table
;---
sln_prefl
	move.l	sys_ldmlst(a6),d0	 ; language dependent list
slpl_loop
	move.l	d0,a5
	move.l	(a5)+,d0
	ble.s	slp_none		 ; not found
	exg	d0,a5
	assert	0,ldm.pref,ldm_type,ldm_group-2,ldm_lang-4
	tst.l	(a5)+			 ; preference?
	bne.s	slp_none		 ; no, all checked
	cmp.w	(a5)+,d1
	bne.s	slpl_loop
	addq.l	#ldm_module-(ldm_lang+2),a5
slp_seta5
	add.l	(a5),a5
	rts

slp_none
	move.l	sys_ldmlst(a6),a5	 ; language dependent list
	move.l	(a5),a5 		 ; first item must be preference
	addq.l	#ldm_module,a5
	bra.s	slp_seta5

;-------------------------------------------------------------------
sms_fprm
	bsr.s	sln_pref		 ; preference
	move.l	d3,d0
	jsr	sms_pref		 ; find preferred ldm
	move.l	a5,a1
	jmp	sms_rtok

;-------------------------------------------------------------------
sms_trns
	tst.l	d2			 ; message table
	beq.s	sms_pset		 ; ... none
	btst	#0,d2			 ; odd?
	bne.l	sms_ikey		 ; ... yes
	move.l	d2,a5
	cmp.w	#$4afb,(a5)		 ; correct flag?
	bne.l	sms_ikey		 ; ... no
	move.l	d2,sys_erms(a6) 	 ; ... yes, set it
	move.l	sys_mstab(a6),a5
	move.l	d2,(a5) 		 ; and set table 0

;-------------------------------------------------------------------
sms_pset
	sf	sys_xact(a6)		 ; Clear TRA
	moveq	#1,d7			 ; and set activate flag
	tst.l	d1			 ; TRA off
	beq.l	sms_rtok
	bclr	#0,d1
	beq.s	sps_address		 ; it is an address
	tst.w	d1			 ; set translate table?
	bgt.s	sps_lind		 ; ... yes, language independent
	beq.s	sps_lang
	moveq	#0,d7			 ; it is set language but do not activate
	clr.w	d1
sps_lang
	swap	d1			 ; get required language
	bne.s	sps_pref		 ; given
	tst.l	sys_xtab(a6)		 ; any table?
	bne.s	sps_xact		 ; ... yes, just activate

	move.w	sys_lang(a6),d1 	 ; current language

sps_pref
	bsr.s	sln_prefl		 ; find pointer to preferences
	moveq	#ldm.prtt,d0
	bsr.l	sms_pref		 ; find preferred printer table
	bra.s	sps_xset

sps_lind
	lea	sps_ibm,a5
	subq.w	#4,d1			 ; IBM is 2, GEM is 4
	bgt.l	sms_ikey		 ; not them
	blt.s	sps_xset		 ; IBM
	lea	sps_gem,a5		 ; GEM
	bra.s	sps_xset

sps_address
	move.l	d1,a5			 ; table address
	cmp.w	#$4afb,(a5)		 ; special language?
	bne.l	sms_ikey		 ; ... no
sps_xset
	move.l	a5,sys_xtab(a6)
sps_xact
	move.b	d7,sys_xact(a6) 	 ; activate it
	bra.l	sms_rtok


sps_ibm
	dc.w	$4afb
	dc.w	sps_ibm1-sps_ibm
	dc.w	sps_ibm2-sps_ibm
sps_ibm1
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07
	dc.b	$08,$09,$0A,$0B,$0C,$0D,$0E,$0F
	dc.b	$10,$11,$12,$13,$14,$15,$16,$17
	dc.b	$18,$19,$1A,$1B,$1C,$1D,$1E,$1F
	dc.b	$20,$21,$22,$23,$24,$25,$26,$27
	dc.b	$28,$29,$2A,$2B,$2C,$2D,$2E,$2F
	dc.b	$30,$31,$32,$33,$34,$35,$36,$37
	dc.b	$38,$39,$3A,$3B,$3C,$3D,$3E,$3F
	dc.b	$40,$41,$42,$43,$44,$45,$46,$47
	dc.b	$48,$49,$4A,$4B,$4C,$4D,$4E,$4F
	dc.b	$50,$51,$52,$53,$54,$55,$56,$57
	dc.b	$58,$59,$5A,$5B,$5C,$5D,$5E,$5F
	dc.b	$9C,$61,$62,$63,$64,$65,$66,$67
	dc.b	$68,$69,$6A,$6B,$6C,$6D,$6E,$6F
	dc.b	$70,$71,$72,$73,$74,$75,$76,$77
	dc.b	$78,$79,$7A,$7B,$7C,$7D,$7E,$00

	dc.b	$84,$00,$86,$82,$94,$00,$00,$81
	dc.b	$87,$A4,$91,$00,$A0,$85,$83,$89
	dc.b	$8A,$88,$8B,$A1,$8D,$8C,$A2,$95
	dc.b	$93,$A3,$97,$96,$E1,$9B,$9D,$60
	dc.b	$8E,$00,$8F,$90,$99,$00,$00,$9A
	dc.b	$80,$A5,$92,$00,$E0,$EB,$E9,$00
	dc.b	$E6,$E3,$ED,$AD,$A8,$00,$15,$00
	dc.b	$AE,$AF,$F8,$F6,$00,$00,$00,$00

	dc.b	$C0,$C1,$C2,$C3,$C4,$C5,$C6,$C7
	dc.b	$C8,$C9,$CA,$CB,$CC,$CD,$CE,$CF
	dc.b	$D0,$D1,$D2,$D3,$D4,$D5,$D6,$D7
	dc.b	$D8,$D9,$DA,$DB,$DC,$DD,$DE,$DF
	dc.b	$B0,$B1,$B2,$B3,$B4,$B5,$B6,$B7
	dc.b	$B8,$B9,$BA,$BB,$BC,$BD,$BE,$BF
	dc.b	$F0,$F1,$F2,$F3,$F4,$F5,$F6,$F7
	dc.b	$F8,$F9,$FA,$FB,$FC,$FD,$FE,$FF

	dc.b	0		; pad
sps_ibm2
	dc.b	16		; 15 replaces

	dc.l	$a54f087e	; O bs tilde
	dc.l	$af5c082e	; \ bs .

	dc.l	$7f63084f	; c bs O
	dc.l	$8161087e	; a bs tilde
	dc.l	$856f087e	; o bs tilde
	dc.l	$866f082f	; o bs /
	dc.l	$8b6f6500	; o e
	dc.l	$a141087e	; A bs tilde
	dc.l	$a64f082f	; O bs /
	dc.l	$ab4f4500	; O E
	dc.l	$b543082d	; C bs -
	dc.l	$b76f0878	; o bs x
	dc.l	$bc3c082d	; < bs -
	dc.l	$bd3e082d	; > bs -
	dc.l	$be5e0821	: ^ bs !
	dc.l	$bf760821	: v bs !


sps_gem
	dc.w	$4afb
	dc.w	sps_gem1-sps_gem
	dc.w	sps_ibm2-sps_gem
sps_gem1
	dc.b	$00,$01,$02,$03,$04,$05,$06,$07
	dc.b	$08,$09,$0A,$0B,$0C,$0D,$0E,$0F
	dc.b	$10,$11,$12,$13,$14,$15,$16,$17
	dc.b	$18,$19,$1A,$1B,$1C,$1D,$1E,$1F
	dc.b	$20,$21,$22,$23,$24,$25,$26,$27
	dc.b	$28,$29,$2A,$2B,$2C,$2D,$2E,$2F
	dc.b	$30,$31,$32,$33,$34,$35,$36,$37
	dc.b	$38,$39,$3A,$3B,$3C,$3D,$3E,$3F
	dc.b	$40,$41,$42,$43,$44,$45,$46,$47
	dc.b	$48,$49,$4A,$4B,$4C,$4D,$4E,$4F
	dc.b	$50,$51,$52,$53,$54,$55,$56,$57
	dc.b	$58,$59,$5A,$5B,$5C,$5D,$5E,$5F
	dc.b	$9C,$61,$62,$63,$64,$65,$66,$67
	dc.b	$68,$69,$6A,$6B,$6C,$6D,$6E,$6F
	dc.b	$70,$71,$72,$73,$74,$75,$76,$77
	dc.b	$78,$79,$7A,$7B,$7C,$7D,$7E,$BD

	dc.b	$84,$B0,$86,$82,$94,$B1,$B2,$81
	dc.b	$87,$A4,$91,$B4,$A0,$85,$83,$89
	dc.b	$8A,$88,$8B,$A1,$8D,$8C,$A2,$95
	dc.b	$93,$A3,$97,$96,$E1,$9B,$9D,$60
	dc.b	$8E,$B7,$8F,$90,$99,$00,$B2,$9A
	dc.b	$80,$A5,$92,$B5,$E0,$EB,$E9,$00
	dc.b	$E6,$E3,$ED,$AD,$A8,$3F,$DD,$00
	dc.b	$AE,$AF,$F8,$F6,$04,$03,$01,$02

	dc.b	$C0,$C1,$C2,$C3,$C4,$C5,$C6,$C7
	dc.b	$C8,$C9,$CA,$CB,$CC,$CD,$CE,$CF
	dc.b	$D0,$D1,$D2,$D3,$D4,$D5,$D6,$D7
	dc.b	$D8,$D9,$DA,$DB,$DC,$DD,$DE,$DF
	dc.b	$E0,$E1,$E2,$E3,$E4,$E5,$E6,$E7
	dc.b	$E8,$E9,$EA,$EB,$EC,$ED,$EE,$EF
	dc.b	$F0,$F1,$F2,$F3,$F4,$F5,$F6,$F7
	dc.b	$F8,$F9,$FA,$FB,$FC,$FD,$FE,$FF


	end
