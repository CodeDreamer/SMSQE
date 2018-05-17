* Make a directory list (for QRAM)		  v0.01   J.R.Oakley  Dec 1986  QJUMP
*
	section setup
*
	include 'dev8_match_keys'
	include 'dev8_ptr_keys'
	include 'dev8_qram_keys'
	include 'dev8_keys_chn'
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_qdos_sms'
	include 'dev8_qdos_ioa'
	include 'dev8_qdos_io'
*
	xref	qr_alch0
	xref	fl_rmcks
*
	xdef	ut_mkdir
*+++
* Generates a list of files on a medium matching a given source pattern
* string.  A destination filename is also generated for each.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1					number of files matched
*	A1	pointer to status area		preserved
*	A2	pointer to source name		preserved
*	A3	pointer to dest name		preserved
*	A5	match routine			pointer to list
*---
ut_mkdir
regdir	reg	d2-d6/a0-a4
	movem.l regdir,-(sp)
	sub.w	#mt.frame,sp		; space for stack frame
	move.l	a5,mt_match(sp) 	; fill in match code address
	move.l	sp,a5			; point to it
*
*	Set up the static parameters for the match routine.
*
	move.l	a2,mt_source(a5)	; point frame to source...
	move.l	a3,mt_dest(a5)		; ...and destination
	lea	mt_file(a5),a2		; and to space for full source name
	move.l	a2,mt_name(a5)
*
*	Open the source directory.
*
	move.l	mt_source(a5),a0	; point to source name
	moveq	#ioa.kdir,d3		; open a directory
	moveq	#myself,d1		; for me
	moveq	#ioa.open,d0
	trap	#do.ioa 		; do it
	tst.l	d0
	bne	fld_exnc		; ...oops
*
*	Look through the directory, checking for matching names: if successful,
*	keep the entry and the corresponding destination.
*
	moveq	#0,d5			; start with no files
	moveq	#0,d6			; and file 0
	lea	mt_flist(a5),a4 	; new list
	bsr	fl_gtspc		; get space for first batch
	bne	fld_exer		; ...oops
fld_gtlp
	addq.w	#1,d6			; file number for this header
	moveq	#hdr.len,d2		; get a 64-byte header
	moveq	#-1,d3			; sometime
	moveq	#iob.fmul,d0
	trap	#do.io
	tst.l	d0
	beq.s	fld_tstl		; ok, see if it's a real file
	cmp.l	#err.eof,d0		; not ok, end of file
	beq.s	fld_cmdv		; yes, good
	bne	fld_exer		; ...oops
fld_tstl
	tst.l	hdr_flen-hdr.len(a1)	; zero bytes long?
	bne.s	fld_mtch		; no, try to match it
	sub.w	#hdr.len,a1		; point back to start
	bra.s	fld_gtlp		; and go for the next header
*
fld_mtch
	move.l	a1,mt_copy(a5)		; where to put the copy name
	lea	hdr_name-hdr.len(a1),a1 ; and point to the actual filename
*
	move.l	mt_match(a5),a2 	; point to match code
	jsr	(a2)			; try matching
	bne.s	fld_nomt		; failed
*
*	File matched, so keep it and read another header
*
	move.w	d6,fl_data-hdr_name(a1) ; fill in file number
	add.w	#fl.dirln-hdr_name,a1	; point to next space
	addq.w	#1,d5			; another file is found
	dbra	d4,fld_gtlp		; and get another file
	bsr	fl_gtspc		; no room, get some more
	bne	fld_excl		; can't!
	bra	fld_gtlp		; and go round
*
fld_nomt
	lea	-hdr_name(a1),a1	; point back to start of header
	bra	fld_gtlp		; and try the next file
	page
*
*	Once all matching directory entries are read, we should check for the
*	device being a genuine MDV (not a RAM_ or FLP_USEd one), and get
*	correct headers if it is.
*
mdvreg	 reg	d5/a0
mdstk_d5 equ	$00    
fld_cmdv
	tst.w	d5			; how many files?
	beq	fld_cnvd		; none
	movem.l mdvreg,-(sp)
	move.l	a0,d3			; from channel ID
	add.w	d3,d3
	add.w	d3,d3			; make offset of channel base in table
	moveq	#sms.info,d0
	trap	#do.sms2		; point to system variables
	move.l	sys_chtb(a0),a1 	; and to channel table
	move.l	0(a1,d3.w),a1		; and to channel base
*
	move.l	chn_drvr(a1),a2 	; point to driver address
	cmp.l	#$c000,iod_frmt(a2)	; is FORMAT entry in ROM?
	bge	fld_nmdv		; no, can't be mdv then
	lea	iod_name(a2),a4 	; point to actual drive name
*
	moveq	#0,d0
	move.b	fs_drive(a1),d0 	; get drive ID
	lsl.b	#2,d0			; offset in pdb table
	lea	sv_fsdef(a0),a2 	; point to pdb table 
	move.l	0(a2,d0.w),a2		; and to drive's own pdb
