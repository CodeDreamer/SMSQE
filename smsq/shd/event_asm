; Event Handling	 V2.00	  1986  Tony Tebby  QJUMP

	section shd

	xdef	shd_sevt		; send event
	xdef	shd_wevt		; wait for event

	xref	shd_susp

	xref	sms_ckjx		; check the job exists
	xref	sms_cjid		; current job
	xref	sms_rtok
	xref	sms_rte

	include 'dev8_keys_sys'
	include 'dev8_keys_jcbq'

;+++
; Send event
;
;	d0  r	0 or invalid job
;	d1 cr	job ID
;	d2 c  p (b) events to send to job
;	a0  r	base of jcb
;
;	all other registers preserved
;---
shd_sevt
	jsr	sms_ckjx		 ; check the job exists
	bne.l	sms_rte
	move.b	jcb_evts(a0),d0 	 ; events so far
	or.b	d2,d0			 ; new events
	move.b	d0,jcb_evts(a0)
	and.b	jcb_evtw(a0),d0 	 ; waiting for one of these?
	beq.s	shd_rtok		 ; ... no
	clr.w	jcb_wait(a0)		 ; un-suspend
	move.b	d0,jcb_d2+3(a0) 	 ; set return parameter
	eor.b	d0,jcb_evts(a0) 	 ; clear events

	move.l	sys_jbpt(a6),a5 	 ; pointer to table
	move.l	a0,d0
	ble.s	shd_rtok		 ; ... no job at all!
	move.l	(a5),a5 		 ; address of our job
	move.b	jcb_pinc(a5),d0
	cmp.b	jcb_pinc(a0),d0 	 ; is our job lower priority?
	bhs.s	shd_done		 ; no
	bra.s	shd_rtok		 ; ... yes

;+++
; Wait for event
;
;	d0  r	0 or invalid job
;	d1  r	job ID
;	d2 c  p (b) events to wait for events occured
;	d3 c  p timout
;	a0  r	base of jcb
;
;	all other registers preserved
;---
shd_wevt
	bsr.l	sms_cjid		 ; get current job
	move.b	d2,d0
	and.b	jcb_evts(a0),d0 	 ; events already set?
	beq.s	shd_suspe		 ; ... no
	move.b	d0,d2
	eor.b	d2,jcb_evts(a0) 	 ; clear events
	bra.s	shd_rtok

shd_suspe
	clr.l	jcb_rflg(a0)		 ; no release flag
	move.b	d2,jcb_evtw(a0) 	 ; set events to wait for
	moveq	#0,d2			 ; no events so far
	bra.l	shd_susp

shd_done
	st	sys_rshd(a6)		 ; reschedule
shd_rtok
	bra.l	sms_rtok		 ; return ok

	end
