; Parallel Driver versions

	section version

	xdef	par_vers

; V2.00   First version.
;
; V2.01   Dynamic output buffering added.
;
; V2.02   Set and read headers added to V2.01.
;
; V2.03   Interrupt server modified for Canon PW1080A, PAR_PULSE added.
;
; V2.04   Zero length translated strings now OK.
;
; V2.05   Static buffer with translate now OK.
;
; V2.06   Auto CR added.
;
; V2.07   Initialisation "out of memory" corrected.
;
; V2.08   680x0 compatible.
;
; V2.09   Generalised version.

par_vers equ	'2.09'

par_vmess
	dc.b	'Parallel port driver V'
	dc.l	par_vers
	dc.b	' ',$a
par_vmend
	end