*
*	We now have A2 pointing to a microdrive's physical definition block.
*	Assuming that we're 13 sectors on from the last directory sector, we
*	can search from there to each file's block 0 in turn, and read the
*	headers in the order they're found on the medium.
*
	lea	md_maps+md.mapl(a2),a1	; point to end of map
	move.w	#256,d4 		; starting at block 255
	move.w	#$ff00,d6		; look for duff sectors
fld_fszl
	subq.w	#1,d4			; this sector
	cmp.w	-(a1),d6		; is it duff
	beq.s	fld_fszl		; yes, try next
	moveq	#-1,d0			; looking for directory sector>=0
	move.w	d4,d3			; starting here
*
fld_fdrl
	tst.b	(a1)			; is this a directory block? 
	bne.s	fld_fdre		; no
	cmp.b	1(a1),d0		; yes, highest so far?
	bgt.s	fld_fdre		; no
	move.w	d3,d6			; yes, update position
	move.b	1(a1),d0		; this is highest now
fld_fdre
	subq.l	#2,a1			; next map entry
	dbra	d3,fld_fdrl		; loop until done
*
	add.w	d4,d4			; max index in map
	add.w	d6,d6			; start point in map
	sub.w	#(13+1)*2,d6		; move back 13 blocks
	bpl.s	fld_ffnl		; OK, find first file number
	add.w	d4,d6			; past start, so back to here
*
*	At this point, D4=max map index, D6=start map index
*
fld_ffnl
	moveq	#0,d0
	move.b	md_maps(a2,d6.w),d0	; get file number
	cmp.b	#$f8,d0 		; is it a file?
	bcc	fld_nofl		; no, do next
	tst.b	md_maps+1(a2,d6.w)	; yes, is it that file's block 0?
	bne.s	fld_nofl		; no, no point reading it then
*
	lea	mt_flist(a5),a0 	; point to file list
	moveq	#0,d3			; no files left in this chunk
	move.w	mdstk_d5+2(sp),d2	; and number of files on it
	bra.s	fld_sfle
fld_sfll
	cmp.w	(a3),d0 		; correct file number?
	beq.s	fld_flfn		; yes, read its header
	add.w	#fl.dirln,a3		; no, point to next data area
fld_sfle
	subq.w	#1,d2			; we've done a file
	bmi.s	fld_nofl		; no more, file wasn't matched
	dbra	d3,fld_sfll		; do next file in this chunk
	move.l	(a0),a0 		; no more, next chunk
	moveq	#9,d3			; it has up to 10 files left
	lea	4+fl_data(a0),a3	; point to first data area
	bra.s	fld_sfll
*
fld_flfn
	move.w	(a4),d1 		; length of source drive name
	addq.w	#2,d1			; add drive number and underscore
	lea	mt_file+2(a5),a1	; where to put full name
	move.l	mt_source(a5),a0	; get pointer to source pattern
	addq.l	#2,a0			; point to characters in it
	bra.s	fld_cpde
fld_cpdl
	move.b	(a0)+,(a1)+		; copy part of drive name
fld_cpde
	dbra	d1,fld_cpdl
*
	lea	hdr_name-fl_data(a3),a0 ; then the filename
	move.w	(a0)+,d1		; the name is this long
	bra.s	fld_cpfe
fld_cpfl
	move.b	(a0)+,(a1)+
fld_cpfe
	dbra	d1,fld_cpfl
*
	lea	mt_file+2(a5),a0	; point to start of string again
	sub.l	a0,a1			; it's this long now
	move.w	a1,-(a0)		; fill in length and point to it
*
	moveq	#myself,d1		; for me
	moveq	#ioa.kshr,d3		; share it
	moveq	#ioa.open,d0
	trap	#do.ioa 		; open the file
*
	lea	hdr_flen-fl_data(a3),a1 ; read header to here
	moveq	#hdr.len,d2		; all of it
	moveq	#-1,d3
	moveq	#iof.rhdr,d0
	trap	#do.io			; read the header
	add.l	#hdr.len,hdr_flen-fl_data(a3) ; unfix length
	move.w	#-1,(a3)		; flag header read
*
	moveq	#ioa.clos,d0
	trap	#do.ioa 		; and close the file again
*
	sub.w	#8*2,d6 		; next block must be >8 away
	subq.w	#1,d5			; one fewer to do
	beq.s	fld_nmdv		; which makes no more
*
fld_nofl
	subq.w	#2,d6			; previous map entry is next on medium
	bpl.s	fld_cdun		; positive
	add.w	d4,d6			; negative, start again at end of map
fld_cdun
	bra	fld_ffnl		; go round again
*
fld_nmdv
	movem.l (sp)+,mdvreg
	page
*
*	Now we can convert the binary data in the headers to an ASCII 
*	string in the data area.
*
fld_cnvd
	lea	mt_flist(a5),a4 	; point to file list
	moveq	#0,d6			; no files left in this chunk
	move.w	d5,d4			; and number of files on it
	bra.s	fld_cnve
fld_cnvl
	bsr.s	fld_mkdt		; make data string
	add.w	#fl.dirln,a3		; point to next data area
