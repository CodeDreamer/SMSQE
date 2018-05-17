; concatenate files
; EX cct,file1$,file2$,... filex$,lib$
; where:
; cct	=  this software
; files$	=  name of the files which are to be concatenated
; lib$	=  name of resulting concatenated file
; this is a "specail" program inthe QODS sense, i.e.
; a new $4afb flag after the job name

	FILETYPE	1

	section cct

	DATA	1000


	BRA.S	start		; normal job header
	DC.W	'W.L.'		; vanity
	DC.W	$4AFB
	DC.W	len-*-2
	DC.B	'CCT'
len
	dc.w	$4afb		; "special" job header
; this is the routine called by EX as part of thebasic command (!)

ex_entry	MOVE.L	A0,A2		; put the utility address somewhere safer
	EXG	A5,A3		; we will process the params from the back end
	MOVEQ	#-15,D0		; preset error to invalid parameter
	SUBQ.L	#2,D7		; there must be at least 2 channels
	BLT.S	ex_rts		; ... but there aren't
	MOVEQ	#0,D0		; ... oh yes there are!
ex_loop	SUBQ.L	#8,A3		; move down one
	CMP.L	A5,A3		; any more parameters
	BLT.S	ex_rts		; ... no
	JSR	(A2)		; get a string from the command line
	BLT.S	ex_rts		; ... oops
	BGT.S	ex_put_id 	; ... #n
	MOVEQ	#1,D3		; the file is an input
	TST.W	D5		; last file?
	BNE.S	ex_open		; ... no ->...
	MOVEQ	#3,D3		; ... yes, open overwrite
ex_open	JSR	2(A2)		; open the file
	BNE.S	ex_rts		; ... oops
ex_put_id MOVE.L	A0,-(A4)		; put the id on the stack
	ADDQ.L	#1,D5		; one more
	BRA.S	ex_loop		; and look at next
ex_rts	RTS


start	MOVEQ	#-15,D0		; preset error
	MOVE.W	(A7)+,D7		; number of files, the last one is the result file
	BEQ	hara		; must be at least 2 ->....
	SUBQ.W	#1,D7
	BEQ	hara		; must be at least 2 & prepare dbf
	MOVE.W	D7,D2		; nbr of last file
	LSL.W	#2,D2		; channel ids are long words
	MOVE.L	(A7,D2.W),A5	; channel ID for result file

	MOVEQ	#40,D1
	BSR.S	get_mem		; get mem into A0
	BNE.S	hara		; oops ->...
	MOVE.L	A0,D5		; D5 = memo
	MOVEQ	#-1,D3		; timeout

concat	MOVE.L	(A7)+,A0		; channel ID for next file to concat
	CMP.L	A0,A5		; same as output?
	BEQ.S	harakiri		; yes, end reached ->...
	MOVE.L	A0,A4		; keep channel ID
	MOVEQ	#38,D2		; space needed for reading file header
	MOVE.L	D5,A1		; point to space
	MOVEQ	#$47,D0
	TRAP	#3		; read file header
	TST.L	D0
	BNE.S	harakiri		; ooops->...
	MOVE.L	D5,A1		; file header
	MOVE.L	(A1),D4		; D4 = file length
	BEQ.S	concat		; 0 length file (???) ->...
	MOVE.L	D4,D1		; memory space need for this file
	BSR.S	get_mem		; get it
	BNE.S	harakiri		; ooops->...
	MOVE.L	A0,A1		; point to space now
	EXG	A0,A4		; A0 = channel ID, A4 =  memory
	MOVE.L	D4,D2		; how much we read in (all of it)
	MOVEQ	#$48,D0
	TRAP	#3		; read entire file
	TST.L	D0
	BNE.S	harakiri
	MOVEQ	#2,D0
	TRAP	#2		; close file
	MOVE.L	A4,A1		; A1 = start of mem for this file
	MOVE.L	D4,D2		; D2 = length
	MOVE.L	A5,A0		; A0 = output file ID
	MOVEQ	#$49,D0
	TRAP	#3		; send the bytes
	TST.L	D0
	BNE.S	harakiri
	MOVE.L	A4,A0		; release memory
	BSR.S	rel_mem
do_loop	BRA.S	concat		; and do again

myregs	REG	D1-D3/A1-A3
get_mem	MOVEM.L	myregs,-(A7)	; typical get mem call
	MOVEQ	#-1,D2		; for myself
	MOVEQ	#$18,D0		; MT_ALCHP
	TRAP	#1
	MOVEM.L	(A7)+,myregs
	TST.L	D0
	RTS

rel_mem	MOVEM.L myregs,-(A7)
	MOVEQ	#$19,D0		; MT_RECHP
	TRAP	#1
	MOVEM.L	(A7)+,myregs
	TST.L	D0
	RTS

harakiri	MOVE.L	D0,D7		; keep error
	MOVE.L	D5,A0		; point to mem for file headers
	BSR.S	rel_mem		; release it
	MOVE.L	D7,D0		; get original error back
hara	MOVE.L	D0,D3		; put it in reg now
hara3	MOVEQ	#-1,D1
	MOVEQ	#5,D0
	TRAP	#1		; remove this job now

	END
