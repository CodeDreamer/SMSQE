; SuperBASIC reserve SuperBASIC space

	section uq

	xdef	sb_revect

	xdef	sb_rar32
	xdef	sb_resar
	xdef	sb_rbk20
;***	xdef	sb_resbk
	xdef	sb_rgr04
;***	xdef	sb_resgr
	xdef	sb_rnt08
	xdef	sb_resnt
	xdef	sb_rrt24
;***	xdef	sb_resrt
	xdef	sb_resbf
	xdef	sb_rescl
	xdef	sb_resnl
;***	xdef	sb_resdt
	xdef	sb_resch
;***	xdef	sb_resln
	xdef	sb_ressr

	xref	sb_insmem
	xref	sb_error

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_mac_assert'

; The reserve stage post occupies $38+5*6 <$60 bytes

sb_revect
	dc.w	2			 ; offset of vector entry from start
	dc.w	sbs_up-sbs_rar32	 ; length to copy (excluding vectors)
	dc.w	5*6			 ; length of vectors to fixup
;+++
; reserve stage posts SuperBASIC  d1 bytes (optional), d1/d2/d3 smashed
;---
sbs_rar32
	moveq	#32,d1			 ; the order of these instructions
;					 ; is critical for compatibility
	moveq	#sb_arthp,d2		 ; with some QL software!!!!
	bra.s	sbs_dn
	moveq	#20,d1
	moveq	#sb_backp,d2
	bra.s	sbs_dn
	moveq	#4,d1
	moveq	#sb_grphp,d2
	bra.s	sbs_dn
	moveq	#8,d1
	moveq	#sb_nmtbp,d2
	bra.s	sbs_up
	moveq	#24,d1
	moveq	#sb_retsp,d2
	bra.s	sbs_up
	bra.s	sbs_resbf
	bra.s	sbs_resbf
	bra.s	sbs_rescl
	bra.s	sbs_rescl
	moveq	#sb_nmlsp,d2
	bra.s	sbs_up
	bra.s	sbs_error
	bra.s	sbs_error
	moveq	#sb_chanp,d2
	bra.s	sbs_up
	bra.s	sbs_error
	bra.s	sbs_error
sbs_resr
	moveq	#sb_srcep,d2
	addq.l	#2,d1			 ; extra for spare link pointer
	assert	sbs_resr-sbs_rar32,$36	 ; ensure that staging post matches
sbs_up
	dc.w	jmp.l
	dc.l	sbr_up-*
sbs_dn
	dc.w	jmp.l
	dc.l	sbr_dn-*
sbs_resbf
	dc.w	jmp.l
	dc.l	sb_resbf-*
sbs_rescl
	dc.w	jmp.l
	dc.l	sb_rescl-*
sbs_error
	dc.w	jmp.l
	dc.l	sb_error-*

;+++
; reserve SuperBASIC  d1 bytes (optional), d1/d2/d3 smashed
;---
sb_rar32
	moveq	#32,d1			 ; the order of these instructions
sb_resar				 ; is critical to the compatibility
	moveq	#sb_arthp,d2		 ; with some QL software!!!!
	bra.s	sbr_dn
sb_rbk20
	moveq	#20,d1
sb_resbk
	moveq	#sb_backp,d2
	bra.s	sbr_dn
sb_rgr04
	moveq	#4,d1
sb_resgr
	moveq	#sb_grphp,d2
	bra.s	sbr_dn
sb_rnt08
	moveq	#8,d1
sb_resnt
	moveq	#sb_nmtbp,d2
	bra.s	sbr_up
sb_rrt24
	moveq	#24,d1
sb_resrt
	moveq	#sb_retsp,d2
	bra.s	sbr_up
; sb_resbf
	bra.l	sb_resbf
; sb_rescl
	bra.l	sb_rescl
sb_resnl
	moveq	#sb_nmlsp,d2
	bra.s	sbr_up
