; ATARI:  TOS header for bootloader

	section base

	dc.w	$FAFA,1 		 ; patch table
	dc.l	text-*

base
	bra.s	start
text	dc.l	-(start-base)-4 	 ; + length of file makes 0000 = fixup
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
	bra.s	head_end+8
head_end
	end
