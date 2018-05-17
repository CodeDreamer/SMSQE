* Reset windows    V0.8    Tony Tebby	 QJUMP
*
*	WMON mode			resets windows to monitor defaults 
*	WTV mode			resets windows to tv defaults
*
	section exten
*
	xdef	wmon
	xdef	wtv
*
	xref	wmon_def
	xref	wtv_def  
	xref	ut_gtint
	xref	ut_gtin1
*
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sbasic'
*
wmon
	lea	wmon_def(pc),a4 set pointer to definitions
	moveq	#0,d7		monitor type
	bra.s	wdef
wtv
	lea	wtv_def(pc),a4
	moveq	#1,d7		tv type
wdef
	sub.w	#$18,sp
	moveq	#0,d5
	moveq	#-1,d6
	cmp.l	a3,a5		 ; any params?
	beq.s	wdef_do
	move.w	#$0f0f,d0
	and.w	(a6,a3.l),d0	 ; nul mode parameter?
	beq.s	wdef_posn	 ; ... yes
	jsr	ut_gtin1	 ; ... no, get mode
	bne.l	wdef_exit
	move.w	(a6,a1.l),d6	 ; put mode safe in d6
	moveq	#-1,d1
	moveq	#-1,d2
	moveq	#sms.dmod,d0
	trap	#1
	cmp.b	#2,d1		 ; mode 2
	beq.s	wdef_posn	 ; ... yes, always clear / reset
	ext.l	d6		 ; ... do not clear / reset
wdef_posn
	addq.l	#8,a3
	cmp.l	a3,a5
	beq.s	wdef_do
	jsr	ut_gtint	 ; get integers
	bne.l	wdef_exit	 ; ... oops
	move.l	(a6,a1.l),d5	 ; offset
	subq.w	#1,d3		 ; one or two ?
	bgt.s	wdef_do 	 ; x and y
	move.l	d5,d1
	swap	d1
	move.w	d1,d5		 ; x and y the same
*
wdef_do
	swap	d6		 ; save mode

	lea	$10(sp),a5	 ; get three cursor positions
	move.l	sb_chanb(a6),a2 get base of channel table
	move.l	(a6,a2.l),a0
	moveq	#-1,d3
	moveq	#iop.pinf,d0
	trap	#3
	tst.l	d0		 ; pointer interface?
	bne.s	wdef_set
	cmp.l	#'1.63',d1
	blo.s	wdef_set	 ; not recent enough

	add.w	#$28*3,a2

	moveq	#2,d4
wdef_pixq
	sub.w	#$28,a2
	move.l	(a6,a2.l),a0
	move.l	a5,a1
	subq.l	#4,a5
	moveq	#-1,d3
	moveq	#iow.pixq,d0	 ; save the cursor position
	trap	#3
	dbra	d4,wdef_pixq

	move.l	a4,a5		 ; scan the definitions to get outline
	moveq	#-1,d3		 ; max origin
	moveq	#0,d2		 ; min limit

	moveq	#2,d4
wdef_oloop
	addq.w	#4,a5		 ; skip border ink and paper
	move.l	(a5)+,d0	 ; size
	move.l	(a5)+,d1	 ; origin
	add.l	d1,d0		 ; limit

wdef_ocheck
	swap	d0
	swap	d1
	swap	d2
	swap	d3
	cmp.w	d2,d0		 ; new limit?
	bls.s	wdef_osize	 ; ... no
	move.w	d0,d2
wdef_osize
	cmp.w	d3,d1		 ; new size?
	bhs.s	wdef_onext
	move.w	d1,d3
wdef_onext
	not.w	d4		 ; do again?
	bmi.s	wdef_ocheck

	dbra	d4,wdef_oloop

	sub.l	d3,d2		 ; set size
	add.l	d5,d3		 ; and origin
	move.l	sp,a1
	movem.l d2/d3,(a1)
	moveq	#0,d1
	moveq	#1,d2
	moveq	#-1,d3
	moveq	#iop.outl,d0	 ; set (preserve) outline
	trap	#3
	swap	d0		 ; msb may be set
	bset	#0,d0		 ; lsb will be set
	and.w	d0,d6		 ; -ve outline failed +ve outline OK 0 don't care

wdef_set
	moveq	#2,d4		and set the first three channels
	lea	$c(sp),a5
wdef_loop
	move.b	(a4)+,d1	border colour
	move.b	(a4)+,d2	and width
	addq.w	#2,a4
	move.l	sp,a1
	move.l	(a4)+,(a1)	size
	move.l	d5,d3
	add.l	(a4)+,d3	origin
	move.l	d3,4(a1)
	move.l	(a6,a2.l),a0	set this channel
	moveq	#-1,d3		no timeout
	moveq	#iow.defw,d0	 define window
	trap	#3		do trap
	tst.w	d6		clear required?
	beq.s	wdef_eloop	... nothing
	bgt.s	wdef_scurs	reset cursor position
	moveq	#iow.clra,d0	... clear all
	bra.s	wdef_trap
wdef_scurs
	move.w	(a5)+,d1
	move.w	(a5)+,d2
	moveq	#iow.spix,d0   ... reset cursor
wdef_trap
	trap	#3

wdef_eloop
	add.w	#$28,a2 	move channel pointer
	dbra	d4,wdef_loop

wdef_mode
	moveq	#sms.dmod,d0	set up display mode
	swap	d6
	move.w	d6,d1		either 8 or 256 gets 8 colour mode
	bmi.s	wdef_ok
	lsr.w	#5,d6
	or.b	d6,d1
	and.w	#8,d1
	move.w	d7,d2		set tv
	trap	#1
wdef_ok
	moveq	#0,d0
wdef_exit
	add.w	#$18,sp
	rts

	end
