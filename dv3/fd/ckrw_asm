; DV3 Standard Floppy Disk Check Before Read / Write   1993	 Tony Tebby

	section dv3

	xdef	fd_ckrw

	xref	fd_ckroot	       ; read root sector for check

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine reads the root sector and checks it
;
;	d0 c  u cylinder + side + sector / error code
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	all registers preserved unless check fails
;	status return 0 or ERR.MCHK
;
;---
fd_ckrw
fcrw.reg reg	d0/d2/a1
	movem.l fcrw.reg,-(sp)

	lea	fdl_buff(a3),a1
	jsr	fd_ckroot		 ; read root sector
	bne.s	fcrw_err
	jsr	ddf_mcheck(a4)		 ; check medium
	bne.s	fcrw_err

	bset	d7,fdl_stpb(a3) 	 ; check done
	moveq	#0,d0

fcrw_exit
	movem.l (sp)+,fcrw.reg
	rts

fcrw_err
	moveq	#err.mchk,d0		 ; error return
	move.l	d0,(sp) 		 ; set it
	bra.s	fcrw_exit
	end
