* Set exception vector (per job) V2.00	  1986  Tony Tebby  QJUMP
*
	section qd
*
	xdef	qd_exv
*
	xref	sms_ckid
	xref	sms_rte
*
	include dev8_keys_jcbq
	include dev8_keys_sys
*
*	d1 cr	job id
*	a0   sp base of job
*	a1 c p	base of table
*
*	all other registers preserved
*
qd_exv
	bsr.l	sms_ckid		check id and set a0
	bne.s	qdx_exit
	sub.w	#$54,a1
	move.l	a1,jcb_exv(a0)		set vector
	move.l	a1,sys_ertb(a6)
	moveq	#0,d0			... ok
qdx_exit
	bra.l	sms_rte
	end
