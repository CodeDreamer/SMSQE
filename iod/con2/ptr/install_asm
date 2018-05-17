* Install a new console driver	V2.02	 2000	 Tony Tebby
*
* 2003-04-24  2.02	Added table pointer for vectored routine
* 2006-10-20  2.03	BLAT macro definitions commented out - macro wasn't used (wl)

	xdef	pt_install
	xdef	pt_installu
	xdef	pt_installv
*
	xref	cn_sched
	xref	cn_io
	xref	pt_open
	xref	pt_io
	xref	pt_close
	xref	pt_modex
	xref	pt_cchset
	xref	pt_changer
	xref	pv_table

	xref	pt_scanmode
	xref	pt_scancrop
	xref	pt_scanclr

	xref	cn_disp_size
	xref	cn_palset

	xref.l	pt.spsln ; length of sprite save area
	xref.l	pt.spcln ; length of sprite cache
	xref.l	pt.palql ; length of QL palette
	xref.l	pt.pal256 ; length of 256 colour palette
*
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_jcb'
	include 'dev8_keys_con'

	section init

*blat macro blv
* xref blatt
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm
*
*blatl macro blv
* xref blattl
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm

pt_installu
	moveq	#sms.xtop,d0
	trap	#do.sms2

*	d0  r	0 = no install, -ve colour depth not supported, +ve chnged
*	d1 c  p screen size (byte / word / long as required by cn_disp_size)
*	d2 c  p colour depth (byte extended to word)
*	a3 c  p base of linkage block
*
pt_install
reglist reg	d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a4/a5
	movem.l reglist,-(sp)

;;;; Here there was an instruction to disable interrupts.
;;;; As the pointer interface operates exclusivley from the scheduler and
;;;; from direct calls, this should not be necessary.
;;;; For the QXL it was disastrous.

; blat #$99
	move.l	d1,d6
	moveq	#0,d7
	move.b	d2,d7

	moveq	#sms.cach,d0
	moveq	#2,d1
	trap	#do.smsq		 ; suppress cache

	tst.l	pt_aschd(a3)		 ; drivers already linked in?
	bne.s	pti_select		 ; ... yes
*
* link in drivers
*
; blat #$66
	lea	pt_lschd(a3),a0
	moveq	#sms.lshd,d0
	trap	#1

	lea	pt_liod(a3),a0
	moveq	#sms.liod,d0
	trap	#1
*
*	set null close routine
*
	lea	pt_ok,a0		 ; null close
	move.l	a0,pt_ptrok(a3) 	 ; OK

pti_select
	cmp.b	pt_cdpth(a3),d7 	 ; already installed?
	beq.l	pti_exit		 ; ... yes

	lea	pt_cdtab(a3),a4
	ext.w	d7
; blat d7
	move.b	(a4,d7.w),d5		 ; mode for this resolution
; blat d5
; blat pt_dmode(a3)

	move.w	d7,d0
	lsl.w	#2,d0			 ; index driver install table
	move.l	pt_drtab-pt_cdtab(a4,d0.w),a1 ; this is the one to install
; blatl a1
	move.l	a1,d0			 ; exists?
	beq.l	pti_exnf		 ; ... no

	move.l	pt_spsav(a3),d0 	 ; remove sprite save area
	beq.s	pti_install		 ; ... none
	move.l	d0,a0
; blat #$81
	moveq	#sms.rchp,d0
	trap	#do.sms2
pti_install
; blat #$82
	jmp	(a1)			 ; jump to the right driver

pt_installv
	move.b	d5,pt_dmode(a3) 	 ; install new driver
	move.b	d7,pt_cdpth(a3)
	pea	cn_sched
	move.l	(sp)+,pt_aschd(a3)
	pea	pt_io(pc)
	move.l	(sp)+,pt_aio(a3)
	pea	pt_open(pc)
	move.l	(sp)+,pt_aopen(a3)
	pea	pt_close(pc)
	move.l	(sp)+,pt_aclos(a3)
	pea	pt_changer(pc)
	move.l	(sp)+,pt_change(a3)
	pea	pv_table(pc)
	move.l	(sp)+,pt_vecs(a3)

	lea	pt_modex,a0
	lea	sms.t1tab+sms.dmod*4,a5  ; pointer mode routine
	bsr.l	pti_vector

	moveq	#sms.achp,d0		 ; allocate the table memory required
	move.l	#pt.spsln+pt.spcln+pt.palql+pt.pal256,d1
	moveq	#0,d2			 ; owned by 0
	trap	#1
	tst.l	d0
	bne.s	pti_exit
	move.l	a0,a5			 ; set sprite save area address
	move.l	a5,pt_spsav(a3)

	lea	pt.spsln(a5),a5
	move.l	a5,pt_spcch(a3) 	 ; set sprite cache address
	jsr	pt_cchset

	add.l	#pt.spcln,a5
	move.l	a5,a4
	lea	pt.palql(a4),a5
	move.l	a4,pt_palql(a3) 	 ; set QL palette map
	move.l	a5,pt_pal256(a3)	 ; set 256 colour palette map
	jsr	cn_palset

	jsr	cn_disp_size		 ; set up sizes (d6,d7)
	jsr	pt_scanmode		 ; set new mode in all channels
	jsr	pt_scancrop		 ; remove all jobs outside
	jsr	pt_scanclr		 ; clear all windows

	moveq	#1,d0
	bra.s	pti_exit
pti_exok
	moveq	#0,d0
pti_exit
	movem.l (sp)+,reglist
	tst.l	d0
	rts
pti_exnf
	moveq	#err.fdnf,d0
	bra.s	pti_exit

pti_vector
	move.l	a0,d0			 ; write a0 to vector area
	swap	d0
	bsr.s	pti_vecw
	swap	d0
pti_vecw
	jmp	sms.wbase

pt_ok
	moveq	#0,d0
	rts

	end
