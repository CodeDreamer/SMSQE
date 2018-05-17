; DV3 Atari FDC Check Write Protect  V3.00   1993   Tony Tebby

	section dv3

	xdef	fd_ckwp 		; check write protect

	xref	fd_hold 		; hold and select
	xref	fd_release		; release
	xref	fd_cmd_wrn		; write (no DMA) command
	xref	fd_fint 		; force interrupt

	xref	at_pd0u 		; pause d0 microseconds

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_keys_atari'

;+++
; Check write protect: selects, tries to write a silly sector
;
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to drive definition
;
;	all registers except d0 preserved
;
;	status standard
;
;---
fd_ckwp
	jsr	fd_hold 		 ; select drive
	blt.s	fcwp_rts

	move.l	#$000000ff,fdl_sadd(a3)  ; track zero, side zero, silly sector

	moveq	#fdc.wsec,d0		 ; write sector
	jsr	fd_cmd_wrn

	moveq	#120,d0
	bsr	at_pd0u 		 ; pause

	move.w	#dma.fstt,dma_mode	 ; get status reg
	moveq	#1<<fds..ro,d0		 ; ... to check write protect bit
	and.w	dma_data,d0
	sne	ddf_wprot(a4)		 ; set write protected

	jsr	fd_fint 		 ; kill command

	moveq	#0,d0
	jmp	fd_release		 ; let it go

fcwp_rts
	rts
	end
