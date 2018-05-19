* Pointer initiaisation code  V1.07    1985   Tony Tebby   QJUMP
*
*	      1.05  modified for new modes (wl)
* 2006-03-21  1.06  modified for background I/O support (MK)
* 2006-05-15  1.07  disable new CTRL+C behaviour of common scheduler code (MK)
* 2018-04-28  1.08  refer to pt_sched_qdos, not pt_sched (MK)
*

	section driver
*
	xdef	pt_init
*
	xref	pt_open
	xref	pt_sched_qdos
	xref	pt_io
	xref	pt_ioxf
	xref	pt_close
	xref	pt_closu
	xref	pt_copyc
	xref	pt_linkc
	xref	pt_isspr
	xref	pt_bginit
	xref	pt_bgprocs
	xref	sp_table
	xref	pv_table
	xref.l	pt.spsln
*
	include 'dev8_mac_assert'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_chn'
	include 'dev8_keys_qlv'
	include 'dev8_keys_jcb'
	include 'dev8_keys_con'
	include 'dev8_mac_proc'

proc_tab
	proc_stt
	proc_def CKEYON
	proc_def CKEYOFF
	proc_end
	proc_stt
	proc_end

pt_con	dc.w	8,'CON__232'
pt.dq2	equ	232-sd.dq2

*
*	a3  r	base of linkage block
*
pt_init
reglist reg	d4/d5/d6/d7/a4/a5
	movem.l reglist,-(sp)
	lea	proc_tab,a1		; new procedures
	move.w	sb.inipr,a2
	jsr	(a2)
	lea	pt_bgprocs,a1
	move.w	sb.inipr,a2
	jsr	(a2)
	moveq	#0,d0
	trap	#1
	move.l	a0,a4			; a4 = sysvars
	moveq	#sms.achp,d0		; first allocate the memory required
	move.l	#pt_end+pt.spsln,d1	; ... for the linkage and the sprite
	moveq	#0,d2			; owned by 0
	trap	#1
	tst.l	d0
	bne.l	exit
*
	lea	pt_end(a0),a5		; set save area address
	move.l	a5,pt_spsav(a0)
	move.l	a0,a5			; save linkage block address
*
	move.l	#pt.ident,pt_ident(a5)
	move.l	a5,sys_clnk(a4) 	; save console linkage
*
	move.l	#$01000040,pt_pos(a5)	; position above centre of screen
	move.l	pt_pos(a5),pt_npos(a5)
	move.b	#pt.supky,pt_pstat(a5)	set pointer suppressed
*
	move.w	#pt.kaccl,pt_kaccl(a5)	set default accelerators
	move.w	#pt.xaccl,pt_xaccl(a5)
	move.w	#pt.yaccl,pt_yaccl(a5)
	move.b	#pt.wake,pt_wake(a5)
	move.b	#pt.relax,pt_relax(a5)

	move.b	#pt.stuf1,pt_stuf1(a5)	set default stuff characters
	move.b	#pt.stuf2,pt_stuf2(a5)
*
* now the mode change flag and dummy keyboard channel
*
	moveq	#sms.info,d0		get sysvar
	trap	#1
	move.l	sys_ckyq(a0),a3 	save keyboard queue
	clr.l	sys_ckyq(a0)		clear keyboard queue pointer
*
*	Unlink any modified console driver before opening our
*	channel.
*
	trap	#0			doing odd things, use supervisor mode
	move.l	a0,a4			keep system variables
	move.l	sys_iodl(a4),a2 	and old driver list
	move.l	a2,d0			smashable copy
	move.l	#$c000,d1		ROM ends here
loop
	beq.s	openfunny		no ROM drivers, wow!!!!!!
	move.l	d0,a0			point to driver linkage
	cmp.l	pt_aio-pt_liod(a0),d1	is the driver in ROM?
	bgt.s	relink			yes, make this first driver
	move.l	(a0),d0 		no, try next one
	bra.s	loop
relink
	move.l	a0,sys_iodl(a4) 	smash driver list
*
openfunny
	lea	pt_con(pc),a0		open our funny channel
	moveq	#ioa.open,d0
	moveq	#0,d3
	moveq	#0,d1
	trap	#2
