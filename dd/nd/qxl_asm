; QXL Card timing constants and specials

	section init

	xdef	nd_qxl

	xref	nd_qxlc
	xref	nd_qxle

	xref	nd_initv
	xref	nd_proctab
	xref	ut_procdef

	include 'dev8_smsq_qxl_keys'

nd_qxl
	lea	nd_qxlc,a0
	lea	nd_qxle,a2
	lea	qxl_netc,a1
	move.w	sr,d0
	trap	#0
ndq_copy
	move.l	(a0)+,(a1)+		 ; copy time critical code
	cmp.l	a2,a0
	blt.s	ndq_copy

	move.w	qxl_clock,d1		 ; clock speed

	move.w	d0,sr

	lea	ndt_qxl,a1
	jsr	nd_initv
	lea	nd_proctab,a1
	jmp	ut_procdef

; The timings are defined as a time, a scale factor, and an offset.
; To derive the timing constant, the time is multiplied by the clock rate
; the offset is subtracted from the result, and the whole is divided by the
; scale and rounded.

; For the standard wait loop, the time is in microseconds, the offset allows
; for the DBRA (i.e. it is equal to the scale) and the scale is the 28 cycles
; of the dbra testing loop.

; The loop timers are more complex. When the timing constant changes by 4, the
; timimg changes by 15 or 13?? cycles. In cycles, therefore, the scale would be
; 3.75 or 3.25???.
; For this reason, the time, scale and offset all are multiplied by 20.

; Special macro for delay - offset allows for
;	     DBRA (+scale), setup (+scale/2) and rounding (-scale/2)

qxld	macro	time,offset
scale setnum 28
[.lab]	dc.w	[time],[offset]+[scale],[scale]
	endm

; Special macro for loop timings because we know the loop timings for 20 MHz


qxll	macro	time,l20
scale setnum 65
[.lab]	dc.w	[time],[time]*20-[l20]*[scale]-[scale]/2,[scale]
	endm

ndt_qxl
nqx_wgap qxld	  200,0   ; wait for gap constant		 200us
nqx_lsct		      ; look for scout			   20000us
nqx_bace qxld	20000,0   ; look for broadcast acknowledge end 20000us
nqx_csct qxld	   30,0   ; check scout 			  30us
nqx_esct qxld	  485,0   ; end of scout			 485us
nqx_wsct qxld	 3000,0   ; wait to send scout			3000us
nqx_tsct qxld	   32,80  ; timer for scout active / inactive	  32us -80cycles
nqx_bsct		  ; broadcast scout detect		 500us
nqx_back qxld	  500,0   ; broadcast acknowledge detect	 500us
nqx_xsct		  ; extension to scout			5000us
nqx_xack qxld	 5000,0   ; (extension to) acknowledge		5000us
nqx_bnak qxld	  200,0   ; broadcast NACK delay		 200us
nqx_stmo qxld	20000,0   ; serial port timout (not used)     (20000us)
nqx_paus qxld	  140,0   ; pause before send			 140us
nqx_send qxll	  224,38  ; send loop timer			  11.2us
nqx_rtmo qxld	 2500,0   ; receive timout			2500us
nqx_rbto qxld	   80,0   ; receive bit timout			  80us
nqx_rdly qxll	  112,22  ; receive delay start bit to start bit   5.6us
nqx_rbit qxll	  224,39  ; receive bit loop timer		  11.2us


* Do repeated net operation	 1985	 Tony Tebby  QJUMP
*
	section nd
*
	xdef	nd_rept 		do repeated operation (a2)
	xdef	nd_break		test for break
*
	include 'dev8_dd_nd_keys'
	include 'dev8_keys_msg8'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlv'
nd_rept
	move.w	#2500,d7		try 2500 times in all (about 25 seconds)
	tst.b	nd_dest(a0)		is it send broadcast?
	bne.s	ndr_try 		... no
	lsr.w	#1,d7			check immediately

ndr_try
	jsr	(a2)			do operation
	beq.s	ndr_rts 		... done
	blt.s	ndr_tbrk		try again if nc

	move.w	#5000,d0		otherwise wait 7ms
	dbra	d0,*

ndr_tbrk
	cmp.w	#2000,d7		wait about 5-6 seconds before check break
	bgt.s	ndr_tend
*
	bsr.s	nd_break		check break
	bne.s	ndr_abort
ndr_tend
	dbra	d7,ndr_try
*
ndr_abort
	movem.l d1/d2/a0/a1/a2,-(sp)	   save IO regs
	move.l	#msg8.nabt,d0
	sub.l	a0,a0
	move.w	ut.werms,a2
	jsr	(a2)
	movem.l (sp)+,d1/d2/a0/a1/a2	   restore IO regs
*
	moveq	#err.nc,d0		not complete
*
ndr_rts
	rts
*
* Test for break
*
* IPC commands to read rows of the keyboard
*
	dc.w	$02			position of CTRL
ipc_rr7 dc.b	9,1,0,0,0,0,7,2 	read CTRL key row
	dc.w	$40			position of SPACE
ipc_rr1 dc.b	9,1,0,0,0,0,1,2 	read SPACE key row
*
nd_break
	move.w	sr,-(sp)
	move.w	#$2000,sr
	movem.l d1/d5/d7/a3,-(sp)	      save volatiles
	lea	ipc_rr7(pc),a3		check CTRL key row
	bsr.s	ipc_do
	beq.s	ndb_exit		 not pressed
	lea	ipc_rr1(pc),a3		check SPACE key row
	bsr.s	ipc_do
ndb_exit
	movem.l (sp)+,d1/d7/d5/a3
	move.w	(sp)+,sr
	rts

ipc_do
	moveq	#mt.ipcom,d0		do command
	trap	#1
	and.b	-(a3),d1		check if key pressed
	rts
	end

	end
