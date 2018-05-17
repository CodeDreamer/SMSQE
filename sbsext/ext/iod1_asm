; IO Calls D1	    V2.01    	1990  Tony Tebby   QJUMP
;
; 2016-04-16  2.01  Added ALPHA_BLEND (MK)
;
;	CLS #channel, area
;	PAN #channel, number, area
;	SCROLL #channel, number, area
;	OVER #channel, key
;	UNDER #channel, key
;	FLASH #channel, key
;	FILL #channel, key
;	ALPHA_BLEND #channel, weight
;
	section exten

	xdef	cls
	xdef	pan
	xdef	scroll
	xdef	over
	xdef	under
	xdef	flash
	xdef	fill
	xdef	alpha_blend

	xref	ut_chan1		 ; get channel default #1
	xref	ut_gxin1		 ; get exactly one integer
	xref	ut_gtli1		 ; get one long integer
	xref	ut_gtint
	xref	ut_trap3

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

cls
	moveq	#iow.clra,d7		 ; clear window
	moveq	#%00011111,d5		 ; all five clears possible
	jsr	ut_chan1		 ; get channel id
	bne.s	iod1_rts
	cmp.l	a3,a5			 ; parameter?
	bne.s	iod1_xt 		 ; ... yes
	clr.w	ch_colmn(a6,d4.l)	 ; ... no, clear all
	bra.s	iod1_tr3

pan
	moveq	#iow.pana,d7		 ; pan window
	moveq	#%00011001,d5		 ; just these possible
	bra.s	iod1_px

scroll
	moveq	#iow.scra,d7		 ; scroll window
	moveq	#%00000111,d5		 ; just these possible
	bra.s	iod1_px

over
	moveq	#iow.sova,d7		 ; overwrite mode
	jsr	ut_chan1		 ; channel id
	bne.s	iod1_rts
	jsr	ut_gtint
	bne.s	iod1_rts
	moveq	#0,d1			 ; defult value zero
	subq.w	#1,d3
	blt.s	iod1_tr3		 ; no param
	move.w	(a6,a1.l),d1
	bra.s	iod1_tr3

under
	moveq	#iow.sula,d7		 ; underline mode
	bra.s	iod1_p1

flash
	moveq	#iow.sfla,d7		 ; flash mode
	bra.s	iod1_p1

fill
	moveq	#iog.fill,d7		 ; fill
	bra.s	iod1_p1

alpha_blend
	moveq	#iow.salp,d7

iod1_p1
	moveq	#0,d5			 ; no extra key permissible
iod1_px
	jsr	ut_chan1		 ; get channel id
	bne.s	iod1_rts		 ; ... oops

	jsr	ut_gtli1		 ; get param
	bne.s	iod1_rts
	addq.l	#8,a3			 ; next parm

iod1_xt
	cmp.l	a3,a5			 ; any extra bit
	beq.s	iod1_d1
	jsr	ut_gxin1		 ; get extra bit
	moveq	#7,d3
	and.w	(a6,a1.l),d3		 ; extra bit
	addq.w	#2,a1
	btst	d3,d5			 ; permissable?
	beq.s	iod1_ipar
	add.w	d3,d7			 ; ... yes, another call
iod1_d1
	move.l	(a6,a1.l),d1		 ; d1 value
iod1_tr3
	move.l	d7,d0			 ; operation
	jmp	ut_trap3		 ; do trap

iod1_ipar
	moveq	#err.ipar,d0		 ; invalid par
iod1_rts
	rts
	end
