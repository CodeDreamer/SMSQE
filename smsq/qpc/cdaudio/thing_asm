; QPC CD-Audio thing

	section thing

	xdef	cd_defs

	xref	thp_ostr
	xref	thp_2olw

	include 'dev8_keys_thg'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'
	include 'dev8_mac_thg'

cd_rword dc.w thp.ret+thp.uwrd
cd_null  dc.w 0

cd_rlong dc.w thp.ret+thp.ulng
	 dc.w 0

cd_glrl  dc.w thp.ulng
	 dc.w thp.ret+thp.ulng
	 dc.w 0

cd_gwrl  dc.w thp.uwrd
	 dc.w thp.ret+thp.ulng
	 dc.w 0

cd_glrw  dc.w thp.ulng
	 dc.w thp.ret+thp.uwrd
	 dc.w 0

cd_defs  dc.l th_name+2+16
	 dc.l cd_thing-*
	 dc.l '1.00'

;+++
; ASCI Thing NAME
;---
cd_tname dc.b 0,16,'CD-Audio Control'

;+++
; This is the Thing with the CD-Audio extensions
;---
cd_thing

cdt.reg reg	d1/d2/a0/a1

;+++
; CD_INIT drivername (ignored nowadays)
;---
cd_init thg_extn {INIT},cd_play,thp_ostr
	movem.l cdt.reg,-(sp)
	movea.l #0,a0
	dc.w	qpc.cdini
	movem.l (sp)+,cdt.reg
	rts

;+++
; CD_PLAY start,end
;---
cd_play thg_extn {PLAY},cd_stop,thp_2olw
	movem.l cdt.reg,-(sp)
	moveq	#1,d1		; Default: start from track 1
	move.l	(a1),d0
	or.l	4(a1),d0
	beq.s	qcd_playfrom	; Just start playing

	move.l	(a1),d1
	bsr.s	qcd_testtracknum
	bne.s	qcd_error
	move.l	4(a1),d0
	beq.s	qcd_playfrom	; only start given

	move.l	d0,d1
	bsr.s	qcd_testtracknum
	bne.s	qcd_error
	bsr.s	qcd_getsectorcount	; endsector in hsg format in d2
	bne.s	qcd_error
	move.l	(a1),d1
	dc.w	qpc.cdtst+1	; track start
	bne.s	qcd_error
	move.l	d1,d3
	dc.w	qpc.cdr2h+3	; redbook to hsg
	sub.l	d3,d2		; sectors from start till end
	and.l	#$7FFFFFFF,d1
	bsr.s	qcd_testforstop
	dc.w	qpc.cdply+1	; d1 = start (redbook) / d2 = sectors (hsg)
	movem.l (sp)+,cdt.reg
	rts

qcd_playfrom
	dc.w	qpc.cdinf+2	; get end of cd in d2
	bne.s	qcd_error
	dc.w	qpc.cdtst+1	; trackstart
	bne.s	qcd_error
	move.l	d1,d3
	dc.w	qpc.cdr2h+2
	dc.w	qpc.cdr2h+3
	sub.l	d3,d2
	and.l	#$7FFFFFFF,d1
	bsr.s	qcd_testforstop
	dc.w	qpc.cdply+1
qcd_error
	movem.l (sp)+,cdt.reg
	rts

qcd_getsectorcount
	move.l	d3,-(a7)
	move.l	d1,d2
	dc.w	qpc.cdtst+2
	bne.s	qcd_errord3
	dc.w	qpc.cdr2h+2	; redbook to hsg
	move.l	d1,d3
	bmi.s	qcd_endisdirect
	dc.w	qpc.cdtln+3	; get track length
	bne.s	qcd_errord3
	add.l	d3,d2
qcd_endisdirect
	moveq	#0,d0
qcd_errord3
	move.l	(a7)+,d3
	tst.l	d0
	rts

qcd_testtracknum
	movem.l d2-d3,-(a7)
	btst	#31,d1
	bne.s	qcd_testok
	dc.w	qpc.cdinf+2
	bne.s	qcd_testerror2
	cmp.b	d3,d1
	bcs.s	qcd_testerror
	lsr.w	#8,d3
	cmp.b	d3,d1
	bhi.s	qcd_testerror
qcd_testok
	movem.l (a7)+,d2-d3
	moveq	#0,d0
	rts
qcd_testerror
	movem.l (a7)+,d2-d3
	moveq	#-1,d0
	rts
qcd_testerror2
	movem.l (a7)+,d2-d3
	rts

qcd_testforstop
	dc.w	qpc.cdsti+4
	btst	#0,d4
	beq.s	qcd_isnotplaying
	dc.w	qpc.cdstp
qcd_isnotplaying
	rts

;+++
; CD_STOP
;---
cd_stop thg_extn {STOP},cd_resume,cd_null
	dc.w	qpc.cdstp
	moveq	#0,d0
	rts

;+++
; CD_RESUME
;---
cd_resume thg_extn {RSME},cd_eject,cd_null
	dc.w	qpc.cdrsm
	rts

;+++
; CD_EJECT
;---
cd_eject thg_extn {EJCT},cd_close,cd_null
	dc.w	qpc.cdejc
	rts

;+++
; CD_CLOSE
;---
cd_close thg_extn {CLSE},cd_isplaying,cd_null
	dc.w	qpc.cdlad
	rts

