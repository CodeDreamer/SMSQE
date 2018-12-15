; ATARI:  make bootable Atari diskette

; ex boot_make, list of files, 'flp1_*d2d'

; The bootloader starts with the first file, the trailer is removed and
; subsequent files are concantenated (after calculation of length and checksum)
; before the host module trailer is replaced at the end

	section base

	xdef	boot_base

	xref	boot_addmod

	xref	fd_bsect		 ; boot sector
	xref	fd_bsend		 ; ... end

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_dos'

boot_base equ	$30000			 ; laod highish

dt_flen  equ	$60			 ; total length of file in buffer
dt_sect1 equ	$64			 ; sectors-1 in buffer
dt_clst1 equ	$66			 ; clusters-1 in buffer
dt_file  equ	$68			 ; pointer to file in buffer

dt_sect  equ	$80			 ; sector buffer
sct.shft equ	9			 ; shift bytes to sectors and vv
sct.len  equ	$200			 ; sector length
dir.shft equ	4			 ; shift directory entries to sectors
dir.elen equ	$20			 ; directory entry length
dt_top	 equ	dt_sect+sct.len

	data	dt_top+$80		 ; not much space

	bra.l	start
	dc.w	0,$4afb,10,'Atari boot'
	dc.w	$4afb		; Special job

	section special

	section main

dir_def
	dc.b	'X          ',$08,0,0,0,0,0,0,0,0,0,0,0,0,0,0,00,00,0,0,0,0
	dc.b	'X       PRG',$00,0,0,0,0,0,0,0,0,0,0,0,0,0,0,02,00,0,0,0,0
dir_end

start
	add.l	a4,a6			 ; base of data area

	moveq	#err.ipar,d0
	move.w	(sp)+,d5		 ; number of channels
	subq.w	#1,d5			 ; two?
	ble.l	suicide 		 ; ... no
	lea	(sp),a5 		 ; pointer to file list

	jsr	boot_addmod		 ; concatenate

; now we create a bootable disk

	move.l	d2,dt_flen(a6)		 ; keep length
	move.l	d2,d4
	subq.l	#1,d4			 ; -1
	moveq	#sct.shft,d0
	asr.l	d0,d4			 ; sectors-1
	move.w	d4,dt_sect1(a6)

	move.l	d4,d5
	moveq	#0,d1
	move.b	fd_bsect+dos_clst,d1
	divu	d1,d5			 ; clusters - 1
	move.w	d5,dt_clst1(a6)

	move.l	a1,dt_file(a6)		 ; file itself

	moveq	#sms.rrtc,d0		 ; get time
	trap	#do.smsq

	lea	fd_bsect,a1
	lea	dos_name(a1),a0
	moveq	#8,d0
	jsr	cr_ihex 		 ; put date in as ID

	lea	dt_sect(a6),a0
	move.w	#sct.len/2-2,d0 	 ; calculate wordwise checksum
	move.w	#dos.atcs,d1
csum_boot
	move.w	(a1)+,d2		 ; while we copy the sector
	move.w	d2,(a0)+
	sub.w	d2,d1
	dbra	d0,csum_boot

	move.w	d1,(a0) 		 ; set it

	move.l	(a5),a0 		 ; output channel ID
	moveq	#0,d5			 ; track zero
	moveq	#0,d6			 ; side zero
	moveq	#1,d7			 ; sector 1

	lea	dt_sect(a6),a4		 ; start of root sector

	move.l	a4,a1
	bsr.l	wrt_sect		 ; write it

	move.l	a4,a1			 ; clear out sector
	moveq	#sct.len/4-1,d0
clr_sect
	clr.l	(a1)+
	dbra	d0,clr_sect

	move.b	fd_bsect+dos_fats,d4	 ; number of FATs
wrt_fat
	subq.b	#1,d4
	blt.s	wrt_dir 		 ; no more FATs
	swap	d4

	move.l	a4,a1
	move.l	#dos.fil3,(a1)		 ; preset first 2 FAT entries
	addq.w	#3,a1

	move.w	dt_clst1(a6),d0 	 ; clusters - 1
	moveq	#2,d1			 ; first cluster

set_fatl
	move.w	d1,d2
	addq.w	#1,d2			 ; next cluster
	tst.w	d0			 ; last?
	bne.s	set_fat
	move.w	#$0fff,d2		 ; last cluster

	    ; set d2 into FAT entry d1
set_fat
	move.w	d2,d3
	mulu	#3,d1
	ror.l	#1,d1			 ; *1.5
	bmi.s	set_fato		 ; was odd

	move.b	d2,(a4,d1.w)		 ; was even, lsbyte
	and.b	#$f0,1(a4,d1.w) 	 ; msbyte, mask out a bit
	lsr.w	#8,d3
	or.b	d3,1(a4,d1.w)
	bra.s	set_fatn

