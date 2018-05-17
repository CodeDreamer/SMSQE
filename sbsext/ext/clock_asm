* Resident clock program    1985   T.Tebby   QJUMP
*
	section exten
*
	xdef	clock
*
	xref	ut_chan0		get channel ID or zero
	xref	ut_gxst1		get one (only) string
	xref	ut_crjd1		create Job
	xref	ut_ajob 		activate Job
	xref	ut_rjbme		suicide
	xref	tm_susrc		suspend and read clock
	xref	err_bp			oops
*
	include dev8_sbsext_ext_keys
*
ck_jmp	 equ	$00		jmp ck_job
ck_flag  equ	$06		$4afb
ck_name  equ	$08		5,'Clock'
ck_chid  equ	$10		0 or -1 if no channel given
ck_cstr  equ	$14		0 if no control string given  (up to 64 chars)
ck.cstrl equ	$40		max length of string
ck_data  equ	$56		end of program data
ck_dayw  equ	ck_data+2	start of day of week (3 letter)
ck_cent  equ	ck_data+6	... century
ck_year  equ	ck_data+8	... year (2 digit)
ck_month equ	ck_data+11	... month (3 letter)
ck_day	 equ	ck_data+15	... day (2 digit)
ck_hour  equ	ck_data+18	... hour (2 digit)
ck_min	 equ	ck_data+21	... minute (2 digit)
ck_sec	 equ	ck_data+24	... second (2 digit)
ck_cvstk equ	ck_data+26	convert stack 26 bytes long
ck_work  equ	26+96		convert stack + stack space

ck_digit dc.b	'C',ck_cent-ck_cvstk
	dc.b	'Y',ck_year-ck_cvstk
	dc.b	'D',ck_day-ck_cvstk
	dc.b	'H',ck_hour-ck_cvstk
	dc.b	'M',ck_min-ck_cvstk
	dc.b	'S',ck_sec-ck_cvstk

*
ck_jobd  dc.w	ck_data+ck_work-$10	data space required
	 dc.w	ck_job-*		address of program
	 dc.w	5,'Clock'		name
*
clock
	bsr.l	ut_chan0		get the channel id (or zero)
	bne.s	ck_rts
	move.l	a0,a4			save it
*
	move.l	a5,d7			set string present flag
	sub.l	a3,d7
	beq.s	ck_cjob 		... none, create job
	bsr.l	ut_gxst1		get a string
	bne.s	ck_rts			... oops
*
	move.w	(a6,a1.l),d7
	cmp.w	#ck.cstrl,d7		is string too long?
	bgt.l	err_bp
	move.l	a1,a5			save string pointer
*
ck_cjob
	move.l	a4,a3			save channel ID
	lea	ck_jobd(pc),a4		set Job definition
	move.w	a3,d0
	sgt	d1
	ext.w	d1
	ext.l	d1
	bsr.l	ut_crjd1		create resident Job
	bne.s	ck_rts
	move.l	a3,(a4)+		set channel id
	move.w	d7,(a4)+		set string
	beq.s	ck_active
*
ck_cpys
	addq.l	#2,a5
	move.w	(a6,a5.l),(a4)+ 	copy string
	subq.w	#2,d7
	bgt.s	ck_cpys
*
ck_active
	moveq	#1,d2			low priority
	bra.l	ut_ajob
ck_rts
	rts
	page
*
ck_job
	lea	ck_cvstk(a6),a3 	set convert stack address
	lea	ck_cstr(a6),a5		set control string address
	tst.w	(a5)			is there one?
	bne.s	ckj_chan		... yes
	lea	ckj_str(pc),a5		... no, set default string
ckj_chan
	move.l	ck_chid(a6),a4		get channel ID
	move.w	a4,d0			is it set?
	bgt.s	ckj_loop		... yes
	lea	ckj_win(pc),a1		... no, open window
	jsr	ut..scr*3+qlv.off     
	move.l	a0,a4			and save the ID
*
ckj_loop
	moveq	#10,d3			for 10 cycles (1/5 second)
	bsr.l	tm_susrc		suspend and read clock
*
	suba.l	a6,a6			set to 0 for cn.date
	move.l	a3,a1			use special stack
	jsr	cn..date*3+qlv.off		     to convert date into
	addq.l	#2,a1
	jsr	cn..day*3+qlv.off		     and day into
*
	moveq	#sd.pos,d0		set the initial position
	moveq	#0,d1
	moveq	#0,d2
	moveq	#-1,d3
	move.l	a4,a0
	trap	#3
	tst.l	d0			ok?
	bne.l	ut_rjbme		... no, suicide
*
	move.l	a3,a6			set address of buffer
	move.l	a5,a2			set address of control string
	move.w	(a2)+,d6		get length
ckj_wr_loop
	move.b	(a2)+,d1		get next character
	cmp.b	#'%',d1 		is it '%'
	bne.s	ckj_dolr		...no, check '$'
	move.b	(a2)+,d1		get control letter
	bclr	#5,d1			upper case
*
	moveq	#5,d0
	lea	ck_digit-1,a1
ckj_digl
	addq.l	#1,a1
	cmp.b	(a1)+,d1		next character
	dbeq	d0,ckj_digl

	moveq	#-1,d0
	move.b	(a1),d0 		offset
	bra.s	ckj_s2ch		send two characters
*
ckj_dolr
	cmp.b	#'$',d1 		is it '$'
	bne.s	ckj_char		... no, it is just an ordinary character
	move.b	(a2)+,d1		get control letter
	bclr	#5,d1			in upper case
	moveq	#3,d2			it is three letter
	moveq	#ck_month-ck_cvstk+1,d0
	cmp.b	#'M',d1 		is it month?
	beq.s	ckj_s3ch		... yes, send string
	moveq	#ck_dayw-ck_cvstk+1,d0	  ... no, assume day of week
ckj_s3ch
	move.b	-1(a3,d0.w),(a6)+
ckj_s2ch
	subq.w	#1,d6			one fewer characters in string
	move.b	(a3,d0.w),(a6)+
	move.b	1(a3,d0.w),d1
ckj_char
	move.b	d1,(a6)+		send a byte
	subq.w	#1,d6			and see if any left in string
	bgt.s	ckj_wr_loop

	move.l	a3,a1
	move.l	a6,d2
	sub.l	a1,d2
	moveq	#io.sstrg,d0		send all the chars
	moveq	#0,d3
	trap	#3
	bra.l	ckj_loop		next time
*
ckj_win dc.b	0,0,16,7
	dc.w	60,20,512-64,256-50
ckj_str dc.w	18,'$d %d $m %h:%m:%s '
	end
