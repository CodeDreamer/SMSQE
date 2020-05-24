; SMSQ_LANG_MSGC  Message Tables - Group C  V2.1	 1994	Tony Tebby
;
; 2020-04-13  2.1  Added Spanish messages (MK)

	section language

	xdef	smsq_msgc

	xref	smsq_tra

	include 'dev8_keys_ldm'
	include 'dev8_mac_text'

; SMSQ group C message tables

smsq_msgc
	dc.w	ldm.msgt,$c,44	  ; English
	dc.w	6
	dc.l	msg_eng-*

	dc.w	ldm.msgt,$c,49	  ; German
	dc.w	6
	dc.l	msg_deu-*

	dc.w	ldm.msgt,$c,39	  ; Italian
	dc.w	6
	dc.l	msg_ita-*

	dc.w	ldm.msgt,$c,33	  ; French
	dc.w	6
	dc.l	msg_fra-*

	dc.w	ldm.msgt,$c,34	  ; Spanish
	dc.w	smsq_tra-*
	dc.l	msg_esp-*


; message  tables

msg_eng dc.w	44
	dc.w	eng_mth3-msg_eng
	dc.w	eng_dow3-msg_eng

eng_mth3 mkstr	{JanFebMarAprMayJunJulAugSepOctNovDec}
eng_dow3 mkstr	{SunMonTueWedThuFriSat}

msg_deu dc.w	49
	dc.w	deu_mth3-msg_deu
	dc.w	deu_dow3-msg_deu

deu_mth3 mkstr	{JanFebM€rAprMaiJunJulAugSepOktNovDez}
deu_dow3 mkstr	{SonMonDieMitDonFreSam}

msg_fra dc.w	33
	dc.w	fra_mth3-msg_fra
	dc.w	fra_dow3-msg_fra

fra_mth3 mkstr	{JanFƒvMarAvrMaiJunJulAo›SepOctNovDƒc}
fra_dow3 mkstr	{DimLunMarMerJeuVenSam}

msg_ita dc.w	39
	dc.w	ita_mth3-msg_ita
	dc.w	ita_dow3-msg_ita

ita_mth3 mkstr	{GenFebMarAprMagGiuLugAgoSetOttNovDic}
ita_dow3 mkstr	{DomLunMarMerGioVenSab}

msg_esp dc.w	34
	dc.w	esp_mth3-msg_esp
	dc.w	esp_dow3-msg_esp

esp_mth3 mkstr	{EneFebMarAbrMayJunJulAgoSepOctNovDic}
esp_dow3 mkstr	{DomLunMarMiƒJueVieSŒb}

	ds.w	0

	end
