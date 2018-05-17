; CHARACTER SELECT	V001

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref


	section utility


	xdef	mu_chsl

;+++
; This routine use CHSL
;
;		Entry			     Exit
;	D0.l				     error, 0 or +1 for ESC
;	D1.l	origin or -1		     preserved
;	D2.l	sub-colour.w | colour.w
;	D4.l	availability type
;	A0	menu title		     preserved
;	A2	font used for subwindow      preserved
;	A4	string buffer		     preserved
;---
stk.frm  equ	  $40
chslreg  reg	  d1-d4/a0-a5

mu_chsl
	 movem.l  chslreg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a5 		     ; prepare stack

	 movem.l  d1-d2,-(sp)
	 move.l   #'CHSL',d2		     ; we want a CHARACTER SELECT
	 xbsr	   ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d2
	 bne.s	  chsl_rts		     ; failed, so return error

	 move.l   #(thp.call+thp.opt+thp.str)<<16,ch_mennm-4(a5) ; menu name is a string
	 move.l   a0,ch_mennm(a5)	     ; menu name

	 move.l   d4,ch_avbit(a5)	     ; availability bits

	 move.l   #(thp.call+thp.opt+thp.arr+thp.uwrd)<<16,ch_avlbl-4(a5)
	 move.l   #0,ch_avlbl(a5)	     ; no availabitility table

	 move.l   a2,ch_font(a5)	     ; font used for subwindow

	 swap	  d1
	 move.l   d1,ch_xpos(a5)	     ; x-position

	 swap	  d1
	 move.l   d1,ch_ypos(a5)

	 and.l	  #$00ff00ff,d2 	     ; only byte
	 move.l   d2,ch_mainc(a5)	     ; main colourway

	 swap	  d2
	 move.l   d2,ch_listc(a5)	     ; list colourway

	 move.l   #(thp.ret+thp.str)<<16,ch_retst-4(a5)
	 
	 move.l   a4,ch_retst(a5)	     ; pointer to return parameter

	 move.l   a1,a4 		     ; address of Thing
	 move.l   a5,a1
	 jsr	  thh_code(a4)		     ; call the Menu

	 xbsr	  ut_frmen		     ; and free Menu Extension
chsl_rts
	 add.l	  #stk.frm,sp		     ; adjust stack
	 movem.l  (sp)+,chslreg
	 tst.l	  d0
	 rts


	end
