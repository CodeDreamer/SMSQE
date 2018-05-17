* Fetch Channel Name	  V2.00    1996  Tony Tebby
* SMSQ compatible version +
*
	section ioa
*
	xdef	ioa_cnam
*
	xref	io_ckchn
	xref	sms_rte
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_iod
	include 'dev8_mac_assert'

*
*	d2 c  p max length of string
*	a0 c  p channel ID
*	a1 c  p pointer to buffer for name >=d2+2
*
*	all other registers preserved
*
reglist reg	d1-d6/a0-a4

ioa_cnam
	movem.l reglist,-(sp)

	bsr.l	io_ckchn		 ; check the channel ID (a0,a3)
	bne.s	icn_exit

	cmp.l	#iod.sqio,iod_sqio(a3)	 ; upgraded device
	bne.s	icn_nimp		 ; ... no
	assert	iod..scn,18
	btst	#iod..scn,iod_sqfb+1(a3) ; channel name supported?
	beq.s	icn_nimp		 ; ... no

	move.l	iod_cnam(a3),a4
	jsr	(a4)			 ; fetch name

icn_exit
	movem.l (sp)+,reglist
	bra.l	sms_rte

icn_nimp
	moveq	#err.nimp,d0
	bra.s	icn_exit
	end
