; SMSQ QXL Host Module
; 2004-07-20		1.01		config items for drive letters (BC)

	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_keys_stella_bl'
	include 'dev8_mac_config02'

	section host

host
	bra.l	jump	     ; $00
qxl_base dc.b	$2b	     ; $04
	 dc.b	0,0,0
	 dc.l	0,0	     ; $08
	 dc.l	0,0,0,0    ; $10
smsq_len dc.l	0	     ; $20

drl_it1 dc.b	'C'	   ; $24
drl_it2 dc.b	'D'
drl_it3 dc.b	'E'
drl_it4 dc.b	'F'
drl_it5 dc.b	'G'
drl_it6 dc.b	'H'
drl_it7 dc.b	'I'
drl_it8 dc.b	'J'

drl_uu1 dc.b	'K'	   ; usage undefined
drl_uu2 dc.b	'L'
drl_uu3 dc.b	'M'
drl_uu4 dc.b	'N'
drl_uu5 dc.b	'O'
drl_uu6 dc.b	'P'

	xref.l	smsq_vers
	mkcfhead {Host},{smsq_vers}

       mkcfitem 'OSPB',code,'B',qxl_base,,,\
       {PC IO Port Address of QXL Card}\
       $28,,{0280h},$29,,{0290h},$2a,,{02A0h},$2b,,{02B0h}\
       $2c,,{02C0h},$2d,,{02D0h},$2e,,{02E0h}\
       $30,,{0300h},$31,,{0310h},$32,,{0320h},$33,,{0330h}\
       $34,,{0340h},$35,,{0350h},$36,,{0360h}

	dc.l	'OSW1'
	dc.b	cf.code,0
	dc.w	drl_it1-*
	dc.w	0,0
	dc.w	drl_ds1-*
	dc.w	drl_atr-*

	dc.l	'OSW2'
	dc.b	cf.code,0
	dc.w	drl_it2-*
	dc.w	0,0
	dc.w	drl_ds2-*
	dc.w	drl_atr-*

	dc.l	'OSW3'
	dc.b	cf.code,0
	dc.w	drl_it3-*
	dc.w	0,0
	dc.w	drl_ds3-*
	dc.w	drl_atr-*

	dc.l	'OSW4'
	dc.b	cf.code,0
	dc.w	drl_it4-*
	dc.w	0,0
	dc.w	drl_ds4-*
	dc.w	drl_atr-*

	dc.l	'OSW5'
	dc.b	cf.code,0
	dc.w	drl_it5-*
	dc.w	0,0
	dc.w	drl_ds5-*
	dc.w	drl_atr-*

	dc.l	'OSW6'
	dc.b	cf.code,0
	dc.w	drl_it6-*
	dc.w	0,0
	dc.w	drl_ds6-*
	dc.w	drl_atr-*

	dc.l	'OSW7'
	dc.b	cf.code,0
	dc.w	drl_it7-*
	dc.w	0,0
	dc.w	drl_ds7-*
	dc.w	drl_atr-*

	dc.l	'OSW8'
	dc.b	cf.code,0
	dc.w	drl_it8-*
	dc.w	0,0
	dc.w	drl_ds8-*
	dc.w	drl_atr-*

	mkcfend

drl_ds1 dc.w	26,'DOS Drive Letter for WIN1_'
drl_ds2 dc.w	26,'DOS Drive Letter for WIN2_'
drl_ds3 dc.w	26,'DOS Drive Letter for WIN3_'
drl_ds4 dc.w	26,'DOS Drive Letter for WIN4_'
drl_ds5 dc.w	26,'DOS Drive Letter for WIN5_'
drl_ds6 dc.w	26,'DOS Drive Letter for WIN6_'
drl_ds7 dc.w	26,'DOS Drive Letter for WIN7_'
drl_ds8 dc.w	26,'DOS Drive Letter for WIN8_'

drl_atr dc.w	'CC',1,'C','DD',1,'D','EE',1,'E','FF',1,'F'
	dc.w	'GG',1,'G','HH',1,'H','II',1,'I','JJ',1,'J'
	dc.w	'KK',1,'K','LL',1,'L','MM',1,'M','NN',1,'N'
	dc.w	'OO',1,'O','PP',1,'P','QQ',1,'Q','RR',1,'R'
	dc.w	'SS',1,'S','TT',1,'T','UU',1,'U','VV',1,'V'
	dc.w	'WW',1,'W','XX',1,'X','YY',1,'Y','ZZ',1,'Z'
	dc.w	-1

jump
	sf	ini_ouch
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0
	jmp	(a0)

	section trailer
fixup
	dc.l	smsq_len-*		 ; set length
	dc.l	0			 ; no offset
	dc.l	0			 ; done

	dc.l	' END'
host_end
trailer
	dc.l	0			 ; no header
	dc.l	trailer-host		 ; length of module
	dc.l	0			 ;
	dc.l	0			 ; checksum
	dc.l	fixup-host		 ; fixup
	dc.l	*+4-host

	end
