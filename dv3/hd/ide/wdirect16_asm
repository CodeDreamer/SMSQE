; DV3 Standard Hard Disk Direct Sector Write (16 bit DMA/PIO)   1998  Tony Tebby

	section dv3

	xdef	hd_wdirect16		; direct sector write (16 bit DMA/PIO)

	xref	hd_wdirect		; direct sector write

	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'

;+++
; This routine writes a sector to a hard disk for direct sector IO
; If the data is at an odd address, it is buffered.
;
;	d0 cr	sector number / error code
;	d7 c  p drive ID / number
;	a1 c  p address to write into
;	a3 c  p linkage block
;	a4 c  p drive definition
;
;	status return 0, ERR.NC or ERR.MCHK
;
;---
hd_wdirect16
	exg	a1,d0			 ; check if address odd
	btst	#0,d0
	exg	d0,a1
	beq.s	hd_wdirect		 ; even address
hdwd.reg reg	d1/a0/a1
stk_a1	 equ	4
	movem.l hdwd.reg,-(sp)
	lea	hdl_buff(a3),a0 	 ; write from buffer
	move.w	ddf_smask(a4),d1
hdwd_loop
	move.b	(a1)+,(a0)+
	dbra	d1,hdwd_loop

	lea	hdl_buff(a3),a1 	 ; write from buffer
	bsr.s	hd_wdirect
hdwd_exit
	movem.l (sp)+,hdwd.reg
	rts

	end
