; Extend transient program area  V2.01	  1986  Tony Tebby   QJUMP

	section mem

	xdef	mem_xtpa	       ; extend tpa

	xref	mem_rusb	       ; round up slave block allocation
	xref	mem_grsb	       ; grab system slave blocks (a0 by d1)
	xref	sb_mdown

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_chp'
	include 'dev8_keys_sbt'
;+++
; Make some more space in tpa
;
;	d0  r	error return 0 or insufficient memory
;	d1 c  p memory required / allocated
;	a0  r	base of tpa allocated
;	a6 c  p system variables area base
;
;	status return standard
;---
mem_xtpa
	move.l	sys_sbab(a6),a0 	 ; grab starting at top of slave blocks
	bsr.l	mem_rusb		 ; ... rounded up to slave block size
	sub.l	d1,a0			 ; new top of slave blocks
	move.l	#-sbt.mins,d0		 ; minimum spare
	add.l	a0,d0			 ; from bottom
	cmp.l	sys_fsbb(a6),d0 	 ; enough spare?
	blt.s	mxt_imem		 ; ... no
	move.l	a0,sys_sbab(a6) 	 ; ... yes, set new top of slave blocks
	sub.l	d1,sys_tpab(a6)
	bsr.l	mem_grsb		 ; grab slave blocks

	move.l	sys_tpab(a6),a0
	jmp	sb_mdown		 ; ... down

; insufficient memory

mxt_imem
	moveq	#err.imem,d0
mxt_rts
	rts
	end
