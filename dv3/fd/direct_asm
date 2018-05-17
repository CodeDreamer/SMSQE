; DV3 Standard Floppy Disk Check for Direct Sector Open V3.00  1993 Tony Tebby

	section dv3

	xdef	fd_direct

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_dv3_fd_keys'

;+++
; DV3 check floppy for direct sector open (or for format)
;
;	d1 c  p sector length
;	d2 c  p density flag
;	d3-d6	scratch
;	d7 c  p drive ID / number
;	a0 c  p channel block
;	a1/a2	scratch
;	a3 c  p pointer to linkage block
;	a4 c  u pointer to physical definition
;
;	error return 0 or error
;---
fd_direct
	cmp.b	fdl_maxd(a3),d7 	 ; drive in range?
	bgt.s	fc_fdnf 		 ; ... no
	st	fdf_dirct(a4)		 ; set opened for direct access
	sf	ddf_wprot(a4)		 ; write protect not checked because
					 ; tab might be moved or disk changed
					 ; while channel is open
	moveq	#0,d0
	rts
fc_fdnf
	moveq	#err.fdnf,d0
	rts
fd_inam
	moveq	#err.inam,d0
	rts
	end
