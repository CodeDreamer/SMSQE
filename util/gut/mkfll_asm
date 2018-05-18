; Make File List		   V1.02   1989  Tony Tebby  QJUMP
;					after Jonathan Oakley
; ????-??-??  1.01  Preserve A2 for MKLIS (Jochen Merz)
; 2017-11-24  1.02  Check 2nd char is 1..8 for Xx_ network check (MK)

	section gen_util

	xdef	gu_mkfll
	xdef	gu_mkdrl
	xdef	gu_mkflc

	xref	gu_fopen
	xref	gu_fclos
	xref	gu_mklis
	xref	gu_flchk
	xref	gu_drchk
	xref	gu_fmul
	xref	gu_iowp
	xref	gu_achp0
	xref	gu_rchp
	xref	cv_lctab

	include 'dev8_keys_hdr'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_fll'
	include 'dev8_mac_assert'
;---
; Generates a list of (sub-)directory files
;
;	Registers:
;		Entry				Exit
;	D0	0 no tree, -ve byte tree	error code
;	D1					number of files
;	A1					pointer to list
;	A2	pointer to directory name	preserved
;---
gu_mkdrl
	lea	gu_drchk,a1		 ; directory checking routine
	bra.s	gu_mkflc
;---
; Generates a list of files matching a given directory name.
;
;	Registers:
;		Entry				Exit
;	D0	0 no tree, -ve byte, tree	error code
;	D1					number of files
;	A1					pointer to list
;	A2	pointer to directory name	preserved
;---
gu_mkfll
	lea	gu_flchk,a1		 ; default checking routine
;---
; Generates a list of files using a given directory name and checking routine.
;
;	Registers:
;		Entry				Exit
;	D0	0 no tree, -ve byte tree	error code
;	D1					number of files
;	A1	pointer to match routine	pointer to list
;	A2	pointer to directory name	preserved
;
;	The checking routine is called with a1 pointing to the file directory
;	entry, and a2 pointing to the directory name. It should preserve
;	all registers and return status zero if the file is acceptable.
;
;---
gu_mkflc
gmf.reg reg	d2/d3/a0/a2/a3
	movem.l gmf.reg,-(sp)		 ; save regs
	move.b	d0,d2
	move.l	#gmf_end,d0		 ; allocate a bit for control block
	jsr	gu_achp0
	bne.l	gmf_exit
	move.l	a0,a3
	move.l	a1,gmf_chek(a3) 	 ; checking routine
	tst.b	d2
	spl	gmf_dtpt(a3)		 ; keep tree flag

; Now open the directory

	move.l	a2,a0			 ; directory name
	moveq	#ioa.kdir,d3		 ; open as a directory
	jsr	gu_fopen(pc)
	bne.l	gmf_nolst		 ; ...oops

	moveq	#0,d1
	moveq	#iof.posa,d0		 ; position at start !!
	jsr	gu_iowp
	bne.l	gmf_clerr

	moveq	#hdr.len,d2		 ; set flag if real directory
	lea	gmf_dnam(a3),a1
	moveq	#iof.rhdr,d0
	jsr	gu_iowp
	bne.s	gmf_nodir		 ; old floppy driver

	addq.b	#1,hdr_type(a1) 	 ; directory type is -1
	beq.s	gmf_vers2

	move.l	#$dfdfdff8,d0		 ; surely this must be Mdv?
	and.l	2(a2),d0
	cmp.l	#'MDV0',d0		 ; ... is it?
	seq	gmf_dtyp+1(a3)		 ; -1 or 0 (byte)
	bra.s	gmf_nodir

gmf_vers2
	st	gmf_dlst(a3)		 ; no list required
	addq.b	#1,gmf_dtyp+1(a3)	 ; ... type 2

gmf_nodir
	addq.b	#1,gmf_dtyp+1(a3)	 ; 0 MDV, 1 old, 2 Version 2
	move.l	a0,gmf_chan(a3) 	 ; keep directory channel ID

	lea	gmf_dvnm+2(a3),a1
	lea	2(a2),a0
	move.l	(a0)+,(a1)+		 ; copy start of name
	move.l	(a0)+,(a1)+
	move.l	(a0)+,(a1)+
	move.l	(a0)+,(a1)+

	lea	cv_lctab,a0
	moveq	#0,d1
	moveq	#0,d2
	moveq	#'_',d3 		 ; set end of section char

	move.l	a2,-(sp)		 ; preserve a2 for mklis	    JM
	move.w	(a2)+,d1
	move.w	d1,gmf_dvnl(a3) 	 ; length of device

	add.l	a2,d1			 ; end of pattern
	cmp.b	2(a2),d3		 ; xx_ for net?
	bne.s	gmf_stdev		 ; ... no, strip the device
*NET	    move.b  (a2),d2
*NET	    cmp.b   #'n',(a0,d2.w)	     ; N at start?
*NET	    bne.s   gmf_stdev
	move.b	1(a2),d2		 ; At least 2nd char is 1..8?	     MK
	cmp.b	#'1',d2 		 ;				     MK
	bcs.s	gmf_stdev		 ;				     MK
	cmp.b	#'8',d2 		 ;				     MK
	bhi.s	gmf_stdev		 ;				     MK
	addq.l	#3,a2			 ; ... yes, strip Xn_

