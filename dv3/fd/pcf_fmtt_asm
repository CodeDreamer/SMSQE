; DV3 PC Compatible Floppy Disk Format track	   1998     Tony Tebby

	section dv3

	xdef	fd_fmtt 		; format track
	xdef	fd_sfmtt		; format track setup

	xref	fd_cmd_fmtt
	xref	fd_stat
	xref	fd_fint

	xref.l	fdc_stat
	xref.l	fdc_data
	xref.s	fdc.intl

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'
	include 'dev8_mac_assert'

;+++
; Set up format track
;
;	status return 0
;
;---
fd_sfmtt
	moveq	#0,d0
	rts

;+++
; Format track
;
;	d1   s
;	d2   s
;	d7 c  p drive ID / number
;	a0   s
;	a1   s	address to write from
;	a2   s
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.MCHK or conventional error
;
;---
fd_fmtt
	lea	fdc_stat,a0		 ; status register address
	lea	fdc_data-fdc_stat(a0),a2 ; data register address

fdf_retry
	lea	fdl_buff(a3),a1 	 ; format track control table
	moveq	#1,d1			 ; sector number
fdf_wsec
	move.b	fdl_trak(a3),d0
	move.b	d0,(a1)+		 ; set header values
	move.b	fdl_side(a3),(a1)+
	move.b	d1,(a1)+
	move.b	fdf_fslen(a4),(a1)+
	addq.w	#1,d1
	cmp.b	fdf_fstrk(a4),d1	  ; off end of track
	ble.s	fdf_wsec

	move.l	a1,d1			 ; length of info
	lea	fdl_buff(a3),a1
	sub.l	a1,d1
	subq.w	#1,d1			 ; allow for dbra

	moveq	#fdcs.wrd,d2		 ; write status

	move.w	sr,-(sp)
	move.w	#fdc.intl,sr

	jsr	fd_cmd_fmtt
	bne.s	fdf_exsr

	move.l	fdl_1sec(a3),d0

				; gold card timimngs
fdf_wait
	cmp.b	(a0),d2 	;15	 ; write?
	beq.s	fdf_put 	;08/10
	subq.l	#1,d0		;06
	bgt.s	fdf_wait	;10
	bra.s	fdf_fint

fdf_put
	move.b	(a1)+,(a2)	;19
	dbra	d1,fdf_wait	;10
		      ; loop time 54 cycles (3.3 us)
		      ; test to write < 58+4 cycles (<4us)

fdf_stat
	jsr	fd_stat 		 ; wait for status at end of command

fdf_exsr
	move.w	(sp)+,sr
	cmp.w	#fdcs.orun,d0		 ; overrun?
	beq	fdf_retry		 ; ... yes, retry

	tst.l	d0
fdf_wtex
	rts

fdf_fint
	move.w	(sp)+,sr
	jmp	fd_fint 		 ; kill command

	end
