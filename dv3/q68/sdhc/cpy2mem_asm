; copy sdhc routines to fast mem & adjust pointers 1.00 (c) W. Lenerz 2017
;
; on entry a3 = device defn block
; the first routine to be copied must be rsect
; then come wsect,sndcmd,srchdrv and hd_hold

	section dv3

	xdef	cpy2mem
	xdef	q68_dvend

	xref	cpy_mmod
	xref	q68_dvstrt
	xref	hd_rsint
	xref	hd_rdirect
	xref	hd_wdirect
	xref	hd_wsint

	include dev8_keys_q68
	include dev8_dv3_keys
	include dev8_dv3_hd_keys

q68_dvend
cpy2mem lea	q68_dvstrt,a0
	jsr	cpy_mmod		; copy into fast mem, 4(a0)=start of routine
	move.l	4(a0),d0		; ptr to hd_rdirect in fast mem
	move.l	d0,ddl_rsect+2(a3)	; hd_rdirect : jump to this routine now
	lea	hd_rsint,a1		; hd_rsint in "slow" mem
	move.l	a1,d1
	lea	hd_rdirect,a1
	move.l	a1,d2			; keep
	sub.l	a1,d1			; offset from rdirect to rsint
	add.l	d0,d1			; poiner to rsint in fast mem
	move.l	d1,hdl_rsint+2(a3)
	lea	hd_wsint,a1
	move.l	a1,d1
	sub.l	d2,d1
	add.l	d0,d1
	move.l	d1,hdl_wsint+2(a3)	; jump to this routine now
	lea	hd_wdirect,a1
	move.l	a1,d1
	sub.l	d2,d1
	add.l	d0,d1
	move.l	d1,ddl_wsect+2(a3)	; jump to this routine now
	rts

	end
