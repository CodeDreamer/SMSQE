; DV3 Atari Floppy Disk Hold / Release controller   1998     Tony Tebby

	section dv3

	xdef	fd_hold
	xdef	fd_release

	xref	fd_select

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'

;+++
; This routine is called to take the disk controller
;
;	d0 cr p preserved if OK
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, err.nc, err.mchk
;---
fd_hold
	tas	sys_dmiu(a6)		 ; grab the DMA (also holds poll)
	bne.s	fdh_nc			 ; ... cannot

	bset	#7,fdl_actm(a3) 	 ; hold polling task
	bne.s	fdh_ncd 		 ; cannot

	cmp.b	fdl_selc(a3),d7 	 ; already selected?
	beq.s	fdh_rts
	move.l	d0,-(sp)
	jsr	fd_select		 ; ... no, select it
	bne.s	fdh_erex
	move.l	(sp)+,d0
	cmp.b	d0,d0
fdh_rts
	rts

fdh_ncd
	sf	sys_dmiu(a6)		 ; dma no longer in use
fdh_nc
	moveq	#err.nc,d0		 ; ... but something is
	rts

fdh_erex
	addq.l	#4,sp			 ; removed saved d0
	rts

;+++
; This routine is called to release the disk controller
;
;	d0 c  p
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return accoding to d0
;---
fd_release
	sf	sys_dmiu(a6)		    ; ... dma not in use
	move.b	fdl_apnd(a3),fdl_actm(a3)   ; ... let it go
	tst.l	d0
	rts

	end
