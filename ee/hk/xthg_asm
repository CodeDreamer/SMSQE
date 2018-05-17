; HOTKEY Execute Thing	V2.01	  1988   Tony Tebby   QJUMP
;
; 2003-03-11  2.01  Added hk_xthgid (MK)

	section hotkey

	xdef	hk_xthg
	xdef	hk_xthgid

	xref	hk_xname
	xref	gu_thexn

	include 'dev8_ee_hk_data'

;+++
; This routine executes a thing defined by a hotkey item
;
;	a1 c  p pointer to item
;	a3 c  p pointer to Hotkey linkage
;
;	status return standard
;---
hk_xthg
	move.l	d1,-(sp)
	bsr.s	hk_xthgid
	movem.l (sp)+,d1
	rts

;+++
; This routine executes a thing defined by a hotkey item
;
;	d1  r	job ID
;	a1 c  p pointer to item
;	a3 c  p pointer to Hotkey linkage
;
;	status return standard
;---
hk_xthgid
reglist reg	d2/a0/a1/a2
	movem.l reglist,-(sp)
	jsr	hk_xname			 ; expand executable item name

	moveq	#0,d1				 ; independent
	moveq	#32,d2				 ; standard priority
	swap	d2
	jsr	gu_thexn			 ; execute it

	movem.l (sp)+,reglist
	rts

	end
