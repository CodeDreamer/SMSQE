; SBAS_LOOPADD - Convert LOOP Stmt to Address V2.00    1995 Tony Tebby

	section sbas

	xdef	sb_loopadd
	xdef	sb_loopstt

	xref	sb_sttaddx
	xref	sb_addsttx

	include 'dev8_keys_sbasic'
	include 'dev8_smsq_sbas_inter_keys'
	include 'dev8_mac_assert'

;+++
; SBASIC Convert LOOP line, statement number and offset to address.
;
;	d0/d1/d2/d3/a0/a1/a2/a3/a4/a5 smashed
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return arbitrary
;---
sb_loopadd
	lea	sls_loopadd,a5
	bra.s	sbla_do

;+++
; SBASIC Convert LOOP address to line, statement number and offset.
;
;	d0/d1/d2/d3/a0/a1/a2/a3/a4/a5 smashed
;	a6 c  p pointer to SuperBASIC variables
;
;	Status return arbitrary
;---
sb_loopstt
	lea	sla_loopstt,a5

sbla_do
	move.l	sb_nmtbb(a6),a3 	 ; name table
	move.l	sb_nmtbp(a6),a4
	bra.s	sla_ntlend

sla_ntloop
	move.b	nt_nvalp(a6,a3.l),d0	 ; usage
	subq.b	#nt.rep,d0		 ; REP?
	blt.s	sla_ntnext		 ; ... no
	subq.b	#nt.for-nt.rep,d0	 ; FOR/REP?
	bgt.s	sla_ntnext		 ; ... no

	move.l	nt_value(a6,a3.l),a0
	assert	dt_range,dt_start-4,dt_end-8
	subq.l	#-dt_end-4,a0
	jsr	(a5)
	jsr	(a5)
	jsr	(a5)

sla_ntnext
	addq.l	#8,a3
sla_ntlend
	cmp.l	a4,a3			 ; all done?
	blt.s	sla_ntloop
	rts

sla_loopstt
	move.l	-(a0),d0		 ; next address
	move.l	d0,d4
	ble.s	sla_rts 		 ; converted or zero
	jsr	sb_addsttx		 ; to statement
	beq.s	sla_rts 		 ; ... no table
	lsl.w	#8,d0			 ; make room for offset
	sub.l	a2,d4			 ; offset
	cmp.w	#$ff,d4 		 ; in range?
	bhi.s	sla_loopset		 ; ... no, panic
	move.b	d4,d0
sla_loopset
	not.l	d0
	move.l	d0,(a0)
sla_rts
	rts

sls_loopadd
	move.l	-(a0),d0		 ; next statement
	bpl.s	sls_rts 		 ; converted or zero
	not.l	d0
	moveq	#0,d4
	move.b	d0,d4			 ; save offset
	lsr.w	#8,d0
	jsr	sb_sttaddx		 ; to address
	bgt.s	sls_loopset		 ; ... ok
	beq.s	sls_rts 		 ; no table
	lea	sls_stop,a2
	move.l	a2,d0			 ; point to stop
	moveq	#0,d4
sls_loopset
	add.l	d4,d0
	move.l	d0,(a0)
sls_rts
	rts

sls_stop
	dc.w	bo.stop


	end
