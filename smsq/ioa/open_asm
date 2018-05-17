* Open device or file	  V2.03    1994  Tony Tebby
* QDOS compatible version +
*
	section ioa
*
	xdef	ioa_open
	xdef	ioa_delf
*
	xref	ioa_opfl
	xref	sms_ckid
	xref	sms_rte
*
	include dev8_keys_err
	include dev8_keys_sys
	include dev8_keys_chn
	include dev8_keys_iod
	include 'dev8_mac_assert'

*
*	d1 cr	job ID
*	d3 c  p additional key
*	a0 cr	pointer to name / channel id
*
*	all other registers preserved
*
reglist reg	d2-d6/a1-a4
stk_d1	equ	0

ioa_delf
	movem.l reglist,-(sp)
	move.l	a0,a1			save pointer to name
	moveq	#-1,d3

	lea	sys_iodl(a6),a5 	device list
	bra.s	iop_ddeve
iop_ddev
	move.l	d0,a5			next device
	cmp.l	#iod.sqio,iod_sqio-iod_iolk(a5) ; upgraded device?
	bne.s	iop_ddeve			; ... no
	assert	iod..sdl,8
	btst	#iod..sdl,iod_sqfb+2-iod_iolk(a5) ; delete supported?
	beq.s	iop_ddeve			; ... no

	lea	-iod_iolk(a5),a3	set linkage base
	move.l	iod_open(a3),a4 	and address of open routine
	movem.l d3/d7/a1/a2/a5,-(sp)	save the registers we need for this loop
	move.l	a1,a0			set pointer to name
; d7 is channel ID
; a0 is pointer to name
; a2 is pointer to table
; a3 is linkage
	jsr	(a4)
	movem.l (sp)+,d3/d7/a1/a2/a5
	tst.l	d0			found and done?
	beq.s	iop_exit1		 ... yes
	cmp.w	#err.fdnf,d0		not found?
	bne.s	iop_exit1		 ... no
iop_ddeve
	move.l	(a5),d0 		another device?
	bne.s	iop_ddev		... yes

	pea	iop_exit
	jmp	ioa_opfl		try deleting a file system device


ioa_open
	movem.l reglist,-(sp)
	move.l	a0,a1			save pointer to name
	bsr.l	sms_ckid		check job ID
	bne.l	iop_exit		... oops
*
* look for empty slot in table
*
iop_fchp
	move.l	sys_chtb(a6),a2 	search channel table for empty entry
	moveq	#0,d7			start at 0
*
iop_stab
	tst.b	(a2)			is it empty?
	blt.s	iop_gtag		... yes, look for device
	addq.l	#4,a2			next channel table address
	addq.w	#1,d7			and number
	cmp.l	sys_chtt(a6),a2 	at top?
	blt.s	iop_stab		... no, carry on searching the table
	moveq	#err.imem,d0		... yes, insufficient memory
iop_exit1
	bra.s	iop_exit

iop_gtag
	swap	d7
	move.w	sys_chtg(a6),d7 	get tag
	swap	d7

*
* search all devices
*
	lea	sys_iodl(a6),a5 	device list
	bra.s	iop_deve

iop_dev
	move.l	d0,a5			next device
	lea	-iod_iolk(a5),a3	set linkage base
	move.l	iod_open(a3),a4 	and address of open routine
	movem.l d1/d3/d7/a1/a2/a5,-(sp)    save the registers we need for this loop
	move.l	a1,a0			set pointer to name
; d1 is job ID
; d7 is channel ID
; a0 is pointer to name
; a2 is pointer to table
; a3 is linkage
	jsr	(a4)
	movem.l (sp)+,d1/d3/d7/a1/a2/a5
	tst.l	d0			found?
	beq.s	iop_set 		... yes
	cmp.w	#err.fdnf,d0		not found?
	bne.s	iop_exit		... no
iop_deve
	move.l	(a5),d0 		another device?
	bne.s	iop_dev 		... yes

iop_opfl
	bsr.l	ioa_opfl		try opening a file system device
	bne.s	iop_exit

iop_set
	move.l	a0,(a2) 		set address of channel in table
	lea	chn_drvr(a0),a1

	move.l	d7,a0			set channel ID

	cmp.w	sys_chtp(a6),d7 	new top channel?
	ble.s	iop_schan		... no
	move.w	d7,sys_chtp(a6) 	... yes, set it

iop_schan
	move.l	a5,(a1)+		set driver
	move.l	d1,(a1)+		... owner
	move.l	a2,(a1)+		remove flag
	swap	d7
	move.w	d7,(a1)+		tag


; update tag

	addq.w	#1,sys_chtg(a6) 	 ; tag one more
	bvc.s	iop_ok			 ; but not negative
	rol.w	sys_chtg(a6)		 ; restart at one

iop_ok
	moveq	#0,d0			 ; ok
iop_exit
	movem.l (sp)+,reglist
	bra.l	sms_rte
	end
