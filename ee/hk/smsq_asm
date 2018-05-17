; HOTKEY Texts etc for SMSQ   V2.01     1994  Tony Tebby
;
; 2002-01-13  2.01  Added configuration block (MK)

	section hotkey

	xdef	hk_smsq      ; to load it
hk_smsq

	xdef	hktx_llrc
	xdef	hktx_grab

	xdef	met_dnam
	xdef	hk_stufc
	xdef	hk_stufp
	xdef	hk_stufl

	xref.l	hk_vers

	include 'dev8_keys_msg8'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_text'
	include 'dev8_mac_config02'

met_dnam dc.w	0
hk_stufc dc.b	' '
hk_stufp dc.b	$fc
hk_stufl dc.l	$200

  mktext hks2,{HOTKEY System 2\}
  mktext spce,{SPACE}
  mktext exec,{EXEC}
  mktext load,{LOAD}
  mktext pick,{PICK}
  mktext wake,{WAKE}
  mktext cmd,{CMD}
  mktext key,{KEY}


hktx_llrc
	move.w	#msg8.llrc,a1
	bra.s	hktx_set
hktx_grab
	move.w	#msg8.grab,a1
hktx_set
	moveq	#sms.mptr,d0
	trap	#do.sms2
	rts

	mkcfhead  {HOTKEY System II},hk_vers

	mkcfitem 'HK21',char,'K',hk_stufc,,,\
	  {Keyboard Stuffer HOTKEY character. This stuffs the current\}\
	  {string in the HOTKEY stuffer buffer into the current keyboard\}\
	  {queue. Strings are put into the stuffer buffer by QRAM and other\}\
	  {utility programs or by the QPTR command HOT_STUFF},%10111

	mkcfitem 'HK22',char,'P',hk_stufp,,,\
	  {Previous string Keyboard Stuffer HOTKEY character. This is\}\
	  {similar to the normal Keyboard Stuffer but it accesses the\}\
	  {string defined before the current stuffer string},%10111

	mkcfitem 'HK2S',long,'S',hk_stufl,,,\
	  {Stuffer buffer size: this limits the number of stuffer strings\}\
	  {that may be held at one time},$80,$7fff
	mkcfend

	end
