* Execute code from a file with name	  v0.02   Apr 1988  J.R.Oakley  QJUMP
*						  Jun 1988  Tony Tebby  QJUMP
*
* 2005-25-10  0.01  Added home directory thing compatibility (MK)
* 2005-11-15  0.02  Fixed race condition (MK)
*
	section gen_util
*
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
*
	xref	gu_exec
	xref	gu_trap3
	xref	gu_fopen
	xref	gu_fclos
	xref	gu_shome
*
	xdef	gu_fexnm
	xdef	gu_fexop
	xdef	gu_fexld
*+++
* This uses the general execute utility GU_EXEC to load and execute a file.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1	owner, or 0, or -1		job ID
*	D2	priority | timeout		preserved
*	A0	file name			preserved
*	A1	pointer to parameter string	preserved
*	A2	pointer to job name		preserved
*	A3	start address or 0		preserved
*	stack	FEXEC $08+GU_EXEC
*		LOAD  $08+GU_TRAP3
*		OPEN  $16+GU_FOPEN/gu_TRAP3
*		CLOSE $00+GU_FCLOS
*---
gu_fexnm
ufereg	reg	d4/a1/a2/a4-a5
	movem.l ufereg,-(sp)
	move.l	a2,a4			; keep name
	move.l	a0,a5			; keep file name for home dir
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
*+++
* Read loads code into the job's code space.
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1	job ID				preserved
*	D2	code length			preserved
*	D4	extra length (header+name)
*	A0	"channel ID"			preserved
*	A1	code space			preserved
*	A4	program name to be added	preserved
*	A5	file name set as home directory preserved
*	A6	unused by this routine		used as required
*
*---
gu_fexld
ufe_load
uflreg	reg	d1/d2/a1/a2
stk_a1	equ	$08
	movem.l uflreg,-(sp)
	move.l	a5,a1			; file name
	jsr	gu_shome(pc)		; set for home directory
	move.l	stk_a1(sp),a1

	moveq	#iof.load,d0		; load a file
	sub.l	d4,d2			; true length
	add.l	d4,a1			; true position
	move.l	a1,a2			; ... keep it
	jsr	gu_trap3(pc)		; with an infinite timeout

	tst.l	d4			; any name to add?
	beq.s	ufl_done		; ... no
	move.l	stk_a1(sp),a1
	move.w	#$4ef9,(a1)+		; jmp  .l
	move.l	a2,(a1)+		; to start
	move.w	#$4afb,(a1)+		; flag
	move.w	(a4)+,d1		; name length
	move.w	d1,(a1)+
	bra.s	ufl_cend
ufl_cloop
	move.b	(a4)+,(a1)+		; name
ufl_cend
	dbra	d1,ufl_cloop
ufl_done
	tst.l	d0
	movem.l (sp)+,uflreg
	rts
*+++
* Open returns the space required for the job, a "channel ID" for load
* and close, and an absolute start address if required:
* if it fails then the "file" should be "closed".
*
*	Registers:
*		Entry				Exit
*	D0					error code
*	D1	owner
*	D2					code length
*	D3					data space length
*	D4					extra space for header+name
*	A0	filename			"channel ID"
*	A1					start address or 0
*	A3	start address or zero
*	A4	pointer to program name 	preserved
*	A5-A6	unused by this routine		used as required
*
*---
gu_fexop
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
	move.l	a4,d4			; name?
	beq.s	ufo_ok

	move.w	(a4),d0 		; length of name
	cmp.w	#sms.mxjn,d0		; enough for expandable job name
	bgt.s	ufo_setn
	moveq	#sms.mxjn,d0
ufo_setn
	moveq	#11,d4
	add.w	d0,d4			; length of name header
	bclr	#0,d4
	add.l	d4,d2			; total program length

ufo_ok
	moveq	#0,d0			; no problem
	bra.s	ufo_exit
ufo_excl
	bsr	ufe_clos		; close file, keeping error
ufo_exit
	move.l	a3,a1			; start address as specified
	add.w	#ufo.fram,sp
	movem.l (sp)+,uforeg
	rts
*
	end
