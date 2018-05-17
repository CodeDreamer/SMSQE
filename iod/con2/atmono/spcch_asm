; Atari ST mono = QL mode sprite cache	   V2.00   1998  Tony Tebby
;
	section driver

	xdef	pt_cchset
	xdef	pt_cchloc

	xdef	pt.spxsw
	xdef	pt.rpxsw
	xdef	pt.spsln
	xdef	pt.spcln

	xref	sp_arrow

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_pt'

pt.spxsw equ	4	  ; shift pixels to sprite word
pt.rpxsw equ	$0f	  ; round up pixels to sprite word
pt.spsln equ	(pt.spspx/4+4)*pt.spspy ; length of sprite save area
pt.spcln equ	0	  ; length of sprite cache

;+++
; Sprite cache setup
;
;	a3 c  p pointer to pointer linkage
;	a5 c  p pointer to sprite cache
;
;---
pt_cchset
	moveq	#0,d0
	rts

;+++
; Sprite cache locate sprite
;
;	d0 c  s -ve pointer,
;		0 ordinary sprite  (or pattern with mask!!!)
;		1 pattern
;		2 blob
;	d2    r pointer to the correct form sprite
;	a1 c  r pointer to sprite type 0 / pointer to native mode sprite
;	d2
;	a3 c  p pointer to pointer linkage
;
;---
pt_cchloc
ptcc.reg reg	d1/d4/d7/a0
stk_d2	equ	4
	movem.l ptcc.reg,-(sp)

	move.b	d0,d7			 ; pointer sprite flag
	moveq	#0,d2			 ; no suitable object found
	move.l	a1,a0			 ; keep start of list
	move.w	#$0108,d1		 ; find standard form (0100 for mono!)
	and.b	pt_dmode(a3),d1
	lsr.b	#3,d1			 ; 0100 or 0101

ptc_srstrt
	moveq	#0,d0			 ; assume sprite isn't dynamic
ptc_sslp
	cmp.w	pto_form(a1),d1 	 ; get the form
	bne.s	ptc_cnext		 ; not acceptable, try next
	tst.b	d7			 ; pointer sprite?
	bpl.s	ptc_setp		 ; ... no
	move.b	pto_vers(a1),d0 	 ; get version number
	beq.s	ptc_setp		 ; not dynamic, just use it
	cmp.b	pt_svers(a3),d0 	 ; OK for use now?
	bhi.s	ptc_setp		 ; ... yes
ptc_cnext
	move.l	pto_nobj(a1),d4 	 ; next object
	beq.s	ptc_nosprite		 ; there are no more
	lea	pto_nobj(a1,d4.l),a1	 ; there it is
	bra.s	ptc_sslp		 ; so try it

ptc_nosprite
	tst.b	d0			 ; .. no, is this object dynamic?
	beq.s	ptc_default		 ; .... no, there isn't an OK one, tough
	clr.b	pt_svers(a3)		 ; .... yes, restart version counter
	move.l	a0,a1			 ; and scan list again
	bra.s	ptc_srstrt

ptc_default
	lea	sp_arrow,a1
	bra.s	ptc_srstrt

ptc_setp
	move.l	a1,d2			 ; good version

	movem.l (sp)+,ptcc.reg
	moveq	#0,d0
	rts

	end
