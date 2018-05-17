; Atari keyboard tables     1988 / 2000  Tony Tebby

	section kbd

	xdef	kbd_krtab
	xdef	kbd_atab


	include 'dev8_smsq_kbd_keys'


;	 KEYROW table	high nibble: bit, low nibble: row

kbd_krtab
	dc.b	$08,$31,$34,$16   ; 00
	dc.b	$14,$60,$20,$26   ; 04
	dc.b	$70,$06,$05,$56   ; 08
	dc.b	$55,$53,$18,$35   ; 0c
	dc.b	$36,$15,$46,$45   ; 10
	dc.b	$66,$65,$76,$25   ; 14
	dc.b	$75,$54,$03,$02   ; 18
	dc.b	$01,$17,$44,$33   ; 1c
	dc.b	$64,$43,$63,$24   ; 20
	dc.b	$74,$23,$04,$73   ; 24
	dc.b	$72,$52,$07,$08   ; 28
	dc.b	$12,$37,$32,$47   ; 2c
	dc.b	$42,$67,$62,$77   ; 30
	dc.b	$22,$57,$07,$28   ; 34
	dc.b	$27,$61,$13,$10   ; 38
	dc.b	$30,$40,$00,$50   ; 3c
	dc.b	$38,$48,$58,$68   ; 40
	dc.b	$78,$09,$19,$29   ; 44
	dc.b	$21,$39,$49,$11   ; 48
	dc.b	$59,$41,$69,$79   ; 4c
	dc.b	$71,$0a,$1a,$2a   ; 50
	dc.b	$3a,$4a,$5a,$6a   ; 54
	dc.b	$7a,$0b,$1b,$2b   ; 58
	dc.b	$3b,$4b,$5b,$6b   ; 5c
	dc.b	$7b,$0c,$1c,$2c   ; 60
	dc.b	$3c,$4c,$5c,$6c   ; 64
	dc.b	$7c,$0d,$1d,$2d   ; 68
	dc.b	$3d,$4d,$5d,$6d   ; 6c
	dc.b	$7d,$0e,$1e,$2e   ; 70

.	equ	kba.norm	normal key
s	equ	kba.shift	shift action
c	equ	kba.ctrl	control action
a	equ	kba.alt 	alt action
g	equ	kba.altgr	alt gr action
k	equ	kba.caps	caps
l	equ	kba.slock	scroll lock

y	equ	kba.sys 	system request
u	equ	kba.undo	undo (and hard reset)
b	equ	kba.break	pause / break action (and hard reset)
t	equ	kba.tab 	tab (and soft reset)

kbd_atab
	dc.b	.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,T
	dc.b	.,.,.,.,.,.,.,.,.,.,.,.,.,C,.,.
	dc.b	.,.,.,.,.,.,.,.,.,.,S,.,.,.,.,.
	dc.b	.,.,.,.,.,.,S,.,A,.,K,.,.,.,.,.
	dc.b	.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.
	dc.b	.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.
	dc.b	.,U,.,.,.,.,.,.,.,.,.,.,.,.,.,.
	dc.b	.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.

	end
