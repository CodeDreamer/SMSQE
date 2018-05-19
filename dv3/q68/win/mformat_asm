; DV3 IDE Format   V300    1998  Tony Tebby
; slightly modified for Q68
; 3.01	2018-05-08   if check OK to format fails, release the controller (wl)

	section dv3

	xdef	hd_mformat

	xref	hd_fpart
	xref	hd_fchk
	xref	dv3_slen
	xref	hd_hold
	xref	hd_release

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'

;+++
; This formats a qxlwin "disk"
;
;	d0 cr	format type / error code
;	d1 cr	format dependent flag or zero / good sectors
;	d2  r	total sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return standard
;---
hd_mformat
imf.reg reg	d3/d4/d5/a0/a1/a2/a5
	movem.l imf.reg,-(sp)

hold	jsr	hd_hold 		; wait until interface is free
	bne.s	hold

	jsr	hd_fchk 		; check OK to format
	bne.s	imf_fnok

	move.b	#ddf.dd,ddf_density(a4) ; set density
	move.b	#2,ddf_slflag(a4)	; set sector length flag
	jsr	dv3_slen
	
	jsr	hd_release

	jsr	hd_fpart		; find qxlwin file
	bne.s	imf_exit

	move.l	d3,ddf_psoff(a4)	; set base of qxlwin file

	move.l	d2,d3			; size of qxlwin file
	blt.s	imf_set
	moveq	#9,d0
	lsr.l	d0,d3			; number of sectors

;	 lea	 ddf_mname(a4),a0
;	 move.l  #'WIN0',(a0)+		 ; set name
;	 add.b	 d7,-1(a0)
;	 move.l  #'    ',(a0)

imf_set
	clr.l	-(sp)			; no 4096 byte
	clr.l	-(sp)			; no 2048 byte
	clr.l	-(sp)			; no 1024 byte
	move.l	d3,-(sp)		; sectors per track
	clr.l	-(sp)			; no 256 byte
	clr.l	-(sp)			; no 128 byte
	clr.l	-(sp)			; no heads/cyls
	move.l	sp,a0
	jsr	ddf_fselect(a4) 	; select format
	add.w	#7*4,sp
	bne.s	imf_exit		; ... oops

	moveq	#ddf.full,d0		; ... the only type of format we can do
	sub.l	a0,a0
	jsr	ddf_format(a4)		; so do it!
	st	ddf_slbl(a4)		; set slave block range

imf_exit
	movem.l (sp)+,imf.reg
	tst.l	d0
	rts

imf_ro
	moveq	#err.rdo,d0
	bra.s	imf_exit
imf_fnok
	jsr	hd_release
imf_fmtf
	moveq	#err.fmtf,d0
	bra.s	imf_exit

	end
