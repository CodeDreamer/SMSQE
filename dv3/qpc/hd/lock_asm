; DV3 QPC Lock/Unlock Drive   V3.00     1998	  Tony Tebby
;				        2000	  Marcel Kilgus
;
; 2017-11-26  3.01  Allow driver number to only be a byte, not word (MK)

	section dv3

	xdef	hd_lock 		; lock drive
	xdef	hd_unlock		; unlock drive

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_smsq_qpc_keys'

;+++
; This routine locks a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_lock
  ; save a5!!
	dc.w	qpc.hdlck
	moveq	#0,d0
	rts

;+++
; This routine unlocks a drive
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_unlock
	move.l	a2,-(a7)
	andi.w	#$ff,d7
	lea	hdl_remd(a3),a2
	tst.b	-1(a2,d7.w)		; removeable?
	beq.s	hd_exit 		; ... no
	dc.w	qpc.hunlk
hd_exit
	move.l	(a7)+,a2
	moveq	#0,d0
	rts

	end
