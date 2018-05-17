; Atari QL Mode initialise Display	 1992	Tony Tebby

	section init

	xdef	cn_initb

	include 'dev8_keys_atari'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'

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
;	a6 c  p system variables
;---
cn_initb
	move.b	#2,sys_qlmr(a6) 	 ; set mode 2

	lea	cnm_initb,a4		  ; Mono display

	move.w	(a4)+,d0		 ; number of words to set up
	bra.s	cni_sete
cni_setl
	move.w	(a4)+,d1		; where to put...
	move.w	(a4)+,0(a3,d1.w)	; ...this data
cni_sete
	dbra	d0,cni_setl

	moveq	#7,d0			 ; clear all of palette reg
	lea	vdr_palt,a0
inm_palt
	clr.l	(a0)+
	dbra	d0,inm_palt

	moveq	#2,d6
	move.b	#vdm.sb1,vdr_sb1	 ; set mono screen base
	move.b	#vdm.sb2,vdr_sb2

	lea	vdm_scrn,a0		 ; screen base
	move.w	#vdm.ssiz/4-1,d0

inm_cls
	clr.l	(a0)+
	dbra	d0,inm_cls
	moveq	#0,d0
	rts

*
*	Table of data to be filled in - offset in dddb/word to set
*
cnm_initb
	dc.w	6
	dc.w	pt_scren,vdm.sb1
	dc.w	pt_scren+2,vdm.sb2<<8
	dc.w	pt_scrsz+2,vdm.ssiz
	dc.w	pt_scinc,vdm.bytl
	dc.w	pt_xscrs,vdm.bytl<<3
	dc.w	pt_yscrs,vdm.line

	end
