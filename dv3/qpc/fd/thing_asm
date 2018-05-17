; QPC Floppy Disk Control Procedures		V3.00	 1993	Tony Tebby
;							  2013	Marcel Kilgus

	section exten

	xdef	fd_thing
	xdef	fd_tname

	xref	dv3_density

	xref	thp_wd
	xref	thp_ostr
	xref	thp_ochr
	xref	thp_nrstr
	xref	dv3_usep
	xref	dv3_acdef

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_smsq_qpc_keys'

fd_2byte dc.w	thp.ubyt		  ; drive (or step)
	 dc.w	thp.ubyt+thp.opt+thp.nnul ;
	 dc.w	0

fd_dstr  dc.w	thp.ubyt	 ; drive
	 dc.w	thp.call+thp.str ; string
	 dc.w	0

;+++
; FLP Thing NAME
;---
fd_tname dc.b	0,11,'FLP Control',$a

;+++
; This is the Thing with the FLP extensions
;---
fd_thing

;+++
; FLP_USE xxx
;---
flp_use thg_extn {USE },flp_drive$,thp_ostr
	jmp	dv3_usep

;+++
; d$ = FLP_DRIVE$ n
;---
flp_drive$ thg_extn {DRV$},flp_drive,thp_nrstr
fd$.reg reg	d7/a1/a3
	movem.l fd$.reg,-(sp)
	move.l	(a1)+,d7
	ble.s	fd$_err
	cmp.l	#8,d7
	bhi.s	fd$_err

	move.w	2(a1),d0		 ; buffer size
	move.l	4(a1),a3

	dc.w	qpc.frdfl		 ; get filename
fd$_exit
	movem.l (sp)+,fd$.reg
	rts

fd$_err
	moveq	#err.ipar,d0
	bra.s	fd$_exit

fdt_inus
	moveq	#err.fdiu,d0
	rts

fdt.reg reg	d1/d2/d3/d5/d6/d7/a0/a1/a2/a3

;+++
; FLP_DRIVE n,f$
;---
flp_drive thg_extn {DRIV},flp_sec,fd_dstr
	movem.l fdt.reg,-(sp)
	bsr.l	fdt_doact		 ; call following code as device driver

	bne.s	fdt_setd		 ; no definition
	tst.b	ddf_nropen(a4)		 ; files open?
	bne.s	fdt_inus
	sf	ddf_mstat(a4)		 ; drive changed
fdt_setd
	move.l	4(a1),a3
	dc.w	qpc.fstfl
	rts


; general do action
fdt_doact
	move.l	(sp)+,a0		 ; action routine
	lea	-ddl_thing(a2),a3	 ; master linkage

	move.l	(a1)+,d7		 ; drive number
	ble.s	fdt_fdnf		 ; ... oops
	cmp.w	#8,d7
	bhi.s	fdt_fdnf

	jsr	dv3_acdef		 ; action on definition
fdt_exit
	movem.l (sp)+,fdt.reg
	rts

fdt_fdnf
	moveq	#err.fdnf,d0		 ; no such drive number
	bra.s	fdt_exit

;+++
; FLP_SEC n
;---
flp_sec thg_extn {SEC },flp_start,thp_wd
	moveq	#1,d0
	cmp.l	(a1),d0 	 ; security > 1
	slo	fdl_sec-ddl_thing(a2)
	moveq	#0,d0
	rts

;+++
; FLP_START
;---
flp_start thg_extn {STRT},flp_track,thp_wd
	move.b	3(a1),fdl_rnup-ddl_thing(a2)
	moveq	#0,d0
	rts

;+++
; FLP_TRACK
;---
flp_track thg_extn {TRAK},flp_density,thp_wd
	move.w	2(a1),fdl_maxt-ddl_thing(a2)
	moveq	#0,d0
	rts

;+++
; FLP_DENSITY
;---
flp_density thg_extn {DENS},flp_step,thp_ochr
fdd.reg reg	d2
	move.l	d2,-(sp)
	moveq	#-1,d2
	move.b	3(a1),d0	     ; name
	beq.s	fdd_dset
	jsr	dv3_density
	bne.s	fdd_ipar
fdd_dset
	move.b	d2,fdl_defd-ddl_thing(a2)
	move.l	(sp)+,d2
	moveq	#0,d0
	rts
fdd_ipar
	move.l	(sp)+,d2
	moveq	#err.ipar,d0
	rts

;+++
; FLP_STEP
;---
flp_step thg_extn {STEP},,fd_2byte
fds.reg reg	d1/a0
	movem.l fds.reg,-(sp)
	lea	fdl_step-ddl_thing(a2),a0

	move.l	(a1)+,d1		 ; drive or step rate
	move.l	(a1),d0 		 ; two params?
	blt.s	fds_all 		 ; ... no

	cmp.b	fdl_maxd-ddl_thing(a2),d1 ; too high?
	bhi.s	fds_ipar
	move.b	d0,-1(a0,d1.l)		  ; set step
	bra.s	fds_ok

fds_all
	moveq	#4,d0
fds_loop
	move.b	d1,(a0)+		 ; set step
	dbra	d0,fds_loop

fds_ok
	moveq	#0,d0
fds_exit
	movem.l (sp)+,fds.reg
	rts

fds_ipar
	moveq	#err.ipar,d0
	bra.s	fds_exit
	end
