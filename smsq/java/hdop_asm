; SMSQmulator "Hardware" Operations @ w. Lenerz 2012-2014
;
; 1.01 2014 May 07 keyboard linkage is linked in
	section sms

	xdef	hdop_init
	xref	sms_hdop
	xdef	kbd_poll

	xref	mse_poll

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
	include 'dev8_keys_java'
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
; This is initialisation for the "hardware" operations
; the keyboard thing is there for compatibility reasons, it is of no use.
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

hdop_ithg
	lea	kbd_thing,a1		 ; our Thing
	lea	kb_thgl(a3),a0		 ; link in thing

	move.l	a1,th_thing+kb_thgl(a3)  ; ... set pointer
	lea	th_verid+kb_thgl(a3),a2
	move.l	#kbd_vers,(a2)+ 	 ; ... set version
	lea	kbd_tnam,a1		 ; thing name
	move.l	(a1)+,(a2)+
	move.b	(a1)+,(a2)+

	jsr	gu_thzlk		 ; link in thing

	move.w	sys_lang(a6),d1 	 ; default keyboard table
	jsr	kbd_sett		 ; set tables

	lea	hdop_poll,a4
	move.l	a4,iod_plad(a3) 	 ; set polling server address
	lea	iod_pllk(a3),a0
	moveq	#sms.lpol,d0
	trap	#do.sms2
	move.l	a3,sys_klnk(a6) 	 ; set pointer to linkage  (v. 1.01)
	lea	kbd_read,a0
	move.l	jva_lkptr,d0
	beq.s	hdop_rts
	move.l	d0,a3
	move.l	a0,jva_kbrd(a3)
hdop_rts
	rts


;+++
; mouse & kbd polling routines
;---
hdop_poll
	jsr	mse_poll
kbd_poll
	jmp	kbd_read		; on interrupt read kbd

	end
