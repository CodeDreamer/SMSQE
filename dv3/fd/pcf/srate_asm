; DV3 PC Compatible Floppy Disk Set Step Rate	 1998	   Tony Tebby

	section fd

	xdef	fd_srate		; set step rate
	xdef	fd_srmin		; set minimum step rate
	xdef	fd_srred		; reduce step rate
	xdef	fd_srredp		; reduce step rate permanently
	xdef	fd_srrest		; restore step rate
	xdef	fd_srtemp		; re-set temporary step rate

	xref	fd_cmd_spec

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'

;+++
; Set step rate
;
;	d7 c  p drive id / number
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srtemp
	move.w	#fdl_tstep-1,d0 	  ; get temporary step rate
	add.w	d7,d0
	move.b	(a3,d0.w),d0
	bra.s	fdr_set

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
	move.b	(a3,d0.w),d0
fdr_set
	bsr.s	fds_rate
	jmp	fd_cmd_spec		 ; specify

;+++
; Set minimum step rate
;
;	a3 c  p linkage block
;	a4 c  p drive definition block
;
;	status return OK
;---
fd_srmin
	moveq	#0,d0			 ; this is minimum
	jmp	fd_cmd_spec		 ; specify

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
	cmp.w	#30,d0			 ; already slow?
	bge.s	fds_mchk		 ; cannot reduce
	move.b	fds_slow(pc,d0.w),d0	 ; next slower step rate
	swap	d1
	move.w	d0,d1			 ; save it

	bsr.s	fds_rate		 ; calculate rate
	bcc.s	fds_mchk		 ; too large

	jsr	fd_cmd_spec		 ; set it

	move.b	d1,d0
	swap	d1
	move.b	d0,(a3,d1.w)		 ; new temporary step rate
	moveq	#0,d0

fds_exd1
	move.l	(sp)+,d1
	tst.l	d0
	rts

fds_mchk
	moveq	#err.mchk,d0
	bra.s	fds_exd1



fds_rate
	cmp.b	#ddf.hd,ddf_density(a4)  ; density?
	beq.s	fds_sstp		 ; ... HD, step specified in ms
	blt.s	fds_sdd 		 ; ... DD, step specified in 2 ms
	add.b	d0,d0			 ; ... ED, step specified in 0.5 ms
	bra.s	fds_sstp
fds_sdd
	addq.b	#1,d0
	lsr.b	#1,d0			 ; DD, steps are doubled
fds_sstp
	neg.b	d0
	asl.b	#3,d0			 ; in ms 4 bits
	bvc.s	fds_step		 ; ... all bits shifted out = 1
	moveq	#0,d0			 ; ... min step rate
fds_step
	add.b	d0,d0
	rts

fds_slow dc.b	1,2,3,4,6,6,9,9,9,12,12,12,15,15,15,20,20,20,20,20
	 dc.b	30,30,30,30,30,30,30,30,30,30,30
	end
