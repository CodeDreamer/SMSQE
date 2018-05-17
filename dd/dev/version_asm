; DEV Version

	section version

	xdef	dev_vmess
	xdef	dev_vmend
	xdef	dev_vers

;	V2.00	Initial version
;	V2.01	DEV_USE name permitted
;	V2.02	Format returns not implemented
;	V2.03	Allows DV3 to change channel block
;	V2.04	Name length check corrected
;	V2.05	DEV_USEN ( and DEV_USE with 0 or one params) added

dev_vers equ  '2.05'
dev_vmess
	dc.w	'DEV  V'
	dc.l	dev_vers
	dc.b	' ',$a
dev_vmend
	ds.w	0
	end
