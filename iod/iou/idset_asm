; IO Device Linkage Setup   V2.02    1996  Tony Tebby	 QJUMP

	section iou

	xdef	iou_idset

	xref	gu_achpp

	include 'dev8_keys_iod'

;+++
; IO device driver initialisation. Allocates the linkage block
; and then sets the standard pointers. If A3 points to a zero long word,
; then it is assumed that the linkage block is already allocated and pointed
; to by A0.
;
; This version handles SQIO compatible IO devices and directory devices
;
;
;      (a0 c  p pointer to iod.sqhd below the linkage block if (a3) is zero)
;	a3 cr	pointer to linkage block definition in form of
;		    long	    length of linkage block + $10 header
;		    long	    iod..sxx flags (see keys_iod)
;
;		    word rel ptrs   external interrupt *
;				    polling *
;				    scheduler *
;				    IO +
;				    open +
;				    close +
;			if iod..ssb   forced slave
;			if iod..scn   channel name
;			if iod..sfm   format
;			if iod..sdd   (word) physical definition block length
;				      drive name
;
; * If the relative pointer is zero, then no routine will be linked in.
; + If the relative pointer is zero, then the other IO pointers must be zero.
;
;	a3  r	pointer to linkage block
;
;	status return standard
;---
iou_idset
iis.reg reg	d1/d2/a0/a1/a4
	movem.l iis.reg,-(sp)
	move.l	(a3)+,d0		 ; length of linkage block
	beq.s	iis_set 		 ; ... preallocated
	jsr	gu_achpp		 ; allocate 
	bne.s	iis_exit
iis_set
	move.l	(a3)+,d2		 ; flags
	addq.l	#8,a0			 ; skip the first 2 long words
	move.l	d2,(a0)+		 ; flags
	move.l	#iod.sqio,(a0)+

	move.l	a0,a1			 ; base of definition

	bsr.s	iis_lksv		 ; link in the servers
	bsr.s	iis_lksv
	bsr.s	iis_lksv

	tst.b	d2			 ; any IO at all?
	beq.s	iis_done		 ; ... no

	bsr.s	iis_lksv		 ; set the IO
	bsr.s	iis_vec 		 ; ... with two extra entry points
	bsr.s	iis_vec

	swap	d2			 ; vector flags

	bsr.s	iis_cvec		 ; slave
	bsr.s	iis_cvec		 ; undefined
	bsr.s	iis_cvec		 ; channel name
	bsr.s	iis_cvec		 ; format
	lsr.w	#1,d2
	bcc.s	iis_done
	addq.w	#2,a0
	move.l	(a3)+,(a0)+		 ; definition block length + name len
	move.l	(a3)+,(a0)+
	subq.l	#6,a3
	move.w	(a3)+,(a0)+		 ; second name
	move.l	(a3)+,(a0)+

iis_done
	move.l	a1,a3
	moveq	#0,d0
iis_exit
	movem.l (sp)+,iis.reg
	rts

iis_lksv
	addq.l	#4,a0			 ; skip linaage
iis_vec
	move.l	a3,a4
	move.w	(a3)+,d0
	bne.s	iis_ssrv
	sub.l	a4,a4			 ; no server
iis_ssrv
	add.w	d0,a4			 ; absolute address
	move.l	a4,(a0)+		 ; set server address
iis_rts
	rts

iis_cvec
	lsr.w	#1,d2			 ; this vector exists?
	bcs.s	iis_vec
	clr.l	(a0)+
	rts
	end
