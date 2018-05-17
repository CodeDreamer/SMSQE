* QDOS Trap #4 Emulation   V2.00    1986  Tony Tebby	QJUMP
*
	section qd
*
	xdef	qd_trap4

	include 'dev8_keys_jcbq'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'

*
* set primary stack frame and locate system variable base
*
qd_trap4
	move.l	a6,-(sp)		 ; save special reg set
	move.l	sms.sysb,a6		 ; system variable base
	move.l	sys_jbpt(a6),a6 	 ; get current job
	move.l	(a6),a6
	st	jcb_rela(a6)		 ; mark relative address
	move.l	(sp)+,a6
	rte
	end