*
	move.l	a2,sys_iodl(a4) 	restore driver list
	move	#0,sr			hack over, back to user mode now
	tst.l	d0			OK?
	bne.l	exit			bonk
*
	moveq	#iow.xtop,d0		do screen extop to set window size zero
	moveq	#-1,d3			... and find channel block, scr size
	lea	pt_extop(pc),a2
	trap	#3

	move.l	d1,a0
	lea	pt_sbase(a5),a2
	move.l	(a0)+,(a2)+		screen base
	move.l	(a0)+,(a2)+		size!!
	move.w	(a0)+,(a2)+		bytes per line
	move.l	(a0),(a2)		screen size
*
	move.l	a4,a0			restore sysvars (may be superfluous)
	move.l	sys_ckyq(a0),a2 	get dummy keyboard queue address
	move.l	a2,pt_dumq1(a5) 	set dummy keyboard queue address
	move.l	a2,d0			set keyboard queue offset
	sub.l	a1,d0
	move.l	d0,pt_kqoff(a5)

	move.l	a3,sys_ckyq(a0) 	restore keyboard queue pointer

	moveq	#sd.dq1l,d1		very short dummy queue 1
	move.w	ioq.setq,a3		reset it
	jsr	(a3)
	move.l	a2,(a2) 		point to myself!!! should be already!!
*
	move.l	a2,a4
	lea	sd.dq2(a2),a2		dummy keyboard queue 2
	moveq	#pt.dq2,d1		reasonable length
	move.w	ioq.setq,a3
	jsr	(a3)			set up queue
	move.l	a4,(a2) 		and link to dummy 1
*
	move.l	a1,pt_mtest(a5) 	save flag channel address
	st	sd_cattr(a1)		and set it
	move.l	a1,d7			save channel base address
	page
*
*	Take a copy of the ROM driver linkage
*
	move.l	chn_drvr(a1),a0 		get ROM driver address
	move.l	a0,d5				save it
	addq.l	#iod_ioad-iod_iolk,a0		point to I/O entry
	assert	iod_ioad,iod_open-4,iod_clos-8
	assert	pt_aiorm,pt_aoprm-4,pt_aclrm-8
	lea	pt_aiorm(a5),a1
	move.l	(a0)+,(a1)+			copy I/O address
	move.l	(a0)+,(a1)+			and open address
	move.l	(a0)+,(a1)+			and close address
	lea	pt_romdr(a5),a0 		point to... 
	move.l	a0,d4				...ROM equivalent linkage
*
*	Now link in our scheduler routine
*
	lea	pt_sched_qdos(pc),a1	 set scheduler loop driver address
	move.l	a1,pt_aschd(a5)
	lea	pt_lschd(a5),a0
	moveq	#sms.lshd,d0
	trap	#1
*
*	Normal IO and close entry points
*
	lea	pt_io(pc),a1		io
	move.l	a1,pt_aio(a5)		in proper linkage
	lea	pt_close(pc),a1 	close
	move.l	a1,pt_aclos(a5) 	in proper linkage
*
*	Special IO and close for previously unused channels
*
	lea	pt_ioxf(pc),a1		special IO
	move.l	a1,pt_aiodm(a5) 	in dummy linkage
	lea	pt_open(pc),a1		open (only one)
	move.l	a1,pt_aopdm(a5) 	in dummy linkage
	lea	pt_closu(pc),a1 	special close
	move.l	a1,pt_acldm(a5) 	in dummy linkage
	lea	pt_liodm(a5),a0
	moveq	#sms.liod,d0		and link in
	trap	#1
*
*	set null close routine
*
	lea	pt_ok,a0		null close
	move.l	a0,pt_ptrok(a5) 	OK
	page
*
*	Go through the channel table, changing all old consoles into new
*	ones, and changing the channel table entries accordingly.  Any job
*	waiting for IO needs its flag address changed, too.
*
	trap	#0			go to supervisor mode
	move.l	a6,-(sp)		set sysvar pointer
*
	moveq	#sms.info,d0		find the system variables
	trap	#1
	move.l	a0,a6
*
	move.l	sys_chtb(a6),a2 	go through all channels
	move.l	(a2),d1 		channel 0 base
	bmi	pti_exno		not open, bad news
	move.l	d1,a0			OK
