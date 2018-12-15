; Q40 SMS BEEP polling routine	  1988  Tony Tebby

	section sms

	xdef	hdop_poll
	xdef	hdop_ssss

	include 'dev8_keys_atari'
	include 'dev8_keys_sys'
	include 'dev8_keys_sss'
	include 'dev8_keys_68000'
	include 'dev8_smsq_q40_hdop_data'

hdop_poll
	tst.w	ho_bptim(a3)		 ; BEEPING?
	beq.l	hop_rts
	bgt.s	hop_tim 		 ; check timer
	not.w	ho_bptim(a3)		 ; start
	bne.s	hop_scnt		 ; set the counters
	moveq	#-1,d0
	lsr.w	#1,d0
	move.w	d0,ho_bptim(a3) 	 ; set to max time
hop_scnt
	move.w	ho_bpint(a3),ho_bpict(a3) ; preset
	move.w	ho_bpwrp(a3),ho_bpwct(a3) ; .. the counters
	move.w	ho_bplow(a3),d0 	 ; low pitch
	tst.w	ho_bpstp(a3)		 ; start here?
	blt.s	hop_set 		 ; ... yes
	move.w	ho_bphgh(a3),d0 	 ; ... no, high pitch
	bra.s	hop_set

hop_tim
	moveq	#sss_sample,d0
	bsr.l	hop_ssss
	cmp.l	#40000,d0		 ; more than 2 seconds in queue?
	bgt.l	hop_rts 		 ; ... yes

	subq.w	#1,ho_bptim(a3)
	beq.l	hop_nbeep		 ; no beep now

	subq.w	#1,ho_bpict(a3) 	 ; increment?
	bne.s	hop_fill		 ; ... no, fill another 10ms or so

	move.w	ho_bpint(a3),ho_bpict(a3) ; reset interval counter
	move.w	ho_bpstp(a3),d0 	 ; step
	ble.s	hop_down

	add.w	ho_bppit(a3),d0 	 ; new pitch
	cmp.w	ho_bplow(a3),d0 	 ; beyond low (high value)
	blt.s	hop_set 		 ; ... no, set it

	subq.w	#1,ho_bpwct(a3) 	 ; change dir?
	beq.s	hop_cdir		 ; ... yes
	move.w	ho_bphgh(a3),d0 	 ; ... no, set it back to high
	bra.s	hop_set 		 ; and carry on

hop_down
	add.w	ho_bppit(a3),d0 	 ; new pitch
	cmp.w	ho_bphgh(a3),d0 	 ; beyond high (low value)
	bgt.s	hop_set 		 ; ... no, set it

	subq.w	#1,ho_bpwct(a3) 	 ; change dir?
	beq.s	hop_cdir		 ; ... yes
	move.w	ho_bplow(a3),d0 	 ; ... no, reset to low
	bra.s	hop_set

hop_cdir
	move.w	ho_bpwrp(a3),ho_bpwct(a3) ; reset wrap count
	neg.w	ho_bpstp(a3)		 ; go the other way

hop_set
	move.w	d0,ho_bppit(a3) 	 ; set pitch
hop_fill
	move.w	ho_carry(a3),d5 	 ; cycles carried forward
	add.w	#20000/50/2,d5		 ; cycles to end of this half tick

	move.w	ho_bppit(a3),d3 	 ; get pitch

hop_floop
	cmp.w	d3,d5			 ; enough room for a complete cycle?
	ble.s	hop_carry		 ; ... no

	move.w	#$c0c0,d1		 ; fill high
	moveq	#0,d2
	move.w	d3,d2
	lsr.w	#1,d2
	bsr.s	hop_half		 ; do a half cycle

	move.w	#$4040,d1		 ; fill low
	moveq	#1,d2
	add.w	d3,d2
	lsr.w	#1,d2
	bsr.s	hop_half
	sub.w	d3,d5			 ; this much gone
	bra.s	hop_floop


hop_carry
	move.w	d5,ho_carry(a3) 	 ; carry forward unused cycles
	bra	hop_tim

hop_rts
	rts

hop_half
	moveq	#sss_setm,d0
	bsr.s	hop_ssss		 ; spare in queue
	move.l	a2,d4
	sub.l	a1,d4			 ; max samples * 2
	asr.l	#1,d4
	cmp.l	d2,d4			 ; at least enough room for this half?
	bge.s	hop_heloop
	exg	d2,d4
	sub.w	d2,d4			 ; this much taken
	bsr.s	hop_heloop
	exg	d4,d2
	bra.s	hop_half		 ; and try again

hop_hloop
; cmp.l   $4c(a4),a1	;$$$$$$$$$$ bug hunt --- removed 08/01/00
; blt.s   hopxxxx
; dc.l $4afbedeb
;hopxxxx
	move.w	d1,(a1)+
hop_heloop
	dbra	d2,hop_hloop
	moveq	#sss_addm,d0
	bra.s	hop_ssss

hop_nbeep
	tst.l	d0			 ; anything in sample queue?
	bne.s	hop_zero		 ; ... yes, add zero to end
	clr.b	sys_qlbp(a6)		 ; ...qr not beeping
	rts

hop_zero
	addq.w	#1,ho_bptim(a3) 	 ; continue until beep gone
	moveq	#$ffffff80,d1		 ; zero amplitude
	moveq	#$ffffff80,d2		 ; zero amplitude
	moveq	#sss_add1,d0

hop_ssss
hdop_ssss
	move.l	a3,a4
	move.l	exv_i4,a3
	move.l	-(a3),a2
	cmp.l	#sss.flag,-(a3)
	bne.s	hop_ssss_dummy		 ; do dummy ops
	jsr	(a2,d0.w)
hop_ssss_exit
	exg	a4,a3
	rts

hop_ssss_dummy
	cmp.w	#sss_setm,d0
	beq.s	hop_setm_dummy
	moveq	#0,d0
	bra.s	hop_ssss_exit
hop_setm_dummy
	move.l	a1,a2
	bra.s	hop_ssss_exit



	end
