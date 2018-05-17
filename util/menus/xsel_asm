; Select extension via XSEL-Select		 V0.03

	include dev8_keys_thg
	include dev8_keys_menu
	include dev8_mac_xref

	section utility

	xdef	mu_xsel
;+++
; This routine use extension-select
;		Entry			     Exit
;	D0.l				     error, 0 or +1 for ESC
;	D1.l	origin or -1		     preserved
;	D2.b	colourways
;	A4	string buffer		     preserved
;---
stk.frm  equ	  $20
xselreg  reg	  d1-d4/a0-a6

mu_xsel
	 movem.l  xselreg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a6 		     ; prepare stack

	 movem.l  d1-d3,-(sp)
	 move.l   #'XSEL',d2		     ; we want a extension SELECT
	 xbsr	  ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d3
	 bne.s	  xsel_rts		     ; failed, so return error

	 swap	  d1
	 move.l   d1,xs_xpos(a6)	     ; x-position

	 swap	  d1
	 move.l   d1,xs_ypos(a6)

	 and.l	  #$ff,d2		     ; only low byte
	 move.l   d2,xs_mainc(a6)	     ; main colourway

	 move.l   #(thp.ret+thp.str)<<16,ds_fname-4(a6)  ; return param is a string
	 move.l   a4,xs_fname(a6)	     ; pointer to return parameter

	 move.l   a1,a4 		     ; address of Thing
	 move.l   a6,a1
	 jsr	  thh_code(a4)		     ; call the Menu

	 xbsr	  ut_frmen		     ; and free Menu Extension
xsel_rts
	 add.l	  #stk.frm,sp		     ; adjust stack
	 movem.l  (sp)+,xselreg
	 tst.l	  d0
	 rts

	 end
