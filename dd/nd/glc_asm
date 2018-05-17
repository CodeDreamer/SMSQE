; Gold Card timing constants

	section init

	xdef	nd_gold

	xdef	nd.ptype
nd.ptype equ	$0
	xdef	nd.card
nd.card  equ	'GC  '
	xref	nd_initb
	xref	nd_proctab
	xref	ut_procdef

	include 'dev8_sys_gold_keys'


nd_gold
	lea	ndt_g16,a1

ndg_init
	jsr	nd_initb
	lea	nd_proctab,a1
	jmp	ut_procdef

ndt_g16
n16_wgap dc.w	   66-1   ; wait for gap constant		 200us
n16_lsct		  ; look for scout		       20000us
n16_bace dc.w	 6560-1   ; look for broadcast acknowledge end 20000us
n16_csct dc.w	   13-1   ; check scout 			  30us
n16_esct dc.w	  758-1   ; end of scout			 485us
n16_wsct dc.w	  980-1   ; wait to send scout			3000us
n16_tsct dc.w	   59	  ; timer for scout active / inactive	  59
n16_bsct		  ; broadcast scout detect		 500us
n16_back dc.w	  164-1   ; broadcast acknowledge detect	 500us
n16_xsct		  ; extension to scout			5000us
n16_xack dc.w	 7812-1   ; (extension to) acknowledge		5000us
n16_bnak dc.w	  312-1   ; broadcast NACK delay		 200us
n16_stmo dc.w	17543	  ; serial port timout		       20000us
n16_paus dc.w	  219-1   ; pause before send			 140us
n16_send dc.w	   30	  ; send loop timer			  30
n16_rtmo dc.w	 1560-1   ; receive timout			2500us
n16_rbto dc.w	   50-1   ; receive bit timout			  80us
n16_rdly dc.w	   63	  ; receive delay start bit to bit 0	  63
n16_rbit dc.w	   32	  ; receive bit loop timer		  32
	end
