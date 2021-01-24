;	Recolour a window	v0.00   1991  Tony Tebby
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D1-D7					smashed
;	A0	CDB				preserved
;	A1	recolour list			smashed
;	A2/A5					smashed
;
;	All other registers preserved
;
	section con
;
	include 'dev8_keys_con'
;
	xdef	cn_recol
;
cn_recol
cnr_ok
	moveq	#0,d0
	rts

	end
