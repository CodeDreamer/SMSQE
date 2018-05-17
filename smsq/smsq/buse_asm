; QL Vector Bus Error Handler   1993  Tony Tebby

	section base

	xdef	sms_buse

	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'

;+++
; If the exception is caused by an access to a QL exception vector
;   the operation is performed and excution contines
; otherwise
;   if the protection level is 0 for the operation
;     a null operation is performed
;   otherwise
;     this routine returns.
;
; This routine is called with the return address and exception frame on the
; stack
;---
sms_buse
	move.l	d0,-(sp)		 ; two working registers
	move.l	a0,-(sp)		 ; in non-obvious order
	move.w	#$e000,d0	; frame format mask
	and.w	8+4+$6(sp),d0
	cmp.w	#$a000,d0	; short or long format?
	beq.l	sbe_long	; yes, great! - easy to handle!

stk_junk equ	8+4+$0
stk_addr equ	8+4+$2
stk_inst equ	8+4+$6
stk_sr	 equ	8+4+$8
stk_pc	 equ	8+4+$a
stk_fram equ	  4+$8

	btst	#4,1+stk_junk(sp)	 ; read?
	beq.s	sbn_write		 ; ... no
	move.l	stk_addr(sp),a0 	 ; access address
	cmp.l	#$140,a0		 ; QL vector
	bhs.s	sbe_nvec		 ; ... no
	cmp.l	#$c0,a0
	blo.s	sbe_nvec		 ; ... no
sbe_trace
	move.l	exv_trac,sms.strac	 ; save trace
	lea	sbe_cont,a0
	move.l	a0,exv_trac

	move.w	stk_inst(sp),d0        ; ... instruction
	move.l	stk_pc(sp),a0
sbe_ilook
	cmp.w	-(a0),d0
	bne.s	sbe_ilook
	move.l	a0,stk_pc(sp)
	move.l	(sp)+,a0
	move.l	(sp)+,d0
	add.w	 #stk_fram,sp		  ; skip return and junk
	move.b	(sp),sms.ssr
	or.w	#$a000,(sp)		  ; set trace and supervisor mode
	rte

sbe_cont
	move.l	sms.strac,exv_trac	 ; restore trace
	move.b	sms.ssr,(sp)		 ; and mode
	rte

sbe_prot
	move.l	sms.sysb,a0
	btst	d0,sys_pmem(a0) 	 ; read or write protected?
	beq.s	sbej_rts		 ; ... no
	move.l	sys_jbpt(a0),d0
	cmp.l	sys_jbtb(a0),d0 	 ; job 0?
	bne.s	sbej_rts		 ; ... no
	btst	#2,sys_pmem(a0) 	 ; job 0 unprotected?
sbej_rts
	rts

sbe_exit
	move.l	(sp)+,a0
	move.l	(sp)+,d0		 ; restore work regs
	rts

sbn_write
	moveq	#0,d0
	bsr.s	sbe_prot		 ; unprot or job = 0 and unprot 0?
	bne.s	sbe_exit		 ; ... no

sbn_cont
	move.l	(sp)+,a0
	move.l	(sp)+,d0
	add.w	 #stk_fram,sp		  ; skip return and junk
	rte

sbe_nvec
	btst	#sr..s,stk_sr(sp)	 ; user mode access?
	bne.s	sbe_exit		 ; .... NO!!!!

	moveq	#1,d0
	bsr.s	sbe_prot		 ; unprot or job = 0 and unprot 0?
	bne.s	sbe_exit		 ; ... no
sbn_rdo
	tst.l	stk_addr(sp)		 ; access address 0?
	bne	sbe_trace		 ; ... no, trace instruction in S mode

	move.w	stk_inst(sp),d0
	and.w	#%1101000110000000,d0
	cmp.w	#%0001000000000000,d0	 ; move.b/w S,Rn
	bne	sbe_trace		 ; ... not recognised

	move.w	#%0000111001000000,d0
	and.w	stk_inst(sp),d0 	 ; ....nnn..A......
	lsr.b	#2,d0			 ; ....nnn....A....
	lsr.w	#4,d0			 ; ........nnn....A
	ror.b	#3,d0			 ; ..........Annn..
	btst	#5,stk_inst(sp) 	 ; byte or word
	bne.s	sbn_jump		 ; word
	add.w	#sbn_clrb-sbn_setw,d0
