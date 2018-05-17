; Gold Microdrive routines

	section patch

	xdef	gl_mdvf
	xdef	gl_mdvsh
	xdef	gl_mdvrd
	xdef	gl_mdvvr
	xdef	gl_mdvwr
	xdef	gl_mdvsl
	xdef	gl_mdvds

	xdef	gl_mdvtm

	include 'dev8_sys_gold_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_assert'

pc_tctrl equ	$18002
pc_mctrl equ	$18020
pc..txfl equ	  1
pc..rxrd equ	  2
pc..gap  equ	  3
pc_tdata equ	$18022
pc_trak1 equ	$18022
pc_trak2 equ	$18023
pc.read  equ	   %0010
pc.erase equ	   %1010
pc.writ  equ	   %1110

sv_tmode equ	$a0
sv_timo  equ	$a6
sv_mdrun equ	$ee

gmf_shdr equ	$00		 ; sector header
gmf.shdr equ	  $0e
gmf_lsct equ	$0e		 ; start of long (format) sector
gmf.lsct equ	  $262		 ; long sector
gmf_sect equ	$1a		 ; start of standard sector
gmf.sect equ	  $200
gmf_map  equ	$270

gmf_dirs equ	$470		 ; directory first sector

gmf.work equ	$670

gl_mdvtm
	 dc.w  (gl_mdvte-gl_mdvtm)/4
gmt_timo dc.w  60000,17543  ; 20 ms   subq.l; bne.s  serial port timout
gmt_fgap dc.w  11062,4267   ; 2800 us dbra	     format gap
gmt_gap  dc.w  14277,5455   ; 3580 us dbra	     write gap
gmt_wgap dc.w	 839,361    ; .5 sec		     wait for end gap
gmt_ngap dc.w	 129,42     ; 100 us		     test no gap
gmt_sync dc.w	  99,31     ; 20 us   dbra	     sync pause
gmt_rdly dc.w	  29,6	    ;			     read delay
gmt_wdat dc.w	1068,624    ; 1ms     test; dbra     wait for data to start
gmt_wbyt dc.w	  63,61     ; 100 us  test; dbra     wait for next byte
gmt_wrof dc.w	 504,191    ; 120 us  dbra	     time before write off
gmt_rnup dc.w	3000,889    ; 1 sec		     runup
gmt_selc dc.w	  45,25     ;			     select loop wait
gmt_scal dc.w	 500,500    ;			     scaling for runup
gmt_wron dc.w	  30,1	    ;			     wait after write on
gl_mdvte

;+++
; Format Microdrive
;---
gl_mdvf
	tst.b	sv_mdrun(a6)	 ; drive running?
	beq.s	gmf_do
	moveq	#err.fdiu,d0
gmf_rts1
	rts

gmf_do
	move.l	d1,d7
	move.l	a1,a4
	move.l	#gmf.work,d1
	moveq	#0,d2
	moveq	#sms.achp,d0
	trap	#1
	tst.l	d0
	bne.s	gmf_rts1

	move.l	a0,a5		 ; sector buffer
	subq.w	#1,(a0)+	 ; flag $ff, sector number $ff

	moveq	#9,d2		 ; max name length - 1
	move.w	(a4)+,d0
	addq.l	#5,a4
	subq.w	#5,d0
gmf_mnlp
	moveq	#' ',d1
	subq.w	#1,d0
	blt.s	gmf_mnset
	move.b	(a4)+,d1
gmf_mnset
	move.b	d1,(a0)+
	dbra	d2,gmf_mnlp

	move.w	sys_rand(a6),(a0)+	 ; Random number

	move.l	#$fd000c10,(a0)+	 ; good sector / checksum

	addq.l	#6,a0			 ; six bytes idle

	subq.w	#1,(a0)+		 ; + two bytes sync

	move.w	#(gmf.lsct-4-6-2)/2-1,d1 ; the rest of the long sector
gmf_fvlp
	move.w	#$aa55,(a0)+
	dbra	d1,gmf_fvlp

	move.w	#$0f0e,gmf_sect+gmf.sect(a5) ; set standard sector checksum

	lea	pc_mctrl,a3	 ; always need this
	jsr	gl_mdvmd

	or.w	#$0700,sr	 ; no interrupts

	move.l	d7,d1
	jsr	gl_mdvsl	 ; select d1

	move.w	gmt_rnup,d0
	mulu	gmt_scal,d0
gmf_runup
	subq.l	#1,d0			 ; 08
	bgt.s	gmf_runup		 ; 10  18 cycles at 16 Mhz = 1.1us

	clr.l	-(sp)
	move.b	#pc.erase,(a3)		 ; erase on

