* Write error message	V0.0    1985	Tony Tebby   QJUMP
*
	section utils
*
	xdef	ut_errms
*
	include dev8_sbsext_ext_keys
*
ut_errms
	jsr	ut..err*3+qlv.off      send error message
	moveq	#0,d0			and clear error
	rts
	end
