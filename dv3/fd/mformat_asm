; DV3 Standard Floppy Disk Format		   1993     Tony Tebby

	section dv3

	xdef	fd_mformat

	xref	fd_hold
	xref	fd_release

	xref	fd_ckwp
	xref	fd_newdensity
	xref	fd_seek
	xref	fd_recal
	xref	fd_reset
	xref	fd_sfmtt
	xref	fd_fmtt
	xref	fd_rid
	xref	fd_rphys
	xref	fd_wait

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'

fdf.ubytes equ	2960		 ; number of useable bytes per track (SD)
fdf.sdoh   equ	 33		 ; sector overhead (SD)
fdf.ddoh   equ	 62		 ; sector overhead (DD, HD, ED)
fdf.goh    equ	  8		 ; gap overhead (fudge for luck)
fdf.runup equ	100		 ; 2 second runup
fdf_bsmap equ	dff_size+dff.size*6
;+++
; This routine formats a medium
;
;	d0 cr	format type / error code
;	d1 cr	format dependent flag or zero / good sectors
;	d2  r	total sectors
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return arbitrary
;---
fd_mformat
fdf.reg reg	d3/d4/d5/d6/a0/a1/a2/a5
	movem.l fdf.reg,-(sp)
	move.l	#(120*2*80+7)/8+fdf_bsmap,d0  ; space for comms and 128 byte
	jsr	gu_achpp		 ; sector map on 80 track DS ED drive
	bne.l	fdf_exnfd
	move.l	a0,a5			 ; only one allocation

	jsr	fd_sfmtt		 ; set up format track
	bne.l	fdf_exntb

	jsr	fd_ckwp
	blt.l	fdf_exnh		 ; ... oops

	tst.b	ddf_wprot(a4)		 ; write protected?
	bne.l	fdf_ro

	jsr	fd_hold 		 ; hold polling task

	move.b	ddf_density(a4),d6	 ; specified density
	bge.s	fdf_reselect
	move.b	fdl_defd(a3),d6 	 ; default density
	move.b	d6,d1
	bge.s	fdf_sdensity		 ; there is one

	moveq	#ddf.sd,d1		 ; sd is valid: next will be the max

fdf_sdensity
	move.b	d1,ddf_density(a4)
fdf_reselect
	jsr	fd_reset		 ; set a valid density

	jsr	fd_recal		 ; restore

	moveq	#fdf.runup,d0
	jsr	fd_wait 		 ; wait a bit

	tst.b	d6			 ; density given?
	bge.s	fdf_setup

	move.b	#9,fdf_fstrk(a4)	 ; try 9 sectors
	move.b	#ddf.512,fdf_fslen(a4)	 ; 512 bytes long
	move.w	#512,ddf_slen(a4)
	move.b	#80,fdf_fgap(a4)	 ; reasonable gap

	moveq	#0,d4
	move.w	fdl_maxt(a3),d4 	 ; innermost track+1
	move.w	d4,d1
	lsr.w	#2,d1			 ; 1/4
	sub.w	d1,d4
	swap	d4
	addq.l	#1,d4
fdf_try
	jsr	fd_newdensity		 ; next density down
	tst.b	ddf_density(a4) 	 ; ... do not try SS
	beq.l	fdf_fmtf		 ; ... cannot format at any density

	jsr	fd_recal		 ; seek to 0
	move.l	d4,fdl_sadd(a3) 	 ; sector address
	jsr	fd_seek 		 ; seek to track

	jsr	fd_fmtt 		 ; format track
	bne.l	fdf_fmtf

	lea	fdl_buff(a3),a1 	 ; use track buffer
	moveq	#1,d2			 ; to read one sector
fdf_rtry
	move.b	#1,fdl_sect(a3) 	 ; try sector 1
	jsr	fd_rphys		 ;
	beq.s	fdf_recal		 ; ... ok
	blt.l	fdf_fmtf		 ; ... bad
	move.b	#8,fdl_sect(a3) 	 ; try sector 8
	jsr	fd_rphys		 ; to read
	beq.s	fdf_recal		 ;
	blt.l	fdf_fmtf		 ; ... bad
	cmp.w	#fdcs.orun,d0		 ; overrun?
	beq.s	fdf_rtry		 ; ... yes, retry
	bra.s	fdf_try 		 ; data error, try different density

fdf_recal
	jsr	fd_recal		 ; seek to track 0

fdf_setup
	lea	dff_size+dff.size*6(a5),a0	0
	clr.l	-(a0)			 ; no 4096 byte

	move.l	#fdf.ubytes,d1		 ; useable bytes per track (SD)
	moveq	#fdf.sdoh+fdf.goh,d5	 ; single density sector overhead
	move.b	ddf_density(a4),d6	 ; density flag
	ble.s	fdf_setsl
	moveq	#fdf.ddoh+fdf.goh,d5	 ; dd, hd, ed overhead
	lsl.l	d6,d1			 ; useable bytes DD, HD, ED
fdf_setsl
	move.w	#2048+2048/16,d3	 ; sector length + 6.25%

	moveq	#4,d2
fdf_setfl
	move.l	d1,d0
	add.w	d5,d3
	divu	d3,d0			 ; number of sectors per track
	sub.w	d5,d3
	move.w	d0,-(a0)
	clr.w	-(a0)
	lsr.w	#1,d3			 ; shorter sector
	dbra	d2,fdf_setfl

	move.w	fdl_maxt(a3),-(a0)	 ; number of tracks per side
	move.w	ddf_heads(a4),-(a0)	 ; number of heads set?
	bgt.s	fdf_fsel		 ; ... yes

	move.w	#2,(a0) 		 ; two heads

