* Heap handling       V0.13   1985  Tony Tebby   QJUMP
*
*	ALCHP (nbytes)			allocate common heap
*	RECHP base_address		return to common heap
*	CLCHP				clear common heap
*	FREE_MEM			get free memory
*
* 2002-05-09  0.13  FREE_MEM now uses sms.frtp (MK)
*
	section exten
*
	xdef	alchp
	xdef	rechp
	xdef	clchp
	xdef	free_mem

	xdef	ext_flint
*
	xref	ut_gxli1
	xref	ut_rtfd1
	xref	err_bp
*
	include dev8_sbsext_ext_keys
	include dev8_keys_qdos_sms
*
* free memory
*
free_mem
	cmp.l	a3,a5		any parameters?
	bne.l	err_bp		... yes
*
	moveq	#sms.frtp,d0
	trap	#1
mem_rlint
	bra.l	ut_rtfd1
*
* allocate common heap
*
alchp
	bsr.s	mem_flint	get one long integer argument
	bne.s	mem_rts 	... oops
	addq.l	#4,d1		we need 4 bytes extra for linked list
	moveq	#mt.alchp,d0	allocate space
	moveq	#myself,d2	owned by me!!
	trap	#1
	tst.l	d0		ok?
	beq.s	mem_link	... yes, link into list
	moveq	#0,d1		... no, return zero
	bra.s	mem_rlint
mem_link
	bsr.s	mem_fptr	fetch link pointer
	jsr	ut..link*3+qlv.off  and link new heap in
*
	move.l	a0,d1		return the base address
	addq.l	#4,d1		plus the space for the link pointer
	bra.s	mem_rlint
*
rechp
	bsr.s	mem_flint	fetch the base address
	bne.s	mem_rts 	... oops
	subq.l	#4,d1		move back 4 to link pointer
	move.l	d1,a0
	bsr.s	mem_fptr	fetch link pointer
rechp_loop
	move.l	(a1),d1 	get next link pointer
	beq.l	err_bp		... end
	cmp.l	a0,d1		does this point to our new one?
	beq.s	rechp_unlk	... yes, good
	move.l	d1,a1		move on
	bra.s	rechp_loop
rechp_unlk
	move.l	(a0),(a1)	transfer link back
	moveq	#mt.rechp,d0	then release it to qdos
	trap	#1
rechp_rts
	rts
*
* fetch one long integer
*
ext_flint
mem_flint
	bsr.l	ut_gxli1	get one long integer
	bne.s	mem_rts
	move.l	(a6,a1.l),d1	get the value
	addq.l	#4,bv_rip(a6)	and tidy stack
	moveq	#0,d0		set status Z
mem_rts
	rts
*
* fetch link pointer
*
mem_fptr
	lea	bv_cheap(a6),a1 address of pointer
	rts
*
* clear common heap
*
clchp
	bsr.s	mem_fptr	fetch link pointer
	move.l	(a1),a4 	and first pointer
	clr.l	(a1)		clear it
clchp_loop
	move.l	a4,d0		is pointer zero?
	beq.s	mem_rts 	... yes, done
	move.l	a4,a0		... no,
	moveq	#mt.rechp,d0	... release the space
	trap	#1
	move.l	(a4),a4 	and get next pointer
	bra.s	clchp_loop
	end
