; SBAS_ILOOP - Loopl Control Structure Interpretation	 1994 Tony Tebby

	section sbas

	xdef	bo_forst
	xdef	bo_forfp
	xdef	bo_forint
	xdef	bo_rep
	xdef	bo_fstv
	xdef	bo_fstr
	xdef	bo_ffpv
	xdef	bo_ffpr
	xdef	bo_fintv
	xdef	bo_fintr
	xdef	bo_fciv
	xdef	bo_fcir
	xdef	bo_fend
	xdef	bo_endf
	xdef	bo_endr
	xdef	bo_next
	xdef	bo_exit

	xref	sb_ibreak
	xref	sb_ierset
	xref	sb_ienimp
	xref	sb_istop
	xref	sb_anyfp2
	xref	sb_fint

	xref	sb_aldat
	xref	sb_redat
	xref	sb_redat8

	xref	qa_addd
	xref	qa_subd

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

;--------------------------------------------- FOR
bo_forst
	moveq	#nt.st,d0		 ; string loop
	bra.l	bof_setup

bof_alst
	cmp.b	#$ff,dt_flstr(a0)	 ; is it flagged as a variable?
	bne.l	bof_invar		 ; ... no

	assert	dt_stalc-dt_frbase,$26	 ; add another 2 so allocation is x8
	moveq	#dt_stalc-dt_frbase+2,d1   ; additional allocation for FOR
	add.w	dt_stalc(a0),d1
	moveq	#dt_frbase-2,d2
	add.w	d1,d2			 ; max string length

	move.l	a0,d3			 ; save variable pointer
	jsr	sb_aldat		 ; allocate in data area
	move.l	d1,(a0) 		 ; allocation
	move.l	d3,a2

	lea	dt_type-dt_frbase(a0),a0 ; set length
	move.w	d2,(a0)+
	move.l	a0,d5			 ; and save base pointer
	move.w	(a2)+,d1
	moveq	#-2,d2
	sub.w	d1,d2			 ; adjustment
	and.w	#$fffe,d2		 ; rounded
	move.w	d1,(a0)+
	beq.s	bofs_adjust

bofs_copy
	move.w	(a2)+,(a0)+		 ; copy string
	subq.w	#2,d1
	bgt.s	bofs_copy

bofs_adjust
	lea	dt_stalc(a2,d2.l),a0	 ; set base of block to release
	moveq	#0,d1
	move.w	(a0),d1
	jsr	sb_redat		 ; release data area

	move.l	d5,a0
	rts

bo_forfp
	moveq	#nt.fp,d0		 ; floating point loop
	bra.s	bof_setup

bof_alfp
	cmp.w	#dt.flfp,dt_flfp(a0)	 ; is it flagged as a variable?
	bne.l	bof_invar		 ; ... no

	move.w	(a0),d2
	move.l	2(a0),d5		 ; save value
	jsr	sb_redat8

	moveq	#dt.forrep,d1
	jsr	sb_aldat		 ; allocate in data area
	move.l	d1,(a0) 		 ; allocation

	lea	dt_type-dt_frbase(a0),a0 ; set type
	clr.w	(a0)+
	assert	dt_type,-2
	move.w	d2,dt_value(a0)
	move.l	d5,dt_value+2(a0)	 ; set new value
	rts


bo_forint
	moveq	#nt.in,d0		 ; integer loop
	bra.s	bof_setup

bof_alint
	cmp.l	#dt.flint,dt_flint(a0)	 ; is it flagged as a variable?
	bne.s	bof_invar		 ; ... no

	move.w	(a0),d2 		 ; save value
	jsr	sb_redat8

	moveq	#dt.forrep,d1
	jsr	sb_aldat		 ; allocate in data area
	move.l	d1,(a0) 		 ; allocation

	lea	-dt_frbase(a0),a0
	st	dt_type(a0)		 ; set type
	move.w	d2,dt_value(a0) 	 ; set value
	rts


