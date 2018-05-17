*	  32 / 16 bit division

;;;	     module    div3216
;;;	     pagelen   74
	  section   utility
*
	  xdef	    fn_div
;---------------------------------------------------------------------------
;
; Division 32 bit durch 16 bit	(D1 / D2 = D1 rest D2)
;
;	  Eingabe				  Ausgabe
;
;
; d0						  bleibt
; d1	  Divident				  Quotient
; d2	  Divisor				  Rest
; d3+						  bleiben
;
; a0+						  bleiben
;
;---------------------------------------------------------------------------
*
divreg	  reg	    d4/d5
*
fn_div	  movem.l   divreg,-(sp)
	  clr.l     d4
	  divu	    d2,d1
	  bvc.s     umsp
	  move.l    d1,d3
	  clr.w     d1
	  swap	    d1
	  divu	    d2,d1
	  move.w    d1,d4
	  move.w    d3,d1
	  divu	    d2,d1
umsp	  move.l    d1,d2
	  swap	    d1
	  move.w    d4,d1
	  swap	    d1
	  clr.w     d2
	  swap	    d2
	  movem.l   (sp)+,divreg
	  rts
*
	  end
