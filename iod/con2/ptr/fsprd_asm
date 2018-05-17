; Find fitting sprite definition	       V1.00   1999 Tony Tebby
; High colour version					2003 Marcel Kilgus
;
; 2004-04-02	1.01	returns sp_error sprite if no fitting sprite can be found (wl)
	section driver

	include 'dev8_keys_con'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_sysspr'

	xdef	pt_fsprd
	xdef	pt_fspr

	xref	pt_ssref
	xref.l	pt.sppref
	xref	pt_sppref
	xref	sp_error

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
; Find fitting sprite/blob/pattern definition in linked list
;
;	d3  r	index in pt_sppref
;	d7 c  p -ve: object is pointer (possibly dynamic)
;	a1 cr	pointer to 1st definition / pointer to fitting definition
;	a3 c  p pointer to pointer linkage
; this routine always succeeds - if no correct sprite can be found, the
; sp_error sprite is returned
;---

pt_fsprd
reglist reg	d1/d2/d4/d5/a0/a2
	movem.l reglist,-(sp)
	moveq	#-1,d3			 ; no suitable object found
	move.l	a1,a0			 ; keep start of list

ptf_srstrt
	moveq	#0,d0			 ; assume sprite isn't dynamic
ptf_sslp
	move.w	pto_form(a1),d4 	 ; get the form
	moveq	#pt.sppref-1,d1
	lea	pt_sppref,a2
ptf_prloop
	cmp.w	(a2)+,d4		 ; look for acceptable form
	dbeq	d1,ptf_prloop
	bne.s	ptf_cnext		 ; not acceptable, try next
	tst.b	d7			 ; pointer sprite?
	bpl.s	ptf_psave		 ; ... no
	move.b	pto_vers(a1),d0 	 ; get version number
	beq.s	ptf_psave		 ; not dynamic, just use it
	cmp.b	pt_svers(a3),d0 	 ; OK for use now?
	bls.s	ptf_cnext		 ; ... no
ptf_psave
	cmp.w	d3,d1			 ; is this one better than last?
	ble.s	ptf_cnext		 ; ... no
	move.l	a1,d2			 ; new good version
	move.w	d1,d3
ptf_cnext
	move.l	pto_nobj(a1),d5 	 ; next object
	beq.s	ptf_cfound		 ; there are no more
	lea	pto_nobj(a1,d5.l),a1	 ; there it is
	bra.s	ptf_sslp		 ; so try it

ptf_cfound
	tst.w	d3			 ; any suitable sprite found?
	bpl.s	ptf_exit
					 ; .. yes
	tst.b	d0			 ; .. no, is this object dynamic?
	beq.s	ptf_default		 ; .... no, there isn't an OK one, tough
	clr.b	pt_svers(a3)		 ; .... yes, restart version counter
	move.l	a0,a1			 ; and scan list again
	bra.s	ptf_srstrt

ptf_default
	lea	sp_error,a1		 ; oops, error, return error sprite
	bra.s	ptf_srstrt

ptf_exit
	move.l	d2,a1
	movem.l (sp)+,reglist
	moveq	#0,d0			 ; routine always succeeds
	rts

	end
