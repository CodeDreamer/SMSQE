* Look for wild card entries   V1.2    1985  Tony Tebby  QJUMP
* 2017 Nov 19  1.03 correct handling of n8_ in wc_cntsc (wl)
*
	section exten
*
	xdef	wc_look1
	xdef	wc_look2
*
	xref	cv_lctab
	xref	stat_do 		write out statistics
	xref	ut_chkri		check for room on RI stack
	xref	ut_fopnp		get name and open file
	xref	ut_gtnm1		get a name
	xref	ut_opmds		open destination
	xref	ut_opdefx		open (with default)
	xref	ut_fdire		read directory entry
	xref	ut_fceof		close file eof is ok
*
	include dev8_sbsext_ext_keys
	include 'dev8_sbsext_ut_opdefx_keys'
*
fr_source equ	$00	pointer to source string
fr_dest   equ	$04	pointer to destination string
fr_name   equ	$08	pointer to actual name in source directory
fr_copy   equ	$0c	pointer to copy name
fr_action equ	$10	pointer to action routine
fr_dchan  equ	$14	directory channel ID
fr_diff   equ	$18	difference in length between names
fr_ftype  equ	$1a	file type
frame	  equ	$1c
fnam_len  equ	$28
*
*	d6 c	winchk control reg
*	d7 c	continue reg
*	a0 c	screen channel ID
*	a4 c	action routine address
*	a5   s	screen channel ID copy
*
* get full file name on stack
*
wc_ffnam
	moveq	#io.dir,d3		open a directory
	bsr.l	ut_fopnp
wcff_copy
	bne.s	wcf_rts
wc_ffcpy
	move.l	a1,-(sp)		save pointer to complete string
	moveq	#fnam_len,d1		check for space
	bsr.l	ut_chkri		... on RI stack
	sub.w	#fnam_len,a1
	move.l	a1,bv_rip(a6)		and move pointer down
*
	move.l	(sp)+,a2		get pointer to file name
	move.w	(a6,a2.l),d0		... and length
	beq.s	wcf_end 		... none at all
wcf_loop
	move.b	2(a6,a2.l),(a6,a1.l)	copy characters (not length)
	addq.l	#1,a2
	addq.l	#1,a1
	subq.w	#1,d0
	bgt.s	wcf_loop
wcf_end
	clr.b	(a6,a1.l)		and put a zero at end
wcf_rts
	rts
*
* WC_LOOK1 entry
*
wc_look1
	moveq	#-1,d4			single file only
	subq.l	#frame-$14,sp		create frame
	movem.l d0/d6/d7/a0/a4,-(sp)	save screen controls and action
	sub.l	a1,a1			point to null string
	bsr.s	wc_ffcpy		... and copy it as destination
	move.l	a5,d0			check number of params
	sub.l	a3,d0
	subq.l	#8,d0
	bgt.s	wc_bp			... too many
	bra.s	wc_ffset		get full filename and setup
*
* WC_LOOK2 entry
*
wc_look2
	moveq	#0,d4			set 2 file ops
	subq.l	#frame-$14,sp		create frame
	movem.l d0/d6/d7/a0/a4,-(sp)	and save screen controls and action
	move.l	a5,d0			get number of parameters
	sub.l	a3,d0
	beq.s	wc2_null		null parameters
	subq.l	#8,d0
	beq.s	wc2_one 		one parameter
	subq.l	#8,d0
	beq.s	wc2_two 		two parameters
wc_bp
	moveq	#err.bp,d0
	bra.s	wc_fr.1
wc2_two
	addq.l	#8,a3			get destination first
	bsr.s	wc_ffnam		... as a full file name
	bne.s	wc_fr.1 		... oops
	moveq	#io.close,d0		and close the directory
	trap	#2
*
	subq.l	#8,a3			now get source 
wc_ffset
	bsr.s	wc_ffnam		... full file name
	bne.s	wc_fr.1 		... oops
	bra.s	wc_set
