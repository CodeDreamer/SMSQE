; SBAS_CMPNUSE - Sort Out Name Usage	 V2.00	   1994 Tony Tebby

	section	sbas

	xdef	sb_cmpnuse

	include	'dev8_keys_sbasic'

;+++
; SBASIC Sort Out Name Usage
;
;	d0  r	zero
;	d1   s
;	a0   s
;	a1   s
;	a6 c  p	pointer	to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_cmpnuse
	move.l	sb_nutbb(a6),a0		 ; table base
	move.l	sb_nutbp(a6),d1		 ; and top
	sub.l	a0,d1
	lea	scn_table,a1
	moveq	#0,d0

	bra.s	scn_eloop
scn_loop
	move.b	(a0),d0			 ; next	code
	move.b	(a1,d0.w),(a0)+		 ; rationalised	(0=impossible)
scn_eloop
	dbra	d1,scn_loop

	moveq	#0,d0
	rts

badn	equ	$00	; bad

....p	 equ	 $01	 ; procedure
...f.	 equ	 $02	 ; function
..a..	 equ	 $04	 ; array
.v...	 equ	 $08	 ; variable

.v..p	 equ	 $09	 ; variable or proc
.v.f.	 equ	 $0a	 ; variable or fun
.va..	 equ	 $0c	 ; array or variable
.va.p	 equ	 $0d	 ; array, var or proc
.vaf.	 equ	 $0e	 ; array, var or fun

pva..	 equ	 $8c	 ; formal param, array or variable
pva.p	 equ	 $8d	 ; formal param, array,	var or proc
pvaf.	 equ	 $8e	 ; formal param, array,	var or fun

xv..p	 equ	 $09	 ; impossible variable or proc
xv.f.	 equ	 $0a	 ; impossible variable or fun
xva..	 equ	 $0c	 ; impossible array or variable
xva.p	 equ	 $0d	 ; impossible array, var or proc
xvaf.	 equ	 $0e	 ; impossible array, var or fun

scn_table
	dc.b	.v...,....p,...f.,badn,..a..,xva.p,xvaf.,badn
	dc.b	.v...,xv..p,xv.f.,badn,xva..,xva.p,xvaf.,badn
	dc.b	.va..,.va.p,.vaf.,badn,.va..,.va.p,.vaf.,badn
	dc.b	.va..,.va.p,.vaf.,badn,.va..,.va.p,.vaf.,badn

	dc.b	.v...,....p,...f.,badn,.va..,.va.p,.vaf.,badn
	dc.b	.v...,.v..p,.v.f.,badn,.va..,.va.p,.vaf.,badn
	dc.b	.va..,.va.p,.vaf.,badn,.va..,.va.p,.vaf.,badn
	dc.b	.va..,.va.p,.vaf.,badn,.va..,.va.p,.vaf.,badn

	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn
	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn
	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn
	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn

	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn
	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn
	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn
	dc.b	pva..,pva.p,pvaf.,badn,pva..,pva.p,pvaf.,badn

	end