bof_setup
	move.l	(a4)+,d3		 ; end of loop
	move.w	(a4)+,d4		 ; name table index
	moveq	#$f,d2
	and.b	nt_vtype(a3,d4.w),d2
	cmp.b	d2,d0			 ; correct type?
	bne.s	bof_invar

	move.l	nt_value(a3,d4.w),a0	 ; set value pointer

	move.b	nt_nvalp(a3,d4.w),d2
	subq.b	#nt.for,d2		 ; already FOR?
	beq.s	bof_sblock		 ; ... yes
	addq.b	#nt.for-nt.rep,d2	 ; or REP?
	beq.s	bof_sfor		 ; ... yes
	addq.b	#nt.rep-nt.var,d2	 ; VAR?
	beq.s	bof_type		 ; ... yes
	addq.b	#nt.var-nt.unset,d2	 ; UNSET?
	bne.s	bof_invar		 ; ... no
	tst.w	nt_usetp(a3,d4.w)	 ; null?
	beq.s	bof_invar		 ; ... yes
bof_type
	jsr	4(a2)			 ; yes, continue with type specific

bof_sval
	move.l	a0,nt_value(a3,d4.w)	 ; FOR block
bof_sfor
	move.b	#nt.for,nt_nvalp(a3,d4.w) ; it is now FOR
bof_sblock
	move.l	a4,a2			 ; find end of ranges
	add.w	(a4)+,a2
	move.l	a2,d0
	lea	dt_range(a0),a2
	move.l	d3,(a2)+		 ; end of loop!
	move.l	d0,(a2)+		 ; start of loop
	move.l	d3,(a2) 		 ; end of loop
	jmp	(a5)			 ; set up

bof_invar
	moveq	#ern4.bfor,d0
	jmp	sb_ierset		 ; bad for variable


;-------------------------------------------- REPeat
bo_rep
	move.l	(a4)+,d3		 ; end of loop
	move.w	(a4)+,d4		 ; name table index
	move.l	nt_value(a3,d4.w),a0	 ; set value pointer

	move.b	nt_nvalp(a3,d4.w),d2
	subq.b	#nt.rep,d2		 ; already REP?
	beq.s	bor_sblock		 ; ... yes
	subq.b	#nt.for-nt.rep,d2	 ; or FOR?
	beq.s	bor_srep		 ; ... yes
	addq.b	#nt.for-nt.var,d2	 ; VAR?
	beq.s	bor_type		 ; ... yes
	addq.b	#nt.var-nt.unset,d2	 ; UNSET?
	bne.s	bof_invar		 ; ... no
	tst.w	nt_usetp(a3,d4.w)	 ; null?
	beq.s	bof_invar		 ; ... yes
bor_type
	pea	bor_sval
	moveq	#$f,d2
	and.b	nt_vtype(a3,d4.w),d2
	subq.b	#nt.fp,d2
	beq	bof_alfp		 ; allocate FP
	bgt	bof_alint		 ; or int
	bra	bof_alst		 ; or string

bor_sval
	move.l	a0,nt_value(a3,d4.w)	 ; REP block
bor_srep
	move.b	#nt.rep,nt_nvalp(a3,d4.w) ; it is now REP
bor_sblock
	lea	dt_range(a0),a2
	clr.l	(a2)+			 ; no ranges
	move.l	a4,(a2)+		 ; start of loop
	move.l	d3,(a2) 		 ; end of loop
	jmp	(a5)			 ; set up

;---------------------------------------- ranges - string
bo_fstv
bo_fstr
	jmp	sb_ienimp

;---------------------------------------- ranges - float
bo_ffpv
	cmp.w	#ar.float,(a1)+ 	 ; check for float
	beq.s	bofv_set
	jsr	sb_anyfp2		 ; anything to floating point

bofv_set
	move.w	(a4)+,d3
	move.l	nt_value(a3,d3.w),a0	 ; value is here
	move.w	(a1)+,(a0)		 ; set value
	move.l	(a1)+,2(a0)
	clr.l	dt_step+2(a0)		 ; no step
	bra.l	bof_go

bo_ffpr
	move.w	(a4)+,d3
	move.l	nt_value(a3,d3.w),a2	 ; value is here

	cmp.w	#ar.float,(a1)+ 	 ; check for float (step)
	beq.s	bofr_sets
	jsr	sb_anyfp2		 ; anything to floating point
