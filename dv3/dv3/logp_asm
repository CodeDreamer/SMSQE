; DV3 General Logical to Physical Translate  V3.00	     1993 Tony Tebby

	section dv3

	xdef	dv3_logp
	xdef	dv3_psector
	xdef	dv3_dsector

	include 'dev8_keys_dos'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_err'

;+++
; DV3 general sector number in partition to physical sector translate or offset
; Cylinder / head / sector format, and sector only format.
;
;	d0 cr	sector wrt partition / physical sector
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return arbitrary
;
;---
dv3_psector
	add.l	ddf_psoff(a4),d0
	tst.b	ddl_cylhds(a3)		 ; cylinder head sector?
	bne.s	dlp_logp		 ; ... yes, calculate
	rts

;+++
; DV3 general logical group / sector to physical sector translate.
; Cylinder / head / sector format.
;
;	d0 cr	logical group / physical sector
;	d1 c  p sector in logical group
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return arbitrary
;
;---
dv3_logp
	mulu	ddf_asect(a4),d0
	add.l	d1,d0			 ; physical sector number
	add.l	ddf_lsoff(a4),d0	 ; offset

;+++
; DV3 general sector number on driove to physical sector translate
; Cylinder / head / sector format.
;
;	d0 cr	sector wrt drive / physical sector
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return arbitrary
;
;---
dv3_dsector
dlp_logp
	move.l	d1,-(sp)
	move.w	ddf_scyl(a4),d1 	 ; sectors per cylinder set?
	beq.s	dlp_sector		 ; ... no, assume first track

	divu	ddf_scyl(a4),d0 	 ; sector+side / cylinder
	swap	d0			 ; cyl in msw
	moveq	#0,d1
	move.w	d0,d1			 ; sector + side
	divu	ddf_strk(a4),d1 	 ; in range
	lsl.w	#8,d1
	move.w	d1,d0			 ; side
	swap	d1
	move.b	d1,d0

dlp_sector
	addq.b	#1,d0			 ; standard sector offset
	move.l	(sp)+,d1
	rts

	end
