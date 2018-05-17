; Use and free Button frame		1989 Jochen Merz

	section utility

	include dev8_keys_qdos_sms

	xdef	ut_usnbt	; use button frame with new position
	xdef	ut_usrbt	; use button frame but re-allocate
	xdef	ut_frbtn	; free button frame

	xref	gu_thjmp

;+++
; Find position in Button Frame for current job.
;
;		Entry				Exit
;	D1.l	width | height			x origin | y origin
;
;	Error returns: <> 0 if Button frame or THING does not exist
;---
ut_usnbt
btnu_reg reg	d2-d3/a0-a2
	movem.l btnu_reg,-(sp)
	moveq	#0,d3		; signal 'new entry'
	bra.s	use_btn
;+++
; Re-allocate a position in Button Frame for current job.
;
;		Entry				Exit
;	D1.l	width | height			x origin | y origin
;
;	Error returns: err.itnf 	Button frame does not exist
;---
ut_usrbt
	movem.l btnu_reg,-(sp)
	moveq	#-1,d3		; signal 're-allocate'
use_btn
	move.l	d1,d2
	moveq	#-1,d1		; for current job
	lea	btnf_nam,a0	; that's the Button Frame
	moveq	#sms.uthg,d0	; use it
	jsr	gu_thjmp
	tst.l	d0
	beq.s	use_ok		; failed, return default
use_err
	moveq	#-1,d2		; no thing, return default position
use_ok
	move.l	d2,d1		; that's the position!
	movem.l (sp)+,btnu_reg
	tst.l	d0
	rts

;+++
; Free entry in Button Frame.
;
; All registers including D0 are preserved.
;---
ut_frbtn
btnf_reg reg	d0-d3/a0-a2
	movem.l btnf_reg,-(sp)
	moveq	#-1,d1		; that's the current job
	lea	btnf_nam,a0	; the Thing we'd like to free
	moveq	#sms.fthg,d0	; free it
	jsr	gu_thjmp
	movem.l (sp)+,btnf_reg
	rts

btnf_nam
	dc.w	12
	dc.b	'Button Frame'

	end
