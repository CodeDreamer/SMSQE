; SuperGoldCard exception handler patches. Deciphered by Marcel Kilgus

	section sgc

	xdef	sgc_privv
	xdef	sgc_8049_rte
	xdef	sgc_ser_sched

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_jcbq'
	include 'dev8_sys_gold_keys'

; Priviledge violation emulator
privv_cont
	movem.l (a7)+,d0/d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5/a6
	addq.l	#4,a7			; forget USP
	jmp	$12345678

; Priviledge violation exception vector
stk.a0	equ	$20
stk.usp equ	$3c
stk.sr	equ	$40
stk.pc	equ	$42

sgc_privv
	subq.l	#4,a7			; space for A7 (USP)
	movem.l d0/d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5/a6,-(a7)
	moveq	#$9,d0			; clear/enable code cache
	dc.l	$4e7b0002
	move.l	usp,a0
	move.l	a0,stk.usp(a7)		; save USP
	move.l	stk.pc(a7),a0		; exception address
	move.w	(a0)+,d0		; the instruction
	moveq	#$ffffffc0,d2
	and.w	d0,d2			; mask all but destination
	subi.w	#$40c0,d2		; move.w SR,ea?
	bne.s	privv_cont		; ... no, let exception progress

	subi.w	#$40c0,d0		; just the destination left
	lsl.w	#2,d0			; *4
	moveq	#7*4,d1 		; complicated modes?
	cmp.w	d1,d0
	bgt.s	privv_do		; ... yes
	lea	2(a7,d0.w),a1		; ... no, just Dx, yay!
privv_set
	move.w	stk.sr(a7),(a1,d2.w)
	move.l	a0,stk.pc(a7)		; continue on next instruction
	move.l	stk.usp(a7),a0		; set USP in case of -(sp), (sp)+
	move	a0,usp

	tst.b	sgo_cache+sgx_work
	bne.s	privv_rte
	moveq	#$0,d0			; disable cache if it was disabled
	dc.l	$4e7b0002
privv_rte
	movem.l (a7)+,d0/d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5/a6
	addq.l	 #4,a7
	rte

privv_do
	cmpi.w	#$e0,d0 		; Ax modes?
	blt.s	privv_ax		; ... yes
	beq.s	privv_absw		; absolute word

	move.l	(a0)+,a1		; absolute long
	bra.s	privv_set

privv_absw
	move.w	(a0)+,a1
	bra.s	privv_set

privv_ax
	and.w	d0,d1			; register bits
	move.l	stk.a0(a7,d1.w),a1	; Ax register
	sub.b	d1,d0			; only mode bits left
	bpl.s	privv_0xx

	lsl.b	#1,d0
	beq.s	privv_100

	move.w	(a0)+,d2		; displacement
	tst.b	d0
	bpl.s	privv_set		; was 16-bit displacement

	move.w	d2,d1			; sign extend 8-bit to 16-bit
	ext.w	d2

	lsr.w	#8,d1
	lsr.b	#2,d1
	bclr	#1,d1			; word or long index register?
	bne.s	privv_index_long

	adda.w	2(a7,d1.w),a1
	bra.s	privv_set
privv_index_long
	adda.l	0(a7,d1.w),a1
	bra.s	privv_set

privv_0xx
	lsl.b	#2,d0
	bpl.s	privv_set		; (Ax)
	addq.l	#2,stk.a0(a7,d1.w)	; (Ax)+
	bra.s	privv_set

privv_100
	subq.l	#2,a1
	move.l	a1,stk.a0(a7,d1.w)	; -(Ax)
	bra.s	privv_set

; Exchange 8049 code RTE trick with normal move2sr/rts code
sgc_8049_rte
	move.b	d7,1(a0)
	move	(a7)+,sr
	rts

; QDOS serial receive interrupt patch. Restore SR normally instead of though RTE
sgc_ser_sched
	move	sr,-(a7)
	ori.w	#$700,sr
	pea	sgc_ser_sched_rte
	move.l	$6(a7),-(a7)					      A712
	rts
sgc_ser_sched_rte
	move	(a7)+,sr
	addq.l	#4,a7
	rts

	end
