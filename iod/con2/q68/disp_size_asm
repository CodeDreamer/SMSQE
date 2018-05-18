; Q68 set display size	 1.00
; based on
; Q40 set display size	        2000  Tony Tebby

	section con

	xdef	cn_disp_size

	xref	cn_disp_clear

	include 'dev8_keys_q68'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_mac_assert'

;+++
; Set size and colour depth according to D6 and D7
;
;	d6 c  p size requested (Q68 colour mode register value)
;	d7 c  p (word) colour depth requested
;	a3 c  p pointer to console linkage
;
;---
cn_disp_size
cnds.reg reg	d1/d2/d3/d4
	movem.l cnds.reg,-(sp)
	move.b	d6,q68_dmode		 ; set display mode
	move.b	d6,sys_qlmr(a6)
	moveq	#0,d1
	move.b	d6,d1
	lsl.w	#3,d1

;***
;	 btst	 #mc..scrb,mc_stat
;	 bne.s	 chk_2ndscr

	movem.w q68_sizes(pc,d1.w),d1/d2/d3/d4
	swap	d1
	clr.w	d1			 ; base of screen
cont2	move.w	d2,d0
	mulu	d4,d0			 ; size of screen
	move.l	d1,pt_scren(a3)
	move.l	d0,pt_scrsz(a3)
	move.w	d2,pt_scinc(a3)
	move.w	d3,pt_xscrs(a3)
	move.w	d4,pt_yscrs(a3)
	jsr	cn_disp_clear

	moveq	#0,d0
	movem.l (sp)+,cnds.reg
	rts

;chk_2ndscr  (test when there is a second screen)
;	 cmp.w	 #8,d1
;	 bne.s	 cont
;	 moveq	 #-6,d1
;	 movem.w q68_sizes(pc,d1.w),d2/d3/d4
;	 move.l  #$28000,d1
;	 bra.s	 cont2

	dc.w	128,512,256
q68_sizes
	dc.w	$2,128,512,256		; ql mode 8		   0
	dc.w	$2,128,512,256		; ql mode 4		   1
	dc.w	$FE80,1024,512,256	; small 16 bit screen	   2
	dc.w	$FE80,2048,1024,512	; large 16 bit screen	   3
	dc.w	$FE80,256,1024,768	; large QL Mode 4 screen   4
	dc.w	$FE80,1024,1024,768	; auora 8 bit		   5
	dc.w	$FE80,1024,512,384	; medium 16 bit screen	   6
	dc.w	$fe80,2048,1024,768	; huge 16 bit		   7
	end
