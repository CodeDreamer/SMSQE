; Buffering (put multi-byte) utility  V2.00    1989  Tony Tebby   QJUMP

	section iou

	xdef	iob_pmul
	xdef	iob_pmul0

	xref	iob_abuf

	include 'dev8_keys_buf'
	include 'dev8_keys_err'
;+++
; This routine performs queued or dynamic buffering for byte serial output.
; This differs from simple queue handling in that the buffer / queue
; header is $20 bytes long and the end of buffer handling is done on
; entry rather than after the last byte is put in. This makes it easier
; to handle out of memory with the dynamic buffer.
;
; If A2 is modified, the pointer to the pointer to the buffer is updated
;
; This routine cannot be called from an interrupt server if the buffer is
; dynamically allocated.
;
; This is a clean routine.
;
;	d0  r	status: 0, or err.nc
;	d1  r  (long) byte sent
;	d2 c  p (long) max bytes to transfer
;	a1 c  u pointer to bytes to be buffered
;	a2 c  u pointer to buffer header (updated if new buffer allocated)
;	all other registers preserved
;--
iob_pmul0
	moveq	#0,d1
;+++
; This routine performs queued or dynamic buffering for byte serial output.
; This differs from simple queue handling in that the buffer / queue
; header is $20 bytes long and the end of buffer handling is done on
; entry rather than after the last byte is put in. This makes it easier
; to handle out of memory with the dynamic buffer.
;
; If A2 is modified, the pointer to the pointer to the buffer is updated
;
; This routine cannot be called from an interrupt server if the buffer is
; dynamically allocated.
;
; This is a clean routine.
;
;	d0  r	status: 0, or err.nc
;	d1 c  u (long) byte count
;	d2 c  p (long) max bytes to transfer (less call value of d1)
;	a1 c  u pointer to bytes to be buffered
;	a2 c  u pointer to buffer header (updated if new buffer allocated)
;	all other registers preserved
;--
iob_pmul
ibpm.reg reg	d3/a3/a4
	movem.l ibpm.reg,-(sp)
	move.l	buf_nxtp(a2),a3 	 ; next put
	tst.b	buf_nxtb(a2)		 ; is it dynamic?
	bge.s	ibpm_dyn

	lea	buf_strt(a2),a4 	 ; start of buffer

	cmp.l	buf_nxtg(a2),a3 	 ; fill up to get pointer or end?
	blt.s	ibpm_q2 		 ; ... up to get
	move.l	buf_endb(a2),d3 	 ; ... up to end
	cmp.l	buf_nxtg(a2),a4 	 ; is next get at start (= at end)?
	beq.s	ibpm_q2m		 ; ... yes, up to get

	sub.l	a3,d3
	move.l	d2,d0
	sub.l	d1,d0			 ; amount to transfer
	cmp.l	d3,d0			 ; enough room?
	ble.s	ibpm_q1a
	move.l	d3,d0
ibpm_q1a
	add.l	d0,d1			 ; this much more has gone
	bra.s	ibpm_q1e
ibpm_q1l
	move.b	(a1)+,(a3)+		 ; put byte
ibpm_q1e
	subq.l	#1,d0
	bge.s	ibpm_q1l

	move.l	d2,d0			 ; all gone?
	sub.l	d1,d0
	bne.s	ibpm_q2s		 ; ... no

	cmp.l	buf_endb(a2),a3 	 ; off end?
	blt.s	ibpm_done		 ; ... no
	move.l	a4,a3			 ; ... yes, start again
	bra.s	ibpm_done

ibpm_q2s
	move.l	a4,a3			 ; start again at beginning
ibpm_q2
	move.l	buf_nxtg(a2),d3 	 ; up to next get
ibpm_q2m
	subq.l	#1,d3			 ; but one
	sub.l	a3,d3			 ; amount of space
	blt.s	ibpm_nc 		 ; less than none

	move.l	d2,d0
	sub.l	d1,d0			 ; amount to transfer
	cmp.l	d3,d0			 ; enough room?
	ble.s	ibpm_q2a
	move.l	d3,d0
ibpm_q2a
	add.l	d0,d1			 ; this much more has gone
	bra.s	ibpm_q2e
ibpm_q2l
	move.b	(a1)+,(a3)+		 ; put byte
ibpm_q2e
	subq.l	#1,d0
	bge.s	ibpm_q2l

	move.l	d2,d0
	sub.l	d1,d0			 ; all gone?

ibpm_done
	move.l	a3,buf_nxtp(a2) 	 ; save pointer
	tst.l	d0
	beq.s	ibpm_exit		 ; ... yes
	bra.s	ibpm_nc 		 ; ... no, but we can do no more

ibpm_abuf
	jsr	iob_abuf		 ; allocate a new buffer
	bne.s	ibpm_nc 		 ; ... no memory left
ibpm_dyn
	move.l	buf_endb(a2),d3
	sub.l	a3,d3			 ; room left
	ble.s	ibpm_abuf		 ; ... none

	move.l	d2,d0
	sub.l	d1,d0			 ; amount to transfer
	cmp.l	d3,d0			 ; enough room?
	ble.s	ibpm_pbs
	move.l	d3,d0
ibpm_pbs
	add.l	d0,d1			 ; this much more has gone
	bra.s	ibpm_pbe
ibpm_pbl
	move.b	(a1)+,(a3)+		 ; put byte
ibpm_pbe
	subq.l	#1,d0
	bge.s	ibpm_pbl

	move.l	a3,buf_nxtp(a2) 	 ; save pointer
	move.l	d2,d0
	sub.l	d1,d0			 ; all gone?
	bgt.s	ibpm_abuf		 ; ... no, allocate another buffer

ibpm_exit
	movem.l  (sp)+,ibpm.reg
	rts

ibpm_nc
	moveq	#err.nc,d0
	bra.s	ibpm_exit
	end
