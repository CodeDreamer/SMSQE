; SMSQ IBM internal key tables	 2000 Tony Tebby
;
; 2000-06-24  1.01  Handles left and right shift key separately. (MK)
; 2007-05-09  1.02  Fixed keyrow line 1, bit 5 ("\" on English kbds) (MK)

	section kbd

	xdef	kbd_krtab
	xdef	kbd_atab

	include 'dev8_smsq_kbd_keys'

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
	dc.b	$72,$52,$07,$51   ; 28
	dc.b	$12,$37,$32,$47   ; 2c
	dc.b	$42,$67,$62,$77   ; 30
	dc.b	$22,$57,$07,$28   ; 34
	dc.b	$27,$61,$13,$10   ; 38
	dc.b	$30,$40,$00,$50   ; 3c
	dc.b	$38,$48,$58,$68   ; 40
	dc.b	$78,$09,$19,$29   ; 44
	dc.b	$39,$49,$59,$69   ; 48
	dc.b	$79,$0a,$1a,$2a   ; 4c
	dc.b	$3a,$4a,$5a,$6a   ; 50
	dc.b	$08,$08,$7a,$08   ; 54
	dc.b	$08,$08,$08,$08   ; 58
	dc.b	$1e,$08,$08,$08   ; 5c
	dc.b	$08,$08,$08,$08   ; 60
	dc.b	$08,$08,$08,$0d   ; 64
	dc.b	$21,$2d,$3d,$11   ; 68
	dc.b	$5d,$41,$7d,$0e   ; 6c
	dc.b	$71,$2e,$3e,$4e   ; 70
	dc.b	$08,$08,$08,$08   ; 74
	dc.b	$08,$08,$08,$08   ; 78
	dc.b	$01,$08,$08,$08   ; 7c

.	equ	kba.norm	normal key
s	equ	kba.shift	shift action
h	equ	kba.shftr
c	equ	kba.ctrl	control action
r	equ	kba.ctrlr
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
	dc.b	.,.,.,.,.,.,H,.,A,.,K,.,.,.,.,.
	dc.b	.,.,.,.,.,.,L,.,.,.,.,.,.,.,.,.
	dc.b	.,.,.,.,Y,.,.,.,G,.,.,.,.,R,.,.
	dc.b	.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.
	dc.b	.,.,.,.,.,B,.,.,.,.,.,.,.,.,.,.

	end