*
wc2_null
wc2_one
	bsr.l	ut_gtnm1		get the name on the RI stack
	bne.s	wc_fr.1 
	move.l	a1,bv_rip(a6)		and save the pointer
	move.l	a1,a3			for reuse
wc2_ddest
	moveq	#io.dir,d3		... a directory
	bsr.l	ut_opmds		as a destination
	beq.s	wc2_ddok		... good filename
	bpl.s	wc2_dev
	moveq	#err.bp,d4
	cmp.l	d0,d4			was it bad parameter?
	bne.s	wc_fr.1 		... no
	moveq	#-1,d4			... yes, must be device
	bra.s	wc2_dcopy
wc2_dev
	moveq	#-1,d4			destination is device, set single file
wc2_ddok
	move.l	a1,-(sp)
	moveq	#io.close,d0		and close file
	trap	#2
	move.l	(sp)+,a1
wc2_dcopy
	bsr.l	wc_ffcpy		copy name into right place
*
	move.l	a3,a1			open old file name
	moveq	#io.dir,d3		... a directory
	moveq	#uod.datd,d2
	jsr	ut_opdefx
wc_fr.1
	bne.l	wc_frame		... oops
	bsr.l	wc_ffcpy		copy name into right place
wc_set
	movem.l (sp),d0/d6/d7/a5	set screen controls
	move.l	a0,fr_dchan(sp) 	save source directory ID
	tst.l	d7			are statistics required?
	bge.s	wc_set1 		... no
	addq.w	#2,d6			two more lines
	move.l	a5,a4			set screen channel
	bsr.l	stat_do 		write out the statistics
	moveq	#1,d7			set to do all
wc_set1
	move.l	bv_rip(a6),a1		source name pointer
	lea	fnam_len(a1),a2 	destination name pointer
	move.l	bv_bfbas(a6),a3 	name pointer (includes string length)
	lea	$40(a3),a4		copy pointer (includes string length)
	movem.l a1-a4,fr_source(sp)	and save pointers 
*
	move.l	a1,a0			count number of sections in source
	bsr.s	wc_cntsc
	neg.b	d4
	move.l	a2,a0			count number of sections in destination
	bsr.s	wc_cntsc
	move.w	d4,fr_diff(sp)		and save the difference
*
* Loop through directory entries
*
wc_loop
	moveq	#io.fstrg,d0		fetch a string
	move.l	fr_dchan(sp),a0 	from the directory
	bsr.l	ut_fdire
	bne.s	wc_cldir		oops, close the directory
	move.b	fh_type(a6,a1.l),fr_ftype(sp) set file type
	lea	fh_name(a1),a1		set start of name
	move.w	(a6,a1.l),d1		is there a name?
	beq.s	wc_loop 		... no
	cmp.l	#fh.nmlen,d1
	bls.s	wc_fncheck
	moveq	#fh.nmlen,d1		... name too long
	move.w	d1,(a6,a1.l)
	move.b	#'*',2+fh.nmlen-1(a6,a1.l)
wc_fncheck
	bsr.s	wc_check		check if name matches
	bne.s	wc_loop
	move.l	a4,d0			running pointer to destination name
	movem.l fr_name(sp),a3/a4	reset the two name pointers
	sub.l	a4,d0			length of destination name
	subq.l	#2,d0			(less the word at the start)
	move.w	d0,(a6,a4.l)		set length
	bsr.s	wc_restu		... unblotch network
	bsr.s	wc_restu
	move.l	a5,a0			set channel ID
	moveq	#1,d0
	add.b	fr_ftype(sp),d0 	set EQ for directory
	move.l	fr_action(sp),a1	do action
	jsr	(a1)
	tst.w	d7			... complete?
	blt.s	wc_cldir		... ... yes
	tst.l	d0			... errors?
	bne.s	wc_cldir		... ... yes
	bra.s	wc_loop
wc_cldir
	move.l	fr_dchan(sp),a0 	directory channel ID
	bsr.l	ut_fceof		... close it
wc_frame
	add.w	#frame,sp		remove stack frame
	tst.l	d0
	rts
	page
