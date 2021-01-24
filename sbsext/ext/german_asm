* Toolkit II Constants (German)   1985   T.Tebby   QJUMP
*
	section defs

	xdef	ext_german
	xdef	ext_language

	xdef	ynaq
	xdef	message
	xdef	dec_point
	xdef	zero_w
	xdef	prior
	xdef	pipe_len
	xdef	pipe_nol
*
ext_german
ext_language

ynaq	 dc.b	 'JNAQ'
message
	 dc.w	yn_messg-*
	 dc.w	ynaq_mes-*
	 dc.w	to_messg-*
	 dc.w	exist_ms-*
	 dc.w	overw_ms-*
	 dc.w	sectr_ms-*
	 dc.w	jhead_ms-*
*
yn_messg dc.w	12,'..J oder N? '
ynaq_mes dc.w	11,'..J/N/A/Q? '
to_messg dc.w	04,' TO '
exist_ms dc.w	16,' gibt es schon, '
overw_ms dc.w	20,'OK zum ‡berschreiben'
sectr_ms dc.b	0,10,' Sektoren',$0a
jhead_ms dc.w	27
	 dc.b	'Job Tag Besitzer Priorit€t',$a
*
dec_point dc.b	'.'
zero_w	 dc.w	0
prior	 dc.w	8
pipe_len dc.w	8,'pipe_256'
pipe_nol dc.w	4,'pipe'
	end
