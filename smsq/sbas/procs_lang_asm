; SBAS_PROCS_LANG - SBASIC Language Procedures	V2.00	 1994	Tony Tebby

	section exten

	xdef	language
	xdef	language$
	xdef	lang_use
	xdef	tra

	xref	ut_gxnm1
	xref	ut_gxin1
	xref	ut_gtli1
	xref	ut_ckri6
	xref	ut_rtint
	xref	ut_retst
	xref	cv_upcas

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; i% = LANGUAGE (lang)
;---
language
	bsr.s	lng_opt 		 ; get optional parameter
	bne.s	lng_rts
	jmp	ut_rtint

;+++
; a$ = LANGUAGE$ (lang)
;---
language$
	bsr.s	lng_opt 		 ; get optional parameter
	bne.s	lng_rts
	jsr	ut_ckri6
	subq.l	#6,a1
	move.l	d2,2(a6,a1.l)
	move.w	#4,(a6,a1.l)		 ; 4 byte string
	jmp	ut_retst

lng_opt
	moveq	#sms.lenq,d7		 ; enquiry only

	move.l	sb_arthp(a6),d6
	sub.l	sb_arthb(a6),d6 	 ; save extent of arithmetic stack
	pea	lopt_reset

	moveq	#0,d1
	move.l	a5,d2
	sub.l	a3,d2
	beq.s	lng_call		 ; no language given
	bra.s	lng_lang

lopt_reset
	move.l	d6,a1
	add.l	sb_arthb(a6),a1 	 ; clean stack
	move.l	a1,sb_arthp(a6)
	tst.l	d0
	rts

;+++
; TRA n,lang
;---
tra
	jsr	ut_gtli1		 ; get one long integer
	bne.s	tra_rts 		 ; ... oops
	move.l	(a6,a1.l),d1		 ; TRA code

	addq.l	#nt.len,a3
	cmp.l	a5,a3			 ; any language?
	beq.s	tra_set 		 ; ... no
	move.w	d1,d6			 ; save code
	bne.s	tra_lang		 ; not zero
	moveq	#1,d1			 ; set one for test
	moveq	#-1,d6			 ; ... zero becomes -1
tra_lang
	subq.l	#1,d1			 ; must be one
	bne.s	lng_ipar		 ; ... it isnt
	moveq	#sms.lenq,d7		 ; enquiry only
	bsr.s	lng_lang		 ; get language
	bne.s	tra_rts
	swap	d1
	move.w	d6,d1			 ; combine with code

tra_set
	moveq	#sms.pset,d0		 ; set printer translate
	trap	#do.sms2
tra_rts
lng_rts
	rts

lng_ipar
	moveq	#err.ipar,d0
	rts

;+++
; LANG_USE
;---
lang_use
	moveq	#sms.lset,d7		 ; to set language

;--- this little subroutine gets the language code and intl car reg into d1/d2
;--- and possible sets the langauge as well

lng_lang
	jsr	ut_gxin1		 ; try to get an integer
	bne.s	lng_car 		 ; must be a car registration
	move.l	#0,d2
	move.w	(a6,a1.l),d1		 ; is it unset (=0)
	bne.s	lng_call		 ; ... no, enquire
lng_car
	jsr	ut_gxnm1		 ; try to get name or string
	bne.s	lng_rts

	move.w	(a6,a1.l),d0		 ; number of characters
	ble.s	lng_ipar		 ; ... oops
	cmp.w	#4,d0			 ; more than 4
	bhi.s	lng_ipar		 ; ... oops
	move.l	#'    ',-(sp)
	move.l	sp,a0
lng_copy
	move.b	2(a6,a1.l),d1
	jsr	cv_upcas
	move.b	d1,(a0)+		 ; an upper case character
	addq.l	#1,a1
	subq.w	#1,d0
	bgt.s	lng_copy

	move.l	(sp)+,d2		 ; car reg code
	moveq	#0,d1

lng_call
	move.l	d7,d0			 ; call key
	trap	#do.sms2
	tst.l	d0
	rts


	end
