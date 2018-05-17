* boot sector for Atari

	section boot

	xdef	fd_bsect

	xref.l	boot_base

	xref	fd_slct
	xref	fd_dslct
	xref	fd_seek
	xref	fd_rsec

	include 'dev8_keys_68000'
	include 'dev8_mac_creg'

fd_bsect
	bra.s	fdb_start
	dc.b	'         '
	dc.b	$00,$02
	dc.b	$02
	dc.b	$01,$00
	dc.b	$02
	dc.b	$70,$00
	dc.b	$a0,$05
	dc.b	$f9
	dc.b	$03,$00
	dc.b	$09,$00
	dc.b	$02,$00
	dc.b	$00,$00

fdb_start
	move.w	#$2700,sr


	move.l	sp,a3			; keep SSP
	move.l	exv_ilin,d4		; get standard illegal instruction
	lea	illeg,a4
	move.l	a4,exv_ilin		; and insert new one
	moveq	#0,d0
	dc.l	$4e7b0002		; disable caches (68030)
illeg
	move.l	a3,sp			; restore stack
	move.l	d4,exv_ilin		; restore illegal instruction


	moveq	#1,d1			 ; drive a
	moveq	#0,d2			 ; side 0
	moveq	#0,d3			 ; track 0
	moveq	#8,d4			 ; sector 8
	lea	boot_base,a6		 ; load base
	move.l	a6,a7			 ; stack below it
	move.l	a6,a1

	jsr	fd_slct
	jsr	fd_seek
	jsr	fd_rsec
	move.l	$3c(a1),d7		 ; length of boot file
	ror.w	#8,d7
	swap	d7
	ror.w	#8,d7
	move.l	d7,d6
	lea	(a1,d7.l),a2		 ; top address

	moveq	#1,d2			 ; side 1
	moveq	#0,d3			 ; track 0
	moveq	#6,d4			 ; sector 6

fdb_side
	jsr	fd_slct 		 ; select to set side
	jsr	fd_seek 		 ; seek
fdb_read
	jsr	fd_rsec 		 ; ... and read
	add.w	#$200,a1		 ; top
	cmp.l	a2,a1
	bge.s	fdb_des 		 ; done
	addq.w	#1,d4			 ; next sector
	cmp.w	#9,d4
	ble.s	fdb_read
	moveq	#1,d4
	addq.w	#1,d2			 ; next side
	cmp.w	#2,d2
	blt.s	fdb_side
	moveq	#0,d2
	addq.w	#1,d3			 ; next track
	bra.s	fdb_side

fdb_des
	jsr	fd_dslct

	moveq	#0,d0
	move.l	d0,d1
	move.l	d0,d3
	move.l	d0,d4
	move.l	d0,d5
	move.l	d0,d6
	move.l	d0,d7
	move.l	d0,a0
	move.l	d0,a1
	move.l	d0,a3
	move.l	d0,a4
	move.l	d0,a5

	lea	boot_base,a6
	cmp.w	#$601a,(a6)		 ; is it TOS header?
	bne.s	fdb_go			 ; ... no
	lea	$26(a6),a6		 ; ... yes jump in later
fdb_go
	move.l	a6,-(a7)
	move.l	d0,a6
	rts
	end
