; ATARI Bootable File Host Module

	section base

	include 'dev8_keys_stella_bl'

base
	bra.s	start
text	dc.l	last_zero-start 	 ; points to 0 at end (TOS fixup)
	dc.l	0			 ; data
	dc.l	0			 ; BSS
	dc.l	0			 ; symbol
	dc.l	0			 ; res
	dc.l	0			 ; res
	dc.w	0			 ; res
start
	pea	boot			 ; the boot code
	move.w	#$26,-(sp)		 ; supexec
	trap	#14

boot
	move.w	 #$2700,sr
	lea	$400,sp 		 ; safe-ish stack
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0	 ; code of next module (= loader)
	jmp	(a0)

fixup
	dc.l	text-*			 ; fixup text length
	dc.l	-(start-base)-4 	 ; + length of file is last_zero-start
	dc.l	0

host_end
 ; all the rest of the modules will be inserted here
trailer
	dc.l	0			 ; no header
	dc.l	trailer-base		 ; length of module
	dc.l	0			 ; length
	dc.l	0			 ; checksum
	dc.l	fixup-base		 ; fixup
last_zero
	dc.l	0

	end
