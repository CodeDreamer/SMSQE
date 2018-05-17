; Super Gold Card timing constants

	section init

	xdef	nd_gold

	xdef	nd.ptype
nd.ptype equ	$20
	xdef	nd.card
nd.card  equ	'SGC '
	xref	nd_initb
	xref	nd_proctab
	xref	ut_procdef

	include 'dev8_sys_gold_keys'

nd_gold
	lea	ndt_sgl,a1

ndg_init
	jsr	nd_initb
	lea	nd_proctab,a1
	jmp	ut_procdef

ndt_sgl
nsg_wgap dc.w	  161-1   ; wait for gap constant		 200us
nsg_lsct		  ; look for scout		       20000us
nsg_bace dc.w	15941-1   ; look for broadcast acknowledge end 20000us
nsg_csct dc.w	   31-1   ; check scout 			  30us
nsg_esct dc.w	 1896-1   ; end of scout			 485us
nsg_wsct dc.w	 2380-1   ; wait to send scout			3000us
nsg_tsct dc.w	   26	  ; timer for scout active / inactive	  59
nsg_bsct		  ; broadcast scout detect		 500us
nsg_back dc.w	  397-1   ; broadcast acknowledge detect	 500us
nsg_xsct		  ; extension to scout			5000us
nsg_xack dc.w	19531-1   ; (extension to) acknowledge		5000us
nsg_bnak dc.w	  780-1   ; broadcast NACK delay		 200us
nsg_stmo dc.w	60000	  ; serial port timout		       20000us
nsg_paus dc.w	  546-1   ; pause before send			 140us
nsg_send dc.w	   31	  ; send loop timer			  30
nsg_rtmo dc.w	 2634-1   ; receive timout			2500us
nsg_rbto dc.w	   86-1   ; receive bit timout			  80us
nsg_rdly dc.w	   27	  ; receive delay start bit to bit 0	  63
nsg_rbit dc.w	   14	  ; receive bit loop timer		  32
	end
