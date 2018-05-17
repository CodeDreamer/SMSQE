; SBasic support routines for Home Thing    (c) M. Kilgus 2005
;
;		0.00	prerelease (wl)
; 2005-11-09	0.01	rewritten to make job-ID optional (MK)
;			added HOME_VER$


	section exten

	xdef	home_set
	xdef	home_cset
	xdef	home_dir$
	xdef	home_file$
	xdef	home_curr$
	xdef	home_ver$
	xdef	home_def

	xref	gu_shome
	xref	gu_shomecurr
	xref	gu_shomedef
	xref	gu_ghome
	xref	gu_ghomefile
	xref	gu_ghomecurr
	xref	gu_homever
	xref	ut_gxst1
	xref	ut_gtstr
	xref	ut_gtli1
	xref	ut_chkri
	xref	ut_retst

	include 'dev8_keys_err'
	include 'dev8_keys_sbasic'

;+++
; HOME_SET (job-id,) device_and_filename$
;---
home_set
	lea	gu_shome,a4
	bra.s	hd_setfunc

;+++
; HOME_CSET (job-id,) device_and_filename$
;---
home_cset
	lea	gu_shomecurr,a4
hd_setfunc
	moveq	#8,d0		; one fixed parameter
	bsr	get_jobid
	bne.s	hd_exit

	move.l	d1,d4
	jsr	ut_gxst1	; get single string
	bne.s	hd_exit

	move.l	d4,d1		; job ID
	lea	(a6,a1.l),a1	; string to set
	jmp	(a4)		; do thing call

;+++
; dir$ = HOME_DIR$ (job-ID)
;---
home_dir$
	lea	gu_ghome,a4
	bra.s	hd_strfunc

;+++
; dir$ = HOME_FILE$ (job-ID)
;---
home_file$
	lea	gu_ghomefile,a4
	bra.s	hd_strfunc

;+++
; dir$ = HOME_CURR$ (job-ID)
;---
home_curr$
	lea	gu_ghomecurr,a4

hd_strfunc
	moveq	#0,d0			; job-ID is the only parameter
	bsr	get_jobid
	bne.s	hd_exit
	moveq	#0,d2			; only determine string length
	jsr	(a4)			; call utility function
	cmp.l	#err.fdnf,d0		; string not found?
	beq.s	hd_returnempty		; special, return empty string
	cmp.l	#err.orng,d0		; out of range?
	beq.s	hd_getstring		; okay, that was to be expected
	tst.l	d0
	bne.s	hd_exit 		; any other error, exit

hd_getstring
	move.l	d1,-(sp)
	moveq	#1,d1			; rounding up
	add.l	d2,d1			; string size
	bclr	#0,d1			; round
	jsr	ut_chkri		; ensure enough space on RI stack
	move.l	d1,d2			; this much space in buffer now
	sub.l	d2,a1			; go to begin of RI space
	move.l	(sp)+,d1		; restore job-ID
	move.w	sr,d7
	trap	#0			; SBASIC must not move now
	adda.l	a6,a1			; absolute
	jsr	(a4)			; call utility function again
	suba.l	a6,a1			; relative again
	move.w	d7,sr
	tst.l	d0
	bne.s	hd_exit 		; error now?
	jmp	ut_retst		; return string

hd_returnempty
	moveq	#2,d1
	jsr	ut_chkri		; ensure enough space on RI stack
	subq.l	#2,a1
	clr.w	(a6,a1.l)		; empty string
	jmp	ut_retst		; return it

hd_ipar
	moveq	#err.ipar,d0
hd_exit
	rts

;+++
; ver$ = HOME_VER$
;---
home_ver$
	jsr	gu_homever
	bne.s	hd_exit

	move.l	d1,d4
	moveq	#6,d1
	jsr	ut_chkri		; ensure enough space on RI stack

	suba.l	#6,a1
	move.w	#4,0(a6,a1.l)		; string length
	move.l	d4,2(a6,a1.l)		; thing version
	jmp	ut_retst		; return string

;+++
; HOME_DEF job-name,file-name
;---
home_def
	jsr	ut_gtstr
	bne.s	hd_exit
	cmp.w	#2,d3
	bne.s	hd_ipar

	lea	(a6,a1.l),a1		; job-name
	move.l	a1,a2
	moveq	#3,d0			; round up length
	add.w	(a1),d0
	bclr	#0,d0
	adda.w	d0,a2			; skip first string
	jmp	gu_shomedef

;+++
; Get optional job ID
;
; In:  d0 = parameter count for function without specified job ID * 8
; Out: d1 = job ID
;      a3 = updated
; ---
get_jobid
	movem.l a1,-(sp)
	add.l	a3,d0
	cmp.l	d0,a5			; exactly the parameter count wo jobID?
	beq.s	gji_default		; yes, just use default

	jsr	ut_gtli1		; get one long integer
	bne.s	gji_exit
	addq.l	#8,a3			; and move past it
	move.l	0(a6,a1.l),d1		; get job ID
	addq.l	#4,sb_arthp(a6) 	; reset ri stack pointer
	bra.s	gji_ok

gji_default
	moveq	#-1,d1			; default to current job
gji_ok
	moveq	#0,d0
gji_exit
	movem.l (sp)+,a1
	rts

	end
