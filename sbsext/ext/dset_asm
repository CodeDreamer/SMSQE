* Directory defaults  V1.0    1985  Tony Tebby   QJUMP
*
*	DATA_USE name			set default directory for data files
*	PROG_USE name			   ..	 ..    ..    ..   programs
*	DEST_USE name			   ..	 ..    ..    ..   destinations
*	SPL_USE name			   ..	 ..    ..    ..   spooler dest
*
*	DDOWN name			move down a directory level
*	DNEXT name			move sideways
*	DUP				move up
*
	section exten 
*
	xdef	data_use
	xdef	prog_use
	xdef	dest_use
	xdef	spl_use
*
	xdef	ddown
	xdef	dnext
	xdef	dup
*
	xref	ut_fdef
	xref	ut_gxnm1
	xref	ut_cnama
	xref	err_bp
*
	include dev8_sbsext_ext_keys
*
spl_use
	moveq	#sv_destd-sv_fdefo,d2
	sf	d7			do no add _ to end
	bra.s	any_use
dest_use
	moveq	#sv_destd-sv_fdefo,d2
	bra.s	pd_use
prog_use
	moveq	#sv_progd-sv_fdefo,d2
	bra.s	pd_use
data_use
	moveq	#sv_datad-sv_fdefo,d2
pd_use
	st	d7			add _ to end if not there
any_use
	bsr.s	def_fdef		get file default pointer
	bsr.l	ut_gxnm1		get the name
	bne.s	def_rts1
	clr.w	(a4)			set old length to zero
	bra.s	def_down
*
ddown
	moveq	#def_down-def_com,d7	set action offset (also sets '_' flag)
	bra.s	dname
dnext
	moveq	#def_next-def_com,d7	set action offset (also sets '_' flag)
dname
	bsr.l	ut_gxnm1		get the name (a1)
	bne.s	def_rts1
	bra.s	d_common
dup
	moveq	#def_up-def_com,d7	set action offset
d_common
	moveq	#sv_datad-sv_fdefo,d2	fetch data default address
	bsr.s	def_fdef
	move.l	a4,-(sp)		(save it)
	moveq	#sv_progd-sv_fdefo,d2	and program default address
	bsr.s	def_fdef		
	move.l	(sp)+,a0
	exg	a4,a1
	bsr.l	ut_cnama		and compare the names
	exg	a1,a4
	bne.s	ddata			... not the same
	bsr.s	def_com 		... the same, operate on program
ddata
	moveq	#sv_datad-sv_fdefo,d2	now do data default
	bsr.s	def_fdef
	bsr.s	def_com 		and operate on data
def_rts1
	bne.s	def_rts
*
	moveq	#sv_destd-sv_fdefo,d2	now try destination
	bsr.s	def_fdef
	move.w	(a4),d1
	cmp.b	#'_',1(a4,d1.w) 	does it end in _?
	bne.s	def_ok
*
def_com
	jmp	def_com(pc,d7.w)	jump to particular operation
*
def_fdef
	bra.l	ut_fdef 		find pointer to file defaults in a4
*
def_next
	bsr.s	def_up			first go up, then down again
def_down
	move.w	(a6,a1.l),d3		get the length of the string
	move.w	(a4)+,d1		and length of old default
	add.w	d3,d1
	cmp.w	#$20,d1 		too long?
	bhi.l	err_bp			... yes
	move.l	a4,a2			save default address
	add.w	-(a2),a4		a2 points to length, a4 to end
	move.w	d1,(a2) 		reset length
	move.l	a1,a0			pointer to string on stack
	bra.s	def_elp
def_loop
	move.b	2(a6,a0.l),(a4)+	move a byte at a time
	addq.l	#1,a0
def_elp
	dbra	d3,def_loop
*
	tst.b	d7			'_' required at end?
	beq.s	def_rts 		... no
	moveq	#'_',d2 		... yes, test for it
	cmp.b	-1(a4),d2
	beq.s	def_ok			... it's there already
	move.b	d2,(a4) 		... no, put it in
	addq.w	#1,(a2) 		and add one to length
	bra.s	def_ok
*
* remove last section from directory name
*
def_up
	move.w	(a4),d1 		get the length
def_uloop
	subq.b	#1,d1			shorter
	ble.s	def_ok			... very short
	cmp.b	#'_',1(a4,d1.w) 	'_' yet?
	bne.s	def_uloop		... no
	move.w	d1,(a4) 		... yes, set new length
def_ok
	moveq	#0,d0
def_rts
	rts
	end
