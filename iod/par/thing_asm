; SER PAR PRT Thing  V2.01     1989  Tony Tebby  QJUMP

	section exten

	xdef	par_thing
	xdef	par_tnam

	xref	par_cknm
	xref	iob_abort
	xref	iob_clear
	xref	thp_chr
	xref	thp_str
	xref	thp_ostr
	xref	thp_rstr
	xref	thp_wd
	xref	thp_owd
	xref	thp_2owd
	xref	thp_lw
	xref	thp_olw
	xref	thp_2olw
	xref	thp_3olw
	xref	thp_nul

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_par'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'

par_tnam dc.b	0,11,'Ser_Par_Prt',$a

ptp_chr  dc.w  thp.opt+thp.ulng  ; unsigned long
	 dc.w  thp.char 	 ; compulsory character
	 dc.w  0

;+++
; This is the Thing with all the SER PAR PRT extensions
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;---
par_thing
	page

par_use thg_extn PARU,par_buff,thp_ostr

	tst.b	prd_parx-prd_thgl(a2)	 ; any PAR?
	beq.s	seru_ok 		 ; ... no
	bsr.s	par_unam
	blt.s	par_ser
	sf	prd_sere-prd_thgl(a2)	 ; SER disabled for PAR
	st	prd_sere+prd_ser1-prd_thgl(a2) ; and enabled for SER
	bra.s	seru_ok
par_ser
	st	prd_sere-prd_thgl(a2)	 ; SER enabled for PAR
	sf	prd_sere+prd_ser1-prd_thgl(a2) ; and disabled for SER
	bra.s	seru_ok

ser_use thg_extn SERU,ser_flow,thp_ostr

	tst.b	prd_serx+prd_ser1-prd_thgl(a2) ; any SER?
	beq.s	seru_ok 		 ; ... no
	bsr.s	par_unam
	ble.s	ser_ser
	sf	prd_pare-prd_thgl(a2)  ; PAR disabled for PAR
	st	prd_pare+prd_ser1-prd_thgl(a2) ; and enabled for SER
	bra.s	seru_ok
ser_ser
	st	prd_pare-prd_thgl(a2)  ; PAR enabled for PAR
	sf	prd_pare+prd_ser1-prd_thgl(a2) ; and disabled for SER
seru_ok
	moveq	#0,d0
	rts

par_unam
	tst.w	(a1)			 ; any parameter?
	beq.s	pun_rts 		 ; ... no
	move.l	4(a1),a1
	cmp.w	#3,(a1)+		 ; 3 characters?
	bne.s	part_ip4
	move.l	#$dfdfdf00,d0
	and.l	(a1)+,d0
	cmp.l	#'SER'<<8,d0		 ; SER?
	beq.s	pun_ser
	cmp.l	#'PAR'<<8,d0		 ; PAR?
	bne.s	part_ip4
	moveq	#1,d0			 ; PAR
	rts
pun_ser
	moveq	#-1,d0			 ; SER
pun_rts
	rts

part_ip4
	addq.l	#4,sp
part_ipar
	moveq	#err.ipar,d0
	rts
	page

ser_port
	move.w	#prd_ser1-prd_thgl,d3	 ; assume ser1
	move.l	(a1)+,d0		 ; unset?
	beq.s	spt_rts 		 ; yes
spt_check
	subq.l	#4,d0			 ; port 1 to 4?
	bhi.s	spt_nport		 ; ... no
	addq.l	#3,d0			 ; add 0 to 3
	mulu	#ser_inc,d0		 ; port number
	add.w	d0,d3
spt_rts
	rts
spt_nport
	subq.l	#4,a1			 ; restore parameter pointer
	rts


ser_flow thg_extn SERF,ser_buff,ptp_chr
serp.reg reg	d3/a1
	movem.l serp.reg,-(sp)
	bsr.s	ser_port
	moveq	#$ffffffdf,d0
	and.l  (a1),d0
	sub.b	#'I',d0 		 ; ignore = 0?
	beq.s	serf_set
	cmp.b	#'H'-'I',d0		 ; hardware = -1?
	beq.s	serf_set
	cmp.b	#'X'-'I',d0		 ; xon/xoff?
	beq.s	serf_x
	moveq	#err.ipar,d0
	bra.s	serp_exit
serf_x
	moveq	#1,d0
serf_set
	move.b	d0,prd_hand(a2,d3.w)	 ; set handshake control
