; SMSQ ATARI ST Keyboard Driver Initialisation

	section init

	xdef	kbd_init

	xref	kbd_initi
	xref	kbd_int
	xref	kbd_mint
	xref	iou_lkvm

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_atari'
	include 'dev8_smsq_kbd_keys'

kbd_init
	jsr	kbd_initi		 ; inistall all but interrupt servers

	moveq	#sms.xtop,d0
	trap	#do.sms2

	or.w	#$2700,sr		 ; no interrupts

	moveq	#kb_veci,d1		 ; funny vectored interrupt - offset
	lea	kbd_int,a1		 ;   - address of server
	jsr	iou_lkvm		 ; link minimum vectored server
	move.l	a1,mfp_vbas+vio_acia	 ; set address
	lea	kbd_mint,a1
	move.l	a1,kbd_midi(a3) 	 ; set dummy MIDI interrupt server
	move.l	a3,kbd_midl(a3)

	bset	#mfp..aci,mfp_ace	 ; enable
	bset	#mfp..aci,mfp_acm	 ; and unmask

; strip keyboard processor queue

kbi_strip
	move.w	#1000,d0
kbi_stloop
	btst	#acia..rr,at_kbdc	 ; read ready?
	dbne	d0,kbi_stloop
	beq.s	kbi_cmdst
	move.b	at_kbdd,d0		 ; get byte code
	bra.s	kbi_strip

; enable keyboard and mouse

kbi_cmdst
	lea	kbi_cmd,a1
kbi_cmloop
	move.b	(a1)+,d0		 ; next command
	blt.s	kbi_ok
kbi_cmwait
	btst	#acia..tr,at_kbdc	 ; ready?
	beq.s	kbi_cmwait		 ; ... no
	move.b	d0,at_kbdd		 ; send command byte
	bra.s	kbi_cmloop

kbi_cmd dc.b	kbc.joff,kbc.auto
	dc.b	kbc.mbut,kbcm.key,kbc.mrel,kbc.mthr,1,1
	dc.b	-1
	ds.w	0
kbi_ok
	moveq	#0,d0
kbi_rts
	rts

	end
