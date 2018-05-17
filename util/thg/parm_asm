; Thing	Extension Parameter Definitions	 V2.00	   1989  Tony Tebby  QJUMP

	section	exten

	xdef	thp_chn
	xdef	thp_chr
	xdef	thp_ochr
	xdef	thp_str
	xdef	thp_ostr
	xdef	thp_oub
	xdef	thp_2oub
	xdef	thp_wd
	xdef	thp_2wd
	xdef	thp_owd
	xdef	thp_2owd
	xdef	thp_lw
	xdef	thp_slwd
	xdef	thp_ulwd
	xdef	thp_olw
	xdef	thp_2olw
	xdef	thp_3olw
	xdef	thp_nul
	xdef	thp_rstr
	xdef	thp_nrstr
	xdef	thp_c1st
	xdef	thp_c2st

	include	'dev8_keys_thg'

thp_2oub dc.w  thp.opt+thp.ubyt	 ; unsigned byte
thp_oub	 dc.w  thp.opt+thp.ubyt	 ; unsigned byte
	 dc.w  0

thp_chn	 dc.w  thp.chid		 ; compulsory channel ID
	 dc.w  0

thp_chr	 dc.w  thp.char		 ; compulsory character
	 dc.w  0

thp_ochr dc.w  thp.opt+thp.char	 ; optional character
	 dc.w  0

thp_str	 dc.w  thp.call+thp.str	 ; compulsory string
	 dc.w  0

thp_ostr dc.w  thp.call+thp.opt+thp.str	; optional string
	 dc.w  0

thp_2wd	 dc.w  thp.uwrd		 ; compulsory unsigned word
thp_wd	 dc.w  thp.uwrd		 ; compulsory unsigned word
	 dc.w  0

thp_2owd dc.w  thp.opt+thp.uwrd	 ; optional unsigned word
thp_owd	 dc.w  thp.opt+thp.uwrd	 ; optional unsigned word
	 dc.w  0

thp_lw	 dc.w  thp.ulng		 ; compulsory unsigned long
	 dc.w  0

thp_slwd dc.w  thp.slng		 ; compulsory signed long
	 dc.w  0

thp_ulwd dc.w  thp.ulng		 ; compulsory unsigned long
	 dc.w  0

thp_3olw dc.w  thp.opt+thp.ulng	 ; optional long word
thp_2olw dc.w  thp.opt+thp.ulng	 ; optional long word
thp_olw	 dc.w  thp.opt+thp.ulng	 ; optional long word
thp_nul	 dc.w  0

thp_c1st dc.w  thp.chid		 ; channel ID
	 dc.w  thp.call+thp.str	 ; compulsory string
	 dc.w  0

thp_c2st dc.w  thp.chid		 ; channel ID
	 dc.w  thp.call+thp.str	 ; compulsory string
	 dc.w  thp.call+thp.str	 ; compulsory string
	 dc.w  0

thp_nrstr dc.w thp.uwrd		 ; N+return string
thp_rstr dc.w  thp.ret+thp.str	 ; return string
	 dc.w  0
	 end
