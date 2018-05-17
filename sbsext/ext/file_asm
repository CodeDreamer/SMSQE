* LOAD and SAVE file ops  V0.0	  1985   Tony Tebby   QJUMP
* 2004-04-02  1.01    some subroutines xdef'd
	section exten
*
*	LRESPR filename 	load into resident procedure area or heap
*
*	EPROM_LOAD filename	load ROM image
*
*	LBYTES filename/#, address		 load bytes into defined area
*	SBYTES filename/#, address, length	 save bytes from defined area
*	SBYTES_O filename/#, address, length	 ... overwriting
*	SEXEC filename/#, address, length, data  save bytes with executable header
*	SEXEC_O filename/#, address, length, data ... overwriting
*
	xdef	lrespr
	xdef	eprom_load
	xdef	lbytes 
	xdef	sbytes
	xdef	sbytes_o
	xdef	sexec
	xdef	sexec_o
*
	xdef	ext_lfile
	xdef	file_in
	xdef	file_load
*
	xref	call_code

	xref	ut_gxlin		get long integers
	xref	ut_fhead		read file header into buffer
	xref	ut_fopen		open file
	xref	ut_fclos		close file
	xref	ut_chget
	xref	ut_chlook
	xref	ut_trp3r		trap #3 (relative a6)
	xref	ut_trap3		trap #3
*
	include dev8_sbsext_ext_keys
	include 'dev8_sbsext_ut_opdefx_keys'

sb_qlibr equ	$ec
*
ext_lfile
	moveq	#0,d0			no parameters after name
	bsr.l	file_in 		open input file and read header
	bne.s	file_rt3		... oops
	tst.b	fh_type(a6,a1.l)	type 0
	beq.s	lr_achp 		... yes
	moveq	#4,d0
	cmp.l	fh_data(a6,a1.l),d0	... 4 byte data space?
	bne.s	lr_achp 		... no
	move.l	a0,-(sp)
	moveq	#mt.inf,d0
	trap	#1
	move.l	(sp)+,a0
	move.l	fh_xtra(a6,a1.l),d0	version the same
	eor.l	d2,d0
	and.l	#$ff00ffff,d0
	bne.s	lr_achp
	moveq	#1,d0
	bra.l	ut_fclos		... yes
lr_achp
	moveq	#mt.alchp,d0		allocate in heap
	move.l	(a6,a1.l),d1		for file length
	moveq	#-1,d2			for my job
	movem.l d1/a0,-(sp)		save length and channel
	trap	#1
	move.l	a0,a1			start in a1
	movem.l (sp)+,d2/a0		channel in a0, length in d2
	tst.l	d0
	bne.l	ut_fclos		... oops
	move.l	a1,a2			save start
	bra.l	file_load		load file (and close)

lrespr
	bsr	ext_lfile
file_rt3
	blt.s	file_rt2		... oops
	bgt.s	ep_ok			... already loaded
call
	move.l	a2,d0
	jmp	call_code
*
eprom_load
	moveq	#0,d0			no parameters after name
	bsr.l	file_in 		open input file and read header
	bne.s	file_rt1		... oops
	move.l	(a6,a1.l),d1		file length
	cmp.l	#$4000,d1		... too long?
	bhi.s	file_bp1		... yes
	move.l	#$4afb0001,d6		EPROM flag
	lea	$c000,a1		where to load
	cmp.l	(a1),d6 		already an EPROM?
	beq.s	ep_res			... yes
	move.l	d6,(a1) 		... no, can I write to it?
	cmp.l	(a1),d6
	beq.s	ep_load 		... yes

ep_res
	moveq	#mt.alres,d0		allocate resident procedure area
	movem.l d1/a0,-(sp)		save length and channel
	trap	#1
	move.l	a0,a1			start in a1
	movem.l (sp)+,d1/a0		channel in a0, length in d1
	tst.l	d0
	bne.l	file_done		... oops

ep_load
	move.l	a1,a3			save start
	move.l	d1,d2
	bsr.s	file_load		load file (and close)
file_rt2
	bne.s	file_rt1		... oops
	cmp.l	(a3),d6 		EPROM flag?
file_bp1
	bne.l	file_bp

	moveq	#0,d6			get channel 0
	jsr	ut_chlook

	lea	8(a3),a1
	jsr	ut..mtext*3+qlv.off	write text

	move.w	4(a3),d0
	beq.s	ep_ini2 		... no basic procs
	lea	(a3,d0.w),a1
	jsr	bp..init*3+qlv.off
ep_ini2
	move.w	6(a3),d0		... init code
	beq.s	ep_ok
	lea	(a3,d0.w),a2
	bsr.s	call
ep_ok
	moveq	#0,d0
	rts

lbytes
	moveq	#8,d0			one parameter only
	bsr.s	file_in 		open input file and read header
file_rt1
	bne.s	file_rts		... oops
	move.l	(a6,a1.l),d2		set length to load
	move.l	(a6,a2.l),a1		set base address
file_load
	moveq	#fs.load,d0		load file
	bra.s	file_trap3
*
sbytes_o
	moveq	#io.overw,d3		open overwrite
	bra.s	sbytes_c
sbytes
	moveq	#io.new,d3		open new
sbytes_c
	moveq	#0,d5			file type zero
	moveq	#$10,d0 		two parameters
	bra.s	file_sbyt
*
sexec_o
	moveq	#io.overw,d3		open overwrite
	bra.s	sexec_c
sexec
	moveq	#io.new,d3		open new
sexec_c
	bset	#31,d3			set program directory
	moveq	#1,d5			file type 1
	moveq	#$18,d0 		3 parameters
file_sbyt
	bsr.s	file_pop		get parameters and open
	bne.s	file_rts		... oops
	move.l	(a6,a2.l),a1		set start address
	addq.l	#2,a2 
	move.l	2(a6,a2.l),d2		set length to send
	move.l	d2,(a6,a2.l)		... in the header as well
	move.w	d5,4(a6,a2.l)		and set the file type
	exg	a1,a2			first set the header
	moveq	#fs.heads,d0
	bsr.l	ut_trp3r
	exg	a2,a1
	moveq	#fs.save,d0		now save the file
*
file_trap3
	bsr.l	ut_trap3		do operation
file_done
	tst.b	d7			close?
	bne.l	ut_fclos		... yes, close file
	tst.l	d0
	rts

file_clbp
	moveq	#err.bp,d0
	bra.s	file_done
*
file_in
	moveq	#io.share,d3		shared file
	bsr.s	file_pop		get parameters and open
	bne.s	file_rts
	bsr.l	ut_fhead		and get header
	bne.s	file_done		... oops
file_rts
	rts
*
file_pop
	move.l	d3,a4			save file action
	bsr.s	file_parm		get other parameters
	bne.s	file_rts
	move.l	a4,d3			set open type
	moveq	#uod.datd+uod.prmt,d2	prompt if exists
	subq.l	#8,a3			backspace to name
	tst.b	1(a6,a3.l)		#?
	bmi.s	file_chan
	bsr.l	ut_fopen		open file
	st	d7			close when done
file_pptr
	move.l	bv_rip(a6),a2		set parameter pointer
	rts
file_chan
	jsr	ut_chget		get channel
	sf	d7			do not close
	bra.s	file_pptr
*
file_parm
	addq.l	#8,a3			skip first parameter
	bsr.l	ut_gxlin		check and get params
	bne.s	file_rts
	tst.w	d3			any parameters?
	beq.s	file_rts		... no
	btst	#0,3(a6,a1.l)		check for odd address
	beq.s	file_rts
file_bp
	moveq	#err.bp,d0		... oops
	rts
	end
