; Atari SMS BEEP polling routine    1988  Tony Tebby	QJUMP

	section sms

	xdef	hdop_poll

	include 'dev8_keys_atari'
	include 'dev8_keys_sys'
	include 'dev8_smsq_atari_hdop_data'

hdop_poll
	tst.w	ho_bptim(a3)		 ; BEEPING?
	beq.l	hop_rts
	bgt.s	hop_tim 		 ; check timer

	not.w	ho_bptim(a3)
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
	subq.w	#1,ho_bptim(a3)
	beq.s	hop_nbeep		 ; no beep now

	subq.w	#1,ho_bpict(a3) 	 ; increment?
	bne.s	hop_rts 		 ; ... no

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
	moveq	#spp.pt1l<<8,d1 	 ; low byte
	move.b	d0,d1
	lea	at_spp+spp_ctrl,a0
	movep.w d1,0(a0)
	lsr.w	#8,d0			 ; high byte
	or.w	#spp.pt1h<<8,d0
	movep.w d0,0(a0) 

	move.w	#spp.amp1<<8+$0f,d0	 ; set amplitude	 
	movep.w d0,0(a0) 

	move.w	#spp.ctrl<<8+spp.cnrm,d0 ; beep on
	movep.w d0,0(a0)
hop_rts
	rts

hop_nbeep
	lea	at_spp+spp_ctrl,a0	 ; zero amplitude
	move.w	#spp.amp1<<8,d0
	movep.w d0,0(a0)
	clr.b	sys_qlbp(a6)		 ; ... not beeping
	rts
	end
