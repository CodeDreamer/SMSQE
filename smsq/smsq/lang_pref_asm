; SMSQ_LANG_PREF  Language Preference Tables  V2.0   1994	Tony Tebby

	section language

	xdef	smsq_pref

	xref	smsq_msg

	include 'dev8_keys_ldm'


smsq_pref
	dc.w	ldm.pref,0,34		 ; Spanish
	dc.w	6
	dc.l	prf_esp-*
	
	dc.w	ldm.pref,0,39		 ; Italian
	dc.w	6
	dc.l	prf_ita-*
	  
	dc.w	ldm.pref,0,45		 ; Denmark
	dc.w	6
	dc.l	prf_dk-*

	dc.w	ldm.pref,0,46		 ; Swedish
	dc.w	6
	dc.l	prf_swe-*

	dc.w	ldm.pref,0,47		 ; Norwegian
	dc.w	6
	dc.l	prf_nor-*

	dc.w	ldm.pref,0,33		 ; French
	dc.w	6
	dc.l	prf_fra-*

	dc.w	ldm.pref,0,49		 ; German
	dc.w	6
	dc.l	prf_deu-*

	dc.w	ldm.pref,0,1		 ; USA English
	dc.w	6
	dc.l	prf_usa-*

	dc.w	ldm.pref,0,44		 ; English (default of defaults)
	dc.w	smsq_msg-*
	dc.l	prf_eng-*

 
; Preference tables

prf_eng
	dc.l	'GB  '
	dc.w	44,0
prf_deu
	dc.l	'D   '
	dc.w	49,0
prf_usa
	dc.l	'USA '
	dc.w	1,44,0
prf_fra
	dc.l	'F   '
	dc.w	33,0
prf_nor
	dc.l	'N   '
	dc.w	47,46,0
prf_swe
	dc.l	'S   '
	dc.w	46,47,0
prf_dk
	dc.l	'DK  '
	dc.w	45,0

prf_ita
	dc.l	'IT  '
	dc.w	39,0

prf_esp
	dc.l	'E   '
	dc.w	34,0
	end
