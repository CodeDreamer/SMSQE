; Wait for SCSI status	    V2.00    1990   QJUMP

	section dv3

	xdef	sc_statrd
	xdef	sc_statwr

	xref	sc_statr
	xref	sc_nmode

	xref	sc_wait80

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_tt'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; This routine	reads the command status (read ops)
;
;	d0  r	error code
;	a3 c  p winchester linkage
;
;	status return 0 OK, ERR.NC or ERR.MCHK or >0 scsi error
;
;---
sc_statrd
	bsr.s	sc_stat 		 ; wait for completion
	bne.s	scs_rts
	move.l	a2,-(sp)
	lea	scd_addr,a2
	movep.l 0(a2),d0		 ; get address
	move.l	d0,a2
	and.w	#3,d0			 ; "restbytes"
	beq.s	scsr_rstat		 ; none
	neg.w	d0
	move.w	scd_rstu,(a2,d0.w)	 ; bytes 0 and 1
	addq.w	#2,d0
	bge.s	scsr_rstat
	move.w	scd_rstl,(a2,d0.w)	 ; byte 2 and rubbish
scsr_rstat
	move.l	(sp)+,a2
	jmp	sc_statr		 ; ... not ok

;+++
; This routine	reads the command status (write ops)
;
;	d0  r	error code
;	a3 c  p winchester linkage
;
;	status return 0 OK, ERR.NC or ERR.MCHK or >0 scsi error
;
;---
sc_statwr
	bsr.s	sc_stat 		 ; wait for completion
	beq.l	sc_statr		 ; OK, read status

scs_rts
	rts

;+++
; This routine waits for interrupt at end of operation.
;
;	a3 c  p pointer to linkage block
;
;	status return standard 0 or ERR.NC
;
;---
sc_stat
	move.w	#250,d0 		 ; wait 250*20 == 5secs
scs_stttim
	swap	d0
	move.b	#239,d0 		 ; wait 240*83 == 20ms
	jsr	sc_wait80

scs_sttw
	btst	#scs..irq,scs_irq	 ; for 5380 interrupt
	bne.s	scs_ok
	btst	#scs..ber,scs_ber	 ; or DMA bus error!!!
	beq.s	scs_berr

	btst	#mfp..tai,at_mfp2+mfp_pnda ; or timeout
	beq.s	scs_sttw
	swap	d0
	dbra	d0,scs_stttim		 ; and again

; pea to
; xref sc_fail
; jsr sc_fail

scs_err
	moveq	#1,d0
	jmp	sc_nmode

scs_ok
	moveq	#0,d0
	rts

scs_berr
	btst	#scd..ber,scd_stat	 ; really bus error?
	beq.s	scs_sttw		 ; ... no
; pea be
; jsr sc_fail
	bra.s	scs_err

;tl dc.b 0,15,' length error',$d,$a,0
;to dc.b 0,10,' timeout',$d,$a
;be dc.b 0,16,' DMA bus error',$d,$a

	end
