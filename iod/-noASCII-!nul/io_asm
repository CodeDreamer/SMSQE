; NUL IO operations  V2.01     1989  Tony Tebby   QJUMP

	section nul

	xdef	nul_io

	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_k'
	include 'dev8_iod_nul_data'

;+++
; NUL device IO operations.
;---
nul_io
	move.w	nlc_key(a0),d7		 ; action key
	blt.s	nli_nc
	add.w	d0,d0
	move.w	nli_tab(pc,d0.w),d0
	jmp	nli_tab(pc,d0.w)

nli_nc
	moveq	#err.nc,d0
	rts

nli_tab
	dc.w	nli_test-nli_tab
	dc.w	nli_fbyt-nli_tab
	dc.w	nli_flin-nli_tab
	dc.w	nli_fmul-nli_tab
	dc.w	nli_elin-nli_tab
	dc.w	nli_sbyt-nli_tab
	dc.w	nli_smul-nli_tab
	dc.w	nli_smul-nli_tab
;  $08
	dc.w	nli_ipar-nli_tab
	dc.w	nli_xtop-nli_tab
	dc.w	nli_pixq-nli_tab
	dc.w	nli_chrq-nli_tab
	dc.w	nli_defb-nli_tab
	dc.w	nli_defw-nli_tab
	dc.w	nli_ecur-nli_tab
	dc.w	nli_dcur-nli_tab
;  $10
	dc.w	nli_scur-nli_tab
	dc.w	nli_scol-nli_tab
	dc.w	nli_newl-nli_tab
	dc.w	nli_pcol-nli_tab
	dc.w	nli_ncol-nli_tab
	dc.w	nli_prow-nli_tab
	dc.w	nli_nrow-nli_tab
	dc.w	nli_spix-nli_tab
;  $18
	dc.w	nli_scra-nli_tab
	dc.w	nli_scrt-nli_tab
	dc.w	nli_scrb-nli_tab
	dc.w	nli_pana-nli_tab
	dc.w	nli_pant-nli_tab
	dc.w	nli_panb-nli_tab
	dc.w	nli_panl-nli_tab
	dc.w	nli_panr-nli_tab
;  $20
	dc.w	nli_clra-nli_tab
	dc.w	nli_clrt-nli_tab
	dc.w	nli_clrb-nli_tab
	dc.w	nli_clrl-nli_tab
	dc.w	nli_clrr-nli_tab
	dc.w	nli_font-nli_tab
	dc.w	nli_rclr-nli_tab
	dc.w	nli_spap-nli_tab
;  $28
	dc.w	nli_sstr-nli_tab
	dc.w	nli_sink-nli_tab
	dc.w	nli_sfla-nli_tab
	dc.w	nli_sula-nli_tab
	dc.w	nli_sova-nli_tab
	dc.w	nli_ssiz-nli_tab
	dc.w	nli_blok-nli_tab
	dc.w	nli_donl-nli_tab
;  $30
	dc.w	nli_dot-nli_tab
	dc.w	nli_line-nli_tab
	dc.w	nli_arc-nli_tab
	dc.w	nli_elip-nli_tab
	dc.w	nli_scal-nli_tab
	dc.w	nli_fill-nli_tab
	dc.w	nli_sgcr-nli_tab
	dc.w	nli_ipar-nli_tab
;  $38
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
;  $40
	dc.w	nli_chek-nli_tab
	dc.w	nli_flsh-nli_tab
	dc.w	nli_posa-nli_tab
	dc.w	nli_posr-nli_tab
	dc.w	nli_posv-nli_tab
	dc.w	nli_minf-nli_tab
	dc.w	nli_shdr-nli_tab
	dc.w	nli_rhdr-nli_tab
;  $48
	dc.w	nli_load-nli_tab
	dc.w	nli_save-nli_tab
	dc.w	nli_rnam-nli_tab
	dc.w	nli_trnc-nli_tab
	dc.w	nli_date-nli_tab
	dc.w	nli_mkdr-nli_tab
	dc.w	nli_vers-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_xinf-nli_tab
;  $50
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
;  $58
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
;  $60
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
;  $68
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_ipar-nli_tab
	dc.w	nli_flim-nli_tab
	dc.w	nli_svpw-nli_tab
	dc.w	nli_rspw-nli_tab
	dc.w	nli_slnk-nli_tab
;  $70
	dc.w	nli_pinf-nli_tab
	dc.w	nli_rptr-nli_tab
	dc.w	nli_rpxl-nli_tab
	dc.w	nli_wblb-nli_tab
	dc.w	nli_lblb-nli_tab
	dc.w	nli_null-nli_tab
	dc.w	nli_wspt-nli_tab
	dc.w	nli_spry-nli_tab
;  $78
	dc.w	nli_flim-nli_tab
	dc.w	nli_null-nli_tab
	dc.w	nli_outl-nli_tab
	dc.w	nli_sptr-nli_tab
	dc.w	nli_pick-nli_tab
	dc.w	nli_swdf-nli_tab
	dc.w	nli_wsav-nli_tab
	dc.w	nli_wrst-nli_tab

nli_key
	move.w	d7,d0			 ; check key
	beq.s	nli_ip4   
	subq.w	#nlc.zero,d0		 ; < null file, = zero, > blank lines
	bge.s	nli_rts
	addq.l	#4,sp
nli_eof
	moveq	#err.eof,d0
