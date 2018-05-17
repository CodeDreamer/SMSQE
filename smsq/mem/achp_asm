; Allocate in common heap  V2.00    1986  Tony Tebby	QJUMP

	section mem

	xdef	mem_achp

	xref	mem_ruch		 ; round up common heap allocation
	xref	mem_rusb		 ; round up slave block allocation
	xref	mem_albu		 ; allocate from bottom up
	xref	mem_rehp		 ; 'return' memory to heap
	xref	mem_grsb		 ; grab system slave blocks (a0 by d1)

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_chp'
	include 'dev8_keys_sbt'
;+++
; Allocate in common heap
;
;	d0  r	error return 0 or insufficient memory
;	d1 cr	memory required / allocated
;	a0  r	base of area allocated
;	a6 c  p system variables area base
;
;	status return standard
;+++
mem_achp
mac.reg reg	d1/a1
stk_d1	equ	$00
	bsr.l	mem_ruch		 ; round d1 up before ....
	movem.l mac.reg,-(sp)		 ; ... saving volatiles
	ble.s	mac_imem
	lea	sys_chpf(a6),a0 	 ; check for room within heap area
	tst.l	(a0)			 ; any?
	beq.s	mac_grab
	bsr.s	mem_albu		 ; ... allocate from bottom up
	beq.s	mac_done		 ; enough space found

; not enough space found, make some more space

; a0 = a1 = pointer to last free space

	add.l	chp_len(a0),a0		 ; end of last free space
	cmp.l	sys_fsbb(a6),a0 	 ; at start of slave block area?
	bne.s	mac_grab		 ; ... no, grab d1's worth of slave blocks
	sub.l	chp_len(a1),d1		 ; ... yes, we do not need quite so much
mac_grab
	move.l	sys_fsbb(a6),a0 	 ; grab starting at base of slave blocks
	bsr.l	mem_rusb		 ; ... rounded up to slave block size
	move.l	#sbt.mins,d0		 ; minimum spare
	move.l	a0,a1
	add.l	d1,a1			 ; new bottom of slave blocks
	add.l	a1,d0			 ; + spare
	cmp.l	sys_sbab(a6),d0 	 ; enough spare?
	bgt.s	mac_imem		 ; ... no
	move.l	a1,sys_fsbb(a6) 	 ; ... yes, set new base of slave blocks
	bsr.l	mem_grsb		 ; grab it

	lea	sys_chpf(a6),a1 	 ; add new bit to heap
	bsr.l	mem_rehp
	move.l	stk_d1(sp),d1
	move.l	a1,a0			 ; and
	bsr.s	mem_albu		 ; allocate from bottom up
	bne.s	mac_imem		 ; ... not enough space found!!!

mac_done
	cmp.l	a0,a1			 ; does a1 point to new free space?
	ble.s	mac_clear		 ; ... no
	moveq	#chp.free,d0
	move.l	d0,chp_ownr(a1) 	 ; ... possibly, set owner

mac_clear
	lea	(a0,d1.l),a1		 ; clear out entire area

mac_cloop
	clr.l	-(a1)
	clr.l	-(a1)
	clr.l	-(a1)
	clr.l	-(a1)
	cmp.l	a0,a1
	bgt.s	mac_cloop

	move.l	d1,chp_len(a0)		 ; set length
	moveq	#0,d0
mac_exit
	movem.l (sp)+,mac.reg
	rts

mac_imem
	moveq	#err.imem,d0		; out of memory
	bra.s	mac_exit
	end
