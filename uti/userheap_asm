; User heap management			1995 Jochen Merz    V0.00
;					based on a model from J. Oakley

	section utility

	include dev8_keys_hpm
	include dev8_keys_qdos_sms
	include dev8_keys_qlv
	include dev8_mac_xref

	xdef	ut_aluhp	; allocate entry in user heap
	xdef	ut_reuhp	; release entry in user heap
	xdef	ut_rcuhp	; release complete user heap - return memory

;+++
; Allocate space in user heap: if this is not possible then a common heap
; chunk is allocated and linked in to the user heap. The space is not cleared!!
; The longword after the user heap pointer should be initally zero.
; It is used for maintaining the chunk list.
;
;		Entry				Exit
;	D1	space required
;	A0	user heap pointer		usable space
;
;	Error return in D0: err.imem
;---
ut_aluhp
uhpreg	reg	a1-a4/d1-d4
	movem.l uhpreg,-(sp)
	addq.l	#4,d1			; allow for length
	move.l	d1,d4
	move.l	a0,a4			; save parameters
;
qca_auhp
	move.l	d4,d1
	move.w	mem.alhp,a2
	jsr	(a2)			; allocate in user heap
	tst.l	d0
	beq.s	qca_exit		; OK

	move.l	#1024-8,d0		; not OK, so get another 1k
	cmp.l	d4,d0			; will it be enough?
	bge.s	qca_enuf		; yes

	move.l	d4,d0			; no, get just enough

qca_enuf
	addq.l	#8,d0			; allow for user heap header
	addq.l	#4,d0			; .. and chunk link
	move.l	d0,d1			; keep it
	xjsr	gu_achp0		; get some real heap
	bne.s	qca_exit		; ..oops

	move.l	a4,a1
	move.l	4(a4),d0
	move.l	a0,4(a4)
	move.l	d0,(a0)
	addq.l	#4,a0
	move.w	mem.rehp,a2		; and "return" it to user heap
	jsr	(a2)
	move.l	a4,a0
	bra.s	qca_auhp		; now we can get some user heap

qca_exit
	addq.l	#4,a0			; point to just after length
	movem.l (sp)+,uhpreg
	rts
;+++
; Return space to a user heap: it is assumed that the length longword
; is intact.
;
;		Entry				Exit
;	A0	entry to return 		preserved
;	A1	user heap pointer		preserved
;---
ut_reuhp
rhpreg	reg	d0-d3/a0-a3
	movem.l rhpreg,-(sp)
	move.l	-(a0),d1		; get length/correct pointer
	move.w	mem.rehp,a2		; return user heap
	jsr	(a2)
	movem.l (sp)+,rhpreg
	rts

;+++
; Return complete user heap to common heap.
;
;		Entry				Exit
;	A1	user heap pointer
;---
ut_rcuhp
	movem.l a0-a1,-(sp)
	move.l	4(a1),a1		; chunk link list starts here

rc_loop
	move.l	a1,d0			; last link ??
	beq.s	rc_done 		; done!

	move.l	d0,a0			; return this bit
	move.l	(a0),a1 		; but get link before you do this!
	xjsr	gu_rchp
	clr.l	(a0)+			; clear this entry in chunk list
	clr.l	(a0)
	bra.s	rc_loop 		; ...and loop

rc_done
	movem.l (sp)+,a0-a1
	clr.l	(a1)+			; clear start of chunk list
	clr.l	(a1)
	rts


	end
