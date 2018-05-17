; Home directory functions  v1.00		        2005  Wolfgang Lenerz
;							       Marcel Kilgus
;
; 2005-11-17	1.00	reset correct job ID when freeing home thing (mk)

	section gen_util

	xdef	gu_shome
	xdef	gu_shomeover
	xdef	gu_shomecurr
	xdef	gu_shomedef
	xdef	gu_ghome
	xdef	gu_ghomefile
	xdef	gu_ghomecurr
	xdef	gu_homever

	xref	gu_thjmp

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'

homereg reg	a0-a4/d1-d3
d1stak	equ	0			; where D1 is on stack
d2stak	equ	4			; same for D2

;+++
; Set home directory for the given job in the home thing.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	A1	Pointer to file name	preserved
;---
gu_shome
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'SETH',d2		; extension in thing to use
	bra.s	sh_common

;+++
; Like "set home directory", but overwrites it.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	A1	Pointer to file name	preserved
;---
gu_shomeover
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'OVER',d2		; extension in thing to use
	bra.s	sh_common

;+++
; Set current directory for the given job in the home thing.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	A1	Pointer to file name	preserved
;---
gu_shomecurr
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'SETC',d2		; extension in thing to use

sh_common
	move.l	a1,a4			; save filename
	lea	home_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	sh_exit 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	move.l	d1stak(sp),d1		; get job ID back
	sub.l	#12,sp			; get some space
	move.l	sp,a1			; and point to it
	move.l	d1,(a1) 		; insert ID of job
	move.l	#$c1000000,4(a1)	; thp.call+thp.str
	move.l	a4,8(a1)		; set pointer to this string
	jsr	thh_code(a0)		; call extn thing
	add.l	#12,sp			; reset stack
	move.l	d0,-(sp)		; remember error
	lea	home_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	(sp)+,d0		; restore error
sh_exit
	movem.l (sp)+,homereg
	rts

;+++
; Get home directory for the given job.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	D2	Buffer size		Buffer size needed (err.orng)
;	A1	Pointer to buffer	preserved
;---
gu_ghome
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'GETH',d2		; extension in thing to use
	bra.s	gh_common

;+++
; Get home file for the given job.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	D2	Buffer size		Buffer size needed (err.orng)
;	A1	Pointer to buffer	preserved
;---
gu_ghomefile
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'GETF',d2		; extension in thing to use
	bra.s	gh_common

;+++
; Get current directory for the given job.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1	Job-ID			preserved
;	D2	Buffer size		Buffer size needed (err.orng)
;	A1	Pointer to buffer	preserved
;---
gu_ghomecurr
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'GETC',d2		; extension in thing to use
gh_common
	move.l	a1,a4			; save buffer pointer
	lea	home_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	gh_exit 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	move.l	d1stak(sp),d1		; get job ID back
	move.l	d2stak(sp),d2		; same for buffer size
	sub.l	#12,sp			; get some space
	move.l	sp,a1			; and point to it
	move.l	d1,(a1) 		; insert ID of job
	move.w	#$a100,4(a1)		; thp.str+thp.ret
	move.w	d2,6(a1)		; buffer size
	move.l	a4,8(a1)		; set pointer to this string
	jsr	thh_code(a0)		; call extn thing
	add.l	#12,sp			; reset stack
	move.l	d2,d2stak(sp)		; return d2 from call
	move.l	d0,-(sp)		; remember error
	lea	home_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	(sp)+,d0		; restore error
gh_exit
	movem.l (sp)+,homereg
	rts

;+++
; Get home thing version.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	D1				thing version
;---
gu_homever
	movem.l homereg,-(sp)		; keep my regs
	move.l	#'GETH',d2		; extension in thing to use
	lea	home_name,a0		; point to name of thing
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	ghv_exit		; no!
	move.l	d3,d1stak(sp)		; overwrite stack d1 with version
	lea	home_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	moveq	#0,d0			; ignore possible error
ghv_exit
	movem.l (sp)+,homereg
	rts

;+++
; Set default directory for a given job name.
;
; Registers:
;		Entry			Exit
;	D0				error code
;	A1	Pointer to job-name	preserved
;	A2	Pointer to directory	preserved
;---
gu_shomedef
	movem.l homereg,-(sp)		; keep my regs
	move.l	a1,a3			; save pointers
	move.l	a2,a4
	lea	home_name,a0		; point to name of thing
	move.l	#'SETD',d2		; extension in thing to use
	moveq	#-1,d3			; wait forever
	moveq	#-1,d1			; I will use the thing
	moveq	#sms.uthg,d0		; use thing
	jsr	gu_thjmp		; on return A2= ptr thg header, a1 to thg
	tst.l	d0			; ok?
	bne.s	gh_exit 		; no!
	move.l	a1,a0			; pointer to thing (!!!)
	move.l	d1stak(sp),d1		; get job ID back
	move.l	d2stak(sp),d2		; same for buffer size
	sub.l	#16,sp			; get some space
	move.l	sp,a1			; and point to it
	move.l	#$C1000000,(a1) 	; thp.str+thp.call
	move.l	a3,4(a1)		; job name
	move.l	#$C1000000,8(a1)	; thp.str+thp.call
	move.l	a4,12(a1)		; directory
	jsr	thh_code(a0)		; call extn thing
	add.l	#16,sp			; reset stack
	move.l	d0,-(sp)		; remember error
	lea	home_name,a0		; free thing
	moveq	#sms.fthg,d0
	moveq	#-1,d1
	jsr	gu_thjmp
	move.l	(sp)+,d0		; restore error
	movem.l (sp)+,homereg
	rts

home_name
	dc.w  4,'HOME'

	end
