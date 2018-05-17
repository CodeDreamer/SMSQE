; DV3 General Check Hard Disk for Open	    V3.00    1992  Tony Tebby

	section dv3

	xdef	hd_check

	xref	hd_fpart
	xref	dv3_fcheck
	xref	dv3_slen

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_mac_assert'

;+++
; DV3 Atari check hard disk for open
;
;	d1-d6	scratch
;	d7 c  p drive ID / number
;	a0 c  p channel block
;	a1/a2	scratch
;	a3 c  p pointer to linkage block
;	a4 c  u pointer to physical definition
;
;	error return 0 or error
;---
hd_check
	jsr	hdl_ckrdy(a3)		 ; check ready (may set changed medium)
	blt.s	hdc_rts 		 ; bad
	beq.s	hdc_mstat		 ; ok
	assert	ddf.mnew,0
	assert	ddf.mchg,1
	and.b	d0,ddf_mstat(a4)	 ; changed, set mstat 0 or 1
hdc_mstat
	tst.b	ddf_mstat(a4)		 ; drive was OK?
	bpl.s	hdc_check		 ; ... no

	assert	ddf.open,0
	tst.b	ddf_lock(a4)
	bmi.s	hdc_ok			 ; door locked, must be OK
	assert	ddf.unlock-1,ddf.open,0
	bclr	#0,ddf_lock(a4) 	 ; change request to unlock to opn
	beq.s	hdc_lock		 ; lock the door, it was already open
	bne.s	hdc_locked		 ; it was request to unlock, mark locked

hdc_check
	move.b	#ddf.dd,ddf_density(a4)  ; set density
	move.b	#2,ddf_slflag(a4)	 ; set sector length flag
	jsr	dv3_slen

	jsr	hdl_ckwp(a3)		 ; check write protect

	jsr	hd_fpart		 ; find the partition (if any)
	bne.s	hdc_rts

	move.l	d1,d0
	lea	hdl_buff(a3),a1
	moveq	#1,d2
	jsr	hdl_rsint(a3)
	bne.s	hdc_rts

	move.l	d3,ddf_psoff(a4)	 ; set sector offset

	jsr	dv3_fcheck		 ; check format
	blt.s	hdc_rts

	tst.l	ddf_slbl(a4)		 ; any slave block range
	bne.s	hdc_lock
	st	ddf_slbl(a4)		 ; ... there is now
hdc_lock
	jsr	hdl_lock(a3)		 ; lock the door
hdc_locked
	assert	ddf.locked,-1
	st	ddf_lock(a4)		 ; mark drive locked
hdc_ok
	moveq	#0,d0
hdc_rts
	rts

	end
