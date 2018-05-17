; DV3 QL5B / QLWA Map Update		V3.00	        1992 Tony Tebby

	section dv3

	xdef	qlf_um5b
	xdef	qlf_umwa

	include 'dev8_keys_ql5b'
	include 'dev8_keys_qlwa'
	include 'dev8_keys_hdr'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'

;+++
; DV3 QL5B Map Update
;
;	d0  r	physical sector to write
;	d1 cr	operation status, 0 on first call, 0 when done
;	d7 c  p drive ID / number
;	a1  r	pointer to sector to write
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return zero (=d1) when done
;
;---
qlf_um5b
	move.l	d1,d0			 ; first call?
	bne.s	qum_try 		 ; ... no
	addq.l	#1,q5a_mupd+qdf_map(a4)  ; ... yes, update count
	st	qdf_mupd(a4)		 ; map sector 0 updated
	sf	ddf_mupd(a4)		 ; clear map updated flag

	moveq	#hdr.len,d0
	add.l	ddf_rdlen(a4),d0	 ; new directory length
	divu	ddf_slen(a4),d0
	swap	d0			 ; block byte
	move.l	d0,q5a_eodr+qdf_map(a4)  ; set directory length

	moveq	#0,d0
	bra.s	qum_do

;+++
; DV3 QLWA Map Update
;
;	d0  r	physical sector to write
;	d1 cr	operation status, 0 on first call, 0 when done
;	d7 c  p drive ID / number
;	a1  r	pointer to sector to write
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return zero (=d1) when done
;
;---
qlf_umwa
	move.l	d1,d0			 ; first call?
	bne.s	qum_try 		 ; ... no
	addq.l	#1,qwa_uchk+qdf_map(a4)  ; ... yes, update count
	st	qdf_mupd(a4)		 ; map sector 0 updated
	sf	ddf_mupd(a4)		 ; clear map updated flag

	moveq	#hdr.len,d0
	add.l	ddf_rdlen(a4),d0	 ; new directory length
	move.l	d0,qwa_rlen+qdf_map(a4)  ; new directory length

	moveq	#0,d0
	bra.s	qum_do

qum_next
	addq.w	#1,d0
qum_try
	cmp.w	qdf_msect(a4),d0	 ; all sectors flushed?
	bge.s	qum_ok

qum_do
	move.w	#qdf_mupd,d1
	add.w	d0,d1
	tst.b	(a4,d1.w)		 ; this sector updated?
	beq.s	qum_next		 ; ... no
	sf	(a4,d1.w)		 ; ... not now

	move.w	d0,d1			 ; this sector
	mulu	ddf_slen(a4),d1 	 ; offset in map
	lea	qdf_map(a4),a1
	add.l	d1,a1			 ; map sector

	move.l	d0,d1			 ; this sector
	moveq	#0,d0			 ; of group 0
	jsr	ddf_logphys(a4)
	addq.w	#1,d1			 ; next sector
	rts

qum_ok
	moveq	#0,d0
	moveq	#0,d1
	rts
	end
