; SMSQ KBD initialisation    1993  Tony Tebby
; Version without interrupt install

	section kbd

	xdef	kbd_initi
	xdef	kbd_inits

	xref	kbd_poll
	xref	kbd_sett

	xref	ut_procdef
	xref	gu_thzlk
	xref	ioq_setq

	xref	kbd_thing
	xref	kbd_tnam

	xref.l	smsq_vers

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_thg'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_mac_proc'
	include 'dev8_mac_vecx'

kbd_procs
	proc_stt
	proc_def KBD_TABLE
	proc_end
	proc_stt
	proc_end

;+++
; initialise vectors, link in polling keyboard code
;
;	a3  r	base of keyboard linkage
;---
kbd_initi
	sub.l	a0,a0			 ; no special initialisation
kbd_inits
	lea	kbd_procs,a1
	jsr	ut_procdef

	moveq	#sms.xtop,d0		 ; supervisor mode
	trap	#do.sms2
	move.l	a0,a5

	move.l	#kb_end,d1		 ; create space for linkage
	moveq	#sms.achp,d0
	moveq	#0,d2			 ; permanent
	trap	#do.sms2
	tst.l	d0
	bne.s	kbi_rts 		 ; oops

	move.l	a0,a3
	move.l	#kb.ibm,kb_id(a3)	 ; set ID
	move.b	#10,kb_err(a3)		 ; reset error suppression

	lea	kbd_thing,a1		 ; our Thing
	lea	kb_thgl(a3),a0		 ; link in thing

	move.l	a1,th_thing+kb_thgl(a3)  ; ... set pointer
	lea	th_verid+kb_thgl(a3),a3
	move.l	#smsq_vers,(a3)+	 ; ... set version
	lea	kbd_tnam,a1		 ; thing name
	move.l	(a1)+,(a3)+
	move.b	(a1)+,(a3)+

	jsr	gu_thzlk
	bne.s	kbi_rts

	lea	kb_qu-kb_thgl(a0),a2	 ; preset queue
	move.l	a2,kb_queue-kb_thgl(a0)  ; set pointer to it
	moveq	#kb.qu,d1
	jsr	ioq_setq		 ; set header

	lea	-kb_thgl(a0),a3

	move.w	sys_lang(a6),d1 	 ; default keyboard table
	jsr	kbd_sett		 ; set tables

; link us in

	lea	kbd_poll,a4
	move.l	a4,iod_plad(a3) 	 ; set polling server address
	lea	iod_pllk(a3),a0
	moveq	#sms.lpol,d0
	trap	#do.smsq

	move.l	a3,sys_klnk(a6) 	 ; set pointer to linkage

	move.l	a5,d0
	beq.s	kbi_rts
	jmp	(a5)			 ; call special supervisor mode init

kbi_rts
	rts

	end
