* Toolkit II Constants (English)   1985   T.Tebby   QJUMP
*
	section defs

	xdef	ext_english
	xdef	ext_language

	xdef	ynaq
	xdef	message
	xdef	dec_point
	xdef	zero_w
	xdef	prior
	xdef	pipe_len
	xdef	pipe_nol
*
ext_english
ext_language

ynaq	 dc.b	 'YNAQ'
message
	 dc.w	yn_messg-*
	 dc.w	ynaq_mes-*
	 dc.w	to_messg-*
	 dc.w	exist_ms-*
	 dc.w	overw_ms-*
	 dc.w	sectr_ms-*
	 dc.w	jhead_ms-*
*
yn_messg dc.w	10,'..Y or N? '
ynaq_mes dc.w	11,'..Y/N/A/Q? '
to_messg dc.w	04,' TO '
exist_ms dc.w	09,' exists, '
overw_ms dc.w	15,'OK to overwrite'
sectr_ms dc.w	09,' sectors',$0a00
jhead_ms dc.w	26
	 dc.b	'Job tag    owner priority',$a
*
dec_point dc.b	'.'
zero_w	 dc.w	0
prior	 dc.w	8
pipe_len dc.w	8,'pipe_256'
pipe_nol dc.w	4,'pipe'
	end
