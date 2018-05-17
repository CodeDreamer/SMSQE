; QL compatible error messages

	section language

	xdef	uq_erms

message macro	text
[.lab]	dc.w	[.len(text)]+1
	dc.b	'[text]',$a
	endm

uq_erms
	dc.w	$4afb
	dc.w	err_nc-uq_erms
	dc.w	err_ijob-uq_erms
	dc.w	err_imem-uq_erms
	dc.w	err_orng-uq_erms
	dc.w	err_bffl-uq_erms
	dc.w	err_ichn-uq_erms
	dc.w	err_fdnf-uq_erms
	dc.w	err_fex-uq_erms
	dc.w	err_fdiu-uq_erms
	dc.w	err_eof-uq_erms
	dc.w	err_drfl-uq_erms
	dc.w	err_inam-uq_erms
	dc.w	err_trns-uq_erms
	dc.w	err_fmtf-uq_erms
	dc.w	err_ipar-uq_erms
	dc.w	err_mchk-uq_erms
	dc.w	err_iexp-uq_erms
	dc.w	err_ovfl-uq_erms
	dc.w	err_nimp-uq_erms
	dc.w	err_rdo-uq_erms
	dc.w	err_isyn-uq_erms
	dc.w	err_rwf-uq_erms

err_nc	 message {incomplete}
err_ijob message {invalid Job ID}
err_imem message {insufficient memory}
err_orng message {value out of range}
err_bffl message {buffer full}
err_ichn message {invalid channel ID}
err_fdnf message {not found}
err_fex  message {already exists}
err_fdiu message {is in use}
err_eof  message {end of file}
err_drfl message {drive is full}
err_inam message {invalid name}
err_trns message {transmission error}
err_fmtf message {format failed}
err_ipar message {invalid parameter}
err_mchk message {medium check failed}
err_iexp message {error in expression}
err_ovfl message {arithmetic overflow}
err_nimp message {not implemented}
err_rdo  message {write protected}
err_isyn message {invalid syntax}
err_rwf  message {read or write failed}
	ds.w	0
	end
