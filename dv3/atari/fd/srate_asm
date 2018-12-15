; DV3 Atari Floppy Disk Set Step Rate	 1998	   Tony Tebby

	section fd

	xdef	fd_srate		; set step rate
	xdef	fd_srmin		; set minimum step rate
	xdef	fd_srred		; reduce step rate
	xdef	fd_srredp		; reduce step rate permanently
	xdef	fd_srrest		; restore step rate

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; Set step rate
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srate
;+++
; Restore step rate
;
;	d7 c  p drive id / number
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srrest
	move.w	#fdl_step-1,d0		 ; get step rate
	add.w	d7,d0
	move.b	(a3,d0.w),fdl_tstep-fdl_step(a3,d0.w) ; reset temporary step rate
	moveq	#0,d0
	rts

;+++
; Set minimum step rate
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srmin
	move.w	#fdl_tstep-1,d0 	 ; set temporary step rate
	add.w	d7,d0
	move.b	#6,(a3,d0.w)		 ; to 6
	moveq	#0,d0
	rts

;+++
; Reduce step rate permanently
;
;	d7 c  p drive id / number
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srredp
	move.w	#fdl_step-1,d0
	add.w	d7,d0
	move.b	fdl_tstep-fdl_step(a3,d0.w),(a3,d0.w) ; set step rate
	moveq	#0,d0
	rts

;+++
; Reduce step rate
;
;	d7 c  p drive id / number
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srred
	move.l	d1,-(sp)
	moveq	#0,d0
	move.w	#fdl_tstep-1,d1
	add.w	d7,d1
	move.b	(a3,d1.w),d0		 ; temporary step rate
	cmp.w	#6,d0			 ; already slow?
	bhs.s	fdsrr_mchk		 ; cannot reduce
	move.b	fds_slow(pc,d0.w),(a3,d1.w) ; next slower step rate

	move.l	(sp)+,d1
	moveq	#0,d0
	rts

fdsrr_mchk
	move.l	(sp)+,d1
	moveq	#err.mchk,d0
	rts

fds_slow dc.b	1,2,3,6,6,6

	end
