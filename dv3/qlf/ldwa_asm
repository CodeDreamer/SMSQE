; DV3 QLWA Format Load File    V3.00    1994	Tony Tebby QJUMP

	section dv3

	xdef	qlf_ldwa

	xref	dv3_sload
	xref	dv3_sbloc

	include 'dev8_keys_qlwa'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_err'

;+++
; DV3 QLWA format load file.
; It checks whether the first sector of a file is in slave blocks, and if
; it is , it does ordinary IO.
;
;	d1 cr	amount read so far
;	d2 c  p amount to load
;	d3   s
;	d4   s
;	d5   s
;	d6 c  p file id
;	d7 c  p drive ID / number
;	a0 c  p channel block 
;	a1 c  u pointer to memory to load into
;	a2   s	internal buffer address
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;	a5    p
;---
qlf_ldwa
	tst.l	d1			 ; any loaded already?
	bne.l	dv3_sload		 ; ... yes, standard load
	tst.b	ddl_msect(a3)
	beq.l	dv3_sload		 ; give up if multiple sector impossible

	move.l	d2,d4
;*** d1 must be zero for code below
;***	sub.l	d1,d4			 ; amount to load
	cmp.l	#1024,d4		 ; at least 1024 bytes?
	blt.l	dv3_sload		 ; ... no

	tst.b	ddf_slld(a4)		 ; slaved loading preferred?
	beq.s	qlw_load		 ; ... no, direct load

	st	d3c_nnslv(a0)		 ; ... do ne set up new slave blocks
	jsr	dv3_sload		 ; try serial load (assume error is nc)
	sf	d3c_nnslv(a0)
	seq	ddf_slld(a4)		 ; slaved loading preferred
	beq.s	qlw_rts
	addq.l	#-err.nc,d0		 ; not complete?
	beq.s	qlw_dload		 ; ... yes, load the rest
	subq.l	#-err.nc,d0
qlw_rts
	rts

qlw_dload
	move.l	d2,d4			 ; amount left to load
	add.l	d1,d2			 ; original d2

qlw_load
qlw.rege reg	d2/d6/a5      ; d2 on entry
qlw.regx reg	d1/d6/a5      ; is d1 on exit
	movem.l qlw.rege,-(sp)

qlw_flush
	moveq	#-1,d0
	jsr	ddl_fflush(a3)
	addq.l	#-err.nc,d0		 ; not complete?
	beq.s	qlw_flush		 ; ... yes, wait
	subq.l	#-err.nc,d0
	bne.l	qlw_exit		 ; ... oops

	subq.l	#1,d1
	asr.l	#8,d1
	asr.l	#1,d1			 ; number of sectors read so far
	addq.l	#1,d1
	bne.s	qlw_fgrp

	move.l	ddf_fhlen(a4),d3	 ; any first sector to skip?
	beq.s	qlw_fgrp		 ; ... no

	move.l	a1,a2
	move.l	ddl_bfbas(a3),a1	 ; first sector into buffer
; d0 / d1 both zero here
	move.l	d6,d0
	moveq	#1,d2
	jsr	ddf_logphys(a4) 	 ; physical sector
	jsr	ddl_rsect(a3)		 ; read one sector
	exg	a1,a2
	bne.l	qlw_exit

	move.l	#512,d2 		 ; amount to copy
	sub.l	d3,d2
	sub.l	d2,d4			 ; amount read
	lsr.w	#5,d2			 ; by 32 bytes
	subq.w	#1,d2
	add.w	d3,a2			 ; where to start
qlw_fsec
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	dbra	d2,qlw_fsec

	moveq	#1,d1
qlw_fgrp
	lea	qdf_map(a4),a5
qlw_rgrp
	move.l	d6,d0			 ; first group in this lump to read
	move.l	d6,d3
	lea	qwa_gmap(a5,d3.l),a2	 ; first group
	add.l	d3,a2			 ; ... now it is
	cmp.w	qwa_sctg(a5),d1 	 ; more than a group taken?
	blt.s	qlw_smax		 ; ... no
	sub.w	qwa_sctg(a5),d1 	 ; ... yes, count down
	move.w	(a2),d6 		 ; next group
	bra.s	qlw_rgrp
qlw_smax
	move.l	d1,d2			 ; this many sectors already taken
	neg.w	d2
	move.w	#$fe,d5
	sub.w	qwa_sctg(a5),d5 	 ; check for full
qlw_sgrp
	addq.w	#1,d3			 ; next physical group
	add.w	qwa_sctg(a5),d2 	 ; more sectors
	move.w	(a2)+,d6		 ; next logical group
	cmp.w	d6,d3			 ; sequential?
	bne.s	qlw_sread		 ; set to read
	cmp.w	d5,d2			 ; only up to $fe at a time
	ble.s	qlw_sgrp
qlw_sread
	move.l	d4,d5
	lsr.l	#8,d5
	lsr.l	#1,d5			 ; number of complete sectors left
	beq.s	qlw_glsec
	sub.l	d2,d5			 ; as many as sequential?
	bhs.s	qlw_rmul		 ; ... yes
	subq.w	#1,d3			 ; old group
	move.w	d3,d6			 ; keep it
	add.l	d5,d2			 ; get just the sequential sectors
qlw_rmul
	jsr	ddf_logphys(a4)
	jsr	ddl_rsect(a3)		 ; read them
	bne.s	qlw_exit
	moveq	#0,d1			 ; from now on, all reads start at beg
	add.l	d2,d2
	lsl.l	#8,d2			 ; length read
	add.l	d2,a1			 ; update pointer
	sub.l	d2,d4			 ; amount left
	beq.s	qlw_ok			 ; ... none left

	tst.l	d5			  ; any full sectors left?
	bgt.s	qlw_rgrp		 ; ... yes
	beq.s	qlw_lsec		 ; ... no, last is in next group
	add.w	qwa_sctg(a5),d5 	 ; next sector in (previous) group

qlw_lsec
	move.l	d6,d0			 ; group
	move.w	d5,d1			 ; sector (msw is 0)
qlw_glsec
	move.l	a1,a2
	move.l	ddl_bfbas(a3),a1	 ; last sector in buffer

	moveq	#1,d2
	jsr	ddf_logphys(a4)
	jsr	ddl_rsect(a3)		 ; read one sector
	exg	a1,a2
	bne.s	qlw_exit

	ror.l	#4,d4			 ; remaining bytes
	bra.s	qlw_es16
qlw_ls16
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
	move.l	(a2)+,(a1)+
qlw_es16
	dbra	d4,qlw_ls16

	clr.w	d4
	rol.l	#4,d4
	bra.s	qlw_ebyt
qlw_lbyt
	move.b	(a2)+,(a1)+
qlw_ebyt
	dbra	d4,qlw_lbyt

qlw_ok
	moveq	#0,d0
qlw_exit
	movem.l (sp)+,qlw.regx
	rts
	end
