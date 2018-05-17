; Gold card patch section extra ROM code initialisation

	section patch

	xdef	gl_extr

	xref	nd_gold
	xref	sgc_xbasic
	xref	par_init
	xref	dev_basic
	xref	dev_init

gl_extr
	jsr	sgc_xbasic
	jsr	par_init
	jsr	nd_gold
	jmp	dev_init

	end
