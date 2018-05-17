; Initialise vector area   V2.01     1992 Tony Tebby

	section init

	xdef	init_vec
	xdef	init_wbase

	xref	init_exv

	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_keys_68000'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_mac_assert'

vec	macro	routine
	xref	[routine]
	dc.w	[routine]-*
	endm

iv_veca0
	move.w	#jmp.l,d0		 ; jmp
	bsr.s	iv_wbase
init_wbase
iv_wba0
	move.l	a0,d0			 ; to a0
iv_wbd0
	swap	d0
	bsr.s	iv_wbase
	swap	d0
iv_wbase
	jmp	sms.wbase


;+++
; Initialise the whole vector area
;---
init_vec
	lea	sms_uvect,a0
	lea	exv_accf,a5		 ; bus error is first settable vector
	lea	exv_top,a2

iv_pres
	bsr.s	iv_wba0 		 ; preset all
	cmp.l	a2,a5
	blo.s	iv_pres

	assert	sms.sysb,exv_top
	lea	ini_sysb,a0
	bsr.s	iv_wba0 		 ; set system base

; the basic area is filled in, now put in the soft reset vectors

	move.l	#sms.resl,d0		 ; long branch
	lea	sms.res,a5
	bsr.s	iv_wbd0

	lea	sms.rese,a5		 ; and lots of short branches
	moveq	#sms.resn-1,d1
	move.w	#sms.ress,d0		 ; the branch

iv_res
	bsr.s	iv_wbase
	subq.b	#2,d0			 ; next is further back
	dbra	d1,iv_res

; now the nasty bit, the QL vectors

	lea	iv_vector,a2

iv_qlva
	move.w	(a2)+,d2		 ; next group
	beq.l	init_exv		 ; done, do the exception vectors
	move.w	(a2)+,d1		 ; offset
	move.w	d2,a1			 ; ql vector
	add.w	d2,d2
	lea	qlv.off(a1),a5		 ; staging post for this vector
	add.w	d2,a5			 ; = offset + 4* QLV
iv_qlv
	move.l	a2,a0
	move.w	(a2)+,d0
	beq.s	iv_qlva

	add.w	d0,a0			 ; address of routine

	move.w	d1,d0
	add.w	a5,d0			 ; offset vector

	exg	a1,a5
	bsr.s	iv_wbase		 ; set vector
	exg	a1,a5

	bsr.s	iv_veca0		 ; and staging post
	bra.s	iv_qlv

iv_vector

; vectored utilities

; Common heap management

	dc.w	mem.achp,0

 vec mem_achp	 $00c0		; Allocate space in Common HeaP
 vec mem_rchp	 $00c2		; Return space to Common HeaP

	dc.w	0

; General memory management

	dc.w	mem.llst,0

 vec mem_llst	 $00d2		; Link into LiST
 vec mem_rlst	 $00d4		; Remove from LiST

 vec mem_rlst	 $00d6		; should be date, will be overwritten

 vec mem_alhp	 $00d8		; ALlocate in HeaP
 vec mem_rehp	 $00da		; REturn to HeaP

; Queue handling utilities

 vec ioq_setq	 $00dc		; SET up a Queue in standard form
 vec ioq_test	 $00de		; TEST a queue for pending byte / space available
 vec ioq_pbyt	 $00e0		; Put a BYTe into a queue
 vec ioq_gbyt	 $00e2		; Get a BYTe out of a queue
 vec ioq_seof	 $00e4		; Set EOF in queue

; string utilities

 vec uq_ssq	 $00e6		; should be Compare STRings, will be overwritten

; Standard serial IO

 vec uq_ssq	 $00e8		; Standard Serial Queue handling
 vec uq_ssio	 $00ea		; Standard Serial IO

	dc.w	0

; IO
	dc.w	iou.dnam,0

 vec iou_dnam	 $0122		; decode Device NAMe

	dc.w	0

; Microdrive

	dc.w	md.read,-$4000

 vec  md_read	 $0124		; read a sector
 vec  md_write	 $0126		; write a sector
 vec  md_verif	 $0128		; verify a sector
 vec  md_rdhdr	 $012a		; read sector header

	dc.w	0
	dc.w	0

sms_uvect
	dc.l	$4afbedeb
	bra.s	sms_uvect
	dc.w	'Undefined Vector'
	end
