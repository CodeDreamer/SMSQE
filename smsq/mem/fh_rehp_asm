* Return Memory to Heap  V2.01	  1986  Tony Tebby   QJUMP
*
* 2003-07-14  2.01  Doesn't break free space list if block was already free (MK)
*
	section mem
*
	xdef	mem_rehp		vectored return to heap
*
	include dev8_keys_hpm
*
*	d1 c  p length of memory returned
*	a0 cr	base of memory returned / ptr to start of free space inc this
*	a1 c sp initial free space pointer / running heap pointer
*	a2   sp saved running heap pointer
*
*
* link new space into arbitrary heap
*
reglist reg	a1/a2
*
mem_rehp
	movem.l reglist,-(sp)
	subq.l	#hpm_nxfr,a1		back space to pseudo free space
*
mrh_loop
	move.l	a1,a2			save pointer
	move.l	hpm_nxfr(a1),d0 	next free space
	beq.s	mrh_lkin		... no more, add to end
	add.l	d0,a1
	cmp.l	a1,a0			next beyond this one?
	blt.s	mrh_lkin		... yes, link in
	beq.s	mrh_exok		... space has already been freed!
	move.l	hpm_len(a1),d0		find end of this one
	add.l	a1,d0
	cmp.l	d0,a0			is new area contiguous?
	bne.s	mrh_loop		... no
*
	add.l	d1,hpm_len(a1)		extend previous area
	move.l	a1,a0			and call it new
	bra.s	mrh_ccont		check if contiguous with next
*
* link in a separate area
*
mrh_lkin
	move.l	d1,hpm_len(a0)		set length of this one
	tst.l	d0			is there a next?
	beq.s	mrh_slink		... no
	add.l	a2,d0			... yes
	sub.l	a0,d0			make relative
mrh_slink
	move.l	d0,hpm_nxfr(a0) 	set link to next
*
	move.l	a0,d0			set previous link pointer
	sub.l	a2,d0
	move.l	d0,hpm_nxfr(a2)
*
mrh_ccont
	move.l	hpm_nxfr(a0),d0 	next free space
	beq.s	mrh_exit		... none
	cmp.l	hpm_len(a0),d0		is it at end?
	bne.s	mrh_exok		... no
	move.l	hpm_nxfr(a0,d0.l),hpm_nxfr(a0) move next free pointer down
	beq.s	mrh_addl		... end
	add.l	d0,hpm_nxfr(a0) 	... not end, adjust
mrh_addl
	add.l	hpm_len(a0,d0.l),d0	extra length
	move.l	d0,hpm_len(a0)		set it
*
mrh_exok
	moveq	#0,d0
mrh_exit
	movem.l (sp)+,reglist
	rts
	end
