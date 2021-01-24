* Character Type table		   V2.00    1986  Tony Tebby  QJUMP
*					 Apr 1988  J.R.Oakley  QJUMP
*
* 2.01	split off from ctype asm (MK)

	section cv
*
	xdef	cv_cttab
*
	include dev8_keys_k
*+++
* 256-byte table of character types
*---
cv_cttab
*
c	equ	k.curtyp
n	equ	k.nonpr
d	equ	k.dig09
l	equ	k.lclet
u	equ	k.uclet
o	equ	k.other
*
	dc.b	n,n,n,n,n,n,n,n
	dc.b	n,n,n,n,n,n,n,n
	dc.b	n,n,n,n,n,n,n,n
	dc.b	n,n,n,n,n,n,n,n
	dc.b	o,o,o,o,o,o,o,o
	dc.b	o,o,o,o,o,o,o,o
	dc.b	d,d,d,d,d,d,d,d
	dc.b	d,d,o,o,o,o,o,o
	dc.b	o,u,u,u,u,u,u,u
	dc.b	u,u,u,u,u,u,u,u
	dc.b	u,u,u,u,u,u,u,u
	dc.b	u,u,u,o,o,o,o,o
	dc.b	o,l,l,l,l,l,l,l
	dc.b	l,l,l,l,l,l,l,l
	dc.b	l,l,l,l,l,l,l,l
	dc.b	l,l,l,o,o,o,o,o
	dc.b	l,l,l,l,l,l,l,l
	dc.b	l,l,l,l,l,l,l,l
	dc.b	l,l,l,l,l,l,l,l
	dc.b	l,l,l,l,l,o,o,o
	dc.b	u,u,u,u,u,u,u,u
	dc.b	u,u,u,u,l,l,u,l
	dc.b	l,l,l,o,o,o,o,o
	dc.b	o,o,o,o,o,o,o,o
	dc.b	c,c,c,c,c,c,c,c
	dc.b	c,c,c,c,c,c,c,c
	dc.b	c,c,c,c,c,c,c,c
	dc.b	c,c,c,c,c,c,c,c
	dc.b	n,n,n,n,n,n,n,n
	dc.b	n,n,n,n,n,n,n,n
	dc.b	n,n,n,n,n,n,n,n
	dc.b	n,n,n,n,n,n,n,n
*
	end
