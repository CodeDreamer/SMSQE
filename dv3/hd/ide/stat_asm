; DV3 IDE wait for status      V3.00    1998 Tony Tebby

	section dv3

	xdef	id_statw

	xref.s	ideo.errr
	xref.s	ideo.stat
	
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_mac_assert'

;+++
; This routine waits for the status at the end of an IDE command
;
;	d0  r	error code
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;	a5 c  p base of register block
;
;	status return +ve IDE error
;		      -ve no status (time-out)
;			0 status ok
;---
id_statw
	move.l	hdl_1sec(a3),d0
	lsl.l	#2,d0			 ; wait 4 sec for run-up
ids_wait
	assert	ide..busy,7
	tst.b	ideo.stat(a5)		 ; busy?
	bpl.s	ids_done		 ; ... no, command done
	subq.l	#1,d0			 ; 4s wait
	bgt.s	ids_wait

	moveq	#err.mchk,d0
	rts

ids_done
	moveq	#1<<ide..err,d0 	 ; set OK or error
	and.b	ideo.stat(a5),d0
	beq.s	ids_rts
	lsl.w	#8,d0			 ; keep error
	move.b	ideo.errr(a5),d0	 ; add detail
	tst.l	d0
ids_rts
	rts

	end
