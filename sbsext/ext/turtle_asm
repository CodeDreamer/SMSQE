; Turtle Graphics	 V2.00	    1990  Tony Tebby	QJUMP
;
	section exten

	xdef	turn
	xdef	turnto
	xdef	move
	xdef	penup
	xdef	pendown

	xref	ut_gxfp1		  ; get one floating point
	xref	ut_chan1
	xref	ut_chkri

	xref	pi_d180

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'

turn
	bsr.s	tu_parms
	bne.s	tu_rts

	bsr.s	tu_pang 		 ; present angle
	moveq	#qa.add,d0
	bsr.s	tu_op			 ; add
	move.l	#08095400,d0		 ; 360
	tst.w	2(a6,a1.l)		 ; negative result?
	bmi.s	tu_add360		 ; ... yes
	cmp.l	(a6,a1.l),d0		 ; >360?
	bhs.s	tu_setang
	bsr.s	tu_pu360
	moveq	#qa.sub,d0
	bsr.s	tu_op			 ; ... yes, reduce it
	bra.s	tu_setang

tu_add360
	bsr.s	tu_pu360
	moveq	#qa.add,d0
	bsr.s	tu_op			 ; negative, bump it up
	bra.s	tu_setang

tu_pu360
	subq.l	#6,a1
	clr.w	4(a6,a1.l)
	move.l	d0,(a6,a1.l)
	rts

turnto
	bsr.s	tu_parms
	bne.s	tu_rts
tu_setang
	move.w	(a6,a1.l),ch_angle(a6,d4.l)
	move.l	2(a6,a1.l),ch_angle+2(a6,d4.l)
tu_ok
	moveq	#0,d0
tu_rts
	rts

tu_pang
	subq.l	#6,a1
	move.w	ch_angle(a6,d4.l),(a6,a1.l)
	move.l	ch_angle+2(a6,d4.l),2(a6,a1.l)
	rts

tu_op
	jmp	qa.op*3+qlv.off

penup
	moveq	#0,d7
	bra.s	tu_pdo
pendown
	moveq	#1,d7
tu_pdo
	jsr	ut_chan1
	bne.s	tu_rts
	move.b	d7,ch_pdwn(a6,d4.l)
	bra.s	tu_ok

; get paramaters
;
;	a0	channel ID
;	(a6,a1) params
;	(a6,d4) channel table entry

tu_parms
	jsr	ut_chan1		 ; get channel
	bne.s	tu_rts
	jsr	ut_gxfp1		 ; one param only
	bne.s	tu_rts
	move.l	#256,d1
	jsr	ut_chkri		 ; room on RI stack
	moveq	#0,d0
	rts

move
	jsr	tu_parms		 ; length
	bne.s	tu_rts
	jsr	tu_pang 		 ; angle
	lea	pi_d180,a2
	subq.l	#6,a1
	move.w	(a2)+,(a6,a1.l)
	move.l	(a2),2(a6,a1.l) 	 ; scale
	moveq	#qa.mul,d0
	bsr	tu_op

	sub.w	#12,a1
	move.w	ch_grphx(a6,d4.l),6(a6,a1.l)	  ; set x coordinate
	move.l	ch_grphx+2(a6,d4.l),8(a6,a1.l)
	move.w	ch_grphy(a6,d4.l),(a6,a1.l)	 ; set y coordinate
	move.l	ch_grphy+2(a6,d4.l),2(a6,a1.l)
	lea	tu_mop,a3
	lea	24(a1),a4
	jsr	qa.mop*3+qlv.off     

	move.w	6(a6,a1.l),ch_grphx(a6,d4.l)  ; save x coordinate
	move.l	8(a6,a1.l),ch_grphx+2(a6,d4.l)
	move.w	(a6,a1.l),ch_grphy(a6,d4.l)   ; save y coordinate
	move.l	2(a6,a1.l),ch_grphy+2(a6,d4.l)

	tst.b	ch_pdwn(a6,d4.l)		 ; pen down?
	beq	tu_ok

	moveq	#iog.line,d0
	moveq	#-1,d3
	trap	#4
	trap	#3
	rts



tu_mop
	dc.b	-18	  ; start
	dc.b	-6	  ; length
	dc.b	-12	  ; angle
	dc.b	qa.cos
	dc.b	qa.mul	  ; l*cos(a)
	dc.b	qa.add	  ; end x
	dc.b	-24
	dc.b	-6
	dc.b	-12
	dc.b	qa.sin
	dc.b	qa.mul	  ; l*sin(a)
	dc.b	qa.add
	dc.b	qa.end,0
	end
