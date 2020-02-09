	section none

	xref	sb_addstt
	xref	rt_int
	include dev8_keys_sbasic

; macro to define keywords where name & name of routine are the same
kwrd	macro	name
	dc.w	[name]-*
	dc.b	[.len(name)]
	dc.b	'[name]'
	endm

debut
	MOVE.W	$110,A2
	LEA	procs,A1
	JMP	(A2)		; link in new keywords
    

procs	DC.W	0,0,2		; pretend there are 2 keywords as name too long
	kwrd	CURRENT_LINE
	dc.l	0

current_line
	moveq	#0,d4
	tst.b	sb_cmdl(a6)	; command line parsing?
	bne	rt_int		; ... yes, so return line number 0
	cmp.l	#'SBAS',-4(a6)	; Sbasic ?
	beq.s	sbas		;    ... yes
	move.w	sb_line(a6),d4	;    ... no, try to get line number
	bra	rt_int		;
; this is copied from TT's sbas_sttadd_asm
sbas	move.l	a4,d0
	subq.l	#2,d0		; this is in "current" statement
	jsr	sb_addstt	; find line and statement number
	swap	d0		; line number in upper word
	move.w	d0,d4		; return it now
	bra	rt_int

	end
