; SMS (QXL) Interrupt Server Version 2
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)

	section comm

	xdef	qxl_isrv

	xref	qxl_mess_pr
	xref	qxl_mess_set
	xref	qxl_mess_add

	include 'dev8_mac_creg'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_keys_psf'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

*blat macro blv
* xref	 blatt
* move.b [blv],-(sp)
* jsr	 blatt
* addq.l #2,sp
* endm
*
*blatl macro blv
* xref	 blattl
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

qxl_isrv
isrv.reg reg	d0/d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4/a5/a6

	movem.l isrv.reg,-(sp)
; blatl sp
; blat #$ff

	gcreg	cacr			 ; cache control
	move.l	d0,-(sp)		 ; save it
	tst.l	d0			 ; data cache enabled?
	bpl.s	isrv_cena		 ; ... no
	tst.w	d0			 ; instruction cache enabled?
	bmi.s	isrv_start		 ; ... yes
isrv_cena
	cinva				 ; ... no, invalidate
	cena40				 ; ... and enable cache

isrv_start
; st qxl_neth
; tst.b $2817f
; beq.s xx1
; blat	#$99
xx1
	tst.w	qxl_clock		 ; clock speed set?
	bge.s	isrv_sync		 ; ... yes, or not ready yet
	subq.w	#1,qxl_clock		 ; count down

isrv_sync
	lea	qxl_ibyte,a1		 ; input address
	lea	qxl_oword,a2		 ; write word, no wait

	move.b	(a1),d0 		 ; real flag?
	beq	qxl_nocomm		 ; ... no, should not happen

; blat #1
	moveq	#0,d0			 ; checksum (preset)

	move.l	qxl_qxpc_mess,a4	 ; QXL-PC messages
	move.l	qxl_qxpc_eom(a4),a5	 ; end of
	move.l	a4,qxl_qxpc_eom(a4)	 ; ... taken
	cmp.l	a5,a4			 ; any?
	bhs.l	no_tx_data		 ; ... no

; blat #2
	lea	(a2),a3 		 ; normally (a3) is write and wait
	moveq	#0,d1			 ; dummy data
	moveq	#0,d2			 ; dummy data
	moveq	#0,d3			 ; dummy data
	moveq	#0,d4			 ; dummy data
	moveq	#0,d5			 ; dummy data
	moveq	#0,d6			 ; dummy data
	moveq	#0,d7			 ; dummy data
	move.l	d1,a0			 ; dummy data

	move.l	d1,a6			 ; no flag

qxl_pc_loop
	clr.w	(a2)			 ; write 0 no wait
	clr.w	(a3)			 ; write 0 and wait
	move.w	a6,(a3) 		 ; write flag and wait

;********
; 16 word write block
;********

sendl	macro	reg,nreg
	move.w	[reg],(a3)
	eor.w	[reg],d0		 ; checksum
	swap	[reg]			 ; prepare lsw
	add.l	d0,d0			 ; scrumple checksum

	move.w	[reg],(a3)
	eor.w	[reg],d0		 ; checksum
	swap	[nreg]			 ; prepare msw next
	add.l	d0,d0			 ; scrumple checksum
	endm

	swap	d1			 ; get msw of first

	sendl	d1,d2			 ; $04
	sendl	d2,d3			 ; $08
	sendl	d3,d4			 ; $0C
	sendl	d4,d5			 ; $10
	sendl	d5,d6			 ; $14
	sendl	d6,d7			 ; $18
	move.l	a0,d1
	sendl	d7,d1			 ; $1c

	move.w	d1,(a3) 		 ; $1e
	eor.w	d1,d0			 ; checksum
	swap	d1			 ; prepare lsw
	add.l	d0,d0			 ; scrumple checksum
	move.l	d0,d2			 ; upper word of checksum

	move.w	d1,(a3) 		 ; $20
	swap	d2			 ; upper word of checksum in lsw d2
	eor.w	d1,d0			 ; checksum
	eor.w	d2,d0			 ; eor upper bits to complete crc
	move.w	d0,(a3) 		 ; send checksum

;*********

	moveq	#0,d0
	clr.w	(a3)			 ; flush checksum

qxl_pc_wack
	move.b	(a1),d1 		 ; read acknowledge
	blt.s	qxl_pc_next		 ; ok
	beq.s	qxl_pc_wack		 ; no sync!!!!
	cmp.l	a2,a3			 ; dumy run?
	beq.s	qxl_pc_next		 ; ... yes
	movem.l -$20(a4),d1/d2		 ; restore d1/d2 (only regs smashed)
	bra.s	qxl_pc_again		 ; try again

qxl_pc_next
	cmp.l	a5,a4			 ; more data?
	bhs.s	no_more_tx_data 	 ; ... no
	movem.l (a4)+,d1-d7/a0		 ; ... yes, set data

qxl_pc_again
	lea	qxl_oword_wait,a3	 ; real write and wait address
	move.w	#$8000,a6		 ; flag is -1
	bra	qxl_pc_loop

no_more_tx_data
no_tx_data
	move.l	a2,a3			 ; no wait first time
	moveq	#0,d1
	moveq	#0,d2			 ; get timer counter tick in a word

; blat #4
rx_data_flag
	clr.w	(a2)			 ; write 0 no wait
	clr.w	(a3)			 ; write 0 and wait
	move.w	d1,(a3) 		 ; write flag and wait
	clr.w	(a2)			 ; clear flag no wait
	move.b	(a1),d2 		 ; timer count and PC_QXL transfer flag
	tst.b	d1			 ; flag non zero?
	bne.s	rx_data 		 ; ... yes
	moveq	#2,d1			 ; +ve flag
	lea	qxl_oword_wait,a3	 ; real wait
	bra.s	rx_data_flag

