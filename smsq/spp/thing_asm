; SER PAR PRT Thing  V2.12     1999  Tony Tebby
;
; 2003-09-24  2.11  prt_use$ bug fixed (wl)
; 2003-10-06  2.12  Some fixes for non-QPC I had sent TT 2 years ago (MK)


	section exten

	xdef	spp_thing
	xdef	spp_tnam

	xref	spp_cknm
	xref	iob_abort
	xref	iob_clear
	xref	thp_str
	xref	thp_ostr
	xref	thp_rstr

	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_serparprt'
	include 'dev8_mac_thg'
	include 'dev8_mac_assert'

spp_tnam dc.b	0,11,'Ser_Par_Prt',$a

ptp_chr  dc.w  thp.opt+thp.ulng  ; unsigned long
	 dc.w  thp.char 	 ; compulsory character
	 dc.w  0

ptp_3olw dc.w  thp.opt+thp.ulng+thp.nnul ; optional long word (second size)
ptp_2olw dc.w  thp.opt+thp.ulng+thp.nnul ; optional long word (size, time)
ptp_olw  dc.w  thp.opt+thp.ulng+thp.nnul ; optional long word (port number)
ptp_nul  dc.w  0

;+++
; This is the Thing with all the SER PAR PRT extensions
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;---
spp_thing
	page

par_use thg_extn PARU,par_buff,thp_ostr
	moveq	#0,d0
	bra.s	seru_do

ser_use thg_extn SERU,ser_flow,thp_ostr
paru.reg reg	a1/a4
	assert	spd_nser-spd_npar,spd_pars-spd_parp,spd_sers-spd_serp
	moveq	#spd_nser-spd_npar,d0

seru_do
	movem.l paru.reg,-(sp)
	move.l	spth_spd-spth_link(a2),a4 ; linkage
	add.l	d0,a4

	tst.w	spd_npar(a4)		 ; any PAR?
	beq.s	seru_ok 		 ; ... no

	bsr.s	par_unam
	blt.s	par_ser
	move.w	#$ff00,spd_parp(a4)	 ; set PAR
	bra.s	seru_ok
par_ser
	move.w	#$00ff,spd_parp(a4)	 ; set SER

seru_ok
	moveq	#0,d0
	movem.l (sp)+,paru.reg
	rts

par_unam
	tst.w	(a1)			 ; any parameter?
	beq.s	pun_rts 		 ; ... no
	move.l	4(a1),a1
	cmp.w	#3,(a1)+		 ; 3 characters?
	bne.s	paru_ip4
	move.l	#$dfdfdf00,d0
	and.l	(a1)+,d0
	cmp.l	#'SER'<<8,d0		 ; SER?
	beq.s	pun_ser
	cmp.l	#'PAR'<<8,d0		 ; PAR?
	bne.s	paru_ip4
	moveq	#1,d0			 ; PAR
	rts
pun_ser
	moveq	#-1,d0			 ; SER
pun_rts
	rts

paru_ip4
	addq.l	#4,sp
paru_ipar
	movem.l (sp)+,paru.reg

part_ipar
	moveq	#err.ipar,d0
	rts
	page

; For an optional port number followed by one or more numeric parameters
; there is a series of tests.
; If the first parameter is between 1 and 16, it is assumed to be a port
; number unless the next parameter is unset (<0) in which case the part is
; assumed to be port number 1
; for this code to work, all parameters must be unsigned must have negative null.
; If all cases, A1 is returned to the first parameter after the port number.
;
ser_port
	move.l	(a1)+,d0		 ; port number set?
	bgt.s	spt_cport		 ; yes
	bra.s	spt_nport

ser_oport
	move.l	(a1)+,d0		 ; port number unset?
	ble.s	spt_nport		 ; yes or not a port
	cmp.l	#16,d0
	bhi.s	spt_nport		 ; > 16?
	tst.l	(a1)			 ; next parameter set?
	bpl.s	spt_cport		 ; ... yes, check port number

spt_nport
	subq.l	#4,a1			 ; backspace parameter pointer
	moveq	#1,d0			 ; port 1

spt_cport
	move.l	spth_spd-spth_link(a2),a3 ; linkage
	cmp.w	spd_nser(a3),d0 	 ; port in range?
	bhi.s	spt_bport		 ; ... no
	subq.w	#1,d0			 ; add 0 to 3
	mulu	#spd.len,d0		 ; offset
	move.l	spd_pser(a3),a3
	add.l	d0,a3
	tst.w	spd_port(a3)		 ; port set?
	ble.s	spt_bport
	rts

spt_bport
ppt_bport
	moveq	#err.fdnf,d0
	rts

