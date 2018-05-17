;	Set pallete mapw       v0.00   1991  Tony Tebby
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D1	First colour			=D2
;	D2	Last (+1) colour		preserced
;	A0	CDB				preserved
;	A1	Palette list			updated
;	A2/A5					smashed
;
;	All other registers preserved
;
	section con
;
	include 'dev8_keys_err'
;
	xdef	cn_palq
	xdef	cn_palt
;
cn_palq
cn_palt
	moveq	#err.nimp,d0
	rts

	end
