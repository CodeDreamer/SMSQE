; Q40 set display size	        2000  Tony Tebby

	section con

	xdef	cn_disp_size

	xref	cn_disp_clear

	include 'dev8_keys_q40'
	include 'dev8_keys_con'
	include 'dev8_mac_assert'

;+++
; Set size and colour depth according to D6 and D7
;
;	d6 c  p size requested (Q40 colour mode register value)
;	d7 c  p (word) colour depth requested
;	a3 c  p pointer to console linkage
;
;---
cn_disp_size
cnds.reg reg	d1/d2/d3/d4
	movem.l cnds.reg,-(sp)

	move.b	d6,q40_dmode		 ; set display mode

	moveq	#0,d1
	move.b	d6,d1
	lsl.w	#3,d1
	movem.w q40_sizes(pc,d1.w),d1/d2/d3/d4

	swap	d1
	clr.w	d1			 ; base of screen
	move.w	d2,d0
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

q40_sizes
	dc.w	$2,128,512,256
	dc.w	$2,128,512,256
	dc.w	$FE80,1024,512,256
	dc.w	$FE80,2048,1024,512

	end