; For an optional port number followed by one or more numeric parameters
; there is a series of tests.
; If the first parameter is between 1 and 16, it is assumed to be a port
; number unless the next parameter is unset (<0) in which case the part is
; assumed to be port number 1
; for this code to work, all parameters must be unsigned and negative null.
; In all cases, A1 is returned to the first parameter after the port number.
;
par_port
	move.l	(a1)+,d0		 ; port number set?
	bgt.s	ppt_cport		 ; yes
	bra.s	ppt_nport

par_oport
	move.l	(a1)+,d0		 ; port number unset?
	ble.s	ppt_nport		 ; yes, or not a port
	cmp.l	#16,d0
	bhi.s	ppt_nport		 ; > 16?
	tst.l	(a1)			 ; next parameter set?
	bpl.s	ppt_cport		 ; ... yes, check port number

ppt_nport
	subq.l	#4,a1			 ; backspace parameter pointer
	moveq	#1,d0			 ; port 1

ppt_cport
	move.l	spth_spd-spth_link(a2),a3 ; linkage
	cmp.w	spd_npar(a3),d0 	 ; port in range?
	bhi.s	ppt_bport		 ; ... no
	subq.w	#1,d0			 ; add 0 to 3
	mulu	#spd.len,d0		 ; offset
ppt_set
	move.l	spd_ppar(a3),a3
	add.l	d0,a3
	tst.w	spd_port(a3)		 ; port set?
	ble.s	ppt_bport
	rts

ser_flow thg_extn SERF,ser_buff,ptp_chr
serp.reg reg	d1/a1/a3
	movem.l serp.reg,-(sp)
	bsr	ser_port
	blt.s	serp_exit
	move.l	(sp),a1
	moveq	#$ffffffdf,d0
	and.l	4(a1),d0
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
	move.b	d0,spd_hand(a3) 	 ; set handshake control
serp_ok
	moveq	#0,d0
serp_exit
	movem.l (sp)+,serp.reg
	rts


	page
prt_use thg_extn PRTU,prt_use$,thp_str

prtu.reg reg	d4/d5/a0/a2/a3
prtu.frame equ	$0c
prtu_thg equ	prtu.frame+$0c
	movem.l prtu.reg,-(sp)
	sub.w	#prtu.frame,sp		 ; room for parameters

	move.l	4(a1),a0		 ; PRT name
	move.l	spth_spd-spth_link(a2),a3 ; linkage
	move.l	sp,a2
	jsr	spp_cknm		 ; check name
	move.l	prtu_thg(sp),a2
	bne.s	prtu_exit		 ; ... oops

	tst.l	d4			 ; PRT itself?
	bmi.s	prtu_inam		 ; ... yes
	tst.b	d5			 ; SRX
	bgt.s	prtu_inam		 ; ... yes

	move.l	spd_pprt(a3),a2 	 ; prt definition
	assert	spd_pname,spd_prtd-$c,0
	move.l	(a0)+,(a2)+
	move.l	(a0)+,(a2)+
	move.l	(a0)+,(a2)+
	move.l	a3,(a2)+
	moveq	#0,d0

prtu_exit
	add.w	#prtu.frame,sp
	movem.l (sp)+,prtu.reg
	rts
prtu_inam
	moveq	#err.inam,d0
	bra.s	prtu_exit

prt_use$ thg_extn PRT$,prt_buff,thp_rstr

	move.l	a0,-(sp)
	move.l	4(a1),a1
	move.l	spth_spd-spth_link(a2),a0  *** modified in 2.11 - use correct offset ***
	move.l	spd_pprt(a0),a0
	assert	spd_pname,0
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

prt_buff thg_extn PRTB,prt_clear,ptp_2olw
	movem.l serp.reg,-(sp)
	bsr.s	prt_port
	bge.s	ser_bfcheck
	bra.l	serp_exit

prt_port		; determine PRT port
	move.l	spth_spd-spth_link(a2),a3 ; linkage
	move.l	spd_pprt(a3),a3
	tst.l	spd_prtd(a3)
	beq.s	prt_nprt
	move.l	spd_prtd(a3),a3
	rts
prt_nprt
	moveq	#err.fdnf,d0
	rts


par_buff thg_extn PARB,par_clear,ptp_3olw
parb_out
	movem.l serp.reg,-(sp)
	bsr	par_oport
	bra.s	ser_bfcheck

ser_buff thg_extn SERB,ser_room,ptp_3olw
	movem.l serp.reg,-(sp)
	bsr	ser_oport
ser_bfcheck
	blt.l	serp_exit

	move.l	4(a1),d0		 ; any input buffer length?
	ble.s	serb_out
	moveq	#0,d1
	move.w	spd_minbf(a3),d1	 ; any minimum?
	beq.s	serb_in
	cmp.l	d1,d0			 ; new is big enough?
	bge.s	serb_in 		 ; ... yes
	move.l	d1,d0			 ; ... no, use minimum
