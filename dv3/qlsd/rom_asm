; ROM header 1.00				 2017 W. Lenerz + M. Kilgus

	section header

base
	dc.l	$4afb0001
	dc.w	0
	dc.w	rom_init-base
	dc.w	0			; No default banner, because JS ROM sucks
; banner will be included here by the linker

	section init

	include 'dev8_keys_qlv'

	xref	banner

rom_init
	lea	base(pc),a1
	cmpa.l	#$000CC000,a1		; Are we a mirror at $000CC000?
	bne.s	no_mirror
	rts				; Yes, ignore!
no_mirror
	lea	banner(pc),a1		; Output banner only if we're no mirror
	move.w	ut.wtext,a2
	jsr	(a2)
	moveq	#0,d7			; signal ROM start
; Linker will put init code here

	end
