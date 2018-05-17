; Q40/Q60 Caches management  v. 1.00	copyright (c) Mark Swift
; 2003-10-17	1.01	adapted for SMSQ/E (FD)
; 2005-01-21	1.02	rationalised, reset sr as was on entry (wl)
	section exten

	xdef copyback
	xdef serialized
	xdef writethrough

	include 'dev8_sbsext_ext_keys'
	include 'dev8_keys_sys'
;

rprt_ni
	moveq	#err.ni,d0
	rts
	 
copyback
	lea	hw_copyback,a2
	bra.s	common
writethrough
	lea	hw_writethrough,a2
	bra.s	common
serialized
	lea	hw_serialized,a2
common
	cmp.l	a3,a5			; are htere parameters?
	bne.s	rprt_bp 		; yes : error

	moveq	#0,d0			; mt.inf
	trap	#1

	move.b	sys_ptyp(a0),d0 	; what type of processor is this?
	cmp.b	#$40,d0
	bcs.s	rprt_ni 		; not at least a '40

	move.w	sr,d0
	trap	#0			; supervisor mode
	ori.w	#$700,sr		; don't interrupt
	move.w	d0,-(sp)		; keep old status
	jsr	(a2)			; do routine

b_ttracux
	move.w	(a7)+,sr		; get old mode back
	moveq	#0,d0
	rts

rprt_bp
	moveq	#err.bp,d0
	rts



; routine to disable the instruction & data caches
; exit: d0 = previous cacr value

cachoff
	moveq	#0,d0

; routine to set the cacr
; entry:	d0 = value to write to cacr
; exit: d0 = previous cacr value

setcach
	move.l	d1,-(a7)

	moveq	#-1,d1
	bsr.s	docach

	move.l	(a7)+,d1
	rts

; routine to alter the state of the cacr
; callable from user or supervisor modes
; entry: d0 = bits to set
;	d1 = bits to clear/alter
; exit: d0 = previous cacr value

docach
	movem.l d2/a0/a6,-(a7)
	movea.l a7,a0
	trap	#0
	move.w	sr,-(a7)
	ori.w	#$0700,sr	; interrupts off

	subq.l	#2,a0
	cmpa.l	a0,a7
	beq.s	docachsv		; entered routine as supervisor

	bclr	#5,0(a7)		; otherwise sr on exit = user mode

docachsv
	move.l	a7,d2		; calculate start of
	andi.w	#-$8000,d2	; system variables
	move.l	d2,a6

	and.l	d1,d0
	not.l	d1

	cmpi.b	#$10,sys_ptyp(a6)
	bls.s	docachx 	; exit if 010 or less

	dc.w	$4e7a,$2002	; movec   cacr,d2
	and.l	d2,d1		; mask off changed bits
	or.l	d0,d1		; or in set bits

	move.l	d2,d0		; store old cacr value

	ori.w	#$0808,d1	; always clear caches on 020/030

	cmpi.b	#$30,sys_ptyp(a6)
	bls.s	docachset

	tst.w	d0		; check 040 bits
	bpl.s	docachdchk	; branch if instruction cache off
	dc.w	$f4b8		; cpusha	  ic
				; otherwise update memory from cache

docachdchk
	tst.l	d0		; check 040 bits
	bpl.s	docachdinv	; branch if data cache off
	dc.w	$f478		; cpusha	  dc
				; otherwise update memory from cache

	tst.l	d1		; check 040 bits
	bmi.s	docachiinv	; branch if leaving data cache on

docachdinv
	dc.w	$f458		; cinva   dc
				; invalidate cache

docachiinv
	dc.w	$f498		; cinva   ic
				; invalidate cache

docachset
	dc.w	$4e7b,$1002	; movec   d1,cacr
				; set the cache

docachx
	move.w	(a7)+,sr
	movem.l (a7)+,d2/a0/a6
	rts

; ttr/acu options : set cache options for memory map

hw_copyback
	movem.l d0-d2/d7,-(a7)
	move.l	#$007fc020,d1	; 0-32mb 0 = cachable, copyback (q40)
	bra.s	ttracu

hw_writethrough
	movem.l d0-d2/d7,-(a7)
	move.l	#$007fc000,d1	; 0-32mb = cachable, writethrough (*q40)
	bra.s	ttracu

hw_serialized
	movem.l d0-d2/d7,-(a7)
	move.l	#$007fc040,d1	; 0-32mb = non-cachable, serialized (q40)

ttracu
	bsr	cachoff 	; disable caches
	move.l	d0,d7		; save cacr value

	dc.w	$4e7b,$1006	; movec d1,dtt0
	dc.w	$4e7b,$1004	; movec d1,itt0

	move.l	#$fe01c040,d1	; base fe000000 = non-cachable, serialized (*q40)
	dc.w	$4e7b,$1007	; movec d1,dtt1
	dc.w	$4e7b,$1005	; movec d1,itt1

	move.l	d7,d0
	bsr	setcach

	movem.l (a7)+,d0-d2/d7
	rts

	end
