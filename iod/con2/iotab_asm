;	Console IO routine table	V2.01   1998 Tony Tebby
;
; 2016-04-16  2.01  Added iow.salp (MK)

	section con

	include 'dev8_keys_iod'

	xref	cn_io

	xref	cn_test
	xref	cn_fbyt
	xref	cn_fmul
	xref	cn_flin
	xref	cn_edln
	xref	cn_sbyte
	xref	cn_smulc
	xref	cn_pixqy
	xref	cn_chrqy
	xref	cn_defbd
	xref	cn_defwn
	xref	cn_ecurs
	xref	cn_dcurs
	xref	cn_scchr
	xref	cn_scpix
	xref	cn_sccol
	xref	cn_scnwl
	xref	cn_scpcl
	xref	cn_scncl
	xref	cn_scprw
	xref	cn_scnrw
	xref	cn_scral
	xref	cn_scrtp
	xref	cn_scrbt
	xref	cn_panal
	xref	cn_pancl
	xref	cn_pancr
	xref	cn_clral
	xref	cn_clrtp
	xref	cn_clrbt
	xref	cn_clrcl
	xref	cn_clrcr
	xref	cn_fount
	xref	cn_recol
	xref	cn_spap
	xref	cn_sstr
	xref	cn_sink
	xref	cn_sulat
	xref	cn_sovat
	xref	cn_scsiz
	xref	cn_blok
	xref	cn_dopnl
	xref	gw_draw
	xref	gw_scale
	xref	gw_flood
	xref	gw_gcur
	xref	cn_papp
	xref	cn_strp
	xref	cn_inkp
	xref	cn_borp
	xref	cn_papt
	xref	cn_strt
	xref	cn_inkt
	xref	cn_bort
	xref	cn_papn
	xref	cn_strn
	xref	cn_inkn
	xref	cn_born
	xref	cn_blkp
	xref	cn_blkt
	xref	cn_blkn
	xref	cn_palq
	xref	cn_palt
	xref	cn_salp

	xdef	cn_iotab
	xdef	cn.iotab

	include 'dev8_keys_err'

cn..crem equ	0
cn.cremv equ	1
;
cn_iotab
	dc.w	cn_test-cn_iotab		 ; d0=$00
	dc.w	cn_fbyt-cn_iotab
	dc.w	cn_flin-cn_iotab
	dc.w	cn_fmul-cn_iotab
	dc.w	cn_edln-cn_iotab
	dc.w	cn_sbyte-cn_iotab+cn.cremv
	dc.w	cn_smulc-cn_iotab+cn.cremv
	dc.w	cn_smulc-cn_iotab+cn.cremv
	dc.w	cn_unimp-cn_iotab		 ; d0=$08
	dc.w	cn_extop-cn_iotab+cn.cremv
	dc.w	cn_pixqy-cn_iotab
	dc.w	cn_chrqy-cn_iotab
	dc.w	cn_defbd-cn_iotab+cn.cremv
	dc.w	cn_defwn-cn_iotab+cn.cremv
	dc.w	cn_ecurs-cn_iotab
	dc.w	cn_dcurs-cn_iotab
	dc.w	cn_scchr-cn_iotab+cn.cremv	 ; d0=$10
	dc.w	cn_sccol-cn_iotab+cn.cremv
	dc.w	cn_scnwl-cn_iotab+cn.cremv
	dc.w	cn_scpcl-cn_iotab+cn.cremv
	dc.w	cn_scncl-cn_iotab+cn.cremv
	dc.w	cn_scprw-cn_iotab+cn.cremv
	dc.w	cn_scnrw-cn_iotab+cn.cremv
	dc.w	cn_scpix-cn_iotab+cn.cremv
	dc.w	cn_scral-cn_iotab+cn.cremv	 ; d0=$18
	dc.w	cn_scrtp-cn_iotab+cn.cremv
	dc.w	cn_scrbt-cn_iotab+cn.cremv
	dc.w	cn_panal-cn_iotab+cn.cremv
	dc.w	cn_unimp-cn_iotab		 ; $$$$ unused
	dc.w	cn_unimp-cn_iotab		 ; $$$$ unused
	dc.w	cn_pancl-cn_iotab+cn.cremv
	dc.w	cn_pancr-cn_iotab+cn.cremv
	dc.w	cn_clral-cn_iotab+cn.cremv	 ; d0=$20
	dc.w	cn_clrtp-cn_iotab+cn.cremv
	dc.w	cn_clrbt-cn_iotab+cn.cremv
	dc.w	cn_clrcl-cn_iotab+cn.cremv
	dc.w	cn_clrcr-cn_iotab+cn.cremv
	dc.w	cn_fount-cn_iotab
	dc.w	cn_recol-cn_iotab+cn.cremv
	dc.w	cn_spap-cn_iotab
	dc.w	cn_sstr-cn_iotab		 ; d0=$28
	dc.w	cn_sink-cn_iotab
	dc.w	cn_nop-cn_iotab
	dc.w	cn_sulat-cn_iotab
	dc.w	cn_sovat-cn_iotab
	dc.w	cn_scsiz-cn_iotab+cn.cremv
	dc.w	cn_blok-cn_iotab+cn.cremv
	dc.w	cn_dopnl-cn_iotab+cn.cremv
	dc.w	gw_draw-cn_iotab+cn.cremv	  ; d0=$30
	dc.w	gw_draw-cn_iotab+cn.cremv
	dc.w	gw_draw-cn_iotab+cn.cremv
	dc.w	gw_draw-cn_iotab+cn.cremv
	dc.w	gw_scale-cn_iotab
	dc.w	gw_flood-cn_iotab
	dc.w	gw_gcur-cn_iotab+cn.cremv
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab		 ; d0=$38
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab		 ; d0=$40
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab		 ; d0=$48
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_papp-cn_iotab		 ; d0=$50
	dc.w	cn_strp-cn_iotab
	dc.w	cn_inkp-cn_iotab
	dc.w	cn_borp-cn_iotab+cn.cremv
	dc.w	cn_papt-cn_iotab
	dc.w	cn_strt-cn_iotab
	dc.w	cn_inkt-cn_iotab
	dc.w	cn_bort-cn_iotab+cn.cremv
	dc.w	cn_papn-cn_iotab		 ; d0=$58
	dc.w	cn_strn-cn_iotab
	dc.w	cn_inkn-cn_iotab
	dc.w	cn_born-cn_iotab+cn.cremv
	dc.w	cn_blkp-cn_iotab+cn.cremv
	dc.w	cn_blkt-cn_iotab+cn.cremv
	dc.w	cn_blkn-cn_iotab+cn.cremv
	dc.w	cn_unimp-cn_iotab
	dc.w	cn_palq-cn_iotab		 ; d0=$60
	dc.w	cn_palt-cn_iotab
	dc.w	cn_salp-cn_iotab

cn.iotab equ	$62

cn_unimp
	moveq	#err.ipar,d0
	rts
cn_nop
	moveq	#0,d0
	rts
;
cn_extop
	move.l	iod_ioad(a3),-(sp)
	lea	cn_io,a5
	move.l	a5,iod_ioad(a3)
	move.l	a3,-(sp)
	jsr	(a2)
	move.l	(sp)+,a3
	move.l	(sp)+,iod_ioad(a3)
	rts
	end
