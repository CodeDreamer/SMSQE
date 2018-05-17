; PIPE device versions

	section version

	xdef	pipe_vers

; V2.00 first version!
;
; V2.01 named pipes added
;
; V2.02 PIPE_ and PIPE_0 treated as PIPE (Turbo bug)
;
; V2.03 error for unnamed PIPES introduced in V2.02 corrected
;
; V2.04 iob.smul into full pipe error corrected.
;	iob.smul now atomic for named pipe.
;
; V2.05 Closing an output pipe tidied up (name and channel table).

pipe_vers equ	 '2.05'

pipe_wmess
	dc.b	'PIPE V'
	dc.l	pipe_vers
	dc.b	' ',$a
pipe_vmend
	end
