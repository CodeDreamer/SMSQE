; cursor sprite con vector
; this sets the per job table so that all channels opened for this job
; will have a normal cursor (d2 = 0) or a sprite cursor (d2=1) it also sets
; the channels already opened by this job to use (not use) sprite cursor
; 2004-04-02	1.00	copyright (c) W. Lenerz

	section driver


	include 'DEV8_keys_sys'
	include 'DEV8_keys_con'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_chn'
	include 'dev8_keys_err'
	include 'dev8_keys_qu'
	include 'dev8_keys_sysspr'

	xdef	pt_cursp

;+++
;  PV_CURSP				$1E
;
;  Call parameters			Return parameters
;  D0					D0   error
;  D1	jobID				D1   smashed
;  D2	status wished (0|1)		D2   preserved
;  D3					D3   preserved
;
;  A0					A0   preserved
;  A1					A1   preserved
;  A2					A2   preserved
;  A3	pointer to CON linkage block	A3   preserved
;---
; errors
;	IJOB	wrong job ID
;	NIMP	something went horribly wrong : no job table!
;	IPAR	wrong parameter in d2
;+++


pvcreg	reg	d2/d5/d6/a0-a2
pt_cursp
	movem.l pvcreg,-(sp)
	move.l	d1,d6			; keep job ID
	move.l	d2,d5			; keep action wished
	beq.s	cn_parok		; was 0 - param is ok
	subq.w	#1,d2
	bne	cs_ipar 		; wasn't 1 or 0 : bad parameter
cn_parok
	moveq	#sms.info,d0
	trap	#1			; get sysvars into A0
	move.l	d5,d2			;
	cmp.l	#-1,d6			; real job ID ?
	bne.s	cn_gtid 		;  ... yes
	move.l	d1,d6			;  ... no, so use this one

; set the byte in the job table, which will determine the cursor status
; of all future windows for that job
cn_gtid
	move.l	d6,d1			; ID of job to treat
	move.l	pt_cjob(a3),d0		; pointer to job status block
	beq.s	cs_nimp 		; there is none????
	move.l	d0,a1			; cursor job table
	move.l	sys_jbtt(a0),d0 	, top of job table
	sub.l	sys_jbtb(a0),d0 	; nbr of jobs * 4
	tst.w	d6			;
	blt.s	cs_ijob 		; negative index into job table???
	lsl.w	#2,d6			; make long word entry into table
	cmp.w	d0,d6			; are we in table?
	bgt.s	cs_ijob 		;  ...no, out of job table!
	swap	d1			; job tag in lower word
	cmp.w	(a1,d6.w),d1		; is this really that job?
	bne.s	cs_ijob 		; ... no ???
	andi.w	#$ff,d2 		; just to be on the safe side
	move.w	d2,2(a1,d6.w)		; set new cursor status for this job
	mulu	#sp.cursor,d2		; sprite number or 0

; now scan the channel defns to find all screen channels open for this job
; and set the necessary data in them
	move.l	sys_clnk(a0),d0 	; pointer scr/con dev driver defn block
	add.l	#$18,d0 		; jump over linkage block
	move.l	sys_chtb(a0),a2 	; channel table base
	move.l	sys_chtt(a0),d5 	; channel table top
	swap	d1
	move.l	d1,d6			; correct job ID
cs_fndch
	cmp.l	d5,a2			; off top of channel table?
	bhs.s	cs_nf			; ... yes, finished
	move.l	(a2)+,a1		; pointer to chan defn block
	cmp.l	chn_ownr(a1),d6 	; is this channel owned by this job?
	bne.s	cs_fndch		;  ... no, do next then
	cmp.l	chn_drvr(a1),d0 	; is channel a scr/con channel?
	bne.s	cs_fndch		;  ... no, do next then
cs_sspr 				; check for sprite cursor now
	lea	sd.extnl+sd_keyq(a1),a1 ; point to keyqueue
	move.l	qu_endq(a1),d1		; pointer to end of queue
	beq.s	no_con			; none, (may be scr channel)
	addq.l	#1,d1			;
	bclr	#0,d1			; make even
	move.l	d1,a1			; pointer sprite space for channel
no_con
	move.l	d2,sd.croff(a1) 	; (re)set pointer sprite
	bra.s	cs_fndch
cs_nf
	moveq	#0,d0
cs_out	movem.l (sp)+,pvcreg
	rts
cs_ijob
	moveq	#err.ijob,d0		; bad job ID
	bra.s	cs_out
cs_nimp
	moveq	#err.nimp,d0		; what?
	bra.s	cs_out

cs_ipar moveq	#err.ipar,d0		; bad parameter in d2
	bra.s	cs_out
	end
