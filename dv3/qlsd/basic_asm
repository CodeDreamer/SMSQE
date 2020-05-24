*** QL-SD Driver basic interface	   v1.02   2018 W. Lenerz + M. Kilgus

; 2020-04-05  1.02  Removed CARD_INIT (wl)
; 2018-07-18  1.01  Fixed WIN_CHECK for everything other than WIN1 (MK)

	section procs

	xdef	win_drive
	xdef	win_use
	xdef	win_check

	xref	inicrd
	xref	hd_fpart_internal
	xref	hd_rscard
	xref	get_str
	xref	norm_nm
	xref	dv3_close

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_iod'
	include 'dev8_keys_sbasic'
	include 'dev8_mac_assert'
	include 'dev8_dv3_qlsd_keys'

;+++
; Find drive definition block
;
;	d0	ddb or 0
;	d5    s
;	d7 c	drive number
;	a1    s
;	a2    r pointer to ddb in system variables + 4
;	a3 c	linkage block
;	a4    r ddb or 0
;---
get_ddb
	lea	sys_fsdd(a5),a2
	moveq	#$f,d5			; there is space for 16 drive defn blcks
	lea	ddl_ddlk(a3),a1 	; ddf_ddlk pointers have this offset
gddb_fdrv
	move.l	(a2)+,d0		; next drv defn blk
	beq.s	gddb_next
	move.l	d0,a4
	cmp.l	ddf_ddlk(a4),a1 	; our driver?
	bne.s	gddb_next		; ... no
	cmp.b	ddf_dnum(a4),d7 	; our drive?
	bne.s	gddb_next		; ... no
	bra.s	gddb_out		; and we're done
gddb_next
	dbf	d5,gddb_fdrv		; try all drive defn blks
	suba.l	a4,a4			; no block found
gddb_out
	move.l	a4,d0			; so the flags are set
	rts

;***************
;
; WIN_DRIVE drive,card,"name"
;
;***************
win_drive
	subq.l	#8,a5
	move.w	sb.gtint,a2
	jsr	(a2)
	subq.w	#2,d3
	bne.s	err_bp
	move.l	(a6,a1.l),d7		; drive nbr | card nbr (1...3)
	move.l	d7,d5
	swap	d5			; drive nbr
	subq.w	#1,d5
	blt.s	err_bp
	cmp.w	#7,d5
	bgt.s	err_bp			; must be 1...8
	move.l	a5,a3
	addq.l	#8,a5
	bsr	get_str 		; get string
	bne.s	wd_rts
	bsr.s	get_linkage
	bne.s	wd_rts
	lea	hdl_unit(a3),a0
	and.b	#3,d7			; force card number into 0..3
	beq.s	err_bp			; and 0 is not allowed either
	move.b	d7,(a0,d5.w)		; card nbr (1...3)
	swap	d7			; drive nbr for get_ddb
	lea	qlsd_names(a3),a0	; pointer to names
	lsl.w	#4,d5
	add.w	d5,a0
	move.w	sr,d0
	trap	#0
	move.w	d0,-(a7)
	add.l	a6,a1			; we can now do this as we're super
	exg	a0,a1
	bsr	norm_nm 		; normalise name & set it
	bne.s	wd_err			; ooops, wrong name

; find corresponding drive definition block
	bsr.s	get_ddb 		; try to find drive definition block
	beq.s	wd_out
	bsr	del_ddb 		; delete definition block

wd_out	moveq	#0,d0
wd_err	move.w	(a7)+,sr
	tst.l	d0
wd_rts	rts

err_bp	moveq	#err.ipar,d0
	rts

;+++
; Search driver linkage block
;
;	d0  r err_fdnf or 0
;	d1  s
;	a3  r driver linkage block
;	a5  r system variables
;---
get_linkage
	moveq	#0,d0
	trap	#1
	move.l	a0,a5			; system variables
	move.l	#'WIN0',d1
chk_dev lea	sys_fsdl(a5),a3 	; device list
srch_lp move.l	(a3),d0 		; is there another device?
	beq.s	err_nf			; no, leave with error ->
	move.l	d0,a3			; this is the device
	cmp.l	ddl_dname+2-ddl_ddlk(a3),d1 ; it is mine?
	bne.s	srch_lp 		; ... no
	sub.w	#ddl_ddlk,a3		; most code assumes no offset here
	bra.s	rts_ok

;***************
;
; WIN_USE device (three letter name)
;
;***************
win_use move.l	#'WIN0',d7		; preset
	cmp.l	a5,a3			; any string?
	beq.s	wu_set			; no, use WIN
	bsr	get_str 		; get string
	bne.s	err_nf
