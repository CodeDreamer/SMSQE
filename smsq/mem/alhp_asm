; Allocate in Heap  V2.01    1986  Tony Tebby	 QJUMP

	section mem

	xdef	mem_alhp		 ; vectored allocate in heap

	xdef	mem_albu		 ; allocate from the bottom up
	xdef	mem_altd		 ; allocate from the top down
	xdef	mem_ruch		 ; round up allocation (common heap)
	xdef	mem_rusb		 ; round up allocation (slave block)
	xdef	mem_rup 		 ; round up (using d0)
	xdef	mem_rdch		 ; round down address (a0) (common heap)

	include 'dev8_keys_err'
	include 'dev8_keys_hpm'
	include 'dev8_keys_sbt'
;+++
; allocate in arbitrary heap
;
;	d0	allocation rounding
;	d1 cr	space required / space allocated
;	a0 cr	initial free space pointer / base of space allocated
;	a1   sp running heap pointer
;	a1  r	pointer to next (new?) free space (albu)
;	a1   s	pointer to previous link pointer (altd)
;	a2   sp saved pointer to large enough free space (altd)
;	a3   sp saved pointer to a2 (altd)
;---
mem_alhp
	moveq	#hpm.almn-1,d0		 ; minimum heap allocation unit
	bsr.l	mem_rup 		 ; ... round up
	move.l	a1,-(sp)		 ; save a1
	bsr.s	mem_albu		 ; allocate from bottom up
	move.l	(sp)+,a1		 ; restore a1
	rts

; allocate memory from the bottom up

mem_albu
	subq.l	#hpm_nxfr,a0		 ; back space to pseudo free space

mab_loop
	move.l	a0,a1			 ; keep previous free space pointer
	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	mem_om			 ; ... out of memory
	add.l	d0,a0
	cmp.l	hpm_len(a0),d1		 ; large enough?
	bhi.s	mab_loop		 ; ... no
	beq.s	mab_fit 		 ; ... exact

	add.l	d1,hpm_nxfr(a1) 	 ; point to new free space from previous
	lea	(a0,d1.l),a1		 ; absolute
	move.l	hpm_len(a0),hpm_len(a1)
	sub.l	d1,hpm_len(a1)		 ; new length

mab_fit
	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	mab_sfree		 ; ... none
	add.l	a0,d0			 ; absolute
	sub.l	a1,d0			 ; make relative
mab_sfree
	move.l	d0,hpm_nxfr(a1)
	bra.s	mem_slen

; allocate memory from the top down

mem_altd
	subq.l	#hpm_nxfr,a0		 ; back space to pseudo free space
	movem.l a2/a3,-(sp)		 ; save a2,a3
	sub.l	a2,a2			 ; no space found

mat_loop
	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	mat_done		 ; ... all done
	move.l	a0,a1			 ; keep previous free space pointer
	add.l	d0,a0
	cmp.l	hpm_len(a0),d1		 ; large enough?
	bhi.s	mat_loop		 ; ... no
	move.l	a0,a2			 ; ... yes
	move.l	a1,a3
	bra.s	mat_loop

mat_done
	move.l	a2,a0			 ; set return address
	move.l	a3,a1			 ; and previous free
	movem.l (sp)+,a2/a3		 ; restore a2 and a3
	move.l	a0,d0			 ; space found?
	beq.s	mem_om			 ; ... no
	move.l	hpm_len(a0),d0		 ; exact?
	sub.l	d1,d0
	bgt.s	mat_sfree		 ; ... no

	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	mat_snext		 ; ... none
	add.l	a0,d0			 ; absolute
	sub.l	a1,d0			 ; make relative
mat_snext
	move.l	d0,hpm_nxfr(a1)
	bra.s	mem_slen

mat_sfree
	move.l	d0,hpm_len(a0)		 ; new length of free space
	add.l	d0,a0			 ; new start of space

; allocation exits

mem_slen
	move.l	d1,hpm_len(a0)		 ; set length in heap
	moveq	#0,d0
	rts
mem_om
	moveq	#err.imem,d0		 ; insufficient memory
	rts
	page

; round up memory allocation in d1

mem_rusb
	move.l	#sbt.size-1,d0		 ; slave block allocation unit - 1
	bra.s	mem_rup
mem_ruch
	moveq	#hpm.achp-1,d0		 ; common heap allocation unit - 1
mem_rup
	add.l	d0,d1
	not.l	d0
	and.l	d0,d1
	rts

; round down memory address in a0

mem_rdch
	move.l	a0,d0
	and.w	#-hpm.achp,d0		 ; common heap allocation unit
	move.l	d0,a0
	rts
	end
