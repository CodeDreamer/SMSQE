; DV3 PC Compatible FDC Check Write Protect  V3.00   1993   Tony Tebby

	section dv3

	xdef	fd_ckwp 		; check write protect

	xref	fd_hold 		; hold and select
	xref	fd_release		; release
	xref	fd_cmd_sense		; sense command
	xref	fd_result		; read result (status byte 3)

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'
	include 'dev8_dv3_fd_pcf_keys'

;+++
; Check write protect: selects, reads sense data
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

	jsr	fd_cmd_sense		 ; sense
	bne.s	fcwp_done

	jsr	fd_result		 ; read result byte
	bne.s	fcwp_done

	and.b	#fdc.wprt,d0
	sne	ddf_wprot(a4)		 ; set write protected
	moveq	#0,d0

fcwp_done
	jmp	fd_release		 ; let it go

fcwp_rts
	rts
	end
