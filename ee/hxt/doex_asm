; Procedure to execute programs  V2.01	   1990   Tony Tebby	QJUMP
;
; 2006-03-27  2.01  Use system specific delay (MK)

	section hotkey

	xdef	hxt_thing
	xdef	hxt_doex

	xref	hxt_go

	xref	hxt_mkxt
	xref	hk_do

	xref	hxt_prmx

	xref	gu_hkuse
	xref	gu_hkfre
	xref	gu_rchp
	xref	gu_exdelay
	xref	gu_pause

	include 'dev8_keys_thg'
	include 'dev8_ee_hk_data'
	include 'dev8_mac_thg'

hxt_thing
;+++
; EX filename \P parameters \J Job name
;---
hxt_doex thg_extn DOEX,hxt_go,hxt_prmx

hdx.reg reg	d1/d6/a0/a1/a3
	movem.l hdx.reg,-(sp)

	sub.l	#4,a1			 ; we had no key at front

	moveq	#hki.xthg,d6		 ; set up execute thing
	jsr	hxt_mkxt		 ; make temporary item
	bne.s	hdx_exit

	jsr	gu_hkuse		 ; use hotkey
	bne.s	hdx_rchp

	move.l	a0,a1			 ; item
	jsr	hk_do
	beq.s	hdx_free		 ; sehr gut

	move.w	#hki.xfil,hki_type(a1)	 ; tough, now try file
	jsr	hk_do			 ; do it

hdx_free
	jsr	gu_hkfre
	bne.s	hdx_rchp		 ; oops
hdx_pause
;	 moveq	 #25,d0 		  ; half a second
	jsr	gu_exdelay		 ; get system specific delay
	jsr	gu_pause		 ; ... pause

hdx_rchp
	jsr	gu_rchp 		 ; done
hdx_exit
	movem.l (sp)+,hdx.reg
	rts
	end