bofr_sets
	move.w	(a1)+,d2
	move.l	(a1)+,d1
	move.w	d2,dt_step(a2)		 ; set step
	move.l	d1,dt_step+2(a2)

	cmp.w	#ar.float,(a1)+ 	 ; check for float (limit)
	beq.s	bofr_setl
	jsr	sb_anyfp2		 ; anything to floating point
bofr_setl
	move.w	(a1)+,d4
	move.l	(a1)+,d3
	assert	dt_llimit,dt_ulimit-6,dt_nlimit-12
	lea	dt_llimit(a2),a0
	move.w	d4,(a0)+		 ; set lower limit
	move.l	d3,(a0)+
	move.w	d4,(a0)+		 ; set upper limit
	move.l	d3,(a0)+
	move.w	d4,(a0)+		 ; set nominal limit
	move.l	d3,(a0)+

	sub.w	#23,d2			 ; appx 1 in 10^7 of step
	bge.s	bofr_setlms		 ; OK
	asr.l	d2,d1
	moveq	#0,d2			 ; unnormalised
bofr_setlms
	move.l	a1,a0			 ; save a1
	lea	dt_ulimit(a2),a1
	jsr	qa_addd 		 ; set upper limit
	subq.l	#dt_ulimit-dt_llimit,a1
	jsr	qa_subd 		 ; set lower limit
	move.l	a0,a1

	cmp.w	#ar.float,(a1)+ 	 ; check for float (initial value)
	beq.s	bofr_setv
	jsr	sb_anyfp2		 ; anything to floating point

bofr_setv
	move.w	(a1)+,d0
	move.w	d0,(a2) 		 ; set value
	move.l	(a1)+,d5
	move.l	d5,2(a2)
	move.l	a2,a0

	blt.s	bofr_nstart		 ; start is negative
	tst.l	d3			 ; is limit negative?
	blt.s	bofr_gt 		 ; ... yes, start > limit
	cmp.w	d0,d4			 ; the same exponent?
	bgt.s	bofr_lt 		 ; ... no, start < limit
	beq.s	bofr_mant		 ; ... yes, check mantissas
	bra.s	bofr_gt

bofr_nstart
	tst.l	d3			 ; is limit positive?
	bgt.s	bofr_lt 		 ; ... yes, start < limit
	cmp.w	d0,d4			 ; the same exponent?
	blt.s	bofr_lt 		 ; ... no, start < limit
	bgt.s	bofr_gt 		 ; ... no, start > limit

bofr_mant
	cmp.l	d5,d3			 ; which is greater
	bgt.s	bofr_lt 		 ; start < limit
	blt.s	bofr_gt 		 ; start > limit

	tst.l	d1			 ; one iteration only unless
	beq.s	bof_nogo		 ; step = 0
	bra.s	bof_go

bofr_gt
	tst.l	d1			 ; start > limit, step should be <0
	blt.s	bof_go
	bra.s	bof_nogo

bofr_lt
	tst.l	d1			 ; start > limit, step should be >=0
	bge.s	bof_go

bof_nogo
	jmp	(a5)

bof_stop
	jmp	sb_istop		 ; stop with error

;------------------------------------------- ranges - integer
bo_fintv
	cmp.w	#ar.int,(a1)+		 ; check for integer
	beq.s	boiv_set
	jsr	sb_fint 		 ; fetch integer
	bne.s	bof_stop

boiv_set
	move.w	(a4)+,d3
	move.l	nt_value(a3,d3.w),a0	 ; value is here
	move.w	(a1)+,(a0)		 ; set value
	clr.w	dt_step(a0)		 ; no step
	bra.s	bof_go

bo_fintr
	move.w	(a4)+,d3
	move.l	nt_value(a3,d3.w),a2	 ; value is here

	cmp.w	#ar.int,(a1)+		 ; check for integer step
	beq.s	boir_sets
	jsr	sb_fint 		 ; fetch integer
	bne.s	bof_stop

boir_sets
	move.w	(a1)+,d2
	move.w	d2,dt_step(a2)		 ; set step

	cmp.w	#ar.int,(a1)+		 ; check for integer limit
	beq.s	boir_setl
	jsr	sb_fint 		 ; fetch integer
	bne.s	bof_stop

