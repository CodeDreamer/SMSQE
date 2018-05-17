* Execute code from a file		v0.01   Apr 1988  J.R.Oakley  QJUMP
*
* 2005-11-15  0.01  Adapted for home directory (MK)
*
	section gen_util
*
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
*
	xref	gu_exec
	xref	gu_trap3
	xref	gu_fopen
	xref	gu_fclos
	xref	gu_shome
*
	xdef	gu_fexec
*+++
* This uses the general execute utility GU_EXEC to load and execute a file.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1	owner, or 0, or -1		job ID
*	D2	priority | timeout		preserved
*	A0	file name			preserved
*	A1	parameter string		preserved
*	A3	start address or 0		preserved
*	stack	FEXEC $08+GU_EXEC
*		LOAD  $08+GU_TRAP3
*		OPEN  $16+GU_FOPEN/gu_TRAP3
*		CLOSE $00+GU_FCLOS
*---
gu_fexec
ufereg	reg	a2/a5
	movem.l ufereg,-(sp)
	move.l	a0,a5			; keep file name for home directory
	lea	ufe_ucod(pc),a2 	; point to user code
	jsr	gu_exec(pc)		; and use it to execute a file
	bne.s	gfex_exit
	moveq	#0,d0			; ignore error
gfex_exit
	movem.l (sp)+,ufereg
	rts
*
ufe_ucod
	bra.s	ufe_load
	nop
	bra.s	ufe_open
	nop
*
ufe_clos
	jmp	gu_fclos(pc)		; file close is easy
*
ufe_load
uflreg	reg	d1/a1
	movem.l uflreg,-(sp)
	move.l	a5,a1
	jsr	gu_shome(pc)		; set home dir (before job is run!)
	movem.l (sp),uflreg		; restore a1
	moveq	#iof.load,d0		; load a file
	jsr	gu_trap3(pc)		; with an infinite timeout
	movem.l (sp)+,uflreg
	rts
*
ufe_open
uforeg	reg	d1 
ufo.fram equ	14		; file header
	movem.l uforeg,-(sp)
	sub.w	#ufo.fram,sp
	moveq	#ioa.kshr,d3		; open the file shared
	jsr	gu_fopen(pc)
	bne.s	ufo_exit		; ...oops
*
	moveq	#ufo.fram,d2		; use the whole frame
	move.l	sp,a1			; which is here $$$$$ SuperBASIC
	moveq	#iof.rhdr,d0		; to read the header
	jsr	gu_trap3(pc)	      
	bne.s	ufo_excl		; ...oops
*
	moveq	#err.ipar,d0		; assume the worst
	cmp.b	#1,hdr_type(sp) 	; executable?
	bne.s	ufo_excl		; no, raatz
	move.l	hdr_flen(sp),d2 	; code length is file length
	move.l	hdr_data(sp),d3 	; dataspace is this
	moveq	#0,d0			; no problem
	bra.s	ufo_exit
ufo_excl
	bsr.s	ufe_clos		; close file, keeping error 
ufo_exit
	move.l	a3,a1			; start address as specified
	add.w	#ufo.fram,sp
	movem.l (sp)+,uforeg
	rts
*
	end
