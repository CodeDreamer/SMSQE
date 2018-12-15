; DV3 Standard Hard Disk Direct Sector Read (16 bit DMA/PIO)   1998  Tony Tebby

	section dv3

	xdef	hd_rdirect16		; direct sector read (16 bit DMA/PIO)

	xref	hd_rdirect		; direct sector read

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; This routine reads a sector from a hard disk for direct sector IO
; If the data is at an odd address, it is buffered.
;
;	d0 cr	sector number
;	d7 c  p drive ID / number
;	a1 c  p address to read into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_rdirect16
	exg	a1,d0			 ; check if address odd
	btst	#0,d0
	exg	d0,a1
	beq.s	hd_rdirect		 ; even address
hdrd.reg reg	a0/a1
stk_a1	 equ	4
	movem.l hdrd.reg,-(sp)
	lea	hdl_buff(a3),a1 	 ; read into buffer
	move.l	a1,a0			 ; save it

	bsr.s	hd_rdirect		 ; read
	bne.s	hdrd_exit		 ; failed

	move.l	stk_a1(sp),a1		 ; destination address
	move.w	ddf_smask(a4),d0
hdrd_loop
	move.b	(a0)+,(a1)+
	dbra	d0,hdrd_loop

	moveq	#0,d0

hdrd_exit
	movem.l (sp)+,hdrd.reg
	rts

	end
