* Create a job in TPA	V2.00	 1986	Tony Tebby  QJUMP
* 2.01 make sure size & data space are even numbers (wl)
*
	section sms
*
	xdef	sms_crjb
*
	xref	sms_ckid		check job id
	xref	mem_atpa		allocate in tpa
	xref	sms_rte
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_jcbq
*
*	d0  r	0, invalid job, out of memory
*	d1 cr	owner job ID / new job ID
*	d2 c  p length of code
*	d3 c  p length of data space
*	d7   s	new job id (swapped)
*	a0  r	base of job
*	a1 c  p start address or 0
*	a6 c  p base of system variables
*
*	all other registers preserved
*
reglist reg	d1/a1
stk_oid  equ	$0
stk_strt equ	$4
*
sms_crjb
	move.l	sp,sys_psf(a6)		 ; set stack frame (for move superBASIC)
	bsr.l	sms_ckid		 ; check job id for possible
	bne.l	sms_rte 		 ; ... oops
	move.l	a1,-(sp)		 ; save job start
	move.l	d1,-(sp)		 ; and owner ID
*
* search job table for new job ID
*
	move.l	sys_jbtb(a6),a1 	 ; search job table for empty entry
	moveq	#-1,d7			 ; start at 0
*
scj_stab
	addq.l	#1,d7
	move.l	(a1)+,d0		 ; is it empty?
	blt.s	scj_atpa		 ; ... yes, allocate memory
	cmp.l	sys_jbtt(a6),a1 	 ; at top?
	blt.s	scj_stab		 ; ... no, carry on searching the table
	moveq	#err.imem,d0		 ; ... yes, insufficient memory
*
scj_exer
	moveq	#-1,d1			 ; set non job
	bra	scj_exit		 ; and exit
*
scj_atpa
	moveq	#jcb_end,d1		 ; set total memory required, header
	addq.l	#1,d2
	bclr	#0,d2
	add.l	d2,d1			 ; + program
	addq.l	#1,d3
	bclr	#0,d3
	add.l	d3,d1			 ; + data
	bsr.l	mem_atpa		 ; allocate
	bne.s	scj_exer		 ; ... oops
*
	move.l	a0,-(a1)		 ; set address of job
*
	cmp.w	sys_jbtp(a6),d7 	 ; new top job?
	ble.s	scj_gtag		 ; ... no
	move.w	d7,sys_jbtp(a6) 	 ; ... yes, set it
*
scj_gtag
	swap	d7
	move.w	sys_jbtg(a6),d7 	 ; get tag
	addq.w	#1,sys_jbtg(a6)
	bvc.s	sjc_fill		 ; keep it positive
	rol.w	sys_jbtg(a6)		 ; and non zero
*
* fill job header
*
sjc_fill
	move.l	stk_strt(sp),a1 	 ; get start address
	move.l	a1,d0			 ; given?
	bne.s	scj_fill		 ; ... yes
	lea	jcb_end(a0),a1		 ; ... no
scj_fill
	addq.l	#4,a0			skip length
	move.l	a1,(a0)+		start address
	move.l	stk_oid(sp),(a0)+	owner
	clr.l	(a0)+			released flag
	move.w	d7,(a0)+		job tag
	clr.w	(a0)+			priority
	clr.l	(a0)+			wait, rel address and waiting job
	clr.l	(a0)+			waiting job id
	move.l	sys_ertb(a6),(a0)+	exception vector table
*
	moveq	#jcb_a4-jcb_save,d0	clear up to a4
scj_clear
	clr.l	(a0)+
	subq.w	#4,d0
	bgt.s	scj_clear
*
	move.l	d2,(a0)+		program size (a4)
	move.l	d2,d0
	add.l	d3,d0			total size (a5)
	move.l	d0,(a0)+
	moveq	#jcb_end-jcb_a6,d1
	add.l	a0,d1			end of header
	move.l	d1,(a0)+		... base of job (a6)
	add.l	d1,d0			top of program
	subq.l	#4,d0			less 4 bytes
	move.l	d0,(a0)+		... initial USP
	clr.w	(a0)+			SR
	move.l	a1,(a0)+		PC
	clr.w	(a0)+			event
*
	move.l	d0,a1			clear TOS
	clr.l	(a1)			(two words)
*
	move.l	d7,d1			set job ID
	swap	d1			unswapped
	moveq	#0,d0			no error
*
scj_exit
	addq.l #4,sp			remove owner id
	move.l (sp)+,a1 		restore a1
	bra.l  sms_rte
	end
