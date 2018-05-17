; VIEW File		    V001

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref

	section utility

	xdef	mu_view

;+++
; This routine use view
;		Entry			     Exit
;	D0.l				     error-code
;	D2.b	colourways
;	D4.l	start-string		     preserved
;	D5.l	end-string		     preserved
;	A0	file-name		     preserved
;---
stk.frm  equ	  $24
viewreg  reg	  d1-d4/a0-a6

mu_view
	 movem.l  viewreg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a6 		     ; prepare stack

	 movem.l  d1-d4,-(sp)
	 move.l   #'VIEW',d2		     ; we want VIEW
	 xbsr	  ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d4
	 bne.s	  view_rts		     ; failed, so return error

	 move.l   #(thp.call+thp.str)<<16,d1
	 move.l   d1,vw_filnm-4(a6)	     ; file name is a string
	 move.l   a0,vw_filnm(a6)	     ; file name

	 move.l   d1,vw_start-4(a6)	     ; start string is a string
	 move.l   d4,vw_start(a6)	     ; start string

	 move.l   d1,vw_end-4(a6)	     ; end string is a string
	 move.l   d5,vw_end(a6) 	     ; end string

	 move.l   #0,vw_chars(a6)	     ; number of lines

	 and.l	  #$ff,d2		     ; only low byte
	 move.l   d2,vw_mainc(a6)	     ; main colourway
	 move.l   d2,vw_listc(a6)	     ; file colourway

	 move.l   a1,a4 		     ; address of Thing
	 move.l   a6,a1
	 jsr	  thh_code(a4)		     ; call the Menu

	 xbsr	  ut_frmen		     ; and free Menu Extension
view_rts
	 add.l	  #stk.frm,sp		     ; adjust stack
	 movem.l  (sp)+,viewreg
	 tst.l	  d0
	 rts


	 end
