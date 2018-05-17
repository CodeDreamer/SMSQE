; Partial save / restore  V2.01 			    1998  Tony Tebby
;
; 2005-11-09  2.01  Screen increment based on CDB (MK)

	section driver

	xdef	pt_psave
	xdef	pt_prest

	xref	pt_hides
	xref	pt_mblock
	xref	pt_carea
	xref.s	pt.spxlw

	xref	gu_rchp

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_err'
	include 'dev8_keys_con'

;	Save part of a window in a save area: either one given
;	or a specially allocated one.
;
;	d1 c r	start point of block in save area -> pointer to save area
;	d2 c p	0 if save area given, size if to be set up
;	a0 c p	base address of channel block / pointer to heap
;	a1 c s	pointer to part defn
;	a2 c	pointer to save area
;	a4   s	saved base address of channel block

reg.entry reg	 d2-d7/a0-a5
reg.exit reg	 d1-d7/a0-a5
stk_d1	equ	$00
stk_d2	equ	$04
pt_psave
	movem.l reg.entry,-(sp)
	clr.l	-(sp)			; return nothing if error

;	First make sure the pointer is invisible

	moveq	#pt.supsr,d4		; suppress for save restore
	jsr	pt_hides(pc)		; and hide it

	move.l	a0,a4			; keep channel base safe
	tst.l	d2			; do we have a save area?
	beq.s	do_save 		; yup, use it

;      Make a save area as big as specified in D2

alcreg	reg	d1/d2/a1/a3
	movem.l alcreg,-(sp)
	move.l	d2,d1			; required save area width (pixels)
	swap	d1
	subq.w	#1,d1			round up to...
	moveq	#pt.spxlw,d0
	lsr.w	d0,d1			...width in long words +1
	addq.w	#2,d1
	move.w	d1,d6			save that
	mulu	d2,d1			space required in long words
	beq.s	pts_exbp		none, don't bother then
	asl.l	#2,d1			space in bytes
	moveq	#ptp.hdrl,d0
	add.l	d0,d1			; plus some for the header

get_save
	moveq	#sms.achp,d0
	moveq	#-1,d2			give the space to me
	trap	#1
	movem.l (sp)+,alcreg		; restore the important registers
	tst.l	d0
	bne.s	pts_exit		whoops

got_save
	move.l	a0,a2			; point to save area
	move.l	d2,ptp_xsiz(a2) 	; fill in size
	move.w	d6,d3
	add.w	d3,d3
	add.w	d3,d3			  ; row increment
	move.w	d3,ptp_rinc(a2)
	move.b	sd_wmode(a4),ptp_mode(a2) ; mode
	move.w	#ptp.flag,ptp_flag(a2)	  ; and flag

do_save
	move.l	a2,stk_d1(sp)		return base of save area
	bsr.s	pts_cprm		calculate save parameters
	bne.s	pts_errx		oops
	jsr	pt_mblock		move from screen to save

pts_ok
	moveq	#0,d0
pts_exit
	movem.l (sp)+,reg.exit
	rts
pts_exbp
	moveq	#err.ipar,d0
	bra.s	pts_exit
pts_errx
	tst.l	stk_d2(sp)		 ; new save area?
	beq.s	pts_exd0		 ; ... no
	move.l	stk_d1(sp),a0		 ; ... yes, release it
	jsr	gu_rchp
pts_exd0
	tst.l	d0
	bra.s	pts_exit

	page

;	Restore the contents of a save area into the screen memory, in the
;	location given.
;
;	d1 c	start point of restore area in save area
;	a0 c p	base address of channel block / pointer to heap
;	a1 c	definition of block to restore
;	a2 c s	pointer to save area
;	a4   s	saved base address of channel block

rstregs reg	d1-d7/a0-a5

pt_prest
	movem.l rstregs,-(sp)

;	first make sure the pointer is invisible

pwr_do
	moveq	#pt.supsr,d4		; suppress for save / restore
	jsr	pt_hides(pc)		; and hide it

	move.l	a0,a4			keep channel base safe

rmvreg	reg	d2/a2
	movem.l rmvreg,-(sp)
	bsr.s	pts_cprm		calculate save parameters
	bne.s	ptr_nomv		whoopsie
	exg	d2,d3
	exg	a2,a3			swap them to get restore parameters
	exg	a4,a5

	jsr	pt_mblock		move from save to screen
	moveq	#0,d0
ptr_nomv
	movem.l (sp)+,rmvreg
	bne.s	ptr_exit		boing
	tst.b	d2			is item to be removed from heap?
	bne.s	ptr_ok			... no
	move.l	a2,a0			set address of heap item
	moveq	#sms.rchp,d0		and return it to the common heap
	trap	#1
ptr_ok
	moveq	#0,d0
ptr_exit
	movem.l (sp)+,rstregs
	rts
	page

;	Calculate parameters for saving a window
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D1	origin in save area		size of window
;	D2					origin of window
;	D3					origin of window in save area
;	A0					base of bitmap in save area
;	A1	definition of block to move	preserved
;	A2	save area			row increment in screen
;	A3	^ linkage block 		row increment in save area
;	A4	^ cdb				base of screen
;	A5					base of save area

size	equ	$00
org	equ	$04
pts_cprm
	moveq	#err.ipar,d0		assume bad parameter
	cmp.w	#ptp.flag,ptp_flag(a2)	has the save area got the right flag?
	bne.s	ptc_exit		no, bother
	move.b	ptp_mode(a2),d2 	and the right mode?
	cmp.b	sd_wmode(a4),d2
	bne.s	ptc_exit		no

	move.l	ptp_xsiz(a2),d2 	allowed size...
	moveq	#0,d3			...and origin in save area
	move.l	d1,d7			requested origin...
	move.l	size(a1),d6		...and size of block
	jsr	pt_carea(pc)		OK?
	bne.s	ptc_exit		nope

	move.l	sd_xsize(a4),d2 	allowed size...
	moveq	#0,d3			...and origin in window
	movem.l (a1),d6/d7		requested size and origin
	tst.l	d6			is size negative?
	bmi.s	ptc_all 		yes, do all
	jsr	pt_carea(pc)
	bne.s	ptc_exit

	move.l	d1,d3			where to put...
	movem.l (a1),d1/d2		...area to get
	add.l	sd_xmin(a4),d2		true origin in screen
	bra.s	ptc_gadr		now do address registers

ptc_all
	move.l	d1,d3			destination origin
	movem.l sd_xmin(a4),d1/d2	source
	exg	d1,d2			correct way round

ptc_gadr
	lea	ptp_bits(a2),a5 	save area base
	move.w	sd_linel(a4),a3 	screen increment
	move.l	sd_scrb(a4),a4		screen base
	move.w	ptp_rinc(a2),a2 	save area row increment
	exg	a2,a3

	moveq	#0,d0
ptc_exit
	rts

	end
