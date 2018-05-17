;	Write sprite as cursor		@ W. Lenerz 2003-2004
;
; 2004-03-27  1.00  first correct version
; 2005-11-08  1.01  Gets screen data out of CDB for background windows (MK)
;
;	Registers:
;	entry				exit
;	A6	sysvars 		preserved
;	All regs kept
;	condition codes set on exit (NOT in D0, which is kept)


	section con
			   
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_keys_sysspr'
	include 'dev8_keys_qu'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_config_keys'

	xref	pt_drop
	xref	sp_drop
	xref	pt_chksp
	xref	gu_achpp

	xdef	curs_init
	xdef	cn_spcur
			
curregs reg	d0-d5/a1-a6


cs_err
	rts
cn_spcur
	cmp.l	#$6000a,sd_xinc(a0)	; normal mode 4 cursor of correct size?
	bne	cs_err			;  ... no, use normal cursor toggle
	tst.b	sd_curf(a0)		; what do we want to do?
	beq.s	cs_err			; nothing, actually
	blt	cn_mkinv		; make it invisible

cn_mkvis
; first : move from screen to save area
;	    screen= source save area = destination
; then	: write out sprite
; as a reminder:
; pv_mblk needs the regs as follows
;	d1 c  s size of section to move
;	d2 c  s old origin in source area
;	d3 c  s new origin in destination area
;	d4    s scratch
;	d5    s scratch
;	d6/d7	preserved
;	a0    p cdb
;	a1	preserved
;	a2 c	row increment of source area
;	a3 c	row increment of destination area
;	a4 c	base address of source area
;	a5 c	base address of destination area
;	a6/a7	preserved
;
	movem.l curregs,-(sp)
	move.l	sys_clnk(a6),a3 	; device driver defn block
	move.l	#$6000a,d1		; size of section (10 rows a 6 pixels)
	move.l	sd_xmin(a0),d2		; orig of wdw
	add.l	sd_xpos(a0),d2		; + orig of curs in wdw = orig of block
	clr.l	d3			; orig in save area = 0,0
	move.l	d3,a2
	move.w	sd_linel(a0),a2 	 ; screen row increment
	move.l	sd_scrb(a0),a4		; screen (source) base address
	lea	sd_keyq(a0),a5		; point to keyboard queue
	move.l	qu_endq(a5),d0		; point to end of queue, start of save area
	beq.s	no_q
	addq.l	#1,d0			; ... now
	bclr	#0,d0
	move.l	d0,a5
no_q
	lea	sd.croff(a5),a5 	; jump over any possible rubbish
	move.l	(a5)+,d0		; pointer sprite
	beq	cerr			; there is none, so leave, do nothing
	move.l	pt_vecs(a3),a1		; point to screen vectors
	move.l	#sd.sarwi,a3		; save area row increment
	move.l	d0,-(a7)		; keep cursor sprite
	jsr	pv_mblk(a1)		; copy block now
	move.l	sys_clnk(a6),a3 	; get ddfb back
	move.l	(a7)+,a1		; get cursor sprite
	jsr	pt_chksp		; get for this screen mode
	blt.s	cn_csret		; ooops!
	cmp.l	#$6000a,pto_size(a1)	; correct cursor sprite size?
	bne.s	cn_csret		; no!
	move.l	sd_xpos(a0),d1		; where to write it, depending ...
	add.l	pto_org(a1),d1		; ... on sprite origin!
	lea	sp_drop(pc),a5		; sprite drop routine
	jsr	pt_drop(pc)		; drop this sprite now
cn_csok
	moveq	#0,d0			; no error, ever, when writing
cn_csret
	movem.l (sp)+,curregs		;
cn_csout
	rts
************************************************
   
; make pointer invisible on screen:
; copy mem from save area to screen
; on entry

cn_mkinv
	movem.l curregs,-(a7)
	move.l	sys_clnk(a6),a3 	; screen device driver defn block
	move.l	#$6000a,d1		; size of section (10 rows a 6 pixels)
	clr.l	d2			; orig in source (save area)
	move.l	sd_xmin(a0),d3		; orig of wdw
	add.l	sd_xpos(a0),d3		; + orig of curs in wdw = orig of block
	move.l	#sd.sarwi,a2		; save area row increment
	lea	sd_keyq(a0),a4		; point to keyboard queue
	move.l	qu_endq(a4),d0		  ; point to end of queue, start of save area
	beq.s	no_q2
	addq.l	#1,d0			; ... now
	bclr	#0,d0
	move.l	d0,a4			; save area (source) address
no_q2
	lea	sd.croff(a4),a4
	move.l	(a4)+,d0		; any sprite?
	beq.s	cerr			; no, so use old routine
	move.l	sd_scrb(a0),a5		; screen destination base address
	move.l	pt_vecs(a3),a1		; point to screen vectors
	clr.l	d0
	move.w	sd_linel(a0),d0
	move.l	d0,a3			; screen (destination) row increment
	pea	cn_csok 		; we return to here
	jmp	pv_mblk(a1)		; copy block now

cerr	moveq	#-1,d0
	bra.s	cn_csret


*********************************************************************
*
* initialise the per job cursor table
* this table is 1 LW per job
* the first word is the job tag
* the second the flag (in lower byte)
*
*********************************************************************

cintreg reg	a0/a1/d7
curs_init
	movem.l cintreg,-(sp)
	move.l	sys_jbtt(a6),d0
	sub.l	sys_jbtb(a6),d0 	; size of job table
	beq.s	init_rts		; what?
	move.l	d0,d7			; keep
	jsr	gu_achpp		; get mem
	bne.s	init_rts		; oops!
	move.l	a0,pt_cjob(a3)		; this is the job table
	move.b	sms.conf+sms_curd,d0	; use sprite cursor or not
	lsr.l	#2,d7			; nbr of jobs
	bra.s	curs_do
curs_lp
	move.l	d0,(a0)+		; set tag to 0 & config info
curs_do
	dbf	d7,curs_lp		; fill in table now

	moveq	#0,d0
init_rts
	movem.l (sp)+,cintreg
	rts

	end
