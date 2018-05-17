; Copied from QPAC II base		V1.00	   1988  Tony Tebby  QJUMP

	section base

;	 xdef	 qp_thing
	xdef	ini_error

;	 xref	 bp_thing
;	 xref	 bs_thing
;	 xref	 btp_thing
;	 xref	 ch_thing
;	 xref	 ex_thing
;	 xref	 fl_thing
;	 xref	 hk_thing
;	 xref	 jb_thing
;	 xref	 pk_thing
;	 xref	 rj_thing
;	 xref	 tg_thing
;	 xref	 sy_thing
;
	xref	bt_name 	 ; include button frame name
	xref	bt_init 	 ; and initialisation code
;	 xref	 btx_name	  ; and button extensions
;	 xref	 men_clm2
;	 xref	 men_wake
;	 xref	 men_exec
;	 xref	 men_ferr
;	 xref	 men_fex3

;	 xref	 qp_addth
;	 xref	 qp_gwman
;	 xref	 gu_fclos

;	 xref.l  qp2_vers

	bra.l	qp_init
	section language
;	 section qp_thing
;qp_thing
	section init
qp_init
;	 jsr	 qp_gwman		  ; make sure we have Window Manager
;	 bne.l	 qpi_rts
;	 jsr	 qp_addth		  ; add all our standard things
;	 bne.l	 ini_error
;
	section init_rts

ini_error
;	 jsr	 gu_fclos

qpi_rts
	rts
	end