gmf_stdev
	cmp.b	(a2)+,d3		 ; skip until past _
	bne.s	gmf_stdev

	sub.l	a2,d1			 ; length of the rest
	sub.w	d1,gmf_dvnl(a3) 	 ; device name length
	lea	gmf_dnam(a3),a1
	move.w	d1,(a1)+		 ; set rest of name
	bra.s	gmf_lclend

gmf_lcloop
	move.b	(a2)+,d2		 ; copy lower case
	move.b	(a0,d2.w),(a1)+
	subq.w	#1,d1
gmf_lclend
	bgt.s	gmf_lcloop
	move.l	(sp)+,a2		 ; get back a2 for mklis	     JM

	lea	gmf_rdir,a0		 ; point to entry generating routine
	move.w	#fll.elen,d0		 ; entry length
	jsr	gu_mklis		 ; and make a list

	move.l	gmf_chan(a3),a0 	 ; channel ID
gmf_close
	jsr	gu_fclos		 ; close the directory
	move.w	gmf_dtpt(a3),d3 	 ; stacked directories
	ble.s	gmf_rtct		 ; ... none
	subq.w	#4,gmf_dtpt(a3)
	move.l	gmf_dtls-4(a3,d3.w),a0	 ; pop one
	bra.s	gmf_close		 ; and close

gmf_rtct
	move.l	a3,a0
	jsr	gu_rchp 		 ; return control block

gmf_exit
	movem.l (sp)+,gmf.reg
	rts

gmf_clerr
	jsr	gu_fclos		 ; close after error
gmf_nolst
	moveq	#0,d0			 ; no list is OK
	move.l	d0,d1
	move.l	d0,a1
	bra.s	gmf_rtct

;+++
; This is called from the list processing routine, to read the header of
; the next file.
;
;	Registers:
;		Entry				Exit
;	D0					error
;	A1	list entry			preserved
;	A3	parameter frame 		preserved
;---
gmf_rdir
grd.reg  reg	d1/d2/d3/a0/a1/a2
	movem.l grd.reg,-(sp)

grd_retry
	move.w	gmf_dtyp(a3),fll_dtyp(a1) ; set type
	lea	fll_fnam-hdr_name(a1),a1 ; put the name in the right place

grd_loop
	moveq	#hdr.len,d1		 ; read header
	move.l	gmf_chan(a3),a0
	jsr	gu_fmul 		 ; fetch multiple
	beq.s	grd_any
	cmp.l	#err.eof,d0		 ; end of file?
	bne.l	grd_exit		 ; ... no
	move.w	gmf_dtpt(a3),d3 	 ; any stacked dirs?
	ble.l	grd_exit		 ; ... no
	jsr	gu_fclos		 ; close this one
	subq.w	#4,gmf_dtpt(a3)
	move.l	gmf_dtls-4(a3,d3.w),gmf_chan(a3) ; use the next
	bra.s	grd_loop

grd_any
	tst.w	hdr_name(a1)		 ; any file name?
	beq.s	grd_loop		 ; ... no, try again

	move.l	gmf_chek(a3),a0 	 ; check the file
	jsr	(a0)
	bne.s	grd_loop		 ; ... ignore it

	move.w	gmf_dtpt(a3),d3 	 ; tree required?
	blt.s	grd_setf		 ; ... no, file entry

	cmp.b	#-1,hdr_type(a1)	 ; directory?
	bne.s	grd_setf		 ; ... no, file entry

	move.l	gmf_chan(a3),gmf_dtls(a3,d3.w) ; stack old ID
	addq.w	#4,gmf_dtpt(a3)

	lea	hdr_name(a1),a0 	  ; file name
	lea	gmf_dvnl(a3),a1 	  ; device name length
	move.w	(a1)+,d0
	move.w	d0,(a1)
	move.w	(a0)+,d1		  ; length of device name
	add.w	d1,(a1)+
	add.w	d0,a1			  ; end of device name
grd_cpdr
	move.b	(a0)+,(a1)+		  ; add dir name to end of device
	subq.w	#1,d1
	bgt.s	grd_cpdr

	lea	gmf_dvnm(a3),a0 	  ; open directory
	moveq	#ioa.kdir,d3
	jsr	gu_fopen
	bne.s	grd_exit

	move.l	a0,gmf_chan(a3) 	  ; new directory channel
	movem.l (sp),grd.reg		  ; old register values
	bra	grd_retry

grd_setf
	lea	fll_flen-fll_fnam+hdr_name(a1),a0 ; where we want the header
	moveq	#-$40,d1
	add.l	(a1)+,d1
	move.l	d1,(a0)+		 ; corrected length
	move.w	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	move.l	(a1)+,(a0)+
	moveq	#hdr.nmln,d1
	cmp.w	(a1),d1 		 ; name too long ***TT
	bge.s	grd_snnmln		 ; ... no
	move.w	d1,(a1) 		 ; name truncated
grd_snnmln
	move.w	(a1),fll_fnln-fll_fnam(a1) ; save file name length
	lea	hdr_date-hdr_name(a1),a1 ; auxiliary info: date/version
	move.l	(a1)+,d1
	move.l	d1,(a0)+		 ; date
	move.w	(a1)+,(a0)+		 ; version
	addq.l	#2,a1
	lsr.l	#1,d1
	divu	#12*60*60,d1		 ; day
	move.w	d1,(a0)+
	move.l	(a1),(a0)		 ; backup date

grd_exit
	tst.l	d0
	movem.l (sp)+,grd.reg
	rts
	end
