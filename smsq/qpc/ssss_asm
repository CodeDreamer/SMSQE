; QPC SMSQ Sampled Sound System  V2.00	  1999  Tony Tebby
;					   2004  Marcel Kilgus

	section ssss

	xdef	ssss_init

	xref	gu_achpp

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_proc'

ssss_flag   equ $00			; do not change order!
ssss_vec    equ $04
ssss_exv    equ $08
ssss_qstart equ $10
ssss_qin    equ $14
ssss_qout   equ $18
ssss_qend   equ $1c
ssss_qbase  equ $20
ssss.len    equ 2*100*1024
ssss_end    equ ssss_qbase+ssss.len

;+++
; SSSS initialisation.
;
;	a0-a3  scratch
;	status return standard
;---
ssss_init
	move.l	#ssss_end,d0		 ; allocate block
	jsr	gu_achpp
	bne.s	ssss_rts

	moveq	#sms.xtop,d0
	trap	#do.sms2

	lea	ssss_vector,a1
	move.l	(a1),ssss_flag(a0)	 ; flag
	move.l	a1,ssss_vec(a0) 	 ; facility vector

	lea	ssss_qbase(a0),a1
	move.l	a1,ssss_qstart(a0)
	move.l	a1,ssss_qin(a0)
	move.l	a1,ssss_qout(a0)
	add.l	#ssss.len,a1
	move.l	a1,ssss_qend(a0)

	lea	ssss_exv(a0),a1
	lea	ssss_isrv,a2
	move.l	(a2),(a1)		 ; just an RTE
	lea	exv_i4,a5
	move.l	a1,(a5)

	lea	ssss_qstart(a0),a1
	dc.w	qpc.ssbuf+1		 ; tell QPC of the buffer
	moveq	#0,d0
ssss_rts
	rts

; interrupt server

ssss_isrv
	rte
	nop

; utility vector

ssss_vector
	dc.l	'SSSS'
	bra.l	sss_add1
	bra.l	sss_setm
	bra.l	sss_addm
	bra.l	sss_kill
	bra.l	sss_sample

; add a sample

sss_add1
	move.l	ssss_qin(a3),a1
	move.b	d1,(a1)+		      ; put samples in
	move.b	d2,(a1)+
	cmp.l	ssss_qend(a3),a1	      ; end of queue?
	bne.s	sssa_updt
	lea	ssss_qbase(a3),a1
sssa_updt
	cmp.l	ssss_qout(a3),a1	      ; queue full?
	beq.s	sssa_rts		      ; ... yes
	move.l	a1,ssss_qin(a3) 	      ; ... no, update in pointer
	dc.w	qpc.ssupd
sssa_rts
	rts

; set up to add multiple samples

sss_setm
	move.l	ssss_qin(a3),a1 	      ; next in
	move.l	ssss_qout(a3),a2	      ; next out
	subq.w	#2,a2			      ; cannot fill completely
	cmp.l	a1,a2			      ; ... wrapped around?
	bge.s	sss_mrts		      ; ... no
	cmp.l	ssss_qstart(a3),a2	      ; is out at start?
	move.l	ssss_qend(a3),a2	      ; (does not smask condition codes)
	bge.s	sss_mrts
	subq.w	#2,a2			      ; ... yes, cannot fill right to end
sss_mrts
	rts

; add multiple samples

sss_addm
	cmp.l	ssss_qend(a3),a1	      ; end of queue
	blt.s	sss_aset		      ; ... no
	move.l	ssss_qstart(a3),ssss_qin(a3)  ; ... back to beginning
	dc.w	qpc.ssupd
	rts

sss_aset
	move.l	a1,ssss_qin(a3)
	dc.w	qpc.ssupd
	rts

; kill the sound

sss_kill
	movem.l d1/d2/a1/a2,-(sp)
	move.l	ssss_qin(a3),a2    ; the bytes will go here

	moveq	#$ffffff80,d1
	moveq	#$ffffff80,d2
sss_kloop
	bsr.s	sss_add1	   ; add zero
	beq.s	sss_kloop	   ; keep on until queue is not full

	move.l	a2,ssss_qout(a3)   ; one sample only in queue
	dc.w	qpc.skill
	movem.l (sp)+,d1/d2/a1/a2
	rts

; return length  of samples in queue

sss_sample
	move.l	ssss_qin(a3),d0
	sub.l	ssss_qout(a3),d0
	bpl.s	sss_sset
	add.l	ssss_qend(a3),d0
	sub.l	ssss_qstart(a3),d0
sss_sset
	lsr.l	#1,d0
	rts

	end
