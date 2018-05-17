; DV3 Standard Floppy Disk Check for Open      V3.00    1992  Tony Tebby

	section dv3

	xdef	fd_check

	xref	fd_ckwp
	xref	fd_rroot
	xref	dv3_slen
	xref	dv3_fcheck

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'

;+++
; DV3 check floppy for open
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
fd_check
	cmp.b	fdl_maxd(a3),d7 	 ; drive in range?
	bgt.s	fdc_mchk		 ; ... no

	bclr	#7,fdf_dirct(a4)	 ; was last open direct access?
	bne.s	fdc_check		 ; always check
	bset	d7,fdl_stpb(a3) 	 ; drive stopped?
	beq.s	fdc_check		 ; ... yes
	assert	0,ddf.mnew,ddf.mok-$ff,ddf.mchg-1
	tst.b	ddf_mstat(a4)		 ; drive OK?
	blt.s	fdc_done		 ; OK

fdc_check
	tst.b	ddf_density(a4) 	 ; density set?
	bne.s	fdc_wp			 ; ... yes, check write protect

	move.b	#ddf.hd,ddf_density(a4)  ; set density

fdc_wp
	jsr	fd_ckwp 		 ; check write protect
	bne.s	fdc_rts 		 ; in use!!

	lea	fdl_buff(a3),a1 	 ; root sector buffer
	jsr	fd_rroot		 ; read sector
	bne.s	fdc_mchk		 ; ... none

	jsr	dv3_fcheck		 ; check medium / format
	blt.s	fdc_mchk		 ; ... bad format

	bset	d7,fdl_stpb(a3) 	 ; drive not stopped since check

	tst.l	ddf_slbl(a4)		 ; any slave block range
	bne.s	fdc_done		 ; ... yes, not first open
	st	ddf_slbl(a4)		 ; ... there is now

fdc_done
	moveq	#0,d0
	assert	ddf.locked,-1
	st	ddf_lock(a4)		 ; lock the drive
fdc_rts
	rts

fdc_mchk
	assert	ddf.mnew,0
	clr.b	ddf_mstat(a4)		 ; no medium at all now
	moveq	#err.mchk,d0
	rts

	end