serb_in
	move.l	d0,spd_ilen(a3) 	 ; set input buffer length
	asr.l	#2,d0			 ; room should be 1/8
	addq.l	#1,d0			 ; +1
	move.l	d0,spd_room(a3) 	 ; ... set room

serb_out
	move.l	(a1),d0
	bge.s	serb_oset
	moveq	#0,d0
serb_oset
	move.l	d0,spd_olen(a3) 	 ; serial output buffer
serb_ok
	bra.l	serp_ok

ser_room thg_extn SERR,ser_eof,ptp_2olw
	movem.l serp.reg,-(sp)
	bsr	ser_oport
	blt	serp_exit

	move.l	(a1),d0
	ble.s	serb_ok
	move.l	d0,spd_room(a3) 	 ; set room
	add.l	d0,d0			 ; ensure buffer more than 2x
	cmp.l	spd_ilen(a3),d0
	blt.s	serb_ok
	move.l	d0,spd_ilen(a3) 	 ; increase buffer
	bra.s	serb_ok

ser_eof thg_extn SERE,ser_clear,ptp_2olw

	movem.l serp.reg,-(sp)
	bsr	ser_oport
	blt	serp_exit

	tst.l	spd_cdchk(a3)		 ; cd checking routine?
	beq.s	serb_ok
	move.l	(a1),d0
	move.w	d0,spd_cdef(a3) 	 ; set eof timer
;	 move.w  d0,spd_cdct(a3)
	bra.s	serb_ok

	page
prt_clear thg_extn PRTC,prt_abt,ptp_nul

	moveq	#0,d0
	bra.s	prtc_chk

prt_abt thg_extn PRTA,ser_pxt,ptp_nul

	moveq	#-1,d0
prtc_chk
prtc.reg reg	d0/d7/a0/a1/a2/a3
	movem.l prtc.reg,-(sp)
	bsr	prt_port
	bge.l	prtc_do
	bra.l	prtc_exit

par_clear thg_extn PARC,par_abt,ptp_olw

	moveq	#0,d0
	bra.s	parc_do

par_abt thg_extn PARA,par_pulse,ptp_olw

	moveq	#-1,d0
parc_do
	movem.l prtc.reg,-(sp)
	bsr	par_port
	blt.s	prtc_exit
	bra.s	prtc_do

ser_clear thg_extn SERC,ser_abt,ptp_olw

	moveq	#0,d0
	bra.s	serc_do

ser_abt thg_extn SERA,prt_use,ptp_olw

	moveq	#-1,d0
serc_do
	movem.l prtc.reg,-(sp)

	bsr	ser_port
	blt.s	prtc_exit

prtc_do
	move.l	(sp),d0
	move.w	sr,d7
	trap	#0			 ; supervisor mode for this
	lea	spd_obuf(a3),a2 	 ; pointer to pointer to output buffer
	tst.l	d0
	bge.s	prtc_clear
	jsr	iob_abort		 ; abort
	bra.s	prtc_restart
prtc_clear
	jsr	iob_clear		 ; clear
prtc_restart
	tst.l	(a2)			 ; ... anything to go?
	beq.s	prtc_rte		 ; ... no
	move.l	spd_oopr(a3),d0 	 ; any output operation?
	beq.s	prtc_rte
	move.l	d0,a0
	jsr	(a0)			 ; ... yes, do it
	moveq	#0,d0

prtc_rte
	move.w	d7,sr
	tst.l	d0
prtc_exit
	move.l	d0,(sp) 		 ; set return d0
	movem.l (sp)+,prtc.reg
	rts
	page

par_pulse thg_extn PARP,par_wait,ptp_2olw
max.puls equ	200
	movem.l serp.reg,-(sp)
	moveq	#spd_puls-$70,d1
parp_do
	bsr	par_oport
	blt	serp_exit
	move.w	2(a1),d0		 ; value
	bge.s	parp_max
	moveq	#0,d0
parp_max
	cmp.w	#max.puls,d0		 ; greater than max?
	ble.s	parp_set		 ; ... no
	move.w	#max.puls,d0
parp_set
	move.w	d0,$70(a3,d1.w) 	 ; set pulse / wait count
	bra	serp_ok 		 ; OK

par_wait thg_extn PARW,ser_use,ptp_2olw
	movem.l serp.reg,-(sp)
	moveq	#spd_wait-$70,d1
	bra	parp_do

ser_pxt thg_extn SERP,,ptp_2olw
	movem.l serp.reg,-(sp)
	bsr	ser_oport
	blt	serp_exit

	move.w	2(a1),spd_stxp(a3)	 ; set xmit pause
	bra	serb_ok


	end
