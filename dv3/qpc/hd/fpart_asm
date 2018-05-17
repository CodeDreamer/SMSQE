; DV3 QPC Find Partition     V3.01    1999  Tony Tebby

	section dv3

	xdef	hd_fpart

	include 'dev8_keys_err'
	include 'dev8_keys_qlwa'
	include 'dev8_keys_atari'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
;+++
; DV3 QPC find partition.
; Always returns no partition table
;
;	d1  r	as d3 but in ths format if required
;	d2  r	size of partition, -1 no partition table
;	d3  r	sector number with partition root
;	d4  r	partition ID
;	d7 c  p drive ID / number
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;
;	error return 0 or error
;---
hd_fpart
	st	hdl_npart(a3)		 ; set no partition found
	moveq	#0,d1
	moveq	#-1,d2			 ; size unknown
	moveq	#0,d3
	move.l	#art.qflg,d4		 ; for QDOS disk
	moveq	#0,d0
	rts

	end
