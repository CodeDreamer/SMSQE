; DOS Version

	section version

	xdef	dos_vmess
	xdef	dos_vmend
	xdef	dos_vers

;	V1.00	Initial version
;	V1.01	Various improvements...
;	V1.02	Fixed IOF.MINF buffer overrun

dos_vers equ  '1.02'
dos_vmess
	dc.w	'QPC DOS device V'
	dc.l	dos_vers
	dc.b	' ',$a
dos_vmend
	ds.w	0
	end