set_fato
	lsl.w	#4,d3			 ; odd, shift it a bit
	and.b	#$0f,(a4,d1.w)		 ; lsbyte, mask out a bit
	or.b	d3,(a4,d1.w)		 ; lsbyte
	lsr.w	#8,d3
	move.b	d3,1(a4,d1.w)

set_fatn
	move.w	d2,d1			 ; next cluster
	dbra	d0,set_fatl

	move.b	fd_bsect+dos_mdid,(a4)

	move.w	fd_bsect+dos_sfat,d4	 ; number of FAT sectors
	ror.w	#8,d4

wrt_fatl
	move.l	a4,a1			 ; write sector of FAT
	bsr.l	wrt_sect

	move.l	a4,a1			 ; clear out sector
	moveq	#sct.len/4-1,d0
clr_fat
	clr.l	(a1)+
	dbra	d0,clr_fat

	subq.w	#1,d4			 ; another sector?
	bgt.s	wrt_fatl		 ; ... yes
	swap	d4
	bra.s	wrt_fat 		 ; ... no, try another FAT

wrt_dir
	move.b	fd_bsect+dos_dire+1,d4	 ; directory entries
	lsl.w	#8,d4
	move.b	fd_bsect+dos_dire,d4

	subq.w	#1,d4
	asr.w	#dir.shft,d4
	addq.w	#1,d4

	lea	dir_def,a2		 ; preset directory
	move.l	a4,a1
	moveq	#(dir_end-dir_def)/4-1,d0
pre_dir
	move.l	(a2)+,(a1)+
	dbra	d0,pre_dir 

	lea	dt_flen(a6),a2
	move.b	(a2)+,-(a1)		 ; file length is last part of entry
	move.b	(a2)+,-(a1)		 ; in reverse order
	move.b	(a2)+,-(a1)
	move.b	(a2)+,-(a1)

	move.l	a4,a1			 ; set names
	lea	4(a5),a2		 ; name from command
	move.w	(a2)+,d0
	cmp.w	#8,d0			 ; too long?
	bls.s	set_nend
	moveq	#7,d0			 ; eight chars only
set_nloop
	moveq	#$ffffffdf,d1
	and.b	(a2)+,d1		 ; upper cased
	move.b	d1,dir.elen(a1) 	 ; in filename
	move.b	d1,(a1)+		 ; and disk name
set_nend
	dbra	d0,set_nloop

wrt_dirl
	move.l	a4,a1			 ; write directory 
	bsr.s	wrt_sect

	move.l	a4,a1			 ; clear out sector
	moveq	#sct.len/4-1,d0
clr_dir
	clr.l	(a1)+
	dbra	d0,clr_dir

	subq.w	#1,d4
	bgt.s	wrt_dirl

; now write file

	move.l	dt_file(a6),a1		 ; write file
	move.w	dt_sect1(a6),d4
file_loop
	bsr.s	wrt_sect		 ; a sector
	dbra	d4,file_loop		 ; at a time


suicide
	move.l	d0,d3			 ; error return
	moveq	#sms.frjb,d0		 ; force remove
	moveq	#myself,d1		 ; ... me
	trap	#do.sms2

do_io
	moveq	#forever,d3
	trap	#do.io
	tst.l	d0
	bne.s	suicide
rts
	rts

wrt_sect
	move.l	a1,-(sp)		 ; save buffer pointer
	move.w	d5,d1
	swap	d1			 ; track
	move.b	d6,d1
	lsl.w	#8,d1			 ; side
	move.b	d7,d1			 ; sector

	moveq	#iof.posa,d0		 ; set sector
	bsr.s	do_io

	addq.b	#1,d7			 ; next sector
	cmp.b	fd_bsect+dos_strk,d7	 ; all done
	bls.s	wrt_sct1
	moveq	#1,d7			 ; first sector
	addq.w	#1,d6			 ; next head
	cmp.b	fd_bsect+dos_head,d6
	blo.s	wrt_sct1
	moveq	#0,d6
	addq.w	#1,d5			 ; next track

wrt_sct1
	move.l	(sp)+,a1		 ; buffer
	move.w	#sct.len,d2		 ; sector length
	moveq	#iob.smul,d0
	bra.s	do_io

cr_ihex
	add.l	d0,a0			new end
	bra.s	cih_eloop

cih_loop
	moveq	#$f,d2			mask out one digit
	and.b	d1,d2
	lsr.l	#4,d1			next digit
	add.b	#'0',d2 		make ascii
	cmp.b	#'9',d2 		digit?
	ble.s	cih_put 		... yes
	addq.b	#'A'-$a-'0',d2		make it a letter
cih_put
	move.b	d2,-(a0)
cih_eloop
	dbra	d0,cih_loop

	rts

	end