;+++
; CD_ISPLAYING
;---
cd_isplaying thg_extn {IPLA},cd_isclosed,cd_rword
	movem.l cdt.reg,-(sp)
	moveq	#0,d1
	dc.w	qpc.cdsti+2
	bne	qcd_error
	btst	#0,d2
	beq.s	qcd_isp
	addq.b	#1,d1
qcd_isp move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_ISCLOSED
;---
cd_isclosed thg_extn {ICLO},cd_isinserted,cd_rword
	movem.l cdt.reg,-(sp)
	moveq	#0,d1
	dc.w	qpc.cdsti+2
	bne	qcd_error
	btst	#1,d2
	beq.s	qcd_isc
	addq.b	#1,d1
qcd_isc move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_ISINSERTED
;---
cd_isinserted thg_extn {IINS},cd_ispaused,cd_rword
	movem.l cdt.reg,-(sp)
	moveq	#0,d1
	dc.w	qpc.cdsti+2
	bne	qcd_error
	btst	#2,d2
	beq.s	qcd_isi
	addq.b	#1,d1
qcd_isi move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_ISPAUSED
;---
cd_ispaused thg_extn {IPAU},cd_track,cd_rword
	movem.l cdt.reg,-(sp)
	moveq	#0,d1
	dc.w	qpc.cdsti+2
	bne	qcd_error
	btst	#3,d2
	beq.s	qcd_iss
	addq.b	#1,d1
qcd_iss move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_TRACK
;---
cd_track thg_extn {TRCK},cd_tracktime,cd_rword
	movem.l cdt.reg,-(sp)
	dc.w	qpc.cdpos+1
	bne	qcd_error
	swap	d1
	lsr.w	#8,d1
qcd_tr0 move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_TRACKTIME
;---
cd_tracktime thg_extn {TTIM},cd_alltime,cd_rlong
	movem.l cdt.reg,-(sp)
	dc.w	qpc.cdpos+1
	bne	qcd_error
	and.l	#$00FFFFFF,d1
	move.l	4(a1),a1
	move.l	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_ALLTIME
;---
cd_alltime thg_extn {ATIM},cd_hsg2red,cd_rlong
	movem.l cdt.reg,-(sp)
	dc.w	qpc.cdpos+1
	bne	qcd_error
	move.l	4(a1),a1
	move.l	d2,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_HSG2RED hsg
;---
cd_hsg2red thg_extn {H2R },cd_red2hsg,cd_glrl
	movem.l cdt.reg,-(sp)
	move.l	(a1),d1
	dc.w	qpc.cdh2r+1
	move.l	8(a1),a1
	move.l	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_RED2HSG red
;---
cd_red2hsg thg_extn {R2H },cd_trackstart,cd_glrl
	movem.l cdt.reg,-(sp)
	move.l	(a1),d1
	dc.w	qpc.cdr2h+1
	move.l	8(a1),a1
	move.l	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_TRACKSTART track
;---
cd_trackstart thg_extn {TSTA},cd_tracklength,cd_gwrl
	movem.l cdt.reg,-(sp)
	moveq	#0,d1
	move.w	2(a1),d1
	bsr	qcd_testtracknum
	bne	qcd_error
	dc.w	qpc.cdtst+1
	bne	qcd_error
	move.l	8(a1),a1
	move.l	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_TRACKLENGTH track
;---
cd_tracklength thg_extn {TLEN},cd_firsttrack,cd_gwrl
	movem.l cdt.reg,-(sp)
	moveq	#0,d1
	move.w	2(a1),d1
	bsr	qcd_testtracknum
	bne	qcd_error
	dc.w	qpc.cdtln+1
	bne	qcd_error
	move.l	8(a1),a1
	move.l	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_FIRSTTRACK
;---
cd_firsttrack thg_extn {FTRK},cd_lasttrack,cd_rword
	movem.l cdt.reg,-(sp)
	dc.w	qpc.cdinf+1
	bne	qcd_error
	moveq	#0,d1
	move.b	d2,d1
	move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_LASTTRACK
;---
cd_lasttrack thg_extn {LTRK},cd_length,cd_rword
	movem.l cdt.reg,-(sp)
	dc.w	qpc.cdinf+1
	bne	qcd_error
	moveq	#0,d1
	lsr.w	#8,d2
	move.b	d2,d1
	move.l	4(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_LENGTH
;---
cd_length thg_extn {LGTH},cd_hour,cd_rlong
	movem.l cdt.reg,-(sp)
	dc.w	qpc.cdinf+1
	bne	qcd_error
	move.l	4(a1),a1
	move.l	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_HOUR red
;---
cd_hour thg_extn {HOUR},cd_minute,cd_glrw
	movem.l cdt.reg,-(sp)
	move.l	(a1),d1
	swap	d1
	andi.l	#$ff,d1
	divu	#60,d1
	move.l	8(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_MINUTE red
;---
cd_minute thg_extn {MIN },cd_second,cd_glrw
	movem.l cdt.reg,-(sp)
	move.l	(a1),d1
	swap	d1
	andi.l	#$ff,d1
	divu	#60,d1
	swap	d1
	move.l	8(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

;+++
; CD_SECOND red
;---
cd_second thg_extn {SEC },,cd_glrw
	movem.l cdt.reg,-(sp)
	move.l	(a1),d1
	lsr.w	#8,d1
	andi.l	#$ff,d1
	move.l	8(a1),a1
	move.w	d1,(a1)
	movem.l (sp)+,cdt.reg
	moveq	#0,d0
	rts

	end
