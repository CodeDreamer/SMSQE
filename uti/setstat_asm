; Set list of statuses to pre-defined or the same status
;					1994 Jochen Merz    V0.00

	include dev8_keys_wstatus
	include dev8_keys_wwork

	section utility

	xdef	ut_slstat	; set list of statuses
	xdef	ut_ssstat	; set same statuses

;+++
; Set list of statuses. To save space, and as it is unlikely that a menu
; has more than 254 loose menu items, a byte status item number is used.
; The list is organised as follows:
;
;	byte	item number
;	byte	status
; It is terminated by a -1 byte.
;
;		Entry				Exit
;	a1	ptr to list			window status area
;	a4	window working def		preserved
;---
sls.reg reg	d1/a0
ut_slstat
	movem.l sls.reg,-(sp)
	move.l	a1,a0		; the list
	move.l	ww_wstat(a4),a1 ; window status area
	moveq	#0,d1
ut_slsloop
	move.b	(a0)+,d1	; get status number
	cmp.b	#$ff,d1 	; end of list?
	beq.s	ut_slsend
	move.b	(a0)+,ws_litem(a1,d1.w) ; fill in status
	bra.s	ut_slsloop
ut_slsend
	movem.l (sp)+,sls.reg
	rts

;+++
; Set same statuses. To save space, and as it is unlikely that a menu
; has more than 254 loose menu items, a byte status item number is used.
; The list is organised as follows:
;
;	byte	item number
; It is terminated by a -1 byte.
;
;		Entry				Exit
;	d0.b	status to set
;	a1	ptr to list			window status area
;	a4	window working def		preserved
;---
ut_ssstat
	movem.l sls.reg,-(sp)
	move.l	a1,a0		; the list
	move.l	ww_wstat(a4),a1 ; window status area
	moveq	#0,d1
ut_sssloop
	move.b	(a0)+,d1	; get status number
	cmp.b	#$ff,d1 	; end of list?
	beq.s	ut_slsend
	move.b	d0,ws_litem(a1,d1.w) ; set status
	bra.s	ut_sssloop

	end
