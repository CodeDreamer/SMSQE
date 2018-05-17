; HOTKEY do operation with file defaults  V2.00     1988   Tony Tebby	 QJUMP

	section hotkey

	xdef	hk_dflts

	xref	met_dnam	 ; default name

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_ee_hk_data'

;+++
; This routine does a file operation with defaults (if required).
; The operation is done without default first. If it fails ERR.INAM or ERR.FDNF,
; then it is retried with either the program default or the built-in default.
; The call values d0 to d3, a1 to a3 are passed to the action routine, and
; they are returned to the calling routine. a0 is the pointer to the filename
; which may be changed internally, but it is returned from the action routine.
; Before the call, the calling routine must push a long word on to the stack.
; This is a pointer to a HKD.BUFL ($100) byte buffer for the name + default.
; d4-d7 and a5-a6 are passed to the action routine but are not preserved
; for the action routine call with default.
;
;	a0 cr	pointer to filename
;	a4 c  p pointer to action routine
;---
hk_dflts
regl1	reg	d0-d3/a1-a4
regl2	reg	a0
frame1	equ	$20
stk_buf equ	frame1+$04
frame2	equ	$04
	movem.l regl1,-(sp)
	move.l	a0,-(sp)
	jsr	(a4)				 ; try action
	beq.s	hkd_exit			 ; ... ok
	addq.l	#-err.fdnf,d0			 ; not found?
	beq.s	hkd_def 			 ; ... yes
	addq.l	#-err.inam+err.fdnf,d0
	bne.s	hkd_serr

hkd_def
	lea	met_dnam,a1			 ; our default
	moveq	#sms.info,d0			 ; try program default
	trap	#do.sms2
	move.l	sys_prgd(a0),d0 		 ; any default?
	beq.s	hkd_sdef
	move.l	d0,a1				 ; ... use it

hkd_sdef
	move.l	(sp)+,a0			 ; get our name reg
	move.l	stk_buf(sp),a2			 ; ... and buffer
	move.w	(a1)+,d0			 ; length of default
	cmp.w	#hkd.bufl-4,d0			 ; too long?
	bhi.s	hkd_inam			 ; ... yes
	cmp.b	#'_',-1(a1,d0.w)		 ; ends with '_'?
	beq.s	hkd_cdef
	addq.w	#1,d0				 ; ... one more

hkd_cdef
	move.w	d0,(a2)+
	bra.s	hkd_cdend
hkd_cdloop
	move.b	(a1)+,(a2)+			 ; copy default
hkd_cdend
	dbra	d0,hkd_cdloop

	move.b	#'_',-1(a2)			 ; set '_' at end

	move.l	a0,a1
	move.l	stk_buf(sp),a0			 ; start of complete name
	move.w	(a1)+,d0			 ; length of name
	add.w	d0,(a0)
	cmp.w	#hkd.bufl-4,(a0)		 ; total too long?
	bhi.s	hkd_inam
	bra.s	hkd_cnend
hkd_cnloop
	move.b	(a1)+,(a2)+			 ; copy name
hkd_cnend
	dbra	d0,hkd_cnloop

	movem.l (sp)+,regl1			 ; restore the rest of the regs
	jmp	(a4)				 ; and do final try

hkd_inam
	movem.l (sp)+,regl1			 ; restore first register set
	moveq	#err.inam,d0			 ; ... but not d0
	rts

hkd_serr
	subq.l	#-err.inam+err.fdnf,d0		 ; set error back to original
	subq.l	#-err.fdnf,d0
hkd_exit
	add.w	#frame1+frame2,sp		 ; return the register values
	rts
	end
