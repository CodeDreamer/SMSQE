; DV3 HDD Control Procedures   V1.00 @ 2020   W. Lenerz
; based on
; DV3 Standard Hard Disk Control Procedures   V1.03    1999   Tony Tebby


	section exten

	xdef	hd_thing
	xdef	hd_tname

	xref	thp_ostr
	xref	thp_nrstr	(in util_thg_parm_asm)
	xref	thp_wd
	xref	dv3_usep
	xref	dv3_acdef

	xref	ut_gxin1
	xref	ut_chkri
	xref	ut_retst

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_java'



hd_bas	 dc.w	thp.ubyt		; compulsory byte =drive
	 dc.w	thp.str+thp.call	; and string
	 dc.w	0


hd_2byte dc.w	thp.ubyt		; compulsory unsigned byte = drive
	 dc.w	thp.ubyt+thp.opt+thp.nnul
	 dc.w	0

hd_bocob dc.w	thp.ubyt		; drive
	 dc.w	thp.char+thp.ubyt	; with byte or character
	 dc.w	0

hd_noptm dc.w	thp.ubyt		; drive
	 dc.w	thp.ubyt+thp.opt+thp.nnul ; default is set
	 dc.w	0

;+++
; ASCI Thing NAME
;---
hd_tname dc.b	0,11,'HDD Control',$a

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
;  drive$=WIN_DRIVE$ (n)		; get native
;---
win_drive$ thg_extn {DRV$},win_drive,thp_nrstr
wd$.reg reg	a1/d7
	movem.l wd$.reg,-(a7)
	move.l	(a1)+,d7
	moveq	#jta.gnam,d0
	dc.w	jva.trpA		; 4(a1) points to buffer (first word= size)
	movem.l (a7)+,wd$.reg
	rts

;+++
; WIN_DRIVE n,f$			; set native filename for drive
;---
win_drive thg_extn {DRIV},win_remv,hd_bas
hdt.reg reg	d1/d2/d3/d5/d6/d7/a0/a1/a2/a3
	movem.l hdt.reg,-(a7)
	bsr.s	hdt_doact		; call following code as device driver

	beq.s	fnd_def 		; definition found
	sub.l	a4,a4			; signal none found
	bra.s	hdt_setd

fnd_def tst.b	ddf_nropen(a4)		; files open?
	bne.s	hdt_inus
	sf	ddf_mstat(a4)		; drive changed

hdt_setd
	lea	-ddl_thing+hdl_remd-1(a2),a3
	move.b	#1,(a3,d7.w)		; not removable
	moveq	#jta.unlk,d0
	dc.w	jva.trpA		; unlock old drive
	moveq	#jta.snam,d0		; set name for new drive (4(a1) = ptr to name)
	dc.w	jva.trpA
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

;+++
; WIN_REMV n, (char)
;---
win_remv thg_extn {REMV},win_wp,hd_bocob
	movem.l hdt.reg,-(sp)
	move.l	(a1),d1 		; param; anything other than 0 = is removable
	sne	d1			; -1 = removable
	bne.s	unlock
	moveq	#jta.lock,d0
	bra.s	setlck

unlock	moveq	#jta.unlk,d0
setlck	dc.w	jva.trpA		; set java lock status

	lea	hdl_remd-1(a3),a0
	move.b	d1,(a0,d7.l)		; set remove flag
hdt_undef
	lea	hdt_sets,a0		; set drive undefined
	pea	hdt_exit
	jmp	dv3_acdef		; action on definition

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
; WIN_FORMAT n, (0|1)
;---
win_format thg_extn {FRMT},,hd_noptm
	movem.l hdt.reg,-(sp)

	bsr.s	hdt_prep

	tst.l	(a1)			 ; true or false?
	sne	d5
	neg.b	d5
	bra.s	wwp_do
	  
	end
