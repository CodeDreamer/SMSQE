; DV3 Check Format	  V3.00 	  1992 Tony Tebby

	section dv3

	xdef	dv3_fcheck

	xref	dv3_setfd

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'

;+++
; This is the routine that is used to scan the table of format dependent vectors
; to try and find a vector that can handle a particular medium.
;
;	d1-d6 p
;	d7 c  p drive number
;	a0 c  p pointer to channel block
;	a1 c  p pointer to root sector
;	a2    p
;	a3 c  p pointer to linkage block
;	a4 c  u pointer to definition block
;	a5    p
;	a6 c  p pointer to system variables
;
;	Status return standard or +ve if changed
;---
dv3_fcheck
dfc.reg reg	d1/d6/a0/a2/a5
	movem.l dfc.reg,-(sp)
	move.l	ddf_mcheck+2(a4),d0	 ; any format dependent driver at all?
	beq.s	dfc_all 		 ; ... no, check all

	move.l	d0,a2
	jsr	(a2)			 ; check this format
	ble.s	dfc_exit		 ; ... ok or bad
	subq.l	#ddf.change,d0		 ; changed?
	beq.s	dfc_change		 ; ... yes

dfc_all
	sf	ddf_mstat(a4)		 ; clear status (should already be)
	move.l	ddl_dv3(a3),a5		 ; dv3 linkage
	move.w	dv3_fdmax(a5),d6	 ; max format dependent
	move.l	dv3_fdtab(a5),a5	 ; and list
	move.l	a5,d0			 ; any?
	beq.s	dfc_bad 		 ; ... no

dfc_loop
	move.l	(a5)+,d0		 ; a format?
	beq.s	dfc_eloop		 ; ... no
	move.l	d0,a2
	jsr	dv3_setfd

	jsr	ddf_mcheck(a4)		 ; check format
	blt.s	dfc_exit		 ; failure
	subq.l	#ddf.change,d0		 ; changed or unrecognised?
	beq.s	dfc_change		 ; changed
dfc_eloop
	subq.w	#1,d6			 ; next format?
	bge.s	dfc_loop

dfc_bad
	moveq	#err.mchk-ddf.change,d0  ; not recognised

dfc_change
	addq.l	#ddf.change,d0
dfc_exit
	movem.l (sp)+,dfc.reg
	rts

	end
