; dev8_uti_cv_hexda_asm

	 include dev8_keys_err

	 section utility

	 xdef	  cv_hexda

	
;+++
; convert hex-long to decimal-ascii
;
;	 ENTRY		   EXIT
;   D0			   error
;   D1.l hex		   preserved
;   D2.w how many bytes    smashed
;   A1	 buffer 	   preserved
;
;   Error return: err.imem if d2 too high
;	  Condition code set
;---
cvreg	 reg   d1-d3/a2-a3/a6
stfr	 equ   $20

cv_hexda
	 movem.l  cvreg,-(sp)
	 sub.l	  #stfr,sp
	 move.l   sp,a6 		     ; prepare workspace
	 moveq	  #err.imem,d0		     ;
	 move.l   d2,d3 		     ; copy how many byrtes
	 sub.l	  #stfr-8,d3
	 bhi.s	  convert_rts		     ; return with error

	 move.l   a1,a2 		     ; copy addr. of buffer
	 move.l   d2,d3 		     ; how many bytes
	 bra.s	  blank_loop_end
blank_loop
	 move.b   #' ',(a1)+
blank_loop_end
	 dbra	  d3,blank_loop 	     ; buffer now only blanks

	 lea	  $2(a6),a1		     ; our workspace
	 move.l   d2,d3 		     ; how many bytes
	 bra.s	  zero_loop_end
zero_loop
	 move.b   #'0',(a1)+
zero_loop_end
	 dbra	  d3,zero_loop		     ; workspace now only zeros

	 move.l   a2,a1 		     ; restore buffer
	 add.l	  #2,a1 		     ; .w  number of chars
	 lea	  $2(a6),a3
	 move.w   d2,-(sp)
	 move.w   d2,d3 		     ; copy how many bytes
	 bsr.s	  help_div
	 bra.s	  convert_loop_end
convert_loop
	 move.b   -(a1),$0(a3,d3.w)
convert_loop_end
	 subq.w   #1,d3 		     ; sub how many bytes
	 cmpa.l   a1,a2
	 bne.s	  convert_loop

	 move.w   (sp)+,d2		     ; restore how many bytes
	 move.w   d2,(a1)		     ; number of chars
	 moveq	  #0,d0 		     ; no error

convert_rts
	 add.l	  #stfr,sp		     ; adjust stack
	 movem.l  (sp)+,cvreg
	 tst.l	  d0
	 rts

help_div
	 divu	  #1000,d1
	 clr.l	  d2
	 move.w   d1,d2
	 bsr.s	  help_d1
	 swap	  d1
	 move.w   d1,d2
help_d1
	 movem.l  d2,-(a7)
	 divu	  #100,d2
	 ori.b	  #'0',d2
	 move.b   d2,(a1)+
	 swap	  d2
	 andi.l   #$ffff,d2
	 divu	  #10,d2
	 ori.b	  #'0',d2
	 move.b   d2,(a1)+
	 swap	  d2
	 andi.l   #$ffff,d2
	 ori.b	  #'0',d2
	 move.b   d2,(a1)+
	 movem.l  (a7)+,d2
	 rts



	end
