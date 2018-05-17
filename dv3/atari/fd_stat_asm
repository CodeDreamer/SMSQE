; DV3 Atari FDC check status   V2.00    1991  Tony Tebby

	section fd

	xdef	fd_stat 		; check status of FDC at end of command
	xdef	fd_stat_seek		; check status of seek
	xdef	fd_stat_recal		; check status of recalibrate
	xdef	fd_fint 		; force interrupt (status ERR.MCHK)

	xref	at_wait80
	xref	at_pd0u
	xref	fd_cmd

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'
	include 'dev8_mac_assert'

;+++
; This routine waits for a seek command to complete.
;
;	status return 0, ERR.MCHK or conventional error
;---
fd_stat_seek
fd_stat_recal
	move.w	#fds.skok,-(sp) 	 ; seek status bits
	bra.s	fds_do

;+++
; This routine waits for a seek command to complete.
;
;	status return 0, ERR.MCHK or conventional error
;---
fd_stat
	move.w	#fds.rwok,-(sp) 	 ; read / write status bits

fds_do
	moveq	#127,d0 		 ; 127 times = 2.5 seconds
fds_swait
	swap	d0
	move.w	#-15,d0 		 ; wait 241*83 == 20ms
	jsr	at_wait80
	swap	d0

fds_sttw
;;;;	   move.w  #dma.fstt,dma_mode	    ; for busy (no interrupt on error)
;;	  move.w  dma_data,d0
;;	  btst	  #fds..bsy,d0
;;	  beq.s   fds_sttr
	btst	#mfp..dmi,mfp_dmi	 ; for interrupt
	beq.s	fds_sttr
	btst	#mfp..tai,mfp_tapi	 ; or timeout
	beq.s	fds_sttw
	move.b	#mfp.tapc,mfp_tapi
	dbra	d0,fds_swait		 ; and again

fds_sttr
;;;;	    move.w  #dma.fstt,dma_mode	     ; get status reg
	move.w	(sp)+,d0
	and.w	dma_data,d0
	beq.s	fds_ok			 ; ... no
	btst	#fds..bsy,d0		 ; still busy?
	bne.s	fds_timeout		 ; ... yes

	btst	#fds..los,d0		 ; overrun?
	bne.s	fds_overrun		 ; ... yes

	btst	#fds..crc,d0		 ; CRC error?
	bne.s	fds_data_error		 ; .. yes

	moveq	#fdcs.nadd,d0		 ; none of above, no address mark
	bra.s	fds_exit

fds_overrun
	moveq	#fdcs.orun,d0
	bra.s	fds_exit

fds_data_error
	moveq	#fdcs.derr,d0
	bra.s	fds_exit

fds_ok
	moveq	#0,d0
fds_exit
	rts


fds_timeout

;+++
; Force Interrupt to complete command
;
;	d0  r	ERR.NC
;---
fd_fint
	moveq	#fdc.fint,d0
	jsr	fd_cmd

	moveq	#120,d0
	bsr	at_pd0u 		 ; pause

fds_mchk
	moveq	#err.mchk,d0
	rts
	end
