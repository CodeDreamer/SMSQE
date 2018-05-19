; SMSQmulator Sampled Sound System   v.2.03

; based on
; Q40 SMSQ Sampled Sound System  V2.00	  1999  Tony Tebby

; 2.04 better way to get the size of sample in the queue.
; 2.03 totally revamped. I don't use the pointer any more, really,
;      the data just gets shoved to java
; 2.02 2016 Oct 24  better way to kill the sound
; 2.01 adapted for SMSQmulator

	section sound

	xdef	ssss_init

	xref	gu_achpp
	xref	gu_rchp
	xref	snd_nam
	include 'dev8_keys_q40'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_proc'
	include 'dev8_keys_java'


ssss_qstart equ $10
ssss_qin    equ $14
ssss_qout   equ $18
ssss_qend   equ $1c
ssss_qbase  equ $20
ssss.len    equ 100*1024
ssss_end    equ ssss_qbase+ssss.len

;+++
; SSSS initialisation.
;
;	a0-a3  scratch
;	status return standard
;---
ssss_init
	move.l	#ssss_end,d0	       ; allocate a block for the queue
	move.l	d0,-(a7)
	jsr	gu_achpp
	bne.s	ssss_xrts
	move.l	(a7)+,d1
	move.l	a0,d0
	add.l	d1,d0
	subq.l	#8,d0
	move.l	#'java',(a0)+		;4
	move.l	d0,ssss_qend(a0)	; this is the end of the queue
	lea	ssss_qbase(a0),a2
	move.l	a2,ssss_qstart(a0)
	move.l	a2,ssss_qin(a0)
	move.l	a2,ssss_qout(a0)
	lea	ssss_vector,a3
	move.l	(a3),(a0)+		; ssss flag   8
	move.l	a3,(a0)+		; facility vector  12
	move.w	#$4e73,(a0)		; RTE	 14
	lea	exv_i4,a1
	move.l	(a1),-(a7)		; keep old interrupt vector in case of error
	move.l	a0,(a1)
	move.l	d0,a1			; end of queue; a2 = start of queue
	moveq	#jt5.sssi,d0
	dc.w	jva.trp5		; link into sound interface in java
	tst.l	d0			; OK?...
	bne.s	ssss_err		; ... no
	addq.l	#4,a7
	jmp	snd_nam 		; link in some sbasic procs
ssss_xrts
	addq.l	#4,a7
	rts


ssss_err
	move.l	(a7)+,(a1)		: error, reset old interrupt vector
	sub.l	#12,a0			; unlink heap
	jmp	gu_rchp
	    
; utility vector

ssss_vector
	dc.l	'SSSS'			; 0
	bra.l	sss_add1		; 4
	bra.l	sss_setm		; 8
	bra.l	sss_addm		; 12
	bra.l	sss_kill		; 16
	bra.l	sss_sample		; 20
	bra.l	sss_close		; 24

    
	    
; set up to add multiple samples.
; on return :
; A1 = start of queue and A2 = end of queue in return
sss_setm
	move.l	ssss_qin(a3),a1 	      ; start of queue
	move.l	ssss_qend(a3),a2	      ; end of queue
sss_mrts
	rts
    
; add a sample
; D1 & D2.b have the samples
sss_add1
	move.l	ssss_qin(a3),a1
	move.b	d1,(a1)+		      ; put samples in
	move.b	d2,(a1)+		      ; fall through

; add multiple samples
; A1 points to end of samples
sss_addm
cont	move.l	d0,-(a7)
	moveq	#jt5.sssa,d0
	dc.w	jva.trp5
	move.l	(a7)+,d0
	rts


; kill the sound : reset all pointers & tell java
sss_kill
	moveq	#jt5.sssk,d0
	dc.w	jva.trp5
	rts


; return length of samples in queue
sss_sample
	moveq	#jt5.sspl,d0
	dc.w	jva.trp5
	rts


; tell java that no more bytes are forthcoming
sss_close
	move.l	d0,-(a7)
	moveq	#jt5.sssc,d0
	dc.w	jva.trp5
	move.l	(a7)+,d0
	rts

	end