gmf_wloop
	  move.l  a5,a1
	  moveq   #gmf.shdr-1,d1
	move.w	gmt_fgap,d0		 ; 2800 us
	dbra	d0,*

	jsr	gmw_fmts		 ; format sector header

	move.w	#gmf.lsct-1,d1
	move.w	gmt_fgap,d0		 ; 2800 us
	dbra	d0,*

	jsr	gmw_fmts		 ; format sector

	subq.b	#1,1(a5)		 ; next sector
	bcc.s	gmf_wloop

;	 move.w  gmt_fgap,d0		  ; 2800 us
;	 dbra	 d0,*
	move.b	#pc.read,(a3)

; now verify all sectors

	moveq	#0,d5
gmf_ploop
	move.w	#$00ff,d5

gmf_vloop
	move.l	a5,a1			 ; sector header buffer
	jsr	gl_mdvsh		 ; next sector header
	bra.s	gmf_fmtf1		 ; ... very bad
	bra.s	gmf_vlend		 ; ... just bad
	jsr	gmv_fmts		 ; verify format sector
	bra.s	gmf_vlend
	add.w	d7,d7			 ; sector*2
	assert	gmf_shdr+gmf.shdr+gmf.lsct,gmf_map
	subq.b	#1,(a1,d7.w)		 ;
	tst.w	d7			 ; was it sector zero?
	beq.s	gmf_vagain
gmf_vlend
	dbra	d5,gmf_vloop
gmf_fmtf1
	bra.s	gmf_fmtf		 ; format failed

gmf_vagain
	bset	#31,d5			 ; try again?
	beq.s	gmf_ploop		 ; ... yes

gmf_mloop
	subq.b	#1,(a1) 		 ; next sector
	cmp.b	#$fe,(a1)		 ; bad?
	bgt.s	gmf_mnext		 ; ... yes
	beq.s	gmf_mset		 ; ... fairly
	addq.w	#1,(sp) 		 ; good, count it
gmf_mset
	move.b	d7,3(sp)		 ; highest sector
	move.b	(a1),d4 		 ; and flag
	move.l	a1,a4
gmf_mnext
	addq.l	#2,a1
	addq.b	#1,d7
	bcc.s	gmf_mloop
	st	(a4)			 ; blat top most
	addq.b	#2,d4			 ; top most was good?
	beq.s	gmf_mchk		 ; ... no
	subq.w	#1,(sp) 		 ; one fewer
gmf_mchk
	cmp.w	#200,(sp)		 ; rotten?
	blt.s	gmf_fmtf

	lea	gmf_map(a5),a0
	move.b	#$f8,(a0)		 ; map sector (fd->f8)

	move.l	(sp),d2
	subq.w	#8,d2			 ; guess for dir
	add.w	d2,d2
gmf_aldir
	subq.w	#2,d2
	cmpi.b	#$fd,(a0,d2.w)		 ; good?
	bne.s	gmf_aldir

gmf_wdir
	clr.b	(a0,d2.w)		 ; mark directory
	move.w	d2,$1fe(a0)
	lsr.w	#1,d2			 ; directory sector
	swap	d2
	clr.w	d2
	bsr.s	gmf_wsec		 ; write map
	bra.s	gmf_fmtf

	swap	d2
	lea	gmf_dirs(a5),a0
	move.l	#$40,(a0)
	bsr.s	gmf_wsec		 ; write directory
	bra.s	gmf_fmtf

	moveq	#0,d7			 ; no errors
	bra.s	gmf_exit
gmf_fmtf
	moveq	#err.fmtf,d7		 ; format failed
gmf_exit
	jsr	gl_mdvds		 ; deselect MDV
	move.l	a5,a0			 ; heap
	moveq	#sms.rchp,d0
	trap	#1
	jsr	gl_stdmd		 ; standard transmit mode
	and.w	#$f8ff,sr
	move.w	(sp)+,d1
	move.w	(sp)+,d2		 ; return params
	move.l	d7,d0			 ; and error
	rts

;*******************************************************

gmf_wsec
	moveq	#0,d5			 ; sector counter
gmf_lsec
	move.l	a5,a1
	jsr	gl_mdvsh		 ; read sector
	rts				 ; ... terrible
	bra.s	gmf_lsec		 ; ... bad
	cmp.b	d7,d2
	beq.s	gmf_wsdo		 ; ... ok
	addq.b	#1,d5			 ; ... wrong
	bcc.s	gmf_lsec
	rts

gmf_wsdo
	addq.l	#2,(sp)
	move.l	a0,a1			 ; sector to write
	move.w	(a1),-(sp)		 ; block is same as first word!!
	jsr	gl_mdvwr		 ; write map or directory
	addq.l	#2,sp
	rts

; write format sector

gmw_fmts
	lea	gmw_ewf,a4
	bra.s	gmw_wsec

; setup / end of write

