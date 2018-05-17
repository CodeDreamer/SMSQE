; DV3 Standard Floppy Disk Direct Sector Write (16 bit DMA)   1998  Tony Tebby

	section dv3

	xdef	fd_wdirect16		; direct sector write (16 bit DMA)

	xref	fd_wdirect		; direct sector write

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine writes a sector to a Floppy disk for direct sector IO
; If the data is at an odd address, it is buffered.
;
;	d0 cr	cylinder + side + sector / error code
;	d7 c  p drive ID / number
;	a1 c  p address to write into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_wdirect16
	exg	a1,d0			 ; check if address odd
	btst	#0,d0
	exg	d0,a1
	beq.s	fd_wdirect		 ; even address
fdwd.reg reg	d1/a0/a1
stk_a1	 equ	4
	movem.l fdwd.reg,-(sp)
	lea	fdl_buff(a3),a0 	 ; write from buffer
	move.w	ddf_smask(a4),d1
fdwd_loop
	move.b	(a1)+,(a0)+
	dbra	d1,fdwd_loop

	lea	fdl_buff(a3),a1 	 ; write from buffer
	bsr.s	fd_wdirect
fdwd_exit
	movem.l (sp)+,fdwd.reg
	rts

	end
