; PAR / SER / PRT SuperBASIC entry points

	section exten

	include 'dev8_mac_basic'
	include 'dev8_keys_err'

par_use   proc	PARU
par_buff  proc	PARB
par_clear proc	PARC
par_abort proc	PARA
par_pulse proc	PARP

ser_use   proc	SERU
ser_buff  proc	SERB
ser_room  proc	SERR
ser_cdeof proc	SERE
; ser_flow  proc  SERF
ser_clear proc	SERC
ser_abort proc	SERA
ser_pause proc	SERP

prt_use   proc	PRTU
prt_buff  proc	PRTB
prt_clear proc	PRTC
prt_abort proc	PRTA

prt_use$  fun40 PRT$

	proc_thg ser_par_prt
	fun40_thg ser_par_prt

	xdef	ser_flow
ser_flow
	moveq	#16,d0
	add.l	a3,d0
	sub.l	a5,d0		 ; two parameters?
	beq.s	serf_do 	 ; ... yes
	subq.l	#8,d0		 ; one?
	bne.s	serf_ipar	 ; ... no

	move.l	4(a6,a5.l),-(sp) ; save bit above
	move.l	4(a6,a3.l),4(a6,a5.l)
	clr.l	4(a6,a3.l)	 ; and set null param
	move.l	(a6,a5.l),-(sp)
	move.l	(a6,a3.l),(a6,a5.l)
	clr.l	(a6,a3.l)
	move.l	a5,-(sp)
	addq.l	#8,a5		 ; two parameters now
	bsr.s	serf_do
	move.l	(sp)+,a5
	move.l	(a6,a5.l),-8(a6,a5.l) ; restore NT
	move.l	(sp)+,(a6,a5.l)
	move.l	4(a6,a5.l),-4(a6,a5.l)
	move.l	(sp)+,4(a6,a5.l)

	tst.l	d0
	rts

serf_do
	proc  SERF

serf_ipar
	moveq	#err.ipar,d0
	rts
	end
