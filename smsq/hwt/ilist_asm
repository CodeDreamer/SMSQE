; SMSQ Hardware table handling	V2.10	  1999  Tony Tebby

	section hwt

	xdef	hwt_ilist		 ; conversion to interrupt list

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_68000'
	include 'dev8_keys_hwt'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

;+++
; Hardware table setup - convert table to interrupt list
;
;	d1 c  p interrupt level to build (byte)
;	a1 c  p interrupt handling code table
;	a0  r	interrupt list
;
;	status return standard
;
; The interrupt handler code table has two vectors to the preable and the
; postable code. The preamble is preceded by its length as it is copied
; into the table.
;
; The list is automatically installed in the appropriate 'autovector'.
; In thias version, the operation is carried out with interrupts disabled
; using the SMSQ write to base area routine.
; If the old vector was to a HWT list, the old hwt list is thrown away.
; The pointer to the new hwt list is returned in case it needs to be installed
; in a hardware vector.
;
;---
hwt_ilist
hil.reg reg	d1/d2/d3/d4/a0/a1/a2/a3/a4/a5
	movem.l hil.reg,-(sp)

	move.l	sms.hwtab,a5		 ; hardware table
	move.l	hwt_ptr(a5),d4		 ; top

	assert	hwt.table,hwt_table
	lea	(a5),a2 		 ; pointer to -1 entry
	moveq	#0,d2			 ; number of entries in table
	bra.s	hil_ecnt
hil_cnt
	cmp.b	hwt_intl(a2),d1 	 ; this interrupt level?
	bne.s	hil_ecnt
	addq.w	#1,d2			 ; another

hil_ecnt
	add.w	#hwt.table,a2		 ; next entry
	cmp.l	d4,a2
	blo.s	hil_cnt 		 ; another

	move.l	a1,a2
	add.w	(a1)+,a2		 ; preamble
	move.w	(a2)+,d3		 ; length of preamble
	moveq	#3,d0			 ; 4+8 bytes extra
	add.w	d2,d0
	add.w	d2,d0
	lsl.w	#2,d0			 ; room for flag (4) and list 8*(n+1)
	add.w	d3,d0			 ; room for preamble

	jsr	gu_achpp
	bne.s	hil_exit

	move.l	#hwti.flag,(a0)+
	move.l	a0,a4			 ; save start of code
hil_pre
	move.w	(a2)+,(a0)+
	subq.w	#2,d3
	bgt.s	hil_pre

	move.l	a0,a3			 ; do not lose base of table

	assert	hwt.table,hwt_table
	lea	(a5),a2 		 ; pointer to -1 entry
	bra.s	hil_esetl
hil_setl
	cmp.b	hwt_intl(a2),d1 	 ; this interrupt level?
	bne.s	hil_esetl
	move.l	hwt_serve(a2),(a3)+
	move.l	hwt_link(a2),(a3)+	 ; set server list entry
hil_esetl
	add.w	#hwt.table,a2		 ; next entry
	cmp.l	d4,a2
	blo.s	hil_setl		 ; another

	add.w	(a1),a1 		 ; set postamble address
	move.l	a1,(a3)+
	move.l	a0,(a3)+		 ; set base of linkage

	moveq	#exv_i1/4-1,d0
	add.b	d1,d0			 ; vector number
	lsl.w	#2,d0			 ; vector address
	move.l	d0,a5
	move.l	(a5),a0 		 ; old vector

	move.l	a4,d0			 ; base
	swap	d0
	move.w	sr,d1
	or.w	#$0700,sr
	jsr	sms.wbase		 ; set vector
	swap	d0
	jsr	sms.wbase
	move.w	d1,sr

	cmp.l	#hwti.flag,-(a0)	 ; hwt interrupt list?
	bne.s	hil_done

	jsr	gu_rchp 		 ; return old

hil_done
	move.l	a4,a0
	moveq	#0,d0

hil_exit
	movem.l (sp)+,hil.reg
	rts

	end
