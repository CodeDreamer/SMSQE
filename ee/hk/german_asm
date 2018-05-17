; HOTKEY messages   1988   T.Tebby   QJUMP

	section language

	xdef	hk_german
	xdef	hk_stufc
	xdef	hk_stufp
	xdef	hk_stufl

	include 'dev8_mac_text'
	include 'dev8_ee_hk_data'

hk_german

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

  mktext grab,{Arbeitsspeichergr„œe (Kilobytes)> }
  mktext hks2,{HOTKEY System 2\}

	xdef	hki_llrc
hki_llrc
	dc.w	'hi'
	dc.w	hki.llrc	 ; dummy last line recall item
	dc.l	0
	mktext	llrc,{Letzte Zeile zur‡ckerhalten}
	ds.w	0
	end
