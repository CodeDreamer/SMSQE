; retry/quit selection			     1991 Jochen Merz	V001

	section utility

	 include  dev8_keys_thg
	 include  dev8_mac_text
	 include  dev8_keys_menu
	 include  dev8_mac_xref

	 xdef	  mu_retry

;+++
; This routine pops up a menu, displays a message and waits for a yes or
; no user selection.
;
;		Entry			Exit
;	D0.l				error, 0 or +1 for ESC
;	D1.l	origin or -1		preserved
;	D2.b	colour
;	D3.l	set if default retry	0 retry, 1 quit
;	A0	window title		preserved
;	A2	window message		preserved
;---
stk.frm  equ	  $44
yno_reg  reg	  d4/a0-a5
retry_txt
	 mkstr	 {Retry}
	 ds.w	  0
quit_txt
	 mkstr	 {Quit}
	 ds.w	  0

mu_retry
	 movem.l  yno_reg,-(sp)
	 sub.l	  #stk.frm,sp
	 move.l   sp,a5 		     ; prepare workspace

	 movem.l  d1-d3,-(sp)
	 move.l   #'ITSL',d2
	 xbsr	  ut_usmen
	 movem.l  (sp)+,d1-d3

	 bne.s	  yno_error		     ; cannot use menu
	 move.l   a1,a4 		     ; that's the Thing

	 move.l   #(thp.call+thp.str)<<16,d4
	 move.l   d4,is_mennm-4(a5)	     ; menu name is a string
	 move.l   a0,is_mennm(a5)	     ; window title

	 move.l   d4,is_prmpt-4(a5)	     ; promt is a string
	 move.l   a2,is_prmpt(a5)	     ; request string

	 lea	  quit_txt,a0
	 lea	  retry_txt,a2
	 tst.b	  d3
	 bne.s	  def_yes		     ; default is yes !!
def_no
	 move.l   d4,is_item1-4(a5)	     ; item 1 is a string
	 move.l   a0,is_item1(a5)	     ; item1   no

	 move.l   d4,is_item2-4(a5)	     ; item 2 is a string
	 move.l   a2,is_item2(a5)	     ; item2   yes

	 bra.s	  yeno_cont
def_yes
	 move.l   d4,is_item1-4(a5)	     ; item 1 is a string
	 move.l   a2,is_item1(a5)	     ; item1   yes

	 move.l   d4,is_item2-4(a5)	     ; item 2 is a string
	 move.l   a0,is_item2(a5)	     ; item2   no
yeno_cont
	 clr.l	  is_item3(a5)		     ; no third item
	 swap	  d1
	 move.l   d1,is_xpos(a5)	     ; x-position

	 swap	  d1
	 move.l   d1,is_ypos(a5)	     ; y-position

	 and.l	 #$ff,d2		     ; only low byte
	 move.l   d2,is_mainc(a5)	     ; colourway

	 move.l   #(thp.ret+thp.ulng)<<16,is_itnum-4(a5)  ; ret param is long
	 lea.l	  $40(a5),a2
	 move.l   a2,is_itnum(a5)

	 move.l   a5,a1 		     ; the parameter table
	 jsr	  thh_code(a4)		     ; get item select via menu

	 xbsr	  ut_frmen		     ; and free it

	 move.l   $40(a5),d3
	 subq.l   #1,d3

yno_error
	 add.l	  #stk.frm,sp
	 movem.l  (sp)+,yno_reg
	 tst.l	  d0
	 rts


	 end
