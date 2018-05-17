; Atari monochrome mode set display size  V2.01	 2000  Tony Tebby

	section con

	xdef	cn_disp_size

	xref	cn_disp_clear
	xref	pt_scansize

	include 'dev8_keys_atari'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'

;+++
; Set the display size
;
;	d7 c  p colour depth = 0
;	a3 c  p console linkage
;	a6 c  p system variables
;---
cn_disp_size
cds.reg reg	d1-d7/a4/a5
	movem.l cds.reg,-(sp)

	lea	cds_table,a4		 ; mono display table
	lea	pt_scren(a3),a5
	move.l	(a4)+,(a5)+		 ; screen base
	move.l	(a4)+,(a5)+		 ; size
	move.w	(a4)+,(a5)+		 ; line length (bytes)
	move.l	(a4)+,(a5)+		 ; x,y pixels

	moveq	#7,d0			 ; clear all of palette reg
	lea	vdr_palt,a5
inm_palt
	clr.l	(a5)+
	dbra	d0,inm_palt

	move.b	#vdm.sb1,vdr_sb1	 ; set mono screen base
	move.b	#vdm.sb2,vdr_sb2

	jsr	cn_disp_clear		 ; clear screen
	jsr	pt_scansize		 ; set sizes in channel blocks
	moveq	#0,d0
	movem.l (sp)+,cds.reg
	rts

;	Table of screen size data

cds_table
	dc.l	vdm_scrn
	dc.l	vdm.ssiz
	dc.w	vdm.bytl
	dc.w	vdm.bytl<<3
	dc.w	vdm.line

	end
