	section	 returns
	xdef	rt_int
	xdef	rt_str
	xdef	rt_fp
	xdef	rt_fp2
	xdef	float
	xdef	rt_str2

; RT_INT:  returns an integer to Basic
; on entry, D4 contains integer to send
rt_int	MOVEQ	#2,D1
	MOVE.L	$58(A6),A1
	MOVE.W	$11A,A2
	JSR	(A2)		; get space on maths stack
	MOVE.L	$58(A6),A1
	SUBQ.L	#2,A1
	MOVE.L	A1,$58(A6)
	MOVE.W	D4,(A6,A1.L)
	MOVEQ	#3,D4
	MOVEQ	#0,D0
	RTS

; RT_FP: converts a long word (in D2) into a floating point
; & returns it to BASIC
; v 1.00	20.3.1990 	 W. Lenerz 1990
;
rt_fp	MOVE.L	D2,D5
	MOVEQ	#6,D1
	MOVE.L	$58(A6),A1
	MOVE.W	$11A,A2
	JSR	(A2)		; get space on maths stack for return value
	MOVE.L	D5,D2
	MOVE.L	$58(A6),A1
	SUBQ.L	#6,A1
	MOVE.L	A1,$58(A6)
rt_fp2	BSR.S	float		; convert long into float
	MOVE.W	D0,(A6,A1.L)
	MOVE.L	D2,2(A6,A1.L)
	MOVEQ	#2,D4
	MOVEQ	#0,D0
	RTS


; FLOAT	converts long int in D2 into floating point
; uses D2,D0
; v. 2.00 5.6.1990	 W. Lenerz 1990

float	MOVEQ	#0,D0
	TST.L	D2		; is it 0?
	BEQ.S	float2		; if 0, no need to convert
	MOVE.W	#$0820,D0
floatlp1	SUBQ.W	#1,D0		; Conversion long word ½ Float
	ASL.L	#1,D2
	BVC.S	floatlp1
	ROXR.L	#1,D2
float2	RTS


; RT_STR	: returns a string to S_BASIC
; A3 points to string (NOT) preceded by length, D4 contains length of string
; D4 MUST not count the length word (this is added automatically)
; v. 2.00 6.4.1990

rt_str	MOVEQ	#0,D1
	MOVE.W	D4,D1
	ADDQ.W	#3,D1
	BCLR	#0,D1		; make sure D1 is even
	MOVE.L	D1,D5		; keep it
	MOVE.L	$58(A6),A1
	MOVE.W	$11A,A2
	JSR	(A2)		; get space on maths stack
	SUB.L	D5,$58(A6)
	MOVE.L	$58(A6),A1
	MOVE.W	D4,(A6,A1.L)
	BEQ.S	end1
	ADDQ.L	#2,A1
	ADDQ.W	#1,D4
	LSR.W	#1,D4		; divide by two, for word sized ops
	SUBQ.W	#1,D4		; minus one, for DBF
strlp1	MOVE.W	(A3)+,(A6,A1.L)
	ADDQ.L	#2,A1
	DBF	D4,strlp1
end1	MOVEQ	#0,D0
	MOVEQ	#1,D4
	RTS


; RT_STR2  : updates a string parameter
;
; ENTRY					EXIT
;
; A0 points to string (not) preceded by length	one after the last char returned
; A3					SMASHED
; A5 points to parameter to be filled in !!!!	smashed <---
; D4 contains length of string this MUST not
; count the length word (added automatically)	smashed
; v. 2.10 1990 Dec 13 21:35:42

rt_str2	MOVEQ	#0,D1
	MOVE.W	D4,D1
	ADDQ.W	#3,D1
	BCLR	#0,D1		; make sure D1 is even & add length word
	MOVE.L	D1,D5		; keep it
	MOVE.L	$58(A6),A1
	MOVE.W	$11A,A2
	JSR	(A2)		; get space on maths stack
	SUB.L	D5,$58(A6)
	MOVE.L	$58(A6),A1
	MOVE.W	D4,(A6,A1.L)
	BEQ.S	not2
	ADD.L	#2,A1
	SUBQ.W	#1,D4		; minus one, for DBF
str2lp1	MOVE.B	(A0)+,(A6,A1.L)
	ADDQ.L	#1,A1
	DBF	D4,str2lp1
not2	MOVE.L	A5,A3
	ADDQ.L	#8,A5		; point to next parameter
another	MOVE.L	A0,-(A7)
	MOVE.W	$120,A2
	JSR	(A2)
	MOVEM.L	(A7)+,A0		; preserve error code
	RTS


	END