gmw_ewf
	moveq	#pc.erase,d4		 ; revert to erase
	bra.s	gmw_woff
gmw_swr
	move.l	a0,a1
	move.w	#$200-1,d1
	moveq	#5,d5			 ; short preamble
	lea	gmw_ewr,a4
	bra.s	gmw_do
gmw_ewr
	moveq	#pc.read,d4
gmw_woff
	move.w	gmt_wrof,d0		 ; write off time = 120us
	dbra	d0,*

	move.b	d4,(a3) 		 ; write off
	rts


; write sector

gl_mdvwr
	move.l	a1,a0
	lea	4(sp),a1		 ; header info
	move.b	#pc.erase,(a3)		 ; erase
	move.w	gmt_gap,d0		 ; for 3580 us
	dbra	d0,*

	moveq	#2-1,d1 		 ; file / sector
	lea	gmw_swr,a4
gmw_wsec
	moveq	#pc.writ,d0
	move.b	d0,(a3)
	move.b	d0,(a3)
	move.w	gmt_wron,d0
	subq.w	#1,d0
	bne.s	*-2
	moveq	#pc..txfl,d6
	lea	pc_tdata-pc_mctrl(a3),a2
	moveq	#9,d5			 ; long preamble

gmw_do
	moveq	#0,d4			 ; preamble bytes
gmw_ploop
	bsr.s	gmw_byte
	subq.b	#1,d5
	bge.s	gmw_ploop

	moveq	#-1,d4			 ; sync bytes
	bsr.s	gmw_byte
	bsr.s	gmw_byte

	move.w	#$0f0f,d3		 ; preset checksum
	moveq	#0,d4
gmw_bloop
	move.b	(a1)+,d4
	add.w	d4,d3
	bsr.s	gmw_byte
	dbra	d1,gmw_bloop
	move.w	d3,d4
	bsr.s	gmw_byte		 ; low byte of checksum
	lsr.w	#8,d4
	bsr.s	gmw_byte		 ; and high byte
	jmp	(a4)

gmw_byte
	btst	d6,(a3) 		 ; wait
	bne.s	gmw_byte		 ; until not full
	move.b	d4,(a2) 		 ; send byte
	rts

;+++
; read sector header
;---
gl_mdvsh
	jsr	gmr_wgap
	rts
	addq.l	#2,(sp) 		 ; there is a medium

	moveq	#gmf.shdr-1,d1
	bsr.l	gmr_rbytes		 ; read bytes
	rts
	cmp.b	#$ff,-gmf.shdr(a1)
	bne.s	grs_rts
	moveq	#0,d7
	move.b	-gmf.shdr+1(a1),d7	 ; sector number
	addq.l	#2,(sp) 		 ; good return
grs_rts
	rts
;+++
; read sector
;---
gl_mdvrd
	lea	gmr_rbytes,a0
	bra.s	gmr_rdvr

;+++
; verify sector
;---
gl_mdvvr
	lea	gmr_vbytes,a0
gmr_rdvr
	jsr	gmr_wgap
	rts
	move.l	a1,-(sp)
	clr.w	-(sp)			 ; odd info read
	move.l	sp,a1
	moveq	#2-1,d1 		 ; file/sector
	bsr.s	gmr_rbytes
	bra.s	gmr_exit

	move.b	#pc.read,d1		  ; set read mode
	move.b	d1,(a3)
	move.w	gmt_sync,d0		  ; pause 20 us
	dbra	d0,*
	move.b	d1,(a3) 		  ; and set again

	move.w	#$200-1,d1
	move.l	2(sp),a1
	jsr	(a0)
	bra.s	gmr_exit
	addq.l	#2,6(sp)		  ; all is OK
gmr_exit
	moveq	#0,d1
	moveq	#0,d2
	move.b	1(sp),d2
	move.b	(sp),d1
	addq.l	#6,sp
	rts

;+++
; verify format sector
;---
gmv_fmts
	jsr	gmr_wgap
	rts
	move.w	#gmf.lsct-1,d1
	bsr.l	gmr_vbytes
	rts
	addq.l	#2,(sp)
	rts


gmr_rbytes
	moveq	#0,d4
	bra.s	gmr_vrbyts
gmr_vbytes
	moveq	#-1,d4
	clr.w	d4
gmr_vrbyts
	move.w	gmt_wdat,d0		 ; and wait for data
	move.w	#$0f0f,d3		 ; checksum
	moveq	#pc..rxrd,d6		 ; bit to check
	lea	pc_trak1-pc_mctrl(a3),a2 ; other track
	lea	pc_trak2-pc_mctrl(a3),a4 ; other track
