; Helper routine to make SMSQ/E code QDOS PE compatible   2005  Marcel Kilgus
;
; 2011-04-12  1.01  Don't call routine if pt_kyrwr is not set yet! (MK)

	section driver

	xdef	kbd_keyrow

	include 'dev8_keys_con'

kbd_keyrow
	movem.l d0/a0/a3,-(sp)
	move.l	pt_kyrwr(a3),d0 	; find keyrow routine
	beq.s	kbd_none		; during setup, scheduler might already
	movea.l d0,a0			;   run before pt_kyrwr is initialised!
	lea	ipc_keyr(pc),a3 	; cursor keys
	cmp.w	#1,d1
	beq.s	kbd_read
	lea	ipc_ctrl(pc),a3 	; control keys
	cmp.w	#7,d1
	beq.s	kbd_read
kbd_none
	moveq	#0,d1
	bra.s	kbd_rts
kbd_read
	jsr	(a0)
kbd_rts
	movem.l (sp)+,d0/a0/a3
	rts

ipc_keyr
	dc.b	9,1,0,0,0,0,1,2 	; read cursor keyrow
	ds.w	0
ipc_ctrl
	dc.b	9,1,0,0,0,0,7,2 	; read shift/alt/ctrl (and odds)
	ds.w	0

	end
