; DV3 Q40 IDE Write Sector   V3.00     1998	 Tony Tebby
; if LBA FAT32 access : byteswap V3.01  W. Lenerz 2017 Nov 21

	section dv3

	xdef	id_wdirect		; write sector
	xdef	id_wsint		; write sector internal
	xdef	id_wsecs		; write with special key

	xref	id_diradd		; address for direct sector

	xref	id_cmdw
	xref	id_statw

	xref.s	ideo.data
	xref.s	ideo.stat

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_dv3_hd_ide_keys'
	include 'dev8_mac_assert'
	include 'dev8_keys_q40'

drv_led   equ	hdl_freq+1		; use led as drive activity indicator?

;+++
; This routine writes sectors to an IDE disk for direct sector IO
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_wdirect
	assert	ddf.drct-1
	tst.b	ddf_ftype(a4)		 ; real direct access?
	bge.s	id_wsint		 ; ... no
	jsr	id_diradd		 ; scrumple address for direct sector
	bne.s	idw_rts

;+++
; This routine writes sectors to an IDE disk at an even address
;
;	d0 cr	sector number / error code
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_wsint
idw.reg reg	d1
	move.l	d1,-(sp)
	move.l	d0,d1

	assert	hdl.hold,-1
	subq.b	#hdl.acss,hdl_acss(a3)	 ; hold poll

	bsr.s	idw_do			 ; write
	ble.s	idw_done
	bsr.s	idw_retry		 ; try again
	ble.s	idw_done
	bsr.s	idw_retry		 ; try again

idw_done
	beq.s	idw_exrp
idw_mchk
	moveq	#err.mchk,d0
idw_exrp
	addq.b	#hdl.acss,hdl_acss(a3)	 ; restore poll
	move.l	(sp)+,d1
	tst.l	d0
idw_rts
	rts


;******************

idw_retry
idw_do
	moveq	#ide.write,d0

;+++
; This routine writes sectors to an IDE disk (special key)
;
;	d0 cr	command / error code
;	d1 c  p sector address
;	d2 c  p number of sectors
;	d7 c  p drive ID / number
;	a1 c  p address to write from
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
id_wsecs
idws.reg reg	a1/d2/d3/d4/a5
	movem.l idws.reg,-(sp)
	tst.b	ddf_lba(a4)
	bne.s	idw_cmd
	add.l	ddf_dtop-4(a4),d1
idw_cmd jsr	id_cmdw 		; write sector(s)
	bne.s	idws_exit		; command not accepted

idws_sector
	move.l	hdl_1sec(a3),d3
	lsl.l	#2,d3			; allow for run-up
idws_wait
	btst	#ide..drq,ideo.stat(a5) ; wait for data request
	bne.s	idws_copy
	subq.l	#1,d3
	bgt.s	idws_wait
	moveq	#err.mchk,d0
	bra.s	idws_stat

idws_copy
	move.w	#255,d3
	tst.b	ddf_lba(a4)		; byte switch needed?
	bne.s	idws_cloop		; no
idws_lba				; yes
	move.w	(a1)+,d4
	rol.w	#8,d4
	move.w	d4,ideo.data(a5)	; copy data
	dbra	d3,idws_lba
	  
idws_cpok
	subq.b	#1,d2
	bne.s	idws_sector
idws_stat
	jsr	id_statw		; wait for completion

idws_exit
	movem.l (sp)+,idws.reg
	tst.l	d0
	rts

; copy bytes when no byte switch needed
idws_cloop
	move.w	(a1)+,ideo.data(a5)	; copy data
	dbra	d3,idws_cloop
	bra.s	idws_cpok

	end
