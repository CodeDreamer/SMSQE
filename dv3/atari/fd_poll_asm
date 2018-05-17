; DV3 Atari Floppy Polling Routine	  1993     Tony Tebby

	section dv3

	xdef	fd_poll

	xref	fd_pflush
	xref	fd_selside0
	xref	fd_deselect
	xref	fd_fint

	include 'dev8_keys_sys'
	include 'dev8_keys_atari'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_mac_assert'

;+++
; Floppy Polling Interrupt Routine - Atari ST
;
; This provides the basic timing services for the Floppy disk system.
; In particular, it counts the disk runup time preventing write operations
; before the disk is runup, and it counts a delay before flushing the slave
; blocks and map.
;
; 1770/2 implicit motor control version.
;
; If there is something to be flushed, and the motor is stopped, the motor
; is re-started. This is the only routine that de-selects the floppy disk
; drives. The run-up counter is reset when the drive is (re-)started. This
; is done whenever the motor is detected as off or the status
; is stopped, and an action is required.
;
; When there are no pending operations, the timer is used to ensure that the
; motor stops. If it does not do this within 127 ticks, another drive is
; selected and the run-down timer is re-set.
;
; In the following table, if the action count reaches its termination, then
; the action for "No count" (ACTM) will follow immediately rather than
; waiting for the next poll.
;
;  DRVS      MOTOR     ACTM	 FREQ		 Operation
;
;  Stopped   No check  No count  None		 None
;  Stopped   No check  No count  Required	 Start drive
;  Stopped   No check  Counting  None		 None
;  Stopped   No check  Counting  Required	 Start drive
;  Stopped   No check  Held	 None		 None
;  Stopped   No check  Held	 Required	 None
;
;  Run-up    Off       No count  None		 Set Stopped / No count
;  Run-up    Off       No count  Required	 Re-start / reset run-up
;  Run-up    Off       Counting  None		 Set Stopped / No count
;  Run-up    Off       Counting  Required	 Count ACTM quickly (to flush)
;  Run-up    Off       Held	 None		 None
;  Run-up    Off       Held	 Required	 None
;
;  Run-up    On        No count  None		 Select other drive / set ACTM
;  Run-up    On        No count  Required	 Flush / clear FREQ / set ACTM
;  Run-up    On        Counting  None		 Count ACTM slowly (to stop)
;  Run-up    On        Counting  Required	 Count ACTM quickly (to flush)
;  Run-up    On        Held	 None		 None
;  Run-up    On        Held	 Required	 None
;
;  Counting  Off       No count  None		 Set Stopped / No count
;  Counting  Off       No count  Required	 Re-start / reset run-up
;  Counting  Off       Counting  None		 Set Stopped / No count
;  Counting  Off       Counting  Required	 Re-start / reset run-up
;  Counting  Off       Held	 None		 Count run-up
;  Counting  Off       Held	 Required	 Count run-up
;
;  Counting  On        No count  None		 Count run-up
;  Counting  On        No count  Required	 Count run-up
;  Counting  On        Counting  None		 Count run-up and ACTM
;  Counting  On        Counting  Required	 Count run-up and ACTM quickly
;  Counting  On        Held	 None		 Count run-up
;  Counting  On        Held	 Required	 Count run-up
;
;---
fd_poll
	tst.b	fdl_drvs(a3)		 ; stopped?
	bge.s	fdp_rnup		 ; ... no
	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_rts 		 ; ... no
	tst.b	sys_dmiu(a6)		 ; dma in use?
	bmi.s	fdp_rts 		 ; ... yes
	tst.b	fdl_actm(a3)		 ; suppressed?
	bge.s	fdp_start		 ; ... no, startup
fdp_rts
	rts

fdp_dchk
	move.w	#dma.fstt,dma_mode
	move.w	dma_data,d0
	assert	fds..mo,7
	not.b	d0			 ; is motor still running?
	rts

fdp_rnup
	beq.s	fdp_actn		 ; run-up
	subq.b	#1,fdl_drvs(a3) 	 ; running up

	tst.b	sys_dmiu(a6)		 ; dma in use?
	bmi.s	fdp_rts

	tst.b	fdl_actm(a3)		 ; suppressed?
	blt.s	fdp_rts 		 ; ... yes

	bsr.s	fdp_dchk		 ; drive still running?
	bmi.s	fdp_rstrt		 ; ... no

	move.b	fdl_actm(a3),d0 	 ; action counter to be decremented?
	ble.s	fdp_acts		 ; ... no
	subq.b	#fdl.acts,d0
	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_acts		 ; ... no
	subq.b	#fdl.actf-fdl.acts,d0	 ; ... yes, decrement a bit quicker
	bge.s	fdp_acts
	moveq	#0,d0			 ; no count now
fdp_acts
	move.b	d0,fdl_actm(a3) 	 ; decremented timer
	bra.s	fdp_run 		 ; keep it running

fdp_rstrt
	tst.b	fdl_freq(a3)		 ; do we need it running?
	beq.s	fdp_desl		 ; ... no

fdp_start
	sf	fdl_stpb(a3)		 ; ensure that we check drive again
	move.b	fdl_rnup(a3),fdl_drvs(a3) ; and wait a bit

fdp_run
	jmp	fd_fint 		; keep motor running



fdp_actn
	tst.b	sys_dmiu(a6)		 ; dma in use?
	bmi.s	fdp_rts 		 ; ... yes

	tst.b	fdl_actm(a3)		 ; suppressed?
	blt.s	fdp_rts 		 ; ... yes

	tst.b	fdl_freq(a3)		 ; flush required?
	beq.s	fdp_stop		 ; ... no

	bsr.s	fdp_dchk		 ; check drive running
	bmi.s	fdp_start		 ; ... no, start it

	subq.b	#fdl.actf,fdl_actm(a3)	 ; countdown to flush
	bgt.s	fdp_run 		 ; keep it running

	clr.b	fdl_actm(a3)		 ; no count now

	jsr	fd_pflush		; flush everything
	clr.b	fdl_freq(a3)		 ; get ready to stop
	move.b	fdl_apnd(a3),fdl_actm(a3) ; and count
fdp_rts1
	rts

fdp_stop
	bsr.l	fdp_dchk		 ; check drive motor sill running
	bmi.s	fdp_desl		 ; ... no, stop now

	subq.b	#fdl.acts,fdl_actm(a3)	 ; ... countdown to stop
	bgt.s	fdp_rts1

	move.b	fdl_apnd(a3),fdl_actm(a3) ; timed out, get ready to stop again
	subq.b	#1,fdl_selc(a3) 	 ; try other drive
	bgt.s	fdp_sel 		 ; OK try 1
	move.b	#2,fdl_selc(a3) 	 ; ... or 2
	bra.s	fdp_sel

fdp_desl
	sf	fdl_stpb(a3)		 ; force check
	clr.b	fdl_actm(a3)		 ; stopped now
	st	fdl_drvs(a3)		 ; yes, really
	jmp	fd_deselect		 ; no drive

fdp_sel
	sf	fdl_stpb(a3)		 ; force check
	moveq	#0,d7
	move.b	fdl_selc(a3),d7 	 ; select this
	jmp	fd_selside0		 ; select drive and side only

	end
