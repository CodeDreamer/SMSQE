; Q40 SMSQ Sampled Sound System  V2.00	  1999  Tony Tebby

	section ssss

	xdef	ssss_init

	xref	q40_insta0
	xref	ut_procdef
	xref	ut_gxli1
	xref	gu_achpp

	include 'dev8_keys_q40'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_proc'

ssss_i2     equ $60
ssss_i2rate equ $64

ssss_qstart equ $70
ssss_qin    equ $74
ssss_qout   equ $78
ssss_qend   equ $7c
ssss_qbase  equ $80
ssss.len    equ 2*100*1024
ssss_end    equ ssss_qbase+ssss.len

ssss_procs
	proc_stt
	proc_ref I2POLL
	proc_end
	proc_stt
	proc_end

i2poll
	jsr	ut_gxli1		 ; get a long
	bne.s	itp_rts
	move.l	exv_i4,a5
	move.l	(a6,a1.l),ssss_i2rate-ssss_entry+ssss_isrv(a5) set rate
	move.l	(a6,a1.l),ssss_i2-ssss_entry+ssss_isrv(a5) init counter
itp_rts
	rts


;+++
; SSSS initialisation.
;
;	a0-a3  scratch
;	status return standard
;---
ssss_init
	lea	ssss_procs,a1
	jsr	ut_procdef


	move.l	#ssss_end,d0		 ; allocate block
	jsr	gu_achpp
	bne.s	ssss_rts

	moveq	#sms.xtop,d0
	trap	#do.sms2

	lea	ssss_isrv,a1
	move.l	(a1)+,(a0)+		 ; flag

	lea	ssss_vector,a2
	move.l	a2,(a0)+		 ; facility vector

	move.l	(a1)+,d0

ssss_copy
	move.w	(a1)+,(a0)+
	subq.w	#2,d0
	bgt.s	ssss_copy

	add.w	#ssss_qbase-(ssss_isrvend-ssss_isrv),a0
	move.l	a0,a1
	move.l	a1,-(a0)		 ; set end
	add.l	#ssss.len,(a0)
	move.l	a1,-(a0)		 ; and pointers
	move.l	a1,-(a0)
	move.l	a1,-(a0)		 ; including start

	lea	exv_i4,a5
	lea	ssss_entry-ssss_isrv-ssss_qstart(a0),a0

	bsr.s	ssss_insta0		 ; install one vector (4)
	bsr.s	ssss_insta0		 ; install one vector (5)
	bsr.s	ssss_insta0		 ; install one vector (6)
	bsr.s	ssss_insta0		 ; install one vector (7)

	st	q40_50uie		 ; enable 50us interrupt

	moveq	#0,d0

ssss_rts
	rts

ssss_insta0
	jmp	q40_insta0


; interrupt server

ssss_isrv
	dc.l	'SSSS'			     ; 4   flag
	dc.l	ssss_isrvend-ssss_entry      ; 4   will be address of vector
ssss_entry
	move.l	a3,-(sp)		     ; 2
	move.l	a2,-(sp)		     ; 2
	lea	ssss_qout+ssss_isrv(pc),a2   ; 4
	st	q40_50uack		     ; 6
	move.l	(a2),a3 		     ; 2   next out
	cmp.l	ssss_qin-ssss_qout(a2),a3    ; 4   any?
	beq.s	ssss_nsound		     ; 2   ... none
	move.b	(a3)+,q40_dacl		     ; 6   set sound
	move.b	(a3)+,q40_dacr		     ; 6
	cmp.l	ssss_qend-ssss_qout(a2),a3   ; 4
	bne.s	ssss_done		     ; 2
	lea	ssss_qbase-ssss_qout(a2),a3  ; 4
ssss_done
	move.l	a3,(a2) 		     ; 2
ssss_nsound
	subq.l	#1,ssss_i2-ssss_qout(a2)     ; 4   pseudo int2 time?
	bne.s	ssss_exit		     ; 2
	move.l	ssss_i2rate-ssss_qout(a2),ssss_i2-ssss_qout(a2) ; 6  reset timer
	cmp.b	#$22,8(sp)		     ; 6  interrupted level 2 server?
	bhs.s	ssss_exit		     ; 2  ... yes
	move.l	(sp)+,a2		     ; 2
	move.l	(sp)+,a3		     ; 2
	move.l	exv_i2,-(sp)		     ; 4
	move.w	#$2300,sr		     ; 4 re-enable int4
	rts				     ; 2

ssss_exit
	move.l	(sp)+,a2		     ; 2
	move.l	(sp)+,a3		     ; 2
ssss_rte
	rte				     ; 2 = 88
ssss_isrvend

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
	rts

sss_aset
	move.l	a1,ssss_qin(a3)
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
