; Check type of sprite or blob		       V2.02   1999 Tony Tebby
;
; 2002-02-17  2.02  Added check to handle system sprites (MK)
;
;	This routine ensures that sprites and blobs/patterns are in
;	in an appropriate mode.
;
	section driver
;
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_con'

;
	xdef	pt_chkbp
	xdef	pt_chksp

	xref	pt_ssref
	xref	pt_cchloc

;+++
; Check blob / pattern
;
;	Registers:
;		Entry				Exit
;	A0	pointer to cdb			preserved
;	A1	pointer to blob 		pointer to right mode object
;	A2	pointer to pattern		pointer to right mode object
;---
pt_chkbp
	move.l	d2,-(sp)
	exg	a1,a2			 ; check pattern
	moveq	#0,d0			 ; check pattern  / mask
	jsr	pt_cchloc
	exg	a1,a2			 ; now check blob
	moveq	#2,d0			 ; check blob
	jsr	pt_cchloc
	move.l	(sp)+,d2
	moveq	#0,d0
	rts

;+++
;	Check one object (a sprite)
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	A0	pointer to cdb			preserved
;	A1	pointer to object		pointer to right mode object
;---
pt_chksp
	move.l	d2,-(sp)
	jsr	pt_ssref
	moveq	#0,d0			 ; check sprite
	jsr	pt_cchloc
	move.l	(sp)+,d2
	moveq	#0,d0
	rts

	end
