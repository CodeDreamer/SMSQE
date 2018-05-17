; Directory handling				1992 Jochen Merz

	include dev8_keys_err
	include dev8_keys_hdr
	include dev8_keys_qdos_ioa
	include dev8_keys_qdos_io
	include dev8_keys_qdos_sms
	include dev8_keys_sys
	include dev8_keys_iod
	include dev8_mac_xref

	section utility

	xdef	fu_dup		; directory up
	xdef	fu_chkun	; check for ending underscore
	xdef	fu_dirchk	; directory existence check
	xdef	fu_getdir	; get lowest directory
	xdef	fu_getdrv	; get driver (including net prefix)
	xdef	fu_devtype	; get device type

;+++
; Inquire device type.
;
;		Entry			Exit
;	d1.b				device type: 0 - unknown
;						     2 - directory device
;	a3	ptr to devicename	preserved
;
;	Error returns:	inam	invalid name
;---
fu_devtype
	movem.l d2-d3/a0-a3,-(sp)
	moveq	#err.inam,d0		; assume bad name
	cmp.w	#3,(a3)+		; length of devicename
	bls.s	devtype_err		; n1_ is minimum!
	moveq	#0,d1			; unknown device
	cmp.b	#'_',2(a3)		; net device?
	beq.s	devtype_net
	cmp.b	#'_',4(a3)		; directory device?
	bne.s	devtype_err		; no, doesn't seem to be one
	move.l	#$dfdfdf00,d2		; DDD mask
	and.l	(a3),d2
	moveq	#3,d3			; three characters long
	bra.s	devtype_all
devtype_net
	move.w	#$df00,d2		; N mask
	and.w	(a3),d2
	moveq	#1,d3

devtype_all
	or.b	#$0030,d2

	move	sr,d7
	trap	#0			; in Supervisor mode

	move.l	d2,-(sp)
	moveq	#sms.info,d0
	trap	#do.sms2		; get system info
	lea	sys_fsdl(a0),a2 	; device driver list
	move.l	(sp)+,d2

devtype_loop
	move.l	(a2),d0 		; off end of list yet?
	ble.s	devtype_eol
	move.l	d0,a2			; next device driver

	lea	iod_dnus-iod_iolk(a2),a0
	cmp.w	(a0)+,d3
	bne.s	devtype_loop
	cmp.b	#1,d3			; is it N0?
	bne.s	devtype_check3
	cmp.w	(a0),d2
	bne.s	devtype_loop
	bra.s	devtype_found

devtype_check3
	cmp.l	(a0),d2 		; same?
	bne.s	devtype_loop		; no
devtype_found
	moveq	#2,d1			; it is a directory device
devtype_eol
	move.w	d7,sr			; back to previous mode
	moveq	#0,d0
devtype_err
	movem.l (sp)+,d2-d3/a0-a3
	rts

;+++
; Check directory if it really physically exists.
;
;		Entry			Exit
;	a3	ptr to directory name	preserved
;
;	Error returns:	OK	if item exists
;			nimp	if no directory
;			imem or any file errors can be returned as well
;---
fu_dirchk
	movem.l d1-d3/a0-a1,-(sp)
	move.l	a3,a0
	moveq	#ioa.kshr,d3		; open for input only
	xjsr	gu_fopen		; open file
	beq.s	chk_opok
	cmp.l	#err.itnf,d0
	beq.s	err_nimp
	bra.s	dchk_err
chk_opok
	sub.l	#16,sp			; a bit room for file header
	move.l	sp,a1			; buffer is here
	moveq	#14,d2			; we need only the first few bytes
	moveq	#iof.rhdr,d0
	xjsr	gu_iow			; read header
	add.l	#16,sp			; and correct stack
	bne.s	chk_corr
	addq.b	#1,hdr_type-16(sp)	; is file a directory?
	beq.s	chk_corr
	moveq	#err.nimp,d0
chk_corr
	xjsr	gu_fclos		; close file
dchk_err
	movem.l (sp)+,d1-d3/a0-a1
	tst.l	d0
	rts
err_nimp
	moveq	#err.nimp,d0
	bra.s	dchk_err


