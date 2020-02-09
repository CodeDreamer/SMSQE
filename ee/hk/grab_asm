; Grabber  V2.00     1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hk_grab

	xref	hk_rude
	xref	hktx_grab
	xref	cv_decil
	xref	gu_mexec

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sys'
	include 'dev8_keys_jcb'

;+++
; Set up job to grab memory and wait for job to be waiting
;
;	d1 c s	(word) memory to grab (-ve ask)
;	a0 c s	guardian window ID
;	d4-d7/a2-a6 preserved
;	status return arbitrary
;---
hk_grab
reglist reg	d4/d7/a2
stk_ask equ	$8
	movem.l reglist,-(sp)

	ext.l	d1
	move.l	d1,d4			 ; save space required
	bgt.s	hgb_info		 ; ... specified

hgb_rspc
	moveq	#iow.scur,d0		 ; position the cursor
	moveq	#5,d1
	moveq	#5,d2
	moveq	#forever,d3
	trap	#3

	jsr	hktx_grab		 ; grab message
	move.w	(a1)+,d2
	moveq	#iob.smul,d0		 ; send multiple bytes
	trap	#do.io

	moveq	#stk_ask,d2
	sub.w	d2,sp			 ; use stack for reply
	move.l	sp,a1
	moveq	#2,d1			 ; two chars there
	move.w	#'32',(a1)+
	moveq	#iob.elin,d0
	trap	#do.io
	move.l	a1,d7			 ; top of buffer
	tst.l	d0
	bne.s	hgb_oops

	moveq	#iow.clra,d0		 ; clear screen
	trap	#do.io

	move.l	a0,a2
	move.l	sp,a0
	jsr	cv_decil
	move.l	a2,a0
	addq.l	#stk_ask,sp		 ; clean up stack
	blt.s	hgb_rspc		 ; ... oops
	move.l	d1,d4			 ; reply
	moveq	#32,d1			 ; at least this much
	cmp.l	d1,d4
	blt.s	hgb_rspc		 ; ... no
	lsl.l	#5,d1			 ; try 1024
	cmp.l	d1,d4
	bgt.s	hgb_rspc		 ; too much

hgb_info
	moveq	#sms.info,d0		 ; get info on me
	trap	#do.sms2

	lea	daemon,a0		 ; my daemon job
	move.l	d1,-(sp)		 ; my ID
	move.w	d4,-(sp)		 ; put grabber memory on stack
	move.w	#6,-(sp)		 ; two bytes
	move.l	sp,a1
frame	equ	8

	moveq	#0,d1			 ; independent
	move.l	#$007fffff,d2		 ; high priority and wait
	moveq	#$10,d3 		 ; min dataspace
	jsr	gu_mexec		 ; exec copy of memory

	addq.l	#frame,sp		 ; clean up stack

	movem.l (sp)+,reglist
	rts

hgb_oops
	jsr	hk_rude 		 ; rude noise
	bra.l	hgb_suicide		 ; can do no more
	page
daemon
	bra.s	dem_start
	dc.w	0
	dc.w	0

	dc.w	$4afb
	dc.w	6,'Daemon'

dem_start
	moveq	#sms.injb,d0		 ; get job information
	moveq	#0,d1			 ; on job 0
	moveq	#0,d2
	trap	#1
	move.b	d3,d7			 ; save priority
	move.l	a0,a6			 ; ... and base

	moveq	#sms.spjb,d0		 ; set priority
	moveq	#0,d1			 ; ... of BASIC
	moveq	#0,d2			 ; ... to zero
	trap	#1

	moveq	#sms.info,d0
	trap	#1			 ; get base of sysvar
	move.l	a0,a5			 ; safe
	move.w	sys_swtc(a5),d6 	 ; save switch queue character
	move.w	#-1,sys_swtc(a5)	 ; none

	move.l	sys_sbab(a0),d1 	 ; bottom of basic
	sub.l	sys_fsbb(a0),d1 	 ; less bottom of free
	sub.l	#$400,d1		 ; less gap
	moveq	#0,d2
	move.w	(sp)+,d2		 ; memory space * 65536
	swap	d2

	lsr.l	#6,d2			 ; ... * 1024
	move.l	(sp)+,d4		 ; Psion job ID
	sub.l	d2,d1			 ; spare space
	beq.s	dem_done		 ; ... no spare
	blt.s	dem_oops		 ; ... not enough space

	move.l	d1,d5			 ; keep it safe
	moveq	#sms.ampa,d0		 ; allocate some extra BASIC area
	trap	#do.sms2
	tst.l	d0
	bne.s	dem_oops

	move.l	d4,d1			 ; release my Psion job
	moveq	#sms.usjb,d0
	trap	#do.sms2
	tst.l	d0
	bne.s	dem_rebas		 ; oops, release BASIC

dem_wait
	clr.b	sys_dfrz(a5)		 ; unfreeze screen
	moveq	#sms.ssjb,d0		 ; suspend
	moveq	#-1,d1			 ; ... myself
	moveq	#20,d3			 ; ... for 20 ticks
	sub.l	a1,a1
	trap	#do.sms2
	tst.b	sys_dfrz(a5)		 ; screen frozen?
	bne.s	dem_wait

	moveq	#sms.injb,d0		 ; get job information
	move.l	d4,d1			 ; ... my grabbed job
	moveq	#0,d2			 ; top job
	trap	#do.sms2
	tst.l	d0			 ; still there?
	bne.s	dem_rebas
	tst.l	d3			 ; suspended?
	bge.s	dem_wait		 ; ... no

dem_rebas
	moveq	#sms.rmpa,d0		 ; release the space we took
	move.l	d5,d1			 ; space to release
	move.l	sys_sbab(a5),a6
	add.w	#jcb_end,a6
	trap	#do.sms2

dem_done
	moveq	#sms.spjb,d0		 ; set priority
	moveq	#0,d1			 ; ... of BASIC
	move.b	d7,d2			 ; to old setting
	trap	#do.sms2
	move.w	d6,sys_swtc(a5) 	 ; reset switch queue

hgb_suicide
	moveq	#-1,d1			 ; kill me
dem_kill
	moveq	#0,d3			 ; error code
	moveq	#sms.frjb,d0		 ; force remove
	trap	#do.sms2
	rts

dem_oops
	move.l	d4,d1			 ; remove Psion job
	bsr.s	dem_kill
	jsr	hk_rude 		 ; make a rude noise
	bra.s	dem_done
	end
