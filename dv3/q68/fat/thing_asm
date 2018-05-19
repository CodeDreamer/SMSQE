; DV3 Standard Hard Disk Control Procedures   V1.03    1999   Tony Tebby
; adapted for Q68			       1.00	2017   W. Lenerz
; 1.01 removed reference to cv_locas (mk)

	section exten

	xdef	fat_thing
	xdef	fat_tname

	xref	thp_ostr
	xref	thp_nrstr
	xref	thp_wd
	xref	dv3_usep
	xref	dv3_acdef

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_q68'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

hd_4byte dc.w	thp.ubyt	 ; two compulsory unsigned byte
	 dc.w	thp.ubyt
	 dc.w	thp.ubyt+thp.opt
	 dc.w	thp.ubyt+thp.opt+thp.nnul
	 dc.w	0

hd_3byte dc.w	thp.ubyt	 ; 3 compulsory unsigned bytes
	 dc.w	thp.ubyt
	 dc.w	thp.ubyt
	 dc.w	0
		    
hd_2byte dc.w	thp.ubyt	 ; compulsory unsigned byte
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

hd_wdstr dc.w	thp.ubyt	 ; drive
	 dc.w	thp.call+thp.str ; string
	 dc.w	0


;+++
; ASCI Thing NAME
;---
fat_tname dc.b	 0,11,'FAT Control',$a

;+++
; This is the Thing with the WIN extensions
;---
fat_thing

;+++
; FAT_USE xxx
;---
fat_use thg_extn {USE },fat_drive,thp_ostr
	jmp	dv3_usep


;+++
; FAT_DRIVE d,c,p	drive (1-8), card (1-2), partition (1-4)
;---
fat_drive thg_extn {DRIV},fat_drive$,hd_3byte
hdt.reg reg	d1/d2/d3/d5/d6/d7/a0/a1/a2/a3

	movem.l hdt.reg,-(sp)
	bsr.s	hdt_doact		 ; call following code as device driver

	bne.s	hdt_setd		 ; no definition
	tst.b	ddf_nropen(a4)		 ; files open?
	bne.s	hdt_inus		 ; yes, can't do it
	sf	ddf_mstat(a4)		 ; drive changed
; here d7 = drive nbr (1 to 8)
hdt_setd
	lea	-ddl_thing(a2),a3	 ; get linkage
	add.w	d7,a3			 ; table entry
	move.l	(a1)+,d1		 ; card
	subq.l	#1,d1			 ; my index starts at 0
	blt.s	hdt_ipar4
	beq.s	hdt_crd
	subq.w	#1,d1			 ; not more than "2" as param!
	bne.s	hdt_ipar4
	addq.l	#1,d1
hdt_crd move.b	d1,hdl_unit-1(a3)	 ; set card
	move.l	(a1)+,d1		 ; partition
	subq.l	#1,d1			 ; must be between 1 and 4
	blt.s	hdt_ipar4
	moveq	#3,d0
	cmp.l	d0,d1
	bgt.s	hdt_ipar4
	addq.l	#1,d1
	move.b	d1,hdl_part-1(a3)	   ; set partition
hdt_rtok
	moveq	#0,d0
	rts

hdt_inus
	moveq	#err.fdiu,d0
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

hdt_ipar4
	addq.l	#4,sp
hdt_ipar
	moveq	#err.ipar,d0
	bra.s	hdt_exit

    
;+++
; drv$=FAT_DRIVE$ (n) return "Card: 0, Partition: 1"
;---
fat_drive$  thg_extn {DRV$},fat_wp,hd_wdstr
	movem.l hdt.reg,-(sp)
	bsr.s	hdt_prep
	move.w	2(a1),d1
	move.l	4(a1),a1
	moveq	#21,d0
	sub.w	d0,d1
	bmi	hdt_ipar
	move.w	d0,(a1)+
	move.l	#'Card',(a1)+
	move.w	#': ',(a1)+
	move.w	#',0',d0
	lea	hdl_unit-1(a3),a2
	move.b	(a2,d7.w),d1		; card (-1,0,q68_coff)
	bge.s	iscrd			; no card, set "N"
nocrd	moveq	#'N'-'0',d1
	bra.s	setcrd
iscrd	lsr.w	#q68_dshft,d1		; 0 or 1
	addq.b	#1,d1			; become 1 or 2
setcrd	add.b	d1,d0
	rol.w	#8,d0
	move.w	d0,(a1)+
	move.l	#' Par',(a1)+
	move.l	#'titi',(a1)+
	move.l	#'on: ',(a1)+
	moveq	#'0',d0
	lea	hdl_part-1(a3),a2
	add.b	(a2,d7.w),d0
	move.b	d0,(a1)+
	movem.l (sp)+,hdt.reg
	moveq	#0,d0
	rts

hdt_prep
	moveq	#8,d0
	move.l	(a1)+,d7		 ; drive number
	beq	hdt_ipar4
	cmp.l	d0,d7
	bhi	hdt_ipar4
	lea	-ddl_thing(a2),a3
	rts

;+++
; FAT_WP n, (0|1)
;---
fat_wp	thg_extn {WPRT},,hd_noptm
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

	end