;+++
; Get lowest directory from a directory- or filename.
; Do nothing in case it is not a directory device.
;
;		Entry			Exit
;	d0				-ve if at root directory
;	a3	ptr to name		name length updated
;---
fu_getdir
	movem.l d1-d3/a1,-(sp)
	bsr	fu_devtype		; get device type
	cmp.b	#2,d1
	bne.s	getd_ok 		; not a directory device!
getd_loop
	bsr	fu_dirchk		; check current directory
	beq.s	getd_ok
	cmp.l	#err.nimp,d0
	beq.s	getd_up
	moveq	#0,d0
	bra.s	getd_ok
getd_up
	moveq	#0,d3			; reset underscore flag
	move.w	(a3),d0 		; name length
	beq.s	getd_ok
getd_lchk
	cmp.b	#'_',2-1(a3,d0.w)	; is current character underscore?
	bne.s	getd_nousc
	addq.w	#1,d3			; another underscore found
getd_nousc
	subq.w	#1,d0
	bne.s	getd_lchk		; until whole string searched
	cmp.w	#1,d3
	blt.s	getd_ok
	bsr.s	fu_dup			; up one level
	beq.s	getd_loop
getd_ok
;	 move.l  a3,a1
;	 bsr.s	 fu_chkun
	movem.l (sp)+,d1-d3/a1
	tst.l	d0
	rts

;+++
; Check name for ending underscore. If missing, add it.
;
;		Entry			Exit
;	a1	ptr to name		preserved
;---
fu_chkun
	movem.l d0,-(sp)
	move.w	(a1),d0 		; get length
	beq.s	no_undsc		; must not be underscored!
	cmp.b	#'_',1(a1,d0.w) 	; does it end in underscore?
	beq.s	no_undsc		; yes ...
	addq.w	#1,(a1) 		; otherwise add it
	move.b	#'_',2(a1,d0.w)
no_undsc
	movem.l (sp)+,d0
	rts

;+++
; Get length of driver name (with optional one or more net driver in front)
; out of a directory- or filename.
;
;		Entry			Exit
;	d1.w				length of driver within name
;	a3	ptr to name		preserved
;
;	Error returns:	err.ipar	if no proper directory name
;---
fu_getdrv
	move.w	(a3),d0 		; length of directory entry
	moveq	#0,d1			; start within directory
chk_net
	cmp.b	#'_',4(a3,d1.w) 	; is it a net device?
	bne.s	no_net
	addq.w	#3,d1			; check next net level
	bra.s	chk_net
no_net
	cmp.b	#'_',6(a3,d1.w) 	; standard device?
	bne.s	getdrv_err		; astonishing, something we cannot treat
	addq.w	#5,d2
	cmp.w	d2,d0			; longer than directory name?
	bhi.s	getdrv_err
	moveq	#0,d0			; all is OK
	rts
getdrv_err
	moveq	#err.ipar,d0
	rts

;+++
; Get up one directory in a string. Care is taken for net device.
;
;		Entry			Exit
;	d0				0 on success, -ve if already in root dir
;	a3	ptr to directory name	preserved
;---
fu_dup
	movem.l d1-d3,-(sp)
	moveq	#err.itnf,d0		; presume error
	move.w	(a3),d1 		; get name length
	beq.s	dup_ret 		; must not be zero
	moveq	#8,d3			; assume net, so minimum 8 characters
	move.l	2(a3),d2		; check for net
	and.l	#$0000ff00,d2		; mask for 'Nx_x'
	cmp.l	#$00005f00,d2		; is it net?
	beq.s	dup_net 		; yes
	moveq	#5,d3			; no net, minimum 5 characters
dup_net
	cmp.w	d3,d1			; already minimum size?
	bls.s	dup_ret
	moveq	#0,d0			; so we can get up!
dup_loop
	subq.w	#1,d1			; get back a character
	cmp.b	#'_',1(a3,d1.w) 	; another underscore reached?
	beq.s	dup_found
	cmp.w	d3,d1			; minimum reached?
	bne.s	dup_loop
dup_ret
	movem.l (sp)+,d1-d3
	tst.l	d0
	rts
dup_found
	move.w	d1,(a3) 		; insert new length
	bra.s	dup_ret

	end
