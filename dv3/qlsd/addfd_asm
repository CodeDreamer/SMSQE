; DV3 Add Format Dependent Vector  V3.00	   1992 Tony Tebby
; for qlsd : don't use DV3 thing "create" one address returned in D7

	section dv3

	xdef	dv3_addfd

	xref	gu_thuse
	xref	gu_thfre
	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_dv3_keys'
	include 'dev8_mac_assert'

;+++
; This is the routine that is used to add one or more new format dependent
; vectors.
;
;	a1 c  p pointer to format dependent vector list
;
;	d7    r pointer to "thing"     ******
;
;	Status return standard
;	This is a clean routine
;---
dv3_addfd
	moveq	#0,d0
daf_do
daf.reg reg	d1/d2/d6/a0/a1/a2/a3
	movem.l daf.reg,-(sp)
	move.l	a1,a3			 ; two copies of new list
	move.l	a1,a2
	move.l	d0,d6

; *****
	moveq	#$60,d0 		; create "thing"
	jsr	gu_achpp
	move.l	a0,a1
	move.l	a0,d7
; *****

	move.w	sr,d0
	trap	#0
	move.w	d0,-(sp)		 ; supervisor mode for this

; *******
;	lea	dv3_name,a0		 ; use thing
;	 jsr	 gu_thuse		  ; use it
;	 bne.s	 daf_user		  ; how can this happen?
; *******

	moveq	#-1,d1			  ; highest format
	moveq	#0,d0			 ; ***** d0 is already 0

daf_cklp
	assert	fdv_next,0
	add.w	d0,a3			 ; next vector

	cmp.b	fdv_ftype(a3),d1	 ; highest type so far
	bge.s	daf_cknxt		 ; ... no
	move.b	fdv_ftype(a3),d1
daf_cknxt
	move.w	fdv_next(a3),d0 	 ; offset to next vector
	bne.s	daf_cklp

	ext.w	d1
	cmp.w	dv3_fdmax(a1),d1	 ; room for these vectors?
	ble.s	daf_link		 ; ... yes


	move.w	d1,d0
	addq.w	#1,d0			 : +1
	lsl.w	#2,d0			 ; four bytes per fdv (upper D0 is zero)
	jsr	gu_achpp
	bne.s	daf_free

	move.l	dv3_fdtab(a1),a3	 ; old table
	move.l	a0,dv3_fdtab(a1)	 ; set new table address

	move.w	dv3_fdmax(a1),d2	 ; old table length
	move.w	d1,dv3_fdmax(a1)	 ; set new length

	move.l	a3,d0			 ; old table
	beq.s	daf_link		 ; ... none

	bra.s	daf_cpelp
daf_cplp
	move.l	(a3)+,(a0)+		 ; copy pointers
daf_cpelp
	dbra	d2,daf_cplp

	move.l	d0,a0
	jsr	gu_rchp 		 ; return old table


daf_link
	move.l	dv3_fdtab(a1),a0

daf_lklp
	assert	fdv_next,0
	add.w	d0,a2			 ; next vector

	moveq	#0,d1
	move.b	fdv_ftype(a2),d1	 ; this type
	lsl.w	#2,d1
	tst.b	d6			 ; remove or add?
	beq.s	daf_rep 		 ; add (replace existing entry)
	cmp.l	(a0,d1.w),a3		 ; remove, is this the same?
	bne.s	daf_lknxt		 ; ... no
	bsr.s	daf_zap 		 ; ... yes, zap it
	bra.s	daf_lknxt

daf_rep
	tst.l	(a0,d1.w)		 ; this FDV in use
	beq.s	daf_add
	bsr.s	daf_zap 		 ; ... zap old entry
daf_add
	move.l	a2,(a0,d1.w)		 ; add new entry


daf_lknxt
	move.w	fdv_next(a2),d0 	 ; offset to next vector
	bne.s	daf_lklp

daf_free
;	 lea	 dv3_name,a0		  ; use thing
;	 jsr	 gu_thfre		  ; free thing

daf_user
	move.w	(sp)+,sr
	tst.l	d0
daf_exit
	movem.l (sp)+,daf.reg
	rts

;**** zap an entry in the FDV table
;**** now we have problem, we need the tidy up drive definition blocks etc

daf_zap
	clr.l	(a0,d1.w)		 ; zap entry
	rts

	end
