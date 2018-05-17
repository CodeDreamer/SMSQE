; DV3 PC Atari Floppy Disk Format track       1998	Tony Tebby

	section dv3

	xdef	fd_sfmtt		; set up to format a track
	xdef	fd_fmtt 		; format track

	xref	fd_cmd_wr
	xref	fd_stat

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

;+++
; Set up format track
;
;	status return standard
;
;---
fd_sfmtt
	move.l	a0,-(sp)
	move.l	fdf_data,d0		 ; length of track
	jsr	gu_achpp
	bne.s	fdsf_exit

	cmp.l	#$1000000,a0		 ; above DMA range?
	blo.s	fdsf_setbuf		 ; ... no
	jsr	gu_rchp 		 ; ... throw it away
	bra.s	fdsf_exitd0


fdsf_setbuf
	move.l	a0,fdl_fmtb(a3) 	 ; save buffer pointer

fdsf_exitd0
	tst.l	d0
fdsf_exit
	move.l	(sp)+,a0
	rts

;+++
; Format track
;
;	d1   s
;	d2   s
;	d3   s
;	d7 c  p drive ID / number
;	a0   s
;	a1   s	address to write from
;	a2   s
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_fmtt
	move.l	fdl_fmtb(a3),d0 	 ; format track buffer
	move.l	d0,a0
	bne.s	fdf_strack		 ; OK, set track
	move.l	sms.128kb,a0		 ; use 128 k buffer
fdf_strack
	lea	fdf_data,a2
	move.l	a0,a1

	move.l	(a2)+,d3		 ; total length

	moveq	#1,d0			 ; first sector
fdf_sloop
	moveq	#0,d1
fdf_rdata
	move.b	(a2)+,d2		 ; get byte to write
	move.b	(a2)+,d1		 ; get number of bytes to write
	blt.s	fdf_nsect		 ; ... next sector
	bgt.s	fdf_wbytes		 ; ... genuine
	move.b	fdl_trak(a3),(a0)+	 ; track
	move.b	fdl_side(a3),(a0)+	 ; side
	move.b	d0,(a0)+		 ; sector
	subq.w	#3,d3
	bra.s	fdf_rdata

fdf_wbytes
	sub.w	d1,d3			 ; a bit off the total
fdf_wbloop
	move.b	d2,(a0)+		 ; byte in buffer
	subq.b	#1,d1
	bgt.s	fdf_wbloop
	bra.s	fdf_rdata

fdf_nsect
	addq.b	#1,d0			 ; next sector
	cmp.w	ddf_strk(a4),d0
	bgt.s	fdf_fend
	ext.w	d1			 ; offset to loop
	add.w	d1,a2
	bra.s	fdf_sloop		 ; and next sector

fdf_fend
	move.b	(a2)+,d2		 ; end fill byte
fdf_floop
	move.b	d2,(a0)+
	subq.w	#1,d3
	bgt.s	fdf_floop


	moveq	#fdc.wtrk,d0
	moveq	#$20,d2 		 ; lots of sectors
	jsr	fd_cmd_wr		 ; write track
	jmp	fd_stat



fdf_data
	dc.l	27000	; track length	(HD) + spare
	dc.b	$4e,60	; initial gap
fdf_prea
	dc.b	$00,12	preamble
	dc.b	$f5,3	sync
	dc.b	$fe,1	index address mark
	dc.b	$00,0	track, side, sector
	dc.b	$02,1	sector length flag
	dc.b	$f7,1	crc
	dc.b	$4e,22	gap
	dc.b	$00,12	preamble
	dc.b	$f5,3	sync
	dc.b	$fb,1	data address mark
	dc.b	$30,100 data
	dc.b	$30,100 more data
	dc.b	$30,100 more data
	dc.b	$30,100 more data
	dc.b	$30,112 ... 512 bytes altogether
	dc.b	$f7,1	crc
	dc.b	$4e,84	gap
	dc.b	$00,fdf_prea-(*+2)	 ; go back to the preamble

	dc.b	$4e,00			 ; fill at end


	end
