; NUL device versions

	section version

	xdef	nul_vers

; V2.00 first version!
;
; V2.01 entries $60 upwards corrected
;
; V2.02 iob.flin returns buffer of zeros and err.bffl if file of zeros
;	iob.elin corrected for d1>0
;	iob.elin, iob.flin, iob.fmul, iob.load speed improved
;
; V2.03 channel name implemented and corrected
;
nul_vers equ	'2.03'

nul_wmess
	dc.b	'NUL device   V'
	dc.l	nul_vers
	dc.b	' ',$a
nul_vmend
	end
