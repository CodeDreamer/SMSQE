; Draw sub-window indices    V1.02     1986  Tony Tebby   QJUMP
; 2020-11-08  1.03  Added support for indexes drawing (AH)
;
	section wman
;
	xdef	wm_index
;
	xref	wm_drndx		new (AH)
	xref	wm_swdef
	xref	wm_drpns
	xref	wm_drbar

	include dev8_keys_wwork
	include dev8_keys_wdef

;+++
; Draw indexes, bars and arrows
; indexes drawing occurs only if bit #6 is set in the clear
; flag byte of sub-win attributes, otherwise do "as before"
;
;	d0  r	error return
;	a0 c  p channel ID of window
;	a3 c  p pointer to sub-window definition
;	a4 c  p pointer to working definition
;
;		all other registers preserved
;---

wwa..nfl equ	6			; indexes flag ... in bit 6
reglist  reg	d1-d7/a1-a4

wm_index

; new section here for indexes drawing (AH)
	btst	#wwa..nfl,wwa_watt+wwa_clfg(a3) ; check indexes flag
	beq.s	wix_asbef		; skip if unset
	bsr.l	wm_drndx		; draw indexes
; don't bother if error occured during drawing indexes
; end of new section (AH)

wix_asbef
	movem.l reglist,-(sp)		; save registers    MOVED by wl

	bsr.l	wm_drbar		; draw bars
	bsr.l	wm_swdef		; set sub window
	bne.s	wix_exit
	bsr.l	wm_drpns		; draw pan and scroll arrows

wix_exit
	tst.l	d0
	movem.l (sp)+,reglist		; restore registers
	rts

	end
