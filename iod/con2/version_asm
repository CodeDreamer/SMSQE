; CON device versions

	section version

	xdef	con_vers

; V2.00 first release version for ST shifter pickup!

; V2.01 ST MONO version.

; V2.02 Corrections to XOR block and line edit.

; V2.03 Corrections to EDLN.

; V2.04 Corrections to black ink on stipple, and stipple on black paper.

; V2.05 Recolour added. IOB.EDLN does not suppress null lines.

; V2.06 Corrections for SCR open.

; V2.07 IOB.FLIN terminated by NL only.

; V2.08 Graphic positioned text added.
;	Pending newlines cleared before cursor position query.
;
; V2.09 iob.smul (etc) with cursor enabled does not do pending newline.
;	iob.elin with cursor position is now compatible with QDOS call instead
;		of compatible with the documentation.
;	iob.flin and iob.elin return err.bffl as soon as the buffer is full.
;	iob.elin up and down arrows recognised at start of line.
;
; V2.10 iow_newl corrected
;
; V2.11 8 pixel wide characters accepted
;
; V2.12 Up and down cursor characters returned from iob.edln.
; V2.12+Pending newline changes.
;
; V2.13 Underscore corrected. Mode 8
;
; V2.14 cursor query cursor handling corrected
;
; V2.15 dangling cursor on edln flin corrected
;
; V2.16 major structural revision for extended colours
;
; V2.17 sprites can be used as cursor

con_vers equ	'2.17'

con_wmess
	dc.b	'Console  V'
	dc.l	con_vers
	dc.b	' ',$a
con_vmend
	end
