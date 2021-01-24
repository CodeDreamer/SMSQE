; QD_BAD: check for QD cancer

	section none

	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_hdr'


*****************************************************************
*
*  Check for, and possibly correct, QD line feed cancer, where QD has inserted
*  unwanted line feeds before the last line in the file.
*
*  Note: "NOLFL" = Not Only Line Feeds Line-> a line with something else than
*	========   LFs in it
*
*  res=QD_BAD(filename$[,threshold%])
*  where
*    filename$	is the name of the file to check (must be a string, no coercion)
*
*    threshold% if this is present and non zero, the file will not only be
*		checked, but also corrected if necessary : If there are a least
*		as many LFs as the threshold value, between the last NOLFL and
*		the NOLFL before that, then the extraneous LFs are stripped, and
*		the file is resaved with the same dates and version number. When
*		this happens there will one empty line between the penultimate
*		and the last line.
*
*    res is the return from the function, the value depends on threshold%:
*	    if threshold% = 0
*		Low word = nbr of LFs between start of last line and end of
*		last NOLF line above.
*		High word : nbr of line feeds between end of file and first NOLF line
*
*	    if threshold%<> 0: 0 if no changes were made, else 1
*
*	    if error when opening/saving file etc : res= negative error  code
*
****************************************************************

qd_bad	move.w	sb.inipr,a2
	lea	procs,a1
	jmp	(a2)		; init new keword

procs	dc.l	0
	dc.w	2
	dc.w	qdbad-*
	dc.b	6,'QD_BAD'
	dc.w	cvdate-*
	dc.b	7,'CV_DATE'
	dc.l	0

err_bp	moveq	#err.ipar,d0
err_out move.l	d0,d7
	bra	rt_fp

qdbad	moveq	#0,d7		; preset no threshold
	addq.l	#8,a3
	cmp.l	a3,a5		; any param?
	blt.s	err_bp		; no param at all, error ->
	beq.s	getstr		; no threshold parameter ->
	move.w	sb.gtint,a2
	jsr	(a2)		; get integer (if more than 1, rest is ignored)
	bne.s	err_bp		; pb ->
	lsl.l	#1,d3		; each param takes 2 bytes
	beq.s	err_bp		; no integer got, complain ->
	add.l	d3,sb_arthp(a6) ; reset ari stack
	move.w	(a6,a1.l),d7	; d7 = threshold
