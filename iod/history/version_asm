; HISTORY device versions

	section version

	xdef	history_vers

; V2.00 first version!
;
; V2.01 ioa.cnam support
;
; V2.02 File size now corresponds to the number of messages in history (MK)
;	File pointer is limited to valid message range (MK)
;	Empty strings are ignored (MK)

history_vers equ    '2.02'

history_wmess
	dc.b	'History  V'
	dc.l	history_vers
	dc.b	' ',$a
history_vmend
	end
