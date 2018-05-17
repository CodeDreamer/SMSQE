; DV3 Version		 V3.00		       1992 Tony Tebby


	section version

	xdef	dv3_vers

; V3.01 Fetch line initial conditions corrected.
;
; V3.02 Delete file does not leave old channel block lying around.
;	QLF delete directory check corrected.
;
; V3.03 Compatibility with old QDOS drivers improved
;
; V3.04 Compatibility with C68 improved (length of channel block etc.)
;	Errors in read header of directory corrected/
;
; V3.05 Truncate sector list (QLWA) sets correct sectors of map to update.
;
; V3.06 MSDOS format problems (make_dir, delete) fixed
;	FORMAT waits for flush at end.
;
; V3.07 QWA format truncate error return fixed.
;	QWA disk change checks first 32 bytes (including update count).
;
; V3.08 EXn extension on MSDOS file treated as exec with 512.2^n data space.
;	MSDOS file overwrite and close problem (zero length file) fixed.
;
dv3_vers equ	 '3.08'
dv3_vmess
	dc.w	'Directory Device Driver  V'
	dc.l	dv3_vers
	dc.b	' ',$a
dv3_vmend
	ds.w	0
	end
