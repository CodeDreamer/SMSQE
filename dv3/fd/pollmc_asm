; DV3 Standard Floppy Polling Routine (with motor control)   1998 Tony Tebby

	section dv3

	xdef	fd_pollmc

	xref	fd_pflush
	xref	fd_start
	xref	fd_deselect

	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'

;+++
; Floppy Polling Interrupt Routine
;
; This provides the basic timing services for the Floppy disk system.
; In particular, it counts the disk runup time preventing write operations
; before the disk is runup, and it counts a delay before flushing the slave
; blocks and map.
;
; Explicit motor control version.
;
; If there is something to be flushed, and the motor is stopped, the motor
; is re-started. This is the only routine that de-selects the floppy disk
; drives. The run-up counter is reset when the drive is (re-)started. This
; is done whenever the drives status is stopped and an action is required.
;
; When there are no pending operations, the timer is used to stop the motor.
;
; In the following table, if the action count reaches its termination, then
; the action for "No count" (ACTM) will follow immediately rather than
; waiting for the next poll.
;
;  DRVS      MOTOR     ACTM	 FREQ		 Operation
;
;  Stopped   Stopped   No count  None		 None
;  Stopped   Stopped   No count  Required	 Start drive
;  Stopped   Stopped   Counting  None		 None
;  Stopped   Stopped   Counting  Required	 Start drive
;  Stopped   Stopped   End count None		 None
;  Stopped   Stopped   End count Required	 Start drive
;  Stopped   Stopped   Held	 None		 None
;  Stopped   Stopped   Held	 Required	 None
;
;  Run-up    On        No count  None		 set ACTM (should not happen)
;  Run-up    On        No count  Required	 set ACTM (should not happen)
;  Run-up    On        Counting  None		 Count ACTM slowly (to stop)
;  Run-up    On        Counting  Required	 Count ACTM quickly (to flush)
;  Run-up    On        End count None		 Stop drive
;  Run-up    On        End count Required	 Flush / clear FREQ / set ACTM
;  Run-up    On        Held	 None		 None
;  Run-up    On        Held	 Required	 None
;
;  Counting  On        No count  None		 Count run-up
;  Counting  On        No count  Required	 Count run-up
;  Counting  On        Counting  None		 Count run-up and ACTM
;  Counting  On        Counting  Required	 Count run-up and ACTM quickly
;  Counting  On        End count None		 Stop drive
;  Counting  On        End count Required	 Set count to 1 (action pending)
;  Counting  On        Held	 None		 Count run-up
;  Counting  On        Held	 Required	 Count run-up
;
;---
fd_pollmc
	tst.b	fdl_drvs(a3)		 ; stopped?
	bge.s	fdp_rnup		 ; ... no
	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_rts 		 ; ... no
	tst.b	fdl_actm(a3)		 ; suppressed?
	bge.l	fd_start		 ; ... no, startup
	rts

fdp_rnup
	beq.s	fdp_actn		 ; run-up
	subq.b	#1,fdl_drvs(a3) 	 ; running up
	beq.s	fdp_actn		 ; ... run up now

	move.b	fdl_actm(a3),d0 	 ; action counter to be decremented?
	ble.s	fdp_rts 		 ; ... no, held

	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_stop		 ; ... no, count down to stop

	subq.b	#fdl.actf,d0		 ; flush, decrement a bit quicker
	bgt.s	fdp_acts		 ; still counting
	moveq	#1,d0			 ; count stays at 1

fdp_acts
	move.b	d0,fdl_actm(a3) 	 ; decremented timer
fdp_rts
	rts

fdp_actn
	tst.b	fdl_actm(a3)		 ; suppressed?
	blt.s	fdp_rts 		 ; ... yes
	beq.s	fdp_actc		 ; ... no count at all, set it again

	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_stop		 ; ... no

	subq.b	#fdl.actf,fdl_actm(a3)	 ; countdown to flush
	bgt.s	fdp_rts 		 ; keep it running

	jsr	fd_pflush		 ; flush everything
	clr.b	fdl_freq(a3)		 ; get ready to stop

fdp_actc
	move.b	fdl_apnd(a3),fdl_actm(a3) ; and count
	rts

fdp_stop
	assert	fdl.acts,1
	subq.b	#fdl.acts,fdl_actm(a3)	 ; ... countdown to stop
	bgt.s	fdp_rts
	st	fdl_drvs(a3)		 ; yes, really
	sf	fdl_stpb(a3)		 ; force check
	jmp	fd_deselect

	end