getstr	move.l	a3,a5		; don't care how many ints were actually passed
	subq.l	#8,a3
	move.w	sb.gtstr,a2
	jsr	(a2)		; get file name
	bne.s	err_bp
	move.w	(a6,a1.l),d0	; string length
	addq.w	#3,d0
	bclr	#0,d0
	add.l	d0,sb_arthp(a6) ; reset ari stack
	move.l	a1,a0		; pointer to filename
	moveq	#ioa.open,d0	; open file...
	moveq	#ioa.kexc,d3	; ... exlusive and for read/write
	moveq	#-1,d1		; ... for myself
	trap	#4		; ... with name rel to a6
	trap	#2		; open now, channel ID goes into a0
	tst.l	d0
	bne	rt_fp		; oops
	move.l	sb_buffb(a6),a1 ;
	move.l	a1,a5
	moveq	#-1,d3
	moveq	#iof.rhdr,d0	; read file header...
	moveq	#64,d2		; ... entirely
	trap	#4		; ... into (a6,a1.l)
	trap	#3		; now
	tst.l	d0
	bne	close		; oops
	move.l	(a6,a5.l),d6	; length of file
	beq	close		; no length (?), we're done
	move.l	d6,d1
	addq.l	#8,d1
	move.l	a0,a3		; a3 = channel ID
	bsr	get_mem 	; get mem for file
	exg	a0,a3		; (this doesn't change cc) a0= ch ID, a3= mem
	bne	close		; oops
	move.l	a3,a1		; a1 = a3 = mempointer
	move.l	d6,d2
	moveq	#iof.load,d0
	trap	#3		; load entire file into (a1) now
	tst.l	d0
	bne	rel_mem 	; ooops
	move.l	a3,a1		; start of file
	add.l	d6,a1		; point end of file
	moveq	#9,d3		; TAB
	moveq	#10,d4		; LF
	move.l	d6,d5		; nbr of bytes in file
	subq.w	#1,d5
; loop to find last NOLFL, tab is NOT considered to be a non-LF character but
; space is
lp1	move.b	-(a1),d1
	sub.b	d3,d1		; is tab ?
	beq.s	do_lp1		; yes, continue, but don't count as LF
	subq.b	#1,d1		; LF?
	bne.s	set_end 	; no, found last line
do_lp1	dbf	d5,lp1
	swap	d0		; d0 .H number of LFs after last line
	bra.s	rel_mem 	; if we get here, start of file reached, done

; reached last non LF char of last NOLFL, now go to start of it
set_end move.l	a1,a5		; a5 = 1 past end of last NOLFL
endLF_1 cmp.b	-(a1),d4	; LF?
	beq.s	end_LF2 	; yes, found start of last NOLF
	dbf	d5,endLF_1	; go to start of it
	bra.s	rel_mem 	; if we get here, start of file reached, done

end_LF2 swap	d0		; d0 .H number of LFs after last line
	move.l	a1,a4		; a4 = 1 past start of last NOLFL

; we're at the start of the last line, count the LFs till the preceeding NOLFL
lp2	move.b	-(a1),d1
	sub.b	d3,d1		; is tab ?
	beq.s	do_lp2		; yes, ignore, but don't count
	subq.b	#1,d1		; LF?
	bne.s	endchk		; no, so end of previous line reached
	addq.w	#1,d0		; one more LF
do_lp2	dbf	d5,lp2

; here
; d0 counter .H LFs past last NOLFL .L = LFs between last and next to last NOLFL
; d4 LF
; d7 threshold
; a0 channel ID
; a1 last char of penultimate NOLFL
; a3 work space
; a4 1 past start of last NOLFL
; a5 last char of last NOLFL

endchk	tst.l	d7		; check only?
	beq.s	rel_mem 	; yes, release mem, close chan, return d0

; maybe change file
	sub.w	d0,d7		; is there a need to change?
	ble.s	change		; yes
	moveq	#0,d0		; no, return 0
	bra.s	rel_mem

; must change file by moving last line up, also remove superfluous LFs at end
change	addq.l	#1,a1		; after prenultimate line
	move.b	d4,(a1)+	; set end of line
	move.b	d4,(a1)+	; add one empty line
	addq.l	#1,a4
	addq.l	#1,a5
cpy_lp	move.b	(a4)+,(a1)+	; copy...
	cmp.l	a4,a5		; ... till end of last line
	bne.s	cpy_lp
	move.b	d4,(a1)+	; set LF at end
	move.l	a1,d2		; end of file
	sub.l	a3,d2		; - start of file = file length
	moveq	#0,d1		; set file position to start of file
	moveq	#iof.posa,d0
	trap	#3
	moveq	#iof.trnc,d0
	trap	#3		; truncate file
	move.l	a3,a1
	moveq	#iof.save,d0
	moveq	#-1,d3
	trap	#3		; save entire file

; now set update and backup dates and version to old values
	move.l	sb_buffb(a6),a1 ; buffer with old file header
	move.l	hdr_date(a6,a1.l),d1 ; old file update date
	moveq	#0,d2		; signal set file update date
	moveq	#iof.date,d0
	trap	#3
	move.l	hdr_bkup(a6,a1.l),d1 ; old bckp date
	moveq	#1,d2		; signal set bckp date
	moveq	#iof.date,d0
	trap	#3
	move.w	hdr_vers(a6,a1.l),d2 ; old version
	moveq	#0,d1		; signal set version
	moveq	#iof.vers,d0
	trap	#3
	moveq	#1,d0		; show file was changed

rel_mem move.l	d0,d7
	exg	a3,a0		; a0 =	mem, a3 =channel id
	bsr.s	fre_mem 	; release mem
	move.l	a3,a0		; a0 =channel ID
	move.l	d7,d0

close	move.l	d0,d7		; close file
	moveq	#ioa.clos,d0
	trap	#2

; return value in d7 as fp
; rt_fp: converts a long word (in d7) into a floating point
rt_fp	moveq	#6,d1
	move.l	sb_arthp(a6),a1
	move.w	$11a,a2
	jsr	(a2)		; get space on maths stack for return value
	subq.l	#6,sb_arthp(a6)
	move.l	sb_arthp(a6),a1
rt_fp2	bsr.s	float		; convert long into float
	move.w	d0,(a6,a1.l)
	move.l	d7,2(a6,a1.l)
	moveq	#2,d4		; ret float
	moveq	#0,d0
out	rts


; converts long int in d7 into floating point
float	moveq	#0,d0
	tst.l	d7		; is value 0?
	beq.s	flt_out 	; yes, so no need to convert
	move.w	#$820,d0
flt_lp1 subq.w	#1,d0		; conversion long word ½ float
	asl.l	#1,d7
	bvc.s	flt_lp1
	roxr.l	#1,d7
flt_out rts


; gets memory, amount to get in D1 on entry
my_reg	reg	d1-d3/a1-a3
get_mem movem.l my_reg,-(a7)	; this gets mem for current job
	moveq	#-1,d2
	moveq	#sms.achp,d0
	trap	#1
	tst.l	d0
	movem.l (a7)+,my_reg
	rts

; free memory (pointed to by a0)
fre_mem movem.l my_reg,-(a7)
	moveq	#sms.rchp,d0
	trap	#1
	movem.l (a7)+,my_reg
	rts



cvdate	move.w	sb.gtint,a2
	jsr	(a2)		; get 6 integers
	bne	err_bp		  ; pb ->
	subq.w	#6,d3
	bne	err_bp
	moveq	#12,d3
	add.l	d3,sb_arthp(a6)
	move.w	cv.datil,a2
	add.l	a6,a1
	jsr	(a2)
	sub.l	a6,a1
	move.l	d1,d7
	bra.s	rt_fp

	end
