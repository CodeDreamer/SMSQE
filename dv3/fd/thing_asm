; DV3 Standard Floppy Disk Control Procedures	V3.01	 1993	Tony Tebby
;
; 2018-03-09  3.01  Fixed overflow in fd_step, tried to set 5 drives (MK)

	section exten

	xdef	fd_thing
	xdef	fd_tname

	xref	dv3_density

	xref	thp_wd
	xref	thp_ostr
	xref	thp_ochr
	xref	dv3_usep

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

fd_2byte dc.w	thp.ubyt		  ; drive (or step)
	 dc.w	thp.ubyt+thp.opt+thp.nnul ;
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
flp_use thg_extn {USE },flp_sec,thp_ostr
	jmp	dv3_usep

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
	moveq	#3,d0
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
