; DV3 Q68 SDHC (WIN) Driver initialisation 1.02  W. Lenerz 2016 - 2020
;
; based on
;
; DV3 Q40 IDE Initialisation  V3.00    1992  Tony Tebby

; 2020-11-27	1.02  card_speedup added, removed test code (wl)
;		1.01  don't check for card 2 init, config items rearranged (wl)

	section dv3

	xdef	win_init		; inits the driver

	xref	dv3_link
	xref	dv3_acdef
	xref	norm_nm
	xref	inicrd
	xref	cpy2mem

	xref	gu_fopen
	xref	gu_fclos

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_mac'
	include 'dev8_mac_assert'
	include 'dev8_mac_basic2'
	include 'dev8_mac_proc'
	include 'dev8_keys_qlv'
	include 'dev8_keys_q68'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_MultiConfig'

;------------------------------------------------
;
; DV3 Q68 SDHC (WIN) driver initialisation
;
;	a3  r	device driver linkage
;
;------------------------------------------------
win_init
	lea	hd_table,a3
	jsr	dv3_link		; link in driver
	tst.l	d0
	bne.s	out			; ooops!
	
	lea	hd_proctab,a1
	move.w	sb.inipr,a2
	jsr	(a2)			; link in procedures
	    
; fill in data about the drives/cards in the device defn block, see keys_q68
cpyreg	reg	d1/d2/d3/d6/a0
	movem.l cpyreg,-(a7)
	lea	hdl_unit(a3),a1
	lea	q68_drv1,a0		; config: what drv on what card
	moveq	#7,d7
	clr.l	d3			; used as flag
cplp	move.b	(a0)+,d0		; card nbr - can be -1, 0, or q68.coff
	ble.s	cp			; drive on no card, or card1, just set it
	st	d3			; d3<>0 = some drives are on card 2
cp	move.b	d0,(a1)+		; fill in the configured drive-> card assignments
	dbf	d7,cplp

; normalize the configured drive names
	move.l	d3,a4
	lea	q68_wn1+2,a0		; point first smsqe name
	move.l	a0,d5
	lea	win_drvs,a1		; point space for normalized names
	move.l	a1,d6
	moveq	#7,d7
	move.l	a1,hdl_targ(a3) 	; point to where I can find it later
norm_lp bsr	norm_nm 		; normalize name at (a0) to (a1)
	moveq	#16,d3
	add.l	d3,d5
	move.l	d5,a0
	add.l	d3,d6
	move.l	d6,a1
	dbf	d7,norm_lp

done	jsr	cpy2mem 		; copy routines into fast mem
	movem.l (a7)+,cpyreg
	moveq	#0,d0			; done
out	rts

hd_table
	link_table	WIN, '1', hdl_end, ddf_dtop
	buffered			; commenting out results in a not working drive
	sectl		512
	mtype		ddl.hd
	msect		255		; this will still use slave blocks for simple reads
	density 	ddf.dd

	poll		hd_poll_check

	check		hd_check
	direct		hd_direct
	rsect		hd_rdirect
	wsect		hd_wdirect
	slbfill 	hd_slbfill
	slbupd		hd_slbupd
	dflush		hd_dflush
	fflush		hd_fflush
;;;	fslave		hd_fslave
	mformat 	hd_mformat
	status		hd_fstatus
	done		hd_done

	thing		hd_tname,hd_thing

	preset_b	hdl_apnd-1, 0,hdl.apnd
	preset_b	hdl_maxd, hdl.maxd, 0
	preset_w	hdl_paus,30

	preset_b	hdl_part,$ff,$ff,$ff,$ff,$ff,$ff,$ff,$ff
	preset_b	hdl_remd,1,1,1,1,1,1,1,1

	preset_v	hdl_rsint,hd_rsint
	preset_v	hdl_wsint,hd_wsint
	preset_v	hdl_ckrdy,hd_ckrdy
	preset_v	hdl_ckwp,hd_ckwp
	preset_v	hdl_lock,hd_lock
	preset_v	hdl_unlock,hd_unlock
	preset_v	hdl_ststp,inicrd

	link_end	hdl_buff

	section exten

	proc_thg {WIN Control}
	fun40_thg {WIN Control}
	fun_thg {WIN Control}

