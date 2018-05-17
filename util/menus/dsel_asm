; Select file via DSEL-Select	   V001

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref

	section utility

	xdef	mu_dsel

;+++
; This routine use directory-select
;		Entry			     Exit
;	D0.l				     error, 0 or +1 for ESC
;	D1.l	origin or -1		     preserved
;	D2.l	sub-colour.w | colour.w
;	A0	menu title		     preserved
;	A4	string buffer		     preserved
;---
stk.frm  equ	  $40
dselreg  reg	  d1-d4/a0-a6

mu_dsel
	 movem.l  dselreg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a6 		     ; prepare stack

	 movem.l  d1-d3,-(sp)
	 move.l   #'DSEL',d2		     ; we want a DIRECTORY SELECT
	 xbsr	  ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d3
	 bne.s	  dsel_rts		     ; failed, so return error
	 
	 move.l   #(thp.call+thp.str)<<16,ds_mennm-4(a6)
	 move.l   a0,ds_mennm(a6)	     ; menu name

	 move.l   #0,ds_lines(a6)	     ; number of lines
	 swap	  d1
	 move.l   d1,ds_xpos(a6)	     ; x-position

	 swap	  d1
	 move.l   d1,ds_ypos(a6)

	 and.l	  #$00ff00ff,d2 	     ; only byte
	 move.l   d2,ds_mainc(a6)	     ; main colourway

	 swap	  d2
	 move.l   d2,ds_ddirc(a6)	     ; directory colourway

	 move.l   #(thp.ret+thp.str)<<16,ds_fname-4(a6)  ; return param is a string
	 move.l   a4,ds_fname(a6)	     ; pointer to return parameter

	 move.l   a1,a4 		     ; address of Thing
	 move.l   a6,a1

	 jsr	  thh_code(a4)		     ; call the Menu
	 tst.l	  d0
	 bmi.s	  dsel_cont		     ; error

	 bne.s	  save_esc		     ; esc or no directory selected ??

	 move.l   ds_fname(a6),a0
	 move.w   (a0),d0		     ; no file selected ?
	 beq.s	  save_esc		     ; save ESC

	 moveq	  #0,d0 		     ; clear d0
	 bra.s	  dsel_cont

save_esc
	 moveq	  #1,d0 		     ; say it's ESC!!

dsel_cont
	 xbsr	  ut_frmen		     ; and free Menu Extension

dsel_rts
	 add.l	  #stk.frm,sp		     ; adjust stack
	 movem.l  (sp)+,dselreg
	 tst.l	  d0
	 rts


	end