fld_cnve
	subq.w	#1,d4			; we've done a file
	bmi.s	fld_exok		; that's the lot
	dbra	d6,fld_cnvl		; do next file in this chunk
	move.l	(a4),a4 		; no more, next chunk
	moveq	#9,d6			; it has up to 10 files left
	lea	4+fl_data(a4),a3	; point to first data area
	bra.s	fld_cnvl
*
*	Error while reading directory
*
fld_exer
	move.l	mt_flist(a5),d4 	; point to list of chunks
	move.l	a0,a5			; save channel ID
	jsr	fl_rmcks(pc)		; remove chunks
	move.l	a5,a0			; restore channel
	sub.l	a5,a5			; no list
	moveq	#0,d1			; no files
	bra.s	fld_excl		; and close directory
*
*	Come here when we've finished.
*
fld_exok
	move.w	d5,d1			; return number of files found
	move.l	mt_flist(a5),a5 	; and pointer to the list
	moveq	#0,d0			; success!
fld_excl
	move.l	d0,d4
	moveq	#ioa.clos,d0		; always close the directory!
	trap	#do.ioa
	move.l	d4,d0			; preserve an error
fld_exit
	add.w	#mt.frame,sp		; restore frame space
	movem.l (sp)+,regdir		; and registers
	rts
*
*	Come here if we can't open the directory
*
fld_exnc
	moveq	#0,d1			; no files
	sub.l	a5,a5			; no pointer to them either
	bra.s	fld_exit
*
*	Get a chunk of space for directory entries: 1k is enough for ten
*	header+dest filename combinations, and a four byte pointer to the next
*	such chunk.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D4					number of filenames in chunk
*	A1	end of current chunk		start of new chunk
*	A4	start of current chunk		start of new chunk
*
spcreg	reg	a0
*
fl_gtspc
	movem.l spcreg,-(sp)
	move.l	#10*fl.dirln+4,d0	; get space for ten filenames and data
	jsr	qr_alch0(pc)		; for myself
	bne.s	flg_exit		; ...oops
	move.l	a0,(a4) 		; link chunk into chain
	lea	4(a0),a1		; leave space for link pointer
	move.l	a0,a4			; point to next link 
	moveq	#9,d4			; space now for ten filenames
	moveq	#0,d0
*
flg_exit
	movem.l (sp)+,spcreg
	rts
*
*	Fill in the data area
*
*	Registers:
*		Entry			Exit
*	D0-D3				smashed
*	A1/A2				smashed
*	A3	^ data string		preserved
*
fld_mkdt
	lea	4+22(a3),a1		; put date a little way in	    
	sub.l	a6,a1			; relative to A6
	move.l	hdr_date-fl_data(a3),d1 ; get date
	move.w	cn.date,a2		; and convert it
	jsr	(a2)
	add.l	a6,a1			; point back at date
	move.l	$12(a3),$2(a3)		; copy time
	move.b	$16(a3),$6(a3)
	move.b	#' ',$7(a3)
*
	move.l	$08(a3),d0		; keep year
	move.l	$0e(a3),d1		; get date
	lsr.l	#8,d1			; shift to lsw
	move.w	d1,$8(a3)		; fill it in
*
	move.b	#' ',d0
	ror.l	#8,d0			; make ' yy '
	move.l	d0,$0e(a3)		; fill that in
*
*	Now get the type
*
	moveq	#'0',d0
	add.b	hdr_type-fl_data(a3),d0 ; get type as a character
	move.b	d0,$12(a3)
	move.b	#' ',$13(a3)		; follow it with a space
*
*	Convert the length
*
	moveq	#9,d0			; ten digits to go, suppress 0's
	lea	$13(a3),a1		; point to where they go
	lea	sub_dat(pc),a2		; things to subtract
	moveq	#-hdr_end,d1		; less header...
	add.l	hdr_flen-fl_data(a3),d1 ; ...length to convert
cnv_loop
	move.l	(a2)+,d2		; something to subtract
	moveq	#0,d3			; digit is zero
sub_num
	sub.l	d2,d1			; knock off 10^n
	bmi.s	end_sub 		; too far
	addq.b	#1,d3			; digit is one more
	bra.s	sub_num
end_sub
	add.l	d2,d1			; restore remainder
	tst.b	d3			; is digit 0?
	bne.s	add_0			; no, can't suppress it then!
	tst.w	d0			; last digit?
	beq.s	add_0			; mustn't suppress that either!
	tst.l	d0			; suppressing 0's?
	bmi.s	add_0			; no
	moveq	#' ',d3 		; yes, use space instead
	bra.s	fill_dig
add_0
	add.b	#'0',d3
	bset	#31,d0			; cancel zero suppression
fill_dig
	move.b	d3,(a1)+
	dbra	d0,cnv_loop
*
	move.w	#28,(a3)		; fill in length
	rts
*
*	Table of powers of 10
*
sub_dat
	dc.l	1000000000
	dc.l	100000000
	dc.l	10000000
	dc.l	1000000
	dc.l	100000
	dc.l	10000
	dc.l	1000
	dc.l	100
	dc.l	10
	dc.l	1
*
	end
