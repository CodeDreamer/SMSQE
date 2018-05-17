; Test event handling from basic

; 1.01 2016 Nov 17 added fsend_event (wl)

	section test

; SEND_EVENT job id, events
; events = WAIT_EVENT (event mask)
; error=FSEND_EVENT (job id, events)

	xdef	send_event
	xdef	wait_event
	xdef	fsend_event

	xref	job_gid
	xref	ut_gtin1
	xref	ut_gxin1
	xref	ut_retin
	xref	ut_rtfd1

	include dev8_keys_qdos_sms
	include dev8_sbsext_ext_keys

fsend_event
	bsr.s	send_event
	move.l	d0,d1
	clr.l	d0
	jmp	ut_rtfd1

send_event
	moveq	#8,d5			2 params
	jsr	job_gid
	bne.s	event_rts
	moveq	#sms.sevt,d0
	trap	#1
event_rts
	rts

wait_event
	jsr	ut_gtin1		; get event mask
	bne.s	event_rts
	moveq	#-1,d3			; wait forever
	add.l	#8,a3
	cmp.l	a3,a5			; a timeout?
	beq.s	weve_do
	jsr	ut_gxin1		; get timeout
	bne.s	event_rts
	move.w	(a6,a1.l),d3
	addq.l	#2,a1
	move.l	a1,bv_rip(a6)		; pop it

weve_do
	move.w	(a6,a1.l),d2		; get event mask
	moveq	#sms.wevt,d0
	trap	#1
	tst.l	d0
	bne.s	event_rts
	move.w	d2,(a6,a1.l)
	jmp	ut_retin

	end
