; Select file via FILE-Select	  V0.04

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref


	section utility


	xdef	mu_fsel

;+++
; This routine use file-select
;
;		Entry			     Exit
;	D0.l				     error, 0 or +1 for ESC or no string
;	D1.l	origin or -1		     preserved
;	D2.l	sub-colour.w | colour.w
;	D4.l	extension name		     preserved
;	D5.l	number of lines 	     preserved
;	A0	menu title		     preserved
;	A2	default name		     preserved
;	A3	directory name		     preserved
;	A4	string buffer		     preserved
;---
stk.frm  equ	  $40
filereg  reg	  d1-d5/a0-a6

mu_fsel
	 movem.l  filereg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a6 		     ; prepare stack

	 movem.l  d1-d2,-(sp)
	 move.l   #'FSEL',d2		     ; we want a FILE SELECT
	 xbsr	   ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d2
	 bne.s	  file_rts		     ; failed, so return error

	 move.l   #(thp.call+thp.str)<<16,d3
	 move.l   d3,fs_mennm-4(a6)	     ; menu name is a string
	 move.l   a0,fs_mennm(a6)	     ; menu name

	 move.l   d3,fs_defnm-4(a6)	     ; default name is a string
	 move.l   a2,fs_defnm(a6)	     ; default name

	 move.l   #(thp.upd+thp.str)<<16,fs_dirnm-4(a6)    ; string and please update
	 move.l   a3,fs_dirnm(a6)	     ; directory name

	 move.l   #(thp.upd+thp.str)<<16,fs_extnm-4(a6)    ; string and please update
	 move.l   d4,fs_extnm(a6)	     ; extension name

	 move.l   d5,fs_lines(a6)	     ; number of lines

	 swap	  d1
	 move.l   d1,fs_xpos(a6)	     ; x-position

	 swap	  d1
	 move.l   d1,fs_ypos(a6)

	 and.l	  #$00ff00ff,d2 	     ; only byte
	 move.l   d2,fs_mainc(a6)	     ; main colourway

	 swap	  d2
	 move.l   d2,fs_filec(a6)	     ; file colourway

	 move.l   #(thp.ret+thp.str)<<16,fs_fname-4(a6)  ; return param is a string
	 move.l   a4,fs_fname(a6)	     ; pointer to return parameter

	 move.l   a1,a4 		     ; address of Thing
	 move.l   a6,a1
	 jsr	  thh_code(a4)		     ; call the Menu
	 tst.l	  d0
	 bmi.s	  file_cont		     ; error

	 bne.s	  save_esc		     ; esc or no file selected ??

	 move.l   fs_fname(a6),a0
	 tst.w	  (a0)			     ; no file selected ?
	 beq.s	  save_esc		     ; save ESC

	 moveq	  #0,d0 		     ; clear d0
	 bra.s	  file_cont

save_esc
	 moveq	  #1,d0 		     ; say it's ESC!!

file_cont
	 xbsr	  ut_frmen		     ; and free Menu Extension

file_rts
	 add.l	  #stk.frm,sp		     ; adjust stack
	 movem.l  (sp)+,filereg
	 tst.l	  d0
	 rts


	end
