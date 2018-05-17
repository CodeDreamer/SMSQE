* Resident alarm program    1985   T.Tebby   QJUMP
*
	section exten
*
	xdef	alarm
*
	xref	ut_gxin2		get two integers
	xref	ut_crjob		create Job
	xref	ut_ajob 		activate Job
	xref	ut_rjbme		suicide
	xref	tm_susrc		suspend and read clock
*
	include dev8_sbsext_ext_keys
*
al_jmp	 equ	$00		jmp al_job
al_flag  equ	$06		$4afb
al_name  equ	$08		12,'Alarm '
al_tstr  equ	$10		time ' hh:mm'
al_time  equ	$16		time of day in 4 second units
al_data  equ	$18		end of program data
al_work  equ	$10		stack space
*
al_jobd
	 dc.w	al_data+al_work-$10	data space required
	 dc.w	al_job-*		address of program
	 dc.w	12,'Alarm '		name (completed with the time)
al_active
	moveq	#1,d2			low priority
	bra.l	ut_ajob
*
al_bp
	moveq	#err.bp,d0
al_rts
	rts
*
	page
alarm
	bsr.l	ut_gxin2		get exactly two integers
	bne.s	al_rts
	move.w	(a6,a1.l),d6		set first time parameter
	move.w	2(a6,a1.l),d7		... and second
	cmp.w	#23,d6			too many hours?
	bhi.s	al_bp			... yes
	cmp.w	#59,d7			too many minutes?
	bhi.s	al_bp			... yes
*
	move.l	a1,-(sp)
	lea	al_jobd(pc),a4		set up alarm job
	bsr.l	ut_crjob
	move.l	(sp)+,a1
	bne.s	al_rts			... oops
*
	move.l	bv_bfbas(a6),a0 	convert time to hh:mm in buffer
	moveq	#' ',d4 		space
	bsr.s	al_conv 		... hh
	moveq	#':',d4 		colon
	bsr.s	al_conv 		... mm
	move.l	bv_bfbas(a6),a0
	move.l	(a6,a0.l),(a4)+ 	put into job
	move.w	4(a6,a0.l),(a4)+
*
	mulu	#60,d6			set time in minutes
	add.w	d6,d7
	mulu	#15,d7			in units of 4 seconds
	move.w	d7,(a4) 		and set in job
	bra.s	al_active		activate job
*
al_conv
	moveq	#100,d0
	add.w	d0,(a6,a1.l)
	jsr	cn..itod*3+qlv.off     
	move.b	d4,-3(a6,a0.l) 
	rts
	page
*
al_job
al_loop
	moveq	#50,d3			for 1 second
	bsr.s	tm_susrc		suspend and read clock
	lsr.l	#2,d1			make time manageable in 4 second units
	divu	#21600,d1		24*60*15=21600 4 seconds in a day
	swap	d1			remainder is 4 seconds
	sub.w	al_time(a6),d1
	cmp.w	#150,d1 		remain set for 10 minutes
	bhi.s	al_loop 		out of range, try again
*
	moveq	#mt.ipcom,d0		beep!!!
	lea	al_beep(pc),a3
	trap	#1
	bra.l	ut_rjbme		and suicide
*
*	command is beep,6,13,1000,20000,5,0,0,0
*
al_beep dc.b	$a,8,0,0,%10101010,%10101010,7,14,0,4,0,80,80,0,1
	dc.b	0	to align!!!!!
*
	end
