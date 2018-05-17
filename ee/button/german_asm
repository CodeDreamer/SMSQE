; German text for buttons	       V0.02   1989  Tony Tebby  QJUMP

	section language

	include 'dev8_mac_config02'
	include 'dev8_ee_button_data'

	xdef	bt_german

	xdef	bt_xorg
	xdef	bt_yorg
	xdef	bt_xsiz
	xdef	bt_ysiz
	xdef	bt_rows

	xref.l	bt_vers

bt_german
	mkcfhead {Buttons},{bt_vers}

	mkcfitem 'Q2BX',word,0,bt_xorg,,,\
       {Button-Rahmen X-Ursprung},0,$7fff

	mkcfitem 'Q2BY',word,0,bt_yorg,,,\
       {Button-Rahmen Y-Ursprung},0,$7fff

	mkcfitem 'Q2BW',word,0,bt_xsiz,,,\
       {Button-Rahmen Breite},btt.minx,$7fff

	mkcfitem 'Q2BH',word,0,bt_ysiz,,,\
       {Button-Rahmen H„he},btt.miny,$7fff

	mkcfitem 'Q2BO',code,0,bt_rows,,,{Organisation},0,C,{Spalten},-1,R,{Zeilen}

	mkcfend

bt_xorg  dc.w	0
bt_yorg  dc.w	0
bt_xsiz  dc.w	512
bt_ysiz  dc.w	256
bt_rows  dc.b	-1

	end
