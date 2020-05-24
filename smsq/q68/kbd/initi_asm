; SMSQE Q68 KBD initialisation	  1993  Tony Tebby
;			     2016  W. Lenerz

; Version without interrupt install
;
; 2019 Jan 06	1.01	save kbr read address in q68_kradd
;		1.00	amended for q68 : do not setup kbd_poll as polling
;			routine, but kbd_read (which then calls kbd_poll).

	section kbd

	xdef	kbd_initiq68

	xref	kbd_read
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
	include 'dev8_keys_q68'
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
kbd_initiq68
	sub.l	a0,a0			 ; no special initialisation

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

	lea	kbd_read,a4
	move.l	a4,iod_plad(a3) 	 ; set polling server address
	move.l	a4,q68_kradd		 ; keep vector
	move.l	a3,q68_kradd+4		 ; and linkage
	lea	iod_pllk(a3),a0
	moveq	#sms.lpol,d0
	trap	#do.smsq

	move.l	a3,sys_klnk(a6) 	 ; set pointer to linkage
	st	kbd_unlock

	move.l	a5,d0
	beq.s	kbi_rts
	jmp	(a5)			 ; call special supervisor mode init

kbi_rts
	rts
	end