; sb_resdt
	bra.l	sb_error

sb_resch
	moveq	#sb_chanp,d2
	bra.s	sbr_up
; sb_resln
	bra.l	sb_error

sb_ressr
	moveq	#sb_srcep,d2
	addq.l	#2,sb_srcep(a6) 	  ; extra for spare link pointer
	bsr.s	sbr_up
	subq.l	#2,sb_srcep(a6)
	rts

sbr_dn
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.s	sbr_nop
	sub.l	0(a6,d2.l),d1		 ; -(pointer - required)
	add.l	sb.loffp(a6,d2.l),d1	 ; lower limit -(pointer - required)
	bgt.s	sbr_alldn
	rts

sbr_up
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.s	sbr_nop
	add.l	0(a6,d2.l),d1		 ; pointer + required
	sub.l	sb.toffp(a6,d2.l),d1	 ; (pointer + required) - top
	bgt.s	sbr_allup
sbr_nop
	rts

sbr_alldn
	move.l	sb.bofpd(a6,d2.l),d0
	sub.l	sb.loffp(a6,d2.l),d0
	add.l	d0,d1			 ; total size size required
	lsr.l	#2,d0			 ; 25% spare
	add.l	d0,d1
	add.l	#sb.alinc-1+sb.dnspr+sb.dnspr,d1 ; arbitrary round up factor
	and.w	#-sb.alinc,d1		 ; allocation rounded up
	move.l	d1,d3			 ; to be kept

sbra.rge reg	d2/a0/a1
sbra.rgx reg	a0/a1

	movem.l sbra.rge,-(sp)
	moveq	#sms.achp,d0		 ; allocate new heap
	moveq	#-1,d2
	trap	#do.sms2
	tst.l	d0			 ; **** new this - SMSQ does not set cc
	bne.l	sb_insmem

	move.l	(sp)+,d2
	exg	d3,a0			 ; d3 is new limit
	add.l	d3,a0			 ; ... a0 is new base
	move.w	#sb.dnspr,a1		 ; spare above base
	add.l	sb.bofpd(a6,d2.l),a1	 ; old base
	moveq	#$1f,d0
	add.l	a1,d0
	sub.l	(a6,d2.l),d0		 ; old size
	lsr.l	#5,d0			 ; /32, rounded up

	add.l	a6,a1
	assert	sb.alinc&$1f,0		 ; ensure allocation is n*32

sbr_mvdn
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	move.l	-(a1),-(a0)
	subq.l	#1,d0
	bgt.s	sbr_mvdn

	move.l	a0,d0
	sub.l	a1,d0			 ; amount area has moved
	add.l	d0,(a6,d2.l)		 ; adjust pointer
	add.l	d0,sb.bofpd(a6,d2.l)	 ; and base
	move.l	sb.loffp(a6,d2.l),a0	 ; old limit is base of memory + a bit
	lea	-sb.dnspr(a6,a0.l),a0	 ; absolute to be released

	sub.l	a6,d3
	add.l	#sb.dnspr,d3		 ; a bit of spare below
	move.l	d3,sb.loffp(a6,d2.l)	 ; new relative limit
	bra.s	sbr_retb		 ; and return old memory block

sbr_allup
	move.l	sb.toffp(a6,d2.l),d0
	sub.l	sb.bofpu(a6,d2.l),d0
	add.l	d0,d1			 ; total size size required
	lsr.l	#2,d0			 ; 25% spare
	add.l	d0,d1
