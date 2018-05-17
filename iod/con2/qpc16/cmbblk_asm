; Combine 2 blocks with alpha blending and write result to screen

	section con

	include 'dev8_smsq_qpc_keys'

	xdef	pt_cmbblk

;	d0    s ? / some arbitray value
;	d1 c  s size of section to combine / scratch
;	d2 c  s origin in source area1 / scratch
;	d3 c  s origin in destination area / scratch
;	d4 c  s origin in source area 2/ scratch
;	d5    s / (address of conversion table) scratch
;	d6 c	alpha / preserved
;	d7 c  s row increment of source area 2 / scratch
;	a0    s / scratch (address of conversion table)
;	a1 c  s base address of source area2  / scratch
;	a2 c  s row increment of source area1 / scratch
;	a3 c  s row increment of destination area / scratch
;	a4 c  s base address of source area1 / scratch
;	a5 c  s base address of destination area / scratch
;	a6/a7	preserved

pt_cmbblk
	dc.w	qpc.cmblk
	rts

	end
