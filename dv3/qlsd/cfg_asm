	section config

	xref	qsd_cf1
	xref	qsd_cf2
	xref	qsd_cf3
	xref	qsd_cf4
	xref	qsd_cf5
	xref	qsd_cf6
	xref	qsd_cf7
	xref	qsd_cf8
	xref	qsd_crd

	xref.l	qlsd_vers

	include 'dev8_keys_err'
	include 'dev8_mac_MultiConfig02'

	mkcfstart

	 mkcfhead {QLSD},{qlsd_vers}

	   mkcfitem 'QSD1',string,0,qsd_cf1,,ppr,{Filename for WIN1},0
	   mkcfitem 'QSD2',string,0,qsd_cf2,,ppr,{WIN2},0
	   mkcfitem 'QSD3',string,0,qsd_cf3,,ppr,{WIN3},0
	   mkcfitem 'QSD4',string,0,qsd_cf4,,ppr,{WIN4},0
	   mkcfitem 'QSD5',string,0,qsd_cf5,,ppr,{WIN5},0
	   mkcfitem 'QSD6',string,0,qsd_cf6,,ppr,{WIN6},0
	   mkcfitem 'QSD7',string,0,qsd_cf7,,ppr,{WIN7},0
	   mkcfitem 'QSD8',string,0,qsd_cf8,,ppr,{WIN8},0

card	   mkcfitem 'QSDJ',code,0,qsd_crd,,,\
	   {WIN1 is on card}\
	   1,1,{1},2,2,{2}
	   mkcfitem 'QSDK',code,0,qsd_crd+1,,,{WIN2 is on card},.card
	   mkcfitem 'QSDL',code,0,qsd_crd+2,,,{WIN3 is on card},.card
	   mkcfitem 'QSDM',code,0,qsd_crd+3,,,{WIN4 is on card},.card
	   mkcfitem 'QSDN',code,0,qsd_crd+4,,,{WIN5 is on card},.card
	   mkcfitem 'QSDO',code,0,qsd_crd+5,,,{WIN6 is on card},.card
	   mkcfitem 'QSDP',code,0,qsd_crd+6,,,{WIN7 is on card},.card
	   mkcfitem 'QSDQ',code,0,qsd_crd+7,,,{WIN8 is on card},.card
	 mkcfblend

	mkcfend

; post processing routine to make sure that a name is a valid 8.3 name
ppr	tst.b	d1
	beq.s	p_ok
	addq.l	#2,a0			; point to item name
	move.l	a0,d7
	move.w	(a0)+,d2		; length
	beq.s	p_ok			; 0 length is fine
	cmp.w	#12,d2			; max chars allowed (8+3+extn)
	bgt.s	ipar			; too many
	subq.w	#1,d2			; prepare for dbf
	moveq	#'.',d5 		; compare with this
	moveq	#0,d6			; nbr of chars compared till now
p_lp1	cmp.b	(a0)+,d5		; try to find extension marker
	beq.s	extn			; found it, check name
	addq.l	#1,d6			; new number of chars checked
	dbf	d2,p_lp1		; check all chars
; if we get here, there is no extension
	subq.w	#8,d6			; max nbr of chars w/o extn allowed
	ble.s	p_ok			; correct name w/ extn
	bra.s	ipar			; wrong name
; d2 nbr of chars remaining
; d6 nbr of chars before extension
extn
	subq.w	#8,d6			; nbr of chars got before extn
	bgt.s	ipar			; too many chars before the extension
	cmp.w	#3,d2			; how many chars remaining for the extn?
	bgt.s	ipar			; too many!
p_ok	moveq	#0,d0
	rts
ipar	moveq	#err.inam,d0
	rts

	end