boir_setl
	move.w	(a1)+,d1
	move.w	d1,dt_limit(a2) 	 ; set limit

	cmp.w	#ar.int,(a1)+		 ; check for integer start
	beq.s	boir_setv
	jsr	sb_fint 		 ; fetch integer
	bne.s	bof_stop

boir_setv
	move.w	(a1)+,d3
	move.w	d3,(a2) 		 ; set value
	move.l	a2,a0

	tst.w	d2
	bgt.s	boir_spos		 ; step positive
	blt.s	boir_sneg		 ; step negative
boir_szer
	cmp.w	d1,d3			 ; zero step, what about range?
	blt.s	bof_go
	bra.s	bof_nogo		 ; no go

boir_sneg
	cmp.w	d1,d3			 ; negative step, what about range
	bge.s	bof_go
	bra.s	bof_nogo

boir_spos
	cmp.w	d1,d3			 ; positive step, what about range
	bgt.s	bof_nogo

bof_go
	move.l	a4,dt_range(a0) 	 ; next range
	move.l	dt_start(a0),a4
	jmp	(a5)


;------------------------------------------- ranges - constant integer
; beware, must enter with (a0) pointing to for variable
bo_fciv
	move.w	(a4)+,(a0)		 ; set value
	clr.w	dt_step(a0)		 ; no step
	bra.s	bof_go

bo_fcir
	move.w	(a4)+,d3
	move.w	d3,(a0) 		 ; set value
	move.w	(a4)+,d1
	move.w	d1,dt_limit(a0) 	 ; set limit
	move.w	(a4)+,d2
	move.w	d2,dt_step(a0)		 ; set step
	bgt.s	boir_spos		 ; step positive
	blt.s	boir_sneg		 ; step negative
	bra.s	boir_szer

;------------------------------------------ end of ranges
; beware, must enter with (a0) pointing to for variable
bo_fend
	clr.w	dt_step(a0)
	clr.l	dt_step(a0)		 ; no step now

	move.l	dt_range(a0),d0 	 ; where to go when done
	subq.l	#2,a4
	move.l	a4,dt_range(a0) 	 ; next range
	move.l	d0,a4
	jmp	(a5)

;------------------------------------------ END of loop
bo_endf
	move.w	(a4)+,d1		 ; for control variable
	cmp.b	#nt.for,nt_nvalp(a3,d1.w)
	bne.s	bon_badv		 ; this end not allowed
	move.l	nt_value(a3,d1.w),a0	 ; control structure
	move.l	a4,dt_end(a0)		 ; set end
	bra.s	bon_for 		 ; next FOR

bo_endr
	move.w	(a4)+,d1		 ; repeat control variable
	cmp.b	#nt.rep,nt_nvalp(a3,d1.w)
	bne.s	bon_badv		 ; this end not allowed
	move.l	nt_value(a3,d1.w),a0	 ; control structure
	move.l	a4,dt_end(a0)		 ; set end
bon_rep
	move.l	dt_start(a0),a4 	 ; next REP
	jmp	sb_ibreak


;------------------------------------------ NEXT and EXIT
bo_next
	move.w	(a4)+,d1		 ; control variable
	move.l	nt_value(a3,d1.w),a0	 ; and structure
	move.b	nt_nvalp(a3,d1.w),d0
	subq.b	#nt.for,d0		 ; FOR?
	beq.s	bon_for 		 ; ... yes
	addq.b	#nt.for-nt.rep,d0	 ; REP?
	beq.s	bon_rep 		 ; ... yes

bon_badv
	moveq	#ern4.blvr,d0		 ; this is not loop
	jmp	sb_ierset

bon_for
	tst.w	dt_type(a0)		 ; type of loop
	bmi.l	bon_ifor		 ; integer for
	bgt.l	bon_sfor		 ; string for

	move.l	dt_step+2(a0),d1	 ; step mantissa
	beq.s	bon_nrange		 ; next range
	move.w	dt_step(a0),d2		 ; step exponent

	move.l	dt_value+2(a0),-(a1)
	move.w	dt_value(a0),-(a1)
	jsr	qa_addd 		 ; next value
	move.w	(a1)+,d2
	move.l	(a1)+,d3		 ; this is it

	blt.s	bofl_nval		 ; negative value
	move.l	dt_llimit+2(a0),d0	 ; is limit negative?
	blt.s	bofl_gt 		 ; ... yes, value is greater
	cmp.w	dt_llimit(a0),d2	 ; the same exponent?
	beq.s	bofl_mant		 ; ... yes, check mantissas
	blt.s	bofl_lt
	bra.s	bofl_gt

