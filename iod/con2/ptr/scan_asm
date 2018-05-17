;	Scan channel blocks	     V2.01   2000 Tony Tebby
;
; 2005-11-15  2.01  Tries to move main SBasic if it's outside of screen (MK)
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	A2	action routine
;	A3	linkage block
;
	section driver

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_chn'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'

	xdef	pt_scanchan
	xdef	pt_scanclr
	xdef	pt_scanmode
	xdef	pt_scansize
	xdef	pt_scancrop

	xref	pt_outln
	xref	cn_sink
	xref	cn_spap
	xref	cn_sstr
	xref	cn_defbd
	xref	cn_clral

	xref	gu_rchp

psc.reg reg	d1-d7/a0-a5

pts_centre
	move.l	pt_xscrs(a3),d1
	lsr.l	#1,d1
	move.l	d1,pt_pos(a3)
	move.l	d1,pt_npos(a3)		 ; set pointer position
	move.l	pt_scrsz(a3),d1
	lsr.l	#1,d1
	add.l	pt_scren(a3),d1
	move.l	d1,pt_addr(a3)		 ; and pointer address!!
	rts

; scan the channel blocks resetting the mode, the size and returning the save area
;  - but first put the pointer in the middle
;
pt_scanmode
	movem.l psc.reg,-(sp)
	bsr.s	pts_centre
	lea	psc_mode,a2
	bra.s	pts_scan

; scan the channel blocks resetting the screen size
;  - but first put the pointer in the middle
;
pt_scansize
	movem.l psc.reg,-(sp)
	bsr.s	pts_centre
	lea	psc_size,a2
	bra.s	pts_scan

; scan the channel blocks cropping the windows (remove the owner job!)
;
pt_scancrop
	movem.l psc.reg,-(sp)
	lea	psc_crop,a2
	bra.s	pts_scan

; scan the channel blocks resetting the colours and clearing the windows
;
pt_scanclr
	movem.l psc.reg,-(sp)
	lea	psc_clear,a2
	bra.s	pts_scan

pt_scanchan
	movem.l psc.reg,-(sp)
pts_scan
	lea	iod_iolk(a3),a5 	 ; console channel linkage
	move.l	sys_chtt(a6),d7 	 ; channel table top
	move.l	sys_chtb(a6),a4

psc_loop
	move.l	(a4)+,d0
	bmi.s	psc_eloop
	move.l	d0,a0
	cmp.l	chn_drvr(a0),a5 	 ; console?
	bne.s	psc_eloop		 ; ... no

	movem.l d7/a2/a4/a5,-(sp)
	add.w	#sd.extnl,a0
	jsr	(a2)
	movem.l (sp)+,d7/a2/a4/a5

psc_eloop
	cmp.l	d7,a4			 ; at top of table?
	blt.s	psc_loop

	moveq	#0,d0
	movem.l (sp)+,psc.reg
	rts

psc_mode
	move.b	pt_dmode(a3),sd_wmode(a0) ; set mode
	tst.b	sd_mysav(a0)		; my save area?
	beq.s	pscm_nosave		; no cannot throw away
	move.l	sd_wsave(a0),d1
	beq.s	pscm_nosave		; no save area
	exg	a0,d1
	jsr	gu_rchp
	move.l	d1,a0
pscm_nosave
	moveq	#0,d0
	move.b	d0,sd_mysav(a0) 	 ; clear save area defs
	move.l	d0,sd_wsave(a0)
	move.l	d0,sd_wssiz(a0)

psc_size
	move.w	pt_scinc(a3),sd_linel(a0) ; set line length
	move.l	pt_scren(a3),sd_scrb(a0)  ; and base
	rts

psc_crop
	assert	sd..prwn,7
	tst.b	sd_prwin(a0)		 ; primary window?
	bpl.s	pscc_rts		 ; ... no
	move.l	sd_xouto(a0),d0
	add.l	sd_xouts(a0),d0
	cmp.w	pt_yscrs(a3),d0 	 ; too low?
	bgt.s	pscc_rjob		 ; ... yes
	cmp.l	pt_xscrs(a3),d0 	 ; too large?
	ble.s	pscc_rts

pscc_rjob
pscc.reg reg	d1-d7/a0-a6
	movem.l pscc.reg,-(sp)
	move.l	chn_ownr-sd.extnl(a0),d1
	beq.s	pscc_basic		 ; Main SBASIC? Cannot kill!
	moveq	#sms.frjb,d0
	moveq	#err.orng,d3
	trap	#do.sms2
	bra.s	pscc_ret

; This is a rather quick&dirty solution to the problem. It simply moves all the
; SBASIC windows to the first 512x256 pixels of the screen. At least SBASIC will
; live on!
pscc_basic
	moveq	#0,d1			 ; no shadow
	moveq	#0,d2
	clr.l	-(sp)			 ; origin 0x0
	move.l	#$02000100,-(sp)	 ; size 512x256
	move.l	sp,a1
	jsr	pt_outln
	adda.w	#8,sp
pscc_ret
	movem.l (sp)+,pscc.reg
pscc_rts
	rts

psc_clear
	move.b	sd_icolr(a0),d1 ; ink
	jsr	cn_sink
	move.b	sd_pcolr(a0),d1 ; paper
	jsr	cn_spap
	move.b	sd_scolr(a0),d1 ; strip
	jsr	cn_sstr
	jsr	cn_clral
	move.b	sd_bcolr(a0),d1 ; border
	move.w	sd_borwd(a0),d2
	jmp	cn_defbd


	end
