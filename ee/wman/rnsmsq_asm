; Read name / edit name (SMSQ version)	    1994 Tony Tebby V0.02

	section wman

	xdef	wm_rnsmsq
wm_rnsmsq
	xdef	wm_rname	; read name
	xdef	wm_ename	; edit name

	include dev8_keys_k
	include dev8_keys_err
	include dev8_keys_qdos_io

;+++
; Read or edit name using SMSQ call.
; Fully call compatible with WM.ENAME and WM.RNAME.
;
;		Entry				Exit
;	d1.b					terminating character
;	a0	channel ID			preserved
;	a1	pointer to string		preserved
;
;	any I/O subsystem errors, >0 if terminator not <NL>
;
;---

wmr_reg reg	d2/d3/a1
stk_name equ	$8

wm_rname
	movem.l wmr_reg,-(sp)
	moveq	#1,d1		; this is read name
	ror.l	#1,d1		; set msbit for throwaway
	bra.s	wmr_begin
wm_ename
	movem.l wmr_reg,-(sp)
	moveq	#0,d1		; and this is edit name
wmr_begin
	move.l	d1,-(sp)
	moveq	#iow.clrl,d0	; clear line
	bsr.s	wmr_io

	move.l	(sp)+,d1
	move.l	stk_name(sp),a1
	move.w	(a1)+,d1
	add.w	d1,a1		; buffer position
				; we use bug in QDOS routine that puts cursor
				; end if pos = 0 and not throwaway
	moveq	#-1,d2		; calculate buffer size for single line
	moveq	#iob.elin,d0
	bsr.s	wmr_io
	move.l	d0,d2
	bne.s	wmr_exit
	moveq	#k.enter,d3
	move.b	-(a1),d2	; terminating character
	move.b	d3,(a1)
	subq.w	#1,d1		; remove terminator from string
	sub.w	d1,a1
	move.w	d1,-(a1)	; set length
	move.w	d2,d1
	cmp.w	d3,d1		; set terminator flags
wmr_exit
	movem.l (sp)+,wmr_reg
	rts

wmr_io
	moveq	#-1,d3		; wait forever
	trap	#3
	move.l	d0,d2
	rts
	end
