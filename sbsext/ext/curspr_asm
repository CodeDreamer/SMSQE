;	sprite cursor per job control	(c) W. Lenerz 2004
; 2004-03-29	1.00	first version, set sprite on per job basis
; 2004-04-02	1.01	CURSPRLOAD introduced
; 2004-04-07	1.02	SYSSPRLOAD introduced

*****************************************
*
*	CURSPRON name or job,tag
*	CURSPROFF name or job,tag
*
*	set sprite cursor on/off for that job
*
****************************************
	section exten

	xref	job_gid
	xref	file_in
	xref	ut_fclos
	xref	ut_trap3
	xref	ut_gtin1

	xdef	curspron
	xdef	cursproff
	xdef	cursprload
	xdef	syssprload

	include 'DEV8_keys_sys'
	include 'DEV8_keys_con'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_chn'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sysspr'
	include 'DEV8_keys_sbasic'
	include 'DEV8_keys_err'


cs_err
	moveq	#err.ipar,d0
cs_out2
	rts

curspron
	moveq	#1,d7			; switch ON
	bra.s	curspr
cursproff
	moveq	#0,d7			; switch OFF
curspr
	moveq	#4,d5			; get 1/2 params
	jsr	job_gid 		; get jobID passed as parameter
	bne.s	cs_out2 		; oops
	move.l	d1,d4
	moveq	#sms.info,d0
	trap	#1			; get sysvars
	move.l	d7,d2			; key
	move.l	d4,d1			; job ID
	move.l	sys_clnk(a0),a3 	; pointer to con linkage block
	move.l	pt_vecs(a3),a1		; pointer to con vector
	jmp	pv_cursp(a1)		; do it now
	      
*************************************************
*
* SYSSPRLOAD sys_spr_nbr, filename
* load this filename as the given system sprite. No check is made whether this
* is a valid sprite!
*
* v. 1.00
********************************************
			 
syssprload
	move.l	a5,d0
	sub.l	a3,d0
	sub.l	#16,d0
	bne.s	cs_err
	jsr	ut_gtin1		; get one int
	bne.s	cs_out2
	move.w	(a6,a1.l),d7		; keep sysprite number
	addq.l	#2,sb_arthp(a6)
	addq.l	#8,a3			; jump over first parameter
	bra.s	sprcom
*************************************************
*
* CURSPRLOAD filename
* load this filename as cursor sprite. No check is made whether this is
* a valid cursor sprite!
*
* v. 1.00
********************************************
		 
cursprload
	moveq	#sp.cursor,d7		; sprite to change

sprcom
	moveq	#0,d0			; no parameters after name
	jsr	file_in 		; open input file and read header
	bne.s	cs_out2 		; ... oops
lr_achp
	moveq	#sms.achp,d0		; allocate in heap
	move.l	(a6,a1.l),d1		; for file length
	moveq	#0,d2			; for job 0 (systemwide cursor!)
	movem.l d1/a0,-(sp)		; save length and channel
	trap	#1
	move.l	a0,a1			; start in a1
	movem.l (sp)+,d2/a0		; channel in a0, length in d2
	tst.l	d0
	bne.l	ut_fclos		; ... oops
	move.l	a1,a2			; save start
	moveq	#iof.load,d0
	bsr.l	ut_trap3		; load file
	bsr.l	ut_fclos		; and close
	tst.l	d0
	bne.s	cs_out2 		; oops, error loading file
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_clnk(a0),a3 	; CON linkage block
	moveq	#sp.cursor,d1		; set this sprite
	move.l	a2,a1
	move.l	pt_vecs(a3),a2		; point to vectors
	jmp	pv_sspr(a2)		; do it now


	end
