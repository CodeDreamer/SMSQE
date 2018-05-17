; Return space to transient program area  V2.02    1994  Tony Tebby

	section mem

	xdef	mem_rtpa	       ; return to tpa

	xref	mem_rusb	       ; round up in slave block allocation units
	xref	mem_rehp	       ; 'return' memory to heap
	xref	mem_ctpas

	include 'dev8_keys_sys'
	include 'dev8_keys_chp'
	include 'dev8_keys_sbt'

;+++
; Return memory to TPA
;
;	d0  r	error return 0
;	a0 c  p base of area to be returned
;	a6 c  p system variables area base
;
;	all other registers preserved
;---
mem_rtpa
mrt.reg reg	d1/a0/a1
	movem.l mrt.reg,-(sp)		 ; save volatiles
	moveq	#chp.free,d0		 ; mark as free space
	move.l	d0,chp_ownr(a0)

	lea	sys_tpaf(a6),a1 	 ; release to tpa
	move.l	chp_len(a0),d1		 ; get length
	bsr.l	mem_rehp		 ; ... and release

	cmp.l	sys_tpab(a6),a0 	 ; transient program area base?
	bne.s	mrt_exit		 ; ... no
	move.l	#-sbt.size,d1
	move.l	chp_len(a0),d0		 ; length of free area
	and.l	d0,d1			 ; amount of slave block area freed
	beq.s	mrt_exok		 ; ... none
	sub.l	d1,d0			 ; all gone?
	beq.s	mrt_reml		 ; ... yes
	add.l	d1,sys_tpaf(a6) 	 ; ... no, move link pointer
	move.l	d0,chp_len(a0,d1.l)	 ; set new length
	move.l	chp_nxfr(a0),d0 	 ; and link
	beq.s	mrt_link
	sub.l	d1,d0			 ; relative
mrt_link
	move.l	d0,chp_nxfr(a0,d1.l)	 ; set link to next
	moveq	#chp.free,d0
	move.l	d0,chp_ownr(a0,d1.l)	 ; and free
	bra.s	mem_ctpas

mrt_reml
	move.l	chp_nxfr(a0),d0 	 ; remove this link
	beq.s	mrt_sfsp		 ; ... no more
	add.l	sys_tpaf(a6),d0 	 ; ... offset old link
mrt_sfsp
	move.l	d0,sys_tpaf(a6) 	 ; set free space link
	bra.s	mem_ctpas

mrt_exok
	moveq	#0,d0
mrt_exit
	movem.l (sp)+,mrt.reg
	rts
	end
