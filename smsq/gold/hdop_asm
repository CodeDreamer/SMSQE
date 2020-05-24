; GOLD Card Hardware Operations
;
; 2020-04-09  1.01  Set sys_klnk with keyboard linkage to fix KBD_TABLE (MK)

	section sms

	xdef	hdop_init
	xdef	sms_hdop
	xdef	ql_hcmds
	xdef	ql_hcmdw
	xdef	ql_hcmdb
	xdef	ql_hcmdn
	xdef	ql_hcmdr
	xdef	kbd_poll

	xref	ser_rx

	xref	kbd_read
	xref	kbd_thing
	xref	kbd_tnam
	xref.l	kbd_vers
	xref	kbd_sett

	xref	gu_thzlk
	xref	ut_procdef

	include 'dev8_keys_err'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_k'
	include 'dev8_keys_sys'
	include 'dev8_keys_thg'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qlhw'
	include 'dev8_keys_iod'
	include 'dev8_keys_con'
	include 'dev8_keys_chn'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'
	include 'dev8_mac_proc'

kbd_procs
	proc_stt
	proc_def KBD_TABLE
	proc_end
	proc_stt
	proc_end

;+++
; This is initialisation for the hardware operations - for the QL this includes
; the physical layers of the keyboard and serial receiver as these are mixed up.
;---
hdop_init
	lea	kbd_procs,a1
	jsr	ut_procdef

	move.l	#kb_end,d1		 ; create space for (keyboard) linkage
	moveq	#sms.achp,d0
	moveq	#0,d2			 ; permanent
	trap	#do.sms2
	move.l	#kb.gold,kb_ID(a0)	 ; set flag
	move.l	a0,a3

	moveq	#sms.xtop,d0		 ; supervisor mode
	trap	#do.sms2

	move.l	a3,sys_klnk(a6) 	 ; remember linkage for KBD_TABLE
	move.l	sys_ckyq(a6),d0 	 ; extra keyboard init?
	beq.s	hdop_ithg		 ; ... no
	move.l	d0,a0			 ; ... yes, do it
	clr.l	sys_ckyq(a6)
	lea	hdop_kbd,a5		 ; address to patch
	lea	sms_hdop,a4		 ; and another
	jsr	(a0)

hdop_ithg
	lea	kbd_thing,a1		 ; our Thing
	lea	kb_thgl(a3),a0		 ; link in thing

	move.l	a1,th_thing+kb_thgl(a3)  ; ... set pointer
	lea	th_verid+kb_thgl(a3),a2
	move.l	#kbd_vers,(a2)+ 	 ; ... set version
	lea	kbd_tnam,a1		 ; thing name
	move.l	(a1)+,(a2)+
	move.b	(a1)+,(a2)+

	jsr	gu_thzlk

	move.w	sys_lang(a6),d1 	 ; default keyboard table
	jsr	kbd_sett		 ; set tables

	lea	hdop_poll,a4
	move.l	a4,iod_plad(a3) 	 ; set polling server address
	lea	iod_pllk(a3),a0
	moveq	#sms.lpol,d0
	trap	#do.smsq

	moveq	#$ffffffc9,d0
	bsr.l	ql_hcmdb		 ; one byte command to set Hermes
	lea	hdop_test,a3
	bsr.l	ql_hcmdw		 ; test
	cmp.b	#$f0,d1 		 ; Hermes?
	bne.s	hdop_rts
	assert	sys.herm,1
	bset	#0,sys_mtyp(a6) 	 ; set Hermes in use
hdop_rts
	rts

hdop_test dc.b	2,4,$f,8,$f,8		 ; send $F, get $F (IPC) or $F0 (Hermes)
hdop_req dc.b	1,4,1,8 		 ; request and get 8 bit reply

;+++
; IPC polling routine
;---
hdop_poll
kbd_poll
;  subq.w  #1,$20000
;  bgt.s   xx
;  move.w  #50,$20000
;  not.w   $20002
;xx
	move.l	a3,a1			 ; save linkage
	lea	hdop_req,a3
	bsr.l	ql_hcmds		 ; request status
	move.b	d1,d7			 ; keep status for ser
	roxr.b	#2,d1			 ; bit 7 keyboard, X sound
	scs	sys_qlbp(a6)		 ; ... set beeping
hdop_kbd
	bpl.s	hdop_ser1		 ; ... no kbd input
	bsr.l	kbd_read		 ; read the keyboard

hdop_ser1
	btst	#4,d7			 ; any ser1?
	beq.s	hdop_ser2
	moveq	#0,d0			 ; ser 1
	moveq	#6,d4			 ; IPC command to read
	jsr	ser_rx

hdop_ser2
	btst	#5,d7			 ; any ser2?
	beq.s	hdop_rts
	moveq	#1,d0			 ; ser 2
	moveq	#7,d4			 ; IPC command to read
	jmp	ser_rx

;+++
; This is sms.hdop processing code
;
;	d1  r	reply
;	a3 c  p pointer to command
;
;	Status return standard
;---
sms_hdop
smsh.reg reg	d2/d3/a2/a3/a4/a5
	bra.s	smsh_do
	nop
	nop