WIN_use 	proc  {USE }
WIN_drive	proc  {DRIV}
;WIN_start	proc  {STRT}
;WIN_stop	proc  {STOP}
;WIN_remv	proc  {REMV}
WIN_wp		proc  {WPRT}
WIN_safe	proc  {SAFE}
CARD_init	proc  {INIT}
WIN_format	proc  {FRMT}
WIN_drive$	fun40 {DRV$}
WIN_check	proc  {CHEK}
card_renf	proc  {CREN}
card_create	proc  {CREA}
card_speed	fun   {SPED},8
card_dir$	move.l	#16*14,d7
		bsr	fun_thg
		dc.l	'CDIR'

hd_proctab
	proc_stt
	proc_ref WIN_USE
	proc_ref WIN_DRIVE
	proc_ref WIN_CHECK
	proc_ref WIN_WP
	proc_ref WIN_FORMAT
	proc_ref WIN_SAFE
	proc_ref CARD_INIT
	proc_ref CARD_CREATE
	proc_ref CARD_RENF
	proc_end
	proc_stt
	proc_ref WIN_DRIVE$
	proc_ref CARD_DIR$
	proc_ref CARD_SPEEDUP,card_speed
	proc_end

	xref.l	win_vers

	mkcfstart

; win config items
				
	mkcfhead {WIN drives settings},{win_vers}

	  mkcfitem 'Q681',string,0,q68_wn1,,ppr,{Filename for WIN1},0
	  mkcfitem 'Q68B',code,0,q68_drv1,,,\
	  {WIN1_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}

	  mkcfitem 'Q682',string,0,q68_wn2,,ppr,{Filename for WIN2},0
	  mkcfitem 'Q68C',code,0,q68_drv2,,,\
	  {WIN2_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}

	  mkcfitem 'Q683',string,0,q68_wn3,,ppr,{Filename for WIN3},0
	  mkcfitem 'Q68D',code,0,q68_drv3,,,\
	  {WIN3_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}
							
	  mkcfitem 'Q684',string,0,q68_wn4,,ppr,{Filename for WIN4},0
	  mkcfitem 'Q68E',code,0,q68_drv4,,,\
	  {WIN4_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}
					
	  mkcfitem 'Q685',string,0,q68_wn5,,ppr,{Filename for WIN5},0
	  mkcfitem 'Q68F',code,0,q68_drv5,,,\
	  {WIN5_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}

	  mkcfitem 'Q686',string,0,q68_wn6,,ppr,{Filename for WIN6},0
	  mkcfitem 'Q68G',code,0,q68_drv6,,,\
	  {WIN6_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}
	  
	  mkcfitem 'Q687',string,0,q68_wn7,,ppr,{Filename for WIN7},0
	  mkcfitem 'Q68H',code,0,q68_drv7,,,\
	  {WIN7_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}

	  mkcfitem 'Q688',string,0,q68_wn8,,ppr,{Filename for WIN8},0
	  mkcfitem 'Q68K',code,0,q68_drv8,,,\
	  {WIN8_ is on card},0,1,{1},q68.coff,2,{2},$ff,0,{None}

	mkcfblend

	mkcfend


; name of the qxl.win files on the SDHC cards

q68_wn1 dc.w   13,8
	dc.w   'QLWA.WIN',0,0

q68_wn2 dc.w   13,8
	dc.w   'QLWA.WIN',0,0

q68_wn3 dc.w   13,9
	dc.w   'QLWA3.WIN',0

q68_wn4 dc.w   13,9
	dc.w   'QLWA4.WIN',0

q68_wn5 dc.w   13,9
	dc.w   'QLWA5.WIN',0

q68_wn6 dc.w   13,9
	dc.w   'QLWA6.WIN',0

q68_wn7 dc.w   13,9
	dc.w   'QLWA7.WIN',0

q68_wn8 dc.w   13,12
	dc.w   'Q68_SMSQ.WIN'

; these two data areas (¾ and ¿) must be right one after the other.
; now follows a table of 8*16 bytes, for the normalized names of the drives
win_drvs
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0
	dc.l	0,0,0,0

q68_drv1	dc.b	 0		;  win1_ is on card 1
q68_drv2	dc.b	 q68.coff	;  win2_ is on card 2
q68_drv3	dc.b	 -1
q68_drv4	dc.b	 -1
q68_drv5	dc.b	 -1
q68_drv6	dc.b	 -1
q68_drv7	dc.b	 -1
q68_drv8	dc.b	 0

	end
