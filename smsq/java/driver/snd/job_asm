; Soundfile jobs (2 & 3)	v. 1.00 (c) w. lenerz 2004


********************************************************************
; set up independant job to play the soundfile
; soundfile2 - that job belongs to caller
; soundfile3 - totally independent job
;
; SOUNDFILE "file_to_play"
;
; v. 1.00	2004 Nov 12 23:19:59
;
; copyright (c) w. lenerz 2004 - see the licence in the documentation
;
**********************************************************************

	section sound

	include dev8_keys_qdos_sms

	xdef	soundfile2
	xdef	soundfile3
	xdef	sndname

	xref	sf1
	xref	sf2
	xref	withbuff2

sndregs reg	a1-a4/d5
soundfile3
	moveq	#0,d7
	bra.s	common
soundfile2
	moveq	#-1,d7
common
	bsr	sf1
	bne.s	sf_out
	move.l	a0,a4			; pointer to name
	movem.l sndregs,-(a7)		; save regs
	sub.l	a1,a1			; start of job code
	move.l	d7,d1			; it will belong to me, or not
	move.l	#100,d2 		; code space required
	move.l	#800,d3 		; data (stack) space
	moveq	#1,d0
	trap	#1			; set up sndsnd job
	lea	-$10(a0),a5
	movem.l (a7)+,sndregs		; get old regs back
	moveq	#-1,d3
	lea	sndname,a1		; name of this job
	lea	sndjob,a2		; real start of this job
	move.w	#$4ef9,(a0)+
	move.l	a2,(a0)+		; make jump to our addresss
	move.w	#$4afb,(a0)+		; standard job marker
	moveq	#3,d2			; now copy job name
onlp1	move.l	(a1)+,(a0)+
	dbf	d2,onlp1
	move.l	a4,a1			; name pointer
	move.w	(a1)+,d0		; in job data
	move.l	a0,a4			; keep
	move.w	d0,(a0)+		; copy now
	bra.s	docplp
cplp	move.b	(a1)+,(a0)+
docplp	dbf	d0,cplp 		; copy name for job
	movem.l d2-d7/a0-a5,-(a5)	; give this job my registers now (!)
	moveq	#0,d3			; don't wait for completion
	moveq	#10,d2			; priority
	moveq	#10,d0
	trap	#1			; start that job
	tst.l	d0
sf_out	rts
sndname
	dc.w	sndname2-*-2
	dc.b	'SOUNDFILE JOB'
sndname2

; code for job itself
sndjob	move.l	a4,a0			; pointer to name
	jsr	sf2			; open channel & get vectors
	bne.s	sepukku 		; couldn't.
	jsr	withbuff2		; do the work
sepukku moveq	#-1,d1			: remove myself
	moveq	#0,d3
	moveq	#sms.frjb,d0
	trap	#1

	end
