; QL mode sprite cache		V2.02		      1998  Tony Tebby
;						       2003  Marcel Kilgus
;
; 2003-02-17  2.01  Extracted code for pt_fsprd
; 2004-04-02  2.02  Returns sp_error sprite if no fitting sprite can be found

	section driver

	xdef	pt_fspr
	xdef	pt_fsprd
	xdef	pt_cchset
	xdef	pt_cchloc

	xdef	pt.spxsw
	xdef	pt.rpxsw
	xdef	pt.spsln
	xdef	pt.spcln

	xref	pt_ssref
	xref	sp_error

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_sysspr'

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
; pv_fspr vector call
;
;	In			   Out
; A1.l	ptr to 1st sprite	   ptr to fitting sprite
; A3.l	ptr to CON linkage block
;---
pt_fspr
	movem.l d3/d7,-(sp)
	moveq	#0,d7
	jsr	pt_ssref
	bsr.s	pt_fsprd
	movem.l (sp)+,d3/d7
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
	bsr	pt_fsprd
	move.l	a1,d2			 ; good version
	rts

;+++
; Find fitting sprite/blob/pattern definition in linked list
;
;	d0  r	error code
;	a1 cr	pointer to 1st definition / pointer to fitting definition
;	a3 c  p pointer to pointer linkage
;---
pt_fsprd
ptf.reg reg    d1/d4/d7/a0
	movem.l ptf.reg,-(sp)

ptf_sysspr
	jsr	pt_ssref		 ; handle system sprite references

	move.b	d0,d7			 ; pointer sprite flag
	moveq	#0,d2			 ; no suitable object found
	move.l	a1,a0			 ; keep start of list
	move.w	#$0108,d1		 ; find standard form (0100 for mono!)
	and.b	pt_dmode(a3),d1
	lsr.b	#3,d1			 ; 0100 or 0101

ptf_srstrt
	moveq	#0,d0			 ; assume sprite isn't dynamic
ptf_sslp
	cmp.w	pto_form(a1),d1 	 ; get the form
	bne.s	ptf_cnext		 ; not acceptable, try next
	tst.b	d7			 ; pointer sprite?
	bpl.s	ptf_setp		 ; ... no
	move.b	pto_vers(a1),d0 	 ; get version number
	beq.s	ptf_setp		 ; not dynamic, just use it
	cmp.b	pt_svers(a3),d0 	 ; OK for use now?
	bhi.s	ptf_setp		 ; ... yes
ptf_cnext
	move.l	pto_nobj(a1),d4 	 ; next object
	beq.s	ptf_nosprite		 ; there are no more
	lea	pto_nobj(a1,d4.l),a1	 ; there it is
	bra.s	ptf_sslp		 ; so try it

ptf_nosprite
	tst.b	d0			 ; .. no, is this object dynamic?
	beq.s	ptf_default		 ; .... no, there isn't an OK one, tough
	clr.b	pt_svers(a3)		 ; .... yes, restart version counter
	move.l	a0,a1			 ; and scan list again
	bra.s	ptf_srstrt

ptf_default
	lea	sp_error,a1		 ; oops, error, return error sprite
	bra.s	ptf_sysspr

ptf_setp
	movem.l (sp)+,ptf.reg
	moveq	#0,d0
	rts

	end
