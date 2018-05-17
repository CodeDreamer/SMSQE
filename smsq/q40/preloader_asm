; SMSQ Q40 Preloader

	section preloader

	include 'dev8_keys_q40'
	include 'dev8_keys_q40_multiIO'

load_base equ	$30000

	dc.l	pl_end-pl_base
pl_base
	bra.s	pl_start
	dc.w	0
	dc.l	pl_start-pl_base

pl_start
	lea	load_base,a0		 ; set load base and sp
	move.l	a0,sp

	clr.b	q40_ebr 		 ; enable extension bus

	move.b	#com.dlab,com1+como_lcr
	move.b	#com.dlab,com1+como_lcr
	move.b	#com.dlab,com1+como_lcr

	move.b	#115200/9600,com1+como_dll ; 9600
	clr.b	com1+como_dlh
	move.b	#com.8bit+com.2stop,com1+como_lcr

	moveq	#$a,d1
	bsr.s	pl_sbyte

pl_wait
	btst	#com..temt,com1+como_lsr  ; wait for shift reg to be empty
	beq.s	pl_wait 		 ; ... wait

	move.b	#com.dlab,com1+como_lcr
	move.b	#115200/38400,com1+como_dll ; 38400
	clr.b	com1+como_dlh
	move.b	#com.8bit+com.2stop,com1+como_lcr
	move.b	#3,com1+como_mcr	 ; enable RTS DTR

	bsr.s	pl_rbyte		 ; get the file length
	lsl.l	#8,d1
	bsr.s	pl_rbyte		 ; get the file length
	lsl.l	#8,d1
	bsr.s	pl_rbyte		 ; get the file length
	lsl.l	#8,d1
	bsr.s	pl_rbyte		 ; get the file length
	move.l	d1,d2

	move.l	a0,a1			 ; copy to load base

pl_rloop
	bsr.s	pl_rbyte		 ; read byte
	move.b	d1,(a1)+
	subq.l	#1,d2
	bgt.s	pl_rloop

	move.b	#0,com1+como_mcr	 ; disable RTS DTR
	jmp	(a0)

pl_sbyte
	btst	#com..thre,com1+como_lsr ; holding reg free?
	beq.s	pl_sbyte		 ; ... wait
	btst	#com..cts,com1+como_msr  ; clear to send
	beq.s	pl_sbyte		 ; ... no
	move.b	d1,com1+como_txdr	 ; send newline
	rts

pl_rbyte
	btst	#com..dr,com1+como_lsr	 ; anything there?
	beq.s	pl_rbyte		 ; ... no
	move.b	com1+como_rxdr,d1	 ; ... yes, get it
	rts
pl_end
	end