*
* count sections
*
wc_cntsc
	move.l	(a6,a0.l),d1		get first four characters
*NET	    and.l   #$dff8ff00,d1	    uc letter, any digit, character
*NET	    cmp.l   #$4e305f00,d1	    N0_?
	sub.l	#$00010000,d1		make range 0...7 if nX_  ***1.3
	and.l	#$00f8ff00,d1		any char, any digit, us, character
	cmp.l	#$00305f00,d1		c0_?
	bne.s	wc_cntlp		... no, not network name
	st	2(a6,a0.l)		blotch out '_'
wc_cntlp
	move.b	(a6,a0.l),d1		get next character
	addq.l	#1,a0
	beq.s	wcr_rts 		... end
	cmp.b	#'_',d1 		is it '_'
	bne.s	wc_cntlp		... no
	addq.b	#1,d4			... yes, add one
	bra.s	wc_cntlp
*
* restore network _
*
wc_restu
	exg	a3,a4			do both names
	cmp.b	#-1,4(a6,a3.l)		is it $FF (one word at front)
	bne.s	wcr_rts 		... no, not blotched
	move.b	#'_',4(a6,a3.l) 	... yes, unblotch
wcr_rts
	rts
	page
*
* check if name matches
*
wc_check
	moveq	#'_',d2 		set end of section char
*
* first set up full name from source directory
*
	move.l	bv_bfbas(a6),a4 	copy 
	addq.l	#2,a4			(avoiding the length)
	move.l	fr_source+4(sp),a0	... source drive
	bsr.l	wc_add
	move.b	d2,(a6,a4.l)
wc_ck_sn
	addq.l	#1,a4
	move.b	2(a6,a1.l),(a6,a4.l)	copy characters of file name
	addq.l	#1,a1
	subq.w	#1,d1
	bgt.s	wc_ck_sn
*
	clr.b	1(a6,a4.l)		put zero at end
	move.l	bv_bfbas(a6),a3 	... start of name
	sub.l	a3,a4			... length
	subq.l	#1,a4			... adjusted
	move.w	a4,(a6,a3.l)
*
* now set all string pointers
*
	movem.l fr_source+4(sp),a1-a4
*
	addq.l	#2,a3			actual name and copy include length
	addq.l	#2,a4
	move.l	a3,a0			skip actual source drive name
	bsr.l	wc_skip
	move.l	a0,a3
	move.l	a1,a0			skip wild source drive name
	bsr.l	wc_skip
	move.l	a0,a1
	move.l	a2,a0			start of dest name with destination driv
	move.w	fr_diff+4(sp),d4	is it all of name?
	bge.s	wc_st_dest
	st	d4			... yes, ensure all of d4 -ve
	moveq	#0,d2			copy to end
wc_st_dest
	bsr.l	wc_add
	move.l	a0,a2
	moveq	#'_',d2 		reset end of section
*
	tst.b	d4			check length difference
	ble.s	wc_sect 		destination shorter or same
wc_st_lp
	bsr.l	wc_add_ 		dest longer, transfer section of dest
	move.l	a0,a2			update destination pointer
	subq.b	#1,d4
	bgt.s	wc_st_lp		and next if there is one
*
* examine section of source name
*
wc_sect
	cmp.b	(a6,a2.l),d2		is destination section missing?
	seq	d3
	addq.b	#1,d4			(one more section)
	move.b	(a6,a1.l),d0		get next source character
	beq.s	wc_nend 		... end of source name, it all matches 
	cmp.b	d2,d0			is source section missing?
	seq	d5
	bne.s	wc_nsect		... no
*
	addq.l	#1,a1			skip past missing section of source
	addq.b	#1,d4			(one more section)
	cmp.b	#1,d4
	ble.s	wc_nsect		not yet at start of destination
	tst.b	d3			missing destination section?
	bne.s	wc_sk_ms		... yes, skip it
	move.l	a2,a0			... no, copy it
	bsr.s	wc_add_
	move.l	a0,a2
	bra.s	wc_nsect
