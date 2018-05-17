; Routine to read standard Hotkey item name   V2.01    1988 Tony Tebby   QJUMP

	section hotkey

	xdef	hot_itnm

	xref	ut_gtnm1

	include 'dev8_keys_err'
	include 'dev8_ee_hot_bv'
	include 'dev8_ee_hk_data'

;+++
; Read Hotkeyed item name as a string into the SuperBASIC buffer.
;
;	d0  r	standard or option character
;	d1  r	option character if d0>0
;	d2-d3	scratch
;	d4  r	(word) start of substring in item name with parameter
;	d5  r	(word) start of substring in item name with job name
;	a1  r	pointer to SuperBASIC buffer
;	a3 cu	pointer to first parameter
;	a5 c  p pointer to last parameter
;
;	status return standard or +ve (option character)
;---
hot_itnm
hin.reg reg	a2/d6/d7
	movem.l hin.reg,-(sp)
	moveq	#0,d4			 ; ... no parameter
	moveq	#0,d5			 ; ... no job / wake name
	moveq	#0,d7			 ; total length of name

	move.b	1(a6,a3.l),d6		 ; next separator
	jsr	ut_gtnm1		 ; get name
	bne.s	hin_exit

	move.l	bv_bfbas(a6),a0
	addq.w	#2,a0			 ; start filling here
	tst.w	(a6,a1.l)		 ; any name?
	beq.l	hin_inam		 ; ... no
	bsr.l	hin_copy

	addq.l	#8,a3			 ; next parameter
	cmp.l	a5,a3			 ; any?
	bge.s	hin_ok			 ; ... no

	lsr.b	#4,d6			 ; check for separator
	subq.b	#1,d6			 ; comma?
	beq.s	hin_getj		 ; ... yes, is it job name?
	subq.b	#1,d6			 ; semicolon?
	beq.s	hin_getp		 ; ... yes, get parameter
	subq.b	#2,d6			 ; !?
	beq.s	hin_getw		 ; ... yes, get wake
	bra.s	hin_ipar		 ; ... oops

hin_getp
	move.b	1(a6,a3.l),d6		 ; next separator
	jsr	ut_gtnm1		 ; get parameter string
	bne.s	hin_exit
	moveq	#hki.psep,d0		 ; put semi colon in
	move.w	d7,d4			 ; start of parameter
	bsr.s	hin_psep

	moveq	#hki.popn,d0		 ; open bracket
	bsr.s	hin_csep		 ; copy with separator

	moveq	#hki.pcls,d0		 ; close bracket
	bsr.s	hin_psep

	addq.l	#8,a3			 ; next parameter
	cmp.l	a5,a3			 ; any?
	bge.s	hin_ok			 ; ... no

	lsr.b	#4,d6			 ; check for separator
	subq.b	#1,d6			 ; comma?
	beq.s	hin_getj		 ; ... yes, is it job name?
	subq.b	#3,d6			 ; !?
	bne.s	hin_ipar		 ; ... oops

hin_getw
	jsr	ut_gtnm1		 ; get wake name
	bne.s	hin_exit
	moveq	#hki.wsep,d0		 ; preceded by !
	bra.s	hin_pw

hin_getj
	jsr	ut_gtnm1		 ; get program name
	bne.s	hin_exit
	cmp.w	#1,(a6,a1.l)		 ; length
	beq.s	hin_opt 		 ; ... 1, option
	moveq	#hki.jsep,d0		 ; ... <>1, Job name
hin_pw
	move.w	d7,d5			 ; start of program / wake name
	bsr.s	hin_csep		 ; copy substring with separator

	addq.l	#8,a3			 ; next parameter

hin_ok
	moveq	#0,d0

hin_exit
	move.l	bv_bfbas(a6),a1
	move.w	d7,(a6,a1.l)		 ; set length
	movem.l (sp)+,hin.reg
	tst.l	d0
	rts

hin_opt
	addq.l	#8,a3			 ; we've taken it
	moveq	#0,d0
	move.b	2(a6,a1.l),d0		 ; set option character
	move.l	d0,d1
	bra.s	hin_exit

hin_ipar
	moveq	#err.ipar,d0
	bra.s	hin_exit

hin_inm4
	addq.l	#4,sp
hin_inam
	moveq	#err.inam,d0
	bra.s	hin_exit

hin_psep
	move.b	d0,(a6,a0.l)		 ; separator
	addq.l	#1,a0
	addq.w	#1,d7
	rts

hin_csep
	bsr.s	hin_psep
hin_copy
	move.w	(a6,a1.l),d0		 ; length of next string
	addq.w	#2,a1
	add.w	d0,d7			 ; new length
	cmp.w	#hkd.bufl-4,d7
	bhi.s	hin_inm4		 ; ... too long
	bra.s	hin_clend
hin_cloop
	move.b	(a6,a1.l),(a6,a0.l)	 ; copy name
	addq.l	#1,a1
	addq.l	#1,a0
hin_clend
	dbra	d0,hin_cloop
	rts
	end