;	 cmp.w	 #3,(a6,a1.l)		 ; 3 letters?
;	 bne	 err_bp 		 ; uses 10 bytes !
	move.l	2(a6,a1.l),d7
	andi.l	#$dfdfdf00,d7
	move.b	#'0',d7
wu_set	bsr.s	get_linkage
	bne.s	do_rts
	move.l	d7,ddl_dnuse+2(a3)
rts_ok
	moveq	#0,d0
do_rts	rts

err_nf
	moveq	#err.fdnf,d0
	rts
err_mchk
	moveq	#err.mchk,d0
	rts

;***************
;
; WIN_CHECK drive
;
;***************
win_check
	move.w	sb.gtint,a2
	jsr	(a2)
	subq.w	#1,d3
	bne	err_bp			; need exactly one parameter
	move.w	(a6,a1.l),d7		; drive number

	bsr.s	get_linkage		; try to find driver linkage block
	bne.s	do_rts

	lea	hdl_unit-1(a3),a1
	moveq	#0,d5
	move.b	(a1,d7.w),d5		; card nbr (1...3)
	bsr	inicrd			; just to be sure it's initialised
	bne.s	do_rts
	bsr	hd_fpart_internal	; find partition file
	bne.s	do_rts

; d1 = cluster number, d4 = start of fat32
	move.l	d1,d3
	move.l	d1,d5
	lsr.l	#7,d3			; 128 clusters per fat sector
	add.l	d4,d3			; now absolute fat sector number
	andi.l	#$7f,d5 		; cluster within fat sector
	lsl.w	#2,d5			; now byte within fat sector

	move.l	d3,d0			; sector number
	moveq	#1,d2			; one sector
	lea	hdl_buff(a3),a1 	; sector buffer
	jsr	hd_rscard		; read it
	bne.s	do_rts

	move.l	#$ffffff07,d4		; ffffff07 anything larger(unsigned) = eof marker

; Read every entry in the fat and check that it is the previous one +1, until
; we hit the end of file marker
;
; d3 = current sector of fat
; d4 = EOF marker
; d5 = byte offset within fat sector
; a1 = pointer to fat sector data
; d1 = file cluster nbr; next entry in fat should be this + 1, if not the file
;      is fragmented
wchk_loop
	move.l	(a1,d5.w),d0		; next fat entry
	beq.s	rts_ok			; empty, so EOF, all ok
	cmp.l	d4,d0			; EOF marker?
	bhi.s	rts_ok			; yes, done
	rol.w	#8,d0			; make nbr in motorola format
	swap	d0
	rol.w	#8,d0
	addq.l	#1,d1			; next entry if not fragmented
	cmp.l	d0,d1			; is it?
	bne.s	err_mchk		; no, so file is fragmented
	addq.w	#4,d5			; next entry
	cmp.w	#512,d5 		; end of this fat sector reached?
	blt.s	wchk_loop		; no
	addq.l	#1,d3			; yes so read next fat sector
	move.l	d3,d0			; this is the sector to read
	jsr	hd_rscard
	bne.s	do_rts
	moveq	#0,d5			; now start from first cluster in sector
	bra.s	wchk_loop

;+++
; Delete all channels for a specific drive and free drive definition block
; Must be called in supervisor mode!
;
;	d0  s
;	d1  s
;	d2  s
;	a2 c  pointer to ddb entry in sysvar + 4
;	a4 c  drive definition block
;	a5 c  system variables
;---
del_ddb
reg.chkchn reg	d3/a1-a3/a5/a6
reg.a2	   equ	8
chkchn	movem.l reg.chkchn,-(sp)
	move.l	sys_chtb(a5),a1 	; point to first channel
	move.l	sys_chtt(a5),a6
ch_lp1	move.l	(a1)+,d0
	bmi.s	ch_next 		; channel is closed
	move.l	d0,a0
	assert	d3c_flid,d3c_drid-4,d3c_drnr-6,d3c_ddef-8
	cmp.l	d3c_ddef(a0),a4 	; is this a channel to this defn blk ?
	bne.s	ch_next 		; no
	move.b	#d3c.asect,d3c_atype(a0); yes, pretend this is a direct sector access channel
	bsr	dv3_close		; close this channel now
ch_next cmp.l	a6,a1			; continue, unless we're done with all channels
	blt.s	ch_lp1
; delete and release drive defn block
	move.l	reg.a2(sp),a2
	clr.l	-4(a2)			; clear pointer = delete block in tble
	
	move.l	a4,a0
	move.l	a5,a6			; mem.rchp need sysvars in A6
	move.w	mem.rchp,a2		; release this block
	jsr	(a2)
	movem.l (sp)+,reg.chkchn
	rts

	end