wc_sk_ms
	addq.l	#1,a2
*
* examine section of actual name in directory
*
wc_nsect
	tst.b	(a6,a3.l)		end of name?
	beq.s	wc_nf			... yes
	move.l	a3,-(sp)		save running pointers to source and name
	move.l	a1,-(sp)
	bsr.s	wc_csect		compare sections
	move.l	(sp)+,a1
	move.l	(sp)+,a0		(do not smash running pointer to name)
	bne.s	wc_no_match		... sections do not match
*
	tst.b	d4			still not got to start of destination?
	ble.s	wc_snext		... yes, do not add to copy name
	move.l	a1,a0			... assume first part of copy from soucr
	move.b	(a6,a2.l),d0		missing section in destination?
	beq.s	wc_cname		... yes
	cmp.b	d2,d0
	beq.s	wc_cname		... yes
*
	move.l	a2,a0			... no, get first part of name from dest
wc_cname
	bsr.s	wc_add_ 		copy first part of name
	move.l	a3,a0			... and rest of actual name
	bsr.s	wc_add
	move.l	a2,a0			skip to next bit of destination
	bsr.s	wc_skip
	move.l	a0,a2
wc_snext
	move.l	a1,a0			skip to next bit of source
	bsr.s	wc_skip
	move.l	a0,a1
	move.l	a3,a0			and to next bit of name
	bsr.s	wc_skip
	move.l	a0,a3
	bra.s	wc_sect 		now next section of source
*
wc_no_match
	tst.b	d5			no match, missing section?
	beq.s	wc_nf			... no, end of check
	tst.b	d3			missing section on destination?
	beq.s	wc_nskip		... no, skip rest of section in name
	bsr.s	wc_add_ 		... yes, copy whole of section of name
	bra.s	wc_nnext
wc_nskip
	bsr.s	wc_skip
wc_nnext
	move.l	a0,a3			update pointer to name
	bra.s	wc_nsect
*
wc_nf
	moveq	#err.nf,d0		set error not found
	bra.s	wc_rts1
wc_ok
	moveq	#0,d0
wc_rts1
	rts
*
* end of source, copy rest of name
*
wc_nend
	tst.w	d4			is there a destination?
	blt.s	wc_ok			... no, done
	tst.b	(a6,a3.l)		any more name?
	beq.s	wc_rts1 		... no
	move.l	a3,a0			copy from name
	moveq	#0,d2			... to end
wc_add_
	moveq	#'_',d0 		add '_'
wc_ad_loop
	move.b	d0,(a6,a4.l)		put byte in copy name
	addq.l	#1,a4
wc_add
	move.b	(a6,a0.l),d0		is next source end of string?
	beq.s	wc_ad_rts		... yes
	addq.l	#1,a0			... no, move on
	cmp.b	d2,d0			is it end of section?
	bne.s	wc_ad_loop		... no, add it to copy name
wc_ad_rts
	rts
*
wc_skip
	move.b	(a6,a0.l),d0		end of string?
	beq.s	wc_sk_rts		... yes
	addq.l	#1,a0			... no, move on
	cmp.b	d2,d0			end of section?
	bne.s	wc_skip
wc_sk_rts
	rts
	page
wc_csect
	move.l	a0,-(sp)
wstr_chk
	moveq	#0,d1
	lea	cv_lctab(pc),a0
	bsr.s	wstr_lc 		get next lc of wild
	move.b	d1,d0			... into d0
	beq.s	wstr_exit		... end of wild name
	cmp.b	d2,d0			end of section?
	beq.s	wstr_exit
	exg	a1,a3
	bsr.s	wstr_lc 		and next lc of string
	exg	a3,a1
	cmp.b	d1,d0			and compare
	beq.s	wstr_chk		... match, carry on
wstr_exit
	move.l	(sp)+,a0
	rts
*
wstr_lc
	move.b	(a6,a1.l),d1		characters
	addq.l	#1,a1
	move.b	(a0,d1.w),d1
	rts
	end
