; Wait for ACSI status	    V2.00    1990   QJUMP

	section dv3

	xdef	ac_stat
	xdef	ac_statd

	xref	at_stat
	xref	at_stata
	xref	ac_statr
	xref	ac_reset

	include 'dev8_keys_sys'
	include 'dev8_keys_err'

;+++
; This routine checks the DMA address and reads the command status
;
;	d0  r	error code
;	a1 c  p end of DMA address
;	a3 c  p winchester linkage
;
;	status return 0 OK, ERR.NC or ERR.MCHK or >0 scsi error
;
;---
ac_statd
	jsr	at_stata		 ; check completion and DMA address
	bgt.s	acs_rset		 ; ... oops
	beq.l	ac_statr		 ; ok
	jsr	ac_statr		 ; ... not ok
	bne.s	acs_rts 		 ; ... for this reason
acs_mchk
	moveq	#err.mchk,d0		 ; there must be some reason
acs_rts
	rts
;+++
; This routine waits for the command status and reads it.
;
;	d0  r	error code
;	a3 c  p winchester linkage
;
;	status return 0 OK, ERR.NC or ERR.MCHK or >0 scsi error
;
;---
ac_stat
	jsr	at_stat 		 ; wait for status
	beq.l	ac_statr
acs_rset
	jmp	ac_reset		 ; ... oops
	end
