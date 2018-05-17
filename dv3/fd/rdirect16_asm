; DV3 Standard Floppy Disk Direct Sector Read (16 bit DMA)   1998  Tony Tebby

	section dv3

	xdef	fd_rdirect16		; direct sector read (16 bit DMA)

	xref	fd_rdirect		; direct sector read

	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; This routine reads a sector from a Floppy disk for direct sector IO
; If the data is at an odd address, it is buffered.
;
;	d0 cr	cylinder + side + sector / error code
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
fd_rdirect16
	exg	a1,d0			 ; check if address odd
	btst	#0,d0
	exg	d0,a1
	beq.s	fd_rdirect		 ; even address
fdrd.reg reg	a0/a1
stk_a1	 equ	4
	movem.l fdrd.reg,-(sp)
	lea	fdl_buff(a3),a1 	 ; read into buffer
	move.l	a1,a0			 ; save it

	bsr.s	fd_rdirect		 ; read
	bne.s	fdrd_exit		 ; failed

	move.l	stk_a1(sp),a1		 ; destination address
	move.w	ddf_smask(a4),d0
fdrd_loop
	move.b	(a0)+,(a1)+
	dbra	d0,fdrd_loop

	moveq	#0,d0

fdrd_exit
	movem.l (sp)+,fdrd.reg
	rts

	end
