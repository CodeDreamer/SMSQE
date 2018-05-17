; DV3 Atari Find Slavee Linkage     V3.00    1992  Tony Tebby

	section dv3

	xdef	ahd_fslave

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_hd_keys'
;+++
; DV3 Atari find drive linkage.
; Checks the master and slave linkages until one is found with partition >= 0
;
;	d0  r
;	a3 c  u pointer to linkage block
;
;	error return 0 or error
;---
ahd_fslave
hfs_try
	add.w	d7,a3
	tst.b	hdl_part-1(a3)		 ; partition defined for this one?
	sub.w	d7,a3
	bpl.s	hfs_ok			 ; ... yes
	move.l	ddl_slave(a3),a3	 ; a slave?
	move.l	a3,d0
	bne.s	hfs_try 		 ; ... yes
	moveq	#err.mchk,d0
	rts

hfs_ok
	moveq	#0,d0
	rts

	end
