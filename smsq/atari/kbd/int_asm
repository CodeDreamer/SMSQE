; Atari QDOS KBD interrupt routine    1988  Tony Tebby   QJUMP

	section kbd

	xdef	kbd_int
	xdef	kbd_mint

	xref	ioq_pbyt

	include dev8_keys_sys
	include dev8_keys_con
	include dev8_keys_atari
	include dev8_keys_qu
	include dev8_smsq_kbd_keys
	include dev8_smsq_smsq_base_keys

	include dev8_mac_assert

regent	reg	d0/d1/d2/a0/a1
regexi	reg	d0/d1/d2/a0/a1/a2/a3

kbd_int
	movem.l regent,-(sp)		 ; a2/a3 already safe
	move.l	sms.sysb,a1
	move.l	sys_clnk(a1),a1
kbi_rdo
	bclr	#mfp..aci,mfp_acs	 ; clear in service

	move.b	at_kbdc,d0		 ; control reg
	bge.l	kbi_midi		 ; ... no interrupt

	btst	#acia..rr,d0		 ; read ready?
	beq.l	kbi_midi		 ; ... no, try midi

	move.b	at_kbdd,d1		 ; get byte code

	and.b	#acia.err,d0		 ; any errors
	bne.s	kbi_err 		 ; ... yes

	tst.b	kb_err(a3)		 ; error handling?
	bne.s	kbi_ster		 ; ... yes

	move.l	kb_padd(a3),d0		 ; processing packet
	bne.l	kbi_pack		 ; ... yes

	moveq	#$7f,d0
	and.b	d1,d0
	cmp.b	#kbk.hit,d0		 ; key?
	bge.s	kbi_spec		 ; ... no
	lea	kb_qu(a3),a2		 ; now put code into buffer
	jsr	ioq_pbyt
	beq.l	kbi_done

kbi_err
	clr.l	kb_padd(a3)		 ; not packet
	clr.w	kb_lcod(a3)		 ; no last character
	clr.b	kb_arep(a3)		 ; not auto repeated
	assert	kb_hit+1,kb_do
	clr.w	kb_hit(a3)		 ; no button

	move.l	kb_qu+qu_nexti(a3),kb_qu+qu_nexto(a3) ; clear queue

kbi_ster
	move.b	#kb.errw,kb_err(a3)	 ; keep on erroring until wait timed out
	bra.l	kbi_done

kbi_spec
	cmp.b	#kbk.do,d0		 ; do button
	bgt.s	kbi_head		 ; ... no, packet header
	moveq	#%11,d2
	and.b	pt_bpoll(a1),d2 	 ; current button status
	sub.b	#kbk.hit,d0
	tst.b	d1			 ; up or down?
	bpl.s	kbi_bdown
	bclr	d0,d2
	bra.s	kbi_bcheck
kbi_bdown
	bset	d0,d2
kbi_bcheck
	cmp.b	#%11,d2 		 ; both buttons?
	bne.s	kbi_bset		 ; ... no
	st	kb_b3(a3)		 ; flag button 3
	bra.l	kbi_done

kbi_bset
	move.b	d2,pt_bpoll(a1) 	 ; this is new button
	beq.l	kbi_done		 ; ... none
	clr.b	pt_lstuf(a1)		 ; it isn't a stuff
	bra.s	kbi_done

kbi_head
	moveq	#kbh.mrmk,d0		 ; is it mouse relative movement?
	and.b	d1,d0
	cmp.b	#kbh.mrel,d0
	beq.s	kbi_mhead		 ; ... yes
	moveq	#kbh.jymk,d0		 ; ... no, try joystick
	and.b	d1,d0
	cmp.b	#kbh.joy0,d0
	bne.s	kbi_done		 ; ... no
	moveq	#1,d0			 ; one parameter
	bra.s	kbi_shead
kbi_mhead
	moveq	#2,d0
kbi_shead
	move.b	d1,kb_phead(a3) 	 ; set header
	move.b	d0,kb_pcnt(a3)		 ; count
	lea	kb_pbuf(a3),a0
	move.l	a0,kb_padd(a3)		 ; address
	bra.s	kbi_done

kbi_pack
	move.l	d0,a0

	move.b	d1,(a0)+		 ; set byte read
	move.l	a0,kb_padd(a3)
	subq.b	#1,kb_pcnt(a3)		 ; any more?
	bgt.s	kbi_done		 ; ... yes

	cmp.b	#kbh.joy0,kb_phead(a3)	 ; joystick or mouse?
	blo.s	kbi_smse		 ; ... mouse
	beq.s	kbi_endp		 ; ... joystick 0
	rol.b	#1,d1			 ; scrumple data byte
	and.w	#$001f,d1		 ; 5 bits
	move.b	kbi_jytb(pc,d1.w),kb_joyst(a3) ; joystick status
	bra.s	kbi_endp

kbi_smse
	move.b	-(a0),d1
	ext.w	d1
	add.w	d1,pt_yinc(a1)		 ; y distance moved
	move.w	d1,d0
	bpl.s	kbi_smsey
	neg.w	d0
kbi_smsey
	move.b	-(a0),d1
	ext.w	d1
	add.w	d1,pt_xinc(a1)
	tst.w	d1
	bpl.s	kbi_smseck
	neg.w	d1
kbi_smseck
	cmp.w	d1,d0
	bge.s	kbi_smseinc
	move.w	d1,d0
kbi_smseinc
	add.w	d0,pt_xicnt(a1) 	 ; mouse interrupts
kbi_endp
	clr.l	kb_padd(a3)

kbi_done
	move.b	at_midic,d0		 ; midi control reg
	bpl	kbi_rdo 		 ; none - redo kbd

kbi_mdo
	move.l	kbd_midi(a3),a2
	move.l	kbd_midl(a3),a3 	 ; midi interrupt
	jsr	(a2)
	bra	kbi_rdo

kbi_midi
	move.b	at_midic,d0		 ; midi control reg
	bmi.s	kbi_mdo 		 ; do midi interrupt

kbi_rte
	movem.l (sp)+,regexi		 ; restore
	rte

kbd_mint
	move.b	at_midid,kbd_midb(a3)	 ; midi byte received
	rts

fire	equ	%01000000
up	equ	%00000100
down	equ	%10000000
left	equ	%00000010
right	equ	%00010000

kbi_jytb
	dc.b	0,fire
	dc.b	up,up+fire
	dc.b	down,down+fire
	dc.b	down+up,down+fire+up
	dc.b	left,left+fire
	dc.b	left+up,left+up+fire
	dc.b	left+down,left+down+fire
	dc.b	left+down+up,left+down+fire+up
	dc.b	right,right+fire
	dc.b	right+up,right+up+fire
	dc.b	right+down,right+down+fire
	dc.b	right+down+up,right+down+fire+up
	dc.b	left+right,left+right+fire
	dc.b	left+right+up,left+right+up+fire
	dc.b	left+right+down,left+right+down+fire
	dc.b	left+right+down+up,left+right+down+fire+up

	end
