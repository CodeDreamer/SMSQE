; Initialise memory map and tables  V2.01    1994  Tony Tebby
; FAST RAM in high memory

	section init

	xdef	mem_init

	include 'dev8_keys_sys'
	include 'dev8_keys_sbt'
	include 'dev8_keys_chp'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_ini_keys'
;+++
;	d6    p
;	d7 c	size of memory (slow memory)
;	a6 c  p base of system vars
;---
mem_init
	moveq	#chp.len,d0
	sub.l	d0,d7			 ; allow room for occupied header

; the following is identical to the standard mem_init

	move.l	a6,a0
	and.w	#$fe00,d7		 ; multiple of 512 bytes
	move.l	d7,a5			 ; top of memory (contiguous only)
	move.l	d7,d1
	sub.l	a6,d1
	lsr.l	#sbt.shft,d1		 ; slave block table size
	move.l	d1,d2
	lsr.l	#5,d2			 ; job table entries
	addq.l	#$8,d2			 ; ... plus a bit
	moveq	#120,d0
	cmp.l	d0,d2			 ; too big?
	bls.s	mi_jbsiz		 ; ... no
	move.l	d0,d2
mi_jbsiz
	lsl.l	#2,d2			 ; job table size
	moveq	#3,d3
	mulu	d2,d3			 ; channel table size

	lea	ini_sstk,a1	   ; top of supervisor stack / base of sbt
	lea	(a1,d1.l),a2	   ; top of sbt / base of jobs
	lea	(a2,d2.l),a3	   ; top of jobs / base of channels
	lea	(a3,d3.l),a4	   ; top of sysvar+stack+jobs+channels+sbt
	move.l	a4,d4		   ; base of heap
	lsr.l	#1,d4
	subq.l	#1,d4
	st	d4
	addq.l	#1,d4
	add.l	d4,d4		   ; rounded up

; basic allocations set

	addq.l	#4,a0
	move.l	d4,(a0)+		 ; base of heap
	clr.l	(a0)+
	move.l	d4,(a0)+		 ; base of fsb
	move.l	a5,(a0)+		 ; base of QL SuperBASIC
	move.l	a5,(a0)+		 ; base of tpa
	clr.l	(a0)+
	move.l	a5,(a0)+		 ; base of rpa
	move.l	a5,(a0)+		 ; ram top
	move.l	a5,(a0)+		 ; default max free

; slave block tables

	lea	sys_sbrp(a6),a0 	 ; slave block running pointer
	lsr.l	#1,d1			 ; half slave block table size
	and.w	#$fff0,d1		 ; nice offset
	move.l	a1,(a0) 		 ; pointer at start of tables
	add.l	d1,(a0)+		 ; no, half way
	move.l	a1,(a0)+
	move.l	a2,(a0)+

; job tables

	clr.l	(a0)+			 ; no jobs
	moveq	#-1,d0
	move.l	d0,(a0)+		no current job
	move.l	a2,(a0)+		base
	move.l	a3,(a0)+

; channel tables

	clr.l	(a0)+			no channels
	move.l	a3,(a0)+		current channel does not exist
	move.l	a3,(a0)+
	move.l	a4,(a0)


; clear slave block tables

	move.l	d4,d0			occupied area
	sub.l	a6,d0
	lsr.l	#sbt.shft,d0
	lea	(a1,d0.l),a0		top of occupied bit of table

mi_sbocc
	clr.b	(a1)			unavailable
	addq.l	#sbt.len,a1
	cmp.l	a0,a1			top of unavailable area?
	blt.s	mi_sbocc		... no

mi_sbfree
	move.b	#sbt.mpty,(a1)		empty
	addq.l	#sbt.len,a1
	cmp.l	a2,a1			top of sbt?
	blt.s	mi_sbfree


; clear job / channel tables

mi_cljch
	st	(a1)			set msbyte
	addq.l	#4,a1
	cmp.l	a4,a1			at top?
	blt.s	mi_cljch		... no


; *************************************
;
; Now the special tricks for HIGH memory
;
	move.l	sms.framt,a2
	lea	ini_sstk,a1		 ; copy the stack
mi_cpys
	move.w	-(a1),-(a2)
	cmp.l	sp,a1
	bhi.s	mi_cpys

	move.l	a2,sp			 ; set new stack

	sub.w	#$200,a2		 ; stack allowance
	move.l	a2,d2
	and.w	#$fff0,d2		 ; make top a multiple of 16 bytes
	move.l	d2,sys_ramt(a6) 	 ; new ramtop
	move.l	d2,sys_rpab(a6) 	 ; and resident procedure base

	move.l	d7,a0			 ; top of slow ram
	move.l	sms.framb,a1		 ; fast ram base

	move.l	a1,d1
	sub.l	a0,d1			 ; the unavailable bit
	move.l	d1,(a0)+		 ; set length
	clr.l	(a0)+			 ; ... no linkage
	move.l	#$12345678,(a0)+	 ; rubbish job

	moveq	#-sys_tpab,d1
	add.l	a1,d1
	sub.l	a6,d1
	move.l	d1,sys_tpaf(a6) 	 ; tpa free space pointer

	sub.l	a1,d2			 ; length of TPA free space
	move.l	d2,(a1)+
	clr.l	(a1)+			 ; no next
	moveq	#-1,d0
	move.l	d0,(a1)+		 ; free

	move.l	sys_chpb(a6),a1 	 ; base of heap
	move.l	#$20200,d1		 ; size of buffer (+underrun)
	add.l	d1,sys_fsbb(a6) 	 ; new top of heap
	move.l	d1,(a1)+
	clr.l	(a1)+			 ; no linkage
	clr.l	(a1)+			 ; owned by 0
	add.w	#$200-$c,a1
	move.l	a1,sms.128kb		 ; for 128 k byte buffer

	moveq	#0,d0
	rts

	end
