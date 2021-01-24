	section dv3

	xdef	qlsd_vers

; 1.00	First release
;
; 1.01	Output version information in ROM message
;	Output if card was found after ROM message
;	Auto-boot: use MDV name if card was in, automatically revert to WIN
;	Removed device name configuration option as auto-boot does the job
;	Fixed data corruption when reading and writing at the same time
;	Added code cache handling on SGC
;	Implemented dynamic timing depending on machine
;	CARD_INIT defaults to "card 1" when no parameter is given
;
; 1.02	Disabled auto-boot on SMSQ/E as that doesn't boot mdv1_boot
;	Fixed SMSQ/E compatibility
;
; 1.03	Support for multi-block read and write
;	Support for hot-swapping: card changes are detected, even if just the
;	  same card was written to by another QL in the meantime
;	WIN2 now defaults to QXL.WIN on card 2, WIN3 to QXL.WIN on card 3
;
; 1.04	Added command WIN_CHECK to check if container on SD is fragmented
;	Fixed QDOS compatibility (tested MGG and JS)
;	Added workaround for double-ROM init on JS rom
;
; 1.05	Output hardware revision on start
;	Extended driver definition block for 3rd party software
;	Fixed card-change detection when using two drives simultaneously
;
; 1.06	Fixed WIN_CHECK for all drives other than WIN1
;	Changed WIN defaults:
;	  WIN1: QXL.WIN (card1)
;	  WIN2: QXL.WIN (card2)
;	  WIN3..8: QXL3.WIN, QXL4.WIN, QXL5.WIN... (card1)
;
; 1.07	Fixed compatibility with DEV device under Minerva
;
; 1.08	Allow SDs with QLWA images on sector 0 (mount .WIN as virtual SD)
;
; 1.09	Fixed rare Minerva heap corruption issue
;	Always assumes card was changed when it was ejected
;	ROM version now includes TK2 QL-network low-level hardware routines

qlsd_vers equ  '1.09'

	end
