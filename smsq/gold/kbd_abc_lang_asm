; base area SMSQ ABC Keyboard Tables

	section header

	xref	smsq_end

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_ldm'
	include 'dev8_smsq_gold_kbd_abc_keys'

	include 'dev8_keys_stella_bl'

header_base
	dc.l	kbd_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-kbd_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select for ABC keyboard
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	30,'SMSQ GOLD ABC Keyboard Tables '
	dc.l	'    '
	dc.w	$200a


select
	cmp.b	#kb.abc>>8,sbl_mtype+2(a5)  ; ABC keybard?
	bne.s	sel_noload
	moveq	#sbl.load,d0
	rts
sel_noload
	moveq	#sbl.noload,d0
	rts

	section base

kbd_base
	lea	kbd_def,a1		 ; link in keyboard tables
	moveq	#sms.lldm,d0
	trap	#do.sms2
	rts

kbd_def
	dc.w	ldm.kbdt,0,44		 ; English
	dc.w	6
	dc.l	kbd_eng-*

	dc.w	ldm.kbdt,0,49		 ; German
	dc.w	0 ;!!!!6
	dc.l	kbd_deu-*

;	 dc.w	 ldm.kbdt,0,33		  ; French
;	 dc.w	 6
;	 dc.l	 kbd_fra-*

;	 dc.w	 ldm.kbdt,0,47		  ; Norwegian
;	 dc.w	 6
;	 dc.l	 kbd_nor-*

;	 dc.w	 ldm.kbdt,0,45		  ; Denmark
;	 dc.w	 6
;	 dc.l	 kbd_dk-*

;	 dc.w	 ldm.kbdt,0,34		  ; Spain
;	 dc.w	 0
;	 dc.l	 kbd_esp-*

kbd_eng dc.w	44
	dc.w	kbde_tab-*,kbd_nsid-*-2
kbd_deu dc.w	49
	dc.w	kbdd_tab-*,kbd_nsid-*-2
;kbd_fra dc.w	 33
;	 dc.w	 kbdf_tab-*,kbdf_nsid-*-2
;kbd_nor dc.w	 47
;	 dc.w	 kbdn_tab-*,kbd_nsid-*-2
;kbd_dk  dc.w	 45
;	 dc.w	 kbdk_tab-*,kbd_nsid-*-2
;kbd_esp dc.w	 34
;	 dc.w	 kbes_tab-*,kbd_nsid-*-2


kbd_nsid
*
* key translation table normal for english keyboards
*
kbde_tab
	 dc.b $00,$1B,$31,$32,$33,$34,$35,$36,$37,$38,$39  * 00
	 dc.b $30,$2D,$3D,$C2,$09,$71,$77,$65,$72,$74	   * 11
	 dc.b $79,$75,$69,$6F,$70,$5B,$5D,$0A,$00,$61	   * 21
	 dc.b $73,$64,$66,$67,$68,$6A,$6B,$6C,$3B,$27	   * 31
	 dc.b $60,$00,$5C,$7A,$78,$63,$76,$62,$6E,$6D	   * 41
	 dc.b $2C,$2E,$2F,$00,$00,$00,$20,$E0,$E8,$EC	   * 51
	 dc.b $F0,$F4,$F8,$EA,$EE,$F2,$F6,$FA,$00,$F9	   * 61
*
* SHIFT
*
	 dc.b $00,$7f,'!','@','#','$','%','^','&','*','('      * 00
	 dc.b ')','_','+',$c1,$fd,'Q','W','E','R','T'	       * 11
	 dc.b 'Y','U','I','O','P','{','}',$fe,$00,'A'	       * 21
	 dc.b 'S','D','F','G','H','J','K','L',':','"'	       * 31
	 dc.b '~',$00,'|','Z','X','C','V','B','N','M'	       * 41
	 dc.b '<','>','?',$00,$00,$00,$fc,$e4,$ea,$ee	       * 51
	 dc.b $f2,$f6,$fa,$ea,$ee,$f2,$f6,$fA,$00,$f9	       * 61
*
* CTRL
*
	 dc.b $00,$80,$91,$92,$93,$94,$95,$96,$97,$98,$99      * 00
	 dc.b $90,$8D,$9D,$C6,$09,$11,$17,$05,$12,$14	       * 11
	 dc.b $19,$15,$09,$0F,$10,$bb,$Bd,$0A,$00,$01	       * 21
	 dc.b $13,$04,$06,$07,$08,$0A,$0B,$0C,$9B,$87	       * 31
	 dc.b $00,$00,$BC,$1A,$18,$03,$16,$02,$0E,$0D	       * 41
	 dc.b $8C,$8E,$8F,$00,$00,$00,$00,$e2,$E9,$ED	       * 51
	 dc.b $F1,$F5,$F9,$EB,$EF,$F3,$F7,$FB,$00,$f9	       * 61
