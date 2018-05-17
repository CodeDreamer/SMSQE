;	 LIST SELECT		    V0.03

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref

		section  utility

		xdef	   mu_list

;+++
; This routine use LIST
;
;		Entry			     Exit
;	D0.l				     error
;	D1.l	origin or -1		     preserved
;	D2.l	sub-colour.w | colour.w      preserved
;	D3.l	item at start|		     item-number
;		menu-item-control	     or return string,-1= ESC,-2 = OK
;	D4.l	max number of lines|	     preserved
;		max number of columns
;	A0	menu-name		     preserved
;	A2	item status-list (or 0)      preserved
;	A3	item-list		     preserved
;---

listreg 	   reg	    d2/d4-d6/a0-a5

stk.frm  equ	  $44

mu_list
	 movem.l  listreg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a5 		     ; prepare workspace

	 movem.l  d1-d3,-(sp)
	 move.l   #'LIST',d2		     ; we want an SELECT FROM LIST
	 xbsr	  ut_usmen		     ; use MENU thing
	 movem.l  (sp)+,d1-d3
	 bne	  list_rts		     ; failed, so return error

	 move.l   a1,a4 		     ; address of Thing

	 move.l   #(thp.call+thp.str)<<16,ls_mennm-4(a5)
	 move.l   a0,ls_mennm(a5)	     ; menu name

	 and.l	  #$00ff00ff,d2 	     ; only byte
	 move.l   d2,ls_mainc(a5)	     ; main colourway

	 swap	  d2
	 move.l   d2,ls_listc(a5)	     ; list colourways

	 move.l   #(thp.call+thp.str+thp.arr)<<16,d2 ; prepare item list type
	 move.w   #0,d2 		     ; prepare array typ 'absolut'
	 move.l   d2,ls_itlty(a5)	     ; ... and save it

	 move.l   a3,ls_itlst(a5)	     ; item list

	 move.l   #(thp.upd+thp.arr+thp.uwrd)<<16,d2 ; prepare status list type
	 move.w   #0,d2 		     ; prepare array typ 'absolut'
	 move.l   d2,ls_stlty(a5)	     ; ... and save it

	 move.l   a2,ls_stlst(a5)	     ; item status list (or 0)

	 move.l   d3,ls_mictl(a5)	     ; menu-item-control

	 swap	  d4
	 move.w   d4,ls_lines+2(a5)	     ; max number of lines (or 0)

	 swap	  d4
	 move.w   d4,ls_colms+2(a5)	     ; max numbers of columns (or 0)

	 swap	  d1
	 move.l   d1,ls_xpos(a5)	     ; x-position

	 swap	  d1
	 move.l   d1,ls_ypos(a5)	     ; y-position

	 move.l   #(thp.ret+thp.uwrd)<<16,ls_item-4(a5)
	 lea	  $40(a5),a2
	 swap	  d3
	 move.w   d3,(a2)		     ; insert item at start
	 move.l   a2,ls_item(a5)	     ; pointer to return parameter
	 swap	  d3

	 move.l   a5,a1
	 jsr	  thh_code(a4)		     ; call the Menu

	 xbsr	  ut_frmen		     ; and free Menu Extension

	 lea	  $40(a5),a0
	 move.w   (a0),d3		     ; the item-number

list_rts
	 add.l	  #stk.frm,sp		     ; adust stack
	 movem.l  (sp)+,listreg
	 tst.l	d0
	 rts


	 end
