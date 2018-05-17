; HOTKEY messages   1988   T.Tebby   QJUMP

	section language

	xdef	hk_italian

	xdef	hk_stufc
	xdef	hk_stufp
	xdef	hk_stufl

	include 'dev8_mac_text'
	include 'dev8_ee_hk_data'

hk_italian
hk_stufc dc.b	' '
hk_stufp dc.b	$fc
hk_stufl dc.l	$200

  mktext spce,{SPACE}
  mktext exec,{EXEC}
  mktext load,{LOAD}
  mktext pick,{PICK}
  mktext wake,{WAKE}
  mktext cmd,{CMD}
  mktext key,{KEY}

  mktext grab,{Allocazione della memoria di lavoro (kilobytes)> }
  mktext hks2,{HOTKEY System 2\}

	xdef	hki_llrc
hki_llrc
	dc.w	'hi'
	dc.w	hki.llrc	 ; dummy last line recall item
	dc.l	0
	mktext	llrc,{richiamo ultima linea}                                  ;32
	ds.w	0
	end