sbr_allui
	moveq	#sb.alinc-1,d0		 ; arbitrary round up factor
	add.l	d0,d1
	and.w	#-sb.alinc,d1		 ; allocation rounded up
	move.l	d1,d3			 ; to be kept

	movem.l sbra.rge,-(sp)
	moveq	#sms.achp,d0		 ; allocate new heap
	moveq	#-1,d2
	trap	#do.sms2
	tst.l	d0			 ; **** new this - SMSQ does not set cc
	bne.l	sb_insmem

	move.l	(sp)+,d2
	add.l	a0,d3			 ; ... new top
	sub.l	a6,d3
	move.l	d3,sb.toffp(a6,d2.l)	 ; new relative top

	move.l	sb.bofpu(a6,d2.l),a1	 ; old base

	moveq	#31,d0
	add.l	(a6,d2.l),d0
	sub.l	a1,d0			 ; old size
	assert	sb.alinc&$1f,0		 ; ensure allocation is n*32
	lsr.l	#5,d0			 ; /32, rounded up

	add.l	a6,a1			 ; absolute base
	move.l	a1,d3			 ; ... keep it

sbr_mvup
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	subq.l	#1,d0
	bgt.s	sbr_mvup

	move.l	a0,d0
	sub.l	a1,d0			 ; amount area has moved
	add.l	d0,(a6,d2.l)		 ; adjust pointer
	add.l	d0,sb.bofpu(a6,d2.l)	 ; and other pointer
	move.l	d3,a0

sbr_retb
	cmp.l	a6,a0			 ; allocation in SBASIC?
	blo.s	sbr_retdo		 ; ... no
	cmp.l	sp,a0
	blo.s	sbr_rtr 		 ; ... yes, do not return it
sbr_retdo
	moveq	#sms.rchp,d0
	trap	#do.sms2
sbr_rtr
	movem.l (sp)+,sbra.rgx
sbr_rts
	rts

sb_rescl
	add.l	sb_cmdlp(a6),d1 	 ; command line pointer + required
	sub.l	sb_cmdlt(a6),d1 	 ; enough room?
	ble.s	sbr_rts 		 ; ... yes
sbr_cl
	move.l	sb_buffb(a6),d0
	move.l	d0,sb_cmdlb(a6) 	 ; expand buffer+command line
	add.l	sb_cmdlt(a6),d1
	sub.l	d0,d1			 ; total size

	moveq	#sb_cmdlp,d2
	bsr	sbr_allui
	move.l	sb_cmdlb(a6),d0 	 ; new buffer base
	move.l	sb_buffb(a6),d1
	move.l	d0,sb_buffb(a6)
	sub.l	d1,d0			 ; distance moved
	add.l	d0,sb_buffp(a6) 	 ; new buffer pointer
	add.l	d0,sb_bufft(a6) 	 ; new buffer top
	move.l	sb_bufft(a6),sb_cmdlb(a6) ; is new command line base
	rts

sb_resbf
	add.l	sb_buffp(a6),d1 	 ; buffer pointer + required
	sub.l	sb_bufft(a6),d1 	 ; enough room?
	ble.s	sbr_rts 		 ; ... yes

	move.l	sb_cmdlt(a6),d0 	 ; old command line top
	sub.l	sb_cmdlb(a6),d0 	 ; old command line allowance
	move.l	d0,-(sp)
	bsr.s	sbr_cl			 ; reserve buffer and command line
	move.l	(sp)+,d1		 ; the old allocation

	movem.l a0/a1,-(sp)
	move.l	sb_cmdlt(a6),a1 	 ; this is the new command line top
	move.l	a1,d0
	sub.l	d1,d0			 ; from the new top, gives the new base
	sub.l	sb_cmdlb(a6),d0 	 ; ... the distance to move
	add.l	d0,sb_cmdlb(a6) 	 ; update the pointers
	add.l	d0,sb_cmdlp(a6)
	add.l	d0,sb_bufft(a6)

	add.l	a6,a1			 ; new top absolute
	move.l	a1,a0			 ; from here
	sub.l	d0,a0

sbr_clup
	move.l	-(a0),-(a1)
	move.l	-(a0),-(a1)
	subq.l	#8,d1
	bgt.s	sbr_clup

	movem.l (sp)+,a0/a1
	rts

	end