rx_data
; blat #5
; blat d2
	bclr	#7,d2			 ; any data to be received?
	beq.l	no_rx_data		 ; end of transaction
; blat #6
; blat d2
	add.w	d2,qxl_mtick_count	 ; increment mini tick count

	move.l	qxl_pcqx_mess,a4	 ; start of the pc_qxl message buffer
	lea	(a2),a3 		 ; normally (a3) is write and wait
	sub.l	a6,a6			 ; no flag
	moveq	#-1,d1			 ; dummy pass flag

pc_qxl_wait
	tst.b	(a1)			 ; wait for PC flag = 0
	bne.s	pc_qxl_wait

pc_qxl_loop
; blat #7
; blat d1
	nop				 ; clear memory write back buffer
	moveq	#0,d0			 ; clear checksum
	clr.w	(a2)			 ; write 0 no wait
	clr.w	(a3)			 ; write 0 and wait
	move.w	a6,(a3) 		 ; write flag and wait
	or.b	(a1),d1 		 ; more data $00, err $FF, done $01
	bgt.l	no_more_rx_data 	 ; ... done

rcvl	macro	reg,extra
	clr.w	(a3)			 ; wait for data
	move.b	(a1),[reg]
; blat [reg]
	add.l	d0,d0			 ; scrumple checksum
	lsl.w	#8,[reg]

	clr.w	(a3)			 ; wait for data
	move.b	(a1),[reg]
; blat [reg]
	eor.w	[reg],d0		 ; add to checksum
	swap	[reg]

	clr.w	(a3)			 ; wait for data
	move.b	(a1),[reg]
; blat [reg]
	[extra]
	lsl.w	#8,[reg]

	clr.w	(a3)			 ; wait for data
	move.b	(a1),[reg]
; blat [reg]
	add.l	d0,d0			 ; scrumple checksum
	eor.w	[reg],d0		 ; add to checksum
	endm

	rcvl	d2
	rcvl	d3
	rcvl	d4
	rcvl	d5
	rcvl	d6,{move.l d2,a0}
	rcvl	d7,{move.l d3,a5}
	rcvl	d2
	rcvl	d3

	clr.w	(a3)			 ; wait for data
	move.b	(a1),d1
; blat d1
	lsl.w	#8,d1
	clr.w	(a3)			 ; wait for data
	move.b	(a1),d1
; blat d1

	eor.w	d0,d1			 ; check the checksum
	clr.w	d0
	swap	d0			 ; and the shifted bits
	eor.l	d0,d1
	bne.s	pc_qxl_error		 ; bad, try again
; blat #8

	exg	a5,d3
	exg	a0,d2
	movem.l d2-d7/a0/a5,(a4)	 ; save data read
	add.w	#$20,a4
	move.w	#$8000,a6
	bra	pc_qxl_loop

pc_qxl_error
; blat #9
	move.w	#4,a6			 ; re-read data (or first block)
	lea	qxl_oword_wait,a3	 ; from now on wait
	moveq	#0,d1			 ; clear first time flag
	st	d1			 ; but read even if at end
	bra	pc_qxl_loop

no_rx_data
	add.w	d2,qxl_mtick_count	 ; increment tick count
	move.l	qxl_pcqx_mess,a4	 ; set ptr to start (end) of messages

no_more_rx_data
	clr.w	(a2)			 ; idle
	tst.b	(a1)			 ; flush idle
	clr.w	(a4)			 ; end of messages

	move.l	sms.sysb,a6		 ; system variable base
; st qxl_netl
	jsr	qxl_mess_pr
; st qxl_neth
	jsr	qxl_mess_set


qxl_nocomm
isrv_done
; blat #$ff
; st qxl_netl
	move.l	(sp)+,d0		 ; cache status
	pcreg	cacr			 ; reset it

	movem.l (sp)+,isrv.reg
	sf	qxl_int_ack		 ; clear interrupt
	nop

	cmp.w	#qxl.mticks,qxl_mtick_count ; ready for scheduler?
	bhs.s	isrv_poll		 ; time now
; tst.b $2817f
; beq.s xx3
; blat	#$11
xx3
	rte

isrv_poll
	movem.l psf.reg,-(sp)
	move.l	sms.sysb,a6		 ; system variable base
	clr.w	qxl_mtick_count 	 ; start again next time

	move.l	qxl_message,a5
	move.w	qxl_ms_beep+qxl_ms_spare(a5),d7  ; beep counting down?
	beq.s	isrv_ptest		 ; ... no
	subq.w	#1,d7
	bne.s	isrv_bcnt		 ; still counting

	movem.l d0/a1,-(sp)
	lea	qxl_ms_beep(a5),a1	 ; set up kill beep
	move.w	#qxm.beep<<8,(a1)
	move.w	#4,-(a1)		 ; 4 byte command
	jsr	qxl_mess_add
	sf	sys_qlbp(a6)
	movem.l (sp)+,d0/a1

isrv_bcnt
	move.w	d7,qxl_ms_beep+qxl_ms_spare(a5)

isrv_ptest
	tas	sys_plrq(a6)		 ; poll requested already?
	bne.s	isrv_rrte

	addq.w	#1,sys_pict(a6) 	 ; one more poll

	and.w	#$f8ff,sr		 ; re-enable interrupts
	move.l	sms.spoll,a5
	jmp	(a5)

isrv_rrte
	movem.l (sp)+,psf.reg
	rte

	end
