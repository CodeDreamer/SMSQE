; HOTKEY Execute File	 V2.01	   1988   Tony Tebby	QJUMP
;
; 2003-03-11  2.01  Added hk_xfilid (MK)

	section hotkey

	xdef	hk_xfil
	xdef	hk_xfilid

	xref	hk_dflts
	xref	hk_xname
	xref	gu_fexnm

	include 'dev8_ee_hk_data'
	include 'dev8_ee_hk_xhdr'

;+++
; This routine executes a file defined by a hotkey item
;
;	a1 c  p pointer to item
;	a3 c  p pointer to pseudo Hotkey linkage
;
;	status return standard
;---
hk_xfil
	move.l	d1,-(sp)
	bsr.s	hk_xfilid
	movem.l (sp)+,d1
	rts

;+++
; This routine executes a file defined by a hotkey item
;
;	d1  r	job ID
;	a1 c  p pointer to item
;	a3 c  p pointer to pseudo Hotkey linkage
;
;	status return standard
;---
hk_xfilid
reglist reg	d2/a0/a1/a2/a3/a4
	movem.l reglist,-(sp)
	pea	hkd_buf2(a3)			 ; default buffer

	lea	hki_name(a1),a4 		 ; name
	add.w	(a4)+,a4			 ; end of it
	move.l	a4,d0
	addq.l	#1,d0 
	bclr	#0,d0
	move.l	d0,a4				 ; rounded up is start address

	jsr	hk_xname			 ; expand executable item name

	moveq	#0,d1				 ; independent
	moveq	#32,d2				 ; standard priority
	swap	d2
	move.l	a4,a3				 ; start address
	lea	gu_fexnm,a4
	jsr	hk_dflts
	addq.l	#4,sp
	movem.l (sp)+,reglist
	rts

	end
