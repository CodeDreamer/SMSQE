; DV3 Atari FDC Read sector ID	   1998       Tony Tebby

	section fd

	xdef	fd_rid

	xref	fd_cmd_rid
	xref	fd_stat

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

;+++ 
; Read sector ID (side 0).
; The track number, side, sector and sector length are read into
; the read address work area.
; If the read ID is successful, fdf_ctrk and fdf_slen are set.
;
;	d0  r	0 OK, -ve disaster, + conventional error
;
;	status return 0 ok, -ve timed out, +ve error 
;---
fd_rid
fdri.reg reg	d1/d2/a1
	movem.l fdri.reg,-(sp)
	lea	fdl_buff(a3),a1 	 ; use buffer to read sector ID bytes
	cmp.l	#$1000000,a1		 ; above DMA range?
	blo.s	fdri_do
	move.l	sms.128kb,a1		 ; ... yes, use 128 k buffer
fdri_do
	moveq	#9,d1			 ; try up to 10 times

fdri_loop
	moveq	#fdc.radd,d0		 ; read address
	jsr	fd_cmd_rid
	jsr	fd_stat
	beq.s	fdri_set		 ; OK
	bmi.s	fdri_exit		 ' ... bad
	cmp.w	#fdcs.derr,d0
	dbgt	d1,fdri_loop		 ; ... try again if data error
	bra.s	fdri_exit		 ; bad

fdri_set
; read address command does not seem to DMA data !!!
;	 move.l  (a1),d1
;	 assert  0,fdc_rtrk,fdc_rsln-3
;	 move.b  d1,fdf_slen(a4)	  ; set sector length
;	 move.b  (a1),d1
;	 move.b  d1,fdf_ctrk(a4)	  ; set track
;	 move.w  #dma.ftrk,dma_mode	  ; set track
;	 move.w  d1,dma_data

	move.w	#dma.fsct,dma_mode	 ; get track because read address does
	move.w	dma_data,d1		 ; seem to work
	move.b	d1,fdf_ctrk(a3) 	 ; set track
	move.w	#dma.ftrk,dma_mode	 ; set track
	move.w	d1,dma_data
	tst.b	fdf_slen(a4)		 ; sector length set?
	bgt.s	fdri_exit		 ; ... yes
	move.b	#2,fdf_slen(a4) 	 ; set default sector length

fdri_exit
	tst.l	d0
	movem.l (sp)+,fdri.reg
	rts

	end
