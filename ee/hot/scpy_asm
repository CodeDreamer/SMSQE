; Procedure to copy string (a6,a1)  V2.00     1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hot_scpy

	include 'dev8_keys_err'
	include 'dev8_ee_hk_data'

;+++
; String at (a6,a1.l) is copied to hkd_buf2 then a1 set to point to hkd_buf2
;
;	a1 cr	string pointer, called rel a6, returned absolute
;	a3 c  p hotkey linkage block
;	status returns 0 or err.inam
;---
hot_scpy
regs	setstr	{a0}
	move.l	[regs],-(sp)
	move.w	(a6,a1.l),d0		 ; length of string
	cmp.w	#hkd.bufl-4,d0		 ; too long?
	bhi.s	hsc_inam		 ; ... yes

	lea	hkd_buf2(a3),a0 	 ; into buffer
	move.w	d0,(a0)+		 ; set length
hsc_loop
	move.b	2(a6,a1.l),(a0)+	 ; copy characters
	addq.l	#1,a1
	subq.w	#1,d0
	bgt.s	hsc_loop

	lea	hkd_buf2(a3),a1 	 ; point to copy of string
	moveq	#0,d0
hsc_exit
	move.l	(sp)+,[regs]
	rts
hsc_inam
	moveq	#err.inam,d0
	bra.s	hsc_exit
	end
