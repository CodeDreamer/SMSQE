; Q68 DV3 SDHC Control Procedures   V1.00    2017 W. Lenerz

; partially based on

; DV3 Standard Hard Disk Control Procedures   V1.03    1999   Tony Tebby


	section exten

	xdef	hd_thing
	xdef	hd_tname
	xdef	win_wpr

	xref	win_drv
	xref	thp_ostr
	xref	dv3_usep
	xref	dv3_acdef

	include 'dev8_keys_thg'
	include 'dev8_keys_q68'
	include 'dev8_keys_qlv'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

hd_byte dc.w thp.ubyt,0

hd_wdstr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.call+thp.str ; string
	 dc.w	0

hd_bochr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.char+thp.opt ; with optional character
	 dc.w	0


hd_noptm dc.w	thp.ubyt	 ; drive
	 dc.w	thp.ubyt+thp.opt+thp.nnul ; default is set
	 dc.w	0


hdt.reg reg	d1/d2/d3/d5/d6/d7/a0/a1/a2/a3

;+++
; Thing NAME
;---
hd_tname dc.b	0,11,'WIN Control',$a

;+++
; This is the Thing with the WIN extensions
;---
hd_thing

;+++
; WIN_USE xxx
;---
win_use thg_extn {USE },win_format,thp_ostr
	jmp    dv3_usep

	    
;+++
; WIN_FORMAT n, (0|1)
;---
win_format thg_extn {FRMT},win_wpr,hd_noptm
	movem.l hdt.reg,-(sp)

	bsr.s	hdt_prep

	tst.l	(a1)			 ; true or false?
	sne	d5
	neg.b	d5
	bra.s	wwp_do
  
; prepare for simple operation

hdt_prep
	moveq	#8,d0
	move.l	(a1)+,d7		 ; drive number
	beq.s	hdt_ipar4
	cmp.l	d0,d7
	bhi.s	hdt_ipar4
	lea	-ddl_thing(a2),a3
	rts
	

;+++
; WIN_WP n, (0|1)
;---
win_wpr thg_extn {WPRT},win_drive$,hd_noptm
	movem.l hdt.reg,-(sp)

	bsr.s	hdt_prep

	tst.l	(a1)			 ; true or false?
	sne	d5
wwp_do
	move.l	a3,-(sp)
	lea	hdt_sets,a0		 ; set drive undefined
	jsr	dv3_acdef		 ; action on definition
	move.l	(sp)+,a3
hdt_setwp
	add.w	#hdl_wprt-1,d7		 ; set write protect

; set a byte in all blocks

hdt_seta
	move.b	d5,(a3,d7.w)		 ; set flag
	move.l	ddl_slave(a3),a3
	move.l	a3,d0
	bne	hdt_seta
	bra	wd$_exit

hdt_sets
	bne.s	hdt_rok 		 ; ... no drive
	sf	ddf_mstat(a4)		 ; drive changed
hdt_rok
	moveq	#0,d0
	rts

hdt_ipar4
	addq.l	#4,sp
hdt_ipar
	moveq	#err.ipar,d0
	bra	wd$_exit


;+++
; drv$= WIN_DRIVE$ (n)
;---
win_drive$ thg_extn {DRV$},win_drv,hd_wdstr
	movem.l hdt.reg,-(sp)
	move.l	(a1)+,d3		; drve nbr
	ble	wd$_ipar
	cmp.l	#8,d3
	bhi.s	wd$_ipar
	subq.b	#1,d3

	lea	-ddl_thing(a2),a3	; master linkage

	lea	hdl_unit(a3),a2 	; pointer to logical to phys table
	clr.l	d1
	move.b	(a2,d3.w),d1		; card this is on (-1, 0 or q68_coft)

	mulu	#16,d3			; 16 bytes per entry in drv name tble
	move.l	hdl_targ(a3),a2
	add.l	d3,a2			; entry after max length word

	move.w	2(a1),d0		; buffer size
	move.l	4(a1),a1		; point to buffer
	moveq	#14,d3			; max length of name + comma and drv nbr
	cmp.w	d3,d0			; enough space in buffer?
	blt.s	wd$_bffl		; no
	lea	2(a1),a3		; name
	clr.w	d0			; name length
	tst.b	(a2)			; any name there ?
	beq.s	wd$_len 		; no
	moveq	#7,d3			; name part is max of 8 chars
wd$lp1	move.b (a2)+,d2 		; char
	cmp.b	#' ',d2 		; do not copy spaces
	beq.s	nmlp
	move.b	d2,(a3)+
	addq.b	#1,d0			; one more char in name
nmlp	dbf	d3,wd$lp1
	move.b	#'.',(a3)+		; point
	addq.w	#1,d0
; now copy extn
	moveq	#2,d3
extn	move.b (a2)+,d2
	cmp.b	#' ',d2 		; do not copy spaces
	beq.s	extlp
	move.b	d2,(a3)+
	addq.b	#1,d0			; one more char in name
extlp	dbf	d3,extn
wd$_cma move.b	#',',(a3)+
	addq.b	#1,d0
	tst.b	d1
	blt.s	wd$_non
	lsr.w	#q68_dshft,d1		; 0 or 1
	add.w	#$31,d1 		; add 1 and convert to ascii
wd$_drv move.b	d1,(a3)+
	addq.b	#1,d0
wd$_len move.w	d0,(a1)
wd$_done
	moveq	#0,d0
wd$_exit
	movem.l (sp)+,hdt.reg
	rts

wd$_ipar
	moveq	#err.ipar,d0
	bra.s	wd$_exit
		      
wd$_bffl
	moveq	#err.bffl,d0
	bra.s	wd$_exit

wd$_non moveq	#'N',d1
	bra.s	wd$_len
	   
	end
