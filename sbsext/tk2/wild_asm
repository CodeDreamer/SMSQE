* Wild card operations	V1.1    1985  Tony Tebby   QJUMP
*
	section exten
*
*	DIR [#channel,] [wildname]			drive status and directo
*	WDIR [#channel,] [wildname]			directory listing
*	WSTAT [#channel,] [wildname]			statistics of files
*	WDEL [#channel,] [wildname]			delete files
*	WCOPY [#channel,] [wildname [,wildname]]	copy files 
*	WREN [#channel,] [wildname [,wildname]] 	rename files
*
	xdef	dir
	xdef	wdir
	xdef	wstat
	xdef	wdel
	xdef	wcopy
	xdef	wren
*
	xref	wc_look1		look for one filename
	xref	wc_look2		look for two filenames
	xref	dec_cstrg		convert long integer to decimal
	xref	stat_do 		write out statistics
	xref	ut_chan0		open channel (default #0)
	xref	ut_winset		open channel and check window
	xref	ut_winchk		check window
	xref	ut_mswq 		message for wild, one name and query
	xref	ut_msw2q		message for wild, 2 names and query
	xref	ut_mswoq		message for wild, overwrite query
	xref	ut_fhead		read file header
	xref	ut_wrtst		write string of bytes
	xref	ut_wrtnl		write newline
	xref	ut_trp3r		trap #3 relative
	xref	ut_copy1		copy files (with header type>=1)
	xref	ut_fclos		close file
	xref	ut_errms		write error message (set d0=0)
*
	include dev8_sbsext_ext_keys
*
fr_dchan equ	$14			position of directory channel in frame
wild_rts
	rts
*
* Wild copy action
*
wi_copy
	beq.s	wild_rts		... ignore directory
	bsr.l	ut_msw2q		and ask if OK to do
	bne.s	wi_rts.1
	moveq	#io.share,d3		try to open source
	move.l	a3,a0
	bsr.l	wi_open
	bne.s	wi_mess 		... oops
	move.l	a0,a3			save source ID
	bsr.s	wi_openn		try to open new destination
	beq.s	wic_copy		... ok, carry on
	bsr.s	wi_overq		check if overwrite existing file
	bne.s	wi_cls			... no, close source
	moveq	#io.overw,d3		try to open destination overwrite
	bsr.s	wi_open4
	beq.s	wic_copy		... good
	moveq	#err.ex,d4
	cmp.l	d4,d0			was it already exists?
	bne.s	wi_derr 		... no, write message and close source
	bsr.s	wi_del4n		... yes, delete and try again
	bne.s	wi_derr 		... ... oops
wic_copy
	bra.l	ut_copy1		copy (header optional)
*
* Wild rename action
*
wi_ren
	bsr.l	ut_msw2q		request
wi_rts.1
	bne.s	wi_rts
	moveq	#io.old,d3		open read/write
	move.l	a3,a0
	bsr.s	wi_open
	bne.s	wi_mess 		... oops, send message and continue
	move.l	a0,a3			save channel ID
	bsr.s	wir_ren 		rename file
	beq.s	wi_cls			... done, close the source
	bsr.s	wi_overq		and request overwrite
	bne.s	wi_cls			... no, close the source
	bsr.s	wi_del4 		delete the destination
	move.l	a3,a0
	bsr.s	wir_ren 		try rename again
	bra.s	wi_derr 		and close up
*
wir_ren
	moveq	#fs.renam,d0		rename
	move.l	a4,a1
	bra.l	ut_trp3r
*
* Query overwrite
*
wi_overq
	moveq	#err.ex,d4		check if destination exists
	cmp.l	d4,d0
	bne.s	wi_derr4		... no, other destination error
	swap	d7			get overwrite continue
	move.l	a5,a0
	bsr.l	ut_mswoq		ask for overwrite
	sne	d4			save status
	swap	d7
	tst.b	d4			was it OK
	rts
	page
*
* destination error
*
wi_derr4
	addq.l	#4,sp			remove return
wi_derr
	beq.s	wi_cls			no error
	bsr.s	wi_mess 		... not good, write out error
*
* close source
*
wi_cls
	move.l	a3,a0
	bra.l	ut_fclos		and close source file
*
* write error message
*
wi_mess
	clr.w	d7			reset continue flag
wi_messc
	move.l	a5,a0			set screen channel
	bra.l	ut_errms		... and write error message
*
*	delete file and reopen
*
wi_del4
	move.l	a4,a0			destination file-name
wi_del0
	moveq	#io.delet,d0		to be deleted
	bra.s	wi_trap2
wi_del4n
	bsr.s	wi_del4 		delete destination
	bne.s	wi_rts			... oops
wi_openn
	moveq	#io.new,d3		open a new file
wi_open4
	move.l	a4,a0			a4 points to name
wi_open
	moveq	#io.open,d0		open a file
wi_trap2
	moveq	#myself,d1		for me
	trap	#4
	trap	#2
	tst.l	d0			check error return
wi_rts
	rts
	page
*
* DIR/WDIR action
*
wi_dir
	sne	d5			directory flag
	bsr.l	ut_winchk		check the window
	move.l	a3,a1			pointer to file name
	move.w	(a6,a1.l),d2		number of characters
	addq.l	#2,a1			start of name
wi_dnams
	addq.l	#1,a1			skip the drive name
	subq.w	#1,d2
	cmp.b	#'_',-1(a6,a1.l)	is previous '_'?
	bne.s	wi_dnams		... no, skip another
	tst.b	d5			directory?
	bne.s	wi_send 		... no, write the file name
	add.w	d2,a1			... yes, add ' ->'
	move.b	#' ',(a6,a1.l)
	move.b	#'-',1(a6,a1.l)
	move.b	#'>',2(a6,a1.l)
	sub.w	d2,a1
	addq.w	#3,d2
	bra.s	wi_send 		write the directory name
*
* WSTAT action
*
wi_stat
	bsr.s	wi_dir			write filename
	bne.s	wi_rts			... oops
	tst.b	d5			was it directory?
	beq.s	wi_rts			... yes
	bsr.l	ut_winchk		check the window
	moveq	#io.share,d3		open
	move.l	a3,a0			source file
	bsr.s	wi_open
	bne.s	wi_messc		... oops, message and continue
	move.l	a0,a3			save file channel ID
	bsr.l	ut_fhead		read header
	bne.s	wis_done		... oops
	move.l	md_deupd(a6,a1.l),-(sp) save the update date
	move.l	d7,-(sp)		the continue flag
	move.l	d6,-(sp)		and the window counter
	move.l	md_delen(a6,a1.l),d1	get file length
	moveq	#0,d4			positive no sign
	moveq	#0,d5			create a string of file length
	moveq	#10,d6			field of 10
	moveq	#$7f,d7 		no commas
	bsr.l	dec_cstrg
	moveq	#10,d2			set count
	move.l	(sp)+,d6
	move.l	(sp)+,d7
	move.l	(sp)+,d1		get the update date
	beq.s	wis_send		... not there
	add.w	#32,a1			move to end
	move.w	cn..date,a2
	jsr	(a2)			convert date
	move.w	#'  ',(a6,a1.l) 	put spaces instead of length
	move.l	bv_bfbas(a6),a1 	set string pointer
	moveq	#32,d2			and length
wis_send
	bsr.s	wi_send 		send it
wis_done
	bra.l	wi_cls
*
wi_send
	move.l	a5,a0			get screen channel
	bsr.l	ut_wrtst		write string
	bne.s	wis_done		... oops
	bra.l	ut_wrtnl		and newline
*
* WDEL action
*
wi_del
	move.l	a3,a1			pointer to filename
	bsr.l	ut_mswq 		query it
	bne.s	wi_rts.2
	move.l	a3,a0			get source name
	bsr.l	wi_del0 		and delete it
	bra.l	wi_messc		send message and continue
	page
*
* WCOPY entry
*
wcopy
	lea	wi_copy(pc),a4		set wild copy address
	bra.s	wcp_wren
*
* WREN entry
*
wren
	lea	wi_ren(pc),a4		set wild rename address
wcp_wren
	moveq	#0,d7			preset check flag
	bsr.l	ut_chan0		get channel ID
	bne.s	wi_rts.2		... oops
	bra.l	wc_look2		and look for two filenames
*
* DIR / WDIR entry
*
dir
	moveq	#-1,d7			set check flag to get status
	bra.s	wdir_1
wdir
	moveq	#1,d7			preset check flag to all
wdir_1
	lea	wi_dir(pc),a4		set directory action
	bra.s	wdir_2
*
* WSTAT entry
*
wstat
	moveq	#1,d7			preset check flag to all
	lea	wi_stat(pc),a4		set statistics action
wdir_2
	bsr.l	ut_winset		setup window checks
	bne.s	wi_rts.2
	bra.s	wild_l1
*
* WDEL entry
*
wdel
	moveq	#0,d7			preset check flag
	lea	wi_del(pc),a4		set delete action
	bsr.l	ut_chan0		and get channel
wild_l1
	bne.s	wi_rts.2
	bra.l	wc_look1		look for one filename
wi_rts.2
	rts
	end
