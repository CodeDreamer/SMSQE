; DV3 Q40 Find Partition (Atari root)	   V3.01    1999  Tony Tebby

	section dv3

	xdef	hd_fpart

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
	include 'dev8_keys_qlwa'
;+++
; DV3 Bytebox find partition.
; It returns the root sector.
;
;	d1  r	as d3 but in chs format if required
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
	moveq	#0,d1			 ; root sector
	moveq	#-1,d2			 ; size unknown
	moveq	#0,d3			 ; offset
	move.l	#qwa.pflg,d4		 ; for QDOS disk
	tst.b	ddl_cylhds(a3)		 ; cylinder head side required?
	beq.s	hdf_ok			 ; no
	moveq	#1,d1			 ; sector 1 on track
hdf_ok
	moveq	#0,d0
	rts
	end
