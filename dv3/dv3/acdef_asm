; DV3 Access Definition Block	       V2.00   1990   Tony Tebby QJUMP

	section dv3

	xdef	dv3_acdef

	xref	dv3_fdef

	include 'dev8_keys_qdos_sms'
;+++
; DV3 find drive definition block given driver linkage and drive number.
; Then call the supplied routine in Supervisor mode.
;
; The supplied routine is called with
;
;	d0	zero on entry, tested on exit from dv3_acdef
;	d1-d2	scratch
;	d3-d6	not used by dv3_acdef
;	d7	Drive ID / number
;	a0	scratch
;	a1-a2	not used by dv3_acdef
;	a3	pointer to linkage block
;	a4	pointer to drive definition block
;	a5	not used by dv3_acdef
;	a6	pointer to sysvar
;
;	d0	error status
;	d7 c  u drive number lsw / Drive ID/number
;	a0 c  p pointer to routine to call
;	a3 c  u pointer to driver linkage
;	all other registers preserved
;	status 0 or ERR.FDNF
;---
dv3_acdef
dad.reg reg	d1/d2/a0/a4/a6
	movem.l dad.reg,-(sp)

	move.w	sr,d1
	trap	#0
	move.w	d1,-(sp)		 ; go to supervisor mode to fiddle
	pea	dad_exit		 ; return from action
	move.l	a0,-(sp)		 ; return from fdef
	moveq	#sms.info,d0
	trap	#do.sms2
	move.l	a0,a6
	jmp	dv3_fdef		 ; find definition

dad_exit
	move.w	(sp)+,sr
	movem.l (sp)+,dad.reg
	tst.l	d0
	rts

	end
