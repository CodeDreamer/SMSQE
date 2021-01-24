; Q40 QL Mode initialise Display       1992  Tony Tebby  QJUMP

	section init

	xdef	cn_initb
	xdef	cn_crcol
	xdef	cn.inkcl

	include 'dev8_keys_q40'

	section con

	section init
;+++
; Set console linkage
;
;	d1  s
;	d2  s
;	a0  s
;	a1  s
;	a3 c  p console linkage
;	a4  s
;---
cn_initb
	moveq	#q40.d4,d1
	jsr	cn_disp_size
	jmp	cn_disp_clear

	end
