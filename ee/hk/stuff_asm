; HOTKEY Stuff Keyboard queue  V2.00	 1988	 Tony Tebby   QJUMP

	section hotkey

	xdef	hk_stuff
	xdef	hk_stfbf
	xdef	hk_stfpr
	xdef	hk_stfcm

	xref	hk_pick
	xref	hk_pick1
	xref	hk_xthg
	xref	hk_getpr

	include 'dev8_keys_k'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qu'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_sms'
	include 'dev8_ee_hk_data'

hsc_item dc.w	 hki.id,hki.wake,0,0,3,'CLI'

;+++
; This routine wakes CLI or picks Basic and waits for up to 4 seconds for the
; keyboard Queue to point to channel #0.
; It then stuffs the command keyboard queue with defined string.
;
;	a1 c  p pointer to item
;
;	status return 0, err.nc
;---
hk_stfcm
reg.cmd reg	d1/d3/d4/a0/a1
	movem.l reg.cmd,-(sp)

	lea	hsc_item,a1			 ; wake CLI
	jsr	hk_pick
	beq.s	hsc_go
	jsr	hk_xthg 			 ; try executing thing
	bne.s	hsc_pbas

	moveq	#19,d4				 ; try up to 20*100ms
hsc_pcli
	jsr	hks_ssjb			 ; wait a bit
	jsr	hk_pick 			 ; pick
	dbeq	d4,hsc_pcli
	beq.s	hsc_go				 ; ok
	bne.s	hsc_nc				 ; bad

hsc_pbas
	moveq	#0,d1				 ; pick basic
	jsr	hk_pick1
	bne.s	hsc_nc

	moveq	#80,d4				 ; 80x5 ticks

hsc_loop
	subq.w	#1,d4
	ble.s	hsc_nc				 ; timed out

	bsr.s	hks_ssjb			 ; wait
	moveq	#sms.info,d0
	trap	#do.sms2
	move.l	sys_chtb(a0),a1 		 ; channel table
	move.l	(a1),a1 			 ; channel zero
	cmp.l	sys_ckyq(a0),a1 		 ; keyboard queue
	bge.s	hsc_loop			 ; below channel 0
	add.l	(a1),a1
	cmp.l	sys_ckyq(a0),a1
	ble.s	hsc_loop			 ; above channel 0

hsc_go
	movem.l (sp)+,reg.cmd
	moveq	#k.nl,d0			 ; terminate with <NL>
	bra.s	hks_go				 ; stuff it

hsc_nc
	moveq	#err.nc,d0
	movem.l (sp)+,reg.cmd
	rts

;+++
; This routine sets the stuffer pointer back to the previous string and
; then stuffs the current keyboard queue
;
;	a1 c  p pointer to previous stuff item
;
;	status return 0
;---
hk_stfpr
	jsr	hk_getpr		 ; backspace to previous
	bra.s	hk_stuff

;+++
; This routine stuffs the current keyboard queue with current stuffer buffer
;
;	a1 c  p pointer to item
;
;	status return 0
;---
hk_stfbf
	move.l	hkd_bpnt(a3),hkd_ppnt(a3) ; reset previous pointer

;+++
; This routine stuffs the current keyboard queue with defined string
;
;	a1 c  p pointer to item
;
;	status return 0
;---
hk_stuff
	moveq	#0,d0				 ; no terminator
hks_go
reglist reg	d1/d2/d3/d4/a0/a1/a2/a3/a4
	movem.l reglist,-(sp)
	move.b	d0,d4
	move.l	hki_ptr(a1),d0			 ; pointer to stuff buffer
	move.l	d0,a4
	bne.s	hks_set 			 ; ... there is one
	lea	hki_name+2(a1),a4		 ; ... use name instead!!

hks_set
	moveq	#sms.info,d0
	trap	#do.sms2
	move.l	sys_ckyq(a0),a2 		 ; set keyboard queue pointer

hks_loop
	bsr.s	hks_do				 ; stuff as much as possible
	beq.s	hks_done

	bsr.s	hks_ssjb
	bra.s	hks_loop

hks_done
	movem.l (sp)+,reglist
	rts

hks_ssjb
	move.l	a1,-(sp)
	moveq	#sms.ssjb,d0			 ; suspend
	moveq	#-1,d1
	moveq	#5,d3				 ; five ticks
	sub.l	a1,a1
	trap	#do.sms2
	move.l	(sp)+,a1
	rts

hks_do
	move.w	sr,d0			 ; save status
	trap	#0
	move.w	d0,-(sp)
	or.w	#$0700,sr		 ; no interruptions please

hks_char
	move.b	(a4),d1 		 ; next character
	bne.s	hks_pbyte
	move.b	d4,d1			 ; terminating char
	beq.s	hks_ok			 ; ... none
hks_pbyte
	move.w	ioq.pbyt,a3		 ; put byte
	jsr	(a3)
	bne.s	hks_exit
	tst.b	(a4)+			 ; was it end?
	bne.s	hks_char		 ; ... no
hks_ok
	moveq	#0,d0
hks_exit
	move.w	(sp)+,sr
	tst.l	d0
	rts
	end
