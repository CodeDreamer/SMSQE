; SMS Priviledge Violation Recovery for 68020/68030/68040   1992 Tony Tebby

	section base

	xdef	sms_privv

	include 'dev8_smsq_smsq_base_keys'

;+++
; Priviledge violation recovery
;   This assumes that there are two long words below the exception frame.
;   These will usually be a return address (jsr sms_privv) and the exception
;      identification (offset address, pointer to message etc.).
;   This routine will attempt to identify and execute all instructions of the
;      type  MOVE.W SR,ea
;   If no recovery is possible, this routine returns.
;   If recovery is possible, the two long words on the stack are thrown away,
;      the saved program counter is updated by 2, 4 or 6 bytes and execution
;      resumed.
;---
sms_privv
spv.reg reg	d0/d1/d2/d3/a0
stk_a0	equ	$10
stk.ret equ	  $08
stk_sr	equ	$1c
stk_pc	equ	$1e
	movem.l spv.reg,-(sp)


	move.w	stk_sr(sp),d3
	move.l	stk_pc(sp),a0		 ; address of offending instruction
	moveq	#2,d2			 ; assumed instruction length

	moveq	#$ffffffc0,d0
	and.w	(a0),d0 		 ; mask all but destination
	cmp.w	#$40c0,d0		 ; move.w SR,ea?
	bne.s	spv_exit

	moveq	#7,d1
	and.w	(a0),d1 		 ; register
	lsl.w	#2,d1			 ; register * 4
	moveq	#$38,d0
	and.w	(a0),d0 		 ; mode
	lsr.w	#2,d0			 ; *2
	move.w	spv_mtab(pc,d0.w),d0
	jmp	spv_mtab(pc,d0.w)
spv_mtab
	dc.w	spv_dreg-spv_mtab
	dc.w	spv_exit-spv_mtab
	dc.w	spv_indr-spv_mtab
	dc.w	spv_psti-spv_mtab

	dc.w	spv_pred-spv_mtab
	dc.w	spv_disp-spv_mtab
	dc.w	spv_indx-spv_mtab
	dc.w	spv_abs-spv_mtab


spv_exit
	movem.l (sp)+,spv.reg
	rts


; set data register

spv_dreg
	cmp.w	#3*4,d1 		 ; 0 to 3?
	bgt.s	spv_dset
	move.w	d3,2(sp,d1.w)		 ; set register on stack
	bra.s	spv_ok			 ; done
spv_dset
	jmp	spv_dset-3*4(pc,d1.w)	 ; code for each D register

	move.w	d3,d4			 ; set d4
	bra.s	spv_ok

	move.w	d3,d5			 ; set d5
	bra.s	spv_ok

	move.w	d3,d6			 ; set d6
	bra.s	spv_ok

	move.w	d3,d7			 ; set d7
	bra.s	spv_ok

; set register indirect

spv_indr
	jmp	spv_indr+4(pc,d1.l)	 ; code for each A reg

	bra.s	spv_ina0
	nop

	move.w	d3,(a1)
	bra.s	spv_ok

	move.w	d3,(a2)
	bra.s	spv_ok

	move.w	d3,(a3)
	bra.s	spv_ok

	move.w	d3,(a4)
	bra.s	spv_ok

	move.w	d3,(a5)
	bra.s	spv_ok

	move.w	d3,(a6)
	bra.s	spv_ok

	move.l	usp,a0
	move.w	d3,(a0)
	bra.s	spv_aok

spv_ina0
	move.l	stk_a0(sp),a0
	move.w	d3,(a0)
	bra.s	spv_aok

; set post increment

spv_psti
	jmp	spv_psti+4(pc,d1.l)	 ; code for each A reg

	bra.s	spv_psa0
	nop

	move.w	d3,(a1)+
	bra.s	spv_ok

	move.w	d3,(a2)+
	bra.s	spv_ok

	move.w	d3,(a3)+
	bra.s	spv_ok

	move.w	d3,(a4)+
	bra.s	spv_ok

	move.w	d3,(a5)+
	bra.s	spv_ok

	move.w	d3,(a6)+
	bra.s	spv_ok

	move.l	usp,a0
	move.w	d3,(a0)+
	move.l	a0,usp
	bra.s	spv_aok

spv_psa0
	move.l	stk_a0(sp),a0
	move.w	d3,(a0)+
	move.l	a0,stk_a0(sp)
;;	bra.s	spv_aok

spv_aok
	move.l	stk_pc(sp),a0
spv_ok
	add.w	d2,a0
	move.l	a0,stk_pc(sp)		 ; move program counter on

	jsr	sms.cinvd		 ; code modified in data cache
	jsr	sms.cinvi		 ; code modified

	movem.l (sp)+,spv.reg		 ; restore regs
	addq.l	#8,sp			 ; skip two return addresses
	rte

