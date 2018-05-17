; Start up a filter job 	1998 Jochen Merz

	include dev8_keys_jcb
	include dev8_keys_qdos_sms
	include dev8_keys_qdos_io
	include dev8_keys_qdos_ioa
	include dev8_keys_err
	include dev8_keys_hdr
	include dev8_mac_xref
	
	section utility

	xdef	ut_exfilter

;+++
; Start up a filter job which may be any executable file or, with SMSQ/E,
; any _bas, .bas, _sav or .sav file. In case of any error, an error window
; is displayed and the error "not complete" is returned.
; The job is created but not activated!
; This routine requires a lot of stack space!!!
;
;		Entry			Exit
;	d1				job-ID or -1
;	d5	input channel ID
;	d6	output channel ID
;	d7	colourway
;	a0	filter filename
;	a4	error if filter fails
;	a5	printer driver job name
;
;	Error codes:
;	nc	filterfile or SBASIC not found
;---
stk.buff equ	16			; use some stack as buffer
par.buff equ	24
exf.regs reg	d2-d3/a1-a3
ut_exfilter
	movem.l exf.regs,-(sp)		; save some regs
	sub.w	#stk.buff,sp
	move.l	a0,a1
	lea	bas_exts,a3
	xjsr	fu_chkext
	move.l	d1,d4			; BASIC flag

	move.l	a0,a2
	moveq	#ioa.kshr,d3		; shared access (read)
	xjsr	gu_fopen
	bne.s	file_error		; open error

	tst.b	d4			; BASIC?
	bne	ex_basic

	move.l	sp,a1			; buffer for header
	moveq	#16,d2			; length of file-header
	moveq	#iof.rhdr,d0		; read header
	xjsr	gu_iow
	bne.s	file_error_close	; read header error

	move.l	sp,a1			; start of header
	cmp.b	#hdrt.exe,hdr_type(a1)	; executable?
	bne.s	file_error_close	; no, error, but close file first

	xjsr	gu_fclos		; close file again
	sub.w	#par.buff,sp
	move.l	sp,a1			; parameter string
	move.w	#12,(a1)+		; word + 2 ID's + word
	move.w	#2,(a1)+		; three ID's
	move.l	d5,(a1)+		; input ID
	move.l	d6,(a1)+		; output ID
	clr.w	(a1)			; no parameter string
	move.l	sp,a1			; parameter string
	move.l	a2,a0			; filename
	moveq	#-1,d1			; owner
	moveq	#0,d2			; don't start and don't wait
	move.l	a5,a2			; driver name
	sub.l	a3,a3
	xjsr	gu_fexnm
	add.w	#par.buff,sp
	bra.s	return

file_error_close
	xjsr	gu_fclos		; close file first
file_error
	move.l	a4,d0
	bset	#31,d0			; the error text
	moveq	#-1,d1			; origin
	move.b	d7,d2			; colourway
	xjsr	mu_rperr		; report error
	moveq	#err.nc,d0		; and return error

return
	add.w	#stk.buff,sp
	movem.l (sp)+,exf.regs
	rts

bas_exts
	dc.b	0,'BAS'
	dc.b	0,'SAV'
	dc.l	-1

met_sbasic
	dc.w	6,'SBASIC'

ex_basic
	sub.w	#par.buff,sp
	move.l	sp,a1			; parameter string
	move.w	#16,(a1)+		; word + 3 ID's + word
	move.w	#3,(a1)+		; three ID's
	move.l	a0,(a1)+		; program ID
	move.l	d5,(a1)+		; input ID
	move.l	d6,(a1)+		; output ID
	clr.w	(a1)			; no parameter string
	move.l	sp,a1			; parameter string
	moveq	#-1,d1			; owner
	moveq	#0,d2			; don't start and don't wait
	lea	met_sbasic,a0
	move.l	a5,a2			; job name
	xjsr	gu_thexn
	add.w	#par.buff,sp
	bra	return

	end
