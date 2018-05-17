; Window manager point to error string	 V0.00	  1986   Tony Tebby   QJUMP
; 2003 Jun 24				1.00	met_ebad no longer xref'd (wl)
	section wman

	xdef	wm_erstr


	include 'dev8_keys_qdos_sms'

err_1.02 equ	$387c
err_1.03 equ	$3894
err_late equ	$14a
err_nul  dc.w	0
;+++
; Convert error in d0 to string
;
;	d0 cr	error code
;	a1  r	pointer to error string
;	status return according to D0
;+++
wm_erstr
reglist  reg	d0/d1/d2/a0
stk_err  equ	$00

	movem.l reglist,-(sp)
	tst.l	d0			 ; any message
	bge.s	wme_none		 ; no

	moveq	#sms.info,d0		 ; find version and sysvar
	trap	#do.sms2

	lea	err_1.02,a1
	sub.l	#'1.02',d2		 ; version 1.02?
	beq.s	wme_set2		 ; yes
	blt.s	wme_badm		 ; no, earlier
	lea	err_1.03,a1
	subq.l	#1,d2			 ; version 1.03?
	beq.s	wme_set2		 ; yes
	
	move.l	err_late(a0),a1 	 ; late version, get pointer
	move.l	a1,a0
	bra.s	wme_set

wme_set2
	lea	-2(a1),a0		 ; early version structure
wme_set
	move.l	stk_err(sp),d0		 ; error code
	add.l	d0,d0			 ; doubled
	bvs.s	wme_mpoint		 ; pointer to message
	neg.l	d0
	move.w	(a0,d0.l),d0		 ; offset
	add.w	d0,a1			 ; actual message
	bra.s	wme_chek		 ; check it

wme_mpoint
	lsr.l	#1,d0			 ; error was pointer
	move.l	d0,a1

wme_chek
	cmp.w	#80,(a1)		 ; is message too long?
	bls.s	wme_exit		 ; no

wme_badm
	lea	met_ebad,a1
	bra.s	wme_exit

wme_none
	lea	err_nul,a1
       
wme_exit
	movem.l (sp)+,reglist
	tst.l	d0
	rts

met_ebad
	dc.w	met2-*-2
	dc.b	'an unknown eror occurred'
met2
	dc.w	0
	end
