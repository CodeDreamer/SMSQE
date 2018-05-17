; WMAN data and configuration  v1.00			2003 Marcel Kilgus
;	24-06-2003		1.01	d0 test added at end (wl)
;	28.04.2013		1.02	added config item for alpha blending when moving
	xdef	wm_initdata

	xref	wm_initsp
	xref	gu_achpp

	include 'dev8_keys_wman_data'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_mac_config02'

	section wman

wcf_move dc.b	0,0
wcf_alph dc.b	128,0
	dc.w	0

	xref.l	wm_vers


	mkcfhead {WMAN},{wm_vers}

	mkcfitem 'WMMV',code,'V',wcf_move,,,\
	{Move window operation} \
	0,S,{Sprite},1,O,{Outline},2,W,{Window},3,T,{Transparent Window}


	mkcfitem 'WMAB',byte,'A',wcf_alph,,,\
	{Alpha value for move window operation},1,$ff

	mkcfend

	dc.w	0
						      

;+++
; Initialise WMAN data space
;---
wm_initdata
	movem.l d1-d3/a0/a5-a6,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_clnk(a0),a6 ; CON linkage

	moveq	#wd_end,d0	; size of WMAN data block
	jsr	gu_achpp
	bne.s	wid_rts
	move.l	a0,pt_wdata(a6) ; save ptr to block
	move.l	a0,a5
	lea	wcf_move,a0	; copy config data
	move.b	(a0),wd_movemd(a5)
			 
	lea	wcf_alph,a0	; copy config data
	move.b	(a0),wd_alpha(a5)
				     
	jsr	wm_initsp	; allocate and initialise system palette
	bne.s	wid_rts
	move.l	a0,wd_syspal(a5); this sets NZ on goldcard!
wid_rts
	movem.l (sp)+,d1-d3/a0/a5-a6
	tst.l	d0
	rts


	end