*
	move.l	chn_drvr(a0),a3 	 get driver address
	move.l	a3,d6			 and keep it for later
	move.l	iod_open-iod_iolk(a3),pt_copen(a5) and keep old open routine
	lea	-pt_liod(a3),a3 	 ... base of linkage block
	move.l	a3,pt_clink(a5) 	 ... saved for link through
	move.l	a5,a3			 set linkage base
*
ch_loop
	move.l	(a2)+,d1		checking if this is the console driver
	blt.s	ch_eloop		... not open
	cmp.l	d1,d7			is it our own funny channel?
	beq.s	ch_eloop		... yes, don't move that
	move.l	d1,a0
	move.l	chn_drvr(a0),d1 	get the driver address
	cmp.l	d1,d6			channel 0 console?
	beq.s	ch_mkptr		yes, make it a Pointer Interface channel
	cmp.l	d1,d5			ROM console?
	bne.s	ch_eloop		no, ignore it
	move.l	d4,chn_drvr(a0) 	yes, ensure MODE doesn't see it
	bra.s	ch_eloop
*
ch_mkptr
	jsr	pt_copyc(pc)		copy channel to new area
	move.l	a0,-4(a2)		and change entry in channel table
	sub.w	#pt_liod-pt_liodm,a3	point to special linkage
	jsr	pt_linkc(pc)		and link in the channel
*
*	Now check to see if a job is waiting for IO.  We need to change 
*	its flag address if there is one.
*
	lea	chn_stat(a0),a4 	pointer to status byte
	tst.b	(a4)			job waiting?
	beq.s	ch_eloop		... no
*
	move.l	chn_jbwt(a0),d1 	get waiting job's ID
	moveq	#0,d2
	moveq	#sms.injb,d0		and information on it
	trap	#1
	move.l	a4,jcb_rflg-jcb_end(a0) point its hold flag to the new status
*
ch_eloop
	cmp.l	sys_chtt(a6),a2
	blt.s	ch_loop
*
exsv
	move.l	(sp)+,a6		restore a6
	and.w	#$d8ff,sr		back to user mode
exit
	movem.l (sp)+,reglist
	tst.l	d0			if OK a3 is set
	bne.s	exit2
	pea	pv_table
	move.l	(sp)+,pt_vecs(a3)	; install con vectors
	jsr	pt_isspr
	bne.s	exit
	jsr	pt_bginit		; init background I/O data

	move.w	#-1,pt_swwin(a3)	; disable new CTRL+C switch
	movem.l d1-d7/a0-a5,-(a7)
sys_sprt
	moveq	#sms.info,d0
	trap	#do.smsq
	move.l	sys_clnk(a0),d0
	beq.s	ss_exit
	move.l	d0,a3
	move.l	pt_vecs(a3),a2

	lea	sp_table,a4
	move.l	a4,a5
	move.w	(a4)+,d5		; that many sprites
	moveq	#0,d1
ss_loop
	move.l	(a4)+,a1		; get pointer to sprite
	add.l	a5,a1
	jsr	pv_sspr(a2)
	addq.w	#1,d1
	cmp.w	d5,d1
	blt.s	ss_loop
ss_exit
	movem.l (sp)+,d1-d7/a0-a5
	tst.l	d0
exit2
	rts
pti_exno
	moveq	#err.ichn,d0		channel was invalid
	bra.s	exsv			exit from supervisor mode, too!
*
*     $$$$$$$ Magic Numbers
*
pt_extop
	lea	bm_scren(a3),a3 	screen parameters
	moveq	#$fffffff7,d0
	and.b	sys_qlmr(a6),d0 	mode 0 or 8?
	bne.s	pt_exset		... no
	lea	pt_wsize,a3		... yes, standard sizes
pt_exset
	move.l	a3,d1			linkage address
	lea	$18(a0),a2		clear all to zero
	lea	$64(a0),a3		up to end
pt_exclr
	clr.l	(a2)+
	cmp.l	a3,a2
	blt.s	pt_exclr
*
	st	sd_curf(a0)		enable cursor
	move.l	a0,a1			return base address of channel block
pt_ok
	moveq	#0,d0			ok
	rts

pt_wsize dc.l	$20000,$8000
	 dc.w	$80,$200,$100		standard sizes
	end