serp_ok
	moveq	#0,d0
serp_exit
	movem.l (sp)+,serp.reg
	rts


	page
prt_use thg_extn PRTU,prt_use$,thp_str

prtu.reg reg	d4/d5/a0/a2/a3
prtu_thg equ	$0c
	movem.l prtu.reg,-(sp)
	move.l	4(a1),a0		 ; PRT name
	sub.w	#$c,sp			 ; room for parameters
	lea	prd_prtl-prd_thgl(a2),a3 ; dummy linkage
	move.l	sp,a2
	jsr	par_cknm		 ; check name
	move.l	sp,a3
	add.w	#$c,sp
	move.l	prtu_thg(sp),a2
	bne.s	prtu_exit		 ; ... oops
	tst.b	d5			 ; output permitted?
	bgt.s	prtu_inam		 ; ... no
	tst.w	d4			 ; PRT itself?
	bmi.s	prtu_inam		 ; ... yes
	bgt.s	prtu_ser

	tst.b	prd_parx-prd_thgl(a2)	 ; any par device?
	beq.s	prtu_inam		 ; ... no

	neg.w	(a3)			 ; negate port number
	move.w	#prd_prtd-prd_prt-prd_parl,prd_prt+prd_parl-prd_thgl(a2) ; par
	clr.w	prd_prt+prd_ser1-prd_thgl(a2) ; and clear ser
	bra.s	prtu_set
prtu_ser
	move.w	(a3),d0
	mulu	#ser_inc,d0
	add.w	#prd_ser1-ser_inc-prd_thgl,d0
	tst.b	prd_serx(a2,d0.w)	 ; this ser device exists?
	beq.s	prtu_inam		 ; ... no

	move.w	#prd_prtd-prd_prt-prd_ser1,prd_prt+prd_ser1-prd_thgl(a2); ser
	clr.w	prd_prt+prd_parl-prd_thgl(a2) ; and clear par

prtu_set
	move.b	1(a3),prd_prtl+prd_prtp-prd_thgl(a2) ; port number
	lea	prd_prtd-prd_thgl(a2),a2  ; set name
	move.l	(a0)+,(a2)+
	move.l	(a0)+,(a2)+
	move.l	(a0)+,(a2)+
	move.l	(a0)+,(a2)+
	moveq	#0,d0
prtu_exit
	movem.l (sp)+,prtu.reg
	rts
prtu_inam
	moveq	#err.inam,d0
	bra.s	prtu_exit

prt_use$ thg_extn PRT$,prt_buff,thp_rstr

	move.l	a0,-(sp)
	move.l	4(a1),a1
	lea	prd_prtd-prd_thgl(a2),a0
	move.w	(a0)+,d0		 ; length of name
	move.w	d0,(a1)+

prt$_loop
	move.w	(a0)+,(a1)+
prt$_lend
	subq.w	#2,d0
	bgt.s	prt$_loop

	moveq	#0,d0
	move.l	(sp)+,a0
	rts
	page

prt_buff thg_extn PRTB,prt_clear,thp_olw
	movem.l serp.reg,-(sp)
	bsr.s	prt_port
	bge.s	serb_out
	bra.l	serp_exit

prt_port		; determine PRT port
	move.w	#prd_parl-prd_thgl,d3
	tst.w	prd_prt-prd_thgl(a2)	 ; PRT is PAR?
	bne.s	prt_par 		 ; ... yes
	tst.w	prd_prt+prd_ser1-prd_thgl(a2) ; PRT is SER?
	beq.s	prt_nprt		 ; ... not set

	moveq	#0,d0
	move.b	prd_prtp+prd_prtl-prd_thgl(a2),d0 ; SER port
	mulu	#ser_inc,d0		 ; ser offset
	add.w	d0,d3			 ; ... from thing linkage
	tst.l	d0
	rts
prt_par
	moveq	#0,d0			 ; no offset
	rts
prt_nprt
	moveq	#err.fdnf,d0
	rts


par_buff thg_extn PARB,par_clear,thp_olw
parb_out
	move.l	(a1),prd_olen-prd_thgl(a2) ; parallel output buffer
	moveq	#0,d0
	rts