nli_rts
	rts
nli_ip4
	addq.w	#4,sp			 ; remove return
nli_xtop
nli_slnk
nli_pinf
nli_rptr
nli_ipar
	moveq	#err.ipar,d0		 ; bad parameter
	rts

nli_test	 ; fetch byte
nli_fbyt
nli_rpxl
	bsr.s	nli_key 		 ; check key
	bgt.s	nli_nl
	moveq	#0,d1			 ; ... zero
	moveq	#0,d0
	rts

nli_nl
	moveq	#k.nl,d1		 ; ... newline
	moveq	#0,d0
	rts

nli_elin	 ; edit line
	move.w	d2,d7			 ; keep buffer length
	sub.w	d1,d2			 ; ... just get this much
	bsr.s	nli_key
	bsr.s	nli_fli1		 ; fetch line
	move.w	d7,d1
	swap	d1
	move.w	d7,d1			 ; set cursor to end of line
	rts

nli_flin	 ; fetch line
	bsr.s	nli_key
nli_fli1
	beq.s	nli_zlbf		 ; fill with zeros
	moveq	#1,d1			 ; line one character long
	move.b	#k.nl,(a1)+
	rts

nli_zlbf
	moveq	#err.bffl,d0
	ext.l	d2			 ; fill is long word
	moveq	#0,d3			 ; fill with zeros
	bra.s	nli_fd3

nli_rhdr	 ; read header
	tst.w	d7			 ; is it possible?
	beq.s	nli_ipar		 ; ... no
	ext.l	d2			 ; ... yes, fill with zeros
	bra.s	nli_zfok

nli_fmul	 ; fetch multiple and load
	ext.l	d2
nli_load
	move.l	#$0a0a0a0a,d3		 ; blank lines
	bsr.s	nli_key 		 ; check key
	bne.s	nli_fok 		 ; ... it is.
nli_zfok
	moveq	#0,d3			 ; zeros
nli_fok
	moveq	#0,d0			 ; return OK
nli_fd3
	moveq	#1,d6			 ; sub.l reg is quicker than subq.l
	move.l	d2,d1			 ; total returned
	move.l	a1,d4			 ; check for odd address
	lsr.w	#1,d4
	bcs.s	nli_filb		 ; fill with bytes

	moveq	#32,d5
	moveq	#$1c,d4 		 ; mask of long words in 16 bytes
	and.w	d2,d4
	sub.l	d4,d7
	neg.w	d4
	asr.w	#1,d4			 ; +0, 4, 8, bytes
	jmp	nli_fmxe(pc,d4.w)

nli_fmxl
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.l	d3,(a1)+
nli_fmxe
	sub.l	d5,d2			 ; 32 bytes gone
	bge.s	nli_fmxl
	add.l	d5,d2
	bra.s	nli_filb

nli_flbl
	move.b d3,(a1)+
nli_filb
	sub.l	d6,d2
	bge.s	nli_flbl
	rts

nli_pixq	 ; window enquiries
nli_chrq
nli_flim
	clr.l	(a1)			 ; four word information block
	clr.l	4(a1)
	bra.s	nli_ok

nli_posv
	tst.b	d2			 ; versatile position
	bpl.s	nli_posa		 ; ... absolute

nli_posr	 ; file position
	add.l	nlc_pos(a0),d1		 ; relative position
nli_posa
	cmp.w	#nlc.file,d7		 ; ... is it null file?
	beq.s	nli_seof		 ; ... yes
	move.l	d1,nlc_pos(a0)		 
	bra.s	nli_ok
nli_seof
	moveq	#0,d1
	moveq	#err.eof,d0
	rts

nli_minf
	moveq	#0,d1			 ; no room
	move.l	#'    ',d3
	move.l	d3,(a1)+
	move.l	d3,(a1)+
	move.w	d3,(a1)+
	bra.s	nli_ok

nli_xinf
	moveq	#64/4-1,d3		 ; null extended info
	move.l	a1,a2
nli_xloop
	clr.l	(a2)+
	dbra	d3,nli_xloop
	bra.s	nli_ok

nli_date	 ; date and version
nli_vers
	moveq	#0,d1
	bra.s	nli_ok

nli_smul	 ; send multiple
	move.w	d2,d1
	add.w	d2,a1
;****	bra.s	nli_ok

nli_sbyt
nli_defb
nli_defw
nli_ecur
nli_dcur
nli_scur
nli_scol
nli_newl
nli_pcol
nli_ncol
nli_prow
nli_nrow
nli_spix
nli_scra
nli_scrt
nli_scrb
nli_pana
nli_pant
nli_panb
nli_panl
nli_panr
nli_clra
nli_clrt
nli_clrb
nli_clrl
nli_clrr
nli_font
nli_rclr
nli_spap
nli_sstr
nli_sink
nli_sfla
nli_sula
nli_sova
nli_ssiz
nli_blok
nli_donl
nli_dot
nli_line
nli_arc
nli_elip
nli_scal
nli_fill
nli_sgcr
nli_chek
nli_flsh
nli_shdr
nli_save
nli_rnam
nli_trnc
nli_mkdr
nli_svpw
nli_rspw
nli_wblb
nli_lblb
nli_wspt
nli_spry
nli_outl
nli_sptr
nli_pick
nli_swdf
nli_wsav
nli_wrst

nli_null
nli_ok
	moveq	#0,d0
	rts
	end
