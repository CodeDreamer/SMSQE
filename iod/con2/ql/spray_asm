; Spray pixels in blob			V2.00   1992  Tony Tebby
;
;	Registers:
;		Entry				Exit
;	D1	x,y position
;	D2	number of pixels to spray
;	A1	pointer to blob 		smashed
;	A2	pointer to pattern
;
	section driver
;
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_con'
;
	xref	pt_chkbp
	xref	pt_doblb
;
	xdef	pt_spray
;
spryreg reg	d1-d3/a1-a3
stk_a1	equ	$0c
stk_a2	equ	$10
stk_a3	equ	$14
;
pts_exit
	movem.l (sp)+,spryreg		; only come here on error
	move.l	(sp)+,a0
	rts
;
pt_spray
	move.l	a0,-(sp)
	movem.l spryreg,-(sp)
	jsr	pt_chkbp(pc)		; check blob and pattern
	bne.s	pts_exit		; not OK, do nothing
	move.l	a2,stk_a2(sp)		; keep (reset?) pattern
;
;	Find out which mode we're using
;
	moveq	#0,d6			; assume mode 4
	tst.b	sd_wmode(a0)		; is it?
	beq.s	pts_fsiz		; yes
	moveq	#m8tab-m4tab,d6 	; no, get offset to mode 8 mask table
;
;	Ensure there's enough space for a reduced version of the
;	blob in the spray buffer: otherwise allocate a new, larger one.
;
pts_fsiz
	move.w	pto_xsiz(a1),d0 	; get X size of blob...
	add.w	#$f,d0			; ...and round it up...
	and.w	#$fff0,d0		; ...to nearest 16 pixels (1 long word)
	lsr.w	#2,d0			; thus width in bytes
	move.w	d0,d5			; keep this safe
	mulu	pto_ysiz(a1),d0 	; thus total size...
	moveq	#pto.hdrl,d4		; ...plus header...
	add.l	d0,d4			; ...makes space required
;
	move.l	pt_spbuf(a3),d0 	; do we have a spray buffer?
	move.l	d0,a0
	beq.s	pts_achp		; no, allocate one
	cmp.l	pt_spbsz(a3),d4 	; yes, is it big enough?
	ble.s	pts_mkbl		; yes, make the blob
	clr.l	pt_spbuf(a3)		; no, we're going to throw it away
	moveq	#sms.rchp,d0
	trap	#1			; return old buffer
pts_achp
	move.l	d4,d1			; get size required
	moveq	#0,d2			; it belongs to the system
	moveq	#sms.achp,d0
	trap	#1			; allocate space for buffer
	tst.l	d0
	bne.s	pts_exit		; ...oops
	subq.l	#8,d1			; space allocated...
	subq.l	#8,d1			; ...is this much
	move.l	stk_a3(sp),a3		; restore pointer to dddb
	move.l	a0,pt_spbuf(a3) 	; fill in address
	move.l	d1,pt_spbsz(a3) 	; and size
	move.l	d1,d4
	movem.l (sp),spryreg
;
;	Copy the actual blob's header into the spray buffer
;
pts_mkbl
	move.l	a0,stk_a1(sp)		; change blob to point to buffer
	move.l	a1,a4			; smashable copy of blob
	move.l	(a4)+,(a0)+		; copy form and adaption
	move.l	(a4)+,(a0)+		; and size
	move.l	(a4)+,(a0)+		; and repeat
	clr.l	(a0)+			; shouldn't have a pattern anyway
	move.l	#8,(a0)+		; mask is after header in buffer
	clr.l	(a0)+			; and there's no next
;
	moveq	#pto.hdrl,d0		; rest is...
	sub.l	d0,d4			; ...this long...
	lsr.l	#2,d4			; ...in long words
	move.l	a0,a4			; it's here
	moveq	#0,d0
	bra.s	pts_clre		; so 
pts_clrl
	move.l	d0,(a4)+		; clear it
pts_clre
	dbra	d4,pts_clrl
;
	move.w	pt_randi(a3),d4 	; get current random number
	move.w	d4,d7			; keep a copy
	move.l	pto_mask(a1),a4 	; point to...
	lea	pto_mask(a1,a4.l),a4	; ...blob's mask
	move.l	pto_size(a1),d3 	; size of blob
;
;	We now have
;
;	D2	number of pixels to set
;	D3	x,y size of blob
;	D4	random number
;	D5	mask line length in bytes
;	D6	offset of pixel table from M4TAB
;	D7	original random number
;	A0	pointer to mask in buffer
;	A4	pointer to mask in blob
;
	bra.s	pts_rnde
pts_rndl
	move.l	a4,a2			; point to blob mask
	move.l	a0,a3			; and buffer mask
	mulu	#pt.randm,d4		; make a new random number
	addq.w	#pt.randa,d4
	move.w	d4,d0			; pretend it's 0 to .9999
	mulu	d3,d0			; and make 0 to...
	swap	d0			; ...n-1
	swap	d3			; get x for later
	mulu	d5,d0			; giving offset in mask
	add.l	d0,a2			; point to line in mask
	add.l	d0,a3			; and in buffer
;
	mulu	#pt.randm,d4		; now another random number 
	addq.w	#pt.randa,d4
	move.w	d4,d0			; same trick for X
	mulu	d3,d0			; to get
	swap	d0			; 0..n-1
	swap	d3			; get Y size for later
	move.w	d0,d1			; word offset is...
	and.w	#$fff8,d1		; ...all but three LSBs
	sub.w	d1,d0			; make bit offset
	add.w	d0,d0			; offset in table
	add.w	d6,d0			; this is the table to use
	move.w	m4tab(pc,d0.w),d0	; get appropriate mask
;
	lsr.w	#2,d1			; offset in this line of blob...
	and.w	0(a2,d1.w),d0		; ...gives pixel to spray
	beq.s	pts_chkr		; no pixel, just check random number
	add.w	d1,a3			; where to set pixel
	move.w	d0,d1
	and.w	(a3),d1 		; is pixel already set?
	bne.s	pts_chkr		; yes, can't count it then
	or.w	d0,(a3) 		; add pixel to any in buffer
	subq.w	#1,d2			; it's a new one
pts_chkr
	cmp.w	d4,d7			; back to the first random number?
	beq.s	pts_fend		; yes, give up
pts_rnde	
	tst.w	d2			; more to do?
	bne.s	pts_rndl		; yes
pts_fend
;
	movem.l (sp)+,spryreg
	move.l	(sp)+,a0
	move.w	d4,pt_randi(a3) 	; keep new random number
	jmp	pt_doblb(pc)		; and do 'blob'
;
m4tab
	dc.w	$8080,$4040,$2020,$1010
	dc.w	$0808,$0404,$0202,$0101
m8tab
	dc.w	$c0c0,$c0c0,$3030,$3030
	dc.w	$0c0c,$0c0c,$0303,$0303
;
	end