gmr_rbloop
	btst	d6,(a3) 		 ; next byte?
	dbne	d0,gmr_rbloop		 ; ... no   wait (25 cycle loop)
	beq.s	gmr_rts0
	move.b	gmt_rdly+1,d4		 ; get timer
	subq.b	#1,d4
	bne.s	*-2
	move.b	(a2),d4 		 ; get byte 8us later (83/108; 115/140)
	exg	a4,a2			 ; the other track
	move.w	gmt_wbyt,d0		 ; reset wait for byte timeout
	add.w	d4,d3
	tst.l	d4			 ; type of loop
	bmi.s	gmr_vbyte
	move.b	d4,(a1)+
	dbra	d1,gmr_rbloop
	bra.s	gmr_cksum
gmr_vbyte
	cmp.b	(a1)+,d4
	dbne	d1,gmr_rbloop
	bne.s	gmr_rts0
gmr_cksum
	btst	d6,(a3) 		 ; next byte?
	dbne	d0,gmr_cksum		 ; ... no   wait (25 cycle loop)
	beq.s	gmr_rts0
	move.b	gmt_rdly+1,d4		 ; get timer
	subq.b	#1,d4
	bne.s	*-2
	move.b	(a2),d4 		 ; get byte 8us later (83/108; 115/140)
	exg	a4,a2			 ; the other track
	move.w	gmt_wbyt,d0		 ; reset wait for byte timeout
	ror.w	#8,d4
	addq.w	#1,d1
	beq.s	gmr_cksum

	cmp.w	d4,d3			 ; check checksum
	bne.s	gmr_rts0
	addq.l	#2,(sp)
gmr_rts0
	rts
gmr_wgap
	move.w	gmt_wgap,d1		 ;   2*65536; 3*65536 = .4/.5 sec
	mulu	gmt_scal,d1
gmr_gap
	subq.l	#1,d1		   ;08	  ; looking for gap
	beq.s	gmr_rts1	   ;08	  ; ... oops
	btst	#pc..gap,(a3)	   ;15	  ; gap now?
	beq.s	gmr_gap 		  ; ... not yet

	move.w	gmt_wgap,d1		 ;   2*65536; 3*65536 = .4/.5 sec
	mulu	gmt_scal,d1
gmr_wait
	subq.l	#1,d1		   ;08	  ; in gap; looking for no gap
	beq.s	gmr_rts1	   ;08	  ; ... oops

	move.w	gmt_ngap,d0	   ;08	  ;  31; 42  no gap for at least 100 us
gmr_test
	btst	#pc..gap,(a3)	   ;15	     ; gap now?
	bne.s	gmr_wait	   ;10 ;12   ; 49 cycles gap = 4;3 us
	dbra	d0,gmr_test	       ;10   ; 37 cycles no gap = 3.1;2.3 us

	move.b	#pc.read,d1
	move.b	d1,(a3)
	move.w	gmt_sync,d0		  ; pause 20 us
	dbra	d0,*
	move.b	d1,(a3) 		  ; and set again
	addq.l	#2,(sp)
gmr_rts1
	rts

*****************************************************************************
; deselect / select Microdrive

gl_mdvds
	moveq	#8,d1
	bra.s	gms_step

gl_mdvsl
	move.b	#3,(a3) 		 ; clock and data
	bsr.s	gms_delay
	move.b	#1,(a3)

gms_loop
	subq.b	#1,d1
	ble.s	gms_rts
	bsr.s	gms_delay
gms_step
	move.b	#2,(a3) 		 ; clock and zero
	bsr.s	gms_delay
	sf	(a3)
	bra.s	gms_loop

gms_delay
	move.w	gmt_selc,d0		; about 13 microseconds
gms_dloop
	subq.w	#1,d0
	bgt.s	gms_dloop
gms_rts
	rts



; set Microdrive mode (Format Only)

gl_mdvmd
	move.w	sv_timo(a6),d0		 ; get RS232 timeout
	ble.s	gmm_set 		 ; ... none
	mulu	gmt_timo,d0	   13333*1.5; 17543*1.14  = n*20ms
gmm_wait
	subq.l	#1,d0		; 08
	bne.s	gmm_wait	; 10  18 cycles = 1.5; 1.14us

	clr.w	sv_timo(a6)

gmm_set
	moveq	#$10,d0 		 ; microdrive mode
	or.b	sv_tmode(a6),d0
	and.b	#$f7,d0
	move.b	d0,sv_tmode(a6)
  ;;;	and.b	#$7f,sv_pcint(a6)	 ; enable microdrive interrupt!!
	bra.s	gl_smode

; set standard mode (Format Only)

gl_stdmd
	and.b	#$e7,sv_tmode(a6)
gl_smode
	move.b	sv_tmode(a6),pc_tctrl
  ;;;	or.b	#$80,sv_pcint(a6)	 ; disable microdrive interrupt!!
	rts

	end
