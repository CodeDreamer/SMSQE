; Make a channel list			V0.04   Apr 1988  J.R.Oakley  QJUMP
;						Tony Tebby QJUMP
;
; 2003-06-23  0.03  Re-established compatibility with QDOS (MK)
; 2020-05-04  0.04  Distinguish between CON and SCR (MK)

	section gen_util

	xdef	gu_mkchl
	xdef	gu_mkcjl

	xref	cv_d1asc
	xref	gu_mklis
	xref	met_udev

	include 'dev8_keys_chn'
	include 'dev8_keys_chp'
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_wstatus'
	include 'dev8_keys_chl'

;+++
; Hack through the channel table making a list of devices in use.  This
; is truly nasty and shouldn't be used!  It may be called from user mode,
; as it goes into Supervisor mode for the nasty bits (i.e. most of it).
;
; If, however, all devices are SMSQ compatible, it is a bit cleaner.
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D1					number of entries
;	A1					pointer to list
;---
gu_mkchl
	moveq	#-1,d1			 ; any job
;+++
; Hack through the channel table making a list of devices owned by a Job. This
; is truly nasty and shouldn't be used!  It may be called from user mode,
; as it goes into Supervisor mode for the nasty bits (i.e. most of it).
;
; If, however, all devices are SMSQ compatible, it is a bit cleaner.
;
;	Registers:
;		Entry				Exit
;	D0					error code
;	D1	Owner Job ID			number of entries
;	A1					pointer to list
;---
gu_mkcjl
mkcreg	reg	d2/d7/a0/a2/a3
	movem.l mkcreg,-(sp)
	move.l	d1,a2			 ; owner Job ID
	move	sr,d7
	trap	#0			 ; in Supervisor mode
	moveq	#sms.info,d0
	trap	#do.sms2		 ; get system info
	move.l	a2,d2			 ; owner Job ID
	move.l	a0,a3			 ; pass sysvar base...
	move.l	sys_chtb(a3),a2 	 ; and channel table to list routine
	moveq	#-1,d1			 ; start with channel 0
	lea	mkc_mknt(pc),a0 	 ; how to fill in an entry
	moveq	#chl.elen,d0		 ; they're this long
	jsr	gu_mklis(pc)		 ; make the list
	move.w	d7,sr			 ; back to previous mode
	tst.l	d0
	movem.l (sp)+,mkcreg
	rts
	page
;+++
; Come here to fill in next channel entry
;
;	Registers:
;		Entry				Exit
;	D0					0 or ERR.EOF
;	D1	channel number			updated
;	D2	Owner JOB ID			preserved
;	A1	entry to set			preserved
;	A2	next channel table entry	updated
;	A3	system variables		preserved
;---
mkc_retry
mkchreg reg	d2/d3/d7/a0/a1/a3-a5
	movem.l (sp)+,mkchreg
mkc_mknt
	movem.l mkchreg,-(sp)
	move.l	sys_chtt(a3),a5 	; point to top of channel table
mkc_nxcl
	cmp.l	a2,a5			; off end of table yet?
	ble	mkc_exef		; yes, nothing else to fill in
	addq.w	#1,d1			; next channel number
	move.l	(a2)+,d0		; get channel base
	bmi.s	mkc_nxcl		; not open

	move.l	d0,a4			 ; a more useful pointer register!
	move.l	chp_ownr(a4),d0 	 ; owner
	tst.w	d2			 ; for any job?
	bmi.s	mkc_fill		 ; ... yes
	cmp.l	d0,d2			 ; ... our job?
	bne.s	mkc_nxcl


; Fill in the channel ID

mkc_fill
	move.l	d0,chl_jbid(a1) 	 ; fill in job ID
	move.l	chn_tag(a4),d0		 ; get the tag...
	move.w	d1,d0			 ; ...and the channel number
	move.l	d0,chl_chid(a1) 	 ; fill in channel ID

	move.l	a1,-(sp)
	move.l	d0,a0			 ; channel
	addq.l	#chl_name,a1		 ; pointer to name
	moveq	#chl.name,d2
	moveq	#ioa.cnam,d0
	trap	#2			 ; BEWARE: might smash A1 on QDOS machines
	move.l	(sp)+,a1		 ; restore pointer to entry
	tst.l	d0
	beq.l	mck_ok

	lea	chl_name+2(a1),a0	 ; set running pointer to device name

