* LOAD and SAVE file ops  V1.02    1985   Tony Tebby	QJUMP
* 2004-04-02  1.01  some subroutines xdef'd
* 2018-01-13  1.02  Fix qlib_sys loading problem on QemuLator (ast least) (MK)
	section exten
*
*	LRESPR filename 	load into resident procedure area or heap
*
*	LBYTES filename/#, address		 load bytes into defined area
*	SBYTES filename/#, address, length	 save bytes from defined area
*	SBYTES_O filename/#, address, length	 ... overwriting
*	SEXEC filename/#, address, length, data  save bytes with executable header
*	SEXEC_O filename/#, address, length, data ... overwriting
*
	xdef	lrespr
	xdef	lbytes 
	xdef	sbytes
	xdef	sbytes_o
	xdef	sexec
	xdef	sexec_o
*
	xdef	file_in
	xdef	file_load
*

	xref	ut_gxlin		get long integers
	xref	ut_fhead		read file header into buffer
	xref	ut_fopen		open file
	xref	ut_fclos		close file
	xref	ut_chget
	xref	ut_chlook
	xref	ut_trp3r		trap #3 (relative a6)
	xref	ut_trap3		trap #3
*
	include 'dev8_sbsext_ext_keys'
	include 'dev8_sbsext_ut_opdefx_keys'

sb_qlibr equ	$ec
*
lrespr
	moveq	#0,d0			no parameters after name
	bsr.l	file_in 		open input file and read header
	bne.s	file_rt3		... oops
	moveq	#mt.alres,d0
	move.l	(a6,a1.l),d1		for file length
	movem.l d1/a0,-(sp)		save length and channel
	trap	#1
	tst.l	d0
	beq.s	lrespr_load
	move.l	(sp),d1
	moveq	#mt.alchp,d0		allocate in heap
	moveq	#-1,d2			for my job
	trap	#1
lrespr_load
	move.l	a0,a1			start in a1
	movem.l (sp)+,d2/a0		channel in a0, length in d2
	tst.l	d0
	bne.l	ut_fclos		... oops
	move.l	a1,a2			save start
	bsr.s	file_load		load file (and close)
file_rt3
	bne.s	file_rt1		... oops
; qlib_sys calls ut_wtext on the channel in a0. This usually fails
; harmlessly but crashes at least QemuLator spectaculary (MK)
	suba.l	a0,a0
	jmp	(a2)

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
