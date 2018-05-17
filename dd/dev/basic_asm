; DEV_USE/DEV_USE$/DEV_NEXT    V2.00	 1989	Tony Tebby  QJUMP

	section exten

	xref	ut_gtint

	include 'dev8_mac_basic'

	proc_thg DEV
	fun40_thg DEV

	xdef	dev_use
dev_use
	moveq	#8,d0
	add.l	a3,d0
	sub.l	a5,d0		; more one parameter
	blt.s	dev_used	; ... yes n, name, next
	bgt.s	dev_usen	; no parameter

	jsr	ut_gtint	; get exactly one int
	bne.s	dev_usen	; ... not an int, use string
	tst.w	(a6,a1.l)	; set non-zero?
	beq.s	dev_usen

dev_used
	  proc	{USE }
dev_usen  proc	{USEN}
dev_list  proc	{LIST}
dev_use$  fun40 {USE$}
dev_next  fun40 {NEXT}

	end
