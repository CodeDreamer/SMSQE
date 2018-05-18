; DV3 QLW1 Format Load File    V3.01    2017	W. Lenerz
; based on
; DV3 QLWA Format Load File    V3.00    1994	Tony Tebby QJUMP
	section dv3

	xdef	qw1_load

	xref	dv3_sload
	xref	dv3_sbloc
				  
	include 'dev8_dv3_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_qlw1'
	include 'dev8_keys_err'

;+++
; DV3 QLW1 format load file.
; It checks whether the first sector of a file is in slave blocks, and if
; it is , it does ordinary IO.
;
;	d1 cr	amount read so far
;	d2 c  p amount to load
;	d3   s
;	d4   s
;	d5   s
;	d6 c  p file id
;	d7 c  p drive ID / number
;	a0 c  p channel block 
;	a1 c  u pointer to memory to load into
;	a2   s	internal buffer address
;	a3 c  p pointer to linkage block
;	a4 c  p pointer to physical definition
;	a5    p
;---
qw1_load
	moveq	#err.nimp,d0
	rts

	end
