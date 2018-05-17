; DV3 Re-allocate Drive Definition Block     V3.00	     1992 Tony Tebby

	section dv3

	xdef	dv3_redef

	include 'dev8_dv3_keys'
	include 'dev8_keys_sys'
	include 'dev8_keys_chp'
	include 'dev8_keys_qlv'
	include 'dev8_smsq_smsq_base_keys'

;+++
; This is the routine that is used to bodge up a re-allocation of the drive
; definition block.
;
; This is a dirty routine which applies only to QDOS memory management
; strategies. It must be called in supervisor mode.
;
;	d0 cr	size required
;	a4 c  u pointer to definition block
;	a6 c  p pointer to system variables
;
;	Status return standard
;---
dv3_redef
drd.reg reg	d1-d3/a0-a3
drd.stk equ	$1c
	movem.l drd.reg,-(sp)
	cmp.l	chp_len(a4),d0		 ; large enough already?
	ble.s	drd_ok
	move.l	d0,d1
	move.w	mem.achp,a2
	jsr	(a2)			 ; allocate new
	bne.s	drd_exit
	exg	a0,a4			 ; a4 is now new

	move.l	a4,a1
	moveq	#chp.len,d0
	add.l	d0,a0
	add.l	d0,a1
	moveq	#(ddf_dtop-chp.len)/4,d0 ; copy for header up to dtop
drd_clp
	move.l	(a0)+,(a1)+
	subq.w	#1,d0
	bgt.s	drd_clp

	move.b	ddf_drid(a4),d0
	lsl.w	#2,d0			 ; index drive table
	lea	sys_fsdd(a6),a1
	move.l	a4,(a1,d0.w)		 ; set drive in table

	sub.w	#ddf_dtop,a0


	lea	drd.stk+4(sp),a1	 ; search up about 40 long words for
					 ; stored values of a4
	moveq	#80,d0
drd_sloop
	cmp.l	(a1),a0 		 ; this one?
	bne.s	drd_snext		 ; ... no
	move.l	a4,(a1) 		 ; patch the stack
drd_snext
	addq.l	#2,a1
	dbra	d0,drd_sloop

	move.w	mem.rchp,a2
	jsr	(a2)			 ; release old bit
drd_ok
	jsr	sms.cinvi		 ; invalidate instruction cache
	moveq	#0,d0
drd_exit
	movem.l (sp)+,drd.reg
	rts
	end
