; SMSQ Hardware table handling	V2.10	  1999  Tony Tebby

	section hwt

	xdef	hwt_create		 ; create empty hardware table
	xdef	hwt_iserve		 ; install interrupt servers in hwt

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_hwt'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

;+++
; Hardware table setup - create empty hardware table
;
;	d0 cr	number of entries in empty table
;
;	status return standard
;---
hwt_create
hwtc.reg reg	d1/a0/a5
	movem.l hwtc.reg,-(sp)
	addq.w	#hwt_table/hwt.table,d0
	assert	hwt.table,$10
	lsl.l	#4,d0
	move.l	d0,d1
	jsr	gu_achpp
	bne.s	hwtc_exit

	bsr.s	hwtc_settab

	add.l	a0,d1
	move.l	d1,(a0) 		 ; set top
	add.w	#hwt_table,a0
	move.l	a0,hwt_ptr-hwt_table(a0) ; set pointer

hwtc_exit
	movem.l (sp)+,hwtc.reg
	rts

hwtc_settab
	lea	sms.hwtab,a5		 ; set table pointer
	move.l	a0,d0
	swap	d0
	bsr.s	hwtc_setw
	swap	d0
hwtc_setw
	jmp	sms.wbase

;+++
; Hardware table setup - install interrupt servers
;
;	d0  r	offset of first entry in hardware table for this driver
;	d1 c  p byte, logicl port number
;	a3 c  p linkage block
;	a4 c  p base address of hardware
;	a5 c  u pointer to vector table in driver definition table
;	status return arbirary
;---
hwt_iserve
hwti.reg reg	d0/d2/d3/d4/a0/a1/a2
	clr.l	d0			 ; return nothing
	movem.l hwti.reg,-(sp)

	move.l	a5,a1
	move.w	(a5)+,d0		 ; get pointer to table and move on
	beq.s	hwti_exit		 ; no table

	add.w	d0,a1

	move.l	sms.hwtab,a0		 ; hardware table
	move.l	hwt_ptr(a0),a2		 ; table pointer
	move.l	a2,d0
	sub.l	a0,d0
	move.l	d0,(sp) 		 ; set offset

hwti_loop
	move.w	(a1)+,d4		 ; next vector to set
	beq.s	hwti_exit		 ; no (more) vectors

	cmp.l	hwt_top(a0),a2		 ; off top yet?
	blo.s	hwti_set		 ; ... no

	move.l	a2,d3
	sub.l	a0,d3			 ; old size
	moveq	#hwt.table*7,d2 	 ; make room for 7 more entries
	add.l	d3,d2			 ; new size

	move.l	a0,a4			 ; keep old base

	move.l	d2,d0
	jsr	gu_achpp		 ; allocate
	bne.s	hwti_exit		 ; should not happen

	move.l	d3,d0
	move.l	a4,a2
hwti_copy
	move.l	(a2)+,(a0)+
	subq.w	#4,d0
	bgt.s	hwti_copy

	move.l	a0,a2			 ; new pointer
	sub.l	d3,a0			 ; new base
	add.l	a0,d2			 ; new top

	move.l	d2,hwt_top(a0)
	move.l	a2,hwt_ptr(a0)

	bsr.s	hwtc_settab		 ; save table

	exg	a0,a4
	jsr	gu_rchp 		 ; return old bit
	exg	a0,a4

hwti_set
	move.l	a3,(a2)+		 ; set linkage
	pea	-2(a1,d4.w)
	move.l	(sp)+,(a2)+		 ; set server
	clr.l	(a2)+			 ; spare
	move.w	(a1)+,(a2)+		 ; interrupt level and priority
	move.w	(a1)+,d0		 ; type / spare
	move.b	d1,d0
	move.w	d0,(a2)+		 ; type / port number
	move.l	a2,hwt_ptr(a0)
	bra.s	hwti_loop

hwti_exit
	movem.l (sp)+,hwti.reg
	rts


	end
