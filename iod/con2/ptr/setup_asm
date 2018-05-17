; Standard Pointer Setup   V2.01    2000 Tony Tebby

	section driver

	xdef	pt_setup

	xref	pt_init
	xref	pt_installu
	xref	cn_procv

;	d1 c	screen size
;	d2 c	colour depth
;	d3 c	driver mode
;	d4 c	colour depth to install (=d2 install, <>d2 do not install)

pt_setup
	jsr	pt_init 		 ; initialise ptr linkage
	blt.s	pts_rts 		 ; ... oops
	bgt.s	pts_install		 ; ... already initialised
	jsr	cn_procv		 ; not initialised - install procs
pts_install
	cmp.b	d2,d4			 ; install this colour depth?
	bne.s	pts_rts 		 ; ... no
	jsr	pt_installu		 ; ... yes

pts_rts
	tst.l	d0
	rts
	end
