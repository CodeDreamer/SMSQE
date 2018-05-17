; Add modules to distribution file for QDOS job
;
; This requires the main routine of the job to have
;
;	strict section ordering
;		base			the QDOS "special job" header
;		special 		empty for the special job code here
;		main			the main part of the program
;					(the boot_concat routine is in main)
;
;	the data declaration
;
; The first of the files must be a Stella bootstrap loader host module or a
; complete Stella format distribution file.
; The last file is the destination for the new distribution file (this routine
; does not operate 'in place').
; The rest of the files have their checksum calculated and are inserted before
; the host module trailer in the order in which they appear.
;
	section main

	xdef	boot_addmod

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_stella_bl'
	include 'dev8_mac_cksum'
	include 'dev8_mac_assert'

dt_buff    equ	$00
dt_flen    equ	$00			 ; file length in buffer
dt_ftyp    equ	$05			 ; file type
dt_datasp  equ	$06			 ; data space
dt.buflen  equ	$40			 ; data buffer length
dt_head    equ	$40
dt.headlen equ	$0e

dt_cksum   equ	$50			 ; long word checksum

dt_top	   equ	$60

;+++
; This routine loads and concatenates a number of files into memory.
; All files must be an even number of bytes long.
;
;	d2  r	length of total concatenated file (total files + 4|8*nfiles+4)
;	d5 c  p number of files to concatenate
;	d6 c  p lsw spare space required at start
;	a1  r	pointer to concatenated file (length of first file)
;	a2  r	pointer to first file header
;	a5 c  u pointer to table of file channel IDs, updated to end
;	a6 c  p pointer to data area (at least $60 bytes)
;
;	status return standard
;
;---
boot_addmod
ba.reg	reg	d1/d3/d4/d5/d7/a0/a4
	movem.l ba.reg,-(sp)
stk_nfil equ	$0c

; first read all the file headers to find the size of memory to allocate

	lea	(a5),a2 		 ; file list
	moveq	#0,d7			 ; accumulate length
	move.l	(a2),a0 		 ; get header of first file
	lea	dt_head(a6),a1
	moveq	#iof.rhdr,d0		 ; read header
	moveq	#dt.headlen,d2
	bsr.l	ba_io

ba_fhloop
	move.l	(a2)+,a0		 ; next file
	lea	dt_buff(a6),a1
	moveq	#iof.rhdr,d0		 ; read header
	moveq	#dt.headlen,d2
	bsr.l	ba_io

	add.l	dt_flen(a6),d7		 ; file length
ba_fhnext
	subq.w	#1,d5
	bgt.s	ba_fhloop		 ; ... and the next

; now we know how long the concatenated files are going to be we can allocate

	moveq	#0,d1			 ; no internal spare
	add.w	d6,d1			 ; plus spare required
	add.l	d7,d1			 ; plus length of code

	moveq	#myself,d2
	moveq	#sms.achp,d0
	trap	#do.sms2		 ; allocate it
	tst.l	d0
	bne.l	ba_exit

; now load all the files

	lea	(a0,d6.w),a4		 ; save base of files

	lea	(a4),a1 		 ; and load files
	move.l	stk_nfil(sp),d5

	move.l	(a5)+,a0		 ; get first file
	moveq	#iof.rhdr,d0		 ; read header
	moveq	#dt.headlen,d2
	bsr.s	ba_io
	move.l	(a1),d2 		 ; length of first file

	moveq	#iof.load,d0		 ; and load
	bsr.s	ba_io
	add.l	d2,a1			 ; end of host module trailer
	lea	(a4,d7.l),a2		 ; end of distibution file
	assert	sbl.trlen,$18
	move.l	-(a1),-(a2)
	ble.s	ba_copyt		 ; ... OK copy trailer
	move.l	d7,(a2) 		 ; file length required
ba_copyt
	move.l	-(a1),-(a2)
	move.l	-(a1),-(a2)
	move.l	-(a1),-(a2)
	move.l	-(a1),-(a2)
	move.l	-(a1),-(a2)

	bra.s	ba_fllend

ba_flloop
	move.l	(a5)+,a0		 ; get next file
	moveq	#iof.rhdr,d0		 ; read header
	moveq	#dt.headlen,d2
	bsr.s	ba_io
	move.l	(a1),d2 		 ; length of next file

	moveq	#iof.load,d0		 ; and load
	bsr.s	ba_io

	move.l	a1,a2			 ; base of header
	move.l	sbl_mbase(a2),d4	 ; length of header
	sub.l	d4,d2			 ; length of module
	add.l	d4,a1			 ; base of module
	move.l	d2,sbl_mlength(a2)
	beq.s	ba_fllend		 ; null modules
	bsr.s	ba_cksum		 ; calculate checksum
	move.l	d3,sbl_cksum(a2)	 ; and save it

ba_fllend
	subq.w	#1,d5
	bgt.s	ba_flloop		 ; ... and the next

	move.l	sbl_flength(a1),d0	 ; length table
	beq.s	ba_hchk 		 ; ... none

	lea	(a4,d0.l),a2		 ; base of length fixup table
ba_fixup
	move.l	(a2)+,d0		 ; next address
	beq.s	ba_hchk 		 ; ... none
	move.l	d7,d1			 ; length
	add.l	(a2)+,d1		 ; adjusted
	move.l	d1,-8(a2,d0.l)		 ; and patched
	bra.s	ba_fixup

ba_hchk
	move.l	sbl_mlength-sbl.trlen(a4,d7.l),d2 ; length to checksum
	move.l	a4,a1
	bsr.s	ba_cksum
	move.l	d3,sbl_cksum-sbl.trlen(a4,d7.l) ; and save it

	move.l	a4,a1			 ; base of file
	move.l	d7,d2			 ; length of file
	lea	dt_head(a6),a2		 ; header of first file

	moveq	#0,d0

ba_exit
	movem.l (sp)+,ba.reg
ba_rts
	rts

ba_io
	moveq	#-1,d3			 ; wait forever
	move.l	a1,-(sp)
	trap	#do.io			 ; do IO
	move.l	(sp)+,a1
	tst.l	d0
	beq.s	ba_rts
	addq.l	#4,sp			 ; on error, give up
	bra.s	ba_exit

ba_ipar
	moveq	#err.ipar,d0
	bra.s	ba_exit

ba_cksum
	lwcksum a1,d2,d3,d0,d1		 ; make checksum
	rts

	section special

; this code is called as a routine by ex to open the channels

ex_entry
	move.l	a0,a2		; put the utility address somewhere safer
	exg	a5,a3		; we will process the params from the back end
	moveq	#err.ipar,d0	; preset error to invalid parameter
	subq.l	#2,d7		; there must be at least 2 channels
	blt.s	ex_rts		; ... but there aren't
	moveq	#0,d0		; ... oh yes there are!
ex_loop
	subq.l	#8,a3		; move down one
	cmp.l	a5,a3		; any more parameters
	blt.s	ex_rts		; ... no
	jsr	(a2)		; get a string from the command line
	blt.s	ex_rts		; ... oops
	bgt.s	ex_put_id	; ... #n
	moveq	#ioa.kshr,d3	; the file is an input
	tst.w	d5		; last file?
	bne.s	ex_open 	; ... no
	moveq	#ioa.kovr,d3	; ... yes, open overwrite
ex_open
	jsr	2(a2)		; open the file
	bne.s	ex_rts		; ... oops
ex_put_id
	move.l	a0,-(a4)	; put the id on the stack
	addq.l	#1,d5		; one more
	bra.s	ex_loop 	; and look at next
ex_rts
	rts

	end