*
* CTRL SHIFT
*
	 dc.b $00,$1F,$81,$A0,$83,$84,$85,$BE,$86,$8A,$88      * 00
	 dc.b 137,$BF,139,$C3,$FD,$B1,$B7,$A5,$B2,$B4	       * 11
	 dc.b $B9,$B5,$A9,$AF,$B0,$1B,$1D,$FE,$00,$A1	       * 21
	 dc.b $B3,$A4,$A6,$a7,$a8,$aa,$ab,$ac,$9a,$82	       * 31
	 dc.b $1e,$00,$1c,$ba,$b8,$a3,$b6,$a2,$ae,$ad	       * 41
	 dc.b $9c,$9e,$9f,$00,$00,$00,$00,$e6,$eb,$ef	       * 51
	 dc.b $f3,$f7,$fb,$eb,$ef,$f3,$f7,$fb,$00,$f9	       * 61

*
* key translation table normal for german keyboards
*
kbdd_tab
	 dc.b $00,$1B,$31,$32,$33,$34,$35,$36,$37,$38,$39  * 00
	 dc.b $30,'œ',$27,194,009,$71,$77,$65,$72,$74	   * 11
	 dc.b $7a,$75,$69,$6F,$70,$87,$2b,$0A,$00,$61	   * 21
	 dc.b $73,$64,$66,$67,$68,$6A,$6B,$6C,$84,'€'	   * 31
	 dc.b '#',$00,'<','y',$78,$63,$76,$62,$6E,$6D	   * 41
	 dc.b $2C,$2E,'-',$00,$00,$00,$20,$E0,$E8,$EC	   * 51
	 dc.b $F0,$F4,$F8,$EA,$EE,$F2,$F6,$FA,$00,$F9	   * 61
*
* SHIFT
*
	 dc.b $00,$7f,'!','"','¶','$','%','&','/','(',')'      * 00
	 dc.b '=','?',$5c,$c1,$fd,'Q','W','E','R','T'	       * 11
	 dc.b 'Z','U','I','O','P','§','*',$fe,$00,'A'	       * 21
	 dc.b 'S','D','F','G','H','J','K','L','¤',' '	       * 31
	 dc.b '^',$00,'>','Y','X','C','V','B','N','M'	       * 41
	 dc.b ';',':','_',$00,$00,$00,$fc,$00,$ea,$ee	       * 51
	 dc.b $f2,$f6,$fe,$ea,$ee,$f2,$f6,$fe,$00,$f9	       * 61
*
* CTRL
*
	 dc.b $00,$00,$91,$92,$93,$94,$95,$96,$60,$7c,$5b      * 00
	 dc.b $5d,$7b,$40,$C6,$09,$11,$17,$05,$12,$14	       * 11
	 dc.b $19,$15,$09,$0F,$10,189,$bd,$0A,$00,$01	       * 21
	 dc.b $13,$04,$06,$07,$08,$0A,$0B,$0C,$9B,']'	       * 31
	 dc.b $7d,$00,$7e,$1A,$18,$03,$16,$02,$0E,$0D	       * 41
	 dc.b $8C,$8E,$8F,$00,$00,$00,$00,$00,$E9,$ED	       * 51
	 dc.b $F1,$F5,$F9,$EB,$EF,$F3,$F7,$FB,$00,$f9	       * 61
*
* CTRL SHIFT
*
	 dc.b $00,$1F,$81,$bc,$83,$8d,$85,$BE,$86,$8A,$88      * 00
	 dc.b $89,$BF,$AB,$C3,$FD,$B1,$B7,$A5,$B2,$B4	       * 11
	 dc.b $B9,$B5,$A9,$AF,$B0,$bb,$1D,$FE,$00,$A1	       * 21
	 dc.b $B3,$98,$A6,$9d,$a8,$aa,$ab,$ac,$9a,$99	       * 31
	 dc.b $8b,$00,$1c,$ba,$b8,$a3,$90,$a2,$ae,$ad	       * 41
	 dc.b $97,$9e,$9f,$00,$00,$00,$00,$00,$eb,$ef	       * 51
	 dc.b $f3,$f7,$fb,$eb,$ef,$f3,$f7,$fb,$00,$f9	       * 61
	 dc.w 0
*
	 end