ser_buff thg_extn SERB,ser_room,thp_3olw
	movem.l serp.reg,-(sp)
	bsr	ser_port
	move.l	4(a1),d0		 ; any input buffer length?
	ble.s	serb_out
	move.l	d0,prd_ilen(a2,d3.w)	 ; set input buffer length
	asr.l	#2,d0			 ; room should be 1/8
	addq.l	#1,d0			 ; +1
	move.l	d0,prd_room(a2,d3.w)	 ; ... set room

serb_out
	move.l	(a1),prd_olen(a2,d3.w)	 ; serial output buffer
serb_ok
	bra.l	serp_ok

ser_room thg_extn SERR,ser_eof,thp_2olw
	movem.l serp.reg,-(sp)
	bsr	ser_port
	move.l	(a1),d0
	move.l	d0,prd_room(a2,d3.w)	 ; set room
	add.l	d0,d0			 ; ensure buffer more than 2x
	cmp.l	prd_ilen(a2,d3.w),d0
	blt.s	serb_ok
	move.l	d0,prd_ilen(a2,d3.w)	; increase buffer
	bra.s	serb_ok

ser_eof thg_extn SERE,ser_clear,thp_2owd

	movem.l serp.reg,-(sp)
	bsr	ser_port
	move.l	(a1),d0
	add.w	#prd_cdef,d3
	move.w	d0,(a2,d3.w)		 ; set eof timer
	bra.s	serb_ok

	page
prt_clear thg_extn PRTC,prt_abt,thp_nul

	moveq	#0,d0
	bra.s	prtc_chk

prt_abt thg_extn PRTA,ser_pxt,thp_nul

	moveq	#-1,d0
prtc_chk
prtc.reg reg	d3/d7/a0/a1/a2/a3
	movem.l prtc.reg,-(sp)
	move.l	d0,d3			 ; save flag
	bsr	prt_port
	bge.l	prtc_do
	bra.l	prtc_exit

par_clear thg_extn PARC,par_abt,thp_nul

	moveq	#0,d0
	bra.s	parc_do

par_abt thg_extn PARA,par_pulse,thp_nul

	moveq	#-1,d0
parc_do
	movem.l prtc.reg,-(sp)
	move.l	d0,d3
	move.w	#prd_parl-prd_thgl,d3
	bra.s	prtc_do

ser_clear thg_extn SERC,ser_abt,thp_owd

	moveq	#0,d0
	bra.s	serc_do

ser_abt thg_extn SERA,prt_use,thp_owd

	moveq	#-1,d0
serc_do
	movem.l prtc.reg,-(sp)
	move.l	d0,d3
	bsr	ser_port

prtc_do
	move.w	sr,d7
	trap	#0			 ; supervisor mode for this
	lea	(a2,d3.w),a3		 ; device driver linkage
	lea	prd_obuf(a3),a2 	 ; pointer to pointer to output buffer
	tst.l	d3
	bge.s	prtc_clear
	jsr	iob_abort		 ; abort
	bra.s	prtc_restart
prtc_clear
	jsr	iob_clear		 ; clear
prtc_restart
	tst.l	(a2)			 ; ... anything to go?
	beq.s	prtc_exit		 ; ... no
	move.l	prd_oopr(a3),d0 	 ; any output operation?
	beq.s	prtc_exit
	move.l	d0,a0
	jsr	(a0)			 ; ... yes, do it
	moveq	#0,d0

prtc_exit
	move.w	d7,sr
	tst.l	d0
	movem.l (sp)+,prtc.reg
	rts
	page

par_pulse thg_extn PARP,ser_use,thp_wd
max.puls equ	2000
min.puls equ	5
sft.puls equ	1
	moveq	#-min.puls,d0		 ; 0=5 microseconds
	add.w	(a1)+,d0		 ; value
	bge.s	parp_max
	moveq	#0,d0
parp_max
	cmp.w	#max.puls,d0		 ; greater than max?
	ble.s	parp_set		 ; ... no
	move.w	#max.puls,d0
parp_set
	lsr.w	#sft.puls,d0		 ; adjust for clock rate / cycles
	move.w	d0,prd_puls-prd_thgl(a2) ; set count
parp_ok
	moveq	#0,d0			 ; OK
	rts

ser_pxt thg_extn SERP,,thp_2owd
	movem.l serp.reg,-(sp)
	bsr	ser_port
	add.w	#prd_stxp,d3
	move.w	2(a1),(a2,d3.w) 	  ; set xmit pause
	bra	serb_ok

	end