sbn_jump
	move.w	d0,a0
	moveq	#3,d0
	jmp	sbn_setw(pc,a0.w)

sbn_setw
	move.w	d0,d0			    ; set returned d0
	bra.s	sbn_setd0w
	move.w	d0,d1
	bra.s	sbn_contnz
	move.w	d0,d2
	bra.s	sbn_contnz
	move.w	d0,d3
	bra.s	sbn_contnz
	move.w	d0,d4
	bra.s	sbn_contnz
	move.w	d0,d5
	bra.s	sbn_contnz
	move.w	d0,d6
	bra.s	sbn_contnz
	move.w	d0,d7
	bra.s	sbn_contnz
	move.w	d0,a0
	bra.s	sbn_seta0w
	move.w	d0,a1
	bra.s	sbn_cont
	move.w	d0,a2
	bra.s	sbn_cont1
	move.w	d0,a3
	bra.s	sbn_cont1
	move.w	d0,a4
	bra.s	sbn_cont1
	move.w	d0,a5
	bra.s	sbn_cont1
	move.w	d0,a6
	bra.s	sbn_cont1
	bra	sbe_exit		 ; set SP to 0!!!???

sbn_contnz
	clr.b	stk_sr+1(sp)		 ; for word, set ccr to nz
sbn_cont1
	bra	sbn_cont

sbn_setd0w
	clr.w	6(sp)			 ; set returned d0.w
	bra	sbn_cont
sbn_seta0w
	clr.l	(sp)			 ; set returned a0
	bra	sbn_cont

sbn_clrb
	clr.b	d0			 ; set returned d0
	bra.s	sbn_clrd0b
	clr.b	d1
	bra.s	sbn_contz
	clr.b	d2
	bra.s	sbn_contz
	clr.b	d3
	bra.s	sbn_contz
	clr.b	d4
	bra.s	sbn_contz
	clr.b	d5
	bra.s	sbn_contz
	clr.b	d6
	bra.s	sbn_contz
	clr.b	d7
	bra.s	sbn_contz

sbn_clrd0b
	clr.w	6(sp)			 ; set returned d0.w

sbn_contz
	move.b	#$4,stk_sr+1(sp)	 ; for byte, set ccr to z
	bra	sbn_cont



sbe_long
	btst	#0,8+4+$a(sp)		 ; error on data?
	beq	sbe_exit		 ; no, bad
	btst	#6,8+4+$a+1(sp) 	 ; read?
	beq.s	sbl_write		 ; ... no

	move.l	8+4+$10(sp),a0		 ; error address

	cmp.l	#$140,a0		 ;  QL vector?
	bhs.s	sbl_nvec

	cmp.l	#$c0,a0
	blo.s	sbl_nvec

	move.w	(a0),8+4+$2e(sp)	 ; .. and into data input buffer
sbl_cont
	bclr	#0,8+4+$a(sp)		 ; signal 'data cycle successful'
	move.l	(sp)+,a0
	move.l	(sp)+,d0		 ; restore work regs
	addq.l	#4,sp			 ; skip return address
	rte

sbl_write
	moveq	#0,d0
	bra.s	sbl_chk

sbl_nvec
	moveq	#1,d0
sbl_chk
	btst	#sr..s,stk_sr(sp)	 ; user mode access?
	bne	sbe_exit		 ; .... NO!!!!
	clr.l	8+4+$2c(sp)		 ; clear data input

	bsr.l	sbe_prot		 ; unprot or job = 0 and unprot 0?
	beq	sbl_cont		 ; ... yes
	bne	sbe_exit		 ; ... no

	end
