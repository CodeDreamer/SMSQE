; SMSQ GOLD Card Host module	v1.01
;
; 2003-10-05  1.01  Fixed copy problem that could hang SMSQ/E later (MK)

	section host

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_stella_bl'

	data	4			 ; flag for LRESPR
base
	moveq	#sms.info,d0
	trap	#do.sms2
	move.l	d2,d4			 ; version number in d4

	trap	#0			 ; supervisor mode for loader
	move.w	#$2700,sr

	moveq	#127,d1 		 ; pause 2.5 seconds

glh_wait
	moveq	#8,d0
	and.b	$18021,d0
	beq.s	glh_wait

	st	$18021
	dbra	d1,glh_wait

	lea	trailer,a0
	move.l	a0,a2
	move.l	a0,a5
glh_lookend
	move.l	sbl_mbase(a5),d0	 ; header length
	beq.s	glh_move		 ; no more
	add.l	sbl_mlength(a5),d0
	add.l	d0,a5			 ; next header
	bra.s	glh_lookend

glh_move
	lea	$30000,a1		 ; a1 is where we want it
					 ; a2 is where it is
					 ; a5 is the top

	move.l	a1,a4
	sub.l	a2,a4
	add.l	a5,a4			 ; a4 is the new top
	add.w	#trailer-base,a4	 ; + a bit spare
	cmp.l	a4,a2			 ; does it overlap?
	blt.s	glh_jump		 ; ... yes, jump to where it is
	move.l	a1,a0			 ; ... no, jump to new start

glh_copy
	move.l	(a2)+,(a1)+
	cmp.l	a5,a2
	blt.s	glh_copy
	clr.l	(a1)			 ; signal "end of modules"

glh_jump
	pea	base			 ; set base address for debug
	add.l	sbl_mbase(a0),a0
	jmp	(a0)

	section trailer

trailer
	dc.l	0			 ; no header
	dc.l	trailer-base		 ; length of module
	dc.l	0			 ; length
	dc.l	0			 ; checksum
	dc.l	0			 ; no fixup
	dc.l	0


	end
