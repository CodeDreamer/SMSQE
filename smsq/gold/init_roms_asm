; GOLD init Plug In ROMs
; 2004-04-23	1.00	attempt to get miracle hard disk working again (TT)
	section init

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'

	section header

header_base
	dc.l	roms_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	roms_end-roms_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	18,'Initialise QL ROMs'
	dc.l	'    '
	dc.w	$200a


	section base
roms_base
	movem.l 0,a0/d1/d3		 ; possible fix for miracle hard disk

	lea	con0_name,a0		 ; open this
	moveq	#0,d1
	moveq	#0,d3
	moveq	#ioa.open,d0
	trap	#do.ioa

	lea	$c000,a3		 ; look at Plug In address
	bsr.s	irm_check

	moveq	#sms.info,d0
	trap	#1
	cmp.b	#sys.mgold,sys_mtyp(a0)  ; Gold card ?
	beq.s	irm_exit		 ; ... yes

	move.l	#$4c0000,a3		 ; for SGC, look at expansions
irm_loop
	bsr.s	irm_check
	adda.w	#$4000,a3
	cmpa.l	#$500000,a3
	blt.s	irm_loop

irm_exit
	bsr.s	irm_cltag
	sub.l	a0,a0
	moveq	#ioa.clos,d0
	trap	#do.ioa
	rts

irm_cltag
	moveq	#sms.xtop,d0
	trap	#do.smsq
	clr.w	sys_chtg(a6)
irm_rts
	rts

irm_check
	clr.l	$20000			 ; get bus tidy
	cmp.l	#$4afb0001,(a3) 	 ; flagged?
	bne.s	irm_rts
	cmp.l	#'Ultr',$c(a3)		 ; Ultrasoft TKIII?
	beq.s	irm_rts
	cmpa.l	#$4cc000,a3
	bne.s	irm_mess
	move.l	#$c000,a1
	move.l	a3,a0
	moveq	#$b,d0
irm_ckrl
	cmpm.l	(a1)+,(a0)+		 ; wrap around?
	dbne	d0,irm_ckrl
	beq.s	irm_rts 		 ; ... yes
irm_mess
	sub.l	a0,a0
	lea	8(a3),a1
	move.w	ut.wtext,a2
	jsr	(a2)			 ; write text

	move.w	4(a3),d0
	beq.s	irm_ini2		 ; ... no basic procs
	lea	(a3,d0.w),a1
	move.w	sb.inipr,a2
	jsr	(a2)
irm_ini2
	move.w	6(a3),d0
	beq.s	irm_rts 		; ... no init code
	lea	(a3,d0.w),a1
	jmp	(a1)



con0_name  dc.w 3,'CON'
roms_end

	end
