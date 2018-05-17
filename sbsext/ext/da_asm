* Direct access file handling V0.9     1984/1985  Tony Tebby  QJUMP
*
*	GET #n [\pointer] {,variable}   get value(s) from file
*	BGET #n [\pointer] {,variable}  get unsigned byte(s) from file
*	WGET #n [\pointer] {,variable}  get unsigned word(s) from file
*	LGET #n [\pointer] {,variable}  get signed long word(s) from file
*	HGET #n {,variable}             get values from header
*	PUT #n [\pointer] {,value}      put values(s) to file
*	BPUT #n [\pointer] {,value}     put unsigned byte(s) to file
*	UPUT #n [\pointer] {,value}     as BPUT but untranslated
*	WPUT #n [\pointer] {,value}     put unsigned word(s) to file
*	LPUT #n [\pointer] {,value}     put signed long word(s) to file
*	HPUT #n {,value}                put values to header
*	TRUNCATE #n [\pointer]		truncate file
*	FLUSH #n			flush file
*	FPOS (#n)			function to find current file pointer  
*
	section exten
*
	xdef	get
	xdef	bget
	xdef	wget
	xdef	lget
	xdef	hget
	xdef	put
	xdef	bput
	xdef	uput
	xdef	wput
	xdef	lput
	xdef	hput
	xdef	truncate
	xdef	flush
	xdef	fpos

*
	xref	ut_chkri		check RI stack
	xref	ut_ckri6		... for 6 bytes
	xref	ut_chan 		get #channel
	xref	ut_gtli1		get one long integer
	xref	ut_gtlin		get long integers
	xref	ut_lfloat		float d1 onto RI stack
	xref	ut_retfp		return floating point
	xref	ut_trp3r		trap #3 relative
	xref	ut_fhead		get file header
*
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_io'

* The HGET and HPUT procedures are not really part of the DA routines but are
* here for convenience
*
hget
	bsr.s	hda_chan	get channel
	bne.s	hda_rts
	jsr	ut_fhead	get header
	bne.s	hda_rts 	... oops
	moveq	#20,d1
	jsr	ut_chkri	get space on stack
	move.l	sb_buffb(a6),a0
	lea	14(a6,a0.l),a0	pointer to end of set header values
	add.l	a6,a1
	move.l	-(a0),-(a1)	extra
	move.l	-(a0),-(a1)	data
	bsr.s	hg_moveb	type
	bsr.s	hg_moveb	access
	move.l	-(a0),-(a1)	length
	sub.l	a6,a1
	move.l	a1,sb_arthp(a6) set arithmetic stack pointer
hg_loop
	cmp.l	a3,a5		another parameter?
	ble.s	hg_ok

	move.w	#$0f0f,d0
	and.w	(a6,a3.l),d0	type?
	beq.s	hg_skip
	moveq	#qa.fltli,d0	float it
	jsr	qa.op*3+qlv.off
	move.l	a1,sb_arthp(a6)
	jsr	sb.putp*3+qlv.off assign value
	bra.s	hg_eloop
hg_skip
	addq.l	#4,a1
hg_eloop
	addq.l	#8,a3
	bra.s	hg_loop

hg_ok
	moveq	#0,d0
hda_rts
	rts

hg_moveb
	move.b	-(a0),d0
	ext.w	d0
	ext.l	d0
	move.l	d0,-(a1)
	rts

hda_chan
	moveq	#3,d6		default channel 3
	jmp	ut_chan 	get channel id

hput
	bsr.s	hda_chan	get channel
	bne.s	hda_rts
	jsr	ut_gtlin	get long integers
	bne.s	hda_rts
	cmp.w	#5,d3		more than 5?
	bhi.s	hda_bp
	move.l	sb_buffb(a6),a2
	lea	20(a6,a2.l),a4	preset 5
	clr.l	-(a4)
	clr.l	-(a4)
	clr.l	-(a4)
	clr.l	-(a4)
	clr.l	-(a4)		clear the header
	add.l	a6,a1
	bra.s	hp_eloop
hp_loop
	move.l	(a1)+,(a4)+
hp_eloop
	dbra	d3,hp_loop

	lea	4(a6,a2.l),a1
	lea	(a1),a4
	move.l	(a1)+,d0	two of the items are bytes
	move.b	d0,(a4)+
	move.l	(a1)+,d0
	move.b	d0,(a4)+
	move.l	(a1)+,(a4)+
	move.l	(a1)+,(a4)+

	move.l	a2,a1
	moveq	#iof.shdr,d0
	jmp	ut_trp3r	... set header

hda_bp
	moveq	#err.ipar,d0
	rts
*
vv_ptr	 equ	$08	position of pointer to vv area on stack
parm_top equ	$04	position of stored a5 on stack
chan_id  equ	$00	position of channel id on stack
*
* get items from a file
*
get
	bsr.l	chan_set
get_loop
	bsr.l	ut_ckri6	check for a little bit of room on the ri stack
	bsr.l	type_set	get type of next
	bgt.s	get_int 	... integer
	beq.s	get_fp		... floating point
	tst.b	1(a6,a3.l)	is it substring?
	beq.l	file_bp 	... yes
*
	moveq	#2,d2		get length of string
	move.l	sb_arthp(a6),a1
	bsr.l	fstrg_push
	bne.l	exit_12 	oops
	move.w	(a6,a1.l),d4	save it
	blt.s	get_lchk	... oops
	moveq	#0,d1
	move.w	d4,d1		round up to nearest even word
	addq.w	#3,d1		(+2)
	bclr	#0,d1
	move.l	d1,d5		save rounded value
	bsr.l	ut_chkri	check for room for string
	suba.w	d5,a1		move stack pointer down
	move.w	d4,(a6,a1.l)	and put length in
	move.w	d4,d2		fetch characters of string
	beq.s	get_let 	... none
	addq.l	#2,a1
	bsr.l	fstrg
	subq.l	#2,a1		and include string length in return value
	bra.s	get_lchk
*
get_fp
	moveq	#6,d2		get six bytes
	bsr.l	fstrg_push
	bne.s	get_lchk
	and.b	#$f,(a6,a1.l)	mask out top end rubbish
	bra.s	get_let
*
get_int
	moveq	#2,d2		get two bytes
	bsr.l	fstrg_push	push bytes onto a1
*
get_lchk
	bne.l	exit_12 	was there a read error?
get_let
	move.l	a1,sb_arthp(a6)   set stack pointer
	jsr	sb.putp*3+qlv.off and assign value
	addq.l	#8,a3		move to next parameter
	bra.s	get_loop	carry on
*
* get a byte (or bytes into a string) and convert to fp if necessary
*
bget
	bsr.l	chan_set	set up channel id etc.
bget_loop
	bsr.l	ut_ckri6	check for room for 1 fp
	bsr.l	type_set	find type
	bge.s	bget_type
	addq.b	#2,d1		was it substring array?
	bne.l	file_bp 	... won't do string
	move.w	6(a6,a1.l),d2	length to fetch
	move.l	(a6,a1.l),a1	and where to put it
	add.l	sb_datab(a6),a1
	bsr.l	fstrg		fetch it
	addq.l	#8,a3		move to next parameter
	bra.s	bget_loop

bget_type
	move.b	d1,d6		save type flag
	moveq	#1,d2		get one byte
	bsr.l	fstrg_push
	bne.l	exit_12 	oops
	subq.l	#1,a1		and put a zero byte on the stack
	clr.b	(a6,a1.l)
	tst.b	d6		was fp required?
	bgt.s	bget_let	... no
	moveq	#qa.float,d0	... yes, float it
	jsr	qa.op*3+qlv.off
bget_let
	move.l	a1,sb_arthp(a6) set arithmetic stack pointer
	jsr	sb.putp*3+qlv.off assign value
	addq.l	#8,a3		move to next parameter
	bra.s	bget_loop
*
* get a word and convert to fp if necessary
*
wget
	bsr.l	chan_set	set up channel id etc.
wget_loop
	bsr.l	ut_ckri6	check for room for 1 fp
	bsr.l	type_set	find type
	blt.l	file_bp 	... string
	move.b	d1,d6		save type flag
	moveq	#2,d2		get two bytes
	bsr.l	fstrg_push
	bne.l	exit_12 	oops
	tst.b	d6		was fp required?
	bgt.s	wget_let	... no
	subq.l	#2,a1
	clr.w	(a6,a1.l)	... yes, make long
	moveq	#qa.fltli,d0	and float it
	jsr	qa.op*3+qlv.off
wget_let
	move.l	a1,sb_arthp(a6) set arithmetic stack pointer
	jsr	sb.putp*3+qlv.off assign value
	addq.l	#8,a3		move to next parameter
	bra.s	wget_loop
*
* get a long and convert to fp
*
lget
	bsr.l	chan_set	set up channel id etc.
lget_loop
	bsr.l	ut_ckri6	check for room for 1 fp
	bsr.l	type_set	find type
	bne.l	file_bp 	... not float
	moveq	#4,d2		get four bytes
	bsr.l	fstrg_push
	bne.l	exit_12 	oops
	bgt.s	wget_let	... no
	moveq	#qa.fltli,d0	... yes, float it
	jsr	qa.op*3+qlv.off
	move.l	a1,sb_arthp(a6) set arithmetic stack pointer
	jsr	sb.putp*3+qlv.off assign value
	addq.l	#8,a3		move to next parameter
	bra.s	lget_loop
*
* put data onto file
*
put
	bsr.l	chan_set	set up the channel id etc.
put_loop
	bsr.l	type_set	find the type
	beq.s	put_fp		floating point
	bgt.s	put_int 	integer
*
	move.l	sb.gtstr*3+qlv.off+2,a2 get a string
	bsr.l	put_on_a1	just one
	move.l	#'ST  ',d2	flag as string
	move.w	(a6,a1.l),d2	find length
	addq.w	#2,d2		and put length and string on file
	bra.s	put_file
put_fp
	move.l	sb.gtfp*3+qlv.off+2,a2	get a floating point
	bsr.l	put_on_a1	just one
	moveq	#6,d2		and put 6 bytes on file
	bra.s	put_file
put_int
	move.l	sb.gtint*3+qlv.off+2,a2  get an integer
	bsr.l	put_on_a1	just one
	moveq	#2,d2		and put 2 bytes on file
put_file
	bsr.l	sstrg		put bytes on file
	bra.s	put_loop	carry on

*
* put bytes to file untranslated
*
uput
	bsr.l	chan_set	set up the channel id etc.
	lea	sustrg,a4
	bra.s	bput_loop
*
* put bytes to the file
*
bput
	bsr.l	chan_set	set up channel id etc.
	lea	sstrg,a4

bput_loop
	cmpa.l	parm_top(sp),a3 end of list?
	beq.l	exit_pos	yes (d0 already set)
	moveq	#$0f,d1
	and.w	(a6,a3.l),d1	type
	subq.w	#1,d1		string?
	bne.s	bput_byte	... no
	move.l	sb.gtstr*3+qlv.off+2,a2 get a string
	bsr.l	put_on_a1
	move.w	(a6,a1.l),d2	... number to send
	addq.l	#2,a1
	bra.s	bput_do

bput_byte
	move.l	sb.gtint*3+qlv.off+2,a2 get an integer
	bsr.l	put_on_a1	just one
	tst.b	(a6,a1.l)	msbyte must be zero
	beq.s	bput_file	good
	moveq	#err.ovfl,d0	no, call it overflow
	bra.l	exit_12
bput_file
	addq.l	#1,a1		just the lsbyte
	moveq	#1,d2
bput_do
	jsr	(a4)		onto the file
	bra.s	bput_loop

*
* put words to the file
*
wput
	bsr.l	chan_set	set up channel id etc.
	jsr	ut_gtlin	get long integers
	bne.l	exit_12
	bra.s	wput_le
wput_loop
	moveq	#2,d2
	addq.w	#2,a1		lsword only
	bsr.l	sstrg
	addq.l	#2,a1		... next long word
wput_le
	dbra	d3,wput_loop
	bra.s	exit_pos
*
* put long words to the file
*
lput
	bsr.l	chan_set	set up channel id etc.
	jsr	ut_gtlin	get long integers
	bne.s	exit_12

	moveq	#0,d2
	move.w	d3,d2
	lsl.l	#2,d2		number of bytes in longs
	bsr.l	sstrg		onto the file
	bra.s	exit_pos

*
* truncate
*
truncate
	moveq	#iof.trnc,d5	truncate
	bra.s	da_action
*
* flush
*
flush
	moveq	#iof.flsh,d5	flush
da_action
	bsr.l	chan_set	set up channel id etc
	cmp.l	parm_top(sp),a3 there should be no other parameters
	bne.s	file_bp
	move.l	d5,d0		do action
	bsr.s	trap4_3 	do trap
	bra.s	exit_12
*
* get file pointer
*
fpos
	bsr.s	chan_set	set up channel id etc
	cmp.l	parm_top(sp),a3 there should be no other parameters
	bne.s	file_bp
	bsr.l	ut_ckri6	make room on stack for return value (fp)
	move.l	a1,d7		save ri stack pointer
	bsr.s	get_pos
	bsr.l	ut_retfp	return a floating point
	bra.s	exit_12
*
get_pos
	moveq	#iof.posr,d0	position file relative
	moveq	#0,d1		by no bytes
	moveq	#0,d3		and return immediately
	move.l	4+chan_id(sp),a0 set channel id
	trap	#3
	move.l	d7,a1		restore ri stack pointer
	tst.l	d0		ok?
	beq.s	pos_ok		yes
	moveq	#err.nc,d2	not complete is quite normal
	cmp.l	d2,d0
	beq.s	pos_ok
	moveq	#err.eof,d2	end of file is still ok
	cmp.l	d2,d0
	bne.s	exit_16 	no, it is not end of file
pos_ok
	bsr.l	ut_lfloat	float d1 onto stack
	moveq	#0,d0		set no error
	rts
*
exit_pos
	move.l	vv_ptr(sp),d7	get the pointer to the pointer variable
	blt.s	exit_12 	... not there
	add.l	sb_datab(a6),d7 add base of vv area
	addq.l	#6,d7		pretend this is the ri stack!!
	bsr.s	get_pos 	get the position
	bra.s	exit_12 	and exit
*
file_bp
	moveq	#err.ipar,d0	bad parameter
exit_12
	add.w	#12,sp		remove channel id and top of parameter list
	rts
exit_16
	addq.l	#4,sp
	bra.s	exit_12
*
* put next item on the a1 stack (a2 set to ca.gt...)
*
put_on_a1
	lea	8(a3),a5	get just one item
	jsr	(a2)		call appropriate type
	move.l	a5,a3		move onto next item
	bne.s	exit_16 	remove return and channel id and saved a5
	rts
*
* fetch bytes
*
fstrg_push
	suba.w	d2,a1		make room on a1 stack
fstrg
	moveq	#iob.fmul,d0	fetch a known number of bytes
	bra.s	trap4_3
*
* send bytes untranslated
*
sustrg
	moveq	#iob.suml,d0	send a known number of bytes
	bra.s	trap4_3
*
* send bytes
*
sstrg
	moveq	#iob.smul,d0	send a known number of bytes
*
trap4_3
	move.l	4+chan_id(sp),a0 set channel id
	bsr.l	ut_trp3r	trap #3 relative
	suba.w	d1,a1		restore a1 to original
	bne.s	exit_16
	rts
*
* set up and save channel id and top of parameter list
*
chan_set
	moveq	#%0111000,d7	save separator on first parameter
	cmp.l	a3,a5		if there is one!
	beq.s	chans_1
	and.b	1(a6,a3.l),d7	all other info is masked out
chans_1
	moveq	#3,d6		default channel 3
	bsr.l	ut_chan 	get channel id
	bne.s	chans_out	return directly
	move.l	(sp),a4 	get return address
	clr.l	(sp)		no pointer to return
	st	(sp)		... pointer in vv
	move.l	a5,-(sp)	save top of parameter list
	move.l	a0,-(sp)	and channel id
*
	cmp.b	#%00110000,d7	was separator '\'
	bne.s	chans_rts	... no
	move.w	#$0f0f,d7	mask odd bits
	and.w	(a6,a3.l),d7	... in this parameter type
	cmp.w	#$0202,d7	is it a floating point variable?
	bne.s	set_fptr	... no
	move.l	4(a6,a3.l),vv_ptr(sp) ... yes, set value pointer
set_fptr
	bsr.l	ut_gtli1	get one long integer
	bne.s	exit_12 	... oops
	addq.l	#8,a3		skip this parameter
	move.l	#iof.posa,d0	set file position
	move.l	(a6,a1.l),d1
	addq.l	#4,sb_arthp(a6) (reset ri stack)
	moveq	#0,d3
	move.l	chan_id(sp),a0
	trap	#3		ignore errors
chans_rts
	jmp	(a4)		and return
*
chans_out
	addq.l	#4,sp		remove one return address
	rts			and return to basic
*
* set up type of next parameter  (string array and substring array are permitted)
*
type_set
	cmpa.l	4+parm_top(sp),a3 end of parameter list?
	blt.s	type_var	... no
	moveq	#0,d0		... yes, ok
	addq.l	#4,sp		remove return
	bra.l	exit_pos	and exit (setting position)
type_var
	move.w	#$0f0f,d1	mask out separators
	and.w	(a6,a3.l),d1	get name type
	beq.s	type_bp 	... null
	cmp.w	#$0300,d1	... is it sub-string array?
	blt.s	type_test	... no
	cmp.w	#$0301,d1	... is it string array?
	bgt.s	type_test	... no
	move.l	4(a6,a3.l),a1	... yes, get value pointer
	add.l	sb_datab(a6),a1
	cmp.w	#1,4(a6,a1.l)	just one dimension?
	beq.s	typs_ok 	... yes
	bra.s	type_bp 	... no
*
type_test
	ror.w	#8,d1		get name usage
	move.w	#%11000101,d2	set mask of acceptable usage
	btst	d1,d2
	beq.s	type_bp 	not permissable
	lsr.w	#8,d1		get type
typs_ok
	subq.b	#2,d1		set -ve for string, 0 for fp, +ve for integer
	rts
type_bp
	addq.l	#4,sp		 remove return
	bra.l	file_bp 	 and bad parameter
	end
