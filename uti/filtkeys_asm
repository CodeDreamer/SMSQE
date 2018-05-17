; Filter definitions			1998 Jochen Merz

	section text

	xdef	flt_pre,flt_post
	xdef	flt_fnams
	xdef	flt_end
	xdef	flt_pica,flt_elite,flt_cond,flt_dron,flt_droff
	xdef	flt_text,flt_num,flt_empty

fs	equ	1
fe	equ	2

flt_pre dc.w	3
	dc.b	fs,'+',fe
flt_post dc.w	3
	dc.b	fs,'-',fe
flt_fnams dc.w	2
	dc.b	fs,'f'
flt_end dc.w	1
	dc.b	fe
flt_pica dc.w	3
	dc.b	fs,'P',fe
flt_elite dc.w	3
	dc.b	fs,'E',fe
flt_cond dc.w	3
	dc.b	fs,'C',fe
flt_dron dc.w	4
	dc.b	fs,'D0',fe
flt_droff dc.w	4
	dc.b	fs,'D1',fe

flt_text dc.w	3
	dc.b	fs,'T',fe
flt_num dc.w	3
	dc.b	fs,'N',fe
flt_empty dc.w	3
	dc.b	fs,' ',fe

	ds.w	0

	end