fdf_fsel
	moveq	#0,d1			 ; nothing special
	jsr	ddf_fselect(a4) 	 ; select format
	bne.l	fdf_exit

	move.l	#fdf.ubytes,d1		 ; usable bytes per track (SD)
	lsl.l	d6,d1			 ; useable bytes SD,DD,HD,ED
	divu	ddf_strk(a4),d1 	 ; bytes available per sector
	sub.w	ddf_slen(a4),d1
	sub.w	#fdf.ddoh,d1		 ; less ordinary overhead
	tst.b	d6			 ; sd?
	bne.s	fdf_smgap		  ; ... no
	add.w	#fdf.ddoh-fdf.sdoh,d1
fdf_smgap
	cmp.w	#$ff,d1 		 ; more than 255?
	bls.s	fdf_sgap
	st	d1
fdf_sgap
	move.b	d1,fdf_fgap(a4)
	move.b	ddf_strk+1(a4),fdf_fstrk(a4) ; sectors per track
	move.b	ddf_slflag(a4),fdf_fslen(a4) ; sector length

	move.w	ddf_atotal+2(a4),d4
	mulu	ddf_asect(a4),d4	 ; number of sectors
	divu	ddf_scyl(a4),d4 	 ; gives number of tracks
	clr.l	fdl_sadd(a3)		 ; start from scratch

;---------------------------------
; now format all tracks

fdf_tcloop
	jsr	fd_seek 		 ; seek to track
	bne.l	fdf_fmtf

	moveq	#2,d0			 ; wait 40-60 ms
	jsr	fd_wait

	cmp.w	#1,ddf_heads(a4)	 ; single sided?
	beq.s	fdf_tcs0		 ; ... yes

	move.b	#1,fdl_side(a3) 	 ; side 1
	jsr	fdf_fmtt_check		 ; format and check
	bne.s	fdf_tcbad
fdf_tcs0
	clr.b	fdl_side(a3)		 ; side 0
	jsr	fdf_fmtt_check		 ; format and check
	beq.s	fdf_tceloop

fdf_tcbad
	blt.s	fdf_fmtf		 ; bad altogether
	move.w	ddf_scyl(a4),d2 	 ; number of bad sectors
	moveq	#0,d0
	move.b	fdl_trak(a3),d0
	mulu	d2,d0			 ; first bad sector

	move.w	d0,d1
	not.w	d1
	and.w	#7,d1			 ; bit 7-0
	lsr.l	#3,d0			 ; of this byte
fdf_tcbloop
	bset	d1,fdf_bsmap(a5,d0.l)	 ; bad sector
	subq.l	#1,d1
	bge.s	fdf_tcbend
	moveq	#7,d1			 ; restart next byte
	addq.l	#1,d0
fdf_tcbend
	subq.b	#1,d2
	bgt.s	fdf_tcbloop

fdf_tceloop
	addq.b	#1,fdl_trak(a3) 	 ; move on one track
	cmp.b	fdl_trak(a3),d4 	 ; end of disk?
	bgt.s	fdf_tcloop		 ; ... no

	bset	d7,fdl_stpb(a3) 	 ; this drive will not have stopped
	moveq	#ddf.full,d0		 ; ... the only type of format we can do
	lea	fdf_bsmap(a5),a0	 ; bad sector map
	jsr	ddf_format(a4)		 ; so do it!
	st	ddf_slbl(a4)		 ; set slave block range

fdf_exit
	jsr	fd_release		 ; release the controller

fdf_exnh	; not held
	move.l	fdl_fmtb(a3),d6 	 ; format track buffer
	beq.s	fdf_exntb
	move.l	d6,a0
	jsr	gu_rchp 		 ; return to heap

fdf_exntb	; no format track buffer
	move.l	a5,a0			 ; format definition
	jsr	gu_rchp 		 ; return to heap

fdf_exnfd	; no format definition to return
	movem.l (sp)+,fdf.reg
	rts

fdf_fmtf
	moveq	#err.fmtf,d0
	bra.s	fdf_exit

fdf_ro
	moveq	#err.rdo,d0
	bra.s	fdf_exnh

;------------------
fdf_fmtt_check
	jsr	fd_fmtt 		 ; format track
	bne.s	fdfc_rts		 ; ... oops

	clr.b	fdl_sect(a3)
fdfc_read
	move.b	fdl_sect(a3),d0 	 ; previous sector
	addq.b	#1,d0			 ; next sector
	cmp.b	fdf_fstrk(a4),d0	 ; all done?
	bgt.s	fdfc_ok 		  ; ... yes
	move.b	d0,fdl_sect(a3)
	lea	fdl_buff(a3),a1 	 ; use track buffer
	moveq	#1,d2			 ; to read one sector
fdfc_rdo
	jsr	fd_rphys		 ; to read
	beq.s	fdfc_read		 ; ... ok
	cmp.w	#fdcs.orun,d0		 ; overrun?
	beq.s	fdfc_rdo		 ; ... yes, retry
	tst.l	d0
fdfc_rts
	rts

fdfc_ok
	moveq	#0,d0
	rts				 ; all ok or error

	end
