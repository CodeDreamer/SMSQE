; Procedure do HOTKEY action   V2.00	 1988	 Tony Tebby   QJUMP

	section hotkey

	xdef	hot_thar
	xdef	hot_thact

	xref	hot_scpy
	xref	hot_thus
	xref	hot_thfr

	include 'dev8_ee_hk_data'

;+++
; Do hotkey action: parameters d0,d1,d2 are passed to routine and
; returned; string at (a6,a1.l) is copied to hkd_buf2 and a1 set to point to it.
;
;	a2 c  p address of hotkey action routine 
;	status returns standard
;---
hot_thar
regs	setstr	{a3}
frame	equ	4
	move.l	[regs],-(sp)
	move.l	d0,-(sp)

	jsr	hot_thus		 ; use thing
	bne.s	hta_exit

	jsr	hot_scpy		 ; copy string (a6,a1.l)
	lea	hkd_buf1(a3),a0 	 ; set a0
	bne.s	hta_thfr
	bra.s	hta_do

;+++
; Do hotkey action: parameters d0,d1,d2,a0,a1 passed to routine and
; returned.
;	d0/d1/d2/a0/a1 call return parameters
;	a2 c  p address of hotkey action routine 
;	status returns standard
;---
hot_thact
	move.l	[regs],-(sp)
	move.l	d0,-(sp)

	jsr	hot_thus		 ; use thing
	bne.s	hta_exit

hta_do
	move.l	(sp),d0 		 ; set d0
	jsr	(a2)			 ; and do action

hta_thfr
	jsr	hot_thfr		 ; free thing
hta_exit
	addq.l	#frame,sp		 ; remove d0
	move.l	(sp)+,[regs]
	rts
	end
