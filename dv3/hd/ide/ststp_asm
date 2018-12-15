; DV3 IDE Start / Stop Drive   V3.00	 1998	   Tony Tebby

	section dv3

	xdef	id_ststp		; start / stop drive

	xref	id_cmdw

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'

;+++
; This routine starts or stops an IDE drive
;
;	d0 cr start (-1), stop ($ffff) rundown (0+ minutes)/ error
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_ststp
idss.reg reg	d1/d2/a5
	movem.l idss.reg,-(sp)
	moveq	#1,d1			 ; set valid address
	moveq	#1,d2			 ; one sector!!

	tst.l	d0			 ; start or stop
	bmi.s	idss_start		 ; start
	tst.w	d0			 ; stop immediate?
	bmi.s	idss_stop		 ; ... yes

	moveq	#60/5,d2		 ; 5 second units
	mulu	d0,d2			 ; timer in number of sectors
	cmp.w	#240,d2 		 ; in range
	bls.s	idss_rund		 ; ... yes
	move.w	#240,d2

idss_rund
	moveq	#ide.rund,d0
	bra.s	idss_cmd

idss_stop
	moveq	#ide.stop,d0
	bra.s	idss_cmd

idss_start
	moveq	#ide.idle,d0		 ; idle to start drive

idss_cmd
	jsr	id_cmdw


idss_exit
	movem.l (sp)+,idss.reg
	moveq	#0,d0
	rts

	end