; set predecrement

spv_pred
	jmp	spv_pred+4(pc,d1.l)	 ; code for each A reg

	bra.s	spv_pra0
	nop

	move.w	d3,-(a1)
	bra.s	spv_ok

	move.w	d3,-(a2)
	bra.s	spv_ok

	move.w	d3,-(a3)
	bra.s	spv_ok

	move.w	d3,-(a4)
	bra.s	spv_ok

	move.w	d3,-(a5)
	bra.s	spv_ok

	move.w	d3,-(a6)
	bra.s	spv_ok

	move.l	usp,a0
	move.w	d3,-(a0)
	move.l	a0,usp
	bra.s	spv_aok

spv_pra0
	move.l	stk_a0(sp),a0
	move.w	d3,-(a0)
	move.l	a0,stk_a0(sp)
	bra.s	spv_aok

; register indirect with displacement

spv_disp
	moveq	#4,d2			; four byte instruction
	move.w	2(a0),d0		; the displacement
	jmp	spv_dscd(pc,d1.w)
spv_dscd
	bra.s	spv_dsa0
	nop

	move.l	a1,a0
	bra.s	spv_dsst

	move.l	a2,a0
	bra.s	spv_dsst

	move.l	a3,a0
	bra.s	spv_dsst

	move.l	a4,a0
	bra.s	spv_dsst

	move.l	a5,a0
	bra.s	spv_dsst

	move.l	a6,a0
	bra.s	spv_dsst

	move.l	usp,a0
	bra.s	spv_dsst

spv_dsa0
	move.l	stk_a0(sp),a0
spv_dsst
	move.w	d3,(a0,d0.w)
	bra	spv_aok

; Register with index and displacement

spv_indx
	move.w	2(a0),d0		; index+displacement
	jmp	spv_ixc(pc,d1.w)
spv_ixc
	bra.s	spv_ixa0
	nop

	move.l	a1,a0
	bra.s	spv_ixsi

	move.l	a2,a0
	bra.s	spv_ixsi

	move.l	a3,a0
	bra.s	spv_ixsi

	move.l	a4,a0
	bra.s	spv_ixsi

	move.l	a5,a0
	bra.s	spv_ixsi

	move.l	a6,a0
	bra.s	spv_ixsi

	move.l	usp,a0
	bra.s	spv_ixsi

spv_ixa0
	move.l	stk_a0(sp),a0
spv_ixsi
	move.b	d0,d1
	ext.w	d1
	add.w	d1,a0			 ; displacement
	move.l	a0,d2			 ; save it
	lsl.l	#4,d0
	swap	d0			 ; index reg in lsw word / long in msb
	lsl.w	#2,d0
	cmp.w	#3*4,d0 		 ; low D reg?
	bhi.s	spc_ixim		 ; ... no
	move.l	(sp,d0.w),a0		 ; ... yes
	bra.s	spv_ixst

spc_ixim
	jmp	spc_ixim-3*4(pc,d0.w)

	move.l	d4,a0
	bra.s	spv_ixst

	move.l	d5,a0
	bra.s	spv_ixst

	move.l	d6,a0
	bra.s	spv_ixst

	move.l	d7,a0
	bra.s	spv_ixst

	bra.s	spv_iia0
	nop

	move.l	a1,a0
	bra.s	spv_ixst

	move.l	a2,a0
	bra.s	spv_ixst

	move.l	a3,a0
	bra.s	spv_ixst

	move.l	a4,a0
	bra.s	spv_ixst

	move.l	a5,a0
	bra.s	spv_ixst

	move.l	a6,a0
	bra.s	spv_ixst

	move.l	usp,a0
	bra.s	spv_ixst

spv_iia0
	move.l	stk_a0(sp),a0
spv_ixst
	tst.l	d0			 ; long word index?
	bmi.s	spv_ixsl

	move.w	a0,a0			 ; ... no, sign extend a0
spv_ixsl
	move.w	d3,(a0,d2.l)		 ; set status register
	moveq	#4,d2			 ; four byte instruction
	bra.l	spv_aok

; absolute addresses

spv_abs
	subq.w	#1*4,d1 		 ; long address
	beq.s	spv_absl
	bhi	spv_exit		 ; ... no, and not short
	move.w	2(a0),a0		 ; absolute short
	moveq	#4,d2
	bra.s	spv_aset
spv_absl
	move.l	2(a0),a0
	moveq	#6,d2			 ; absolute long
spv_aset
	move.w	d3,(a0)
	bra.l	spv_aok

	end
