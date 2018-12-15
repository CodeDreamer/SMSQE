; Interrupt 2 handler	V2.11	 1999	Tony Tebby
;
; 2018-11-22 v. 2.11 correct nbr of ticks (49, not 50) for clock counter (wl)

	section int

	xdef	q40_int2

	xdef	hw_poll

	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_psf'
	include 'dev8_keys_q40'
	include 'dev8_keys_q40_multiio'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_creg'

int2.reg  reg	  d0-d2/d6/d7/a1-a6
int2.rest reg	  d0-d2/d6/d7/a1-a4
int2.psf  reg	  d7


q40_int2
	dc.w	int2_entry-*
	dc.w	int2_end-*

;+++
; Server entry code (nearly standard)
;---
int2_entry
	dc.w	int2_table-int2_entry-2
	movem.l int2.reg,-(sp)		 ; 4
	lea	int2_table(pc),a5	 ; 4
	move.l	sms.sysb,a6		 ; 4
	moveq	#0,d6			 ; 2  (should not be required - see exit code)
	move.l	(a5)+,a4		 ; 2
	move.l	(a5)+,a3		 ; 2
	jmp	(a4)			 ; 2 = 20
int2_table

;+++
; Server end of list code (non standard)
; Instead of simply looping, this "end of list" routine is a real server itself.
; It first checks the frame interrupt.
; If there is then no pending interrupt, it exits. Otherwise it loops.
;---
int2_end
	moveq	#1<<q40..kbd+1<<q40..ser,d0    ;;;; +1<<q40..eint,d0 no ext
	and.b	q40_ir,d0
	or.b	d6,d0
	beq.s	int2_pcheck		 ; no interrupts left, do poll if req

	move.l	a3,a5			 ; re-start at the beginning
	moveq	#0,d6
	move.l	(a5)+,a4
	move.l	(a5)+,a3
	jmp	(a4)

int2_pcheck
	btst	#q40..frame,q40_ir	 ; frame interrupt?
	bne.s	int2_poll

int2_rte
	movem.l (sp)+,int2.reg		 ; restore regs
	rte


;+++
; All 'real' work has been done, if there has been a frame interrupt,
; this routine does the pre-conditionning for the polling interrupt handler
; and also maintains the real time clock register.
;---
int2_poll
	st	q40_fack		 ;  clear frame interrupt
	subq.w	#1,sys_rtcf(a6) 	 ; 50 Hz counter
	bpl.s	int2_dopoll		 ; ... ok
	move.w	#49,sys_rtcf(a6)	 ; and count down again
	addq.l	#1,sys_rtc(a6)		 ; another second gone

int2_dopoll
	addq.w	#1,sys_pict(a6) 	 ; one more poll

	tas	sys_50i(a6)		 ; is 50 Hz in service?
	bne.s	int2_rte		 ; ... yes

	movem.l (sp)+,int2.rest
	move.l	d7,-(sp)		 ; set stack frame
	and.w	#$f8ff,sr		 ; restore interrupts
	move.l	sms.spoll,a5
	jmp	(a5)

;+++
; HW_POLL is fairly dummy
;---
hw_poll
	cpushd				 ; push data (tidies up the screen)
	sf	sys_50i(a6)		 ; clear 50 Hz in use
	rts

	end
