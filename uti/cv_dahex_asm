; dev8_uti_cv_dahex_asm


	 include dev8_keys_err


	 section utility


	 xdef	  cv_dahex

	
;+++
; convert decimal-ascii to long-hex
;
;	 ENTRY		   EXIT
;   D0			   error
;   D3.w how many bytes    smashed
;   A1	 source string	   preserved
;   A5.l buffer 	   preserved
;
;   Error return: err.ovfl   no decimal-ascii
;		  Condition code set
;---
cvreg	 reg  d1-d7/a1
cv_dahex
	 movem.l  cvreg,-(sp)
	 moveq	  #err.ovfl,d0
	 clr.l	  d1
	 clr.l	  d4
	 bra.s	  bti_loop_end
bti_loop
	 move.b   (a1)+,d4
	 cmpi.b   #'0',d4
	 blt.s	  cv_rts		      ; error, less '0'
	 cmpi.b   #'9',d4
	 bgt.s	  cv_rts		      ; error, higher '9'
	 subi.b   #'0',d4
	 move.l   d1,d6
	 add.l	  d6,d1
	 lsl.l	  #3,d6
	 add.l	  d6,d1
	 add.l	  d4,d1

bti_loop_end
	 dbra	  d3,bti_loop		     ; loop arround
	 move.l   d1,(a5)
	 moveq	  #0,d0 		     ; no error
cv_rts
	 movem.l  (sp)+,cvreg
	 tst.l	  d0
	 rts



	end