smsh_do
	movem.l smsh.reg,-(sp)
	move.b	(a3)+,d2		 ; command number
	moveq	#0,d1
	move.b	(a3)+,d1		 ; number of parameter bytes
	move.w	d1,d0
	addq.w	#2,d0			 ; room required on stack
	add.w	d0,d0
	move.l	sp,a2			 ; save stack ptr
	sub.w	d0,sp
	move.l	sp,a4			 ; running pointer to command
	move.w	#4,(a4)+		 ; 0,4 bit
	move.b	d2,(a4)+
	move.l	(a3)+,d2		 ; the format
	bra.s	smsh_eloop
smsh_loop
	move.b	(a3)+,d3		 ; next byte
	moveq	#4,d0			 ; four bit
	roxr.l	#2,d2			 ; nop bit in msb, 4/8 in sign
	bmi.s	smsh_eloop		 ; ... nop
	bcc.s	smsh_nbyt		 ; ... 4 bit
	moveq	#8,d0			 ; ... 8 bit
smsh_nbyt
	move.b	d0,(a4)+		 ; length
	move.b	d3,(a4)+		 ; and contents
smsh_eloop
	dbra	d1,smsh_loop

	moveq	#%11,d0
	and.b	(a3),d0 		 ; reply
	move.b	smsh_btab(pc,d0.w),(a4)+ ; last byte

	move.l	sp,a3			 ; command is here
	sub.l	a3,a4
	move.w	a4,d0
	lsr.w	#1,d0
	subq.w	#1,d0
	move.b	d0,(a3) 		 ; total number of bytes to send

	bsr.s	ql_hcmd
	move.l	a2,sp			 ; restore stack pointer
	movem.l (sp)+,smsh.reg
	move.l	sms.rte,a5
	jmp	(a5)			 ; return from trap

smsh_btab dc.b	4,0,8,0

;+++
; This is ql_hcmd with the registers (except d1/a3) saved
;---
ql_hdop
qlh.reg reg	d2/a4/a5
	movem.l qlh.reg,-(sp)
	bsr.s	ql_hcmd
	movem.l (sp)+,qlh.reg
	rts

;+++
; This routine processes a command for the IPC link. Interrupts are disabled.
; The nibbles / bytes of the command are pointed to by (a3), the first byte is
; the number of nibbles or bytes to send, this is followed by pairs of bytes,
; the first byte of the pair is the number of bits to send, followed by the
; the byte with the bits. The byte after the last send byte is the number of
; bits to receive (up to 32).
;
;	d0  r	zero
;	d1  r	(long) reply
;	d2   s
;	a3 c  u pointer to command
;	a4  r	ipcrd
;	a5  r	iprwr
;
;	Status return 0
;---
ql_hcmd
	move.w	sr,-(sp)		 ; save sr
	or.w	#$0700,sr		 ; no interrupts
	bsr.s	ql_hcmds
	move.w	(sp)+,sr
	moveq	#0,d0
	rts

;------------------
ql_hcmds
	lea	pc_ipcwr,a5
	lea	pc_ipcrd-pc_ipcwr(a5),a4 ; hardware addresses
;------------------
ql_hcmdw
	move.b	(a3)+,d1		 ; number of bytes to send

qlhc_loop
	move.b	(a3)+,d2		 ; bits in next byte
	moveq	#0,d0
	move.b	(a3)+,d0		 ; bits of next byte / nibble
	ror.w	d2,d0			 ; into top end of word
	bsr.s	qlhc_wloop

	subq.b	#1,d1
	bgt.s	qlhc_loop

	move.b	(a3)+,d2
	beq.s	qlhc_rts		  ; no reply

;-----------
ql_hcmdr
	moveq	#0,d1

qlhc_rloop
	move.b	#pc.ipcrd,(a5)
qlhc_rwait
	btst	#pc..ipca,(a4)		 ; accepted yet?
	bne.s	qlhc_rwait		 ; ... no
	move.b	(a4),d0 		 ; rx bit in msb
	add.b	d0,d0			 ; ... in X
	roxl.w	#1,d1			 ; ... in bit 0

	subq.b	#1,d2			 ; next bit
	bgt.s	qlhc_rloop
qlhc_rts
	rts

;--------------
ql_hcmdn
	ror.w	#4,d0			 ; nibble to send in top end
	moveq	#4,d2			 ; four bits
	bra.s	ql_hdcmd1
;--------------
ql_hcmdb
	lsl.w	#8,d0			 ; byte to send in top end
	moveq	#8,d2			 ; eight bits
ql_hdcmd1
	lea	pc_ipcwr,a5
	lea	pc_ipcrd-pc_ipcwr(a5),a4 ; hardware addresses

qlhc_wloop
	move.b	#pc.ipcwr>>2,d0 	 ; IPC write bits
	rol.w	#1,d0
	add.b	d0,d0			 ; bit to send in bit 1
	move.b	d0,(a5)
qlhc_wwait
	btst	#pc..ipca,(a4)		 ; accepted yet?
	bne.s	qlhc_wwait		 ; ... no

	subq.b	#1,d2
	bgt.s	qlhc_wloop
	rts

	end
