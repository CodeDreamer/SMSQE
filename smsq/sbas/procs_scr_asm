; SBAS_PROCS_SCR - SBASIC SCR Information  V2.00    1994  Tony Tebby

	section exten

	xdef	scr_base
	xdef	scr_llen
	xdef	scr_xlim
	xdef	scr_ylim

	xref	ut_chan1
	xref	ut_rtfd1

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_con'

;+++
; SCR_BASE
;---
scr_base
	moveq	#pt_sbase/2,d7
	moveq	#-1,d5
	bra.s	scr_do
;+++
; SCR_LLEN
;---
scr_llen
	moveq	#pt_bytel/2-1,d7
	bra.s	scr_dow
;+++
; SCR_XLIM
;---
scr_xlim
	moveq	#pt_ssizx/2-1,d7
	bra.s	scr_dow
;+++
; SCR_YLIM
;---
scr_ylim
	moveq	#pt_ssizy/2-1,d7

scr_dow
	moveq	#0,d5
	not.w	d5
scr_do
	add.w	d7,d7			 ; true index
	jsr	ut_chan1		 ; try 1,0 etc.
	bne.s	scr_rts
	moveq	#0,d1
	moveq	#0,d2
	moveq	#-1,d3
	moveq	#iop.slnk,d0		 ; find linkage
	trap	#3
	tst.l	d0
	bne.s	scr_rts

	move.l	(a1,d7.w),d1		 ; value to return
	and.l	d5,d1
	jmp	ut_rtfd1

scr_rts
	rts
	end
