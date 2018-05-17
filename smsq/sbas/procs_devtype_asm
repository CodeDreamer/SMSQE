; SBAS_PROCS_DEVTYPE - SBASIC Find Device Type	V2.00	 1994	Tony Tebby

	section exten

	xdef	devtype

	xref	ut_chan
	xref	ut_par0
	xref	ut_rtint

	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'

;+++
; i% = DEVTYPE (#n)
;---
devtype
	move.w	#$8000,d7		 ; assume not open
	moveq	#-1,d6			 ; do not open
	clr.w	d6			 ; but default	#0
	jsr	ut_chan
	bne.s	dt_nchn
	jsr	ut_par0
	moveq	#0,d7			 ; channel exists
	moveq	#1,d6			 ; bit to set if console
	moveq	#iow.pixq,d0
	subq.l	#8,sp
	move.l	sp,a1
	bsr.s	dt_check
	addq.l	#8,sp
	bne.s	dt_rts
	moveq	#2,d6			 ; bit to set if file
	moveq	#0,d1
	moveq	#iof.posr,d0
	bsr.s	dt_check
	beq.s	dt_ret
	moveq	#err.eof,d1		 ; eof is OK
	cmp.l	d1,d0
	bne.s	dt_rts			 ; not eof
	moveq	#0,d0			 ; eof is ok
dt_ret
	move.w	d7,d1
	jmp	ut_rtint

dt_nchn
	cmp.w	#err.ichn,d0		 ; invalid channel?
	beq.s	dt_ret			 ; ... yes
	rts

dt_check
	moveq	#forever,d3
	trap	#do.io
	tst.l	d0
	beq.s	dt_set
	moveq	#err.ipar,d1		 ; ipar is the only acceptable error
	sub.l	d1,d0
	beq.s	dt_rts			 ; ... yes
	add.l	d1,d0
dt_set
	eor.l	d6,d7			 ; change bits
dt_rts
	tst.l	d0
	rts

	end
