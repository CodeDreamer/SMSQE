; Set-up to drop object into screen	V2.01   1992 Tony Tebby
;
; 2003-02-24  2.01  Preserves sprite width in MSW of d5 (MK)
;
;	d1 c p	position of object
;	a0 c p	pointer to definition block
;	a1 c p	pointer to object
;	a3 c p	pointer to linkage block
;	a5 c s	pointer to action routine
;	a6   p
;	all others scratch

	section driver

	xdef	pt_drop

	xref	pt_hides
	xref	io_tstw
	xref	pt_swind
	xref	pt_sadd
	xref	sp_dropi

	include dev8_keys_qdos_pt
	include dev8_keys_con

pt_drop
	movem.l d1/a0/a1,-(sp)	      save the pointers
	movem.l a3/a6,-(sp)	      and another set
stk_a0	equ	$0c
	move.l	a2,-(sp)
	exg	a1,a5		set pointer to object
	bsr.l	pt_swind	set the drop pointers for the object
	move.l	(sp)+,a2	restore pattern pointer (if exists)
	bne.l	exit
	move.l	(sp),a0 	use temporary pointer to linkage block
	tst.b	pt_pstat(a0)	is pointer visible?
	bne.l	dr_direct	... no, just drop it

dr_insave
reg.save reg	d2/d3/d4/d5/d6/a1/a2/a3/a4/a5/a6
stk.save equ	11*4
	movem.l reg.save,-(sp)	save drop pointers

	move.l	pt_psprt(a0),a4 get the pointer to the pointer sprite

; first we set up the horizontal mask

	addq.l	#4,a4		ignore form
	move.w	(a4)+,d2	width of pointer save area
	subq.w	#1,d2
	lsr.w	#2,d2		in long words (-1)
	addq.w	#1,d2
	cmp.w	d2,d5		is object wider?
	bgt.l	dr_remsp	... yes, can't cope
	move.w	d2,d4		save width
	addq.w	#1,d4		(complete)
	lsl.w	#2,d4		in bytes

	add.w	d0,d2		add offset
	cmp.w	d2,d5		compare against width of object
	bge.s	width_p1
	move.w	d5,d2
width_p1
	addq.w	#1,d2		... plus 1

	moveq	#0,d3		set LHS aligned
	tst.w	d0		is object to left or right
	blt.s	to_right
	move.w	d0,d3		object is to left
	moveq	#0,d0		set no offset for save area
	bra.s	set_width
to_right
	asl.w	#2,d0		set the horizontal offset of the ptr save area
set_width
	sub.w	d3,d2		set the width of overlap
	ble.l	dr_in_screen	nothing to drop into save area

;; set the overlap mask
;
;	 move.l  d5,-(sp)	 save long word count
;	 moveq	 #-1,d5 	 preset mask
;	 clr.w	 d5
;	 ror.l	 d2,d5		 get width number of bits
;	 not.w	 d5
;	 ror.l	 d3,d5		 ... in the right place
;	 move.l  (sp)+,d5	 and reset count
;
;; set the non overlapped width of the save area
;
;	 move.w  d4,a6		 total width
;	 asl.w	 #2,d2
;	 sub.w	 d2,a6		 ... less overlap
;
;; now we find the vertical overlap
;
;	 move.w  (a4)+,d2	 height of pointer save area
;	 move.l  pt_spsav(a0),a3 set address of save area (as if screen!!)
;	 sub.w	 d0,a3		 ... less horizontal offset
;
;	 subq.w  #1,d2		 height of save area less one
;	 add.w	 d1,d2		 plus offset
;	 cmp.w	 d2,d6		 compare against height
;	 bge.s	 bottom_set
;	 move.w  d6,d2		 take the lesser
;
;bottom_set
;	 moveq	 #0,d3
;	 tst.w	 d1		 is object above or below?
;	 blt.s	 below
;	 ext.l	 d1		 d1 needs to be long
;	 move.w  d1,d3		 object above
;	 move.w  d5,d0		 number of long words/row
;	 mulu	 d3,d0		 * number of rows
;	 lsl.l	 #2,d0		 * bytes / long
;	 bra.s	 set_height
;below
;	 muls	 d1,d4		 set offset of save area
;	 sub.w	 d4,a3		 and offset it!!
;	 moveq	 #0,d0
;	 moveq	 #0,d1		 no offsets
;
;set_height
;	 sub.w	 d3,d2		 and the height (-1)
;	 blt.s	 dr_in_screen	 ... nothing to drop into save area
;
;	 move.w  d2,d6		 set height drop in
;
;; update save area!!!
;
;	 movem.l (sp),d2/d3	 restore the pattern offsets
;	 jsr	 (a1)
;
; update screen
;
;	 movem.l (sp)+,reg.save  restore drop pointers
;	 moveq	 #0,d0		 no offsets
;	 moveq	 #0,d1
;	 jsr	 (a1)
;	 movem.l (sp),a3/a6
;	 bsr.l	 pt_sadd	 set the pointer addresses
;	 bsr.l	 sp_dropi	 and drop the pointer back in
;	 bra.s	 exit_ok
;
; remove sprite

dr_remsp
	movem.l stk.save(sp),a3/a6
	move.l	stk.save+stk_a0(sp),a0
	move.l	pt_psprt(a3),a4 	point at sprite
	move.l	pt_pos(a3),d2		this is where its origin is
	sub.l	pto_org(a4),d2		so this is its screen origin
	moveq	#$fffffff0,d0
	swap	d0
	and.l	d0,d2			origin of save area
	move.l	#$001f0000,d1		round up width (and add one)
	add.l	d2,d1			TT special !!!
	and.l	d0,d1
	add.l	pto_size(a4),d1 	and how far across it goes
	bsr.l	io_tstw 		is it in our window?
	ble.s	dr_in_screen		... no

	moveq	#pt.show,d4
	jsr	pt_hides

dr_in_screen
	movem.l (sp)+,reg.save	restore drop pointers
dr_direct
	moveq	#0,d0		no offsets
	moveq	#0,d1
	jsr	(a1)
exit_ok
	moveq	#0,d0
exit
	movem.l  (sp)+,a3/a6
	movem.l  (sp)+,d1/a0/a1
	rts
	end
