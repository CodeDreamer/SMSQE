; Procedure to execute programs    V2.03     1988   Tony Tebby   QJUMP
;
; 2003-01-18  2.01  added function fep	   pjwitte 2oo3
; 2003-03-11  2.02  Fixed launching of files, bug introduced in 2.01 (MK)
; 2006-03-27  2.03  Use system specific exec delay (MK)

	section hotkey

	xdef	exep
	xdef	fep

	xref	hot_parm
	xref	hot_ldit
	xref	hot_thus
	xref	hot_thfr

	xref	hk_xthgid		 ; ex hotkey, return job ID
	xref	hk_xfilid		 ; ex file, return job ID
					 
	xref	gu_achp0
	xref	gu_rchp
	xref	gu_exdelay
	xref	gu_pause

	xref	ut_rtfd1		 ; return float

	include 'dev8_ee_hk_data'
	include 'dev8_ee_hk_xhdr'

;+++
; jobID = FEP(filename |program name| |I| |G|P|U| |window|memory|)
;---
fep
	bsr.s	exep			 ; do a normal exep, then
	bne.s	ex_rts			 ; dont bother if error
	bra.l	ut_rtfd1		 ; return job ID as float

;+++
; EXEP filename |program name| |I| |G|P|U| |window|memory|
;---
exep
	jsr	hot_parm		 ; standard parameter set
	bne.s	ex_rts

	moveq	#hkh.plen+hki_name+4,d0  ; allocate heap item
	add.w	(a6,a1.l),d0		 ; including variable length file name
	jsr	gu_achp0
	bne.s	ex_rts

	jsr	hot_thus		 ; use hk
	bne.s	ex_rchp

	moveq	#hki.xthg,d6		 ; execute thing
	jsr	hot_ldit		 ; set up load item

	move.l	a0,a1			 ; item
	or.w	d2,d1			 ; any odd parameters?
	bne.s	ex_do			 ; ... yes

	jsr	hk_xthgid		 ; execut hotkey, return jobid

	beq.s	ex_pause		 ; sehr gut
ex_do
	jsr	hk_xfilid		 ; now try execute file

ex_pause
	jsr	hot_thfr		 ; free
	bne.s	ex_rchp 		 ; oops
;	 moveq	 #25,d0 		  ; half a second
	jsr	gu_exdelay		 ; get system specific delay
	jsr	gu_pause		 ; ... pause

ex_rchp
	jmp	gu_rchp 		 ; done
ex_rts
	rts

	end
