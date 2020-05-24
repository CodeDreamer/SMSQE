; Gold Card timing constants
; 2020-04-09  1.01  adapt to changed keys in sys_gold_keys (wl)
	section init

	xdef	nd_gold

	xref	nd_initb
	xref	nd_proctab
	xref	ut_procdef

	include 'dev8_sys_gold_keys'

nd_gold
	lea	ndt_sgl,a1
	tst.w	glk.card+glx_ptch	 ; patch to 16 MHz?
	beq.s	ndg_init
	lea	ndt_g16,a1

ndg_init
	jsr	nd_initb
	lea	nd_proctab,a1
	jmp	ut_procdef

ndt_sgl
n12_wgap dc.w	  161-1   ; wait for gap constant		 200us
n12_lsct		  ; look for scout		       20000us
n12_bace dc.w	15941-1   ; look for broadcast acknowledge end 20000us
n12_csct dc.w	   31-1   ; check scout 			  30us
n12_esct dc.w	 1896-1   ; end of scout			 485us
n12_wsct dc.w	 2380-1   ; wait to send scout			3000us
n12_tsct dc.w	   26	  ; timer for scout active / inactive	  59
n12_bsct		  ; broadcast scout detect		 500us
n12_back dc.w	  397-1   ; broadcast acknowledge detect	 500us
n12_xsct		  ; extension to scout			5000us
n12_xack dc.w	19531-1   ; (extension to) acknowledge		5000us
n12_bnak dc.w	  780-1   ; broadcast NACK delay		 200us
n12_stmo dc.w	60000	  ; serial port timout		       20000us
n12_paus dc.w	  546-1   ; pause before send			 140us
n12_send dc.w	   31	  ; send loop timer			  30
n12_rtmo dc.w	 2634-1   ; receive timout			2500us
n12_rbto dc.w	   86-1   ; receive bit timout			  80us
n12_rdly dc.w	   27	  ; receive delay start bit to bit 0	  63
n12_rbit dc.w	   14	  ; receive bit loop timer		  32

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
