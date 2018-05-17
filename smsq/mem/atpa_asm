; Allocate in transient program area  V2.01    1986  Tony Tebby   QJUMP

	section mem

	xdef	mem_atpa	       ; allocate in tpa

	xref	mem_ruch	       ; round up common heap allocation
	xref	mem_altd	       ; allocate from top down
	xref	mem_xtpa	       ; extend the TPA
	xref	mem_rehp	       ; add to TPA

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_chp'
	include 'dev8_keys_sbt'
;+++
; Allocate in TPA
;
;	d0  r	error return 0 or insufficient memory
;	d1 cr	memory required / allocated
;	a0  r	base of area allocated
;	a6 c  p system variables area base
;	status return standard
;---
mem_atpa
mat.reg reg	d1/a1
stk_d1	equ	$00
	bsr.l	mem_ruch		 ; round d1 up
	movem.l mat.reg,-(sp)		 ; save volatiles
	lea	sys_tpaf(a6),a0 	 ; check for room within tpa area
	bsr.l	mem_altd		 ; allocate from top down
	beq.s	mat_exit		 ; ... enough space found

	lea	sys_tpaf-chp_nxfr(a6),a0 ; pointer to first free space
	move.l	chp_nxfr(a0),d0
	beq.s	mat_grab
	add.l	d0,a0
	cmp.l	sys_tpab(a6),a0 	 ; at start of tpa?
	bne.s	mat_grab		 ; ... no, grab d1's worth of slave blocks
	sub.l	chp_len(a0),d1		 ; ... yes, we do not need quite so much
mat_grab
	bsr.s	mem_xtpa		 ; extend the tpa
	bne.s	mat_exit		 ; ... oops

	moveq	#chp.free,d0
	move.l	d0,chp_ownr(a0) 	 ; mark new bit as free
	lea	sys_tpaf(a6),a1 	 ; add new bit to heap
	bsr.l	mem_rehp
	move.l	stk_d1(sp),d1		 ; restore length required
	move.l	a1,a0			 ; and
	bsr.l	mem_altd		 ; allocate from bottom up
mat_exit
	movem.l (sp)+,mat.reg
	rts
	end
