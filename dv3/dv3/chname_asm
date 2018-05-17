; Setup name of DV3 device channel   V2.00     1996  Tony Tebby

	section dv3

	xdef	dv3_chname

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_mac_assert'

;+++
; DV3 SMSQ format routine
;
;	d2 c  p max length of string
;	d4    s
;	a1 c  p pointer to name buffer
;	a2    s
;	a3 c  u pointer to linkage
;	a4    s
;---
dv3_chname
	move.w	d2,d4
	cmp.w	#d3c.qnml+6,d4		 ; space for name
	ble.s	dcn_ipar		 ; none

	move.l	d3c_ddef(a0),a4 	 ; definition block

	clr.w	(a1)+			 ; no name
	move.l	a1,a2
	move.l	ddl_dname+2(a3),(a2)+	 ; drive name
	move.b	ddf_dnum(a4),d0
	add.b	d0,-1(a2)		 ; + number
	move.b	#'_',(a2)+		 ; _
	subq.w	#5,d4

	lea	d3c_qname(a0),a4
	move.w	(a4)+,d0
	sub.w	d0,d4
	bra.s	dcn_cle

dcn_cloop
	move.b	(a4)+,(a2)+		 ; copy characters of name
dcn_cle
	dbra	d0,dcn_cloop

	bne.s	dcn_dir
	move.b	#'_',-1(a2)		 ; replace final '_' (directory)

	assert	d3c.adir,-1
dcn_dir
	tst.b	d3c_atype(a0)
	bpl.s	dcn_done

	subq.w	#3,d4			 ; enough space for ' ->'?
	blo.s	dcn_done
	move.b	#' ',(a2)+
	move.b	#'-',(a2)+
	move.b	#'>',(a2)+

dcn_done
	sub.l	a1,a2			 ; set length of name
	move.w	a2,-(a1)
	moveq	#0,d0
	rts

dcn_ipar
	moveq	#err.ipar,d0
	rts

	end
