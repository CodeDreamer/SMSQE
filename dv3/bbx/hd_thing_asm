; Bytebox Hard Disk Control Procedures	 V1.04	  1999   Tony Tebby

	section exten

	xdef	hd_thing
	xdef	hd_tname

	xref	thp_ostr
	xref	thp_nrstr
	xref	thp_wd
	xref	dv3_usep
	xref	dv3_acdef

	xref	cv_locas

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_scsi'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

hd_4byte dc.w	thp.ubyt	 ; two compulsory unsigned byte
	 dc.w	thp.ubyt
	 dc.w	thp.ubyt+thp.opt
	 dc.w	thp.ubyt+thp.opt+thp.nnul
	 dc.w	0

hd_byte  dc.w	thp.ubyt
	 dc.w	0

hd_bochr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.char+thp.opt ; with optional character
	 dc.w	0

hd_noptm dc.w	thp.ubyt	 ; drive
	 dc.w	thp.ubyt+thp.opt+thp.nnul ; default is set
	 dc.w	0

;+++
; ASCI Thing NAME
;---
hd_tname dc.b	0,11,'WIN Control',$a

;+++
; This is the Thing with the WIN extensions
;---
hd_thing

;+++
; WIN_USE xxx
;---
win_use thg_extn {USE },win_drive$,thp_ostr
	jmp	dv3_usep

;+++
;  WIN_DRIVE$ n,d$
;---
win_drive$ thg_extn {DRV$},win_drive,thp_nrstr
wd$.reg reg	d1/d2/d3/a1/a2/a3
	movem.l wd$.reg,-(sp)
	move.l	(a1)+,d3
	beq.s	wd$_err
	subq.l	#8,d3
	bhi.s	wd$_err
	move.l	4(a1),a1
	clr.w	(a1)+			 ; null string

wd$_done
	moveq	#0,d0
wd$_exit
	movem.l (sp)+,wd$.reg
	rts

wd$_err
	moveq	#err.ipar,d0
	bra.s	wd$_exit

;+++
; WIN_DRIVE n,t,u,p
;---
win_drive thg_extn {DRIV},win_start,hd_4byte
hdt.reg reg	d1/d2/d3/d5/d6/d7/a0/a1/a2/a3

hdt_rtok
	moveq	#0,d0
	rts

hdt_inus
	moveq	#err.fdiu,d0
	rts

;+++
; WIN_START n
;---
win_start thg_extn {STRT},win_stop,hd_byte
	move.l	#scc.strt,d0		 ; start
	bra.s	hdt_stst

;+++
; WIN_STOP n
;---
win_stop thg_extn {STOP},win_remv,hd_byte
	move.l	#scc.stop,d0		 ; stop
hdt_stst
	movem.l hdt.reg,-(sp)

	move.l	d0,d5			 ; start or stop
	bsr.s	hdt_doact		 ; call following code as device driver
	move.l	d3,d0			 ; defined?
	beq.s	hdt_rts 		 ; ... no
	move.l	d5,d3			 ; start / stop parameters
	jsr	hdl_ststp(a3)		 ; start or stop
	moveq	#0,d0			 ; ignore error
hdt_rts
	rts


; general do action

hdt_doact
	move.l	(sp)+,a0		 ; action routine
	lea	-ddl_thing(a2),a3	 ; master linkage

	move.l	(a1)+,d7		 ; drive number
	ble.s	hdt_fdnf		 ; ... oops
	cmp.w	#8,d7
	bhi.s	hdt_fdnf
	move.w	#hdl_part-1,d3
	add.l	d7,d3			 ; offset  in linkage

hdt_actest
	tst.b	(a3,d3.w)		 ; any partition?
	bge.s	hdt_acdef
	move.l	ddl_slave(a3),a3	 ; another slave
	move.l	a3,d0
	bne.s	hdt_actest
	lea	-ddl_thing(a2),a3	 ; master linkage
	moveq	#0,d3			 ; no partition

hdt_acdef
	jsr	dv3_acdef		 ; action on definition

hdt_exit
	movem.l (sp)+,hdt.reg
	rts

hdt_fdnf
	moveq	#err.fdnf,d0		 ; no such drive number
	bra.s	hdt_exit

;+++
; WIN_REMV n, (char)
;---
win_remv thg_extn {REMV},win_wp,hd_bochr

	moveq	#0,d0
	rts

hdt_ipar4
	addq.l	#4,sp
hdt_ipar
	moveq	#err.ipar,d0
	bra.s	hdt_exit

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
; WIN_FORMAT, (0|1)
;---
win_format thg_extn {FRMT},win_slug,hd_noptm
	movem.l hdt.reg,-(sp)

	bsr.s	hdt_prep

	tst.l	(a1)			 ; true or false?
	sne	d5
	neg.b	d5
	bra.s	wwp_do

;+++
; WIN_WP n, (0|1)
;---
win_wp	thg_extn {WPRT},win_format,hd_noptm
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
	bra	hdt_exit

hdt_sets
	bne.s	hdt_rok 		 ; ... no drive
	sf	ddf_mstat(a4)		 ; drive changed
hdt_rok
	moveq	#0,d0
	rts


;+++
; WIN_SLUG n
;---
win_slug thg_extn {SLUG},,hd_byte
	moveq	#0,d0
	rts
	end
