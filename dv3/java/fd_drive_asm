; DV3 JAVA Floppy Disk get/set drive name

; 0.01 put A1 into ari stack pointer after java call
; 0.00 @ 2014 W. Lenerz


	section dv3

	xdef	flp_drive
	xdef	flp_drive$

	xref	ut_chkri
	xref	ut_gxin1
	xref	ut_retst
	xref	ut_gxst1


	include 'dev8_keys_java'
	include 'dev8_keys_sbasic'


***************************************************
*
* result$=flp_drive$(x)
*	where x = the drive (1...8)
*
* returns the native filename as string
*
*
****************************************************

flp_drive$
	move.l	#260,d1
	jsr	ut_chkri		; ensure room for 256 bytes on maths stack
	move.w	#0,(a6,a1.l)		; preset wrong parameter or no parameter
	jsr	ut_gxin1		; get exactly one int, or nothing
	moveq	#jt8.getd,d0
	dc.w	jva.trp8		; get data to java & back on the ari stack
	move.l	a1,sb_arthp(a6)
	tst.l	d0			; ooops, bad parameter
	bne.s	out
	jmp	ut_retst		; return data

***************************************************
*
* flp_drive(x,string$)
*	where x = the drive (1...8)
*	string$ is the native filename to be set
*
****************************************************


flp_drive
	subq.l	#8,a5
	jsr	ut_gxin1		; get one int
	bne.s	out
	move.w	(a6,a1.l),d6		; drive number
	move.l	a5,a3
	addq.l	#8,a5
	jsr	ut_gxst1		; get one string
	bne.s	out
	moveq	#jt8.setd,d0
	dc.w	jva.trp8		; get data to java
	tst.l	d0
out	rts


	end
