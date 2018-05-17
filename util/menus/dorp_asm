; Do action and Report	     V001

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref


	section utility


	xdef	mu_dorp

;+++
; This routine use Do action and Report
;
;		Entry			     Exit
;	D0.l				     error, 0 or +1 for ESC
;	D1.l	origin or -1		     preserved
;	D2.b	colourways		     preserved
;	D3.l	control bits		     preserved
;	D4.l	number of chars 	     preserved
;	A0	menu title		     preserved
;	A2	description of action	     preserved
;	A3	action routine		     preserved
;---
stk.frm  equ	  $40
dorpreg  reg	  d1-d4/a0-a5

mu_dorp
	 movem.l  dorpreg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a5 		     ; prepare stack

	 movem.l  d1-d4,-(sp)
	 move.l   #'DORP',d2		     ; we want a Do action and Report
	 xbsr	   ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d4
	 bne.s	  dorp_rts		     ; failed, so return error

	 move.l   #(thp.call+thp.str)<<16,do_mennm-4(a5) ; menu name is a string
	 move.l   a0,do_mennm(a5)	     ; menu name

	 move.l   #(thp.call+thp.str)<<16,do_actds-4(a5) ; action description is a string
	 move.l   a2,do_actds(a5)	     ; action description

	 move.l   d4,do_chars(a5)	     ; number of chars
	 swap	  d1
	 move.l   d1,do_xpos(a5)	     ; x-position

	 swap	  d1
	 move.l   d1,do_ypos(a5)

	 and.l	 #$ff,d2		     ; only low byte
	 move.l   d2,do_mainc(a5)	     ; main colourway
	 move.l   d3,do_mictl(a5)	     ; menu item control
	 move.l   a3,do_pact(a5)	     ; action routine

	 move.l   a1,a4 		     ; address of Thing
	 move.l   a5,a1
	 jsr	  thh_code(a4)		     ; call the Menu

	 xbsr	  ut_frmen		     ; and free Menu Extension
dorp_rts
	 add.l	  #stk.frm,sp		     ; adjust stack
	 movem.l  (sp)+,dorpreg
	 tst.l	  d0
	 rts


	end
