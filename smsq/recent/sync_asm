; SYNC extension for Recent thing itself  V1.00  (c) W. Lenerz 2015


; 2015-11-29	1.00	initial version

	section exten

	xdef	rc_sync

	xref	gu_thjmp		; thing helper routine
	xref	hash			; make a hash value

	include dev8_keys_thg
	include dev8_keys_err
	include dev8_keys_qdos_io
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_sms
	include dev8_keys_recent_thing
	include dev8_mac_thg


p_gtar	dc.w	thp.ulng,0


;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
;
; SYNC extension : tries to give current job IDs to jobs in heap
;
;	a2 c  p thing linkage block
;
;	return : OK unless error in job information trap
;
;+++
;
; parameter list:
; none
;
;++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
syncreg reg	d1-d4/a0/a1/a3/a4
rc_sync thg_extn {SYNC},,p_gtar
	movem.l syncreg,-(a7)
	moveq	#0,d1			; start with job 0
synclp	move.l	d1,d4			; keep ID of this job
	clr.l	d2
	moveq	#sms.injb,d0
	trap	#1			; get info on job, D1 = next job in tree
	tst.l	d0
	bne.s	syncout 		; ooops
	addq.l	#6,a0			; point to name marker
	cmp.w	#$4afb,(a0)+		; any name?
	bne.s	do_lp			; no
	tst.w	(a0)			; any name?
	beq.s	do_lp			; no
	move.l	a0,a3			; point to name
	move.l	d1,a4			: keep next job ID
	bsr	hash			; make hash
	move.l	d1,d2			; hash
	move.l	a4,d1			: get next job ID back
	move.l	rcnt_heap(a2),d0	; point to 1st heap
	beq.s	syncout 		; there is none, done
synlp2	move.l	d0,a1			; point to heap
	cmp.l	rcnt_hsh(a1),d2 	; same hash?
	bne.s	do_lp2			; no
	move.l	d4,rcnt_jbID(a1)	; yes, found job, set my job ID
	bra.s	do_lp			; no need to check other heaps
do_lp2	move.l	rcnt_next(a1),d0	; point to next heap
	bne.s	synlp2			; and check that, if any

do_lp	tst.l	d1			; is there a job ID for next job?
	bne.s	synclp			; yes, test that one
	clr.l	d0			; no, we're done
syncout movem.l (a7)+,syncreg
	rts

	end