; Now check to see if this channel is open to a directory device

	lea	sys_fsdl(a3),a5 	 ; point to directory driver list
	move.l	chn_drvr(a4),d0 	 ; and this channel's driver
mkc_ddch
	move.l	(a5),d3 		 ; next directory driver
	beq.s	mkc_serd		 ; isn't one, it's a serial I/O channel
	move.l	d3,a5			 ; point to next
	cmp.l	d0,a5			 ; is it the same driver?
	bne.s	mkc_ddch		 ; no, check next

; Come here if the channel is open to a directory device.

	lea	iod_dnus-iod_iolk(a5),a5 ; point to drive name
	bsr	cpstr			; copy the string

	moveq	#sys_fsdd>>2,d0 	; get base of physical defns...
	move.b	chn_drid(a4),d3 	; ...plus this channel's drive ID
	add.b	d3,d0
	lsl.w	#2,d0			; thus physical defn table entry
	move.l	0(a3,d0.w),a3		; and thus to physical defn block
	move.b	iod_dnum(a3),d0 	; and (phew!) drive number
	add.b	#'0',d0 		; make it ASCII
	move.b	d0,(a0)+		; fill it in
	move.b	#'_',(a0)+		; and underscore at end

mkc_cdev
	lea	chn_name(a4),a5 	; copy the filename itself
	tst.w	(a5)			; sensible length?
	bra	mkc_cpy 		; fill in and set length

; Come here to find a serial I/O name.	Most serial drivers have a
; string of less that 8 characters within 16 words of their start,
; for use with the IO.NAME utility.  We search for that, unless there
; is no open routine, in which case the channel is a CON or SCR
; of the Pointer Interface.

mkc_serd
	move.l	d0,a5			; point to driver (linkage) ...
	move.l	iod_open-iod_iolk(a5),d0 ; ...and the open routine
	beq.s	mkc_scon		; there isn't one, it's a CON
	move.l	d0,a5

	moveq	#15,d0			; search 16 words
mkc_smax
	moveq	#8,d3			; for one less than $0008
mkc_sopl
	cmp.w	(a5)+,d3
	dbhi	d0,mkc_sopl		; search until found
	bls.s	mkc_nf			; no success
	move.l	a5,a3			; point to characters
	move.w	-(a5),d3		; success, get count of characters
	bra.s	mkc_ccae		; and check them
mkc_cclp
	cmp.b	#'A',(a3)		; must be in 'A'...
	bcs.s	mkc_ccfl		; (...oops)
	cmp.b	#'Z',(a3)+		; ... to 'Z'
	bhi.s	mkc_ccfl		; (...oops)
mkc_ccae
	dbra	d3,mkc_cclp
	bra.s	mkc_sern		; success, we found it

mkc_ccfl
	addq.l	#2,a5			; point to next word
	bra.s	mkc_smax		; and look again

mkc_nf
	lea	met_udev,a5		 ; point to default string
	bra	mkc_cpy 		 ; and copy to buffer

; It's a pointer CON, so set it up

mkc_scon
	lea	condev(pc),a5		; point to appropriate string

; A serial I/O device (i.e. non-filing) can have additional information,
; if this code recognises it.  We recognise SER, PRT, and CON (ptr or ROM).

mkc_sern
	move.l	2(a5),d0		; get first three characters
	cmp.l	#$53455200,d0		; SERial port?
	bne.s	mkc_cprt		; no, see if it's PRT
	cmp.l	#$C000,a5		; is the open routine in ROM?
	bcs	mkc_serp		; yes, hack some parameters onto the end

mkc_cprt
	cmp.l	#'PRT%',d0		; is it a PRT?
	bne.s	mkc_ccon		; no, might be a CON
	bsr	cpstr			; copy the PRT
	move.b	#' ',(a0)+		; and a space
	bra	mkc_cdev		; and copy the device name