bofl_nval
	move.l	dt_llimit+2(a0),d0	 ; is limit positive?
	bgt.s	bofl_lt 		 ; ... yes
	cmp.w	dt_llimit(a0),d2	 ; the same exponent?
	beq.s	bofl_mant		 ; ... yes, check mantissas
	blt.s	bofl_gt
	bra.s	bofl_lt

bofl_mant
	cmp.l	d0,d3			 ; set rel according to mantissas
	bgt.s	bofl_gt
	beq.s	bonf_last		 ; = is last step

bofl_lt
	tst.l	d1			 ; positive or negative step?
	bgt.s	bonf_value		 ; positive, continue range
	bra.s	bonf_upper

bofl_gt
	tst.l	d1			 ; positive or negative step?
	bmi.s	bonf_value		 ; negative, continue range

bonf_upper			 ; check against upper limit
	tst.l	d3			 ; value
	blt.s	bofu_nval		 ; negative value
	move.l	dt_ulimit+2(a0),d0	 ; is limit negative?
	blt.s	bofu_gt 		 ; ... yes, value is greater
	cmp.w	dt_ulimit(a0),d2	 ; the same exponent?
	beq.s	bofu_mant		 ; ... yes, check mantissas
	blt.s	bofu_lt
	bra.s	bofu_gt

bofu_nval
	move.l	dt_ulimit+2(a0),d0	 ; is limit positive?
	bgt.s	bofu_lt 		 ; ... yes
	cmp.w	dt_ulimit(a0),d2	 ; the same exponent?
	beq.s	bofu_mant		 ; ... yes, check mantissas
	blt.s	bofu_gt
	bra.s	bofu_lt

bofu_mant
	cmp.l	d0,d3			 ; set rel according to mantissas
	bgt.s	bofu_gt
	beq.s	bonf_last		 ; = is last step

bofu_lt
	tst.l	d1			 ; positive or negative step?
	bgt.s	bonf_last		 ; positive, last step
	bra.s	bon_nrange

bofu_gt
	tst.l	d1			 ; positive or negative step?
	bmi.s	bonf_last		 ; negative, last step

bon_nrange
	move.l	dt_range(a0),d0
	move.l	a4,dt_range(a0)
	move.l	d0,a4
	jmp	(a5)

bonf_last
	move.w	dt_nlimit(a0),d2	 ; last is at limit
	move.l	dt_nlimit+2(a0),d3

bonf_value
	move.w	d2,dt_value(a0) 	 ; set next value
	move.l	d3,dt_value+2(a0)
	move.l	dt_start(a0),a4
	jmp	sb_ibreak


bon_ifor
	move.w	dt_value(a0),d2
	move.w	dt_step(a0),d1		 ; step
	ble.s	boni_neg		 ; negative or zero
	add.w	d1,d2			 ; next value
	bvs.s	bon_nrange		 ; ... overflow
	cmp.w	dt_limit(a0),d2 	 ; beyond limit?
	bgt.s	bon_nrange		 ; ... yes

boni_value
	move.w	d2,dt_value(a0) 	 ; set next value
	move.l	dt_start(a0),a4
	jmp	sb_ibreak

boni_neg
	beq.s	bon_nrange		 ; next range
	add.w	d1,d2			 ; next value
	cmp.w	dt_limit(a0),d2 	 ; beyond limit?
	bge.s	boni_value		 ; ... no
	bra.s	bon_nrange

bon_sfor
	jmp	sb_ienimp

bo_exit
	move.w	(a4)+,d1		 ; control variable
	move.l	nt_value(a3,d1.w),a0	 ; and structure
	move.b	nt_nvalp(a3,d1.w),d0
	subq.b	#nt.for,d0		 ; FOR?
	beq.s	box_do			 ; ... yes
	addq.b	#nt.for-nt.rep,d0	 ; REP?
	bne	bon_badv		 ; ... no
box_do
	move.l	dt_end(a0),a4		 ; go to end
	jmp	(a5)

	end
