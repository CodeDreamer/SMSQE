; Pointer mode routine	V1.12				 1991	Tony Tebby
;							  2002	Marcel Kilgus
; 2002-02-13  1.12  Clear background with wallpaper colour (MK)
; 2003-10-14  1.13  cn_fblock is no longer xref.l'd (wl - how did it get here in the first place?)
	section driver
;
	xdef	pt_modex

	xref	pt_xmode
	xref	pt_xmodec
	xref	pt_wchka
	xref	pt_mrall
	xref	pt_mrstj
	xref	cn_fblock

	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_chn'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

pt_modex
ptm.reg reg	d1/d3-d7/a0-a4
stk_d1	equ	0
	movem.l ptm.reg,-(sp)
	move.l	sys_clnk(a6),a3
	tst.b	d1				; enquire?
	bmi.l	ptm_req 			; ... yes

;	 cmp.b	 pt_dmode(a3),d1		 ; mode changed
;	 beq.s	 ptm_done			 ; ... no, forget it

	jsr	pt_xmodec			; check if mode can be set
	bne	ptm_done			; ... cannot

	sf	sys_dfrz(a6)			; screen unfrz

	moveq	#1,d3				; lock all windows
	jsr	pt_wchka
	moveq	#0,d3				; reset locks correctly
	jsr	pt_wchka
	move.l	pt_head(a3),a0			; top window
	move.b	#sd.sunlk,sd_wlstt-sd_prwlt(a0) ; unlock top!
	jsr	pt_mrall			; and the rest

	move.l	stk_d1(sp),d1
	jsr	pt_xmode			; set mode
	move.b	d1,pt_dmode(a3) 		; and save it

	movem.l d2-d7/a1-a2,-(a7)
	move.l	pt_xscrs(a3),d1 		; whole screen
	moveq	#0,d2
	move.w	pt_bgstp(a3),d6 		; background colour
	move.l	pt_bgclm(a3),d7
	move.l	pt_scren(a3),a1 		; screen is destination
	move.w	pt_scinc(a3),a2 		; destination increment
	jsr	cn_fblock
	movem.l (a7)+,d2-d7/a1-a2

; windows saved, screen filled with background - now restore windows

	move.b	#pt.supmd,pt_pstat(a3)	  set pointer suppressed
	assert	pt_bstat,pt_bsupp-1,pt_bpres-2,pt_bcurr-3
	move.l	#ptb.psup<<16,pt_bstat(a3)
	clr.b	pt_reltm(a3)		  and relax

; first restore all primary windows, from the bottom up

	moveq	#sms.info,d0		who just went MODE?
	trap	#1
	move.l	d1,d7			he did, keep his job ID

	lea	pt_head(a3),a0		fix his mode now

ptm_chg
	move.l	(a0),d0 			   next window
	beq.s	ptm_rest			   isn't one, oddly enough
	move.l	d0,a0				   point to it
	cmp.l	chn_ownr-sd_prwlt-sd.extnl(a0),d7  owned by MODE job?
	bne.s	ptm_chg 			   no, try next
	move.b	pt_dmode(a3),sd_wmode-sd_prwlt(a0) set its mode

ptm_rest
	moveq	#-1,d3			don't save newly locked windows
	jsr	pt_wchka(pc)		check all for correct mode

	jsr	pt_mrstj(pc)		restore/reset all windows

ptm_done
	move.b	sys_qlmr(a6),$18063		; MAGIC!!!!
ptm_req
	moveq	#0,d1
	moveq	#0,d2
	move.b	pt_dmode(a3),d1
	move.l	d1,stk_d1(sp)			; return current mode
	moveq	#0,d0
	movem.l (sp)+,ptm.reg
	move.l	sms.rte,a5
	jmp	(a5)

	end