mkc_ccon
	cmp.l	#'CON_',d0		; is it a CONsole device?
	beq.s	mkc_csiz		; yes, add size
	bra	mkc_cpy 		; no, just copy name

; Add size onto CON_ channels

mkc_csiz
	move.l	d1,d7			 ; keep channel number
	tst.l	sd_keyq+sd.extnl(a4)	 ; any keyboard queue?
	bne.s	mkc_iscon
	lea	scrdev(pc),a5		 ; nope, must be SCR then
mkc_iscon
	bsr	cpstr			 ; copy it
	move.b	#'_',(a0)+		 ; add underscore
	moveq	#0,d1
	move.w	sd_borwd+sd.extnl(a4),d3 ; get border
	add.w	d3,d3

	move.w	sd_xsize+sd.extnl(a4),d1 ; size...
	add.w	d3,d1
	add.w	d3,d1
	jsr	cv_d1asc
	move.b	#'x',(a0)+

	move.w	sd_ysize+sd.extnl(a4),d1
	add.w	d3,d1
	jsr	cv_d1asc
	move.b	#'a',(a0)+

	move.w	sd_xmin+sd.extnl(a4),d1  ; ...and origin information
	sub.w	d3,d1
	jsr	cv_d1asc
	move.b	#'x',(a0)+

	move.w	sd_ymin+sd.extnl(a4),d1
	asr.w	#1,d3
	sub.w	d3,d1
	jsr	cv_d1asc

	move.l	d7,d1			 ; restore channel number
	bra.s	mkc_mkln		 ; set the length

; Hack the parameters onto the end of a SER name: the format expected
; in the cdb is:

se_portn equ $18   port number 1..2
se_parit equ $1A   parity      0=none, 1=odd, 2=even, 3=mark, 4=space
se_hands equ $1C   handshake   -1=default, 0=ignore, 1=handshake
se_eofch equ $1E   EOL/EOF     -2=default, -1=raw, 0=EOL=CR/EOF=^Z, 1=EOL=CR

serpar	dc.b	' oems'
serhnd	dc.b	'ih'
sereof	dc.b	'rzc'
	ds.w	0

mkc_serp
	bsr.s	cpstr			; put in the 'SER'
	move.w	se_portn(a4),d0
	add.b	#'0',d0
	move.b	d0,(a0)+		; add port number

	move.w	se_parit(a4),d0
	beq.s	mkc_srnp		; no parity specified
	move.b	serpar(pc,d0.w),(a0)+

mkc_srnp
	move.w	se_hands(a4),d0
	bmi.s	mkc_srnh		; no handshake specified
	move.b	serhnd(pc,d0.w),(a0)+

mkc_srnh
	move.w	se_eofch(a4),d0
	addq.w	#1,d0			; EOF starts at -2 for default
	bmi.s	mkc_mkln		; no EOF specified
	move.b	sereof(pc,d0.w),(a0)+
	bra.s	mkc_mkln		; now set length

mkc_cpy
	bsr.s	cpstr
mkc_mkln
	lea	chl_name+2(a1),a3	; start of name
	sub.l	a3,a0			; length of it
	move.w	a0,-(a3)		; fill it in
mck_ok
	moveq	#0,d0
mkc_exit
	movem.l (sp)+,mkchreg		; restore registers
	rts
mkc_exef
	moveq	#err.eof,d0
	bra.s	mkc_exit
;
; copy characters     0 -> '_'	   1 -> '.'
;
;	Registers:
;		Entry				Exit
;	D0					smashed
;	A5	source string			smashed
;	A0	destination pointer		updated
;
cpstr
	move.w	(a5)+,d0
	ble.s	cps_rts
cps_lp
	move.b	(a5)+,d3
	cmp.b	#1,d3				special
	bhi.s	cps_cpy
	blo.s	cps_us
	moveq	#'.',d3
	bra.s	cps_cpy
cps_us
	moveq	#'_',d3
cps_cpy
	move.b	d3,(a0)+
	subq.w	#1,d0
	bgt.s	cps_lp
cps_rts
	rts

condev	dc.w	3,'CON_'
scrdev	dc.w	3,'SCR_'

	end
